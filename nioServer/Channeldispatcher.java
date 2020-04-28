package com.example.manager.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Channeldispatcher
{

    public static void dispatcher( SelectionKey key )
    {
        SocketChannel channel;

        if( key.isAcceptable() )
        {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key
                    .channel();
            try
            {
                channel = serverChannel.accept();
                System.out.println( "接受新连接" + channel.getRemoteAddress() );
                //此处要设置 阻塞模式 false
                channel.configureBlocking( false );
                if( !channel.isBlocking() )
                    channel.register( key.selector(), SelectionKey.OP_READ );
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
            byte[] body = "响应体+server".getBytes();

            ByteBuffer wrapHead = ByteBuffer.wrap( head );
            ByteBuffer wrapbody = ByteBuffer.wrap( body );

            try
            {
                while( wrapHead.hasRemaining() )
                    channel.write( new ByteBuffer[] {wrapHead, wrapbody} );
                channel.register( key.selector(), SelectionKey.OP_READ );
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
