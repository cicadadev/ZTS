package gcp.dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CodeService;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsTemplate;
import gcp.dms.model.DmsTemplateDisplay;
import gcp.dms.model.base.BaseDmsTemplate;
import gcp.dms.model.base.BaseDmsTemplateDisplay;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.BrandSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Service("displayCommonService")
public class DisplayCommonService extends BaseService {

	private final Log	logger	= LogFactory.getLog(getClass());
	

	@Autowired
	private CornerService	cornerService;

	@Autowired
	private CategoryService	displayCategoryService;

	@Autowired
	private CodeService		codeService;

	/**
	 * 
	 * @Method Name : getTemplateListByType
	 * @author : stella
	 * @date : 2016. 5. 3.
	 * @description : 템플릿 타입별 조회
	 *
	 * @param displaySearch
	 * @return
	 */
	public List<BaseDmsTemplate> getTemplateListByType(DmsDisplaySearch displaySearch) throws Exception {
		return (List<BaseDmsTemplate>) dao.selectList("dms.common.getTemplateListByType", displaySearch);
	}

	
	/**
	 * 템플릿 Id 로 전시 코너 목록 조회
	 * @Method Name : getTemplateCornerMapList
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<BaseDmsTemplateDisplay> getTemplateCornerMapList(DmsDisplaySearch search) {
		return (List<BaseDmsTemplateDisplay>) dao.selectList("dms.common.getTemplateCornerMapList", search);
	}
	
	/**
	 * 템플릿-코너 매핑 정보 저장
	 * 
	 * @Method Name : saveTemplateCornerMap
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param list
	 * @throws Exception
	 */
	public void saveTemplateCornerMap(@RequestBody List<BaseDmsTemplateDisplay> list) throws Exception {
		for (BaseDmsTemplateDisplay td : list) {
			td.setStoreId(SessionUtil.getStoreId());
			dao.insertOneTable(td);
		}
	}

	/**
	 * 템플릿-코너 매핑 정보 삭제
	 * 
	 * @Method Name : deleteTemplateCornerMap
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param list
	 * @throws Exception
	 */
	public void deleteTemplateCornerMap(@RequestBody List<BaseDmsTemplateDisplay> list) throws Exception {
		for (BaseDmsTemplateDisplay td : list) {
			td.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(td);
		}
	}
	
	/**
	 * 프론트 전시 템플릿 조회
	 * 
	 * @Method Name : getCategoryTemplatePage
	 * @author : emily
	 * @date : 2016. 6. 29.
	 * @description : 템플릿 유형에 맛게 Template의 객체에 담아서 return
	 * 				템플릿, 전시(코너), 전시대상(코너아이템) 정보는 공통으로 가져옴.

	 * 필수 parameter : templateId, storeId, templateTypeCd
	 * @param search
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public DmsTemplate getTemplateInfo(DmsDisplaySearch search) throws Exception{
		
		if(CommonUtil.isEmpty(search.getTemplateId())){
			throw new ServiceException("dms.template.empty");
		}
		
		/*************************************************************************
		 * 템플릿의 코너목록 조회
		 *************************************************************************/
		DmsTemplate template = (DmsTemplate) dao.selectOne("dms.common.getTemplateCornerList", search);
		
		if(template == null){
			throw new ServiceException("dms.corner.empty");
		}else{ 
			
			/*************************************************************************
			 * 코너유형에 따라서 아이템목록 조회 //
			 *************************************************************************/
			for (DmsTemplateDisplay templateDisplay : template.getDmsTemplateDisplays()) {
				String itemDivId = "";
				if (CommonUtil.isNotEmpty(search.getDisplayCategoryId())) {
					itemDivId = search.getDisplayCategoryId();
				} else if (CommonUtil.isNotEmpty(search.getDisplayItemId())) {
					itemDivId = search.getDisplayItemId();
				}
				
				search.setDisplayItemDivId(itemDivId);

				cornerService.setDisplayCornerItems(templateDisplay.getDmsDisplay(), search);
				
			}

		}
		
		return template;
	}

	/**
	 * 프론트 브랜드 목록 조회
	 * @Method Name : getAllBrendList
	 * @author : emily
	 * @date : 2016. 7. 26.
	 * @description : 
	 *	템플릿 정보가 있는 브랜드 목록
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsBrand> getAllBrendList() {
		return (List<PmsBrand>) dao.selectList("dms.common.getAllBrendList", null);
	}

	/**
	 * 프론트 모바일 left 브랜드검색_초성,알파벳조회
	 * @Method Name : getBrandSearchList
	 * @author : roy
	 * @date : 2016. 9. 9.
	 * @description : 
	 *
	 * @param getBrandSearchList
	 * @return List<PmsBrand>
	 */
	@SuppressWarnings("unchecked")
	public List<PmsBrand> getBrandSearchList(BrandSearch search) {
		return (List<PmsBrand>) dao.selectList("dms.common.getBrandSearchList", search);
	}
	
	/**
	 * @Method Name : getLatestProductList
	 * @author : ian
	 * @date : 2016. 9. 20.
	 * @description : 최근 본 상품
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getLatestProductList(MmsMemberSearch search, String type) {

		String xml ="";
		if("MY_PAGE".equals(type)) {
			xml = "mms.mypage.getLatestProductListMypage";
		} else if("SKY_SCRAPER".equals(type)) {
			xml = "dms.common.getLatestProductList";
		}
		
		List<PmsProduct> sortList = new ArrayList<PmsProduct>();
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList(xml, search);
		
		String memCd = CommonUtil.isNotEmpty(search.getMemGradeCd()) ? search.getMemGradeCd() : "";
		productList = setPricetable(productList, memCd);

		// 쿠키 등록순으로 sorting
		String[] id = search.getProductIds().split(",");
		for(int i=0; i<id.length; i++) {
			String changeId = id[i].replaceAll("\\'", "");
			for(int j=0; j<productList.size(); j++) {
				if(changeId.equals(productList.get(j).getProductId())) {
					sortList.add(productList.get(j));
				}
			}
		}
		
		return sortList;
	}
	
	/**
	 * @Method Name : getMoHistoryProduct
	 * @author : ian
	 * @date : 2016. 9. 22.
	 * @description : 모바일히스토리 상품
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PmsProduct getMoHistoryProduct(MmsMemberSearch search) {
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("dms.common.getLatestProductList", search);
		
		String memCd = CommonUtil.isNotEmpty(search.getMemGradeCd()) ? search.getMemGradeCd() : "";
		productList = setPricetable(productList, memCd);
		
		return productList.get(0);
	}
	
	/**
	 * @Method Name : getWishList
	 * @author : ian
	 * @date : 2016. 9. 20.
	 * @description : 찜 상품
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getWishList(MmsMemberSearch search) {
		
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("dms.common.getWishList", search);
		
		String memCd = CommonUtil.isNotEmpty(search.getMemGradeCd()) ? search.getMemGradeCd() : "";
		productList = setPricetable(productList, memCd);
		
		return productList;
	}
	
	/**
	 * @Method Name : setPricetable
	 * @author : ian
	 * @date : 2016. 9. 20.
	 * @description : 가격 & 아이콘 정보
	 *
	 * @param product
	 * @param memCd
	 * @return
	 */
	private List<PmsProduct> setPricetable(List<PmsProduct> productList, String memCd) {
		for(PmsProduct product : productList) {
			switch (memCd) {
				case BaseConstants.MEMBER_GRADE_PRESTAGE : 
					product.setSalePrice(product.getPmsProductprice().getPrestigeSalePrice());
					product.setPointSaveYn(product.getPmsProductprice().getPrestigePointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getPrestigeCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getPrestigeDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_VIP :
					product.setSalePrice(product.getPmsProductprice().getVipSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getVipPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getVipCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getVipDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_GOLD :
					product.setSalePrice(product.getPmsProductprice().getGoldSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getGoldPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getGoldCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getGoldDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_SILVER :
					product.setSalePrice(product.getPmsProductprice().getSilverSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getSilverPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getSilverCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getSilverDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_FAMILY :
					product.setSalePrice(product.getPmsProductprice().getFamilySalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getFamilyPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getFamilyCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getFamilyDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_WELCOME :
					product.setSalePrice(product.getPmsProductprice().getWelcomeSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getWelcomePointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getWelcomeCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getWelcomeDeliveryFeeFreeYn());
					break;
				default  :
					product.setSalePrice(product.getPmsProductprice().getSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getDeliveryFeeFreeYn());
					break;
			}

		}
		return productList;
	}

	/**
	 * GNB 영역 조회
	 * 
	 * @Method Name : getGnbInfo
	 * @author : intune
	 * @date : 2016. 11. 5.
	 * @description :
	 *
	 * @return
	 */
	@Cacheable(cacheName = "getGnbInfo-cache")
	public Map<String, Object> getGnbInfo() {

		//JSONObject returnVal = new JSONObject();
		Map<String, Object> returnVal = new HashMap<String, Object>();

		/*****************************************************************************
		 * 전체 카테고리 목록 조회
		 *****************************************************************************/
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		List<DmsDisplaycategory> categoryList = displayCategoryService.getAllCategoryList(search);
		search.setMobilePageYn("Y");
		search.setPageSize(3);
		List<DmsDisplaycategory> ctgList = displayCategoryService.getDepthCategoryList(search, categoryList);
		returnVal.put("ctgList", ctgList);

		/*****************************************************************************
		 * 월령 코드 목록 조회 - 상품의 월령코드 목록만 조회함.
		 *****************************************************************************/
		CcsCodeSearch codeSearch = new CcsCodeSearch();
		codeSearch.setCdGroupCd("AGE_TYPE_CD");
		CcsCodegroup ageCode = codeService.getFrontCodeList(codeSearch);
		returnVal.put("ageCodeList", ageCode.getCcsCodes());

		return returnVal;
	}
	
	@Cacheable(cacheName = "getGnbCorner-cache")
	public Map<String, DmsDisplay> getGnbCorner(DmsDisplaySearch search1){

		Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
		
		List<DmsDisplay> ctgConerList = cornerService.getChildCornerList(search1);

		if(ctgConerList != null && ctgConerList.size() > 0){
			for (DmsDisplay ctgdisp : ctgConerList) {
				//logger.info("#### 전시카테고리 코너:"+ctgdisp);
				returnMap.put(ctgdisp.getDisplayId(), ctgdisp);
			}
		}
		return returnMap;
	}
}
