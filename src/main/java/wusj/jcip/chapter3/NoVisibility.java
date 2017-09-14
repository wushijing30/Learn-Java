package wusj.jcip.chapter3;

import java.util.concurrent.ConcurrentHashMap;

import org.omg.CORBA.PRIVATE_MEMBER;

public class NoVisibility {
	private static boolean ready;
	private static int number;
	
	private static class ReaderThread extends Thread{
		public void run(){
			while(!ready)
				Thread.yield();
			System.out.println(Thread.currentThread().getName() + ": " + number);
		}
	}
	
	public static void main(String[] args) {
		new ReaderThread().start();
		new ReaderThread().start();
		number = 42;
		ready = true;
	}
}
