package gcp.mms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class MmsStyleSearch extends BaseSearchCondition {
	
	private BigDecimal memberNo;
	private String brandId;
	private String genderTypeCd;
	private String genderTypeName;
	private String themeCd;
	private String themeName;
	private String sortKeyword;		// 인기순:'popular', 최신순:'new'
	private String displayYn;
	private String styleNo;
	private String idShowYn;
}
