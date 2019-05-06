package com.zxj.httpserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;

public class SelectHanderContext {
//	private SelectHandler sh;

	public SelectHanderContext() {

	}

	public void doHandler(SelectionKey key, Selector sel) throws IOException {
		if (key.isAcceptable()) {
			System.out.println("channel is accepatable!");
			
			//当服务端有客户端来连接的时候，触发acceptable事件
			//从对应的key中取得channel
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			try {
				//获取连接的客户端的channel并为它注册相应的事件，此处为read事件
				SocketChannel clientChannel = serverChannel.accept();
				clientChannel.configureBlocking(false);
				SelectionKey k2 = clientChannel.register(sel, SelectionKey.OP_READ);
				//为客户端注册的readkey绑定一个buffer对象
				k2.attach(ByteBuffer.allocate(10));
				System.out.println("有客户端连接 主机地址是：" + clientChannel.getRemoteAddress());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				serverChannel.close();
			}
		} else if (key.isConnectable()) {
			System.out.println("channel is connected!");
		} else if (key.isReadable()) {
			//当客户端channel为readable状态，读取数据
			SocketChannel clientChannel = (SocketChannel) key.channel();
			System.out.println("连接通道可读，客户端连接 主机地址是：" + clientChannel.getRemoteAddress());
			ByteBuffer bf = (ByteBuffer) key.attachment();
			String msg = "";
			boolean clientEnd = false;
			if (bf.hasRemaining()) {
				//从channel中读取数据到buffer中，之后在buffer里操作数据
				int len = clientChannel.read(bf);
				if (len != -1 && bf.position() > 1) {
					char lastChar = (char) bf.get(bf.position() - 1);
					char last2Char = (char) bf.get(bf.position() - 2);
					System.out.println("读取内容:"+lastChar+last2Char);
					if (String.valueOf(new char[] { last2Char, lastChar }).equals("\r\n")) {
						System.out.println("client inupt end.");
						clientEnd = true;
					}
				}

				if (len == -1) {
					System.out.println("client closed.");
					clientEnd = true;
				}

			} else {
				System.out.println("buff is full.");
				msg = "You can only enter " + 10 + " chars\r\n";
				clientEnd = true;
			}

			if (clientEnd) {
				bf.flip();
				//当客户端channel数据读取完毕后，为它再注册一个write事件
				clientChannel.register(sel, SelectionKey.OP_WRITE,ByteBuffer.allocate(20));
			}
		} else if (key.isWritable()) {
			System.out.println("channel is writeable!");
			SocketChannel clientChannel = (SocketChannel)key.channel();
			ByteBuffer wbf = (ByteBuffer)key.attachment();
			wbf.flip();
			String msg = "12306";
			wbf=wbf.wrap(msg.getBytes());
			clientChannel.write(wbf);
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("esle !!!");
		}
	}
}
