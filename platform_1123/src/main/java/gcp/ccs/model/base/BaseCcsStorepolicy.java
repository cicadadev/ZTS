package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsPolicy;
import gcp.ccs.model.CcsStore;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsStorepolicy extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String policyId; //정책ID		[primary key, primary key, primary key, not null]
	private String value; //값		[not null]

	private CcsPolicy ccsPolicy;
	private CcsStore ccsStore;
}