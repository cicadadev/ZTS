package intune.gsf.tool.databackup.vo;

import java.util.ArrayList;
import java.util.List;

public class QueryModel {

	private String			tableName;
	private String			querys;
	private String			parentTables;

	private List<String>	parentTableList	= new ArrayList<String>();

	
	
	public String getQuerys() {
		return querys;
	}

	public void setQuerys(String querys) {
		this.querys = querys;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getParentTables() {
		return parentTables;
	}

	public void setParentTables(String parentTables) {

		if (parentTables != null && parentTables.length() > 0) {
			String[] arry = parentTables.split(",");
			List<String> list = new ArrayList<String>();
			for (String p : arry) {
				list.add(p);
			}
			this.setParentTableList(list);
		}
		this.parentTables = parentTables;

	}

	public List<String> getParentTableList() {
		return parentTableList;
	}

	public void setParentTableList(List<String> parentTableList) {
		this.parentTableList = parentTableList;
	}

}
