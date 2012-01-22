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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
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
    public void testConstruct() {

        // Arrange
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/test-server/");

        // Act
        HttpRequest httpRequest = new HttpRequest(uri);

        // Assert
        assertThat(httpRequest, is(notNullValue()));
    }

    @Test
    public void testConstruct_Null() {

        // Arrange
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("URI mey not to be null")));

        // Act
        URI uri = null;
        new HttpRequest(uri);

        // Assert
        fail("例外が発生しませんでした");
    }

    @Test
    public void testAddHeaders() {

        // Arrange
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest httpRequest = new HttpRequest(uri);

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("name1", "value1"));
        values.add(new NameValueObject("name2", "value2"));
        values.add(new NameValueObject("name3", "value3"));
        values.add(new NameValueObject("name4", "value4"));

        // Act
        httpRequest.addHeaders(values);

        // Assert
        assertThat(httpRequest.headers.getHeaders().size(), is(values.size()));
    }

    @Test
    public void testAddHeaders_CallTwice() {

        // Arrange
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest httpRequest = new HttpRequest(uri);

        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("name1", "value1"));
        values1.add(new NameValueObject("name2", "value2"));
        values1.add(new NameValueObject("name3", "value3"));

        List<NameValueObject> values2 = new ArrayList<NameValueObject>();
        values2.add(new NameValueObject("name4", "value4"));
        values2.add(new NameValueObject("name5", "value5"));
        values2.add(new NameValueObject("name6", "value6"));

        // Act
        httpRequest.addHeaders(values1);
        httpRequest.addHeaders(values2);

        // Assert
        assertThat(httpRequest.headers.getHeaders().size(), is(equalTo(6)));
    }

    @Test
    public void testAddHeaders_Null() {

        // Arrange
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest httpRequest = new HttpRequest(uri);

        // Act
        httpRequest.addHeaders(null);

        // Assert
        assertThat(httpRequest.headers.isEmpty(), is(true));

    }

    @Test
    public void testAddHeader() {

        // Arrange
        HttpRequest httpRequest = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));
        NameValueObject header = new NameValueObject("name", "value");

        // Act
        httpRequest.addHeader(header);

        // Assert
        assertThat(httpRequest.headers.getHeaders().size(), is(1));
        assertThat(httpRequest.headers.getHeaders().get(0), is(sameInstance(header)));
    }

    @Test
    public void testAddHeader_CallMany() {

        // Arrange
        HttpRequest httpRequest = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Act
        httpRequest.addHeader(new HttpHeader("name1", "value1"));
        httpRequest.addHeader(new HttpHeader("name2", "value2"));
        httpRequest.addHeader(new HttpHeader("name3", "value3"));
        httpRequest.addHeader(new HttpHeader("name4", "value4"));

        // Assert
        assertThat(httpRequest.headers.getHeaders().size(), is(4));
    }

    @Test
    public void testAddHeaders_Empty() {

        // Arrange
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        HttpRequest httpRequest = new HttpRequest(uri);
        List<NameValueObject> values = new ArrayList<NameValueObject>();

        // Act
        httpRequest.addHeaders(values);

        // Assert
        assertThat(httpRequest.headers.isEmpty(), is(true));

    }

    @Test
    public void testAddHeader_Null() {

        // Arrange
        HttpRequest httpRequest = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));
        NameValueObject header = null;
        // Act
        httpRequest.addHeader(header);

        // Assert
        assertThat(httpRequest.headers.isEmpty(), is(true));

    }

    @Test
    public void testSetAuthInfo() {

        // Arrange
        HttpRequest httpRequest = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Act
        AuthInfo authInfo = new AuthInfo(AuthType.BASIC, "user.name", "user.password");
        httpRequest.setAuthInfo(authInfo);

        // Assert
        assertThat(httpRequest.getAuthInfo(), is(equalTo(authInfo)));
    }

    @Test
    public void testSetAuthInfo_Null() {

        // Arrange
        HttpRequest httpRequest = new HttpRequest(URIUtil.toURI("http://www.ambrosoli.jp/"));

        // Act
        httpRequest.setAuthInfo(null);

        // Assert
        assertThat(httpRequest.getAuthInfo(), is(nullValue()));
    }

}
