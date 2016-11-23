package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsControlchannel extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal controlNo; //제어번호		[primary key, primary key, primary key, not null]
	private String channelId; //채널ID		[primary key, primary key, primary key, not null]

	private CcsChannel ccsChannel;
	private CcsControl ccsControl;
}