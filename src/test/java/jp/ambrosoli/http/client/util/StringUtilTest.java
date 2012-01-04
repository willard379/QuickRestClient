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
package jp.ambrosoli.http.client.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

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

}
