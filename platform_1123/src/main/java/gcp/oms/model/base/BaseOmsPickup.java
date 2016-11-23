package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsPickupproduct;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPickup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String pickupId; //매장픽업신청ID		[primary key, primary key, primary key, not null]
	private BigDecimal memberNo; //회원번호		[null]
	private String memberId; //회원ID		[null]
	private String name1; //주문자명1		[not null]
	private String name2; //주문자명2		[null]
	private String name3; //주문자명3		[null]
	private String name4; //주문자명4		[null]
	private String countryNo; //국가국번		[null]
	private String phone1; //전화번호1		[not null]
	private String phone2; //전화번호2		[null]
	private String phone3; //전화번호3		[null]
	private String pickupReqDt; //픽업신청일시		[null]

	private List<OmsPickupproduct> omsPickupproducts;
}