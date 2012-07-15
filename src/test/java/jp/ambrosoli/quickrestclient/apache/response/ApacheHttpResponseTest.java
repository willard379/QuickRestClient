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

import static org.hamcrest.Matchers.*;
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
    public void ApacheHttpResponseのコンストラクタにorg_apache_http_HttpResponseを渡すと_インスタンスが生成されること() {

        // Setup
        HttpResponse response = mock(HttpResponse.class);

        // Exercise
        ApacheHttpResponse actual = new ApacheHttpResponse(response);

        // Verify
        assertThat(actual, is(notNullValue()));

    }

    @Test
    public void ApacheHttpResponseのコンストラクタにnullを渡すと_NullPointerExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(NullPointerException.class)));

        // Exercise
        new ApacheHttpResponse(null);

        // Verify
        fail("NullPointerExceptionが発生しませんでした。");
    }

    @Test
    public void getAllHeadersを呼び出すと_すべてのHTTPヘッダーが返されること() {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getAllHeaders()).thenReturn(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        List<HttpHeader> actual = sut.getAllHeaders();

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(5));

    }

    @Test
    public void getAsByteArrayを呼び出すと_レスポンスボディがbyte配列で返されること() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("Stay here, I'll be back"));

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        byte[] actual = sut.getAsByteArray();

        // Verify
        assertThat(actual, is(equalTo("Stay here, I'll be back".getBytes())));
    }

    @Test
    public void getAsInputStreamを呼び出すと_レスポンスボディがInputStreamで返されること() throws IOException {

        InputStream actual = null;
        try {

            // Setup
            HttpResponse response = mock(HttpResponse.class);
            when(response.getEntity()).thenReturn(new StringEntity("I am a pen!"));

            // Exercise
            ApacheHttpResponse sut = new ApacheHttpResponse(response);
            actual = sut.getAsInputStream();

            // Verify
            assertThat(actual, is(notNullValue()));
            String actualStr = new BufferedReader(new InputStreamReader(actual)).readLine();
            assertThat(actualStr, is(equalTo("I am a pen!")));

        } finally {
            if (actual != null) {
                actual.close();
            }
        }
    }

    @Test
    public void getAsStringを呼び出すと_レスポンスボディがStringで返されること() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        String actual = sut.getAsString();

        // Verify
        assertThat(actual, is(equalTo("There is an apple.")));

    }

    @Test
    public void getAsStringをエンコーディングを指定して呼び出すと_レスポンスボディが指定したエンコードのStringで返されること() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity("There is an apple."));

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        String actual = sut.getAsString(StringUtil.DEFAULT_ENCODING);

        // Verify
        assertThat(actual, is(equalTo("There is an apple.")));

    }

    @Test
    public void getContentTypeを呼び出すと_HTTPレスポンスヘッダーのContentTypeの値が返されること() throws Exception {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        StringEntity entity = new StringEntity("I am a pen!");
        entity.setContentType("application/json");
        when(response.getEntity()).thenReturn(entity);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        String actual = sut.getContentType();

        // Verify
        assertThat(actual, is(equalTo("application/json")));
    }

    @Test
    public void HTTPレスポンスのEntityがnullの状態でgetContentTypeを呼び出すと_nullが返されること() {
        // Setup
        HttpResponse response = mock(HttpResponse.class);
        when(response.getEntity()).thenReturn(null);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        String actual = sut.getContentType();

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void HTTPレスポンスヘッダーにContentTypeがない状態でgetContentTypeを呼び出すと_nullが返されること() throws Exception {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setEntity(new ByteArrayEntity(new byte[0]));

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        String actual = sut.getContentType();

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getContentLengthを呼び出すと_HTTPレスポンスボディのバイト数が返されること() {

        // Setup
        HttpResponse response = mock(HttpResponse.class);
        byte[] data = new byte[100];
        when(response.getEntity()).thenReturn(new ByteArrayEntity(data));

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        long actual = sut.getContentLength();

        // Verify
        assertThat(actual, is(equalTo((long) data.length)));
    }

    @Test
    public void HTTPレスポンスのEntityがnullの状態でgetContentLengthを呼び出すと_0が返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        long actual = sut.getContentLength();

        // Verify
        assertThat(actual, is(0L));
    }

    @Test
    public void getHeaderを呼び出すと_該当するHTTPレスポンスヘッダーの最初の1件が返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        HttpHeader actual = sut.getHeader("Location");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getName(), is(equalTo("Location")));
        assertThat(actual.getValue(), is(equalTo("http://www.ambrosoli.jp/test-server/")));
    }

    @Test
    public void getHeaderの引数にHTTPレスポンスヘッダーに含まれないヘッダー名を指定した場合_nullが返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        HttpHeader actual = sut.getHeader("hoge");

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getHeadersを呼び出すと_該当するHTTPレスポンスヘッダーがすべて返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        List<HttpHeader> actual = sut.getHeaders("Set-Cookie");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(2));
        assertThat(actual.get(0).getName(), is(equalTo("Set-Cookie")));
        assertThat(actual.get(0).getValue(), is(equalTo("name=willard379")));
        assertThat(actual.get(1).getName(), is(equalTo("Set-Cookie")));
        assertThat(actual.get(1).getValue(), is(equalTo("age=17")));
    }

    @Test
    public void getHeadersの引数にHTTPレスポンスヘッダーに含まれないヘッダー名を指定した場合_空のListが返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setHeaders(this.SAMPLE_HEADERS);

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        List<HttpHeader> actual = sut.getHeaders("Fizz");

        // Verify
        assertThat(actual.isEmpty(), is(true));
    }

    @Test
    public void getStatusCodeを呼び出すと_HTTPステータスコードが返されること() {

        // Setup
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        // Exercise
        ApacheHttpResponse sut = new ApacheHttpResponse(response);
        int actual = sut.getStatusCode();

        // Verify
        assertThat(actual, is(200));
    }

    @Test
    public void HTTPステータスコードが200の状態でisSuccessを呼び出すと_trueが返されること() {

        // Setup
        ApacheHttpResponse sut = new ApacheHttpResponse(new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK"));

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HTTPステータスコードが201の状態でisSuccessを呼び出すと_trueが返されること() {

        // Setup
        ApacheHttpResponse sut = new ApacheHttpResponse(new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_CREATED, "OK"));

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HTTPステータスコードが500の状態でisSuccessを呼び出すと_falseが返されること() {

        // Setup
        ApacheHttpResponse sut = new ApacheHttpResponse(new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"));

        // Exercise
        boolean actual = sut.isSuccess();

        // Verify
        assertThat(actual, is(false));

    }

    @Test
    public void convertHttpHeadersを呼び出すと_Header配列からHttpHeaderのListが生成されて返されること() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        Header[] headers = new Header[5];
        headers[0] = new BasicHeader("Transfer-Encoding", "chunked");
        headers[1] = new BasicHeader("Upgrade", "HTTP/2.0, SHTTP/1.3");
        headers[2] = new BasicHeader("Location", "http://www.ambrosoli.jp/test-server/");
        headers[3] = new BasicHeader("Set-Cookie", "name=willard379");
        headers[4] = new BasicHeader("Set-Cookie", "age=17");

        // Exercise
        List<HttpHeader> actual = sut.convertHttpHeaders(headers);

        // Verify
        assertThat(actual, is(notNullValue()));
        for (int i = 0; i < headers.length; i++) {
            assertThat(actual.get(i).getName(), is(equalTo(headers[i].getName())));
            assertThat(actual.get(i).getValue(), is(equalTo(headers[i].getValue())));
        }

    }

    @Test
    public void convertHttpHeadersの引数でnullを渡すと_nullが返されること() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        // Exercise
        List<HttpHeader> actual = sut.convertHttpHeaders(null);

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void convertHttpHeadersの引数に要素数0のHeader配列を渡すと_空のListが返されること() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        // Exercise
        List<HttpHeader> actual = sut.convertHttpHeaders(new Header[0]);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.isEmpty(), is(true));
    }

    @Test
    public void convertHttpHeaderの引数にHeaderを渡すと_HeaderからHttpHeaderが生成されて返されること() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        Header header = new BasicHeader("Transfer-Encoding", "chunked");

        // Exercise
        HttpHeader actual = sut.convertHttpHeader(header);

        // Verify
        assertThat(header, is(notNullValue()));
        assertThat(header.getName(), is(actual.getName()));
        assertThat(header.getValue(), is(actual.getValue()));

    }

    @Test
    public void convertHttpHeaderの引数にnullを渡すと_nullが返されること() {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        // Exercise
        HttpHeader actual = sut.convertHttpHeader(null);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void toByteArrayを呼び出すと_レスポンスボディの内容がbyte配列で返されること() throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        String src = "And so, my fellow Americans: ask not what your country can do for you - ask what you can do for your country.";
        HttpEntity entity = new StringEntity(src);

        // Exercise
        byte[] actual = sut.toByteArray(entity);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(equalTo(src.getBytes())));

    }

    @Test
    public void toByteArrayの引数にnullを渡すと_nullが返されること() throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        HttpEntity entity = null;

        // Exercise
        byte[] actual = sut.toByteArray(entity);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void toByteArrayを呼び出した際にInputStreamからIOExceptionが発生した場合_IORuntimeExceptionにラップされてスローされること()
            throws IOException {

        // Setup
        HttpResponse response = mock(org.apache.http.HttpResponse.class);
        ApacheHttpResponse sut = new ApacheHttpResponse(response);

        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 0);

        when(input.read((byte[]) any())).thenThrow(new IOException());

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        sut.toByteArray(entity);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }
}
