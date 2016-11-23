/**
 * 
 */
package gcp.oms.service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import com.google.common.collect.Maps;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CodeService;
import gcp.common.util.ValidationUtil;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.excel.CancelApprovalBulk;
import gcp.oms.model.excel.LocationMappingBulk;
import gcp.oms.model.excel.PartnerShippingBulk;
import gcp.oms.model.excel.ShippingProcessBulk;
import gcp.pms.model.PmsSaleproduct;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.excel.ObjectMapper;
import intune.gsf.common.exceptions.EtcException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.LocaleUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : OmsExcelService.java
 * @author : brad
 * @date : 2016. 8. 24.
 * @description : order Service
 */
@Service("OmsExcelService")
public class OmsExcelService extends BaseService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SmartValidator smartValidator;

	@Autowired
	private LogisticsService logisticsService;
	
	@Autowired
	CodeService codeService;

	
	public Map<String, Object> bulkUpload(File excelFile, String uploadType) {
		Map<String, Object> result = null;
		try {
			if ("location".equals(uploadType)) {
				result = ObjectMapper.readfile(excelFile).convert(LocationMappingBulk.class).map("name");
				this.locationBulk(result, uploadType);
			}else if("shipping".equals(uploadType)){
				result = ObjectMapper.readfile(excelFile).convert(ShippingProcessBulk.class).map("name");
				this.shippingBulk(result, uploadType);
			}else if("partnerShipping".equals(uploadType)){
				result = ObjectMapper.readfile(excelFile).convert(PartnerShippingBulk.class).map("name");
				this.partnerShippingBulk(result, uploadType);
			}else if("cancelApproval".equals(uploadType)){
				result = ObjectMapper.readfile(excelFile).convert(CancelApprovalBulk.class).map("name");
				this.cancelApprovalBulk(result, uploadType);
			}
		} catch (Exception e) {
			throw new EtcException("엑셀파일 객체 맵핑 에러!!!");
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : locationBulk
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description :
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> locationBulk(Map<String, Object> result, String uploadType) {
		String storeId = SessionUtil.getStoreId();
		String userId = SessionUtil.getLoginId();

		List<LocationMappingBulk> dataList = (List<LocationMappingBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = "insert".equals(uploadType) ? ValidationUtil.Insert.class : ValidationUtil.Update.class;
		
		List<String> msgList = null;
		int rownum = 0;
		
		for (LocationMappingBulk bulk : dataList) {

			rownum++;
			try {
				
				String saleProductId = bulk.getSaleproductId();
				String updateType = CommonUtil.isEmpty(saleProductId) ? "updateLocationWithProductId" : "updateLocationWithSaleproductId";
				
				String gubun = "";
				
				if(CommonUtil.isEmpty(saleProductId)){
					gubun = "상품";
				}else{
					gubun = "단품";
				}
				
				// v1. validation product
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [" + gubun + "]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				PmsSaleproduct saleproduct = new PmsSaleproduct();
				BeanUtils.copyProperties(bulk, saleproduct);

				saleproduct.setStoreId(storeId);
				saleproduct.setUpdId(userId);
				saleproduct.setWarehouseId(BaseConstants.WAREHOUSE_ID);

				logisticsService.insertExcelNewTx(saleproduct, updateType);
				
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		return result;
	}
	
	/**
	 * 
	 * @Method Name : shippingBulk
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description :
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> shippingBulk(Map<String, Object> result, String uploadType) {

		List<ShippingProcessBulk> dataList = (List<ShippingProcessBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = "insert".equals(uploadType) ? ValidationUtil.Insert.class : ValidationUtil.Update.class;
		
		List<String> msgList = null;
		int rownum = 0;
		Set<String> history = new HashSet<>();
		List<OmsLogistics> updateList = new ArrayList<>();
		
		// 미출고 사유 코드
		CcsCodeSearch codegroupSearch = new CcsCodeSearch();
		codegroupSearch.setCdGroupCd("CANCEL_DELIVERY_REASON_CD");
		
		List<String> cancelReasonCds = new ArrayList<>();
		List<CcsCode> codelist = codeService.selectCodeList(codegroupSearch);
		for(CcsCode ccsCode : codelist){
			cancelReasonCds.add(ccsCode.getCd());
		}
		
		for (ShippingProcessBulk bulk : dataList) {
			
			bulk.setStoreId(SessionUtil.getStoreId());
			bulk.setSaleTypeCd("SALE_TYPE_CD.PURCHASE");
			rownum++;
			try {
				
				msgList = new ArrayList<String>();
				// 1. validation not empty field
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				// 소수점 제거
				BigDecimal logisticsInoutNo = bulk.getLogisticsInoutNo().setScale(0, RoundingMode.DOWN);
				BigDecimal orderProductNo = bulk.getOrderProductNo().setScale(0, RoundingMode.DOWN);
				BigDecimal outQty = bulk.getOutQty().setScale(0, RoundingMode.DOWN);
				
				//2. validation duplicate data
				String key = String.valueOf(logisticsInoutNo) + "," + bulk.getOrderId() + "," + String.valueOf(orderProductNo); 
				if(history.contains(key)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("중복오류:엑셀에 중복된 데이터가 존재합니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 그리드에 보여줄 물류 데이터 가져오기.
				OmsLogistics omsLogistics = logisticsService.selectShippingLogistics(bulk);
				//2. validation no data
				if(CommonUtil.isEmpty(omsLogistics)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("입력오류:존재하지 않는 데이터입니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 중복된 값 체크를 위해 세트에 담아둔다.
				history.add(key);
				// 미출고 사유 코드가 valid 하지 않으면 넣지 않는다.
				String cancelDeliveryReasonCd = cancelReasonCds.indexOf(bulk.getCancelDeliveryReasonCd()) > -1 ? bulk.getCancelDeliveryReasonCd() : "";
				
				omsLogistics.setOutQty(outQty);
				omsLogistics.setCancelDeliveryReasonCd(cancelDeliveryReasonCd);
				
				updateList.add(omsLogistics);
				
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		result.put("updateList", updateList);
		return result;
	}
	
	/**
	 * 
	 * @Method Name : partnerShippingBulk
	 * @author : brad
	 * @date : 2016. 10. 4.
	 * @description : 협력사 미출고 일괄등록
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> partnerShippingBulk(Map<String, Object> result, String uploadType) {

		List<PartnerShippingBulk> dataList = (List<PartnerShippingBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = "insert".equals(uploadType) ? ValidationUtil.Insert.class : ValidationUtil.Update.class;
		
		List<String> msgList = null;
		
		int rownum = 0;
		Set<String> history = new HashSet<>();
		List<OmsLogistics> updateList = new ArrayList<>();
		
		CcsCodeSearch codegroupSearch = new CcsCodeSearch();
		// 미출고사유 코드 
		List<String> cancelReasonCds = new ArrayList<>();
		codegroupSearch.setCdGroupCd("CANCEL_DELIVERY_REASON_CD");
		List<CcsCode> codeList = codeService.selectCodeList(codegroupSearch);
		for(CcsCode ccsCode : codeList){
			cancelReasonCds.add(ccsCode.getCd());
		}
		// 택배사 코드
		List<String> deliveryServiceCds = new ArrayList<>();
		codegroupSearch.setCdGroupCd("DELIVERY_SERVICE_CD");
		List<CcsCode> codeList2 = codeService.selectCodeList(codegroupSearch);
		for(CcsCode ccsCode : codeList2){
			deliveryServiceCds.add(ccsCode.getCd());
		}
		
		for (PartnerShippingBulk bulk : dataList) {
			
			bulk.setStoreId(SessionUtil.getStoreId());
			bulk.setSaleTypeCd("SALE_TYPE_CD.CONSIGN");
			rownum++;
			try {
				
				msgList = new ArrayList<String>();
				// 1. validation not empty field
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 소수점 제거
				BigDecimal logisticsInoutNo = bulk.getLogisticsInoutNo().setScale(0, RoundingMode.DOWN);
				BigDecimal orderProductNo = bulk.getOrderProductNo().setScale(0, RoundingMode.DOWN);
				BigDecimal outQty = bulk.getOutQty().setScale(0, RoundingMode.DOWN);
				
				//2. validation duplicate data
				String key = String.valueOf(logisticsInoutNo) + "," + bulk.getOrderId() + "," + String.valueOf(orderProductNo); 
				if(history.contains(key)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("중복오류:엑셀에 중복된 데이터가 존재합니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 그리드에 보여줄 물류 데이터 가져오기.
				OmsLogistics omsLogistics = logisticsService.selectShippingLogistics(bulk);
				//2. validation no data
				if(CommonUtil.isEmpty(omsLogistics)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("입력오류:존재하지 않는 데이터입니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 중복된 값 체크를 위해 세트에 담아둔다.
				history.add(key);
				// 미출고 사유 코드, 배송업체 코드가 valid 하지 않으면 넣지 않는다.
				String cancelDeliveryReasonCd = cancelReasonCds.indexOf(bulk.getCancelDeliveryReasonCd()) > -1 ? bulk.getCancelDeliveryReasonCd() : "";
				String deliveryServiceCd = deliveryServiceCds.indexOf(bulk.getDeliveryServiceCd()) > -1 ? bulk.getDeliveryServiceCd() : "";
				String invoiceNo = bulk.getInvoiceNo();
				
				omsLogistics.setCancelDeliveryReasonCd(cancelDeliveryReasonCd);
				omsLogistics.setDeliveryServiceCd(deliveryServiceCd);
				omsLogistics.setInvoiceNo(invoiceNo);
				omsLogistics.setOutQty(outQty);
				
				updateList.add(omsLogistics);
				
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		result.put("updateList", updateList);
		return result;
	}
	
	/**
	 * 
	 * @Method Name : cancelApprovalBulk
	 * @author : brad
	 * @date : 2016. 11. 5.
	 * @description : 배송승인취소 일괄등록
	 *
	 * @param result
	 * @param uploadType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> cancelApprovalBulk(Map<String, Object> result, String uploadType) {

		List<CancelApprovalBulk> dataList = (List<CancelApprovalBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = "insert".equals(uploadType) ? ValidationUtil.Insert.class : ValidationUtil.Update.class;
		
		List<String> msgList = null;
		
		int rownum = 0;
		Set<String> history = new HashSet<>();
		List<OmsLogistics> updateList = new ArrayList<>();
		
		CcsCodeSearch codegroupSearch = new CcsCodeSearch();
		// 미출고사유 코드 
		List<String> deliveryCancelReasonCds = new ArrayList<>();
		codegroupSearch.setCdGroupCd("DELIVERY_CANCEL_REASON_CD");
		List<CcsCode> codeList = codeService.selectCodeList(codegroupSearch);
		for(CcsCode ccsCode : codeList){
			deliveryCancelReasonCds.add(ccsCode.getCd());
		}
		
		for (CancelApprovalBulk bulk : dataList) {
			
			bulk.setStoreId(SessionUtil.getStoreId());
			rownum++;
			try {
				
				msgList = new ArrayList<String>();
				// 1. validation not empty field
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 소수점 제거
				BigDecimal logisticsInoutNo = bulk.getLogisticsInoutNo().setScale(0, RoundingMode.DOWN);
				BigDecimal orderProductNo = bulk.getOrderProductNo().setScale(0, RoundingMode.DOWN);
				
				//2. validation duplicate data
				String key = String.valueOf(logisticsInoutNo) + "," + bulk.getOrderId() + "," + String.valueOf(orderProductNo);
				if(history.contains(key)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("중복오류:엑셀에 중복된 데이터가 존재합니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 그리드에 보여줄 물류 데이터 가져오기.
				OmsLogistics omsLogistics = logisticsService.selectCancelApprovalList(bulk);
				//2. validation no data
				if(CommonUtil.isEmpty(omsLogistics)){
					Map<String, String> errmap = Maps.newHashMap();
					msgList.add("입력오류:존재하지 않는 데이터입니다.");
					errmap.put(rownum + " 번째 행 :", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				
				// 중복된 값 체크를 위해 세트에 담아둔다.
				history.add(key);
				// 미출고 사유 코드, 배송업체 코드가 valid 하지 않으면 넣지 않는다.
				String deliveryCancelReasonCd = deliveryCancelReasonCds.indexOf(bulk.getDeliveryCancelReasonCd()) > -1 ? bulk.getDeliveryCancelReasonCd() : "";
				omsLogistics.setDeliveryCancelReasonCd(deliveryCancelReasonCd);
				
				updateList.add(omsLogistics);
				
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		result.put("updateList", updateList);
		return result;
	}

	public boolean validate(SmartValidator smartValidator, Object model, Class<?> groupClass, List<String> errmsgList) {
		// Inject or instantiate a JSR-303 validator
		DataBinder binder = new DataBinder(model, model.getClass().getName());

		// binder.setValidator(validator);
		binder.setValidator(smartValidator);

		// validate the target object
		binder.validate(groupClass);
		// binder.validate();
		BindingResult results = binder.getBindingResult();

		if (results.hasErrors()) {
			List<FieldError> errs = results.getFieldErrors();

			for (FieldError err : errs) {

				try {
					String message = messageSource.getMessage(err, LocaleUtil.getCurrentLocale());
					// System.out.println(">>>>>>>>>>>>>> message : " + message);
					// String[] params = { err.getField() };
					// errMsg.append(" / " + MessageUtil.getMessage(err.getDefaultMessage(), params));

					// for (String code : err.getCodes()) {
					// System.out.println(">>> message code : " + code);
					// }
					errmsgList.add(message);
				} catch (Exception e) {
					System.out.println(">>>>>>>>>>>>>> e : " + e);
				}
			}
			return false;
		}
		return true;
	}
	
}
