package com.rpc.service.impl;

import com.rpc.common.service.HelloService;
import com.rpc.server.RpcService;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 18/1/3 下午12:06
 */
@RpcService(value = HelloService.class, version = "1.0.0")
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String name) {
        return name;
    }
}
