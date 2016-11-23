package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsSaleproductpricereserve;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsPricereserve extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal priceReserveNo; //가격변경예약번호		[primary key, primary key, primary key, not null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal regularDeliveryPrice; //정기배송가		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private String priceReserveStateCd; //변경신청상태코드		[not null]
	private String rejectReason; //반려사유		[null]
	private String reqDt; //신청일시		[not null]
	private String reserveDt; //예약일시		[not null]
	private String approvalDt; //승인일시		[null]
	private String completeDt; //변경반영일시		[null]

	private List<PmsSaleproductpricereserve> pmsSaleproductpricereserves;
	private PmsProduct pmsProduct;

	public String getPriceReserveStateName(){
			return CodeUtil.getCodeName("PRICE_RESERVE_STATE_CD", getPriceReserveStateCd());
	}
}