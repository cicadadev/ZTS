package gcp.sps.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CodeService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealgroup;
import gcp.sps.model.SpsDealmember;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsDealsaleproductprice;
import gcp.sps.model.search.SpsDealProductSearch;
import gcp.sps.model.search.SpsDealSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class DealService extends BaseService {

	@Autowired
	private CommonService commonService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private CodeService		codeService;

	@Autowired
	private ProductService	productService;

	private final Log		logger	= LogFactory.getLog(getClass());

	public List<SpsDeal> getDealList(SpsDealSearch dealsearch) throws Exception {
		return (List<SpsDeal>) dao.selectList("sps.deal.getDealList", dealsearch);
	}

	public void updateDeal(SpsDeal spsDeal) throws Exception {

		dao.updateOneTable(spsDeal);
	}

	public List<SpsDealgroup> getOneDepthDealGroupList(SpsDealSearch spsDealSearch) throws Exception {
		return (List<SpsDealgroup>) dao.selectList("sps.deal.getOneDepthDealGroupList", spsDealSearch);
	}

	public List<SpsDealgroup> getTwoDepthDealGroupList(SpsDealSearch spsDealSearch) throws Exception {
		return (List<SpsDealgroup>) dao.selectList("sps.deal.getTwoDepthDealGroupList", spsDealSearch);
	}

	public void saveOneDepthDivTitle(List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		for (SpsDealgroup spsDealgroup : spsDealGroupLsit) {
			spsDealgroup.setStoreId(SessionUtil.getStoreId());
			if ("I".equals(spsDealgroup.getCrudType())) {
				dao.insertOneTable(spsDealgroup);
			} else {
				dao.updateOneTable(spsDealgroup);
			}
		}
	}

	public void saveTwoDepthDivTitle(List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		for (SpsDealgroup spsDealgroup : spsDealGroupLsit) {
			spsDealgroup.setStoreId(SessionUtil.getStoreId());
			if ("I".equals(spsDealgroup.getCrudType())) {
				// 이미지 temp to real
				if (CommonUtil.isNotEmpty(spsDealgroup.getImg())) {
					spsDealgroup.setImg(FileUploadUtil.moveTempToReal(spsDealgroup.getImg()));
				} else {
					spsDealgroup.setImg("");
				}
				dao.insertOneTable(spsDealgroup);
			} else {
				// 이미지 temp to real
				if (CommonUtil.isNotEmpty(spsDealgroup.getImg())) {
					spsDealgroup.setImg(FileUploadUtil.moveTempToReal(spsDealgroup.getImg()));
				} else {
					spsDealgroup.setImg("");
				}
				dao.updateOneTable(spsDealgroup);
			}
		}
	}

	public void delOneDepthDivTitle(List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		for (SpsDealgroup spsDealgroup : spsDealGroupLsit) {
			spsDealgroup.setStoreId(SessionUtil.getStoreId());
			spsDealgroup.setUpperDealGroupNo(spsDealgroup.getDealGroupNo());
			spsDealgroup.setUpdId(SessionUtil.getLoginId());

			dao.update("sps.deal.updateDealproduct", spsDealgroup);
			dao.delete("sps.deal.deleteDealGroup", spsDealgroup);
			dao.deleteOneTable(spsDealgroup);
		}
	}

	public void delTwoDepthDivTitle(List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		for (SpsDealgroup spsDealgroup : spsDealGroupLsit) {
			spsDealgroup.setStoreId(SessionUtil.getStoreId());
			spsDealgroup.setUpdId(SessionUtil.getLoginId());
			dao.update("sps.deal.updateDealproduct", spsDealgroup);
			dao.deleteOneTable(spsDealgroup);
		}
	}

	public List<SpsDealgroup> getDealGroupTree(SpsDealSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<SpsDealgroup>) dao.selectList("sps.deal.getDealGroupTree", search);
	}

	public List<SpsDealproduct> getDealProductList(SpsDealProductSearch spsDealProductSearch) {
		spsDealProductSearch.setStoreId(SessionUtil.getStoreId());
		return (List<SpsDealproduct>) dao.selectList("sps.deal.getDealProductList", spsDealProductSearch);
	}

	public void saveDealProduct(List<SpsDealproduct> spsDealProductList) throws Exception {

		for (SpsDealproduct spsDealproduct : spsDealProductList) {
			//SPS_DEALPRODUCT
			spsDealproduct.setStoreId(SessionUtil.getStoreId());
			spsDealproduct.setInsId(SessionUtil.getLoginId());
			spsDealproduct.setUpdId(SessionUtil.getLoginId());
			if ("I".equals(spsDealproduct.getCrudType())) {

				dao.insertOneTable(spsDealproduct);
//				dao.insert("sps.deal.insertDealProduct", spsDealproduct);
			} else {
				dao.updateOneTable(spsDealproduct);
			}

			// SPS_DEALMEMBER
			// SPS_DEALSALEPRODUCTPRICE

			// SPS_DEALMEMBER
			SpsDealmember dealmember = new SpsDealmember();
			dealmember.setStoreId(SessionUtil.getStoreId());
			dealmember.setDealId(spsDealproduct.getDealId());
			dealmember.setDealProductNo(spsDealproduct.getDealProductNo());
			dao.delete("deleteDealMember", dealmember);

			CcsCodeSearch codeSearchh = new CcsCodeSearch();
			codeSearchh.setCdGroupCd("MEM_GRADE_CD");
			List<CcsCode> memberGradeCdList = codeService.selectCodeList(codeSearchh);

			if (spsDealproduct.getSpsDealmembers() != null) {

				for (CcsCode ccsCode : memberGradeCdList) {
					BigDecimal salePrice = new BigDecimal(0);
					BigDecimal preOpenDays = new BigDecimal(0);
					SpsDealmember spsDealmember = new SpsDealmember();
					for (SpsDealmember spsDealMember : spsDealproduct.getSpsDealmembers()) {
						if (ccsCode.getCd().equals(spsDealMember.getMemGradeCd())) {
							if (spsDealMember.getAddSalePrice() != null) {
								salePrice = spsDealMember.getAddSalePrice();
							}
							if (spsDealMember.getPreOpenDays() != null) {
								preOpenDays = spsDealMember.getPreOpenDays();
							}
						}
					}

					spsDealmember.setStoreId(SessionUtil.getStoreId());
					spsDealmember.setMemGradeCd(ccsCode.getCd());
					spsDealmember.setDealId(spsDealproduct.getDealId());
					spsDealmember.setAddSalePrice(salePrice);
					spsDealmember.setPreOpenDays(preOpenDays);
					spsDealmember.setDealProductNo(spsDealproduct.getDealProductNo());
					dao.insertOneTable(spsDealmember);
				}

			} else {
				for (CcsCode ccsCode : memberGradeCdList) {
					SpsDealmember spsDealmember = new SpsDealmember();
					spsDealmember.setStoreId(SessionUtil.getStoreId());
					spsDealmember.setAddSalePrice(new BigDecimal(0));
					spsDealmember.setDealId(spsDealproduct.getDealId());
					spsDealmember.setDealProductNo(spsDealproduct.getDealProductNo());
					spsDealmember.setMemGradeCd(ccsCode.getCd());
					spsDealmember.setPreOpenDays(new BigDecimal(0));
					dao.insertOneTable(spsDealmember);
				}
			}

			// SPS_DEALSALEPRODUCTPRICE
			SpsDealsaleproductprice dealSaleProductPrice = new SpsDealsaleproductprice();
			dealSaleProductPrice.setStoreId(SessionUtil.getStoreId());
			dealSaleProductPrice.setDealId(spsDealproduct.getDealId());
			dealSaleProductPrice.setDealProductNo(spsDealproduct.getDealProductNo());
			dao.delete("deleteDealSaleProductPrice", dealSaleProductPrice);
			
			PmsProductSearch productSearch = new PmsProductSearch();
			productSearch.setProductId(spsDealproduct.getProductId());
			productSearch.setStoreId(SessionUtil.getStoreId());
			List<PmsSaleproduct> slaeProductList = productService.getSaleProductList(productSearch);

			if (spsDealproduct.getSpsDealsaleproductprices() != null) {
				for (PmsSaleproduct pmsSaleproduct : slaeProductList) {
					BigDecimal salePrice = new BigDecimal(0);
					SpsDealsaleproductprice spsDealsaleproductprice = new SpsDealsaleproductprice();

					for (SpsDealsaleproductprice price : spsDealproduct.getSpsDealsaleproductprices()) {
						if (pmsSaleproduct.getSaleproductId().equals(price.getSaleproductId())) {
							salePrice = price.getAddSalePrice();
						}
					}

					spsDealsaleproductprice.setStoreId(SessionUtil.getStoreId());
					spsDealsaleproductprice.setDealId(spsDealproduct.getDealId());
					spsDealsaleproductprice.setSaleproductId(pmsSaleproduct.getSaleproductId());
					spsDealsaleproductprice.setAddSalePrice(salePrice);
					spsDealsaleproductprice.setDealProductNo(spsDealproduct.getDealProductNo());
					dao.insertOneTable(spsDealsaleproductprice);
				}
			} else {
				for (PmsSaleproduct pmsSaleproduct : slaeProductList) {
					SpsDealsaleproductprice spsDealsaleproductprice = new SpsDealsaleproductprice();
					spsDealsaleproductprice.setStoreId(SessionUtil.getStoreId());
					spsDealsaleproductprice.setDealId(spsDealproduct.getDealId());
					spsDealsaleproductprice.setSaleproductId(pmsSaleproduct.getSaleproductId());
					spsDealsaleproductprice.setAddSalePrice(new BigDecimal(0));
					spsDealsaleproductprice.setDealProductNo(spsDealproduct.getDealProductNo());
					dao.insertOneTable(spsDealsaleproductprice);
				}
			}
		}
	}

	/**
	 * 
	 * @Method Name : deleteDealProduct
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description :
	 *
	 * @param spsDealProductList
	 * @throws Exception
	 */
	public void deleteDealProduct(List<SpsDealproduct> spsDealProductList) throws Exception {

		for (SpsDealproduct spsDealproduct : spsDealProductList) {

			// SPS_DEALSALEPRODUCTPRICE
			SpsDealsaleproductprice spsDealsaleproductprice = new SpsDealsaleproductprice();
			spsDealsaleproductprice.setStoreId(SessionUtil.getStoreId());
			spsDealsaleproductprice.setDealId(spsDealproduct.getDealId());
			spsDealsaleproductprice.setDealProductNo(spsDealproduct.getDealProductNo());
			dao.delete("deleteDealSaleProductPrice", spsDealsaleproductprice);

			// SPS_DEALMEMBER
			SpsDealmember spsDealmember = new SpsDealmember();
			spsDealmember.setStoreId(SessionUtil.getStoreId());
			spsDealmember.setDealId(spsDealproduct.getDealId());
			spsDealmember.setDealProductNo(spsDealproduct.getDealProductNo());
			dao.delete("deleteDealMember", spsDealmember);

			// SPS_DEALPRODUCT
			dao.deleteOneTable(spsDealproduct);
		}
	}
	
	
	public List<SpsDealproduct> getExistsDealProductList(SpsDealProductSearch search) {
		return (List<SpsDealproduct>) dao.selectList("sps.deal.getExistsDealProductList", search);
	}

	/**
	 * 
	 * @Method Name : getShockingzeroMainProduct
	 * @author : stella
	 * @date : 2016. 8. 3.
	 * @description : 프론트 쇼킹제로 메인 상품 조회
	 *
	 * @param SpsDealProductSearch
	 * @throws Exception
	 */
	public List<SpsDealproduct> getShockingzeroMainProduct(SpsDealProductSearch search) throws Exception {
		return (List<SpsDealproduct>) dao.selectList("sps.deal.getShockingzeroMainProduct", search);
	}
	
	/**
	 * 
	 * @Method Name : getShockingzeroProductList
	 * @author : stella
	 * @date : 2016. 8. 3.
	 * @description : 프론트 쇼킹제로 상품 조회
	 *
	 * @param SpsDealProductSearch
	 * @throws Exception
	 */
	public List<SpsDealproduct> getShockingzeroProductList(SpsDealProductSearch search) throws Exception {
		return (List<SpsDealproduct>) dao.selectList("sps.deal.getShockingzeroProductList", search);
	}
	
	public List<String> checkShockingzeroCount(SpsDealProductSearch search) throws Exception {
		return (List<String>) dao.selectList("sps.deal.checkShockingzeroCount", search);
	}

	/**
	 * 
	 * @Method Name : getExistProductCoupon
	 * @author : stella
	 * @date : 2016. 9. 7.
	 * @description : 프론트 쇼킹제로 상품쿠폰 여부 조회
	 *
	 * @param SpsDealProductSearch
	 * @throws Exception
	 */
	public String getExistProductCoupon(SpsDealProductSearch search) throws Exception {
		return (String) dao.selectOne("sps.coupon.getExistProductCoupon", search);
	}

	/**
	 * 
	 * @Method Name : convertMemberTypeToDealType
	 * @author : dennis
	 * @date : 2016. 8. 22.
	 * @description : 회원유형 -> 딜유형.
	 *
	 * @param memberTypeCds
	 * @return
	 */
	public List<String> convertMemberTypeToDealType(List<String> memberTypeCds) {
		List<String> result = new ArrayList<String>();
		for (String memberTypeCd : memberTypeCds) {
			if ("MEMBER_TYPE_CD.MEMBERSHIP".equals(memberTypeCd)) {
				result.add("DEAL_TYPE_CD.MEMBER");
			}
			if ("MEMBER_TYPE_CD.PREMIUM".equals(memberTypeCd)) {
				result.add("DEAL_TYPE_CD.PREMIUM");
			}
			if ("MEMBER_TYPE_CD.EMPLOYEE".equals(memberTypeCd)) {
				result.add("DEAL_TYPE_CD.EMPLOYEE");
			}
			if ("MEMBER_TYPE_CD.CHILDREN".equals(memberTypeCd)) {
				result.add("DEAL_TYPE_CD.CHILDREN");
			}
			if ("MEMBER_TYPE_CD.B2E".equals(memberTypeCd)) {
				result.add("DEAL_TYPE_CD.B2E");
			}
		}
		result.add("DEAL_TYPE_CD.SHOCKDEAL");//쇼킹제로가 추가
		return result;
	}

	/**
	 * 상품에 걸려있는 쇼킹딜 조회
	 * 
	 * @Method Name getProductShockingDeal
	 * @author : eddie
	 * @date : 2016. 8. 25.
	 * @description : 쇼킹딜에 동일상품이 걸려있는경우 최근등록된 상품 기준으로 조회
	 *
	 * @param control
	 * @param dealSearch
	 * @return
	 */
	public SpsDeal getProductShockingDeal(CcsControlSearch control, SpsDealSearch dealSearch) {

		// 상품에 걸려있는 쇼킹딜 조회 
		SpsDeal deal = (SpsDeal) dao.selectOne("sps.deal.getProductShockDeal", dealSearch);
		logger.debug("deal Obj : " + deal);
		if (deal == null) {
			return null;
		}

		// 허용 제어 체크
		control.setControlNo(deal.getControlNo());

		logger.debug("deal controlNo : " + control.getControlNo());
		control.setExceptionFlag(false);

		if (CommonUtil.isNotEmpty(control.getControlNo())) {
			boolean success = commonService.checkControl(control);

			logger.debug("deal controlNo success : " + success);

			if (!success) {
				return null;
			}
		}

		return deal;
	}

	public List<SpsDeal> getApplyDealList(SpsDealSearch spsDealSearch) {

		BigDecimal memberNo = spsDealSearch.getMemberNo();

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);

		String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

		List<String> dealTypeList = convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		spsDealSearch.setMemGradeCd(memGradeCd);
		spsDealSearch.setDealTypeCds(dealTypeCds);

		return (List<SpsDeal>) dao.selectList("sps.deal.getApplyDealList", spsDealSearch);
	}

	public int checkDealProductPeriod(SpsDealProductSearch productSearch) {
		return (int) dao.selectOne("sps.deal.checkDealProductPeriod", productSearch);
	}
	
	/**
	 * @Method Name : getDealDepthList
	 * @author : ian
	 * @date : 2016. 9. 29.
	 * @description : 딜 구분타이틀 뎁스 리스트
	 *
	 * @param storeId
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SpsDealgroup> getDealDepthList(SpsDealSearch spsDealSearch) throws Exception {
		return (List<SpsDealgroup>) dao.selectList("sps.deal.getDealDepthList", spsDealSearch);
	}

	/**
	 * 
	 * @Method Name : getMemberGradeBenefit
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 딜 상품의 등급별 선오픈일 및 추가 할인 조회
	 *
	 * @param spsDealSearch
	 * @return
	 */
	public List<SpsDealmember> getMemberGradeBenefit(SpsDealProductSearch spsDealSearch) {
		return (List<SpsDealmember>) dao.selectList("sps.deal.getMemberGradeBenefit", spsDealSearch);
	}

	/**
	 * 
	 * @Method Name : getSaleProductPrice
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 딜 상품의 단품별 추가 할인 조회
	 *
	 * @param spsDealSearch
	 * @return
	 */
	public List<SpsDealsaleproductprice> getSaleProductPrice(SpsDealProductSearch spsDealSearch) {
		return (List<SpsDealsaleproductprice>) dao.selectList("sps.deal.getSaleProductPrice", spsDealSearch);
	}

	/**
	 * 
	 * @Method Name : getApplyDealOne
	 * @author : dennis
	 * @date : 2016. 10. 18.
	 * @description : 적용 deal 정보 1개 조회
	 *
	 * @param spsDealSearch
	 * @return
	 */
	public SpsDeal getApplyDealOne(SpsDealSearch spsDealSearch) {
		BigDecimal memberNo = spsDealSearch.getMemberNo();

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);

		String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

		List<String> dealTypeList = convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		spsDealSearch.setMemGradeCd(memGradeCd);
		spsDealSearch.setDealTypeCds(dealTypeCds);
		return (SpsDeal) dao.selectOne("sps.deal.getApplyDealOne", spsDealSearch);
	}

}
