package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsWarehouse;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsWarehouselocation extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String warehouseId; //창고ID		[primary key, primary key, primary key, not null]
	private String locationId; //로케이션ID		[primary key, primary key, primary key, not null]
	private String locationUseYn; //로케이션사용여부		[null]

	private List<PmsSaleproduct> pmsSaleproducts;
	private PmsWarehouse pmsWarehouse;
}