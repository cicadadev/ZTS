package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsPresent;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsPresentproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String presentId; //사은품ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]

	private PmsProduct pmsProduct;
	private SpsPresent spsPresent;
}