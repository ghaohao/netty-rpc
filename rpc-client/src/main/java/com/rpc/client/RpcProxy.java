package com.rpc.client;

import com.rpc.common.model.RpcRequest;
import com.rpc.common.model.RpcResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 18/1/3 上午10:34
 */
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private String address;

    public RpcProxy(String address){
        this.address = address;
    }

    public <T> T create(final Class<?> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setRequestId(UUID.randomUUID().toString());
                request.setInterfaceName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);

                //TODO 服务发现

                if(StringUtils.isEmpty(address)){
                    throw new RuntimeException("no server address found");
                }

                String host = address.split(":")[0];
                int port = Integer.valueOf(address.split(":")[1]);

                RpcClient client = new RpcClient(host, port);
                RpcResponse response = client.send(request);

                if(response == null){
                    throw new RuntimeException("no response return");
                }

                if(response.hasException()){
                    throw response.getException();
                }

                return response.getResult();
            }
        });
    }
}
