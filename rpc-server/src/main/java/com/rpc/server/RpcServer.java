package com.rpc.server;

import com.rpc.common.codec.RpcDecoder;
import com.rpc.common.codec.RpcEncoder;
import com.rpc.common.model.RpcRequest;
import com.rpc.common.model.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午5:28
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    private Map<String, Object> handlerMap = new HashMap<>();
    private String serverAddress;

    public RpcServer(String serverAddress){
        this.serverAddress = serverAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcServerHandler(handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            String[] split = serverAddress.split(":");
            ChannelFuture future = bootstrap.bind(split[0], Integer.valueOf(split[1])).sync();

            LOGGER.debug("server started...");

            //TODO 需要优化
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> map = ctx.getBeansWithAnnotation(RpcService.class);
        if(MapUtils.isNotEmpty(map)){
            for(Object obj : map.values()){
                RpcService service = obj.getClass().getAnnotation(RpcService.class);
                String serviceName = service.value().getName();
                String version = service.version();
                if(StringUtils.isNotEmpty(version)){
                    serviceName += "-" + version;
                }
                handlerMap.put(serviceName, obj);
            }
        }
    }
}
