package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsOffshop;
import gcp.dms.model.DmsExhibit;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibitoffshop extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private String offshopId; //매장ID		[primary key, primary key, primary key, not null]

	private CcsOffshop ccsOffshop;
	private DmsExhibit dmsExhibit;
}