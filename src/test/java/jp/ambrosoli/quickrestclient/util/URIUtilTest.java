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

import java.net.URI;

import jp.ambrosoli.quickrestclient.util.URIUtil;

import org.junit.Test;

public class URIUtilTest {

    @Test
    public void testIsSSL() {
        assertThat(URIUtil.isSSL(URI.create("http://ambrosoli.jp/")), is(false));
        assertThat(URIUtil.isSSL(URI.create("https://ambrosoli.jp/")), is(true));
        assertThat(URIUtil.isSSL(URI.create("ftp://ambrosoli.jp/")), is(false));
        assertThat(URIUtil.isSSL(URI.create("willard379")), is(false));
        assertThat(URIUtil.isSSL((URI) null), is(false));
    }

    @Test
    public void testIsSSL_String() {
        assertThat(URIUtil.isSSL("http://ambrosoli.jp/"), is(false));
        assertThat(URIUtil.isSSL("https://ambrosoli.jp/"), is(true));
        assertThat(URIUtil.isSSL((String) null), is(false));
        assertThat(URIUtil.isSSL("ftp://ambrosoli.jp/"), is(false));
        assertThat(URIUtil.isSSL("willard379"), is(false));
    }

    @Test
    public void testIsPlain() {
        assertThat(URIUtil.isPlain(URI.create("http://ambrosoli.jp/")), is(true));
        assertThat(URIUtil.isPlain(URI.create("https://ambrosoli.jp/")), is(false));
        assertThat(URIUtil.isPlain(URI.create("ftp://ambrosoli.jp/")), is(false));
        assertThat(URIUtil.isPlain(URI.create("willard379")), is(false));
        assertThat(URIUtil.isPlain((URI) null), is(false));
    }

    @Test
    public void testIsPlain_String() {
        assertThat(URIUtil.isPlain("http://ambrosoli.jp/"), is(true));
        assertThat(URIUtil.isPlain("https://ambrosoli.jp/"), is(false));
        assertThat(URIUtil.isPlain((String) null), is(false));
        assertThat(URIUtil.isPlain("ftp://ambrosoli.jp/"), is(false));
        assertThat(URIUtil.isPlain("willard379"), is(false));
    }

    @Test
    public void testToURI() {
        assertThat(URIUtil.toURI("http://ambrosoli.jp/").toString(),
                is(equalTo("http://ambrosoli.jp/")));
        assertThat(URIUtil.toURI("https://ambrosoli.jp/").toString(),
                is(equalTo("https://ambrosoli.jp/")));
        assertThat(URIUtil.toURI(null), is(nullValue()));
        assertThat(URIUtil.toURI(""), is(nullValue()));
    }

    @Test
    public void testGetPort() {
        assertThat(URIUtil.getPort(URIUtil.toURI("http://ambrosoli.jp:8080/")), is(8080));
        assertThat(URIUtil.getPort(URIUtil.toURI("http://ambrosoli.jp/")), is(80));
        assertThat(URIUtil.getPort(URIUtil.toURI("https://ambrosoli.jp:5432/")), is(5432));
        assertThat(URIUtil.getPort(URIUtil.toURI("https://ambrosoli.jp/")), is(443));
        assertThat(URIUtil.getPort(null), is(-1));
        assertThat(URIUtil.getPort(URIUtil.toURI("ftp://ambrosoli.jp/")), is(-1));
    }

    @Test
    public void testAddQueryString() {
        URI uri = URIUtil.toURI("http://ambrosoli.jp/");
        assertThat(URIUtil.addQueryString(uri, "?a=A").toString(),
                is(equalTo("http://ambrosoli.jp/?a=A")));
        assertThat(URIUtil.addQueryString(uri, null).toString(),
                is(equalTo("http://ambrosoli.jp/")));
        assertThat(URIUtil.addQueryString(uri, "").toString(), is(equalTo("http://ambrosoli.jp/")));
        assertThat(URIUtil.addQueryString(null, "?a=A"), is(nullValue()));
        assertThat(URIUtil.addQueryString(null, null), is(nullValue()));
    }
}
