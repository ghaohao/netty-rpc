package com.rpc.server;

import com.rpc.common.model.RpcRequest;
import com.rpc.common.model.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午4:21
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);
    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        try {
            response.setResult(handle(rpcRequest));
        }catch (Exception e){
            response.setException(e);
        }

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException {
        String interfaceName = rpcRequest.getInterfaceName();
        String version = rpcRequest.getVersion();
        if(version != null){
            interfaceName += "-" + version;
        }

        Object bean = handlerMap.get(interfaceName);
        if(bean == null){
            throw new RuntimeException("cannot find service bean by ket: %s" + interfaceName);
        }

        FastClass fastClass = FastClass.create(bean.getClass());
        FastMethod method = fastClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(bean, rpcRequest.getParameters());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}
