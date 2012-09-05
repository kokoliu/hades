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

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Conventions工具类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class Conventions {

	/**
	 * Suffix added to names when using arrays.
	 */
	private static final String PLURAL_SUFFIX = "List";


	/**
	 * Set of interfaces that are supposed to be ignored
	 * when searching for the 'primary' interface of a proxy.
	 */
	private static final Set<Class> ignoredInterfaces = new HashSet<Class>();

	static {
		ignoredInterfaces.add(Serializable.class);
		ignoredInterfaces.add(Externalizable.class);
		ignoredInterfaces.add(Cloneable.class);
		ignoredInterfaces.add(Comparable.class);
	}


	public static String getVariableName(Object value) {
		Assert.notNull(value, "Value must not be null");
		Class valueClass;
		boolean pluralize = false;

		if (value.getClass().isArray()) {
			valueClass = value.getClass().getComponentType();
			pluralize = true;
		}
		else if (value instanceof Collection) {
			Collection collection = (Collection) value;
			if (collection.isEmpty()) {
				throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
			}
			Object valueToCheck = peekAhead(collection);
			valueClass = getClassForValue(valueToCheck);
			pluralize = true;
		}
		else {
			valueClass = getClassForValue(value);
		}

		String name = ClassUtil.getShortNameAsProperty(valueClass);
		return (pluralize ? pluralize(name) : name);
	}

	
	private static Class getClassForValue(Object value) {
		Class valueClass = value.getClass();
		if (Proxy.isProxyClass(valueClass)) {
			Class[] ifcs = valueClass.getInterfaces();
			for (Class ifc : ifcs) {
				if (!ignoredInterfaces.contains(ifc)) {
					return ifc;
				}
			}
		}
		else if (valueClass.getName().lastIndexOf('$') != -1 && valueClass.getDeclaringClass() == null) {
			// '$' in the class name but no inner class -
			// assuming it's a special subclass (e.g. by OpenJPA)
			valueClass = valueClass.getSuperclass();
		}
		return valueClass;
	}

	/**
	 * Pluralize the given name.
	 */
	private static String pluralize(String name) {
		return name + PLURAL_SUFFIX;
	}

	/**
	 * Retrieves the <code>Class</code> of an element in the <code>Collection</code>.
	 * The exact element for which the <code>Class</code> is retreived will depend
	 * on the concrete <code>Collection</code> implementation.
	 */
	private static Object peekAhead(Collection collection) {
		Iterator it = collection.iterator();
		if (!it.hasNext()) {
			throw new IllegalStateException(
					"Unable to peek ahead in non-empty collection - no element found");
		}
		Object value = it.next();
		if (value == null) {
			throw new IllegalStateException(
					"Unable to peek ahead in non-empty collection - only null element found");
		}
		return value;
	}

}
