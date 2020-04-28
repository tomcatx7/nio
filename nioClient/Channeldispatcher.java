package com.example.executor.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Channeldispatcher
{
    //Set<ChannelHandlerImp> handlerImps = new HashSet<>();

    public static void dispatcher( SelectionKey key )
    {
        SocketChannel channel = (SocketChannel) key.channel();

        if( key.isConnectable() )
        {
            try
            {
                System.out.println( "客户端连接成功" + channel.getRemoteAddress() );
                channel.register( key.selector(), SelectionKey.OP_WRITE );
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }

        }
        else if( key.isReadable() )
        {
            channel = (SocketChannel) key.channel();
            ByteBuffer buff = ByteBuffer.allocate( 1 );
            try
            {
                while( buff.hasRemaining() )
                    channel.read( buff );
                System.out.println( "读到数据" + new String( buff.array() ) );
                channel.register( key.selector(), SelectionKey.OP_WRITE );
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
        else if( key.isWritable() )
        {
            channel = (SocketChannel) key.channel();
            byte[] head = "响应头".getBytes();
            byte[] body = "响应体+client".getBytes();

            ByteBuffer wrapHead = ByteBuffer.wrap( head );
            ByteBuffer wrapbody = ByteBuffer.wrap( body );

            try
            {
                while( wrapHead.hasRemaining() )
                {
                    channel.write( new ByteBuffer[] {wrapHead, wrapbody} );
                }

                channel.register( key.selector(), SelectionKey.OP_READ );
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
