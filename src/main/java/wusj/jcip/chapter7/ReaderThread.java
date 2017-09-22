package wusj.jcip.chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 清单7.11 在Thread中，通过覆写interrupt来封装非标准取消
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
	//覆写interrupt方法
	//想要中断阻塞于socket-IO的任务，他并不响应中断请求
	//可以通过关闭socket,使任务不再阻塞
	@Override
	public void interrupt() {
		try{
			socket.close();
		}catch (IOException e){
			//忽略该异常
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
			//允许线程退出
			e.printStackTrace();
		}
	}
	
	public void processBuffer(byte[] buf, int count) {
		setTimeout(false);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(new Date()) + "-服务器端输出：" + new String(buf));
	}

}
