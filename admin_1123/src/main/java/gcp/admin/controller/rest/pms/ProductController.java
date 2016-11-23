package gcp.admin.controller.rest.pms;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.common.util.PmsUtil;
import gcp.external.model.ErpItem;
import gcp.external.service.ErpService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsPricereserve;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductnotice;
import gcp.pms.model.PmsProductnoticefield;
import gcp.pms.model.PmsProductreserve;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSaleproductpricereserve;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.base.BasePmsProduct;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.PmsExcelService;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.ProductService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.ExcelUploadResult;

@RestController
@RequestMapping("api/pms/product")
public class ProductController {
	
	
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private ProductService	productService;

	@Autowired
	private PmsExcelService	pmsExcelService;

	@Autowired
	private ErpService		erpService;

	@Autowired
	private BrandService	brandService;
	
	@Autowired
	private ProductReviewService	productReviewService;

	/**
	 * 상품 목록 조회
	 * 
	 * @Method Name : getProductList
	 * @author : eddie
	 * @date : 2016. 8. 9.
	 * @description :
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getProductList(@RequestBody PmsProductSearch pmsProductSearch) {

		// PO 로그인일경우 business id 값 세팅
//		pmsProductSearch.setBusinessId(SessionUtil.getBusinessId());

		List<PmsProduct> list = productService.getProductList(pmsProductSearch);
		return list;
	}

	
//	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
//	public String getProductListExcel(@RequestBody PmsProductSearch pmsProductSearch) {
//
//		List<PmsProduct> list = new ArrayList<PmsProduct>();
//		pmsProductSearch.setStoreId(SessionUtil.getStoreId());
//		list = productService.getProductList(pmsProductSearch);
//
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("productId", list.get(i).getProductId());
//			map.put("name", list.get(i).getName());
//			dataList.add(map);
//		}
//		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) pmsProductSearch, dataList);
//
//		return msg;
//	}


	@RequestMapping(value = "/productListWithSaleStock", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getProductListWithSaleStock(@RequestBody List<BasePmsProduct> productList) {
		List<PmsProduct> list = productService.getProductListWithSaleStock(productList);

		return list;
	}

	/**
	 * 
	 * @Method Name : getStockList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 단품 재고 목록 조회
	 *
	 * @param pmsProductSearch
	 * @return List<PmsSaleproduct>
	 */
	@RequestMapping(value = "/stock/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsSaleproduct> getStockList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.getStockList(pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updateStockList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 단품 재고 수정
	 *
	 * @param pmsStockList
	 * @throws Exception
	 */
	@RequestMapping(value = "/stock/save", method = { RequestMethod.POST })
	public void updateStockList(@RequestBody List<PmsSaleproduct> pmsStockList) throws Exception {
		productService.updateStockList(pmsStockList);
	}

	@RequestMapping(value = "/bulk/{id}", method = RequestMethod.POST) // insert/update/reserve/price
	public ExcelUploadResult bulkupload(@PathVariable("id") String id, MultipartHttpServletRequest mRequest, Model model) {
		int totalCnt = 0;
		int failCnt = 0;
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		
		Iterator<String> files = mRequest.getFileNames();
		String uploadTempPath = Config.getString("upload.excel.path");
		try {
			while (files.hasNext()) {
				MultipartFile mfile = mRequest.getFile(files.next());
				
				File excel = FileUploadUtil.saveFile(mfile, uploadTempPath);

				if (!"img".equals(id)) {
					Map<String, Object> result = pmsExcelService.bulkUpload(excel, id);
					totalCnt = (int) result.get("totalCount");
					errorList = (List<Map<String, String>>) result.get("errorList");
					failCnt = errorList.size();
				} else {
					totalCnt = 1;
					failCnt = 0;
				}
			}
		} catch (Exception e) {
			logger.debug("##### 상품 일괄 에러 : " + e.getMessage());
		}

		ExcelUploadResult summary = new ExcelUploadResult();
		summary.setTotalCnt(totalCnt);
		summary.setFailCnt(failCnt);
		summary.setSuccessCnt(totalCnt - failCnt);
		
		if (failCnt > 0) {
			List<String> headerNmList = new ArrayList<String>();
			headerNmList.add("에러_데이터");
			headerNmList.add("에러_내용");
			
			String errFileName = "상품일괄_업로드_에러";	// TODO - property
			if ("price".equals(id)) {
				errFileName = "가격정보_변경_에러";
			} else if ("status".equals(id)) {
				errFileName = "상품정보_변경_에러";
			} else if ("notice".equals(id)) {
				errFileName = "품목정보_등록_에러";
			} else {
			}
			
			String uploadPath = Config.getString("excel.upload.path.error");
			String excelPath = ExcelUtil.createExcelFile(headerNmList, errorList, uploadPath, errFileName);
			logger.debug(">>>>>> error path : " + excelPath);
			summary.setExcelPath(excelPath);
		}

		return summary;
	}
	
	/**
	 * 상품 상세 저장
	 * 
	 * @Method Name : saveProduct
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description :
	 *
	 * @param product
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail", method = { RequestMethod.POST })
	public String saveProduct(@RequestBody PmsProduct product) throws Exception {

		product.setStoreId(SessionUtil.getStoreId());
		product.setUpdId(SessionUtil.getLoginId());

		if (CommonUtil.isEmpty(product.getProductId())) {
			return productService.insertProduct(product);
		} else {
			productService.updateProduct(product);
			return product.getProductId();
		}
	}

	/**
	 * pmsProduct 테이블 수정
	 * 
	 * @Method Name : savePmsProduct
	 * @author : eddie
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param pmsProduct
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST })
	public void savePmsProduct(@RequestBody PmsProduct pmsProduct) throws Exception {
		pmsProduct.setStoreId(SessionUtil.getStoreId());
		productService.updateOneTable(pmsProduct);
	}

	/**
	 * 상품 상세 조회
	 * @Method Name : getProductDetail
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description : 
	 *
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{productId}", method = { RequestMethod.GET })
	public BasePmsProduct getProductDetail(@PathVariable("productId") String productId) throws Exception {
		// 상품 정보 조회
		PmsProduct product = productService.getProductDetail(productId);
		
		// 현재 상태가 승인 이전인지 여부
		product.setApprovalYn(PmsUtil.isApproval(product.getSaleStateCd()) ? "Y" : "N");
		return product;
	}
	
	/**
	 * 상품 가격 예약 목록 조회
	 * 
	 * @Method Name : selectProductPriceReserveList
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description :
	 *
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/price/reserve/list", method = { RequestMethod.POST })
	public List<PmsPricereserve> selectProductPriceReserveList(@RequestBody PmsProductSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return productService.selectProductPriceReserveList(search);
	}
	/**
	 * 상품 가격 예약 등록
	 * @Method Name : saveProductPriceReserve
	 * @author : eddie
	 * @date : 2016. 6. 5.
	 * @description : 
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/price/reserve/save", method = { RequestMethod.POST })
	public void saveProductPriceReserve(@RequestBody List<PmsPricereserve> reserves) throws Exception {
		productService.saveProductPriceReserves(reserves);
	}
	
	@RequestMapping(value = "/price/reserve/delete", method = { RequestMethod.POST })
	public void deleteProductPriceReserve(@RequestBody List<PmsPricereserve> reserves) throws Exception {
		productService.deleteProductPriceReserves(reserves);
	}
	
	/**
	 * 
	 * @Method Name : getApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 상품 승인관리 조회
	 *
	 * @param pmsProductSearch
	 * @return List<PmsProduct>
	 */
	@RequestMapping(value = "/approval/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getApprovalList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.getApprovalList(pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updateApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 상품 승인 수정
	 *
	 * @param pmsProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/approval/save", method = { RequestMethod.POST })
	public void updateApprovalList(HttpServletRequest request,
			@RequestBody List<PmsProduct> pmsProductList) throws Exception {
		productService.updateProductState(pmsProductList);
	}

	/**
	 * 
	 * @Method Name : getPriceApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 가격변경 승인 조회
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/priceApproval/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getPriceApprovalList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.getPriceApprovalList(pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updatePriceApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 가격변경 승인 수정
	 *
	 * @param pmsProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/priceApproval/save", method = { RequestMethod.POST })
	public void updatePriceApprovalList(HttpServletRequest request, @RequestBody List<PmsPricereserve> pmsPricereserveList)
			throws Exception {
		String priceReserveStateCd = request.getParameter("priceReserveStateCd");
		String rejectReason = request.getParameter("rejectReason");
		productService.updatePricereserveList(pmsPricereserveList, priceReserveStateCd, rejectReason);
	}
	

	/**
	 * 
	 * @Method Name : getProductPriceHistoryList
	 * @author : emily
	 * @date : 2016. 6. 7.
	 * @description : 상품가격이력 조회 팝업
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/productPriceHistory/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsPricereserve> getProductPriceHistoryList(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsPricereserve> pricList = productService.getProductPriceHistoryList(search);

		if (pricList != null && pricList.size() > 0) {
			for (PmsPricereserve list : pricList) {
				if (list.getPmsSaleproductpricereserves() != null && list.getPmsSaleproductpricereserves().size() > 0) {
					list.setItemHistCd("단품가격이력보기");
				}
			}
		}
		return pricList;
	}
	
	/**
	 * 
	 * @Method Name : getItemPriceHistoryList
	 * @author : emily
	 * @date : 2016. 6. 7.
	 * @description : 단품가격이력 조회 팝업
	 *
	 * @param priceReserveNo
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/itemPriceHistory/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsSaleproductpricereserve> getItemPriceHistoryList(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsSaleproductpricereserve> priceList = productService.getSalproductPriceList(search);

		return priceList;
	}

	/**
	 * 
	 * @Method Name : getProductnoticeList
	 * @author : dennis
	 * @date : 2016. 6. 8.
	 * @description : 상품고시정보 조회
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/notice/list", method = { RequestMethod.POST, RequestMethod.POST })
	public List<PmsProduct> getProductnoticeList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.getProductnoticeList(pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updateNoticeList
	 * @author : dennis
	 * @date : 2016. 6. 8.
	 * @description : 상품고시정보 반려
	 *
	 * @param pmsProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/notice/save", method = { RequestMethod.POST })
	public void updateNoticeList(@RequestBody List<PmsProduct> pmsProductList) throws Exception {
		productService.updateNoticeConfirmList(pmsProductList);
	}

	/**
	 * 
	 * @Method Name : getProductnoticefieldList
	 * @author : dennis
	 * @date : 2016. 6. 8.
	 * @description : 상품고시정보 항목 조회
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/noticefield/list", method = { RequestMethod.POST, RequestMethod.POST })
	public List<PmsProductnoticefield> getProductnoticefieldList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.getProductnoticefieldList(pmsProductSearch);
	}

	/**
	 * 상품 고시 목록 조회
	 * 
	 * @Method Name : getProductNoticeTypeList
	 * @author : eddie
	 * @date : 2016. 6. 9.
	 * @description :
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/notice/listdetail", method = { RequestMethod.POST })
	public List<PmsProductnotice> getProductNoticeTypeList(@RequestBody PmsProductSearch pmsProductSearch) {
		return productService.selectProductNoticeList(pmsProductSearch);
	}
	
	/**
	 * 
	 * @Method Name : getReviewList
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품평 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/productReview/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsReview> getReviewList(@RequestBody PmsProductReviewSearch search) {

		search.setStoreId(SessionUtil.getStoreId());

		List<PmsReview> list = productReviewService.getReviewList(search);

		return list;
	}

	/**
	 * 
	 * @Method Name : getReviewDetail
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품평 상세 조회
	 *
	 * @param review
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productReview/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public PmsReview getReviewDetail(@RequestBody PmsReview review) throws Exception {

		review.setStoreId(SessionUtil.getStoreId());
		PmsReview result = productReviewService.getReviewDetail(review);
		return result;
	}

	/**
	 * 
	 * @Method Name : updateReview
	 * @author : emily
	 * @date : 2016. 6. 14.
	 * @description : 상품평 그리드 수정 & 등록
	 *
	 * @param review
	 * @throws Exception
	 */
	@RequestMapping(value = "/productReview/save", method = { RequestMethod.POST })
	public void updateReview(@RequestBody List<PmsReview> review) throws Exception {
		
		productReviewService.updateReview(review);
	}

	/**
	 * 
	 * @Method Name : update
	 * @author : roy
	 * @date : 2016. 8. 3.
	 * @description : 상품평 정보 수정
	 *
	 * @param review
	 * @throws Exception
	 */
	@RequestMapping(value = "/productReview", method = RequestMethod.PUT)
	public String update(@RequestBody PmsReview review) throws Exception {
		review.setStoreId(SessionUtil.getStoreId());
		review.setUpdId(SessionUtil.getLoginId());
		return String.valueOf(productReviewService.updatePmsReview(review));
	}
	

	/**
	 * ERP에서 상품 정보 조회
	 * @Method Name : getErpItem
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param itemid
	 */
	@RequestMapping(value = "/erp/item/{itemid}", method = { RequestMethod.GET })
	public List<ErpItem> getErpItem(@PathVariable("itemid") String itemid) {

		// erp로부터 상품정보 조회
		List<ErpItem> items = erpService.getErpItem(itemid);

		
		// 매핑 브랜드 조회
		if (items != null && CommonUtil.isNotEmpty(items.get(0).getTkrBrand())) {
			BrandSearch brandSearch = new BrandSearch();
			brandSearch.setErpBrandId(items.get(0).getTkrBrand());
			brandSearch.setStoreId(SessionUtil.getStoreId());
			PmsBrand pmsBrand = brandService.getBrandByErpBrandId(brandSearch);
			if (CommonUtil.isNotEmpty(pmsBrand)) {
				items.get(0).setPmsBrand(pmsBrand);
			}
		}
		//TODO 원산지, 제조국.. 고시정보 매핑 항목번호 조회
		return items;
	}

	/**
	 * ERP에서 상품 정보 조회 ( paging )
	 * @Method Name : getErpItemList
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param itemid
	 */
	@RequestMapping(value = "/erp/item/list", method = { RequestMethod.POST })
	public List<ErpItem> getErpItemList(@RequestBody PmsProductSearch search) {
		return erpService.getErpItemList(search);
	}
	
	/**
	 * 
	 * @Method Name : getErpItemSaleProductList
	 * @author : allen
	 * @date : 2016. 9. 1.
	 * @description : ERP 단품 정보 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/erp/item/saleProduct/list", method = { RequestMethod.POST })
	public List<ErpItem> getErpItemSaleProductList(@RequestBody PmsProductSearch search) {
		return erpService.getItemSaleProductList(search);
	}
	
	/**
	 * 상품 이미지 저장
	 * @Method Name : saveProductImages
	 * @author : eddie
	 * @throws Exception 
	 * @date : 2016. 6. 17.
	 * @description : 
	 *
	 */
	@RequestMapping(value = "/images", method = { RequestMethod.POST })
	public void saveProductImages(@RequestBody PmsProduct product) throws Exception {
		
		productService.saveProductImages(product);
		
	}
	
	/**
	 * 상품 이미지 목록 조회
	 * 
	 * @Method Name : getProductImages
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param productId
	 * @throws Exception
	 */
	@RequestMapping(value = "/images/{productId}", method = { RequestMethod.GET })
	public PmsProduct getProductImages(@PathVariable("productId") String productId) throws Exception {
		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());

		PmsProduct product = new PmsProduct();
		product.setProductId(productId);
		product.setStoreId(SessionUtil.getStoreId());
		product = (PmsProduct) productService.selectOneTable(product);
		product.setPmsProductimgs(productService.getProductImages(search));
		return product;
	}

	/**
	 * 상품의 단품 목록 조회
	 * 
	 * @Method Name : getSaleproductList
	 * @author : eddie
	 * @date : 2016. 6. 20.
	 * @description :
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/saleproduct/{productId}", method = { RequestMethod.GET })
	public List<PmsSaleproduct> getSaleproductList(@PathVariable("productId") String productId) {
		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());
		return productService.getSaleProductList(search);
	}
	/**
	 * 세트상품의 구성상품 목록 조회
	 * @Method Name : selectSetproductList
	 * @author : eddie
	 * @date : 2016. 6. 20.
	 * @description : 
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/setproduct/{productId}", method = { RequestMethod.GET })
	public List<PmsSetproduct> selectSetproductList(@PathVariable("productId") String productId) {
		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());
		return productService.selectSetproductList(search);
	}
	
	/**
	 * 단품 가격 예약 목록 조회
	 * 
	 * @Method Name : getSaleproductPriceReserveList
	 * @author : eddie
	 * @date : 2016. 6. 20.
	 * @description :
	 *
	 * @param search productId, priceReserveNo
	 * @return
	 */
	@RequestMapping(value = "/salePriceReserve/list", method = { RequestMethod.POST })
	public List<PmsSaleproductpricereserve> getSaleproductPriceReserveList(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return productService.getSaleproductPriceReserveList(search);
	}

	/**
	 * 상품 정보 예약 이력 조회
	 * 
	 * @Method Name : getProductReserveList
	 * @author : eddie
	 * @date : 2016. 7. 21.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/reserve/list", method = { RequestMethod.POST })
	public List<PmsProductreserve> getProductReserveList(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return productService.getProductReserveList(search);
	}
}
