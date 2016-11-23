package gcp.table.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.table.model.base.BaseTableSample;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class TableSample extends BaseTableSample {
}