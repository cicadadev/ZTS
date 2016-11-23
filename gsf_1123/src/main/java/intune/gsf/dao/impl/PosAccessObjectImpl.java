package intune.gsf.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SchemeUtil;
import intune.gsf.dao.DataAccessObject;

@Repository("pos")
// public class ErpAccessObjectImpl<T> extends SqlSessionDaoSupport implements
// DataAccessObject<T> {
public class PosAccessObjectImpl<T> implements DataAccessObject<T> {

	@Autowired
	private SqlSessionTemplate posSession; // mssql

	// @Override
	// @Resource(name = "mssqlSession")
	// public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
	// super.setSqlSessionFactory(sqlSession);
	// }

	@Override
	public List<?> selectList(String queryId, Object object) {
		return posSession.selectList(queryId, object);
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
		posSession.select(queryId, object, customHandler);
		
		return customHandler.resultList;
	}

	@Override
	public int selectCount(String queryId, Object object) {
		return posSession.selectOne(queryId, object);
	}

	@Override
	public Object selectOne(String queryId, Object object) {
		return posSession.selectOne(queryId, object);
	}

	@Override
	public int insert(String queryId, Object object) {
		return posSession.insert(queryId, object);
	}

	@Override
	public int update(String queryId, Object object) {
		return posSession.update(queryId, object);
	}

	@Override
	public int delete(String queryId, Object object) {
		return posSession.delete(queryId, object);
	}

	@Override
	public Object selectOneTable(Object obj) throws Exception {

		String query = SchemeUtil.getSelectQueryStr(obj);

		Map<String, String> map = new HashMap<String, String>();
		map.put("query", query);

		Map<String, Object> result = posSession.selectOne("ccs.common.selectOneTable", map);

		if (result != null) {
			return CommonUtil.convertMapToObject(result, obj.getClass());
		} else {
			return null;
		}
	}

	@Override
	public int insertOneTable(Object obj) throws Exception {

		Map<String, String> map = Maps.newHashMap();
		SchemeUtil.getInsertQueryStr(obj, map);

		return posSession.insert("ccs.common.insertOneTable", map);
	}

	@Override
	public int updateOneTable(Object obj) throws Exception {

		String query = SchemeUtil.getUpdateQueryStr(obj);

		Map<String, String> map = new HashMap<String, String>();
		map.put("query", query);

		return posSession.update("ccs.common.updateOneTable", map);
	}

	@Override
	public int deleteOneTable(Object obj) throws Exception {

		String query = SchemeUtil.getDeleteQueryStr(obj);

		Map<String, String> map = new HashMap<String, String>();
		map.put("query", query);

		return posSession.delete("ccs.common.deleteOneTable", map);
	}

	/**
	 * DB시퀀스 조회
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
		return "";
	}

	@Override
	public int insertOne(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateOne(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
//	public int insertOneTableBatch(Object obj) throws Exception {
//		// TODO Auto-generated method stub
//		return 0;
//	}

}
