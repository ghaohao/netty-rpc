package com.rpc.client.sample;

import com.rpc.client.RpcProxy;
import com.rpc.facade.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
            RpcProxy rpcProxy = context.getBean(RpcProxy.class);

            HelloService helloService = rpcProxy.create(HelloService.class);
            String result = helloService.hello("World");
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
