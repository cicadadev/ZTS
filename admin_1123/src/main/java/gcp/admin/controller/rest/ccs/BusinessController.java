package gcp.admin.controller.rest.ccs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsBusinessholiday;
import gcp.ccs.model.CcsCommission;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.search.CcsBusinessSearch;
import gcp.ccs.service.BusinessService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/ccs/business")
public class BusinessController {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private BusinessService businessService;

	/**
	 * 
	 * @Method Name : businessListPopup
	 * @author : dennis
	 * @date : 2016. 5. 25.
	 * @description : 거래처 검색 (popup)
	 *
	 * @param ccsBusinessSearch
	 * @return
	 */
	@RequestMapping(value = "/popup/list", method = RequestMethod.POST)
	public List<CcsBusiness> businessListPopup(@RequestBody CcsBusinessSearch ccsBusinessSearch) {
		ccsBusinessSearch.setStoreId(SessionUtil.getStoreId());
		ccsBusinessSearch.setLangCd(SessionUtil.getLangCd());
		
		List<CcsBusiness> list = businessService.getBusinessList(ccsBusinessSearch);
		
		if (list != null && list.size() > 0) {
			for (CcsBusiness ccsBusinessInfo : list) {
				ccsBusinessInfo.setManageMds((CommonUtil.isNotEmpty(ccsBusinessInfo.getManagerName()) ? ccsBusinessInfo.getManagerName()
						+ (CommonUtil.isNotEmpty(ccsBusinessInfo.getSalesName()) ? (", " + ccsBusinessInfo.getSalesName()) : "")
						: CommonUtil.isNotEmpty(ccsBusinessInfo.getSalesName()) ? (ccsBusinessInfo.getSalesName()) : ""));
			}
		}
		
		return list;
	}
	
	/**
	 * 업체의 배송정책 목록 조회
	 * 
	 * @Method Name : selectDeliverypolicyByBusinessId
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value = "/dilivery/policy/{businessId}", method = { RequestMethod.GET })
	public List<CcsDeliverypolicy> selectDeliverypolicyByBusinessId(@PathVariable("businessId") String businessId) {
		CcsBusinessSearch ccsBusinessSearch = new CcsBusinessSearch();
		ccsBusinessSearch.setStoreId(SessionUtil.getStoreId());
		ccsBusinessSearch.setBusinessId(businessId);
		return businessService.selectDeliverypolicyByBusinessId(ccsBusinessSearch);
	}

	/**
	 * 
	 * @Method Name : getBusinessListInfo
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체관리 화면 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsBusiness> getBusinessListInfo(@RequestBody CcsBusinessSearch search) {

		search.setStoreId(SessionUtil.getStoreId());
		List<CcsBusiness> list= businessService.getBusinessListInfo(search); 
		
		// 담당 MD 설정
		if (list != null && list.size() > 0) {
			for (CcsBusiness ccsBusinessInfo : list) {
				ccsBusinessInfo.setManageMds((CommonUtil.isNotEmpty(ccsBusinessInfo.getManagerName()) ? ccsBusinessInfo.getManagerName()
						+ (CommonUtil.isNotEmpty(ccsBusinessInfo.getSalesName()) ? (", " + ccsBusinessInfo.getSalesName()) : "")
						: CommonUtil.isNotEmpty(ccsBusinessInfo.getSalesName()) ? (ccsBusinessInfo.getSalesName()) : ""));
			}
		}
		return list;
	}

	/**
	 * 
	 * @Method Name : updateBusiness
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 정보 수정& 등록
	 *
	 * @param bu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void saveBusiness(HttpServletRequest request, @RequestBody CcsBusiness business) throws Exception {

		Object obj = SessionUtil.getLoginInfo(request);
		if (obj != null) {
			business.setStoreId(SessionUtil.getStoreId());
			business.setUpdId(SessionUtil.getLoginId());
		} else {
			business.setStoreId(Config.getString("common.storeid"));
		}

		businessService.updateBusiness(business);

	}

	/**
	 * 
	 * @Method Name : changeState
	 * @author : roy
	 * @date : 2016. 7. 28.
	 * @description : 업체 상태 변경
	 *
	 * @param bu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeState", method = RequestMethod.PUT)
	public String changeState(@RequestBody CcsBusiness business) throws Exception {

		String resultCode = "";

		try {
			business.setStoreId(SessionUtil.getStoreId());
			business.setUpdId(SessionUtil.getLoginId());
			businessService.changeState(business);
			resultCode = "success";
		} catch (Exception e) {
			resultCode = "fail";
		}

		return resultCode;
	}

	/**
	 * 
	 * @Method Name : getDeliveryList
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체 관리의 배송정책 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/delivery/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsDeliverypolicy> getDeliveryList(@RequestBody CcsBusinessSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		//배송정책 조회
		List<CcsDeliverypolicy> deliveryList = businessService.getDeliverypolicyList(search);
		return deliveryList;
	}

	/**
	 * 
	 * @Method Name : getBusinessholidayList
	 * @author : emily
	 * @date : 2016. 6. 16.
	 * @description : 업체 관리 휴일 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/holiday/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsBusinessholiday> getBusinessholidayList(@RequestBody CcsBusinessSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		//업체 휴일 조회
		List<CcsBusinessholiday> holidayList = businessService.getBusinessHolidayList(search);
		return holidayList;
	}

	/**
	 * 
	 * @Method Name : getBusinessDetail
	 * @author : emily
	 * @date : 2016. 6. 17.
	 * @description : 업체 정보 상세 조회
	 *
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value = "/{businessId}", method = { RequestMethod.GET, RequestMethod.POST })
	public CcsBusiness getBusiness(@PathVariable("businessId") String businessId) {
		CcsBusiness business = new CcsBusiness();
		business.setStoreId(SessionUtil.getStoreId());
		business.setBusinessId(businessId);
		return businessService.getBusinessDetail(business);
	}

	/**
	 * 
	 * @Method Name : updateDelivery
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 배송정책 수정
	 *
	 * @param delivery
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delivery", method = RequestMethod.PUT)
	public String updateDeliverypolicy(@RequestBody CcsDeliverypolicy delivery) throws Exception {

		String resultCode = "";
		delivery.setStoreId(SessionUtil.getStoreId());
		delivery.setUpdId(SessionUtil.getLoginId());
		try {
			businessService.updateDeliverypolicy(delivery);
			resultCode = "success";

		} catch (Exception e) {
			resultCode = "fail";
		}

		return resultCode;
	}

	/**
	 * 
	 * @Method Name : getDeliverypolicy
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 배송정책 상세 조회
	 *
	 * @param deliveryPolicyNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delivery/detail", method = { RequestMethod.POST })
	public CcsDeliverypolicy getDeliverypolicy(@RequestBody CcsDeliverypolicy delivery) throws Exception {

		delivery.setStoreId(SessionUtil.getStoreId());
		return businessService.getDeliverypolicy(delivery);
	}

	/**
	 * 
	 * @Method Name : insertDelivery
	 * @author : emily
	 * @date : 2016. 6. 19.
	 * @description : 업체관리 배송정책 등록
	 *
	 * @param deli
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delivery", method = RequestMethod.POST)
	public String insertDelivery(@RequestBody CcsDeliverypolicy deli) throws Exception {

		String resultCode = "";
		try {
			resultCode = "success";
			businessService.insertDelivery(deli);

		} catch (Exception e) {
			resultCode = "fail";
			logger.error(e);
		}

		return resultCode;
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
	@RequestMapping(value = "/delivery/delete", method = RequestMethod.POST)
	public BaseEntity deleteDeliverypolicy(@RequestBody List<CcsDeliverypolicy> poliList) throws Exception {
		BaseEntity baseMasage = new BaseEntity();

		try {
			businessService.deleteDeliverypolicy(poliList);
		} catch (Exception e) {
			baseMasage.setSuccess(false);
			baseMasage.setResultMessage(e.getMessage());
		}
		return baseMasage;
	}

	/**
	 * 
	 * @Method Name : saveHoliday
	 * @author : emily
	 * @date : 2016. 6. 20.
	 * @description : 업체관리 휴일정보 등록
	 *
	 * @param holiday
	 * @throws Exception
	 */
	@RequestMapping(value = "/holiday/save", method = { RequestMethod.POST })
	public BaseEntity saveBusinessholiday(@RequestBody List<CcsBusinessholiday> holiday) throws Exception {

		BaseEntity base = new BaseEntity();

		for (CcsBusinessholiday info : holiday) {
			info.setStoreId(SessionUtil.getStoreId());
			info.setUpdId(SessionUtil.getLoginId());
			if (CommonUtil.isNotEmpty(info.getHoliday())) {
				info.setHoliday(info.getHoliday().replace("-", "").substring(0, 8));
			}
		}

		try {
			businessService.saveHoliday(holiday);
			
		} catch (ServiceException e) {
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
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
	@RequestMapping(value = "/holiday/delete", method = RequestMethod.POST)
	public void deleteBusinessholiday(@RequestBody List<CcsBusinessholiday> holiday) throws Exception {
			businessService.deleteBusinessholiday(holiday);

	}

	/**
	 * 
	 * @Method Name : getCommossionList
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 업체수수료 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/commission/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsCommission> getCommissionList(@RequestBody CcsBusinessSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<CcsCommission>) businessService.getCommissionList(search);
	}

	/**
	 * 
	 * @Method Name : getCommissionListByCategory
	 * @author : eddie
	 * @date : 2016. 6. 30.
	 * @description : 업체/카테고리별 수수료율 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/commission/category/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsCommission> getCommissionListByCategory(@RequestBody CcsBusinessSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<CcsCommission>) businessService.getCommissionListByCategory(search);
	}

	/**
	 * 
	 * @Method Name : deleteCommossion
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 업체 수수료 삭제
	 *
	 * @param commission
	 * @throws Exception
	 */
	@RequestMapping(value = "/commission/delete", method = RequestMethod.POST)
	public void deleteCommission(@RequestBody List<CcsCommission> commission) throws Exception {
		businessService.deleteCommission(commission);
	}

	/**
	 * 
	 * @Method Name : saveCommission
	 * @author : emily
	 * @date : 2016. 6. 27.
	 * @description : 수수료 정보 저장
	 *
	 * @param commission
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commission/save", method = { RequestMethod.POST })
	public BaseEntity saveCommission(@RequestBody List<CcsCommission> commission) throws Exception {

		BaseEntity base = new BaseEntity();

		for (CcsCommission info : commission) {
			info.setUpdId(SessionUtil.getLoginId());
			info.setStoreId(SessionUtil.getStoreId());
		}
		try {
			businessService.saveCommission(commission);

		} catch (ServiceException e) {
			base.setSuccess(false);
		}
		return base;
	}
}
