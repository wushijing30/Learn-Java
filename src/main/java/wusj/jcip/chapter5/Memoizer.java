package wusj.jcip.chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
/**
 * 缓存器
 * 适用于那些计算过长复杂的任务
 * @param <A>
 * @param <V>
 */
public class Memoizer<A, V> implements Computable<A, V>{
	//先有一个缓存的容器
	private final ConcurrentMap<A, FutureTask<V>> cache 
		= new ConcurrentHashMap<A, FutureTask<V>>();
	//可计算的具体实现
	private final Computable<A, V> c;
	
	public Memoizer(Computable<A, V> c) {
		this.c = c;
	}
	
	public V compute(final A arg) throws InterruptedException {
		while(true){
			Future<V> f = cache.get(arg);
			if(f == null){
				Callable<V> eval = new Callable<V>() {
					public V call() throws InterruptedException{
						return c.compute(arg);
					}
				};
				FutureTask<V> ft = new FutureTask<V>(eval);
				//putIfAbsent()是原子操作
				f = cache.putIfAbsent(arg, ft);
				//之前不存在ft,则开始ft.run()
				if(f == null){
					f = ft;
					ft.run();//实际是调用call()
				}
			}
			try {
				return f.get();//如果还在计算，则阻塞
			} catch (CancellationException e) {
				//如果任务取消，则移除相应的计算结果
				cache.remove(arg, f);
			} catch (ExecutionException e){
				throw launderThrowable(e.getCause());
			}
		}
	}
	
	public static RuntimeException launderThrowable(Throwable t) {
		if(t instanceof RuntimeException){
			return (RuntimeException)t;
		}else if(t instanceof Error){
			throw (Error)t;
		}else{
			throw new IllegalStateException("Not uncheck", t);
		}
	}
	
}
