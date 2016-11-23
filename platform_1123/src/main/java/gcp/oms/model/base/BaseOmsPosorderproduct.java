package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsPosorder;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPosorderproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private String erpSaleproductId; //ERP단품ID		[primary key, primary key, primary key, not null]
	private String taxType; //과세구분		[null]
	private String brandName; //브랜드명		[null]
	private String productName; //상품명		[not null]
	private BigDecimal salePrice; //단가		[not null]
	private BigDecimal orderQty; //수량		[not null]
	private BigDecimal orderAmt; //금액||판매가*수량		[not null]

	private OmsPosorder omsPosorder;
}