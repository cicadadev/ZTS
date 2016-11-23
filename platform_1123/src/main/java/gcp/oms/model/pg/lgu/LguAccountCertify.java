package gcp.oms.model.pg.lgu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class LguAccountCertify extends LguBase {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -721124124623494929L;

	// ㅁ.거래구분
	// 2: 예금주확인 (성명+은행+계좌번호)
	// 3: 성명+예금주확인 (성명+은행+계좌번호+생년월일)
	// 4: 계좌유효성확인 (은행+계좌번호)
	// 5: 계좌확인 (은행+계좌번호+생년월일)
	// 6: 계좌예금주 확인 (은행+계좌번호)
	// ☞ 사업자번호 또는 생년월일로 확인 가능 은행(거래구분 “3” 또는 “5” 가능 은행): 국민, 농협, 기업, 씨티, SC, 부산, 경남, 광주
	// 그 외 은행은 거래구분 “3” 또는 “5” 사용 불가
	private String lgdGubun; // 거래구분 : '2' 고정
	private String lgdBankCode; // 은행코드
	private String lgdAccountNo; // 계좌번호
	private String lgdName; // 예금주명
	private String lgdPrivateNo; // 생년월일 6자리 (YYMMDD) or 사업자번호 10자리
	private String lgdCheckNhyn; // (옵션) 농협(단위조합) 계좌 여부 체크 (디폴트: N)
	private String lgdBuyerIp; // 구매자IP
}