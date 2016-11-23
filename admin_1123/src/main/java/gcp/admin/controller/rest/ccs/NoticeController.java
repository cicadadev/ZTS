package gcp.admin.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.NoticeService;
import gcp.common.util.BoSessionUtil;
import gcp.sps.model.SpsEvent;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/notice")
public class NoticeController {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private NoticeService noticeService;

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsNotice> selectList(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsNotice> noticeList = noticeService.selectList(ccsNoticeSearch);

		return noticeList;
	}

	@RequestMapping(value = "/polist", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsNotice> selectPoList(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			ccsNoticeSearch.setBusinessId(BoSessionUtil.getBusinessId());
		}
		List<CcsNotice> noticeList = noticeService.selectPoList(ccsNoticeSearch);

		return noticeList;
	}

	@RequestMapping(method = RequestMethod.GET)
	public CcsNotice selectOne(@RequestParam("noticeNo") String noticeNo) throws Exception {
		CcsNotice notice = new CcsNotice();
		notice.setNoticeNo(new BigDecimal(noticeNo));
		notice.setStoreId(SessionUtil.getStoreId());
		
		return noticeService.selectOne(notice);
	}

	/**
	 * MD 공지 조회
	 * 
	 * @Method Name : selectOneDetail
	 * @author : intune
	 * @date : 2016. 10. 17.
	 * @description :
	 *
	 * @param noticeNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/md", method = RequestMethod.GET)
	public CcsNotice selectOneDetail(@RequestParam("noticeNo") String noticeNo) throws Exception {
		CcsNotice notice = new CcsNotice();
		notice.setNoticeNo(new BigDecimal(noticeNo));
		notice.setStoreId(SessionUtil.getStoreId());

		return noticeService.selectMdNotice(notice);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String insert(@RequestBody CcsNotice notice) throws Exception {
		String resultCode;
		try {
			notice.setStoreId(SessionUtil.getStoreId());

			resultCode = String.valueOf(noticeService.insert(notice));
		} catch (Exception e) {
			resultCode = "fail";
			logger.debug(e);

		}

		return resultCode;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@RequestBody CcsNotice notice) throws Exception {
		String resultCode;
		try {
			notice.setStoreId(SessionUtil.getStoreId());
			notice.setUpdId(SessionUtil.getLoginId());

			resultCode = String.valueOf(noticeService.update(notice));
		} catch (Exception e) {
			resultCode = "fail";
			logger.debug(e);

		}

		return resultCode;
	}
	
	/**
	 * 이벤트 존재 여부 체크
	 * 
	 * @Method Name : checkEvent
	 * @author : roy
	 * @date : 2016. 9. 5.
	 * @description : 이벤트 존재 여부 체크
	 *
	 * @param ccsNotices
	 * @return result
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkEvent", method = RequestMethod.PUT)
	public String checkEvent(@RequestBody CcsNotice notice) throws Exception {
		String resultCode;
		try {
			SpsEvent event = new SpsEvent();
			event.setStoreId(SessionUtil.getStoreId());
			event.setEventId(notice.getEventId());

			if(noticeService.checkEvent(event) > 0){
				resultCode = "true";
			}else{
				resultCode = "fail";
			}
		} catch (Exception e) {
			resultCode = "fail";
			logger.debug(e);
		}

		return resultCode;
	}

	/**
	 * 그리드 데이터 일괄변경(고정여부, 사용여부)
	 * 
	 * @Method Name : saveGrid
	 * @author : victor
	 * @date : 2016. 6. 21.
	 * @description : 정렬순서와 사용여부에 대한 수정값을 일괄저장한다.
	 *
	 * @param ccsNotices
	 * @return int (변경된 그리드 row 수)
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public int saveGrid(@RequestBody List<CcsNotice> ccsNotices) throws Exception {
		int updCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (CcsNotice Notice : ccsNotices) {
			Notice.setUpdDt(currentDate);
			Notice.setUpdId(userId);
			updCnt += noticeService.update(Notice);
		}
		return updCnt;
	}

	/**
	 * 그리드 데이터 일괄삭제
	 * 
	 * @Method Name : deleteGrid
	 * @author : victor
	 * @date : 2016. 6. 21.
	 * @description : 그리드에서 선택한 값을 일괄 삭제한다.
	 *
	 * @param ccsNotices
	 * @return int (삭제된 그리드 row 수)
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public int deleteGrid(@RequestBody List<CcsNotice> ccsNotices) throws Exception {
		int delCnt = 0;
		for (CcsNotice notice : ccsNotices) {
			delCnt += noticeService.delete(notice);
		}
		return delCnt;
	}
}
