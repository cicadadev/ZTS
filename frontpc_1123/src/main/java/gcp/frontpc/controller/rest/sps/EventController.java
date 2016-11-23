package gcp.frontpc.controller.rest.sps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.service.MemberService;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@RestController("EventController")
@RequestMapping("api/sps/event")
public class EventController extends BaseController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private EventService	eventService;

	@Autowired
	private CommonService	commonService;

	@Autowired
	private MemberService	memberService;
	/**
	 * 제휴카드 등록
	 * 
	 * @Method Name : issueCoupon
	 * @author : eddie
	 * @date : 2016. 10. 24.
	 * @description :
	 *
	 * @param zts
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/save", method = { RequestMethod.POST })
	public String saveAffiliatecard(MmsMemberZts zts, HttpServletRequest request) throws Exception {

		zts.setMemberNo(SessionUtil.getMemberNo());

		return eventService.saveAffiliatecard(zts);

	}

	/**
	 * 제휴카드 등록 여부 체크
	 * 
	 * @Method Name : checkAffiliateCard
	 * @author : intune
	 * @date : 2016. 11. 2.
	 * @description :
	 *
	 * @param zts
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/check", method = { RequestMethod.POST })
	public String checkAffiliateCard(HttpServletRequest request) throws Exception {

		MmsMemberZts zts = new MmsMemberZts();
		zts.setMemberNo(SessionUtil.getMemberNo());
		return eventService.checkAffiliateCard(zts);

	}

	/**
	 * 신한(고운맘, 키즈)카드 등록
	 * 
	 * @Method Name : saveAffiliatecardShinhan
	 * @author : intune
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @param zts
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shinhan/save", method = { RequestMethod.GET })
	public String saveAffiliatecardShinhan(MmsMemberZts zts, HttpServletRequest request) throws Exception {

		zts.setMemberNo(SessionUtil.getMemberNo());

		return eventService.saveAffiliatecardShinhan(zts);

	}

	/**
	 * 이벤트 응모(공통)
	 * 
	 * @Method Name : saveEventjoin2
	 * @author : roy
	 * @date : 2016. 11. 20.
	 * @description :
	 *
	 * @param join
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/join/save2", method = RequestMethod.GET)
	public Map<String, String> saveEventjoin2(SpsEventSearch join, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		SpsEvent eventDetail = new SpsEvent();
		join.setStoreId(SessionUtil.getStoreId());

		eventDetail = eventService.getEventDetail(join);

//		허용설정 정보
		BigDecimal memberNo = SessionUtil.getMemberNo();
		String channelId = SessionUtil.getChannelId();
		String storeId = SessionUtil.getStoreId();
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd();
		String gradeCd = FoSessionUtil.getMemGradeCd();
		List<String> memberTypeCds = new ArrayList<String>();
		memberService.getMemberTypeInfo(memberTypeCds);

		boolean resultControl = false;
		boolean duplicateJoin = false;

		String currentDay = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String today = DateUtil.getCurrentDate(DateUtil.FORMAT_2);

//		응모 정보
		List<SpsEventjoin> eventJoinDetail = (List<SpsEventjoin>) eventService.getEventJoinList(join);

		for (SpsEventjoin info : eventJoinDetail) {
			String insDt = info.getInsDt();
			if (DateUtil.compareToDate(today, insDt, DateUtil.FORMAT_2) == 0) {
				duplicateJoin = true;
				break;
			}
		}
//		기간 Check && 이벤트 상태
//		 -1 이전, 0 같음, 1 이후
		if (!(eventDetail.getEventStateCd().equals("EVENT_STATE_CD.RUN")
				&& DateUtil.compareToDate(currentDay, eventDetail.getEventJoinStartDt(), DateUtil.FORMAT_1) == 1
				&& DateUtil.compareToDate(currentDay, eventDetail.getEventJoinEndDt(), DateUtil.FORMAT_1) == -1)) {
			result.put(BaseConstants.RESULT_MESSAGE, "이벤트 기간이 아닙니다.");
		}
//		등록 제한 1일1회
		else if (eventDetail.getJoinControlCd().equals("JOIN_CONTROL_CD.DAY") && duplicateJoin) {
			result.put(BaseConstants.RESULT_MESSAGE, "하루에 1번 응모 가능합니다.");
		}
//		등록 제한 전체1회
		else if (eventDetail.getJoinControlCd().equals("JOIN_CONTROL_CD.TOTAL") && eventJoinDetail.size() > 0) {
			result.put(BaseConstants.RESULT_MESSAGE, "이미 응모하셨습니다.");
		}
		else {
//			허용제한
			if (CommonUtil.isNotEmpty(eventDetail.getControlNo())) {

				//제어 check
				CcsControlSearch search = new CcsControlSearch();
				search.setStoreId(storeId);
				search.setControlNo(eventDetail.getControlNo());
				search.setChannelId(channelId);
				search.setMemberNo(memberNo);
				search.setDeviceTypeCd(deviceTypeCd);
				search.setMemberTypeCds(memberTypeCds);
				search.setMemGradeCd(gradeCd);
				search.setExceptionFlag(false);
				resultControl = commonService.checkControl(search);
			}

			if (resultControl) {

			} else {
				SpsEventjoin eventJoin = new SpsEventjoin();
				eventJoin.setStoreId(SessionUtil.getStoreId());
				eventJoin.setMemberNo(SessionUtil.getMemberNo());
				eventJoin.setEventId(join.getEventId());

				if (CommonUtil.isEmpty(eventJoin.getWinYn())) {
					eventJoin.setWinYn("N");
				}

				eventService.insertOneTable(eventJoin);
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			}
		}

		return result;
	}
}
