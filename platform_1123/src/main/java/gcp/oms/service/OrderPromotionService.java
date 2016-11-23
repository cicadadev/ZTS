package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.PmsUtil;
import gcp.external.model.membership.MembershipPointRes;
import gcp.external.model.membership.MembershipSavePointReq;
import gcp.external.service.MembershipService;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrdercoupon;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.pms.service.PresentService;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.SpsPresentproduct;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.model.search.SpsPresentSearch;
import gcp.sps.service.CouponService;
import gcp.sps.service.PointService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : OrderPromotionService.java
 * @author : dennis
 * @date : 2016. 11. 7.
 * @description : 주문 프로모션 service
 */
@Service("orderPromotionService")
public class OrderPromotionService extends BaseService {
	private static final Logger	logger	= LoggerFactory.getLogger(OrderPromotionService.class);

	@Autowired
	private OrderService		orderService;

	@Autowired
	private MembershipService	membershipService;

	@Autowired
	private CouponService		couponService;

	@Autowired
	private PresentService		presentService;

	@Autowired
	private PointService		pointService;

	@Autowired
	private CommonService		commonService;

	//////////////////////////////////////// 사은품 /////////////////////////////////////////////////

	public Map<String, String> makePresent(String selectPresent) {
		Map<String, String> map = new HashMap<String, String>();
		String[] sp = selectPresent.split("\\|\\|");
		for (String sps : sp) {
			String[] subSp = sps.split(":");
			String presentTypeCd = subSp[0];
			if (BaseConstants.PRESENT_TYPE_CD_PRODUCT.equals(presentTypeCd)) {
				String orderProductNo = subSp[1];
				String presentId = subSp[2];
				String productIds = subSp[3];
				String key = orderProductNo + "_" + presentId;

				map.put(key, productIds);

//				if (subSp.length > 4) {
//					String deliveryAddressNo = subSp[4];
//					String deliveryPolicyNo = subSp[5];
//
//					map.put(key + "dan", deliveryAddressNo);
//					map.put(key + "dpn", deliveryPolicyNo);
//				}
			} else if (BaseConstants.PRESENT_TYPE_CD_ORDER.equals(presentTypeCd)) {
				String presentId = subSp[1];
				String productIds = subSp[2];
				String key = presentId;

				map.put(key, productIds);

			} else if ("DELIVERY_INFO".equals(presentTypeCd)) {
				String presentId = subSp[1];
				String productId = subSp[2];
				String key = presentId + "_" + productId;
				String deliveryAddressNo = subSp[3];
//				String deliveryPolicyNo = subSp[4];

				map.put(key + "dan", deliveryAddressNo);
//				map.put(key + "dpn", deliveryPolicyNo);
			}
		}
		return map;
	}

//	private Map<String, String> makeCoupon(String selectCoupon) {
//		Map<String, String> map = new HashMap<String, String>();
//		String[] sc = selectCoupon.split("\\|\\|");
//		for (String scs : sc) {
//			String[] subSc = scs.split(":");
//			String couponTypeCd = subSc[0];
//			if (BaseConstants.COUPON_TYPE_CD_PRODUCT.equals(couponTypeCd)) {
//				String orderProductNo = subSc[1];
//				String couponIssueNos = subSc[2];
//				map.put(orderProductNo, couponIssueNos);
//			} else if (BaseConstants.COUPON_TYPE_CD_ORDER.equals(couponTypeCd)) {
//				String couponIssueNos = subSc[1];
//				map.put("ORDER_COUPON", couponIssueNos);
//			}
//		}
//		return map;
//	}

	public void setOrderProductPresent(List<SpsPresent> orderPresentList, OmsOrderproduct omsOrderproduct,
			Map<String, String> selectPresentMap, OmsOrder omsOrder) {

		String orderStat = omsOrder.getOrderStat();

		BigDecimal memberNo = omsOrder.getMemberNo();
		String deviceTypeCd = omsOrder.getDeviceTypeCd();
		String channelId = omsOrder.getChannelId();
		String storeId = omsOrder.getStoreId();
		String memGradeCd = omsOrder.getMemGradeCd();
		List<String> memberTypeCds = omsOrder.getMemberTypeCds();

		String dealId = omsOrderproduct.getDealId();
		String productId = omsOrderproduct.getProductId();
		String saleproductId = omsOrderproduct.getSaleproductId();
		String categoryId = omsOrderproduct.getCategoryId();
		String brandId = omsOrderproduct.getBrandId();
		BigDecimal orderProductNo = omsOrderproduct.getOrderProductNo();
		BigDecimal orderQty = omsOrderproduct.getOrderQty();

		SpsPresentSearch spsPresentSearch = new SpsPresentSearch();
		spsPresentSearch.setProductId(productId);
		spsPresentSearch.setBrandId(brandId);
		spsPresentSearch.setCategoryId(categoryId);
		spsPresentSearch.setDealId(dealId);
		spsPresentSearch.setStoreId(storeId);

		//사은품 조회
		List<SpsPresent> presentList = presentService.getApplyPresentList(spsPresentSearch);

		if (presentList != null) {

			//상품사은품 목록.
			List<SpsPresent> productPresentList = new ArrayList<SpsPresent>();
			for (SpsPresent spsPresent : presentList) {

				String presentTypeCd = spsPresent.getPresentTypeCd();

				boolean resultControl = false;
				BigDecimal controlNo = spsPresent.getControlNo();
				if (CommonUtil.isNotEmpty(controlNo)) {
					//제어 check
					CcsControlSearch search = new CcsControlSearch();
					search.setStoreId(storeId);
					search.setControlNo(controlNo);
					search.setChannelId(channelId);
					search.setMemberNo(memberNo);
					search.setDeviceTypeCd(deviceTypeCd);
					search.setMemberTypeCds(memberTypeCds);
					search.setMemGradeCd(memGradeCd);
					search.setControlType("PRESENT");
					search.setApplyObjectName("사은품," + spsPresent.getName());
					resultControl = commonService.checkControl(search);
				}

				if (CommonUtil.isEmpty(controlNo) || resultControl) {
					if (BaseConstants.PRESENT_TYPE_CD_ORDER.equals(presentTypeCd)) {//주문사은품
						setOrderPresent(orderPresentList, spsPresent, omsOrderproduct, selectPresentMap);
					} else if (BaseConstants.PRESENT_TYPE_CD_PRODUCT.equals(presentTypeCd)) {//상품사은품

						//사은품 선택 check
						if ("ORDERSHEET".equals(orderStat)) {	//주문서
							checkSelectPresent(spsPresent, orderProductNo, selectPresentMap);
						} else {
							if ("PRESENT_SELECT_TYPE_CD.SELECT".equals(spsPresent.getPresentSelectTypeCd())) {
								omsOrder.setOrderStat("PRESENT");
							}
						}
						//상품사은품 목록 담음.
						productPresentList.add(spsPresent);
					} else {
						throw new ServiceException("잘못된 PRESENT_TYPE_CD : " + presentTypeCd);
					}
				}

			}
			omsOrderproduct.setSpsPresents(productPresentList);
		}
	}

	private void checkSelectPresent(SpsPresent spsPresent, BigDecimal orderProductNo, Map<String, String> selectPresentMap) {

		logger.debug("============== 사은품 선택 check ===========");
		logger.debug("selectPresentMap : " + selectPresentMap);
		logger.debug("orderProductNo : " + orderProductNo);

		String checkPresent = spsPresent.getPresentId();
		logger.debug("checkPresent : " + checkPresent);

		if ("PRESENT_SELECT_TYPE_CD.SELECT".equals(spsPresent.getPresentSelectTypeCd())) {

			if (CommonUtil.isNotEmpty(selectPresentMap)) {

				if (BaseConstants.PRESENT_TYPE_CD_PRODUCT.equals(spsPresent.getPresentTypeCd())) {
					checkPresent = orderProductNo + "_" + checkPresent;
				}
				String productIds = selectPresentMap.get(checkPresent);
				logger.debug("productIds : " + productIds);

				//		else if (BaseConstants.PRESENT_TYPE_CD_ORDER.equals(spsPresent.getPresentTypeCd())) {
				//			
				//		}

				if (CommonUtil.isNotEmpty(productIds)) {
					String[] presentProductIds = productIds.split(",");
					logger.debug("presentProductIds : " + presentProductIds);

					List<Integer> delIdx = new ArrayList<Integer>();
					int idx = 0;
					for (SpsPresentproduct spsPresentproduct : spsPresent.getSpsPresentproducts()) {
						boolean ex = false;
						for (String presentProductId : presentProductIds) {
							if (presentProductId.equals(spsPresentproduct.getProductId())) {
								logger.debug("ex productId : " + spsPresentproduct.getProductId());
								ex = true;
							}
						}
						if (!ex) {
							delIdx.add(idx);
						}
						idx++;
					}
					int i = 0;
					for (int didx : delIdx) {
						spsPresent.getSpsPresentproducts().remove(didx - i);	//선택되지 않은 사은품 삭제
						i++;
					}

				} else {	//선택된것이 없으면 모두 삭제.
					spsPresent.setSpsPresentproducts(null);
				}

			} else {
				spsPresent.setSpsPresentproducts(null);
			}
		} else {

		}
		logger.debug("============== 사은품 선택 check ===========");
	}

	public void checkOrderPresent(OmsOrder omsOrder, List<SpsPresent> orderPresentList, Map<String, String> selectPresentMap) {

		List<Integer> delIdxList = new ArrayList<Integer>();

		int idx = 0;
		//주문사은품 적용 check
		//대상금액은 deal 적용된 판매가.
		for (SpsPresent spsPresent : orderPresentList) {

			if ("ORDERSHEET".equals(omsOrder.getOrderStat())) {	//주문서갈때.
				checkSelectPresent(spsPresent, null, selectPresentMap);
			}

			BigDecimal minOrderAmt = spsPresent.getMinOrderAmt();
			BigDecimal maxOrderAmt = spsPresent.getMaxOrderAmt();
			BigDecimal salePrice = new BigDecimal(0);
			logger.debug("========= 주문사은품 적용 상품==========");
			for (OmsOrderproduct presentOrderproduct : spsPresent.getOmsOrderproducts()) {
				logger.debug("/////////");
				logger.debug("productId : " + presentOrderproduct.getProductId());
				logger.debug("orgSaleprice : " + salePrice);
				BigDecimal totalSaleprice = presentOrderproduct.getTotalSalePrice();
				BigDecimal orderQty = presentOrderproduct.getOrderQty();
				logger.debug("totalSaleprice : " + totalSaleprice);
				logger.debug("orderQty : " + orderQty);
				salePrice = salePrice.add(totalSaleprice.multiply(orderQty));
				logger.debug("sum : " + salePrice);
				logger.debug("/////////");
			}
			logger.debug("MIN : " + minOrderAmt);
			logger.debug("MAX : " + maxOrderAmt);
			logger.debug("========= 주문사은품 적용 상품==========");

			if (!(minOrderAmt.compareTo(salePrice) <= 0 && maxOrderAmt.compareTo(salePrice) >= 0)) {
				delIdxList.add(idx);
			} else {
				if ("PRESENT_SELECT_TYPE_CD.SELECT".equals(spsPresent.getPresentSelectTypeCd())) {
					omsOrder.setOrderStat("PRESENT");
				}
			}
			idx++;
		}

		int i = 0;
		for (int delIdx : delIdxList) {
			orderPresentList.remove(delIdx - i);
			i++;
		}
	}

	private void setOrderPresent(List<SpsPresent> orderPresentList, SpsPresent spsPresent, OmsOrderproduct omsOrderproduct,
			Map selectPresentMap) {
		int cnt = 0;
		for (SpsPresent orderPresent : orderPresentList) {
			String presentId = orderPresent.getPresentId();
			if (presentId.equals(spsPresent.getPresentId())) {
				orderPresent.getOmsOrderproducts().add(omsOrderproduct);
				cnt++;
			}
		}
		//주문사은품 목록에 없을때.
		if (cnt == 0) {
			if (spsPresent.getOmsOrderproducts() != null) {
				spsPresent.getOmsOrderproducts().add(omsOrderproduct);
			} else {
				List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();
				omsOrderproducts.add(omsOrderproduct);
				spsPresent.setOmsOrderproducts(omsOrderproducts);
			}
			orderPresentList.add(spsPresent);
		}
	}
	//사은품저장
	public void savePresentproduct(OmsOrderproduct omsOrderproduct, SpsPresentproduct spsPresentproduct, SpsPresent spsPresent,
			List<OmsDelivery> deliveryList) throws Exception {

		BigDecimal zero = new BigDecimal(0);
		//사은품 배송정책 없을경우 insert위한 data 생성.
		OmsDelivery presentDelivery = new OmsDelivery();
		CcsDeliverypolicy cd = omsOrderproduct.getCcsDeliverypolicy();

		presentDelivery.setOrderId(omsOrderproduct.getOrderId());
		presentDelivery.setDeliveryAddressNo(omsOrderproduct.getDeliveryAddressNo());
		presentDelivery.setDeliveryPolicyNo(cd.getDeliveryPolicyNo());
		presentDelivery.setStoreId(cd.getStoreId());
		presentDelivery.setName(cd.getName());
		presentDelivery.setDeliveryServiceCd(cd.getDeliveryServiceCd());
		presentDelivery.setDeliveryFee(cd.getDeliveryFee());
		presentDelivery.setMinDeliveryFreeAmt(cd.getMinDeliveryFreeAmt());
		presentDelivery.setOrderDeliveryFee(zero);
		presentDelivery.setDeliveryCouponDcAmt(zero);
		presentDelivery.setApplyDeliveryFee(zero);
		presentDelivery.setWrapTogetherYn(BaseConstants.YN_N);
		presentDelivery.setOrderWrapFee(zero);
		presentDelivery.setWrapCouponDcAmt(zero);
		presentDelivery.setApplyWrapFee(zero);

		//사은품 배송적책 저장.
		insertPresentDelivery(deliveryList, presentDelivery);

		//사은품 저장.
		omsOrderproduct.setPresentId(spsPresent.getPresentId());	//사은품ID
		omsOrderproduct.setPresentName(spsPresent.getName());	//사은품프로모션명
		omsOrderproduct.setPresentMinOrderAmt(spsPresent.getMinOrderAmt());	//최소주문금액
		omsOrderproduct.setPresentMaxOrderAmt(spsPresent.getMaxOrderAmt());	//최대주문금액

		omsOrderproduct.setOrderProductNo(null);	//주문상품일련번호 초기화(새로 따기위함.)		
		omsOrderproduct.setTotalSalePrice(zero);
		omsOrderproduct.setProductPoint(zero);
		omsOrderproduct.setAddPoint(zero);
		omsOrderproduct.setTotalPoint(zero);
		omsOrderproduct.setPaymentAmt(zero);
		omsOrderproduct.setTax(zero);
//			omsOrderproduct.setSetQty(omsOrderproduct.getOrderQty());
		omsOrderproduct.setSetQty(new BigDecimal(1));	//빅횽 요청으로 1 setting
		omsOrderproduct.setCalibrateSalePrice(zero);	//세트보정판매가.
		omsOrderproduct.setCalibratePoint(zero);	//세트보정포인트.
		omsOrderproduct.setCalibrateProductDcAmt(zero);	//세트보정할인가.
		omsOrderproduct.setCalibratePlusDcAmt(zero);	//세트보정할인가.
		omsOrderproduct.setCalibrateOrderDcAmt(zero);	//세트보정할인가.
		omsOrderproduct.setProductCouponDcAmt(zero);
		omsOrderproduct.setPlusCouponDcAmt(zero);
		omsOrderproduct.setOrderCouponDcAmt(zero);

		dao.insertOne(omsOrderproduct);

		if (!"ORDER_PRODUCT_STATE_CD.REQ".equals(omsOrderproduct.getOrderProductStateCd())) {	//요청이 아닐때.
			//사은품재고차감
			orderService.updateStockQty(omsOrderproduct, true, "");
		}

	}

	private void insertPresentDelivery(List<OmsDelivery> deliveryList, OmsDelivery presentDelivery) throws Exception {

		boolean ex = false;
		for (OmsDelivery delivery : deliveryList) {
			BigDecimal deliveryAddressNo = delivery.getDeliveryAddressNo();
			BigDecimal deliveryPolicyNo = delivery.getDeliveryPolicyNo();
			BigDecimal presentDeliveryAddressNo = presentDelivery.getDeliveryAddressNo();
			BigDecimal presentDeliveryPolicyNo = presentDelivery.getDeliveryPolicyNo();

			if (deliveryAddressNo.compareTo(presentDeliveryAddressNo) == 0
					&& deliveryPolicyNo.compareTo(presentDeliveryPolicyNo) == 0) {
				ex = true;
			}
		}
		if (!ex) {
			logger.debug("사은품 배송정책 insert !!!!");
			logger.debug(presentDelivery.toString());
			dao.insertOne(presentDelivery);

			deliveryList.add(presentDelivery);
		}

	}

	//////////////////////////////////////// 쿠폰 /////////////////////////////////////////////////

	/**
	 * 
	 * @Method Name : setOrderProductCoupon
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 상품단위 적용대상 쿠폰 세팅
	 * 
	 *              1. 초기 노출쿠폰 자동적용 setting (상품당)
	 * 
	 *              2. 초기 노출쿠폰 자동적용 중복일경우 check하여 다음 최적쿠폰으로 적용 ( method : calcOptimalCoupon )
	 *
	 * @param omsOrder
	 * @param orderCouponList
	 * @param omsOrderproduct
	 * @throws Exception
	 */
	public void setOrderProductCoupon(OmsOrder omsOrder, List<OmsOrdercoupon> orderProductCouponList,
			List<OmsOrdercoupon> orderCouponList,
			OmsOrderproduct omsOrderproduct) throws Exception {

		BigDecimal memberNo = omsOrder.getMemberNo();
		String deviceTypeCd = omsOrder.getDeviceTypeCd();
		String channelId = omsOrder.getChannelId();
		String storeId = omsOrder.getStoreId();
		String memGradeCd = omsOrder.getMemGradeCd();
		List<String> memberTypeCds = omsOrder.getMemberTypeCds();

		String productId = omsOrderproduct.getProductId();
		String saleproductId = omsOrderproduct.getSaleproductId();
//		String categoryId = omsOrderproduct.getCategoryId();
//		String brandId = omsOrderproduct.getBrandId();
//		BigDecimal orderProductNo = omsOrderproduct.getOrderProductNo();		

		BigDecimal orderQty = omsOrderproduct.getOrderQty();

		String couponId = omsOrderproduct.getCouponId();
		String orderStat = omsOrder.getOrderStat();
		logger.debug("쿠폰 발급!!!!!!!!!! : " + orderStat);
		if ("ORDERSHEET".equals(orderStat) && CommonUtil.isNotEmpty(couponId) && CommonUtil.isNotEmpty(omsOrder.getMemberNo())) {

			logger.debug("쿠폰 발급!!!!!!!!!!");
			//쿠폰 발급
			SpsCouponissue issue = new SpsCouponissue();
			issue.setMemberNo(memberNo);
			issue.setCouponId(couponId);
			issue.setControllCheckType("FO_DN");//허용설정 체크타입(다운로드쿠폰)
			Map result = couponService.issueCoupon(issue,true);
			String resultCode = (String) result.get("resultCode");

			//TODO 확인필요.
//			if (!BaseConstants.RESULT_FLAG_SUCCESS.equals(resultCode)) {
//				String msg = (String) result.get("resultMsg");
//				throw new ServiceException("oms.bind.error", new String[] { msg });
//			}
		}

//		OmsOrdercoupon orgOptimalcoupon = omsOrderproduct.getOptimalProductCoupon();

		SpsCouponSearch spsCouponSearch = new SpsCouponSearch();
		spsCouponSearch.setStoreId(storeId);
		spsCouponSearch.setChannelId(channelId);
		spsCouponSearch.setMemberNo(memberNo);
		spsCouponSearch.setDeviceTypeCd(deviceTypeCd);
		spsCouponSearch.setMemberTypeCds(memberTypeCds);
		spsCouponSearch.setMemGradeCd(memGradeCd);
		spsCouponSearch.setProductId(productId);
		spsCouponSearch.setSaleproductId(saleproductId);
		spsCouponSearch.setTargetAmt(omsOrderproduct.getTotalSalePrice());
		spsCouponSearch.setDealId(omsOrderproduct.getDealId());	//딜적용되어있는 상품은 딜허용인것.
		spsCouponSearch.setMemberCouponYn("Y");	//발급받은것
		spsCouponSearch.setCommissionRate(omsOrderproduct.getCommissionRate());	//상품 수수료율
		spsCouponSearch.setControlType("COUPON_ORDER");

		// 쿠폰 조회
		List<SpsCoupon> couponList = couponService.getApplyCouponList(spsCouponSearch);

		if (couponList != null) {

			SpsCoupon optimalProductCoupon = null;
			SpsCoupon optimalPlusCoupon = null;

			BigDecimal productCouponCnt = new BigDecimal(0);
			BigDecimal plusCouponCnt = new BigDecimal(0);

			//상품쿠폰 목록.
			List<OmsOrdercoupon> productCouponList = new ArrayList<OmsOrdercoupon>();
			for (SpsCoupon orderCoupon : couponList) {
				OmsOrdercoupon newOrderCoupon = new OmsOrdercoupon();

				String couponTypeCd = orderCoupon.getCouponTypeCd();

				if (BaseConstants.COUPON_TYPE_CD_ORDER.equals(couponTypeCd)
						|| BaseConstants.COUPON_TYPE_CD_DELIVERY.equals(couponTypeCd)
						|| BaseConstants.COUPON_TYPE_CD_WRAP.equals(couponTypeCd)) {//주문쿠폰,배송비무료,선물포장비무료
					BeanUtils.copyProperties(orderCoupon, newOrderCoupon);
					setOrderCoupon(orderCouponList, newOrderCoupon, omsOrderproduct);
				} else if (BaseConstants.COUPON_TYPE_CD_PRODUCT.equals(couponTypeCd)
						|| BaseConstants.COUPON_TYPE_CD_PLUS.equals(couponTypeCd)) {//상품쿠폰,플러스쿠폰

					BigDecimal totalSalePrice = omsOrderproduct.getTotalSalePrice();
//					totalSalePrice = totalSalePrice.multiply(orderQty);

					BigDecimal minOrderAmt = orderCoupon.getMinOrderAmt();
					BigDecimal maxDcAmt = orderCoupon.getMaxDcAmt();
					BigDecimal couponDcAmt = orderCoupon.getApplyDcAmt();

					logger.debug("\n=========== 쿠폰 최저가 적용 check ==============");
					logger.debug("쿠폰 type : " + couponTypeCd);
					logger.debug("쿠폰 최소금액 : " + minOrderAmt);
					logger.debug("쿠폰 최대할인금액 : " + maxDcAmt);
					logger.debug("쿠폰 할인금액 : " + couponDcAmt);
					logger.debug("쿠폰 적용 대상금액1 : " + totalSalePrice);

					//최소 구매금액보다 클때 담음.
					if (totalSalePrice.compareTo(minOrderAmt) >= 0) {
						BeanUtils.copyProperties(orderCoupon, newOrderCoupon);
						productCouponList.add(newOrderCoupon);
						if (BaseConstants.COUPON_TYPE_CD_PRODUCT.equals(couponTypeCd)) {
							productCouponCnt = productCouponCnt.add(new BigDecimal(1));
						} else if (BaseConstants.COUPON_TYPE_CD_PLUS.equals(couponTypeCd)) {
							plusCouponCnt = plusCouponCnt.add(new BigDecimal(1));
						}

						if ((couponDcAmt.multiply(orderQty)).compareTo(maxDcAmt) <= 0) {

							//상품 최적쿠폰
							if (BaseConstants.COUPON_TYPE_CD_PRODUCT.equals(couponTypeCd)) {
								boolean ex = false;
								for (OmsOrdercoupon orgCoupon : orderProductCouponList) {
									if (orgCoupon.getCouponId().equals(orderCoupon.getCouponId())
											&& orgCoupon.getCouponIssueNo().compareTo(orderCoupon.getCouponIssueNo()) == 0) {
										ex = true;
										break;
									}
								}
								if (!ex && totalSalePrice.compareTo(minOrderAmt) >= 0) {
									if (couponId != null && couponId.equals(orderCoupon.getCouponId())) {
										optimalProductCoupon = orderCoupon;
									} else {
										if (optimalProductCoupon == null) {
											optimalProductCoupon = orderCoupon;
										} else {
											BigDecimal orgApplyDcAmt = optimalProductCoupon.getApplyDcAmt();
											BigDecimal applyDcAmt = orderCoupon.getApplyDcAmt();
											String orgSYn = optimalProductCoupon.getSingleApplyYn();
											if ("Y".equals(orgSYn)) {
												orgApplyDcAmt = orgApplyDcAmt.multiply(orderQty);
											}
											String sYn = orderCoupon.getSingleApplyYn();
											if ("Y".equals(sYn)) {
												applyDcAmt = applyDcAmt.multiply(orderQty);
											}

											if (applyDcAmt.compareTo(orgApplyDcAmt) > 0) {
												optimalProductCoupon = orderCoupon;
											}
										}
									}
								}

							}

						}

//						plus 최적쿠폰
//						if (BaseConstants.COUPON_TYPE_CD_PLUS.equals(couponTypeCd)) {
//
//							BigDecimal plusTotalSalePrice = totalSalePrice;
//							if (optimalProductCoupon != null
//									&& optimalProductCoupon.getApplyDcAmt().compareTo(new BigDecimal(0)) > 0) {
//								plusTotalSalePrice = plusTotalSalePrice.subtract(optimalProductCoupon.getApplyDcAmt());
//							}
//							logger.debug("쿠폰 적용 대상금액2 : " + plusTotalSalePrice);
//							if (plusTotalSalePrice.compareTo(minOrderAmt) >= 0) {
//								if (optimalPlusCoupon == null) {
//									optimalPlusCoupon = orderCoupon;
//								} else {
//									BigDecimal orgApplyDcAmt = optimalPlusCoupon.getApplyDcAmt().multiply(orderQty);
//									BigDecimal applyDcAmt = orderCoupon.getApplyDcAmt().multiply(orderQty);
//									if (applyDcAmt.compareTo(orgApplyDcAmt) > 0) {
//										optimalPlusCoupon = orderCoupon;
//									}
//								}
//							}
//						}


					}
				} else {
					throw new ServiceException("잘못된 COUPON_TYPE_CD : " + couponTypeCd);
				}
			}

			if (couponId != null) {	//최적가로 선택된 쿠폰있을때.
				if (optimalProductCoupon != null && !couponId.equals(optimalProductCoupon.getCouponId())) {
					//throw new ServiceException("최적가 쿠폰 적용 오류.");
				}
			}

			OmsOrdercoupon newOptimalCoupon = new OmsOrdercoupon();
			if (optimalProductCoupon != null) {
				BeanUtils.copyProperties(optimalProductCoupon, newOptimalCoupon);

				//다운로드쿠폰만 최적으로 담음. (노출쿠폰)
				if (BaseConstants.YN_Y.equals(optimalProductCoupon.getDownShowYn())) {
					omsOrderproduct.setOptimalProductCoupon(newOptimalCoupon);
					orderProductCouponList.add(newOptimalCoupon);
				}
			}
			
			//최적쿠폰적용
//			OmsOrdercoupon newPlusOptimalCoupon = new OmsOrdercoupon();
//			if (optimalPlusCoupon != null) {
//				BeanUtils.copyProperties(optimalPlusCoupon, newPlusOptimalCoupon);
//				omsOrderproduct.setOptimalPlusCoupon(newPlusOptimalCoupon);
//			}

			omsOrderproduct.setOmsOrdercoupons(productCouponList);
			omsOrderproduct.setProductCouponCnt(productCouponCnt);
			omsOrderproduct.setPlusCouponCnt(plusCouponCnt);

			//상품쿠폰이 없으면 plus쿠폰도 삭제.
//			if (omsOrderproduct.getOptimalProductCoupon() == null) {
//				omsOrderproduct.setOptimalPlusCoupon(null);
//			}
		}

	}

	/**
	 * 
	 * @Method Name : setOrderCoupon
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 주문쿠폰 세팅
	 * 
	 *              쿠폰들중 주문쿠폰을 주문쿠폰목록에 담는다.
	 *
	 * @param orderCouponList
	 * @param orderCoupon
	 * @param omsOrderproduct
	 */
	private void setOrderCoupon(List<OmsOrdercoupon> orderCouponList, OmsOrdercoupon orderCoupon,
			OmsOrderproduct omsOrderproduct) {
		int cnt = 0;
		for (OmsOrdercoupon omsOrderCoupon : orderCouponList) {
			String couponId = omsOrderCoupon.getCouponId();
			BigDecimal couponIssueNo = omsOrderCoupon.getCouponIssueNo();
			if (couponId.equals(orderCoupon.getCouponId()) && couponIssueNo.compareTo(orderCoupon.getCouponIssueNo()) == 0) {
				omsOrderCoupon.getOmsOrderproducts().add(omsOrderproduct);
				cnt++;
			}
		}
		//주문쿠폰 목록에 없을때.
		if (cnt == 0) {
			if (orderCoupon.getOmsOrderproducts() != null) {
				orderCoupon.getOmsOrderproducts().add(omsOrderproduct);
			} else {
				List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();
				omsOrderproducts.add(omsOrderproduct);
				orderCoupon.setOmsOrderproducts(omsOrderproducts);
			}
			orderCouponList.add(orderCoupon);
		}
	}

	/**
	 * 
	 * @Method Name : checkOrderCoupon
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 주문쿠폰 적용check            
	 * 
	 * @param orderTF
	 * @param orderCouponList
	 * @param omsOrder
	 */
	public void checkOrderCoupon(List<OmsOrdercoupon> orderCouponList, OmsOrder omsOrder) {

		List<Integer> delIdxList = new ArrayList<Integer>();

		OmsOrdercoupon optimalCoupon = null;

		int idx = 0;
		//주문쿠폰 적용 check
		for (OmsOrdercoupon orderCoupon : orderCouponList) {

			if ("COUPON_TYPE_CD.ORDER".equals(orderCoupon.getCouponTypeCd())) {

				BigDecimal minOrderAmt = orderCoupon.getMinOrderAmt();

				BigDecimal salePrice = new BigDecimal(0);

				BigDecimal optimalSalePrice = new BigDecimal(0);

				logger.debug("========= 주문쿠폰 적용 상품==========");
				for (OmsOrderproduct couponOrderproduct : orderCoupon.getOmsOrderproducts()) {
					logger.debug("/////////");
					logger.debug("productId : " + couponOrderproduct.getProductId());
					logger.debug("orgSaleprice : " + salePrice);
					BigDecimal totalSaleprice = couponOrderproduct.getTotalSalePrice();
					BigDecimal productCouponDcAmt = new BigDecimal(0);
					BigDecimal plusCouponDcAmt = new BigDecimal(0);
					if (CommonUtil.isNotEmpty(couponOrderproduct.getOptimalProductCoupon())) {
						productCouponDcAmt = (BigDecimal) CommonUtil
								.replaceNull(couponOrderproduct.getOptimalProductCoupon().getApplyDcAmt(), new BigDecimal(0));
					}

					if (CommonUtil.isNotEmpty(couponOrderproduct.getOptimalPlusCoupon())) {

						plusCouponDcAmt = (BigDecimal) CommonUtil
								.replaceNull(couponOrderproduct.getOptimalPlusCoupon().getApplyDcAmt(), new BigDecimal(0));
					}
					BigDecimal orderQty = couponOrderproduct.getOrderQty();
					
					logger.debug("totalSaleprice : " + totalSaleprice);
					logger.debug("productCouponDcAmt : " + productCouponDcAmt);
					logger.debug("plusCouponDcAmt : " + plusCouponDcAmt);
					logger.debug("orderQty : " + orderQty);

					salePrice = salePrice.add(totalSaleprice.multiply(orderQty));

					optimalSalePrice = optimalSalePrice.add((salePrice.subtract(productCouponDcAmt).subtract(plusCouponDcAmt)));

					logger.debug("sum : " + salePrice);
					logger.debug("/////////");
				}
				logger.debug("MIN : " + minOrderAmt);
				logger.debug("========= 주문쿠폰 적용 상품==========");

				if (!(minOrderAmt.compareTo(salePrice) <= 0)) {	//주문쿠폰적용 해당 안되면 제거
					delIdxList.add(idx);
				} else {

					//최적쿠폰적용

//					logger.debug("================== order coupon optimal ");
//					logger.debug("minOrderAmt : " + minOrderAmt);
//					logger.debug("optimalSalePrice : " + optimalSalePrice);
//					if (minOrderAmt.compareTo(optimalSalePrice) <= 0) {	//상품,plus쿠폰 적용뒤 적용될때.
//
//						setCouponData(orderCoupon, optimalSalePrice, null);
//						BigDecimal couponDcAmt = orderCoupon.getCouponDcAmt();
//						logger.debug("couponDcAmt : " + couponDcAmt);
//
//						//최적 쿠폰 조회
//						if (CommonUtil.isNotEmpty(optimalCoupon)) {
//							BigDecimal orgCouponDcAmt = optimalCoupon.getCouponDcAmt();
//							logger.debug("orgCouponDcAmt : " + orgCouponDcAmt);
//							if (couponDcAmt.compareTo(orgCouponDcAmt) > 0) {
//								optimalCoupon = orderCoupon;
//							}
//						} else {
//							optimalCoupon = orderCoupon;
//						}
//					}
//					logger.debug("================== order coupon optimal ");
				}
			}


			idx++;
		}

		int i = 0;
		for (int delIdx : delIdxList) {
			orderCouponList.remove(delIdx - i);
			i++;
		}

		omsOrder.setOptimalCoupon(optimalCoupon);
	}


	/**
	 * 
	 * @Method Name : calcOptimalCoupon
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description :
	 * 
	 *              주문단위 최적쿠폰 비교. 상품쿠폰 : 자동발금이나 발급받은것중 최적가 쿠폰있으면 그대로 setting plus쿠폰 : 발급받은것 수량에 따라
	 *              상품쿠폰있는곳에 setting
	 * 
	 *              1. 초기 노출쿠폰 자동적용 setting ( method : checkOrderCoupon )
	 * 
	 *              2. 초기 노출쿠폰 자동적용 중복일경우 check하여 다음 최적쿠폰으로 적용
	 *
	 * @param products
	 */
	public void calcOptimalCoupon(List<OmsOrderproduct> products) {

		List<OmsOrdercoupon> confirmList = new ArrayList<OmsOrdercoupon>();
		List<OmsOrdercoupon> confirmPlusList = new ArrayList<OmsOrdercoupon>();

		logger.debug("============ calcOptimalCoupon start");
		int idx = 0;
		for (OmsOrderproduct product : products) {
			
			OmsOrdercoupon optCoupon = product.getOptimalProductCoupon();
			calOptimalCouponSub(optCoupon, product, products, confirmList, "product", idx);

			//최적쿠폰적용 (plus)
			//plus 쿠폰 최적 중복여부 check
//			OmsOrdercoupon optPlusCoupon = product.getOptimalPlusCoupon();
//			calOptimalCouponSub(optPlusCoupon, product, products, confirmPlusList, "plus", idx);

			idx++;

			//deal에따라 적용되지 않는 쿠폰 제거
			deleteCouponDealCheck(product);
		}

		

		logger.debug("============ calcOptimalCoupon end");
	}

	private void calOptimalCouponSub(OmsOrdercoupon optCoupon, OmsOrderproduct product, List<OmsOrderproduct> products,
			List<OmsOrdercoupon> confirmList, String type, int idx) {
		if (optCoupon != null) {	//최적가쿠폰이 적용되어있을때.
			BigDecimal orderProdudctNo = product.getOrderProductNo();

			logger.debug("coupon type : " + type);
			logger.debug("productName : " + product.getProductName());
			logger.debug("productId : " + product.getProductId());
			logger.debug("productNo : " + orderProdudctNo);
			String initCouponId = product.getCouponId();
			logger.debug("initCouponId : " + initCouponId);

			String couponId = optCoupon.getCouponId();
			BigDecimal couponIssueNo = optCoupon.getCouponIssueNo();
			logger.debug("couponId : " + couponId);
			logger.debug("couponIssueNo : " + couponIssueNo);
			if (CommonUtil.isNotEmpty(couponIssueNo)) {
				BigDecimal couponDcAmt = optCoupon.getApplyDcAmt();
				logger.debug("couponDcAmt : " + couponDcAmt);

				int subIdx = 0;
				for (OmsOrderproduct subProduct : products) {

					if (idx < subIdx) {
						String subInitCouponId = subProduct.getCouponId();
						OmsOrdercoupon subOptCoupon = null;
						if ("product".equals(type)) {
							subOptCoupon = subProduct.getOptimalProductCoupon();
						} else if ("plus".equals(type)) {
							subOptCoupon = subProduct.getOptimalPlusCoupon();
						}
						logger.debug("				subInitCouponId : " + subInitCouponId);
						/**
						 * 상품 : 중복이면서 최적가 쿠폰이 아닌것이면 다음 최저가 쿠폰setting plus : 중복이면 다음 최저가 쿠폰 setting
						 */
						if (subOptCoupon != null && ("plus".equals(type) || CommonUtil.isEmpty(subInitCouponId))) {	//앞에서 넘어온 최적가 쿠폰 없을때. or plus 쿠폰일때.
							String subCouponId = subOptCoupon.getCouponId();
							BigDecimal subCouponIssueNo = subOptCoupon.getCouponIssueNo();
							logger.debug("				subProductNo : " + subProduct.getOrderProductNo());
							logger.debug("				subCouponId : " + subCouponId);
							logger.debug("				subCouponIssueNo : " + subCouponIssueNo);
							logger.debug("				subCouponDcAmt : " + subOptCoupon.getApplyDcAmt());
							if (CommonUtil.isNotEmpty(subCouponIssueNo)
									&& orderProdudctNo.compareTo(subProduct.getOrderProductNo()) != 0) {
								if (couponId.equals(subCouponId) && couponIssueNo.compareTo(subCouponIssueNo) == 0) {	//중복쿠폰
									if (CommonUtil.isNotEmpty(initCouponId)
											|| couponDcAmt.compareTo(subOptCoupon.getApplyDcAmt()) >= 0) {	//앞에서 넘오온 최적가 쿠폰잇을때, 할인금액이 작을때.
										confirmList.add(optCoupon);
										selectNextCoupon(subProduct, confirmList, type);
									}
								}
							}
						}
					}
					subIdx++;
				}
			}
		}
	}

	/**
	 * 
	 * @Method Name : selectNextCoupon
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 확정목록에 없는것 중 최저가.
	 *
	 * @param product
	 * @param confirmList
	 */
	private void selectNextCoupon(OmsOrderproduct product, List<OmsOrdercoupon> confirmList, String type) {

		OmsOrdercoupon optimalCoupon = null;

		for (OmsOrdercoupon coupon : product.getOmsOrdercoupons()) {
			boolean check = false;
			if ("product".equals(type) && "COUPON_TYPE_CD.PRODUCT".equals(coupon.getCouponTypeCd())) {
				check = true;
			} else if ("plus".equals(type) && "COUPON_TYPE_CD.PLUS".equals(coupon.getCouponTypeCd())) {
				check = true;
			}
			if (check) {
				String couponId = coupon.getCouponId();
				BigDecimal couponIssueNo = coupon.getCouponIssueNo();

				boolean ex = false;
				for (OmsOrdercoupon confirmCoupon : confirmList) {
					String cid = confirmCoupon.getCouponId();
					BigDecimal cin = confirmCoupon.getCouponIssueNo();
					if (cid.equals(couponId) && cin.compareTo(couponIssueNo) == 0) {
						ex = true;
					}
				}
				if (!ex) {
					if (optimalCoupon == null) {
						optimalCoupon = coupon;
					} else {
						if (optimalCoupon.getApplyDcAmt().compareTo(coupon.getApplyDcAmt()) < 0) {
							optimalCoupon = coupon;
						}
					}
				}
			}
		}

		if ("product".equals(type)) {
			if (optimalCoupon != null) {
				logger.debug("쿠폰 확정 : " + optimalCoupon.getCouponIssueNo());
				confirmList.add(optimalCoupon);
				product.setOptimalProductCoupon(optimalCoupon);
			} else {
				product.setOptimalProductCoupon(null);
//				product.setOptimalPlusCoupon(null);
			}
		} else if ("plus".equals(type)) {
			if (optimalCoupon != null) {
				logger.debug("쿠폰 확정 : " + optimalCoupon.getCouponIssueNo());
				confirmList.add(optimalCoupon);
//				if (product.getOptimalProductCoupon() != null) {
				product.setOptimalPlusCoupon(optimalCoupon);
//				} else {
//					product.setOptimalPlusCoupon(null);
//				}
			} else {
				product.setOptimalPlusCoupon(null);
			}
		}
	}

	/**
	 * 
	 * @Method Name : deleteCouponDealCheck
	 * @author : dennis
	 * @date : 2016. 8. 24.
	 * @description : 최적쿠폰 적용후 deal or 일반 같은 쿠폰만 남겨둠.
	 *
	 * @param product
	 */
	private void deleteCouponDealCheck(OmsOrderproduct product) {

		logger.debug("================= deal적용 중복쿠폰일때 삭제시작 ===================");
		List<Integer> delIdxList = new ArrayList<Integer>();
		String dealId = product.getDealId();
		boolean dealFlag = false;
		if (CommonUtil.isNotEmpty(dealId)) {
			dealFlag = true;
		}
		logger.debug("상품 deal id :" + dealId);

		int idx = 0;
		List<OmsOrdercoupon> resultCouponList = new ArrayList<OmsOrdercoupon>();
		for (OmsOrdercoupon coupon : product.getOmsOrdercoupons()) {

			logger.debug("				쿠폰 명 :" + coupon.getName());
			logger.debug("				쿠폰 ID :" + coupon.getCouponId());
			logger.debug("				쿠폰 issu no :" + coupon.getCouponIssueNo());
			logger.debug("				쿠폰 deal id :" + coupon.getDealId());

			if (dealFlag && dealId.equals(coupon.getDealId())) {
				logger.debug("deal 일때 같은 deal만");
			} else if (!dealFlag && CommonUtil.isEmpty(coupon.getDealId())) {
				boolean ex = false;
				for (OmsOrdercoupon resultCoupon : resultCouponList) {
					if (resultCoupon.getCouponId().equals(coupon.getCouponId())
							&& resultCoupon.getCouponIssueNo().compareTo(coupon.getCouponIssueNo()) == 0) {
						ex = true;
					}
				}
				if (!ex) {
					logger.debug("deal이 아닐때 일반");
					resultCouponList.add(coupon);
				} else {
					logger.debug("deal이 아닐때 일반쿠폰 중복 삭제!!");
					delIdxList.add(idx);
				}
			} else {
				logger.debug("삭제!!!!!");
				delIdxList.add(idx);
			}
			idx++;
		}

		int i = 0;
		for (int delIdx : delIdxList) {
			product.getOmsOrdercoupons().remove(delIdx - i);
			i++;
		}
		logger.debug("================= deal적용 중복쿠폰일때 삭제끝 ===================");
	}


	//주문쿠폰 list 생성
	public void calcOmsOrderCoupon(List<OmsOrdercoupon> ordercouponList, OmsOrdercoupon coupon, BigDecimal totalOrderAmt,
			OmsOrderproduct product)
			throws Exception {

		String paramCuponId = coupon.getCouponId();
		BigDecimal paramCouponIssueNo = coupon.getCouponIssueNo();

		boolean ex = false;
		for (OmsOrdercoupon omsOrdercoupon : ordercouponList) {
			String couponId = omsOrdercoupon.getCouponId();
			BigDecimal couponIssueNo = omsOrdercoupon.getCouponIssueNo();
			
			if (couponId.equals(paramCuponId) && couponIssueNo.compareTo(paramCouponIssueNo) == 0) {
				ex = true;

				setCouponData(omsOrdercoupon, totalOrderAmt, product);
			}
			
		}

		if (!ex) {
			coupon = (OmsOrdercoupon) dao.selectOne("oms.order.getOrdercouponByCoupon", coupon);
			setCouponData(coupon, totalOrderAmt, product);

			dao.insertOne(coupon);

			ordercouponList.add(coupon);
		}
	}


	private void setCouponData(OmsOrdercoupon coupon, BigDecimal totalOrderAmt, OmsOrderproduct product) {
//		String couponIssueStateCd = coupon.getCouponIssueStateCd();
//		if (coupon == null) {
//			throw new ServiceException("oms.order.nonExCoupon");
//		} else if (!"COUPON_ISSUE_STATE_CD.REG".equals(couponIssueStateCd)) {
//			throw new ServiceException("oms.order.nonCoupon", coupon.getName());
//		}


		if (CommonUtil.isNotEmpty(coupon)) {

			//쿠폰검증을 위한 data 처리			
			String dealApplyYn = coupon.getDealApplyYn();	//딜허용여부
			String couponTypeCd = coupon.getCouponTypeCd();
			String dcApplyTypeCd = coupon.getDcApplyTypeCd();
			BigDecimal dcValue = coupon.getDcValue();
			BigDecimal maxDcAmt = coupon.getMaxDcAmt();
			BigDecimal minOrderAmt = coupon.getMinOrderAmt();
			BigDecimal orgApplyDcAmt = coupon.getApplyDcAmt();
			BigDecimal applyDcAmt = new BigDecimal(0);
			
			String productDealId = "";
			if (CommonUtil.isNotEmpty(product)) {
				productDealId = product.getDealId();
			}
			boolean prc = false;

			//딜적용 가능여부가 N 이거나 dealId가 있으면 딜허용이 Y인것.
			//배송비포장비쿠폰은 check 안함.
			if (CommonUtil.isNotEmpty(productDealId) && "Y".equals(dealApplyYn)) {
				prc = true;
			} else if (CommonUtil.isEmpty(dealApplyYn) || "N".equals(dealApplyYn)) {
				prc = true;
			} else if ("COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd) || "COUPON_TYPE_CD.WRAP".equals(couponTypeCd)) {
				prc = true;
			}

			if (prc) {
				//배송비, 포장비는 무료! totalOrderAmt 와 같다.
				if ("COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd) || "COUPON_TYPE_CD.WRAP".equals(couponTypeCd)) {
					applyDcAmt = totalOrderAmt;
				} else {
					if ("DC_APPLY_TYPE_CD.AMT".equals(dcApplyTypeCd)) {
						if (totalOrderAmt.compareTo(dcValue) >= 0) {
							applyDcAmt = dcValue;
						} else {
							applyDcAmt = totalOrderAmt;
						}
					} else if ("DC_APPLY_TYPE_CD.RATE".equals(dcApplyTypeCd)) {
						applyDcAmt = (totalOrderAmt.multiply(dcValue).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP))
								.multiply(new BigDecimal(10));
						if (applyDcAmt.compareTo(maxDcAmt) > 0) {
							applyDcAmt = maxDcAmt;
						}
					}
				}

				if (CommonUtil.isEmpty(orgApplyDcAmt)) {
					orgApplyDcAmt = new BigDecimal(0);
				}

				//배송비 무료가 적용되는 만큼 할인가격을 더해준다.
				if ("COUPON_TYPE_CD.DELIVERY".equals(couponTypeCd)) {
					applyDcAmt = totalOrderAmt.add(orgApplyDcAmt);
				} else if ("COUPON_TYPE_CD.WRAP".equals(couponTypeCd)) {
					//포장쿠폰은 1000원
					applyDcAmt = new BigDecimal(1000);

				}

				//상품 쿠폰 or plus 쿠폰은 수량만큼 곱한다.
				if ("COUPON_TYPE_CD.PRODUCT".equals(couponTypeCd) || "COUPON_TYPE_CD.PLUS".equals(couponTypeCd)) {

					if ("Y".equals(coupon.getSingleApplyYn())) {	//1개적용시 수량을 곱하지 않는다.
					} else {
						BigDecimal orderQty = product.getOrderQty();
						applyDcAmt = applyDcAmt.multiply(orderQty);
					}

					applyDcAmt = orgApplyDcAmt.add(applyDcAmt);
				}
			} else {
				//적용대상이 아니고 주문 쿠폰일때 총계산대상 금액에서 해당 상품 대상 금액 제외 하고 다시 계산.
				if ("COUPON_TYPE_CD.ORDER".equals(couponTypeCd)) {

					BigDecimal objAmt = new BigDecimal(0);
					if (product != null) {
						BigDecimal productCouponDcAmt = (BigDecimal) CommonUtil.replaceNull(product.getProductCouponDcAmt(),
								new BigDecimal(0));
						BigDecimal plusCouponDcAmt = (BigDecimal) CommonUtil.replaceNull(product.getPlusCouponDcAmt(),
								new BigDecimal(0));

						objAmt = (product.getTotalSalePrice().subtract(productCouponDcAmt).subtract(plusCouponDcAmt))
								.multiply(product.getOrderQty());
					}
					logger.debug("주문쿠폰 적용 제외 대상 금액 : " + objAmt);
					totalOrderAmt = totalOrderAmt.subtract(objAmt);

					if ("DC_APPLY_TYPE_CD.AMT".equals(dcApplyTypeCd)) {
						if (totalOrderAmt.compareTo(dcValue) >= 0) {
							applyDcAmt = dcValue;
						} else {
							applyDcAmt = totalOrderAmt;
						}
					} else if ("DC_APPLY_TYPE_CD.RATE".equals(dcApplyTypeCd)) {
						applyDcAmt = (totalOrderAmt.multiply(dcValue).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP))
								.multiply(new BigDecimal(10));
						if (applyDcAmt.compareTo(maxDcAmt) > 0) {
							applyDcAmt = maxDcAmt;
						}
					}
				}
			}
			coupon.setApplyDcAmt(applyDcAmt);
			coupon.setCouponDcAmt(applyDcAmt);
		}

	}

	//////////////////////////////////////////////////// 포인트 /////////////////////////////////////////////////////

	public void setOrderProductPoint(OmsOrderproduct omsOrderproduct, OmsOrder omsOrder) {

		BigDecimal pointSaveRate = omsOrderproduct.getPointSaveRate();
		BigDecimal totalSalePrice = omsOrderproduct.getTotalSalePrice();
		BigDecimal productCouponDcAmt = (BigDecimal) CommonUtil.replaceNull(omsOrderproduct.getProductCouponDcAmt(),
				new BigDecimal(0));
		BigDecimal plusCouponDcAmt = (BigDecimal) CommonUtil.replaceNull(omsOrderproduct.getPlusCouponDcAmt(), new BigDecimal(0));
		totalSalePrice = totalSalePrice.subtract(productCouponDcAmt).subtract(plusCouponDcAmt);

		//포인트적용가 판매가 - 상품쿠폰 - 플러스쿠폰
		BigDecimal objPrice = PmsUtil.applyPointRate(totalSalePrice, pointSaveRate);//적용대상금액(개당)	

		String orderTypeCd = omsOrder.getOrderTypeCd();
		if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
			omsOrderproduct.setProductPoint(objPrice);	//상품적립포인트(개당)
			omsOrderproduct.setAddPoint(new BigDecimal(0));	//추가적립포인트(개당)
			omsOrderproduct.setTotalPoint(new BigDecimal(0));//총적립포인트(개당)
		} else {
			// 포인트 적립 프로모션 적용
			SpsPointsave spsPointsave = pointService.getPointPromotion(omsOrderproduct.getProductId(), objPrice);

			// 비로그인이거나 프로모션이 없으면 basicPoint 만 리턴함
			omsOrderproduct.setPointSaveId(spsPointsave.getPointSaveId());
			omsOrderproduct.setPointName(spsPointsave.getName());
			omsOrderproduct.setPointTypeCd(spsPointsave.getPointSaveTypeCd());
			omsOrderproduct.setPointValue(spsPointsave.getPointValue());

			omsOrderproduct.setProductPoint(objPrice);	//상품적립포인트(개당)
			omsOrderproduct.setAddPoint(spsPointsave.getAddPoint());	//추가적립포인트(개당)
			omsOrderproduct.setTotalPoint(spsPointsave.getTotalPoint());//총적립포인트(개당)
			omsOrderproduct.setSpsPointsave(spsPointsave);
		}
	}

	/**
	 * 
	 * @Method Name : saveOrderPointNewTx
	 * @author : dennis
	 * @date : 2016. 10. 20.
	 * @description : 포인트 적립
	 *
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrderPointNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		BigDecimal orderAmt = order.getOrderAmt();
		BigDecimal dcAmt = order.getDcAmt();
		BigDecimal tgtAmt = orderAmt.subtract(dcAmt);
				
		MembershipSavePointReq req = new MembershipSavePointReq();
		req.setMmbCertDvVlu(order.getMemberNo().toString());
		req.setAcmlPint(order.getTotalPointsave().toString());//적립포인트(필수)
		req.setUniqRcgnNo(order.getOrderId());//고유관리번호, 주문번호/취소번호등(필수)
		req.setTrscRsnCd("RS07");//거래사유코드 RS01:0to7 쇼핑몰 상품구매,RS02:상품평 작성(0to7), RS07:첫구매 상품평 작성(0to7)

		// 적립공통
		req.setTrscBizDvCd("21");// 21:포인트적립거래, 22:포인트적립거래취소

		//적립 데이터				
		req.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
		req.setTotSelAmt(orderAmt.toString());//총매출금액 ( 구매금액 )(필수)
		req.setTotDcAmt(dcAmt.toString());// 총할인금액 (쿠폰할인금액)(옵션)
		req.setMbrshDcAmt("0");// 멤버십할인금액(옵션)
		req.setAcmlTgtAmt(tgtAmt.toString());//포인트적립대상금액 (정책확인)
		req.setMmbBabySeqNo(null);//아기일련번호(옵션)
		req.setPrdctCd(null);//분유제품코드(옵션)
		req.setAftacmlYn("N");//사후적립여부(필수:N)
		req.setMbrshpintSetlYn("N");//멤버십포인트결제여부(필수:N)
		req.setPintUseTypCd("11");//포인트사용유형코드, 일반구매(옵션)
//		req.setUsePint("0");//사용포인트 (한거래를 사용요청 후 적립요청을 할 경우 Setting)(옵션)

		req.setPintAcmlTypCd("20");//포인트적립유형코드(필수, 10’- 일반, ‘20’- 이벤트) 
		req.setRmk("주문 적립 BATCH");//비고(옵션)


		MembershipPointRes res = membershipService.saveMemberPoint(req);

		if ("00000".equals(res.getResCd())) {
			result.put("saveDt", res.getApvDt());
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} else {
			result.put(BaseConstants.RESULT_MESSAGE, res.getResMsg());
		}

		return result;
	}

	/**
	 * 
	 * @Method Name : saveOrderPoint
	 * @author : dennis
	 * @date : 2016. 10. 20.
	 * @description : 포인트 적립
	 *
	 * @return
	 */
	public Map<String, Object> saveOrderPoint(OmsOrderSearch paramOrder) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		List<OmsOrder> orderPointList = (List<OmsOrder>) dao.selectList("oms.order.gerOrderPointTarget", paramOrder);

		int totalCnt = orderPointList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderPointList) {
			Map subResult = new HashMap();
			try {
				subResult = ((OrderPromotionService) getThis()).saveOrderPointNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			//실패시.
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n=============================");
				errorMsg.append("\n주문 ID : " + order.getOrderId());
				errorMsg.append("\n회원 MEMEBER_NO : " + order.getMemberNo());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n=============================\n");
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;
			} else {
				String saveDt = (String) subResult.get("saveDt");
				if (CommonUtil.isEmpty(saveDt)) {
					saveDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
				}
				order.setSaveDt(saveDt);
				dao.update("oms.order.updatePointSaveDt", order);
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
