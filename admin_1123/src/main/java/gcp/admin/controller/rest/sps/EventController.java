package gcp.admin.controller.rest.sps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventcoupon;
import gcp.sps.model.SpsEventgift;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@RestController("EventController")
@RequestMapping("api/sps/event")
public class EventController extends BaseController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private EventService	eventService;

	/**
	 * @Method Name : getEvent
	 * @author : peter
	 * @date : 2016. 5. 10.
	 * @description : 이벤트 목록 리스트
	 *
	 * @param sps
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<SpsEvent> getEventList(@RequestBody SpsEventSearch ses) {
		List<SpsEvent> list = null;
		try {
			ses.setStoreId(SessionUtil.getStoreId());
			list = eventService.getEventList(ses);
		} catch (ServiceException se) {
			SpsEvent set = new SpsEvent();
			set.setSuccess(false);
			set.setResultCode(se.getMessageCd());
			set.setResultMessage(se.getMessage());
			list.add(set);
		}
		return list;
	}

	/**
	 * @Method Name : updateEvent
	 * @author : peter
	 * @date : 2016. 5. 27.
	 * @description : 그리드 변경 데이터 저장
	 *
	 * @param eventId
	 * @param bse
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	public void updateEvent(@RequestBody List<SpsEvent> seList) throws Exception {
//		eventService.updateEvent(seList);
//	}

	/**
	 * @Method Name : deleteEvent
	 * @author : peter
	 * @date : 2016. 5. 27.
	 * @description : 그리드 선택 항목 삭제
	 *
	 * @param eventId
	 * @throws Exception
	 */
//	@RequestMapping(value = "/delete", method = RequestMethod.POST)
//	public void deleteEvent(@RequestBody List<SpsEvent> seList) throws Exception {
//		eventService.deleteEvent(seList);
//	}

	/**
	 * @Method Name : eventExcelDown
	 * @author : peter
	 * @date : 2016. 5. 26.
	 * @description : 그리드 목록 Excel 다운
	 *
	 * @param ses
	 * @return
	 */
	@RequestMapping(value = "/excel", method = RequestMethod.POST)
	public String eventExcelDown(@RequestBody SpsEventSearch ses) {

		List<SpsEvent> seList = new ArrayList<SpsEvent>();
		ses.setStoreId(SessionUtil.getStoreId());
		seList = eventService.getEventList(ses);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < seList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("eventId", seList.get(i).getEventId());
			map.put("name", seList.get(i).getName());
			map.put("eventTypeName", seList.get(i).getEventTypeName());
			if ("Y".equals(seList.get(i).getDisplayYn())) {
				map.put("displayYn", "전시");
			} else {
				map.put("displayYn", "미전시");
			}
			map.put("eventStartDt", (seList.get(i).getEventStartDt()).toString());
			map.put("eventEndDt", (seList.get(i).getEventEndDt()).toString());
			map.put("eventStateName", seList.get(i).getEventStateName());
			map.put("winNoticeDate", (seList.get(i).getWinNoticeDate()).toString());
			map.put("insId", seList.get(i).getInsId());
			map.put("insDt", seList.get(i).getInsDt());
			map.put("updId", seList.get(i).getUpdId());
			map.put("updDt", seList.get(i).getUpdDt());
			dataList.add(map);
		}

		String msg = ExcelUtil.excelDownlaod((SpsEventSearch) ses, dataList);

		return msg;
	}

	/**
	 * @Method Name : insertEvent
	 * @author : peter
	 * @date : 2016. 5. 27.
	 * @description :
	 *
	 * @param set
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/insert", method = RequestMethod.POST)
	public String insertEvent(@RequestBody SpsEvent spe) throws Exception {
		spe.setStoreId(SessionUtil.getStoreId());
		spe.setUpdId(SessionUtil.getLoginId());
		String eventId = eventService.insertEvent(spe);
		
		return eventId;
	}

	/**
	 * @Method Name : getEventDetail
	 * @author : peter
	 * @date : 2016. 5. 16.
	 * @description :
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/popup/detail", method = RequestMethod.POST)
	public SpsEvent getEventDetail(@RequestBody SpsEventSearch ses) {
		SpsEvent spe = new SpsEvent();

		try {
			ses.setStoreId(SessionUtil.getStoreId());
			spe = eventService.getEventDetail(ses);
		} catch (ServiceException se) {
			spe = new SpsEvent();
			spe.setSuccess(false);
			spe.setResultCode(se.getMessageCd());
			spe.setResultMessage(se.getMessage());
		}

		return spe;
	}

	/**
	 * @Method Name : updateEventInfo
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 단건 이벤트 정보 수정
	 *
	 * @param set
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/update", method = RequestMethod.POST)
	public void updateEventInfo(@RequestBody SpsEvent spe) throws Exception {
		spe.setStoreId(SessionUtil.getStoreId());
		eventService.updateEventInfo(spe);
	}

	/**
	 * @Method Name : getCouponList
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 이벤트 쿠폰 목록
	 *
	 * @param ses
	 * @return
	 */
	@RequestMapping(value = "/popup/coupon/list", method = RequestMethod.POST)
	public List<SpsEventcoupon> getEventCouponList(@RequestBody SpsEventSearch ses) {
		ses.setStoreId(SessionUtil.getStoreId());
		List<SpsEventcoupon> list = eventService.getEventCouponList(ses);
		return list;
	}

	/**
	 * @Method Name : getEventJoinList
	 * @author : peter
	 * @date : 2016. 5. 16.
	 * @description : 이벤트 응모자 목록
	 *
	 * @param ses
	 * @return
	 */
	@RequestMapping(value = "/popup/join/list", method = RequestMethod.POST)
	public List<SpsEventjoin> getEventJoinList(@RequestBody SpsEventSearch ses) {
		ses.setStoreId(SessionUtil.getStoreId());
		List<SpsEventjoin> list = eventService.getEventJoinList(ses);
		return list;
	}

	/**
	 * @Method Name : updateEventjoin
	 * @author : peter
	 * @date : 2016. 6. 13.
	 * @description : 응모자 목록 당첨여부 변경
	 *
	 * @param sejList
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/join/save", method = RequestMethod.POST)
	public void updateEventjoin(@RequestBody List<SpsEventjoin> sejList) throws Exception {
		eventService.updateEventjoin(sejList);
	}

	@RequestMapping(value = "/popup/join/excel", method = RequestMethod.POST)
	public String eventJoinExcelDown(@RequestBody SpsEventSearch ses) {

		List<SpsEventjoin> sejList = new ArrayList<SpsEventjoin>();
		ses.setStoreId(SessionUtil.getStoreId());
		sejList = eventService.getEventJoinList(ses);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < sejList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("memberNo", sejList.get(i).getMemberNo().toString());
			map.put("memName", sejList.get(i).getMemName());
			map.put("phone1", sejList.get(i).getPhone1());
			map.put("phone2", sejList.get(i).getPhone2());
			map.put("insDt", sejList.get(i).getInsDt());
			map.put("winYn", sejList.get(i).getWinYn());
			dataList.add(map);
		}

		String msg = ExcelUtil.excelDownlaod((SpsEventSearch) ses, dataList);

		return msg;
	}

	/**
	 * @Method Name : getEventjoinDetail
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 회원 응모 상세정보
	 *
	 * @param ses
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/join/detail", method = RequestMethod.POST)
	public SpsEventjoin getEventjoinDetail(@RequestBody SpsEventSearch ses) throws Exception {
		ses.setStoreId(SessionUtil.getStoreId());
		SpsEventjoin sej = eventService.getEventjoinDetail(ses);

		return sej;
	}

	/**
	 * @Method Name : getEventWinhistory
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 회원 당첨이력
	 *
	 * @param ses
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/winhistory/list", method = RequestMethod.POST)
	public List<SpsEventjoin> getEventWinhistory(@RequestBody SpsEventSearch ses) throws Exception {
		ses.setStoreId(SessionUtil.getStoreId());
		List<SpsEventjoin> list = eventService.getEventWinhistory(ses);

		return list;
	}

	/**
	 * @Method Name : imgUpload
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 배너 이미지 파일 업로드
	 *
	 * @param req
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public void imgUpload(MultipartHttpServletRequest req) throws Exception {
		logger.debug("\t >> TEST.fileUpload Start");
		eventService.fileUpload(req);
		logger.debug("\n\t >> TEST.fileUpload End");
	}

	/**
	 * @Method Name : deleteEvent
	 * @author : stella
	 * @date : 2016. 7. 25.
	 * @description : 이벤트  삭제
	 *
	 * @param eventList
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)	
	public void deleteEvent(@RequestBody List<SpsEvent> eventList) throws Exception {
		eventService.deleteEvent(eventList);
	}
	
	/**
	 * @Method Name : deleteEventProduct
	 * @author : stella
	 * @date : 2016. 7. 22.
	 * @description : 체험이벤트 상품 삭제
	 *
	 * @param search
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST)	
	public void deleteEventProduct(@RequestBody SpsEvent event) throws Exception {
		event.setStoreId(SessionUtil.getStoreId());		
		eventService.deleteEventProduct(event);
	}

	/**
	 * @Method Name : getEventGiftList
	 * @author : stella
	 * @date : 2016. 8. 17.
	 * @description : 이벤트 경품 목록 리스트
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/gift/list", method = RequestMethod.POST)
	public List<SpsEventgift> getEventGiftList(@RequestBody SpsEventSearch search) {
		List<SpsEventgift> list = new ArrayList<>();
		
		try {
			search.setStoreId(SessionUtil.getStoreId());
			list = eventService.getEventGiftList(search);
		} catch (ServiceException se) {
			SpsEventgift eventGift = new SpsEventgift();
			eventGift.setSuccess(false);
			eventGift.setResultCode(se.getMessageCd());
			eventGift.setResultMessage(se.getMessage());
			list.add(eventGift);
		}
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateEventStatus
	 * @author : allen
	 * @date : 2016. 8. 23.
	 * @description : 이벤트 상태 업데이트
	 *
	 * @param request
	 * @param spsEvnt
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateEventStatus", method = RequestMethod.POST)
	public void updateEventStatus(HttpServletRequest request, @RequestBody SpsEvent spsEvnt) throws Exception {
		eventService.updateEventStatus(spsEvnt);
	}

	/**
	 * 
	 * @Method Name : insertEventJoin
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description : 이벤트 당첨자 저장
	 *
	 * @param eventJoin
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/join/insert", method = RequestMethod.POST)
	public void insertEventJoin(@RequestBody List<SpsEventjoin> eventJoinList) throws Exception {
		eventService.insertEventJoin(eventJoinList);
	}

}
