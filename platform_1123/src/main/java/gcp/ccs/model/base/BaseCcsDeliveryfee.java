package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsDeliverypolicy;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsDeliveryfee extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal deliveryPolicyNo; //배송정책번호		[primary key, primary key, primary key, not null]
	private String zipCd; //우편번호		[primary key, primary key, primary key, not null]
	private String deliveryYn; //배송가능여부		[null]
	private BigDecimal deliveryFee; //배송비		[null]
	private BigDecimal minDeliveryFreeAmt; //배송비무료최소주문금액		[null]

	private CcsDeliverypolicy ccsDeliverypolicy;

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}
}