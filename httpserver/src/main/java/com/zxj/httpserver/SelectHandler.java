package com.zxj.httpserver;

import java.nio.channels.SelectionKey;

public interface SelectHandler {
	void handlerKey(SelectionKey key);
}
