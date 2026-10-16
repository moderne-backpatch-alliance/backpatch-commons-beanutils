/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package org.apache.commons.beanutils.bugs;

import java.math.RoundingMode;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

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

    public EnumDeclaringClassTestCase(String name) {
        super(name);
    }

    /**
     * By default opt-in to security that does not allow access to "declaringClass".
     */
    public void testSuppressClassPropertyByDefaultFromBeanUtilsBean() throws Exception {
        final Fixture fixture = new Fixture();
        try {
            BeanUtils.getProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through BeanUtils.getProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
        try {
            PropertyUtils.getNestedProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through PropertyUtils.getNestedProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
    }

    /**
     * By default opt-in to security that does not allow access to "declaringClass".
     */
    public void testSuppressClassPropertyByDefaultFromPropertyUtilsBean() throws Exception {
        final Fixture fixture = new Fixture();
        try {
            PropertyUtils.getProperty(fixture, "testEnum.declaringClass.classLoader");
            fail("Could access declaringClass property through PropertyUtils.getProperty");
        } catch (final NoSuchMethodException ex) {
            // ok
        }
    }
}
