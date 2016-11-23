package gcp.sps.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

import gcp.sps.model.base.BaseSpsCoupon;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsCouponCommonCode {
	
	private String id;
	private String name;
	private String value;
	
	
}