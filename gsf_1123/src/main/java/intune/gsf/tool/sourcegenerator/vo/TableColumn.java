package intune.gsf.tool.sourcegenerator.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class TableColumn {

	private String	tableName;
	private String	columnName;
	private String	columnType;
	private String	pkCol;
	private String	comments;
	private String	nullable;
	private String	length;

}
