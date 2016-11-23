package gcp.crm.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import java.sql.Date;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCrmMemberhistory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, not null]
	private String joinCd; //자매사구분		[null]
	private String joinStatus; //약관동의여부		[null]
	private Date createDate; //서비스가입동의일		[null]
	private Date updateDate; //서비스가입변경일		[null]
	private Date outDate; //서비스가입탈퇴일		[null]


	public String getJoinName(){
			return CodeUtil.getCodeName("JOIN_CD", getJoinCd());
	}
}