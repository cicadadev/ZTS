package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsPopup;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsPopupurl extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal popupNo; //팝업번호		[primary key, primary key, primary key, not null]
	private BigDecimal popupUrlNo; //팝업공지URL번호		[primary key, primary key, primary key, not null]
	private String deviceChannelTypeCd; //디바이스채널유형코드		[not null]
	private String popupUrl; //팝업노출URL		[null]

	private CcsPopup ccsPopup;

	public String getDeviceChannelTypeName(){
			return CodeUtil.getCodeName("DEVICE_CHANNEL_TYPE_CD", getDeviceChannelTypeCd());
	}
}