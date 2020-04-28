package com.example.manager.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer
{
    public static void main( String[] args ) throws IOException
    {
        Selector selector = Selector.open();

        ServerSocketChannel socketChannel = ServerSocketChannel.open();

        socketChannel.configureBlocking( false );

        socketChannel.bind( new InetSocketAddress( 80 ) );

        socketChannel.register( selector, SelectionKey.OP_ACCEPT );

        while( true )
        {
            int n = selector.select();
            if( n == 0 )
                continue;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while( iterator.hasNext() )
            {
                SelectionKey selectionKey = iterator.next();
                Channeldispatcher.dispatcher( selectionKey );
                //移除掉已经处理的事件，此处移除的是副本,并不是真正的移除selector上的key
                iterator.remove();
            }
        }

    }

}
