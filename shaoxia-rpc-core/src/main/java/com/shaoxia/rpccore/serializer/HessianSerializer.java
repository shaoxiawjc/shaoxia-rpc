package com.shaoxia.rpccore.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-26 15:15
 */
public class HessianSerializer implements Serializer{
	@Override
	public <T> byte[] serialize(T object) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
		hessianOutput.writeObject(object);
		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		HessianInput hessianInput = new HessianInput(byteArrayInputStream);
		return type.cast(hessianInput.readObject());
	}

}
