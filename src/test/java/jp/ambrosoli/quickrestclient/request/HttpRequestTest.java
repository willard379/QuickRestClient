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
package jp.ambrosoli.quickrestclient.request;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.util.URIUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HttpRequestTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void コンストラクタの引数にURIを指定して呼び出すと_HttpRequestが生成されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/test-server/");

        // Exercise
        HttpRequest actual = new HttpRequest(uri);

        // Verify
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void コンストラクタの引数にnullを渡すと_IllegalArgumentExceptionが発生すること() {

        // Setup
        URI uri = null;

        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("URI mey not to be null")));

        // Exercise
        new HttpRequest(uri);

        // Verify
        fail("例外が発生しませんでした");
    }

    @Test
    public void addHeadersの引数にNameValueObject配列を渡すと_HttpRequestにNameValueObjectのListが保持されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest sut = new HttpRequest(uri);

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("name1", "value1"));
        values.add(new NameValueObject("name2", "value2"));
        values.add(new NameValueObject("name3", "value3"));
        values.add(new NameValueObject("name4", "value4"));

        // Exercise
        sut.addHeaders(values);

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(values.size()));
    }

    @Test
    public void addHeadersを２回呼び出すと_上書きではなく追加で保持されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest sut = new HttpRequest(uri);

        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("name1", "value1"));
        values1.add(new NameValueObject("name2", "value2"));
        values1.add(new NameValueObject("name3", "value3"));

        List<NameValueObject> values2 = new ArrayList<NameValueObject>();
        values2.add(new NameValueObject("name4", "value4"));
        values2.add(new NameValueObject("name5", "value5"));
        values2.add(new NameValueObject("name6", "value6"));

        // Exercise
        sut.addHeaders(values1);
        sut.addHeaders(values2);

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(equalTo(6)));
    }

    @Test
    public void addHeadersにnullを渡すと_HttpRequestに追加されないこと() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest sut = new HttpRequest(uri);

        // Exercise
        sut.addHeaders(Arrays.asList(new HttpHeader("name", "value")));
        sut.addHeaders(null);

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(1));

    }

    @Test
    public void addHeaderの引数にNameValueObjectを渡すと_HttpRequestに追加されること() {

        // Setup
        HttpRequest sut = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));
        NameValueObject header = new NameValueObject("name", "value");

        // Exercise
        sut.addHeader(header);

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(1));
        assertThat(sut.headers.getHeaders().get(0), is(sameInstance(header)));
    }

    @Test
    public void addHeaderを複数回呼び出すと_上書きではなく追加で保持されること() {

        // Setup
        HttpRequest sut = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Exercise
        sut.addHeader(new HttpHeader("name1", "value1"));
        sut.addHeader(new HttpHeader("name2", "value2"));
        sut.addHeader(new HttpHeader("name3", "value3"));
        sut.addHeader(new HttpHeader("name4", "value4"));

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(4));
    }

    @Test
    public void addHeaderの引数にnullを渡すと_HttpRequestに追加されないこと() {

        // Setup
        HttpRequest sut = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Exercise
        sut.addHeader(null);
        sut.addHeader(null);
        sut.addHeader(null);
        sut.addHeader(new HttpHeader("name4", "value4"));

        // Verify
        assertThat(sut.headers.getHeaders().size(), is(1));
    }

    @Test
    public void addHeadersの引数に空のListを渡した場合_HttpRequestに追加されないこと() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest sut = new HttpRequest(uri);
        List<NameValueObject> values = new ArrayList<NameValueObject>();

        // Exercise
        sut.addHeaders(values);

        // Verify
        assertThat(sut.headers.isEmpty(), is(true));

    }

    @Test
    public void setAuthInfoの引数にAuthInfoを指定した場合_HttpRequestにAuthInfoが保持されること() {

        // Setup
        HttpRequest sut = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Exercise
        AuthInfo authInfo = new AuthInfo(AuthType.BASIC, "user.name", "user.password");
        sut.setAuthInfo(authInfo);

        // Verify
        assertThat(sut.getAuthInfo(), is(equalTo(authInfo)));
    }

    @Test
    public void setAuthInfoの引数にnullを渡した場合_HttpRequestにnullが保持されること() {

        // Setup
        HttpRequest sut = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Exercise
        sut.setAuthInfo(null);

        // Verify
        assertThat(sut.getAuthInfo(), is(nullValue()));
    }

}
