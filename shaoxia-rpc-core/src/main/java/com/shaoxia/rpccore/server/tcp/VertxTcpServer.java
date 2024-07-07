package com.shaoxia.rpccore.server.tcp;


import com.shaoxia.rpccore.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-02 10:48
 */
public class VertxTcpServer implements HttpServer {



	@Override
	public void doStart(int port) {
		// 创建vertx实例
		Vertx vertx = Vertx.vertx();

		// 创建TCP服务器
		NetServer server = vertx.createNetServer();

		// 处理请求
		server.connectHandler(new TcpServerHandler());

		server.listen(port,result->{
			if (result.succeeded()){
				System.out.println("TCP Server started on port: "+port);
			}else {
				System.out.println("Fail Server start TCP"+result.cause());
			}
		});
	}

	public static void main(String[] args) {
		new VertxTcpServer().doStart(9999);
	}
}
