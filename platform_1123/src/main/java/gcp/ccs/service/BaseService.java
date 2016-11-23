package gcp.ccs.service;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import intune.gsf.dao.DataAccessObject;

@Service
public class BaseService {
	@Autowired
	private ApplicationContext context;

	public Object getThis() throws Exception {
		return context.getBean(this.getClass());
		//return new Object();
	}

//	@Resource(name="dao")
	@Autowired
	@Qualifier("dao")
	protected DataAccessObject<T>	dao;
	
	@Autowired
	@Qualifier("erp")
	protected DataAccessObject<T>	erp;
	
	@Autowired
	@Qualifier("edi")
	protected DataAccessObject<T>	edi;

	@Autowired
	@Qualifier("tms")
	protected DataAccessObject<T>	tms;

	@Autowired
	@Qualifier("pos")
	protected DataAccessObject<T>	pos;

	public Object selectOneTable(Object obj) throws Exception {
		return dao.selectOneTable(obj);
	}

	public int insertOneTable(Object obj) throws Exception {
		return dao.insertOneTable(obj);
	}

	public int updateOneTable(Object obj) throws Exception {
		return dao.updateOneTable(obj);
	}

	public int deleteOneTable(Object obj) throws Exception {
		return dao.deleteOneTable(obj);
	}

}
