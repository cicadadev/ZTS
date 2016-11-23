package gcp.table.model.base;

import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseTableSample extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String sampleId; //primary key, not null
	private String sampleName; //null

}