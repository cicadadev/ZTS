/**
 * 
 */
package gcp.pms.service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import com.google.common.collect.Maps;

import gcp.ccs.service.BaseService;
import gcp.common.util.ValidationUtil;
import gcp.dms.model.DmsDisplaycategoryproduct;
import gcp.pms.model.PmsPricereserve;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductattribute;
import gcp.pms.model.PmsProductnotice;
import gcp.pms.model.PmsProductreserve;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSaleproductoptionvalue;
import gcp.pms.model.PmsSaleproductpricereserve;
import gcp.pms.model.excel.PmsDispCategoryBulk;
import gcp.pms.model.excel.PmsPriceReserveBulk;
import gcp.pms.model.excel.PmsProductAttrBulk;
import gcp.pms.model.excel.PmsProductBulk;
import gcp.pms.model.excel.PmsProductBulkUpdate;
import gcp.pms.model.excel.PmsProductNoticeBulk;
import gcp.pms.model.excel.PmsProductReserveBulk;
import gcp.pms.model.excel.PmsSaleProductBulk;
import gcp.pms.model.excel.PmsSaleProductOptBulk;
import gcp.pms.model.excel.PmsSaleProductPriceReserveBulk;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.excel.ObjectMapper;
import intune.gsf.common.exceptions.EtcException;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.LocaleUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.pms.service
 * @FileName : PmsExcelService.java
 * @author : dennis
 * @date : 2016. 06. 08.
 * @description : Product Service
 */
@Service("pmsExcelService")
public class PmsExcelService extends BaseService {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SmartValidator smartValidator;

	@Autowired
	ProductService productService;

	public Map<String, Object> bulkUpload(File excelFile, String uploadType) {
		Map<String, Object> result = null;
		try {
			if ("price".equals(uploadType)) {
				result = ObjectMapper.readfile(excelFile).convert(PmsPriceReserveBulk.class).map("name");
				this.priceReserve(result);
			} else if ("status".equals(uploadType)) {
				result = ObjectMapper.readfile(excelFile).convert(PmsProductReserveBulk.class).map("name");
				this.productReserve(result);
			} else if ("notice".equals(uploadType)) {
				result = ObjectMapper.readfile(excelFile).convert(PmsProductNoticeBulk.class).map("idx");
				this.noticeBulk(result);
			} else {
//				result = ObjectMapper.readfile(excelFile).convert(PmsProductBulk.class).map("name");
				if ("update".equals(uploadType)) {// 상품 일괄 변경
					result = ObjectMapper.readfile(excelFile).convert(PmsProductBulkUpdate.class).map("name");
					this.productBulkUpdate(result, uploadType);
				} else {
					result = ObjectMapper.readfile(excelFile).convert(PmsProductBulk.class).map("name");
					this.productBulk(result, uploadType);
				}
			}
		} catch (Exception e) {
			throw new EtcException("엑셀파일 객체 맵핑 에러!!!");
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : productBulkInsert
	 * @author : victor
	 * @date : 2016. 6. 13.
	 * @description :
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> productBulk(Map<String, Object> result, String uploadType) {
		String storeId = SessionUtil.getStoreId();
		String userId = SessionUtil.getLoginId();
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		List<PmsProductBulk> dataList = (List<PmsProductBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = "insert".equals(uploadType) ? ValidationUtil.Insert.class : ValidationUtil.Update.class;
		
		List<String> msgList = null;
		int rownum = 0;
		for (PmsProductBulk bulk : dataList) {

			rownum++;
			try {
				// v1. validation product
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [상품]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}

				// 1. 상품
				PmsProduct product = new PmsProduct();
				BeanUtils.copyProperties(bulk, product);

				// 2. 단품
				List<PmsSaleproduct> saleList = new ArrayList<PmsSaleproduct>();

				String[] saleArray = StringUtils.split(bulk.getPmsSaleProductArray(), "~");
				String[] saleOptArray = StringUtils.split(bulk.getPmsSaleProductOptionArray(), "~");
				for (int i = 0; i < saleArray.length; i++) {

					String[] fields = StringUtils.splitByWholeSeparatorPreserveAllTokens(saleArray[i], "|");
					// 2.1 단품
					PmsSaleProductBulk saleBulk = new PmsSaleProductBulk(fields, product.getProductId());
					// v2.1. validation saleproduct
					if (!this.validate(smartValidator, saleBulk, groupClass, msgList)) {
						continue;
					}

					PmsSaleproduct sale = new PmsSaleproduct();
					BeanUtils.copyProperties(saleBulk, sale);

					// 2.2. 단품옵션
					List<PmsSaleproductoptionvalue> optList = new ArrayList<PmsSaleproductoptionvalue>();
					String[] optArray = StringUtils.split(saleOptArray[i], ",");
					for (String opt : optArray) {
						String[] optFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(opt, "|");
						PmsSaleProductOptBulk optBulk = new PmsSaleProductOptBulk(optFields, saleBulk.getSaleproductId());
						// v2.2. validation saleproduct option
						if (!this.validate(smartValidator, optBulk, groupClass, msgList)) {
							continue;
						}
						
						PmsSaleproductoptionvalue option = new PmsSaleproductoptionvalue();
						BeanUtils.copyProperties(optBulk, option);

						option.setStoreId(storeId);
						option.setInsId(userId);
						option.setUpdId(userId);
						optList.add(option);
					}
					sale.setPmsSaleproductoptionvalues(optList);
					sale.setStoreId(storeId);
					sale.setInsId(userId);
					sale.setUpdId(userId);
					saleList.add(sale);
				}
				if (msgList.size() > 0) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [단품]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				product.setPmsSaleproducts(saleList);

				// 3. PmsProductattribute
				List<PmsProductattribute> attrList = new ArrayList<PmsProductattribute>();

				String[] attrArray = StringUtils.split(bulk.getPmsProductAttributeArray(), "~");
				for (String attr : attrArray) {
					String[] attrFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(attr, "|");
					PmsProductAttrBulk attrBulk = new PmsProductAttrBulk(attrFields, product.getProductId());
					// v3. validation saleproduct option
					if (!this.validate(smartValidator, attrBulk, groupClass, msgList)) {
						continue;
					}

					PmsProductattribute attribute = new PmsProductattribute();
					BeanUtils.copyProperties(attrBulk, attribute);

					attribute.setStoreId(storeId);
					attribute.setInsId(userId);
					attribute.setUpdId(userId);
					attrList.add(attribute);
				}
				if (msgList.size() > 0) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [상품속성]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				product.setPmsProductattributes(attrList);

				// 4. DmsDisplaycategoryproduct
				String[] dispCtgrArray = StringUtils.split(bulk.getDmsDisplayCategoryArray(), "~");

				List<DmsDisplaycategoryproduct> dispCtgrList = new ArrayList<DmsDisplaycategoryproduct>();
				for (String ctgr : dispCtgrArray) {
					String[] ctgrFields = StringUtils.splitByWholeSeparatorPreserveAllTokens(ctgr, "|");
					PmsDispCategoryBulk ctgrBulk = new PmsDispCategoryBulk(ctgrFields, product.getProductId());
					// v4. validation saleproduct option
					if (!this.validate(smartValidator, ctgrBulk, groupClass, msgList)) {
//					if (this.validate(ctgrBulk, groupClass, rownum, errorList, smartValidator) < 1) {
						continue;
					}

					DmsDisplaycategoryproduct category = new DmsDisplaycategoryproduct();
					BeanUtils.copyProperties(ctgrBulk, category);

					category.setStoreId(storeId);
					category.setInsId(userId);
					category.setUpdId(userId);
					dispCtgrList.add(category);
				}
				if (msgList.size() > 0) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [카테고리]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				product.setDmsDisplaycategoryproducts(dispCtgrList);

				// 5. PmsProductnotice
				List<PmsProductnotice> noticeList = new ArrayList<PmsProductnotice>();
				for (int i = 1; i <= 15; i++) {

					Method method = PmsProductBulk.class.getMethod("getProductNoticeFieldDetail" + i);
					Object obj = ReflectionUtils.invokeMethod(method, bulk);

					PmsProductNoticeBulk noticeBulk = new PmsProductNoticeBulk();
					noticeBulk.setProductId(product.getProductId());
					noticeBulk.setProductNoticeTypeCd(bulk.getProductNoticeTypeCd());
					noticeBulk.setProductNoticeFieldId(String.valueOf(i));
					noticeBulk.setDetail(obj == null ? "" : (String) obj);
					// v5. validation saleproduct option
					if (!this.validate(smartValidator, noticeBulk, groupClass, msgList)) {
						continue;
					}
					
					PmsProductnotice notice = new PmsProductnotice();
					BeanUtils.copyProperties(noticeBulk, notice);

					notice.setStoreId(storeId);
					notice.setInsId(userId);
					notice.setUpdId(userId);
					noticeList.add(notice);
				}
				if (msgList.size() > 0) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [고시정보]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				product.setPmsProductnotices(noticeList);
				
				
				product.setStoreId(storeId);
				product.setInsId(userId);
				product.setUpdId(userId);

				productService.insertExcelNewTx(product, "insert".equals(uploadType) ? "insertProduct" : "updateProduct");
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
	 * @Method Name : productBulkUpdate
	 * @author : roy
	 * @date : 2016. 9. 29.
	 * @description :
	 *
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> productBulkUpdate(Map<String, Object> result, String uploadType) {
		String storeId = SessionUtil.getStoreId();
		String userId = SessionUtil.getLoginId();
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		//log.info();
//		상품정보 일괄변경: 상품정보 일괄 변경(즉시반영)-> 



		List<PmsProductBulkUpdate> dataList = (List<PmsProductBulkUpdate>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		Class<?> groupClass = ValidationUtil.Update.class;

		List<String> msgList = null;
		int rownum = 0;
		for (PmsProductBulkUpdate bulk : dataList) {

			rownum++;
			try {
				// v1. validation product
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, groupClass, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [상품]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}

				if (!bulk.getProductId().matches("[0-9]+")) {
					throw new Exception(bulk.getProductId() + "유효하지 않은 형식입니다.");
				}
				// 1. 상품
				PmsProduct product = new PmsProduct();
				BeanUtils.copyProperties(bulk, product);

				product.setStoreId(storeId);
				product.setInsId(userId);
				product.setUpdId(userId);

				if (productService.existProductCheck(product) < 1)
				{
					throw new Exception("해당 상품(" + product.getProductId() + ")이 존재하지 않습니다.");
				}
				
				productService.updateExcelNewTx(product);
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> productReserve(Map<String, Object> result) throws Exception {
		String storeId = SessionUtil.getStoreId();
//		String userId = SessionUtil.getLoginId();
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		List<PmsProductReserveBulk> dataList = (List<PmsProductReserveBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		List<String> msgList = null;
		int rownum = 0;
		for (PmsProductReserveBulk bulk : dataList) {
			rownum++;
			try {
				// v1. validation
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, null, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행", msgList.toString());
					errorList.add(errmap);
					continue;
				}

				// 1. 상품변경예약
				PmsProductreserve product = new PmsProductreserve();
				BeanUtils.copyProperties(bulk, product);
				
				product.setStoreId(storeId);
//				product.setInsId(userId);
//				product.setInsDt(currentDate);
//				product.setUpdId(userId);

				productService.insertExcelNewTx(product, "insertPmsProductreserve");
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
	 * @Method Name : excelFileUpload
	 * @author : allen
	 * @date : 2016. 5. 31.
	 * @description : 상품 정보 변경 예약
	 *
	 * @param req
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> priceReserve(Map<String, Object> result) {
		String storeId = SessionUtil.getStoreId();
//		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		List<PmsPriceReserveBulk> dataList = (List<PmsPriceReserveBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");

		List<String> msgList = null;
		int rownum = 0;
		for (PmsPriceReserveBulk bulk : dataList) {
			rownum++;
			try {
				// v1. validation
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, null, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행[상품가격]", msgList.toString());
					errorList.add(errmap);
					continue;
				}

				// 1. 가격변경예약
				PmsPricereserve price = new PmsPricereserve();
				BeanUtils.copyProperties(bulk, price);

				price.setReqDt(currentDate);
				price.setReserveDt(currentDate);
				
				price.setStoreId(storeId);
//				price.setInsId(userId);
//				price.setUpdId(userId);
				
				// 2. 단품가격
				List<PmsSaleproductpricereserve> salePriceList = new ArrayList<PmsSaleproductpricereserve>();
				String[] salePriceArray = StringUtils.split(bulk.getPmsSaleproductpricereserveArray(), "~");
				for (int i = 0; i < salePriceArray.length; i++) {

					String[] fields = StringUtils.splitByWholeSeparatorPreserveAllTokens(salePriceArray[i], "|");
					PmsSaleProductPriceReserveBulk salePriceBulk = new PmsSaleProductPriceReserveBulk(fields, price.getProductId());
					// v2.1. validation saleproduct
					if (!this.validate(smartValidator, salePriceBulk, null, msgList)) {
						continue;
					}

					PmsSaleproductpricereserve saleProductReserve = new PmsSaleproductpricereserve();
					BeanUtils.copyProperties(salePriceBulk, saleProductReserve);
					
					salePriceList.add(saleProductReserve);
				}
				if (msgList.size() > 0) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행 [단품가격]:", msgList.toString());
					errorList.add(errmap);
					continue;
				}
				price.setPmsSaleproductpricereserves(salePriceList);

				productService.insertExcelNewTx(price, "insertPmsPricereserve");
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> noticeBulk(Map<String, Object> result) {
		String storeId = SessionUtil.getStoreId();
//		String userId = SessionUtil.getLoginId();
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		
		List<PmsProductNoticeBulk> dataList = (List<PmsProductNoticeBulk>) result.get("dataList");
		List<Map<String, String>> errorList = (List<Map<String, String>>) result.get("errorList");
		
		PmsProductSearch pdelete = new PmsProductSearch();
		pdelete.setProductId(dataList.get(0).getProductId());
		pdelete.setStoreId(SessionUtil.getStoreId());
		dao.delete("pms.product.deleteProductNotice", pdelete);

		List<String> msgList = null;
		int rownum = 0;
		for (PmsProductNoticeBulk bulk : dataList) {
			rownum++;
			try {
				// v1. validation
				msgList = new ArrayList<String>();
				if (!this.validate(smartValidator, bulk, null, msgList)) {
					Map<String, String> errmap = Maps.newHashMap();
					errmap.put(rownum + " 번째 행", msgList.toString());
					errorList.add(errmap);
					continue;
				}

				String sqlErr = "";
				for (int i = 1; i <= 15; i++) {
					Method method = PmsProductNoticeBulk.class.getMethod("getProductNoticeFieldDetail" + i);
					Object obj = ReflectionUtils.invokeMethod(method, bulk);

					if (!ObjectUtils.isEmpty(obj)) {
						// PmsProductNoticeBulk noticeBulk = new
						// PmsProductNoticeBulk();
						// noticeBulk.setProductId(bulk.getProductId());
						// noticeBulk.setProductNoticeTypeCd(bulk.getProductNoticeTypeCd()); 
						bulk.setProductNoticeFieldId(String.valueOf(i));
						bulk.setDetail((String) obj);
						// v5. validation saleproduct option

						PmsProductnotice notice = new PmsProductnotice();
						BeanUtils.copyProperties(bulk, notice);

						notice.setStoreId(storeId);
//						notice.setInsId(userId);
//						notice.setUpdId(userId);
//						notice.setUpdDt(currentDate);

						try {
							dao.insertOneTable(notice);
//							productService.insertExcelNewTx(notice, "updatePmsProductnotice");
						} catch (Exception e) {
							logger.error(">>>>>>>>> sql exception : " + e.getMessage());
							sqlErr += e.getMessage();
						}
					}
				}
				if (sqlErr.length() > 0) {
					throw new Exception(sqlErr);
				}
			} catch (Exception e) {
				Map<String, String> errmap = Maps.newHashMap();
				errmap.put(rownum + " 번째 행", e.getMessage());
				errorList.add(errmap);
			}
		}

		result.put("errorList", errorList);
		return result;
	}

//	public int validate(Object model, Class<?> groupClass, int rownum, List<Map<String, String>> errorList, SmartValidator smartValidator) {
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
					logger.error(">>>>>>>>>>>>>> e : " + e);
				}
			}
			return false;
		}
		return true;
	}
	/*
	public boolean validate(SmartValidator smartValidator, Object model, Class<?> groupClass, List<Map<String, String>> errorList, int rownum) {
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
			
			StringBuffer errMsg = new StringBuffer();
			for (FieldError err : errs) {
				
				try {
					String message = messageSource.getMessage(err, LocaleUtil.getCurrentLocale());
					// System.out.println(">>>>>>>>>>>>>> message : " + message);
					// String[] params = { err.getField() };
					// errMsg.append(" / " + MessageUtil.getMessage(err.getDefaultMessage(), params));
					
//					for (String code : err.getCodes()) {
//						System.out.println(">>> message code : " + code);
//					}
					errMsg.append(" / " + message);
				} catch (Exception e) {
					System.out.println(">>>>>>>>>>>>>> e : " + e);
				}
			}
			Map<String, String> errmap = Maps.newHashMap();
			errmap.put(rownum + " 번째 행", errMsg.toString().replaceFirst("/", ""));
			errorList.add(errmap);
			
			return false;
		}
		return true;
	}
	*/
}
