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
public class BaseMmsMemberbaby extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal babyNo; //아기번호		[primary key, primary key, primary key, not null]
	private String babyName; //아기명		[null]
	private String babyOrder; //몇째아이순서		[null]
	private String twinYn; //쌍둥이여부		[not null]
	private String birthday; //아기생일		[null]
	private String lunarYn; //생일음력여부		[null]
	private String babyGenderCd; //자녀성별코드		[not null]
	private String feedTypeCd; //수유형태코드		[null]
	private String useYn; //사용여부		[null]

	private MmsMember mmsMember;

	public String getBabyGenderName(){
			return CodeUtil.getCodeName("BABY_GENDER_CD", getBabyGenderCd());
	}

	public String getFeedTypeName(){
			return CodeUtil.getCodeName("FEED_TYPE_CD", getFeedTypeCd());
	}
}