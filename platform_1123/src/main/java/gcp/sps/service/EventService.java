package gcp.sps.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsControlchannel;
import gcp.ccs.model.CcsControldevice;
import gcp.ccs.model.CcsControlmembergrade;
import gcp.ccs.model.CcsControlmembertype;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsAffiliatecard;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventbrand;
import gcp.sps.model.SpsEventcoupon;
import gcp.sps.model.SpsEventgift;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.SpsEventprize;
import gcp.sps.model.search.SpsEventSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.CompressionUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.FileMeta;

@Service("eventService")
public class EventService extends BaseService {

	@Autowired
	private CommonService	commonService;

	private final Log		logger	= LogFactory.getLog(getClass());

	/**
	 * @Method Name : getEvent
	 * @author : peter
	 * @date : 2016. 5. 10.
	 * @description : 이벤트 목록 조회
	 *
	 * @param sps
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEvent> getEventList(SpsEventSearch ses) throws ServiceException {
		return (List<SpsEvent>) dao.selectList("sps.event.getEventList", ses);
	}

	/**
	 * @Method Name : updateEvent
	 * @author : peter
	 * @date : 2016. 5. 25.
	 * @description : 이벤트 목록 그리드에서 선택한 자료 변경
	 *
	 * @param seList
	 * @throws Exception
	 */
//	public void updateEvent(List<SpsEvent> seList) throws Exception {
//		for (SpsEvent seOne : seList) {
//			dao.updateOneTable(seOne);
//		}
//	}

	/**
	 * @Method Name : deleteEvent
	 * @author : peter
	 * @date : 2016. 5. 19.
	 * @description : 이벤트 목록 그리드에서 선택한 자료 삭제
	 *
	 * @param seList
	 * @throws Exception
	 */
//	public void deleteEvent(List<SpsEvent> seList) throws Exception {
//		for (SpsEvent seOne : seList) {
//			dao.deleteOneTable(seOne);
//		}
//	}

	public String insertEvent(SpsEvent spe) throws Exception {
		String storeId = spe.getStoreId();
		String updId = spe.getUpdId();

		// 0. 허용 설정
		boolean isControlSet = spe.getCcsControl() != null && "N".equals(spe.getIsAllPermit());

		if (isControlSet) {
			BigDecimal newControlNo = commonService.updateControl(spe.getControlNo(), spe.getCcsControl());
			spe.setControlNo(newControlNo);
		}

		//이미지 복사(temp to real)
		spe.setImg1(FileUploadUtil.moveTempToReal(spe.getImg1()));
		spe.setImg2(FileUploadUtil.moveTempToReal(spe.getImg2()));

		//제한설정 처리
		BigDecimal controlNo = null;
		if (null != spe.getCcsControl() && !"".equals(spe.getCcsControl())) {
			controlNo = insertCcsControl(spe);
		}

		//신규 이벤트 등록
		spe.setControlNo(controlNo);
		dao.insert("sps.event.insertEvent", spe);
		String eventId = spe.getEventId();

		//브랜드 정보 저장
		int brandCnt = spe.getSpsEventbrands().size();
		if (brandCnt > 0) {
			for (SpsEventbrand sebOne : spe.getSpsEventbrands()) {
				if (null != sebOne.getBrandId() && !"".equals(sebOne.getBrandId())) {
					sebOne.setStoreId(storeId);
					sebOne.setEventId(eventId);
					sebOne.setInsId(updId);
					sebOne.setUpdId(updId);
					dao.insertOneTable(sebOne);
				}
			}
		}

		//이벤트 유형: 쿠폰이벤트인 경우
		if ("EVENT_TYPE_CD.COUPON".equals(spe.getEventTypeCd())) {
			int couponCnt = spe.getSpsEventcoupons().size();
			if (couponCnt > 0) {
				for (SpsEventcoupon secOne : spe.getSpsEventcoupons()) {
					secOne.setStoreId(storeId);
					secOne.setEventId(eventId);
					dao.insertOneTable(secOne);
				}
			}
		}
		//이벤트 유형: 출석이벤트인 경우
		else if ("EVENT_TYPE_CD.ATTEND".equals(spe.getEventTypeCd())) {
			int prizeCnt = spe.getSpsEventprizes().size();
			if (prizeCnt > 0) {
				for (SpsEventprize sepOne : spe.getSpsEventprizes()) {
					sepOne.setStoreId(storeId);
					sepOne.setEventId(eventId);
					dao.insertOneTable(sepOne);
				}
			}
		}
		//이벤트 유형: 수동이벤트인 경우
		else if ("EVENT_TYPE_CD.MANUAL".equals(spe.getEventTypeCd())) {
			int giftCnt = spe.getSpsEventgifts().size();
			if (giftCnt > 0) {
				for (SpsEventgift segOne : spe.getSpsEventgifts()) {
					segOne.setStoreId(storeId);
					segOne.setEventId(eventId);
					dao.insertOneTable(segOne);
				}
			}
		}

		return eventId;
	}

	/**
	 * @Method Name : insertEventJoin
	 * @author : stella
	 * @date : 2016. 8. 30.
	 * @description : 이벤트 당첨자 저장
	 * 
	 * @param sej
	 * @return
	 * @throws ServiceException
	 */
	public void insertEventJoin(List<SpsEventjoin> sejList) throws Exception {
		String storeId = SessionUtil.getStoreId();

		for (SpsEventjoin sej : sejList) {
			sej.setStoreId(storeId);
			dao.insertOneTable(sej);

			if (CommonUtil.isNotEmpty(sej.getGiftName())) {
				SpsEventgift eventGift = new SpsEventgift();
				eventGift.setStoreId(storeId);
				eventGift.setEventId(sej.getEventId());
				eventGift.setName(sej.getGiftName());

				dao.insertOneTable(eventGift);
			}
		}
	}

	/**
	 * @Method Name : getEventDetail
	 * @author : peter
	 * @date : 2016. 5. 16.
	 * @description : 이벤트 상세 정보
	 *
	 * @param ses
	 * @return
	 * @throws ServiceException
	 */
	public SpsEvent getEventDetail(SpsEventSearch ses) throws ServiceException {
		if (null == ses.getEventId() || "".equals(ses.getEventId())) {
//			throw new ServiceException("sps.event.error");
		}

		return (SpsEvent) dao.selectOne("sps.event.getEventDetailInfo", ses);
	}

	/**
	 * @Method Name : updateEventInfo
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 이벤트 정보 수정
	 *
	 * @param set
	 * @throws Exception
	 */
	public void updateEventInfo(SpsEvent spe) throws Exception {
		String storeId = spe.getStoreId();
		String eventId = spe.getEventId();

		// 0. 허용설정
		BigDecimal newControlNo = null;
		BigDecimal deleteControlNo = null;
		boolean isControlDelete = spe.getControlNo() != null && "Y".equals(spe.getIsAllPermit());
		boolean isControlSet = spe.getCcsControl() != null && "N".equals(spe.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(spe.getControlNo(), spe.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = spe.getControlNo();
			spe.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			spe.setControlNo(newControlNo);
		}

		//이미지 복사(temp to real)
		spe.setImg1(FileUploadUtil.moveTempToReal(spe.getImg1()));
		spe.setImg2(FileUploadUtil.moveTempToReal(spe.getImg2()));

		//기존 이벤트 update
		dao.update("sps.event.updateEventInfo", spe);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}

		//브랜드 정보 저장
		//-> 기존 브랜드 정보 전체 삭제
		SpsEventbrand spsEventbrand = new SpsEventbrand();
		spsEventbrand.setStoreId(storeId);
		spsEventbrand.setEventId(eventId);
		dao.delete("sps.event.deleteEventbrand", spsEventbrand);
		//-> 현재 브랜드 정보 재등록
		int brandCnt = spe.getSpsEventbrands().size();
		if (brandCnt > 0) {
			for (SpsEventbrand sebOne : spe.getSpsEventbrands()) {
				if (null != sebOne.getBrandId() && !"".equals(sebOne.getBrandId())) {
					sebOne.setStoreId(storeId);
					sebOne.setEventId(eventId);
					dao.insertOneTable(sebOne);
				}
			}
		}

		//이벤트 유형: 쿠폰이벤트인 경우
		if ("EVENT_TYPE_CD.COUPON".equals(spe.getEventTypeCd())) {
			if (spe.getSpsEventcoupons() != null) {
				//기존 쿠폰이벤트정보 전체 삭제
				SpsEventcoupon spsEventcoupon = new SpsEventcoupon();
				spsEventcoupon.setStoreId(storeId);
				spsEventcoupon.setEventId(eventId);
				dao.delete("sps.event.deleteEventcoupon", spsEventcoupon);
				//현재 그리드의 쿠폰으로 재등록
				int couponCnt = spe.getSpsEventcoupons().size();
				if (couponCnt > 0) {
					for (SpsEventcoupon secOne : spe.getSpsEventcoupons()) {
						secOne.setStoreId(storeId);
						secOne.setEventId(eventId);
						dao.insertOneTable(secOne);
					}
				}
			}
		}
		//이벤트 유형: 출석이벤트인 경우
		else if ("EVENT_TYPE_CD.ATTEND".equals(spe.getEventTypeCd())) {
			//기존 출석이벤트정보 전체 삭제
			if (spe.getSpsEventprizes() != null) {
				SpsEventprize sep = new SpsEventprize();
				sep.setStoreId(storeId);
				sep.setEventId(eventId);
				dao.delete("sps.event.deleteEventprize", sep);
				//출석이벤트 현재 정보 재등록
				int prizeCnt = spe.getSpsEventprizes().size();
				if (prizeCnt > 0) {
					for (SpsEventprize sepOne : spe.getSpsEventprizes()) {
						sepOne.setStoreId(storeId);
						sepOne.setEventId(eventId);
						dao.insertOneTable(sepOne);
					}
				}
			}

		}
		//이벤트 유형: 수동이벤트인 경우
		else if ("EVENT_TYPE_CD.MANUAL".equals(spe.getEventTypeCd())) {
			if (spe.getSpsEventgifts() != null) {
				//기존 수동이벤트정보 전체 삭제
				SpsEventgift seg = new SpsEventgift();
				seg.setStoreId(storeId);
				seg.setEventId(eventId);
				dao.delete("sps.event.deleteEventGift", seg);
				//수동이벤트 현재 정보 재등록
				int giftCnt = spe.getSpsEventgifts().size();
				if (giftCnt > 0) {
					for (SpsEventgift segOne : spe.getSpsEventgifts()) {
						segOne.setStoreId(storeId);
						segOne.setEventId(eventId);
						dao.insertOneTable(segOne);
					}
				}
			}
		}

		if (!"EVENT_TYPE_CD.COUPON".equals(spe.getEventTypeCd()) && "Y".equals(spe.getCouponExistYn())) {
			SpsEventcoupon sec = new SpsEventcoupon();
			sec.setStoreId(storeId);
			sec.setEventId(eventId);
			dao.delete("sps.event.deleteEventcoupon", sec);
		}
		if (!"EVENT_TYPE_CD.ATTEND".equals(spe.getEventTypeCd()) && "Y".equals(spe.getPrizeExistYn())) {
			SpsEventprize sep = new SpsEventprize();
			sep.setStoreId(storeId);
			sep.setEventId(eventId);
			dao.delete("sps.event.deleteEventprize", sep);
		}
		if (!"EVENT_TYPE_CD.EXP".equals(spe.getEventTypeCd()) && "Y".equals(spe.getProductExistYn())) {
			spe.setProductId(null);
			dao.update("sps.event.updateEventProduct", spe);
		}
		if (!"EVENT_TYPE_CD.MANUAL".equals(spe.getEventTypeCd()) && "Y".equals(spe.getGiftExistYn())) {
			SpsEventgift seg = new SpsEventgift();
			seg.setStoreId(storeId);
			seg.setEventId(eventId);
			dao.delete("sps.event.deleteEventGift", seg);
		}
	}

	/**
	 * @Method Name : getCouponList
	 * @author : peter
	 * @date : 2016. 5. 29.
	 * @description : 쿠폰 목록
	 *
	 * @param scp
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEventcoupon> getEventCouponList(SpsEventSearch ses) throws ServiceException {
		List<SpsEventcoupon> list = (List<SpsEventcoupon>) dao.selectList("sps.event.getEventCouponList", ses);
		return list;
	}

	/**
	 * @Method Name : getEventJoinList
	 * @author : peter
	 * @date : 2016. 5. 16.
	 * @description : 응모자 목록
	 *
	 * @param ses
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEventjoin> getEventJoinList(SpsEventSearch ses) throws ServiceException {
		List<SpsEventjoin> list = (List<SpsEventjoin>) dao.selectList("sps.event.getEventJoinList", ses);
		return list;
	}

	/**
	 * @Method Name : updateEventjoin
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 응모자 목록 당첨여부 변경 반영
	 *
	 * @param sejList
	 * @throws Exception
	 */
	public void updateEventjoin(List<SpsEventjoin> sejList) throws Exception {
		for (SpsEventjoin sejOne : sejList) {
			dao.updateOneTable(sejOne);
		}
	}

	/**
	 * @Method Name : getEventjoinDetail
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 회원 응모 상세정보
	 *
	 * @param ses
	 * @return
	 * @throws ServiceException
	 */
	public SpsEventjoin getEventjoinDetail(SpsEventSearch ses) throws ServiceException {
		SpsEventjoin sej = (SpsEventjoin) dao.selectOne("sps.event.getEventJoinDetail", ses);
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
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEventjoin> getEventWinhistory(SpsEventSearch ses) throws ServiceException {
		List<SpsEventjoin> sejList = (List<SpsEventjoin>) dao.selectList("sps.event.getEventWinhistory", ses);
		return sejList;
	}

	/**
	 * @Method Name : fileUpload
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 배너 이미지 파일 업로드
	 *
	 * @param req
	 * @throws Exception
	 */
	public void fileUpload(MultipartHttpServletRequest req) throws Exception {
		String uploadPath = "C:/upload/testZip/";	// 저장경로

		LinkedList<FileMeta> fileList;
		fileList = FileUploadUtil.uploadFile(req, uploadPath);

		for (int j = 0; j < fileList.size(); j++) {
			logger.debug("\t upload Info >> " + fileList.get(j).getFileName() + fileList.get(j).getFileType()
					+ fileList.get(j).getFileSize());
		}

		String fileName = fileList.get(0).getFileName();
		String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);

		// 압축파일일 경우 압축 해제
		if ("zip".equals(fileFormat)) {
			CompressionUtil cu = new CompressionUtil();
			List<String> result = new ArrayList<String>();

			// unzip(파일, 디렉토리, 인코딩)
			// 인코딩 UTF-8 로 설정시 압축해제시에 한글파일명 깨짐
			result = cu.unzip(new File(uploadPath + fileName), new File(uploadPath), "euc-kr");

			// 임시
			for (int i = 0; i < result.size(); i++) {
				logger.debug("\t unzip List _ result.get(" + i + ") >" + result.get(i));
			}
		}
	}

	/**
	 * @Method Name : deleteEvent
	 * @author : stella
	 * @date : 2016. 7. 25.
	 * @description : 이벤트 삭제
	 *
	 * @param eventList
	 * @throws Exception
	 */
	public void deleteEvent(List<SpsEvent> eventList) throws Exception {
		for (SpsEvent event : eventList) {
			String storeId = SessionUtil.getStoreId();

			// 이벤트 타입에 따라 대상 테이블 데이터도 삭제
			event.setStoreId(storeId);
			SpsEvent eventCheck = (SpsEvent) dao.selectOne("sps.event.checkExistEventType", event);
			if ("Y".equals(eventCheck.getCouponExistYn())) {
				SpsEventcoupon eventCoupon = new SpsEventcoupon();
				eventCoupon.setStoreId(storeId);
				eventCoupon.setEventId(event.getEventId());
				dao.delete("sps.event.deleteEventcoupon", eventCoupon);
			}
			if ("Y".equals(eventCheck.getPrizeExistYn())) {
				SpsEventprize eventPrize = new SpsEventprize();
				eventPrize.setStoreId(storeId);
				eventPrize.setEventId(event.getEventId());
				dao.delete("sps.event.deleteEventprize", eventPrize);
			}
			if ("Y".equals(eventCheck.getJoinExistYn())) {
				SpsEventjoin eventJoin = new SpsEventjoin();
				eventJoin.setStoreId(storeId);
				eventJoin.setEventId(event.getEventId());
				dao.delete("sps.event.deleteEventjoin", eventJoin);
			}
			if ("Y".equals(eventCheck.getGiftExistYn()) || "EVENT_TYPE_CD.MANUAL".equals(event.getEventTypeCd())) {
				SpsEventgift eventGift = new SpsEventgift();
				eventGift.setStoreId(storeId);
				eventGift.setEventId(event.getEventId());
				dao.delete("sps.event.deleteEventGift", eventGift);
			}

			// 이벤트 브랜드 삭제
			SpsEventbrand eventBrand = new SpsEventbrand();
			eventBrand.setStoreId(storeId);
			eventBrand.setEventId(event.getEventId());
			dao.delete("sps.event.deleteEventbrand", eventBrand);

			// 이벤트 삭제
			dao.deleteOneTable(event);
		}
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
	public void deleteEventProduct(SpsEvent event) throws Exception {
		dao.update("sps.event.updateEventProduct", event);
	}

	/**
	 * @Method Name : getEventGiftList
	 * @author : stella
	 * @date : 2016. 8. 17.
	 * @description : 이벤트 경품 목록 조회
	 *
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEventgift> getEventGiftList(SpsEventSearch search) throws ServiceException {
		return (List<SpsEventgift>) dao.selectList("sps.event.getEventGiftList", search);
	}

	/**
	 * @Method Name : insertCcsControl
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 제한 설정 신규 등록
	 *
	 * @param spe
	 * @throws Exception
	 */
	private BigDecimal insertCcsControl(SpsEvent spe) throws Exception {
		String storeId = spe.getStoreId();
		BigDecimal controlNo = new BigDecimal(0);

		boolean isMemType = false;
		boolean isMemGrade = false;
		boolean isChannel = false;
		boolean isDevice = false;

		CcsControl ccsControl = new CcsControl();
		ccsControl.setStoreId(storeId);
		//회원유형 제한
		if (null != spe.getCcsControl().getMemberTypeArr()) {
			isMemType = true;
		}
		//회원등급 제한
		if (null != spe.getCcsControl().getMemGradeArr()) {
			isMemGrade = true;
		}
		//채널 제한
		if ("CHANNEL_CONTROL_CD.CHANNEL".equals(spe.getCcsControl().getChannelControlCd())) {
			isChannel = true;
		}
		ccsControl.setChannelControlCd(spe.getCcsControl().getChannelControlCd());
		// 디바이스 제한
		if (null != spe.getCcsControl().getDeviceTypeArr()) {
			isDevice = true;
		}
		//제한 조건이 하나라도 있으면 CCS_CONTROL 저장
		if (isMemType == true || isMemGrade == true || isChannel == true || isDevice == true) {
			dao.insert("ccs.common.insertControl", ccsControl);
			controlNo = ccsControl.getControlNo();
		} else {
			controlNo = null;
		}
		//CCS_CONTROLMEMBERTYPE
		if (isMemType) {
			CcsControlmembertype controlMembertype = new CcsControlmembertype();
			controlMembertype.setStoreId(storeId);
			controlMembertype.setControlNo(controlNo);
			for (String memberTypeCd : spe.getCcsControl().getMemberTypeArr()) {
				controlMembertype.setMemberTypeCd(memberTypeCd);
				dao.insertOneTable(controlMembertype);
			}
		}
		//CCS_CONTROLMEMBERGRADE
		if (isMemGrade) {
			CcsControlmembergrade controlMembergrade = new CcsControlmembergrade();
			controlMembergrade.setStoreId(storeId);
			controlMembergrade.setControlNo(controlNo);
			for (String memGradeCd : spe.getCcsControl().getMemGradeArr()) {
				controlMembergrade.setMemGradeCd(memGradeCd);
				dao.insertOneTable(controlMembergrade);
			}
		}
		//CCS_CONTROLCHANNEL
		if (isChannel) {
			CcsControlchannel controlChannel = new CcsControlchannel();
			controlChannel.setStoreId(storeId);
			controlChannel.setControlNo(controlNo);
			for (String channelId : spe.getCcsControl().getChannelIdArr()) {
				controlChannel.setChannelId(channelId);
				dao.insertOneTable(controlChannel);
			}
		}
		//CCS_CONTROLDEVICE
		if (isDevice) {
			CcsControldevice controlDevice = new CcsControldevice();
			controlDevice.setStoreId(storeId);
			controlDevice.setControlNo(ccsControl.getControlNo());
			for (String deviceTypeCd : spe.getCcsControl().getDeviceTypeArr()) {
				controlDevice.setDeviceTypeCd(deviceTypeCd);
				dao.insertOneTable(controlDevice);
			}
		}
		return controlNo;
	}

	/**
	 * @Method Name : deleteCcsControlmembertype
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 회원유형 자료 삭제
	 *
	 * @param storeId
	 * @param controlNo
	 */
	private void deleteCcsControlmembertype(String storeId, BigDecimal controlNo) {
		CcsControlmembertype controlMembertype = new CcsControlmembertype();
		controlMembertype.setStoreId(storeId);
		controlMembertype.setControlNo(controlNo);

		dao.delete("ccs.common.deleteControlMemberType", controlMembertype);
	}

	/**
	 * @Method Name : deleteCcsControlmemberGrade
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 회원등급 자료 삭제
	 *
	 * @param storeId
	 * @param controlNo
	 */
	private void deleteCcsControlmembergrade(String storeId, BigDecimal controlNo) throws Exception {
		CcsControlmembergrade controlMembergrade = new CcsControlmembergrade();
		controlMembergrade.setStoreId(storeId);
		controlMembergrade.setControlNo(controlNo);

		dao.insert("ccs.common.deleteControlMemberGrade", controlMembergrade);
	}

	/**
	 * @Method Name : deleteCcsControlchannel
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 채널정보 자료 삭제
	 *
	 * @param storeId
	 * @param controlNo
	 */
	private void deleteCcsControlchannel(String storeId, BigDecimal controlNo) throws Exception {
		CcsControlchannel controlChannel = new CcsControlchannel();
		controlChannel.setStoreId(storeId);
		controlChannel.setControlNo(controlNo);

		dao.insert("ccs.common.deleteControlChannel", controlChannel);
	}

	/**
	 * @Method Name : deleteCcsControldevice
	 * @author : peter
	 * @date : 2016. 6. 14.
	 * @description : 디바이스 자료 삭제
	 *
	 * @param storeId
	 * @param controlNo
	 */
	private void deleteCcsControldevice(String storeId, BigDecimal controlNo) throws Exception {
		CcsControldevice controlDevice = new CcsControldevice();
		controlDevice.setStoreId(storeId);
		controlDevice.setControlNo(controlNo);

		dao.insert("ccs.common.deleteControlDevice", controlDevice);
	}

	/**
	 * 
	 * @Method Name : updateEventStatus
	 * @author : allen
	 * @date : 2016. 8. 23.
	 * @description : 이벤트 상태 업데이트
	 *
	 * @param spsEvent
	 */
	public void updateEventStatus(SpsEvent spsEvent) {
		spsEvent.setStoreId(SessionUtil.getStoreId());
		spsEvent.setUpdId(SessionUtil.getLoginId());
		dao.update("sps.event.updateEventStatus", spsEvent);
	}

	/**
	 * 
	 * @Method Name : getWinnerDetail
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description : 이벤트 당첨자 정보 조회(당첨자 일괄 업로드용)
	 *
	 * @param memberNo
	 */
	public SpsEventjoin getWinnerDetail(String memberNo) throws Exception {
		SpsEventjoin join = new SpsEventjoin();

		MmsMemberZts memberInfo = (MmsMemberZts) dao.selectOne("mms.member.getMemberDetail", memberNo);
		join.setMemName(memberInfo.getMmsMember().getMemberName().concat("(").concat(memberInfo.getMmsMember().getMemberId()).concat(")"));
		join.setPhone1(memberInfo.getMmsMember().getPhone1());
		join.setPhone2(memberInfo.getMmsMember().getPhone2());

		if (CommonUtil.isNotEmpty(memberInfo.getMmsAddresss().get(0).getAddress1())) {
			join.setMemAddress(
					memberInfo.getMmsAddresss().get(0).getAddress1().concat(memberInfo.getMmsAddresss().get(0).getAddress2()));
		}

		return join;
	}

	/**
	 * @Method Name : getEventWinnerList
	 * @author : roy
	 * @date : 2016. 8. 31.
	 * @description : 당첨자 목록
	 *
	 * @param ses
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsEventgift> getEventWinnerList(SpsEventSearch ses) throws ServiceException {
		List<SpsEventgift> list = (List<SpsEventgift>) dao.selectList("sps.event.getEventWinnerList", ses);
		return list;
	}

	/**
	 * @Method Name : getEventjoinCntInfo
	 * @author : stella
	 * @date : 2016. 9. 9.
	 * @description : 이벤트 참여내역 카운트
	 *
	 * @param search
	 * @return SpsEventjoin
	 * @throws Exception
	 */
	public SpsEventjoin getEventjoinCntInfo(SpsEventSearch search) throws Exception {
		SpsEventjoin joinHistory = new SpsEventjoin();

		List<SpsEventjoin> joinList = (List<SpsEventjoin>) dao.selectList("sps.event.getEventjoinCntInfo", search);
		if (joinList.size() > 0) {
			joinHistory = joinList.get(0);
		} else {
			joinHistory.setTotalCount(0);
			joinHistory.setRunEventCount("0");
			joinHistory.setStopEventCount("0");
		}

		return joinHistory;
	}

	/**
	 * @Method Name : getEventjoinHistory
	 * @author : stella
	 * @date : 2016. 9. 9.
	 * @description : 이벤트 참여 내역
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<SpsEventjoin> getEventjoinHistory(SpsEventSearch search) throws Exception {
		return (List<SpsEventjoin>) dao.selectList("sps.event.getEventjoinHistory", search);
	}

	/**
	 * @Method Name : getEventbrand
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 브랜드별 이미지정보 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<SpsEvent> getBrandEventInfo(SpsEventSearch search) throws Exception {
		return (List<SpsEvent>) dao.selectList("sps.event.getBrandEventInfo", search);
	}

	/**
	 * FO 진행중인 모든 이벤트 조회
	 * 
	 * @Method Name : getRunAllEventList
	 * @author : stella
	 * @date : 2016. 10. 20.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@Cacheable(cacheName = "getRunAllEventList-cache")
	public List<SpsEvent> getRunAllEventList(SpsEventSearch search) throws Exception {
		return (List<SpsEvent>) dao.selectList("sps.event.getRunAllEventList", search);
	}

	/**
	 * FO 진행중인 생생 이벤트 조회
	 * 
	 * @Method Name : getExpEventList
	 * @author : stella
	 * @date : 2016. 11. 14.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<SpsEvent> getExpEventList(SpsEventSearch search) throws Exception {
		return (List<SpsEvent>) dao.selectList("sps.event.getExpEventList", search);
	}

	/**
	 * 제휴카드 등록
	 * 
	 * @Method Name : saveAffiliatecard
	 * @author : eddie
	 * @date : 2016. 10. 23.
	 * @description :
	 *
	 * @return
	 * @throws Exception
	 */
	public String saveAffiliatecard(MmsMemberZts zts) throws Exception {

		String result = "";

		// 카드 인증
		MmsAffiliatecard existCard = (MmsAffiliatecard) dao.selectOne("mms.member.getMemberCardAuth", zts);

		if (existCard != null && existCard.getAffiliatecardNo() != null) {
			result = "S";

			zts.setExpireMonthPlus(12);//12개월 만기
			zts.setMemGradeCd(BaseConstants.MEM_GRADE_CD_VIP);
			dao.update("mms.member.updateAffiliateCard", zts);

			// 세션 갱신 : vip로 설정
			FoLoginInfo info = (FoLoginInfo) FoSessionUtil.getLoginInfo();
			if (!BaseConstants.MEM_GRADE_CD_PRESTAGE.equals(info.getMemGradeCd())) {
				info.setMemGradeCd(BaseConstants.MEM_GRADE_CD_VIP);
			}

		} else {
			//fail
			result = "F";
		}

		return result;
	}

	/**
	 * 제휴 카드 등록 여부 체크
	 * 
	 * @Method Name : checkAffiliateCard
	 * @author : intune
	 * @date : 2016. 11. 2.
	 * @description :
	 *
	 * @param zts
	 * @return
	 * @throws Exception
	 */
	public String checkAffiliateCard(MmsMemberZts zts) throws Exception {

		// check 
		String cardNo = (String) dao.selectOne("mms.member.getMemberAffiliateCard", zts);

		String result = "";
		if (CommonUtil.isNotEmpty(cardNo)) {
			result = "F";// 이미 존재함
		} else {
			result = "S";// 등록가능
		}
		return result;
	}

	/**
	 * 신한 고운맘/KisPlus 카드 등록
	 * 
	 * @Method Name : saveAffiliatecardShinhan
	 * @author : eddie
	 * @date : 2016. 11. 2.
	 * @description :
	 *
	 * @param zts
	 * @return
	 * @throws Exception
	 */
	public String saveAffiliatecardShinhan(MmsMemberZts zts) throws Exception {

		String result = "S";
		zts.setAffiliatecardAuthDt(BaseConstants.SYSDATE);
		zts.setAffiliatecardExpireDt("20190430235959");//2019년 4월 고정
		zts.setAffiliatecardCd("AFFILIATECARD_CD.SHINHAN");
		zts.setAffiliatecardNo(BaseConstants.AFFILIATE_SHINHAN_CARDNO);
		zts.setMemGradeCd(BaseConstants.MEM_GRADE_CD_VIP);
		dao.updateOneTable(zts);

		// 세션 갱신 : vip로 설정
		FoLoginInfo info = (FoLoginInfo) FoSessionUtil.getLoginInfo();
		if (!BaseConstants.MEM_GRADE_CD_PRESTAGE.equals(info.getMemGradeCd())) {
			info.setMemGradeCd(BaseConstants.MEM_GRADE_CD_VIP);
		}
		return result;
	}

	/**
	 * 회원 다음 등급 예상
	 * 
	 * @Method Name : getMemberOrderAmt
	 * @author : intune
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public Map<String, Object> getNextGradeAmt(SpsEventSearch search) {

		//family : 100000미만
		//silver : 300000미만
		//gold : 600000미만
		//vip : 600000이상

		Map<String, Object> result = new HashMap<String, Object>();

		String myGradeCd = FoSessionUtil.getMemGradeCd();

		int myLevel = FoSessionUtil.getGradeLevel(myGradeCd);

		Long sum = (Long) dao.selectOne("mms.member.getMemberOrderSum", search);

		// 내등급이 웰컴이고 구매내역이 없으면 ..
		if (sum == null && "MEM_GRADE_CD.WELCOME".equals(myGradeCd)) {
			result.put("pGradeCd", "MEM_GRADE_CD.WELCOME");
			result.put("nextGrade", "MEM_GRADE_CD.FAMILY");
			result.put("remainAmt", 1l);
			result.put("totalAmt", 0l);

			return result;
		} else if (sum == null) {
			sum = 0l;
		}
		// 예상등급
		String pGradeCd = "";
		if (sum <= 0) {
			pGradeCd = "MEM_GRADE_CD.WELCOME";
		} else if (sum < 100000) {
			pGradeCd = "MEM_GRADE_CD.FAMILY";
		} else if (sum < 300000) {
			pGradeCd = "MEM_GRADE_CD.SILVER";
		} else if (sum < 600000) {
			pGradeCd = "MEM_GRADE_CD.GOLD";
		} else {
			pGradeCd = "MEM_GRADE_CD.VIP";
		}

		String next = FoSessionUtil.getNextMemGradeCd(pGradeCd);

		//다음 안내 등급
		int nextLevel = FoSessionUtil.getGradeLevel(next);

		//다음 안내 등급이 현재등급보다 낮으면 현재 등급으로 리턴
		boolean isLevelDown = false;
		if (myLevel > nextLevel) {
			next = myGradeCd;
			isLevelDown = true;
		}
		long remainAmt = 0;

		if ("MEM_GRADE_CD.FAMILY".equals(next)) {
			remainAmt = 1l;
		} else if ("MEM_GRADE_CD.SILVER".equals(next)) {
			remainAmt = 100000 - sum;
		} else if ("MEM_GRADE_CD.GOLD".equals(next)) {
			remainAmt = 300000 - sum;
		} else if ("MEM_GRADE_CD.VIP".equals(next)) {
			remainAmt = 600000 - sum;
			if (remainAmt <= 0) {
				remainAmt = 0l;
			}
		}
		result.put("pGradeCd", pGradeCd);
		result.put("nextGrade", next);
		result.put("remainAmt", remainAmt);
		result.put("totalAmt", sum);
		// 다음등급 남은금액
		return result;
	}

	/**
	 * @Method Name : getExpEventDetail
	 * @author : stella
	 * @date : 2016. 11. 14.
	 * @description : 생생테스터 상세
	 *
	 * @param search
	 * @return
	 */
	public List<SpsEventjoin> getExpEventDetail(SpsEventSearch search) throws Exception {
		return (List<SpsEventjoin>) dao.selectList("sps.event.getVividityEventDetail", search);
	}

	/**
	 * @Method Name : getEvent
	 * @author : ian
	 * @date : 2016. 11. 14.
	 * @description : 모바일 히스토리 event
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public SpsEvent getEvent(SpsEvent search) throws Exception {
		return (SpsEvent) dao.selectOneTable(search);
	}

	/**
	 * 이벤트 응모(신청)
	 * 
	 * @Method Name : saveEventjoin
	 * @author : stella
	 * @date : 2016. 11. 18.
	 * @description :
	 *
	 * @param join
	 * @return
	 * @throws Exception
	 */
	public int saveEventjoin(SpsEventjoin join) throws Exception {
		return dao.insert("sps.event.insertEventJoin", join);
	}

}
