package wusj.jcip.chapter7;

import java.util.concurrent.TimeUnit;

public class TimeRunTest {
	class TimedTask implements Runnable{
		public void run() {
			int i = 0;
			while (true) {
				// ���һ�������ǿ�ȡ���ģ���ô��Ӧ�ÿ��Զ� Thread �� interrupt() ����������ȡ��ʱ����Ӧ��
				// System.out.println()û�ж� Thread �� interrupt() ����������ȡ��ʱ����Ӧ
				// ���Ը������ǲ���ȡ����
				//System.out.println(i++); 
				//����Ӧ�õ���һЩ����Ӧ�жϵķ���
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
