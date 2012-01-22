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
package jp.ambrosoli.quickrestclient.headers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.apache.headers.ApacheHeaderBuilder;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.NameValueObject;

import org.apache.http.Header;
import org.junit.Test;

public class HttpHeadersTest {

    @Test
    public void testIsEmpty() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeader(new NameValueObject("a", "a"));

        // Act
        boolean empty = httpHeaders.isEmpty();

        // Assert
        assertThat(empty, is(false));
    }

    @Test
    public void testIsEmpty_InitialState() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();

        // Act
        boolean empty = httpHeaders.isEmpty();

        // Assert
        assertThat(empty, is(true));

    }

    @Test
    public void testIsEmpty_Empty() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(new ArrayList<NameValueObject>());

        // Act
        boolean empty = httpHeaders.isEmpty();

        // Assert
        assertThat(empty, is(true));

    }

    @Test
    public void testIsEmpty_Null() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(null);

        // Act
        boolean empty = httpHeaders.isEmpty();

        // Assert
        assertThat(empty, is(true));

    }

    @Test
    public void testGetConfirmedHeaders_InitialState() {

        // Arrange
        ApacheHeaderBuilder builder = new ApacheHeaderBuilder();
        HttpHeaders httpHeaders1 = new HttpHeaders();

        // Act
        Header[] headers1 = httpHeaders1.getConformedHeaders(builder);

        // Assert
        assertThat(headers1, is(nullValue()));

    }

    @Test
    public void testGetConfirmedHeaders_Empty() {

        // Arrange
        ApacheHeaderBuilder builder = new ApacheHeaderBuilder();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(new ArrayList<NameValueObject>());

        // Act
        Header[] headers2 = httpHeaders.getConformedHeaders(builder);

        // Assert
        assertThat(headers2, is(nullValue()));

    }

    @Test
    public void testGetConfirmedHeaders_Null() {

        // Arrange
        ApacheHeaderBuilder builder = new ApacheHeaderBuilder();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(null);

        // Act
        Header[] headers2 = httpHeaders.getConformedHeaders(builder);

        // Assert
        assertThat(headers2, is(nullValue()));

    }

    @Test
    public void testGetConfirmedHeaders() {

        // Arrange
        ApacheHeaderBuilder builder = new ApacheHeaderBuilder();
        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1"));
        values.add(new NameValueObject("Name2", "Value2"));
        values.add(new NameValueObject("Name3", "Value3"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(values);

        // Act
        Header[] headers3 = httpHeaders.getConformedHeaders(builder);

        // Assert
        assertThat(headers3.length, is(values.size()));
        assertThat(headers3[0].getName(), is(equalTo(values.get(0).getName())));
        assertThat(headers3[0].getValue(), is(equalTo(values.get(0).getValue())));
    }

    @Test
    public void tesAddHeader() {

        // Act
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeader(new NameValueObject("Name3", "Value3-2"));

        // Assert
        List<NameValueObject> headers = httpHeaders.getHeaders();
        assertThat(headers.size(), is(1));

        NameValueObject nvo = headers.get(0);
        assertThat(nvo.getName(), is(equalTo("Name3")));
        assertThat(nvo.getValue(), is(equalTo("Value3-2")));

    }

    @Test
    public void tesAddHeader_Null() {

        // Act
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeader(null);

        // Assert
        List<NameValueObject> headers = httpHeaders.getHeaders();
        assertThat(headers.size(), is(0));
    }

    @Test
    public void testAddHeaders() {

        // Arrange
        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1"));
        values.add(new NameValueObject("Name2", "Value2"));
        values.add(new NameValueObject("Name3", "Value3"));

        // Act
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(values);

        // Assert
        List<NameValueObject> headers = httpHeaders.getHeaders();
        assertThat(headers.size(), is(3));

        for (int i = 0; i < headers.size(); i++) {
            assertThat(headers.get(i).getName(), is(equalTo(values.get(i).getName())));
            assertThat(headers.get(i).getValue(), is(equalTo(values.get(i).getValue())));
        }

    }

    @Test
    public void testAddHeaders_Null() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();

        // Act
        httpHeaders.addHeaders(null);

        // Assert
        assertThat(httpHeaders.getHeaders().size(), is(0));
    }

    @Test
    public void testAddHeaders_Append() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();

        // あらかじめ2件分のヘッダーをセットしておく
        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("Name1", "Value1"));
        values1.add(new NameValueObject("Name2", "Value2"));
        httpHeaders.addHeaders(values1);

        // Act
        // 3件分のヘッダーを追加する
        List<NameValueObject> values2 = new ArrayList<NameValueObject>();
        values2.add(new NameValueObject("Name3", "Value3"));
        values2.add(new NameValueObject("Name4", "Value4"));
        values2.add(new NameValueObject("Name5", "Value5"));
        httpHeaders.addHeaders(values2);

        // Assert
        List<NameValueObject> headers = httpHeaders.getHeaders();
        assertThat(headers.size(), is(5));

        httpHeaders.addHeaders(null);
        assertThat(httpHeaders.getHeaders().size(), is(5));
    }

    @Test
    public void testAddHeaders_AppendNull() {

        // Arrange
        // あらかじめ2件分のデータをセットしておく
        HttpHeaders httpHeaders = new HttpHeaders();

        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("Name1", "Value1"));
        values1.add(new NameValueObject("Name2", "Value2"));
        httpHeaders.addHeaders(values1);

        // Act
        // nullを追加する
        httpHeaders.addHeaders(null);

        // Assert
        // 件数が2件のままであること
        assertThat(httpHeaders.getHeaders().size(), is(2));
    }

    @Test
    public void testClear_InitialState() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();

        // Act
        httpHeaders.clear();

        // Assert
        assertThat(httpHeaders.isEmpty(), is(true));

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1"));
        values.add(new NameValueObject("Name2", "Value2"));
        values.add(new NameValueObject("Name3", "Value3"));
        httpHeaders.addHeaders(values);
        httpHeaders.clear();
        assertThat(httpHeaders.isEmpty(), is(true));

    }

    @Test
    public void testClear() {

        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1"));
        values.add(new NameValueObject("Name2", "Value2"));
        values.add(new NameValueObject("Name3", "Value3"));
        httpHeaders.addHeaders(values);

        // Act
        httpHeaders.clear();

        // Assert
        assertThat(httpHeaders.isEmpty(), is(true));

    }

}
