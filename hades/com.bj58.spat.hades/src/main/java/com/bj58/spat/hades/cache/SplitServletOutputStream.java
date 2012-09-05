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
package com.bj58.spat.hades.cache;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;


/**
 * 扩展自<code>ServletOutputStream</code> 用于捕获输出的流。
 * 通过复写 <code>write()</code> 方法和输出数据和流(原始数据流和被捕获数据流。)
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class SplitServletOutputStream extends ServletOutputStream {
    OutputStream captureStream = null;
    OutputStream passThroughStream = null;

    /**
     * Constructs a split output stream that both captures and passes through
     * the servlet response.
     *
     * @param captureStream The stream that will be used to capture the data.
     * @param passThroughStream The pass-through <code>ServletOutputStream</code>
     * that will write the response to the client as originally intended.
     */
    public SplitServletOutputStream(OutputStream captureStream, OutputStream passThroughStream) {
        this.captureStream = captureStream;
        this.passThroughStream = passThroughStream;
    }

    /**
     * Writes the incoming data to both the output streams.
     *
     * @param value The int data to write.
     * @throws IOException
     */
    public void write(int value) throws IOException {
        captureStream.write(value);
        passThroughStream.write(value);
    }

    /**
     * Writes the incoming data to both the output streams.
     *
     * @param value The bytes to write to the streams.
     * @throws IOException
     */
    public void write(byte[] value) throws IOException {
        captureStream.write(value);
        passThroughStream.write(value);
    }

    /**
     * Writes the incoming data to both the output streams.
     *
     * @param b The bytes to write out to the streams.
     * @param off The offset into the byte data where writing should begin.
     * @param len The number of bytes to write.
     * @throws IOException
     */
    public void write(byte[] b, int off, int len) throws IOException {
        captureStream.write(b, off, len);
        passThroughStream.write(b, off, len);
    }

    /** 
     * Flushes both the output streams.
     * @throws IOException
     */    
    public void flush() throws IOException {
        super.flush();
        captureStream.flush(); //why not?
        passThroughStream.flush();
    }    

    /**
     * Closes both the output streams.
     * @throws IOException 
     */    
    public void close() throws IOException {
        super.close();
        captureStream.close();
        passThroughStream.close();
    }    

}