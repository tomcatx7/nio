package com.zxj.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
	private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
	private static final int BUFFER_SIZE = 1024;
	private int port;
	private String host;
	private ServerSocketChannel ssc;
	
	public NioServer(String host,int port) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.port = port;
	}
	
	public void start() {
		ssc = null;
		try {
			//开启服务端管道
			ssc = ServerSocketChannel.open();
			ssc.bind(new InetSocketAddress(host, port));
			//开启selector监听事件
			Selector selector = Selector.open();
			//设置服务端管道为非阻塞模式
			ssc.configureBlocking(false);
			//服务端管道注册一个连接事件SelectionKey.OP_ACCEPT
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			SelectHanderContext hdc = new SelectHanderContext();
			while (true) {
				Set<SelectionKey> selkeys = null;
				//注册器开始监听管道事件
				selector.select();
				//获取触发事件的selectedKeys
				selkeys = selector.selectedKeys();
						
				for (Iterator<SelectionKey> it = selkeys.iterator();it.hasNext();) {
					//遍历key，处理对应key的事件
					hdc.doHandler(it.next(),selector);
					//处理完之后从迭代器中移除
					it.remove();
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if (ssc !=null) {
				try {
					ssc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new NioServer("localhost", 8888).start();
	}

}
