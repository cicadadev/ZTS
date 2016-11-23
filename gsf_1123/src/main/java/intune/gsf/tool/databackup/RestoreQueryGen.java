package intune.gsf.tool.databackup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import intune.gsf.tool.databackup.vo.QueryModel;

public class RestoreQueryGen {

	public RestoreQueryGen() throws IOException {
	}

	static String				url				= "jdbc:oracle:thin:@192.2.72.104:1521:ORCL";																																																																																																																																 // url 형

	static String				dbAccount		= "ZEROTO7";
	static String				dbPassword		= "ZEROTO7PW";

	static String				tableOwner		= "ZEROTO7";

//	백업 쿼리
//	SELECT    'CREATE TABLE '
//	       || TABLE_NAME
//	       || '_C AS SELECT * FROM '
//	       || TABLE_NAME
//	       || ';'
//	  FROM ALL_TABLES
//	 WHERE OWNER = 'ZEROTO7' AND TABLE_NAME != 'TOAD_PLAN_TABLE'

	private static String		tableInfoQuery	=
	"  SELECT I1.TABLE_NAME, I2.PARENT_TABLE, NVL (I2.PARENT_TABLE, 0) ORDERBY     "
	+"    FROM ALL_TABLES I1,                                                       "
	+"         (  SELECT CONS.TABLE_NAME AS CHILD_TABLE,                            "
	+"                   WM_CONCAT (DISTINCT COL.TABLE_NAME)PARENT_TABLE            "
	+"              FROM DBA_CONS_COLUMNS COL, DBA_CONSTRAINTS CONS                 "
	+"             WHERE     CONS.R_OWNER = COL.OWNER                               "
	+"                   AND CONS.R_CONSTRAINT_NAME = COL.CONSTRAINT_NAME           "
	+"                   AND COL.OWNER = 'ZEROTO7'                                  "
	+"                   AND COL.TABLE_NAME NOT LIKE '%_C' and  col.table_name <> CONS.table_name                         "
	+"          GROUP BY CONS.TABLE_NAME) I2                                        "
	+"   WHERE     I1.TABLE_NAME = I2.CHILD_TABLE(+)                                "
	+"         AND I1.TABLE_NAME NOT LIKE '%_C'                                     "
	+"         AND I1.OWNER = 'ZEROTO7'                                             "
	+"         AND I1.TABLE_NAME <> 'TOAD_PLAN_TABLE'                               "
	+" ORDER BY ORDERBY DESC, I1.TABLE_NAME ASC                                      ";
	
	
	
	private static String insertQuery = 
					"				SELECT TABLE_NAME,                                            "
					+"		          'INSERT INTO '                                              "
					+"		       || TABLE_NAME                                                  "
					+"		       || ' ('                                                        "
					+"		       || COLUMNSS                                                    "
					+"		       || ')  SELECT '                                                "
					+"		       || COLUMNSS                                                    "
					+"		       || ' FROM '                                                    "
					+"		       || TABLE_NAME                                                  "
					+"		       || '_C;'                                                       "
					+"		          QUERY                                                       "
					+"		  FROM (  SELECT I1.TABLE_NAME,                                       "
					+"		                 LISTAGG (I1.COLUMN_NAME, ',')                        "
					+"		                    WITHIN GROUP (ORDER BY I1.COLUMN_NAME)            "
					+"		                    COLUMNSS                                          "
					+"		            FROM (  SELECT TABLE_NAME, COLUMN_NAME                    "
					+"		                      FROM ALL_TAB_COLUMNS                            "
					+"		                     WHERE     OWNER = 'ZEROTO7'                      "
					+"		                           AND TABLE_NAME <> 'TOAD_PLAN_TABLE'        "
					+"		                           AND TABLE_NAME NOT LIKE '%_C'              "
					+"		                  ORDER BY TABLE_NAME, COLUMN_NAME) I1,               "
					+"		                 (  SELECT TABLE_NAME, COLUMN_NAME                    "
					+"		                      FROM ALL_TAB_COLUMNS                            "
					+"		                     WHERE     OWNER = 'ZEROTO7'                      "
					+"		                           AND TABLE_NAME <> 'TOAD_PLAN_TABLE'        "
					+"		                           AND TABLE_NAME LIKE '%_C'                  "
					+"		                  ORDER BY TABLE_NAME, COLUMN_NAME) I2                "
					+"		           WHERE     I1.TABLE_NAME || '_C' = I2.TABLE_NAME            "
					+"		                 AND I1.COLUMN_NAME = I2.COLUMN_NAME                  "
					+"		        GROUP BY I1.TABLE_NAME)";
	
	private List<QueryModel>	queryModelList	= new ArrayList<QueryModel>();
	
	
	private List<QueryModel>	insertQuerys	= new ArrayList<QueryModel>();
	
	
	private List<String>  getTableColumnInfo(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {

		// 출력 준비
		stmt = conn.createStatement();

		//System.out.println(tableInfoQuery);

		pstmt = conn.prepareStatement(tableInfoQuery);
//		pstmt.setString(1, "7934");
		rs = pstmt.executeQuery();

		rs = stmt.executeQuery(tableInfoQuery);

		while (rs.next()) {

			QueryModel model = new QueryModel();
			model.setTableName(rs.getString(1));
			model.setParentTables(rs.getString(2));

			queryModelList.add(model);
		}

		List<String> newOrder = new ArrayList<String>();

		String startTable = "";
		while (true) {

			
			//System.out.println("신규리스트:"+newOrder.size());
			
			for (int i = queryModelList.size() - 1; i >= 0; i--) {

				int sCount = 0;
				int pSize = queryModelList.get(i).getParentTableList().size();
				
				if (newOrder.size() == 0 || pSize == 0) {
					//System.out.println("부모없음:"+queryModelList.get(i).getTableName());
					newOrder.add(queryModelList.get(i).getTableName());
					queryModelList.remove(i);
					break;
				}
				
				//System.out.println("부모존재:"+queryModelList.get(i).getTableName());
				//System.out.println("포함!!!"+queryModelList.get(i).getTableName()+"||"+queryModelList.get(i).getParentTables());
				
				for (String parent : queryModelList.get(i).getParentTableList()) {
					if (newOrder.contains(parent)) {
						//System.out.println("포함!!!"+queryModelList.get(i).getTableName()+"||"+queryModelList.get(i).getParentTables());
						sCount++;
					}
				}

				if (pSize == sCount) {
					//System.out.println("OK------------_______");
					newOrder.add(queryModelList.get(i).getTableName());
					queryModelList.remove(i);
					break;
				}
			}

			//System.out.println("남은수:"+queryModelList.size());
			if (queryModelList.size() == 0) {
				break;
			}

		}

		for (String table : newOrder) {
			//System.out.println(table);
		}
		
		return newOrder;

	}
	
	
	private void getTableColumnInfo2(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {

//		System.out.println(insertQuery);
		
		// 출력 준비
		stmt = conn.createStatement();

		pstmt = conn.prepareStatement(insertQuery);
		rs = pstmt.executeQuery();

		rs = stmt.executeQuery(insertQuery);

		while (rs.next()) {

			QueryModel model = new QueryModel();
			model.setTableName(rs.getString(1));
			model.setQuerys(rs.getString(2));

			insertQuerys.add(model);
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
				List<String> newOrder = getTableColumnInfo(conn, stmt, pstmt, rs);
				
				getTableColumnInfo2(conn, stmt, pstmt, rs);
				
				
				
				for(String orderTable : newOrder){
					//System.out.println(orderTable);
					
					
					for(QueryModel query: insertQuerys){
						
						if(orderTable.equals(query.getTableName())){
							System.out.println(query.getQuerys());
						}
					}
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

	}

	public static void main(String[] args) throws IOException {

		RestoreQueryGen gen = new RestoreQueryGen();
		gen.getTableInfos();
	}

}