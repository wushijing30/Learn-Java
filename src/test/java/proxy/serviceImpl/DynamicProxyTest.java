package proxy.serviceImpl;

import junit.framework.TestCase;
import proxy.service.Hello;

import java.lang.reflect.Proxy;

/**
 * Created by wusj on 2017/10/28.
 */
public class DynamicProxyTest extends TestCase {
    public void testInvoke() throws Exception {
        Hello hello = new HelloImpl();
        DynamicProxy dynamicProxy = new DynamicProxy(hello);
        Hello helloProxy = (Hello) Proxy.newProxyInstance(
                hello.getClass().getClassLoader(),
                hello.getClass().getInterfaces(),
                dynamicProxy);
        helloProxy.sys("mol");
    }

}