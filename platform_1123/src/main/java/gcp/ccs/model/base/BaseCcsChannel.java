package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsControlchannel;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrder;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsStore;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsChannel extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String channelId; //채널ID		[primary key, primary key, primary key, not null]
	private String channelTypeCd; //채널유형코드		[not null]
	private String businessId; //업체ID		[null]
	private String businessChannelId; //업체채널ID		[null]
	private String name; //채널명		[not null]
	private String note; //설명		[null]
	private String pcUrl; //PCURL		[null]
	private String mobileUrl; //모바일URL		[null]
	private String channelStateCd; //채널상태코드		[not null]

	private List<CcsControlchannel> ccsControlchannels;
	private List<OmsCart> omsCarts;
	private List<OmsOrder> omsOrders;
	private CcsBusiness ccsBusiness;
	private CcsStore ccsStore;

	public String getChannelTypeName(){
			return CodeUtil.getCodeName("CHANNEL_TYPE_CD", getChannelTypeCd());
	}

	public String getChannelStateName(){
			return CodeUtil.getCodeName("CHANNEL_STATE_CD", getChannelStateCd());
	}
}