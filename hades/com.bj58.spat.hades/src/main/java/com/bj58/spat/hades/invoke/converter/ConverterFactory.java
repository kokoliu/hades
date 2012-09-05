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
package com.bj58.spat.hades.invoke.converter;

import java.util.HashMap;
import java.util.Map;

import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;

/**
 * Factory for all converters.
 * 
 *  @author Service Platform Architecture Team (spat@58.com)
 */
public class ConverterFactory {

    private static final Log log = LogFactory.getLog(ConverterFactory.class);

    private Map<Class<?>, Converter<?>> map = new HashMap<Class<?>, Converter<?>>();

    public ConverterFactory() {
        loadInternal();
    }

    void loadInternal() {
        Converter<?> c = null;
        
        c = new StringConverter();
        map.put(String.class, c);

        c = new BooleanConverter();
        map.put(boolean.class, c);
        map.put(Boolean.class, c);

        c = new CharacterConverter();
        map.put(char.class, c);
        map.put(Character.class, c);

        c = new ByteConverter();
        map.put(byte.class, c);
        map.put(Byte.class, c);

        c = new ShortConverter();
        map.put(short.class, c);
        map.put(Short.class, c);

        c = new IntegerConverter();
        map.put(int.class, c);
        map.put(Integer.class, c);

        c = new LongConverter();
        map.put(long.class, c);
        map.put(Long.class, c);

        c = new FloatConverter();
        map.put(float.class, c);
        map.put(Float.class, c);

        c = new DoubleConverter();
        map.put(double.class, c);
        map.put(Double.class, c);
    }

    public void loadExternalConverter(String typeClass, String converterClass) {
        try {
            loadExternalConverter(Class.forName(typeClass), (Converter<?>) Class.forName(converterClass).newInstance());
        }
        catch (Exception e) {
            log.warn("Cannot load converter '" + converterClass + "' for type '" + typeClass + "'.", e);
        }
    }

    public void loadExternalConverter(Class<?> clazz, Converter<?> converter) {
        if (clazz==null)
            throw new NullPointerException("Class is null.");
        if (converter==null)
            throw new NullPointerException("Converter is null.");
        if (map.containsKey(clazz)) {
            log.warn("Cannot replace the exist converter for type '" + clazz.getName() + "'.");
            return;
        }
        map.put(clazz, converter);
    }

    public boolean canConvert(Class<?> clazz) {
        return clazz.equals(String.class) || map.containsKey(clazz);
    }

    public Object convert(Class<?> clazz, String s) {
        Converter<?> c = map.get(clazz);
        return c.convert(s);
    }
}
