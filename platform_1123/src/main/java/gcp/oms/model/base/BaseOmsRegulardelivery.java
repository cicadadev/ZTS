package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsRegulardeliveryproduct;
import gcp.mms.model.MmsMember;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsRegulardelivery extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String regularDeliveryId; //정기배송신청ID		[primary key, primary key, primary key, not null]
	private BigDecimal memberNo; //회원번호		[not null]
	private String memberId; //회원ID		[null]
	private String name1; //주문자명1		[not null]
	private String name2; //주문자명2		[null]
	private String name3; //주문자명3		[null]
	private String name4; //주문자명4		[null]
	private String countryNo; //국가국번		[null]
	private String phone1; //전화번호1		[null]
	private String phone2; //전화번호2		[null]
	private String phone3; //전화번호3		[null]
	private String deliveryName1; //수령인명1		[null]
	private String deliveryName2; //수령인명2		[null]
	private String deliveryName3; //수령인명3		[null]
	private String deliveryName4; //수령인명4		[null]
	private String deliveryCountryNo; //수령인국가국번		[null]
	private String deliveryPhone1; //수령인전화번호1		[null]
	private String deliveryPhone2; //수령인전화번호2		[null]
	private String deliveryPhone3; //수령인전화번호3		[null]
	private String deliveryZipCd; //수령인우편번호		[null]
	private String deliveryAddress1; //수령인주소1		[null]
	private String deliveryAddress2; //수령인주소2		[null]
	private String deliveryAddress3; //수령인주소3		[null]
	private String deliveryAddress4; //수령인주소4		[null]
	private String note; //배송메모		[null]

	private List<OmsRegulardeliveryproduct> omsRegulardeliveryproducts;
	private MmsMember mmsMember;

	public String getDeliveryZipName(){
			return CodeUtil.getCodeName("DELIVERY_ZIP_CD", getDeliveryZipCd());
	}
}