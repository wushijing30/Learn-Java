package wusj.jcip.support;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private static final String SERVER_IP_ARR = "localhost"; //服务器地址
	private static final int SERVER_PORT = 12345; //服务器端口
	
	public static void start(){
		Socket socket = null;
		try{
			socket = new Socket(SERVER_IP_ARR, SERVER_PORT);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			while(true){
				System.out.println("input：");
				String userInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.write(userInput.getBytes());
			}
		}catch(Exception e){
			System.out.println("客户端异常" + e.getMessage());
			e.printStackTrace();
			
		}finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		start();
	}
}
