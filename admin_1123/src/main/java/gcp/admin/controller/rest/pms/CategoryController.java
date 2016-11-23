package gcp.admin.controller.rest.pms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.BoSessionUtil;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.search.PmsCategorySearch;
import gcp.pms.service.StandardCategoryService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController("productCategoryController")
@RequestMapping("api/pms/category")
public class CategoryController {

	@Autowired
	private StandardCategoryService standardCategoryService;

	/**
	 * 
	 * @Method Name : getCategoryTree
	 * @author : stella
	 * @date : 2016. 5. 9.
	 * @description : 표준카테고리 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.POST)
	public List<PmsCategory> getCategoryTree(HttpServletRequest request, @RequestBody PmsCategorySearch categorySearch) {
		List<PmsCategory> categoryList = new ArrayList<>();

		try {
			Object obj = SessionUtil.getLoginInfo(request);
			if (obj != null) {
				categorySearch.setStoreId(SessionUtil.getStoreId());
			} else {
				categorySearch.setStoreId(Config.getString("common.storeid"));
			}

			// 업체는 업체 카테고리만 조회
			String SystemType =BoSessionUtil.getSystemType();
			if ("PO".equals(SystemType)) {
				categorySearch.setCategoryRootId(Config.getString("partner.rootcategory"));
			}

			categoryList = standardCategoryService.getCategory(categorySearch);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return categoryList;
	}

	@RequestMapping(value = "/popup/search", method = RequestMethod.POST)
	public List<PmsCategory> businessListPopup(@RequestBody PmsCategorySearch categorySearch) throws Exception {
		categorySearch.setStoreId(SessionUtil.getStoreId());
		List<PmsCategory> categoryList = standardCategoryService.getCategory(categorySearch);
		return categoryList;
	}
	
	/**
	 * 
	 * @Method Name : getCategoryDetail
	 * @author : stella
	 * @date : 2016. 6. 22.
	 * @description : 표준카테고리 상세 조회
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public PmsCategory getCategoryDetail(@RequestBody PmsCategorySearch categorySearch) throws Exception {

		categorySearch.setStoreId(SessionUtil.getStoreId());
		PmsCategory pmsCategory = (PmsCategory) standardCategoryService.getCategoryDetail(categorySearch);

		return pmsCategory;
	}

//	/**
//	 * 
//	 * @Method Name : getCategoryAttribute
//	 * @author : stella
//	 * @date : 2016. 5. 18.
//	 * @description : 표준 카테고리 기본정보 & 속성,속성값 목록 조회
//	 *
//	 * @param categorySearch
//	 * @return
//	 */
//	@RequestMapping(value = "/attribute/list", method = RequestMethod.POST)
//	public List<PmsCategoryattribute> getCategoryAttribute(@RequestBody PmsCategorySearch categorySearch) {
//		List<PmsCategoryattribute> categoryAttributeList = new ArrayList<>();
//
//		try {
//			categorySearch.setStoreId(SessionUtil.getStoreId());
//			categoryAttributeList = standardCategoryService.getCategoryAttribute(categorySearch);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return categoryAttributeList;
//	}

//	/**
//	 * 
//	 * @Method Name : createCategory
//	 * @author : stella
//	 * @date : 2016. 5. 11.
//	 * @description : 표준카테고리 등록
//	 *
//	 * @param category
//	 * @return
//	 */
//	@RequestMapping(value = "/insert", method = RequestMethod.POST)
//	public String insertCategory(@RequestBody PmsCategory category) {
//		try {
//			category.setStoreId(SessionUtil.getStoreId());
//			standardCategoryService.insertCategory(category);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//
//		return category.getCategoryId();
//	}

//	/**
//	 * 
//	 * @Method Name : insertCategoryAttribute
//	 * @author : stella
//	 * @date : 2016. 6. 13.
//	 * @description : 표준카테고리에 속성 등록(매핑)
//	 *
//	 * @param categoryAttributes
//	 * @return
//	 */
//	@RequestMapping(value = "/attribute/update", method = RequestMethod.POST)
//	public String insertCategoryAttribute(@RequestBody List<PmsCategoryattribute> categoryAttributes) {
//		try {
//			standardCategoryService.updateCategoryAttributes(categoryAttributes.get(0).getCategoryId(), categoryAttributes);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//		
//		return Integer.toString(categoryAttributes.size());
//	}

	/**
	 * 
	 * @Method Name : updateCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 표준카테고리 기본정보 등록/수정
	 *
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCategory(@RequestBody PmsCategory category) {
		
		try {
			category.setStoreId(SessionUtil.getStoreId());

			if (CommonUtil.isNotEmpty(category.getCategoryId())) {
				standardCategoryService.updateCategory(category);
			} else {
				standardCategoryService.insertCategory(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

		return category.getCategoryId();
	}
	
//	/**
//	 * 
//	 * @Method Name : updateCategoryRatingName
//	 * @author : stella
//	 * @date : 2016. 7. 7.
//	 * @description : 표준카테고리 별점항목명 수정
//	 *
//	 * @param categoryRatings
//	 * @return
//	 */
//	@RequestMapping(value = "/rating/save", method = RequestMethod.POST)
//	public String updateCatgoryRating(@RequestBody List<PmsCategoryrating> categoryRatings) {
//		try {
//			standardCategoryService.updateCategoryRating(categoryRatings.get(0).getCategoryId(), categoryRatings);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//
//		return Integer.toString(categoryRatings.size());
//	}

//	/**
//	 * 
//	 * @Method Name : getCatgoryRatingList
//	 * @author : eddie
//	 * @date : 2016. 7. 18.
//	 * @description :
//	 *
//	 * @param categoryRatings
//	 * @return
//	 */
//	@RequestMapping(value = "/rating/list", method = RequestMethod.POST)
//	public List<PmsCategoryrating> getCatgoryRatingList(@RequestBody PmsCategorySearch search) {
//		return standardCategoryService.getCategoryRatingList(search);
//	}

	/**
	 * 
	 * @Method Name : deleteCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 표준카테고리 삭제
	 *
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public BaseEntity deleteCategory(@RequestBody PmsCategory category) {
		BaseEntity result = new BaseEntity();
		try {
			category.setStoreId(SessionUtil.getStoreId());
			standardCategoryService.deleteCategory(category);
		} catch (Exception e) {
			result.setResultMessage(e.getMessage());
			result.setSuccess(false);
		}

		return result;
	}
	
}
