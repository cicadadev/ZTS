package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductreserve extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal productReserveNo; //상품변경예약번호		[primary key, primary key, primary key, not null]
	private String adCopy; //상품홍보문구		[null]
	private String saleStateCd; //판매상태코드		[not null]
	private String reserveDt; //예약일시		[not null]
	private String productReserveStateCd; //변경예약상태코드		[not null]
	private String completeDt; //변경반영일시		[null]

	private PmsProduct pmsProduct;

	public String getSaleStateName(){
			return CodeUtil.getCodeName("SALE_STATE_CD", getSaleStateCd());
	}

	public String getProductReserveStateName(){
			return CodeUtil.getCodeName("PRODUCT_RESERVE_STATE_CD", getProductReserveStateCd());
	}
}