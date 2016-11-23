package gcp.pms.model.erp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsErpApxItemmapping {
	private String	itemId;
	private String	exOptionid;
	private String	exItemid;
	private String	unitid;
	private String	inventsizeid;
	private String	inventcolorid;
	private String	exUnitid;
	private String	exItemname;
	private int	registyn;
	private String	modifieddatetime;
	private String	modifiedby;
	private String	createdatetime;
	private String	createdby;
	private String	dataareaid;
	private int	recversion;
	private int	recid;
}