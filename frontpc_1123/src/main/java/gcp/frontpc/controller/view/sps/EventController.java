package gcp.frontpc.controller.view.sps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@Controller("eventViewController")
@RequestMapping("sps/event")
public class EventController extends BaseController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private EventService	eventService;

	@RequestMapping(value="/main")
	public ModelAndView getMain(HttpServletRequest request) throws Exception{
		
		ModelAndView model = new ModelAndView();
		boolean isMoblie = FoSessionUtil.isMobile(request);
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setPagingYn("N");

		boolean isApp = FoSessionUtil.isApp(request);
		if (isApp) {
			search.setControlDeviceTypeCd("DEVICE_TYPE_CD.APP");
		} else if (isMoblie) {
			search.setControlDeviceTypeCd("DEVICE_TYPE_CD.MW");
		} else {
			search.setControlDeviceTypeCd("DEVICE_TYPE_CD.PC");
		}

		List<SpsEvent> eventList = eventService.getRunAllEventList(search);
		model.addObject("eventList", eventList);
		
		List<SpsEvent> expEventList = eventService.getExpEventList(search);
		model.addObject("expEventList", expEventList);
		
		if(isMoblie){
			if(CommonUtil.equals("mainCon", mobileType)){
				model.setViewName("/sps/event/inner/eventMain.mainCon");
			}else{
				model.setViewName(CommonUtil.makeJspUrl(request));
			}
		} else {
			model.setViewName(CommonUtil.makeJspUrl(request));
		}
		
		return model;
	}
	
	/**
	 * 이벤트 상세
	 * 
	 * @Method Name : getEventDetail
	 * @author : stella
	 * @date : 2016. 10. 20.
	 * @description : 
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/detail")
	public ModelAndView getEventDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		
		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setEventId(request.getParameter("eventId"));
		
		SpsEvent event = eventService.getEventDetail(search);
		model.addObject("event", event);
		
//		이벤트 유형 분기
		if (BaseConstants.EVENT_TYPE_CD_COUPON.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventCoupon");
		} else if (BaseConstants.EVENT_TYPE_CD_ATTEND.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventAttend");
		} else if (BaseConstants.EVENT_TYPE_CD_JOIN.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventJoin");
		} else if (BaseConstants.EVENT_TYPE_CD_INFO.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventInfo");
		} else if (BaseConstants.EVENT_TYPE_CD_EXP.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventDetail");
		} else if (BaseConstants.EVENT_TYPE_CD_MANUAL.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventManual");
		} else if (BaseConstants.EVENT_TYPE_CD_REPLY.equals(event.getEventTypeCd())) {
			model.setViewName("/sps/event/eventReply");
		}
		
		if(FoSessionUtil.isMobile(request)) {
			String temp = "EVENT," + event.getEventId();
			CookieUtil.createCookieString(request, response, "moHistory", temp, CookieUtil.SECONDS_OF_10YEAR, new BigDecimal(3650));
		}
		
		return model;
	}

	/**
	 * 회원 혜택
	 * 
	 * @Method Name : getEventBenefit
	 * @author : intune
	 * @date : 2016. 10. 22.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/benefit")
	public ModelAndView getEventBenefit(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();

		String viewName = "";
		if (FoSessionUtil.isMemberLogin()) {
			if (FoSessionUtil.isMobile(request)) {
				viewName = "/sps/event/eventBenefit2_mo";
			} else {
				viewName = "/sps/event/eventBenefit2";
			}
		} else {

			viewName = "/sps/event/eventBenefit1";

		}
		model.setViewName(viewName);
		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(FoSessionUtil.getMemberNo());
		Map<String, Object> result = eventService.getNextGradeAmt(search);

		model.addObject("pGradeCd", (String) result.get("pGradeCd"));
		model.addObject("myGradeCd", (String) FoSessionUtil.getMemGradeCd());
		model.addObject("nextGradeCd", (String) result.get("nextGrade"));
		model.addObject("remainAmt", (Long) result.get("remainAmt"));
		model.addObject("totalAmt", (Long) result.get("totalAmt"));
		return model;
	}

	@RequestMapping(value = "/card")
	public ModelAndView getCard(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/sps/event/card" + request.getParameter("cno"));

		model.addObject("cno", request.getParameter("cno"));
		return model;
	}
	
	/**
	 * 브랜드관 템플릿 event 조회(모바일용)
	 * 
	 * @Method Name : getBrandCornerList
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 
	 * 				BRAND ID 필수
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brand/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getBrandCornerList(@RequestBody DmsDisplaySearch search , HttpServletRequest request) throws Exception{
		ModelAndView model = new ModelAndView("/dms/template/inner/template" + search.getTemplateTypeCd() + "/brandEvent_mo");
		
		/**********************************************************************************
		 * 리스트 조회 - EVENT
		 **********************************************************************************/
		SpsEventSearch eventSearch = new SpsEventSearch();
		eventSearch.setStoreId(SessionUtil.getStoreId());
		eventSearch.setBrandId(search.getBrandId());
		eventSearch.setPagingYn("N");
		
		List<SpsEvent> eventList = eventService.getBrandEventInfo(eventSearch);
		model.addObject("eventList", eventList);
		
		return model;
	}

	/**
	 * 모바일 앱
	 * 
	 * @Method Name : getEventMobileApp
	 * @author : intune
	 * @date : 2016. 11. 03.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobileApp")
	public ModelAndView getEventMobileApp(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();

		String viewName = "";
		if (FoSessionUtil.isMobile(request)) {
			viewName = "/sps/event/eventMobileApp_mo";
		} else {
			viewName = "/sps/event/eventMobileApp";
		}

		model.setViewName(viewName);
		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());

		return model;
	}
	
	/**
	 * 생생이벤트 상세
	 * 
	 * @Method Name : getVividityDetail
	 * @author : stella
	 * @date : 2016. 10. 20.
	 * @description : 
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/vividity/detail")
	public ModelAndView getVividityDetail(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView("/sps/event/eventVividityDetail");

		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setEventId(request.getParameter("eventId"));
		
		SpsEvent expEvent = eventService.getExpEventList(search).get(0);
		//expEvent.setSpsEventjoins(eventService.getExpEventDetail(search));		
		
		model.addObject("expEvent", expEvent);
		
		// sns 공유용 메타데이터 세팅
		String imgPath = "";
		if (FoSessionUtil.isMobile()) {
			imgPath = expEvent.getImg2();
		} else {
			imgPath = expEvent.getImg1();
		}

		model.addObject("ogTagTitle", expEvent.getName());
		model.addObject("ogTagImage", Config.getString("image.domain") + imgPath);
		model.addObject("ogTagUrl",
				Config.getString("front.domain.url") + "/sps/event/vividity/detail?eventId=" + expEvent.getEventId());

		return model;
	}
	
	/**
	 * 생생이벤트 상세
	 * 
	 * @Method Name : getVividityDetail
	 * @author : stella
	 * @date : 2016. 10. 20.
	 * @description : 
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/vividity/join/ajax")
	public ModelAndView getVividityJoinList(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView("/sps/event/inner/vividityJoinList");

		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setEventId(request.getParameter("eventId"));
		search.setPageSize(4);
		
		List<SpsEventjoin> joinList = eventService.getExpEventDetail(search);
		
		model.addObject("expJoinList", joinList);
		model.addObject("search", search);
		
		return model;
	}
	
}