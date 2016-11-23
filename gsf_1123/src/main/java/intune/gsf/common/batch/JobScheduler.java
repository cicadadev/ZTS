package intune.gsf.common.batch;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Job Scheduler Class
 * <pre>
 *  - /WEB-INF/config/spring/context/context-scheduler.xml
 * </pre>
 * @author ditto
 *
 */
@Component
public class JobScheduler {
	
	public void sampleJob() {
		System.out.println("this method executed at every 5 sec. Current time is : " + new Date());
	}

}
