package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsWarehouselocation;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsWarehouse extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String warehouseId; //창고ID		[primary key, primary key, primary key, not null]
	private String name; //창고명		[not null]

	private List<PmsWarehouselocation> pmsWarehouselocations;
}