package intune.gsf.tool.sourcegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.tool.sourcegenerator.vo.RelationInfo;
import intune.gsf.tool.sourcegenerator.vo.TableColumn;

public class Generator {

	public Generator() throws IOException {

		// 프로퍼티 객체 생성
		props = new Properties();

		// 프로퍼티 파일 스트림에 담기
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propFile);
			props.load(new java.io.BufferedInputStream(fis));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}

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

	private Properties						props			= null;
	private Properties						jdbcprop					= null;

	private static String					projectDir		= "C:\\ZTS\\project";
	
	private static String					jdbcFile		= projectDir + "\\gcp2.0_platform\\src\\main\\resources\\config\\system\\jdbc.xml";
	private String							url							= null;

	private String							dbAccount					= null;
	private String							dbPassword					= null;

	private String							tableOwner					= null;

	static String							prefix			= "Base";
	static String							prefixS			= "base";
	private static String					intunegsfDir	= projectDir + "\\intune_gsf\\src\\main\\java";
	private static String					propFile		= intunegsfDir
			+ "\\intune\\gsf\\tool\\sourcegenerator\\generator.properties";

	private static String					platformDir		= projectDir + "\\gcp2.0_platform\\src\\main";
	private static String					targetDir		= platformDir + "\\java\\gcp\\";
	private static String					sqlMapFile		= platformDir + "\\resources\\config\\mybatis\\sql-map-config.xml";

	private static String					constantsDir	= intunegsfDir + "\\intune\\gsf\\common\\constants";
	private static String					constantsJavaPath	= constantsDir + "\\SchemeConstants.java";

	private static String					propertiesDir		= projectDir
			+ "\\gcp2.0_admin\\src\\main\\webapp\\WEB-INF\\messages\\field";

	private static String					propertiesPath		= propertiesDir + "\\field_message.properties";

	private List<Map<String, String>>		typeAlias		= new ArrayList<Map<String, String>>();

	private Map<String, List<TableColumn>>	tmap			= new LinkedHashMap<String, List<TableColumn>>();

	private Map<String, List<RelationInfo>>	rmap1			= new HashMap<String, List<RelationInfo>>();

	private Map<String, List<RelationInfo>>	rmap2			= new HashMap<String, List<RelationInfo>>();

	private String							tableInfoQuery				= "";

	//BaseEntity에 들어갈 기본 컬럼
	private List<String>					baseColumnList				= Arrays.asList("INS_DT", "INS_ID", "UPD_DT", "UPD_ID");

	//code name getter 관련

	private static String					codeNameTargetPostFix	= "_CD";
	private static String					codeNameTargetPostFixCamel	= "Cd";

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

		//System.out.println(tableInfoQuery);

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

//			System.out.println(
//					"" + tableColumn.getTableName() + "||" + tableColumn.getColumnName() + "||" + tableColumn.getColumnType());

			prevTableName = currentTableName;

		}

	}

	/**
	 * 릴레이션 정보 조회1
	 * 
	 * @param conn
	 * @param stmt
	 * @param pstmt
	 * @param rs
	 * @throws SQLException
	 */
	private void getRelationInfo1(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {

		// 출력 준비
		String sql = "SELECT cons.table_name AS child_table, col.table_name parent_table"
				+ " FROM dba_cons_columns col, dba_constraints cons" + "   WHERE     cons.r_owner = col.owner"
				+ "       AND cons.r_constraint_name = col.constraint_name" + "        AND COL.OWNER = '" + tableOwner + "'"
				+ "	GROUP BY cons.table_name, col.table_name" + " ORDER BY col.table_name";

		stmt = conn.createStatement();

		//System.out.println(sql);

		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		rs = stmt.executeQuery(sql);

		List<RelationInfo> list = new ArrayList<RelationInfo>();

		String currentTableName = null;
		String prevTableName = null;

		while (rs.next()) {

			RelationInfo r = new RelationInfo();
			r.setParentTable(rs.getString(2));
			r.setChildTable(rs.getString(1));

			currentTableName = rs.getString(2);

			if (currentTableName.equals(prevTableName)) {
				//현재 테이블
				list.add(r);

			} else {
				//새테이블
				list = new ArrayList<RelationInfo>();
				list.add(r);
			}

			rmap1.put(currentTableName, list);

//			System.out.println("RMapInfo::" + r.getParentTable() + "||" + r.getChildTable());

			prevTableName = currentTableName;

		}

	}

	/**
	 * 릴레이션 정보 조회2
	 * 
	 * @param conn
	 * @param stmt
	 * @param pstmt
	 * @param rs
	 * @throws SQLException
	 */
	private void getRelationInfo2(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {

		// 출력 준비
		String sql = "SELECT cons.table_name AS child_table, col.table_name parent_table"
				+ " FROM dba_cons_columns col, dba_constraints cons" + "   WHERE     cons.r_owner = col.owner"
				+ "       AND cons.r_constraint_name = col.constraint_name" + "        AND COL.OWNER = '" + tableOwner + "'"
				+ "	GROUP BY cons.table_name, col.table_name" + " ORDER BY cons.table_name";

		stmt = conn.createStatement();

//		System.out.println(sql);

		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		rs = stmt.executeQuery(sql);

		List<RelationInfo> list = new ArrayList<RelationInfo>();

		String currentTableName = null;
		String prevTableName = null;

		while (rs.next()) {

			RelationInfo r = new RelationInfo();
			r.setParentTable(rs.getString(2));
			r.setChildTable(rs.getString(1));

			currentTableName = rs.getString(1);

			if (currentTableName.equals(prevTableName)) {
				//현재 테이블
				list.add(r);

			} else {
				//새테이블
				list = new ArrayList<RelationInfo>();
				list.add(r);
			}

			rmap2.put(currentTableName, list);

//			System.out.println("RMapInfo::" + r.getChildTable() + "||" + r.getParentTable());

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

				//릴레이션 정보1
				getRelationInfo1(conn, stmt, pstmt, rs);

				//릴레이션 정보2
				getRelationInfo2(conn, stmt, pstmt, rs);

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

	private String getPackageName(String tableName) {
		String pakageName = tableName.split("_")[0];
		pakageName = pakageName.toLowerCase();

		return pakageName;
	}

	private void deleteModelInPackage(String dir) {

		//기존 파일 삭제
		File file = new File(dir);

		if (file.exists()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (!f.isDirectory()) {
					f.delete();
				}
			}
			//file.delete();
		}

	}

	/**
	 * 자마소스코드 생성
	 */
	private void makeJavaSourceCode() {

		List<String> packages = new ArrayList<String>();

		for (String tableName : tmap.keySet()) {

			String pakageName = getPackageName(tableName);

			if (!packages.contains(pakageName)) {
				packages.add(pakageName);
			}
		}

		//기존 모델 삭제
		for (String packageName : packages) {
			String dir = targetDir + packageName + "\\model\\base";

			if ("true".equals(this.getProperties("base.model"))) {
				deleteModelInPackage(dir);
			}
			if ("true".equals(this.getProperties("extends.model"))) {
				dir = targetDir + packageName + "\\model";
				deleteModelInPackage(dir);
			}
		}

		int cnt = 0;

		if ("true".equals(this.getProperties("extends.model")) || "true".equals(this.getProperties("base.model"))) {
			System.out.println("\n\n## Model 생성 시작 !!\n");
		}

		for (String key : tmap.keySet()) {

//			System.out.println("[" + cnt++ + "] " + key);
			List<TableColumn> tableColumns = tmap.get(key);

			if ("true".equals(this.getProperties("extends.model"))) {
				makeOneFile2(key);//extends model 생성
			}

			if ("true".equals(this.getProperties("base.model"))) {
				makeOneFile(key, tableColumns);//base model생성
			}

		}
		if ("true".equals(this.getProperties("scheme"))) {
			System.out.println("\n\n## SchemeConstants.java 생성 시작 !!\n");
			makeSchemeConstantsFile();//SchemeConstants.java 생성
		}
	}



	private String convertCamelClass(String txt, String preFix) {
		txt = txt.toLowerCase();
		boolean flag = true;
		String newTxt = StringUtils.isEmpty(preFix)?"":preFix;
		for (int i = 0; i < txt.length(); i++) {

			char ch = txt.charAt(i);

			if (ch == '_') {
				//pass
				flag = true;
			} else if (flag) {
				String st = ch + "";
				newTxt += st.toUpperCase();
				flag = false;
			} else {
				newTxt += txt.charAt(i);
				flag = false;
			}
		}
		return newTxt;
	}

	/**
	 * 클래스 파일 생성 (base model)
	 * 
	 * @param tableName
	 * @param tableColumns
	 */
	private void makeOneFile(String tableName, List<TableColumn> tableColumns) {
		
		String pakageName = this.getPackageName(tableName);

		StringBuffer bf = new StringBuffer();
		StringBuffer bf_all = new StringBuffer();
//		StringBuffer bf_contructor = new StringBuffer();

		StringBuffer getter = new StringBuffer();
		String pkgPath = "gcp." + pakageName + ".model.base";
		bf_all.append("package " + pkgPath + ";\n\n");
		bf_all.append("import lombok.Data;\n");
		bf_all.append("import lombok.EqualsAndHashCode;\n");
		bf_all.append("import lombok.ToString;\n");
//		bf_all.append("import intune.gsf.common.utils.SessionUtil;\n");
		bf_all.append("import intune.gsf.model.BaseEntity;\n");

		bf.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		
//		bf_contructor.append("\n\tpublic ").append(convertCamelClass(tableName)).append("() {").append("\n");
//		bf_contructor.append("\t\tthis.storeId = SessionUtil.getStoreId();").append("\n");	// storeId
//		bf_contructor.append("\t\tthis.langCd = SessionUtil.getLangCd();").append("\n");	// langCd
//		bf_contructor.append("\t\tthis.insId = SessionUtil.getUserId();").append("\n");		// insId
//		bf_contructor.append("\t\tthis.updId = SessionUtil.getUserId();").append("\n");		// updId

		boolean isTimeStamp = false;
		boolean isList = false;
		boolean isBigDecimal = false;
		boolean isDate = false;
		boolean isGetter = false;
		for (TableColumn c : tableColumns) {

			//BaseEntity에 들어갈 컬럼
			if (baseColumnList.contains(c.getColumnName())) {
				continue;
			}
			// 필드 선언부 문자열 생성

			bf.append("\t");
			bf.append("private");
			bf.append(" ");
			if (c.getColumnType().indexOf("CHAR") > -1) {
				bf.append("String");
			} else if (c.getColumnType().indexOf("TIMESTAMP") > -1 || c.getColumnType().indexOf("CLOB") > -1) {
				bf.append("String");
			} else if (c.getColumnType().indexOf("NUMBER") > -1) {
				bf.append("BigDecimal");
				isBigDecimal = true;
			} else if (c.getColumnType().indexOf("INTEGER") > -1) {
				bf.append("long");
			} else if (c.getColumnType().indexOf("DATE") > -1) {
				isDate = true;
				bf.append("Date");
			}
			bf.append(" ");
			bf.append(CommonUtil.convertCamel(c.getColumnName(), false)).append(";");

			// 컬럼 코멘트
			String nullable = "";
			String pkCols = c.getPkCol();
			String[] pkArr = pkCols.split(",");
			bf.append(" //");

			bf.append(c.getComments()).append("		[");

			for (String pk : pkArr) {
				if (pk.equals(c.getColumnName())) {
					bf.append("primary key, ");
				}
			}

			if ("Y".equals(c.getNullable())) {
				nullable = "null";
			} else {
				nullable = "not null";
			}
			bf.append(nullable).append("]\n");


			//code name getter 추가
			if (c.getColumnName().endsWith(codeNameTargetPostFix)) {

				String columnCamel = CommonUtil.convertCamel(c.getColumnName(), true);
				int last = columnCamel.lastIndexOf(codeNameTargetPostFixCamel);
				String columnCamelName = columnCamel.substring(0, last) + "Name";

				getter.append("\n\tpublic String get" + columnCamelName + "(){").append("\n\t")
						.append("\t\treturn CodeUtil.getCodeName(\"" + c.getColumnName() + "\", get" + columnCamel + "());")
						.append("\n").append("\t}\n");
				isGetter = true;


			}

		}


		bf.append("\n");
//		bf_contructor.append("\t}\n");

		//릴레이션 정보 추가1
		List<RelationInfo> rinfo = rmap1.get(tableName);

		if (rinfo != null && rinfo.size() > 0) {
			isList = true;
			for (RelationInfo r : rinfo) {

				//System.out.println("RInfo::" + r.getParentTable() + "||" + r.getChildTable());
				String className = convertCamelClass(r.getChildTable(), null);
				bf.append("\tprivate List<" + className + ">").append(" " + CommonUtil.convertCamel(r.getChildTable(), false))
						.append("s;\n");

				String rPackageName = r.getChildTable().substring(0, 3);
				rPackageName = rPackageName.toLowerCase();

				String importStr = "import gcp." + rPackageName + ".model." + className + ";\n";
				bf_all.append(importStr);
			}
		}

		//릴레이션 정보 추가2
		List<RelationInfo> rinfo2 = rmap2.get(tableName);

		if (rinfo2 != null && rinfo2.size() > 0) {
			for (RelationInfo r : rinfo2) {

				String className = convertCamelClass(r.getParentTable(), null);

				bf.append("\tprivate ").append(className).append(" " + CommonUtil.convertCamel(r.getParentTable(), false))
						.append(";\n");

				String rPackageName = r.getParentTable().substring(0, 3);
				rPackageName = rPackageName.toLowerCase();

				String importStr = "import gcp." + rPackageName + ".model." + className + ";\n";
				bf_all.append(importStr);
			}
		}
		
		bf.append(getter);// cd getter 추가

		if (isTimeStamp) {
			bf_all.append("import java.sql.Timestamp;\n");
		}
		if (isBigDecimal) {
			bf_all.append("import java.math.BigDecimal;\n");
		}
		if (isList) {
			bf_all.append("import java.util.List;\n");
		}
		if (isDate) {
			bf_all.append("import java.sql.Date;\n");
		}
		
		if (isGetter) {
			String importStr = "import gcp.common.util.CodeUtil;\n";
			bf_all.append(importStr);
		}

		bf_all.append("\n");

		bf_all.append("@EqualsAndHashCode(callSuper=true)\n").append("@ToString(callSuper=true)\n").append("@Data\n");
		bf_all.append("public class " + convertCamelClass(tableName, prefix) + " extends BaseEntity{\n")
//				.append(bf_contructor)
		.append(bf)
		.append("}");

		String dir = targetDir + pakageName + "\\model\\base";
		//System.out.println("target:" + dir);
		try {

			File file = new File(dir);

			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = convertCamelClass(tableName, prefix);
			String fullpath = dir + "\\" + fileName + ".java";

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

	/**
	 * 클래스 파일 생성 ( extends model )
	 * 
	 * @param tableName
	 * @param tableColumns
	 */
	private void makeOneFile2(String tableName) {

		String pakageName = getPackageName(tableName);

		StringBuffer bf = new StringBuffer();
		StringBuffer bf_all = new StringBuffer();
		String pkgPath = "gcp." + pakageName + ".model";
		bf_all.append("package " + pkgPath + ";\n\n");
		bf_all.append("import lombok.Data;\n");
		bf_all.append("import lombok.EqualsAndHashCode;\n");
		bf_all.append("import lombok.ToString;\n");
		bf_all.append("import gcp." + pakageName + ".model.base." + convertCamelClass(tableName, prefix) + ";\n");

		bf_all.append("\n");

		bf_all.append("@EqualsAndHashCode(callSuper = false)\n").append("@ToString(callSuper = true)\n").append("@Data\n");
		bf_all.append("public class " + CommonUtil.convertCamel(tableName, true) + " extends "
				+ convertCamelClass(tableName, prefix) + " {\n")
				.append(bf).append("}");

		String dir = targetDir + pakageName + "\\model";

		try {

			File file = new File(dir);

			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = CommonUtil.convertCamel(tableName, true);
			String fullpath = dir + "\\" + fileName + ".java";

			//makeTypeAlias(pkgPath, fileName);

			File file2 = new File(fullpath);

//			if (file2.exists()) {
//				file2.delete();
//			}

			FileWriter fw = new FileWriter(file2, true);

			fw.write(bf_all.toString());
			fw.flush();

			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 스키마 상수 파일 생성
	 * 
	 * @param tableName
	 * @param tableColumns
	 */
	private void makeSchemeConstantsFile() {
		StringBuffer bf = new StringBuffer();

		for (String key : tmap.keySet()) {

			List<TableColumn> list = tmap.get(key);
			String[] arr = list.get(0).getPkCol().split(",");

			String pkStr = "";
			for (String pk : arr) {
				if (pkStr.equals("")) {
					pkStr += "\"" + pk + "\"";
				} else {
					pkStr += ", " + "\"" + pk + "\"";
				}
			}

			bf.append("\tpublic static String[] " + key + " = { " + pkStr + " };\n");

		}

		StringBuffer bf_all = new StringBuffer();

		bf_all.append("package intune.gsf.common.constants;\n\n");
		bf_all.append("\n");

		bf_all.append("public class SchemeConstants {\n").append(bf).append("}");

		try {

			File file = new File(constantsDir);

			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(constantsJavaPath);

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

	private void makeTypeAlias(String pkgPath, String fileName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", pkgPath + "." + fileName);
		map.put("alias", StringUtils.uncapitalize(fileName));
		typeAlias.add(map);
	}

	private void changeXml() {

		if ("true".equals(this.getProperties("sql.map.config"))) {
			System.out.println("\n\n## sql-map-config.xml typeAliases 생성 시작 !!\n");
		}

		makeTypeAlias("java.util", "HashMap");
		makeTypeAlias("java.util", "Map");
		makeTypeAlias("java.util", "List");

		for (String tableName : tmap.keySet()) {
			String pakageName = getPackageName(tableName);

			String pkgPath = "gcp." + pakageName + ".model";
			String fileName = convertCamelClass(tableName, null);
			makeTypeAlias(pkgPath, fileName);

			pkgPath = "gcp." + pakageName + ".model.base";
			fileName = convertCamelClass(tableName, prefix);
			makeTypeAlias(pkgPath, fileName);

		}
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder parser = dbf.newDocumentBuilder();

			Document xmldoc = parser.parse(sqlMapFile);

			NodeList cnodes = xmldoc.getElementsByTagName("configuration");
			NodeList nodes = xmldoc.getElementsByTagName("typeAliases");
			Node typeAliasesNode = nodes.item(0);							//typeAliases 1개
			NodeList typeAList = typeAliasesNode.getChildNodes();

			for (int i = 0; i < typeAList.getLength(); i++) {
				NamedNodeMap attrn = typeAList.item(i).getAttributes();
				if (attrn != null) {
					String typeValue = attrn.getNamedItem("type").getNodeValue();
//					System.out.println("type Value :" + typeValue);

					if (typeValue.indexOf(".grid.") > -1 || typeValue.indexOf(".search.") > -1
							|| typeValue.indexOf(".external.") > -1) {

					} else {
						nodes.item(0).removeChild(typeAList.item(i));
					}

				}
			}

			//Element typeAlisesNode = xmldoc.createElement("typeAliases");

			for (Map<String, String> map : typeAlias) {

				Element taNode = xmldoc.createElement("typeAlias");

				Attr attr = xmldoc.createAttribute("type");
				attr.setValue(map.get("type"));
				taNode.setAttributeNode(attr);
				attr = xmldoc.createAttribute("alias");
				attr.setValue(map.get("alias"));
				taNode.setAttributeNode(attr);
				typeAliasesNode.appendChild(taNode);
				//System.out.println("========" + taNode.getAttributes().item(0));
			}

			//cnodes.item(0).appendChild(typeAlisesNode);

			//-------------------------파일로 저장

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer t = factory.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DocumentType doctype = xmldoc.getDoctype();
			t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			DOMSource xml = new DOMSource(xmldoc);
			StreamResult s = new StreamResult(new File(sqlMapFile));
			t.transform(xml, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private String getProperties(String key) {

		String msg = props.getProperty(key);
		return msg;

	}

	private void makeMessageProperties() {

		System.out.println("\n\n## field_message.properties 생성 시작 !!\n");

		Properties prop = new Properties();

		for (String key : tmap.keySet()) {
			
			List<TableColumn> tc = tmap.get(key);

			for (TableColumn t : tc) {
				if (baseColumnList.contains(t.getColumnName())) {
					continue;
				}
				String strKey = CommonUtil.convertCamel(t.getTableName(), false) + "."
						+ CommonUtil.convertCamel(t.getColumnName(), false);
				String value = "";
				
				if (!CommonUtil.isEmpty(t.getComments())) {

					String[] comments = t.getComments().split("\\|\\|");
					value = comments[0];

				}
				
				prop.setProperty(strKey, value);

			}
		}
		try {

			File file = new File(propertiesDir);

			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(propertiesPath);

			if (file2.exists()) {
				file2.delete();
			}

			FileOutputStream out = new FileOutputStream(file2);

			prop.store(out, "field_message");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException {

		Generator gen = new Generator();

		//테이블 정보 조회
		gen.getTableInfos();

		//자바모델생성
		gen.makeJavaSourceCode();

		if ("true".equals(gen.getProperties("sql.map.config"))) {
			//sql-map-config 생성
			gen.changeXml();
		}

		// 메세지 프로퍼티 생성
		if ("true".equals(gen.getProperties("field.properties"))) {
			//sql-map-config 생성
			gen.makeMessageProperties();
		}

		System.out.println("\n\n## Source Generation 완료 !!\n");
	}

}