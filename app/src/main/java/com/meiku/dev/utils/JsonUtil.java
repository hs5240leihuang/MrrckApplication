package com.meiku.dev.utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	public enum JSON_TYPE {
		/** JSONObject */
		JSON_TYPE_OBJECT,
		/** JSONArray */
		JSON_TYPE_ARRAY,
		/** 不是JSON格式的字符串 */
		JSON_TYPE_ERROR
	}

	private static JsonParser jsonParser = new JsonParser();

	private static final GsonBuilder sGsonBuilder = new GsonBuilder()
			// .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性

			// .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
			// .disableHtmlEscaping() //取消unicode及等号的转义
			.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
			.setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式

	// .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) //会把字段首字母大写

	;

	/**
	 * 获取GSON对象
	 * 
	 * @return
	 */
	public static Gson getGsonObj() {

		return sGsonBuilder.create();

		// GsonBuilder builder = new GsonBuilder();
		// builder.registerTypeAdapter(Timestamp.class,new
		// TimestampTypeAdapter()).
		// setDateFormat("yyyy-MM-dd HH:mm:ss") ; //可以支持TIMASTAMP
		// return builder.create();
	}

	/**
	 * String转JsonObject
	 * 
	 * @param strJson
	 * @return
	 */
	public static JsonObject String2Object(String strJson) {
		return jsonParser.parse(strJson).getAsJsonObject();
	}

	/**
	 * 实体对象转成JsonObject
	 */
	public static JsonObject Entity2JsonObj(Object obj) {
		return jsonParser.parse(objToJson(obj)).getAsJsonObject();
	}

	/**
	 * Map转成JsonObject
	 */
	public static JsonObject Map2JsonObj(Map<String, Object> map) {
		return jsonParser.parse(hashMapToJson(map)).getAsJsonObject();
	}

	/**
	 * 对象转JSON
	 */
	public static String objToJson(Object obj) {
		return getGsonObj().toJson(obj);
	}

	/**
	 * JSON转对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object jsonToObj(Class obClass, String json) {
		try {
			Gson gson = getGsonObj();
			return gson.fromJson(json, obClass);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * list对象转JSON
	 */
	public static String listToJson(@SuppressWarnings("rawtypes") List listBean) {
		Gson gson = getGsonObj();
		Type type = new TypeToken<List>() {
		}.getType(); // 指定集合对象属性
		String listToJson = gson.toJson(listBean, type);
		return listToJson;
	}

	/**
	 * LIST to JsonArray,支持泛型
	 * 
	 * @param listBean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JsonArray listToJsonArray(List listBean) {
		String jstring = listToJson(listBean);
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(jstring).getAsJsonArray();
		return Jarray;
	}

	/**
	 * json转LIST Type type = new TypeToken<List<类>>(){}.getType()
	 */
	public static List<?> jsonToList(String json, Type type) {
		try {
			Gson gson = getGsonObj();
			// Type type2 = new TypeToken<List<>>(){}.getType(); //指定集合对象属性
			List<?> listFromJson = gson.fromJson(json, type);
			return listFromJson;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * HashMap转成json
	 */
	public static String hashMapToJson(Map<String, Object> map) {
		Gson gson = getGsonObj();
		String mapToJson = gson.toJson(map);
		return mapToJson;
	}
	
	/**
	 * HashMap转成json
	 */
	public static String hashMapToJsonD(Map<String, Object> map) {
		 GsonBuilder gb =new GsonBuilder();
		 gb.disableHtmlEscaping();
		String mapToJson = gb.create().toJson(map);
		return mapToJson;
	}

	/**
	 * JSON转HashMap
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> jsonToMap(String json) {
		Gson gson = getGsonObj();
		Map<String, String> map = (Map<String, String>) gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		return map;
	}


	/**
	 * JSON转HashMap
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap2(String json) {
		Gson gson = getGsonObj();
		Map<String, Object> map = (Map<String, Object>) gson.fromJson(json,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		return map;
	}
	
	/***
	 * 
	 * 获取JSON类型 判断规则 判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
	 * 
	 * @param str
	 * @return
	 */
	public static JSON_TYPE getJSONType(String str) {
		if (Tool.isEmpty(str)) {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}

		final char[] strChar = str.substring(0, 1).toCharArray();
		final char firstChar = strChar[0];
		if (firstChar == '{') {
			return JSON_TYPE.JSON_TYPE_OBJECT;
		} else if (firstChar == '[') {
			return JSON_TYPE.JSON_TYPE_ARRAY;
		} else {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}
	}

	// /**
	// * 组装返回统一格式
	// */
	// public static String getResp(boolean isSu,String msg,String data) {
	// JsonRespDto resp = new JsonRespDto();
	// resp.setSuccess(isSu);
	// resp.setMessage(msg);
	// resp.setData(data);
	// return objToJson(resp);
	// }

	public static void main(String rage[]) {

	}

	public static String ToZhuanyiJson(String strjson)

	{

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strjson.length(); i++)

		{

			char c = strjson.toCharArray()[i]; // strjosn转换为字节数组

			switch (c)

			{

			case '\"':
				sb.append("\\\"");
				break;

			// case '\\&': sb.Append("\\&"); break;

			case '\\':
				sb.append("\\\\");
				break;

			case '/':
				sb.append("\\/");
				break;

			case '\b':
				sb.append("\\b");
				break;

			case '\f':
				sb.append("\\f");
				break;

			case '\n':
				sb.append("\\n");
				break;

			case '\r':
				sb.append("\\r");
				break;

			case '\t':
				sb.append("\\t");
				break;

			default:
				sb.append(c);
				break;

			}

		}

		return sb.toString();

	}

}
