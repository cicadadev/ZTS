package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsStyle;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsStyleproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal styleNo; //스타일번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]

	private MmsStyle mmsStyle;
	private PmsProduct pmsProduct;
}