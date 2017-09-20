package wusj.jcip.chapter7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator implements Runnable{
	private final List<BigInteger> primes = new ArrayList<BigInteger>();
	private volatile boolean cancelled;
	public void run() {
		BigInteger p = BigInteger.ONE;
		while(!cancelled){
			p = p.nextProbablePrime();
			//ArrayList 不是线程安全的
			synchronized (this) {
				primes.add(p);
			}
		}
	}
	public void cancel(){
		cancelled = true;
	}
	public synchronized List<BigInteger> get() {
		return new ArrayList<BigInteger>(primes);
	}
}
