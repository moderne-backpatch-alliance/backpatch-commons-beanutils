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

import java.math.RoundingMode;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * Fix CVE: https://nvd.nist.gov/vuln/detail/CVE-2025-48734
 */
public class EnumDeclaringClassTestCase extends TestCase {

    // A JDK enum: this source level (1.3) cannot declare one.
    public static class Fixture {

        String name = "default";
        RoundingMode testEnum = RoundingMode.HALF_UP;

        public String getName() {
            return name;
        }

        public RoundingMode getTestEnum() {
            return testEnum;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setTestEnum(final RoundingMode day) {
            this.testEnum = day;
        }
    }

    /**
     * By default opt-in to security that does not allow access to "declaringClass".
     */
    public void testSuppressClassPropertyByDefaultFromBeanUtilsBean() throws Exception {
        final Fixture fixture = new Fixture();
        final BeanUtilsBean bub = new BeanUtilsBean();
        try {
            bub.getProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through BeanUtilsBean.getProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
        try {
            bub.getPropertyUtils().getNestedProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through PropertyUtilsBean.getNestedProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
    }

    /**
     * By default opt-in to security that does not allow access to "declaringClass".
     */
    public void testSuppressClassPropertyByDefaultFromPropertyUtilsBean() throws Exception {
        final Fixture fixture = new Fixture();
        final PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        try {
            propertyUtilsBean.getNestedProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through PropertyUtilsBean.getNestedProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
    }
}
