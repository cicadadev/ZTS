package gcp.oms.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.service.TmsService;
import gcp.oms.model.OmsOrderTms;
import gcp.oms.model.OmsRegulardeliveryproduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.ImageUrlUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : OrderTmsService.java
 * @author : dennis
 * @date : 2016. 10. 24.
 * @description : 주문관련 TMS
 */
@Service("orderTmsService")
public class OrderTmsService extends BaseService {
	private static final Logger	logger	= LoggerFactory.getLogger(OrderTmsService.class);
	
	@Autowired
	private TmsService			tmsService;

	/**
	 * 
	 * @Method Name : saveOrderTms
	 * @author : dennis
	 * @date : 2016. 10. 24.
	 * @description : 주문관련 SMS
	 * 
	 *              ORDER_COMPLETE : 주문완료.(101)
	 * 
	 *              VIRTUAL_PAYMENT : 무통장주문(102)
	 * 
	 *              PICKUP_COMPLETE : 픽업신청완료.(105,106)
	 * 
	 *              PICKUP_AUTO_CANCEL : 픽업자동취소(109,110)
	 * 
	 *              REGULAR_COMPLETE : 정기배송신청완료.(111,013)
	 * 
	 *              REGULAR_PAYMENTINFO : 정기배송결재안내(112)
	 * 
	 *              REGULAR_PAYMENT : 정기배송 결재(113)
	 * 
	 *              REGULAR_PAYMENT_FAIL : 정기배송 결재실패(114)
	 * 
	 *              GIFT_COMPLETE : 기프티콘 결제완료.(116,117)
	 * 
	 *              GIFT_INFO : 기프티콘 유효기간.(118)
	 * 
	 *              GIFT_CANCEL : 기프티콘 취소.(119)
	 *
	 * @param orderTms
	 */
	public void saveOrderTms(OmsOrderTms orderTms) {
		
		logger.debug("====== orderTms param : +" + orderTms.toString());

		//유형
		String type = orderTms.getType();
		//주문자명
		String orderName = orderTms.getOrderName();
		//받는사람이름.
		String deliveryName = orderTms.getDeliveryName();
		//상품명
		String productName = orderTms.getProductName();
		//주문,매장픽업,정기배송 id
		String orderId = orderTms.getOrderId();
		//결제금액
		String paymentAmt = null;
		if (CommonUtil.isNotEmpty(orderTms.getPaymentAmt())) {
			paymentAmt = CommonUtil.formatMoney(orderTms.getPaymentAmt());
		}

		//가상계좌 은행
		String bankName = orderTms.getBankName();
		//가상계좌 번호
		String accountNo = orderTms.getAccountNo();

		//주문일자
		String orderDt = orderTms.getOrderDt();
		//가상계좌입금마감일, 기프티콘자동취소일자.
		String endDt = orderTms.getEndDt();

		//픽업일자
		String pickupDate = orderTms.getPickupDate();
		//매장명
		String offshopName = orderTms.getOffshopName();

		//정기배송일자
		String regularDeliveryDt = orderTms.getRegularDeliveryDt();
		if (CommonUtil.isNotEmpty(regularDeliveryDt)) {

			try {
				regularDeliveryDt = DateUtil.convertFormat(regularDeliveryDt, DateUtil.FORMAT_1, DateUtil.FORMAT_2);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//제목
		String subject = "";
		//내용
//		StringBuffer content = new StringBuffer();

		if ("PICKUP_COMPLETE".equals(type)) {

			subject = "0to7.com 픽업신청";
//			content.append("[").append(subject).append("]");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶신청번호: ").append(orderId);
//			content.append(" ▶매장명: ").append(offshopName);
//			content.append(" ▶픽업요청일: ").append(pickupDate);

			orderTms.setMsgCode("105");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(orderId);
			orderTms.setMap4(offshopName);
			orderTms.setMap5(pickupDate);

		} else if ("PICKUP_AUTO_CANCEL".equals(type)) {

			subject = "0to7.com 픽업기간만료";
//			content.append("[").append(subject).append("]");
//			content.append(" 픽업가능기간 경과로 주문이 자동취소되었습니다.");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶신청번호: ").append(orderId);
//			content.append(" ▶매장명: ").append(offshopName);

			orderTms.setMsgCode("109");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(orderId);
			orderTms.setMap4(offshopName);

		} else if ("VIRTUAL_PAYMENT".equals(type)) {

			subject = "0to7.com 입금안내";
//			content.append("[").append(subject).append("]");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶주문번호: ").append(orderId);
//			content.append(" ▶은행명: ").append(bankName);
//			content.append(" ▶계좌번호: ").append(accountNo);
//			content.append(" ▶입금액: ").append(paymentAmt);
//			content.append(" ▶입금기한: ").append(endDt);

			orderTms.setMsgCode("102");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(orderId);
			orderTms.setMap4(bankName);
			orderTms.setMap5(accountNo);
			orderTms.setMap6(paymentAmt);
			orderTms.setMap7(endDt);

		} else if ("ORDER_COMPLETE".equals(type)) {

			subject = "0to7.com 주문완료";
//			content.append("[").append(subject).append("]");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶주문번호: ").append(orderId);
//			content.append(" ▶결제금액: ").append(paymentAmt + "원");

			orderTms.setMsgCode("101");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(orderId);
			orderTms.setMap4(paymentAmt);

		} else if ("REGULAR_COMPLETE".equals(type)) {	//정기배송 신청완료.

			String firstDate = orderTms.getFirstDate();
			String periodCd = orderTms.getPeriodCd();
			String deliveryPeriodValue = orderTms.getPeriodValue();
			String deliveryCnt = orderTms.getDeliveryCnt();
			String url = CommonUtil.makeShortUrl("/mms/mypage/regular/change/" + orderId);

			String week = weekDy(deliveryPeriodValue);

			subject = "0to7.com 정기배송신청";
//			content.append("[").append(subject).append("]");
//			content.append(" 정기배송을 신청해 주셔서 감사합니다.");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶정기배송일: ").append(firstDate).append("부터 ").append(periodCd).append("주 간격 ").append(week);
//			content.append(" ▶신청횟수 : ").append(deliveryCnt).append("회");
//		    content.append(" ▶자동결제: 배송 3일전 17:00");
//			content.append(" ▶신청내역확인( ").append(url).append(" )");

			orderTms.setMsgCode("111");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(firstDate);
			orderTms.setMap4(periodCd + "주 간격 " + week);
			orderTms.setMap5(deliveryCnt);
			orderTms.setMap6(url);

		} else if ("REGULAR_PAYMENT".equals(type)) {		//정기배송 결제완료.

			subject = "0to7.com 정기배송결제완료";
//			content.append("[").append(subject).append("]");
//			content.append(" 정기배송상품 결제가 완료되었습니다.");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶배송예정일: ").append(regularDeliveryDt);
//			content.append(" ▶결제금액: ").append(paymentAmt + "원");

			orderTms.setMsgCode("113");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(regularDeliveryDt);
			orderTms.setMap4(paymentAmt);

		} else if ("REGULAR_PAYMENTINFO".equals(type)) {	//정기배송 결제안내

			subject = "0to7.com 정기배송결제안내";
//			content.append("[").append(subject).append("]");
//			content.append(" 정기배송상품 결제가 진행될 예정입니다.");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶정기배송일: ").append(regularDeliveryDt);
//			content.append(" ▶결제예정금액: ").append(paymentAmt + "원");
//			content.append(" ▶결제예정일시: ").append(DateUtil.getCurrentDate(DateUtil.FORMAT_2) + " 17:00");

			orderTms.setMsgCode("112");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(regularDeliveryDt);
			orderTms.setMap4(paymentAmt);
			orderTms.setMap5(DateUtil.getCurrentDate(DateUtil.FORMAT_2));

		} else if ("REGULAR_PAYMENT_FAIL".equals(type)) {

			subject = "0to7.com 정기배송결제실패";
			orderTms.setMsgCode("114");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(regularDeliveryDt);
			orderTms.setMap4(paymentAmt);

		} else if ("GIFT_COMPLETE".equals(type)) {
			subject = "0to7.com 선물결제완료";
//			content.append("[").append(subject).append("]");
//			content.append(" ▶상품명: ").append(productName);
//			content.append(" ▶주문번호: ").append(orderId);
//			content.append(" ▶결제금액: ").append(paymentAmt).append("원");

			orderTms.setMsgCode("116");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
			orderTms.setMap3(orderId);
			orderTms.setMap4(paymentAmt);

		} else if ("GIFT_INFO".equals(type)) {

			try {
				endDt = DateUtil.getAddDate(DateUtil.FORMAT_2, orderDt, new BigDecimal(6));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String url = CommonUtil.makeShortUrl("/mms/mypage/order/giftCert");

			subject = "0to7.com 선물취소안내";
//			content.append("[").append(subject).append("]");
//			content.append(deliveryName).append("님 받으신 선물 유효기간이 내일까지 입니다. 배송을 위한 정보 미입력시 주문이 자동취소됩니다.");
//			content.append(" ▶유효기간: ").append(endDt);
//			content.append(" ▶선물확인( ").append(url).append(" )");

			orderTms.setMsgCode("118");
			orderTms.setMap1(deliveryName);
			orderTms.setMap2(endDt);
			orderTms.setMap3(url);

		} else if ("GIFT_CANCEL".equals(type)) {
			subject = "0to7.com 선물자동취소";
//			content.append("[").append(subject).append("]");
//			content.append("  배송정보 입력 유효기간 경과로 선물이 자동 취소되어 사용할 수 없습니다.");
//			content.append(" ▶상품명: ").append(productName);

			orderTms.setMsgCode("119");
			orderTms.setMap1(productName);
			orderTms.setMap2("");
		}

//		subject = "0to7.com 픽업신청";
		orderTms.setSubject(subject);
//		orderTms.setMapContent(content.toString());

		logger.debug("====== orderTms 1 : +" + orderTms.toString());

		tmsService.sendTmsSmsQueue(orderTms);

		//추가 메시지.
		//매장픽업 매니저한테 발송.
		//기프트콘 대상자한테 발송.
		String addPhone = orderTms.getAddPhone();

		if (CommonUtil.isNotEmpty(addPhone)) {

			orderTms.setToPhone(addPhone);

			if ("PICKUP_COMPLETE".equals(type)) {
				subject = "0to7.com 픽업신청";
//				content = new StringBuffer();
//				content.append("[").append(subject).append("]");
//				content.append(" 매장픽업주문이 발생하였습니다.");
//				content.append(" ▶상품명: ").append(productName);
//				content.append(" ▶신청번호: ").append(orderId);
//				content.append(" ▶매장명: ").append(offshopName);
//				content.append(" ▶고객명: ").append(orderName);
//				content.append(" ▶픽업요청일: ").append(pickupDate);

				orderTms.setMsgCode("106");
				orderTms.setMap1(productName);
				orderTms.setMap2("");
				orderTms.setMap3(orderId);
				orderTms.setMap4(offshopName);
				orderTms.setMap5(orderName);
				orderTms.setMap6(pickupDate);

			} else if ("PICKUP_AUTO_CANCEL".equals(type)) {
				//주문자 sms와 내용 동일
				orderTms.setMsgCode("110");
			} else if ("GIFT_COMPLETE".equals(type)) {

				try {
					endDt = DateUtil.getAddDate(DateUtil.FORMAT_2, orderDt, new BigDecimal(6));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = CommonUtil.makeShortUrl("/mms/mypage/order/giftCert");

				subject = "0to7.com 선물도착";
//				content = new StringBuffer();
//				content.append("[").append(subject).append("]");
//				content.append(orderName).append("님께서 선물을 보내셨습니다.");
//				content.append(" ▶상품명: ").append(productName);
//				content.append(" ▶유효기간: ").append(endDt);
//				content.append(" ▶선물확인( ").append(url).append(" )");
//				content.append(" 선물확인 링크를 통해 배송을 위한 정보를 직접 입력해 주세요.");
//				content.append(" 유효기간 내 입력하지 않을 경우 주문은 자동취소됩니다.");

				orderTms.setMsgCode("117");
				orderTms.setMap1(orderName);
				orderTms.setMap2(productName);
				orderTms.setMap3("");
				orderTms.setMap4(endDt);
				orderTms.setMap5(url);
			}

			logger.debug("====== orderTms 2 : +" + orderTms.toString());
			orderTms.setSubject(subject);
//			orderTms.setMapContent(content.toString());

			tmsService.sendTmsSmsQueue(orderTms);

		}

	}
	
	public void saveOrderEmail(OmsOrderTms orderTms) {
		//유형
		String type = orderTms.getType();
		//주문자명
		String orderName = orderTms.getOrderName();
		//받는사람이름.
		String deliveryName = orderTms.getDeliveryName();
		//상품명
		String productName = orderTms.getProductName();
		//주문,매장픽업,정기배송 id
		String orderId = orderTms.getOrderId();
		//결제금액
		String paymentAmt = null;
		if (CommonUtil.isNotEmpty(orderTms.getPaymentAmt())) {
			paymentAmt = CommonUtil.formatMoney(orderTms.getPaymentAmt());
		}

		String orderDt = orderTms.getOrderDt();
		String phone1 = orderTms.getPhone1();
		String phone2 = orderTms.getPhone2();
		String address = orderTms.getAddress();
		String note = orderTms.getNote();
		String mapContent = orderTms.getMapContent();

		String subject = "";

		if ("REGULAR_COMPLETE".equals(type)) {
			subject = "0to7.com 정기배송신청";
			orderTms.setMsgCode("013");
			orderTms.setMap1(orderName);
			orderTms.setMap2(DateUtil.getCurrentDate(DateUtil.FORMAT_14));
			orderTms.setMap3(orderId);
			orderTms.setMap4(deliveryName);
			orderTms.setMap5(phone2);
			if (CommonUtil.isNotEmpty(phone1)) {
				orderTms.setMap6(" / " + phone1);
			}
			orderTms.setMap7(address);
			orderTms.setMap8(note);
			orderTms.setMapContent(mapContent);
		}
		
		orderTms.setSubject(subject);
		tmsService.sendTmsEmailQueue(orderTms);
	}
	
	private String weekDy(String deliveryPeriodValue) {
		String week = "";
		if ("1".equals(deliveryPeriodValue)) {
			week = "일요일";
		} else if ("2".equals(deliveryPeriodValue)) {
			week = "월요일";
		} else if ("3".equals(deliveryPeriodValue)) {
			week = "화요일";
		} else if ("4".equals(deliveryPeriodValue)) {
			week = "수요일";
		} else if ("5".equals(deliveryPeriodValue)) {
			week = "목요일";
		} else if ("6".equals(deliveryPeriodValue)) {
			week = "금요일";
		} else if ("7".equals(deliveryPeriodValue)) {
			week = "토요일";
		} else if ("1".equals(deliveryPeriodValue)) {
			week = "일요일";
		}
		return week;
	}

	public String getRegularProductTempate(List<OmsRegulardeliveryproduct> productList ){
		StringBuffer content = new StringBuffer();
		String imgPath = Config.getString("front.domain.url");
		content.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; margin:20px 0 0; padding:0; border-top:1px solid #141759;\">");
		
		for(OmsRegulardeliveryproduct product : productList){

			String deliveryTypeCd = product.getDeliveryProductTypeCd();
			String productId = product.getProductId();
			String productName = product.getPmsProduct().getName();
			String saleproductName = product.getPmsSaleproduct().getName();
			String orderQty = product.getOrderQty().toString();
			String totalSalePrice = CommonUtil.formatMoney(product.getRegularDeliveryPrice().toString());
			String deliveryCnt = product.getDeliveryCnt().toString();
			String regularDeliveryDt = product.getRegularDeliveryDt();
			BigDecimal period = product.getDeliveryPeriodValue();
			String weekDy = weekDy(period.toString());
			String imgUrl = ImageUrlUtil.productMakeURL(productId, "90", null, null);

			BigDecimal paymentDy = (period.add(new BigDecimal(4))).remainder((new BigDecimal(7)));
			if (paymentDy.compareTo(new BigDecimal(0)) == 0) {
				paymentDy = new BigDecimal(7);
			}

			String dt = regularDeliveryDt + " (" + weekDy + ")";
			String paymentDt = DateUtil.getAddDate(DateUtil.FORMAT_2, regularDeliveryDt, new BigDecimal(-3)) + "("
					+ weekDy(paymentDy.toString()) + ")" + " / "
					+ deliveryCnt;
		
			content.append("	<tr>																																																																																																																																																");
			content.append("		<td style=\"margin:0; padding:0; border-bottom:1px solid #dddddd;\">                                                                                                                                                                                                                              ");
			content.append("			<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; margin:0; padding:0; text-align:center; line-height:1.8; font-size:13px;\">                                                                                                                          ");
			content.append("				<tr>                                                                                                                                                                                                                                                                                          ");
			content.append(
					"					<td style=\"width:120px; padding:16px 0 16px; color:#333333; vertical-align:top;\"><img src=\"")
					.append(imgUrl)
					.append("\" border=\"0\" alt=\" style=\"vertical-align:middle; max-width:100%;\"></td>                                                                                 ");
			content.append("					<td style=\"padding:10px 0 10px; color:#333333; text-align:left; vertical-align:top;\">                                                                                                                                                                                                     ");
			content.append(productName).append(
					"						<br>                                                                                                                                                                                                                                                    ");
			if ("DELIVERY_PRODUCT_TYPE_CD.SET".equals(deliveryTypeCd)) {
				for (OmsRegulardeliveryproduct subProduct : product.getOmsRegulardeliveryproducts()) {
					String subProductName = subProduct.getPmsProduct().getName();
					String subSaleproductName = subProduct.getPmsSaleproduct().getName();
					String setQty = subProduct.getSetQty().toString();
					saleproductName = subProductName + " : " + subSaleproductName + " (" + setQty + "개)";
					content.append(
							"						<span style=\"color:#cccccc;\">└</span> <span style=\"color:#999999; font-size:12px;\">")
							.append(saleproductName)
							.append("</span><br>                                                                                                                                                                                  ");
				}
			} else {
				content.append(
						"						<span style=\"color:#cccccc;\">└</span> <span style=\"color:#999999; font-size:12px;\">")
						.append(saleproductName)
						.append("</span><br>                                                                                                                                                                                  ");
			}

			content.append("						<br>                                                                                                                                                                                                                                                                                      ");
			content.append("						<span style=\"color:#666666;\">").append(orderQty).append(
					"개 /</span> <span style=\"color:#000000; font-size:14px; font-weight:bold;\">").append(totalSalePrice)
					.append("원</span>                                                                                                                                                              ");
			content.append("					</td>                                                                                                                                                                                                                                                                                       ");
			content.append("				</tr>                                                                                                                                                                                                                                                                                         ");
			content.append("				<tr>                                                                                                                                                                                                                                                                                          ");
			content.append("					<td style=\"padding:16px 0 23px; color:#333333; vertical-align:top; \" colspan=\"2\">                                                                                                                                                                                                       ");
			content.append("						<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; margin:0; padding:0; text-align:left; line-height:1.85; font-size:12px; background:#f4f7f8\">                                                                                                  ");
			content.append("							<tr>                                                                                                                                                                                                                                                                                    ");
			content.append("								<td style=\"width:50%; margin:0; padding:11px 10px 13px; letter-spacing:-0.1em;\">                                                                                                                                                                                                    ");
			content.append(
					"									<span style=\"display:inline-block; width:85px; margin:0; padding:0; color:#333333; font-size:12px; font-weight:bold;\"><img src=\"")
					.append(imgPath)
					.append("/resources/img/common/mail/icon_dot01.png\" border=\"0\" alt=\"dot\" style=\"vertical-align:top; margin-top:10px; font-weight:bold;\"> 결제예정일시</span>  ");


			content.append(
					"									<span style=\"display:inline-block; margin:0; padding:0; color:#666666; letter-spacing:-0.05em;\">")
					.append(paymentDt)
					.append("회중 1회차</span>                                                                                                                                              ");
			content.append("								</td>                                                                                                                                                                                                                                                                                 ");
			content.append("								<td style=\"width:50%; margin:0; padding:11px 10px 13px; letter-spacing:-0.1em;\">                                                                                                                                                                                                    ");
			content.append(
					"									<span style=\"display:inline-block; width:85px; margin:0; padding:0; color:#333333; font-size:12px; font-weight:bold;\"><img src=\"")
					.append(imgPath)
					.append("/resources/img/common/mail/icon_dot01.png\" border=\"0\" alt=\"dot\" style=\"vertical-align:top; margin-top:10px; font-weight:bold;\"> 배송 예정일</span>   ");
			content.append(
					"									<span style=\"display:inline-block; margin:0; padding:0; color:#666666; letter-spacing:-0.05em;\">")
					.append(dt)
					.append("</span>                                                                                                                                                            ");
			content.append("								</td>                                                                                                                                                                                                                                                                                 ");
			content.append("							</tr>                                                                                                                                                                                                                                                                                   ");
			content.append("						</table>                                                                                                                                                                                                                                                                                  ");
			content.append("					</td>                                                                                                                                                                                                                                                                                       ");
			content.append("				</tr>                                                                                                                                                                                                                                                                                         ");
			content.append("			</table>                                                                                                                                                                                                                                                                                        ");
			content.append("		</td>                                                                                                                                                                                                                                                                                             ");
			content.append(
					"	</tr>                                                                                                                                                                                                                                                                                               ");

		}
		content.append("</table>");
		return content.toString();
	}

}
