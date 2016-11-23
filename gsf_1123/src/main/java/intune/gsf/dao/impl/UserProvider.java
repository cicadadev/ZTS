package intune.gsf.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;

import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SchemeUtil;
import intune.gsf.model.TableClassColumnInfo;

public class UserProvider {

	private static final Logger logger = LoggerFactory.getLogger(DataAccessObjectImpl.class);

	public String insert(Object obj) throws Exception {
		String query = this.makeInsertQuery(obj);
		if (logger.isDebugEnabled()) {
//			logger.debug(" insert query : {}", query);
		}
		return query;
	}

	private String makeInsertQuery(final Object obj) throws Exception {
		return new SQL() {
			{
				List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);

				String className = SchemeUtil.getClassNameFromClazz(obj.getClass());// 클래스 명
				String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환

				INSERT_INTO(tableName);
				for (TableClassColumnInfo tableCol : fields) {

					// insert 문의 컬럼값 영역
					String columnName = tableCol.getDbColumnName();
					String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);

					String insertValue = (tableCol.getValue() == null || "null".equals(tableCol.getValue()) ? "" : tableCol.getValue() + "");

					// insert 문의 values 영역 생성
					if ("Y".equals(tableCol.getMaxPkYn())) {// 컬럼이 pk 이고 max+1 인경우
						// 컬럼값을 max select 문으로 대체
						VALUES(columnName, "#{id}");
					} else if (BaseConstants.DB_INSERT_DT.equals(tableCol.getDbColumnName())) {// 등록일
						VALUES(columnName, "SYSDATE");
					} else if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getDbColumnName())) {// 수정일
						VALUES(columnName, "SYSDATE");
					} else if (tableCol.getValue() != null && BaseConstants.SYSDATE.equals(tableCol.getValue().toString())) {
						VALUES(columnName, "SYSDATE");
					} else if (!"".equals(insertValue) && tableCol.getClassColumnName().endsWith("Dt")) {
						VALUES(columnName, "TO_DATE(#{" + fieldName + "}, 'YYYY-MM-DD HH24:MI:SS')");
					} else {
						VALUES(columnName, "#{" + fieldName + "}");
					}
				}
			}
		}.toString();
	}

	public String update(Object obj) throws Exception {
		String query = this.makeUpdateQuery(obj);
		if (logger.isDebugEnabled()) {
			logger.debug(" update query : {}", query);
		}
		return query;
	}

	private String makeUpdateQuery(final Object obj) throws Exception {
		return new SQL() {
			{
				String className = SchemeUtil.getClassNameFromClazz(obj.getClass());// 클래스 명
				String tableName = CommonUtil.convertTableNotation(className);// table 표기법 변환

				UPDATE(tableName);

				List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);
				for (TableClassColumnInfo tableCol : fields) {

					String columnName = tableCol.getDbColumnName();
					String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);

					String columnType = tableCol.getColumnType();

					if (BaseConstants.DB_INSERT_DT.equals(tableCol.getDbColumnName())
							|| BaseConstants.DB_INSERT_ID.equals(tableCol.getDbColumnName())) {
						tableCol.setValue(null);// insert date/id 는 미입력
					}

					if (BaseConstants.DB_UPDATE_DT.equals(tableCol.getDbColumnName())) {// 수정일
						SET(columnName + " = SYSDATE");
					} else if (!"Y".equals(tableCol.getPkYn())) {// PK가 아닌컬럼 set

						if (tableCol.getValue() == null) {// null이면 update안함
							continue;
						}

						if (("java.math.BigDecimal".equals(columnType)
								&& BaseConstants.BIGDECIMAL_NULL.equals(new BigDecimal(tableCol.getValue().toString())))
								|| "".equals(tableCol.getValue().toString())) {
							// ""이면 null로 update
							SET(columnName + " = null");
						} else if (BaseConstants.SYSDATE.equals(tableCol.getValue().toString())) {
							// SYSDATE로 세팅했을경우
							SET(columnName + " = SYSDATE");
						} else if (tableCol.getClassColumnName().endsWith("Dt")) {
							// 날짜 컬럼일경우 TO_DATE()처리
							SET(columnName + " = TO_DATE(#{" + fieldName + "}, 'YYYY-MM-DD HH24:MI:SS')");
							// } else if ("java.math.BigDecimal".equals(columnType)) {
							// SET(tableColumnName + " = #{" + classColumnName + "}");
						} else {
							// 일반 컬럼들
							SET(columnName + " = #{" + fieldName + "}");
						}

					} else {// pk컬럼은 where문
						WHERE(columnName + " = #{" + fieldName + "}");
					}
				}
			}
		}.toString();
	}
}
