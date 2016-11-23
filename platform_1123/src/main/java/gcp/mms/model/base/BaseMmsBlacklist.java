package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsBlacklist extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal blacklistNo; //블랙리스트번호		[primary key, primary key, primary key, not null]
	private String blacklistTypeCd; //블랙리스트유형코드		[null]
	private BigDecimal memberNo; //회원번호		[not null]
	private String startDt; //블랙리스트시작일시		[not null]
	private String endDt; //블랙리스트종료일시		[not null]
	private String blacklistReason; //블랙리스트사유		[null]
	private String blacklistStateCd; //블랙리스트상태코드		[null]

	private MmsMemberZts mmsMemberZts;

	public String getBlacklistTypeName(){
			return CodeUtil.getCodeName("BLACKLIST_TYPE_CD", getBlacklistTypeCd());
	}

	public String getBlacklistStateName(){
			return CodeUtil.getCodeName("BLACKLIST_STATE_CD", getBlacklistStateCd());
	}
}