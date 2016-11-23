package gcp.frontpc.controller.view.ccs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.NoticeService;
import gcp.sps.model.SpsEventgift;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("noticeViewController")
@RequestMapping("ccs/cs")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private EventService eventService;

	/**
	 * 공지사항
	 * 
	 * @Method Name : notice
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/notice/list", method ={ RequestMethod.GET , RequestMethod.POST })
	public ModelAndView notice(CcsNoticeSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		if (CommonUtil.isNotEmpty(request.getParameter("brandId"))) {
			mv.addObject("brandId", request.getParameter("brandId"));
		}
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "공지사항");
		mv.addObject("pageNavi", pageMap);

		mv.addObject("search", search);
		
		return mv;
	}
	
	/**
	 * 공지사항 목록 조회
	 * 
	 * @Method Name : getNoticeListSearch
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/notice/list/ajax", method = RequestMethod.GET)
	public ModelAndView getNoticeListSearch(CcsNoticeSearch search, HttpServletRequest request) throws Exception {

		search.setStoreId(SessionUtil.getStoreId());
		
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		search.setNewDt(yesterDay);
		search.setPageSize(10);
		List<CcsNotice> list = noticeService.getNoticeListSearch(search);

		ModelAndView mv = new ModelAndView("/ccs/cs/notice/inner/noticeList");
		mv.addObject("list", list);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}
	
	/**
	 * @Method Name : NoticeDetail
	 * @author : roy
	 * @date : 2016. 8. 30.
	 * @description : 공지사항 상세
	 *
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/notice/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView NoticeDetail(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		CcsNotice notice = new CcsNotice();

		notice.setReadCnt(new BigDecimal(request.getParameter("readCnt")).add(new BigDecimal("1")));
		notice.setNoticeNo(new BigDecimal(request.getParameter("noticeNo")));
		notice.setStoreId(SessionUtil.getStoreId());
		
		noticeService.updateNoticeReadCnt(notice);
		
		mv.addObject("notice", noticeService.selectOne(notice));
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "공지사항");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * 당첨자 발표
	 * 
	 * @Method Name : event
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/event/list", method = RequestMethod.GET)
	public ModelAndView event(CcsNoticeSearch search, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		mv.addObject("search", search);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "당첨자 발표");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * 당첨자발표 목록 조회
	 * 
	 * @Method Name : getEventNoticeListSearch
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/event/notice/list/ajax", method = RequestMethod.GET)
	public ModelAndView getEventNoticeListSearch(CcsNoticeSearch search, HttpServletRequest request) throws Exception {

		search.setStoreId(SessionUtil.getStoreId());
		search.setPageSize(10);
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		search.setNewDt(yesterDay);
		
		List<CcsNotice> list = noticeService.getEventNoticeListSearch(search);

		ModelAndView mv = new ModelAndView("/ccs/cs/event/inner/eventList");

		mv.addObject("list", list);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}
	
	/**
	 * @Method Name : eventDetail
	 * @author : roy
	 * @date : 2016. 8. 31.
	 * @description : 당첨자 발표 상세
	 *
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/event/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView eventDetail(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		CcsNotice notice = new CcsNotice();
		
		notice.setReadCnt(new BigDecimal(request.getParameter("readCnt")).add(new BigDecimal("1")));
		notice.setNoticeNo(new BigDecimal(request.getParameter("noticeNo")));
		notice.setStoreId(SessionUtil.getStoreId());
		noticeService.updateNoticeReadCnt(notice);

		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setEventId(notice.getEventId());
		
		List<SpsEventgift> event = eventService.getEventWinnerList(search);
		
		mv.addObject("event", event);
		mv.addObject("notice", noticeService.selectOne(notice));
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "당첨자 발표");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * 
	 * @Method Name : getBrandTemplate
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : FO 브랜드관 템플릿 메인 브랜드별 공지사항 리스트
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "/brand/notice/list/ajax", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getBrandNoticeList(CcsNoticeSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/cs/notice/inner/noticeList");		

		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		
		search.setNewDt(yesterDay);
		search.setStoreId(SessionUtil.getStoreId());
		search.setBrandId(request.getParameter("brandId"));
		search.setPageSize(10);
		
		List<CcsNotice> list = noticeService.getBrandNoticeList(search);
		mv.addObject("list", list);
		mv.addObject("search", search);
		
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		
		return mv;
	}
}
