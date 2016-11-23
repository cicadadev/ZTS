package intune.gsf.common.utils;

import java.util.concurrent.Executors;

public class BatchUtil {

	public static SerialExecutor getSerialExecutor() {
		return new SerialExecutor(Executors.newSingleThreadScheduledExecutor() );
	}

}
