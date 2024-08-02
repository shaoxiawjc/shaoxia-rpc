package com.shaoxia.rpccore.server.tcp;

import com.shaoxia.rpccore.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-07-06 21:11
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

	/**
	 * 解析器，用于解决半包、粘包问题
	 */
	private final RecordParser recordParser;

	public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
		recordParser = initRecordParser(bufferHandler);
	}

	@Override
	public void handle(Buffer buffer) {
		recordParser.handle(buffer);
	}

	/**
	 * 初始化解析器
	 *
	 * @param bufferHandler
	 * @return
	 */
	private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
		// 构造 parser，初始化消息头长度
		RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

		parser.setOutput(new Handler<Buffer>() {
			// 初始化
			int size = -1;
			// 一次完整的读取（头 + 体）
			Buffer resultBuffer = Buffer.buffer();

			@Override
			public void handle(Buffer buffer) {
				// 1. 每次循环，首先读取消息头
				if (-1 == size) {
					// 读取消息体长度
					size = buffer.getInt(13);
					parser.fixedSizeMode(size);
					// 写入头信息到结果
					resultBuffer.appendBuffer(buffer);
				} else {
					// 2. 然后读取消息体
					// 写入体信息到结果
					resultBuffer.appendBuffer(buffer);
					// 已拼接为完整 Buffer，执行处理
					bufferHandler.handle(resultBuffer);
					// 重置一轮
					parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
					size = -1;
					resultBuffer = Buffer.buffer();
				}
			}
		});
		return parser;
	}
}
