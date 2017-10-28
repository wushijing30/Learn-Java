package proxy.serviceImpl;

import proxy.service.Hello;

/**
 * 代理模式
 * Created by wusj on 2017/10/26.
 */
public class StaticProxy implements Hello {

    private Hello hello;

    public StaticProxy(){
        hello = new HelloImpl();
    }

    @Override
    public void sys(String name) {
        before();
        System.out.println("Hello! " + name);
        after();
    }

    private void before(){
        System.out.println("Before");
    }

    private void after(){
       System.out.println("After");
    }
}
