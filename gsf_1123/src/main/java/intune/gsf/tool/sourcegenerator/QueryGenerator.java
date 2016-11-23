package intune.gsf.tool.sourcegenerator;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.tool.sourcegenerator.vo.TableColumn;

public class QueryGenerator {

	public QueryGenerator() {
		jdbcprop = new Properties();
		try {
			FileInputStream jdbcfis = new FileInputStream(jdbcFile);
			jdbcprop.loadFromXML(jdbcfis);
			url = jdbcprop.getProperty("jdbc.url");
			dbAccount = jdbcprop.getProperty("jdbc.system.username");
			dbPassword = jdbcprop.getProperty("jdbc.system.password");
			tableOwner = jdbcprop.getProperty("jdbc.system.owner");
			tableInfoQuery = "SELECT COLUMN_NAME, DATA_TYPE" + " FROM ALL_TAB_COLUMNS"
					+ " WHERE OWNER = '" + tableOwner + "' AND TABLE_NAME = '__TABLE_NAME__'";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Properties		jdbcprop		= null;

	private String	url				= null;

	private String	dbAccount		= null;
	private String	dbPassword		= null;

	private String	tableOwner		= null;

	private String	tableInfoQuery	= null;

	private static String	projectDir		= "C:\\ZTS\\project";
	private static String	jdbcFile		= projectDir + "\\gcp2.0_platform\\src\\main\\resources\\config\\system\\jdbc.xml";

	public static void main(String[] args) {
		String tableName = "PMS_PRODUCT";

		QueryGenerator q = new QueryGenerator();

		List<TableColumn> list = q.getTableInfo(tableName);
		q.makeInsertQuery(tableName, list);
	}

	private void makeInsertQuery(String tableName, List<TableColumn> list) {

		String query = "INSERT INTO " + tableName + " (\n";

		for (int i = 0; i < list.size(); i++) {

			String comma = ",";

			if (list.size() == i + 1) {
				comma = ")";
			}

			query += "\t\t\t" + list.get(i).getColumnName() + comma + "\n";
		}
		query += "\t\tVALUES (\n";
		for (int i = 0; i < list.size(); i++) {

			String comma = ",";

			if (list.size() == i + 1) {
				comma = ")";
			}

			String type = list.get(i).getColumnType();
			String jdbcType = "";
			if (type.contains("NUMBER")) {
				jdbcType = "NUMERIC";
			} else if (type.contains("VARCHAR2")) {
				jdbcType = "VARCHAR";
			} else if (type.contains("TIMESTAMP")) {
				jdbcType = "VARCHAR";
			}
			String mybatisColumn = "#{" + CommonUtil.convertCamel(list.get(i).getColumnName(), false) + ", jdbcType=" + jdbcType
					+ "}";
			if ("INS_DT".equals(list.get(i).getColumnName()) || "UPD_DT".equals(list.get(i).getColumnName())) {
				mybatisColumn = "SYSDATE";
			}
			query += "\t\t\t" + mybatisColumn + comma + "\n";
		}
		System.out.println(query);
	}

	private List<TableColumn> getTableInfo(String tableName) {

		tableInfoQuery = tableInfoQuery.replace("__TABLE_NAME__", tableName);

		List<TableColumn> list = new ArrayList<TableColumn>();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // JDBC 드라이버 로드
			conn = DriverManager.getConnection(url, dbAccount, dbPassword); // 데이터베이스 연결(id/pw)
			if (conn == null) {
				System.out.println("fail");
			} else {

				// 출력 준비
				stmt = conn.createStatement();

				pstmt = conn.prepareStatement(tableInfoQuery);
				rs = pstmt.executeQuery();

				rs = stmt.executeQuery(tableInfoQuery);

				while (rs.next()) {

					TableColumn tableColumn = new TableColumn();
					tableColumn.setColumnName(rs.getString(1));
					tableColumn.setColumnType(rs.getString(2));

					list.add(tableColumn);
				}
			}
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { // 연결 해제(한정돼 있으므로)
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
		}

		return list;
	}
}
