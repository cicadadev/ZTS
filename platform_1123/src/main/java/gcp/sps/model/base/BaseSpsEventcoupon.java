package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsEvent;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventcoupon extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private String couponId; //쿠폰ID		[primary key, primary key, primary key, not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private SpsCoupon spsCoupon;
	private SpsEvent spsEvent;
}