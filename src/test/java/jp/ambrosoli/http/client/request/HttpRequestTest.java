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
package jp.ambrosoli.http.client.request;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.http.client.params.NameValueObject;
import jp.ambrosoli.http.client.util.URIUtil;

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
}
