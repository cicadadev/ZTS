package intune.gsf.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class SearchVO extends HashMap<String, Object> {

	private static final long serialVersionUID = 927550403170508492L;
	
	@Override
	public Object put(String key, Object value) {
		return super.put(key, value);
	}

	@Override
	public Object get(Object key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		if(obj instanceof String) {
			String str = (String) obj;
			
			if(str.trim().length() == 0) {
				return null;
			}
		}
		
		return obj;
	}
	
	public Integer getInt(String key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		try {
			return (Integer) obj;
		} catch(ClassCastException e) {
			if(obj instanceof String) {
				return Integer.parseInt((String) obj);
			} else {
				throw e;
			}
		}
	}
	
	public Double getDouble(String key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		return (Double) obj;
	}
	
	public Long getLong(String key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		return (Long) obj;
	}
	
	public BigDecimal getBigDecimal(String key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		return (BigDecimal) obj;
	}
	
	public Date getDate(String key) {
		Object obj = super.get(key);
		
		if(obj == null) {
			return null;
		}
		
		return (Date) obj;
	}


}
