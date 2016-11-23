package gcp.sps.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.SpsPresentdeal;
import gcp.sps.model.SpsPresentproduct;
import gcp.sps.model.search.SpsPresentSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@Service("presentPromotionService")
public class PresentPromotionService extends BaseService {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CommonService	commonService;

	/**
	 * @Method Name : getPresentPromotion
	 * @author : ian
	 * @date : 2016. 5. 3.
	 * @description : 사은품 프로모션 관리
	 *
	 * @param sps
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SpsPresent> getPresentPromotion(SpsPresentSearch presentSearch) {

//		// 사은품 프로모션 번호 검색시 다른 조건 무시
//		if (CommonUtil.isNotEmpty(presentSearch.getPresentIds())) {
//			String temp = presentSearch.getPresentIds();
//			presentSearch = new SpsPresentSearch();
//			presentSearch.setPresentIds(temp);
//		}
		presentSearch.setStoreId(SessionUtil.getStoreId());

		List<SpsPresent> list = (List<SpsPresent>) dao.selectList("sps.present.getPresentPromotionList", presentSearch);
		return list;
	};

	/**
	 * @Method Name : insertPresentPromotion, updatePresentPromotion
	 * @author : ian
	 * @date : 2016. 5. 12.
	 * @description : 사은품 프로모션 등록, 수정, 삭제
	 *
	 * @param sp
	 * @throws ServiceException
	 */
	public void deletePresentPromotion(List<SpsPresent> list) throws Exception {

		String storeId = SessionUtil.getStoreId();
		for (SpsPresent present : list) {
			String presentId = present.getPresentId();
			BigDecimal applyNo = present.getApplyNo();
			BigDecimal controlNo = present.getControlNo();

			logger.debug(" \t applyNo >> " + applyNo);
			logger.debug(" \t controlNo >> " + controlNo);

			/*
			 * 1. 사은품 하위 테이블 삭제
			 * 	- sps_presentdeal
			 * 	- sps_presentproduct
			 * 
			 * 2. 사은품 마스터 삭제
			 * 	- sps_present
			 * 
			 * 3. 적용대상, 제한설정 정보 삭제
			 * 	- ccs_apply, ccs_applytarget, ccs_excproducts
			 * 	- ccs_control, ccs_controlchannel, ccs_controldevice, ccs_controlmembergrade, ccs_controlmembertype
			 * */

			// 1. 사은품 하위 테이블 삭제
			// 1-1 sps_presentdeal 삭제
			if ("Y".equals(present.getDealApplyYn())) {
				SpsPresentdeal deal = new SpsPresentdeal();
				deal.setStoreId(storeId);
				deal.setPresentId(presentId);
				dao.delete("sps.present.deletePresentDeal", deal);
			}
			// 1-2 sps_presentproduct 삭제
			SpsPresentproduct presentProduct = new SpsPresentproduct();
			presentProduct.setStoreId(storeId);
			presentProduct.setPresentId(presentId);
			dao.delete("sps.present.deletePresentProdcut", presentProduct);

			// 2. 사은품 삭제
			present.setStoreId(storeId);
			dao.deleteOneTable(present);

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
	};

	public String insertPresentPromotion(SpsPresent present) throws Exception {

		// 제한설정
		// 1. [신규]제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 2. [신규]전체허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass

		// 0. 허용설정
		BigDecimal newControlNo = null;
		boolean isControlSet = present.getCcsControl() != null && "N".equals(present.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(present.getControlNo(), present.getCcsControl());
		}
		if (newControlNo != null) {
			present.setControlNo(newControlNo);
		}

		// 사은품 프로모션 등록
		String storeId = SessionUtil.getStoreId();
		present.setStoreId(storeId);
		dao.insertOneTable(present);
//		dao.insert("sps.present.insertPresentPromotion", present);
		String presentId = present.getPresentId();

		// 쿠폰 허용 딜
		if ("Y".equals(present.getDealApplyYn())) {
			insDelDeal(present, true);
		}

		// 사은품 사용 대상 마스터 저장
		CcsApply apply = new CcsApply();
		apply.setStoreId(present.getStoreId());
		apply.setTargetTypeCd(present.getCcsApply().getTargetTypeCd());
		commonService.insertApply(apply);
		present.setApplyNo(apply.getApplyNo());			// 적용대상번호 취득

		// present 제어번호, 적용대상번호 update
		dao.updateOneTable(present);

//		if (present.getCcsApply().getCcsApplytargets() != null) {
//			for (CcsApplytarget applyTarget : present.getCcsApply().getCcsApplytargets()) {
//				applyTarget.setStoreId(storeId);
//				applyTarget.setApplyNo(apply.getApplyNo());
//				dao.insertOneTable(applyTarget);
//			}
//		}
//		if (present.getCcsApply().getCcsExcproducts() != null) {
//			for (CcsExcproduct excProduct : present.getCcsApply().getCcsExcproducts()) {
//				excProduct.setStoreId(storeId);
//				excProduct.setApplyNo(apply.getApplyNo());
//				dao.insertOneTable(excProduct);
//			}
//		}
		
		if (present.getCcsApply().getCcsApplytargets() != null) {
			apply.setCcsApplytargets(present.getCcsApply().getCcsApplytargets());
		}
		if (present.getCcsApply().getCcsExcproducts() != null) {
			apply.setCcsExcproducts(present.getCcsApply().getCcsExcproducts());
		}
		commonService.saveCcsApply(apply);
		
		if (present.getSpsPresentproducts() != null) {
			for (SpsPresentproduct spp : present.getSpsPresentproducts()) {
				spp.setStoreId(storeId);
				spp.setPresentId(presentId);
				dao.insertOneTable(spp);
			}
		}

		return presentId;
	}

	public String updatePresentPromotion(SpsPresent present) throws Exception {
		/*  
		 * - 사은품 마스터 update
		 * 
		 * - 제한유형	(clear)
		
			3. [수정]전체허용 -> 제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
			4. [수정]전체허용 -> 허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass
			5. [수정]제한 -> 허용 : controlNo(not null), ccsControl(null), isAllPermit(Y) => ccs_control 삭제
			6. [수정]제한 -> 제한(내용변경 없음) : controlNo(not null), ccsControl(null), isAllPermit(N) => by pass
			7. [수정]제한 -> 제한(내용변경 있음) : controlNo(not null), ccsControl(not null), isAllPermit(N)
		 * 
		 * - 딜 전문관 적용
		 * 		- 추가 : spspresentdeal 에 추가
		 * 		- 삭제 : spspresentdeal 에 삭제
		 * 
		 * - 사은품 사용대상 변경시
		 * 		- 제외상품(excprodcut), 적용대상(applytarget) 기존 데이터 삭제
		 * */

		String storeId = SessionUtil.getStoreId();
		String presentId = present.getPresentId();

		// 0. 허용설정
		BigDecimal newControlNo = null;
		BigDecimal deleteControlNo = null;
		boolean isControlDelete = present.getControlNo() != null && "Y".equals(present.getIsAllPermit());
		boolean isControlSet = present.getCcsControl() != null && "N".equals(present.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(present.getControlNo(), present.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = present.getControlNo();
			present.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			present.setControlNo(newControlNo);
		}

		// 딜전문관 사용 --> 미사용 // 미사용 --> 사용
		this.insDelDeal(present, false);

		//적용대상 저장
		CcsApply ccsApply = present.getCcsApply();
		ccsApply.setStoreId(storeId);
		BigDecimal applyNo = commonService.saveCcsApply(ccsApply);

		present.setApplyNo(applyNo);

		// 제공사은품 기존 정보 삭제
		SpsPresentproduct delSpp = new SpsPresentproduct();
		delSpp.setStoreId(storeId);
		delSpp.setPresentId(presentId);
		dao.delete("sps.present.deletePresentProdcut", delSpp);

		// 제공사은품 정보 insert
		if (present.getSpsPresentproducts() != null) {
			for (SpsPresentproduct spp : present.getSpsPresentproducts()) {
				spp.setStoreId(storeId);
				spp.setPresentId(presentId);
				dao.insertOneTable(spp);
			}
		}

		// 사은품 프로모션 마스터 수정
		present.setUpdId(SessionUtil.getLoginId());
		dao.updateOneTable(present);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}

		return present.getPresentId();
	}

	/**
	 * @Method Name : updatePresentState
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 사은품 프로모션 상태 변경 (진행 / 중지)
	 *
	 * @param present
	 * @return
	 * @throws Exception
	 */
	public String updatePresentState(SpsPresent present) throws Exception {
		present.setStoreId(SessionUtil.getStoreId());
		present.setUpdId(SessionUtil.getLoginId());
		dao.update("sps.present.updatePresentState", present);

		return present.getPresentId();
	}

	/**
	 * @Method Name : insDelDeal
	 * @author : ian
	 * @date : 2016. 6. 15.
	 * @description : 사은품 허용딜 insert & delete
	 *
	 * @param present
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void insDelDeal(SpsPresent present, boolean insert) throws Exception {

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
			dao.delete("sps.present.deletePresentDeal", present);
		}

		if ("Y".equals(present.getDealApplyYn())) {
			List<SpsPresentdeal> dealList = (List<SpsPresentdeal>) dao.selectList("sps.present.getPresentDeal", present);
			for (SpsPresentdeal deal : dealList) {
				deal.setPresentId(present.getPresentId());
				dao.insertOneTable(deal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpsPresentproduct> getPresentPromotionProduct(SpsPresentproduct presentProduct) {
		return (List<SpsPresentproduct>) dao.selectList("sps.present.getPresentPromotionProduct", presentProduct);
	};

	/**
	 * @Method Name : getPresentPromotionDetail
	 * @author : ian
	 * @date : 2016. 5. 17.
	 * @description : 사은품 프로모션 상세
	 *
	 * @param bps
	 * @return
	 */
	public SpsPresent getPresentPromotionDetail(SpsPresent present) {
		String storeId = SessionUtil.getStoreId();
		present.setStoreId(storeId);

		SpsPresent spsPresent = (SpsPresent) dao.selectOne("sps.present.getPresentPromotionDetail", present);
		String targetTypeCd = spsPresent.getCcsApply().getTargetTypeCd();

		List<CcsApplytarget> targets = null;
		List<CcsExcproduct> exProducts = null;
		List<SpsPresentproduct> presents = null;

		BigDecimal applyNo = spsPresent.getApplyNo();
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

		SpsPresentproduct spp = new SpsPresentproduct();
		spp.setStoreId(storeId);
		spp.setPresentId(present.getPresentId());
		presents = this.getPresentPromotionProduct(spp);

		if (targets != null) {
			spsPresent.getCcsApply().setCcsApplytargets(targets);
		}
		if (exProducts != null) {
			spsPresent.getCcsApply().setCcsExcproducts(exProducts);
		}
		if (presents != null) {
			spsPresent.setSpsPresentproducts(presents);
		}

		return spsPresent;
	};

	/**
	 * @Method Name : getExistsList
	 * @author : ian
	 * @date : 2016. 6. 24.
	 * @description : 적용대상 중복 확인
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getExistsList(SpsPresentSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		List<String> list = new ArrayList<String>();

		list = (ArrayList<String>) dao.selectList("getExistsPresentProductList", search);

		return list;
	}

}
