package com.util;
import com.fasterxml.jackson.databind.ObjectMapper;
public final class JsonUtil {
	public static ObjectMapper objectMapper;
	/**
	 * 鎶奀lass杞崲涓簀son瀛楃涓�
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonStr(Object object) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return  objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 鎶妀son杞崲涓篊lass
	 *
	 * @param json
	 * @return
	 */
	public static Object jsonToObject(String json , Class<?> cls) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return  objectMapper.readValue(json,cls);
		} catch (Exception e) {
			return null;
		}
	}
}
