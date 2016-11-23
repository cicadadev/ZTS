package gcp.dms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsDisplaycategoryproduct;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.search.DmsDisplaySearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("displayCategoryService")
public class CategoryService extends BaseService {

	private final Log		logger	= LogFactory.getLog(getClass());
	
	/**
	 * 
	 * @Method Name : getDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 3.
	 * @description : 전시카테고리 조회
	 *
	 * @param displaySearch
	 * @return
	 */
	public List<DmsDisplaycategory> getDisplayCategory(DmsDisplaySearch displaySearch) throws Exception {

		return (List<DmsDisplaycategory>) dao.selectList("dms.category.getDisplayCategoryAll", displaySearch);
	}

	/**
	 * 
	 * @Method Name : getDisplayCategoryList
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 전시카테고리 리스트 조회
	 *
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplaycategory> getDisplayCategoryList() throws Exception {
		return (List<DmsDisplaycategory>) dao.selectList("dms.category.getDisplayCategoryList", "");
	}

	/**
	 * 
	 * @Method Name : getDisplayCategoryDetail
	 * @author : stella
	 * @date : 2016. 5. 4.
	 * @description : 전시카테고리 관리_ 기본정보 & 코너 목록 조회
	 *
	 * @param displaySearch
	 * @return
	 */
	public List<DmsDisplay> getDisplayCategoryDetail(DmsDisplaySearch displaySearch) throws Exception {
		return (List<DmsDisplay>) dao.selectList("dms.corner.getDisplayCategoryCorners", displaySearch);
	}

	/**
	 * 
	 * @Method Name : insertDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 11.
	 * @description : 신규 전시카테고리 등록
	 *
	 * @param displayCategory
	 * @return
	 */
	public void insertDisplayCategory(DmsDisplaycategory displayCategory) throws Exception {
		dao.insertOneTable(displayCategory);
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
	public Integer updateDisplayCategory(DmsDisplaycategory displayCategory) throws Exception {
		if (CommonUtil.isEmpty(displayCategory.getTemplateId())) {
			displayCategory.setTemplateId("");// template 빈값일때 삭제처리
		}
		return dao.updateOneTable(displayCategory);
	}

	/**
	 * 
	 * @Method Name : deleteDisplayCategory
	 * @author : stella
	 * @date : 2016. 5. 17.
	 * @description : 전시카테고리 삭제
	 *
	 * @param displayCategoryId
	 * @return
	 * @throws Exception
	 */
	public int deleteDisplayCategory(DmsDisplaycategory displayCategory) throws Exception {
		
		
		displayCategory.setStoreId(SessionUtil.getStoreId());

		// 삭제전 체크
		// 1. 하위 카테고리 존재 여부
		if (!CommonUtil.isEmpty(dao.selectOne("dms.category.getChildCategory", displayCategory))) {
			throw new ServiceException("pms.category.delete.child");
		}

		// 2. 매핑 상품 존재 여부
		if (!CommonUtil.isEmpty(dao.selectOne("dms.category.getDisplaycategoryProductMap", displayCategory))) {
			throw new ServiceException("pms.category.delete.content");
		}
		
		
		return dao.deleteOneTable(displayCategory);
	}
	
	public List<DmsDisplaycategory> getCategoryCouponList(DmsDisplaySearch dmsDisplaySearch){
		return (List<DmsDisplaycategory>)dao.selectList("dms.category.getCategoryCouponList", dmsDisplaySearch);
	}
	
	/**
	 * 
	 * @Method Name : getCategoryCornerItems
	 * @author : stella
	 * @date : 2016. 7. 12.
	 * @description : 전시카테고리 삭제 전, 코너 내 상품 있는지 확인
	 *
	 * @param dmsDisplaySearch
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplayitem> getCategoryCornerItems(DmsDisplaySearch dmsDisplaySearch) throws Exception {
		List<DmsDisplayitem> displayItems = new ArrayList<>();
		
		List<DmsDisplay> displayList = getDisplayCategoryDetail(dmsDisplaySearch);
		for (DmsDisplay display : displayList) {
			DmsDisplaySearch search = new DmsDisplaySearch();
			
			search.setStoreId(SessionUtil.getStoreId());
			search.setDisplayId(display.getDisplayId());
			search.setDisplayItemDivId(dmsDisplaySearch.getDisplayItemDivId());
			
			DmsDisplayitem displayItem = new DmsDisplayitem();
			displayItem = (DmsDisplayitem) dao.selectOne("dms.corner.checkCornerItemForCategory", search);
			if (CommonUtil.isNotEmpty(displayItem)) {
				displayItems.add(displayItem);
			}
		}
		
		return displayItems;
	}
	
	/**
	 * 프론트 depth 카테고리 목록
	 * @Method Name : getAllCategoryList
	 * @author : emily
	 * @date : 2016. 7. 5.
	 * @description : 모든 카테고리 목록 트리구조로 가져옴.
	 *		프론트 전시 네비게이션의  카테고리 목록
	 * @param dmsDisplaySearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Cacheable(cacheName = "getAllCategoryList-cache")
	public List<DmsDisplaycategory> getAllCategoryList(DmsDisplaySearch dmsDisplaySearch) {
		return (List<DmsDisplaycategory>) dao.selectList("dms.category.getDepthAllCategoryList", dmsDisplaySearch);
	}
	
	/**
	 * 프론트 Depth 카테고리 목록 조회
	 * 
	 * @Method Name : getDepth2CategoryList
	 * @author : emily
	 * @date : 2016. 7. 26.
	 * @description : 공통 Header의 전체 카테고리, template의 현제카테고리와 하위카테고리 목록 조회 depth2까지만 가져온다.
	 * @param dmsDisplaySearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Cacheable(cacheName = "getDepth2CategoryList-cache")
	public List<DmsDisplaycategory> getDepth2CategoryList(DmsDisplaySearch dmsDisplaySearch){
		return (List<DmsDisplaycategory>)dao.selectList("dms.category.getDepth2CategoryList", dmsDisplaySearch);
		
	}
	
	/**
	 * 프론트  전체카테고리 목록 
	 * @Method Name : getDepthCategoryList
	 * @author : emily
	 * @date : 2016. 7. 20.
	 * @description : 전체 카테고리 트리구조를 객체로 반환 한다.
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplaycategory> getDepthCategoryList(DmsDisplaySearch search, List<DmsDisplaycategory> ctgList){
		
		List<DmsDisplaycategory> result = new ArrayList<DmsDisplaycategory>();
		List<DmsDisplaycategory> dep1 = new ArrayList<DmsDisplaycategory>();
		List<DmsDisplaycategory> dep2 = new ArrayList<DmsDisplaycategory>();
		List<DmsDisplaycategory> dep3 = new ArrayList<DmsDisplaycategory>();
		//List<DmsDisplayitem> itemList = new ArrayList<DmsDisplayitem>();
		
		if(ctgList == null || ctgList.size() == 0){
			search.setRootCategoryId(search.getRootCategoryId());
			ctgList = getAllCategoryList(search);
		}
		
		if(ctgList != null && ctgList.size() > 0){
			for (DmsDisplaycategory dms : ctgList) {
				
				if(Integer.parseInt(dms.getDepth()) == 1){
					dep1.add(dms);
				}else if(Integer.parseInt(dms.getDepth()) == 2){
					dep2.add(dms);
				}else if(Integer.parseInt(dms.getDepth()) == 3){
					dep3.add(dms);
				}
			}
			
			/****************************************************
			 * depth별로 객체에 담기
			 ****************************************************/
			for (DmsDisplaycategory info1 : dep1) {
				
				if(dep2 != null && dep2.size() >0 ){
					List<DmsDisplaycategory> de2 = new ArrayList<DmsDisplaycategory>();	
					for (DmsDisplaycategory info2 : dep2) {
						
						if (CommonUtil.equals(info1.getDisplayCategoryId(), info2.getUpperDisplayCategoryId())) {
							
							if(dep3 != null && dep3.size() > 0){
								List<DmsDisplaycategory> de3 = new ArrayList<DmsDisplaycategory>();
								for (DmsDisplaycategory info3 : dep3) {
									
									if (CommonUtil.equals(info2.getDisplayCategoryId(),info3.getUpperDisplayCategoryId())) {
										de3.add(info3);
									}
								}
								Collections.sort(de3, new NoAscCompare());
								info2.setDmsDisplaycategorys(de3); //
								info2.setTotalCount(de3.size());//3depth카테고리카운트
							}
							de2.add(info2);							
						}
					}
					Collections.sort(de2, new NoAscCompare());
					info1.setDmsDisplaycategorys(de2);
					info1.setTotalCount(de2.size());
					//하위카테고리 모바일 페이징
					if(CommonUtil.equals("Y", search.getMobilePageYn())){
						int maxPage = de2.size()/search.getPageSize();
						if (de2.size() % search.getPageSize() != 0) {
							maxPage++;
						}
						info1.setPageSize(maxPage);
					}
				}
				result.add(info1);
				Collections.sort(result, new NoAscCompare());
			}
		}
		
		return result;
	}
	
	/**
	 * 카테고리 정렬 INNER Class
	 * @Pagckage Name : gcp.dms.service
	 * @FileName : CategoryService.java
	 * @author : emily
	 * @date : 2016. 9. 23.
	 * @description :
	 */
	static class NoAscCompare implements Comparator<DmsDisplaycategory> {
	  
	   public int compare(DmsDisplaycategory arg0, DmsDisplaycategory arg1) {
	       
	       return arg0.getSortNo().compareTo(arg1.getSortNo());
	   }
	}
	
	/**
	 * FO 전시카테고리 상품 조회
	 * 
	 * @Method Name : getCategoryProduct
	 * @author : stella
	 * @date : 2016. 10. 19.
	 * @description : 브랜드관 전시카테고리 상품 조회(Y볼루션)
	 *
	 * @param categoryProduct
	 * @return
	 */
	public List<DmsDisplaycategoryproduct> getCategoryProduct(DmsDisplaySearch search) throws Exception {
		return (List<DmsDisplaycategoryproduct>) dao.selectList("dms.category.getDisplayCategoryproduct", search);
	}
	
	/**
	 * 단일 카테고리 정보 조회
	 * @Method Name : getCurrentCategory
	 * @author : emily
	 * @date : 2016. 10. 28.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public DmsDisplaycategory getCurrentCategory(DmsDisplaycategory search)  throws Exception {
		return (DmsDisplaycategory) dao.selectOneTable(search);
	}
	
	/**
	 * 프론트 추천 카테고리 목록 조회
	 * @Method Name : getRecommendCategory
	 * @author : emily
	 * @date : 2016. 11. 1.
	 * @description : 
	 *	검색 키워드로 추천 카테고리 목록을 조회한다.
	 * @param search
	 * @return
	 */
	public List<DmsDisplaycategory> getRecommendCategory (DmsDisplaySearch search){
		return (List<DmsDisplaycategory>) dao.selectList("dms.category.getRecommendCategory", search);
	}
	
	/**
	 * 하위 카테고리 정보 조회 
	 * @Method Name : getUpperDisplaycategory
	 * @author : intune
	 * @date : 2016. 11. 3.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplaycategory> getLowDisplaycategory(DmsDisplaycategory search)  throws Exception {
		return ( List<DmsDisplaycategory>) dao.selectList("dms.category.getLowDisplaycategory",search);
	}
}
