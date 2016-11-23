
package intune.gsf.common.utils;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialExecutor implements Executor{

	private final Queue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
	private final ExecutorService executor;
	private Runnable active;

	public SerialExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public synchronized void execute(final Runnable r) {
		tasks.offer(new Runnable() {
			public void run() {
				try {
					r.run();
				} finally {
					scheduleNext();
				}
			}
		});

		if (active == null) {
			this.scheduleNext();
		}
	}

	private synchronized void scheduleNext() {
		if ( (active = tasks.poll() ) != null ) {
			executor.execute(active);
		}
		//else {
		//	executor.shutdown();
		//}
	}
}
