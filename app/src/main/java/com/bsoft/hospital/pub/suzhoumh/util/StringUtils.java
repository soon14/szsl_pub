package com.bsoft.hospital.pub.suzhoumh.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     */
    public static boolean isTrimEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     */
    public static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    public static String replaceAll(String s, String find, String replace) {
        if (isEmpty(s)) {
            return s;
        }
        StringBuilder buffer = new StringBuilder(s);
        int pos = buffer.toString().indexOf(find);
        int len = find.length();
        while (pos > -1) {
            buffer.replace(pos, pos + len, replace);
            pos = buffer.toString().indexOf(find);
        }
        return buffer.toString();
    }

    public static String limitTextLength(String s, int len) {
        if (isEmpty(s)) {
            return "";
        }
        if (s.length() <= len) {
            return s;
        } else {
            return s.substring(0, len - 1) + "...";
        }

    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean hasChinese(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private static String PATTERN_LEFT = ".*([";
    private static String PATTERN_RIGHT = "])";
    private static String PATTERN_FINAL = ".*";

    /**
     * 获取搜索字符的正则表达式
     */
    public static String getSearchRegularExpression(String searchText) {
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
     * 判断是否为数字
     */
    public static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String numberFormat(double value, int precision, boolean isPercent) {
        if (isPercent) {
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确
            nt.setMaximumFractionDigits(precision);
            return nt.format(value);
        } else {
            NumberFormat nf = NumberFormat.getNumberInstance();
            //double精度
            nf.setMaximumFractionDigits(precision);
            return nf.format(value);
        }
    }

    /**
     * 身份证号替换，保留前四位和后四位
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     */
    public static String idCardReplaceWithStar(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return null;
        } else {
            return replaceAction(idCard, "(?<=\\d{4})\\d(?=\\d{4})");
        }
    }

    private static String replaceAction(String username, String regularExpresion) {
        return username.replaceAll(regularExpresion, "*");
    }

    /**
     * 银行卡替换，保留后四位
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     */
    public static String bankCardReplaceWithStar(String bankCard) {
        if (bankCard == null || bankCard.isEmpty()) {
            return null;
        } else {
            return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }

    /**
     * 手机号替换，保留前三位和后四位
     * 如果手机号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     */
    public static String mobileReplaceWithStar(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            return null;
        } else {
            return replaceAction(mobile, "(?<=\\d{3})\\d(?=\\d{4})");
        }
    }

    public static boolean isNumOrLetter(String str) {
        return str == null || str.matches("[0-9A-Za-z]*");
    }

    public static String formatFee(double fee) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(fee);
    }

    public static String formatFeeWithLabel(double fee) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return "¥ " + decimalFormat.format(fee);
    }

    public static String formatQuantityWithLabel(double quantity) {
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        return "x" + decimalFormat.format(quantity);
    }

    public static String formatBrackets(String content) {
        return isEmpty(content) ? "" : "(" + content + ")";
    }
}
