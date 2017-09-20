package wusj.jcip.chapter7;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrimeGeneratorTest {

	/**
	 * 生成素数的程序运行一秒
	 * @return 一个素数列表
	 */
	List<BigInteger> aSecondOfPrimes() throws InterruptedException {
		PrimeGenerator generator0 = new PrimeGenerator();
		new Thread(generator0).start();
		new Thread(generator0).start();
		new Thread(generator0).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} finally{
			generator0.cancel();
		}
		return generator0.get();
	}
	
	public static void main(String[] args) {
		PrimeGeneratorTest prime = new PrimeGeneratorTest();
		try {
			List<BigInteger> primeList = prime.aSecondOfPrimes();
			for (BigInteger bigInteger : primeList) {
					System.out.println(bigInteger.toString());
				}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
