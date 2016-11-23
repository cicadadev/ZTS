import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = { "classpath:config/spring/context-admin.xml" })
public class JUNIT_DataGenerator {
//
//	public static String STORE_ID = "1001";
//	public static String BUSINESS_ID = "1001";
//	public static String USE_Y = "Y";
//	public static String USE_N = "N";
//
//	@Autowired
//	private BaseService baseService;
//
//	@Autowired
//	private OrderService orderService;
//
//	// @Test
//	public void bigdecimal() {
//		BigDecimal a = new BigDecimal(3);
//		BigDecimal b = new BigDecimal(2);
//		System.out.println(a.compareTo(b));
//	}
//
//	// @Test
//	@Ignore
//	public void test() {
//		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
//		System.out.println(encoder.encodePassword("1111", null));
//	}
//
//	// @Test
//	@Ignore
//	public void test1() {
//		String a = "test||test";
//		String[] b = a.split("\\|\\|");
//		System.out.println(b[0]);
//	}
//
//	@Test
////	@Ignore
//	public void test2() throws UnsupportedEncodingException {
//
////		String word = "무궁화 꽃이 피었습니다.";
//		String word = "�ʼ�����[LGD_OID] �����Դϴ�";
//		System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
//		System.out.println("utf-8 -> ksc5601       : " + new String(word.getBytes("utf-8"), "ksc5601"));
//		System.out.println("utf-8 -> x-windows-949 : " + new String(word.getBytes("utf-8"), "x-windows-949"));
//		System.out.println("utf-8 -> iso-8859-1    : " + new String(word.getBytes("utf-8"), "iso-8859-1"));
//		System.out.println("iso-8859-1 -> euc-kr        : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
//		System.out.println("iso-8859-1 -> ksc5601       : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
//		System.out.println("iso-8859-1 -> x-windows-949 : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
//		System.out.println("iso-8859-1 -> utf-8         : " + new String(word.getBytes("iso-8859-1"), "utf-8"));
//		System.out.println("euc-kr -> utf-8         : " + new String(word.getBytes("euc-kr"), "utf-8"));
//		System.out.println("euc-kr -> ksc5601       : " + new String(word.getBytes("euc-kr"), "ksc5601"));
//		System.out.println("euc-kr -> x-windows-949 : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
//		System.out.println("euc-kr -> iso-8859-1    : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));
//		System.out.println("ksc5601 -> euc-kr        : " + new String(word.getBytes("ksc5601"), "euc-kr"));
//		System.out.println("ksc5601 -> utf-8         : " + new String(word.getBytes("ksc5601"), "utf-8"));
//		System.out.println("ksc5601 -> x-windows-949 : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
//		System.out.println("ksc5601 -> iso-8859-1    : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));
//		System.out.println("x-windows-949 -> euc-kr     : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
//		System.out.println("x-windows-949 -> utf-8      : " + new String(word.getBytes("x-windows-949"), "utf-8"));
//		System.out.println("x-windows-949 -> ksc5601    : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
//		System.out.println("x-windows-949 -> iso-8859-1 : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));
//
//	}
//
//	@Ignore
//	@Test
//	public void order() throws Exception {
//		for (int i = 1; i < 30; i++) {
//			try {
//				 this.saveOrder(i);
////				this.savePickup(i);
//			} catch (EtcException e) {
//				System.out.println(">>>>>>>> EtcException : 파일없음.");
//			} catch (Exception e) {
//				System.out.println(">>>>>>>> 입력에러.................... " + e);
//			}
//		}
//	}
//
//	@Transactional
//	public void saveBrand(int no) throws Exception {
//		PmsBrand pb = new PmsBrand();
//		pb.setStoreId(STORE_ID);
//		String brandId = "BRAND_" + CommonUtil.leftPad(no + "", 10, "0");
//		pb.setBrandId(brandId);
//		pb.setName(brandId);
////		pb.setLeafYn(USE_Y);
//		pb.setInsId("ORDER");
//
//		baseService.insertOneTable(pb);
//	}
//
//	@Transactional
//	public void saveDeliverypolicy(int no) throws Exception {
//
//		Random r = new Random();
//
//		CcsDeliverypolicy cd = new CcsDeliverypolicy();
//		String deliveryPolicyNo = CommonUtil.leftPad(no + "", 10, "0");
//		cd.setStoreId(STORE_ID);
//		cd.setBusinessId(BUSINESS_ID);
//		cd.setDeliveryPolicyNo(new BigDecimal(deliveryPolicyNo));
//		cd.setName(deliveryPolicyNo);
//		cd.setDeliveryFeeTypeCd(CodeUtil.getCodeList("DELIVERY_FEE_TYPE_CD").get(r.nextInt(3)).getCd());
//		cd.setDeliveryFee(new BigDecimal(2500));
//		cd.setInsId("ORDER");
//
//		baseService.insertOneTable(cd);
//	}
//
//	public static String getNumRandom(int loopcount) {
//		String str = "";
//		int d = 0;
//		for (int i = 1; i <= loopcount; i++) {
//			Random r = new Random();
//			d = r.nextInt(9);
//			str = str + Integer.toString(d);
//		}
//		return str;
//	}
//
//	public static String getEngRandom(int loopcount) {
//		String str = "";
//		for (int i = 1; i <= loopcount; i++) {
//			char ch = (char) ((Math.random() * 26) + 97);
//			str += String.valueOf(ch);
//		}
//		return str;
//	}
//
//	public static String getKorRandom(int loopcount) {
//		String str = "";
//		for (int i = 1; i <= loopcount; i++) {
//			char ch = (char) ((Math.random() * 11172) + 0xAC00);
//			str += String.valueOf(ch);
//		}
//		return str;
//	}
//
//	@Transactional
//	@Rollback(true)
//	public void savePickup(int no) throws Exception {
//
//		System.out.println(">>>>>>>>> loop : " + no);
//		Random random = new Random();
//
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
//		String pickupDate = DateUtil.getAddDate(DateUtil.FORMAT_1, currentDate, new BigDecimal(3));
//
//		// 1. 주문
//		OmsPickup omsPickup = new OmsPickup();
//		// String pickupId = CommonUtil.leftPad(no + "", 10, "0");
//
//		omsPickup.setStoreId(STORE_ID);
//		// omsPickup.setPickupId(pickupId);
//		omsPickup.setOffshopId("1");
//		omsPickup.setMemberNo(new BigDecimal(random.nextInt(100)));
//		omsPickup.setPickupStateCd(CodeUtil.getCodeList("PICKUP_STATE_CD").get(random.nextInt(3)).getCd());
//		omsPickup.setPickupReqDt(currentDate);
//		omsPickup.setPickupReserveDt(pickupDate);
//		omsPickup.setPickupDeliveryDt(omsPickup.getPickupStateCd().equals("PICKUP_STATE_CD.DELIVERY") ? pickupDate : "");
//		omsPickup.setPickupCancelDt(omsPickup.getPickupStateCd().equals("PICKUP_STATE_CD.CANCEL") ? pickupDate : "");
//		// omsPickup.setPosMid(String posMid);
//
//		List<OmsPickupproduct> omsPickupproducts = new ArrayList<OmsPickupproduct>();
//		for (int j = 0; j <= random.nextInt(9); j++) { // 상품
//			try {
//				List<PmsSaleproduct> list = orderService.selectSaleProductList(String.valueOf(j + 1));
//				if (list == null || list.size() < 1) {
//					// throw new Exception("상품없음.");
//					continue;
//				}
//
//				// for (int k = 0; k < random.nextInt(list.size()); k++) { // 단품
//				// PmsSaleproduct pms = list.get(k);
//				for (PmsSaleproduct pms : list) { // 단품
//
//					OmsPickupproduct detail = new OmsPickupproduct();
//					detail.setStoreId(omsPickup.getStoreId());
//					// detail.setPickupId(omsPickup.getPickupId());
//					detail.setOffshopId(omsPickup.getOffshopId());
//					// detail.setProductNo(BigDecimal productNo);
//					detail.setBrandId(org.apache.commons.lang.StringUtils.defaultIfEmpty(pms.getPmsProduct().getBrandId(), "1"));
//					detail.setCategoryId(org.apache.commons.lang.StringUtils.defaultIfEmpty(pms.getPmsProduct().getCategoryId(), "2"));
//					detail.setProductTypeCd(pms.getPmsProduct().getProductTypeCd());
//					detail.setProductId(pms.getPmsProduct().getProductId());
//					detail.setErpProductId(pms.getPmsProduct().getErpProductId());
//					detail.setProductName(pms.getPmsProduct().getName());
//					detail.setSaleproductId(pms.getSaleproductId());
//					detail.setErpSaleproductId(pms.getErpSaleproductId());
//					detail.setErpColorId(pms.getErpColorId());
//					detail.setErpSizeId(pms.getErpSizeId());
//					detail.setSaleproductName(pms.getName());
//					detail.setListPrice(pms.getPmsProduct().getListPrice());
//					detail.setSalePrice(pms.getPmsProduct().getSalePrice());
//					detail.setAddSalePrice(pms.getAddSalePrice());
//					detail.setTotalSalePrice(detail.getSalePrice().add(detail.getAddSalePrice()));
//					detail.setSupplyPrice(pms.getPmsProduct().getSupplyPrice());
//					detail.setCommissionRate(pms.getPmsProduct().getCommissionRate());
//					detail.setPickupSaleproductStateCd(omsPickup.getPickupStateCd());
//					detail.setOrderQty(new BigDecimal(random.nextInt(9) + 1));
//					detail.setPickupReqDt(omsPickup.getPickupReqDt());
//					detail.setPickupDeliveryDt(omsPickup.getPickupDeliveryDt());
//					detail.setPickupCancelDt(omsPickup.getPickupCancelDt());
//					// detail.setOmsPickup(OmsPickup omsPickup);
//
//					detail.setOrderAmt(detail.getTotalSalePrice().multiply(detail.getOrderQty())); // 결제금액(상품당)
//					omsPickupproducts.add(detail);
//				}
//			} catch (Exception e) {
//				System.out.println(">>>>>>>> ERROR : " + e);
//			}
//		}
//
//		omsPickup.setOmsPickupproducts(omsPickupproducts);
//
//		// pickupService.savePickup(omsPickup);
//
//		baseService.insertOneTable(omsPickup);
//		System.out.println(">>>>> omsPickup.getPickupId() : " + omsPickup.getPickupId());
//		for (OmsPickupproduct product : omsPickup.getOmsPickupproducts()) {
//			product.setPickupId(omsPickup.getPickupId());
//			baseService.insertOneTable(product);
//			System.out.println(">>>>> product.getProductNo() : " + product.getProductNo());
//		}
//
//	}
//
//	@Transactional
//	@Rollback(true)
//	public void saveOrder(int no) throws Exception {
//
//		System.out.println(">>>>>>>>> loop : " + no);
//		Random random = new Random();
//
//		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
//
//		// 1. 주문
//		OmsOrder omsOrder = new OmsOrder();
//		String orderId = CommonUtil.leftPad(no + "", 10, "0");
//		String orderAmt = CommonUtil.rightPad(getNumRandom(3) + "", 2, "0");
//
//		omsOrder.setOrderId(orderId);
//		omsOrder.setStoreId(STORE_ID);
//		omsOrder.setOrderTypeCd(CodeUtil.getCodeList("ORDER_TYPE_CD").get(random.nextInt(6)).getCd());
//		omsOrder.setDeviceTypeCd(CodeUtil.getCodeList("DEVICE_TYPE_CD").get(random.nextInt(2)).getCd());
////		omsOrder.setChannelId(String.valueOf(random.nextInt(7)));
//		omsOrder.setChannelId("3");
//		omsOrder.setSiteId("1");
//		// omsOrder.setSiteOrderId("");
////		omsOrder.setMemberNo(new BigDecimal(random.nextInt(100)));
//		omsOrder.setMemberNo(BigDecimal.ONE);
//		// omsOrder.setSiteMemId("");
//		omsOrder.setName1(getKorRandom(3));
//		// omsOrder.setName2(String name2);
//		// omsOrder.setName3(String name3);
//		// omsOrder.setName4(String name4);
//		omsOrder.setCountryNo("82");
//		omsOrder.setPhone1("0" + getNumRandom(1) + "-" + getNumRandom(3) + "-" + getNumRandom(4));
//		omsOrder.setPhone2("0" + getNumRandom(2) + "-" + getNumRandom(4) + "-" + getNumRandom(4));
//		// omsOrder.setPhone3(String phone3);
//		omsOrder.setEmail(getEngRandom(5) + "@intune.co.kr");
//		omsOrder.setZipCd(getNumRandom(5));
//		omsOrder.setAddress1("주문자주소" + no + "주문자주소" + no + "주문자주소" + no + "주문자주소" + no + "주문자주소" + no);
//		omsOrder.setAddress2("주문자 상세주소" + no + "주문자 상세주소" + no + "주문자 상세주소" + no + "주문자 상세주소" + no + "주문자 상세주소" + no);
//		// omsOrder.setAddress3(String address3);
//		// omsOrder.setAddress4(String address4);
//		omsOrder.setGiftPhone(
//				"ORDER_TYPE_CD.GIFT".equals(omsOrder.getOrderTypeCd()) ? "0" + getNumRandom(2) + "-" + getNumRandom(4) + "-" + getNumRandom(4) : "");
//		omsOrder.setOrderAmt(new BigDecimal(orderAmt));
//		omsOrder.setOrderDt(currentDate);
//		// omsOrder.setCancelDt(String cancelDt);
//		omsOrder.setOrderStateCd(CodeUtil.getCodeList("ORDER_STATE_CD").get(random.nextInt(3)).getCd());
//		omsOrder.setOrderDeliveryStateCd(CodeUtil.getCodeList("ORDER_DELIVERY_STATE_CD").get(random.nextInt(3)).getCd());
//		omsOrder.setInsDt(currentDate);
//		omsOrder.setInsId("victor");
//
//		// 2. 배송주소
//		BigDecimal bOrderAmt = BigDecimal.ZERO;
//		BigDecimal bDelvAmt = BigDecimal.ZERO;
//		List<OmsDeliveryaddress> omsDeliveryaddresss = new ArrayList<OmsDeliveryaddress>();
//		for (int i = 0; i <= random.nextInt(2); i++) {
//			OmsDeliveryaddress address = new OmsDeliveryaddress();
//			address.setOrderId(orderId);
//			// address.setDeliveryAddressNo(BigDecimal deliveryAddressNo);
//			address.setName1(getKorRandom(3));
//			// address.setName2(String name2);
//			// address.setName3(String name3);
//			// address.setName4(String name4);
//			// address.setCountryNo(String countryNo);
//			address.setPhone1("0" + getNumRandom(1) + "-" + getNumRandom(3) + "-" + getNumRandom(4));
//			address.setPhone2("0" + getNumRandom(2) + "-" + getNumRandom(4) + "-" + getNumRandom(4));
//			// address.setPhone3(String phone3);
//			// address.setEmail(String email);
//			address.setZipCd(getNumRandom(5));
//			address.setAddress1("배송지 주소" + i + "배송지 주소" + i + "배송지 주소" + i + "배송지 주소" + i + "배송지 주소" + i + "배송지 주소" + i + "배송지 주소" + i);
//			address.setAddress2("배송지상세주소" + i + "배송지상세주소" + i + "배송지상세주소" + i + "배송지상세주소" + i + "배송지상세주소" + i + "배송지상세주소" + i);
//			// address.setAddress3(String address3);
//			// address.setAddress4(String address4);
//			address.setNote("배송시 전화주세요~");
//
//			// 3. 주문상품
//			List<BigDecimal> deliveryPolicyList = new ArrayList<BigDecimal>(); // 배송정책
//			List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();
//			for (int j = 0; j <= random.nextInt(5); j++) { // 상품
//
//				try {
//					List<PmsSaleproduct> list = orderService.selectSaleProductList(String.valueOf(j + 1));
//					if (list == null || list.size() < 1) {
//						// throw new Exception("상품없음.");
//						continue;
//					}
//
//					for (int k = 0; k < random.nextInt(list.size()); k++) { // 단품
//						PmsSaleproduct pms = list.get(k);
//						// for (PmsSaleproduct pms : list) { // 단품
//
//						OmsOrderproduct detail = new OmsOrderproduct();
//						detail.setAddPoint(BigDecimal.ZERO);
//						detail.setAddSalePrice(pms.getAddSalePrice());
//						detail.setAgeTypeCd(pms.getPmsProduct().getAgeTypeCd());
//						// detail.setAlipayTransId(String alipayTransId);
//						detail.setBoxDeliveryYn("N");
//						// detail.setBoxUnitCd(String boxUnitCd);
//						// detail.setBoxUnitQty(BigDecimal boxUnitQty);
//						detail.setBrandId(String.valueOf(random.nextInt(10)));
//						detail.setBusinessId(pms.getPmsProduct().getBusinessId());
//						detail.setBusinessName("NOTNULL");
//						detail.setBusinessProductId(pms.getPmsProduct().getBusinessProductId());
//						detail.setBusinessSaleproductId(pms.getBusinessSaleproductId());
//						// detail.setCancelDt(String cancelDt);
//						detail.setCancelQty(BigDecimal.ZERO);
//						detail.setCategoryId(String.valueOf(random.nextInt(10)));
//						detail.setCommissionRate(pms.getPmsProduct().getCommissionRate());
//						// detail.setConfirmDt(String confirmDt);
//						// detail.setCurrencyCd(String currencyCd);
//						// detail.setCurrencyPrice(BigDecimal currencyPrice);
//						// detail.setDealId(String dealId);
//						// detail.setDealName(String dealName);
//						// detail.setDealProductNo(BigDecimal dealProductNo);
//						detail.setDealTypeCd("DEAL_TYPE_CD.MEMBER");
//						// detail.setDeliveryAddressNo(deliveryAddressNo);
//						// detail.setDeliveryDt(String deliveryDt);
//						detail.setDeliveryFeeFreeYn(pms.getPmsProduct().getDeliveryFeeFreeYn());
//						// detail.setDeliveryOrderDt(String deliveryOrderDt);
//						detail.setDeliveryPolicyNo(pms.getPmsProduct().getDeliveryPolicyNo()); // 배송정책
//						// detail.setDeliveryReadyDt(String deliveryReadyDt);
//						detail.setDeliveryReserveDt(currentDate);
//						// detail.setEmsNo(String emsNo);
//						detail.setErpBusinessId("NOTNULL");
//						detail.setErpColorId(pms.getErpColorId());
//						detail.setErpProductId(pms.getPmsProduct().getErpProductId());
//						detail.setErpSaleproductId(pms.getErpSaleproductId());
//						detail.setErpSizeId(pms.getErpSizeId());
//						detail.setExchangeQty(BigDecimal.ZERO);
//						detail.setFixedDeliveryYn("N");
//						detail.setListPrice(pms.getPmsProduct().getListPrice());
//						// detail.setLpNo(String lpNo);
//						detail.setOptionYn(pms.getPmsProduct().getOptionYn());
//						// detail.setOrderAmt(detail.getTotalSalePrice().multiply(detail.getOrderQty()));
//						// detail.setOrderCouponDcAmt(BigDecimal orderCouponDcAmt);
//						// detail.setOrderCouponId(String orderCouponId);
//						// detail.setOrderCouponIssueNo(BigDecimal orderCouponIssueNo);
//						detail.setOrderDeliveryTypeCd("ORDER_DELIVERY_TYPE_CD.ORDER");
//						detail.setOrderDt(currentDate);
//						detail.setOrderId(orderId);
//						// detail.setOrderProductNo(BigDecimal orderProductNo);
//						detail.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.REQ");
//						// detail.setOrderProductStateCd(CodeUtil.getCodeList("ORDER_PRODUCT_STATE_CD").get(random.nextInt(5)).getCd());
//						detail.setOrderProductTypeCd(CodeUtil.getCodeList("ORDER_PRODUCT_TYPE_CD").get(random.nextInt(1)).getCd());
//						detail.setOrderQty(new BigDecimal(random.nextInt(9) + 1));
//						// detail.setOriginOrderProductNo(BigDecimaloriginOrderProductNo); 교환
//						detail.setOverseasPurchaseYn("N");
//						// detail.setPartnerTransId(String partnerTransId);
//						// detail.setPlusCouponDcAmt(BigDecimal plusCouponDcAmt);
//						// detail.setPlusCouponId(String plusCouponId);
//						// detail.setPlusCouponIssueNo(BigDecimal plusCouponIssueNo);
//						detail.setPointName("NOTNULL");
//						// detail.setPointSaveId(String pointSaveId);
//						detail.setPointSaveRate(pms.getPmsProduct().getPointSaveRate());
//						detail.setPointTypeCd("NOTNULL");
//						detail.setPointValue(BigDecimal.ZERO);
//						// detail.setProductCouponDcAmt(BigDecimal productCouponDcAmt);
//						// detail.setProductCouponId(String productCouponId);
//						// detail.setProductCouponIssueNo(BigDecimal productCouponIssueNo);
//						detail.setProductId(pms.getPmsProduct().getProductId());
//						detail.setProductName(pms.getPmsProduct().getName());
//						detail.setProductNoticeTypeCd(pms.getPmsProduct().getProductNoticeTypeCd());
//						detail.setProductPoint((pms.getPmsProduct().getPointSaveRate().multiply(detail.getOrderQty())).divide(new BigDecimal(100)));
//						detail.setProductTypeCd(pms.getPmsProduct().getProductTypeCd());
//						detail.setPurchaseYn("N");
//						detail.setRedeliveryQty(BigDecimal.ZERO);
//						detail.setReserveYn("N");
//						detail.setReturnQty(BigDecimal.ZERO);
//						// detail.setReviewNo(BigDecimal reviewNo);
//						// detail.setSabangOrderId(String sabangOrderId);
//						// detail.setSabangProductId(String sabangProductId);
//						// detail.setSabangSaleproductId(String sabangSaleproductId);
//						detail.setSalePrice(pms.getPmsProduct().getSalePrice());
//						detail.setSaleproductId(pms.getSaleproductId());
//						detail.setSaleproductName(pms.getName());
//						detail.setSaleTypeCd("SALE_TYPE_CD.PURCHASE");
//						// detail.setSaveDt(String saveDt);
//						// detail.setSetDcAmt(BigDecimal setDcAmt);
//						// detail.setSetDcRate(String setDcRate);
//						detail.setSetQty(BigDecimal.ZERO);
//						// detail.setShipDt(String shipDt);
//						// detail.setSiteProductId(String siteProductId);
//						detail.setStoreId(STORE_ID);
//						detail.setSupplyPrice(pms.getPmsProduct().getSupplyPrice());
//						detail.setTax(BigDecimal.ZERO);
//						detail.setTaxTypeCd(pms.getPmsProduct().getTaxTypeCd());
//						detail.setTextOptionName(pms.getPmsProduct().getTextOptionName());
//						detail.setTextOptionValue("Y".equals(detail.getTextOptionYn()) ? "텍스트옵션값" : "");
//						detail.setTextOptionYn(pms.getPmsProduct().getTextOptionYn());
//						detail.setTotalPoint(BigDecimal.ZERO);
//						detail.setTotalSalePrice(detail.getSalePrice().add(detail.getAddSalePrice())); // 총판매금액(개당)
//						// detail.setUpperOrderProductNo(BigDecimalupperOrderProductNo); 세트
//						detail.setWrapVolume(BigDecimal.ZERO);
//						detail.setWrapYn("N");
//
//						// 결제금액 세팅
//						detail.setOrderAmt(detail.getTotalSalePrice().multiply(detail.getOrderQty())); // 결제금액(상품당)
//						bOrderAmt = bOrderAmt.add(detail.getOrderAmt());
//
//						// 배송정책 세팅
//						if (!deliveryPolicyList.contains(detail.getDeliveryPolicyNo())) {
//							deliveryPolicyList.add(detail.getDeliveryPolicyNo());
//						}
//
//						omsOrderproducts.add(detail);
//					}
//				} catch (Exception e) {
//					System.out.println(">>>>>>>> ERROR : " + e);
//					System.out.println(">>>>>>>> ERROR : " + e);
//					System.out.println(">>>>>>>> ERROR : " + e);
//					System.out.println(">>>>>>>> ERROR : " + e);
//					System.out.println(">>>>>>>> ERROR : " + e);
//				}
//			}
//			address.setOmsOrderproducts(omsOrderproducts);
//			// omsOrder.setOmsPayments(List<OmsPayment> omsPayments);
//			// omsOrder.setOmsOrderproducts(List<OmsOrderproduct> omsOrderproducts);
//
//			if (deliveryPolicyList.size() < 1) {
//				continue;
////				throw new Exception("배송정책없음.");
//			}
//
//			// 4. 배송정책
//			List<OmsDelivery> omsDeliverys = new ArrayList<OmsDelivery>();
//			for (BigDecimal deliveryPolicyNo : deliveryPolicyList) {
//				// java.util.ArrayList cannot be cast to gcp.ccs.model.CcsDeliverypolicy
//				CcsDeliverypolicy policy = orderService.selectDeliveryPolicy(deliveryPolicyNo);
//
//				OmsDelivery delv = new OmsDelivery();
//
//				delv.setOrderId(orderId);
//				// delv.setDeliveryAddressNo(BigDecimal deliveryAddressNo);
//				delv.setDeliveryPolicyNo(deliveryPolicyNo);
//				delv.setStoreId(STORE_ID);
//				delv.setName(policy.getName());
//				// delv.setDeliveryServiceCd(CodeUtil.getCodeList("DELIVERY_SERVICE_CD").get(0).getCd());
//				delv.setDeliveryServiceCd(policy.getDeliveryServiceCd());
//				delv.setDeliveryFeeTypeCd(policy.getDeliveryFeeTypeCd());
//				delv.setDeliveryFee(policy.getDeliveryFee());
//				delv.setMinDeliveryFreeAmt(policy.getMinDeliveryFreeAmt());
//
//				if (bOrderAmt.compareTo(delv.getMinDeliveryFreeAmt()) > 0) {
//					delv.setOrderDeliveryFee("0");
//				} else {
//					delv.setOrderDeliveryFee(delv.getDeliveryFee().toString());
//				}
//				bDelvAmt = bDelvAmt.add(new BigDecimal(delv.getOrderDeliveryFee()));
//
//				// delv.setDeliveryCouponId(String deliveryCouponId);
//				// delv.setDeliveryCouponIssueNo(BigDecimal deliveryCouponIssueNo);
//				delv.setDeliveryCouponDcAmt(BigDecimal.ZERO);
//				delv.setApplyDeliveryFee(delv.getDeliveryFee());
//				delv.setWrapTogetherYn("N");
//				delv.setOrderWrapFee(BigDecimal.ZERO);
//				// delv.setWrapCouponId(String wrapCouponId);
//				// delv.setWrapCouponIssueNo(BigDecimal wrapCouponIssueNo);
//				// delv.setWrapCouponDcAmt(BigDecimal wrapCouponDcAmt);
//				delv.setApplyWrapFee(BigDecimal.ZERO);
//				delv.setDeliveryBurdenCd(
//						"DELIVERY_FEE_TYPE_CD.FREE".equals(delv.getDeliveryFeeTypeCd()) ? "DELIVERY_BURDEN_CD.STORE" : "DELIVERY_BURDEN_CD.BUYER");
//
//				omsDeliverys.add(delv);
//			}
//			address.setOmsDeliverys(omsDeliverys);
//
//			omsDeliveryaddresss.add(address);
//		}
//		omsOrder.setOmsDeliveryaddresss(omsDeliveryaddresss);
//
//		// 5. 결제
//		List<OmsPayment> omsPayments = new ArrayList<OmsPayment>();
//
//		List<CcsCode> payTypeList = CodeUtil.getCodeList("PAYMENT_TYPE_CD");
//
//		// 주결제 수단
//		{
//			OmsPayment pay = new OmsPayment();
//
//			// pay.setPaymentNo(BigDecimal paymentNo);
//			pay.setOrderId(orderId);
//			pay.setPaymentTypeCd(payTypeList.get(random.nextInt(4)).getCd());
//			pay.setMajorPaymentYn("Y");
//			pay.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
//			pay.setPaymentBusinessCd("123");
//			pay.setPaymentBusinessName("결제사명");
//			pay.setInstallmentCnt(new BigDecimal(1));
//			pay.setInterestFreeYn("N");
//			pay.setEscrowYn("N");
//			pay.setPaymentAmt(bOrderAmt);
//			pay.setPaymentFee(BigDecimal.ZERO);
//			pay.setPaymentDt(currentDate);
//			// pay.setCancelDt(String cancelDt);
//			pay.setPgShopId("0to7_" + getNumRandom(2));
//			pay.setPgApprovalNo("pgApprovalNo_" + getNumRandom(3));
//			// pay.setPgCancelNo(String pgCancelNo);
//			// pay.setClaimNo(BigDecimal claimNo);
//
//			omsPayments.add(pay);
//		}
//		// 보조결제 수단
//		if (bDelvAmt.compareTo(BigDecimal.ZERO) > 0) {
//			// for (int i = 0; i <= random.nextInt(2); i++) {
//			OmsPayment pay = new OmsPayment();
//
//			// pay.setPaymentNo(BigDecimal paymentNo);
//			pay.setOrderId(orderId);
//			// pay.setPaymentTypeCd(payTypeList.get(i + 5).getCd()); // 5,6,7
//			pay.setPaymentTypeCd(payTypeList.get(random.nextInt(2) + 5).getCd()); // 5,6,7
//			pay.setMajorPaymentYn("N");
//			pay.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
//			pay.setPaymentBusinessCd("123");
//			pay.setPaymentBusinessName("결제사명");
//			pay.setInstallmentCnt(new BigDecimal(1));
//			pay.setInterestFreeYn("N");
//			pay.setEscrowYn("N");
//			pay.setPaymentAmt(bDelvAmt);
//			pay.setPaymentFee(BigDecimal.ZERO);
//			pay.setPaymentDt(currentDate);
//			// pay.setCancelDt(String cancelDt);
//			// pay.setPgShopId();
//			// pay.setPgApprovalNo();
//			// pay.setPgCancelNo(String pgCancelNo);
//			// pay.setClaimNo(BigDecimal claimNo);
//
//			omsPayments.add(pay);
//			// }
//		}
//		// for (int i = 0; i < random.nextInt(payTypeList.size()); i++) {
//		// OmsPayment pay = new OmsPayment();
//		//
//		// // pay.setPaymentNo(BigDecimal paymentNo);
//		// pay.setOrderId(orderId);
//		// pay.setPaymentTypeCd(CodeUtil.getCodeList("PAYMENT_TYPE_CD").get(random.nextInt(5)).getCd());
//		// pay.setMajorPaymentYn("Y");
//		// pay.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
//		// pay.setPaymentBusinessCd("123");
//		// pay.setPaymentBusinessName("결제사명");
//		// pay.setInstallmentCnt(new BigDecimal(1));
//		// pay.setInterestFreeYn("N");
//		// pay.setEscrowYn("N");
//		// pay.setPaymentAmt(new BigDecimal(10000));
//		// pay.setPaymentFee(BigDecimal.ZERO);
//		// pay.setPaymentDt(currentDate);
//		// // pay.setCancelDt(String cancelDt);
//		// pay.setPgShopId("0to7_" + getNumRandom(2));
//		// pay.setPgApprovalNo("pgApprovalNo_" + getNumRandom(3));
//		// // pay.setPgCancelNo(String pgCancelNo);
//		// // pay.setClaimNo(BigDecimal claimNo);
//		// pay.setInsDt(currentDate);
//		// pay.setInsId("victor");
//		//
//		// omsPayments.add(pay);
//		// }
//
//		omsOrder.setOrderAmt(bOrderAmt.add(bDelvAmt));
//		omsOrder.setOmsPayments(omsPayments);
//
//		// 6. 메모
//		List<OmsOrdermemo> omsOrdermemos = new ArrayList<OmsOrdermemo>();
//
//		for (int i = 0; i < random.nextInt(3); i++) {
//			OmsOrdermemo memo = new OmsOrdermemo();
//			memo.setOrderId(orderId);
//			if (i % 2 == 0) {
//				memo.setDetail(getKorRandom(50) + "\n\r" + getKorRandom(50) + "\n\r" + getKorRandom(50));
//			} else {
//				memo.setDetail(getKorRandom(150));
//			}
//
//			omsOrdermemos.add(memo);
//		}
//
//		omsOrder.setOmsOrdermemos(omsOrdermemos);
//
//		orderService.saveOrder(omsOrder);
//	}
//
//	public void insertUnitProductPrice(String saleproductId) throws Exception {
//		// Random r = new Random();
//		// PmsPrice priceM = new PmsPrice();
//		// //priceM.setPriceTypeCd(CodeUtil.getCodeList("PRICE_TYPE_CD").get(r.nextInt(5)).getCd());
//		// priceM.setPriceTypeCd("PRICE_TYPE_CD.GENERAL");//일반가
//		// priceM.setSaleproductId(saleproductId);
//		// priceM.setApplyStartDt(DateUtil.getCurrnetTimestamp());
//		// priceM.setStoreId("1001");
//		// int price = r.nextInt(100000) / 100 * 100;
//		// int salePrice = price == 0 ? 100 : price;
//		// priceM.setSalePrice(BigDecimal.valueOf(salePrice));
//		// priceM.setListPrice(BigDecimal.valueOf(salePrice + 100));
//		// priceM.setSupplyPrice(BigDecimal.valueOf(salePrice + 100));
//		// int point = r.nextInt(1000) / 10 * 10;
//		// priceM.setSavePoint(BigDecimal.valueOf(point));
//		// baseService.insertOneTable(priceM);
//	}
//
//	private void insertUnitProduct(String productId, int no) throws Exception {
//		Random r = new Random();
//		String defaultProductId = r.nextInt(100000000) + "";
//		PmsSaleproduct pms = new PmsSaleproduct();
//		pms.setStoreId("1001");
//		// pms.setSaleproductId(defaultProductId);
//		pms.setProductId(productId);
//		pms.setName("TEST_UNIT_" + no);
//		pms.setSaleproductStateCd(CodeUtil.getCodeList("SALEPRODUCT_STATE_CD").get(r.nextInt(5)).getCd());
//		pms.setAddSalePrice(new BigDecimal("1000"));
//
//		baseService.insertOneTable(pms);
//
//		// 단품 가격
//		// insertUnitProductPrice(pms.getSaleproductId());
//
//		if (no == 1) {
//			// 대표단품
//			PmsProduct product = new PmsProduct();
//			product.setProductId(productId);
//			product.setStoreId("1001");
//			baseService.updateOneTable(product);
//		}
//	}
//
//	private void insertCategory() throws Exception {
//		PmsCategory cate = new PmsCategory();
//		cate.setStoreId("1001");
//		cate.setCategoryId("0011");
//		cate.setName("test category 1");
//		cate.setLeafYn("Y");
//		cate.setUseYn("Y");
//		baseService.insertOneTable(cate);
//	}
//
//	private void inseretProduct(int no) throws Exception {
//		Random r = new Random();
//
//		String[] yn = { "Y", "N" };
//
//		PmsProduct product = new PmsProduct();
//		product.setStoreId("1001");
//		product.setProductId(CommonUtil.leftPad(no + "", 10, "0"));
//		product.setCategoryId("1");// 표준카테고리
//		product.setBrandId("1");// 브랜드
//		product.setName("TEST_PRODUCT_" + CommonUtil.leftPad(no + "", 5, "0"));
//		// product.setSaleStateCd(CodeUtil.getCodeList("SALE_STATE_CD").get(r.nextInt(4)).getCd());//판매중
//		product.setSaleStateCd("SALE_STATE_CD.SALE");// 판매중
//		product.setDisplayYn(yn[r.nextInt(2)]);
//		// product.setMinQty(BigDecimal.valueOf(10));
//		// product.setMaxQty(BigDecimal.valueOf(20));
//		product.setTaxTypeCd(CodeUtil.getCodeList("TAX_TYPE_CD").get(r.nextInt(2)).getCd());
//		product.setPointSaveRate(BigDecimal.valueOf(10));
//		product.setSaleStartDt("2012-06-03");
//		product.setSaleEndDt("2012-06-05");
//		product.setOptionYn(yn[r.nextInt(2)]);
//		product.setOffshopPickupYn(yn[r.nextInt(2)]);
//		product.setRegularDeliveryYn(yn[r.nextInt(2)]);
//		product.setFixedDeliveryYn(yn[r.nextInt(2)]);
//		product.setWrapYn(yn[r.nextInt(2)]);
//		// product.setWrapFee(BigDecimal.valueOf(5));
//		product.setStockControlTypeCd(CodeUtil.getCodeList("STOCK_CONTROL_TYPE_CD").get(r.nextInt(3)).getCd());
//		product.setUseYn(yn[r.nextInt(2)]);
//		product.setProductTypeCd(CodeUtil.getCodeList("PRODUCT_TYPE_CD").get(r.nextInt(6)).getCd());// 상품유형
//		// product.setProductStateCd(CodeUtil.getCodeList("PRODUCT_STATE_CD").get(r.nextInt(3)).getCd());//상품상태
//		product.setAgeTypeCd("AGE_TYPE_CD.4YEAR");
//		product.setProductNoticeTypeCd("PRODUCT_NOTICE_TYPE_CD");
//		product.setUnitQty(new BigDecimal("100"));
//
//		int price = r.nextInt(100000) / 100 * 100;
//		int salePrice = price == 0 ? 100 : price;
//
//		product.setListPrice(BigDecimal.valueOf(salePrice));
//		product.setSalePrice(BigDecimal.valueOf(salePrice));
//		product.setSupplyPrice(BigDecimal.valueOf(salePrice));
//		product.setDeliveryFeeFreeYn("N");
//		product.setReserveYn("N");
//		product.setGiftYn("N");
//		product.setWrapVolume(new BigDecimal("1"));
//
//		baseService.insertOneTable(product);
//		for (int i = 1; i <= 2; i++) {
//			insertUnitProduct(product.getProductId(), i);
//		}
//
//	}
//
//	// @Test
//	// @Transactional
//	// @Rollback(false)
//	public void productInsert() throws Exception {
//
//		// 상품 등록 : 상품, 단품, 단품가격
//
//		for (int i = 1; i <= 40; i++) {
//
//			try {
//				inseretProduct(i);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Test
	public void calc() {
		BigDecimal value = new BigDecimal(3333);
		BigDecimal div = new BigDecimal(300);
		BigDecimal div2 = new BigDecimal(200);
		BigDecimal sum = div.add(div2);
		BigDecimal d1 = value.multiply(div.divide(sum, 2, RoundingMode.HALF_UP));
		BigDecimal d2 = value.multiply(div2.divide(sum, 2, RoundingMode.HALF_UP));
		BigDecimal d3 = value.subtract(d1).subtract(d2);
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
		System.out.println(d1.add(d2).add(d3));
	}
}
