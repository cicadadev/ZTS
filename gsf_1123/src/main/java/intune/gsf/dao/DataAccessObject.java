package intune.gsf.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface DataAccessObject<T> {
	
	public List<?> selectList(String queryId, Object object);
	
	public List<?> selectListWithHandler(String queryId, Object object);
	
	public int selectCount(String queryId, Object object);

	public Object selectOne(String queryId, Object object);

	public int insert(String queryId, Object object) throws Exception;

	public int update(String queryId, Object object);

	public int delete(String queryId, Object object);

	public Object selectOneTable(Object obj) throws Exception;
	
	public int insertOneTable(Object obj) throws Exception;
	
//	public int insertOneTableBatch(Object obj) throws Exception;

	public int updateOneTable(Object obj) throws Exception;

	public int deleteOneTable(Object obj) throws Exception;
	
	public Object getDbSequence(Object obj) throws Exception;

	public int insertOne(Object obj) throws Exception;

	public int updateOne(Object obj) throws Exception;

}
