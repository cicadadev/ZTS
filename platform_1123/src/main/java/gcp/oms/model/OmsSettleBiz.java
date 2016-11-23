package gcp.oms.model;

import java.math.BigDecimal;

import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsSettleBiz extends BaseEntity {
	//공급업체ID
	private String businessId;
	//공급업체명
	private String businessName;
	//단품ID
	private String saleproductId;
	//수수료율
	private BigDecimal commissionRate;
	//수수료 금액
	private BigDecimal saleChargeAmt;
	//최종판매금액(합계)
	private BigDecimal finalSaleAmt;
	//최종판매금액(신용카드)
	private BigDecimal finalCardAmt;
	//최종판매금액(현금)
	private BigDecimal finalCashAmt;
	//최종판매금액(휴대폰)
	private BigDecimal finalMobileAmt;
	//최종판매금액(기타)
	private BigDecimal finalEtcAmt;
	//최종판매금액(자사쿠폰금액)
	private BigDecimal finalOwnCouponAmt;
	//최종판매금액(업체쿠폰금액)
	private BigDecimal finalBizCouponAmt;
	//위탁판매수수료
	private BigDecimal finalSaleChargeAmt;
	//최종지급액
	private BigDecimal finalSupportAmt;

	//결제유형: 카드
	private BigDecimal cardSum;
	//결제유형: 가상계좌
	private BigDecimal virtualSum;
	//결제유형: 계좌이체
	private BigDecimal transferSum;
	//결제유형: 휴대폰결제
	private BigDecimal mobileSum;
	//결제유형: 외상매출금
	private BigDecimal creditsaleSum;
	//결제유형: 포인트
	private BigDecimal pointSum;
	//결제유형: 예치금
	private BigDecimal depositSum;
	//결제유형: 상품권
	private BigDecimal voucherSum;
	//결제유형: 현금
	private BigDecimal cashSum;
	//플러스쿠폰 합계
	private BigDecimal plusCouponDcAmtSum;
	//주문쿠폰 합계
	private BigDecimal orderCouponDcAmtSum;
	//예치금 합계
	private BigDecimal depositAmtSum;
	//사용포인트 합계
	private BigDecimal usePointSum;
	//현금 환불 합계
	private BigDecimal cashRefundAmtSum;
	//예치금 환불 합계
	private BigDecimal depositRefundAmtSum;
}
