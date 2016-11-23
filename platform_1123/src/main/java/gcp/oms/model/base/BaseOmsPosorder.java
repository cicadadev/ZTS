package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsPosorderproduct;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPosorder extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private String memberNo; //회원번호		[null]
	private String cardIssueNo; //멤버십카드번호		[null]
	private String offshopId; //매장ID		[null]
	private BigDecimal productAmt; //상품금액		[not null]
	private BigDecimal tax; //부가세		[not null]
	private BigDecimal orderAmt; //주문금액||상품금액+부가세		[not null]
	private String orderDt; //주문일시		[not null]
	private BigDecimal usePoint; //사용포인트		[not null]
	private BigDecimal savePoint; //적립포인트		[not null]
	private BigDecimal availPoint; //가용포인트		[not null]

	private List<OmsPosorderproduct> omsPosorderproducts;
}