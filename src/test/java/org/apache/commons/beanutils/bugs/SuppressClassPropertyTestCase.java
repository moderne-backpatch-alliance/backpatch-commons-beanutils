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
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.AlphaBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.beanutils.locale.LocaleBeanUtilsBean;

/**
 * The "class" property is not reachable through the other entry points
 * that read property descriptors.
 */
public class SuppressClassPropertyTestCase extends TestCase {

    public void testPropertyDescriptorsOmitClass() {
        assertFalse(containsName(PropertyUtils.getPropertyDescriptors(AlphaBean.class), "class"));
        assertFalse(containsName(new PropertyUtilsBean().getPropertyDescriptors(new AlphaBean()), "class"));
    }

    public void testDescribeOmitsClass() throws Exception {
        Map described = BeanUtils.describe(new AlphaBean("a"));
        assertFalse(described.containsKey("class"));
        assertEquals("a", described.get("name"));
        assertFalse(PropertyUtils.describe(new AlphaBean("a")).containsKey("class"));
    }

    public void testPropertyUtilsBeanCannotReadClass() throws Exception {
        PropertyUtilsBean pub = new PropertyUtilsBean();
        AlphaBean bean = new AlphaBean();
        assertNull(pub.getPropertyDescriptor(bean, "class"));
        assertFalse(pub.isReadable(bean, "class"));
        try {
            pub.getSimpleProperty(bean, "class");
            fail("Could access class property through PropertyUtilsBean.getSimpleProperty");
        } catch (NoSuchMethodException ex) {
            // ok
        }
    }

    public void testLocaleBeanUtilsBeanCannotReadClass() throws Exception {
        try {
            new LocaleBeanUtilsBean().getProperty(new AlphaBean(), "class");
            fail("Could access class property through LocaleBeanUtilsBean.getProperty");
        } catch (NoSuchMethodException ex) {
            // ok
        }
    }

    public void testWrapDynaClassOmitsClass() {
        assertNull(WrapDynaClass.createDynaClass(AlphaBean.class).getDynaProperty("class"));
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
