package wusj.jcip.chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
/**
 * ������
 * ��������Щ����������ӵ�����
 * @param <A>
 * @param <V>
 */
public class Memoizer<A, V> implements Computable<A, V>{
	//����һ�����������
	private final ConcurrentMap<A, FutureTask<V>> cache 
		= new ConcurrentHashMap<A, FutureTask<V>>();
	//�ɼ���ľ���ʵ��
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
				//putIfAbsent()��ԭ�Ӳ���
				f = cache.putIfAbsent(arg, ft);
				//֮ǰ������ft,��ʼft.run()
				if(f == null){
					f = ft;
					ft.run();//ʵ���ǵ���call()
				}
			}
			try {
				return f.get();//������ڼ��㣬������
			} catch (CancellationException e) {
				//�������ȡ�������Ƴ���Ӧ�ļ�����
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
