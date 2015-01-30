package com.bridge4biz.wash.util;

import java.util.HashMap;
import java.util.Map;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class SocketIO {

	static Socket socket = null;

	public SocketIO() {
		IO.Options opts = new IO.Options();
		opts.forceNew = true;
		opts.reconnection = true;
		try {
			socket = IO.socket("http://www.cleanbasket.co.kr:8000", opts);
			// socket = IO.socket("http://www.cleanbasket.co.kr:8001", opts);
			socket.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void stop() {
		if (socket != null) {
			socket.disconnect();
		}
	}

	static public void broadCast(PushMessage pushMessage) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("constant", pushMessage.constant);
		message.put("uid", pushMessage.uid);
		message.put("oid", pushMessage.oid);
		socket.emit("message", message);
	}

}
