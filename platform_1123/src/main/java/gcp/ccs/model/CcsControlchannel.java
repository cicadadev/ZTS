package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsControlchannel;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsControlchannel extends BaseCcsControlchannel {
	private String name; //채널명		[not null]
}