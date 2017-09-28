package wusj.jcip.chapter7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*
 * �嵥7.11 ��֧�ֹرյ�������-��������־����
 */
public class LogWriter {
	private final BlockingQueue<String> queue;
	private final LoggerThread logger;
	private static final int CAPACITY = 1000;
	
	public LogWriter(Writer writer) {
		this.queue = new LinkedBlockingQueue<String>(CAPACITY);
		this.logger = new LoggerThread(writer);
	}
	
	public void start(){
		logger.start();
	}
	
	public void log(String msg) throws InterruptedException{
		queue.put(msg);
	}
	private class LoggerThread extends Thread{
		private final PrintWriter writer;
		
		public LoggerThread(Writer writer){
			this.writer = new PrintWriter(writer, true);//�Զ�flush
		}
		@Override
		public void run() {
			try {
				while(true){
					writer.println(queue.take());
				}
			} catch (InterruptedException e) {
				//����
			}finally{
				writer.close();
			}
		}
	}
}