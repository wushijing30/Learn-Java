package wusj.jcip.chapter7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
/**
 * 通过发出中断请求取消任务
 */
public class PrimeProducer extends Thread{
	private final BlockingQueue<BigInteger> queue;
	public PrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			BigInteger p = BigInteger.ONE;
			while(!Thread.currentThread().isInterrupted()){
				queue.put(p = p.nextProbablePrime());
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
	
	public void cancel() {
		interrupted();
	}

}
