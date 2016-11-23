package gcp.oms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsSettleSearch extends BaseSearchCondition {
	/** 매출원장 조회조건 **/
	//주문구분
	private String orderType;
	//사이트
	private String siteId;
	//정산유형
	private String settleType;
	//공급업체번호
	private String businessId;
	//공급업체명
	private String businessName;
	//ERP업체코드
	private String erpBusinessId;
	//상품번호
	private String productId;
	//단품번호
	private String saleproductId;
	//주문번호
	private String orderId;

	/** 정산내역(위탁) 조회조건 **/
	//매출기간 년도
	private String saleYear;
	//매출기간 월
	private String saleMonth;
	//매출기간 년월
	private String saleDate;

	/** 기타 **/
	//정산유형값
	private String settleTypeValue;
	//위탁매입여부
	private String purchaseYn;
	//딜유형
	private String dealTypeCd;
}
