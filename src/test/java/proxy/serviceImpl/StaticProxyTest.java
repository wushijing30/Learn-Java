package proxy.serviceImpl;

import junit.framework.TestCase;
import proxy.service.Hello;

/**
 * Created by wusj on 2017/10/28.
 */
public class StaticProxyTest extends TestCase {
    public void testSys() throws Exception {
        Hello helloProxy = new StaticProxy();
        helloProxy.sys("mol");
    }

}