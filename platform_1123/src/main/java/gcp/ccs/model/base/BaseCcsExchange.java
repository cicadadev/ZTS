package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsExchange extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String currencyCd; //통화코드		[primary key, primary key, primary key, not null]
	private BigDecimal exchangeRate; //환율		[not null]


	public String getCurrencyName(){
			return CodeUtil.getCodeName("CURRENCY_CD", getCurrencyCd());
	}
}