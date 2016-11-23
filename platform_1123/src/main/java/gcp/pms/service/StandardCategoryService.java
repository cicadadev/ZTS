package gcp.pms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsCategoryattribute;
import gcp.pms.model.PmsCategoryrating;
import gcp.pms.model.search.PmsCategorySearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.pms.service
 * @FileName : StandardCategoryService.java
 * @author : dennis
 * @date : 2016. 4. 20.
 * @description : Standard Category Service
 */
@Service
public class StandardCategoryService extends BaseService {
	
	

	/**
	 * 
	 * @Method Name : getCategory
	 * @author : stella
	 * @date : 2016. 5. 9.
	 * @description : 표준카테고리 조회
	 *
	 * @param categorySearch
	 * @return
	 */
	public List<PmsCategory> getCategory(PmsCategorySearch categorySearch) throws Exception {
		return (List<PmsCategory>) dao.selectList("pms.category.getCategoryAll", categorySearch);
	}
	
	/**
	 * 표준 카테고리 상세 정보 조회
	 * 
	 * @Method Name : getCategoryDetail
	 * @author : stella
	 * @date : 2016. 6. 22.
	 * @description : 1. pms_category 조회 2. pms_categoryattribute 조회 3. pms_categoryrating 조회
	 *
	 * @param categorySearch
	 * @return
	 * @throws Exception
	 */
	public PmsCategory getCategoryDetail(PmsCategorySearch categorySearch) throws Exception {

		PmsCategory category = new PmsCategory();
		category.setStoreId(categorySearch.getStoreId());
		category.setCategoryId(categorySearch.getCategoryId());

		// pms_category
		category = (PmsCategory) dao.selectOneTable(category);

		//pms_categoryattribute
		category.setPmsCategoryattributes(getCategoryAttribute(categorySearch));

		//pms_categoryrating
		category.setPmsCategoryratings(getCategoryRatingList(categorySearch));

		return category;
	}

	/**
	 * 
	 * @Method Name : getCategoryAttribute
	 * @author : stella
	 * @date : 2016. 5. 18.
	 * @description : 표준카테고리 속성 & 속성값 목록 조회
	 *
	 * @param categorySearch
	 * @return
	 * @throws Exception
	 */
	public List<PmsCategoryattribute> getCategoryAttribute(PmsCategorySearch categorySearch) throws Exception {
		return (List<PmsCategoryattribute>) dao.selectList("pms.category.getCategoryAttributes", categorySearch);
	}

	/**
	 * 
	 * @Method Name : insertCategory
	 * @author : stella
	 * @date : 2016. 5. 11.
	 * @description : 신규 표준카테고리 등록
	 *
	 * @param category
	 * @return
	 */
	public void insertCategory(PmsCategory category) throws Exception {

		if ("N".equals(category.getLeafYn())) {
			category.setPointSaveRate(new BigDecimal(0));
		}
		if (CommonUtil.isEmpty(category.getSortNo())) {
			category.setSortNo(new BigDecimal("9999"));
		}

		// pmsCategory 등록
		dao.insertOneTable(category);

		// 표준카테고리 별점 항목 등록
		if ("Y".equals(category.getLeafYn()) && category.getPmsCategoryratings() != null
				&& category.getPmsCategoryratings().size() > 0) {
			updateCategoryRating(category.getCategoryId(), category.getPmsCategoryratings());
		}

		// 카테고리 속성 정보 등록
		if ("Y".equals(category.getLeafYn()) && category.getPmsCategoryattributes() != null
				&& category.getPmsCategoryattributes().size() > 0) {
			updateCategoryAttributes(category.getCategoryId(), category.getPmsCategoryattributes());
		}
	}


	/**
	 * 
	 * @Method Name : updateCategoryAttributes
	 * @author : stella
	 * @date : 2016. 6. 13.
	 * @description : 표준카테고리 속성 등록/수정
	 *
	 * @param categoryAttribute
	 * @return
	 * @throws Exception
	 */
	public void updateCategoryAttributes(String categoryId, List<PmsCategoryattribute> categoryAttributes) throws Exception {
		String storeId = SessionUtil.getStoreId();

		PmsCategory category = new PmsCategory();
		category.setCategoryId(categoryId);
		category.setStoreId(storeId);

		dao.delete("pms.category.deleteCategoryAttribute", category);

		if (categoryAttributes != null) {
			for (PmsCategoryattribute categoryAttribute : categoryAttributes) {
				categoryAttribute.setStoreId(storeId);
				categoryAttribute.setUseYn("Y");
				categoryAttribute.setCategoryId(categoryId);
				dao.insertOneTable(categoryAttribute);
			}
		}
	}

	/**
	 * 
	 * @Method Name : updateCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 표준카테고리 기본정보 수정
	 *
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void updateCategory(PmsCategory category) throws Exception {
		// 카테고리 정보 수정
		if (CommonUtil.isEmpty(category.getSortNo())) {
			category.setSortNo(new BigDecimal("9999"));
		}
		dao.updateOneTable(category);

		// 카테고리 별점 항목 수정
		updateCategoryRating(category.getCategoryId(), category.getPmsCategoryratings());

		// 카테고리 속성 정보 수정
		updateCategoryAttributes(category.getCategoryId(), category.getPmsCategoryattributes());

	}

	/**
	 * 
	 * @Method Name : updateCatgoryRatingName
	 * @author : stella
	 * @date : 2016. 7. 7.
	 * @description : 표준카테고리 별점항목명 수정
	 *
	 * @param categoryRatings
	 * @return
	 * @throws Exception
	 */
	public void updateCategoryRating(String categoryId, List<PmsCategoryrating> categoryRatings) throws Exception {

		if (categoryRatings != null) {
			for (PmsCategoryrating categoryRating : categoryRatings) {
				categoryRating.setStoreId(SessionUtil.getStoreId());
				categoryRating.setCategoryId(categoryId);

				if (CommonUtil.isEmpty(categoryRating.getRatingId())) {
					dao.insertOneTable(categoryRating);
				} else {
					dao.updateOneTable(categoryRating);
				}

			}
		}
	}

	/**
	 * 카테고리 별점 목록 조회
	 * 
	 * @Method Name : getCategoryRatingList
	 * @author : intune
	 * @date : 2016. 7. 18.
	 * @description :
	 *
	 * @param search
	 */
	public List<PmsCategoryrating> getCategoryRatingList(PmsCategorySearch search) {
		return (List<PmsCategoryrating>) dao.selectList("pms.category.getCategoryRatingList", search);
	}

	/**
	 * 
	 * @Method Name : deleteCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 표준카테고리 삭제
	 *
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void deleteCategory(PmsCategory category) throws Exception {

		category.setStoreId(SessionUtil.getStoreId());

		// 삭제전 체크 
		// 1. 하위 카테고리 존재 여부
		if (!CommonUtil.isEmpty(dao.selectOne("pms.category.getChildCategory", category))) {
			throw new ServiceException("pms.category.delete.child");
		}
		// 2. 매핑 상품 존재 여부
		if (!CommonUtil.isEmpty(dao.selectOne("pms.category.getCategoryMapProduct", category))) {
			throw new ServiceException("pms.category.delete.content");
		}

		/*
		 * 1. 카테고리 별점 항목 삭제
		 * 2. 카테고리 속성 삭제
		 * 3. 카테고리 삭제
		 */
		dao.delete("pms.category.deleteCategoryAttribute", category);
		dao.delete("pms.category.deleteCategoryRating", category);
		dao.deleteOneTable(category);

	}
	
	/**
	 * 
	 * @Method Name : deleteCategoryRating
	 * @author : stella
	 * @date : 2016. 6. 23.
	 * @description : 표준카테고리 별점 항목 삭제
	 *
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void deleteCategoryRating(List<PmsCategoryrating> categoryRatingList) throws Exception {		
		String storeId = categoryRatingList.get(0).getStoreId();
		
		for (PmsCategoryrating rating : categoryRatingList) {
			rating.setStoreId(storeId);
			rating.setUseYn("N");
			
			dao.update("pms.category.updateCategoryRating", rating);			
		}

	}
	
	/**
	 * 
	 * @Method Name : deleteCategoryAttribute
	 * @author : stella
	 * @date : 2016. 6. 23.
	 * @description : 표준카테고리 속성 삭제
	 *
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void deleteCategoryAttribute(List<PmsCategoryattribute> categoryAttributes) throws Exception {		
		String storeId = categoryAttributes.get(0).getStoreId();
		String categoryId = categoryAttributes.get(0).getCategoryId();
		
		for (PmsCategoryattribute categoryAttribute : categoryAttributes) {
			categoryAttribute.setStoreId(storeId);
			categoryAttribute.setCategoryId(categoryId);
			categoryAttribute.setUseYn("N");
			
			dao.update("pms.category.updateCategoryAttribute" ,categoryAttribute);
		}

	}

}
