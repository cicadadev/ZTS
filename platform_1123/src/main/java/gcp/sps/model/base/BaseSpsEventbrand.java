package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsBrand;
import gcp.sps.model.SpsEvent;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventbrand extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[primary key, primary key, primary key, not null]

	private PmsBrand pmsBrand;
	private SpsEvent spsEvent;
}