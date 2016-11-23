package gcp.common;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DaoAspect {
	private final Log logger = LogFactory.getLog(getClass());

	@Around("execution(public * intune.gsf.dao.*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		Object[] args = joinPoint.getArgs();
		String queryId = "";
		for (Object arg : args) {
			if ("java.lang.String".equals(arg.getClass().getName())) {
				queryId = (String) arg;
				break;
			}

		}

		// 시작 시간
		long startTime = System.currentTimeMillis();

		Object obj = joinPoint.proceed();

		// 종료 시간
		long endTime = System.currentTimeMillis();
		float periodTime = (endTime - startTime) / 1000.0f;

		logger.debug("- 쿼리수행시간 : [" + queryId + "] : " + periodTime + "Sec   ");
		if (periodTime >= 5) {
			// 시간 출력
			logger.fatal("-------------------------------------------------------------------------------------------");
			logger.fatal("-  [매우심각]튜닝요청 바람[" + queryId + "]의 수행시간이 5초 이상 :  " + periodTime + "Sec   ");
			logger.fatal("-------------------------------------------------------------------------------------------");
		} else if (periodTime >= 1) {
			// 시간 출력
			logger.warn("-------------------------------------------------------------------------------------------");
			logger.warn("-  [심각]튜닝요청 바람[" + queryId + "]의 수행시간이 1초 이상 :  " + periodTime + "Sec   ");
			logger.warn("-------------------------------------------------------------------------------------------");
		} else if (periodTime >= 0.5) {
			// 시간 출력
			logger.warn("-------------------------------------------------------------------------------------------");
			logger.warn("-  튜닝요청 바람[" + queryId + "]의 수행시간이 0.5초 이상 :  " + periodTime + "Sec   ");
			logger.warn("-------------------------------------------------------------------------------------------");
		}
		return obj;
	}

	private String formatTime(long lTime) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(lTime);
		return (c.get(Calendar.HOUR_OF_DAY) + "시 " + c.get(Calendar.MINUTE) + "분 " + c.get(Calendar.SECOND) + "."
				+ c.get(Calendar.MILLISECOND) + "초");

	}
}