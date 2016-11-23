package gcp.frontpc.controller.view.dms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CornerService;
import gcp.dms.service.ExhibitService;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("cornerViewController")
@RequestMapping("dms/corner")
public class CornerController {
	
	private final Log		logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private CornerService	cornerService;
	
	@Autowired
	private EventService	eventService;
	
	@Autowired
	private ExhibitService	 exhibitService;

	/**
	 * 코너의  상품목록 아이템조회
	 * @Method Name : getSearchProductList
	 * @author : emily
	 * @date : 2016. 9. 20.
	 * @description : 
	 *				1. 코너의 카테고리별 상품목록 조회
	 *				2. 검색엔진외의 카테고리의 상품목록 조회
	 *				전시카테고리ID 필수값
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productList",method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getCornerProductList( DmsDisplaySearch search , HttpServletRequest request) throws Exception{
		
		ModelAndView model = new ModelAndView("/dms/include/cornerProductList");
		
		String displayId = CommonUtil.replaceNull(search.getDisplayId(),request.getParameter("displayId"));
		search.setDisplayId(displayId);
		
		if(CommonUtil.isNotEmpty(search.getSort())){
			if(search.getSort().indexOf(".") < 0){
				search.setSort("PRODUCT_SORT_CD."+search.getSort());
			}
		}
		
//		if(CommonUtil.isEmpty(search.getCurrentPage())){
//			search.setCornerCurrentPage(1);
//		}
//		
//		if(CommonUtil.isEmpty(search.getPageSize())){
//			search.setCornerPageSize(40);
//		}
		
		if(CommonUtil.isNotEmpty(search.getPageSize()) && CommonUtil.isNotEmpty(search.getCurrentPage())){
			search.setCornerFirstRow(((search.getCurrentPage()-1)*search.getPageSize())+1);
			search.setCornerLastRow(search.getCurrentPage()*search.getPageSize());
		}
		
		try{
			//search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
			List<DmsDisplay> conerList = cornerService.getCommonCornerList(search);
			List<PmsProduct> result = new ArrayList<PmsProduct>();
			
			if(conerList != null && conerList.size() > 0){
				List<DmsDisplayitem> list = conerList.get(0).getDmsDisplayitems();
				
				if(list != null && list.size() > 0){
					//상품일경우
					if(CommonUtil.equals(BaseConstants.DISPLAY_ITEM_TYPE_CD_PRODUCT, conerList.get(0).getDisplayItemTypeCd())){
						for (DmsDisplayitem disp : list) {
							result.add(disp.getPmsProduct());
						}
					}else{
						//카테고리 상품 목록일경우
						result = list.get(0).getPmsProductList();
					}
					search.setTotalCount(list.get(0).getTotalCount());
				}
			
				//search.setTotalCount(result.size());
				model.addObject("prdList", result);
				model.addObject("csearch", search);
			}
		
		}catch(Exception e){
			//logger.error(e);
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * 카테고리의 코너 아이템 조회
	 * @Method Name : getCategoryCorner
	 * @author : emily
	 * @date : 2016. 10. 7.
	 * @description : 
	 *		1. 분유관 카테고리의 코너 아이템을 가져온다
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/categoryCornerItems")
	public ModelAndView getCategoryCorner(DmsDisplaySearch search, HttpServletRequest request)throws Exception{
		ModelAndView model = new ModelAndView(search.getViewName());
		
		search.setDisplayItemDivId(search.getDisplayCategoryId());
		search.setDisplayId(search.getDisplayId());
		List<DmsDisplay> corner = cornerService.getCommonCornerList(search);
		Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
		for (DmsDisplay dmsDisplay : corner) {
			returnMap.put(dmsDisplay.getDisplayId(), dmsDisplay);			
		}
		
		model.addObject("cornerMap",returnMap);
		return model;
	}
	
	/**
	 * 브랜드관 템플릿 event & exhibit 조회
	 * 
	 * @Method Name : getBrandCornerList
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 
	 * 				BRAND ID 필수
	 * 				이벤트 목록, 기획전 목록 조회하여 한 페이지에 뿌리기 위해 cornerController에서 조회함
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brand/event/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getBrandCornerList(@RequestBody DmsDisplaySearch search , HttpServletRequest request) throws Exception{
		ModelAndView model = new ModelAndView("/dms/template/inner/template" + search.getTemplateTypeCd() + "/brandEvent");
		
		/**********************************************************************************
		 * 리스트 조회 - EVENT
		 **********************************************************************************/
		SpsEventSearch eventSearch = new SpsEventSearch();
		eventSearch.setStoreId(SessionUtil.getStoreId());
		eventSearch.setBrandId(search.getBrandId());
		eventSearch.setPagingYn("N");
		
		List<SpsEvent> eventList = eventService.getBrandEventInfo(eventSearch);
		model.addObject("eventList", eventList);
		
		/**********************************************************************************
		 * 리스트 조회 - EXHIBIT
		 **********************************************************************************/
		DmsExhibitSearch exhibitSearch = new DmsExhibitSearch();
		exhibitSearch.setStoreId(SessionUtil.getStoreId());
		exhibitSearch.setBrandId(search.getBrandId());
		exhibitSearch.setPagingYn("N");
		
		List<DmsExhibit> exhibitList = exhibitService.getBrandExhibitInfo(exhibitSearch);
		model.addObject("exhibitList", exhibitList);
		
		return model;		
	}
	
	/**
	 * 코너의 아이템 반환
	 * @Method Name : getCornerItem
	 * @author : intune
	 * @date : 2016. 11. 15.
	 * @description : 
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cornerItem/view")
	public ModelAndView getCornerItem(DmsDisplaySearch search, HttpServletRequest request)throws Exception{
		ModelAndView model = new ModelAndView("/dms/display/inner/corner"+search.getDisplayId());
		
		search.setStoreId(SessionUtil.getStoreId());
		search.setDisplayId(search.getDisplayId());
		List<DmsDisplay> corner = cornerService.getCommonCornerList(search);
		
		if(corner != null && corner.size() > 0){
			DmsDisplay hcorner = corner.get(0);
			if(hcorner.getDmsDisplayitems() != null && hcorner.getDmsDisplayitems().size() > 0){
				model.addObject("items",hcorner.getDmsDisplayitems().get(0));
				
				/*Map<String, List<DmsDisplayitem>> returnMap = new HashMap<String, List<DmsDisplayitem>>();
				returnMap.put(hcorner.getDisplayId(), hcorner.getDmsDisplayitems());			
				model.addObject("cornerMap",returnMap);*/
			}
		}
		return model;
	}
}
