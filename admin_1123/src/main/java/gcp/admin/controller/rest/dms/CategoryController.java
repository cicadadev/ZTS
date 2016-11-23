package gcp.admin.controller.rest.dms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.BoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController("displayCategoryController")
@RequestMapping("api/dms/category")
public class CategoryController {
	
	@Autowired
	private CategoryService displayCategoryService;
	/**
	 * 
	 * @Method Name : getDisplayCategories
	 * @author : stella
	 * @date : 2016. 5. 3.
	 * @description : 전시카테고리 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.POST)
	public List<DmsDisplaycategory> getDisplayCategoryTree(@RequestBody DmsDisplaySearch displaySearch) {
		List<DmsDisplaycategory> displayCategoryList = new ArrayList<>();

		try {
			displaySearch.setStoreId(SessionUtil.getStoreId());
			displaySearch.setLangCd(SessionUtil.getLangCd());

			// 업체는 업체 카테고리만 조회
			String SystemType = BoSessionUtil.getSystemType();
			if ("PO".equals(SystemType)) {
				displaySearch.setCategoryRootId(Config.getString("partner.rootdmscategory"));
			}

			displayCategoryList = displayCategoryService.getDisplayCategory(displaySearch);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return displayCategoryList;
	}

	/**
	 * 
	 * @Method Name : getDisplayCategories
	 * @author : stella
	 * @date : 2016. 5. 3.
	 * @description : 전시카테고리 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<DmsDisplaycategory> getDisplayCategoryList(HttpServletRequest request) throws Exception {
		return displayCategoryService.getDisplayCategoryList();
	}

	/**
	 * 
	 * @Method Name : getDisplayCategoryDetail
	 * @author : stella
	 * @date : 2016. 5. 4.
	 * @description : 전시카테고리_ 기본정보 & 코너 목록 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/corner/list", method = RequestMethod.POST)
	public List<DmsDisplay> getDisplayCategoryDetail(@RequestBody DmsDisplaySearch displaySearch) {
		List<DmsDisplay> displayCategoryDetail = new ArrayList<>();

		try {
			displaySearch.setStoreId(SessionUtil.getStoreId());
			displaySearch.setLangCd(SessionUtil.getLangCd());

			displayCategoryDetail = displayCategoryService.getDisplayCategoryDetail(displaySearch);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return displayCategoryDetail;
	}

	/**
	 * 
	 * @Method Name : createDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 6.
	 * @description : 전시카테고리 등록
	 *
	 * @param displayCateagory
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String createDisplayCategory(@RequestBody DmsDisplaycategory displayCategory) {
		try {
			displayCategory.setStoreId(SessionUtil.getStoreId());
			displayCategoryService.insertDisplayCategory(displayCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return displayCategory.getDisplayCategoryId();
	}

	/**
	 * 
	 * @Method Name : updateDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 전시카테고리 기본정보 수정
	 *
	 * @param displayCategory
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateDisplayCategory(@RequestBody DmsDisplaycategory displayCategory) throws Exception {
		displayCategory.setStoreId(SessionUtil.getStoreId());

		if (CommonUtil.isNotEmpty(displayCategory.getDisplayCategoryId())) {
			displayCategoryService.updateDisplayCategory(displayCategory);
		} else {
			displayCategoryService.insertDisplayCategory(displayCategory);
		}

		return displayCategory.getDisplayCategoryId();
	}

	/**
	 * 
	 * @Method Name : deleteDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 전시카테고리 삭제
	 *
	 * @param displayCategoryId
	 * @throws Exception
	 */
	@RequestMapping(value = "/{displayCategoryId}/delete", method = RequestMethod.POST)
	public BaseEntity deleteDisplayCategory(@PathVariable("displayCategoryId") String displayCategoryId) {
		BaseEntity result = new BaseEntity();

		try {
			DmsDisplaycategory displayCategory = new DmsDisplaycategory();
			displayCategory.setStoreId(SessionUtil.getStoreId());
			displayCategory.setDisplayCategoryId(displayCategoryId);

			displayCategoryService.deleteDisplayCategory(displayCategory);
		} catch (Exception e) {
			result.setResultMessage(e.getMessage());
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 전시 카테고리 매핑 상품 목록 조회
	 * @Method Name : getCategoryCouponList
	 * @author : paul
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param dmsDisplaySearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.POST)
	public List<DmsDisplaycategory> getCategoryCouponList(@RequestBody DmsDisplaySearch dmsDisplaySearch) throws Exception {

		dmsDisplaySearch.setStoreId(SessionUtil.getStoreId());
		dmsDisplaySearch.setLoginId(SessionUtil.getLoginId());
		
		return displayCategoryService.getCategoryCouponList(dmsDisplaySearch);
	}

	/**
	 * @Method Name : checkCategoryCornerItems
	 * @author : stella
	 * @date : 2016. 7. 12.
	 * @description : 전시카테고리 삭제 전, 코너 내 상품 있는지 확인
	 *
	 * @param dmsDisplaySearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/corner/item/check", method = RequestMethod.POST)
	public List<DmsDisplayitem> checkCategoryCornerItems(@RequestBody DmsDisplaySearch dmsDisplaySearch) {
		List<DmsDisplayitem> displayItem = new ArrayList<>();
		
		try {
			dmsDisplaySearch.setStoreId(SessionUtil.getStoreId());
			displayItem = displayCategoryService.getCategoryCornerItems(dmsDisplaySearch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return displayItem;
	}
	
}
