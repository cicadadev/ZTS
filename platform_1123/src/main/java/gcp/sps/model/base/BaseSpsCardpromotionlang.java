package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsCardpromotion;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsCardpromotionlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal cardPromotionNo; //카드사할인번호		[primary key, primary key, primary key, not null]
	private String landCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //카드사할인명		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]

	private SpsCardpromotion spsCardpromotion;

	public String getLandName(){
			return CodeUtil.getCodeName("LAND_CD", getLandCd());
	}
}