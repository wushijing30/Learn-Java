package wusj.jcip.chapter7;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * 在规定时限内执行任务
 */
public class TimeRun {
	private static final ScheduledExecutorService cancelExec = new ScheduledThreadPoolExecutor(1);
	private static final ExecutorService taskExec = Executors.newCachedThreadPool();
	/*
	 * 由于不知道调用者的中断策略，调用者可能不响应中断，那任务即使超时也会执行下去
	 */
	public static void timeRunV1(Runnable r, long timeout, TimeUnit unit){
		//获取当前线程
		final Thread taskThread = Thread.currentThread();
		cancelExec.schedule(new Runnable() {
			public void run() {
				//在超时之后发出中断请求
				taskThread.interrupt();
			}
		}, timeout, unit);
		r.run();
	}
	
	public static void timeRunV2(final Runnable r, long timeout, TimeUnit unit) 
			throws InterruptedException {
		//用来执行任务的线程有自己的中断策略：保存任务执行中出现的异常
		class RethrowableTask implements Runnable{
			private volatile Throwable t;
			public void run() {
				try{
					r.run();
				}catch(Throwable t){
					this.t = t;//保存任务执行中出现的异常
				}
			}
			
			void rethrow(){
				if(t != null){
					throw launderThrowable(t);
				}
			}
		};
		
		RethrowableTask task = new RethrowableTask();
		final Thread taskThread = new Thread(task);
		taskThread.start();
		cancelExec.schedule(new Runnable() {
			public void run() {
				taskThread.interrupt();
			}
		}, timeout, unit);
		//等待子线程r结束之后才能继续运行
		taskThread.join(unit.toMillis(timeout));
		//检查任务中是否出现异常，有则抛出
		task.rethrow();
	}
	
	public static void timeRunV3(Runnable r, long timeout, TimeUnit unit)
			throws InterruptedException{
		Future<?> task = taskExec.submit(r);
		try {
			task.get(timeout, unit);
		} catch (TimeoutException e) {

		}catch (ExecutionException e) {
			throw launderThrowable(e);
		}finally{
			//即使任务已经结束，取消也是无害的
			task.cancel(true);
		}
	}
	
	
	public static RuntimeException launderThrowable(Throwable t) {
		if(t instanceof RuntimeException){
			return (RuntimeException)t;
		}else if(t instanceof Error){
			throw (Error)t;
		}else{
			throw new IllegalStateException("Not uncheck", t);
		}
	}
}
