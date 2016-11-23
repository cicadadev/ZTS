package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsCodelang;
import gcp.ccs.model.CcsCodegroup;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsCode extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String cd; //코드		[primary key, primary key, primary key, not null]
	private String cdGroupCd; //코드그룹코드		[not null]
	private String name; //코드명		[not null]
	private String note; //설명		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<CcsCodelang> ccsCodelangs;
	private CcsCodegroup ccsCodegroup;

	public String getCdGroupName(){
			return CodeUtil.getCodeName("CD_GROUP_CD", getCdGroupCd());
	}
}