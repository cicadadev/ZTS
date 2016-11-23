package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsStore;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsCountry extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String countryCd; //국가코드		[primary key, primary key, primary key, not null]
	private String name; //국가명		[not null]
	private String countryNo; //국가번호		[null]
	private String currencyUnit; //통화단위		[null]
	private String currencyUnitPrefixYn; //통화단위앞여부		[null]
	private String dateFormat; //일자포맷		[not null]
	private String datetimeFormat; //일시포맷		[not null]

	private List<CcsStore> ccsStores;

	public String getCountryName(){
			return CodeUtil.getCodeName("COUNTRY_CD", getCountryCd());
	}
}