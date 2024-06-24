package com.shaoxia.rpceasy.server;

import io.vertx.core.Vertx;

/**
 * @author wjc28
 * @version 1.0
 * @description: 使用Vertx实现
 * @date 2024-06-23 21:06
 */
public class VertxHttpServer implements HttpServer{
	@Override
	public void doPort(int port) {
		// 创建VertX实例
		Vertx vertx = Vertx.vertx();

		// 创建Http服务器
		io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

		// 监听端口并处理请求
		httpServer.requestHandler(new HttpServerHandler());

		// 启动Http服务器并监听指定窗口
		httpServer.listen(port, result->{
			if (result.succeeded()) {
				System.out.println("listen to port "+port);
			}else {
				System.out.println("fail "+result.cause());
			}
		});
	}
}
