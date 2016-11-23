package gcp.oms.model.pg.lgu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class LguCancelPartial extends LguBase {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -721124124623494929L;

	private String lgdCancelAmount;// 부분취소 금액
	private String lgdCancelTaxfreeAmount;// 면세대상 부분취소 금액 (과세/면세 혼용상점만 적용)
	private String lgdCancelReason;// 취소사유

	// 신용카드
	private String lgdRemainAmount;// 취소전 남은 금액(신용카드만)

	// 신용카드, 휴대폰
	private String lgdReqRemain;// 취소 후 남은 금액 리턴여부(1:리턴, 0: 리턴안함)

	// 계좌이체, 가상계좌
	private String lgdPcancelCnt; // 부분환불요청횟수,요청 시, 중복요청에 의한 환불을 막기 위해 상점에서 요청횟수를 관리/사용할 경우 사용

	// 가상계좌(무통장)
	private String lgdRfBankCode;// 환불계좌 은행코드(필수)
	private String lgdRfAccountNum;// 환불계좌 번호(필수)
	private String lgdRfCustomerName;// 환불계좌 예금주(필수)
	private String lgdRfPhone;// 요청자 연락처

	private String lgdDivideYn;// [분할정산 상점 부분취소시 필수]분할정산 대상 상점( Y: 분할정산 사용, N: 분할정산 사용 안함)
	private String lgdDivideMidSub;// [분할정산 상점 부분취소시 필수]분할정산 하위 상점
}