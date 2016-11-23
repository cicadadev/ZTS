package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsControlchannel;
import gcp.ccs.model.CcsControldevice;
import gcp.ccs.model.CcsControlmembergrade;
import gcp.ccs.model.CcsControlmembertype;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.DmsExhibit;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDiscount;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.SpsPresent;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsControl extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal controlNo; //제어번호		[primary key, primary key, primary key, not null]
	private String channelControlCd; //채널제어코드		[not null]

	private List<CcsControlchannel> ccsControlchannels;
	private List<CcsControldevice> ccsControldevices;
	private List<CcsControlmembergrade> ccsControlmembergrades;
	private List<CcsControlmembertype> ccsControlmembertypes;
	private List<DmsDisplayitem> dmsDisplayitems;
	private List<DmsExhibit> dmsExhibits;
	private List<PmsProduct> pmsProducts;
	private List<SpsCoupon> spsCoupons;
	private List<SpsDeal> spsDeals;
	private List<SpsDiscount> spsDiscounts;
	private List<SpsEvent> spsEvents;
	private List<SpsPointsave> spsPointsaves;
	private List<SpsPresent> spsPresents;
	private CcsStore ccsStore;

	public String getChannelControlName(){
			return CodeUtil.getCodeName("CHANNEL_CONTROL_CD", getChannelControlCd());
	}
}