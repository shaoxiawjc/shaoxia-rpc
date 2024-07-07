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

	private final RecordParser recordParser;

	public TcpBufferHandlerWrapper(Handler<Buffer> recordParser) {
		this.recordParser = initParser(recordParser);
	}

	private RecordParser initParser(Handler<Buffer> recordParser) {
		RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

		parser.setOutput(new Handler<Buffer>() {
			// 初始化
			int size = -1;
			// 一次完整的读取
			Buffer resultBuffer = Buffer.buffer();
			@Override
			public void handle(Buffer buffer) {
				if (-1 == size){
					size = buffer.getInt(4);
					parser.fixedSizeMode(size);
					resultBuffer.appendBuffer(buffer);
				}else {
					resultBuffer.appendBuffer(buffer);
					// 重置
					parser.fixedSizeMode(8);
					size = -1;
					resultBuffer = Buffer.buffer();
				}

			}
		});

		return parser;
	}

	@Override
	public void handle(Buffer buffer) {
		recordParser.handle(buffer);
	}
}
