package intune.gsf.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RestParam {

	Map<String, String>	parameter	= new HashMap<String, String>();
	Map<String, String>	header		= new HashMap<String, String>();

	private String		url;

	public RestParam(String url) {

		this.url = url;

	}

	public RestParam(String url, Object data) {

		this.url = url;

		try {
			this.parameter = getObjectToMap(data);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	private Map<String, String> getObjectToMap(Object obj)

			throws IllegalArgumentException, IllegalAccessException {

		Field[] fields = obj.getClass().getDeclaredFields();

		Map<String, String> resultMap = new HashMap<String, String>();

		for (int i = 0; i <= fields.length - 1; i++) {

			if ("serialVersionUID".equals(fields[i].getName())) {

				continue;

			}
//			System.out.println(fields[i].getName());
//			System.out.println(fields[i].get(obj).toString());
			fields[i].setAccessible(true);

			resultMap.put(fields[i].getName(), fields[i].get(obj).toString());

		}

		return resultMap;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

}
