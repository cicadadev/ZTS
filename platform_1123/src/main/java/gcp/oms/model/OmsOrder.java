package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.common.util.CodeUtil;
import gcp.mms.model.MmsMemberZts;
import gcp.oms.model.base.BaseOmsOrder;
import gcp.sps.model.SpsPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsOrder extends BaseOmsOrder {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1499689402257421849L;

	private BigDecimal deliveryAmt;
	private BigDecimal wrappingAmt;
	// private String paymentAmt;

	private String totalPoint;

	private String orderQty;
	private String cancelQty;
	private String returnQty;
	private String exchangeQty;
	private String outQty;

	private String pgShopId;
	private BigDecimal paymentNo;  // 에스크로결제 결제번호
	private String siteName;

	private String receiver;
	private String receiverMobile;
	private int memoCnt;

	private MmsMemberZts mmsMemberZts;

	private String orderStat; // 주문단계 1.PRESENT -> 2.ORDERSHEET -> 3.ORDER
	private String cartProductNos; // 선택된 장바구니상품 번호
	private String selectPresent; // 선택된 사은품 정보.
	private List<SpsPresent> spsPresents; // 주문사은품
	private String selectCoupon; // 선택된 쿠폰 정보.
	private BigDecimal totalOrderSalePrice; // 총판매가
	private String giftYn; // 기프티콘여부
	private String cancelAllYn; // 기프티콘여부

	private String readyCount; // 출고대기
	private String deliveryOrderCount; // 출고지시
	private String shipCount; // 출고완료
	private String deliveryCount; // 배송완료
	private String returnOrderCount; // 클레임(입고대기)
	private String totalOrderAmt; // 주문금액

	private OmsOrdercoupon optimalCoupon; // 최적쿠폰

	private String personalCustomsCode; // 개인통관부호

	private List<String> memberTypeCds;
	private String memGradeCd;
	private String expireDt;

	private OmsOrderproduct omsOrderproduct;

	private String regularDeliveryId; // 정기배송id
	private String regularDeliveryDt; // 정기배송일자

	private BigDecimal totalPointsave; // 총 적립예정포인트
	private String saveDt;	//적립일자

	private String deliveryStep; // 마이페이지 메인 주문/배송현황
	private BigDecimal stepCnt; // 단계별 카운트

	private String orderLoginReturn;
	
	private List<OmsPickupproduct> omsPickupproducts;
	
	private String deliveryZipCd;
	
	private String oneDeliInfoYn;
	
	public String getOrderDeliveryStateNote(){
		return CodeUtil.getCodeNote(getOrderDeliveryStateCd());
	}
}