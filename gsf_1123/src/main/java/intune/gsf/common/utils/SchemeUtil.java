package intune.gsf.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.constants.SchemeConstants;
import intune.gsf.model.TableClassColumnInfo;

/**
 * 쿼리 자동 생성을 위한 유틸
 * 
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : SchemeUtil.java
 * @author : eddie
 * @date : 2016. 4. 21.
 * @description : Table Scheme Util
 */
public class SchemeUtil {
	private static final Logger	logger				= LoggerFactory.getLogger(SchemeUtil.class);

	private static String[]		PK_MAX_POSTFIX		= { "_NO", "_ID" };										//MAX+1 post fix

	private static List<String>	baseEntityColumn	= Arrays.asList("insId", "insDt", "updId", "updDt");

	/**
	 * 
	 * @Method Name : getSelectQueryStr
	 * @author : eddie
	 * @date : 2016. 4. 21.
	 * @description : Make Select-Query String
	 *
	 * @param clazz
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	public static String getSelectQueryStr(Object obj) throws Exception {

		String className = getClassNameFromClazz(obj.getClass());
		String tableName = CommonUtil.convertTableNotation(className);

		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

		String query = "select * from " + tableName + " where ";
		String where = "";

		for (TableClassColumnInfo table : fields) {
			 if ("Y".equals(table.getPkYn())) {
				if (CommonUtil.isNotEmpty(where)) {
					where += " and ";
				 }

				String columnType = table.getColumnType();
				if ("java.math.BigDecimal".equals(columnType)) {
					where += table.getDbColumnName() + "=" + table.getValue();
				} else {
					where += table.getDbColumnName() + "=" + "'" + "" + table.getValue() + "'";
				}
			}
		}

		return query + where;
	}

	/**
	 * 모델 파라메터로 TableClassColumnInfo 정보 조회(DB컬럼명, Class 컬럼명, pk여부.. )
	 * 
	 * @Method Name : makeColumnInfo
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static List<TableClassColumnInfo> makeColumnInfo(Object obj)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {


		Class myClass = null;
		Class baseEntity = null;
		Class superClass = obj.getClass().getSuperclass();

		Field[] fields = null;
		Field[] baseFields = null;
		if (superClass.getName().contains("intune.gsf.model.BaseEntity")) {
			myClass = obj.getClass();
			baseEntity = superClass;
		} else {
			myClass = obj.getClass().getSuperclass();
			baseEntity = superClass.getSuperclass();
		}
		fields = myClass.getDeclaredFields();
		baseFields = baseEntity.getDeclaredFields();
//		fields = (Field[]) ArrayUtils.addAll(fields, baseFields);



		List<TableClassColumnInfo> columnList = new ArrayList<TableClassColumnInfo>();

		setField(columnList, fields, myClass, obj, false);
		setField(columnList, baseFields, baseEntity, obj, true);
		return columnList;

	}

	private static void setField(List<TableClassColumnInfo> columnList, Field[] fields, Class myClass, Object obj,
			boolean baseEntity)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		String tableName = CommonUtil.convertTableNotation(SchemeUtil.getClassNameFromClazz(obj.getClass()));

		ArrayList<String> list = new ArrayList<String>();
		list.add("java.lang.String");
		list.add("java.sql.Timestamp");
		list.add("java.math.BigDecimal");
		list.add("java.lang.Integer");



		for (int i = 0; i < fields.length; ++i) {

			if (list.contains(fields[i].getType().getName()) && !baseEntity
					|| (baseEntity && baseEntityColumn.contains(fields[i].getName()))) {

				//System.out.println("type:" + fields[i].getType().getName() + ",name:" + fields[i].getName());

				TableClassColumnInfo ti = new TableClassColumnInfo();

				String columnName = fields[i].getName();
				ti.setClassColumnName(columnName);
				ti.setDbColumnName(CommonUtil.convertTableNotation(columnName));
				ti.setColumnType(fields[i].getType().getName());

				Field field = myClass.getDeclaredField(ti.getClassColumnName());
				field.setAccessible(true);
				Object value = field.get(obj);
				ti.setValue(value);

				if (CommonUtil.isEmpty(value)) {
					try {
					if (BaseConstants.PARAM_INSERT_ID.equals(columnName)) {
							ti.setValue(CommonUtil.replaceNull(SessionUtil.getLoginId(), BaseConstants.NON_MEMBER_ID));
					} else if (BaseConstants.PARAM_UPDATE_ID.equals(columnName)) {
							ti.setValue(CommonUtil.replaceNull(SessionUtil.getLoginId(), BaseConstants.NON_MEMBER_ID));
					} else if (BaseConstants.PARAM_STORE_ID.equals(columnName)) {
//						ti.setValue(SessionUtil.getStoreId());
					} else if (BaseConstants.PARAM_LANG_CD.equals(columnName)) {
						ti.setValue(SessionUtil.getLangCd());
					}
					} catch (UsernameNotFoundException u) {
						//logger.error(u.getMessage(), u);
					}
				}

				//pk 여부
				String[] pkList = SchemeUtil.getPkList(tableName);
				for (String pk : pkList) {
					if (ti.getDbColumnName().equals(pk)) {
						ti.setPkYn("Y");

						//해당 테이블의 pk중 no or id가 있는지(max+1 컬럼)
						if (SchemeUtil.isEmptyObject(ti.getValue()) && (pk.toUpperCase().endsWith(PK_MAX_POSTFIX[0])
								|| pk.toUpperCase().endsWith(PK_MAX_POSTFIX[1]))) {
							ti.setMaxPkYn("Y");
						}
					}
				}

				columnList.add(ti);
			}
		}
	}

	/**
	 * 
	 * @Method Name : getInsertQueryStr
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description : Insert Query 생성
	 *
	 * @return
	 * @throws Exception
	 */
	public static void getInsertQueryStr(Object obj, Map<String, String> map) throws Exception {

		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
		String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환
		SchemeUtil.getInsertMaxSelectStr(tableName, fields, map);	// max, max+1 query

		String query = "";
		String columns = "";
		String values = "";

		query += "INSERT INTO " + tableName + " (";

		for (TableClassColumnInfo tableCol : fields) {

			// insert 문의 컬럼값 영역
			String tableColumnName = tableCol.getDbColumnName();

			if (CommonUtil.isEmpty(columns)) {
				columns += tableColumnName;
			} else {
				columns += "," + tableColumnName;
			}

			if (!CommonUtil.isEmpty(values)) {
				values += ",";
			}

			String insertValue = (tableCol.getValue() == null || "null".equals(tableCol.getValue()) ? "" : tableCol.getValue()+"");

			// insert 문의 values 영역 생성
			if ("Y".equals(tableCol.getMaxPkYn())) {// 컬럼이 pk 이고 max+1 인경우
				// 컬럼값을 max select 문으로 대체
				values += "(" + map.get("selectMaxPlusOne") + ")";
			} else if (BaseConstants.DB_INSERT_DT.equals(tableCol.getDbColumnName())) {// 등록일
				values += "SYSDATE";
			} else if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getDbColumnName())) {// 수정일
				values += "SYSDATE";
			} else if (tableCol.getValue() != null && BaseConstants.SYSDATE.equals(tableCol.getValue().toString())) {
				values += "SYSDATE";
			} else if (!"".equals(insertValue) && tableCol.getClassColumnName().endsWith("Dt")) {
				values += "TO_DATE('" + tableCol.getValue() + "', 'YYYY-MM-DD HH24:MI:SS')";
			} else {
				values += "'" + insertValue + "'";
			}
		}
		query += columns + ") VALUES (" + values + ")";
		
		map.put("query", query);
	}
//	public static String getInsertQueryStr(Object obj) throws Exception {
//		
//		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);
//		
//		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
//		String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환
//		
//		String query = "";
//		String columns = "";
//		String values = "";
//		
//		query += "INSERT INTO " + tableName + " (";
//		
//		for (TableClassColumnInfo tableCol : fields) {
//			
//			// insert 문의 컬럼값 영역
//			String tableColumnName = tableCol.getDbColumnName();
//			
//			if (CommonUtil.isEmpty(columns)) {
//				columns += tableColumnName;
//			} else {
//				columns += "," + tableColumnName;
//			}
//			
//			if (!CommonUtil.isEmpty(values)) {
//				values += ",";
//			}
//			
//			// insert 문의 values 영역 생성
//			if ("Y".equals(tableCol.getMaxPkYn())) {// 컬럼이 pk 이고 max+1 인경우
//				// 컬럼값을 max select 문으로 대체
//				values += SchemeUtil.getInsertMaxSelectStr(tableName, fields);
//			} else if (BaseConstants.DB_INSERT_DT.equals(tableCol.getDbColumnName())) {// 등록일
//				values += "SYSDATE";
//			} else if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getDbColumnName())) {// 수정일
//				values += "SYSDATE";
//			} else {
//				values += "'" + (tableCol.getValue() == null || "null".equals(tableCol.getValue()) ? "" : tableCol.getValue()) + "'";
//			}
//		}
//		
//		query += columns + ") VALUES (" + values + ")";
//		return query;
//	}

	/**
	 * Update Query 생성
	 * 
	 * @Method Name : getUpdateQueryStr
	 * @author : eddie
	 * @date : 2016. 4. 25.
	 * @description :
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String getUpdateQueryStr(Object obj) throws Exception {

		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
		String tableName = CommonUtil.convertTableNotation(className);//table 표기법 변환

		String query = "UPDATE " + tableName + " SET ";

		String setStr = "";
		String whereStr = "";
		for (TableClassColumnInfo tableCol : fields) {

			String columnType = tableCol.getColumnType();


			if (BaseConstants.DB_INSERT_DT.equals(tableCol.getDbColumnName())
					|| BaseConstants.DB_INSERT_ID.equals(tableCol.getDbColumnName())) {
				tableCol.setValue(null);//insert date/id 는 미입력
			}
			

			
			if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getDbColumnName())) {// 수정일
				
				if (CommonUtil.isNotEmpty(setStr)) {
					setStr += ",";
				}
				
				setStr += tableCol.getDbColumnName() + " = SYSDATE";
			}
			else if (!"Y".equals(tableCol.getPkYn())) {// PK가 아닌컬럼 set

				if (tableCol.getValue() == null) {//null이면 update안함
					continue;
				}

				if (CommonUtil.isNotEmpty(setStr)) {
					setStr += ",";
				}

				if (("java.math.BigDecimal".equals(columnType)
						&& BaseConstants.BIGDECIMAL_NULL.equals(new BigDecimal(tableCol.getValue().toString())))
						|| "".equals(tableCol.getValue().toString())) {
					//""이면 null로 update
					setStr += tableCol.getDbColumnName() + " = null";

				} else if (BaseConstants.SYSDATE.equals(tableCol.getValue().toString())) {
					// SYSDATE로 세팅했을경우 
					setStr += tableCol.getDbColumnName() + " = SYSDATE";

				} else if (tableCol.getClassColumnName().endsWith("Dt")) {
					// 날짜 컬럼일경우 TO_DATE()처리
					setStr += tableCol.getDbColumnName() + " = TO_DATE('" + tableCol.getValue() + "', 'YYYY-MM-DD HH24:MI:SS')";

				} else if ("java.math.BigDecimal".equals(columnType)) {
					setStr += tableCol.getDbColumnName() + " = " + tableCol.getValue();
				} else {
					// 일반 컬럼들
					setStr += tableCol.getDbColumnName() + " = '" + tableCol.getValue() + "'";
				}

			} else {// pk컬럼은 where문

				if (CommonUtil.isNotEmpty(whereStr)) {
					whereStr += " AND ";
				}
				whereStr += tableCol.getDbColumnName() + " = '" + tableCol.getValue() + "'";
			}

		}

		query += setStr + " WHERE " + whereStr;

		return query;
	}

	/**
	 * delete쿼리 생성
	 * 
	 * @Method Name : getDeleteQueryStr
	 * @author : eddie
	 * @date : 2016. 4. 25.
	 * @description :
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String getDeleteQueryStr(Object obj) throws Exception {

		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
		String tableName = CommonUtil.convertTableNotation(className);//table 표기법 변환

		String query = "DELETE FROM " + tableName + " WHERE ";

		String whereStr = "";
		for (TableClassColumnInfo tableCol : fields) {

			if ("Y".equals(tableCol.getPkYn())) {

				if (CommonUtil.isNotEmpty(whereStr)) {
					whereStr += " AND ";
				}
				whereStr += tableCol.getDbColumnName() + " = '" + tableCol.getValue() + "'";
			}

		}

		query += whereStr;

		return query;
	}

	/**
	 * PK중에 max+1 대상인 컬럼 조회
	 * 
	 * @Method Name : getMaxColumnInPk
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static String getMaxColumnInPk(String tableName, Object obj) throws Exception {
		String[] pkList = SchemeUtil.getPkList(tableName);
		
		for(String pk : pkList){

			Field field = obj.getClass().getDeclaredField(CommonUtil.convertCamel(pk, false));
			field.setAccessible(true);
			Object value = field.get(obj);

			if ((pk.toUpperCase().endsWith(PK_MAX_POSTFIX[0]) || pk.toUpperCase().endsWith(PK_MAX_POSTFIX[1])) && value == null) {// 파라메터로 넘긴 값이 없으면 max+1 대상pk
				return pk;
			}
		}
		return null;
	}

	public static boolean isEmptyObject(Object v){
		boolean isEmpty = v == null || v != null && v.toString().length() == 0;
		return isEmpty;
	}
	/**
	 * MAX + 1 Select 쿼리문 생성
	 * 
	 * @Method Name : getInsertMaxSelectStr
	 * @author : eddie
	 * @date : 2016. 4. 25.
	 * @description :
	 *
	 * @param tableName
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static void getInsertMaxSelectStr(String tableName, List<TableClassColumnInfo> fields, Map<String, String> map) throws Exception {
		String whereStr = "";
		String maxColumn = "";
		String maxClassColumn = "";
		String selectMaxPlusOne = "";
		String selectMax = "";
		
		for (TableClassColumnInfo column : fields) {
			if (CommonUtil.isEmpty(whereStr) && "Y".equals(column.getPkYn()) && !SchemeUtil.isEmptyObject(column.getValue())) {
				whereStr += " WHERE " + column.getDbColumnName() + " = '" + column.getValue() + "'";
			} else if ("Y".equals(column.getPkYn()) && !SchemeUtil.isEmptyObject(column.getValue())) {
				whereStr += " AND " + column.getDbColumnName() + " = '" + column.getValue() + "'";
			} else if ("Y".equals(column.getMaxPkYn())) {
				maxColumn = column.getDbColumnName();
				maxClassColumn = column.getClassColumnName();
			}
		}
		if (CommonUtil.isNotEmpty(maxColumn)) {
			if (maxColumn.endsWith(PK_MAX_POSTFIX[1])) {
				maxColumn = "TO_NUMBER(" + maxColumn + ")";
			}
//			selectMaxPlusOne = "(SELECT NVL ( (SELECT MAX (" + maxColumn + ") + 1" + " FROM " + tableName + whereStr + "), 1) MAXNUM FROM DUAL)";
			selectMaxPlusOne = "SELECT NVL (MAX (" + maxColumn + ") + 1, 1) FROM " + tableName + whereStr;
			selectMax = "SELECT MAX (" + maxColumn + ") as " + maxClassColumn + " FROM " + tableName + whereStr;
		}
		// System.out.println(">>>>>>>>>>>>>> query:::: " + maxPlusOneQuery);
		map.put("key", maxClassColumn);
		map.put("keyName", maxClassColumn);
		map.put("selectMax", selectMax);
		map.put("selectMaxPlusOne", selectMaxPlusOne);
//		return selectMaxPlusOne;
	}
	
	/**
	 * 
	 * @Method Name : getPkList
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description : 테이블 명으로 PK 컬럼들 조회
	 *
	 * @param tableName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String[] getPkList(String tableName) throws IllegalArgumentException, IllegalAccessException {

		Field[] fields = SchemeConstants.class.getDeclaredFields();

		for (int i = 0; i < fields.length; ++i) {
			if (fields[i].getName().equals(tableName)) {
				return (String[]) fields[i].get(SchemeConstants.class);
			}
		}

		return null;

	}

	/**
	 * Class정보로 클래스명 조회
	 * 
	 * @Method Name : getClassNameFromClazz
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getClassNameFromClazz(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
		String className = clazz.getName();
		int lastIndex = className.lastIndexOf(".");
		className = className.substring(lastIndex + 1);
		return className.replace("Base", "");
	}


	public static String getWhereInConditionString(Map<String, String> keywordArry) {

		if (keywordArry != null && !keywordArry.isEmpty()) {
			List<String> strArr = new ArrayList<String>();
			for (String key : keywordArry.keySet()) {
				if (CommonUtil.isNotEmpty(keywordArry.get(key)) && !"false".equals(keywordArry.get(key))) {
					strArr.add("'" + keywordArry.get(key) + "'");
				}
			}

			return StringUtils.join(strArr, ",");
		}
		return null;
	}

	/**
	 * 
	 * @Method Name : getInsertQueryStr
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description : Insert Query 생성
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getInsertQueryStr2(Object obj, Map<String, String> map) throws Exception {

		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

		String className = getClassNameFromClazz(obj.getClass());// 클래스 명
		String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환
		SchemeUtil.getInsertMaxSelectStr(tableName, fields, map);	// max, max+1 query
//
//		System.out.println("@Key:" + map.get("key"));
//		System.out.println("@keyName:" + map.get("keyName"));
//		System.out.println("@selectMax:" + map.get("selectMax"));
//		System.out.println("@selectMaxPlusOne:" + map.get("selectMaxPlusOne"));

		return map.get("selectMaxPlusOne");
	}
}
