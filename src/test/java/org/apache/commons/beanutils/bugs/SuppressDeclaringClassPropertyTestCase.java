/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.beanutils.bugs;

import java.beans.PropertyDescriptor;
import java.math.RoundingMode;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.beanutils.locale.LocaleBeanUtilsBean;

/**
 * An enum's "declaringClass" property is not reachable through the other
 * entry points that read property descriptors.
 */
public class SuppressDeclaringClassPropertyTestCase extends TestCase {

    public void testPropertyDescriptorsOmitDeclaringClass() {
        assertFalse(containsName(PropertyUtils.getPropertyDescriptors(RoundingMode.class), "declaringClass"));
        assertFalse(containsName(new PropertyUtilsBean().getPropertyDescriptors(RoundingMode.HALF_UP), "declaringClass"));
    }

    public void testDescribeOmitsDeclaringClass() throws Exception {
        assertFalse(BeanUtils.describe(RoundingMode.HALF_UP).containsKey("declaringClass"));
        assertFalse(PropertyUtils.describe(RoundingMode.HALF_UP).containsKey("declaringClass"));
    }

    public void testPropertyUtilsBeanCannotReadDeclaringClass() throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        assertNull(pub.getPropertyDescriptor(RoundingMode.HALF_UP, "declaringClass"));
        assertFalse(pub.isReadable(RoundingMode.HALF_UP, "declaringClass"));
        try {
            pub.getSimpleProperty(RoundingMode.HALF_UP, "declaringClass");
            fail("Could access declaringClass property through PropertyUtilsBean.getSimpleProperty");
        } catch (NoSuchMethodException ex) {
            // ok
        }
    }

    public void testLocaleBeanUtilsBeanCannotReadDeclaringClass() throws Exception {
        try {
            new LocaleBeanUtilsBean().getProperty(new EnumDeclaringClassTestCase.Fixture(), "testEnum.declaringClass");
            fail("Could access declaringClass property through LocaleBeanUtilsBean.getProperty");
        } catch (NoSuchMethodException ex) {
            // ok
        }
    }

    public void testWrapDynaClassOmitsDeclaringClass() {
        assertNull(WrapDynaClass.createDynaClass(RoundingMode.class).getDynaProperty("declaringClass"));
    }

    private static boolean containsName(PropertyDescriptor[] descriptors, String name) {
        for (int i = 0; i < descriptors.length; i++) {
            if (name.equals(descriptors[i].getName())) {
                return true;
            }
        }
        return false;
    }
}
