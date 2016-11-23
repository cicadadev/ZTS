package gcp.admin.controller.rest.oms;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.ccs.model.CcsSite;
import gcp.external.service.SweetTrackerService;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.search.OmsLogisticsSearch;
import gcp.oms.service.LogisticsService;
import gcp.oms.service.OmsExcelService;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsWarehouse;
import gcp.pms.model.PmsWarehouselocation;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;
import intune.gsf.model.ExcelUploadResult;

@RestController
@RequestMapping("api/oms/logistics")
public class LogisticsController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticsController.class);

	@Autowired
	LogisticsService logisticsService;
	
	@Autowired
	OmsExcelService omsExelService;
	
	@Autowired
	SweetTrackerService sweetTrackerService;

	/**
	 * 
	 * @Method Name : getOrderProductList
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송승인 화면 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsOrderproduct> getOrderProductList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsOrderproduct> list = logisticsService.getOrderProductList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getCancelApprovalList
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송승인취소 화면 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/cancel/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getCancelApprovalList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getCancelApprovalList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : insertLogistics
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송 승인 처리
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/approval/{type}", method = { RequestMethod.PUT })
	public BaseEntity insertLogistics(@PathVariable("type") String type, @RequestBody List<OmsOrderproduct> omsOrderproductList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.insertLogistics(omsOrderproductList, type); 
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 배송승인 에러 : " + e.getMessage() + "=========");
			e.printStackTrace();
			base.setSuccess(false);
			base.setResultMessage("처리중 에러가 발생하였습니다.. \\n 관리자에게 문의하시기바랍니다.\\n ");
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getInvoiceList
	 * @author : brad
	 * @date : 2016. 7. 27.
	 * @description : 운송장 생성 대상 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/invoice/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getInvoiceList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getInvoiceList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateInvoiceNo
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 운송장 다운로드
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/invoice/{type}", method = { RequestMethod.PUT })
	public BaseEntity updateInvoiceNo(@PathVariable("type") String type) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			Map<String, String> resultMap = logisticsService.updateInvoiceNo(type);
			base.setResultMessage(resultMap.get("resultMsg"));
			
//			if("Y".equals(resultMap.get("existSbnOrder"))){
//				logisticsService.sendInvoiceInfoToSbn();
//			}
		} catch (Exception e) {
			logger.error("========= 운송장 다운로드 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : insertHanjinData
	 * @author : brad
	 * @date : 2016. 8. 3.
	 * @description : 한진 데이타 전송
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/sendData", method = { RequestMethod.PUT })
	public BaseEntity insertHanjinData(@RequestBody List<OmsLogistics> omsLogisticsList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.insertHanjinData(omsLogisticsList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 한진 데이타 전송 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			
			if(ServiceException.class.isInstance(e)){
				base.setResultMessage(e.getMessage());
			}else{
				base.setResultMessage("처리중 에러가 발생하였습니다.. \\n 관리자에게 문의하시기바랍니다.\\n");
			}
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : cancelDeliveryApproval
	 * @author : brad
	 * @date : 2016. 8. 4.
	 * @description : 배송 승인 취소
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/cancel", method = { RequestMethod.PUT })
	public BaseEntity cancelDeliveryApproval(@RequestBody List<OmsLogistics> omsLogisticsList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.updateCancelDeliveryApproval(omsLogisticsList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 배송 승인 취소 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage("처리하지 못했습니다. \\n 관리자에게 문의하시기바랍니다.\\n");
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getPickingList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 피킹리스트 화면 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/picking/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsOrderproduct> getPickingList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsOrderproduct> list = logisticsService.getPickingList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getAllLocationList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 사용하는 모든 로케이션 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/location/allList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsWarehouselocation> getAllLocationList() throws Exception {
		String warehouseId = BaseConstants.WAREHOUSE_ID;
		List<PmsWarehouselocation> list = logisticsService.getAllLocationList(warehouseId);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getLocationList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 로케이션 관리 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/location/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsWarehouselocation> getLocationList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setWarehouseId(BaseConstants.WAREHOUSE_ID);
		List<PmsWarehouselocation> list = logisticsService.getLocationList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateLocationList
	 * @author : brad
	 * @date : 2016. 8. 23.
	 * @description : 창고 로케이션 업데이트
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/location/save", method = { RequestMethod.POST })
	public BaseEntity updateLocationList(@RequestBody List<PmsWarehouselocation> locationList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			logisticsService.updateLocationList(locationList);
		} catch (Exception e) {
			logger.error("========= 로케이션 관리 업데이트 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getLocationMappingList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 창고 로케이션 매핑 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/location/mapping/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsSaleproduct> getLocationMappingList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		List<PmsSaleproduct> list = logisticsService.getLocationMappingList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getMappingSaleProductList
	 * @author : brad
	 * @date : 2016. 8. 23.
	 * @description : 창고 로케이션 매핑 수정 단품 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/location/mapping/saleproduct/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsSaleproduct> getMappingSaleProductList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		List<PmsSaleproduct> list = logisticsService.getMappingSaleProductList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateMappingLocationId
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description : 창고 로케이션 매핑 개별수정
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/location/mapping/update", method = { RequestMethod.PUT })
	public BaseEntity updateMappingLocationId(@RequestBody List<PmsSaleproduct> pmsSaleproductList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			logisticsService.updateMappingLocationId(pmsSaleproductList);
		} catch (Exception e) {
			logger.error("========= 배송 승인 취소 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : bulkupload
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description : 물류파트 엑셀 일괄수정
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bulk/{id}", method = RequestMethod.POST) 
	public ExcelUploadResult bulkupload(@PathVariable("id") String id, MultipartHttpServletRequest mRequest, Model model) {
		int totalCnt = 0;
		int failCnt = 0;
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		List<OmsLogistics> updateList = new ArrayList<>();
		
		Iterator<String> files = mRequest.getFileNames();
		String uploadTempPath = Config.getString("upload.excel.path");
		
		try {
			while (files.hasNext()) {
				MultipartFile mfile = mRequest.getFile(files.next());
				
				File excel = FileUploadUtil.saveFile(mfile, uploadTempPath);

				if (!"img".equals(id)) {
					Map<String, Object> result = omsExelService.bulkUpload(excel, id);
					totalCnt = (int) result.get("totalCount");
					errorList = (List<Map<String, String>>) result.get("errorList");
					updateList = (List<OmsLogistics>) result.get("updateList");
					failCnt = errorList.size();
				} else {
					totalCnt = 1;
					failCnt = 0;
				}
			}
		} catch (Exception e) {
			logger.debug("##### 물류 일괄수정 에러 : " + e.getMessage());
		}

		ExcelUploadResult summary = new ExcelUploadResult();
		summary.setTotalCnt(totalCnt);
		summary.setFailCnt(failCnt);
		summary.setSuccessCnt(totalCnt - failCnt);
		summary.setSuccessList(updateList);
		
		if (failCnt > 0) {
			List<String> headerNmList = new ArrayList<String>();
			headerNmList.add("에러_데이터");
			headerNmList.add("에러_내용");
			
			String errFileName = "";
			if ("location".equals(id)) {
				errFileName = "상품_로케이션_매핑_에러";
			} else {
				errFileName = "미출고_일괄등록_에러";
			} 
			
			String uploadPath = Config.getString("excel.upload.path.error");
			String excelPath = ExcelUtil.createExcelFile(headerNmList, errorList, uploadPath, errFileName);
			logger.debug(">>>>>> error path : " + excelPath);
			summary.setExcelPath(excelPath);
		}

		return summary;
	}
	
	/**
	 * 
	 * @Method Name : getAllSiteList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 사이트 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/siteList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsSite> getAllSiteList() throws Exception {
		List<CcsSite> list = logisticsService.getAllSiteList(SessionUtil.getStoreId());
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getAllWarehouseList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 창고 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/warehouseList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsWarehouse> getAllWarehouseList() throws Exception {
		List<PmsWarehouse> list = logisticsService.getAllWarehouseList();
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getShippingProcessList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 출고완료처리 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/shipping/process/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getShippingProcessList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getShippingProcessList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateShippingConfirm
	 * @author : brad
	 * @date : 2016. 9. 5.
	 * @description : 출고완료처리
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/shipping/confirm", method = { RequestMethod.PUT })
	public BaseEntity updateShippingConfirm(@RequestBody List<OmsLogistics> omsLogisticsList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.updateShippingConfirm(omsLogisticsList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 출고 완료 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getShippingList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 출고/미출고 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/shipping/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getShippingList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getShippingList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updatePartnerInvoiceNo
	 * @author : brad
	 * @date : 2016. 11. 4.
	 * @description : 협력사 운송장번호 업데이트
	 *
	 * @param logisticsSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shipping/save", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity updatePartnerInvoiceNo(@RequestBody List<OmsLogistics> omsLogisticsList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.updatePartnerInvoiceNo(omsLogisticsList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 협력사 운송장번호 업데이트 에러 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage("처리중 에러가 발생하였습니다.. \\n 관리자에게 문의하시기바랍니다.\\n");
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getPartnerDeliveryList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 배송의뢰서_협력사
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/partner/delivery/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsOrderproduct> getPartnerDeliveryList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsOrderproduct> list = logisticsService.getPartnerDeliveryList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : insertPartnerLogistics
	 * @author : brad
	 * @date : 2016. 9. 10.
	 * @description : 협력사 배송 승인
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/partner/approval", method = { RequestMethod.PUT })
	public BaseEntity insertPartnerLogistics(@RequestBody List<OmsOrderproduct> omsOrderproductList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.insertPartnerLogistics(omsOrderproductList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 운송장 다운로드 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage(e.getMessage());
		}
		return base;
	}
	
	/**
	 * 
	 * @Method Name : getPartnerShippingList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 출고완료처리_협력사
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/partner/shipping/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getPartnerShippingList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getPartnerShippingList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getReturnConfirmList
	 * @author : brad
	 * @date : 2016. 8. 11.
	 * @description : 반품 확인 리스트
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/return/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getReturnConfirmList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getReturnConfirmList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getPartnerReturnConfirmList
	 * @author : brad
	 * @date : 2016. 8. 11.
	 * @description : 반품 확인 협력사 리스트
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "partner/return/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsLogistics> getPartnerReturnConfirmList(@RequestBody OmsLogisticsSearch logisticsSearch) throws Exception {
		logisticsSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsLogistics> list = logisticsService.getReturnConfirmList(logisticsSearch);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : updateReturnConfirm
	 * @author : brad
	 * @date : 2016. 9. 15.
	 * @description : 반품 처리
	 *
	 * @param search
	 * @return
	 */
	
	@RequestMapping(value = "/return/confirm", method = { RequestMethod.PUT })
	public BaseEntity updateReturnConfirm(@RequestBody List<OmsLogistics> omsLogisticsList) throws Exception {
		BaseEntity base = new BaseEntity();
		
		try {
			String result = logisticsService.updateReturnConfirm(omsLogisticsList);
			base.setResultMessage(result);
		} catch (Exception e) {
			logger.error("========= 운송장 다운로드 : " + e.getMessage() + "=========");
			base.setSuccess(false);
			base.setResultMessage("처리중 에러가 발생하였습니다.. \\n 관리자에게 문의하시기바랍니다.\\n");
		}
		return base;
	}

}
