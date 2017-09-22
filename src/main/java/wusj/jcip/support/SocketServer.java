package wusj.jcip.support;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wusj.jcip.chapter7.ReaderThread;

public class SocketServer {
	private static final int PORT = 12345;//�����Ķ˿�
	private static ReaderThread readerThread;
	private static ScheduledExecutorService executor;
	
	public static void start(){
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			while(true){
				//������ֱ���пͻ��˽���
				Socket client = serverSocket.accept();
				//����ͻ�������
				readerThread = new ReaderThread(client);
				readerThread.start();
				//5s���ж�, 
				//��������:���5s���в����µ����󣬾͸ı���readerThread���������ж����������񣿣���������
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
			System.out.println("�������쳣");
			e.printStackTrace();
		}finally{
			try {
				if(serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				System.out.println("�رշ�����");
				e.printStackTrace();
			}
		}
	}
}
