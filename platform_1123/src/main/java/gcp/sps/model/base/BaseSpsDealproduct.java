package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsDealmember;
import gcp.sps.model.SpsDealsaleproductprice;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealgroup;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDealproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String dealId; //딜ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal dealProductNo; //딜상품번호		[primary key, primary key, primary key, not null]
	private BigDecimal dealGroupNo; //상품그룹번호		[null]
	private String productId; //상품ID		[null]
	private String startDt; //딜시작일시		[null]
	private String endDt; //딜종료일시		[null]
	private BigDecimal totalDealStockQty; //총딜재고수량		[null]
	private BigDecimal dealStockQty; //잔여딜재고수량		[null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[not null]
	private String deliveryFeeFreeYn; //배송비무료여부		[not null]
	private String dealStateCd; //딜상태코드		[not null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<SpsDealmember> spsDealmembers;
	private List<SpsDealsaleproductprice> spsDealsaleproductprices;
	private PmsProduct pmsProduct;
	private SpsDeal spsDeal;
	private SpsDealgroup spsDealgroup;

	public String getDealStateName(){
			return CodeUtil.getCodeName("DEAL_STATE_CD", getDealStateCd());
	}
}