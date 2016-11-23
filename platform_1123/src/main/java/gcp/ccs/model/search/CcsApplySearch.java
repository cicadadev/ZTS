package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsApplySearch extends BaseSearchCondition {
	private String		storeId;
	private String		applyNo;
	private String		targetTypeCd;

	// 적용대상 중복체크
	private String[]	idArray;
	private String		applyType;
}
