package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPointif extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal pointIfNo; //포인트연동번호		[primary key, primary key, primary key, not null]
	private String mmbCertDvCd; //회원인증구분코드		[not null]
	private String mmbCertDvVlu; //회원인증구분값		[not null]
	private String pintUseTypCd; //포인트사용유형코드		[null]
	private String usePint; //사용포인트		[null]
	private String uniqRcgnNo; //관계사고유식별번호||고유관리번호-주문번호,취소번호등		[not null]
	private String pintAcmlTypCd; //포인트적립유형코드		[null]
	private String orgApvDt; //원통합승인일자		[null]
	private String orgApvNo; //원통합승인번호		[null]
	private String orgUniqRcgnNo; //원관계사고유식별번호		[null]
	private String totSelAmt; //총매출금액||구매금액		[null]
	private String acmlTgtAmt; //포인트적립대상금액		[null]
	private String acmlPint; //적립포인트		[null]
	private String trscDt; //거래일자||관계사기준매출,결제일자-YYYYMMDD		[not null]
	private String trscHr; //거래시간||관계사기준매출,결제시간-HH24MISS		[not null]
	private String pointIfReqData; //포인트연동요청전문		[null]
	private String pointIfReturnData; //포인트연동수신전문		[null]
	private String trscTypCd; //거래유형코드		[null]
	private String trscBizDvCd; //업무구분코드		[null]


	public String getMmbCertDvName(){
			return CodeUtil.getCodeName("MMB_CERT_DV_CD", getMmbCertDvCd());
	}

	public String getPintUseTypName(){
			return CodeUtil.getCodeName("PINT_USE_TYP_CD", getPintUseTypCd());
	}

	public String getPintAcmlTypName(){
			return CodeUtil.getCodeName("PINT_ACML_TYP_CD", getPintAcmlTypCd());
	}

	public String getTrscTypName(){
			return CodeUtil.getCodeName("TRSC_TYP_CD", getTrscTypCd());
	}

	public String getTrscBizDvName(){
			return CodeUtil.getCodeName("TRSC_BIZ_DV_CD", getTrscBizDvCd());
	}
}