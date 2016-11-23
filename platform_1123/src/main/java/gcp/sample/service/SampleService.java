package gcp.sample.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;

import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.service.BaseService;
import gcp.common.util.CodeUtil;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.sample.model.Sample;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;

/**
 * 
 * @Pagckage Name : gcp.sample.service
 * @FileName : SampleService.java
 * @author : dennis
 * @date : 2016. 4. 19.
 * @description : Sample 조회/등록/수정/삭제
 */
@Service("sampleService")
public class SampleService extends BaseService {

	private static final Logger	logger	= LoggerFactory.getLogger(SampleService.class);
	

	/**
	 * 
	 * @Method Name : getSampleList
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 목록 조회
	 *
	 * @return List<Sample>
	 * @throws Exception
	 */
	public List<Sample> getSampleList() throws Exception {
		return (List<Sample>) dao.selectList("sample.getSampleList", null);
	}

	/**
	 * 
	 * @Method Name : getSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample id에 해당하는 sample 조회
	 *
	 * @param sampleId
	 * @return Sample
	 * @throws Exception
	 */
	public TableSample getSample(String sampleId) throws ServiceException {

		if ("1".equals(sampleId)) {
			throw new ServiceException("sample.error");
		}

		return (TableSample) dao.selectOne("sample.getSample", sampleId);
	}

	/**
	 * 
	 * @Method Name : getSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample id에 해당하는 sample 조회
	 *
	 * @param sampleId
	 * @return Sample
	 * @throws Exception
	 */
	public TableSample getSample2(String sampleId) {

		if ("1".equals(sampleId)) {
			throw new RuntimeException();
		}

		return (TableSample) dao.selectOne("sample.getSample", sampleId);
	}

	/**
	 * 
	 * @Method Name : createSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 등록
	 *
	 * @param sample
	 * @throws Exception
	 */
	public void createSample(TableSample sample) throws Exception {
		dao.insert("sample.createSample", sample);

		for (int row = 0; row < 5; row++) {
			sample.setSampleName(sample.getSampleName() + "_" + row);
			try {
				createSampleNewTx(sample, row);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		sample.setSampleName(sample.getSampleName() + "_LAST");
		dao.insert("sample.createSample", sample);

	}

	/**
	 * 
	 * @Method Name : updateSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 수정
	 *
	 * @param sample
	 * @throws Exception
	 */
	public void updateSample(Sample sample) throws Exception {
		dao.update("sample.updateSample", sample);
	}

	private void createSampleNewTx(TableSample sample, int row) throws Exception {
			if (row == 3) {
				throw new ServiceException("TEST Error") {};
			}
		dao.insert("sample.createSample", sample);

	}

	/**
	 * 
	 * @Method Name : deleteSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 삭제
	 *
	 * @param sampleId
	 * @throws Exception
	 */
	public void deleteSample(String sampleId) throws Exception {
		dao.delete("sample.deleteSample", sampleId);
	}

	public List<DmsDisplaySearch> getPagingList(DmsDisplaySearch dds) throws Exception {
		return (List<DmsDisplaySearch>) dao.selectList("sample.getPagingList", dds);
	}

	public int getPageCnt(DmsDisplaySearch dds) throws Exception {
		return (int) dao.selectCount("sample.getPageCnt", dds);
	}
	
	

	public void insertProduct(int no) throws Exception {
		Random r = new Random();

		String[] yn = { "Y", "N" };

		PmsProduct product = new PmsProduct();
		product.setStoreId("1001");
		product.setProductId(CommonUtil.leftPad(no + "", 10, "0"));
		product.setCategoryId("1");//표준카테고리
		product.setBrandId("1");//브랜드
		product.setName("TEST_PRESENT_" + CommonUtil.leftPad(no + "", 5, "0"));
		//product.setSaleStateCd(CodeUtil.getCodeList("SALE_STATE_CD").get(r.nextInt(4)).getCd());//판매중
		product.setSaleStateCd("SALE_STATE_CD.SALE");//판매중
		product.setDisplayYn(yn[r.nextInt(2)]);
		product.setMinQty(BigDecimal.valueOf(10));
//		product.setMaxQty(BigDecimal.valueOf(20));
		product.setTaxTypeCd(CodeUtil.getCodeList("TAX_TYPE_CD").get(r.nextInt(2)).getCd());
		product.setPointSaveRate(BigDecimal.valueOf(10));
		product.setSaleStartDt("2012-06-03");
		product.setSaleEndDt("2088-06-05");
		product.setOptionYn(yn[r.nextInt(2)]);
		product.setOptionYn("N");
		product.setOffshopPickupYn(yn[r.nextInt(2)]);
		product.setRegularDeliveryYn(yn[r.nextInt(2)]);
		product.setFixedDeliveryYn(yn[r.nextInt(2)]);
		product.setWrapYn(yn[r.nextInt(2)]);
//		product.setWrapFee(BigDecimal.valueOf(5));
		product.setStockControlTypeCd(CodeUtil.getCodeList("STOCK_CONTROL_TYPE_CD").get(r.nextInt(3)).getCd());
		product.setUseYn("Y");
//		product.setProductTypeCd(CodeUtil.getCodeList("PRODUCT_TYPE_CD").get(r.nextInt(6)).getCd());//상품유형
		product.setProductTypeCd("PRODUCT_TYPE_CD.PRESENT");//상품유형
//		product.setAgeTypeCd("AGE_TYPE_CD.4YEAR");
		product.setProductNoticeTypeCd(CodeUtil.getCodeList("PRODUCT_NOTICE_TYPE_CD").get(r.nextInt(10)).getCd());
		product.setUnitQty(new BigDecimal("1"));
		product.setRegularDeliveryPrice(new BigDecimal("0"));
		product.setRegularDeliveryMaxQty(new BigDecimal("0"));
		product.setRegularDeliveryMinCnt(new BigDecimal("0"));
		product.setRegularDeliveryYn("N");
		int price = r.nextInt(100000) / 100 * 100;
		int salePrice = price == 0 ? 100 : price;
		
		product.setListPrice(BigDecimal.valueOf(salePrice));
		product.setSalePrice(BigDecimal.valueOf(salePrice));
//		product.setFixedDeliveryPrice(new BigDecimal("2500"));
		product.setSupplyPrice(BigDecimal.valueOf(salePrice));
		product.setDeliveryFeeFreeYn("N");
		product.setReserveYn("N");
		product.setGiftYn("N");
		product.setWrapVolume(new BigDecimal("1"));
		product.setCommissionRate(new BigDecimal("10"));
		product.setTextOptionYn("N");
		product.setOverseasPurchaseYn("N");
		product.setBoxDeliveryYn("N");
		
		
		dao.insertOneTable(product);
		
		
		for (int no2 = 1; no2 <= 1; no2++) {
//			insertUnitProduct(product.getProductId(), i);

			String defaultProductId = r.nextInt(100000000) + "";
			
			PmsSaleproduct pms = new PmsSaleproduct();
			pms.setStoreId("1001");
			pms.setProductId(product.getProductId());
			pms.setName("TEST_UNIT_" + no2);
			pms.setSaleproductStateCd("SALEPRODUCT_STATE_CD.SALE");
			pms.setAddSalePrice(new BigDecimal("0"));
			pms.setRealStockQty(new BigDecimal("9999"));
			pms.setSafeStockQty(new BigDecimal("9999"));
			dao.insertOneTable(pms);

			// 재고
//			insertUnitStock(pms.getSaleproductId());

//			PmsStock stock = new PmsStock();
//			stock.setStoreId("1001");
//			stock.setSaleproductId(pms.getSaleproductId());
//			stock.setTotalStockQty(BigDecimal.valueOf(999999));
//			stock.setSafeStockQty(BigDecimal.valueOf(999999));
//			stock.setRealStockQty(BigDecimal.valueOf(999999));
//			dao.insertOneTable(stock);
//
//			if (no2 == 1) {
//				//대표단품
//				PmsProduct product1 = new PmsProduct();
//				product1.setProductId(product.getProductId());
////				product1.setSaleproductId("1");
//				product1.setStoreId("1001");
//				dao.updateOneTable(product1);
//			}

		}

	}
	
	private void insertUnitProduct(String productId, int no) throws Exception {
		Random r = new Random();
		String defaultProductId = r.nextInt(100000000) + "";
		PmsSaleproduct pms = new PmsSaleproduct();
		pms.setStoreId("1001");
//		pms.setSaleproductId(defaultProductId);
		pms.setProductId(productId);
		pms.setName("TEST_UNIT_" + no);
		pms.setSaleproductStateCd(CodeUtil.getCodeList("SALEPRODUCT_STATE_CD").get(r.nextInt(5)).getCd());
		pms.setAddSalePrice(new BigDecimal("1000"));

		dao.insertOneTable(pms);

		// 재고
		insertUnitStock(pms.getSaleproductId());
		//단품 가격
//		insertUnitProductPrice(pms.getSaleproductId());

		if (no == 1) {
			//대표단품
			PmsProduct product = new PmsProduct();
			product.setProductId(productId);
//			product.setSaleproductId("1");
			product.setStoreId("1001");
			dao.updateOneTable(product);
		}


	}
	
	
	private void insertUnitStock(String saleproductId) throws Exception {
//		PmsStock stock = new PmsStock();
//		stock.setStoreId("1001");
//		stock.setSaleproductId(saleproductId);
//		stock.setTotalStockQty(BigDecimal.valueOf(999999));
//		stock.setSafeStockQty(BigDecimal.valueOf(999999));
//		stock.setRealStockQty(BigDecimal.valueOf(999999));
//		dao.insertOneTable(stock);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * data generator를 위한 service 사용금지.
	 */
	@Deprecated
	public List<PmsProduct> getProductList() throws Exception {
		return (List<PmsProduct>) dao.selectList("sample.oms_getProductList", null);
	}

	public List<PmsSaleproduct> selectSaleProductList(String productId) throws Exception {
		return (List<PmsSaleproduct>) dao.selectList("sample.oms_selectSaleProductList", productId);
//		return (PmsSaleproduct) dao.selectList("sample.oms_getProduct", productId);
	}

	public CcsDeliverypolicy selectDeliveryPolicy(BigDecimal deliveryPolicyNo) throws Exception {
		return (CcsDeliverypolicy) dao.selectOne("sample.oms_selectDeliveryPolicy", deliveryPolicyNo);
	}

	@Rollback(true)
	public boolean saveOrder(OmsOrder omsOrder) throws Exception {
		// String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		// String storeId = SessionUtil.getStoreId();

		try {

			// 1. omsOrder
			if (dao.insertOneTable(omsOrder) > 0) {
				String orderId = omsOrder.getOrderId();

				// 2. omsDeliveryaddress
				for (OmsDeliveryaddress address : omsOrder.getOmsDeliveryaddresss()) {
					BigDecimal deliveryAddressNo = (BigDecimal) dao.selectOne("sample.oms_selectDeliveryAddressNo", orderId);

					address.setOrderId(orderId);
					address.setDeliveryAddressNo(deliveryAddressNo);
					if (dao.insertOneTable(address) > 0) {
//						BigDecimal deliveryAddressNo = address.getDeliveryAddressNo();

						// 3. omsOrdersaleproduct
						System.out.println(
								">>>>>>>>>>>>>>> address.getOmsOrderproducts() : " + address.getOmsOrderproducts().size());
						for (OmsOrderproduct orderProduct : address.getOmsOrderproducts()) {

							orderProduct.setOrderId(orderId);
							orderProduct.setDeliveryAddressNo(deliveryAddressNo);
							if (dao.insertOneTable(orderProduct) < 1) {
								// error
								throw new Exception();
							}
						}

						// 4. omsDelivery
						System.out.println(">>>>>>>>>>>>>>> address.getOmsDeliverys() : " + address.getOmsDeliverys().size());
						for (OmsDelivery delv : address.getOmsDeliverys()) {
							delv.setOrderId(orderId);
							delv.setDeliveryAddressNo(deliveryAddressNo);
							if (dao.insertOneTable(delv) < 1) {
								// error
								throw new Exception();
							}
						}
					} else {
						// error
						throw new Exception();
					}
				}

				// 5. omsPayment
				for (OmsPayment omsPayment : omsOrder.getOmsPayments()) {
//					BigDecimal paymentNo = (BigDecimal) dao.selectOne("sample.oms_selectPaymentNo", omsOrder.getStoreId());

					omsPayment.setOrderId(orderId);
//					omsPayment.setPaymentNo(paymentNo);
					dao.insertOneTable(omsPayment);

					// if(주결제수단){
					// pg
					// OmsPg omsPg = new OmsPg();
					// pgService.payres(omsPg);
					// }
				}

				// 6. omsOrdermemo
//				for (OmsOrdermemo memo : omsOrder.getOmsOrdermemos()) {
//					dao.insertOneTable(memo);
//				}

			} else {
				logger.debug("################## sql error : sample.oms_insertOrder", "omsOrder 등록실패");
				throw new Exception();
			}

		} catch (Exception e) {
			logger.debug("##### orderservice.save error : " + e.getMessage());
			throw new Exception();
		}

		return true;
	}	
	
}
