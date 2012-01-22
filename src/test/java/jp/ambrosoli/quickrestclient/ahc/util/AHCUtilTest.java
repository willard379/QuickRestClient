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
package jp.ambrosoli.quickrestclient.ahc.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AHCUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testConsumeEntity() throws Exception {

        // Arrange
        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 100);
        assertThat(entity.isStreaming(), is(true));

        // Act
        AHCUtil.consumeEntity(entity);

        // Assert
        verify(input).close();

    }

    @Test
    public void testConsumeEntity_Null() throws Exception {

        // Act
        AHCUtil.consumeEntity(null);
    }

    @Test
    public void testConsumeEntity_Empty() throws Exception {
        AHCUtil.consumeEntity(new BasicHttpEntity());
    }

    @Test
    public void testConsumeEntity_NoInputStream() throws Exception {

        // Arrange
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenReturn(null);

        // Act
        AHCUtil.consumeEntity(entity);

    }

    @Test
    public void testConsumeEntity_Exception() throws Exception {

        // Arrange
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenThrow(new IOException());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Act
        AHCUtil.consumeEntity(entity);

    }

    @Test
    public void testConvertHttpHeaders() {

        // Arrange
        Header[] headers = new Header[5];
        headers[0] = new BasicHeader("Transfer-Encoding", "chunked");
        headers[1] = new BasicHeader("Upgrade", "HTTP/2.0, SHTTP/1.3");
        headers[2] = new BasicHeader("Location", "http://www.ambrosoli.jp/test-server/");
        headers[3] = new BasicHeader("Set-Cookie", "name=willard379");
        headers[4] = new BasicHeader("Set-Cookie", "age=17");

        // Act
        List<HttpHeader> headerList = AHCUtil.convertHttpHeaders(headers);

        // Assert
        assertThat(headerList, is(notNullValue()));
        for (int i = 0; i < headers.length; i++) {
            assertThat(headerList.get(i).getName(), is(equalTo(headers[i].getName())));
            assertThat(headerList.get(i).getValue(), is(equalTo(headers[i].getValue())));
        }

    }

    @Test
    public void testConvertHttpHeaders_Null() {

        // Act
        List<HttpHeader> headerList = AHCUtil.convertHttpHeaders(null);

        // Assert
        assertThat(headerList, is(nullValue()));

    }

    @Test
    public void testConvertHttpHeaders_Empty() {

        // Act
        List<HttpHeader> headerList2 = AHCUtil.convertHttpHeaders(new Header[0]);

        // Assert
        assertThat(headerList2, is(notNullValue()));
        assertThat(headerList2.isEmpty(), is(true));
    }

    @Test
    public void testConvertHttpHeader() {

        // Arrange
        Header header = new BasicHeader("Transfer-Encoding", "chunked");

        // Act
        HttpHeader httpHeader = AHCUtil.convertHttpHeader(header);

        // Assert
        assertThat(header, is(notNullValue()));
        assertThat(header.getName(), is(httpHeader.getName()));
        assertThat(header.getValue(), is(httpHeader.getValue()));

        assertThat(AHCUtil.convertHttpHeader(null), is(nullValue()));
    }

    @Test
    public void testConvertHttpHeader_Null() {

        // Act
        HttpHeader httpHeader = AHCUtil.convertHttpHeader(null);

        // Assert
        assertThat(httpHeader, is(nullValue()));
    }

    @Test
    public void testToByteArray() throws IOException {

        // Arrange
        String src = "And so, my fellow Americans: ask not what your country can do for you - ask what you can do for your country.";
        HttpEntity entity = new StringEntity(src);

        // Act
        byte[] data = AHCUtil.toByteArray(entity);

        // Assert
        assertThat(data, is(notNullValue()));
        assertThat(data, is(equalTo(src.getBytes())));

        assertThat(AHCUtil.toByteArray(null), is(nullValue()));
    }

    @Test
    public void testToByteArray_Null() throws IOException {

        // Arrange
        HttpEntity entity = null;

        // Act
        byte[] data = AHCUtil.toByteArray(entity);

        // Assert
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testToByteArray_Exception() throws IOException {

        // Arrange
        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 0);

        when(input.read((byte[]) any())).thenThrow(new IOException());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Act
        AHCUtil.toByteArray(entity);

    }

}
