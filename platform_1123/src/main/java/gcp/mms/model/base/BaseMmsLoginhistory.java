package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsLoginhistory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String loginDt; //로그인일시		[primary key, primary key, primary key, not null]
	private String deviceTypeCd; //디바이스유형코드		[null]
	private String mobileOsTypeCd; //모바일OS유형코드		[null]
	private String appVersion; //앱버전		[null]

	private MmsMemberZts mmsMemberZts;

	public String getDeviceTypeName(){
			return CodeUtil.getCodeName("DEVICE_TYPE_CD", getDeviceTypeCd());
	}

	public String getMobileOsTypeName(){
			return CodeUtil.getCodeName("MOBILE_OS_TYPE_CD", getMobileOsTypeCd());
	}
}