package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsAddress extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal addressNo; //배송지번호		[primary key, primary key, primary key, not null]
	private String name; //배송지명		[null]
	private String deliveryName1; //수령인명1		[not null]
	private String deliveryName2; //수령인명2		[null]
	private String deliveryName3; //수령인명3		[null]
	private String deliveryName4; //수령인명4		[null]
	private String countryNo; //국가국번		[null]
	private String phone1; //전화번호1		[null]
	private String phone2; //전화번호2		[null]
	private String phone3; //전화번호3		[null]
	private String zipCd; //우편번호		[not null]
	private String address1; //주소1		[not null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]

	private List<MmsMemberZts> mmsMemberZtss;
	private MmsMemberZts mmsMemberZts;

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}
}