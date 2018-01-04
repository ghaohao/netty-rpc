package com.rpc.common.codec;

import com.rpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午6:04
 */
public class RpcEncoder extends MessageToByteEncoder{
    private Class<?> clazz;

    public RpcEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object object, ByteBuf byteBuf)
            throws Exception {
        if(clazz.isInstance(object)){
            byte[] data = SerializationUtil.serialize(object);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}