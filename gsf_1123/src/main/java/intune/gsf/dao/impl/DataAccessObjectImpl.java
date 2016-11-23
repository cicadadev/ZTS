package intune.gsf.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Maps;

import intune.gsf.common.excel.converter.TypeConverter;
import intune.gsf.common.excel.converter.TypeConverters;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SchemeUtil;
import intune.gsf.dao.DataAccessObject;
import intune.gsf.dao.UserMapper;
import intune.gsf.model.TableClassColumnInfo;

@Repository("dao")
// public class DataAccessObjectImpl<T> extends SqlSessionDaoSupport implements
// DataAccessObject<T> {
public class DataAccessObjectImpl<T> implements DataAccessObject<T> {

	private static final Logger logger = LoggerFactory.getLogger(DataAccessObjectImpl.class);

	@Autowired
	private SqlSessionTemplate sqlSession; // oracle
	
//	@Autowired
//	private SqlSessionTemplate sqlSessionBatch; // oracle batch
	// @Override
	// @Resource(name = "oracleSession")
	// public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
	// super.setSqlSessionFactory(sqlSession);
	// }

	@Override
	public List<?> selectList(String queryId, Object object) {
		return sqlSession.selectList(queryId, object);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> selectListWithHandler(String queryId, Object object) {
		@SuppressWarnings("unchecked")
		class CustomHandler<E> implements ResultHandler {
			List<E> resultList = new ArrayList<>();   
			@Override
			public void handleResult(ResultContext context) {
				resultList.add((E) context.getResultObject());
			}
		}
		CustomHandler customHandler = new CustomHandler();
		sqlSession.select(queryId, object, customHandler);
		
		return customHandler.resultList;
	}

	@Override
	public int selectCount(String queryId, Object object) {
		return sqlSession.selectOne(queryId, object);
	}

	@Override
	public Object selectOne(String queryId, Object object) {
		return sqlSession.selectOne(queryId, object);
	}

	@Override
	public int insert(String queryId, Object object) throws Exception {
		int retVal = 0;
		try {
			retVal = sqlSession.insert(queryId, object);
		} catch (DataAccessException dae) {
			logger.error("****** DataAccessException : {} ", dae.getMessage());
			throw dae;
		} catch (Exception e) {
			logger.error("****** Exception : {}", e.getMessage());
			throw e;
		}
		return retVal;
	}

	@Override
	public int update(String queryId, Object object) {
		return sqlSession.update(queryId, object);
	}

	@Override
	public int delete(String queryId, Object object) {
		return sqlSession.delete(queryId, object);
	}

	@Override
	public Object selectOneTable(Object obj) throws Exception {

		String query = SchemeUtil.getSelectQueryStr(obj);

		Map<String, String> map = new HashMap<String, String>();
		map.put("query", query);

		Map<String, Object> result = sqlSession.selectOne("ccs.common.selectOneTable", map);

		if (result != null) {
			return CommonUtil.convertMapToObject(result, obj.getClass());
		} else {
			return null;
		}
	}

	@Override
	public int insertOneTable(Object obj) throws Exception {
		Map<String, String> map = Maps.newHashMap();
		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);
		String tableName = CommonUtil.convertTableNotation(SchemeUtil.getClassNameFromClazz(obj.getClass()));// table 표기법 변환
		this.getInsertMaxSelectStr(tableName, fields, map); // max+1 query

		int insertCnt = 0;
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		if (!StringUtils.isEmpty(map.get("selectKey"))) {
			this.setSelectkey(obj, map);
			insertCnt = mapper.insertOneTable(obj);
			this.setFieldKey(obj, map); // pk 값 세팅 BaseEntity.id ==> 각테이블의 pk 로...
		} else {
			insertCnt = mapper.insertOneTableWithKey(obj);
		}
		return insertCnt;
	}
	
//	@Override
//	public int insertOneTableBatch(Object obj) throws Exception {
//		Map<String, String> map = Maps.newHashMap();
//		List<TableClassColumnInfo> fields = SchemeUtil.makeColumnInfo(obj);
//		String tableName = CommonUtil.convertTableNotation(SchemeUtil.getClassNameFromClazz(obj.getClass()));// table 표기법 변환
//		this.getInsertMaxSelectStr(tableName, fields, map); // max+1 query
//		
//		int insertCnt = 0;
//		UserMapper mapper = sqlSessionBatch.getMapper(UserMapper.class);
//		if (!StringUtils.isEmpty(map.get("selectKey"))) {
//			this.setSelectkey(obj, map);
//			insertCnt = mapper.insertOneTable(obj);
//			this.setFieldKey(obj, map); // pk 값 세팅 BaseEntity.id ==> 각테이블의 pk 로...
//		} else {
//			insertCnt = mapper.insertOneTableWithKey(obj);
//		}
//		return insertCnt;
//	}
	
	@Override
	public int updateOneTable(Object obj) throws Exception {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		return mapper.updateOneTable(obj);
	}
	
	/**
	 * Changes the annotation value for the given key of the given annotation to newValue and returns the previous value.
	 */
	@SuppressWarnings("unchecked")
	public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
		Object handler = Proxy.getInvocationHandler(annotation);
		Field f;
		try {
			f = handler.getClass().getDeclaredField("memberValues");
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(e);
		}
		f.setAccessible(true);
		Map<String, Object> memberValues;
		try {
			memberValues = (Map<String, Object>) f.get(handler);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		Object oldValue = memberValues.get(key);
		if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
			throw new IllegalArgumentException();
		}
		memberValues.put(key, newValue);
		return oldValue;
	}	

	@Override
	public int deleteOneTable(Object obj) throws Exception {

		String query = SchemeUtil.getDeleteQueryStr(obj);

		Map<String, String> map = new HashMap<String, String>();
		map.put("query", query);

		return sqlSession.delete("ccs.common.deleteOneTable", map);
	}

	/**
	 * DB시퀀스 조회 - TODO - 정리
	 * 
	 * @Method Name : getDbSequence
	 * @author : Administrator
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param tableName
	 * @return
	 */
	public Object getDbSequence(Object obj) throws Exception {
		Map<String, String> map = Maps.newHashMap();
		SchemeUtil.getInsertQueryStr(obj, map);
		String key = sqlSession.selectOne("ccs.common.getDbSequence", map.get("selectMaxPlusOne"));
		map.put("key", key);
		this.setFieldKey(obj, map);
		return obj;
	}
	
	private void getInsertMaxSelectStr(String tableName, List<TableClassColumnInfo> fields, Map<String, String> map) throws Exception {
		String whereStr = "";
		String maxColumn = "";
		String maxClassColumn = "";
		String selectMaxPlusOne = "";

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
			if (maxColumn.endsWith("_ID")) {
				maxColumn = "TO_NUMBER(" + maxColumn + ")";
			}
			selectMaxPlusOne = "SELECT NVL (MAX (" + maxColumn + ") + 1, 1) FROM " + tableName + whereStr;
		}
		map.put("keyName", maxClassColumn);
		map.put("selectKey", selectMaxPlusOne);
	}
	
	@SuppressWarnings("rawtypes")
	private void setFieldKey(Object obj, Map<String, String> map) throws Exception {
		if (!StringUtils.isEmpty(map.get("keyName"))) {
			Field idField = ReflectionUtils.findField(obj.getClass(), "id");
			Field field = ReflectionUtils.findField(obj.getClass(), map.get("keyName"));	// BaseEntity.id
			ReflectionUtils.makeAccessible(idField);
			ReflectionUtils.makeAccessible(field);
			
			for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
				if (field.getType().equals(entry.getKey())) {
					ReflectionUtils.setField(field, obj, entry.getValue().convert(idField.get(obj), null));
					break;
				}
			}
		}
	}
	@SuppressWarnings("rawtypes")
	private void setSelectkey(Object obj, Map<String, String> map) {
		if (!StringUtils.isEmpty(map.get("selectKey"))) {
			Field field = ReflectionUtils.findField(obj.getClass(), "selectKey");	// BaseEntity.keyName
			ReflectionUtils.makeAccessible(field);
			
			for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
				if (field.getType().equals(entry.getKey())) {
					ReflectionUtils.setField(field, obj, entry.getValue().convert(map.get("selectKey"), null));
					break;
				}
			}
		}
	}	


	@Deprecated
	@Override
	public int updateOne(Object obj) throws Exception {
		String className = SchemeUtil.getClassNameFromClazz(obj.getClass());// 클래스 명
		return sqlSession.update("common.update." + className, obj);
	}

	@Override
	public int insertOne(Object obj) throws Exception {

		Map<String, String> map = Maps.newHashMap();
		String maxPlusOneQry = SchemeUtil.getInsertQueryStr2(obj, map);
		Map<String, String> param = Maps.newHashMap();

		if (CommonUtil.isNotEmpty(maxPlusOneQry)) {
			param.put("maxPlusOneQry", maxPlusOneQry);

			String maxNo = sqlSession.selectOne("common.insert.max", param);

			Field field = ReflectionUtils.findField(obj.getClass(), map.get("keyName"));
			ReflectionUtils.makeAccessible(field);
			for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
				if (field.getType().equals(entry.getKey())) {
					ReflectionUtils.setField(field, obj, entry.getValue().convert(maxNo, null));
					break;
				}
			}
		}

		String className = SchemeUtil.getClassNameFromClazz(obj.getClass());// 클래스 명

		int retVal = sqlSession.insert("common.insert." + className, obj);

		return retVal;
	}

}
