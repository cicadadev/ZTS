package intune.gsf.common.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final int DEFAULT_CACHE_EXPIRE_TIME = 1000 * 10;
	static int cacheExpireTime = DEFAULT_CACHE_EXPIRE_TIME;

	private static Logger			logger						= LoggerFactory.getLogger(Config.class);

	private static List<Properties>	props						= null;

	static class Null {

	}

	static final Null NULL = new Null();

	/**
	 * 주어진 위치에서 설정파일을 읽는다.
	 * <br>
	 * ex) Config.loadConfig("/WEB-INF/classes/system/config.xml",this.getServletContext());
	 * @param configLocation
	 */
//	public static void loadConfig(String configLocation, ServletContext servletContext) {
//
//		String location = configLocation;
//
//		if (location != null) {
//
//			if (servletContext != null) {
//				if (!ResourceUtils.isUrl(location)) {
//					location = SystemPropertyUtils.resolvePlaceholders(location);
//					try {
//						location = WebUtils.getRealPath(servletContext, location);
//					} catch (FileNotFoundException e) {
//						log.error("'{}' file not found.", location);
//					}
//				}
//			}
//			
//			String path = org.springframework.util.SystemPropertyUtils.resolvePlaceholders(location).trim();
//
//			props = new Properties();
//
//			FileInputStream fis;
//			try {
//				fis = new FileInputStream(path);
//				props.loadFromXML(fis);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (InvalidPropertiesFormatException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}



	public static Long getLong(String cfg) {
		String tmp = getString(cfg);
		if (tmp == null || "".equals(tmp.trim())) {
			return null;
		}
		return Long.parseLong(tmp);
	}

	public static String getString(String key) {
		String value = null;
		for (Properties prop : props) {
			String value1 = prop.getProperty(key);
//			logger.debug("============================= get value :" + value1);
			if (value1 != null && value1.length() > 0) {
				value = value1;
			}
		}
//		logger.debug("============================= get value result :" + value);
		return value;
	}


	public static void setProps(List<Properties> props) {
		for (Properties prop : props) {
//			logger.debug("============================= loading properties :" + prop);

			Iterator itr = prop.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				String value = prop.getProperty(key);

				for (int k = 0; k < value.length(); k++) {
					int idx = value.indexOf("${");					
					if (idx > -1) {
						String key2 = value.substring(idx + 2, value.indexOf("}"));
//						logger.debug("============================= change key :" + key2);
						String bindValue = prop.getProperty(key2);
//						logger.debug("============================= change value :" + bindValue);
						value = value.replace("${" + key2 + "}", bindValue);
					}else{
						prop.setProperty(key, value);
						break;
					}
				}
			}
//			logger.debug("============================= loading properties :" + prop);

		}
		Config.props = props;
	}

	public static Integer getInt(String key) {
		String tmp = getString(key);
		if (tmp == null || "".equals(tmp.trim())) {
			return null;
		}
		return Integer.parseInt(tmp);
	}

	public static Integer getInt(String key, Integer defaultValue) {
		String tmp = getString(key);
		if (tmp == null || "".equals(tmp.trim())) {
			return defaultValue;
		}
		return Integer.parseInt(tmp);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String tmp = getString(key);
		if (tmp == null || "".equals(tmp.trim())) {
			return defaultValue;
		}
		return Boolean.parseBoolean(tmp.trim());
	}

}
