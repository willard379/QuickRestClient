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

        // Setup
        HttpResponse response = mock(HttpResponse.class);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Verify
        assertThat(httpResponse, is(notNullValue()));

    }

    @Test(expected = NullPointerException.class)
    public void testConstruct_NoEntity() {

        // Exercise
        new ApacheHttpResponse(null);
    }

    @Test
    public void testGetAllHeaders() {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getAllHeaders()).thenReturn(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        List<HttpHeader> headers = httpResponse.getAllHeaders();

        // Verify
        assertThat(headers, is(notNullValue()));
        assertThat(headers.size(), is(5));

    }

    @Test
    public void testGetAsByteArray() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("Stay here, I'll be back"));

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        byte[] asByteArray = httpResponse.getAsByteArray();

        // Verify
        assertThat(asByteArray, is(equalTo("Stay here, I'll be back".getBytes())));
    }

    @Test
    public void testGetAsInputStream() throws IOException {

        InputStream input = null;
        try {

            // Setup
            HttpResponse response = mock(HttpResponse.class);
            when(response.getEntity()).thenReturn(new StringEntity("I am a pen!"));

            // Exercise
            ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
            input = httpResponse.getAsInputStream();

            // Verify
            assertThat(input, is(notNullValue()));
            String str = new BufferedReader(new InputStreamReader(input)).readLine();
            assertThat(str, is(equalTo("I am a pen!")));

        } finally {
            input.close();
        }
    }

    @Test
    public void testGetAsString() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String strResponse = httpResponse.getAsString();

        // Verify
        assertThat(strResponse, is(equalTo("There is an apple.")));

    }

    @Test
    public void testGetAsString_WithCharEncoding() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String strResponse = httpResponse.getAsString(StringUtil.DEFAULT_ENCODING);

        // Verify
        assertThat(strResponse, is(equalTo("There is an apple.")));

    }

    @Test
    public void testGetContentType() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        StringEntity entity = new StringEntity("I am a pen!");
        entity.setContentType("application/json");
        when(response.getEntity()).thenReturn(entity);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Verify
        assertThat(contentType, is(equalTo("application/json")));
    }

    @Test
    public void testGetContentType_NoEntity() {
        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(null);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Verify
        assertThat(contentType, is(nullValue()));
    }

    @Test
    public void testGetContentType_NoHeader() throws Exception {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setEntity(new ByteArrayEntity(new byte[0]));

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        String contentType = httpResponse.getContentType();

        // Verify
        assertThat(contentType, is(nullValue()));
    }

    @Test
    public void testGetContentLength() {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        byte[] data = new byte[100];
        when(response.getEntity()).thenReturn(new ByteArrayEntity(data));

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        long contentLength = httpResponse.getContentLength();

        // Verify
        assertThat((long) data.length, is(equalTo(contentLength)));
    }

    @Test
    public void testGetContentLength_NoEntity() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Exercise
        long contentLength = httpResponse.getContentLength();

        // Verify
        assertThat(contentLength, is(0L));
    }

    @Test
    public void testGetHeader() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        HttpHeader header = httpResponse.getHeader("Location");

        // Verify
        assertThat(header, is(notNullValue()));
    }

    @Test
    public void testGetHeaders() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        List<HttpHeader> headers = httpResponse.getHeaders("Set-Cookie");

        // Verify
        assertThat(headers, is(notNullValue()));
        assertThat(headers.size(), is(2));
        assertThat(headers.get(0).getValue(), is(equalTo("name=willard379")));
        assertThat(headers.get(1).getValue(), is(equalTo("age=17")));
    }

    @Test
    public void testGetStatusCode() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        // Exercise
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);
        int statusCode = httpResponse.getStatusCode();

        // Verify
        assertThat(statusCode, is(200));
    }

    @Test
    public void testIs200_OK() {

        // Setup
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(new BasicHttpResponse(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));

        // Exercise
        boolean isOk = httpResponse.isSuccess();

        // Verify
        assertThat(isOk, is(true));

    }

    @Test
    public void testIs200_OK_False() {

        // Setup
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(new BasicHttpResponse(
                HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"));

        // Exercise
        boolean isOk = httpResponse.isSuccess();

        // Verify
        assertThat(isOk, is(false));

    }

    @Test
    public void testConvertHttpHeaders() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        Header[] headers = new Header[5];
        headers[0] = new BasicHeader("Transfer-Encoding", "chunked");
        headers[1] = new BasicHeader("Upgrade", "HTTP/2.0, SHTTP/1.3");
        headers[2] = new BasicHeader("Location", "http://www.ambrosoli.jp/test-server/");
        headers[3] = new BasicHeader("Set-Cookie", "name=willard379");
        headers[4] = new BasicHeader("Set-Cookie", "age=17");

        // Exercise
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(headers);

        // Verify
        assertThat(headerList, is(notNullValue()));
        for (int i = 0; i < headers.length; i++) {
            assertThat(headerList.get(i).getName(), is(equalTo(headers[i].getName())));
            assertThat(headerList.get(i).getValue(), is(equalTo(headers[i].getValue())));
        }

    }

    @Test
    public void testConvertHttpHeaders_Null() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Exercise
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(null);

        // Verify
        assertThat(headerList, is(nullValue()));

    }

    @Test
    public void testConvertHttpHeaders_Empty() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Exercise
        List<HttpHeader> headerList = httpResponse.convertHttpHeaders(new Header[0]);

        // Verify
        assertThat(headerList, is(notNullValue()));
        assertThat(headerList.isEmpty(), is(true));
    }

    @Test
    public void testConvertHttpHeader() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        Header header = new BasicHeader("Transfer-Encoding", "chunked");

        // Exercise
        HttpHeader httpHeader = httpResponse.convertHttpHeader(header);

        // Verify
        assertThat(header, is(notNullValue()));
        assertThat(header.getName(), is(httpHeader.getName()));
        assertThat(header.getValue(), is(httpHeader.getValue()));

    }

    @Test
    public void testConvertHttpHeader_Null() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        // Exercise
        HttpHeader httpHeader = httpResponse.convertHttpHeader(null);

        // Verify
        assertThat(httpHeader, is(nullValue()));
    }

    @Test
    public void testToByteArray() throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        String src = "And so, my fellow Americans: ask not what your country can do for you - ask what you can do for your country.";
        HttpEntity entity = new StringEntity(src);

        // Exercise
        byte[] data = httpResponse.toByteArray(entity);

        // Verify
        assertThat(data, is(notNullValue()));
        assertThat(data, is(equalTo(src.getBytes())));

    }

    @Test
    public void testToByteArray_Null() throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        HttpEntity entity = null;

        // Exercise
        byte[] data = httpResponse.toByteArray(entity);

        // Verify
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testToByteArray_Exception() throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse httpResponse = new ApacheHttpResponse(response);

        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 0);

        when(input.read((byte[]) any())).thenThrow(new IOException());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Exercise
        httpResponse.toByteArray(entity);

        // Verify
        fail();
    }

}
