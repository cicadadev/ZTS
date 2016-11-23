package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsPolicylang;
import gcp.ccs.model.CcsStorepolicy;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsPolicy extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String policyId; //정책ID		[primary key, primary key, primary key, not null]
	private String policyTypeCd; //정책구분코드		[not null]
	private String name; //정책명		[not null]
	private String value; //값		[not null]
	private String note; //설명		[null]

	private List<CcsPolicylang> ccsPolicylangs;
	private List<CcsStorepolicy> ccsStorepolicys;

	public String getPolicyTypeName(){
			return CodeUtil.getCodeName("POLICY_TYPE_CD", getPolicyTypeCd());
	}
}