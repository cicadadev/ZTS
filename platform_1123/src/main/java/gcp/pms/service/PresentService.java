package gcp.pms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.common.util.PmsUtil;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductimg;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.search.SpsPresentSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("presentService")
public class PresentService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private CommonService	commonService;

	@Autowired
	private MemberService	memberService;

	@SuppressWarnings("unchecked")
	public List<PmsProduct> getPresentList(PmsProductSearch search) {
		// 상품 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(search.getProductIds())) {
			String temp = search.getProductIds();
			search = new PmsProductSearch();
			search.setProductIds(temp);
		}
		search.setStoreId(SessionUtil.getStoreId());

		return (List<PmsProduct>) dao.selectList("pms.present.getPresentList", search);
	}

	public PmsProduct getPresentDetail(PmsProductSearch search) {
		return (PmsProduct) dao.selectOne("pms.present.getPresentDetail", search);
	}

	/**
	 * 사은품 상품 등록
	 * @Method Name : insertPresent
	 * @author : eddie
	 * @date : 2016. 6. 16.
	 * @description : 
	 *
	 * @param present
	 * @throws Exception 
	 */
	public String insertPresent(PmsProduct present) throws Exception {
		// 기본값 세팅
		setDefaultPresent(present);
		
		// 1. pms_product 등록
		dao.insertOneTable(present);
//		dao.insert("pms.product.insertProduct", present);
		
		present.getPmsSaleproduct().setProductId(present.getProductId());

		// 2. pms_saleproduct 등록
		// 바코드가 없으면 등록안함.
		dao.insertOneTable(present.getPmsSaleproduct());
//		if(CommonUtil.isNotEmpty(present.getPmsSaleproduct().getErpSaleproductId())) {
//		}

		// 3. pms_productimg 등록
		if (present.getPmsProductimg() != null) {
			present.getPmsProductimg().setProductId(present.getProductId());
			savePresentImage(present.getPmsProductimg());
		}

		return present.getProductId();
	}

	/**
	 * 사은품 수정
	 * @Method Name : updatePresent
	 * @author : eddie
	 * @date : 2016. 6. 16.
	 * @description : 
	 *
	 * @param present
	 * @throws Exception
	 */
	public void updatePresent(PmsProduct present) throws Exception{
		logger.debug(" \t update present");
		// 기본값 세팅
		setDefaultPresent(present);
		
		// pms_product 수정
		dao.updateOneTable(present);
		
		present.getPmsSaleproduct().setProductId(present.getProductId());

		// pms_saleproduct 수정
		dao.updateOneTable(present.getPmsSaleproduct());

		// pms_productimg 수정
		if (present.getPmsProductimg() != null) {
			present.getPmsProductimg().setProductId(present.getProductId());
			savePresentImage(present.getPmsProductimg());
		}
	}

	public void savePresentImage(PmsProductimg image) throws Exception {

		String productId = image.getProductId();
		String orgImg = CommonUtil.isEmpty(image.getOrgPath()) ? "not" : image.getOrgPath();

		/*
		 * orgImg
		 *  insert : 없음
		 *  update : 1. 기존 -> path 같음
		 *  		 2. 수정 -> path 다름
		 *  delete : 없음
		 * */

		if (!orgImg.equals(image.getImg())) {
			if (!BaseConstants.CRUD_TYPE_CREATE.equals(image.getCrudType())) {
				// target : Delete, UpDate
				// DB 삭제
				dao.deleteOneTable(image);
				
				//파일 삭제
				try {
					FileUploadUtil.deleteFile(image.getImg());
					
					// 수정시 real 삭제
					if (BaseConstants.CRUD_TYPE_UPDATE.equals(image.getCrudType())) {
						FileUploadUtil.deleteFile(image.getOrgPath());
					}
				} catch (ServiceException e) {
					logger.error("@@ deleteFileError : " + e.getMessage());
				}

			}

			if (!BaseConstants.CRUD_TYPE_DELETE.equals(image.getCrudType())) {
				PmsProductSearch search = new PmsProductSearch();
				search.setProductId(productId);
				search.setStoreId(SessionUtil.getStoreId());
				
				int imgNo = 0;
				String newFileName = productId + "_" + imgNo;
				
//				String oldFilePath = image.getImg();
//				// 파일명 변경 : 정의된 파일 형식으로 변경
//				String newFilePath = FileUploadUtil.changeFileName(oldFilePath, newFileName)
//						.replace(FileUploadUtil.SEPARATOR + FileUploadUtil.TEMP_PATH, "");

				String tempFilePath = image.getImg();

				// 임시 파일경로를 저장할 경로로 변경
				String newFilePath = PmsUtil.getImagePathDb() + FileUploadUtil.SEPARATOR + productId + FileUploadUtil.SEPARATOR
						+ newFileName + "." + FileUploadUtil.getFileExtension(tempFilePath);

				
				image.setImg(newFilePath);
				image.setStoreId(SessionUtil.getStoreId());
				image.setImgNo(BigDecimal.valueOf(imgNo));
				dao.deleteOneTable(image);
				dao.insertOneTable(image);
				
				// temp -> real 로 이동
				try {
					FileUploadUtil.moveFile(tempFilePath, newFilePath);
				} catch (ServiceException e) {
					logger.error("@@ moveFileError : " + e.getMessage());
				}
			}
		}
		//TODO 이미지 리사이징

	}

	private void setDefaultPresent(PmsProduct present){
		// 기본값 세팅
		present.setStoreId(SessionUtil.getStoreId());
		present.setSaleStateCd("SALE_STATE_CD.SALE");//판매중
		present.setDisplayYn("Y");
		present.setMinQty(BigDecimal.valueOf(0));
//		present.setTaxTypeCd("");//확인
		present.setPointSaveRate(BigDecimal.valueOf(0));
		present.setOptionYn("N");
		present.setOffshopPickupYn("N");
		present.setRegularDeliveryYn("N");
		present.setFixedDeliveryYn("N");
		present.setWrapYn("N");
//		present.setStockControlTypeCd("");//확인
//		present.setUseYn("Y");
		present.setProductTypeCd("PRODUCT_TYPE_CD.PRESENT");//상품유형
		present.setUnitQty(new BigDecimal("1"));
		present.setRegularDeliveryPrice(new BigDecimal("0"));
		present.setRegularDeliveryMaxQty(new BigDecimal("0"));
		present.setRegularDeliveryMinCnt(new BigDecimal("0"));
		present.setRegularDeliveryYn("N");
		
		present.setListPrice(new BigDecimal("0"));
		present.setSalePrice(new BigDecimal("0"));
		present.setSupplyPrice(new BigDecimal("0"));
		present.setDeliveryFeeFreeYn("N");
		present.setReserveYn("N");
		present.setGiftYn("N");
		present.setWrapVolume(new BigDecimal("0"));
		present.setCommissionRate(new BigDecimal("0"));
		present.setTextOptionYn("N");
		present.setOverseasPurchaseYn("N");
		present.setBoxDeliveryYn("N");
		present.setBusinessId(Config.getString("zeroto7.business.id"));//자사업체 고정
		
		present.setOutSendYn("N");	// 사방넷 전송 여부

		// TODO 사은품 등록시 필요한지?
		present.setDeliveryPolicyNo(new BigDecimal("1"));	// 배송정책번호

		if (present.getPmsSaleproduct() != null) {
			PmsSaleproduct pms = present.getPmsSaleproduct();
			pms.setStoreId(SessionUtil.getStoreId());
			pms.setProductId(present.getProductId());
			pms.setName("기본단품");
			pms.setSaleproductStateCd("SALEPRODUCT_STATE_CD.SALE");
			pms.setAddSalePrice(new BigDecimal("0"));
//			pms.setRealStockQty(new BigDecimal("0"));
			pms.setSafeStockQty(new BigDecimal("0"));
		}
	}

	private String getImagePath() {
		return Config.getString("upload.root") + Config.getString("upload.image.dir") + FileUploadUtil.SEPARATOR + "pms/present";
	}

	/**
	 * 
	 * @Method Name : getProductPresent
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 상품 사은품 조회
	 *
	 * @param productId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsPresent> getApplyPresentList(SpsPresentSearch spsPresentSearch) {
		return (List<SpsPresent>) dao.selectList("sps.present.getApplyPresentList", spsPresentSearch);
	}

	/**
	 * 상품에 걸린 사은품 조회
	 * 
	 * @Method Name : getPresentPromotion
	 * @author : eddie
	 * @date : 2016. 10. 13.
	 * @description :
	 *
	 * @param spsPresentSearch
	 * @return
	 */
	public List<SpsPresent> getPresentPromotion(SpsPresentSearch spsPresentSearch) {

		List<SpsPresent> productPresentList = new ArrayList<SpsPresent>();
		BigDecimal memberNo = SessionUtil.getMemberNo();
		String channelId = SessionUtil.getChannelId();
		String storeId = SessionUtil.getStoreId();
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd();
		String gradeCd = FoSessionUtil.getMemGradeCd();
		List<String> memberTypeCds = new ArrayList<String>();
		memberService.getMemberTypeInfo(memberTypeCds);

		//사은품 조회
		List<SpsPresent> presentList = getApplyPresentList(spsPresentSearch);

		if (presentList != null) {

			//상품사은품 목록.

			for (SpsPresent spsPresent : presentList) {

				boolean resultControl = false;
				BigDecimal controlNo = spsPresent.getControlNo();
				if (CommonUtil.isNotEmpty(controlNo)) {
					//제어 check
					CcsControlSearch search = new CcsControlSearch();
					search.setStoreId(storeId);
					search.setControlNo(controlNo);
					search.setChannelId(channelId);
					search.setMemberNo(memberNo);
					search.setDeviceTypeCd(deviceTypeCd);
					search.setMemberTypeCds(memberTypeCds);
					search.setMemGradeCd(gradeCd);
					search.setControlType("PRESENT");
					search.setExceptionFlag(false);
//					search.setApplyObjectName("사은품," + spsPresent.getName());
					resultControl = commonService.checkControl(search);
				}

				if (CommonUtil.isEmpty(controlNo) || resultControl) {

					//상품사은품 목록 담음.
					productPresentList.add(spsPresent);
				}

			}
		}

		return productPresentList;
	}
}

