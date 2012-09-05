 /*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.hades.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 字符串操作的帮助函数
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class StringUtil {

	public static final String FOLDER_SEPARATOR = "/";

	public static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	public static final String TOP_PATH = "..";

	public static final String CURRENT_PATH = ".";

	public static final char EXTENSION_SEPARATOR = '.';
	
	/**
	 * 检查包含空白字符在内的字符系列长度
	 * <p><pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 检查包含空白字符在内的字符系列长度
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}
	
	/**
	 * 检查字符串过滤前后空白后的长度
	 * @param str
	 * @return boolean
	 */
	public static boolean hasLengthAfterTrimWhiteSpace(String str) {
		return str != null && str.trim().length() > 0;
	}
	
	
	/**
	 * 复制字符串
	 * 
	 * @param cs 字符串
	 * @param num 数量
	 * @return 新字符串
	 */
	public static String dup(CharSequence cs, int num) {
		if (isEmpty(cs) || num <= 0)
			return "";
		StringBuilder sb = new StringBuilder(cs.length() * num);
		for (int i = 0; i < num; i++)
			sb.append(cs);
		return sb.toString();
	}

	/**
	 * 复制字符
	 * 
	 * @param c
	 *            字符
	 * @param num
	 *            数量
	 * @return 新字符串
	 */
	public static String dup(char c, int num) {
		if (c == 0 || num < 1)
			return "";
		StringBuilder sb = new StringBuilder(num);
		for (int i = 0; i < num; i++)
			sb.append(c);
		return sb.toString();
	}

	/**
	 * 将字符串首字母大写
	 * @param s 字符串
	 * @return 首字母大写后的新字符串
	 */
	public static String capitalize(CharSequence s) {
		if (null == s)
			return null;
		int len = s.length();
		if (len == 0)
			return "";
		char char0 = s.charAt(0);
		if (Character.isUpperCase(char0))
			return s.toString();
		return new StringBuilder(len).append(Character.toUpperCase(char0))
										.append(s.subSequence(1, len))
										.toString();
	}

	/**
	 * 将字符串首字母小写
	 * @param s 字符串
	 * @return 首字母小写后的新字符串
	 */
	public static String lowerFirst(CharSequence s) {
		if (null == s)
			return null;
		int len = s.length();
		if (len == 0)
			return "";
		char c = s.charAt(0);
		if (Character.isLowerCase(c))
			return s.toString();
		return new StringBuilder(len).append(Character.toLowerCase(c))
										.append(s.subSequence(1, len))
										.toString();
	}

	/**
	 * 检查两个字符串的忽略大小写后是否相等.
	 * 
	 * @param s1 字符串A
	 * @param s2 字符串B
	 * @return true 如果两个字符串忽略大小写后相等,且两个字符串均不为null
	 */
	public static boolean equalsIgnoreCase(String s1, String s2) {
		return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
	}

	/**
	 * 检查两个字符串是否相等.
	 * 
	 * @param s1 字符串A
	 * @param s2 字符串B
	 * @return true 如果两个字符串相等,且两个字符串均不为null
	 */
	public static boolean equals(String s1, String s2) {
		return s1 == null ? s2 == null : s1.equals(s2);
	}

	/**
	 * 判断字符串是否以特殊字符开头
	 * 
	 * @param s 字符串
	 * @param c 特殊字符
	 * @return 是否以特殊字符开头
	 */
	public static boolean startsWithChar(String s, char c) {
		return null != s ? (s.length() == 0 ? false : s.charAt(0) == c) : false;
	}

	/**
	 * 判断字符串是否以特殊字符结尾
	 * 
	 * @param s 字符串
	 * @param c 特殊字符
	 * @return 是否以特殊字符结尾
	 */
	public static boolean endsWithChar(String s, char c) {
		return null != s ? (s.length() == 0 ? false : s.charAt(s.length() - 1) == c) : false;
	}

	/**
	 * @param cs 字符串
	 * @return 是不是为空字符串
	 */
	public static boolean isEmpty(CharSequence cs) {
		return null == cs || cs.length() == 0;
	}

	/**
	 * @param cs 字符串
	 * @return 是不是为空白字符串
	 */
	public static boolean isBlank(CharSequence cs) {
		if (null == cs)
			return true;
		int length = cs.length();
		for (int i = 0; i < length; i++) {
			if (!(Character.isWhitespace(cs.charAt(i))))
				return false;
		}
		return true;
	}

	/**
	 * 去掉字符串前后空白
	 * 
	 * @param cs 字符串
	 * @return 新字符串
	 */
	public static String trim(CharSequence cs) {
		if (null == cs)
			return null;
		if (cs instanceof String)
			return ((String) cs).trim();
		int length = cs.length();
		if (length == 0)
			return cs.toString();
		int l = 0;
		int last = length - 1;
		int r = last;
		for (; l < length; l++) {
			if (!Character.isWhitespace(cs.charAt(l)))
				break;
		}
		for (; r > l; r--) {
			if (!Character.isWhitespace(cs.charAt(r)))
				break;
		}
		if (l > r)
			return "";
		else if (l == 0 && r == last)
			return cs.toString();
		return cs.subSequence(l, r + 1).toString();
	}

	/**
	 * 将字符串按半角逗号，拆分成数组，空元素将被忽略
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串数组
	 */
	public static String[] splitIgnoreBlank(String s) {
		return StringUtil.splitIgnoreBlank(s, ",");
	}

	/**
	 * 根据一个正则式，将字符串拆分成数组，空元素将被忽略
	 * @param s 字符串
	 * @param regex 正则式
	 * @return 字符串数组
	 */
	public static String[] splitIgnoreBlank(String s, String regex) {
		if (null == s)
			return null;
		String[] ss = s.split(regex);
		List<String> list = new LinkedList<String>();
		for (String st : ss) {
			if (isBlank(st))
				continue;
			list.add(trim(st));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 将一个整数转换成最小长度为某一固定数值的十进制形式字符串
	 * 
	 * @param d 整数
	 * @param width 宽度
	 * @return 新字符串
	 */
	public static String fillDigit(int d, int width) {
		return alignRight(String.valueOf(d), width, '0');
	}

	/**
	 * 将一个整数转换成最小长度为某一固定数值的十六进制形式字符串
	 * 
	 * @param d
	 *            整数
	 * @param width
	 *            宽度
	 * @return 新字符串
	 */
	public static String fillHex(int d, int width) {
		return alignRight(Integer.toHexString(d), width, '0');
	}

	/**
	 * 将一个整数转换成最小长度为某一固定数值的二进制形式字符串
	 * 
	 * @param d 整数
	 * @param width 宽度
	 * @return 新字符串
	 */
	public static String fillBinary(int d, int width) {
		return alignRight(Integer.toBinaryString(d), width, '0');
	}

	/**
	 * 将一个整数转换成固定长度的十进制形式字符串
	 * 
	 * @param d 整数
	 * @param width 宽度
	 * @return 新字符串
	 */
	public static String toDigit(int d, int width) {
		return StringUtil.cutRight(String.valueOf(d), width, '0');
	}

	/**
	 * 将一个整数转换成固定长度的十六进制形式字符串
	 * 
	 * @param d 整数
	 * @param width 宽度
	 * @return 新字符串
	 */
	public static String toHex(int d, int width) {
		return StringUtil.cutRight(Integer.toHexString(d), width, '0');
	}

	/**
	 * 将一个整数转换成固定长度的二进制形式字符串
	 * 
	 * @param d 整数
	 * @param width 宽度
	 * @return 新字符串
	 */
	public static String toBinary(int d, int width) {
		return StringUtil.cutRight(Integer.toBinaryString(d), width, '0');
	}

	/**
	 * 保证字符串为一固定长度。超过长度，切除，否则补字符。
	 * 
	 * @param s 字符串
	 * @param width 长度
	 * @param c 补字符
	 * @return 修饰后的字符串
	 */
	public static String cutRight(String s, int width, char c) {
		if (null == s)
			return null;
		int len = s.length();
		if (len == width)
			return s;
		if (len < width)
			return StringUtil.dup(c, width - len) + s;
		return s.substring(len - width, len);
	}

	/**
	 * 在字符串左侧填充一定数量的特殊字符
	 * 
	 * @param cs 字符串
	 * @param width 字符数量
	 * @param c 字符
	 * @return 新字符串
	 */
	public static String alignRight(CharSequence cs, int width, char c) {
		if (null == cs)
			return null;
		int len = cs.length();
		if (len >= width)
			return cs.toString();
		return new StringBuilder().append(dup(c, width - len)).append(cs).toString();
	}

	/**
	 * 在字符串右侧填充一定数量的特殊字符
	 * 
	 * @param cs 字符串
	 * @param width 字符数量
	 * @param c 字符
	 * @return 新字符串
	 */
	public static String alignLeft(CharSequence cs, int width, char c) {
		if (null == cs)
			return null;
		int length = cs.length();
		if (length >= width)
			return cs.toString();
		return new StringBuilder().append(cs).append(dup(c, width - length)).toString();
	}

	/**
	 * @param cs
	 *            字符串
	 * @param lc
	 *            左字符
	 * @param rc
	 *            右字符
	 * @return 字符串是被左字符和右字符包裹 -- 忽略空白
	 */
	public static boolean isQuoteByIgnoreBlank(CharSequence cs, char lc, char rc) {
		if (null == cs)
			return false;
		int len = cs.length();
		if (len < 2)
			return false;
		int l = 0;
		int last = len - 1;
		int r = last;
		for (; l < len; l++) {
			if (!Character.isWhitespace(cs.charAt(l)))
				break;
		}
		if (cs.charAt(l) != lc)
			return false;
		for (; r > l; r--) {
			if (!Character.isWhitespace(cs.charAt(r)))
				break;
		}
		return l < r && cs.charAt(r) == rc;
	}

	/**
	 * @param cs
	 *            字符串
	 * @param lc
	 *            左字符
	 * @param rc
	 *            右字符
	 * @return 字符串是被左字符和右字符包裹
	 */
	public static boolean isQuoteBy(CharSequence cs, char lc, char rc) {
		if (null == cs)
			return false;
		int length = cs.length();
		return length > 1 && cs.charAt(0) == lc && cs.charAt(length - 1) == rc;
	}

	/**
	 * 获得一个字符串集合中，最长串的长度
	 * 
	 * @param coll 字符串集合
	 * @return 最大长度
	 */
	public static int maxLength(Collection<? extends CharSequence> coll) {
		int re = 0;
		if (null != coll)
			for (CharSequence s : coll)
				if (null != s)
					re = Math.max(re, s.length());
		return re;
	}

	/**
	 * 获得一个字符串数组中，最长串的长度
	 * 
	 * @param array
	 *            字符串数组
	 * @return 最大长度
	 */
	public static <T extends CharSequence> int maxLength(T[] array) {
		int re = 0;
		if (null != array)
			for (CharSequence s : array)
				if (null != s)
					re = Math.max(re, s.length());
		return re;
	}

	/**
	 * 对obj进行toString()操作,如果为null返回""
	 * 
	 * @param obj
	 * @return obj.toString()
	 */
	public static String sNull(Object obj) {
		return sNull(obj, "");
	}

	/**
	 * 对obj进行toString()操作,如果为null返回def中定义的值
	 * 
	 * @param obj
	 * @param defaultValue
	 *            如果obj==null返回的内容
	 * @return obj的toString()操作
	 */
	public static String sNull(Object obj, String defaultValue) {
		return obj != null ? obj.toString() : defaultValue;
	}

	/**
	 * 对obj进行toString()操作,如果为空串返回""
	 * 
	 * @param obj
	 * @return obj.toString()
	 */
	public static String sBlank(Object obj) {
		return sBlank(obj, "");
	}

	/**
	 * 对obj进行toString()操作,如果为空串返回def中定义的值
	 * 
	 * @param obj
	 * @param def
	 *            如果obj==null返回的内容
	 * @return obj的toString()操作
	 */
	public static String sBlank(Object obj, String def) {
		if (null == obj)
			return def;
		String s = obj.toString();
		return StringUtil.isBlank(s) ? def : s;
	}

	/**
	 * 截去第一个字符
	 * <p>
	 * 比如:
	 * <ul>
	 * <li>removeFirst("12345") => 2345
	 * <li>removeFirst("A") => ""
	 * </ul>
	 * 
	 * @param str
	 *            字符串
	 * @return 新字符串
	 */
	public static String removeFirst(CharSequence str) {
		if (str == null)
			return null;
		if (str.length() > 1)
			return str.subSequence(1, str.length()).toString();
		return "";
	}

	/**
	 * 如果str中第一个字符和 c一致,则删除,否则返回 str
	 * <p>
	 * 比如:
	 * <ul>
	 * <li>removeFirst("12345",1) => "2345"
	 * <li>removeFirst("ABC",'B') => "ABC"
	 * <li>removeFirst("A",'B') => "A"
	 * <li>removeFirst("A",'A') => ""
	 * </ul>
	 * 
	 * @param str
	 *            字符串
	 * @param c
	 *            第一个个要被截取的字符
	 * @return 新字符串
	 */
	public static String removeFirst(String str, char c) {
		return (StringUtil.isEmpty(str) || c != str.charAt(0)) ? str : str.substring(1);
	}

	/**
	 * 判断一个字符串数组是否包括某一字符串
	 * 
	 * @param ss
	 *            字符串数组
	 * @param s
	 *            字符串
	 * @return 是否包含
	 */
	public static boolean isin(String[] ss, String s) {
		if (null == ss || ss.length == 0 || StringUtil.isBlank(s))
			return false;
		for (String w : ss)
			if (s.equals(w))
				return true;
		return false;
	}

	private static Pattern email_Pattern = Pattern.compile("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

	/**
	 * 检查一个字符串是否为合法的电子邮件地址
	 * 
	 * @param input
	 *            需要检查的字符串
	 * @return true 如果是有效的邮箱地址
	 */
	public static synchronized final boolean isEmail(CharSequence input) {
		return email_Pattern.matcher(input).matches();
	}

	/**
	 * 将一个字符串某一个字符后面的字母变成大写，比如
	 * 
	 * <pre>
	 *  upperWord("hello-world", '-') => "helloWorld"
	 * </pre>
	 * 
	 * @param s
	 *            字符串
	 * @param c
	 *            字符
	 * 
	 * @return 转换后字符串
	 */
	public static String upperWord(CharSequence s, char c) {
		StringBuilder sb = new StringBuilder();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if (ch == c) {
				do {
					i++;
					if (i >= len)
						return sb.toString();
					ch = s.charAt(i);
				} while (ch == c);
				sb.append(Character.toUpperCase(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(lowerFirst("BizImpl"));
	}
	
	/**
	 * Test if the given String starts with the specified prefix,
	 * ignoring upper/lower case.
	 * @param str the String to check
	 * @param prefix the prefix to look for
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}
	
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String has actual text.
	 * More specifically, returns <code>true</code> if the string not <code>null</code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its length is
	 * greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}
	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}
	
	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String
	 * was <code>null</code>)
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}
	
	/**
	 * Count the occurrences of the substring in string s.
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}
	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param arr the array to display
	 * @param delim the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		if (arr.length == 1) {
			return ObjectUtils.nullSafeToString(arr[0]);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] {str};
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}
}
