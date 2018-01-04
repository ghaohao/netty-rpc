package com.rpc.client.sample;

import com.rpc.client.RpcProxy;
import com.rpc.common.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
            RpcProxy rpcProxy = context.getBean(RpcProxy.class);

            HelloService helloService = rpcProxy.create(HelloService.class, "1.0.0");
            String result = helloService.hello("Hello World!");
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
