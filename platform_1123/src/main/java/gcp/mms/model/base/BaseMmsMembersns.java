package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMember;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsMembersns extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal snsMemberNo; //결합SNS회원번호		[primary key, primary key, primary key, not null]
	private String snsChannelCd; //SNS채널코드		[not null]
	private String useYn; //사용여부		[not null]

	private MmsMember mmsMember;

	public String getSnsChannelName(){
			return CodeUtil.getCodeName("SNS_CHANNEL_CD", getSnsChannelCd());
	}
}