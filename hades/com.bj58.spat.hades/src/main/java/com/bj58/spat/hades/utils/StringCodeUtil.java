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

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.CharEncoding;

import com.bj58.spat.hades.exception.CodingException;

/**
 * 带编码的字符串工具
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StringCodeUtil extends StringUtil {

	 /**
     * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the result into a new
     * byte array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesIso8859_1(String string) {
        return getBytesUnchecked(string, CodingUtil.ISO8859_1);
    }

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUsAscii(String string) {
        return getBytesUnchecked(string, CodingUtil.US_ASCII);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16(String string) {
        return getBytesUnchecked(string, CodingUtil.UTF16);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Be(String string) {
        return getBytesUnchecked(string, CodingUtil.UTF16BE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Le(String string) {
        return getBytesUnchecked(string, CodingUtil.UTF16LE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
     * array.
     * 
     * @param string
     *            the String to encode
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf8(String string) {
        return getBytesUnchecked(string, CodingUtil.UTF8);
    }

    /**
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param string
     *            the String to encode
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
        	 throw new CodingException(charsetName, e);
        }
    }


    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#String(byte[], String)
     */
    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new CodingException(charsetName, e);
        }
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the ISO-8859-1 charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringIso8859_1(byte[] bytes) {
        return newString(bytes, CodingUtil.ISO8859_1);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUsAscii(byte[] bytes) {
        return newString(bytes, CodingUtil.US_ASCII);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16 charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUtf16(byte[] bytes) {
        return newString(bytes, CodingUtil.UTF16);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16BE charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUtf16Be(byte[] bytes) {
        return newString(bytes, CodingUtil.UTF16BE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16LE charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUtf16Le(byte[] bytes) {
        return newString(bytes, CodingUtil.UTF16LE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
     * 
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *             charset is required.
     */
    public static String newStringUtf8(byte[] bytes) {
        return newString(bytes, CodingUtil.UTF8);
    }
}
