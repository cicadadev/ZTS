package gcp.common.interceptor;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import gcp.common.util.BoSessionUtil;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }) })
public class MybatisInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();

//		MappedStatement ms = (MappedStatement) args[0];

		Object param = (Object) args[1];

		//logger.debug("parameter : " + param);

		setDefaultParam(param);

//		logger.debug("MYBATIS PARAMETERS : =================> " + param);

//		BoundSql boundSql = ms.getBoundSql(param);
//
//		String sql = boundSql.getSql();
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

	private Object setDefaultParam(Object obj) {
		if (obj != null) {
			Class cls = obj.getClass();
			try {
				while (cls.getSuperclass() != null) {
					cls = cls.getSuperclass();
					//logger.debug(cls.getSimpleName());

					if (!BaseConstants.BASE_ENTITY_NAME.equals(cls.getSimpleName())) {
						continue;
					}

					Field[] fields = cls.getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						//logger.debug(field.getName());

						String system = Config.getString("system.type");

						if (!CommonUtil.isEmpty(field.get(obj))) {
							continue;
						}

						// uptId, istId 세팅
						if (BaseConstants.PARAM_UPDATE_ID.equals(field.getName())) {
							field.set(obj, SessionUtil.getLoginId());
						} else if (BaseConstants.PARAM_INSERT_ID.equals(field.getName())) {
							field.set(obj, SessionUtil.getLoginId());
						}

						try {

							if (BaseConstants.SYSTEM_BATCH.equals(system)) {// 배치일경우
								if (BaseConstants.PARAM_UPDATE_ID.equals(field.getName())) {
									field.set(obj, BaseConstants.DEFAULT_BATCH_USER_ID);
								} else if (BaseConstants.PARAM_INSERT_ID.equals(field.getName())) {
									field.set(obj, BaseConstants.DEFAULT_BATCH_USER_ID);
								}
							} else if (BaseConstants.SYSTEM_PO.equals(BoSessionUtil.getSystemType())) { // PO 일경우

								// po일때 system type
								if (BaseConstants.PARAM_SYSTEM.equals(field.getName())) {

									field.set(obj, BaseConstants.SYSTEM_PO);

								}
								// po일때 businessId
								if (BaseConstants.PARAM_BUSINESS_ID.equals(field.getName())
										&& BoSessionUtil.getBusinessId() != null
										&& BoSessionUtil.getBusinessId().length() > 0) {
									field.set(obj, BoSessionUtil.getBusinessId());
								}
							}


//						else if (BaseConstants.PARAM_STORE_ID.equals(field.getName())) {
//							field.set(obj, SessionUtil.getStoreId());
//						} else if (BaseConstants.PARAM_LANG_CD.equals(field.getName())) {
//							field.set(obj, SessionUtil.getLangCd());
//						}
						} catch (UsernameNotFoundException u) {
							//logger.error(u.getMessage(), u);
						}
					}

				}
			} catch (Exception e) {
				//logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return obj;
	}

}
