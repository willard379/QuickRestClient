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

import static org.hamcrest.Matchers.*;
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
    public void HTTPステータスコードが200の状態でisSuccessを呼び出すと_trueが返されること() {

        // Setup
        MockAbstractHttpResponseImpl sut = new MockAbstractHttpResponseImpl();
        sut.sc = 200;

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HTTPステータスコードが201の状態でisSuccessを呼び出すと_trueが返されること() {

        // Setup
        MockAbstractHttpResponseImpl sut = new MockAbstractHttpResponseImpl();
        sut.sc = 201;

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HTTPステータスコードが500の状態でisSuccessを呼び出すと_falseが返されること() {

        // Setup
        MockAbstractHttpResponseImpl sut = new MockAbstractHttpResponseImpl();
        sut.sc = 500;

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(false));

    }

    @Test
    public void HTTPステータスコードが0の状態でisSuccessを呼び出すと_falseが返されること() {

        // Setup
        MockAbstractHttpResponseImpl sut = new MockAbstractHttpResponseImpl();
        sut.sc = 0;

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(false));

    }

    @Test
    public void HttpResponseのデータがnullの状態でgetAsByteArrayを呼び出すと_nullが返されること() {

        // Setup
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(null);

        // Exercise
        byte[] actual = sut.getAsByteArray();

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpResponseにデータがある状態でgetAsByteArrayを呼び出すと_HttpResponseのデータがbyte配列で返されること() {

        // Setup
        byte[] data = "BUILD SUCCESS".getBytes();
        ResponseContent content = new ByteArrayResponseContent(data);
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        // Exercise
        byte[] actual = sut.getAsByteArray();

        // Verify
        assertThat(actual, is(equalTo(data)));

    }

    @Test
    public void HttpResponseにデータがない状態でgetAsInputStreamを呼び出すと_nullが返されること() throws IOException {

        // Setup
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(null);

        // Exercise
        InputStream actual = sut.getAsInputStream();

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpResponseにデータがある状態でgetAsInputStreamを呼び出すと_HttpResponseのデータがInputStreamで返されること()
            throws IOException {

        // Setup
        String data = "Robinmask";
        ResponseContent content = new ByteArrayResponseContent(data.getBytes());
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        // Exercise
        InputStream actual = sut.getAsInputStream();

        // Verify
        assertThat(actual, is(notNullValue()));
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(actual));
            assertThat(br.readLine(), is(equalTo(data)));
        } finally {
            actual.close();
        }
    }

    @Test
    public void HttpResponseにデータがない状態でgetAsStringを呼び出すと_nullが返されること() {

        // Setup
        ResponseContent content = null;
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String actual = sut.getAsString();

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpResponseにデータがある状態でgetAsStringを呼び出すと_HttpResponseのデータがStringで返されること() {

        // Setup
        String data = "Test";
        ResponseContent content = new ByteArrayResponseContent(data.getBytes());
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String actual = sut.getAsString();

        // Verify
        assertThat(actual, is(equalTo(data)));
    }

    @Test
    public void HttpResponseにデータがある状態でgetAsStringの引数にエンコーディングを指定して呼び出した場合_HttpResponseのデータが指定したエンコーディングの文字列として返されること()
            throws IOException {

        // Setup
        String data = "𩸽";
        ResponseContent content = new ByteArrayResponseContent(data.getBytes("UTF-16"));
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        // Exercise
        String actual = sut.getAsString("UTF-16");

        // Verify
        assertThat(actual, is(equalTo(data)));
    }

    @Test
    public void HttpResponseにデータがない状態でgetAsStringの引数にエンコーディングを指定して呼び出した場合_nullが返されること()
            throws IOException {

        // Setup
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(null);

        // Exercise
        String actual = sut.getAsString("MS932");

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void HttpResponseにデータがある状態でwriteToを呼び出した場合_HttpResponseのデータがOutputStreamに書き出されてcloseされていること()
            throws Exception {

        // Setup
        ResponseContent content = new ByteArrayResponseContent("しめじ".getBytes("UTF-8"));
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(content);

        ByteArrayOutputStream output = spy(new ByteArrayOutputStream());

        // Exercise
        sut.writeTo(output);

        // Verify
        String result = StringUtil.toString(output.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("しめじ")));
        verify(output).close();
    }

    @Test
    public void HttpResponseにデータがない状態でwriteToを呼び出した場合_OutputStreamに書きだされずにcloseが呼ばれていること()
            throws Exception {

        // Setup
        AbstractHttpResponse sut = new MockAbstractHttpResponseImpl(null);
        OutputStream output = mock(OutputStream.class);

        // Exercise
        sut.writeTo(output);

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