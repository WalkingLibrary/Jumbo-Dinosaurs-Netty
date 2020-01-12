package com.jumbodinosaurs.netty;

import com.jumbodinosaurs.objects.HTTP.HTTPResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ResponseEncoder extends MessageToMessageEncoder<HTTPResponse>
{
    @Override
    protected void encode(ChannelHandlerContext context,
                          HTTPResponse response,
                          List<Object> out) throws Exception
    {
        ByteBuf buffer = context.alloc().buffer();
        buffer.writeBytes(response.getMessage().getBytes());
        if(response.getBytesOut() != null)
        {
            buffer.writeBytes(response.getBytesOut());
        }
        out.add(buffer);
    }
}
