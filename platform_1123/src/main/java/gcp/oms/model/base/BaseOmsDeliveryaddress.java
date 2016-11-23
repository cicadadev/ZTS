package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsOrder;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsDeliveryaddress extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal deliveryAddressNo; //배송지번호		[primary key, primary key, primary key, not null]
	private String name1; //수령인명1		[not null]
	private String name2; //수령인명2		[null]
	private String name3; //수령인명3		[null]
	private String name4; //수령인명4		[null]
	private String countryNo; //국가국번		[null]
	private String phone1; //전화번호1		[null]
	private String phone2; //전화번호2		[null]
	private String phone3; //전화번호3		[null]
	private String email; //이메일		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]
	private String note; //배송메모		[null]

	private List<OmsDelivery> omsDeliverys;
	private List<OmsOrderproduct> omsOrderproducts;
	private OmsOrder omsOrder;

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}
}