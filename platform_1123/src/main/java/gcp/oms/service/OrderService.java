package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderTms;
import gcp.oms.model.OmsOrdercoupon;
import gcp.oms.model.OmsOrdermemo;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPresentproduct;
import gcp.oms.model.OmsRegulardeliveryschedule;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.SpsPresentproduct;
import gcp.sps.service.DealService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : OrderService.java
 * @author : dennis
 * @date : 2016. 4. 19.
 * @description : 주문 service
 */
@Service("orderService")
public class OrderService extends BaseService {
	private static final Logger	logger	= LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private PaymentService		paymentService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private CommonService		commonService;
	
	@Autowired
	private ProductService		productService;

	@Autowired
	private DealService			dealService;

	@Autowired
	private CartService			cartService;

	@Autowired
	private OrderTmsService		orderTmsService;

	@Autowired
	private OrderPromotionService	orderPromotionService;

	//////////////////////// START BO ORDER ////////////////////////////////
	/**
	 * 
	 * @Method Name : selectList
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : 주문목록 조회
	 *
	 * @param orderSearch
	 * @return List<OmsOrder>
	 * @throws Exception
	 */
	public List<?> selectList(String queryId, Object object) throws Exception {
		return (List<?>) dao.selectList(queryId, object);
	}

	public OmsOrder selectOne(String orderId) throws Exception {
		return (OmsOrder) this.selectOne("oms.order.select.one", orderId);
	}

	public Object selectOne(String queryId, Object object) throws Exception {
		return dao.selectOne(queryId, object);
	}

	public int insert(Object object) throws Exception {
		return dao.insertOneTable(object);
	}

	public int update(Object object) throws Exception {
		return dao.updateOneTable(object);
	}
	
	public int updateOption(OmsOrderproduct orderProduct) throws Exception {
		// 취소,교환,반품,
		// 옵션변경,재배송

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		orderProduct.setInsDt(currentDate);
		orderProduct.setUpdDt(currentDate);

		// 1. 단품갱신
		int updCnt = 0;
		if (dao.insert("oms.order.update.updateOption", orderProduct) > 0) {
			// 2. 갱신된 단품 재고차감
			this.updateStock(orderProduct, orderProduct.getOrderQty().negate());

			// 3. 기존 단품 재고증감
			BigDecimal bCancelQty = orderProduct.getOrderQty().subtract(orderProduct.getCancelQty());
			this.updateStock(orderProduct, bCancelQty);
			updCnt = 1;
		}
		return updCnt;
	}
	
	public void updateStock(OmsOrderproduct orderProduct, BigDecimal stockQty) throws Exception {
		if (!"ORDER_PRODUCT_TYPE_CD.WRAP".equals(orderProduct.getOrderProductTypeCd())) {
			PmsSaleproduct stock = new PmsSaleproduct();
			stock.setStoreId(orderProduct.getStoreId());
			stock.setSaleproductId(orderProduct.getSaleproductId());
			stock.setRealStockQty(stockQty);
			stock.setUpdId(orderProduct.getUpdId());
			stock.setUpdDt(orderProduct.getUpdDt());

			dao.update("pms.product.updateStock", stock);

			if (!stock.getResult().equals("SUCCESS")) {
				throw new ServiceException("oms.common.error", new String[] { stock.getMsg() });
			} else {
				PmsProductSearch search = new PmsProductSearch();
				search.setStoreId(orderProduct.getStoreId());
				search.setProductId(orderProduct.getProductId());
				search.setUpdId(orderProduct.getUpdId());
				search.setUpdDt(orderProduct.getUpdDt());

				if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(orderProduct.getOrderProductTypeCd())) {
					productService.updateProductStateOne(search);
				}
			}

			// 딜재고 차감
			if (!StringUtils.isEmpty(orderProduct.getDealId())) {
				OmsOrderproduct dealproduct = new OmsOrderproduct();
				dealproduct.setStoreId(orderProduct.getStoreId());
				dealproduct.setProductId(orderProduct.getProductId());
				dealproduct.setDealId(orderProduct.getDealId());
				dealproduct.setDealProductNo(orderProduct.getDealProductNo());

				dealproduct.setOrderQty(stockQty);
				dealproduct.setProductName(orderProduct.getProductName());
				dealproduct.setSaleproductName(orderProduct.getSaleproductName());

				this.updateDealStockQty(dealproduct, false, "");
			}
		}
	}

	public int delete(Object order) throws Exception {
		return dao.deleteOneTable(order);
	}

	@SuppressWarnings("unchecked")
	public List<OmsOrdermemo> selectMemo(OmsOrdermemo memo) throws Exception {
		return (List<OmsOrdermemo>) dao.selectList("oms.order.select.memo", memo);
	}

	public int insertMemo(OmsOrdermemo memo) throws Exception {
		return dao.insertOneTable(memo);
	}
	
	//////////////////////// END BO ORDER ////////////////////////////////
	
	
	public OmsOrder getOrderLoginSearch(OmsOrder omsOrder) {

//		String orderPwd = omsOrder.getOrderPwd();
//		if (orderPwd != null) {
//			ShaPasswordEncoder encoder = new ShaPasswordEncoder();
//			omsOrder.setOrderPwd(encoder.encodePassword(orderPwd, null));
//		}

		String phone2 = omsOrder.getPhone2();
		omsOrder.setPhone2(phone2.replaceAll("-", ""));

		return (OmsOrder) dao.selectOne("oms.order.getOrderLoginSearch", omsOrder);
	}


	public OmsOrder getOmsOrderByCart(OmsCart omsCart) {
		return (OmsOrder) dao.selectOne("oms.order.getOmsOrderByCart", omsCart);
	}



	public String getNewOrderId() {
		return (String) dao.selectOne("oms.order.getNewOrderId", null);
	}

	/**
	 * 
	 * @Method Name : getOrderList
	 * @author : dennis
	 * @date : 2016. 6. 30.
	 * @description :
	 *
	 * @param omsOrder
	 * @return
	 * @throws Exception
	 * 
	 */
	public OmsOrder getOrderList(OmsOrder omsOrder) throws Exception {

		/*
		 * omsOrder - spsPresents (주문사은품)
		 * 			- omsOrderproducts (주문상품) - spsPresents (상품사은품)
		 * 										  - omsOrderproducts (SET SUB)
		 * 
		 * 
		 * 제어
		 * 	상품,사은품,deal
		 */

		String orderStat = omsOrder.getOrderStat();
		String orderTypeCd = omsOrder.getOrderTypeCd();
		String storeId = omsOrder.getStoreId();
		String channelId = SessionUtil.getChannelId();	//제휴채널		

		omsOrder.setChannelId(channelId);

		BigDecimal memberNo = omsOrder.getMemberNo();
		String deviceTypeCd = omsOrder.getDeviceTypeCd();

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);
		String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

		omsOrder.setMemberTypeCds(memberTypeCds);
		omsOrder.setMemGradeCd(memGradeCd);

		List<String> dealTypeList = dealService.convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		String selectPresent = omsOrder.getSelectPresent();
		Map<String, String> selectPresentMap = null;
		if (CommonUtil.isNotEmpty(selectPresent)) {
			selectPresentMap = orderPromotionService.makePresent(selectPresent);
		}

		//주문상품
		List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();

		//주문사은품 목록
		List<SpsPresent> orderPresentList = new ArrayList<SpsPresent>();

		//주문쿠폰 목록
		List<OmsOrdercoupon> orderCouponList = new ArrayList<OmsOrdercoupon>();

		//상품쿠폰 목록
		List<OmsOrdercoupon> productCouponList = new ArrayList<OmsOrdercoupon>();

		BigDecimal orderProductNo = new BigDecimal(0);

		//총판매가(주문)
		BigDecimal totalOrderSalePrice = new BigDecimal(0);

		for (OmsOrderproduct omsOrderproduct : omsOrder.getOmsOrderproducts()) {
			//주문상품 조회
			OmsOrderproduct orderproduct = new OmsOrderproduct();
			omsOrderproduct.setMemGradeCd(memGradeCd);
			omsOrderproduct.setDealTypeCds(dealTypeCds);
			orderproduct = (OmsOrderproduct) dao.selectOne("oms.order.getOmsOrderproductByPms", omsOrderproduct);

			String orderProductTypeCd = orderproduct.getOrderProductTypeCd();

			if ("ORDER_PRODUCT_TYPE_CD.PRESENT".equals(orderProductTypeCd)) {
				throw new ServiceException("oms.order.noPresent");
			}

			BigDecimal orderQty = orderproduct.getOrderQty();

			String msg = "상품번호 : " + omsOrderproduct.getProductId() + " / 단품번호 : " + omsOrderproduct.getSaleproductId();

			if (orderproduct == null) {
				throw new ServiceException("oms.order.nonExProduct", new String[] { msg });
			}

			String saleStateCd = orderproduct.getSaleStateCd();

			if (!"SALE_STATE_CD.SALE".equals(saleStateCd)) {
				throw new ServiceException("oms.order.nonSale", new String[] {
						CommonUtil.makeProductErrorName(orderproduct.getProductName(), orderproduct.getSaleproductName()) });
			}

			orderproduct.setOrderProductNo(orderProductNo);

			//상품 제어 check
			BigDecimal controlNo = orderproduct.getControlNo();
			if (CommonUtil.isNotEmpty(controlNo)) {
				//제어 check (Exception 처리)
				CcsControlSearch search = new CcsControlSearch();
				search.setExceptionFlag(true);
				search.setStoreId(storeId);
				search.setControlNo(controlNo);
				search.setChannelId(channelId);
				search.setMemberNo(memberNo);
				search.setDeviceTypeCd(deviceTypeCd);
				search.setMemberTypeCds(memberTypeCds);
				search.setMemGradeCd(memGradeCd);
				search.setApplyObjectName("상품," + orderproduct.getProductName());
				search.setControlType("PRODUCT");
				commonService.checkControl(search);
			}
			//최소주문가능 수량 check
			if (orderproduct.getMinQty().compareTo(orderproduct.getOrderQty()) > 0) {
				throw new ServiceException("oms.order.minQtyError",
						new String[] { orderproduct.getProductName(), orderproduct.getMinQty().toString() });
			}

			//상품인당 주문수량check
			Map map = new HashMap();
			map.put("memberNo", omsOrder.getMemberNo());
			map.put("productId", omsOrderproduct.getProductId());
			BigDecimal curOrderQty = (BigDecimal) dao.selectOne("oms.order.getMemberOrderQty", map);
			BigDecimal personQty = orderproduct.getPersonQty();

			if (CommonUtil.isNotEmpty(personQty) && personQty.compareTo(new BigDecimal(0)) == 0) {

			} else {
				if (curOrderQty.compareTo(personQty) >= 0) {
					throw new ServiceException("oms.order.personQtyError",
							new String[] { orderproduct.getProductName(), personQty.toString() });
				}
			}


			BigDecimal upperOrderProductNo = orderProductNo;

			logger.debug("=============> orderProductNo : " + orderProductNo);
			logger.debug("=============> orderProductTypeCd : " + orderProductTypeCd);
			orderProductNo = orderProductNo.add(new BigDecimal(1));


			//SET SUB
			if (BaseConstants.ORDER_PRODUCT_TYPE_CD_SET.equals(orderProductTypeCd)) {
				List<OmsOrderproduct> subProducts = new ArrayList<OmsOrderproduct>();

				BigDecimal subTotalSaleAmt = new BigDecimal(0);	//구성상품 금액(상품당)

				for (OmsOrderproduct subOrderproduct : omsOrderproduct.getOmsOrderproducts()) {
					logger.debug("선택 : productId : " + subOrderproduct.getProductId());
					logger.debug("선택 : saleproductId : " + subOrderproduct.getSaleproductId());
					OmsOrderproduct subProduct = new OmsOrderproduct();
					subOrderproduct.setMemGradeCd(memGradeCd);
					subOrderproduct.setDealTypeCds(dealTypeCds);

					subProduct = (OmsOrderproduct) dao.selectOne("oms.order.getOmsOrderproductByPms", subOrderproduct);

					msg = "상품번호 : " + omsOrderproduct.getProductId() + " / 단품번호 : " + omsOrderproduct.getSaleproductId();
					msg += " / 구성상품번호 : " + subOrderproduct.getProductId() + " / 구성단품번호 : " + subOrderproduct.getSaleproductId();

					if (subProduct == null) {
						throw new ServiceException("oms.order.nonExProduct", new String[] { msg });
					}

					String subsaleStateCd = subProduct.getSaleStateCd();

					if (!"SALE_STATE_CD.SALE".equals(subsaleStateCd)) {
						throw new ServiceException("oms.order.nonSale", new String[] {
								CommonUtil.makeProductErrorName(subProduct.getProductName(), subProduct.getSaleproductName()) });
					}

					subProduct.setOrderProductNo(orderProductNo);
					subProduct.setUpperOrderProductNo(upperOrderProductNo);
					subProduct.setOrderProductTypeCd(BaseConstants.ORDER_PRODUCT_TYPE_CD_SUB);

					PmsSetproduct pmsSetproduct = new PmsSetproduct();
					pmsSetproduct.setStoreId(storeId);
					pmsSetproduct.setProductId(orderproduct.getProductId());
					pmsSetproduct.setSubProductId(subProduct.getProductId());

					pmsSetproduct = (PmsSetproduct) dao.selectOneTable(pmsSetproduct);		//set 정보
					subProduct.setProductName(pmsSetproduct.getName());

					BigDecimal setQty = pmsSetproduct.getQty();	//set 구성 수량
					subProduct.setSetQty(setQty);

					subTotalSaleAmt = subTotalSaleAmt.add(subProduct.getListPrice().multiply(setQty));

					//구성단품 재고 check
					BigDecimal suborderQty = setQty.multiply(orderQty);
					BigDecimal realStockQty = subProduct.getRealStockQty();
					if (suborderQty.compareTo(realStockQty) > 0) {
						throw new ServiceException("oms.order.nonStockQty", new String[] {
								CommonUtil.makeProductErrorName(subProduct.getProductName(), subProduct.getSaleproductName()) });
					}

					subProducts.add(subProduct);
					logger.debug("=============> orderProductNo : " + orderProductNo);
					orderProductNo = orderProductNo.add(new BigDecimal(1));
				}

				//sub 총금액
				orderproduct.setSetTotalSalePrice(subTotalSaleAmt);

				//(subTotalSalePrice - totalSalePrice)
				//주문생성시 수량 곱함.
//				BigDecimal setDcAmt = subTotalSaleAmt.add(orderproduct.getTotalSalePrice().multiply(new BigDecimal(-1)));

				//setDcAmt * 100 / subTotalSalePrice
//				BigDecimal setDcRate = setDcAmt.multiply(new BigDecimal(100)).divide(subTotalSaleAmt, 2,
//						BigDecimal.ROUND_HALF_UP);

//				orderproduct.setSetDcAmt(setDcAmt);					//SET 할인가
//				orderproduct.setSetDcRate(setDcRate.toString());	//SET 할인율

				orderproduct.setOmsOrderproducts(subProducts);

			} else {	//SET 아닐때.

				//단품 재고 check				
				BigDecimal realStockQty = orderproduct.getRealStockQty();
				if (orderQty.compareTo(realStockQty) > 0) {
					throw new ServiceException("oms.order.nonStockQty", new String[] {
							CommonUtil.makeProductErrorName(orderproduct.getProductName(), orderproduct.getSaleproductName()) });
				}

			}

			totalOrderSalePrice = totalOrderSalePrice.add(orderproduct.getTotalSalePrice().multiply(orderproduct.getOrderQty()));

			//정기배송주문건은 사은품, 쿠폰 없음.
			if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
				//상품 사은품	
				orderPromotionService.setOrderProductPresent(orderPresentList, orderproduct, selectPresentMap, omsOrder);

				//상품 쿠폰				
				orderPromotionService.setOrderProductCoupon(omsOrder, productCouponList, orderCouponList, orderproduct);

			}

			//상품목록에 상품 담음.
			omsOrderproducts.add(orderproduct);

			//적립예정포인트 세팅
			orderPromotionService.setOrderProductPoint(orderproduct, omsOrder);
		}

		//정기배송주문건은 최적 쿠폰 없음
		if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
			//최적 쿠폰 계산.
			orderPromotionService.calcOptimalCoupon(omsOrderproducts);
		}

		//주문상품 setting
		omsOrder.setOmsOrderproducts(omsOrderproducts);

		//정기배송주문건은 사은품 없음. 쿠폰 없음.
		if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
			//주문사은품 적용 check
			orderPromotionService.checkOrderPresent(omsOrder, orderPresentList, selectPresentMap);

			//주문사은품 목록 담음.
			omsOrder.setSpsPresents(orderPresentList);

			//주문 쿠폰		
			orderPromotionService.checkOrderCoupon(orderCouponList, omsOrder);

			//주문쿠폰 목록 담음.
			omsOrder.setOmsOrdercoupons(orderCouponList);
		}

		//주문총판매금액.
		omsOrder.setTotalOrderSalePrice(totalOrderSalePrice);

		omsOrder.setSiteId(BaseConstants.SITE_ID_0TO7);	//SITE ID 자사

		//주문유형.
		if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
			if (BaseConstants.YN_Y.equals(omsOrder.getGiftYn())) {
				omsOrder.setOrderTypeCd("ORDER_TYPE_CD.GIFT");
			} else {
				omsOrder.setOrderTypeCd("ORDER_TYPE_CD.GENERAL");
			}
		}

		return omsOrder;
	}


	/////////////////////////////////////// 주문생성 시작 //////////////////////////////////////
	public Map<String, Object> createOrder(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String orderTypeCd = order.getOrderTypeCd();

		OmsOrder omsOrder = new OmsOrder();
		if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
			omsOrder = order;
			omsOrder.setSiteId(BaseConstants.SITE_ID_0TO7);	//SITE ID 자사
		} else {
			omsOrder = getOrderList(order);
		}


		if (omsOrder != null) {
			//주문번호
			String orderId = getNewOrderId();
			omsOrder.setOrderId(orderId);

			String storeId = omsOrder.getStoreId();
			String channelId = SessionUtil.getChannelId();
			omsOrder.setChannelId(channelId);
			BigDecimal memberNo = omsOrder.getMemberNo();
			String deviceTypeCd = omsOrder.getDeviceTypeCd();

			//카드사몰 유입 check
			if (omsOrder.getOmsPayments() != null && omsOrder.getOmsPayments().get(0) != null) {
				OmsPayment payment = omsOrder.getOmsPayments().get(0);
				if (payment.getPaymentAmt().compareTo(new BigDecimal(0)) > 0) {
					paymentService.cardMallCheck(channelId, payment);
				}
			}

			String personalCustomsCode = order.getPersonalCustomsCode();	//개인통관부호

			BigDecimal totalProductCouponDcAmt = new BigDecimal(0);	//총상품쿠폰금액
			BigDecimal totalPlusCouponDcAmt = new BigDecimal(0);	//총plus쿠폰금액
			BigDecimal totalOrderCouponDcAmt = new BigDecimal(0);	//총주문쿠폰금액
			BigDecimal totalDeliveryCouponDcAmt = new BigDecimal(0);	//총배송비쿠폰금액
			BigDecimal totalWrapCouponDcAmt = new BigDecimal(0);	//총포장비쿠폰금액
			BigDecimal totalOrderDeliveryFee = new BigDecimal(0);	//총주문배송비	
			BigDecimal totalOrderWrapFee = new BigDecimal(0);		//총포장비
			BigDecimal totalSavePoint = new BigDecimal(0);	//총적립포인트
			
			BigDecimal couponProductAmt = new BigDecimal(0);	//상품쿠폰계산금액
			BigDecimal couponPlusAmt = new BigDecimal(0);		//plus쿠폰계산금액				
			BigDecimal couponOrderAmt = new BigDecimal(0);		//주문쿠폰계산금액
			BigDecimal couponDeliveryAmt = new BigDecimal(0);		//배송비쿠폰계산금액
			BigDecimal couponWrapAmt = new BigDecimal(0);		//포장쿠폰계산금액
			BigDecimal deliveryOrderFee = new BigDecimal(0);	//주문배송비			

			String productName = "";

			//주문상품
			List<OmsOrderproduct> newOmsOrderproducts = new ArrayList<OmsOrderproduct>();
			//주문쿠폰
			List<OmsOrdercoupon> newOmsOrdercoupons = new ArrayList<OmsOrdercoupon>();

			//omsOrder insert
			
			//주문상태
			String orderStateCd = "ORDER_STATE_CD.COMPLETE";	//주문완료.
			//주문상품상태
			String orderProductStateCd = "ORDER_PRODUCT_STATE_CD.READY";
			String orderDeliveryStateCd = "ORDER_DELIVERY_STATE_CD.READY";
			
			if ("ORDER_TYPE_CD.GIFT".equals(omsOrder.getOrderTypeCd())) {	//GIFT 일때.
				orderStateCd = "ORDER_STATE_CD.PAYED";	//결제완료.
				orderProductStateCd = "ORDER_PRODUCT_STATE_CD.REQ";
				orderDeliveryStateCd = "ORDER_DELIVERY_STATE_CD.REQ";
			}

			OmsPayment op = omsOrder.getOmsPayments().get(0);
			if ("PAYMENT_METHOD_CD.VIRTUAL".equals(op.getPaymentMethodCd())
					&& op.getPaymentAmt().compareTo(new BigDecimal(0)) > 0) {	//가상계좌일때 주문요청
				orderStateCd = "ORDER_STATE_CD.REQ";
				orderProductStateCd = "ORDER_PRODUCT_STATE_CD.REQ";
				orderDeliveryStateCd = "ORDER_DELIVERY_STATE_CD.REQ";
			}
			omsOrder.setOrderStateCd(orderStateCd);

			//배송상태
			omsOrder.setOrderDeliveryStateCd(orderDeliveryStateCd);

			String orderDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
			omsOrder.setOrderDt(orderDt);

			omsOrder.setSiteTypeCd("SITE_TYPE_CD.OWN");	//자사몰
			dao.insertOne(omsOrder);

			//배송지번호 mapping
			Map<BigDecimal, BigDecimal> deliveryAddressNoMap = new HashMap<BigDecimal, BigDecimal>();

			//배송지별,배송정책별 상품판매금액
			Map<String, BigDecimal> totalDeliverySalePriceMap = new HashMap<String, BigDecimal>();

			//주문적용대상 쿠폰목록.
			List<OmsOrdercoupon> orderCouponList = omsOrder.getOmsOrdercoupons();

			//주문상품일련번호 mapping
			Map<BigDecimal, BigDecimal> orderProductNoMap = new HashMap<BigDecimal, BigDecimal>();

			//상품 배송정책 모음
			List<OmsDelivery> deliveryList = new ArrayList<OmsDelivery>();

			int productCnt = 0;
			//배송지
			for (OmsDeliveryaddress omsDeliveryaddress : omsOrder.getOmsDeliveryaddresss()) {


				//임시 배송지번호
				BigDecimal tempDeliveryAddressNo = omsDeliveryaddress.getDeliveryAddressNo();

//				BigDecimal deliveryAddressNo = (BigDecimal) dao.selectOne("oms.order.getNewDeliveryAddressNo", null);
//				omsDeliveryaddress.setDeliveryAddressNo(deliveryAddressNo);

				omsDeliveryaddress.setOrderId(orderId);

				//배송지 insert
				dao.insertOne(omsDeliveryaddress);
				BigDecimal deliveryAddressNo = omsDeliveryaddress.getDeliveryAddressNo();
				deliveryAddressNoMap.put(tempDeliveryAddressNo, deliveryAddressNo);

				//배송지별 배송
				for (OmsDelivery omsDelivery : omsDeliveryaddress.getOmsDeliverys()) {
					omsDelivery.setStoreId(storeId);
					omsDelivery.setOrderId(orderId);
					omsDelivery.setDeliveryAddressNo(deliveryAddressNo);

					OmsOrdercoupon newOmsOrdercoupon = new OmsOrdercoupon();
					newOmsOrdercoupon.setStoreId(storeId);
					newOmsOrdercoupon.setOrderId(orderId);

					BigDecimal orderDeliveryFee = omsDelivery.getOrderDeliveryFee();

					//배송비쿠폰
					String deliveryCouponId = omsDelivery.getDeliveryCouponId();
					BigDecimal deliveryCouponIssueNo = omsDelivery.getDeliveryCouponIssueNo();
					BigDecimal deliveryCouponDcAmt = omsDelivery.getDeliveryCouponDcAmt();	//배송비쿠폰할인금액
					if (CommonUtil.isNotEmpty(deliveryCouponIssueNo)) {
						newOmsOrdercoupon.setCouponId(deliveryCouponId);
						newOmsOrdercoupon.setCouponIssueNo(deliveryCouponIssueNo);


						orderPromotionService.calcOmsOrderCoupon(newOmsOrdercoupons, newOmsOrdercoupon, orderDeliveryFee, null);

						totalDeliveryCouponDcAmt = totalDeliveryCouponDcAmt.add(deliveryCouponDcAmt);

					}

					//포장비
					totalOrderWrapFee = totalOrderWrapFee.add(omsDelivery.getOrderWrapFee());

					//포장비쿠폰
					String wrapCouponId = omsDelivery.getWrapCouponId();
					BigDecimal wrapCouponIssueNo = omsDelivery.getWrapCouponIssueNo();
					BigDecimal wrapCouponDcAmt = omsDelivery.getWrapCouponDcAmt();	//포장비쿠폰할인금액
					if (CommonUtil.isNotEmpty(wrapCouponIssueNo)) {
						newOmsOrdercoupon.setCouponId(wrapCouponId);
						newOmsOrdercoupon.setCouponIssueNo(wrapCouponIssueNo);
						newOmsOrdercoupon.setSingleApplyYn("Y");

						BigDecimal orderWrapFee = omsDelivery.getOrderWrapFee();

						orderPromotionService.calcOmsOrderCoupon(newOmsOrdercoupons, newOmsOrdercoupon, orderWrapFee, null);

						totalWrapCouponDcAmt = totalWrapCouponDcAmt.add(wrapCouponDcAmt);

					}

					//포장비는 포장지상품에 들어감. delivery에는 세팅 안한다.
					omsDelivery.setOrderWrapFee(new BigDecimal(0));
					omsDelivery.setWrapCouponId(null);
					omsDelivery.setWrapCouponIssueNo(null);
					omsDelivery.setWrapCouponDcAmt(new BigDecimal(0));
					omsDelivery.setApplyWrapFee(new BigDecimal(0));

					//배송 insert
					dao.insertOne(omsDelivery);

					deliveryOrderFee = deliveryOrderFee.add(orderDeliveryFee);

					deliveryList.add(omsDelivery);
				}

				BigDecimal totalOrderQty = new BigDecimal(0);

				//주문쿠폰 대상금액(loop 돌면서 해당되지 않으면 minus
				BigDecimal totalAmtByOrderCoupon = omsOrder.getTotalOrderSalePrice();

				//배송지별 상품				
				for (OmsOrderproduct deliveryProduct : omsDeliveryaddress.getOmsOrderproducts()) {
					BigDecimal orderQty = deliveryProduct.getOrderQty();
					BigDecimal totalSalePrice = deliveryProduct.getTotalSalePrice();//.multiply(orderQty);
					BigDecimal styleNo = deliveryProduct.getStyleNo();
					String productSingleApplyYn = "N";
					String plusSingleApplyYn = "N";

					totalOrderQty = totalOrderQty.add(orderQty);

					//주문수량있을때
					if (orderQty.compareTo(BigDecimal.ZERO) > 0) {

						BigDecimal deliveryOrderProductNo = deliveryProduct.getOrderProductNo();
						String deliveryProductId = deliveryProduct.getProductId();
						String deliverySaleproductId = deliveryProduct.getSaleproductId();
						String deliveryProductName = deliveryProduct.getProductName();
						String deliverySaleproductName = deliveryProduct.getSaleproductName();

						OmsOrdercoupon newOmsOrdercoupon = new OmsOrdercoupon();
						newOmsOrdercoupon.setStoreId(storeId);
						newOmsOrdercoupon.setOrderId(orderId);

						//포장유무
						String wrapYn = deliveryProduct.getWrapYn();


						//최종결제가(상품당)
						BigDecimal paymentAmt = deliveryProduct.getPaymentAmt();

						//상품쿠폰
						String productCouponId = deliveryProduct.getProductCouponId();
						BigDecimal productCouponIssueNo = deliveryProduct.getProductCouponIssueNo();
						BigDecimal productCouponDcAmt = deliveryProduct.getProductCouponDcAmt();		//상품쿠폰할인금액

						if (CommonUtil.isNotEmpty(productCouponId)) {
							newOmsOrdercoupon.setCouponId(productCouponId);
							newOmsOrdercoupon.setCouponIssueNo(productCouponIssueNo);

							orderPromotionService.calcOmsOrderCoupon(newOmsOrdercoupons, newOmsOrdercoupon, totalSalePrice,
									deliveryProduct);
							
							if ("Y".equals(newOmsOrdercoupon.getSingleApplyYn())) {
								productSingleApplyYn = "Y";
								totalProductCouponDcAmt = totalProductCouponDcAmt.add(productCouponDcAmt);
							} else {
								totalProductCouponDcAmt = totalProductCouponDcAmt.add(productCouponDcAmt.multiply(orderQty));
							}
							

						} else {
							productCouponDcAmt = new BigDecimal(0);
						}

						//PLUS쿠폰
						String plusCouponId = deliveryProduct.getPlusCouponId();
						BigDecimal plusCouponIssueNo = deliveryProduct.getPlusCouponIssueNo();
						BigDecimal plusCouponDcAmt = deliveryProduct.getPlusCouponDcAmt();		//PLUS쿠폰할인금액

						if (CommonUtil.isNotEmpty(plusCouponId)) {
							newOmsOrdercoupon.setCouponId(plusCouponId);
							newOmsOrdercoupon.setCouponIssueNo(plusCouponIssueNo);

//							totalSalePrice.subtract(productCouponDcAmt);	//.multiply(orderQty)

							//상품쿠폰 할인금액 뺀 판매가.
							orderPromotionService.calcOmsOrderCoupon(newOmsOrdercoupons, newOmsOrdercoupon,
									totalSalePrice.subtract(productCouponDcAmt),
									deliveryProduct);

							if ("Y".equals(newOmsOrdercoupon.getSingleApplyYn())) {
								plusSingleApplyYn = "Y";
								totalPlusCouponDcAmt = totalPlusCouponDcAmt.add(plusCouponDcAmt);
							} else {
								totalPlusCouponDcAmt = totalPlusCouponDcAmt.add(plusCouponDcAmt.multiply(orderQty));
							}


						} else {
							plusCouponDcAmt = new BigDecimal(0);
						}

						//주문쿠폰
						String orderCouponId = deliveryProduct.getOrderCouponId();
						BigDecimal orderCouponIssueNo = deliveryProduct.getOrderCouponIssueNo();
						BigDecimal orderCouponDcAmt = deliveryProduct.getOrderCouponDcAmt();		//주문쿠폰할인금액
						BigDecimal calibrateOrderDcAmt = deliveryProduct.getCalibrateOrderDcAmt();		//주문쿠폰할인금액(짜투리)

						if (CommonUtil.isNotEmpty(orderCouponId)) {
							newOmsOrdercoupon.setCouponId(orderCouponId);
							newOmsOrdercoupon.setCouponIssueNo(orderCouponIssueNo);
							omsOrder.getTotalOrderSalePrice();
							logger.debug("주문쿠폰계산 대상 금액 : " + totalAmtByOrderCoupon);
							totalAmtByOrderCoupon = totalAmtByOrderCoupon.subtract(productCouponDcAmt.multiply(orderQty))
									.subtract(plusCouponDcAmt.multiply(orderQty));
							logger.debug("주문쿠폰계산 상품쿠폰 금액 : " + productCouponDcAmt);
							logger.debug("주문쿠폰계산 플러스쿠폰 금액 : " + plusCouponDcAmt);
							logger.debug("주문쿠폰계산 결과대상 금액 : " + totalAmtByOrderCoupon);

							orderPromotionService.calcOmsOrderCoupon(newOmsOrdercoupons, newOmsOrdercoupon, totalAmtByOrderCoupon,
									deliveryProduct);	//상품 플러스 쿠폰을 뺀 할인금액.

							totalOrderCouponDcAmt = totalOrderCouponDcAmt.add(orderCouponDcAmt);

							logger.debug("주문 할인 쿠폰금액 : " + orderCouponDcAmt);
							logger.debug("주문 할인 쿠폰금액 짜투리 : " + calibrateOrderDcAmt);


						} else {
							//주문쿠폰 없으면 대상금액에서 제외.
							BigDecimal objAmt = (deliveryProduct.getTotalSalePrice()
									.subtract(deliveryProduct.getProductCouponDcAmt())
									.subtract(deliveryProduct.getPlusCouponDcAmt())).multiply(deliveryProduct.getOrderQty());

							totalAmtByOrderCoupon = totalAmtByOrderCoupon.subtract(objAmt);
						}

						boolean productEx = false;

						logger.debug("배송의 주문상품일련번호 : " + deliveryOrderProductNo);
						logger.debug("배송의 주문상품 : " + deliveryProductId);
						logger.debug("배송의 주문단품 : " + deliverySaleproductId);
						logger.debug("배송의 주문상품 : " + deliveryProductName);
						logger.debug("배송의 주문단품 : " + deliverySaleproductName);

						//배송지 분리전 주문상품정보를 배송지별로 분리하여 insert
						for (OmsOrderproduct omsOrderproduct : omsOrder.getOmsOrderproducts()) {
							BigDecimal orderProductNo = omsOrderproduct.getOrderProductNo();
							String productId = omsOrderproduct.getProductId();
							String saleproductId = omsOrderproduct.getSaleproductId();
							logger.debug("주문상품일련번호 : " + orderProductNo);
							logger.debug("DB의 주문상품 : " + productId);
							logger.debug("DB의 주문단품 : " + saleproductId);

							//임시 주문상품일련번호가 같은것.
							if (orderProductNo != null && orderProductNo.compareTo(deliveryOrderProductNo) == 0
									&& deliveryProductId.equals(productId) && deliverySaleproductId.equals(saleproductId)) {
								logger.debug("같음.");

								productEx = true;

								OmsOrderproduct newOmsOrderproduct = new OmsOrderproduct();

								BeanUtils.copyProperties(omsOrderproduct, newOmsOrderproduct);

//								newOmsOrderproduct = omsOrderproduct;

								String overseasYn = newOmsOrderproduct.getOverseasPurchaseYn();
								String productPCC = null;
								if (BaseConstants.YN_Y.equals(overseasYn)) {
									if (CommonUtil.isNotEmpty(personalCustomsCode)) {
										productPCC = personalCustomsCode;
									} else {
										throw new ServiceException("oms.order.nonPersonCustomsCode");
									}
								}

								if (productCnt == 0) {
									productName = newOmsOrderproduct.getProductName();
								}

								newOmsOrderproduct.setPersonalCustomsCode(productPCC);

								newOmsOrderproduct.setOrderProductNo(null);	//주문상품일련번호 초기화(새로 따기위함.)
								newOmsOrderproduct.setOrderId(orderId);		//주문ID
//								newOmsOrderproduct.setDeliveryPolicyNo(deliveryPolicyNo);
								newOmsOrderproduct.setDeliveryAddressNo(deliveryAddressNo);	//배송지번호
								newOmsOrderproduct.setOrderQty(orderQty);		//주문수량
								newOmsOrderproduct.setWrapYn(wrapYn);		//포장여부
								newOmsOrderproduct.setPaymentAmt(paymentAmt);		//최종결제가(상품당)

								//상품쿠폰
								newOmsOrderproduct.setProductCouponId(productCouponId);
								newOmsOrderproduct.setProductCouponIssueNo(productCouponIssueNo);
								newOmsOrderproduct.setProductCouponDcAmt(productCouponDcAmt);
								newOmsOrderproduct.setProductSingleApplyYn(productSingleApplyYn);

								//PLUS쿠폰
								newOmsOrderproduct.setPlusCouponId(plusCouponId);
								newOmsOrderproduct.setPlusCouponIssueNo(plusCouponIssueNo);
								newOmsOrderproduct.setPlusCouponDcAmt(plusCouponDcAmt);
								newOmsOrderproduct.setPlusSingleApplyYn(plusSingleApplyYn);

								//주문쿠폰
								boolean applyOrdercoupon = checkApplyOrderCouponProduct(orderCouponId, orderCouponIssueNo,
										orderCouponList, newOmsOrderproduct);
								if (applyOrdercoupon) {
									newOmsOrderproduct.setOrderCouponId(orderCouponId);
									newOmsOrderproduct.setOrderCouponIssueNo(orderCouponIssueNo);
									orderCouponDcAmt = orderCouponDcAmt.subtract(calibrateOrderDcAmt);
								} else {
									orderCouponDcAmt = new BigDecimal(0);
									calibrateOrderDcAmt = new BigDecimal(0);
								}
								//할인금액은 짜투리를 빼고 넣는다.
								newOmsOrderproduct.setOrderCouponDcAmt(orderCouponDcAmt);

								//SET수량
//								newOmsOrderproduct.setSetQty(newOmsOrderproduct.getOrderQty());
								newOmsOrderproduct.setSetQty(new BigDecimal(1));	//빅횽 요청으로 1 setting

//								newOmsOrderproduct.setCancelQty(new BigDecimal(0));
//								newOmsOrderproduct.setReturnQty(new BigDecimal(0));
//								newOmsOrderproduct.setExchangeQty(new BigDecimal(0));
//								newOmsOrderproduct.setRedeliveryQty(new BigDecimal(0));

								//포인트 계산.
								orderPromotionService.setOrderProductPoint(newOmsOrderproduct, omsOrder);
								totalSavePoint = totalSavePoint.add(newOmsOrderproduct.getTotalPoint().multiply(orderQty));

								//과세구분
								String taxTypeCd = newOmsOrderproduct.getTaxTypeCd();
								BigDecimal tax = new BigDecimal(0);
								if (BaseConstants.TAX_TYPE_CD_TAX.equals(taxTypeCd)) {
									BigDecimal taxRate = new BigDecimal(10);	//부가세 10%
									tax = newOmsOrderproduct.getTotalSalePrice().multiply(taxRate).divide(new BigDecimal(100), 2,
											BigDecimal.ROUND_HALF_UP);
								}
								//세금
								newOmsOrderproduct.setTax(tax);

								//주문상품상태
								newOmsOrderproduct.setOrderProductStateCd(orderProductStateCd);

								//주문상품유형
								String orderProductTypeCd = newOmsOrderproduct.getOrderProductTypeCd();

								//set 할인금액.
//								BigDecimal setDcAmt = newOmsOrderproduct.getSetDcAmt();
//								if (BaseConstants.ORDER_PRODUCT_TYPE_CD_SET.equals(orderProductTypeCd)) {
//									newOmsOrderproduct.setSetDcAmt(setDcAmt.multiply(orderQty));
//								}


								//new orderproductNo
//								BigDecimal newOrderProductNo = (BigDecimal) dao.selectOne("oms.order.getNewOrderProductNo", null);

								newOmsOrderproduct.setCalibrateSalePrice(new BigDecimal(0));
								newOmsOrderproduct.setCalibratePoint(new BigDecimal(0));
								newOmsOrderproduct.setCalibrateProductDcAmt(new BigDecimal(0));
								newOmsOrderproduct.setCalibratePlusDcAmt(new BigDecimal(0));
								newOmsOrderproduct.setCalibrateOrderDcAmt(calibrateOrderDcAmt);

								newOmsOrderproduct.setOrderDt(orderDt);
								newOmsOrderproduct.setStyleNo(styleNo);

								//general, set 상품 insert
								dao.insertOne(newOmsOrderproduct);
								productCnt++;
								
								//정기배송 주문완료 처리
								if("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)){
									OmsRegulardeliveryschedule or = new OmsRegulardeliveryschedule();
									or.setRegularDeliveryId(newOmsOrderproduct.getRegularDeliveryId());
									or.setDeliveryProductNo(newOmsOrderproduct.getDeliveryProductNo());
									or.setRegularDeliveryOrder(newOmsOrderproduct.getRegularDeliveryOrder());
									or.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.ORDER");	//주문완료.
									or.setDeliveryDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));	//주문완료일시
									int row = dao.updateOneTable(or);
									if (row == 0) {
										throw new ServiceException("oms.regular.completeError");
									}
								}

//								newOmsOrderproduct.setOrderProductNo(newOrderProductNo);

								newOmsOrderproducts.add(newOmsOrderproduct);


								//상위 주문상품일련번호
								BigDecimal upperOrderProductNo = newOmsOrderproduct.getOrderProductNo();

								//임시상품일련번호와 실상품일련번호 mapping
								orderProductNoMap.put(orderProductNo, upperOrderProductNo);

								BigDecimal deliveryPolicyNo = newOmsOrderproduct.getDeliveryPolicyNo();

								BigDecimal remainListPrice = newOmsOrderproduct.getListPrice();
								BigDecimal remainSalePrice = newOmsOrderproduct.getTotalSalePrice();
								BigDecimal remainProductPoint = newOmsOrderproduct.getProductPoint();
								BigDecimal remainAddPoint = newOmsOrderproduct.getAddPoint();
								BigDecimal remainProductCouponDcAmt = productCouponDcAmt;
								BigDecimal remainPlusCouponDcAmt = plusCouponDcAmt;

								BigDecimal orderCouponDcAmtone = new BigDecimal(0);
								BigDecimal remainOrderCouponDcAmt = new BigDecimal(0);

								BigDecimal remainOrderCouponDcAmtone = new BigDecimal(0);

								if (applyOrdercoupon) {
									orderCouponDcAmt = orderCouponDcAmt.add(calibrateOrderDcAmt);	//쿠폰 짜투리를 더하고 분배
									orderCouponDcAmtone = orderCouponDcAmt.divide(orderQty, -1, BigDecimal.ROUND_HALF_UP);	//개당으로 변경.
									logger.debug("주문쿠폰 할인금액 (개당) : " + orderCouponDcAmtone);
									remainOrderCouponDcAmtone = orderCouponDcAmt.subtract(orderCouponDcAmtone.multiply(orderQty));	//개당으로 분배시 남는금액.
									remainOrderCouponDcAmt = orderCouponDcAmtone;	//개당 주문쿠폰 남은금액																	
								}


								BigDecimal commissionRate = newOmsOrderproduct.getCommissionRate();
								BigDecimal pointSaveRate = newOmsOrderproduct.getPointSaveRate();

								BigDecimal sumSetSale = new BigDecimal(0);
								BigDecimal sumSetPoint = new BigDecimal(0);
								BigDecimal sumSetDc = new BigDecimal(0);
								BigDecimal remainSetCal = new BigDecimal(0);
								BigDecimal remainSetPoint = new BigDecimal(0);
								BigDecimal remainSetDc = new BigDecimal(0);

								logger.debug("====== 주문쿠폰 ==================");
								logger.debug("주문쿠폰 금액 : " + orderCouponDcAmt);
								logger.debug("주문쿠폰 남은금액 : " + remainOrderCouponDcAmtone);
								logger.debug("주문쿠폰 금액(개당) : " + orderCouponDcAmtone);
								logger.debug("주문쿠폰 남은금액(개당) : " + remainOrderCouponDcAmt);
								logger.debug("====== 주문쿠폰 ==================");
//								remainOrderCouponDcAmt = remainOrderCouponDcAmt.add(remainOrderCouponDcAmtone);

								//Set 일때.
								if (BaseConstants.ORDER_PRODUCT_TYPE_CD_SET.equals(orderProductTypeCd)) {
									//SET의 총원판매가 (SET 구성의 원판매가 * set 구성수량) * 수량 합.
									BigDecimal setTotalSaleAmt = newOmsOrderproduct.getSetTotalSalePrice();
									//.multiply(orderQty);

									logger.debug("구성상품 판매가 합 : " + setTotalSaleAmt);

									int subProductCnt = 0;
									for (OmsOrderproduct subOmsOrderproduct : omsOrderproduct.getOmsOrderproducts()) {

										OmsOrderproduct newSubOrderproduct = new OmsOrderproduct();
										
										if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {	//정기배송일때 product 정보 조회
											BigDecimal setQty = subOmsOrderproduct.getSetQty();
											subOmsOrderproduct = (OmsOrderproduct) dao
													.selectOne("oms.order.getOmsOrderproductByPms", subOmsOrderproduct);
											subOmsOrderproduct.setSetQty(setQty);
										}

										BeanUtils.copyProperties(subOmsOrderproduct, newSubOrderproduct);

//										newSubOrderproduct = subOmsOrderproduct;

										newSubOrderproduct.setOrderProductNo(null);	//주문상품일련번호 초기화(새로 따기위함.)

										newSubOrderproduct.setPersonalCustomsCode(productPCC);	//개인통관 부호

										/////////////////////////////////////// 분배 계산을 위한 값 /////////////////////////////////////////////////////
										//구성상품의 판매가
										BigDecimal totalSaleAmt = newSubOrderproduct.getListPrice()
												.multiply(newSubOrderproduct.getSetQty());
										
										//분배비율
										BigDecimal divideRate = totalSaleAmt.divide(setTotalSaleAmt, 10,
												BigDecimal.ROUND_HALF_UP);

										//구성상품수량
										BigDecimal setQty = newSubOrderproduct.getSetQty();


										//sub판매가(개당)
										BigDecimal subTotalSalePrice = ((newOmsOrderproduct.getTotalSalePrice()
												.multiply(divideRate)).divide(setQty, -1, BigDecimal.ROUND_HALF_UP));
										BigDecimal subListPrice = ((newOmsOrderproduct.getListPrice().multiply(divideRate))
												.divide(setQty, -1, BigDecimal.ROUND_HALF_UP));

										//sub공급가(개당)
										//sub판매가 * (1-수수료)
										BigDecimal cm = (new BigDecimal(100)).subtract(commissionRate);

										BigDecimal subSupplyPrice = subTotalSalePrice.multiply(cm).divide(new BigDecimal(100), 0,
												BigDecimal.ROUND_HALF_UP);
										



										//sub포인트(개당)
										BigDecimal subProductPoint = (newOmsOrderproduct.getProductPoint().multiply(divideRate))
												.divide(setQty, -1, BigDecimal.ROUND_HALF_UP);
												
										BigDecimal subAddPoint = (newOmsOrderproduct.getAddPoint().multiply(divideRate))
												.divide(setQty, -1, BigDecimal.ROUND_HALF_UP);

										BigDecimal subTotalPoint = subProductPoint.add(subAddPoint);

										//상품쿠폰할인금액 분배
										BigDecimal subProductCouponDcAmt = productCouponDcAmt.multiply(divideRate).divide(setQty,
												-1,
														BigDecimal.ROUND_HALF_UP);

										//PLUS쿠폰할인금액 분배
										BigDecimal subPlusCouponDcAmt = plusCouponDcAmt.multiply(divideRate).divide(setQty)
												.setScale(-1,
												BigDecimal.ROUND_HALF_UP);

										//주문쿠폰할인금액 분배(구성상품당)
										BigDecimal subOrderCouponDcAmt = orderCouponDcAmtone.multiply(divideRate).setScale(-1,
												BigDecimal.ROUND_HALF_UP);


										logger.debug("\n============= 구성상품 개당 계산 금액 =================");
										logger.debug("구성상품 개수 : " + newSubOrderproduct.getSetQty());
										logger.debug("구성상품 원판매가 : " + newSubOrderproduct.getListPrice());
										logger.debug("구성상품 분배비율 : " + divideRate);
										logger.debug("판매가(개당) 		: " + subTotalSalePrice);
										logger.debug("product point		: " + subProductPoint);
										logger.debug("add point			: " + subAddPoint);
										logger.debug("product쿠폰		: " + subProductCouponDcAmt);
										logger.debug("plus쿠폰			: " + subPlusCouponDcAmt);
										logger.debug("주문쿠폰			: " + subOrderCouponDcAmt);
										logger.debug("============= 구성상품 개당 계산 금액 =================\n");

										remainListPrice = remainListPrice.subtract(subListPrice.multiply(setQty));
										remainSalePrice = remainSalePrice.subtract(subTotalSalePrice.multiply(setQty));
										remainProductPoint = remainProductPoint.subtract(subProductPoint.multiply(setQty));
										remainAddPoint = remainAddPoint.subtract(subAddPoint.multiply(setQty));
										remainProductCouponDcAmt = remainProductCouponDcAmt
												.subtract(subProductCouponDcAmt.multiply(setQty));
										remainPlusCouponDcAmt = remainPlusCouponDcAmt
												.subtract(subPlusCouponDcAmt.multiply(setQty));
										remainOrderCouponDcAmt = remainOrderCouponDcAmt.subtract(subOrderCouponDcAmt);

										logger.debug("\n============= 구성상품 남은 금액 =================");
										logger.debug("남은 판매가			: " + remainSalePrice);
										logger.debug("남은 product point	: " + remainProductPoint);
										logger.debug("남은 add point		: " + remainAddPoint);
										logger.debug("남은 상품 쿠폰		: " + remainProductCouponDcAmt);
										logger.debug("남은 plus 쿠폰		: " + remainPlusCouponDcAmt);
										logger.debug("남은 주문 쿠폰		: " + remainOrderCouponDcAmt);
										logger.debug("============= 구성상품 남은 금액 =================\n");

										BigDecimal calibratePoint = remainProductPoint.add(remainAddPoint);

										BigDecimal subDcAmt = subProductCouponDcAmt.add(subPlusCouponDcAmt);

										//최종결제가(구성상품당)
										BigDecimal subPaymentAmt = ((subTotalSalePrice.subtract(subDcAmt)).multiply(setQty))
												.subtract(subOrderCouponDcAmt);

										/////////////////////////////////////// 분배 계산을 위한 값 /////////////////////////////////////////////////////																			
										
										//과세구분(상위상품)
										newSubOrderproduct.setTaxTypeCd(taxTypeCd);
										
										//세금
										BigDecimal subTax = tax.multiply(divideRate);

										newSubOrderproduct.setTax(subTax);
										
										newSubOrderproduct.setOrderId(orderId);
										newSubOrderproduct.setUpperOrderProductNo(upperOrderProductNo);
										newSubOrderproduct.setOrderProductTypeCd(BaseConstants.ORDER_PRODUCT_TYPE_CD_SUB);
										newSubOrderproduct.setDeliveryPolicyNo(deliveryPolicyNo);
										newSubOrderproduct.setDeliveryAddressNo(deliveryAddressNo);
										newSubOrderproduct.setOrderQty(orderQty.multiply(setQty));
										newSubOrderproduct.setListPrice(subListPrice);
										newSubOrderproduct.setSupplyPrice(subSupplyPrice);
										newSubOrderproduct.setSalePrice(subTotalSalePrice);
										newSubOrderproduct.setTotalSalePrice(subTotalSalePrice);
										newSubOrderproduct.setCommissionRate(commissionRate);

										newSubOrderproduct.setProductPoint(subProductPoint);		//구성상품 상품적립포인트(개당)
										newSubOrderproduct.setAddPoint(subAddPoint);		//구성상품 추가적립포인트(개당)
										newSubOrderproduct.setPointSaveRate(pointSaveRate);
										newSubOrderproduct.setTotalPoint(subTotalPoint);		//구성상품 총적립포인트(개당)

										//주문상품상태
										newSubOrderproduct.setOrderProductStateCd(orderProductStateCd);

										//상품쿠폰
										newSubOrderproduct.setProductCouponId(productCouponId);
										newSubOrderproduct.setProductCouponIssueNo(productCouponIssueNo);
										newSubOrderproduct.setProductCouponDcAmt(subProductCouponDcAmt);

										//PLUS쿠폰
										newSubOrderproduct.setPlusCouponId(plusCouponId);
										newSubOrderproduct.setPlusCouponIssueNo(plusCouponIssueNo);
										newSubOrderproduct.setPlusCouponDcAmt(subPlusCouponDcAmt);

										//주문쿠폰
										newSubOrderproduct.setOrderCouponId(orderCouponId);
										newSubOrderproduct.setOrderCouponIssueNo(orderCouponIssueNo);
										newSubOrderproduct.setOrderCouponDcAmt(subOrderCouponDcAmt);

										newSubOrderproduct.setPaymentAmt(subPaymentAmt);

//										newSubOrderproduct.setCancelQty(new BigDecimal(0));
//										newSubOrderproduct.setReturnQty(new BigDecimal(0));
//										newSubOrderproduct.setExchangeQty(new BigDecimal(0));
//										newSubOrderproduct.setRedeliveryQty(new BigDecimal(0));

										newSubOrderproduct.setOrderDt(orderDt);

										newSubOrderproduct.setWrapYn(wrapYn);

										newSubOrderproduct.setStyleNo(styleNo);

										sumSetSale = sumSetSale.add(subTotalSalePrice.multiply(setQty));
										sumSetPoint = sumSetPoint.add(subTotalPoint.multiply(setQty));
										sumSetDc = sumSetDc.add(subDcAmt.multiply(setQty)).add(subOrderCouponDcAmt);


										if (subProductCnt + 1 == omsOrderproduct.getOmsOrderproducts().size()) {
											subListPrice = subListPrice.add(remainListPrice);	//정가는 마지막에 짜투리 넣는다.																																	
											
											//sub 에서 남은 주문쿠폰짜투리 + set상품에서 남은 주문쿠폰 짜투리
											BigDecimal sumRemainOrderDcAmt = remainOrderCouponDcAmt.add(remainOrderCouponDcAmtone);
											
											newSubOrderproduct.setCalibratePoint(calibratePoint);
											newSubOrderproduct.setCalibrateProductDcAmt(remainProductCouponDcAmt);
											newSubOrderproduct.setCalibratePlusDcAmt(remainPlusCouponDcAmt);
											newSubOrderproduct.setCalibrateOrderDcAmt(sumRemainOrderDcAmt);
											newSubOrderproduct.setCalibrateSalePrice(remainSalePrice);

											//최종결제금액 다시 계산 (주문쿠폰 짜투리 빼기)
											subPaymentAmt = ((subTotalSalePrice.subtract(subDcAmt)).multiply(setQty))
													.add(remainSalePrice).subtract(subOrderCouponDcAmt.add(sumRemainOrderDcAmt));
											newSubOrderproduct.setPaymentAmt(subPaymentAmt);

											remainSetCal = remainSalePrice.multiply(setQty);
											remainSetPoint = calibratePoint;
											remainSetDc = (remainProductCouponDcAmt.add(remainPlusCouponDcAmt))
													.add(sumRemainOrderDcAmt);
										} else {
											newSubOrderproduct.setCalibratePoint(new BigDecimal(0));
											newSubOrderproduct.setCalibrateProductDcAmt(new BigDecimal(0));
											newSubOrderproduct.setCalibratePlusDcAmt(new BigDecimal(0));
											newSubOrderproduct.setCalibrateOrderDcAmt(new BigDecimal(0));
											newSubOrderproduct.setCalibrateSalePrice(new BigDecimal(0));
										}

										//sub 상품 insert
										dao.insertOne(newSubOrderproduct);

										if (!"ORDER_STATE_CD.REQ".equals(orderStateCd)) {	//요청이 아닐때.
											//상품재고차감
											updateStockQty(newSubOrderproduct, true, "");
										}

										newOmsOrderproducts.add(newSubOrderproduct);
										
										subProductCnt++;
									}

//									remainSetDcAmt = remainProductDcAmt.add(remainPlusDcAmt)
//											.add(remainOrderCouponDcAmt);

									logger.debug("========== SUB DATA 검증 ============");

									logger.debug("1. SET DATA ============");
									logger.debug("		totalSalePrice		:	" + newOmsOrderproduct.getTotalSalePrice());
									logger.debug("		totalPoint			:	" + newOmsOrderproduct.getTotalPoint());
									logger.debug("		productCouponDcAmt	:	" + productCouponDcAmt);
									logger.debug("		plusCouponDcAmt		:	" + plusCouponDcAmt);
									logger.debug("		orderCouponDcAmt	:	" + orderCouponDcAmtone);
									logger.debug("		remainOrderCouponDcAmtone	:	" + remainOrderCouponDcAmtone);

									logger.debug("2. SUB DATA ============");
									logger.debug("		sumSetSale			:	" + sumSetSale);
									logger.debug("		sumSetPoint			:	" + sumSetPoint);
									logger.debug("		sumSetDc			:	" + sumSetDc);
									logger.debug("		remainSetCal			:	" + remainSetCal);
									logger.debug("		remainSetPoint			:	" + remainSetPoint);
									logger.debug("		remainSetDcAmt			:	" + remainSetDc);

									logger.debug("========== SUB DATA ============");
									
									if (newOmsOrderproduct.getTotalSalePrice().compareTo(sumSetSale.add(remainSetCal)) != 0) {
										throw new ServiceException("oms.order.subSaleError");
									}
									
									if (newOmsOrderproduct.getTotalPoint().compareTo(sumSetPoint.add(remainSetPoint)) != 0) {
										throw new ServiceException("oms.order.subPointError");
									}
									BigDecimal sumDc = productCouponDcAmt.add(plusCouponDcAmt).add(orderCouponDcAmtone)
											.add(remainOrderCouponDcAmtone);
									if (sumDc.compareTo(sumSetDc.add(remainSetDc)) != 0) {
										throw new ServiceException("oms.order.subDcError");
									}

//									dao.updateOneTable(newOmsOrderproduct);

								}
								//Set 일때. END


								//상품사은품 
								if (omsOrderproduct.getSpsPresents() != null) {

									for (SpsPresent spsPresent : omsOrderproduct.getSpsPresents()) {
										if (spsPresent.getSpsPresentproducts() != null) {
											for (SpsPresentproduct presentProduct : spsPresent.getSpsPresentproducts()) {
												PmsProduct product = presentProduct.getPmsProduct();
												for (PmsSaleproduct saleproduct : product.getPmsSaleproducts()) {

													OmsOrderproduct presentOrderproduct = new OmsOrderproduct();
													presentOrderproduct.setProductId(product.getProductId());
													presentOrderproduct.setSaleproductId(saleproduct.getSaleproductId());

													//											presentOrderproduct.setMemGradeCd(memGradeCd);
													//											presentOrderproduct.setDealTypeCds(dealTypeCds);
													presentOrderproduct = (OmsOrderproduct) dao
															.selectOne("oms.order.getOmsOrderproductByPms", presentOrderproduct);

													presentOrderproduct.setOrderId(orderId);	//주문번호											
													presentOrderproduct.setUpperOrderProductNo(upperOrderProductNo);//상위주문상품일련번호
													presentOrderproduct.setStoreId(storeId);

													//사은품재고가 주문수량보다 적으면 적은만큼 준다.
													BigDecimal realStockQty = presentOrderproduct.getRealStockQty();
													if (orderQty.compareTo(realStockQty) > 0) {
														presentOrderproduct.setOrderQty(realStockQty);
													} else {
														presentOrderproduct.setOrderQty(orderQty);
													}
													presentOrderproduct.setDeliveryAddressNo(deliveryAddressNo);	//배송지번호

//													presentOrderproduct.setDeliveryPolicyNo(deliveryPolicyNo);
													//											presentOrderproduct.setReserveYn(reserveYn);
													presentOrderproduct.setWrapYn(BaseConstants.YN_N);		//포장여부

													//											presentOrderproduct.setCategoryId(newOmsOrderproduct.getCategoryId());	//상품의 categoryId

													presentOrderproduct.setOrderProductStateCd(orderProductStateCd);
													presentOrderproduct
															.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT");

													presentOrderproduct.setOrderDt(orderDt);
													orderPromotionService.savePresentproduct(presentOrderproduct, presentProduct,
															spsPresent,
															deliveryList);
												}
											}
										}
									}
								}
								//상품사은품 END

								//배송비 비교을 위한 배송정책별 상품판매가 set								
								logger.debug("======== 배송비 검증을위한 총 상품금액 map ============");
								BigDecimal curTotalSalePrice = newOmsOrderproduct.getSalePrice()
										.add(newOmsOrderproduct.getAddSalePrice());
								logger.debug("curTotalSalePrice : " + curTotalSalePrice);
								String key = deliveryAddressNo + "_" + deliveryPolicyNo;
								BigDecimal exTotalOrderDeliveryFee = totalDeliverySalePriceMap.get(key);
								if (CommonUtil.isEmpty(exTotalOrderDeliveryFee)) {
									totalDeliverySalePriceMap.put(key, curTotalSalePrice.multiply(orderQty));
								}else{
									totalDeliverySalePriceMap.put(key,
											exTotalOrderDeliveryFee.add(curTotalSalePrice.multiply(orderQty)));
								}

								if (BaseConstants.YN_Y.equals(newOmsOrderproduct.getDeliveryFeeFreeYn())) {
									//배송지별,배송정책별 배송비가 무료인 상품 존재하는지 check
									totalDeliverySalePriceMap.put(key + "_deliveryFree", new BigDecimal(0));
								}
								logger.debug("totalDeliverySalePriceMap : " + totalDeliverySalePriceMap.toString());
								//배송비 비교을 위한 배송정책별 상품판매가 set
								
								if (!"ORDER_STATE_CD.REQ".equals(orderStateCd)
										&& !"ORDER_PRODUCT_TYPE_CD.SET".equals(orderProductTypeCd)) {	//요청이 아닐때. 세트아닐때.
									//상품재고 차감.
									updateStockQty(newOmsOrderproduct, true, "");
								}

								//딜재고 차감. 1인구매제한수량 check
								String dealId = newOmsOrderproduct.getDealId();
								if (CommonUtil.isNotEmpty(dealId) && !"ORDER_STATE_CD.REQ".equals(orderStateCd)) {
									updateDealStockQty(newOmsOrderproduct, true, "");
								}
								break;	//deliveryOrderProductNo에 해당하는건 insert후 나간다.
							}
							//임시 주문상품일련번호가 같은것. END
						}
						//배송지 분리전 주문상품정보를 배송지별로 분리하여 insert END

						if (!productEx) {
							throw new ServiceException("oms.order.nonExProduct",
									new String[] { deliveryProductName + "-" + deliverySaleproductName });
						}
					}
					//주문수량 있을때 END
				}
				//배송지별 상품 END
			}
			//배송지 END
			
			//주문사은품 
			List<SpsPresent> orderPresents = omsOrder.getSpsPresents();
			if(orderPresents != null){

				for(SpsPresent orderPresent : orderPresents){

					if (orderPresent.getSpsPresentproducts() != null) {

						for (SpsPresentproduct presentProduct : orderPresent.getSpsPresentproducts()) {
							PmsProduct product = presentProduct.getPmsProduct();
							String productId = product.getProductId();

							String presentId = orderPresent.getPresentId();
							String selectPresent = omsOrder.getSelectPresent();
							Map<String, String> selectPresentMap = null;
							if (CommonUtil.isNotEmpty(selectPresent)) {
								selectPresentMap = orderPromotionService.makePresent(selectPresent);
							}
							//선택된 배송지
							String key = presentId + "_" + productId;
							String dan = selectPresentMap.get(key + "dan");	//deliveryAddressNo
//						String dpn = selectPresentMap.get(key + "dpn");	//deliveryPolicyNo

							if (CommonUtil.isNotEmpty(dan)) {
								for (PmsSaleproduct saleproduct : product.getPmsSaleproducts()) {

									OmsOrderproduct presentOrderproduct = new OmsOrderproduct();
									presentOrderproduct.setProductId(productId);
									presentOrderproduct.setSaleproductId(saleproduct.getSaleproductId());
//								presentOrderproduct.setMemGradeCd(memGradeCd);
//								presentOrderproduct.setDealTypeCds(dealTypeCds);
									presentOrderproduct = (OmsOrderproduct) dao.selectOne("oms.order.getOmsOrderproductByPms",
											presentOrderproduct);
									presentOrderproduct.setOrderId(orderId);	//주문번호
									presentOrderproduct.setStoreId(storeId);
									presentOrderproduct.setOrderQty(new BigDecimal(1));	//수량 1

									BigDecimal deliveryAddressNo = new BigDecimal(dan);
									deliveryAddressNo = deliveryAddressNoMap.get(deliveryAddressNo);	//실 배송지번호로 변경.
//								BigDecimal deliveryPolicyNo = new BigDecimal(dpn);

									presentOrderproduct.setDeliveryAddressNo(deliveryAddressNo);	//배송지번호
//								presentOrderproduct.setDeliveryPolicyNo(deliveryPolicyNo);
									presentOrderproduct.setWrapYn(BaseConstants.YN_N);		//포장여부

									presentOrderproduct.setOrderProductStateCd(orderProductStateCd);	//주문상품상태
									presentOrderproduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.ORDERPRESENT");
//
//									String saleTypeCd = presentOrderproduct.getSaleTypeCd();
//									if (CommonUtil.isEmpty(saleTypeCd)) {
//										presentOrderproduct.setSaleTypeCd(" ");	//매입유형코드
//									}

									presentOrderproduct.setOrderDt(orderDt);

									orderPromotionService.savePresentproduct(presentOrderproduct, presentProduct, orderPresent,
											deliveryList);
								}

							}
						}
					}


					//주문사은품 대상 상품 insert
					for (OmsOrderproduct orderproduct : orderPresent.getOmsOrderproducts()) {
						OmsPresentproduct presentProduct = new OmsPresentproduct();
						presentProduct.setPresentId(orderPresent.getPresentId());
						presentProduct.setOrderId(orderId);
						//주문상품일련번호 mapping 정보.
						BigDecimal orderProductNo = (BigDecimal) orderProductNoMap.get(orderproduct.getOrderProductNo());
						presentProduct.setOrderProductNo(orderProductNo);

						dao.insertOne(presentProduct);
					}
						
				}
				//주문사은품 END
			}

			//포장상품insert			
			this.saveWrapOrderproduct(omsOrder, orderProductStateCd);

			//배송비 검증
			logger.debug("========= 배송비 검증");
			totalOrderDeliveryFee = deliveryFeeValidation(totalDeliverySalePriceMap);
			logger.debug("1.SUM DATA");
			logger.debug("		totalOrderDeliveryFee	:" + totalOrderDeliveryFee);
			logger.debug("2.DELIVERY DATA");
			logger.debug("		deliveryOrderFee	:" + deliveryOrderFee);
			logger.debug("========= 배송비 검증");

			if (totalOrderDeliveryFee.compareTo(deliveryOrderFee) != 0) {
				throw new ServiceException("oms.order.deliveryFeeError");
			}

			//쿠폰 금액 검증.
			logger.debug("========= 쿠폰 금액 검증");
			logger.debug("1.SUM DATA");
			logger.debug("		totalProductCouponDcAmt	: " + totalProductCouponDcAmt);
			logger.debug("		totalPlusCouponDcAmt	: " + totalPlusCouponDcAmt);
			logger.debug("		totalOrderCouponDcAmt	: " + totalOrderCouponDcAmt);
			logger.debug("		totalDeliveryCouponDcAmt: " + totalDeliveryCouponDcAmt);
			logger.debug("		totalWrapCouponDcAmt	: " + totalWrapCouponDcAmt);

			BigDecimal totalCouponDcAmt = new BigDecimal(0);
			for (OmsOrdercoupon coupon : newOmsOrdercoupons) {
				String couponTypeCd = coupon.getCouponTypeCd();
				if("COUPON_TYPE_CD.PRODUCT".equals(couponTypeCd)){
					couponProductAmt = couponProductAmt.add(coupon.getApplyDcAmt());
				} else if ("COUPON_TYPE_CD.PLUS".equals(couponTypeCd)) {
					couponPlusAmt=couponPlusAmt.add(coupon.getApplyDcAmt());
				} else if ("COUPON_TYPE_CD.ORDER".equals(couponTypeCd)) {
					couponOrderAmt=couponOrderAmt.add(coupon.getApplyDcAmt());
				} else if ("COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd)) {
					couponDeliveryAmt=couponDeliveryAmt.add(coupon.getApplyDcAmt());
				} else if ("COUPON_TYPE_CD.WRAP".equals(couponTypeCd)) {
					couponWrapAmt=couponWrapAmt.add(coupon.getApplyDcAmt());
				}
				totalCouponDcAmt = totalCouponDcAmt.add(coupon.getApplyDcAmt());

				//쿠폰 금액 update
				dao.updateOneTable(coupon);

				//쿠폰사용처리
				SpsCouponissue spsCouponissue = new SpsCouponissue();
				spsCouponissue.setStoreId(coupon.getStoreId());
				spsCouponissue.setCouponId(coupon.getCouponId());
				spsCouponissue.setCouponIssueNo(coupon.getCouponIssueNo());
				spsCouponissue.setCouponIssueStateCd("COUPON_ISSUE_STATE_CD.USE");
				spsCouponissue.setUseDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
				dao.updateOneTable(spsCouponissue);
			}

			logger.debug("2.COUPON DATA");
			logger.debug("		totalProductCouponDcAmt	: " + couponProductAmt);
			logger.debug("		totalPlusCouponDcAmt	: " + couponPlusAmt);
			logger.debug("		totalOrderCouponDcAmt	: " + couponOrderAmt);
			logger.debug("		totalDeliveryCouponDcAmt: " + couponDeliveryAmt);
			logger.debug("		totalWrapCouponDcAmt	: " + couponWrapAmt);

			boolean couponError = false;

			if (totalProductCouponDcAmt.compareTo(couponProductAmt) != 0
					|| totalPlusCouponDcAmt.compareTo(couponPlusAmt) != 0 || totalOrderCouponDcAmt.compareTo(couponOrderAmt) != 0
					|| totalDeliveryCouponDcAmt.compareTo(couponDeliveryAmt) != 0
					|| totalWrapCouponDcAmt.compareTo(couponWrapAmt) != 0) {
				couponError = true;
			}
			if (couponError) {
				throw new ServiceException("oms.order.couponError");
			}

			logger.debug("========= 쿠폰 금액 검증");

			logger.debug("========= 포인트 적립 검증");
			logger.debug("		1.SUM DATA	:" + omsOrder.getTotalPointsave());
			logger.debug("		2.POINT DATA :" + totalSavePoint);

			//정기배송일때 적립포인트는 계산값.
			if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)
					&& omsOrder.getTotalPointsave().compareTo(totalSavePoint) != 0) {
				throw new ServiceException("oms.order.pointSaveError");
			}

			logger.debug("========= 포인트 적립 검증");

			BigDecimal totalOrderSalePrice = omsOrder.getTotalOrderSalePrice();
			BigDecimal totalPaymentAmt = totalOrderSalePrice.add(totalOrderDeliveryFee).add(totalOrderWrapFee)
					.subtract(totalCouponDcAmt);


			logger.debug("========= 금액 검증");
			logger.debug("		1.총주문금액		:" + totalOrderSalePrice);
			logger.debug("		2.총배송비			:" + totalOrderDeliveryFee);
			logger.debug("		3.총포장비			:" + totalOrderWrapFee);
			logger.debug("		4.총쿠폰금액		:" + totalCouponDcAmt);
			logger.debug("		5.총결제 예상금액	:" + totalPaymentAmt);
			logger.debug("		6.총포인트 적립 예상금액	:" + totalSavePoint);
			logger.debug("========= 금액 검증");

			BigDecimal totalPayAmt = new BigDecimal(0);
			//결제
			List<OmsPayment> orderPay = omsOrder.getOmsPayments();
			String paymentResultFlag = "";
			String paymentResultMsg = "";
			String errorMethodCd = "";
			List<OmsPayment> paymentPayList = new ArrayList<OmsPayment>();

			String bankName = "";
			String accountNo = "";
			BigDecimal virtualPaymentAmt = new BigDecimal(0);
			String virtualEndDt = "";
			for (int i = orderPay.size() - 1; i >= 0; i--) {
				OmsPayment payment = orderPay.get(i);

				BigDecimal paymentAmt = payment.getPaymentAmt();

				totalPayAmt = totalPayAmt.add(paymentAmt);

				if (i == 0) {
					logger.debug("========= 최종 결제금액");
					logger.debug("		" + totalPayAmt);
					logger.debug("========= 최종 결제금액");
					if (totalPaymentAmt.compareTo(totalPayAmt) != 0) {
						throw new ServiceException("oms.order.payError");
					}
				}

				if (paymentAmt.compareTo(new BigDecimal(0)) > 0) {
					payment.setOrderId(orderId);
					payment.setMemberNo(memberNo);
					payment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
					payment.setPaymentTypeCd("PAYMENT_TYPE_CD.PAYMENT");//결제유형(결제)
					payment.setChannelId(channelId);
					result = paymentService.savePayment(payment);
					paymentResultFlag = (String) result.get(BaseConstants.RESULT_FLAG);
					if (BaseConstants.RESULT_FLAG_FAIL.equals(paymentResultFlag)) {
						paymentResultMsg = (String) result.get(BaseConstants.RESULT_MESSAGE);
						errorMethodCd = payment.getPaymentMethodCd();
						break;
					} else {
						paymentPayList.add(payment);
					}

					if ("PAYMENT_METHOD_CD.VIRTUAL".equals(payment.getPaymentMethodCd())) {
						bankName = payment.getPaymentBusinessNm();
						accountNo = payment.getAccountNo();
						virtualPaymentAmt = payment.getPaymentAmt();
						virtualEndDt = payment.getVirtualAccountDepositEndDt();
					}
				}
			}

			String orderPhone = omsOrder.getPhone2();
			String productName2 = productName;
			if (productCnt > 1) {
				productName2 = productName2 + " 외 " + (productCnt - 1) + "건";
			}
			OmsOrderTms orderTms = new OmsOrderTms();
			orderTms.setToPhone(orderPhone);
			orderTms.setProductName(productName2);
			orderTms.setOrderId(orderId);
			orderTms.setPaymentAmt(totalPayAmt.toString());

			if (BaseConstants.RESULT_FLAG_FAIL.equals(paymentResultFlag)) {

				//정기배송시 결제실패 SMS
				if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
					orderTms.setType("REGULAR_PAYMENT_FAIL");
					orderTms.setRegularDeliveryDt(order.getRegularDeliveryDt());
					orderTmsService.saveOrderTms(orderTms);
				}

				try {
					paymentService.updateRollBackNewTx(paymentPayList, errorMethodCd);
				} catch (Exception e) {
					logger.error(e.getMessage());
					paymentResultMsg += " ///// 복원 오류 : " + e.getMessage();
				}

				throw new ServiceException("oms.pg.error", new Object[] { paymentResultMsg });
			}

			//장바구니 삭제
			String cartProductNos = omsOrder.getCartProductNos();
			cartService.deleteCartOrder(cartProductNos, storeId);


			//sms success
			if ("ORDER_STATE_CD.REQ".equals(orderStateCd)) {
				orderTms.setType("VIRTUAL_PAYMENT");
				orderTms.setPaymentAmt(virtualPaymentAmt.toString());
				orderTms.setBankName(bankName);
				orderTms.setAccountNo(accountNo);
				orderTms.setEndDt(virtualEndDt);
			} else {
				if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
					orderTms.setType("REGULAR_PAYMENT");
					orderTms.setRegularDeliveryDt(order.getRegularDeliveryDt());
				} else if ("ORDER_TYPE_CD.GIFT".equals(orderTypeCd)) {
					orderTms.setType("GIFT_COMPLETE");
					orderTms.setOrderName(order.getGiftName());
					orderTms.setAddPhone(order.getGiftPhone());
					orderTms.setOrderDt(orderDt);
				} else {
					orderTms.setType("ORDER_COMPLETE");
				}
			}

			orderTmsService.saveOrderTms(orderTms);

			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			result.put("orderId", orderId);

		} else {
			result.put(BaseConstants.RESULT_MESSAGE, "주문정보 없음.");
		}

		return result;
	}

	/**
	 * 
	 * @Method Name : saveWrapOrderproduct
	 * @author : dennis
	 * @date : 2016. 11. 11.
	 * @description : 포장비 insert
	 *
	 * @param order
	 * @throws Exception
	 */
	private void saveWrapOrderproduct(OmsOrder order,String orderProductStateCd) throws Exception {
		List<OmsOrderproduct> productList = (List<OmsOrderproduct>) dao.selectList("oms.order.getOrderWrapList", order);				
		
		for (OmsOrderproduct product : productList) {
			product.setProductId(Config.getString("wrap.item.cd"));
			product.setSaleproductId(Config.getString("wrap.unit.id"));
			product.setErpProductId(Config.getString("wrap.barcode"));
			product.setErpSaleproductId(Config.getString("wrap.barcode"));
			product.setLocationId(Config.getString("wrap.section.nm"));
			product.setBusinessId(Config.getString("wrap.ven.id"));
			product.setBusinessName(Config.getString("wrap.ven.name"));
			product.setOrderDt(order.getOrderDt());
			product.setOrderProductStateCd(orderProductStateCd);
			dao.insertOne(product);
		}
	}

	/**
	 * 
	 * @Method Name : updateStockQty
	 * @author : dennis
	 * @date : 2016. 9. 1.
	 * @description : 상품재고 UPDATE
	 *
	 * @param storeId
	 * @param saleproductId
	 * @param qty
	 * @param minus
	 */
	public void updateStockQty(OmsOrderproduct orderproduct, boolean minus, String stockMinus) throws Exception {
		PmsSaleproduct pmsSaleproduct = new PmsSaleproduct();
		pmsSaleproduct.setStoreId(orderproduct.getStoreId());
		pmsSaleproduct.setSaleproductId(orderproduct.getSaleproductId());
		BigDecimal qty = orderproduct.getOrderQty();
		if (minus) {
			pmsSaleproduct.setRealStockQty(qty.negate());
		} else {
			pmsSaleproduct.setRealStockQty(qty);
		}
		pmsSaleproduct.setStockMinus(stockMinus);
		dao.update("pms.product.updateStock", pmsSaleproduct);

		if (!"SUCCESS".equals(pmsSaleproduct.getResult())) {
			String msg = pmsSaleproduct.getMsg();
			logger.debug(msg);
			String productname = orderproduct.getProductName() + "-" + orderproduct.getSaleproductName();
			throw new ServiceException("oms.order.nonStockQty", new String[] { productname });
		}
	}

	/**
	 * 
	 * @Method Name : updateDealStockQty
	 * @author : dennis
	 * @date : 2016. 10. 11.
	 * @description : 딜재고 UPDATE
	 *
	 * @param orderproduct
	 * @param minus
	 * @param stockMinus
	 */
	public void updateDealStockQty(OmsOrderproduct orderproduct, boolean minus, String stockMinus) throws Exception {
		SpsDealproduct spsDealproduct = new SpsDealproduct();
		spsDealproduct.setStoreId(orderproduct.getStoreId());
		spsDealproduct.setProductId(orderproduct.getProductId());
		spsDealproduct.setDealId(orderproduct.getDealId());
		spsDealproduct.setDealProductNo(orderproduct.getDealProductNo());
		BigDecimal qty = orderproduct.getOrderQty();
		if (minus) {
			spsDealproduct.setDealStockQty(qty.negate());
		} else {
			spsDealproduct.setDealStockQty(qty);
		}
		spsDealproduct.setStockMinus(stockMinus);
		dao.update("sps.deal.updateStock", spsDealproduct);

		if (!"SUCCESS".equals(spsDealproduct.getResult())) {
			String msg = spsDealproduct.getMsg();
			logger.debug(msg);
			String productname = orderproduct.getProductName() + "-" + orderproduct.getSaleproductName();
			throw new ServiceException("oms.order.nonStockQty", new String[] { productname });
		}
	}

	private boolean checkApplyOrderCouponProduct(String couponId, BigDecimal couponIssuNo,
			List<OmsOrdercoupon> newOmsOrdercoupons,
			OmsOrderproduct newOmsOrderproduct) {
		boolean result = false;

		if (newOmsOrdercoupons != null) {
			for (OmsOrdercoupon coupon : newOmsOrdercoupons) {
				String orgCouponId = coupon.getCouponId();
				BigDecimal orgCouponIssueNo = coupon.getCouponIssueNo();
				if (orgCouponId.equals(couponId) && orgCouponIssueNo.compareTo(couponIssuNo) == 0) {
					for (OmsOrderproduct applyProduct : coupon.getOmsOrderproducts()) {
						String orgProductId = applyProduct.getProductId();
						if (orgProductId.equals(newOmsOrderproduct.getProductId())) {
							result = true;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @Method Name : deliveryFeeValidation
	 * @author : dennis
	 * @date : 2016. 8. 5.
	 * @description : 배송비검증
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public BigDecimal deliveryFeeValidation(Map<String, BigDecimal> map) throws Exception {
		BigDecimal fee = new BigDecimal(0);
		Set<String> keys = map.keySet();

		logger.debug("======= 배송지별,배송정책별 배송비 계산");
		for (String key : keys) {
			if (key.indexOf("_deliveryFree") == -1) {	//무료check key가 아닐때.
				BigDecimal orderDeliveryFee = new BigDecimal(0);
				String[] arr = key.split("_");
				logger.debug("=============================");
				logger.debug("		배송지번호		:" + arr[0]);
				logger.debug("		배송정책번호	:" + arr[1]);
				BigDecimal free = map.get(key + "_deliveryFree");
				logger.debug("		free	:" + free);
				if (CommonUtil.isNotEmpty(free) && free.compareTo(new BigDecimal(0)) == 0) {
					logger.debug("		배송비 무료!!!!");
				} else {
					//배송지별,배송정책별 상품 총판매가.
					BigDecimal productSalePrice = map.get(key);
					logger.debug("		상품총판매가	:" + productSalePrice);

					CcsDeliverypolicy policy = new CcsDeliverypolicy();
					policy.setDeliveryPolicyNo(new BigDecimal(arr[1]));
					policy = (CcsDeliverypolicy) dao.selectOneTable(policy);

					BigDecimal minDeliveryFreeAmt = (BigDecimal) CommonUtil.replaceNull(policy.getMinDeliveryFreeAmt(),
							new BigDecimal(0));

					if (minDeliveryFreeAmt.compareTo(productSalePrice) > 0) {
						orderDeliveryFee = (BigDecimal) CommonUtil.replaceNull(policy.getDeliveryFee(), new BigDecimal(0));
					}

					logger.debug("		주문배송비	:" + orderDeliveryFee);
					fee = fee.add(orderDeliveryFee);
				}
			}
		}
		logger.debug("======= 배송지별,배송정책별 배송비 계산");

		return fee;
	}

	/**
	 * 
	 * @Method Name : getOrderComplete
	 * @author : dennis
	 * @date : 2016. 7. 22.
	 * @description : 주문완료조회
	 *
	 * @param omsOrder
	 * @return
	 */
	public OmsOrder getOrderComplete(OmsOrder omsOrder) {
		return (OmsOrder) dao.selectOne("oms.order.getOrderComplete", omsOrder);
	}

	/**
	 * 
	 * @Method Name : insertOmsERPIFNewTx
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : ERP 데이터 생성.
	 *
	 * @param omsOrder
	 */
	public void insertOmsERPIFNewTx(OmsOrder omsOrder) {
		try {
			//ERPIF
			//TODO : 추후수정.
//			dao.update("oms.order.insertOmsERPIF", omsOrder);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @Method Name : updateProduct
	 * @author : allen
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param omsOrderproduct
	 */
	public void updateOrderProductState(OmsOrderproduct omsOrderproduct) {
		dao.update("oms.order.update.updateOrderProduct", omsOrderproduct);
	}

	/**
	 * 
	 * @Method Name : updateOrder
	 * @author : allen
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param omsOrder
	 */
	public void updateOrderDeliveryState(OmsOrder omsOrder) {
		dao.update("oms.order.update.updateOrder", omsOrder);
	}

	/**
	 * 
	 * @Method Name : saveOrderGiftOrderCancelInfoNewTx
	 * @author : dennis
	 * @date : 2016. 10. 13.
	 * @description : 기프트콘 취소 안내
	 *
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrderGiftOrderCancelInfoNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String phone2 = order.getGiftPhone();
		String deliveryName = order.getGiftName();
		String productName = "";
		int productCnt = 0;

		for (OmsOrderproduct product : order.getOmsOrderproducts()) {

			if (productCnt == 0) {
				productName = product.getProductName();
			}

			productCnt++;

		}

		//sms
		String productName2 = productName;
		if (productCnt > 1) {
			productName2 = productName2 + " 외 " + (productCnt - 1) + "건";
		}

		OmsOrderTms orderTms = new OmsOrderTms();
		orderTms.setType("GIFT_INFO");
		orderTms.setToPhone(phone2);
		orderTms.setProductName(productName2);
		orderTms.setDeliveryName(deliveryName);
		orderTms.setOrderDt(order.getOrderDt());
		orderTmsService.saveOrderTms(orderTms);

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		return result;
	}

	/**
	 * 
	 * @Method Name : saveOrderGiftOrderCancelInfo
	 * @author : dennis
	 * @date : 2016. 10. 13.
	 * @description : 기프트콘 취소 안내
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrderGiftOrderCancelInfo() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		List<OmsOrder> orderList = (List<OmsOrder>) dao.selectList("oms.order.getGiftOrderCancelObject", null);

		int totalCnt = orderList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderList) {
			Map subResult = new HashMap();
			try {
				subResult = ((OrderService) getThis()).saveOrderGiftOrderCancelInfoNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			//실패시.
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n=============================");
				errorMsg.append("\n주문 ID : " + order.getOrderId());
				errorMsg.append("\n주문일자 : " + order.getOrderDt());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n=============================\n");
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;
			} else {
				successCnt++;
			}
		}

		resultMsg.append("\n총건수 	: " + totalCnt);
		resultMsg.append("\n성공 	: " + successCnt);
		resultMsg.append("\n실패 	: " + failCnt);

		if (BaseConstants.RESULT_FLAG_FAIL.equals(result.get(BaseConstants.RESULT_FLAG))) {
			resultMsg.append(errorMsg);
		}

		result.put(BaseConstants.RESULT_MESSAGE, resultMsg.toString());
		return result;
	}

}
