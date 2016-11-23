package intune.gsf.tool.sourcegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.tool.sourcegenerator.vo.TableColumn;

public class XmlQueryGenerator {

	public XmlQueryGenerator() {
		jdbcprop = new Properties();
		try {
			FileInputStream jdbcfis = new FileInputStream(jdbcFile);
			jdbcprop.loadFromXML(jdbcfis);
			url = jdbcprop.getProperty("jdbc.url");
			dbAccount = jdbcprop.getProperty("jdbc.system.username");
			dbPassword = jdbcprop.getProperty("jdbc.system.password");
			tableOwner = jdbcprop.getProperty("jdbc.system.owner");
			tableInfoQuery = "SELECT I1.*, C.COLUMN_NAME, C.DATA_TYPE,"
					+ "  C.DATA_LENGTH, C.NULLABLE, D.COMMENTS FROM (  SELECT T.TABLE_NAME, T.OWNER,"
					+ " LISTAGG (B.COLUMN_NAME, ',') WITHIN GROUP (ORDER BY B.POSITION)  PK_COL"
					+ " FROM ALL_TABLES T, DBA_CONSTRAINTS A, DBA_CONS_COLUMNS B WHERE T.TABLE_NAME = A.TABLE_NAME"
					+ " AND T.TABLE_NAME = B.TABLE_NAME AND A.CONSTRAINT_NAME = B.CONSTRAINT_NAME"
					+ " AND CONSTRAINT_TYPE = 'P' AND T.OWNER = '" + tableOwner + "' AND T.OWNER=B.OWNER AND T.OWNER=B.OWNER"
					+ " GROUP BY T.TABLE_NAME, T.OWNER) I1, ALL_TAB_COLUMNS C, ALL_COL_COMMENTS D"
					+ " WHERE I1.TABLE_NAME = C.TABLE_NAME AND I1.OWNER = C.OWNER AND C.TABLE_NAME = D.TABLE_NAME"
					+ " AND C.OWNER = D.OWNER AND C.COLUMN_NAME = D.COLUMN_NAME " + "ORDER BY I1.TABLE_NAME, C.COLUMN_ID";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Properties								jdbcprop		= null;

	private static String							projectDir		= "C:\\ZTS\\project";
	private static String							platformDir		= projectDir + "\\gcp2.0_platform\\src\\main";
	private static String							targetDir		= platformDir + "\\java\\gcp\\";
	private static String							jdbcFile		= projectDir
			+ "\\gcp2.0_platform\\src\\main\\resources\\config\\system\\jdbc.xml";

	private static String[]							PK_MAX_POSTFIX	= { "_NO", "_ID" };
	private String									url				= null;
	private String									dbAccount		= null;
	private String									dbPassword		= null;
	private String									tableOwner		= null;

	private String									tableInfoQuery	= "";

	// 테이블컬럼정보
	private static Map<String, List<TableColumn>>	tmap			= new LinkedHashMap<String, List<TableColumn>>();

	/**
	 * 테이블 컬럼 정보 조회
	 * 
	 * @param conn
	 * @param stmt
	 * @param pstmt
	 * @param rs
	 * @throws SQLException
	 */
	private void getTableColumnInfo(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {

		// 출력 준비
		stmt = conn.createStatement();

		System.out.println(tableInfoQuery);

		pstmt = conn.prepareStatement(tableInfoQuery);
//		pstmt.setString(1, "7934");
		rs = pstmt.executeQuery();

		rs = stmt.executeQuery(tableInfoQuery);

		List<TableColumn> list = new ArrayList<TableColumn>();

		String currentTableName = null;
		String prevTableName = null;

		while (rs.next()) {

			TableColumn tableColumn = new TableColumn();
			tableColumn.setTableName(rs.getString(1));
			tableColumn.setPkCol(rs.getString(3));
			tableColumn.setColumnName(rs.getString(4));
			tableColumn.setColumnType(rs.getString(5));
			tableColumn.setLength(rs.getString(6));
			tableColumn.setNullable(rs.getString(7));
			tableColumn.setComments(rs.getString(8));

			currentTableName = rs.getString(1);

			if (currentTableName.equals(prevTableName)) {
				//현재 테이블
				list.add(tableColumn);

			} else {
				//새테이블
				list = new ArrayList<TableColumn>();
				list.add(tableColumn);
			}

			tmap.put(currentTableName, list);

			System.out.println(
					"" + tableColumn.getTableName() + "||" + tableColumn.getColumnName() + "||" + tableColumn.getColumnType());

			prevTableName = currentTableName;

		}

	}

	/**
	 * 테이블 정보 조회
	 */
	private void getTableInfos() {

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

				//테이블 정보
				getTableColumnInfo(conn, stmt, pstmt, rs);

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

	}

//	public void getInsertMaxSelectStr(String tableName, List<TableClassColumnInfo> fields, Map<String, String> map)
//			throws Exception {
//		String whereStr = "";
//		String maxColumn = "";
//		String maxClassColumn = "";
//		String selectMaxPlusOne = "";
//		String selectMax = "";
//
//		List<TableColumn> columns = tmap.get(tableName);
//
//		String[] pkList = SchemeUtil.getPkList(tableName);
//		
//		pkList.
//		
//		for (TableColumn column : columns) {
//			if (CommonUtil.isEmpty(whereStr) && pkList.) {
//				whereStr += " WHERE " + column.getColumnName() + " = ''";
//			} else if ("Y".equals(column.getPkYn()) && !SchemeUtil.isEmptyObject(column.getValue())) {
//				whereStr += " AND " + column.getDbColumnName() + " = ''";
//			} else if ("Y".equals(column.getMaxPkYn())) {
//				maxColumn = column.getDbColumnName();
//				maxClassColumn = column.getClassColumnName();
//			}
//		}
//		if (CommonUtil.isNotEmpty(maxColumn)) {
//			if (maxColumn.endsWith(PK_MAX_POSTFIX[1])) {
//				maxColumn = "TO_NUMBER(" + maxColumn + ")";
//			}
//			selectMaxPlusOne = "SELECT NVL (MAX (" + maxColumn + ") + 1, 1) FROM " + tableName + whereStr;
//			selectMax = "SELECT MAX (" + maxColumn + ") as " + maxClassColumn + " FROM " + tableName + whereStr;
//		}
//		// System.out.println(">>>>>>>>>>>>>> query:::: " + maxPlusOneQuery);
//		map.put("key", maxClassColumn);
//		map.put("keyName", maxClassColumn);
//		map.put("selectMax", selectMax);
//		map.put("selectMaxPlusOne", selectMaxPlusOne);
////		return selectMaxPlusOne;
//	}

	public StringBuffer getUpdateQueryStr(String tableName) throws Exception {
		StringBuffer bf_all = new StringBuffer();

		bf_all.append("\n<update id=\"" + CommonUtil.convertCamel(tableName, true) + "\">");

		bf_all.append("\nUPDATE " + tableName + " SET ");

		String setStr = BaseConstants.DB_UPDATE_DT + " = SYSDATE";
		String whereStr = "";

		List<TableColumn> columns = tmap.get(tableName);

		for (TableColumn tableCol : columns) {

			String jdbyType = "";

			if (tableCol.getColumnType().indexOf("NUMBER") >= 0) {
				jdbyType = "NUMERIC";
			} else if (tableCol.getColumnType().indexOf("CLOB") >= 0) {
				jdbyType = "CLOB";
			} else {
				jdbyType = "VARCHAR";
			}

			String bindColumn = CommonUtil.convertCamel(tableCol.getColumnName(), false);

			if (BaseConstants.DB_INSERT_DT.equals(tableCol.getColumnName())
					|| BaseConstants.DB_INSERT_ID.equals(tableCol.getColumnName())) {
				continue;
			}

			String comma = "";
			if (CommonUtil.isNotEmpty(setStr)) {
				comma += ",";
			}


			if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getColumnName())) {// 수정일
				continue;

			} else if (tableCol.getPkCol().indexOf(tableCol.getColumnName()) == -1) {// PK가 아닌컬럼 set

				
				if (tableCol.getColumnType().indexOf("NUMBER") >= 0) {
					setStr += "<choose><when test='" + bindColumn + " eq \"9223372036854775807\"'>,"
							+ tableCol.getColumnName() + " = null</when><when test=\"" + bindColumn + "!=null\">,"
							+ tableCol.getColumnName() + " = #{" + bindColumn + ", jdbcType=NUMERIC}</when></choose>";
				}
				else if (tableCol.getColumnType().indexOf("TIMESTAMP") >= 0) {
					// 날짜 컬럼일경우 TO_DATE()처리

					setStr += "<if test=\"" + bindColumn + "!=null and " + bindColumn + "!=''\">" + comma
							+ tableCol.getColumnName()
							+ " = TO_DATE(#{" + bindColumn + "}, 'YYYY-MM-DD HH24:MI:SS')</if><if test=\"" + bindColumn
							+ "==''\"> " + comma + tableCol.getColumnName() + " = null</if>";

//					setStr += tableCol.getColumnName() + " = TO_DATE(#{" + bindColumn + "}, 'YYYY-MM-DD HH24:MI:SS')";
				} else {
					// 일반 컬럼들
					
					setStr += "<if test=\"" + bindColumn + "!=null\">" + comma + tableCol.getColumnName() + " = #{" + bindColumn
							+ ", jdbcType=" + jdbyType + "}</if>";
					
//					setStr += tableCol.getColumnName() + " = #{" + bindColumn + ", jdbcType=" + jdbyType + "}";
				}

			} else {// pk컬럼은 where문

				if (CommonUtil.isNotEmpty(whereStr)) {
					whereStr += " AND ";
				}
				whereStr += tableCol.getColumnName() + " = #{" + bindColumn + ", jdbcType=" + jdbyType + "}";
			}

		}

		bf_all.append(setStr + " WHERE " + whereStr);
		bf_all.append("\n</update>");
		return bf_all;
	}
	public StringBuffer getInsertQueryStr(String tableName) throws Exception {

//		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);
//
//		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
//		String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환
//		SchemeUtil.getInsertMaxSelectStr(tableName, fields, map);	// max, max+1 query

		StringBuffer bf_all = new StringBuffer();

		bf_all.append("\n<insert id=\"" + CommonUtil.convertCamel(tableName, true) + "\">");
		String columnStr = "";
		String values = "";

		bf_all.append("\nINSERT INTO " + tableName + " (");

		List<TableColumn> columns = tmap.get(tableName);

		for (TableColumn tableCol : columns) {

			String camelColumnName = CommonUtil.convertCamel(tableCol.getColumnName(), false);
			String jdbyType = "";

			if (tableCol.getColumnType().indexOf("NUMBER") >= 0) {
				jdbyType = "NUMERIC";
			} else if (tableCol.getColumnType().indexOf("CLOB") >= 0) {
				jdbyType = "CLOB";
			} else {
				jdbyType = "VARCHAR";
			}

			// insert 문의 컬럼값 영역
			String tableColumnName = tableCol.getColumnName();

			if (CommonUtil.isEmpty(columnStr)) {
				columnStr += tableColumnName;
			} else {
				columnStr += "," + tableColumnName;
			}

			if (!CommonUtil.isEmpty(values)) {
				values += ",";
			}

			// insert 문의 values 영역 생성
			if (BaseConstants.DB_INSERT_DT.equals(tableCol.getColumnName())) {// 등록일
				values += "SYSDATE";
			} else if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getColumnName())) {// 수정일
				values += "SYSDATE";
			} else if (tableCol.getColumnType().indexOf("TIMESTAMP") >= 0) {
				values += "TO_DATE(#{" + camelColumnName + "}, 'YYYY-MM-DD HH24:MI:SS')";
			} else {
				values += "#{" + camelColumnName + ", jdbcType=" + jdbyType + "}";
			}
		}
		bf_all.append(columnStr + ") VALUES (" + values + ") \n</insert>");
//		System.out.println(bf_all.toString());

		return bf_all;

	}

	private void makeFile(StringBuffer bf_all, String queryType) {

//		String pakageName = getPackageName();

//		String dir = targetDir + pakageName + "\\model";
		String dir = targetDir + "\\ccs\\sql";

		try {

			File file = new File(dir);

			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = "common." + queryType + ".xml";
			String fullpath = dir + "\\" + fileName;

			//makeTypeAlias(pkgPath, fileName);

			File file2 = new File(fullpath);

			if (file2.exists()) {
				file2.delete();
			}

			FileWriter fw = new FileWriter(file2, true);

			fw.write(bf_all.toString());
			fw.flush();

			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void makeInsert() throws Exception {
		//테이블 정보 조회
		getTableInfos();
		StringBuffer bf_all = new StringBuffer();
		bf_all.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bf_all.append(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		bf_all.append("<mapper namespace=\"common.insert\">\n");

		bf_all.append("\n<select id=\"max\" resultType=\"String\">");
		bf_all.append("${maxPlusOneQry}");
		bf_all.append("</select>\n");

		for (String key : tmap.keySet()) {
			bf_all.append(getInsertQueryStr(key));
		}
		bf_all.append("\n</mapper>");
		makeFile(bf_all, "insert");
	}

	public void makeUpdate() throws Exception {
		//테이블 정보 조회
		getTableInfos();
		StringBuffer bf_all = new StringBuffer();
		bf_all.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		bf_all.append(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		bf_all.append("<mapper namespace=\"common.update\">\n");

		for (String key : tmap.keySet()) {
			bf_all.append(getUpdateQueryStr(key));
		}
		bf_all.append("\n</mapper>");
		makeFile(bf_all, "update");
	}

	public static void main(String[] args) throws Exception {
		XmlQueryGenerator gen = new XmlQueryGenerator();
		gen.makeInsert();
		gen.makeUpdate();
	}

}