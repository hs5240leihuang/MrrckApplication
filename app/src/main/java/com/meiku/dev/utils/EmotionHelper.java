package com.meiku.dev.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.lidroid.xutils.util.LogUtils;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;

/**
 * Created by lzw on 14-9-25.
 */
public class EmotionHelper {
	private static final int ONE_PAGE_SIZE = 28;
	public static List<List<String>> emojiGroups;
	private static Pattern pattern;
	private static Map<String, String> localEmoMap = null;
	private static Map<String, String> remoteEmoMap = null;
	private static String[] emojiCodes = MrrckApplication.getInstance()
			.getResources().getStringArray(R.array.emotion_local);

	static {
		int pages = emojiCodes.length / ONE_PAGE_SIZE
				+ (emojiCodes.length % ONE_PAGE_SIZE == 0 ? 0 : 1);
		emojiGroups = new ArrayList<List<String>>();
		for (int page = 0; page < pages; page++) {
			List<String> onePageEmojis = new ArrayList<String>();
			int start = page * ONE_PAGE_SIZE;
			int end = Math.min(page * ONE_PAGE_SIZE + ONE_PAGE_SIZE,
					emojiCodes.length);
			for (int i = start; i < end; i++) {
				onePageEmojis.add(emojiCodes[i]);
			}
			emojiGroups.add(onePageEmojis);
		}
		pattern = Pattern.compile("\\:[a-z0-9-_]*\\:");
	}

	public static boolean contain(String[] strings, String string) {
		for (String s : strings) {
			if (s.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static CharSequence replace(Context context, String text) {
		if (TextUtils.isEmpty(text)) {
			return text;
		}
		SpannableString spannableString = new SpannableString(text);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String factText = matcher.group();
			String key = factText.substring(1, factText.length() - 1);
			if (contain(emojiCodes, factText)) {
				Bitmap bitmap = getEmojiDrawable(context, key);
				ImageSpan image = new ImageSpan(context, bitmap);
				int start = matcher.start();
				int end = matcher.end();
				spannableString.setSpan(image, start, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

	public static void isEmojiDrawableAvailable() {
		for (String emojiCode : emojiCodes) {
			String code = emojiCode.substring(1, emojiCode.length() - 1);
			Bitmap bitmap = getDrawableByName(MrrckApplication.getInstance(),
					code);
			if (bitmap == null) {
				LogUtils.i("not available test " + code);
			}
		}
	}

	public static Bitmap getEmojiDrawable(Context context, String name) {
		return getDrawableByName(context, "emoji_" + name);
	}

	public static Bitmap getDrawableByName(Context ctx, String name) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeResource(
				ctx.getResources(),
				ctx.getResources().getIdentifier(name, "drawable",
						ctx.getPackageName()), options);
		return bitmap;
	}

	/**
	 * 将本地表情符号，转为发送的表情符号
	 * 
	 * @param localEmotion
	 * @return
	 */
	public static String getSendEmotion(Context context, String localEmotion) {
		initMap(context);
		localEmotion = converString(localEmotion, ":", 4, localEmoMap);
		/*
		 * initMap(context); if (null != localEmoMap) { if(null != localEmotion
		 * && !"".equals(localEmotion)){ boolean flag = true; while (flag) { for
		 * (Map.Entry<String, String> map : localEmoMap.entrySet()) { if
		 * (localEmotion.contains(map.getKey())) { localEmotion =
		 * localEmotion.replace(map.getKey(), map.getValue()); break; } } } } if
		 * (localEmoMap.containsKey(localEmotion)) { return "123"; } }
		 */
		return localEmotion;
		/*
		 * int index = -1; for (int i = 0; i < localEmo.length; i++) { String
		 * item = localEmo[i]; if (localEmotion.equals(item)) { index = i; } }
		 * 
		 * if (index != -1) { return remoteEmo[index]; } else { return
		 * localEmotion; }
		 */
	}

	public static String converString(String arg1, String arg2, int i,
			Map<String, String> map) {
		String returnStr = arg1;
		boolean flag = true;
		while (flag) {
			try {
				int start = returnStr.indexOf(arg2);
				String s = returnStr.substring(start, start + i);
				// LogUtil.d("hl", start + "___key____" + s);
				if (null != map.get(s)) {
					returnStr = returnStr.replace(s, map.get(s));
				} else {
					flag = false;
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		return returnStr;
	}

	public static String converString(String allstr, String left, int i,
			String right, Map<String, String> map) {

		String returnStr = allstr;
		boolean flag = true;
		while (flag) {
			try {
				int start = returnStr.indexOf(left);
				int startRight = returnStr.indexOf(right);
				String s = returnStr.substring(start, startRight + 1);
				// char charnow = returnStr.charAt(start + i);
				// LogUtil.d("hl", start + "__" + charnow + "_____s=" + s);
				// if (right.equals(String.valueOf(charnow))) {
				// s = returnStr.substring(start, start + i + 1);
				// LogUtil.d("hl", start + "__修正4位___" + s);
				// }else
				// if(startRight>4&&right.equals(String.valueOf(returnStr.charAt(start
				// + i+1)))){
				// s = returnStr.substring(start, start + i + 2);
				// LogUtil.d("hl", start + "__修正5位___" + s);
				// }
				LogUtil.d("hl", s + "__map___" + map.get(s));
				if (null != map.get(s)) {
					returnStr = returnStr.replace(s, map.get(s));
				} else {
					flag = false;
				}

			} catch (Exception e) {
				flag = false;
			}
		}
		return returnStr;

	}

	public static void initMap(Context context) {
		if (null == localEmoMap || null == remoteEmoMap) {
			String[] localEmo = context.getResources().getStringArray(
					R.array.emotion_local);
			String[] remoteEmo = context.getResources().getStringArray(
					R.array.emotion_remote);
			if (null == localEmoMap) {
				localEmoMap = new HashMap<String, String>();
				localEmoMap = initMap(localEmo, remoteEmo);
			}
			if (null == remoteEmoMap) {
				remoteEmoMap = new HashMap<String, String>();
				remoteEmoMap = initMap(remoteEmo, localEmo);
			}
		}
	}

	public static Map initMap(String[] ags1, String[] ags2) {
		if (null == ags1 || null == ags2) {
			return null;
		}
		/*
		 * if(null == localEmoMap){ localEmoMap = new HashMap<String,String>();
		 * }
		 */
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < ags1.length; i++) {
			map.put(ags1[i], ags2[i]);
		}
		return map;
	}

	/**
	 * 将发送表情符号，转为本地的表情符号
	 * 
	 * @param remoteEmotion
	 * @return
	 */
	public static String getLocalEmotion(Context context, String remoteEmotion) {
		if (Tool.isEmpty(remoteEmotion)) {
			return "";
		}
		String[] localEmo = context.getResources().getStringArray(
				R.array.emotion_local);
		String[] remoteEmo = context.getResources().getStringArray(
				R.array.emotion_remote);
		for (int i = 0, size = remoteEmo.length; i < size; i++) {
			if (remoteEmotion.contains(remoteEmo[i])) {
				remoteEmotion = remoteEmotion
						.replace(remoteEmo[i], localEmo[i]);
			}
		}
		return remoteEmotion;
	}

	public static Map<String, String> getLocalEmoMap(Context context) {
		initMap(context);
		return localEmoMap;
	}

	
}
