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
package jp.ambrosoli.quickrestclient.apache.response;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.util.StringUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApacheHttpResponseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Header[] SAMPLE_HEADERS = new Header[] {
            new BasicHeader("Transfer-Encoding", "chunked"),
            new BasicHeader("Upgrade", "HTTP/2.0, SHTTP/1.3"),
            new BasicHeader("Location", "http://www.ambrosoli.jp/test-server/"),
            new BasicHeader("Set-Cookie", "name=willard379"),
            new BasicHeader("Set-Cookie", "age=17"), };

    @Test
    public void testConstruct() {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Assert
        assertThat(httpResponse, is(notNullValue()));

    }

    @Test(expected = NullPointerException.class)
    public void testConstruct_NoEntity() {

        // Act
        new ApacheHttpResponse(null);
    }

    @Test
    public void testGetAllHeaders() {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        when(response.getAllHeaders()).thenReturn(this.SAMPLE_HEADERS);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        List<HttpHeader> headers = httpResponse.getAllHeaders();

        // Assert
        assertThat(headers, is(notNullValue()));
        assertThat(headers.size(), is(5));

    }

    @Test
    public void testGetAsByteArray() throws Exception {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("Stay here, I'll be back"));

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        byte[] asByteArray = httpResponse.getAsByteArray();

        // Assert
        assertThat(asByteArray, is(equalTo("Stay here, I'll be back".getBytes())));
    }

    @Test
    public void testGetAsInputStream() throws IOException {

        InputStream input = null;
        try {

            // Arrange
            HttpResponse response = mock(HttpResponse.class);
            when(response.getEntity()).thenReturn(new StringEntity("I am a pen!"));

            // Act
            ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
            input = httpResponse.getAsInputStream();

            // Assert
            assertThat(input, is(notNullValue()));
            String str = new BufferedReader(new InputStreamReader(input)).readLine();
            assertThat(str, is(equalTo("I am a pen!")));

        } finally {
            input.close();
        }
    }

    @Test
    public void testGetAsString() throws Exception {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String strResponse = httpResponse.getAsString();

        // Assert
        assertThat(strResponse, is(equalTo("There is an apple.")));

    }

    @Test
    public void testGetAsString_WithCharEncoding() throws Exception {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String strResponse = httpResponse.getAsString(StringUtil.DEFAULT_ENCODING);

        // Assert
        assertThat(strResponse, is(equalTo("There is an apple.")));

    }

    @Test
    public void testGetContentType() throws Exception {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        StringEntity entity = new StringEntity("I am a pen!");
        entity.setContentType("application/json");
        when(response.getEntity()).thenReturn(entity);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Assert
        assertThat(contentType, is(equalTo("application/json")));
    }

    @Test
    public void testGetContentType_NoEntity() {
        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(null);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Assert
        assertThat(contentType, is(nullValue()));
    }

    @Test
    public void testGetContentType_NoHeader() throws Exception {

        // Arrange
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setEntity(new ByteArrayEntity(new byte[0]));

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Assert
        assertThat(contentType, is(nullValue()));
    }

    @Test
    public void testGetContentLength() {

        // Arrange
        HttpResponse response = mock(HttpResponse.class);
        byte[] data = new byte[100];
        when(response.getEntity()).thenReturn(new ByteArrayEntity(data));

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        long contentLength = httpResponse.getContentLength();

        // Assert
        assertThat((long) data.length, is(equalTo(contentLength)));
    }

    @Test
    public void testGetContentLength_NoEntity() {

        // Arrange
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Act
        long contentLength = httpResponse.getContentLength();

        // Assert
        assertThat(contentLength, is(0L));
    }

    @Test
    public void testGetHeader() {

        // Arrange
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        HttpHeader header = httpResponse.getHeader("Location");

        // Assert
        assertThat(header, is(notNullValue()));
    }

    @Test
    public void testGetHeaders() {

        // Arrange
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        List<HttpHeader> headers = httpResponse.getHeaders("Set-Cookie");

        // Assert
        assertThat(headers, is(notNullValue()));
        assertThat(headers.size(), is(2));
        assertThat(headers.get(0).getValue(), is(equalTo("name=willard379")));
        assertThat(headers.get(1).getValue(), is(equalTo("age=17")));
    }

    @Test
    public void testGetStatusCode() {

        // Arrange
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        // Act
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        int statusCode = httpResponse.getStatusCode();

        // Assert
        assertThat(statusCode, is(200));
    }

    @Test
    public void testIs200_OK() {

        // Arrange
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(new BasicHttpResponse(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));

        // Act
        boolean isOk = httpResponse.isSuccess();

        // Assert
        assertThat(isOk, is(true));

    }

    @Test
    public void testIs200_OK_False() {

        // Arrange
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(new BasicHttpResponse(
                HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"));

        // Act
        boolean isOk = httpResponse.isSuccess();

        // Assert
        assertThat(isOk, is(false));

    }

    @Test
    public void testConvertHttpHeaders() {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        Header[] headers = new Header[5];
        headers[0] = new BasicHeader("Transfer-Encoding", "chunked");
        headers[1] = new BasicHeader("Upgrade", "HTTP/2.0, SHTTP/1.3");
        headers[2] = new BasicHeader("Location", "http://www.ambrosoli.jp/test-server/");
        headers[3] = new BasicHeader("Set-Cookie", "name=willard379");
        headers[4] = new BasicHeader("Set-Cookie", "age=17");

        // Act
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(headers);

        // Assert
        assertThat(headerList, is(notNullValue()));
        for (int i = 0; i < headers.length; i++) {
            assertThat(headerList.get(i).getName(), is(equalTo(headers[i].getName())));
            assertThat(headerList.get(i).getValue(), is(equalTo(headers[i].getValue())));
        }

    }

    @Test
    public void testConvertHttpHeaders_Null() {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Act
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(null);

        // Assert
        assertThat(headerList, is(nullValue()));

    }

    @Test
    public void testConvertHttpHeaders_Empty() {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Act
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(new Header[0]);

        // Assert
        assertThat(headerList, is(notNullValue()));
        assertThat(headerList.isEmpty(), is(true));
    }

    @Test
    public void testConvertHttpHeader() {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        Header header = new BasicHeader("Transfer-Encoding", "chunked");

        // Act
        HttpHeader httpHeader = httpResponse.convertHttpHeader(header);

        // Assert
        assertThat(header, is(notNullValue()));
        assertThat(header.getName(), is(httpHeader.getName()));
        assertThat(header.getValue(), is(httpHeader.getValue()));

    }

    @Test
    public void testConvertHttpHeader_Null() {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Act
        HttpHeader httpHeader = httpResponse.convertHttpHeader(null);

        // Assert
        assertThat(httpHeader, is(nullValue()));
    }

    @Test
    public void testToByteArray() throws IOException {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        String src = "And so, my fellow Americans: ask not what your country can do for you - ask what you can do for your country.";
        HttpEntity entity = new StringEntity(src);

        // Act
        byte[] data = httpResponse.toByteArray(entity);

        // Assert
        assertThat(data, is(notNullValue()));
        assertThat(data, is(equalTo(src.getBytes())));

    }

    @Test
    public void testToByteArray_Null() throws IOException {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        HttpEntity entity = null;

        // Act
        byte[] data = httpResponse.toByteArray(entity);

        // Assert
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testToByteArray_Exception() throws IOException {

        // Arrange
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 0);

        when(input.read((byte[]) any())).thenThrow(new IOException());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Act
        httpResponse.toByteArray(entity);

        // Assert
        fail();
    }

}
