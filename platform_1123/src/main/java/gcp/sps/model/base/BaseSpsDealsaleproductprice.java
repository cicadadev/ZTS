package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsDealproduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDealsaleproductprice extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String dealId; //딜ID		[primary key, primary key, primary key, not null]
	private BigDecimal dealProductNo; //딜상품번호		[primary key, primary key, primary key, not null]
	private String saleproductId; //단품ID		[primary key, primary key, primary key, not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]

	private SpsDealproduct spsDealproduct;
}