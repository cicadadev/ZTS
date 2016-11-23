package gcp.external.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.PmsSendgoodsmapping;
import gcp.external.model.search.PmsSendgoodsSearch;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductimg;
import gcp.pms.model.PmsProductnotice;
import gcp.pms.model.PmsSaleproduct;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.ByteUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Service
public class SendgoodsService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : getSendgoodsList
	 * @author : peter
	 * @date : 2016. 6. 22.
	 * @description : 상품 목록 조회
	 *
	 * @param pss
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getSendgoodsList(PmsSendgoodsSearch pss) throws ServiceException {
		//상품 번호 검색시 다른 조건 무시
//		if (CommonUtil.isNotEmpty(pss.getProductIds())) {
//			String temp = pss.getProductIds();
//			pss = new PmsSendgoodsSearch();
//			pss.setProductIds(temp);
//		}
		pss.setStoreId(SessionUtil.getStoreId());
		return (List<PmsProduct>) dao.selectList("external.sendgoods.getSendgoodsList", pss);
	}

	/**
	 * @Method Name : insertSendgoods
	 * @author : peter
	 * @date : 2016. 6. 22.
	 * @description : 선택 상품 사방넷 전송
	 *
	 * @param ppList
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public String insertSendgoods(List<PmsProduct> ppList) throws Exception {
		String storeId = SessionUtil.getStoreId();
		PmsSendgoodsSearch pssOrg = new PmsSendgoodsSearch();
		pssOrg.setStoreId(storeId);
		String idArr = "";
		//상품ID
		for (PmsProduct ppOne : ppList) {
			idArr += "'" + ppOne.getProductId() + "',";
		}
		pssOrg.setProductIds(idArr.substring(0, idArr.length() - 1));

		//상품정보 조회
		List<PmsProduct> sendList = (List<PmsProduct>) dao.selectList("external.sendgoods.getProductList", pssOrg);
		int insCnt = 0;
		int skipCnt = 0;
		for (PmsProduct ppOne : sendList) {
			logger.debug("productId: " + ppOne.getProductId());
			logger.debug("productTypeCd: " + ppOne.getProductTypeCd());

			//매핑용 모델 선언
			PmsSendgoodsmapping psgm = null;

			//일반 상품인 경우
			if ("PRODUCT_TYPE_CD.GENERAL".equals(ppOne.getProductTypeCd())) {
				psgm = makeGeneralProduct(storeId, ppOne);
			}
			//세트 상품인 경우
			else if ("PRODUCT_TYPE_CD.SET".equals(ppOne.getProductTypeCd())) {
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("storeId", storeId);
				srchMap.put("productId", ppOne.getProductId());
				List<Map<String, BigDecimal>> optionMap = (List<Map<String, BigDecimal>>) dao
						.selectList("external.sendgoods.getSetproductOptionCount", srchMap);
				//구성상품의 옵션 갯수 체크
				boolean isMassOption = false;
				for (Map<String, BigDecimal> option : optionMap) {
					logger.debug("productId: " + option.get("PRODUCT_ID") + ", count: " + option.get("OPTION_CNT"));
					if (option.get("OPTION_CNT").intValue() > 1) {
						isMassOption = true;
						break;
					}
				}
				//구성상품 중 하나라도 옵션이 여러개 존재하면 skip
				if (isMassOption) {
					logger.info("Skip Product: " + ppOne.getProductId() + ", productTypeCd: " + ppOne.getProductTypeCd());
					skipCnt++;
					continue;
				} else {
					psgm = makeSetProduct(storeId, ppOne);
				}
			}
			//둘 다 아니면 skip
			else {
				logger.info("Skip Product: " + ppOne.getProductId() + ", productTypeCd: " + ppOne.getProductTypeCd());
				skipCnt++;
				continue;
			}

			//사방넷 테이블에 등록: 무조건 delete -> insert
			try {
				dao.delete("external.sendgoods.deleteSendgoods", psgm.getCompaynyGoodsCd());
				dao.insert("external.sendgoods.insertSendgoods", psgm);
				//상세정보(goods_remarks) 저장: CLOB 데이터라 별도로 저장
				dao.update("external.sendgoods.updateGoodsRemarksData", psgm);
				insCnt++;
			} catch (Exception e) {
				String[] param = { "delete & insert zts_term.send_goods",
						"[ " + psgm.getCompaynyGoodsCd() + " ]" + e.getMessage() };
				throw new ServiceException("pms.sql.error", param);
			}

			//등록 완료후 상품테이블에 전송완료 처리: 처음 전송인 경우만 update
			PmsSendgoodsSearch pss = new PmsSendgoodsSearch();
			pss.setStoreId(storeId);
			pss.setProductId(ppOne.getProductId());
			dao.update("external.sendgoods.updateOutSendResult", pss);
		}
		String rtnValue = "총 " + (insCnt + skipCnt) + "건 처리완료[전달-" + insCnt + ", 제외-" + skipCnt + "]\n";

		return rtnValue;
	}

	/**
	 * @Method Name : makeGeneralProduct
	 * @author : peter
	 * @date : 2016. 10. 25.
	 * @description : 사방넷 일반상품 처리
	 *
	 * @param storeId
	 * @param product
	 * @return
	 * @throws Exception
	 */
	private PmsSendgoodsmapping makeGeneralProduct(String storeId, PmsProduct product) throws Exception {
		logger.debug("\n makeGeneralProduct()");
		//공통항목 설정
		PmsSendgoodsmapping sendGoods = makeCommonProductInfo(storeId, product);

		//옵션1(옵션항목)
		String options = "";
		String noOptionSaleproductId = "";
		if ("Y".equals(product.getOptionYn())) { //옵션이 있는 경우
			for (PmsSaleproduct pspOne : product.getPmsSaleproducts()) {
				options += pspOne.getName() + "|" + pspOne.getSaleproductId() + "|^^" + pspOne.getRealStockQty() + "^^"
						+ pspOne.getAddSalePrice() + "^^" + pspOne.getSaleproductId() + "^^EA^^" + pspOne.getSaleproductStateCd()
						+ ",";
			}
		} else { //옵션이 없는 경우
			for (PmsSaleproduct pspOne : product.getPmsSaleproducts()) {
				options += "없음|" + pspOne.getSaleproductId() + "|^^" + pspOne.getRealStockQty() + "^^" + pspOne.getAddSalePrice()
						+ "^^" + pspOne.getSaleproductId() + "^^EA^^" + pspOne.getSaleproductStateCd() + ",";
				//GOODS_NM_PR 에서 사용
				noOptionSaleproductId = pspOne.getSaleproductId();
			}
		}
		sendGoods.setChar1Val(options.substring(0, options.length() - 1));
		//출력상품명
		if ("Y".equals(product.getOptionYn())) { //옵션이 있는 경우
			sendGoods.setGoodsNmPr(null);
		} else {
			sendGoods.setGoodsNmPr(noOptionSaleproductId);
		}
		logger.debug("aaa: " + sendGoods.getChar1Val());

		return sendGoods;
	}

	/**
	 * @Method Name : makeSetProduct
	 * @author : peter
	 * @date : 2016. 10. 25.
	 * @description : 사방넷 세트 상품 처리
	 *
	 * @param storeId
	 * @param product
	 * @return
	 * @throws Exception
	 */
	private PmsSendgoodsmapping makeSetProduct(String storeId, PmsProduct product) throws Exception {
		//세트 구성상품별 판매 가능한 재고수량 계산, 재고는 구성상품재고의 최소값으로 설정
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("productId", product.getProductId());
		int realQty = (int) dao.selectOne("external.sendgoods.getSubproductMinQtyOfSet", srchMap);

		//공통항목 설정
		PmsSendgoodsmapping sendGoods = makeCommonProductInfo(storeId, product);

		//옵션1(옵션항목): 옵션없는 일반상품과 동일하게 처리(구성상품의 단품이 아닌 세트상품의 단품정보를 입력)
		String options = "";
		String noOptionSaleproductId = "";
		for (PmsSaleproduct pspOne : product.getPmsSaleproducts()) {
			options += "없음|" + pspOne.getSaleproductId() + "|^^" + realQty + "^^" + pspOne.getAddSalePrice()
					+ "^^" + pspOne.getSaleproductId() + "^^EA^^" + pspOne.getSaleproductStateCd() + ",";
			//GOODS_NM_PR 에서 사용
			noOptionSaleproductId = pspOne.getSaleproductId();
		}
		sendGoods.setChar1Val(options.substring(0, options.length() - 1));
		//출력상품명
		sendGoods.setGoodsNmPr(noOptionSaleproductId);

		return sendGoods;
	}

	/**
	 * @Method Name : makeCommonProductInfo
	 * @author : peter
	 * @date : 2016. 10. 25.
	 * @description : 사방넷 상품 공통 처리
	 *
	 * @param storeId
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private PmsSendgoodsmapping makeCommonProductInfo(String storeId, PmsProduct product) throws Exception {
		logger.debug("makeCommonProductInfo()");
		//table 조회용 모델
//		PmsSendgoodsSearch search = new PmsSendgoodsSearch();
//		search.setStoreId(storeId);
//		search.setProductId(product.getProductId());
//		search.setCategoryId(product.getCategoryId());

		//저장용 모델
		PmsSendgoodsmapping psgm = new PmsSendgoodsmapping();

		//상품명, 상품약어, 모델명
		StringBuffer goodsNm = new StringBuffer();
		if (null != product.getAdCopy() && !"".equals(product.getAdCopy())) {
			goodsNm.append(product.getAdCopy()).append("_").append(product.getName());
		} else {
			goodsNm.append(product.getName());
		}
		if (null != product.getErpProductId() && !"".equals(product.getErpProductId())) {
			goodsNm.append("_").append(product.getErpProductId());
		}
		psgm.setGoodsNm(goodsNm.toString());
		psgm.setGoodsKeyword(goodsNm.toString());
		psgm.setModelNm(goodsNm.toString());
		//브랜드명
		psgm.setBrandNm(product.getBrandName());
		//자체코드
		psgm.setCompaynyGoodsCd(product.getProductId());
		//사이트검색어
		psgm.setGoodsSearch(product.getKeyword());
		//대분류,중분류,소분류,세분류코드
		List<PmsCategory> pcList = (List<PmsCategory>) dao.selectList("external.sendgoods.getCategoryTree", product);
		String[] classCdArr = new String[4];
		int categoryCnt = pcList.size();
		for (int i = 0; i < 4; i++) {
			if (i < categoryCnt) {
				classCdArr[i] = pcList.get(i).getCategoryId();
			} else {
				classCdArr[i] = null;
			}
		}
		psgm.setClassCd1(classCdArr[0]);
		psgm.setClassCd2(classCdArr[1]);
		psgm.setClassCd3(classCdArr[2]);
		psgm.setClassCd4(classCdArr[3]);
		logger.debug("classCd1: " + psgm.getClassCd1());
		//제조사
		psgm.setMaker(product.getMaker());
		//원산지
		psgm.setOrigin(product.getOrigin());
		//상품상태
		psgm.setStatus(product.getSaleStateCd());
		//세금구분
		psgm.setTaxYn(product.getTaxTypeCd());
		//배송비: 고정값
		psgm.setDelvCost("'0");
		//원가
		psgm.setGoodsCost("'" + product.getSupplyPrice());
		//판매가
		psgm.setGoodsPrice("'" + product.getSalePrice());
		//TAG(소비자가)
		psgm.setGoodsConsumerPrice("'" + product.getListPrice());
		//Image URL setting
		String BASE_IMAGE_PATH = Config.getString("image.domain");
		List<PmsProductimg> ppiList = (List<PmsProductimg>) dao.selectList("external.sendgoods.getProductImage", product);
		String[] imgPathArr = new String[14];
		int imgCnt = ppiList.size();
		for (int i = 0; i < 14; i++) {
			if (i < imgCnt) {
				imgPathArr[i] = ppiList.get(i).getImg();
			} else {
				imgPathArr[i] = null;
			}
		}
		psgm.setImgPath(imgPathArr[0] == null ? null : BASE_IMAGE_PATH + imgPathArr[0]);
		psgm.setImgPath1(imgPathArr[1] == null ? null : BASE_IMAGE_PATH + imgPathArr[1]);
		psgm.setImgPath2(imgPathArr[2] == null ? null : BASE_IMAGE_PATH + imgPathArr[2]);
		psgm.setImgPath3(imgPathArr[3] == null ? null : BASE_IMAGE_PATH + imgPathArr[3]);
		psgm.setImgPath4(imgPathArr[4] == null ? null : BASE_IMAGE_PATH + imgPathArr[4]);
		psgm.setImgPath5(imgPathArr[5] == null ? null : BASE_IMAGE_PATH + imgPathArr[5]);
		psgm.setImgPath6(imgPathArr[6] == null ? null : BASE_IMAGE_PATH + imgPathArr[6]);
		psgm.setImgPath7(imgPathArr[7] == null ? null : BASE_IMAGE_PATH + imgPathArr[7]);
		psgm.setImgPath8(imgPathArr[8] == null ? null : BASE_IMAGE_PATH + imgPathArr[8]);
		psgm.setImgPath9(imgPathArr[9] == null ? null : BASE_IMAGE_PATH + imgPathArr[9]);
		psgm.setImgPath10(imgPathArr[10] == null ? null : BASE_IMAGE_PATH + imgPathArr[10]);
		psgm.setImgPath11(imgPathArr[11] == null ? null : BASE_IMAGE_PATH + imgPathArr[11]);
		psgm.setImgPath12(imgPathArr[12] == null ? null : BASE_IMAGE_PATH + imgPathArr[12]);
		psgm.setImgPath13(imgPathArr[13] == null ? null : BASE_IMAGE_PATH + imgPathArr[13]);
		logger.debug("image0: " + psgm.getImgPath());
		//상품상세설명
		psgm.setGoodsRemarks(product.getDetail());
		//상품 속성분류: 상품고시정보 코드
		String codeValue = product.getProductNoticeTypeCd();
		if ("PRODUCT_NOTICE_TYPE_CD.038".equals(codeValue)) {
			codeValue = "PRODUCT_NOTICE_TYPE_CD.035";
		}
		psgm.setProp1Cd(codeValue.substring(codeValue.lastIndexOf(".") + 1, codeValue.length()));
		//고시정보 설정
		List<PmsProductnotice> ppnList = (List<PmsProductnotice>) dao.selectList("external.sendgoods.getProductNoticeInfo",
				product);
		String[] noticeArr = new String[25];
//		int noticeCnt = ppnList.size();
//		for (int i = 0; i < 24; i++) {
//			if (i < noticeCnt) {
//				noticeArr[i] = ppnList.get(i).getDetail();
//			} else {
//				noticeArr[i] = null;
//			}
//		}
		for (PmsProductnotice notice : ppnList) {
			int sortNo = notice.getSortNo();
			noticeArr[sortNo] = notice.getDetail();
		}
		psgm.setPropVal1(noticeArr[1] == null ? null : noticeArr[1]);
		psgm.setPropVal2(noticeArr[2] == null ? null : noticeArr[2]);
		psgm.setPropVal3(noticeArr[3] == null ? null : noticeArr[3]);
		psgm.setPropVal4(noticeArr[4] == null ? null : noticeArr[4]);
		psgm.setPropVal5(noticeArr[5] == null ? null : noticeArr[5]);
		psgm.setPropVal6(noticeArr[6] == null ? null : noticeArr[6]);
		psgm.setPropVal7(noticeArr[7] == null ? null : noticeArr[7]);
		psgm.setPropVal8(noticeArr[8] == null ? null : noticeArr[8]);
		psgm.setPropVal9(noticeArr[9] == null ? null : noticeArr[9]);
		psgm.setPropVal10(noticeArr[10] == null ? null : noticeArr[10]);
		psgm.setPropVal11(noticeArr[11] == null ? null : noticeArr[11]);
		psgm.setPropVal12(noticeArr[12] == null ? null : noticeArr[12]);
		psgm.setPropVal13(noticeArr[13] == null ? null : noticeArr[13]);
		psgm.setPropVal14(noticeArr[14] == null ? null : noticeArr[14]);
		psgm.setPropVal15(noticeArr[15] == null ? null : noticeArr[15]);
		psgm.setPropVal16(noticeArr[16] == null ? null : noticeArr[16]);
		psgm.setPropVal17(noticeArr[17] == null ? null : noticeArr[17]);
		psgm.setPropVal18(noticeArr[18] == null ? null : noticeArr[18]);
		psgm.setPropVal19(noticeArr[19] == null ? null : noticeArr[19]);
		psgm.setPropVal20(noticeArr[20] == null ? null : noticeArr[20]);
		psgm.setPropVal21(noticeArr[21] == null ? null : noticeArr[21]);
		psgm.setPropVal22(noticeArr[22] == null ? null : noticeArr[22]);
		psgm.setPropVal23(noticeArr[23] == null ? null : noticeArr[23]);
		psgm.setPropVal24(noticeArr[24] == null ? null : noticeArr[24]);
		logger.debug("property1: " + psgm.getPropVal1());
//		//출력상품명
//		if ("Y".equals(product.getOptionYn())) { //옵션이 있는 경우
//			psgm.setGoodsNmPr(null);
//		} else {
//			psgm.setGoodsNmPr(noOptionSaleproductId);
//		}
		//추가상품상세설명2: 반품,교환,환불정보
		String goodsRemarks3 = product.getClaimInfo();
		logger.debug("char_size: " + goodsRemarks3.length() + ", byte_size: " + goodsRemarks3.getBytes().length
				+ ", 3byte_length: " + goodsRemarks3.getBytes("UTF-8").length);
		if (goodsRemarks3.getBytes().length > 3500) {
			goodsRemarks3 = ByteUtil.getMaxByteString(goodsRemarks3, 3495);
		}
		psgm.setGoodsRemarks3(goodsRemarks3);

		return psgm;
	}
}
