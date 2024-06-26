package com.shaoxia.rpccore.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author wjc28
 * @version 1.0
 * @description: JSON序列化器
 * @date 2024-06-26 15:00
 */
public class JSONSerializer implements Serializer{
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(Object obj) {
		try {
			String jsonString = serializeObject(obj);
			return jsonString.getBytes();
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize object", e);
		}
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> type) {
		try {
			String jsonString = new String(data);
			return objectMapper.readValue(jsonString, type);
		} catch (IOException e) {
			throw new RuntimeException("Failed to deserialize object", e);
		}
	}

	private String serializeObject(Object obj) throws JsonProcessingException {
		if (obj == null) {
			return "null";
		}

		Class<?> objClass = obj.getClass();

		if (objClass == String.class) {
			return "\"" + escapeString((String) obj) + "\"";
		} else if (objClass == Integer.class || objClass == int.class ||
				objClass == Long.class || objClass == long.class ||
				objClass == Float.class || objClass == float.class ||
				objClass == Double.class || objClass == double.class ||
				objClass == Boolean.class || objClass == boolean.class) {
			return obj.toString();
		} else if (objClass.isArray()) {
			return serializeArray(obj);
		} else if (Collection.class.isAssignableFrom(objClass)) {
			return serializeCollection((Collection<?>) obj);
		} else if (Map.class.isAssignableFrom(objClass)) {
			return serializeMap((Map<?, ?>) obj);
		} else {
			return serializeComplexObject(obj);
		}
	}

	private String serializeArray(Object array) throws JsonProcessingException {
		StringBuilder json = new StringBuilder();
		json.append("[");

		int length = java.lang.reflect.Array.getLength(array);
		for (int i = 0; i < length; i++) {
			Object element = java.lang.reflect.Array.get(array, i);
			json.append(serializeObject(element));
			if (i < length - 1) {
				json.append(",");
			}
		}

		json.append("]");
		return json.toString();
	}

	private String serializeCollection(Collection<?> collection) throws JsonProcessingException {
		StringBuilder json = new StringBuilder();
		json.append("[");

		int size = collection.size();
		int i = 0;
		for (Object element : collection) {
			json.append(serializeObject(element));
			if (i < size - 1) {
				json.append(",");
			}
			i++;
		}

		json.append("]");
		return json.toString();
	}

	private String serializeMap(Map<?, ?> map) throws JsonProcessingException {
		StringBuilder json = new StringBuilder();
		json.append("{");

		int size = map.size();
		int i = 0;
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			json.append(serializeObject(entry.getKey()));
			json.append(":");
			json.append(serializeObject(entry.getValue()));
			if (i < size - 1) {
				json.append(",");
			}
			i++;
		}

		json.append("}");
		return json.toString();
	}

	private String serializeComplexObject(Object obj) throws JsonProcessingException {
		StringBuilder json = new StringBuilder();
		json.append("{");

		Field[] fields = obj.getClass().getDeclaredFields();
		int size = fields.length;
		int i = 0;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				json.append("\"").append(field.getName()).append("\":");
				json.append(serializeObject(field.get(obj)));
				if (i < size - 1) {
					json.append(",");
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			i++;
		}

		json.append("}");
		return json.toString();
	}

	private String escapeString(String str) {
		return str.replace("\"", "\\\"");
	}
}
