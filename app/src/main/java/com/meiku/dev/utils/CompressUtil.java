package com.meiku.dev.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.config.AppConfig;

public class CompressUtil {
	// 压缩字节最小长度，小于这个长度的字节数组不适合压缩，压缩完会更大
	public static final int BYTE_MIN_LENGTH = 100;
	public final static String encode = "UTF-8";

	public static String compress(String str) throws IOException {

		byte[] blockcopy = ByteBuffer.allocate(4)
				.order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(str.length())
				.array();
		ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
		GZIPOutputStream gos = new GZIPOutputStream(os);
		gos.write(str.getBytes(encode));
		gos.close();
		os.close();
		byte[] compressed = new byte[4 + os.toByteArray().length];
		System.arraycopy(blockcopy, 0, compressed, 0, 4);
		System.arraycopy(os.toByteArray(), 0, compressed, 4,
				os.toByteArray().length);
		return Base64.encode(compressed);

	}

	public static String decompress(String zipText) throws IOException {
		byte[] compressed = Base64.decode(zipText);
		if (compressed.length > 4) {
			GZIPInputStream gzipInputStream = new GZIPInputStream(
					new ByteArrayInputStream(compressed, 4,
							compressed.length - 4));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int value = 0; value != -1;) {
				value = gzipInputStream.read();
				if (value != -1) {
					baos.write(value);
				}
			}
			gzipInputStream.close();
			baos.close();
			String sReturn = new String(baos.toByteArray(), encode);
			return sReturn;
		} else {
			return "";
		}
	}

	public static String CompressBody(ReqBase req) {
		Map<String, Object> reqmap = new HashMap<String, Object>();
		req.getHeader().setZipFlag(AppConfig.DATA_COMPRESS);// 设置压缩标记
		reqmap.put("header", JsonUtil.objToJson(req.getHeader()));
		try {
			reqmap.put("body", compress(req.getBody().toString()));
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e("hl", " Compress ERROR");
		}

		return JsonUtil.hashMapToJson(reqmap);
	}

	public static String DecompressBody(String body) {
		try {
			return decompress(body);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e("hl", " Decompress ERROR");
			return "";
		}
	}

}
