package proxy.serviceImpl;

import proxy.service.Hello;

/**
 * Created by wusj on 2017/10/28.
 */
public class HelloImpl implements Hello {
    @Override
    public void sys(String name) {
        System.out.println("Hello! " + name);
    }
}
