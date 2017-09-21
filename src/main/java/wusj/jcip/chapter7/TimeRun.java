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
 * �ڹ涨ʱ����ִ������
 */
public class TimeRun {
	private static final ScheduledExecutorService cancelExec = new ScheduledThreadPoolExecutor(1);
	private static final ExecutorService taskExec = Executors.newCachedThreadPool();
	/*
	 * ���ڲ�֪�������ߵ��жϲ��ԣ������߿��ܲ���Ӧ�жϣ�������ʹ��ʱҲ��ִ����ȥ
	 */
	public static void timeRunV1(Runnable r, long timeout, TimeUnit unit){
		//��ȡ��ǰ�߳�
		final Thread taskThread = Thread.currentThread();
		cancelExec.schedule(new Runnable() {
			public void run() {
				//�ڳ�ʱ֮�󷢳��ж�����
				taskThread.interrupt();
			}
		}, timeout, unit);
		r.run();
	}
	
	public static void timeRunV2(final Runnable r, long timeout, TimeUnit unit) 
			throws InterruptedException {
		//����ִ��������߳����Լ����жϲ��ԣ���������ִ���г��ֵ��쳣
		class RethrowableTask implements Runnable{
			private volatile Throwable t;
			public void run() {
				try{
					r.run();
				}catch(Throwable t){
					this.t = t;//��������ִ���г��ֵ��쳣
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
		//�ȴ����߳�r����֮����ܼ�������
		taskThread.join(unit.toMillis(timeout));
		//����������Ƿ�����쳣�������׳�
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
			//��ʹ�����Ѿ�������ȡ��Ҳ���޺���
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
