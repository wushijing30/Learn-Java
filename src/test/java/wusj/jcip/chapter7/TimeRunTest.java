package wusj.jcip.chapter7;

import java.util.concurrent.TimeUnit;

public class TimeRunTest {
	class TimedTask implements Runnable{
		public void run() {
			int i = 0;
			while (true) {
				// 如果一个任务是可取消的，那么它应该可以对 Thread 的 interrupt() 方法做出被取消时的响应。
				// System.out.println()没有对 Thread 的 interrupt() 方法做出被取消时的响应
				// 所以该任务是不可取消的
				//System.out.println(i++); 
				//这里应该调用一些可相应中断的方法
				try {
					Thread.sleep(Long.MAX_VALUE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("@@@@@@@@@@@@@@@@@");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		TimeRunTest test = new TimeRunTest();
		TimedTask timedTask = test.new TimedTask();
//		TimeRun.timeRunV1(timedTask, 1, TimeUnit.SECONDS);
		try {
			TimeRun.timeRunV2(timedTask, 1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			TimeRun.timeRunV3(timedTask, 1, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
}
