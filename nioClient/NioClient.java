package com.example.executor.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient
{
    public static void main( String[] args ) throws IOException
    {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking( false );

        socketChannel.register( selector, SelectionKey.OP_CONNECT );

        socketChannel.connect( new InetSocketAddress( "127.0.0.1", 80 ) );


        while( true )
        {
            int n = selector.select();
            if( n == 0 )
                continue;
            //获取当前活跃的channel. 一个channel绑定一个selectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while( iterator.hasNext() )
            {
                SelectionKey selectionKey = iterator.next();
                //处理对应的活跃事件
                Channeldispatcher.dispatcher( selectionKey );
                //移除掉已经处理的事件，此处移除的是副本,并不是真正的移除selector上的key
                iterator.remove();

            }
        }
    }
}
