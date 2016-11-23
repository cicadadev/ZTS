package gcp.sps.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsApplytarget;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsControlchannel;
import gcp.ccs.model.CcsControldevice;
import gcp.ccs.model.CcsControlmembergrade;
import gcp.ccs.model.CcsControlmembertype;
import gcp.ccs.model.CcsExcproduct;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.external.model.coupon.CampaignCoupon;
import gcp.external.model.coupon.TargetMember;
import gcp.mms.model.MmsMember;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsOrder;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsCoupondeal;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.search.SpsCouponIssueSearch;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.model.search.SpsDealSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import net.sf.json.JSONObject;

@Service("couponService")
public class CouponService extends BaseService {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CommonService	commonService;

	@Autowired
	private DealService		dealService;

	/**
	 * @Method Name : getCouponList
	 * @author : ian
	 * @date : 2016. 4. 20.
	 * @Description : 쿠폰 내역
	 *
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SpsCoupon> getCouponList(SpsCouponSearch couponSearch) {

		couponSearch.setStoreId(SessionUtil.getStoreId());
		List<SpsCoupon> resultList = new ArrayList<SpsCoupon>();
		List<SpsCoupon> list = (List<SpsCoupon>) dao.selectList("sps.coupon.getCouponList", couponSearch);
		for (SpsCoupon coupon : list) {
			if (coupon.getCcsBusiness() != null && !CommonUtil.isEmpty(coupon.getCcsBusiness().getName())
					&& !CommonUtil.isEmpty(coupon.getBusinessId())) {
				coupon.setBusinessInfo(coupon.getCcsBusiness().getName() + " (" + coupon.getBusinessId() + ")");
			}
			resultList.add(coupon);
		}

		return resultList;
	};

	/**
	 * @Method Name : deleteCoupon
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 대기상태인 쿠폰프로모션 삭제
	 *
	 * @param list
	 * @throws Exception
	 */
	public void deleteCoupon(List<SpsCoupon> list) throws Exception {
		String storeId = SessionUtil.getStoreId();
		for (SpsCoupon coupon : list) {
			String couponId = coupon.getCouponId();
			BigDecimal applyNo = coupon.getApplyNo();
			BigDecimal controlNo = coupon.getControlNo();

			logger.debug(" \t applyNo >> " + applyNo);
			logger.debug(" \t controlNo >> " + controlNo);

			/*
			 * 1. 쿠폰 하위 테이블 삭제
			 * 	- sps_coupondeal
			 * 	- sps_couponissue
			 * 
			 * 2. 쿠폰 삭제
			 * 
			 * 3. 적용대상, 제한설정 정보 삭제
			 * 	- ccs_apply, ccs_applytarget, ccs_excproducts
			 * 	- ccs_control, ccs_controlchannel, ccs_controldevice, ccs_controlmembergrade, ccs_controlmembertype
			 * */

			// 1. 쿠폰 하위 테이블 삭제
			// 1-1 sps_coupondeal 삭제
			if ("Y".equals(coupon.getDealApplyYn())) {
				SpsCoupondeal deal = new SpsCoupondeal();
				deal.setStoreId(storeId);
				deal.setCouponId(couponId);
				dao.delete("sps.coupon.deleteCouponDeal", deal);
			}
			// 1-2 sps_couponissue 삭제
			SpsCouponissue issue = new SpsCouponissue();
			issue.setStoreId(storeId);
			issue.setCouponId(couponId);
			dao.delete("sps.coupon.deleteIssueCoupon", issue);

			// 2. 쿠폰 삭제
			coupon.setStoreId(storeId);
			dao.deleteOneTable(coupon);

			// 3. 설정정보 삭제
			// 3-1 적용대상 설정 삭제
			if (applyNo != null) {
				CcsApplytarget target = new CcsApplytarget();
				CcsExcproduct product = new CcsExcproduct();
				CcsApply apply = new CcsApply();

				// 적용대상 삭제
				target.setStoreId(storeId);
				target.setApplyNo(applyNo);
				commonService.deleteApplytargetByApplyNoNewTx(target);

				// 제외상품 삭제
				product.setStoreId(storeId);
				product.setApplyNo(applyNo);
				commonService.deleteExcproductByApplyNoNewTx(product);

				// 적용대상 마스터 삭제
				apply.setStoreId(storeId);
				apply.setApplyNo(applyNo);
				dao.deleteOneTable(apply);
			}

			// 3-2 제한설정 삭제
			if (controlNo != null) {
				CcsControl control = new CcsControl();
				CcsControlchannel channel = new CcsControlchannel();
				CcsControldevice device = new CcsControldevice();
				CcsControlmembergrade grade = new CcsControlmembergrade();
				CcsControlmembertype type = new CcsControlmembertype();
				
				// 채널 삭제
				channel.setStoreId(storeId);
				channel.setControlNo(controlNo);
				commonService.deleteChannelByControlNo(channel);

				// 디바이스 삭제
				device.setStoreId(storeId);
				device.setControlNo(controlNo);
				commonService.deleteDeviceByControlNo(device);
				
				// 회원등급 삭제
				grade.setStoreId(storeId);
				grade.setControlNo(controlNo);
				commonService.deleteMemberGradeByControlNo(grade);
				
				// 회원유형 삭제
				type.setStoreId(storeId);
				type.setControlNo(controlNo);
				commonService.deleteMemberTypeByControlNo(type);
				
				// 제한설정 마스터 삭제
				control.setStoreId(storeId);
				control.setControlNo(controlNo);
				dao.deleteOneTable(control);
			}

		}
	}

	/**
	 * @Method Name : getCouponIssueCount
	 * @author : ian
	 * @date : 2016. 6. 9.
	 * @description : 쿠폰 총 발급 매수 & 총 사용(USE)
	 *
	 * @param spsCouponSearch
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getCouponIssueState(String couponId, BigDecimal memberNo) throws Exception {

		BigDecimal totalCnt = BigDecimal.ZERO;
		BigDecimal issueCnt = BigDecimal.ZERO;	// 미등록
		BigDecimal regCnt = BigDecimal.ZERO;	// 등록
		BigDecimal useCnt = BigDecimal.ZERO;	// 사용
		BigDecimal stopCnt = BigDecimal.ZERO;	// 중지
		BigDecimal privateCinCnt = BigDecimal.ZERO;	// 인증번호 사용으로 발급된 쿠폰

		SpsCouponSearch spsCouponSearch = new SpsCouponSearch();
		spsCouponSearch.setStoreId(SessionUtil.getStoreId());
		spsCouponSearch.setCouponId(couponId);
		if (memberNo != null) {
			spsCouponSearch.setMemberNo(memberNo);
		}

		/*
		 * if(
		 * 
		 * 
		 * */
		List<SpsCouponissue> list = (List<SpsCouponissue>) dao.selectList("sps.coupon.getCouponIssueState", spsCouponSearch);

		for (SpsCouponissue issue : list) {
			String code = issue.getCouponIssueStateCd();
			if ("COUPON_ISSUE_STATE_CD.ISSUE".equals(code)) {
				issueCnt = issue.getCount();
			} else if ("COUPON_ISSUE_STATE_CD.REG".equals(code)) {
				regCnt = issue.getCount();
			} else if ("COUPON_ISSUE_STATE_CD.USE".equals(code)) {
				useCnt = issue.getCount();
			} else if ("COUPON_ISSUE_STATE_CD.STOP".equals(code)) {
				stopCnt = issue.getCount();
			} else if ("PRIVATE_CIN".equals(code)) {
				privateCinCnt = issue.getCount();
			} else {
				totalCnt = issue.getCount();
			}
		}

		String rtnVal = totalCnt + "," + issueCnt + "," + regCnt + "," + useCnt + "," + stopCnt + "," + privateCinCnt;

		return rtnVal;
	}

	/**
	 * @Method Name : duplicateCheck
	 * @author : pual
	 * @date : 2016. 6. 8.
	 * @description : 인증 번호 중복 검사
	 *
	 * @param spsCouponIssueSearch
	 * @return
	 * @throws Exception
	 */
	public String duplicateCheck(SpsCouponIssueSearch spsCouponIssueSearch) throws Exception {

		String rtnValStr = "NOT";//사용가능

		int rtnVal = (int) dao.selectOne("sps.coupon.getDuplicateCheck", spsCouponIssueSearch);

		if (rtnVal > 0) {
			rtnValStr = "USED";//사용불가
		}

		return rtnValStr;
	}

	/**
	 * @Method Name : getCouponDetail
	 * @author : ian
	 * @date : 2016. 6. 9.
	 * @description : 쿠폰상세
	 *
	 * @param spsCoupon
	 * @return
	 * @throws ServiceException
	 */
	public SpsCoupon getCouponDetail(SpsCoupon spsCoupon) throws ServiceException {
		SpsCoupon coupon = (SpsCoupon) dao.selectOne("sps.coupon.getCouponDetail", spsCoupon);
		String targetTypeCd = coupon.getCcsApply().getTargetTypeCd();

		List<CcsApplytarget> targets = null;
		List<CcsExcproduct> exProducts = null;

		BigDecimal applyNo = coupon.getApplyNo();
		if (BaseConstants.TARGET_TYPE_CD_ALL.equals(targetTypeCd)) {
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		} else if (BaseConstants.TARGET_TYPE_CD_PRODUCT.equals(targetTypeCd)) {
			targets = commonService.getCcsApplytargetPrByApplyNo(applyNo);
		} else if (BaseConstants.TARGET_TYPE_CD_CATEGORY.equals(targetTypeCd)) {
			targets = commonService.getCcsApplytargetCaByApplyNo(applyNo);
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		} else if (BaseConstants.TARGET_TYPE_CD_BRAND.equals(targetTypeCd)) {
			targets = commonService.getCcsApplytargetBrByApplyNo(applyNo);
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		}

		if (targets != null) {
			coupon.getCcsApply().setCcsApplytargets(targets);
		}
		if (exProducts != null) {
			coupon.getCcsApply().setCcsExcproducts(exProducts);
		}

		return coupon;
	};


	/**
	 * @Method Name : createCoupon
	 * @author : ian
	 * @date : 2016. 6. 9.
	 * @description : 쿠폰 생성(insert)
	 *
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public String createCoupon(SpsCoupon coupon) throws Exception {

		// 제한설정
		// 1. [신규]제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 2. [신규]전체허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass
		// 99. [복사]제한 : controlNo(not null), ccsControl(null), isAllPermit(N), isMakeModel(null)
		// 0. 허용설정
		BigDecimal newControlNo = null;
		boolean isControlSet = coupon.getCcsControl() != null && "N".equals(coupon.getIsAllPermit());
		boolean isMakeModel = coupon.getCcsControl() == null && "N".equals(coupon.getIsAllPermit()) && CommonUtil.isEmpty(coupon.getIsMakeModel());

		if(isMakeModel) {
			CcsControl ccsControl =  new CcsControl();
			ccsControl.setStoreId(coupon.getStoreId());
			ccsControl.setControlNo(coupon.getControlNo());
			ccsControl.setUpdId(coupon.getUpdId());
			newControlNo = commonService.updateControlByCopyReg(ccsControl);
		}
		
		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(null, coupon.getCcsControl());
		}
		if (newControlNo != null) {
			coupon.setControlNo(newControlNo);
		}
		

		// 쿠폰 프로모션 등록
		String storeId = SessionUtil.getStoreId();
		coupon.setStoreId(storeId);
//		dao.insert("sps.coupon.insertCoupon", coupon);
		dao.insertOneTable(coupon);
		String couponId = coupon.getCouponId();

		// 쿠폰 허용 딜
		if ("Y".equals(coupon.getDealApplyYn())) {
			insDelDeal(coupon, true);
		}

		// 쿠폰 사용 대상 마스터 저장
		CcsApply apply = new CcsApply();
		apply.setStoreId(coupon.getStoreId());
		apply.setTargetTypeCd(coupon.getCcsApply().getTargetTypeCd());
		commonService.insertApply(apply);
		coupon.setApplyNo(apply.getApplyNo());			// 적용대상번호 취득

		// coupon 테이블 제어번호, 적용대상번호 update
		dao.updateOneTable(coupon);

		// 쿠폰발급
		// 		- 인증번호 미사용시 발급내역 탭에서 수동발급 or 일괄등록
		if ("Y".equals(coupon.getPublicCin())) {
			this.createCouponIssue(coupon);
		}
		
		if (coupon.getCcsApply().getCcsApplytargets() != null) {
			apply.setCcsApplytargets(coupon.getCcsApply().getCcsApplytargets());
		}
		if (coupon.getCcsApply().getCcsExcproducts() != null) {
			apply.setCcsExcproducts(coupon.getCcsApply().getCcsExcproducts());
		}
		commonService.saveCcsApply(apply);
		

		return couponId;
	}

	/**
	 * @Method Name : updateCoupon
	 * @author : ian
	 * @date : 2016. 6. 15.
	 * @description : 쿠폰 수정
	 *
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public String updateCoupon(SpsCoupon coupon) throws Exception {
		/*  
		 * - 쿠폰 마스터 update
		 * 
		 * - 제한유형	(clear)
		
			3. [수정]전체허용 -> 제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
			4. [수정]전체허용 -> 허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass
			5. [수정]제한 -> 허용 : controlNo(not null), ccsControl(null), isAllPermit(Y) => ccs_control 삭제
			6. [수정]제한 -> 제한(내용변경 없음) : controlNo(not null), ccsControl(null), isAllPermit(N) => by pass
			7. [수정]제한 -> 제한(내용변경 있음) : controlNo(not null), ccsControl(not null), isAllPermit(N)
		 * 
		 * - 인증번호 미사용에서 사용
		 * 
		 * - 딜 전문관 적용
		 * 		- 추가 : spsCoupondeal 에 추가
		 * 		- 삭제 : spsCoupondeal 에 삭제
		 * 
		 * - 쿠폰사용대상 저장
		 * */



		String storeId = SessionUtil.getStoreId();

		// 0. 허용설정
		BigDecimal newControlNo = null;
		BigDecimal deleteControlNo = null;
		boolean isControlDelete = coupon.getControlNo() != null && "Y".equals(coupon.getIsAllPermit());
		boolean isControlSet = coupon.getCcsControl() != null && "N".equals(coupon.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(coupon.getControlNo(), coupon.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = coupon.getControlNo();
			coupon.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			coupon.setControlNo(newControlNo);
		}
		
		// 딜전문관 사용 --> 미사용 // 미사용 --> 사용
		this.insDelDeal(coupon, false);

		// 쿠폰 발급 현황
		String cnt = this.getCouponIssueState(coupon.getCouponId(), null);
		String[] cntArr = cnt.split(",");
		// 0 전체 1 미등록 2 등록 3 사용 4 중지 5 인증번호 사용유무
		BigDecimal totalCnt = new BigDecimal(cntArr[0]);
		BigDecimal issueCnt = new BigDecimal(cntArr[1]);
		
		// 쿠폰 발급 수정 모델 세팅
		SpsCouponissue issue = new SpsCouponissue();
		issue.setStoreId(storeId);
		issue.setCouponId(coupon.getCouponId());

		/*
		 * 
		 * 1. 진행중 : 사용
		 * 			- 최대발급매수 증가
		 * 			- 최대발급매수 감소
		 * 
		 * 2. 진행중 : 미사용
		 * 			- 최대발급매수 변화 영향없음 bypass
		 * 
		 * */

		// BigDecimal -1 작다, 0 같다, 1 크다
		if ("COUPON_STATE_CD.READY".equals(coupon.getCouponStateCd())) {
			// 인증번호 사용
			if ("Y".equals(coupon.getPublicCin())) {
				logger.debug("\t total coupon count >> " + totalCnt);
				logger.debug("\t current coupon count >> " + coupon.getMaxIssueQty());
				
				// 인증쿠폰 - 단일 경우 인증번호 변경 여부 체크
				String certiCode = "USED";
				if("COUPON_ISSUE_TYPE_CD.PUBLIC".equals(coupon.getCouponIssueTypeCd())) {
					SpsCouponIssueSearch issueSearch = new SpsCouponIssueSearch();
					issueSearch.setStoreId(coupon.getStoreId());
					issueSearch.setPrivateCin(coupon.getPrivateCin());
					certiCode = this.duplicateCheck(issueSearch);
				}
				
				// 발급매수 증가/감소 있음  or  인증쿠폰(단일) -> 번호 변경 있음
				if (totalCnt.compareTo(coupon.getMaxIssueQty()) != 0 || "NOT".equals(certiCode)) {
					dao.delete("sps.coupon.deleteIssueCoupon", issue);

					// 발급매수 증감분으로 발행
					this.createCouponIssue(coupon);
				}
			}
			// 인증번호 미사용
			else {
				// 미사용인데 발행 쿠폰 있을 경우
				if (totalCnt.compareTo(BigDecimal.ZERO) == 1) {
					dao.delete("sps.coupon.deleteIssueCoupon", issue);
				}
			}
		} else if ("COUPON_STATE_CD.RUN".equals(coupon.getCouponStateCd())) {
			// 인증번호 사용
			if ("Y".equals(coupon.getPublicCin())) {
				// 증가
				if (totalCnt.compareTo(coupon.getMaxIssueQty()) < 0) {
					//변경된 발급매수 - 전체 발급량 = 증가분
					
					BigDecimal originMaxIssueQty = coupon.getMaxIssueQty();
					logger.debug("\t current issue coupon cnt >> " + coupon.getMaxIssueQty());
					logger.debug("\n\t current issue coupon cnt >> " + coupon.getMaxIssueQty().subtract(totalCnt));
					
					coupon.setMaxIssueQty(coupon.getMaxIssueQty().subtract(totalCnt));
					this.createCouponIssue(coupon);
					
					coupon.setMaxIssueQty(originMaxIssueQty);
				}
				// 감소
				else if (totalCnt.compareTo(coupon.getMaxIssueQty()) > 0) {
					// 현재 미등록 쿠폰이 감소분보다 같거나 클 때
					if (issueCnt.compareTo(totalCnt.subtract(coupon.getMaxIssueQty())) >= 0) {
						issue.setDeleteType("ISSUE");
						BigDecimal minus = totalCnt.subtract(coupon.getMaxIssueQty());
						issue.setCount(minus);
						
						dao.delete("sps.coupon.deleteIssueCoupon", issue);
					} else {
						// TODO
						// 감소분보다 줄일수 있는 양 적음
						// 스크립트에서 처리중
					}
				}
			}
		}

		//적용대상 저장
		CcsApply ccsApply = coupon.getCcsApply();
		ccsApply.setStoreId(storeId);
		BigDecimal applyNo = commonService.saveCcsApply(ccsApply);

		coupon.setApplyNo(applyNo);

		// 쿠폰 마스터 수정
		coupon.setUpdId(SessionUtil.getLoginId());
		
		if("DC_APPLY_TYPE_CD.AMT".equals(coupon.getDcApplyTypeCd())) {
			if("COUPON_TYPE_CD.PRODUCT".equals(coupon.getCouponTypeCd()) || "COUPON_TYPE_CD.PLUS".equals(coupon.getCouponTypeCd()) ) {
				coupon.setMaxDcAmt(null);
			}
		}
			
//		dao.updateOneTable(coupon);
		dao.update("sps.coupon.updateCoupon",coupon);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}

		return coupon.getCouponId();
	}

	/**
	 * @Method Name : couponStop
	 * @author : ian
	 * @date : 2016. 6. 23.
	 * @description : 쿠폰 정지
	 *
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public String updateCouponState(SpsCoupon coupon) throws Exception {
		coupon.setStoreId(SessionUtil.getStoreId());
		coupon.setUpdId(SessionUtil.getLoginId());
		dao.update("sps.coupon.updateCouponState", coupon);

		return coupon.getCouponId();
	}

	/**
	 * @Method Name : delControl
	 * @author : ian
	 * @date : 2016. 6. 15.
	 * @description : 제한설정 sub 테이블 삭제
	 *
	 * @param coupon
	 * @throws Exception
	 */
	public void delControl(SpsCoupon coupon) throws Exception {

		CcsControlchannel controlChannel = new CcsControlchannel();
		controlChannel.setStoreId(SessionUtil.getStoreId());
		controlChannel.setControlNo(coupon.getControlNo());
		dao.delete("ccs.common.deleteControlChannel", controlChannel);

		CcsControlmembertype controlMemType = new CcsControlmembertype();
		controlMemType.setStoreId(SessionUtil.getStoreId());
		controlMemType.setControlNo(coupon.getControlNo());
		dao.delete("ccs.common.deleteControlMemberType", controlMemType);

		CcsControlmembergrade controlMemGrade = new CcsControlmembergrade();
		controlMemGrade.setStoreId(SessionUtil.getStoreId());
		controlMemGrade.setControlNo(coupon.getControlNo());
		dao.delete("ccs.common.deleteControlMemberGrade", controlMemGrade);

		CcsControldevice controlDevice = new CcsControldevice();
		controlDevice.setStoreId(SessionUtil.getStoreId());
		controlDevice.setControlNo(coupon.getControlNo());
		dao.delete("ccs.common.deleteControlDevice", controlDevice);
	}

	/**
	 * @Method Name : insDelDeal
	 * @author : ian
	 * @date : 2016. 6. 15.
	 * @description : 쿠폰 허용딜 insert & delete
	 *
	 * @param coupon
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void insDelDeal(SpsCoupon coupon, boolean insert) throws Exception {

		/* 
		 * Update시 case
		 * 
		 * 1. 적용(Y) -> 적용(Y) : 기존 spsdeal table 삭제, 재insert
		 * 2. 적용(Y) -> 미적용(N) : 기존 spsdeal table 삭제
		 * 3. 미적용(N) -> 적용(Y) : insert
		 * 4. 미적용(N) -> 적용(N) : NOT THING
		 * 
		 * */

		if (!insert) {
			dao.delete("sps.coupon.deleteCouponDeal", coupon);
		}
		
		if ("Y".equals(coupon.getDealApplyYn())) {
			List<SpsCoupondeal> dealList = (List<SpsCoupondeal>) dao.selectList("sps.coupon.getCouponDeal", coupon);
			for (SpsCoupondeal deal : dealList) {
				deal.setCouponId(coupon.getCouponId());
				dao.insertOneTable(deal);
			}
		}
	}

	/**
	 * @Method Name : createCouponIssue
	 * @author : ian
	 * @date : 2016. 5. 24.
	 * @Description : 쿠폰 발급 (인증코드 사용시)
	 *
	 * @param baseSpsCouponissue
	 * @param coupon
	 * @throws Exception
	 */
	public void createCouponIssue(SpsCoupon coupon) throws Exception {

		//최대발급수량제한
		int issueval = ((BigDecimal) coupon.getMaxIssueQty()).intValue();
		String couponIssueTypeCd = coupon.getCouponIssueTypeCd();

		// 현재 쿠폰 발행수
		String cnt = this.getCouponIssueState(coupon.getCouponId(), null);
		String[] cntArr = cnt.split(",");
		// 0 전체 1 미등록 2 등록 3 사용 4 중지
		BigDecimal totalCnt = new BigDecimal(cntArr[0]);
		
		// bigdecimal 비교 -1 작다, 0 같다, 1 크다
		if (totalCnt.compareTo(coupon.getMaxIssueQty()) < 0) {
			// 인증번호사용 Y
			if ("Y".equals(coupon.getPublicCin())) {
				SpsCouponissue spsCouponissue = new SpsCouponissue();
				spsCouponissue.setCouponId(coupon.getCouponId());
				spsCouponissue.setStoreId(SessionUtil.getStoreId());
				spsCouponissue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.ISSUE");
				spsCouponissue.setInsId(coupon.getUpdId());
				spsCouponissue.setUpdId(coupon.getUpdId());

				
				String xml = "";
				// 단일 _ PUBLIC _ 개별인증번호 하나에 다수의 사용자
				if (BaseConstants.COUPON_ISSUE_TYPE_CD_PUBLIC.equals(couponIssueTypeCd)) {
					spsCouponissue.setPrivateCin(coupon.getPrivateCin());
					xml = "sps.coupon.issuePublicCoupon";
				}
				// 복수 _ PRIVATE _ 각 개별인증번호에 한명의 사용자
				else if (BaseConstants.COUPON_ISSUE_TYPE_CD_PRIVATE.equals(couponIssueTypeCd)) {
					xml = "sps.coupon.issuePrivateCoupon";
				}
				
				int share = issueval / 1000;
				int remain = issueval % 1000;
				// 1. 총 발급매수를 1000회씩 끊어서 insert
				{
					if(share > 0) {
						for (int i=0; i< share; i++) {
							Map<String, List<SpsCouponissue>> map = new HashMap<String, List<SpsCouponissue>>();
							List<SpsCouponissue> issues = new ArrayList<SpsCouponissue>();
							for (int j = 0; j < 1000; j++) {
								issues.add(spsCouponissue);
							}
							map.put("issues", issues);
							
							dao.insert(xml, map);
						}
					}
				}	// 1. 발급매수 / 1000 나눈 몫 insert		
				
				// 2. 나머지 총 발급매수 insert
				{
					if(remain > 0) {
						Map<String, List<SpsCouponissue>> map2 = new HashMap<String, List<SpsCouponissue>>();
						List<SpsCouponissue> issues2 = new ArrayList<SpsCouponissue>();
						
						for (int i=0; i< remain; i++) {
							issues2.add(spsCouponissue);
						}
						
						map2.put("issues", issues2);
						dao.insert(xml, map2);
					}
				}	// end 2. 발급매수 / 1000 나눈 나머지 수량 insert
				
			}	// end 인증쿠폰 여부
		}
	}

	/**
	 * @Method Name : getCouponIssuedList
	 * @author : ian
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰 발급 내역
	 *
	 * @param spsCouponIssueSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsCouponissue> getCouponIssuedList(SpsCouponIssueSearch spsCouponIssueSearch) {
		List<SpsCouponissue> resultList = new ArrayList<SpsCouponissue>();
		List<SpsCouponissue> list = (List<SpsCouponissue>) dao.selectList("sps.coupon.getCouponIssuedList", spsCouponIssueSearch);
		for (SpsCouponissue couponIssue : list) {
			if (couponIssue.getMmsMember() != null) {
				if (StringUtils.isNotEmpty(couponIssue.getMmsMember().getMemberName())) {
					couponIssue.setMemberInfo(
							couponIssue.getMmsMember().getMemberName() + " (" + couponIssue.getMmsMember().getMemberId() + ")");
				}
			}
			resultList.add(couponIssue);
		}
		return resultList;
	}

	/**
	 * @Method Name : stopCouponIssue
	 * @author : ian
	 * @date : 2016. 6. 21.
	 * @description : 발급 쿠폰 사용 중지
	 *
	 * @param issues
	 * @throws Exception
	 */
	public void stopCouponIssue(List<SpsCouponissue> issues) throws Exception {
		for (SpsCouponissue couponIssue : issues) {
			couponIssue.setStoreId(SessionUtil.getStoreId());
			couponIssue.setUpdId(SessionUtil.getLoginId());
			couponIssue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.STOP");
			dao.updateOneTable(couponIssue);
		}
	}

	/**
	 * @Method Name : issueCoupon
	 * @author : ian
	 * @date : 2016. 6. 21.
	 * @description : 쿠폰 발급
	 *
	 * @param issues memberNo 필수<br/>
	 *            controllCheckType : 허용설정체크 유형<br/>
	 *            BO : admin 수동발급, 배치발급<br/>
	 *            FO : front에서 회원에게발급<br/>
	 *            FO_DN : front상품다운로드 쿠폰 발급
	 * 
	 * @param check 회원에게 발급된 쿠폰 상태 확인 유무
	 * @return resultCode <br/>
	 *         0000 : 발급성공<br/>
	 *         0001 : 총 발급수량 초과<br/>
	 *         0002 : 인당 발급수량 초과<br/>
	 *         0003 : 유효하지 않은 쿠폰<br/>
	 *         0010 : 적용 대상 아님(허용설정체크실패)<br/>
	 *         0100 : 회원 정보 쿠폰 발급기준 미 충족<br/>
	 *         resultMsg 결과메세지
	 * @throws ServiceException
	 */
	public Map<String, String> issueCoupon(SpsCouponissue issue, boolean check) throws Exception {

	
		Map<String, String> resultMap = new HashMap<String, String>();
		String resultCode = "";
		String storeId = SessionUtil.getStoreId();

		String cnt = "";
		String[] cntArr = null;
		BigDecimal totalCnt = null;

		// 필수 parameter 확인 : 회원번호, 쿠폰번호
		if (CommonUtil.isEmpty(issue.getMemberNo()) || CommonUtil.isEmpty(issue.getCouponId())) {
			resultMap.put("resultCode", "0100");
			resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.issue.0100"));
			return resultMap;
		}
		
		// 쿠폰 정보 조회
		SpsCoupon coupon = new SpsCoupon();
		coupon.setStoreId(storeId);
		coupon.setCouponId(issue.getCouponId());
		coupon.setCampaignYn(issue.getCampaignYn());
		coupon = (SpsCoupon) dao.selectOne("sps.coupon.getIssueableCoupon", coupon);

		if (coupon != null) {

			//허용설정 체크값이 존재할경우 허용설정 체크
			if (CommonUtil.isNotEmpty(issue.getControllCheckType())) {
				// 허용설정 체크
				CcsControlSearch search = new CcsControlSearch();
				List<String> memberTypeCds = new ArrayList<String>();

				MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
				mmsMemberSearch.setMemberNo(issue.getMemberNo());
				MmsMember member = (MmsMember) dao.selectOne("mms.member.getMemberLogin", mmsMemberSearch);
				if ("Y".equals(member.getPremiumYn())) {
					memberTypeCds.add("MEMBER_TYPE_CD.PREMIUM");
				}
				if ("Y".equals(member.getMmsMemberZts().getMembershipYn())) {
					memberTypeCds.add("MEMBER_TYPE_CD.MEMBERSHIP");
				}
				if ("Y".equals(member.getEmployeeYn())) {
					memberTypeCds.add("MEMBER_TYPE_CD.EMPLOYEE");
				}
				if ("Y".equals(member.getMmsMemberZts().getChildrenYn())) {
					memberTypeCds.add("MEMBER_TYPE_CD.CHILDREN");
				}
				if ("Y".equals(member.getMmsMemberZts().getB2eYn())) {
					memberTypeCds.add("MEMBER_TYPE_CD.B2E");
				}

				if (memberTypeCds.size() == 0) {
					memberTypeCds.add("MEMBER_TYPE_CD.GENERAL");
				}

				if ("BO".equals(issue.getControllCheckType())) {

					//어드민 수동발급일경우( 디바이스, 채널 체크안함)
					search.setControlType("COUPON_BO");
					search.setMemberTypeCds(memberTypeCds);
					search.setMemGradeCd(member.getMmsMemberZts().getMemGradeCd());
				} else if ("FO".equals(issue.getControllCheckType())) {

					// 프론트 쿠폰발행 ( 전체체크)
					search.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd());
					search.setChannelId(SessionUtil.getChannelId());
					search.setMemberTypeCds(memberTypeCds);
					search.setMemGradeCd(member.getMmsMemberZts().getMemGradeCd());
				} else if ("FO_DN".equals(issue.getControllCheckType())) {

					//프론트 다운로드 쿠폰(회원체크안함)
					search.setControlType("COUPON");
					search.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd());
				}
				search.setControlNo(coupon.getControlNo());
				search.setStoreId(BaseConstants.STORE_ID);
				boolean isPass = commonService.checkControl(search);

				if (!isPass) {
					resultMap.put("resultCode", "0010");
					resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.issue.0010"));
					return resultMap;
				}
			}

			// 쿠폰 발급
			issue.setStoreId(storeId);

			String today = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
			issue.setRegDt(today);

			// 쿠폰 인당 발급 기준 코드
			issue.setMemIssueBasisCd(coupon.getMemIssueBasisCd());

//			// 캠페인쿠폰 사용 시작 일자 임시저장
//			String campaignUseStartDt = "";
//			if(BaseConstants.YN_Y.equals(issue.getCampaignYn())) {
//				if(CommonUtil.isNotEmpty(issue.getUseStartDt())) {
//					campaignUseStartDt = issue.getUseStartDt(); 
//				}
//			}
			
			// 사용가능 시작/종료일 설정
			Map<String, String> date = setInvalidDateIssueCoupon(coupon, today);
			issue.setUseStartDt(date.get("start"));
			issue.setUseEndDt(date.get("end"));
			
//			if (BaseConstants.TERM_TYPE_CD_DAYS.equals(coupon.getTermTypeCd())) {
//				// 사용가능일수
//				BigDecimal days = coupon.getTermDays();
//				issue.setUseStartDt(today);
//				issue.setUseEndDt(DateUtil.getAddDate(DateUtil.FORMAT_1, today, days));
//			} else if (BaseConstants.TERM_TYPE_CD_TERM.equals(coupon.getTermTypeCd())) {
//				// 사용시작일, 사용종료일
//				issue.setUseStartDt(coupon.getTermStartDt());
//				issue.setUseEndDt(coupon.getTermEndDt());
//			} else if (BaseConstants.TERM_TYPE_CD_LASTDAY.equals(coupon.getTermTypeCd())) {
//				// 해당 월의 마지막 일
//				issue.setUseStartDt(today);
//				issue.setUseEndDt(DateUtil.getMonthLastDay(DateUtil.FORMAT_1, null));
//			} else if (BaseConstants.TERM_TYPE_CD_WEEK.equals(coupon.getTermTypeCd())) {
//				// 해당주의 일요일
//				issue.setUseStartDt(today);
//				issue.setUseEndDt(DateUtil.getWeekStartEndDay(DateUtil.FORMAT_1, today, false));
//			}
			
//			// 캠페인시작일자와 실제 사용일자 비교
//			if(CommonUtil.isNotEmpty(campaignUseStartDt)) {
//				int resultDt =  DateUtil.compareToDate(campaignUseStartDt, issue.getUseStartDt(), DateUtil.FORMAT_1);
//				if(resultDt > 0) {
//					issue.setUseStartDt(campaignUseStartDt);
//				}
//			}

			// 1. 최대 쿠폰 발행수
			BigDecimal maxCouponIssue = coupon.getMaxIssueQty();
			BigDecimal maxMemIssueQty = coupon.getMaxMemIssueQty();

			// 현재 쿠폰 발행수
			cnt = this.getCouponIssueState(coupon.getCouponId(), null);

			// 0 전체 1 미등록 2 등록 3 사용 4 중지
			cntArr = cnt.split(",");
			totalCnt = new BigDecimal(cntArr[0]);

			// 인증쿠폰의 경우 최대 발급량은 쿠폰 마스터 생성시 이미 도달
			if (totalCnt.compareTo(maxCouponIssue) == 0
					&& BaseConstants.COUPON_ISSUE_TYPE_CD_SYSTEM.equals(coupon.getCouponIssueTypeCd())) {

				resultCode = BaseConstants.COUPON_ISSUE_RESULT_MAX;

			} else {

				// 2. 인당 발급수
				BigDecimal memCnt = (BigDecimal) dao.selectOne("sps.coupon.getMemIssueUseCount", issue);

				if (memCnt.compareTo(maxMemIssueQty) == -1) {

					issue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.REG");

					// 인증번호 미사용
					if (CommonUtil.isEmpty(issue.getPrivateCin())) {
						dao.insertOneTable(issue);
					}
					// 인증번호 사용
					else {
						dao.update("sps.coupon.issuePrivateCinCoupon", issue);
					}

					resultCode = BaseConstants.COUPON_ISSUE_RESULT_SUCCESS;//성공

				}
				// 3. 인당 발급량 
				else {
					// 3-1. 최대발급 수량 도달시 회원의 유효 쿠폰 확인
					if (check) {
						cnt = this.getCouponIssueState(coupon.getCouponId(), issue.getMemberNo());
						// 0 전체 1 미등록 2 등록 3 사용 4 중지
						cntArr = cnt.split(",");

						BigDecimal usableCouponCnt = new BigDecimal(cntArr[2]);
						// 0 보다 크다
						if (usableCouponCnt.compareTo(BigDecimal.ZERO) == 1) {
							resultCode = "0000";//TODO 성공???
						}
						// 0 보다 작거나 같다
						else {
							resultCode = BaseConstants.COUPON_ISSUE_RESULT_PERSON_MAX;
						}
					}
					// 3-2. 인당 발급 수량 초과
					else {
						resultCode = BaseConstants.COUPON_ISSUE_RESULT_PERSON_MAX;
					}
				}
			}

		} else {
			// 유효하지 않은 쿠폰
			resultCode = BaseConstants.COUPON_ISSUE_RESULT_INVALID;
		}

		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.issue." + resultCode));
//		if (!BaseConstants.COUPON_ISSUE_RESULT_SUCCESS.equals(resultCode)) {
//		}

		return resultMap;
	}

	/**
	 * @Method Name : setInvalidDateIssueCoupon
	 * @author : ian
	 * @date : 2016. 11. 2.
	 * @description : 쿠폰 발행시 유효기간 설정
	 *
	 * @param coupon
	 * @param today
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> setInvalidDateIssueCoupon(SpsCoupon coupon, String today) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String start = "";
		String end = "";
		
		// 사용가능 시작/종료일 설정
		if(BaseConstants.TERM_TYPE_CD_TERM.equals(coupon.getTermTypeCd())) {
			// 사용시작일, 사용종료일
			start = coupon.getTermStartDt();
			end = coupon.getTermEndDt();			
		} else {
			start = today;
			if (BaseConstants.TERM_TYPE_CD_DAYS.equals(coupon.getTermTypeCd())) {
				// 사용가능일수
				BigDecimal days = coupon.getTermDays();
				end = DateUtil.getAddDate(DateUtil.FORMAT_1, today, days);
			} else if (BaseConstants.TERM_TYPE_CD_LASTDAY.equals(coupon.getTermTypeCd())) {
				// 해당 월의 마지막 일
				end = DateUtil.getMonthLastDay(DateUtil.FORMAT_1, null);
			} else if (BaseConstants.TERM_TYPE_CD_WEEK.equals(coupon.getTermTypeCd())) {
				// 해당주의 일요일
				end = DateUtil.getWeekStartEndDay(DateUtil.FORMAT_1, today, false);
			}
		}
		
		map.put("start", start);
		map.put("end", end);
		
		return map; 
	}
	
	/**
	 * @Method Name : issueCoupon
	 * @author : ian
	 * @date : 2016. 6. 21.
	 * @description : 쿠폰 발급 가능 여부 체크 및 발급
	 *
	 * @param issues
	 * @return
	 * @throws ServiceException
	 */
	public List<SpsCoupon> checkIssueCoupon(List<SpsCoupon> couponList) throws Exception {

		List<SpsCoupon> result = new ArrayList<SpsCoupon>();
		SpsCouponissue issue = new SpsCouponissue();
		String storeId = SessionUtil.getStoreId();

		for (int i = 0; i < couponList.size(); i++) {
			boolean resultFlag = false;

			String cnt = "";
			String[] cntArr = null;
			BigDecimal totalCnt = null;

			// 1. 최대 쿠폰 발행수
			BigDecimal maxCouponIssue = couponList.get(i).getMaxIssueQty();
			BigDecimal maxMemIssueQty = couponList.get(i).getMaxMemIssueQty();

			// 현재 쿠폰 발행수
			cnt = this.getCouponIssueState(couponList.get(i).getCouponId(), null);
			// 0 전체 1 미등록 2 등록 3 사용 4 중지
			cntArr = cnt.split(",");
			totalCnt = new BigDecimal(cntArr[0]);

			if (!"COUPON_STATE_CD.STOP".equals(couponList.get(i).getCouponStateCd())) {
				String currentDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
				String startDt = couponList.get(i).getIssueStartDt();
				String endDt = couponList.get(i).getIssueEndDt();
				if (DateUtil.compareToDate(currentDt, startDt, DateUtil.FORMAT_1) != -1
						&& DateUtil.compareToDate(currentDt, endDt, DateUtil.FORMAT_1) != 1) {
					// bigdecimal 비교 -1 작다, 0 같다, 1 크다
					if (totalCnt.compareTo(maxCouponIssue) == -1) {

						// 2. 인당 발급수
						BigDecimal memCnt = (BigDecimal) dao.selectOne("sps.coupon.getMemIssueUseCount", couponList.get(i));
						if (memCnt.compareTo(maxMemIssueQty) == -1) {
							resultFlag = true;
						}
					}
				}
			}


			if (resultFlag) {
				// 쿠폰 발급
				issue.setCouponId(couponList.get(i).getCouponId());
				issue.setMemberNo(new BigDecimal(couponList.get(i).getMemberNo()));
				issue.setStoreId(storeId);
				issue.setRegDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));

				// 사용가능 시작/종료일 설정
				if ("TERM_TYPE_CD.DAYS".equals(couponList.get(i).getTermTypeCd())) {
					// 사용가능일수
					BigDecimal days = couponList.get(i).getTermDays();
					issue.setUseStartDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
					issue.setUseEndDt(DateUtil.getAddDate(DateUtil.FORMAT_1, DateUtil.getCurrentDate(DateUtil.FORMAT_1), days));
				} else if ("TERM_TYPE_CD.TERM".equals(couponList.get(i).getTermTypeCd())) {
					// 사용시작일, 사용종료일
					issue.setUseStartDt(couponList.get(i).getTermStartDt());
					issue.setUseEndDt(couponList.get(i).getTermEndDt());
				}
				issue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.REG");
				dao.insertOneTable(issue);
				result.add(couponList.get(i));
			}
		}

		return result;
	}


	/**
	 * @Method Name : makePublicCin
	 * @author : ian
	 * @date : 2016. 7. 13.
	 * @description : 쿠폰 개별 인증번호 생성 (1~9, A-Z, 14자리로 반환)
	 *
	 * @return
	 */
	public String makePublicCin() throws Exception {

		// 인증코드 생성
		String result = CommonUtil.makeCode(14);

		// 인증코드 중복 검사
		// NOT 사용가능, USED 사용불가
		SpsCouponIssueSearch check = new SpsCouponIssueSearch();
		check.setStoreId(SessionUtil.getStoreId());
		check.setPrivateCin(result);

		if ("USED".equals(this.duplicateCheck(check))) {
			while (true) {
				result = CommonUtil.makeCode(14);
				check.setPrivateCin(result);
				if ("NOT".equals(this.duplicateCheck(check))) {
					break;
				}
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @Method Name : getMemberCouponList
	 * @author : allen
	 * @date : 2016. 7. 15.
	 * @description : 회원 쿠폰 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsCouponissue> getMemberCouponList(SpsCouponSearch search) {
		return (List<SpsCouponissue>) dao.selectList("sps.coupon.getMemberCouponList", search);
	}

	/**
	 * 
	 * @Method Name : getMemberCouponCnt
	 * @author : allen
	 * @date : 2016. 7. 22.
	 * @description : 회원 사용가능한 쿠폰수 조회
	 *
	 * @param search
	 * @return
	 */
	public SpsCouponissue getMemberCouponCnt(SpsCouponSearch search) {
		return (SpsCouponissue) dao.selectOne("sps.coupon.getMemberCouponCnt", search);
	}

	/**
	 * 
	 * @Method Name : getApplyCouponList
	 * @author : dennis
	 * @date : 2016. 7. 22.
	 * @description : 적용가능 쿠폰 조회
	 *
	 * @param spsCouponSearch
	 * @return
	 */
//	public List<OmsOrdercoupon> getApplyCouponList(SpsCouponSearch spsCouponSearch) {
//		return (List<OmsOrdercoupon>) dao.selectList("sps.coupon.getApplyCouponList", spsCouponSearch);
//	}

	/**
	 * 
	 * @Method Name : getCouponCountByMemberNo
	 * @author : dennis
	 * @date : 2016. 7. 22.
	 * @description : 회원이 보유한 쿠폰 개수
	 *
	 * @param omsOrder
	 * @return
	 */
	public int getCouponCountByMemberNo(OmsOrder omsOrder) {
		return dao.selectCount("sps.coupon.getCouponCountByMemberNo", omsOrder);
	}

	public List<SpsCoupon> getApplyCouponList2(SpsCouponSearch spsCouponSearch) throws Exception {
		return (List<SpsCoupon>) dao.selectList("sps.coupon.getApplyCouponList", spsCouponSearch);
	}

	/**
	 * 
	 * @Method Name : getApplyCouponList
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 상품별 적용가능 쿠폰 조회
	 *
	 * @param spsCouponSearch
	 * @return
	 * @throws Exception
	 */
	public List<SpsCoupon> getApplyCouponList(SpsCouponSearch spsCouponSearch) throws Exception {


		String storeId = spsCouponSearch.getStoreId();
		String channelId = spsCouponSearch.getChannelId();
		BigDecimal memberNo = spsCouponSearch.getMemberNo();
		String deviceTypeCd = spsCouponSearch.getDeviceTypeCd();
		BigDecimal targetAmt = spsCouponSearch.getTargetAmt();
		String productId = spsCouponSearch.getProductId();
		String saleproductId = spsCouponSearch.getSaleproductId();
		String dealId = spsCouponSearch.getDealId();
		String controlType = spsCouponSearch.getControlType();

		List<String> memberTypeCds = spsCouponSearch.getMemberTypeCds();
		String memGradeCd = spsCouponSearch.getMemGradeCd();

		String downShowYn = spsCouponSearch.getDownShowYn();

		BigDecimal commissionRate = spsCouponSearch.getCommissionRate();

		logger.debug("=============== 쿠폰 적용 목록 ====================");
		logger.debug("storeId 		: " + storeId);
		logger.debug("channelId 	: " + channelId);
		logger.debug("dealId 		: " + dealId);
		logger.debug("memberNo 		: " + memberNo);
		logger.debug("deviceTypeCd 	: " + deviceTypeCd);
		logger.debug("targetAmt 	: " + targetAmt);
		logger.debug("productId 	: " + productId);
		logger.debug("saleproductId : " + saleproductId);
		logger.debug("downShowYn 	: " + downShowYn);
		logger.debug("memGradeCd 	: " + memGradeCd);
		logger.debug("memberTypeCds : " + memberTypeCds.toString());
		logger.debug("commissionRate : " + commissionRate.toString());
		logger.debug("controlType : " + controlType);

		List<SpsCoupon> resultList = new ArrayList<SpsCoupon>();

		//쿠폰목록
		List<SpsCoupon> couponList = (List<SpsCoupon>) dao.selectList("sps.coupon.getApplyCouponList", spsCouponSearch);

		//딜목록
		SpsDealSearch spsDealSearch = new SpsDealSearch();
		spsDealSearch.setMemberNo(memberNo);
		spsDealSearch.setProductId(productId);
		spsDealSearch.setSaleproductId(saleproductId);
		List<SpsDeal> dealList = dealService.getApplyDealList(spsDealSearch);

		for (SpsCoupon coupon : couponList) {

			//제어
			boolean resultControl = false;
			BigDecimal controlNo = coupon.getControlNo();
			if (CommonUtil.isNotEmpty(controlNo)) {
				//제어 check
				CcsControlSearch search = new CcsControlSearch();
				search.setStoreId(storeId);
				search.setControlNo(controlNo);
				search.setDeviceTypeCd(deviceTypeCd);
				search.setChannelId(channelId);
				search.setMemberNo(memberNo);
				search.setMemberTypeCds(memberTypeCds);
				search.setMemGradeCd(memGradeCd);

				if ("Y".equals(coupon.getDownShowYn())) {
					search.setControlType("COUPON");
				} else if (CommonUtil.isNotEmpty(controlType)) {
					search.setControlType(controlType);
				}

				resultControl = commonService.checkControl(search);
			} else {
				resultControl = true;
			}

			if (resultControl) {


				//deal 적용쿠폰 계산
				String couponDealId = coupon.getDealId();
				String dealApplyYn = coupon.getDealApplyYn();
				BigDecimal minOrderAmt = coupon.getMinOrderAmt();
				String feeLimitApplyYn = coupon.getFeeLimitApplyYn();
				String couponTypeCd = coupon.getCouponTypeCd();
				
				boolean checkFlag = true;
				logger.debug("couponTypeCd : " + couponTypeCd);

				//장바구니, 포장, 무료배송비 쿠폰은 한계수수료 or 상품개당최소금액 을 check하지 않는다. 주문전체 금액으로 check
				if ("COUPON_TYPE_CD.ORDER".equals(couponTypeCd) || "COUPON_TYPE_CD.WRAP".equals(couponTypeCd)
						|| "COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd)) {
					checkFlag = false;
				}

				SpsCoupon newCoupon2 = new SpsCoupon();
				BigDecimal applyDcAmt2 = new BigDecimal(0);
				BigDecimal comPrice2 = targetAmt;
				if (checkFlag && CommonUtil.isNotEmpty(couponDealId) && BaseConstants.YN_Y.equals(dealApplyYn)) {

					for (SpsDeal deal : dealList) {
						String spsDealId = deal.getDealId();
						if (spsDealId.equals(couponDealId)) {
							BigDecimal dealCommissionRate = deal.getSpsDealproducts().get(0).getCommissionRate();
							BigDecimal dealTargetAmt = deal.getTotalSalePrice();

							if (!checkFlag || (checkCommissionRate(dealCommissionRate, feeLimitApplyYn)
									&& checkMinOrderAmt(minOrderAmt, dealTargetAmt))) {	//한계수수료 미만상품 check, 최소금액 check

								BeanUtils.copyProperties(coupon, newCoupon2);

								newCoupon2.setDealSalePrice(deal.getSalePrice());
								newCoupon2.setDealAddSalePrice(deal.getAddSalePrice());
								newCoupon2.setDealTypeCd(deal.getDealTypeCd());
								newCoupon2.setDeliveryFeeFreeYn(deal.getSpsDealproducts().get(0).getDeliveryFeeFreeYn());
								newCoupon2.setPointSaveRate(deal.getSpsDealproducts().get(0).getPointSaveRate());
								newCoupon2.setDealStockQty(deal.getSpsDealproducts().get(0).getDealStockQty());
								newCoupon2.setTargetAmt(dealTargetAmt);
								newCoupon2.setDealListPrice(deal.getListPrice());
								calcCoupnDcAmt(newCoupon2);
								applyDcAmt2 = newCoupon2.getApplyDcAmt();
								comPrice2 = dealTargetAmt.subtract(applyDcAmt2);
							}

						}
					}
				}
				
				//일반쿠폰 계산
				SpsCoupon newCoupon = new SpsCoupon();
				BeanUtils.copyProperties(coupon, newCoupon);
				newCoupon.setTargetAmt(targetAmt);
				calcCoupnDcAmt(newCoupon);
				newCoupon.setDealId(null);				

				if (!checkFlag
						|| (checkCommissionRate(commissionRate, feeLimitApplyYn)
						&& checkMinOrderAmt(minOrderAmt, targetAmt))) {	//한계수수료 미만상품 check, 최소금액 check
				}else{
					newCoupon = null;
				}

				String dealTypeCd = newCoupon2.getDealTypeCd();
				if ("DEAL_TYPE_CD.PREMIUM".equals(dealTypeCd) || "DEAL_TYPE_CD.CHILDREN".equals(dealTypeCd)
						|| "DEAL_TYPE_CD.B2E".equals(dealTypeCd)) {
					resultList.add(newCoupon2);
				} else {
					// 동일 쿠폰에 대해 일반가에 적용한 값과 딜에 적용한 값의 최저값 비교
					if (newCoupon != null) {
						BigDecimal comPrice = targetAmt.subtract(newCoupon.getApplyDcAmt());
						logger.debug("comPrice : " + comPrice);
						logger.debug("comPrice2 : " + comPrice2);
						if (comPrice2.compareTo(comPrice) > 0) {
//					resultList.add(newCoupon);
							setCouponList(resultList, newCoupon);
						} else {
//					resultList.add(newCoupon2);
							setCouponList(resultList, newCoupon2);
						}
					}
				}

			}
		}

		return resultList;
	}

	public boolean checkCommissionRate(BigDecimal commissionRate, String feeLimitApplyYn) {
		logger.debug("!!!!!!!!한계수수료 미만 check !!!!!!!!");

		if (commissionRate.compareTo(new BigDecimal(10)) >= 0 || BaseConstants.YN_Y.equals(feeLimitApplyYn)) {
			return true;
		} else {
			logger.debug("한계수수료 미만 ==> ");
			logger.debug("commissionRate : " + commissionRate);
			logger.debug("feeLimitApplyYn : " + feeLimitApplyYn);
			return false;
		}
	}

	public boolean checkMinOrderAmt(BigDecimal minOrderAmt, BigDecimal totalSalePrice) {
		if (minOrderAmt.compareTo(totalSalePrice) <= 0) {
			return true;
		}
		logger.debug("!!!!!!!!최소구매금액 미만!!!!!!!!");
		logger.debug("minOrderAmt : " + minOrderAmt);
		logger.debug("totalSalePrice : " + totalSalePrice);
		return false;
	}

	private void setCouponList(List<SpsCoupon> resultList, SpsCoupon coupon) {

		/**
		 * 같은 쿠폰있을시 제거 couponId, couponIssueNo, dealId 동일한것 제거
		 */
		int delIdx = -1;
		int idx = 0;
		boolean addFlag = false;
		if (coupon != null && CommonUtil.isNotEmpty(coupon.getCouponId())) {
			if (resultList.size() == 0) {
				resultList.add(coupon);
			} else {
				for (SpsCoupon orgCoupon : resultList) {
					String orgCouponId = orgCoupon.getCouponId();
					BigDecimal orgCouponIssueNo = orgCoupon.getCouponIssueNo();
					String orgDealId = orgCoupon.getDealId();
					String couponId = coupon.getCouponId();
					BigDecimal couponIssueNo = coupon.getCouponIssueNo();
					String dealId = coupon.getDealId();
					if (orgCouponId != null && orgCouponId.equals(couponId) && orgCouponIssueNo != null
							&& orgCouponIssueNo.compareTo(couponIssueNo) == 0 && orgDealId != null && orgDealId.equals(dealId)) {
						if (orgCoupon.getApplyDcAmt().compareTo(coupon.getApplyDcAmt()) < 0) {
							String dealTypeCd = orgCoupon.getDealTypeCd();
							String couponTypeCd = orgCoupon.getCouponTypeCd();

//							if ("COUPON_TYPE_CD.PRODUCT".equals(couponTypeCd)) {	//상품쿠폰일시 우선순위적용.
								if (!"DEAL_TYPE_CD.PREMIUM".equals(dealTypeCd) && !"DEAL_TYPE_CD.CHILDREN".equals(dealTypeCd)
										&& !"DEAL_TYPE_CD.B2E".equals(dealTypeCd)) {
									delIdx = idx;
								}

//							} else {
//								delIdx = idx;
//							}
							addFlag = true;
						}
					} else {
						addFlag = true;
					}
					idx++;
				}
			}
		}
		if (delIdx > -1) {
			resultList.remove(delIdx);
		}
		if (addFlag) {
			resultList.add(coupon);
		}
	}

	public void calcCoupnDcAmt(SpsCoupon coupon) {
		//쿠폰검증을 위한 data 처리			
		String couponTypeCd = coupon.getCouponTypeCd();
		String dcApplyTypeCd = coupon.getDcApplyTypeCd();
		BigDecimal dcValue = coupon.getDcValue();
		BigDecimal maxDcAmt = coupon.getMaxDcAmt();
		BigDecimal minOrderAmt = coupon.getMinOrderAmt();
		BigDecimal targetAmt = coupon.getTargetAmt();
		BigDecimal applyDcAmt = new BigDecimal(0);

		//배송비, 포장비는 무료! targetAmt 와 같다.
		if ("COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd) || "COUPON_TYPE_CD.WRAP".equals(couponTypeCd)) {
			applyDcAmt = targetAmt;
		} else {
			if ("DC_APPLY_TYPE_CD.AMT".equals(dcApplyTypeCd)) {
				if (targetAmt.compareTo(dcValue) >= 0) {
					applyDcAmt = dcValue;
				} else {
					applyDcAmt = targetAmt;
				}
			} else if ("DC_APPLY_TYPE_CD.RATE".equals(dcApplyTypeCd)) {
				applyDcAmt = targetAmt.multiply(dcValue).divide(new BigDecimal(100)).setScale(-1, BigDecimal.ROUND_HALF_UP);
				if (maxDcAmt != null) {
					if (applyDcAmt.compareTo(maxDcAmt) > 0) {
						applyDcAmt = maxDcAmt;
					}
				}
			}
		}
		coupon.setApplyDcAmt(applyDcAmt);
	}

	/**
	 * @Method Name : createCampaignCoupon
	 * @author : ian
	 * @date : 2016. 9. 26.
	 * @description : 캠페인 쿠폰 발행
	 *
	 * @param issues
	 * @return
	 * @throws Exception
	 */
	public JSONObject createCampaignCoupon(CampaignCoupon coupons) throws Exception {

		JSONObject totalResult = new JSONObject();
		
		try {
//			for (TargetMember member : coupons.getTargetMembers()) {
//				
//				SpsCouponissue issue = new SpsCouponissue();
//				issue.setStoreId(coupons.getStoreId());
//				issue.setSegmentId(member.getSegmentId());
//				issue.setMemberNo(new BigDecimal(member.getMemberNo()));
//				
//				issue.setCampaignId(coupons.getCampaignId());
//				issue.setCampaignOrder(new BigDecimal(coupons.getCampaignOrder()));
//				issue.setCouponId(coupons.getCouponId());
//				
//				issue.setUseStartDt(coupons.getUseStartDt());
//				issue.setUseEndDt(coupons.getUseEndDt());
//				
//				issue.setRegDt(BaseConstants.SYSDATE);
//				issue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.REG");
//				issue.setCampaignYn("Y");
//				issue.setInsId(BaseConstants.SYSTEM_CAMPAIGN);
//				issue.setUpdId(BaseConstants.SYSTEM_CAMPAIGN);
//				
//				dao.insertOneTable(issue);
//			}
			
			int share = coupons.getTargetMembers().size() / 1000;
			int remain = coupons.getTargetMembers().size() % 1000;
			// 1. 총 발급매수를 1000회씩 끊어서 insert
			{
				if(share > 0) {
					for (int i=0; i< share; i++) {
						
						Map<String, List<SpsCouponissue>> map = new HashMap<String, List<SpsCouponissue>>();
						List<SpsCouponissue> issues = new ArrayList<SpsCouponissue>();
						for (int j = 0; j < 1000; j++) {
							SpsCouponissue issue = new SpsCouponissue();
							
							issue.setStoreId(coupons.getStoreId());
							issue.setCouponId(coupons.getCouponId());
							
							issue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.REG");
							
							issue.setMemberNo(new BigDecimal(coupons.getTargetMembers().get(j+(i*1000)).getMemberNo()));
							issue.setRegDt(BaseConstants.SYSDATE);
							issue.setUseStartDt(coupons.getUseStartDt());
							issue.setUseEndDt(coupons.getUseEndDt());
							
							issue.setCampaignId(coupons.getCampaignId());
							issue.setCampaignOrder(new BigDecimal(coupons.getCampaignOrder()));
							issue.setSegmentId(coupons.getTargetMembers().get(j+(i*1000)).getSegmentId());
							
							issue.setInsId(BaseConstants.SYSTEM_CAMPAIGN);
							issue.setUpdId(BaseConstants.SYSTEM_CAMPAIGN);
							issues.add(issue);
						}
						map.put("issues", issues);
						
						dao.insert("sps.coupon.issueCampaignCoupon", map);
					}
				}
			}	// 1. 발급매수 / 1000 나눈 몫 insert		
			
			// 2. 나머지 총 발급매수 insert
			{
				if(remain > 0) {
					Map<String, List<SpsCouponissue>> map2 = new HashMap<String, List<SpsCouponissue>>();
					List<SpsCouponissue> issues2 = new ArrayList<SpsCouponissue>();
					
					for (int i=0; i< remain; i++) {
						SpsCouponissue issue = new SpsCouponissue();
						
						issue.setStoreId(coupons.getStoreId());
						issue.setCouponId(coupons.getCouponId());
						
						issue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.REG");
						
						issue.setMemberNo(new BigDecimal(coupons.getTargetMembers().get(i+(share*1000)).getMemberNo()));
						issue.setRegDt(BaseConstants.SYSDATE);
						issue.setUseStartDt(coupons.getUseStartDt());
						issue.setUseEndDt(coupons.getUseEndDt());
						
						issue.setCampaignId(coupons.getCampaignId());
						issue.setCampaignOrder(new BigDecimal(coupons.getCampaignOrder()));
						issue.setSegmentId(coupons.getTargetMembers().get(i+(share*1000)).getSegmentId());
						
						issue.setInsId(BaseConstants.SYSTEM_CAMPAIGN);
						issue.setUpdId(BaseConstants.SYSTEM_CAMPAIGN);
						
						issues2.add(issue);
					}
					
					map2.put("issues", issues2);
					dao.insert("sps.coupon.issueCampaignCoupon", map2);
				}
			}	// end 2. 발급매수 / 1000 나눈 나머지 수량 insert
			
			totalResult.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_SUCCESS);
			totalResult.put("resultMsg", "발행 성공");
		} catch (Exception e) {
			logger.error("\t createCampaignCoupon() ### " + e.getMessage());
			totalResult.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_UNKNOWN);
			totalResult.put("resultMsg", e.getMessage());
		}
		
		return totalResult;
	}
	
	/**
	 * @Method Name : createFoissueCoupon
	 * @author : ian
	 * @date : 2016. 9. 26.
	 * @description : 프론트 쿠폰 발급
	 *
	 * @param issues
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> createFoissueCoupon(List<SpsCouponissue> issues) throws Exception {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		for(SpsCouponissue issue : issues) {
			
			// 인증 번호 쿠폰 
			if(CommonUtil.isNotEmpty(issue.getPrivateCin())) {
				SpsCoupon master = (SpsCoupon) getPrivateCinCouponCheckStep1(issue);
				
				// 1. 유효하지 않은 쿠폰코드
				if(master != null) {
					
					issue.setCouponId(master.getCouponId());
					
					List<SpsCouponissue> coupon = (List<SpsCouponissue>) getPrivateCinCouponCheckStep2(issue);
					if(coupon.size() > 0) {
						issue = coupon.get(0);
						issue.setMemberNo(SessionUtil.getMemberNo());
						issue.setControllCheckType("BO");	// FO 인증번호 쿠폰(마이페이지) 발급은 BO 허용설정 체크와 동일
						resultMap = issueCoupon(issue, false);
					} else {
						// 단일
						if(BaseConstants.COUPON_ISSUE_TYPE_CD_PUBLIC.equals(master.getCouponIssueTypeCd())) {
							resultMap.put("resultCode", "SOLDOUT_COUPON");
							resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.invalid.soldout"));
						}
						// 복수
						else if(BaseConstants.COUPON_ISSUE_TYPE_CD_PRIVATE.equals(master.getCouponIssueTypeCd())) {
							resultMap.put("resultCode", "DUPLICATE_COUPON");
							resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.invalid.duplicate"));							
						}
					}
				} else {
					resultMap.put("resultCode", "NOT_EXISTS_COUPON");
					resultMap.put("resultMsg", MessageUtil.getMessage("sps.coupon.invalid.id"));	
				}
			}
			// 일반 쿠폰
			else {
				issue.setControllCheckType("FO");
				resultMap = issueCoupon(issue, false);
			}
			
			resultList.add(resultMap);
		}
		return resultList;
	}

	
	
	/**
	 * @Method Name : getPrivateCinCouponCheckStep1
	 * @author : ian
	 * @date : 2016. 10. 24.
	 * @description : 인증번호로 마스터 번호 취득
	 *
	 * @param issue
	 * @return
	 * @throws Exception
	 */
	private SpsCoupon getPrivateCinCouponCheckStep1(SpsCouponissue issue) throws Exception {
		return (SpsCoupon) dao.selectOne("sps.coupon.getPrivateCinCouponCheckStep1", issue);
	}
	
	/**
	 * @Method Name : getPrivateCinCouponCheckStep2
	 * @author : ian
	 * @date : 2016. 10. 24.
	 * @description : 취득한 쿠폰번호 포함 인증번호로 미등록 쿠폰 취득 
	 *
	 * @param issue
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<SpsCouponissue> getPrivateCinCouponCheckStep2(SpsCouponissue issue) throws Exception {
		return (List<SpsCouponissue>) dao.selectList("sps.coupon.getPrivateCinCouponCheckStep2", issue);
	}

	/**
	 * 
	 * @Method Name : createOffshopCoupon
	 * @author : dennis
	 * @date : 2016. 10. 31.
	 * @description : 오프라인 쿠폰 발급.
	 *
	 * @param coupon
	 */
	public void createOffshopCoupon(SpsCouponissue coupon) {
//		 1 :  매장픽업신청시 발행할 쿠폰 => 000245_302
//		 2 :  회원가입시 발행할 쿠폰 => 000246_302
//		 3 :  멤버십 혜택으로 제공할 쿠폰 => 000247_302
//		String no = coupon.getOffshopTypeNo();
//		if ("1".equals(no)) {
//			coupon.setOffshopType("000245_302");
//		} else if ("2".equals(no)) {
//			coupon.setOffshopType("000246_302");
//		} else if ("3".equals(no)) {
//			coupon.setOffshopType("000247_302");
//		}
		logger.debug("coupon data : " + coupon.toString());
		try {
			pos.update("sps.coupon.createOffshopCoupon", coupon);
			String result = coupon.getResult();
			logger.debug("OFFSHOP COUPON CODE : " + result);
		} catch (Exception e) {
			throw new ServiceException("oms.bind.error", e.getMessage());
		}
	}
	
	/**
	 * @Method Name : getFirstBuyMemberList
	 * @author : ian
	 * @date : 2016. 11. 2.
	 * @description : 첫 구매 회원 추출
	 *
	 * @param member
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsMember> getFirstBuyMemberList(MmsMemberSearch search){
		return (List<MmsMember>) dao.selectList("sps.coupon.getFirstBuyMemberList", search);
	}
	
	/**
	 * @Method Name : insertCouponissue
	 * @author : ian
	 * @date : 2016. 11. 2.
	 * @description : 첫 구매 감사 쿠폰 배치 발행
	 *
	 * @param issue
	 * @throws Exception
	 */
	public void insertCouponissue(SpsCouponissue issue) throws Exception {
		dao.insertOne(issue);
	}

}
