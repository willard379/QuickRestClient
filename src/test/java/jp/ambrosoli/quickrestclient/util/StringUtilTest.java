/*
 * Copyright (c) 2011-2012 ambrosoli.jp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.ambrosoli.quickrestclient.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.quickrestclient.exception.UnsupportedEncodingRuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testIsEmpty() {
        assertThat(StringUtil.isEmpty(null), is(true));
        assertThat(StringUtil.isEmpty(""), is(true));
        assertThat(StringUtil.isEmpty(" "), is(false));
        assertThat(StringUtil.isEmpty("bob"), is(false));
        assertThat(StringUtil.isEmpty("  bob  "), is(false));
    }

    @Test
    public void testIsEmpty_StringBuilder() {
        assertThat(StringUtil.isEmpty(new StringBuilder()), is(true));
        assertThat(StringUtil.isEmpty(new StringBuilder(" ")), is(false));
        assertThat(StringUtil.isEmpty(new StringBuilder("bob")), is(false));
        assertThat(StringUtil.isEmpty(new StringBuilder("  bob  ")), is(false));
    }

    @Test
    public void testIsNotEmpty() {
        assertThat(StringUtil.isNotEmpty(null), is(false));
        assertThat(StringUtil.isNotEmpty(""), is(false));
        assertThat(StringUtil.isNotEmpty(" "), is(true));
        assertThat(StringUtil.isNotEmpty("bob"), is(true));
        assertThat(StringUtil.isNotEmpty("  bob  "), is(true));
    }

    @Test
    public void testIsNotEmpty_StringBuilder() {
        assertThat(StringUtil.isNotEmpty(new StringBuilder("")), is(false));
        assertThat(StringUtil.isNotEmpty(new StringBuilder(" ")), is(true));
        assertThat(StringUtil.isNotEmpty(new StringBuilder("bob")), is(true));
        assertThat(StringUtil.isNotEmpty(new StringBuilder("  bob  ")), is(true));
    }

    @Test
    public void testToString() throws Exception {

        // Setup
        byte[] data = "オーシャン".getBytes("eucjp");

        // Exercise
        String result = StringUtil.toString(data, "eucjp");

        // Verify
        assertThat(result, is(equalTo("オーシャン")));
    }

    @Test
    public void testToString_Empty() throws Exception {

        // Setup
        byte[] data = new byte[0];

        // Exercise
        String result = StringUtil.toString(data, "UTF-8");

        // Verify
        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testToString_NullData() throws Exception {

        // Setup
        byte[] data = null;

        // Exercise
        String result = StringUtil.toString(data, "UTF-8");

        // Verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testToString_NullEncoding() throws Exception {

        // Setup
        byte[] data = "ロマンス".getBytes("UTF-8");

        // Exercise
        String result = StringUtil.toString(data, null);

        // Verify
        assertThat(result, is(equalTo("ロマンス")));
    }

    @Test
    public void testToString_Exception() throws Exception {

        // Setup
        byte[] data = "ハネムーン".getBytes("UTF-8");

        // Expected
        this.expectedException.expect(UnsupportedEncodingRuntimeException.class);

        // Exercise
        StringUtil.toString(data, "UTF-48");

        // Verify
        fail("UnsupportedEncodingRuntimeExceptionが発生しませんでした。");
    }
}
