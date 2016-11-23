package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsPricereserve;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsSaleproductpricereserve extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal priceReserveNo; //가격변경예약번호		[primary key, primary key, primary key, not null]
	private String saleproductId; //단품ID		[primary key, primary key, primary key, not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]

	private PmsPricereserve pmsPricereserve;
	private PmsSaleproduct pmsSaleproduct;
}