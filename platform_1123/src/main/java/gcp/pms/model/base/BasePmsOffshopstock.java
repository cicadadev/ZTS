package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsOffshopstock extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String saleproductId; //단품ID		[primary key, primary key, primary key, not null]
	private String offshopId; //매장ID		[primary key, primary key, primary key, not null]
	private BigDecimal safeStockQty; //안전재고수량		[not null]
	private BigDecimal realStockQty; //실재고수량		[not null]

	private PmsSaleproduct pmsSaleproduct;
}