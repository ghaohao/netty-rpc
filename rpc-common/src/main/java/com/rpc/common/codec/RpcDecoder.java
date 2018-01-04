package com.rpc.common.codec;

import com.rpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午6:04
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;
    private static final int HEAD_LENGTH = 4;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {

        //头字节长度是4
        if(in.readableBytes() < HEAD_LENGTH){
            return;
        }

        //标记当前readIndex的位置
        in.markReaderIndex();

        //读取传送来的消息的长度，readInt方法会增加4
        int dataLength = in.readInt();

        //如果消息体的长度是0则关闭连接
        if(dataLength < 0){
            ctx.close();
        }

        //读到的消息体长度如果小于传送过来的消息长度，则resetReaderIndex;
        //需要配合markReaderIndex使用，重置到mark的位置
        if(in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];

        //读取消息体
        in.readBytes(body);

        //将消息反序列化
        out.add(SerializationUtil.deserialize(body, clazz));
    }
}