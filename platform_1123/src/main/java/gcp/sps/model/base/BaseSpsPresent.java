package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsPresentdeal;
import gcp.sps.model.SpsPresentlang;
import gcp.sps.model.SpsPresentproduct;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsPresent extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String presentId; //사은품ID		[primary key, primary key, primary key, not null]
	private String name; //사은품프로모션명		[not null]
	private String presentTypeCd; //사은품유형코드		[not null]
	private String presentSelectTypeCd; //사은품선택유형코드		[not null]
	private BigDecimal selectQty; //선택가능수량		[null]
	private BigDecimal applyNo; //적용대상번호		[null]
	private BigDecimal controlNo; //적용제어번호		[null]
	private BigDecimal minOrderAmt; //최소주문금액		[null]
	private BigDecimal maxOrderAmt; //최대주문금액		[null]
	private String dealApplyYn; //딜적용가능여부		[not null]
	private String startDt; //사은품시작일시		[null]
	private String endDt; //사은품종료일시		[null]
	private String presentStateCd; //사은품상태코드		[not null]

	private List<SpsPresentdeal> spsPresentdeals;
	private List<SpsPresentlang> spsPresentlangs;
	private List<SpsPresentproduct> spsPresentproducts;
	private CcsApply ccsApply;
	private CcsControl ccsControl;

	public String getPresentTypeName(){
			return CodeUtil.getCodeName("PRESENT_TYPE_CD", getPresentTypeCd());
	}

	public String getPresentSelectTypeName(){
			return CodeUtil.getCodeName("PRESENT_SELECT_TYPE_CD", getPresentSelectTypeCd());
	}

	public String getPresentStateName(){
			return CodeUtil.getCodeName("PRESENT_STATE_CD", getPresentStateCd());
	}
}