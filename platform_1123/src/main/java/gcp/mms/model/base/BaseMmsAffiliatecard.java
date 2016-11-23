package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsAffiliatecard extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String affiliatecardCd; //제휴카드코드		[primary key, primary key, primary key, not null]
	private String affiliatecardNo; //제휴카드인증번호		[primary key, primary key, primary key, not null]


	public String getAffiliatecardName(){
			return CodeUtil.getCodeName("AFFILIATECARD_CD", getAffiliatecardCd());
	}
}