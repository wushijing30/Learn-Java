package wusj.jcip.support;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wusj.jcip.chapter7.ReaderThread;

public class SocketServer {
	private static final int PORT = 12345;//监听的端口
	private static ReaderThread readerThread;
	private static ScheduledExecutorService executor;
	
	public static void start(){
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			while(true){
				//阻塞，直到有客户端接入
				Socket client = serverSocket.accept();
				//处理客户端请求
				readerThread = new ReaderThread(client);
				readerThread.start();
				//5s后中断, 
				//存在问题:如果5s内有产生新的请求，就改变了readerThread，会错误的中断新来的任务？？？？？？
				executor = Executors.newScheduledThreadPool(1);
				executor.scheduleWithFixedDelay(new Runnable() {
					public void run() {
						if(readerThread.isTimeout()){
							readerThread.interrupt();
						}else{
							readerThread.setTimeout(true);
						}
					}
				}, 5, 5, TimeUnit.SECONDS);
			}
		} catch (IOException e) {
			System.out.println("服务器异常");
			e.printStackTrace();
		}finally{
			try {
				if(serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				System.out.println("关闭服务器");
				e.printStackTrace();
			}
		}
	}
}
