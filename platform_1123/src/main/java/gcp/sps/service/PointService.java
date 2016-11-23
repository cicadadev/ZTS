package gcp.sps.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import gcp.mms.service.MemberService;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.search.SpsPointSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("pointService")
public class PointService extends BaseService {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CommonService commonService;

	@Autowired
	private MemberService	memberService;
	/**
	 * 
	 * @Method Name : getPointList
	 * @author : dennis
	 * @date : 2016. 6. 14.
	 * @description : 메일포인트 조회
	 *
	 * @param spsPointSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsPointsave> getPointList(SpsPointSearch spsPointSearch) {
//		// 포인트 프로모션 번호 검색시 다른 조건 무시
//		if (CommonUtil.isNotEmpty(spsPointSearch.getPointSaveIds())) {
//			String temp = spsPointSearch.getPointSaveIds();
//			spsPointSearch = new SpsPointSearch();
//			spsPointSearch.setPointSaveIds(temp);
//		}
		spsPointSearch.setStoreId(SessionUtil.getStoreId());
		return (List<SpsPointsave>) dao.selectList("sps.point.getPointList", spsPointSearch);
	}

	/**
	 * @Method Name : deletePoint
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 대기상태인 포인트만 삭제
	 *
	 * @param list
	 * @throws Exception
	 */
	public void deletePoint(List<SpsPointsave> list) throws Exception {

		String storeId = SessionUtil.getStoreId();
		for (SpsPointsave point : list) {
			BigDecimal applyNo = point.getApplyNo();
			BigDecimal controlNo = point.getControlNo();

			logger.debug(" \t applyNo >> " + applyNo);
			logger.debug(" \t controlNo >> " + controlNo);

			/*
			 * 1. 포인트 삭제
			 * 	- sps_pointsave
			 * 
			 * 2. 적용대상, 제한설정 정보 삭제
			 * 	- ccs_apply, ccs_applytarget, ccs_excproducts
			 * 	- ccs_control, ccs_controlchannel, ccs_controldevice, ccs_controlmembergrade, ccs_controlmembertype
			 * */

			// 1. 포인트 삭제
			point.setStoreId(storeId);
			dao.deleteOneTable(point);

			// 2. 설정정보 삭제
			// 2-1 적용대상 설정 삭제
			if (applyNo != null) {
				CcsApplytarget target = new CcsApplytarget();
				CcsExcproduct product = new CcsExcproduct();

				// 적용대상 삭제
				target.setStoreId(storeId);
				target.setApplyNo(applyNo);
				commonService.deleteApplytargetByApplyNoNewTx(target);

				// 제외상품 삭제
				product.setStoreId(storeId);
				product.setApplyNo(applyNo);
				commonService.deleteExcproductByApplyNoNewTx(product);

			}

			// 2-2 제한설정 삭제
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
	};

	/**
	 * 
	 * @Method Name : getPointDetail
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : 포인트 프로모션 상세 조회
	 *
	 * @param spsPointSearch
	 * @return
	 */
	public SpsPointsave getPointDetail(SpsPointSearch spsPointSearch) {

		SpsPointsave spsPointsave = (SpsPointsave) dao.selectOne("sps.point.getPointDetail", spsPointSearch);
		String targetTypeCd = spsPointsave.getCcsApply().getTargetTypeCd();

		List<CcsApplytarget> targets = null;
		List<CcsExcproduct> exProducts = null;

		BigDecimal applyNo = spsPointsave.getApplyNo();
		if (BaseConstants.TARGET_TYPE_CD_ALL.equals(targetTypeCd)) {
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		}else if(BaseConstants.TARGET_TYPE_CD_PRODUCT.equals(targetTypeCd)){
			targets = commonService.getCcsApplytargetPrByApplyNo(applyNo);
		}else if(BaseConstants.TARGET_TYPE_CD_CATEGORY.equals(targetTypeCd)){
			targets = commonService.getCcsApplytargetCaByApplyNo(applyNo);
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		}else if(BaseConstants.TARGET_TYPE_CD_BRAND.equals(targetTypeCd)){
			targets = commonService.getCcsApplytargetBrByApplyNo(applyNo);
			exProducts = commonService.getCcsExcproductByApplyNo(applyNo);
		}

		if (targets != null) {
			spsPointsave.getCcsApply().setCcsApplytargets(targets);
		}
		if (exProducts != null) {
			spsPointsave.getCcsApply().setCcsExcproducts(exProducts);
		}

		return spsPointsave;
	}

	/**
	 * @Method Name : insertPoint
	 * @author : ian
	 * @date : 2016. 8. 3.
	 * @description :
	 *
	 * @param spsPointsave
	 * @return
	 * @throws Exception
	 */
	public String insertPoint(SpsPointsave spsPointsave) throws Exception {

		// 0. 허용설정
		BigDecimal newControlNo = null;
		boolean isControlSet = spsPointsave.getCcsControl() != null && "N".equals(spsPointsave.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(spsPointsave.getControlNo(), spsPointsave.getCcsControl());
		}
		if (newControlNo != null) {
			spsPointsave.setControlNo(newControlNo);
		}

		//포인트 insert			
		String storeId = SessionUtil.getStoreId();
		spsPointsave.setStoreId(storeId);
		dao.insertOneTable(spsPointsave);
//		dao.insert("sps.point.insertPointsave", spsPointsave);

		// 쿠폰 사용 대상 마스터 저장
		CcsApply apply = new CcsApply();
		apply.setStoreId(spsPointsave.getStoreId());
		apply.setTargetTypeCd(spsPointsave.getCcsApply().getTargetTypeCd());
		commonService.insertApply(apply);
		spsPointsave.setApplyNo(apply.getApplyNo());			// 적용대상번호 취득

		//point 적용대상번호 update
		spsPointsave.setApplyNo(apply.getApplyNo());
		dao.updateOneTable(spsPointsave);

//		if (spsPointsave.getCcsApply().getCcsApplytargets() != null) {
//			for (CcsApplytarget applyTarget : spsPointsave.getCcsApply().getCcsApplytargets()) {
//				applyTarget.setStoreId(storeId);
//				applyTarget.setApplyNo(apply.getApplyNo());
//				dao.insertOneTable(applyTarget);
//			}
//		}
//		if (spsPointsave.getCcsApply().getCcsExcproducts() != null) {
//			for (CcsExcproduct excProduct : spsPointsave.getCcsApply().getCcsExcproducts()) {
//				excProduct.setStoreId(storeId);
//				excProduct.setApplyNo(apply.getApplyNo());
//				dao.insertOneTable(excProduct);
//			}
//		}
		if (spsPointsave.getCcsApply().getCcsApplytargets() != null) {
			apply.setCcsApplytargets(spsPointsave.getCcsApply().getCcsApplytargets());
		}
		if (spsPointsave.getCcsApply().getCcsExcproducts() != null) {
			apply.setCcsExcproducts(spsPointsave.getCcsApply().getCcsExcproducts());
		}
		commonService.saveCcsApply(apply);

		return spsPointsave.getPointSaveId();
	}

	/**
	 * @Method Name : updatePoint
	 * @author : ian
	 * @date : 2016. 8. 3.
	 * @description :
	 *
	 * @param spsPointsave
	 * @return
	 * @throws Exception
	 */
	public String updatePoint(SpsPointsave spsPointsave) throws Exception {
		
		String storeId = SessionUtil.getStoreId();

		// 0. 허용설정
		BigDecimal newControlNo = null;
		BigDecimal deleteControlNo = null;
		boolean isControlDelete = spsPointsave.getControlNo() != null && "Y".equals(spsPointsave.getIsAllPermit());
		boolean isControlSet = spsPointsave.getCcsControl() != null && "N".equals(spsPointsave.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(spsPointsave.getControlNo(), spsPointsave.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = spsPointsave.getControlNo();
			spsPointsave.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			spsPointsave.setControlNo(newControlNo);
		}

		//적용대상 저장
		CcsApply ccsApply = spsPointsave.getCcsApply();
		ccsApply.setStoreId(storeId);
		BigDecimal applyNo = commonService.saveCcsApply(ccsApply);

		//point 적용대상번호 update
		spsPointsave.setApplyNo(applyNo);
		spsPointsave.setStoreId(storeId);
		dao.updateOneTable(spsPointsave);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}

		return spsPointsave.getPointSaveId();
	}

	/**
	 * 
	 * @Method Name : getApplyPointList
	 * @author : dennis
	 * @date : 2016. 7. 22.
	 * @description : 적용가능 포인트조회
	 *
	 * @param spsPoinSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsPointsave> getApplyPointList(SpsPointSearch spsPointSearch) {
		return (List<SpsPointsave>) dao.selectList("sps.point.getApplyPointList", spsPointSearch);
	}

	/**
	 * @Method Name : updatePointState
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 포인트 프로모션 상태 변경 (진행 / 중지)
	 *
	 * @param pointSave
	 * @return
	 * @throws Exception
	 */
	public String updatePointState(SpsPointsave pointSave) throws Exception {
		pointSave.setStoreId(SessionUtil.getStoreId());
		pointSave.setUpdId(SessionUtil.getLoginId());
		dao.update("sps.point.updatePointState", pointSave);
		return pointSave.getPointSaveId();
	}

	/**
	 * 포인트 프로모션을 가격에 적용한 정보를 리턴
	 * 
	 * @Method Name : getPointPromotion
	 * @author : intune
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @param productId
	 * @param basicPoint
	 * @return
	 */
	public SpsPointsave getPointPromotion(String productId, BigDecimal basicPoint) {



		String channelId = SessionUtil.getChannelId();
		String storeId = SessionUtil.getStoreId();
		HttpServletRequest request = SessionUtil.getHttpServletRequest();
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
		String memGradeCd = FoSessionUtil.getMemGradeCd();

		SpsPointsave resultObj = new SpsPointsave();
		resultObj.setBasicPoint(basicPoint);
		resultObj.setTotalPoint(basicPoint);
		resultObj.setAddPoint(new BigDecimal(0));
		resultObj.setPointValue(new BigDecimal(0));

		SpsPointSearch spsPointSearch = new SpsPointSearch();
		spsPointSearch.setProductId(productId);
		spsPointSearch.setStoreId(storeId);

		BigDecimal memberNo = SessionUtil.getMemberNo();
		if (CommonUtil.isEmpty(memberNo)) {
			return resultObj;
		}

		List<SpsPointsave> pointList = getApplyPointList(spsPointSearch);

		BigDecimal minPoint = basicPoint;

		if (pointList != null) {	//적용가능한 point promotion있을때
			for (SpsPointsave spsPointsave : pointList) {
				String pointSaveTypeCd = spsPointsave.getPointSaveTypeCd();
				BigDecimal pointValue = spsPointsave.getPointValue();
				BigDecimal curPrice = new BigDecimal(0);

				boolean resultControl = false;
				BigDecimal controlNo = spsPointsave.getControlNo();
				if (CommonUtil.isNotEmpty(controlNo)) {
					//제어 check
					CcsControlSearch search = new CcsControlSearch();
					search.setStoreId(SessionUtil.getStoreId());
					search.setControlNo(controlNo);
					search.setChannelId(channelId);
					search.setMemberNo(memberNo);
					search.setDeviceTypeCd(deviceTypeCd);

					List<String> memberTypeCds = new ArrayList<String>();
					memberService.getMemberTypeInfo(memberTypeCds);

					search.setMemberTypeCds(memberTypeCds);
					search.setMemGradeCd(memGradeCd);
					search.setApplyObjectName("포인트," + spsPointsave.getName());
					resultControl = commonService.checkControl(search);
				}

				if (resultControl || CommonUtil.isEmpty(controlNo)) {

					if (BaseConstants.POINT_SAVE_TYPE_CD_MULTI.equals(pointSaveTypeCd)) {	//배수적립
						curPrice = basicPoint.multiply(pointValue);
					} else if (BaseConstants.POINT_SAVE_TYPE_CD_ADD.equals(pointSaveTypeCd)) {	//추가적립
						curPrice = basicPoint.add(pointValue);
					}
					if (curPrice.compareTo(minPoint) > 0) {	//이전값보다 큰것
						resultObj.setPointSaveId(spsPointsave.getPointSaveId());
						resultObj.setName(spsPointsave.getName());
						resultObj.setPointSaveTypeCd(pointSaveTypeCd);
						resultObj.setPointValue(pointValue);

						minPoint = curPrice;
						resultObj = spsPointsave;
					}
				}
			}
		}

//		if(BigDeciaml.minPoint)
		resultObj.setAddPoint(minPoint.subtract(basicPoint));
		resultObj.setTotalPoint(minPoint);
		logger.debug("## basicPoint : " + basicPoint);
		logger.debug("## TotalPoint : " + minPoint);
		return resultObj;

	}
}
