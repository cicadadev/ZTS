package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsCategory;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsCategoryattribute extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[primary key, primary key, primary key, not null]
	private String attributeId; //속성ID		[primary key, primary key, primary key, not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private PmsAttribute pmsAttribute;
	private PmsCategory pmsCategory;
}