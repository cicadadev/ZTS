package intune.gsf.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;

import intune.gsf.dao.impl.UserProvider;

public interface UserMapper {
	@SelectKey(keyProperty = "id" ,before = true ,resultType = String.class ,statement = "${selectKey}")
	@InsertProvider(type = UserProvider.class ,method = "insert")
	public int insertOneTable(Object obj) throws Exception;
	
	@InsertProvider(type = UserProvider.class ,method = "insert")
	public int insertOneTableWithKey(Object obj) throws Exception;
	
	@UpdateProvider(type = UserProvider.class ,method = "update")
	public int updateOneTable(Object obj) throws Exception;
}
