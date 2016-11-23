package gcp.frontpc.controller.view.dms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;

@Controller("displayCategoryViewController")
@RequestMapping("dms/category")
public class CategoryController {
	private final Log		logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 프론트 전시 카테고리 네비게이션
	 * 
	 * @Method Name : getCategryList
	 * @author : emily
	 * @date : 2016. 7. 19.
	 * @description :
	 *
	 * @param dispCategoryId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/navi",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getCategryList(@RequestParam(value="categoryId", required = false, defaultValue = "") String dispCategoryId
			, HttpServletRequest request, DmsDisplaySearch search) throws Exception {
		//ModelAndView model = new ModelAndView("/dms/include/navi_category");
		ModelAndView model = new ModelAndView();
		
		/**************************************************************************
		 * 카테고리 전체 목록
		 **************************************************************************/
		DmsDisplaySearch ctgrSearch = new DmsDisplaySearch();
		ctgrSearch.setStoreId(BaseConstants.STORE_ID);	
		ctgrSearch.setRootCategoryId(Config.getString("root.display.category.id"));
		List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(ctgrSearch);
		model.addObject("categoryList", categoryList);
		
		/**************************************************************************
		 * parameter
		 **************************************************************************/
		List<DmsDisplaycategory> currentCategory = new ArrayList<DmsDisplaycategory>();
		String displayCategoryId ="";
		
		/**************************************************************************
		 * 대카의 change 이벤트와 현재페이지의  네비게이션 기능
		 **************************************************************************/
		if(CommonUtil.equals(BaseConstants.YN_Y, search.getLinkType())){
			//대카 change 경우 카테고리 변경
			model.setViewName("/dms/include/navi_category_inner");
			displayCategoryId = CommonUtil.replaceNull(search.getDisplayCategoryId(), "");
			
			/**************************************************************************
			 * 대카테고리의 하위카테고리 목록 조회
			 **************************************************************************/
			if (CommonUtil.isNotEmpty(displayCategoryId)) {
				ctgrSearch.setDisplayCategoryId(displayCategoryId);
				currentCategory = categoryService.getDepth2CategoryList(ctgrSearch);
				
				if(currentCategory != null && currentCategory.size()> 0 ){
					DmsDisplaycategory category = currentCategory.get(0);
					if(category.getDmsDisplaycategorys()!= null && category.getDmsDisplaycategorys().size()> 0 ){
						category.setDmsDisplaycategory(category.getDmsDisplaycategorys().get(0)); //2depth
						
						DmsDisplaycategory search1 = new DmsDisplaycategory();
						search1.setStoreId(BaseConstants.STORE_ID);	
						search1.setUpperDisplayCategoryId(category.getDmsDisplaycategory().getDisplayCategoryId());
						List<DmsDisplaycategory> depthCtg = categoryService.getLowDisplaycategory(search1);
						
						if(depthCtg != null && depthCtg.size() > 0){
							category.getDmsDisplaycategory().setDmsDisplaycategory(depthCtg.get(0)); //3depth
						}
					}
				}
			}
			
		}else{
			
			model.setViewName("/dms/include/navi_category");
			displayCategoryId = CommonUtil.replaceNull(dispCategoryId, "");
			
			/**************************************************************************
			 * 현재카테고리 정보
			 **************************************************************************/
			if (CommonUtil.isNotEmpty(displayCategoryId)) {
				ctgrSearch.setDisplayCategoryId(displayCategoryId);
				currentCategory = categoryService.getAllCategoryList(ctgrSearch);
			}
		}

		if (currentCategory != null && currentCategory.size() > 0) {
			model.addObject("currentCategory", currentCategory.get(0));
		}
		model.addObject("search", search);
		return model;
	}
}
