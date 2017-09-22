package wusj.jcip.chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �嵥7.11 ��Thread�У�ͨ����дinterrupt����װ�Ǳ�׼ȡ��
 *
 */
public class ReaderThread extends Thread{
	private static final int BUFSZ = 2048;
	private volatile boolean isTimeout;
	private final Socket socket;
	private final InputStream in;
	public ReaderThread(Socket s) throws IOException {
		this.socket = s;
		this.in = socket.getInputStream();
	}
	
	public boolean isTimeout() {
		return this.isTimeout;
	}
	
	public void setTimeout(boolean isTimeout) {
		this.isTimeout = isTimeout;
	}
	//��дinterrupt����
	//��Ҫ�ж�������socket-IO��������������Ӧ�ж�����
	//����ͨ���ر�socket,ʹ����������
	@Override
	public void interrupt() {
		try{
			socket.close();
		}catch (IOException e){
			//���Ը��쳣
		}finally{
			super.interrupt();
		}
	}
	
	@Override
	public void run() {
		try {
			byte[] buf = new byte[BUFSZ];
			while(true){
				System.out.println("read blocking!");
				int count = in.read(buf);
				if(count < 0)
					break;
				else if(count > 0){
					processBuffer(buf, count);
				}
			}
		} catch (Exception e) {
			//�����߳��˳�
			e.printStackTrace();
		}
	}
	
	public void processBuffer(byte[] buf, int count) {
		setTimeout(false);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(new Date()) + "-�������������" + new String(buf));
	}

}
