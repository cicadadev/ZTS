package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsBusinessholiday;
import gcp.ccs.model.CcsCommission;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.search.CcsBusinessSearch;
import gcp.pms.model.PmsCategory;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.ccs.service
 * @FileName : BusinessService.java
 * @author : dennis
 * @date : 2016. 5. 23.
 * @description : business service
 */
@Service
public class BusinessService extends BaseService {


	/**
	 * 
	 * @Method Name : getBusinessList
	 * @author : dennis
	 * @date : 2016. 5. 23.
	 * @description : 거래처 popup 조회
	 *
	 * @param ccsBusinessSearch
	 * @return List<CcsBusiness>
	 */
	@SuppressWarnings("unchecked")
	public List<CcsBusiness> getBusinessList(CcsBusinessSearch ccsBusinessPopupSearch) {
		return (List<CcsBusiness>) dao.selectList("ccs.business.getBusinessList", ccsBusinessPopupSearch);
	}

	/**
	 * 업체의 배송정책 목록 조회
	 * 
	 * @Method Name : selectDeliverypolicyByBusinessId
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param ccsBusinessPopupSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsDeliverypolicy> selectDeliverypolicyByBusinessId(CcsBusinessSearch ccsBusinessPopupSearch) {
		return (List<CcsDeliverypolicy>) dao.selectList("ccs.business.selectDeliverypolicyByBusinessId", ccsBusinessPopupSearch);
	}
	
	/**
	 * 
	 * @Method Name : getBusinessListInfo
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체관리 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsBusiness> getBusinessListInfo(CcsBusinessSearch search){
		// id 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(search.getBusinessIds())) {

			String temp = CommonUtil.isNotEmpty(search.getBusinessIds()) ? search.getBusinessIds() : "";

			search = new CcsBusinessSearch();
			if (!"".equals(temp)) {
				search.setBusinessIds(temp);
			}
		}
		search.setStoreId(SessionUtil.getStoreId());

		return (List<CcsBusiness>) dao.selectList("ccs.business.getBusinessListInfo", search);
	}
	
	/**
	 * 
	 * @Method Name : getDeliverypolicyList
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체관리 배송정책목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsDeliverypolicy> getDeliverypolicyList(CcsBusinessSearch search){
		return (List<CcsDeliverypolicy>) dao.selectList("ccs.business.getDeliverypolicyList", search);
	}
	
	/**
	 * 
	 * @Method Name : getBusinessHolidayList
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체관리 휴일 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsBusinessholiday> getBusinessHolidayList(CcsBusinessSearch search) {
		return (List<CcsBusinessholiday>) dao.selectList("ccs.business.getBusinessHolidayList", search);

	}

	/**
	 * 
	 * @Method Name : getBusinessDetail
	 * @author : emily
	 * @date : 2016. 6. 17.
	 * @description : 업체관리 정보 상세조회
	 *
	 * @param business
	 * @return
	 */
	public CcsBusiness getBusinessDetail(CcsBusiness business) {
		
		CcsBusiness detail = (CcsBusiness) dao.selectOne("ccs.business.getBusinessDetail", business); 
		List<PmsCategory> commitionList = (List<PmsCategory>) dao.selectList("ccs.business.getCommissionList", business);
		
		if(commitionList != null){
			detail.setPmsCategorys(commitionList);
		}
		return detail;
	}

	/**
	 * 
	 * @Method Name : updateBusiness
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 정보 수정
	 *
	 * @param business
	 * @return
	 * @throws Exception
	 */
	public String updateBusiness(CcsBusiness business) throws Exception {

		if (business != null) {
			if (CommonUtil.isNotEmpty(business.getBusinessId())) {
				business.setStoreId(SessionUtil.getStoreId());
				business.setUpdId(SessionUtil.getLoginId());
				// 업체 수정
				dao.updateOneTable(business);

			} else {
				// 입점신청 or 업체등록
				if (CommonUtil.isEmpty(business.getOverseasPurchaseYn())) {
					business.setOverseasPurchaseYn("N");//필수값	
				}
				if (CommonUtil.isEmpty(SessionUtil.getStoreId())) {
					business.setStoreId(SessionUtil.getStoreId());
				}
				if (CommonUtil.isEmpty(SessionUtil.getLoginId())) {
					business.setUpdId(SessionUtil.getLoginId());
				}
				

				insertBusiness(business);

				for (CcsCommission list : business.getCcsCommissions()) {
					list.setBusinessId(business.getBusinessId());
				}
			}

			// 카테고리 수수료율 저장
			deleteBatchCommission(business);
			saveCommission(business.getCcsCommissions());
		}

		return business.getBusinessId();
	}
	
	/**
	 * 
	 * @Method Name : changeState
	 * @author : roy
	 * @date : 2016. 8. 19.
	 * @description : 업체상태 수정
	 *
	 * @param business
	 * @return
	 * @throws Exception
	 */
	public String changeState(CcsBusiness business) throws Exception {
		// 업체 수정
		business.setStoreId(SessionUtil.getStoreId());
		business.setUpdId(SessionUtil.getLoginId());
		dao.updateOneTable(business);

		if ("BUSINESS_STATE_CD.STOP".equals(business.getBusinessStateCd())) {
			dao.update("ccs.business.updateBusinessProducts", business);
		}
		return business.getBusinessId();
	}

	/**
	 * 
	 * @Method Name : updateDelivery
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 배송정책 수정
	 *
	 * @param delivery
	 * @throws Exception
	 */
	public int updateDeliverypolicy(CcsDeliverypolicy delivery) throws Exception {
		delivery.setStoreId(SessionUtil.getStoreId());
		delivery.setUpdId(SessionUtil.getLoginId());
		return dao.updateOneTable(delivery);
	}

	/**
	 * 
	 * @Method Name : getDeliverypolicy
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 배송정책 상세조회
	 *
	 * @param delivery
	 * @return
	 * @throws Exception
	 */
	public CcsDeliverypolicy getDeliverypolicy(CcsDeliverypolicy delivery) throws Exception {
		return (CcsDeliverypolicy) dao.selectOneTable(delivery);
	}

	/**
	 * 
	 * @Method Name : insertDelivery
	 * @author : emily
	 * @date : 2016. 6. 20.
	 * @description : 업체관리 배송정책 등록
	 *
	 * @param deli
	 * @throws Exception
	 */
	public void insertDelivery(CcsDeliverypolicy deli) throws Exception {
		deli.setStoreId(SessionUtil.getStoreId());
		deli.setUpdId(SessionUtil.getLoginId());
		dao.insertOneTable(deli);
	}

	/**
	 * 
	 * @Method Name : deleteDeliverypolicy
	 * @author : emily
	 * @date : 2016. 6. 20.
	 * @description : 업체관리 배송정책 삭제
	 *
	 * @param poliList
	 * @throws Exception
	 */
	public void deleteDeliverypolicy(List<CcsDeliverypolicy> poliList) throws Exception {
		for (CcsDeliverypolicy info : poliList) {
			dao.deleteOneTable(info);
		}
	}

	/**
	 * 
	 * @Method Name : saveHoliday
	 * @author : emily
	 * @date : 2016. 6. 20.
	 * @description : 업체관리 휴일정보 저장
	 *
	 * @param holiday
	 * @throws Exception
	 */
	public void saveHoliday(List<CcsBusinessholiday> holiday) throws Exception {
		
		for (CcsBusinessholiday info : holiday) {

			/*int holidayCnt = getBusinessHolidayDuplicate(info);
			if(holidayCnt > 0){
				throw new ServiceException("ccs.business.holiday.duplicate");
			}*/
			if (CommonUtil.isNotEmpty(info.getInsDt())) {
				info.setStoreId(SessionUtil.getStoreId());
				info.setUpdId(SessionUtil.getLoginId());
				dao.updateOneTable(info);
			} else {
				info.setStoreId(SessionUtil.getStoreId());
				info.setUpdId(SessionUtil.getLoginId());
				dao.insertOneTable(info);
			}
		}
	}

	public int getBusinessHolidayDuplicate(CcsBusinessholiday holiday) {

		return dao.selectCount("ccs.business.getBusinessHolidayDuplicate", holiday);
	}

	/**
	 * 
	 * @Method Name : deleteBusinessholiday
	 * @author : emily
	 * @date : 2016. 6. 20.
	 * @description : 업체관리 휴일정보 삭제
	 *
	 * @param holiday
	 * @throws Exception
	 */
	public void deleteBusinessholiday(List<CcsBusinessholiday> holiday) throws Exception {
		for (CcsBusinessholiday info : holiday) {
			info.setHoliday(info.getHoliday().replace("-", ""));
			dao.deleteOneTable(info);
		}
	}

	/**
	 * 
	 * @Method Name : insertBusiness
	 * @author : emily
	 * @date : 2016. 6. 21.
	 * @description : 업체 기본정보 등록
	 *
	 * @param business
	 * @return
	 * @throws Exception
	 */
	public void insertBusiness(CcsBusiness business) throws Exception {
		business.setStoreId(SessionUtil.getStoreId());
		business.setUpdId(SessionUtil.getLoginId());
		dao.insertOneTable(business);
	}

	/**
	 * 
	 * @Method Name : getCommossionList
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 업체 수수료 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsCommission> getCommissionList(CcsBusinessSearch search) {
		return (List<CcsCommission>) dao.selectList("ccs.business.getCommissionList", search);

	}

	/**
	 * 
	 * @Method Name : getCommissionListByCategory
	 * @author : eddie
	 * @date : 2016. 6. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<CcsCommission> getCommissionListByCategory(CcsBusinessSearch search) {
		return (List<CcsCommission>) dao.selectList("ccs.business.getCommissionListByCategory", search);

	}

	/**
	 * 
	 * @Method Name : deleteCommossion
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 수수료정보 목록 삭제
	 *
	 * @param commission
	 * @throws Exception
	 */
	public void deleteCommission(List<CcsCommission> commission) throws Exception {
		for (CcsCommission info : commission) {
			dao.deleteOneTable(info);
		}
	}

	/**
	 * 
	 * @Method Name : saveCommission
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 수수료 정보 저장
	 *
	 * @param commission
	 * @throws Exception
	 */
	public void saveCommission(List<CcsCommission> commission) throws Exception {

		for (CcsCommission info : commission) {
			if (CommonUtil.isNotEmpty(info.getStrCommissionRate())) {
				String CommissionRateArr[] = info.getStrCommissionRate().trim().split(",");
				
				for (String commissionRate : CommissionRateArr) {
					if (isNumber(commissionRate)) {
						info.setStoreId(SessionUtil.getStoreId());
						info.setUpdId(SessionUtil.getLoginId());
						info.setCommissionRate(new BigDecimal(commissionRate));
						info.setCommissionId(null);
						dao.insertOneTable(info);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Method Name : deleteBatchCommission
	 * @author : allen
	 * @date : 2016. 8. 3.
	 * @description : 카테고리 수수료율 일괄 삭제
	 *
	 * @param business
	 */
	public void deleteBatchCommission(CcsBusiness business) {
		dao.delete("ccs.business.deleteBatchCommission", business);
	}
	
	private boolean isNumber(String str) {
		boolean result = false;
		try {
			Double.parseDouble(str);
			result = true;
		} catch (Exception e) {
		}

		return result;
	}
}
