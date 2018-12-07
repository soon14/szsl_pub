package com.app.tanklib.util;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 字符串工具类
 * 
 * @author jie.li
 * 
 */
public class StringUtil {

	private static String PATTERN_LEFT = ".*([";
	private static String PATTERN_RIGHT = "])";
	private static String PATTERN_FINAL = ".*";

	// private static String[] phones = { "130", "131", "132", "133", "134",
	// "135", "136", "137", "138", "139", "150", "151", "152", "153",
	// "154", "155", "156", "157", "158", "159", "180", "181", "182",
	// "183", "184", "185", "186", "187", "188", "189" };

	public static String replaceAll(String s, String find, String replace) {
		StringBuffer buffer = new StringBuffer(s);
		int pos = buffer.toString().indexOf(find);
		int len = find.length();
		while (pos > -1) {
			buffer.replace(pos, pos + len, replace);
			pos = buffer.toString().indexOf(find);
		}
		return buffer.toString();
	}

	public static String getTextLimit(String s, int len) {
		if (isEmpty(s)) {
			return "";
		}
		if (s.length() <= len) {
			return s;
		} else {
			return s.substring(0, len - 1) + "...";
		}

	}

	public static String getTextViewText(String s) {
		if (null == s) {
			return "";
		}
		return s;
	}

	/**
	 * 判断字符串是否为null或空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}

		return false;
	}

	public static boolean isMobilPhoneNumber(String mobilPhoneNumber) {
		// if (mobilPhoneNumber.trim().length() != 11) {
		// return false;
		// }
		//
		// String phone = mobilPhoneNumber.substring(0, 3);
		// for (String str : phones) {
		// if (phone.equals(str)) {
		// return true;
		// }
		// }
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobilPhoneNumber);
		return m.matches();

		// return false;
	}

	/**
	 * 判断字符串前后截掉空字符后,是否为null或空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTrimEmpty(String str) {
		if (str == null) {
			return true;
		}

		str = str.trim();
		return isEmpty(str);
	}

	/**
	 * 判断是否是中文字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否含有中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		char[] chars = str.toCharArray();
		for (char c : chars) {
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否符合复杂密码要求
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPassWord(String str) {
		char[] pw = str.toCharArray();
		boolean isPassWord = false;
		for (int i = 1; i < pw.length; i++) {
			if (Math.abs(pw[i] - pw[i - 1]) > 1) {
				isPassWord = true;
				break;
			}
		}
		return isPassWord;
	}

	/**
	 * 获取搜索字符的正则表达式
	 * 
	 * @param searchText
	 * @return
	 */
	public static String getPatterString(String searchText) {
		if (searchText == null)
			return "";
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？] ";
		final int len = searchText.length();
		if (len > 0) {
			String pattern = "";
			for (int i = 0; i < len; ++i) {
				String subStr = searchText.substring(i, i + 1);
				if (regEx.contains(subStr)) {
					pattern += PATTERN_LEFT + "\\" + subStr + PATTERN_RIGHT;
				} else {
					pattern += PATTERN_LEFT + subStr + PATTERN_RIGHT;
				}
			}
			return pattern + PATTERN_FINAL;
		} else {
			return "";
		}
	}
	
	
	/**
	 * 读取raw文件夹下的文件
	 * @param resourceId raw文件夹下的文件资源ID
	 * @return 文件内容
	 * 
	 * */
	public static String readFileFromRaw(Context context, int resourceId) {
		if( null == context || resourceId < 0 ){
			return null;
		}
		
		String result = null;
		try {
			InputStream inputStream = context.getResources().openRawResource( resourceId );
			// 获取文件的字节数
			int length = inputStream.available();
			// 创建byte数组
			byte[] buffer = new byte[length];
			// 将文件中的数据读到byte数组中
			inputStream.read(buffer);
			result = EncodingUtils.getString(buffer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}

}
