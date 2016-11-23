package intune.gsf.model;

import lombok.Data;

@Data
public class TableClassColumnInfo {
	private String dbColumnName;
	private String classColumnName;
	private Object value;
	private String pkYn;
	private String maxPkYn;
	private String columnType;
}
