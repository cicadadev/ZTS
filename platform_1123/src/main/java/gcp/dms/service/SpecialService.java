package gcp.dms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.utils.SessionUtil;

@Service
public class SpecialService extends BaseService {
	
	@Autowired
	CommonService				commonService;

	private static final Logger logger = LoggerFactory.getLogger(SpecialService.class);

	/**
	 * 
	 * @Method Name : getPickupBrandList
	 * @author : intune
	 * @date : 2016. 9. 30.
	 * @description : 픽업매장 브랜드 리스트
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsBrand> getPickupBrandList(DmsDisplaySearch search) {
		return (List<PmsBrand>) dao.selectList("dms.special.getPickupBrandList", search);
	}

	public List<DmsDisplaycategory> getPickupInfoList(DmsDisplaySearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<DmsDisplaycategory>) dao.selectList("dms.special.getPickupInfoList", search);
	}

	/**
	 * 프론트 메인 매장 픽업관 상품목록
	 * @Method Name : getMainPickupPrdList
	 * @author : intune
	 * @date : 2016. 10. 3.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProduct> getMainPickupPrdList (DmsDisplaySearch search){
		return (List<PmsProduct>) dao.selectList("dms.special.getMainPickupPrdList", search);
	}

	/**
	 * 
	 * @Method Name : getGiftshopCategoryList
	 * @author : intune
	 * @date : 2016. 10. 3.
	 * @description : 기프트샵 코너 상품의 카테고리 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplaycategory> getGiftshopCategoryList(PmsProductSearch search) {
		return (List<DmsDisplaycategory>) dao.selectList("dms.special.getGiftshopCategoryList", search);
	}

	/**
	 * 
	 * @Method Name : getThemeProductList
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description : 테마 상품 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProduct> getThemeProductList(DmsDisplaySearch search) {
		return (List<PmsProduct>) dao.selectList("dms.special.getThemeProductList", search);
	}

	/**
	 * 
	 * @Method Name : getThemeCategoryNproductList
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description : 중카테고리별 테마 상품 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplaycategory> getThemeCategoryNproductList(DmsDisplaySearch search) {
		return (List<DmsDisplaycategory>) dao.selectList("dms.special.getThemeCategoryNproductList", search);
	}

	/**
	 * 
	 * @Method Name : getCornerProductList
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description : 전문관 코너의 상품 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProduct> getGiftShopCornerPrdList(DmsDisplaySearch search) {
		return (List<PmsProduct>) dao.selectList("dms.special.getGiftShopCornerPrdList", search);
	}

	/**
	 * 
	 * @Method Name : getSubscriptionPrdList
	 * @author : intune
	 * @date : 2016. 10. 5.
	 * @description : 정기배송 중카테고리 및 상품 리스트
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplaycategory> getSubscriptionPrdList(DmsDisplaySearch search) {
		return (List<DmsDisplaycategory>) dao.selectList("dms.special.getSubscriptionPrdList", search);
	}
}
