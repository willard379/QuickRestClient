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
package jp.ambrosoli.quickrestclient.response;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.util.StringUtil;

import org.junit.Test;

public class AbstractHttpResponseTest {

    @Test
    public void testIsSuccess_200() {

        // Setup
        MockAbstractHttpResponseImpl response = new MockAbstractHttpResponseImpl();
        response.sc = 200;

        // Exercise
        boolean ok = response.isSuccess();

        // Verify
        assertThat(ok, is(true));

    }

    @Test
    public void testIsSuccess_201() {

        // Setup
        MockAbstractHttpResponseImpl response = new MockAbstractHttpResponseImpl();
        response.sc = 201;

        // Exercise
        boolean ok = response.isSuccess();

        // Verify
        assertThat(ok, is(true));

    }

    @Test
    public void testIsSuccess_500() {

        // Setup
        MockAbstractHttpResponseImpl response = new MockAbstractHttpResponseImpl();
        response.sc = 500;

        // Exercise
        boolean ok = response.isSuccess();

        // Verify
        assertThat(ok, is(false));

    }

    @Test
    public void testIsSuccess_0() {

        // Setup
        MockAbstractHttpResponseImpl response = new MockAbstractHttpResponseImpl();
        response.sc = 0;

        // Exercise
        boolean ok = response.isSuccess();

        // Verify
        assertThat(ok, is(false));

    }

    @Test
    public void testGetAsByteArray_Null() {

        // Setup
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(null);

        // Exercise
        byte[] data = response.getAsByteArray();

        // Verify
        assertThat(data, is(nullValue()));

    }

    @Test
    public void testGetAsByteArray() {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("I dislike Tsukemen!".getBytes());
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        byte[] data = response.getAsByteArray();

        // Verify
        assertThat(data, is(sameInstance(data)));

    }

    @Test
    public void testGetAsInputStream_Null() throws IOException {

        // Setup
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(null);

        // Exercise
        InputStream inputStream = response.getAsInputStream();

        // Verify
        assertThat(inputStream, is(nullValue()));

    }

    @Test
    public void testGetAsInputStream() throws IOException {

        // Setup
        ResponseContent content = new ByteArrayResponseContent(
                "I have respect for Robinmask.".getBytes());
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        InputStream input = response.getAsInputStream();

        // Verify
        assertThat(input, is(notNullValue()));
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            assertThat(br.readLine(), is(equalTo("I have respect for Robinmask.")));
        } finally {
            input.close();
        }
    }

    @Test
    public void testGetAsString_NoContent() {

        // Setup
        ResponseContent content = null;
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String data = response.getAsString();

        // Verify
        assertThat(data, is(nullValue()));

    }

    @Test
    public void testGetAsString() {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("Test".getBytes());
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String data = response.getAsString();

        // Verify
        assertThat(data, is(equalTo("Test")));
    }

    @Test
    public void testGetAsStringString() throws IOException {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("えむえすきゅーさんに".getBytes("MS932"));
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String data = response.getAsString("MS932");

        // Verify
        assertThat(data, is(equalTo("えむえすきゅーさんに")));
    }

    @Test
    public void testGetAsStringString_NoContent() throws IOException {

        // Setup
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(null);

        // Exercise
        String data = response.getAsString("MS932");

        // Verify
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testGetAsStringString_Null() throws IOException {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("としこし".getBytes("UTF-8"));
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String data = response.getAsString(null);

        // Verify
        assertThat(data, is(equalTo("としこし")));
    }

    @Test
    public void testWriteTo() throws Exception {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("しめじ".getBytes("UTF-8"));
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(content);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Exercise
        response.writeTo(output);

        // Verify
        String result = StringUtil.toString(output.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("しめじ")));
    }

    @Test
    public void testWriteTo_Null() throws Exception {

        // Setup
        AbstractHttpResponse response = new MockAbstractHttpResponseImpl(null);
        OutputStream output = mock(OutputStream.class);

        // Exercise
        response.writeTo(output);

        // Verify
        verify(output).close();
    }

}

class MockAbstractHttpResponseImpl extends AbstractHttpResponse {

    int sc;

    public MockAbstractHttpResponseImpl() {
        super();
    }

    public MockAbstractHttpResponseImpl(final ResponseContent content) {
        super(content);
    }

    public List<HttpHeader> getAllHeaders() {
        return null;
    }

    public long getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public HttpHeader getHeader(final String headerName) {
        return null;
    }

    public List<HttpHeader> getHeaders(final String headerName) {
        return null;
    }

    public int getStatusCode() {
        return this.sc;
    }

}