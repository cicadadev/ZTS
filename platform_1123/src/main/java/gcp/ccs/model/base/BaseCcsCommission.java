package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusiness;
import gcp.pms.model.PmsCategory;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsCommission extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String businessId; //업체ID		[primary key, primary key, primary key, not null]
	private String commissionId; //수수료ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[null]
	private BigDecimal commissionRate; //수수료율		[not null]

	private CcsBusiness ccsBusiness;
	private PmsCategory pmsCategory;
}