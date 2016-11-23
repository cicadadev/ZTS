package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsDiscountlang;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDiscount extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String discountId; //할인ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String name; //할인명		[not null]
	private String discountTypeCd; //할인유형코드		[not null]
	private String couponShowYn; //즉시할인쿠폰노출여부		[not null]
	private BigDecimal applyNo; //적용대상번호		[null]
	private BigDecimal controlNo; //적용제어번호		[null]
	private String dcApplyTypeCd; //할인적용유형코드		[not null]
	private BigDecimal dcValue; //할인적용값		[not null]
	private BigDecimal maxDcAmt; //최대할인금액		[null]
	private String startDt; //할인시작일시		[null]
	private String endDt; //할인종료일시		[null]
	private String discountStateCd; //할인상태코드		[not null]

	private List<SpsDiscountlang> spsDiscountlangs;
	private CcsApply ccsApply;
	private CcsControl ccsControl;

	public String getDiscountTypeName(){
			return CodeUtil.getCodeName("DISCOUNT_TYPE_CD", getDiscountTypeCd());
	}

	public String getDcApplyTypeName(){
			return CodeUtil.getCodeName("DC_APPLY_TYPE_CD", getDcApplyTypeCd());
	}

	public String getDiscountStateName(){
			return CodeUtil.getCodeName("DISCOUNT_STATE_CD", getDiscountStateCd());
	}
}