package gcp.oms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gcp.ccs.model.CcsMessage;
import gcp.ccs.service.BaseService;
import gcp.external.model.membership.MembershipPointRes;
import gcp.external.model.membership.MembershipSavePointReq;
import gcp.external.model.membership.MembershipUsePointReq;
import gcp.external.service.KakaoService;
import gcp.external.service.MembershipService;
import gcp.external.service.PgService;
import gcp.external.util.KakaoUtil;
import gcp.mms.model.MmsDeposit;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsClaimWrapper;
import gcp.oms.model.OmsClaimdelivery;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrdercoupon;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentif;
import gcp.oms.model.OmsPointif;
import gcp.oms.model.pg.lgu.LguBase;
import gcp.oms.model.pg.lgu.LguCancelPartial;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsPaymentSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;
import kr.co.lgcns.module.lite.CnsPayWebConnector;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : ClaimService.java
 * @author : dennis
 * @date : 2016. 4. 20.
 * @description : Claim Service
 */
@Service
public class ClaimService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

	@Autowired
	private PgService pgService;

	@Autowired
	private KakaoService kakaoService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MembershipService membershipService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ClaimTmsService claimTmsService;

	public List<?> selectList(String queryId, Object claimSearch) throws Exception {
		return (List<?>) dao.selectList(queryId, claimSearch);
	}

	public Object selectOne(String orderId) throws Exception {
		return dao.selectOne("oms.claim.select.count", orderId);
	}

	public Object selectOne(String queryId, Object object) throws Exception {
		return dao.selectOne(queryId, object);
	}

	public int insert(Object claim) throws Exception {
		return dao.insertOneTable(claim);
	}

	public int update(Object claim) throws Exception {
		return dao.updateOneTable(claim);
	}

	/**
	 * 클레임 데이터 세팅
	 * 
	 * @Method Name : createClaim
	 * @author : victor
	 * @date : 2016. 10. 7.
	 * @description : 프론트에서 넘어온 클레임 정보로 클레임 데이터를 세팅한다.
	 *
	 * @param omsClaim
	 * @return CcsMessage
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CcsMessage makeClaimData(OmsClaimWrapper omsClaimWrapper, BigDecimal memberNo) throws Exception {

		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("클레임 중 알수 없는 에러가 발생하였습니다.");

		omsClaimWrapper.setClaimPath("FO");
		// omsClaimWrapper.setOmsClaim(omsClaim);
		omsClaimWrapper.setMemberNo(memberNo);

		// omsClaimWrapper.setOmsClaimdelivery();
		// omsClaimWrapper.setOmsDeliveryaddress();//교환,배송에서
		// omsClaimWrapper.setOmsPayments(omsPayments);

		// orderProduct.getOmsOrder().getMemberNo()

		// if (!"CLAIM_TYPE_CD.CANCEL".equals(omsClaimWrapper.getClaimTypeCd())) {
		String orderId = "";
		String orderProductNos = "";
		List<OmsOrderproduct> tmpProducts = omsClaimWrapper.getOmsOrderproducts();
		for (OmsOrderproduct orderproduct : tmpProducts) {
			orderId = orderproduct.getOrderId();
			orderProductNos += "," + orderproduct.getOrderProductNo();
		}
		OmsClaimSearch claimSearch = new OmsClaimSearch();
		claimSearch.setOrderId(orderId);
		claimSearch.setOrderProductNos(orderProductNos.replaceFirst(",", ""));

		// 1. omsOrderproducts
		String claimTypeCd = omsClaimWrapper.getClaimTypeCd();
		BigDecimal setClaimQty = BigDecimal.ZERO;
		String setClaimReason = "";
		List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();
		List<OmsOrderproduct> targetProducts = (List<OmsOrderproduct>) dao.selectList("oms.claim.select.targetList", claimSearch);
		for (OmsOrderproduct targetProduct : targetProducts) {
			for (OmsOrderproduct tmPproduct : tmpProducts) {
				if (targetProduct.getOrderProductNo().compareTo(tmPproduct.getOrderProductNo()) == 0) {
					OmsClaimproduct claimproduct = tmPproduct.getOmsClaimproduct();
					// 교환상품들...
					
					if ("CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
						targetProduct.setNewSaleProductId(tmPproduct.getNewSaleProductId());
						targetProduct.setNewSaleProductNm(tmPproduct.getNewSaleProductNm());
						
						// 구성상품 취소수량 세팅
						if ("ORDER_PRODUCT_TYPE_CD.SUB".equals(targetProduct.getOrderProductTypeCd())) {
							claimproduct.setClaimQty(setClaimQty.multiply(targetProduct.getSetQty()));
							claimproduct.setClaimReasonCd(setClaimReason);
						} else if ("ORDER_PRODUCT_TYPE_CD.SET".equals(targetProduct.getOrderProductTypeCd())) {
							setClaimQty = claimproduct.getClaimQty();
							setClaimReason = claimproduct.getClaimReasonCd();
						}
					}
					
					if (targetProduct.getAvailableClaimQty().compareTo(claimproduct.getClaimQty()) == 0) {
						targetProduct.setCancelAll(true);
					}
					targetProduct.setOmsClaimproduct(claimproduct);
					omsOrderproducts.add(targetProduct);

					// // 1. 취소
					// if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) {
					// }
					// // 2. 교환/반품
					// else {
					// }
				}
			}
		}
		omsClaimWrapper.setOmsOrderproducts(omsOrderproducts);

		// 2. omsClaimdelivery
		{
			OmsClaimdelivery omsClaimdelivery = new OmsClaimdelivery();
			OmsDeliveryaddress address = omsOrderproducts.get(0).getOmsDeliveryaddress();
			OmsDelivery policy = address.getOmsDeliverys().get(0);

			omsClaimdelivery.setOrderId(address.getOrderId());
			omsClaimdelivery.setDeliveryAddressNo(policy.getDeliveryAddressNo());
			omsClaimdelivery.setDeliveryPolicyNo(policy.getDeliveryPolicyNo());
			omsClaimdelivery.setDeliveryFee(policy.getDeliveryFee());
			// claimDelivery.setClaimNo(claimNo);
			omsClaimdelivery.setStoreId(policy.getStoreId());
			omsClaimdelivery.setRefundDeliveryFee(BigDecimal.ZERO);
			omsClaimdelivery.setOrderDeliveryFee(BigDecimal.ZERO);
			omsClaimdelivery.setReturnDeliveryFee(BigDecimal.ZERO);
			omsClaimdelivery.setExchangeDeliveryFee(BigDecimal.ZERO);
			omsClaimdelivery.setRefundWrapFee(BigDecimal.ZERO);
			omsClaimdelivery.setDeliveryServiceCd(policy.getDeliveryServiceCd());
			omsClaimdelivery.setReturnName(address.getName1());
			omsClaimdelivery.setReturnPhone1(address.getPhone1());
			omsClaimdelivery.setReturnPhone2(address.getPhone2());
			omsClaimdelivery.setReturnEmail(address.getEmail());
			omsClaimdelivery.setReturnZipCd(address.getZipCd());
			omsClaimdelivery.setReturnAddress1(address.getAddress1());
			omsClaimdelivery.setReturnAddress2(address.getAddress2());
			
			omsClaimdelivery.setDeliveryCouponDcCancelAmt(BigDecimal.ZERO);
			omsClaimdelivery.setWrapCouponDcCancelAmt(BigDecimal.ZERO);
			
			// claimDelivery.setOmsClaim(omsClaim);

			omsClaimWrapper.setOmsClaimdelivery(omsClaimdelivery); // 취소,교환,재배송,반품
			omsClaimWrapper.setOmsDeliveryaddress(address);// 교환,재배송
		}

		// 3. omsPayments - 취소,반품만 (교환,재배송 제외)
		if ("CLAIM_TYPE_CD.CANCEL".equals(omsClaimWrapper.getClaimTypeCd()) || "CLAIM_TYPE_CD.RETURN".equals(omsClaimWrapper.getClaimTypeCd())) {
			List<OmsPayment> omsPayments = (List<OmsPayment>) dao.selectList("oms.payment.selectList", claimSearch);
			omsClaimWrapper.setOmsPayments(omsPayments);
		}

		ccsmessage = this.insertClaimData(omsClaimWrapper, omsClaimWrapper.getUpdId());
		return ccsmessage;
	}

	/**
	 * 주문클레임 메인로직
	 * 
	 * @Method Name : insertClaimData
	 * @author : victor
	 * @date : 2016. 10. 21.
	 * @description : 
	 * 
	 * <b>DB 데이터 입력 순서 </b> 
	 * <pre>
	 *  01. oms_claim 
	 *  02. oms_claimproduct 
	 *  03. oms_claimdelivery 
	 *  04. oms_order 
	 *  05. oms_orderproduct 
	 *  06. oms_ordercoupon 
	 *  07. oms_deliveryaddress 
	 *  08. oms_delivery 
	 *  09. oms_payment 
	 *  10. oms_logistics 
	 *  11. pms_saleproduct 
	 *  12. 예치금 
	 *  13. 포인트 
	 * </pre>
	 * 
	 * <b> [[ 취소 ]] </b>
	 * <pre>
	 *  01. oms_claim : insert 
	 *  02. oms_claimproduct : insert 
	 *  03. oms_claimdelivery : insert 
	 *  04. oms_order : update [전체취소만] 
	 *  05. oms_orderproduct : update 
	 *  11. pms_saleproduct : update(stock) [미결제(가상계좌)는 안함] 
	 *  06. oms_ordercoupon : update 
	 *  09. oms_payment : insert(update)
	 * </pre>
	 * 
	 * <b> [[ 교환 ]] </b>
	 * <pre>
	 *  01. oms_claim : insert(반품상품) 
	 *  02. oms_claimproduct : insert(반품상품) 
	 *  03. oms_claimdelivery : insert(반품상품)(반품배송비) 
	 *  10. oms_logistics : insert(입고상품)
	 * 
	 *  05. oms_orderproduct : insert(출고상품) [반품상품은 갱신안함->입고완료시에 반품수량, 재고 갱신] 
	 *  11. pms_saleproduct : update(--stock) 
	 *  06. oms_deliveryaddress : insert(출고상품)(출고배송비) 
	 *  07. oms_delivery : insert(출고상품)
	 * </pre>
	 * 
	 * <b> [[ 반품 ]] </b>
	 * <pre>
	 *  01. oms_claim : insert(반품상품) 
	 *  02. oms_claimproduct : insert(반품상품) 
	 *  03. oms_claimdelivery : insert(반품상품)(반품배송비)(추가주문배송비) 
	 *  09. oms_payment : insert 
	 *  10. oms_logistics : insert(반품상품)
	 * 
	 *  환불완료 
	 *  06. oms_ordercoupon : update(환불금액) 
	 *  09. oms_payment : update
	 * </pre>
	 * 
	 * <b> [[ 재배송 ]] </b>
	 * <pre>
	 *  01. oms_claim : insert(반품상품) 
	 *  02. oms_claimproduct : insert(반품상품) 
	 *  03. oms_claimdelivery : insert(반품상품)(반품배송비) 
	 *  10. oms_logistics : insert(입고상품)
	 * 
	 *  05. oms_orderproduct : insert && update (출고상품) (입고상품) 
	 *  11. pms_saleproduct : update(--stock) [반품상품 재고 갱신은 안함] 
	 *  06. oms_deliveryaddress : insert(출고상품)(출고배송비) 
	 *  07. oms_delivery : insert(출고상품)
	 * </pre>
	 * 
	 * @param omsClaimWrapper
	 * @param updId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CcsMessage insertClaimData(OmsClaimWrapper omsClaimWrapper, String updId) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String orderId = "";
//		String msgCode = "";
		//
		// String phone2 = "";
		BigDecimal memberNo = null;
		BigDecimal logisticsInoutNo = null;

		String penaltyType = "BUYER";
//		String penaltyType = "STORE";
		String claimStateCd = omsClaimWrapper.getClaimStateCd();
		String claimTypeCd = omsClaimWrapper.getClaimTypeCd();
		String claimType = claimTypeCd.replaceFirst("CLAIM_TYPE_CD.", ""); // [CANCEL, EXCHANGE, RETURN, REDELIVERY]
		String claimPath = omsClaimWrapper.getClaimPath();
		String claimProductStateCd = null;
		BigDecimal claimNo = null;

		OmsClaim omsClaim = new OmsClaim();
		{
			// set oms_claim
			omsClaim.setProductCouponDcCancelAmt(BigDecimal.ZERO);
			omsClaim.setPlusCouponDcCancelAmt(BigDecimal.ZERO);
			omsClaim.setOrderCouponDcCancelAmt(BigDecimal.ZERO);
			
			omsClaim.setClaimTypeCd(claimTypeCd);
			omsClaim.setUpdDt(currentDate);
			omsClaim.setUpdId(updId);

			// oms_claimproduct
			List<OmsClaimproduct> omsClaimproducts = new ArrayList<OmsClaimproduct>();
			List<OmsOrderproduct> tmpProducts = omsClaimWrapper.getOmsOrderproducts();
			for (int i = 0; i < tmpProducts.size(); i++) {
				OmsOrderproduct orderProduct = tmpProducts.get(i);
				OmsClaimproduct omsClaimproduct = orderProduct.getOmsClaimproduct();
				if (i == 0) {
					orderId = orderProduct.getOrderId();
					memberNo = orderProduct.getOmsOrder().getMemberNo();
				}

				if (!StringUtils.isEmpty(omsClaimproduct.getClaimReasonCd())) { // 세트상품은 교환 재배송에서 null 임.(구성상품 단위로...)
					String claimReason = StringUtils.replace(omsClaimproduct.getClaimReasonCd(), "CLAIM_REASON_CD.", "");// 귀책사유 혼재되면 판매자 귀책
//					if ("CHANGE,DESIGN,PRICE,TYPING".contains(claimReason)) {
//						penaltyType = "BUYER";
//					}
					if (!"CHANGE,DESIGN,PRICE,TYPING".contains(claimReason)) {
						penaltyType = "STORE";
					}
				}

				omsClaimproducts.add(omsClaimproduct);
			}
			omsClaim.setOrderId(orderId);
			omsClaim.setOmsClaimproducts(omsClaimproducts);

			switch (claimType) {
				case "CANCEL":
					omsClaim.setAcceptDt(currentDate);
					omsClaim.setCompleteDt(currentDate);
					claimStateCd = "CLAIM_STATE_CD.COMPLETE";
					claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.CANCEL_ORDER";// 주문취소
					break;
				case "EXCHANGE":
					if ("STORE".equals(penaltyType)) {
						claimStateCd = "CLAIM_STATE_CD.REQ";// 신청
						claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.REQ";// 신청
					} else {
						if ("BO".equals(omsClaimWrapper.getClaimPath())) {
							claimStateCd = "CLAIM_STATE_CD.PAYMENT_READY";// 추가결제대기
							claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.REQ";// 신청
						} else {
							claimStateCd = "CLAIM_STATE_CD.ACCEPT";// 접수
							claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.RETURN_ORDER";// 입고지시
						}
					}
					break;
				case "RETURN":
					if ("STORE".equals(penaltyType)) {
						claimStateCd = "CLAIM_STATE_CD.REQ";// 신청
						claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.REQ";// 신청
					} else {
						claimStateCd = "CLAIM_STATE_CD.ACCEPT";// 접수
						claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.RETURN_ORDER";// 입고지시
						if ("BO".equals(omsClaimWrapper.getClaimPath())) {
							claimStateCd = "CLAIM_STATE_CD.ACCEPT";// 접수
							claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.RETURN_ORDER";// 입고지시
						}
					}
					break;
				case "REDELIVERY":
					claimStateCd = "CLAIM_STATE_CD.ACCEPT";// 접수(출고가 완료될때 완료)
					claimProductStateCd = "CLAIM_PRODUCT_STATE_CD.RETURN";// 입고완료
					break;
			}
			omsClaim.setReturnPickupTypeCd("RETURN_PICKUP_TYPE_CD." + penaltyType);
			omsClaim.setReqDt(currentDate);
			omsClaim.setClaimStateCd(claimStateCd);
			String claimStatus = claimStateCd.replaceFirst("CLAIM_STATE_CD.", ""); // [REQ, PAYMENT_READY, ACCEPT, COMPLETE]
			switch (claimStatus) {
				case "ACCEPT":
					omsClaim.setAcceptDt(currentDate);
					break;
				case "COMPLETET":
					omsClaim.setCompleteDt(currentDate);
					break;
			}
		}

		// 전체취소 판단
		boolean isCancelAll = false;

		// 전체취소
		if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd) && "1".equals(omsClaimWrapper.getIsAllCancel())) {
			ccsmessage = this.updateCancelAll(omsClaim, memberNo);
		} else {

			List<OmsClaimproduct> omsClaimproducts = omsClaim.getOmsClaimproducts();
			String orderProductNos = "";
			for (OmsClaimproduct claimProduct : omsClaimproducts) {
				orderId = claimProduct.getOrderId();
				orderProductNos += "," + claimProduct.getOrderProductNo();
			}

			OmsClaimSearch claimSearch = new OmsClaimSearch();
			claimSearch.setOrderId(orderId);
			claimSearch.setOrderProductNos(orderProductNos.replaceFirst(",", ""));

			if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) { // 주문취소만 전체취소로...
				isCancelAll = true;
			}
			// boolean hasOrderCoupon = false;
			BigDecimal bOrderCouponTargetAmt = BigDecimal.ZERO;
			BigDecimal bTotalRefundAmt = BigDecimal.ZERO;

			List<OmsOrderproduct> targetProducts = new ArrayList<OmsOrderproduct>();
			if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd) || "CLAIM_TYPE_CD.RETURN".equals(claimTypeCd)) {
				targetProducts = (List<OmsOrderproduct>) dao.selectList("oms.claim.select.targetList", claimSearch); // 전체상품
			} else {
				targetProducts = omsClaimWrapper.getOmsOrderproducts(); // 교환,반품,재배송 할 상품만 넘어옴
			}

			// 클레임쿠폰
			List<OmsOrdercoupon> claimCoupons = this.getClaimCoupon(targetProducts, omsClaimproducts);
			// for (OmsOrdercoupon claimCoupon : claimCoupons) {
			// if ("COUPON_TYPE_CD.ORDER".equals(claimCoupon.getCouponTypeCd())) {
			// hasOrderCoupon = true;
			// }
			// }
			for (OmsOrderproduct targetProduct : targetProducts) {
				String productType = targetProduct.getOrderProductTypeCd();
				if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(productType) || "ORDER_PRODUCT_TYPE_CD.SET".equals(productType)) {
					if (!targetProduct.isCancelAll()) {
						isCancelAll = false;
					}
					BigDecimal bTotalSaleAmt = targetProduct.getTotalSalePrice();
					BigDecimal bProductCouponAmt = targetProduct.getProductCouponDcAmt();
					BigDecimal bPlusCouponAmt = targetProduct.getPlusCouponDcAmt();

					BigDecimal bAvailQty = targetProduct.getAvailableClaimQty();
					BigDecimal bClaimQty = BigDecimal.ZERO;
					if (targetProduct.getOmsClaimproduct() != null) {
						bClaimQty = targetProduct.getOmsClaimproduct().getClaimQty();
					}

					// 주문쿠폰 가능금액
					bOrderCouponTargetAmt = bOrderCouponTargetAmt
							.add((bTotalSaleAmt.subtract(bProductCouponAmt.subtract(bPlusCouponAmt))).multiply(bAvailQty.subtract(bClaimQty)));
				}
			}

//			BigDecimal cancelPoint = BigDecimal.ZERO;
			if (!isCancelAll) {
				/***************************/
				/******* A. 반품로직 *********/
				/***************************/
				// 취소,교환,반품,재배송

				// 교환 교환 배송비
				OmsClaimdelivery omsClaimdelivery = omsClaimWrapper.getOmsClaimdelivery();
				if ("BUYER".equals(penaltyType)) {
					if ("CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
						omsClaimdelivery.setExchangeDeliveryFee(omsClaimdelivery.getDeliveryFee().multiply(BigDecimal.valueOf(2)));
					} else if ("CLAIM_TYPE_CD.RETURN".equals(claimTypeCd)) {
						// 클레임완료때 실제값을 저장한다.
						omsClaimdelivery.setOrderDeliveryFee(BigDecimal.ZERO);
						omsClaimdelivery.setReturnDeliveryFee(BigDecimal.ZERO);
//						omsClaimdelivery.setReturnDeliveryFee(omsClaimdelivery.getDeliveryFee());
					}
				} else {
					omsClaimdelivery.setExchangeDeliveryFee(BigDecimal.ZERO);
					omsClaimdelivery.setReturnDeliveryFee(BigDecimal.ZERO);
				}

				try {
					/** 반품.1. INSERT oms_claim */
					/** 반품.2. INSERT oms_claimproduct */
					claimNo = this.insertDBClaim(omsClaim);
					this.insertDBClaimproduct(omsClaim, omsClaimproducts, claimProductStateCd);
				} catch (ServiceException se) {
					ccsmessage.setSuccess(false);
					ccsmessage.setResultCode("-999");
					ccsmessage.setResultMessage(se.getMessage());
					return ccsmessage;
				}

				/***************************/
				/******* B. 출고로직 *********/
				/***************************/
				// 교환,재배송
				BigDecimal deliveryAddressNo = null;
				if ("CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd) || "CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
					OmsDeliveryaddress address = omsClaimWrapper.getOmsDeliveryaddress();

					StringBuffer newAddr = new StringBuffer();
					newAddr.append(address.getName1());
					newAddr.append(address.getZipCd());
					newAddr.append(address.getPhone1());
					newAddr.append(address.getPhone2());
					newAddr.append(address.getAddress1());
					newAddr.append(address.getAddress2());

					// 동일 배송지 인지 체크
					boolean existsAddress = false;
					address.setDeliveryAddressNo(null);
					List<OmsDeliveryaddress> addrList = (List<OmsDeliveryaddress>) dao.selectList("oms.delivery.selectOne", address);
					for (OmsDeliveryaddress a : addrList) {
						StringBuffer oldAddr = new StringBuffer();
						oldAddr.append(a.getName1());
						oldAddr.append(a.getZipCd());
						oldAddr.append(a.getPhone1());
						oldAddr.append(a.getPhone2());
						oldAddr.append(a.getAddress1());
						oldAddr.append(a.getAddress2());

						if (oldAddr.toString().contentEquals(newAddr)) {
							existsAddress = true;
							deliveryAddressNo = a.getDeliveryAddressNo();
						}
					}

					/** 출고.4. INSERT oms_deliveryaddress */
					if (!existsAddress) {
						if (dao.insertOneTable(address) > 0) {
							deliveryAddressNo = address.getDeliveryAddressNo();
							for (OmsDelivery delivery : address.getOmsDeliverys()) {
								delivery.setDeliveryAddressNo(deliveryAddressNo);

//								delivery.setApplyDeliveryFee(omsClaimdelivery.getExchangeDeliveryFee());// 오직 교환의 출고 상품에만 발생함. 20161105 - 없어짐..클레임쪽에 2배로 넣음.

								/** 출고.5. INSERT oms_delivery */
								dao.insertOneTable(delivery);
							}
						}
					}
				}

				/** 반품.3. INSERT oms_claimdelivery (포장비, 배송비) */
				omsClaimdelivery.setClaimNo(claimNo);
				omsClaimdelivery.setInsId(updId);
				omsClaimdelivery.setInsDt(currentDate);
				dao.insertOneTable(omsClaimdelivery);
				
				boolean existClaimDeliveryFee = false;// 주문의 클레임들 중에 원배송비가 발생했는지를 체크

				for (OmsOrderproduct targetProduct : targetProducts) {
					if (targetProduct.getClaimDeliveryFee().compareTo(BigDecimal.ZERO) > 0) {
						existClaimDeliveryFee = true;
					}
					OmsClaimproduct omsClaimproduct = targetProduct.getOmsClaimproduct();
					if (omsClaimproduct != null) { // 클레임 상품이 있는 경우만 대상
						BigDecimal bClaimQty = omsClaimproduct.getClaimQty();
						// 기존상품 반품
						bTotalRefundAmt = bTotalRefundAmt.add(bClaimQty.multiply(targetProduct.getTotalSalePrice()));

						if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) {
							targetProduct.setUpdId(updId);
							targetProduct.setCancelQty(bClaimQty);
							if (targetProduct.isCancelAll()) {
								targetProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.CANCEL");
								targetProduct.setCancelDt(currentDate);
							}
						} else {
							targetProduct.setClaimNo(claimNo);
							if ("CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd)) {// 재배송일때만 반품수량 처리 - 나머지는 입고완료때 처리...
								targetProduct.setReturnQty(bClaimQty);
							}
						}

						/** 반품.6. UPDATE oms_orderproduct */
						if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd) || "CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd)) {
							if (dao.update("oms.order.update.updateOrderProduct", targetProduct) > 0) {
								// 재배송(가반품으로 종료), 교환반품(입고완료일때 수행), 취소(가상계좌 입금대기 말고는 수행)
								if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) {
									boolean updateStock = true;
									List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();
									for (OmsPayment omsPayment : omsPayments) {
										if ("PAYMENT_METHOD_CD.VIRTUAL".equals(omsPayment.getPaymentMethodCd())) {
											if ("PAYMENT_STATE_CD.PAYMENT_READY".equals(omsPayment.getPaymentStateCd())) {
												updateStock = false;
											}
										}
									}
									if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd) && updateStock) {
										orderService.updateStock(targetProduct, bClaimQty);
									}
								}
							}
						}
						
						/** 반품.7. INSERT oms_logistics */
						if (!"CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) {
							if (!"CLAIM_STATE_CD.REQ".equals(claimStateCd) && !"CLAIM_STATE_CD.PAYMENT_READY".equals(claimStateCd)) {
								logisticsInoutNo = this.insertLogistics(targetProduct, claimTypeCd, claimNo, logisticsInoutNo);
							}
						}

						// 교환(재배송)상품 출고 (출고로직)
						if ("CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd) || "CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
							String orderDeliveryTypeCd = "ORDER_DELIVERY_TYPE_CD.ORDER";
							String orderProductStateCd = "ORDER_PRODUCT_STATE_CD.READY";// 출고지시대기

							if ("CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd)) {
								orderDeliveryTypeCd = "ORDER_DELIVERY_TYPE_CD.REDELIVERY";
								targetProduct.setNewSaleProductId(targetProduct.getSaleproductId()); // 자기상품 재출고
								targetProduct.setNewSaleProductNm(targetProduct.getSaleproductName());
							} else if ("CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
								orderDeliveryTypeCd = "ORDER_DELIVERY_TYPE_CD.EXCHANGE";
							}

							if ("CLAIM_STATE_CD.REQ".equals(claimStateCd) || "CLAIM_STATE_CD.PAYMENT_READY".equals(claimStateCd)) {
								orderProductStateCd = "ORDER_PRODUCT_STATE_CD.REQ";// 주문접수
							}

							targetProduct.setOrderQty(bClaimQty);
							targetProduct.setOrderProductStateCd(orderProductStateCd);
							targetProduct.setOrderDeliveryTypeCd(orderDeliveryTypeCd);// 재배송
							targetProduct.setOrderDt(currentDate);
							targetProduct.setInsId(updId);
							targetProduct.setDeliveryAddressNo(deliveryAddressNo);

							/** 출고.8. INSERT oms_orderproduct */
							if (dao.update("oms.claim.update.selectInsert", targetProduct) > 0) { // TODO - payment_amt 재계산 필요
								if (!"ORDER_PRODUCT_STATE_CD.REQ".equals(orderProductStateCd)) {
									orderService.updateStock(targetProduct, bClaimQty.negate());// 재고
								}
							}
						}
					}
				}

				// 배송비, 포장비, 배송쿠폰, 포장쿠폰
				if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd)) {
					omsClaimdelivery.setSelectKey("cancel");
					/** 반품.9. oms_ordercoupon */
					// bTotalRefundAmt = this.updateCouponCancel(omsClaim, omsClaimdelivery, claimCoupons, bOrderCouponTargetAmt, bTotalRefundAmt,
					// existClaimDeliveryFee);
					BigDecimal bCouponRefundAmt = this.updateCouponCancel(omsClaim, omsClaimdelivery, claimCoupons, bOrderCouponTargetAmt, existClaimDeliveryFee);
					bTotalRefundAmt = bTotalRefundAmt.add(bCouponRefundAmt);
				}
				
				/**
				 * 반품.10. oms_payment
				 * 
				 * <pre>
				 * 취소 : oms_payment && PG 환불(취소) 프로세스 바로 진행. 
				 * 반품 : oms_payment만 진행 && PG 환불은 입고완료 후 진행. 
				 * 교환 : 환불 프로세스 없음. 추가결제 금액 있으면 진행. 
				 * 재배송 : 환불 프로세스 없음.
				 * </pre>
				 */
				if (!CommonUtil.isEmpty(omsClaimWrapper.getOmsPayments())) {
					List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();
					int i = 0;
					for (OmsPayment omsPayment : omsPayments) {
						omsPayment.setInsDt(currentDate);
						omsPayment.setInsId(updId);
						omsPayment.setOrderId(orderId);
						omsPayment.setClaimNo(claimNo);
						omsPayment.setPaymentDt(currentDate);// 환불완료일시,결제일시
						omsPayment.setMemberNo(memberNo);
						
						omsPayment.setEscrowYn("N");
						omsPayment.setEscrowIfYn("N");
						omsPayment.setInterestFreeYn("N");
						omsPayment.setPartialCancelYn("N");
						omsPayment.setPaymentFee(BigDecimal.ZERO);

						if ("CLAIM_TYPE_CD.CANCEL".equals(claimTypeCd) || "CLAIM_TYPE_CD.RETURN".equals(claimTypeCd)) {
							if ("CLAIM_TYPE_CD.RETURN".equals(claimTypeCd)) {
								omsPayment.setPaymentAmt(BigDecimal.ZERO);
								omsPayment.setRefundReasonCd("REFUND_REASON_CD.RETURN");
							} else {
								omsPayment.setRefundReasonCd("REFUND_REASON_CD.CANCEL");
							}
							if ("CLAIM_STATE_CD.REQ".equals(claimStateCd) || "CLAIM_STATE_CD.PAYMENT_READY".equals(claimStateCd)) {
								omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND_REQ"); // 환불신청
							} else if ("CLAIM_STATE_CD.ACCEPT".equals(claimStateCd)) {
								omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND_REQ"); // 환불신청
							} else if ("CLAIM_STATE_CD.COMPLETE".equals(claimStateCd)) {
								omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND"); // 환불완료
							}
							omsPayment.setPaymentNo(null);

							if ("BO".equals(claimPath)) {
								if (omsPayment.getRefundAmt() != null && omsPayment.getRefundAmt().compareTo(BigDecimal.ZERO) > 0) {
									// 환불완료가 아니면 pg, 포인트, 예치금 연동은 안함.
									ccsmessage = this.insertPaymentCancelPart(omsPayment);
									if (ccsmessage.isSuccess()) {
										try {
											omsPayment.setPaymentNo(paymentService.getNewPaymentNo());	// 취소는 new payment_no 로
											if (dao.insertOneTable(omsPayment) < 1) {
												throw new ServiceException("oms.common.error", new Object[] { "결제 취소데이터 등록 중 에러 발생" });
											}
										} catch (Exception e) {
											throw new ServiceException("oms.common.error", new Object[] { e.getLocalizedMessage() });
										}
									}
								}
							} else {
								// TODO - 결제수단 많아질때..
								// TODO - FO 체크
//								if (omsPayment.getPaymentAmt().compareTo(BigDecimal.ZERO) > 0 && i == 0) {
								if (bTotalRefundAmt.compareTo(BigDecimal.ZERO) > 0 && i == 0) {
									omsPayment.setRefundAmt(bTotalRefundAmt);// BO는 화면에서 계산...
									ccsmessage = this.insertPaymentCancelPart(omsPayment);
									if (ccsmessage.isSuccess()) {
										try {
											omsPayment.setPaymentNo(paymentService.getNewPaymentNo());	// 취소는 new payment_no 로
											if (dao.insertOneTable(omsPayment) < 1) {
												throw new ServiceException("oms.common.error", new Object[] { "결제 취소데이터 등록 중 에러 발생" });
											}
										} catch (Exception e) {
											throw new ServiceException("oms.common.error", new Object[] { e.getLocalizedMessage() });
										}
									}
								}
							}
						} else if ("CLAIM_TYPE_CD.EXCHANGE".equals(omsClaimWrapper.getClaimTypeCd())) {// only front, bo 는 타지 않음.
							if ("PAYMENT_METHOD_CD.KAKAO".equals(omsPayment.getPaymentMethodCd())) {
//								omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getKakao().getAmt()));
								omsPayment.setPaymentNo(new BigDecimal(omsPayment.getKakao().getMerchantTxnNum()));
							} else {
//								omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_AMOUNT()));
								omsPayment.setPaymentNo(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_OID()));
							}
							omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.ADDPAYMENT");// 결제유형(추가결제)
							omsPayment.setPaymentAmt(omsClaimdelivery.getExchangeDeliveryFee());
							ccsmessage = paymentService.insertNewPayment(omsPayment);
						}
					}
				}
			} else {
				ccsmessage = this.updateCancelAll(omsClaim, memberNo);
			}
		}
		
		// 취소,교환,반품,재배송 sms
		if (claimTypeCd.equals("CLAIM_TYPE_CD.CANCEL")) {
			ccsmessage = claimTmsService.createClaimTms(omsClaimWrapper, "103");
		} else {
			if ("CLAIM_STATE_CD.ACCEPT".equals(claimStateCd)) {
				if (claimTypeCd.equals("CLAIM_TYPE_CD.EXCHANGE")) {
					ccsmessage = claimTmsService.createClaimTms(omsClaimWrapper, "121");
				} else if (claimTypeCd.equals("CLAIM_TYPE_CD.RETURN")) {
					ccsmessage = claimTmsService.createClaimTms(omsClaimWrapper, "123");
				} else if (claimTypeCd.equals("CLAIM_TYPE_CD.REDELIVERY")) {
					ccsmessage = claimTmsService.createClaimTms(omsClaimWrapper, "128");
				}
			}
		}
		return ccsmessage;
	}

	/**
	 * BO - 교환,반품 의 반려,접수,철회
	 * 
	 * @Method Name : updateClaimData
	 * @author : victor
	 * @date : 2016. 10. 22.
	 * @description :
	 * <pre>
	 *	반품의  반려,접수,철회
	 *	교환의  반려,접수,철회
	 * </pre>
	 * @param omsOrderproducts
	 * @return
	 * @throws Exception
	 */
	public CcsMessage updateClaimData(OmsClaimWrapper omsClaimWrapper) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);
		ccsmessage.setResultCode("0000");
		ccsmessage.setResultMessage("클레임 성공");

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String updId = SessionUtil.getLoginId();

		String claimTypeCd = omsClaimWrapper.getClaimTypeCd();
		String claimStateCd = omsClaimWrapper.getClaimStateCd();
		List<OmsOrderproduct> omsOrderproducts = omsClaimWrapper.getOmsOrderproducts();
		List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();

		try {
			OmsClaim omsClaim = new OmsClaim();
			omsClaim.setUpdDt(currentDate);
			omsClaim.setUpdId(updId);
			omsClaim.setClaimStateCd(claimStateCd); // accept or reject

			int updCnt = 0;
			for (OmsOrderproduct omsOrderproduct : omsOrderproducts) {
				omsOrderproduct.setUpdDt(currentDate);
				omsOrderproduct.setUpdId(updId);

				OmsClaimproduct claimproduct = omsOrderproduct.getOmsClaimproduct();
				claimproduct.setUpdDt(currentDate);
				claimproduct.setUpdId(updId);
				claimproduct.setOrderId(omsOrderproduct.getOrderId());
				claimproduct.setOrderProductNo(omsOrderproduct.getOrderProductNo());

				BigDecimal bLogisticsNo = null;
				if ("CLAIM_STATE_CD.REJECT".equals(claimStateCd)) {
					omsClaim.setCancelDt(currentDate);// 반려일자
					claimproduct.setClaimProductStateCd("CLAIM_PRODUCT_STATE_CD.REJECT");// 상태(반려)
					omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.CANCELCLAIM");// 클레임취소// 반려일때 출고상품 클레임 취소 해줌
				} else if ("CLAIM_STATE_CD.WITHDRAW".equals(claimStateCd)) {
					omsClaim.setCancelDt(currentDate);// 철회일자
					claimproduct.setClaimProductStateCd("CLAIM_PRODUCT_STATE_CD.CANCEL");// 상태(철회??)
					omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.CANCELCLAIM");// 클레임취소// 철회일때 출고상품 클레임 취소 해줌
					
					OmsLogistics omsLogistics = new OmsLogistics();
					omsLogistics.setOrderId(claimproduct.getOrderId());
					omsLogistics.setOrderProductNo(claimproduct.getOrderProductNo());
					omsLogistics.setClaimNo(claimproduct.getClaimNo());
//					omsLogistics.setInReserveQty(claimproduct.getClaimQty().toString());
					omsLogistics.setUpdDt(currentDate);
					omsLogistics.setUpdId(updId);
					omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.CANCELRETURN");
					
					/* update oms_logistics */
					dao.update("oms.claim.update.logistics", omsLogistics);
				} else {
					omsClaim.setAcceptDt(currentDate);// 접수일자
					claimproduct.setReturnOrderDt(currentDate);// 입고지시일자
					claimproduct.setClaimProductStateCd("CLAIM_PRODUCT_STATE_CD.RETURN_ORDER");// 상태(입고지시)
					omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");// 출고지시대기

					// 2. oms_logistics ==> 입고예정 데이터 등록
					bLogisticsNo = this.insertLogistics(omsOrderproduct, "EXCHANGE OR RETURN", claimproduct.getClaimNo(), bLogisticsNo);
				}

				// A. 입고상품
				// update oms_claimproduct
				updCnt = dao.updateOneTable(claimproduct);

				// B. 출고상품
				//3. oms_orderproduct ==> 상태(출고지시대기)
				if (updCnt > 0 && "CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)) {
					OmsOrderproduct newProduct = new OmsOrderproduct();
					newProduct.setUpdDt(currentDate);
					newProduct.setUpdId(updId);
					newProduct.setOrderId(omsOrderproduct.getOrderId());
					newProduct.setOrderProductNo(omsOrderproduct.getNewOrderProductNo());
					newProduct.setOrderProductStateCd(omsOrderproduct.getOrderProductStateCd());

					// 1. oms_orderproduct ==> 상태(수량에 따른 주문취소), 수량(반품수량)
					dao.update("oms.order.update.updateOrderProduct", newProduct);
				}

				omsClaim.setOrderId(omsOrderproduct.getOrderId());
				omsClaim.setClaimNo(claimproduct.getClaimNo());
			}

			// 4. update oms_claim
			dao.updateOneTable(omsClaim);
			
			// 5. update oms_payment
			if (claimTypeCd.equals("CLAIM_TYPE_CD.RETURN")) {
				if ("CLAIM_STATE_CD.REJECT".equals(claimStateCd) || "CLAIM_STATE_CD.WITHDRAW".equals(claimStateCd)) {
					for (OmsPayment omsPayment : omsPayments) {
						if (omsPayment.getRefundAmt().compareTo(BigDecimal.ZERO) > 0) {
							omsPayment.setOrderId(omsClaim.getOrderId());
							omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND_CANCEL");
							omsPayment.setUpdDt(currentDate);
							omsPayment.setUpdId(updId);

							dao.update("oms.payment.updatePayment", omsPayment);
						}
					}
				}
			} else if (claimTypeCd.equals("CLAIM_TYPE_CD.EXCHANGE")) {
				if ("CLAIM_STATE_CD.WITHDRAW".equals(claimStateCd)) {
					OmsPayment omsPayment = new OmsPayment();
					omsPayment.setOrderId(omsClaim.getOrderId());
					omsPayment.setClaimNo(omsClaim.getClaimNo());

					Object object = dao.selectOne("oms.payment.selectOne", omsPayment);
					if (object != null) {
						omsPayment = (OmsPayment) object;
						omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND"); // 교환 철회에 의한 추가결제 환불
						omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.REFUND");
						omsPayment.setRefundReasonCd("REFUND_REASON_CD.CANCEL");
						omsPayment.setPaymentDt(omsClaim.getUpdDt());// 환불완료일시,결제일시
						omsPayment.setInsDt(currentDate);
						omsPayment.setInsId(updId);
						omsPayment.setEscrowYn("N");
						omsPayment.setEscrowIfYn("N");
						omsPayment.setInterestFreeYn("N");
						omsPayment.setPartialCancelYn("N");
						omsPayment.setPaymentFee(BigDecimal.ZERO);

						boolean doLGU = this.createPaymethod(omsPayment, ccsmessage, "0");
						
						if (doLGU) {
							ccsmessage = this.cancelAll(omsPayment);
							if (ccsmessage.isSuccess()) {
								omsPayment.setPgShopId(ccsmessage.getId());
								omsPayment.setPgCancelNo(ccsmessage.getNo());
							} else {
								throw new ServiceException("oms.pg.error.allcancel", new Object[] { ccsmessage.getResultMessage() });
							}
						}

						if (ccsmessage.isSuccess()) {
							omsPayment.setPaymentNo(paymentService.getNewPaymentNo());	// 취소는 new payment_no 로
							dao.insertOneTable(omsPayment);
						} else {
							throw new ServiceException("oms.pg.error.allcancel", new Object[] { ccsmessage.getResultMessage() });
						}						
					}
				}
			}
		} catch (Exception e) {
			ccsmessage.setResultMessage("클레임 접수 처리 중 에러가 발생하였습니다. ");
			throw new ServiceException("oms.common.error", new Object[] { ccsmessage.getResultMessage() });
		}

		// 취소,교환,반품,재배송 sms
		if ("CLAIM_STATE_CD.ACCEPT".equals(claimStateCd)) {
			if (claimTypeCd.equals("CLAIM_TYPE_CD.EXCHANGE")) {
				claimTmsService.createClaimTms(omsClaimWrapper, "121");
			} else if (claimTypeCd.equals("CLAIM_TYPE_CD.RETURN")) {
				claimTmsService.createClaimTms(omsClaimWrapper, "123");
			}
		}
		return ccsmessage;
	}

	/**
	 * BO - 교환완료
	 * 
	 * @Method Name : updateClaimComplete
	 * @author : victor
	 * @date : 2016. 10. 22.
	 * @description :
	 *
	 * @param omsClaimWrapper
	 * @param updId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public CcsMessage updateExchangeComplete(OmsClaimWrapper omsClaimWrapper, String updId) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);
		ccsmessage.setResultCode("0000");
		ccsmessage.setResultMessage("클레임 성공");

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String orderId = "";

		String penaltyType = "BUYER";
//		String penaltyType = "STORE";
		String claimTypeCd = omsClaimWrapper.getClaimTypeCd();
		BigDecimal claimNo = null;

		OmsClaim omsClaim = new OmsClaim();
		{

			// oms_claimproduct
			List<OmsClaimproduct> omsClaimproducts = new ArrayList<OmsClaimproduct>();
			List<OmsOrderproduct> tmpProducts = omsClaimWrapper.getOmsOrderproducts();
			for (int i = 0; i < tmpProducts.size(); i++) {
				OmsOrderproduct orderProduct = tmpProducts.get(i);
				OmsClaimproduct claimProduct = orderProduct.getOmsClaimproduct();
				if (i == 0) {
					orderId = orderProduct.getOrderId();
					claimNo = claimProduct.getClaimNo();
				}

				String claimReason = StringUtils.replace(claimProduct.getClaimReasonCd(), "CLAIM_REASON_CD.", "");// 귀책사유
				if (!"CHANGE,DESIGN,PRICE,TYPING".contains(claimReason)) {	// 귀책사유 혼재되어 있는경우 판매자 귀책
					penaltyType = "STORE";
				}
				omsClaimproducts.add(claimProduct);
			}
			// set oms_claim
			omsClaim.setClaimNo(claimNo);
			omsClaim.setClaimTypeCd(claimTypeCd);
			omsClaim.setUpdDt(currentDate);
			omsClaim.setUpdId(updId);
			omsClaim.setOrderId(orderId);
			omsClaim.setReturnPickupTypeCd("RETURN_PICKUP_TYPE_CD." + penaltyType);
			omsClaim.setCompleteDt(currentDate);
			omsClaim.setClaimStateCd("CLAIM_STATE_CD.COMPLETE");
		}

		if (dao.update("oms.claim.update.claim", omsClaim) < 1) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultCode("-999");
			ccsmessage.setResultMessage("클레임 완료처리에 실패하였습니다.");
		}

		return ccsmessage;
	}

	/**
	 * BO - 반품완료
	 * 
	 * @Method Name : updateReturnComplete
	 * @author : victor
	 * @date : 2016. 10. 22.
	 * @description :
	 *
	 * @param omsClaimWrapper
	 * @param updId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CcsMessage updateReturnComplete(OmsClaimWrapper omsClaimWrapper, String updId) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String orderId = "";

//		String penaltyType = "STORE";
		String penaltyType = "BUYER";
		String claimTypeCd = omsClaimWrapper.getClaimTypeCd();
		// String claimStateCd = omsClaimWrapper.getClaimStateCd();
		BigDecimal claimNo = null;
		BigDecimal memberNo = null;

		OmsClaim omsClaim = new OmsClaim();
		{

			// oms_claimproduct
			List<OmsClaimproduct> omsClaimproducts = new ArrayList<OmsClaimproduct>();
			List<OmsOrderproduct> tmpProducts = omsClaimWrapper.getOmsOrderproducts();
			for (int i = 0; i < tmpProducts.size(); i++) {
				OmsOrderproduct orderProduct = tmpProducts.get(i);
				OmsClaimproduct claimProduct = orderProduct.getOmsClaimproduct();
				if (i == 0) {
					orderId = orderProduct.getOrderId();
					memberNo = orderProduct.getOmsOrder().getMemberNo();
					claimNo = claimProduct.getClaimNo();
				}

				String claimReason = StringUtils.replace(claimProduct.getClaimReasonCd(), "CLAIM_REASON_CD.", "");// 귀책사유
				if (!"CHANGE,DESIGN,PRICE,TYPING".contains(claimReason)) {	// 귀책사유 혼재되어 있는 경우 판매자 귀책
					penaltyType = "STORE";
				}
				omsClaimproducts.add(claimProduct);
			}
			// set oms_claim
			omsClaim.setClaimNo(claimNo);
			omsClaim.setClaimTypeCd(claimTypeCd);
			omsClaim.setUpdDt(currentDate);
			omsClaim.setUpdId(updId);
			omsClaim.setOrderId(orderId);
			omsClaim.setOmsClaimproducts(omsClaimproducts);
			omsClaim.setReturnPickupTypeCd("RETURN_PICKUP_TYPE_CD." + penaltyType);
			omsClaim.setCompleteDt(currentDate);
			omsClaim.setClaimStateCd("CLAIM_STATE_CD.COMPLETE");
		}


		List<OmsClaimproduct> omsClaimproducts = omsClaim.getOmsClaimproducts();
		String orderProductNos = "";
		for (OmsClaimproduct claimProduct : omsClaimproducts) {
			orderId = claimProduct.getOrderId();
			orderProductNos += "," + claimProduct.getOrderProductNo();
		}

		OmsClaimSearch claimSearch = new OmsClaimSearch();
		claimSearch.setOrderId(orderId);
		claimSearch.setOrderProductNos(orderProductNos.replaceFirst(",", ""));

		BigDecimal bOrderCouponTargetAmt = BigDecimal.ZERO;
		BigDecimal bTotalRefundAmt = BigDecimal.ZERO;
		BigDecimal bCancelSavePoint = BigDecimal.ZERO;

		List<OmsOrderproduct> targetProducts = (List<OmsOrderproduct>) dao.selectList("oms.claim.select.targetList", claimSearch); // 전체상품

		boolean existClaimDeliveryFee = false;// 주문의 클레임들 중에 원배송비가 발생했는지를 체크
		// 클레임쿠폰
		List<OmsOrdercoupon> claimCoupons = this.getClaimCoupon(targetProducts, omsClaimproducts);
		for (OmsOrderproduct targetProduct : targetProducts) {
			if (targetProduct.getClaimDeliveryFee().compareTo(BigDecimal.ZERO) > 0) {
				existClaimDeliveryFee = true;
			}
			if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(targetProduct.getOrderProductTypeCd())
					|| "ORDER_PRODUCT_TYPE_CD.SET".equals(targetProduct.getOrderProductTypeCd())) {

				BigDecimal bTotalSaleAmt = targetProduct.getTotalSalePrice();
				BigDecimal bProdCpnAmt = targetProduct.getProductCouponDcAmt();
				BigDecimal bPlusCpnAmt = targetProduct.getPlusCouponDcAmt();

				BigDecimal bAvailQty = targetProduct.getAvailableClaimQty();
				BigDecimal bClaimQty = BigDecimal.ZERO;
				if (targetProduct.getOmsClaimproduct() != null) {
					bClaimQty = targetProduct.getOmsClaimproduct().getClaimQty();
				}

				// 주문쿠폰 가능금액
				bOrderCouponTargetAmt = bOrderCouponTargetAmt
						.add((bTotalSaleAmt.subtract(bProdCpnAmt.subtract(bPlusCpnAmt))).multiply(bAvailQty.subtract(bClaimQty)));
			}

			if (targetProduct.getOmsClaimproduct() != null) { // 클레임 상품이 있는 경우만 대상
				BigDecimal bClaimQty = targetProduct.getOmsClaimproduct().getClaimQty();
				bTotalRefundAmt = bTotalRefundAmt.add(bClaimQty.multiply(targetProduct.getTotalSalePrice()));

				targetProduct.setUpdId(updId);
				targetProduct.setReturnQty(bClaimQty);

				/** 반품.6. UPDATE oms_orderproduct */
				if (dao.update("oms.order.update.updateOrderProduct", targetProduct) > 0) {
					// 재배송(가반품으로 종료), 교환반품(입고완료일때 수행), 취소(가상계좌 입금대기 말고는 수행)
					orderService.updateStock(targetProduct, bClaimQty);
				}
				
				/** 포인트 적립취소금액 - 반품일때만.(귀책사유 있는가??) */
				String productType = targetProduct.getOrderProductTypeCd();
				// if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(productType) || "ORDER_PRODUCT_TYPE_CD.SET".equals(productType)) {
				if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(productType) || "ORDER_PRODUCT_TYPE_CD.SUB".equals(productType)) {
					if (!StringUtils.isEmpty(targetProduct.getSaveDt())) {
						bCancelSavePoint = bCancelSavePoint.add(targetProduct.getTotalPoint().multiply(bClaimQty));
						// if (targetProduct.isCancelAll() && targetProduct.getCalibratePoint() != null) { // targetProduct.getTotalPoint()에 포함됨
						// bCancelSavePoint.add(targetProduct.getCalibratePoint());
						// }
					}
				}
			}
		}

		/***************************/
		/******* A. 반품로직 *********/
		/***************************/
		// 취소,교환,반품,재배송

		// 반품의 추가배송비, 반품배송비, 환불배송비
		OmsClaimdelivery omsClaimdelivery = omsClaimWrapper.getOmsClaimdelivery();
		if ("BUYER".equals(penaltyType)) {
			omsClaimdelivery.setReturnDeliveryFee(omsClaimdelivery.getDeliveryFee());
		}

		/** 반품.1. UDPATE oms_claim */
		dao.update("oms.claim.update.claim", omsClaim);
		for (OmsClaimproduct cProduct : omsClaimproducts) {
			cProduct.setUpdDt(currentDate);
			cProduct.setUpdId(updId);
			// cProduct.setClaimNo(omsClaim.getClaimNo());

			/** 반품.2. UPDATE oms_claimproduct */
			dao.update("oms.claim.update.claimProduct", cProduct);
			
			OmsLogistics omsLogistics = new OmsLogistics();
			omsLogistics.setOrderId(orderId);
			omsLogistics.setOrderProductNo(cProduct.getOrderProductNo());
			omsLogistics.setClaimNo(claimNo);
			omsLogistics.setInReserveQty(cProduct.getClaimQty().toString());
			omsLogistics.setUpdDt(currentDate);
			omsLogistics.setUpdId(updId);
			
			/** 반품.7. UPDATE oms_logistics */
			dao.update("oms.claim.update.logistics", omsLogistics);
		}
		
		// 배송비, 포장비, 배송쿠폰, 포장쿠폰
		omsClaimdelivery.setSelectKey("return");
		/** 반품.9. oms_ordercoupon */
//		bTotalRefundAmt = this.updateCouponCancel(omsClaim, omsClaimdelivery, claimCoupons, bOrderCouponTargetAmt, bTotalRefundAmt, existClaimDeliveryFee);
		BigDecimal bCouponRefundAmt = this.updateCouponCancel(omsClaim, omsClaimdelivery, claimCoupons, bOrderCouponTargetAmt, existClaimDeliveryFee);
		bTotalRefundAmt = bTotalRefundAmt.add(bCouponRefundAmt);
		
		if (bCancelSavePoint.compareTo(BigDecimal.ZERO) > 0) {
			OmsPayment omsPayment = new OmsPayment();
			omsPayment.setOrderId(orderId);
			omsPayment.setClaimNo(claimNo);
			omsPayment.setMemberNo(memberNo);
			omsPayment.setPaymentAmt(bCancelSavePoint);

			ccsmessage = this.insertPoint(omsPayment, "savePoint");
			if (!ccsmessage.isSuccess()) {
				throw new ServiceException("oms.common.error", new Object[] { ccsmessage.getResultMessage() });
			}
		}
		
		/**
		 * 반품.10. oms_payment
		 * <pre>
		 * 취소 : oms_payment && PG 환불(취소) 프로세스 바로 진행.
		 * 반품 : oms_payment만 진행 && PG 환불은 입고완료 후 진행. 
		 * 교환 : 환불 프로세스 없음. 추가결제 금액 있으면 진행. 
		 * 재배송 : 환불 프로세스 없음.
		 * </pre>
		 */
		List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();
		if (!CommonUtil.isEmpty(omsPayments)) {
			String smsCode = "125"; // 신용카드, 카카오페이 승인취소 sms
			boolean sendSms = true;
			for (OmsPayment omsPayment : omsPayments) {
				omsPayment.setUpdDt(currentDate);
				omsPayment.setUpdId(updId);
				omsPayment.setOrderId(orderId);
				omsPayment.setClaimNo(claimNo);
				omsPayment.setPaymentDt(currentDate);// 환불완료일시,결제일시
				omsPayment.setMemberNo(memberNo);
				omsPayment.setRefundReasonCd("REFUND_REASON_CD.RETURN");
				if ("PAYMENT_METHOD_CD.CASH".equals(omsPayment.getPaymentMethodCd())) {
					sendSms = false;
					omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND_READY"); // 환불대기
				} else {
					omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND"); // 환불완료
				}
				if (omsPayment.getRefundAmt() != null && omsPayment.getRefundAmt().compareTo(BigDecimal.ZERO) > 0) {
					ccsmessage = this.insertPaymentCancelPart(omsPayment);

					if (ccsmessage.isSuccess()) {
						dao.updateOneTable(omsPayment);
						// dao.update("oms.payment.refundPayment", omsPayment);

						if ("PAYMENT_STATE_CD.REFUND".equals(omsPayment.getPaymentStateCd())) {
							if ("PAYMENT_METHOD_CD.VIRTUAL".equals(omsPayment.getPaymentMethodCd())) {
								smsCode = "134";
							} else if ("PAYMENT_METHOD_CD.TRANSFER".equals(omsPayment.getPaymentMethodCd())) {
								smsCode = "134";
							} else if ("PAYMENT_METHOD_CD.MOBILE".equals(omsPayment.getPaymentMethodCd())) {
								smsCode = "126";
							}
							// else {smsCode = "125"}
						}
					}
				}
			}
			// 반품완료 sms
			if (sendSms) {
				claimTmsService.createClaimTms(omsClaimWrapper, smsCode);
			}
		}

		return ccsmessage;
	}

	// public CcsMessage updateClaimComplete(OmsClaimWrapper omsClaimWrapper) throws Exception {
	// CcsMessage ccsmessage = new CcsMessage();
	// ccsmessage.setSuccess(true);
	// ccsmessage.setResultCode("0000");
	// ccsmessage.setResultMessage("클레임 성공");
	//
	// String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
	// String userId = SessionUtil.getLoginId();
	//
	// try {
	//
	// List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();
	// for (OmsPayment omsPayment : omsPayments) {
	// omsPayment.setUpdDt(currentDate);
	// omsPayment.setUpdId(userId);
	// omsPayment.setCancelDt(null);
	// // omsPayment.setOrderId(orderId);
	// // omsPayment.setClaimNo(null);
	// omsPayment.setPaymentDt(currentDate);// 환불완료일시,결제일시
	// omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND"); // 환불완료
	//
	// ccsmessage = this.insertPaymentCancelPart(omsPayment);
	//
	// if (ccsmessage.isSuccess()) {
	// try {
	// if (dao.update("oms.payment.updatePayment", omsPayment) < 1) {
	// throw new ServiceException("oms.common.error", new Object[] { "결제 취소데이터 등록 중 에러 발생" });
	// }
	// } catch (Exception e) {
	// throw new ServiceException("oms.common.error", new Object[] { e.getLocalizedMessage() });
	// }
	// }
	// }
	// } catch (Exception e) {
	// ccsmessage.setResultMessage("클레임 완료 처리 중 에러가 발생하였습니다. ");
	// throw new ServiceException("oms.common.error", new Object[] { ccsmessage.getResultMessage() });
	// }
	// return ccsmessage;
	// }

	private BigDecimal insertDBClaim(OmsClaim omsClaim) {
		try {
			dao.insert("oms.claim.update.cancel", omsClaim);
			return omsClaim.getClaimNo();
		} catch (Exception e) {
			throw new ServiceException("oms.common.error", new Object[] { "클레임 데이터 등록 중 에러가 발생하였습니다." });
		}
	}

	private void insertDBClaimproduct(OmsClaim omsClaim, List<OmsClaimproduct> omsClaimproducts, String claimProductStateCd) {
		String currentDate = omsClaim.getUpdDt();
		try {
			for (OmsClaimproduct cp : omsClaimproducts) {
				cp.setUpdDt(currentDate);
				cp.setUpdId(omsClaim.getUpdId());
				cp.setClaimNo(omsClaim.getClaimNo());
				if ("CLAIM_PRODUCT_STATE_CD.RETURN".equals(claimProductStateCd)) {
					cp.setReturnDt(currentDate);
				} else if ("CLAIM_PRODUCT_STATE_CD.RETURN_ORDER".equals(claimProductStateCd)) {
					cp.setReturnOrderDt(currentDate);
				}
				cp.setClaimProductStateCd(claimProductStateCd);
				dao.update("oms.claim.update.returnProduct1", cp);
			}
		} catch (Exception e) {
			throw new ServiceException("oms.common.error", new Object[] { "클레임 상품 데이터 등록 중 에러가 발생하였습니다." });
		}
	}

	private List<OmsOrdercoupon> getClaimCoupon(List<OmsOrderproduct> targetProducts, List<OmsClaimproduct> omsClaimproducts) {
		List<OmsOrdercoupon> claimCoupons = new ArrayList<OmsOrdercoupon>();
		for (OmsOrderproduct targetProduct : targetProducts) {
			if ("ORDER_PRODUCT_STATE_CD.CANCEL".equals(targetProduct.getOrderProductStateCd())) {
				targetProduct.setCancelAll(true);
			} else {
				targetProduct.setCancelAll(false);
			}
			targetProduct.setOmsClaimproduct(null);

			for (OmsClaimproduct claimProduct : omsClaimproducts) {
				if (targetProduct.getOrderProductNo().compareTo(claimProduct.getOrderProductNo()) == 0) {
					BigDecimal bClaimQty = claimProduct.getClaimQty();
					if (targetProduct.getAvailableClaimQty().compareTo(bClaimQty) == 0) {
						targetProduct.setCancelAll(true);
						claimProduct.setCancelAll(true);
					}
					targetProduct.setOmsClaimproduct(claimProduct); // TODO - 주문사은품은 안담길 수 있으므로 주요체크
					
					String productType = targetProduct.getOrderProductTypeCd().replaceFirst("ORDER_PRODUCT_TYPE_CD.", "");

					// 상품,플러스 쿠폰 세팅
//					if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(targetProduct.getOrderProductTypeCd())
//							|| "ORDER_PRODUCT_TYPE_CD.SET".equals(targetProduct.getOrderProductTypeCd())) {
					if ("GENERAL".equals(productType) || "SET".equals(productType)) {

						if (!StringUtils.isEmpty(targetProduct.getProductCouponId())) {
							OmsOrdercoupon coupon = new OmsOrdercoupon();
							coupon.setCouponId(targetProduct.getProductCouponId());
							coupon.setCouponIssueNo(targetProduct.getProductCouponIssueNo());
							coupon.setStoreId(targetProduct.getStoreId());
							coupon.setOrderId(targetProduct.getOrderId());
							coupon.setCouponDcCancelAmt(targetProduct.getProductCouponDcAmt().multiply(bClaimQty));
							coupon.setCouponTypeCd("COUPON_TYPE_CD.PRODUCT");
							if (targetProduct.isCancelAll()) {
								if ("Y".equals(targetProduct.getProductSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(targetProduct.getProductCouponDcAmt());
								}
								coupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");
							} else {
								if ("Y".equals(targetProduct.getProductSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(BigDecimal.ZERO);
								}
							}
							claimCoupons.add(coupon);
						}
						if (!StringUtils.isEmpty(targetProduct.getPlusCouponId())) {
							OmsOrdercoupon coupon = new OmsOrdercoupon();
							coupon.setCouponId(targetProduct.getPlusCouponId());
							coupon.setCouponIssueNo(targetProduct.getPlusCouponIssueNo());
							coupon.setStoreId(targetProduct.getStoreId());
							coupon.setOrderId(targetProduct.getOrderId());
							coupon.setCouponDcCancelAmt(targetProduct.getPlusCouponDcAmt().multiply(bClaimQty));
							coupon.setCouponTypeCd("COUPON_TYPE_CD.PLUS");
							if (targetProduct.isCancelAll()) {
								if ("Y".equals(targetProduct.getPlusSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(targetProduct.getProductCouponDcAmt());
								}
								coupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");
							} else {
								if ("Y".equals(targetProduct.getPlusSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(BigDecimal.ZERO);
								}
							}
							claimCoupons.add(coupon);
						}
						// check 정액(/수량), 정율(허들이하일때 일괄)
						if (!StringUtils.isEmpty(targetProduct.getOrderCouponId())) {
							OmsOrdercoupon coupon = new OmsOrdercoupon();
							coupon.setCouponId(targetProduct.getOrderCouponId());
							coupon.setCouponIssueNo(targetProduct.getOrderCouponIssueNo());
							coupon.setStoreId(targetProduct.getStoreId());
							coupon.setOrderId(targetProduct.getOrderId());
							coupon.setCouponDcCancelAmt(targetProduct.getOrderCouponDcAmt());
							coupon.setCouponTypeCd("COUPON_TYPE_CD.ORDER");

							claimCoupons.add(coupon);
						}
					}
					
					// 포장비 무료쿠폰 세팅
					if ("WRAP".equals(productType)) {
						if (!StringUtils.isEmpty(targetProduct.getProductCouponId())) {
							OmsOrdercoupon coupon = new OmsOrdercoupon();
							coupon.setCouponId(targetProduct.getProductCouponId());
							coupon.setCouponIssueNo(targetProduct.getProductCouponIssueNo());
							coupon.setStoreId(targetProduct.getStoreId());
							coupon.setOrderId(targetProduct.getOrderId());
							coupon.setCouponDcCancelAmt(targetProduct.getPlusCouponDcAmt().multiply(bClaimQty));
							coupon.setCouponTypeCd("COUPON_TYPE_CD.WRAP");
							if (targetProduct.isCancelAll()) {
								if ("Y".equals(targetProduct.getProductSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(targetProduct.getProductCouponDcAmt());
								}
								coupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");// 포장비 쿠폰은 1장(상품수량1개)만 사용가능하고, 취소하는 포장지에 쿠폰이 적용되어 있으면 같이 취소된다.
							} else {
								if ("Y".equals(targetProduct.getProductSingleApplyYn())) {
									coupon.setCouponDcCancelAmt(BigDecimal.ZERO);
								}
							}
							claimCoupons.add(coupon);
						}
					}
				}
			}
		}
		return claimCoupons;
	}
	
	@SuppressWarnings("unchecked")
	private BigDecimal updateCouponCancel(OmsClaim omsClaim, OmsClaimdelivery omsClaimdelivery, List<OmsOrdercoupon> claimCoupons,
			BigDecimal bOrderCouponTargetAmt, boolean existClaimDeliveryFee) {
		
		// 상품의 클레임 수량을 증가 시킨 후 계산한다.
		OmsDelivery policy = (OmsDelivery) dao.selectOne("oms.claim.select.delivery", omsClaimdelivery);
		boolean isRecoveryWrapCoupon = false;
		boolean isRecoveryOrderCoupon = false;		
		boolean isRecoveryDeliveryCoupon = false;
		boolean isChangeOrderCoupon = true;
		boolean updateClaimdelivery = false;
		
		BigDecimal bCouponRefundAmt = BigDecimal.ZERO;
		BigDecimal bRefundOrderCouponAmt = BigDecimal.ZERO;
		
		// 환불배송비, 배송비쿠폰
		{
			// ==> 배송비 (O) -> (O) : 액션없음.
			// ==> 배송비 (O) -> (X) : N/A
			// ==> 배송비 (X) -> (O) : 배송비 무료 상품 다 취소, 취소로 인한 배송 무료기준 해제
			// ==> 배송비 (X) -> (X) : 액션없음.
			BigDecimal bRefundDeliveryFee = BigDecimal.ZERO; // 포장비 환불

			if (!StringUtils.isEmpty(policy.getDeliveryCouponId())) {
				if (policy.getProductSumAmt().compareTo(BigDecimal.ZERO) > 0) {
					// 여전히 배송비 쿠폰이 적용됨. --> 쿠폰적용가 변동없음.배송비는 여전히 무료
				} else {
					// 배송비 쿠폰 적용 취소(배송정책의 전체상품취소)
					// refundDeliveryFee = policy.deliveryCouponDcAmt;
					bRefundDeliveryFee = policy.getOrderDeliveryFee();
					// ==> 사용쿠폰 복원(배송)
					isRecoveryDeliveryCoupon = true;
				}
			} else {
				if ("N".equals(policy.getDeliveryFeeFreeYn())) {
					if (policy.getProductSumAmt().compareTo(BigDecimal.ZERO) > 0) {
						if (policy.getProductSumAmt().compareTo(policy.getMinDeliveryFreeAmt()) < 0) {// 배송비 부과케이스
							if (policy.getApplyDeliveryFee().compareTo(BigDecimal.ZERO) > 0) {
							} else {
								// 무료 ==> 유료
								if ("RETURN_PICKUP_TYPE_CD.BUYER".equals(omsClaim.getReturnPickupTypeCd()) && !existClaimDeliveryFee) {
									omsClaimdelivery.setOrderDeliveryFee(policy.getDeliveryFee());// 부분 클레임에 따른 추가배송비 부과
									updateClaimdelivery = true;
								}
							}
						}
					} else {
						// 부과된 배송료 환불세팅
						if ("CLAIM_TYPE_CD.CANCEL".equals(omsClaim.getClaimTypeCd())) {
							// refundDeliveryFee = policy.applyDeliveryFee;
							bRefundDeliveryFee = policy.getApplyDeliveryFee();
						} else {
							/* http://192.2.72.118/redmine/issues/2924
							if (policy.getApplyDeliveryFee().compareTo(BigDecimal.ZERO) > 0) {
							} else {
								// 무료 ==> 유료
								if ("RETURN_PICKUP_TYPE_CD.BUYER".equals(omsClaim.getReturnPickupTypeCd()) && !existClaimDeliveryFee) {
									omsClaimdelivery.setOrderDeliveryFee(policy.getDeliveryFee());// 부분 클레임에 따른 추가배송비 부과
									updateClaimdelivery = true;
								}
							}
							*/
							if ("RETURN_PICKUP_TYPE_CD.BUYER".equals(omsClaim.getReturnPickupTypeCd()) && !existClaimDeliveryFee) {
								omsClaimdelivery.setOrderDeliveryFee(policy.getDeliveryFee());// 부분 클레임에 따른 추가배송비 부과
								updateClaimdelivery = true;
							}
						}
					}
				}
			}
			omsClaimdelivery.setRefundDeliveryFee(bRefundDeliveryFee);
			if (bRefundDeliveryFee.compareTo(BigDecimal.ZERO) > 0) {
				updateClaimdelivery = true;
			}
		}

		// 환불포장비, 포장쿠폰
//		{
//			// ==> 포장비 (O) -> (O) : 일부 수량 취소될 시
//			// ==> 배송비 (O) -> (X) : 일부(전체) 수량 취소될 시
//			// ==> 배송비 (X) -> (O) : 해당사항없음.
//			// ==> 배송비 (X) -> (X) : 액션없음.
//			BigDecimal bBeforeWrapFee = policy.getOrderWrapFee().subtract(policy.getSumRefundWrapFee());
//			BigDecimal bAfterWrapFee = policy.getWrapVolume().multiply(BaseConstants.WRAPPING_AMT);
//			BigDecimal bRefundWrapFee = BigDecimal.ZERO; // 포장비 환불
//
//			if (policy.getWrapVolume().compareTo(BigDecimal.ZERO) > 0) {
//				BigDecimal bClaimWrapFee = bBeforeWrapFee.subtract(bAfterWrapFee);
//				bRefundWrapFee = bClaimWrapFee;
//				if (!StringUtils.isEmpty(policy.getWrapCouponId())) {
//					if (bAfterWrapFee.compareTo(policy.getWrapCouponDcAmt()) < 0) {
//						isRecoveryWrapCoupon = true;
//					}
//				}
//			} else {
//				// 전체(혹은 포장상품) 취소되어 포장비 없어짐.
//				bRefundWrapFee = bBeforeWrapFee;
//				isRecoveryWrapCoupon = true;
//			}
//			omsClaimdelivery.setRefundWrapFee(bRefundWrapFee);
//			if (bRefundWrapFee.compareTo(BigDecimal.ZERO) > 0) {
//				updateClaimdelivery = true;
//			}
//		}

		// 주문(장바구니) 쿠폰
		{
			OmsOrderSearch search = new OmsOrderSearch();
			search.setOrderId(omsClaim.getOrderId());
			List<OmsOrdercoupon> couponList = (List<OmsOrdercoupon>) dao.selectList("oms.order.select.couponList", search);

			// TODO - 복수배송 고려.
			for (OmsOrdercoupon coupon : couponList) {
				if ("COUPON_TYPE_CD.ORDER".equals(coupon.getCouponTypeCd())) {
					// 사용가능
					if (bOrderCouponTargetAmt.compareTo(coupon.getMinOrderAmt()) >= 0) {
						BigDecimal bRemainOrderCouponDcAmt = BigDecimal.ZERO;
						if ("DC_APPLY_TYPE_CD.AMT".equals(coupon.getDcApplyTypeCd())) {
							isChangeOrderCoupon = false;
							bRemainOrderCouponDcAmt = coupon.getCouponDcAmt();
						} else {
							// 부분 or 전체 취소
							bRemainOrderCouponDcAmt = bOrderCouponTargetAmt.multiply(coupon.getDcValue()).multiply(BigDecimal.valueOf(0.001));
							bRemainOrderCouponDcAmt = bRemainOrderCouponDcAmt.setScale(0, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10));
						}
						bRefundOrderCouponAmt = coupon.getCouponDcAmt().subtract(coupon.getCouponDcCancelAmt()).subtract(bRemainOrderCouponDcAmt);
						if (bRefundOrderCouponAmt.compareTo(BigDecimal.ZERO) != 1) {
							isRecoveryOrderCoupon = true;
						}
					}
					// 사용불가능 => 쿠폰취소
					else {
						bRefundOrderCouponAmt = coupon.getCouponDcAmt().subtract(coupon.getCouponDcCancelAmt());
						isRecoveryOrderCoupon = true;
					}
				}

				// 배송비 , 포장비 쿠폰 세팅
				if (coupon.getCouponId().equals(policy.getDeliveryCouponId())) {
					if (coupon.getCouponIssueNo().compareTo(policy.getDeliveryCouponIssueNo()) == 0) {
						if (isRecoveryDeliveryCoupon) {
							coupon.setCouponDcCancelAmt(policy.getDeliveryCouponDcAmt());
							coupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");
							claimCoupons.add(coupon);
						}
					}
				} else if (coupon.getCouponId().equals(policy.getWrapCouponId())) {
					if (coupon.getCouponIssueNo().compareTo(policy.getWrapCouponIssueNo()) == 0) {
						// ==> 사용쿠폰 복원(포장)
						if (isRecoveryWrapCoupon) {
							coupon.setCouponDcCancelAmt(policy.getWrapCouponDcAmt());
							coupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");
							claimCoupons.add(coupon);
						}
					}
				}
			}
		}

		// 반품은 마지막에 수행함...........
		
		BigDecimal bProductCouponCancel = BigDecimal.ZERO;
		BigDecimal bPlusCouponCancel = BigDecimal.ZERO;
		BigDecimal bOrderCouponCancel = BigDecimal.ZERO;
		BigDecimal bDeliveryCouponCancel = BigDecimal.ZERO;
		BigDecimal bWrapCouponCancel = BigDecimal.ZERO;
		boolean isProductCouponCancel = false;
		boolean isDeliveryCouponCancel = false;
		for (OmsOrdercoupon claimCoupon : claimCoupons) {
			claimCoupon.setUpdDt(omsClaim.getUpdDt());
			claimCoupon.setUpdId(omsClaim.getUpdId());

			String cpntype = claimCoupon.getCouponTypeCd().replaceFirst("COUPON_TYPE_CD.", "");
			BigDecimal bCancelAmt = claimCoupon.getCouponDcCancelAmt();
			switch (cpntype) {
				case "PRODUCT":
					bProductCouponCancel = bProductCouponCancel.add(bCancelAmt);
					isProductCouponCancel = true;
					break;
				case "WRAP":
					// 포장비쿠폰 취소금액 세팅
					// bWrapCouponCancel = bWrapCouponCancel.add(bCancelAmt);
					// isDeliveryCouponCancel = true;
					bProductCouponCancel = bProductCouponCancel.add(bCancelAmt); // 포장쿠폰은 상품쿠폰과 동일
					isProductCouponCancel = true;
					break;
				case "PLUS":
					bPlusCouponCancel = bPlusCouponCancel.add(bCancelAmt);
					isProductCouponCancel = true;
					break;
				case "ORDER":
					if (!isChangeOrderCoupon) {
						continue; // 취소조건이 안되는 경우는 패스
					}
					if (isRecoveryOrderCoupon) {
						claimCoupon.setCouponStateCd("COUPON_STATE_CD.CANCEL");
					}
					// 장바구니(주문)쿠폰 취소금액 세팅
					claimCoupon.setCouponDcCancelAmt(bRefundOrderCouponAmt);
					bOrderCouponCancel = bOrderCouponCancel.add(bCancelAmt);
					isProductCouponCancel = true;
					break;
				case "DELIVERY":
					// 배송쿠폰 취소금액 세팅
					bDeliveryCouponCancel = bDeliveryCouponCancel.add(bCancelAmt);
					isDeliveryCouponCancel = true;
					break;
			}
			bCouponRefundAmt = bCouponRefundAmt.subtract(bCancelAmt); // 쿠폰할인값 제거

			// 주문쿠폰취소값
			dao.update("oms.claim.update.coupon", claimCoupon);

			// 회원쿠폰복원
			if ("COUPON_STATE_CD.CANCEL".equals(claimCoupon.getCouponStateCd())) {
				dao.update("oms.claim.update.spsCoupon", claimCoupon);
			}
		}
		
		// 클레임 발생시 취소되는 쿠폰금액
		if (isProductCouponCancel) {
			omsClaim.setProductCouponDcCancelAmt(bProductCouponCancel);
			omsClaim.setPlusCouponDcCancelAmt(bPlusCouponCancel);
			omsClaim.setOrderCouponDcCancelAmt(bOrderCouponCancel);
			dao.update("oms.claim.update.productCouponCancel", omsClaim);
		}
		if (isDeliveryCouponCancel || updateClaimdelivery) {
			omsClaimdelivery.setDeliveryCouponDcCancelAmt(bDeliveryCouponCancel);
			omsClaimdelivery.setWrapCouponDcCancelAmt(bWrapCouponCancel);
			dao.update("oms.claim.update.deliveryCouponCancel", omsClaimdelivery);
		}
		
		/** 반품.9. oms_ordercoupon */
		// 배송비, 포장비 추가
		bCouponRefundAmt = bCouponRefundAmt.add(omsClaimdelivery.getRefundDeliveryFee()).add(omsClaimdelivery.getRefundWrapFee());
		// TODO - 원배송비가 다시 발생한 경우 refund 에서 빼야하나??
		bCouponRefundAmt = bCouponRefundAmt.subtract(omsClaimdelivery.getOrderDeliveryFee()).subtract(omsClaimdelivery.getReturnDeliveryFee());
		
		return bCouponRefundAmt;
	}
	

	/**
	 * 전체주문 취소일 경우만 excute
	 * 
	 * @Method Name : updateCancelAll
	 * @author : victor
	 * @date : 2016. 10. 12.
	 * @description :
	 *
	 * @param omsClaim
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private CcsMessage updateCancelAll(OmsClaim omsClaim, BigDecimal memberNo) throws Exception {

		// ===== A. 전체 취소면 전체테이블 취소 갱신 && 클레임 추가 && pg 취소
		// ========== INSERT oms_claim
		dao.insert("oms.claim.update.cancel", omsClaim);
		// ========== INSERT oms_claimproduct
		dao.update("oms.claim.update.cancelProduct1", omsClaim);
		if (CommonUtil.isNotEmpty(omsClaim.getOmsClaimproducts())) {
			List<OmsClaimproduct> claimProducts = omsClaim.getOmsClaimproducts();
			for (OmsClaimproduct cp : claimProducts) {
				cp.setClaimNo(omsClaim.getClaimNo());
				dao.update("oms.claim.update.cancelProduct2", cp);
			}
		}
		// ========== INSERT oms_claimdelivery (포장비, 배송비)
		dao.update("oms.claim.update.cancelDelivery", omsClaim);

		// ========== UPDATE oms_order
		dao.update("oms.claim.update.order", omsClaim);
		// ========== UPDATE oms_orderproduct
		dao.update("oms.claim.update.orderProduct", omsClaim);

		// ========== UPDATE sps_ordercoupon 사용쿠폰 복원
		{
			OmsOrderSearch search = new OmsOrderSearch();
			search.setOrderId(omsClaim.getOrderId());
			search.setCouponStateCd("COUPON_STATE_CD.APPLY");
			List<OmsOrdercoupon> couponList = (List<OmsOrdercoupon>) dao.selectList("oms.order.select.couponList", search);
			BigDecimal bProductCouponCancel = BigDecimal.ZERO;
			BigDecimal bPlusCouponCancel = BigDecimal.ZERO;
			BigDecimal bOrderCouponCancel = BigDecimal.ZERO;
			if (couponList != null && couponList.size() > 0) {
				for (OmsOrdercoupon ordercoupon : couponList) {
					dao.update("oms.claim.update.spsCoupon", ordercoupon);
					BigDecimal bRemainCouponAmt = ordercoupon.getCouponDcAmt().subtract(ordercoupon.getCouponDcCancelAmt());
					if ("COUPON_TYPE_CD.PRODUCT".equals(ordercoupon.getCouponTypeCd())) {
						bProductCouponCancel = bProductCouponCancel.add(bRemainCouponAmt);
					} else if ("COUPON_TYPE_CD.PLUS".equals(ordercoupon.getCouponTypeCd())) {
						bPlusCouponCancel = bPlusCouponCancel.add(bRemainCouponAmt);
					} else if ("COUPON_TYPE_CD.ORDER".equals(ordercoupon.getCouponTypeCd())) {
						bOrderCouponCancel = bOrderCouponCancel.add(bRemainCouponAmt);
						// } else if ("COUPON_TYPE_CD.DELIVERY".equals(ordercoupon.getCouponTypeCd())) {
						// omsClaimdelivery.setPlusCouponDcCancelAmt(plusCouponDcCancelAmt);
						// } else if ("COUPON_TYPE_CD.WRAP".equals(ordercoupon.getCouponTypeCd())) {
						// omsClaimdelivery.setPlusCouponDcCancelAmt(plusCouponDcCancelAmt);
					}
				}
				omsClaim.setProductCouponDcCancelAmt(bProductCouponCancel);
				omsClaim.setPlusCouponDcCancelAmt(bPlusCouponCancel);
				omsClaim.setOrderCouponDcCancelAmt(bOrderCouponCancel);
				dao.update("oms.claim.update.productCouponCancel", omsClaim);
			}
		}
		// ========== UPDATE oms_ordercoupon 쿠폰할인 취소금액
		dao.update("oms.claim.update.orderCoupon", omsClaim);

		// ========== INSERT mms_deposit 주문에 사용한 예치금 복원
		// 사용예치금
		// TODO - 사용포인트 (적립포인트는 나중에...)

		// ========== INSERT oms_payment (SELECT)
		return this.insertPaymentCancelAll(omsClaim, memberNo);
	}

	@SuppressWarnings("unchecked")
	private CcsMessage insertPaymentCancelAll(OmsClaim omsClaim, BigDecimal memberNo) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();

		OmsPaymentSearch omsPaymentSearch = new OmsPaymentSearch();
		omsPaymentSearch.setOrderId(omsClaim.getOrderId());
		List<OmsPayment> omsPayments = (List<OmsPayment>) dao.selectList("oms.payment.claimList", omsPaymentSearch);

		for (OmsPayment omsPayment : omsPayments) {
			if (omsPayment.getPaymentAmt().compareTo(BigDecimal.ZERO) > 0) {
				omsPayment.setMemberNo(memberNo);
				omsPayment.setClaimNo(omsClaim.getClaimNo());
				
				omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.REFUND");
				omsPayment.setRefundReasonCd("REFUND_REASON_CD.CANCEL");
				omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND");
				omsPayment.setPaymentDt(omsClaim.getUpdDt());// 환불완료일시,결제일시
				omsPayment.setInsDt(omsClaim.getUpdDt());
				omsPayment.setInsId(omsClaim.getUpdId());
				omsPayment.setEscrowYn("N");
				omsPayment.setEscrowIfYn("N");
				omsPayment.setInterestFreeYn("N");
				omsPayment.setPartialCancelYn("N");
				omsPayment.setPaymentFee(BigDecimal.ZERO);

				boolean doLGU = this.createPaymethod(omsPayment, ccsmessage, "0");
				
				if (doLGU) {
					ccsmessage = this.cancelAll(omsPayment);
					if (ccsmessage.isSuccess()) {
						omsPayment.setPgShopId(ccsmessage.getId());
						omsPayment.setPgCancelNo(ccsmessage.getNo());
					} else {
						throw new ServiceException("oms.pg.error.allcancel", new Object[] { ccsmessage.getResultMessage() });
					}
				}

				if (ccsmessage.isSuccess()) {
					omsPayment.setPaymentNo(paymentService.getNewPaymentNo());	// 취소는 new payment_no 로
					dao.insertOneTable(omsPayment);
				} else {
					throw new ServiceException("oms.pg.error.allcancel", new Object[] { ccsmessage.getResultMessage() });
				}
			}
		}

		if (CommonUtil.isEmpty(ccsmessage.getResultCode())) {
			ccsmessage.setSuccess(true);
			ccsmessage.setResultCode("0000");
			ccsmessage.setResultMessage("주문취소를 완료하였습니다.");
		}

		return ccsmessage;
	}
	
	/**
	 * 결제연동
	 * @Method Name : insertPaymentCancelPart
	 * @author : victor
	 * @date : 2016. 11. 5.
	 * @description : 
	 * <pre>
	 *	LGU PG 연동
	 *	KAKAO PG 연동
	 *	포인트 PG 연동
	 *	예치금 PG 연동
	 * </pre>
	 * @param omsPayment
	 * @return
	 * @throws Exception
	 */
	private CcsMessage insertPaymentCancelPart(OmsPayment omsPayment) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);

		if (!"PAYMENT_STATE_CD.REFUND".equals(omsPayment.getPaymentStateCd())) {
			omsPayment.setPaymentAmt(BigDecimal.ZERO);
		} else {
			omsPayment.setPaymentAmt(omsPayment.getRefundAmt());
		}
		omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.REFUND");// 결제유형 - 환불

		boolean doLGU = this.createPaymethod(omsPayment, ccsmessage, "1");
		if (doLGU && "PAYMENT_STATE_CD.REFUND".equals(omsPayment.getPaymentStateCd())) {
			ccsmessage = this.cancelPartial(omsPayment);
			if (ccsmessage.isSuccess()) {
				omsPayment.setPgShopId(ccsmessage.getId());
				omsPayment.setPgCancelNo(ccsmessage.getNo());// PG취소 승인번호
			} else {
				throw new ServiceException("oms.pg.error.partialcancel", new Object[] { ccsmessage.getResultMessage() });
			}
		}
		return ccsmessage;
	}
	
	private boolean createPaymethod(OmsPayment omsPayment, CcsMessage ccsmessage, String cancelType) throws Exception {
		String method = omsPayment.getPaymentMethodCd();
		String status = omsPayment.getPaymentStateCd();

		boolean doLGU = false;
		switch (method) {
			case "PAYMENT_METHOD_CD.CARD":
				doLGU = true;
				break;
			case "PAYMENT_METHOD_CD.VIRTUAL":
				if ("PAYMENT_STATE_CD.PAYMENT".equals(status)) {
					doLGU = true;
				} else {
					omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.CANCEL");
					ccsmessage.setSuccess(true);
				}
				break;
			case "PAYMENT_METHOD_CD.TRANSFER":
				doLGU = true;
				break;
			case "PAYMENT_METHOD_CD.MOBILE":
				doLGU = true;
				break;
			case "PAYMENT_METHOD_CD.KAKAO":
				if ("PAYMENT_STATE_CD.REFUND".equals(status)) {
					ccsmessage = this.cancelKakao(omsPayment, cancelType, omsPayment.getClaimNo().toString());
					if (!ccsmessage.isSuccess()) {
						throw new ServiceException("oms.pg.error", new Object[] { ccsmessage.getResultMessage() });
					}
				}
				break;
			case "PAYMENT_METHOD_CD.POINT":
				if ("PAYMENT_STATE_CD.REFUND".equals(status)) {
					this.insertPoint(omsPayment, "usePoint");
				}
				break;
			case "PAYMENT_METHOD_CD.DEPOSIT":
				if ("PAYMENT_STATE_CD.REFUND".equals(status)) {
					this.insertDeposit(omsPayment, omsPayment.getMemberNo());
				}
				break;
		}
		return doLGU;
	}

	/**
	 * 예치금 연동
	 * @Method Name : insertDeposit
	 * @author : victor
	 * @date : 2016. 11. 5.
	 * @description : 
	 *	예치금 증감 연동
	 * @param omsPayment
	 * @param memberNo
	 * @throws Exception
	 */
	private void insertDeposit(OmsPayment omsPayment, BigDecimal memberNo) throws Exception {
		MmsDeposit mmsDeposit = new MmsDeposit();
		mmsDeposit.setMemberNo(memberNo);
		mmsDeposit.setDepositAmt(omsPayment.getPaymentAmt());
		mmsDeposit.setOrderId(omsPayment.getOrderId());
		mmsDeposit.setClaimNo(omsPayment.getClaimNo());
		mmsDeposit.setDepositTypeCd("DEPOSIT_TYPE_CD.CLAIMREFUND");
		mmsDeposit.setInsId(omsPayment.getUpdId());
		mmsDeposit.setInsDt(omsPayment.getUpdDt());

		Map<String, String> result = memberService.saveMemberDeposit(mmsDeposit);
		if (BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get("resultCode"))) {
			omsPayment.setDepositNo(mmsDeposit.getDepositNo());// 예치금번호
		}
	}
	
	/**
	 * 포인트 연동
	 * @Method Name : insertPoint
	 * @author : victor
	 * @date : 2016. 11. 5.
	 * @description : 
	 * <pre>
	 *	사용 포인트 증감
	 *	적립 포인트 차감
	 * </pre>
	 * @param omsPayment
	 * @param pointIfType
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private CcsMessage insertPoint(OmsPayment omsPayment, String pointIfType) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);

		OmsPointif pointif = new OmsPointif();
		pointif.setUniqRcgnNo(omsPayment.getOrderId());
		pointif.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
		pointif.setMmbCertDvVlu(omsPayment.getMemberNo().toString());// 회원번호(필수)
		pointif.setPintUseTypCd("11");
		if ("usePoint".equals(pointIfType)) {
			pointif.setTrscTypCd("300"); // 거래유형 - 200 적립, 300 사용
			pointif.setTrscBizDvCd("31"); // 업무구분 - 21 포인트 적립, 31 포인트사용
		} else if ("savePoint".equals(pointIfType)) {
			pointif.setTrscTypCd("200"); // 거래유형 - 200 적립, 300 사용
			pointif.setTrscBizDvCd("21"); // 업무구분 - 21 포인트 적립, 31 포인트사용
		}

		List<OmsPointif> pointifList = (List<OmsPointif>) dao.selectList("oms.order.select.pointif", pointif);
		if (pointifList != null && pointifList.size() > 0) {

			MembershipPointRes response = new MembershipPointRes();
			if ("usePoint".equals(pointIfType)) {
				MembershipUsePointReq use = new MembershipUsePointReq();
				// 공통
				use.setTrscBizDvCd("32");// 31:포인트사용거래, 32:포인트사용거래취소
				// 데이터
				use.setMmbCertDvCd(pointif.getMmbCertDvCd());// 조회 유형 회원번호(필수)
				use.setMmbCertDvVlu(pointif.getMmbCertDvVlu());// 회원번호(필수)
				use.setTrscRsnCd("CS01");// 거래사유코드 CS01:0to7 쇼핑몰 상품구매
				use.setUniqRcgnNo(omsPayment.getOrderId() + "_" + omsPayment.getClaimNo());// 클레임번호
				use.setOrgApvDt(pointifList.get(0).getOrgApvDt());
				use.setOrgApvNo(pointifList.get(0).getOrgApvNo());
				use.setOrgUniqRcgnNo(pointif.getUniqRcgnNo()); // 주문번호
				use.setPintUseTypCd(pointif.getPintUseTypCd());
				use.setUsePint(omsPayment.getPaymentAmt().toString());// 사용취소포인트
				
				response = membershipService.useMemberPoint(use);
			} else {
				MembershipSavePointReq save = new MembershipSavePointReq();
				// 공통
				save.setTrscBizDvCd("22");// 21:포인트적립거래, 22:포인트적립거래취소
				
				// 사용 데이터
				save.setMmbCertDvCd(pointif.getMmbCertDvCd());// 조회 유형 회원번호(필수)
				save.setMmbCertDvVlu(pointif.getMmbCertDvVlu());// 회원번호(필수)
				save.setTrscRsnCd("RS01");// 거래사유코드 RS01:0to7 쇼핑몰 상품구매
				// use.setUniqRcgnNo(uniqRcgnNo);
				save.setUniqRcgnNo(omsPayment.getOrderId() + "_" + omsPayment.getClaimNo());// 클레임번호
				save.setOrgApvDt(pointifList.get(0).getOrgApvDt());
				save.setOrgApvNo(pointifList.get(0).getOrgApvNo());
				save.setOrgUniqRcgnNo(pointif.getUniqRcgnNo()); // 주문번호
				
//				save.setMbrshpintSetlYn("N");
				save.setPintUseTypCd(pointif.getPintUseTypCd());
				save.setPintAcmlTypCd("30");// 클레임
				save.setAcmlPint(omsPayment.getPaymentAmt().negate().toString());//적립취소포인트
				
				response = membershipService.saveMemberPoint(save);
			}
			if (!"0000".equals(response.getResCd())) {
				ccsmessage.setSuccess(false);
				ccsmessage.setResultMessage(response.getResMsg());
			}
		} else {
			ccsmessage.setSuccess(false);
			String pointType = "적립";
			if ("usePoint".equals(pointIfType)) {
				pointType = "사용";
			}
			ccsmessage.setResultMessage("주문번호 " + omsPayment.getOrderId() + " 에 대한 " + pointType + " 포인트 연동 내역이 없습니다.");
		}

		return ccsmessage;
	}

	/**
	 * 물류 입고지시/완료
	 * 
	 * @Method Name : insertLogistics
	 * @author : victor
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param orderProduct
	 * @param claimTypeCd
	 * @param claimNo
	 * @param logisticsInoutNo
	 * @return logisticsInoutNo
	 * @throws Exception
	 */
	public BigDecimal insertLogistics(OmsOrderproduct orderProduct, String claimTypeCd, BigDecimal claimNo, BigDecimal logisticsInoutNo)
			throws Exception {
		// 세트는 제외
		if ("ORDER_PRODUCT_TYPE_CD.SET".equals(orderProduct.getOrderProductTypeCd())) {
			return logisticsInoutNo;
		}

		BigDecimal bClaimQty = orderProduct.getOmsClaimproduct().getClaimQty();

		// 1.2 주문입출고 - 클레임번호 생성후
		OmsLogistics omsLogistics = new OmsLogistics();
		omsLogistics.setOrderId(orderProduct.getOrderId());
		omsLogistics.setOrderProductNo(orderProduct.getOrderProductNo());
		omsLogistics.setClaimNo(claimNo);
		omsLogistics.setWarehouseInoutTypeCd("WAREHOUSE_INOUT_TYPE_CD.IN");

		omsLogistics.setInReserveQty(bClaimQty.toString()); // 입고예정수량
		if ("CLAIM_TYPE_CD.REDELIVERY".equals(claimTypeCd)) {
			omsLogistics.setVirtualInQty(bClaimQty); // 가입고수량
//			if ("SALE_TYPE_CD.PURCHASE".equals(orderProduct.getSaleTypeCd())) {
//				omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.RETURN"); // 입고완료
//			} else {
//				omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.RETURN_READY"); // 입고완료
//			}
			omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.RETURN"); // 입고완료
			omsLogistics.setCompleteDt(orderProduct.getUpdDt());
		} else {
			omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.RETURN_READY"); // 입고대기
		}
		
		
		
		
		omsLogistics.setTrackingIfYn("N");
		omsLogistics.setInsId(orderProduct.getUpdId());
		omsLogistics.setInsDt(orderProduct.getUpdDt());

		// BigDecimal yunbun1 = (BigDecimal) dao.selectOne("oms.logistics.getIoSeq", null);
		// BigDecimal yunbun2 = (BigDecimal) dao.selectOne("oms.logistics.getIoSeq", null);
		omsLogistics.setIoSeq(this.selectLogisticsIoSeq());

		if (logisticsInoutNo == null) {
			logisticsInoutNo = (BigDecimal) dao.selectOne("oms.logistics.getMaxLogisticsInoutNo", null);
		}
		omsLogistics.setLogisticsInoutNo(logisticsInoutNo);

		dao.insertOneTable(omsLogistics);
		return logisticsInoutNo;
	}

	private BigDecimal selectLogisticsIoSeq(){
		return (BigDecimal) dao.selectOne("oms.logistics.getIoSeq", null);
	}
	/**
	 * 교환 - 추가결제
	 * @Method Name : updateExchange
	 * @author : victor
	 * @date : 2016. 11. 9.
	 * @description : 
	 *	교환의 추가결제 대기 --> 추가결제 완료이후 상품상태 갱신 
	 * @param omsPayment
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CcsMessage updateExchange(OmsPayment omsPayment) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("클레임 접수처리에 실패하였습니다.");

		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		OmsClaim omsClaim = new OmsClaim();
		omsClaim.setOrderId(omsPayment.getOrderId());
		omsClaim.setClaimNo(omsPayment.getClaimNo());
		omsClaim.setUpdDt(currentDate);
		omsClaim.setUpdId(userId);
		omsClaim.setAcceptDt(currentDate);
		omsClaim.setClaimStateCd("CLAIM_STATE_CD.ACCEPT");
		
		// 교환대상상품조회
		List<OmsOrderproduct> omsOrderproducts = (List<OmsOrderproduct>) dao.selectList("oms.order.select.exchangeTarget", omsClaim);
		
		BigDecimal logisticsInoutNo = null;
		BigDecimal claimQty = BigDecimal.ZERO;
		for (OmsOrderproduct omsOrderproduct : omsOrderproducts) {
			omsOrderproduct.setUpdDt(currentDate);
			omsOrderproduct.setUpdId(userId);
			omsOrderproduct.setCancelDt(null);
			omsOrderproduct.setCancelQty(BigDecimal.ZERO);
			omsOrderproduct.setReturnQty(BigDecimal.ZERO);

			if (!"ORDER_DELIVERY_TYPE_CD.ORDER".equals(omsOrderproduct.getOrderDeliveryTypeCd())) {
				// 출고상품
				/** 출고.8. update oms_orderproduct */
				claimQty = omsOrderproduct.getOrderQty();
				omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");	// 출고지시대기
				if (dao.update("oms.order.update.updateOrderProduct", omsOrderproduct) > 0) {
					orderService.updateStock(omsOrderproduct, omsOrderproduct.getOrderQty().negate());// 재고
				}
			} else {
				// 입고상품
				/** 반품.7. INSERT oms_logistics */
				
//				BigDecimal bClaimQty = orderProduct.getOmsClaimproduct().getClaimQty();
				OmsClaimproduct omsClaimproduct = new OmsClaimproduct();
				omsClaimproduct.setOrderId(omsPayment.getOrderId());
				omsClaimproduct.setClaimNo(omsPayment.getClaimNo());
				omsClaimproduct.setClaimQty(claimQty);
				
				omsOrderproduct.setOmsClaimproduct(omsClaimproduct);
				logisticsInoutNo = this.insertLogistics(omsOrderproduct, "CLAIM_TYPE_CD.RETURN", omsClaim.getClaimNo(), logisticsInoutNo);
			}
		}
			
		if (dao.update("oms.claim.update.claim", omsClaim) > 0) {
			OmsClaimproduct omsClaimproduct = new OmsClaimproduct();
			omsClaimproduct.setOrderId(omsPayment.getOrderId());
			omsClaimproduct.setClaimNo(omsPayment.getClaimNo());
			omsClaimproduct.setUpdDt(currentDate);
			omsClaimproduct.setUpdId(userId);
			omsClaimproduct.setReturnOrderDt(currentDate);
			omsClaimproduct.setClaimProductStateCd("CLAIM_PRODUCT_STATE_CD.RETURN_ORDER");
			// 일괄갱신
			if (dao.update("oms.claim.update.claimProduct", omsClaimproduct) > 0) {
				ccsmessage = paymentService.insertNewPayment(omsPayment);
			} else {
				throw new ServiceException("oms.common.error", new Object[] { "추가결제 데이터 등록 중 에러 발생" });
			}
		} else {
			throw new ServiceException("oms.common.error", new Object[] { "추가결제 데이터 등록 중 에러 발생" });
		}

		return ccsmessage;
	}

	/**
	 * 전체취소
	 * 
	 * @Method Name : cancelAll
	 * @author : victor
	 * @date : 2016. 10. 13.
	 * @description :
	 *
	 * @param omsPayment
	 * @return
	 */
	public CcsMessage cancelAll(OmsPayment omsPayment) {
		LguBase cancel = new LguBase();
		CcsMessage ccsmessage = new CcsMessage();

		try {
			cancel.setLgdMid(omsPayment.getPgShopId());
			cancel.setLgdTid(omsPayment.getPgApprovalNo());
		} catch (Exception e) {
			logger.error(" 전체취소 파라미터 설정 에러 : {} ", e.getMessage());
		} finally {
			ccsmessage = pgService.cancelAll(cancel, omsPayment.getPaymentMethodCd());

			try {
				// 전문 insert
				OmsPaymentif omsPaymentif = new OmsPaymentif();
				omsPaymentif.setOrderId(omsPayment.getOrderId());
				omsPaymentif.setPaymentNo(omsPayment.getPaymentNo());
				omsPaymentif.setPaymentReqData(cancel.toString());
				omsPaymentif.setPaymentReturnData(ccsmessage.getMsg());

				dao.insertOneTable(omsPaymentif); // REQUIRED 이므로 자동으로 new transaction
				// pgService.insertPaymentTransferNewTx(omsPaymentif);
			} catch (Exception e) {
				logger.error(" 전체취소 전문 등록 에러 : {} ", e.getMessage());
			}
		}
		return ccsmessage;
	}

	public CcsMessage cancelKakao(OmsPayment omsPayment, String partialCancelCode, String claimNo) {
		// Kakao kakao = new Kakao();
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		try {
			// kakao.setMID(omsPayment.getPgShopId());
			// kakao.setTxnId(omsPayment.getPgApprovalNo());
			// kakao.setCancelAmt(omsPayment.getPaymentAmt().toString());

			// OmsPaymentif omsPaymentif = new OmsPaymentif();
			// omsPaymentif.setOrderId(omsPayment.getOrderId());
			// omsPaymentif.setPaymentNo(omsPayment.getPaymentNo());
			// omsPaymentif.setPaymentReqData(kakao.toString());

			// omsPayment.setKakao(kakao);
			// omsPayment.setOmsPaymentif(omsPaymentif);

			CnsPayWebConnector connector = new CnsPayWebConnector();
			// 1. 로그 디렉토리 생성
			connector.setLogHome(Config.getString("kakao.log.path"));
			connector.setCnsPayHome(Config.getString("kakao.config.path"));
			// connector.setRequestData(request);

			connector.addRequestData("MID", omsPayment.getPgShopId());
			connector.addRequestData("TID", omsPayment.getPgApprovalNo());
			connector.addRequestData("CancelAmt", omsPayment.getPaymentAmt().toString());

			// 클레임 번호가 1보다 크면 무조건 부분취소로 간주한다.
			connector.addRequestData("PartialCancelCode", "1".equals(claimNo) ? partialCancelCode : "1");// "0" : 전체, "1": 부분
			connector.addRequestData("CancelMsg", omsPayment.getRefundReasonCd());
			connector.addRequestData("SupplyAmt", "0");
			connector.addRequestData("ServiceAmt", "0");
			connector.addRequestData("CancelIP", "0");
			connector.addRequestData("CancelNo", claimNo);
			// connector.addRequestData("PreCancelCode", "0");

			String ediDate = KakaoUtil.getyyyyMMddHHmmss(); // 전문생성일시
			String encodeKey = Config.getString("kakao.encodeKey");

			//////// 위변조 처리/////////
			// 결제 취소 요청용 키값
			String md_src = ediDate + omsPayment.getPgShopId() + omsPayment.getPaymentAmt().toString();
			String hash_String = KakaoUtil.SHA256Salt(md_src, encodeKey);

			// 2. 요청 페이지 파라메터 셋팅
			// connector.setRequestData(request);

			// 3. 추가 파라메터 셋팅
			connector.addRequestData("actionType", "CL0");
			connector.addRequestData("EdiDate", ediDate);
			connector.addRequestData("EncryptData", hash_String);
			connector.addRequestData("EncodeKey", encodeKey);
			// connector.addRequestData("CancelIP".getRemoteAddr());

			// 4. CNSPAY Lite 서버 접속하여 처리
			connector.requestAction();
			// 5. 결과 처리
			String resultCode = connector.getResultData("ResultCode"); // 결과코드 (정상 :2001(취소성공), 그 외 에러)
			String resultMsg = connector.getResultData("ResultMsg"); // 결과메시지
			String cancelAmt = connector.getResultData("CancelAmt"); // 취소금액
			String cancelDate = connector.getResultData("CancelDate"); // 취소일
			String cancelTime = connector.getResultData("CancelTime"); // 취소시간
			String cancelNum = connector.getResultData("CancelNum"); // 취소번호
			String payMethod = connector.getResultData("PayMethod"); // 취소 결제수단
			String mid = connector.getResultData("MID"); // MID
			String tid = connector.getResultData("TID"); // TID
			String errorCD = connector.getResultData("ErrorCD"); // 에러코드
			String errorMsg = connector.getResultData("ErrorMsg"); // 에러메시지
			String AuthDate = cancelDate + cancelTime; // 취소거래시간
			String stateCD = connector.getResultData("StateCD"); // 거래상태코드 (0: 승인, 1:전취소, 2:후취소)
			String ccPartCl = connector.getResultData("CcPartCl"); // 부분취소 가능여부 (0:부분취소불가, 1:부분취소가능)
			String PreCancelCode = connector.getResultData("PreCancelCode"); // 취소 종류

			boolean cancelSuccess = false;

			if (resultCode.equals("2001")) {
				cancelSuccess = true; // 결과코드 (정상 :2001 , 그 외 에러)
			}

			// 전문 insert
			OmsPaymentif omsPaymentif = new OmsPaymentif();
			omsPaymentif.setOrderId(omsPayment.getOrderId());
			omsPaymentif.setPaymentNo(omsPayment.getPaymentNo());
			omsPaymentif.setPaymentReqData(omsPayment.toString());
			omsPaymentif.setPaymentReturnData(resultMsg);

			dao.insertOneTable(omsPaymentif); // REQUIRED 이므로 자동으로 new transaction

			kakaoService.setPaymentData(omsPayment, connector);
			omsPayment.setPgApprovalNo(null); // 승인번호
			omsPayment.setPgCancelNo(tid);
			omsPayment.setPaymentDt(AuthDate);

			ccsmessage.setSuccess(cancelSuccess);
			ccsmessage.setResultMessage(resultMsg);

		} catch (Exception e) {
			logger.error(" 전체취소 파라미터 설정 에러 : {} ", e.getMessage());
			ccsmessage.setResultMessage("전체취소 파라미터 설정 에러");
			throw new ServiceException("oms.pg.error.allcancel", new Object[] { ccsmessage.getResultMessage() });
		} finally {
			try {
				// Map<String, Object> result = kakaoService.kakaopayCancelRequest(omsPayment);

				// if (BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get("BaseConstants.RESULT_FLAG"))) {
				// ccsmessage.setSuccess(true);
				// } else {
				// ccsmessage.setResultMessage(String.valueOf(result.get(BaseConstants.RESULT_MESSAGE)));
				// }

			} catch (Exception e1) {
				logger.error(" 카카오페이 취소 에러 : {} ", e1.getMessage());
			}
		}
		return ccsmessage;
	}

	/**
	 * /** 부분취소1
	 * 
	 * @Method Name : cancelPartial
	 * @author : victor
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param omsPayment
	 * @return CcsMessage
	 */
	public CcsMessage cancelPartial(OmsPayment omsPayment) {
		return this.cancelPartial(omsPayment, null, null);
	}

	/**
	 * 부분취소2
	 * 
	 * @Method Name : cancelPartial
	 * @author : victor
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param omsPayment
	 * @param claimReason
	 * @param phone2
	 * @return CcsMessage
	 */
	public CcsMessage cancelPartial(OmsPayment omsPayment, String claimReason, String phone2) {
		LguCancelPartial cancel = new LguCancelPartial();
		CcsMessage ccsmessage = new CcsMessage();

		try {
			cancel.setLgdMid(omsPayment.getPgShopId());
			cancel.setLgdTid(omsPayment.getPgApprovalNo());
			cancel.setLgdCancelAmount(omsPayment.getRefundAmt().toString());
			cancel.setLgdCancelReason(claimReason);
			// cancel.setLgdRemainAmount(omsPayment.getPaymentAmt().subtract(omsPayment.getRefundAmt()).toString());
			// cancel.setLgdReqRemain("1"); // 취소후 남은 금액 리턴

			if ("PAYMENTMETHODCD.VIRTUAL".equals(omsPayment.getPaymentMethodCd())) {
				cancel.setLgdRfBankCode(omsPayment.getPaymentBusinessCd());
				cancel.setLgdRfAccountNum(omsPayment.getRefundAccountNo());
				cancel.setLgdRfCustomerName(omsPayment.getAccountHolderName());
				cancel.setLgdRfPhone(phone2);
			}
		} catch (Exception e) {
			logger.error(" 부분취소 파라미터 설정 에러 : {} ", e.getMessage());
		} finally {
			ccsmessage = pgService.cancelPartial(cancel);

			try {
				// 전문 insert
				OmsPaymentif omsPaymentif = new OmsPaymentif();
				omsPaymentif.setOrderId(omsPayment.getOrderId());
				omsPaymentif.setPaymentNo(omsPayment.getPaymentNo());
				omsPaymentif.setPaymentReqData(cancel.toString());
				omsPaymentif.setPaymentReturnData(ccsmessage.getMsg());

				dao.insertOneTable(omsPaymentif); // REQUIRED 이므로 자동으로 new transaction
				// pgService.insertPaymentTransferNewTx(omsPaymentif);
			} catch (Exception e) {
				logger.error(" 부분취소 전문 등록 에러 : {} ", e.getMessage());
			}
		}
		return ccsmessage;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int delete(Object claim) throws Exception {
		return dao.deleteOneTable(claim);
	}

	public CcsMessage createClaim(OmsClaim omsClaim, BigDecimal memberNo) throws Exception {

		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("클레임 중 알수 없는 에러가 발생하였습니다.");

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		omsClaim.setUpdDt(currentDate);
		omsClaim.setReqDt(currentDate);
		omsClaim.setAcceptDt(currentDate);
		omsClaim.setCompleteDt(currentDate);

		return this.updateCancelAll(omsClaim, memberNo);
	}

	/**
	 * 
	 * @Method Name : updateVirtualPaymentCancelNewTx
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 가상계좌 취소(주문건당)
	 *
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateVirtualPaymentCancelNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		OmsClaim omsClaim = new OmsClaim();
		omsClaim.setOrderId(order.getOrderId());
		omsClaim.setClaimStateCd("CLAIM_STATE_CD.COMPLETE");
		omsClaim.setClaimTypeCd("CLAIM_TYPE_CD.CANCEL");
		omsClaim.setCancelAll(true);
		String updDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		omsClaim.setUpdDt(updDt);
		omsClaim.setUpdId("BATCH");
		CcsMessage subResult = createClaim(omsClaim, order.getMemberNo());
		if (!subResult.isSuccess()) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			result.put(BaseConstants.RESULT_MESSAGE, subResult.getResultMessage());
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : updateVirtualPaymentCancel
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 가상계좌 취소
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateVirtualPaymentCancel() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		List<OmsOrder> orderList = (List<OmsOrder>) dao.selectList("oms.order.getVirtualOrderCancelList", null);

		int totalCnt = orderList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderList) {
			Map subResult = new HashMap();
			try {
				subResult = ((ClaimService) getThis()).updateVirtualPaymentCancelNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			// 실패시.
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

	public Map<String, Object> updateGiftOrderCancelNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		OmsClaim omsClaim = new OmsClaim();
		omsClaim.setOrderId(order.getOrderId());
		omsClaim.setClaimStateCd("CLAIM_STATE_CD.COMPLETE");
		omsClaim.setClaimTypeCd("CLAIM_TYPE_CD.CANCEL");
		omsClaim.setCancelAll(true);
		String updDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		omsClaim.setUpdDt(updDt);
		omsClaim.setUpdId("BATCH");
		CcsMessage subResult = createClaim(omsClaim, order.getMemberNo());
		if (!subResult.isSuccess()) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			result.put(BaseConstants.RESULT_MESSAGE, subResult.getResultMessage());
		}
		return result;
	}

	public Map<String, Object> updateGiftOrderCancel() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		List<OmsOrder> orderList = (List<OmsOrder>) dao.selectList("oms.order.getGiftOrderCancel", null);

		int totalCnt = orderList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderList) {
			Map subResult = new HashMap();
			try {
				subResult = ((ClaimService) getThis()).updateGiftOrderCancelNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			// 실패시.
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
