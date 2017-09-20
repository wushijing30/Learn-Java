package wusj.jcip.chapter7;

import java.util.concurrent.CountDownLatch;

import wusj.jcip.chapter5.HarnessTask;
/**
 * 闭锁
 * @author wusj
 *
 */
public class HarnessTest {
	public static long timeTasks(int nThread, final Runnable task) 
		   throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThread);
		for(int i = 0; i < nThread; i++){
			Thread t = new Thread(){
				public void run() {
					try {
						startGate.await();//子线程正在等待计数器减到0
						try {
							task.run();
						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException e) {
						// TODO: handle exception
					}
				}
			};
			t.start();
		}
		
		long start = System.currentTimeMillis();
		startGate.countDown();//减1， startGete计数器为0， 子线程开始任务
		endGate.await();//主线程等待所有子线程完成任务
		long end = System.currentTimeMillis();
		return end - start;
	}
	
	public static void main(String[] args) {
		try {
			long time = timeTasks(3, new HarnessTask());
			System.out.println(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
