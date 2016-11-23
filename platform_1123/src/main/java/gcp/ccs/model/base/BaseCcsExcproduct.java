package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsApply;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsExcproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal applyNo; //적용대상번호		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]

	private CcsApply ccsApply;
	private PmsProduct pmsProduct;
}