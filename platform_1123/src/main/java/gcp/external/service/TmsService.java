package gcp.external.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.ShoppingAlarm;
import gcp.external.model.TmsQueue;
import gcp.mms.model.search.MmsMemberSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;

@Service("tmsService")
public class TmsService extends BaseService {
	/**
	 * tms 전송
	 * 
	 * @Method Name : sendTmsEmailQueue
	 * @author : roy
	 * @date : 2016. 10. 13.
	 * @description :
	 *
	 * @param TmsQueue
	 * @throws Exception
	 */
	public void sendTmsEmailQueue(TmsQueue tmsQueue) {
		try {
//			properties.xml tms전송 제어
			if (BaseConstants.YN_Y.equals(Config.getString("tmsYn"))) {
				((TmsService) getThis()).saveTmsEmailQueueNewTx(tmsQueue);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void saveTmsEmailQueueNewTx(TmsQueue tmsQueue) throws Exception {
		// 테스트 메일
//		mail.setMsgCode("001");
//		mail.setToId("zts_test");
//		mail.setToName("zts_test");
//		mail.setToEmail("roy@intune.co.kr");
//		mail.setFromName("zts_test");
//		mail.setFromEmail("roy@intune.co.kr");
//		mail.setSubject("TMS 메일 연동 테스트 입니다.");
//		mail.setMapContent("");
//		mail.setMap1("INSERT MAP1");
//		mail.setMap2("INSERT MAP2");
//		mail.setMap3("INSERT MAP3");
//		mail.setMap4("INSERT MAP4");
//		mail.setMap5("INSERT MAP5");

		if (CommonUtil.isEmpty(tmsQueue.getMsgCode())) {
			tmsQueue.setMsgCode("001");
		}
		if (CommonUtil.isEmpty(tmsQueue.getSubject())) {
			tmsQueue.setSubject("제로투세븐");
		}
		if (CommonUtil.isEmpty(tmsQueue.getToId())) {
			tmsQueue.setToId("ZTS_MEMBER_ID");
		}
		if (CommonUtil.isEmpty(tmsQueue.getFromEmail())) {
			tmsQueue.setFromEmail("mallmaster@maeil.com");
		}
		if (CommonUtil.isEmpty(tmsQueue.getFromName())) {
			tmsQueue.setFromName("제로투세븐");
		}
		tms.insert("external.tms.insertQueue", tmsQueue);
	}

	public void sendTmsSmsQueue(TmsQueue tmsQueue) {
		try {
//			properties.xml tms전송 제어
			if (BaseConstants.YN_Y.equals(Config.getString("tmsYn"))) {
				((TmsService) getThis()).saveTmsSmsQueueNewTx(tmsQueue);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void saveTmsSmsQueueNewTx(TmsQueue tmsQueue) throws Exception {

		// 테스트 SMS
//		mail.setMsgCode("100");
//		mail.setToId("zts_test");
//		mail.setToName("zts_test");
//		mail.setToPhone("01000000000");
//		mail.setFromName("zts_test");
//		mail.setFromPhone("15888744");
//		mail.setSubject("TMS SMS 연동 테스트 입니다.");
//		mail.setMapContent("");
//		mail.setMap1("INSERT MAP1");
//		mail.setMap2("INSERT MAP2");
//		mail.setMap3("INSERT MAP3");
//		mail.setMap4("INSERT MAP4");
//		mail.setMap5("INSERT MAP5");

		if (!CommonUtil.isEmpty(tmsQueue.getToPhone())) {
			tmsQueue.setToPhone(tmsQueue.getToPhone().replaceAll("-", ""));
		}

		if (CommonUtil.isEmpty(tmsQueue.getMsgCode())) {
			tmsQueue.setMsgCode("100");
		}
//		TODO 임시 세팅
		if (CommonUtil.isEmpty(tmsQueue.getSubject())) {
			tmsQueue.setSubject("제로투세븐");
		}
		if (CommonUtil.isEmpty(tmsQueue.getToId())) {
			tmsQueue.setToId("ZTS_MEMBER_ID");
		}
		if (CommonUtil.isEmpty(tmsQueue.getToName())) {
			tmsQueue.setToName("ZTS_MEMBER_NAME");
		}
		if (CommonUtil.isEmpty(tmsQueue.getFromPhone())) {
			tmsQueue.setFromPhone("15888744");
		}
		tms.insert("external.tms.insertQueue", tmsQueue);
	}

	/**
	 * @Method Name : getMemberShoppingAlarmCount
	 * @author : ian
	 * @date : 2016. 11. 1.
	 * @description : 쇼핑 알림 보관함 메세지 개수
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getMemberShoppingAlarmCount(MmsMemberSearch search) throws Exception {
		return (BigDecimal) tms.selectOne("external.tms.getMemberShoppingAlarmCount", search);
	}
	
	/**
	 * @Method Name : getMemberShoppingAlarm
	 * @author : ian
	 * @date : 2016. 11. 1.
	 * @description : 쇼핑 알림 보관함 리스트
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ShoppingAlarm> getMemberShoppingAlarm(MmsMemberSearch search) throws Exception {
		return (List<ShoppingAlarm>) tms.selectList("external.tms.getMemberShoppingAlarm", search);
	}

}
