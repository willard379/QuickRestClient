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
package jp.ambrosoli.quickrestclient;

import static jp.ambrosoli.quickrestclient.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ambrosoli.quickrestclient.Http;
import jp.ambrosoli.quickrestclient.exception.SocketTimeoutRuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.junit.Ignore;
import org.junit.Test;

public class HttpTest {

    @Test
    public void testUrl() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/").execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testGetRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/get")
                .method(Http.GET).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPostRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/post")
                .method(Http.POST).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPutRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/put")
                .method(Http.PUT).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testDeleteRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/delete")
                .method(Http.DELETE).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testHeadRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/head")
                .method(Http.HEAD).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testOptionsRequest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/options")
                .method(Http.OPTIONS).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testGetRequestWithString() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/get")
                .method("GET").execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPostRequestWithStringLowerCase() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/method/post")
                .method("post").execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testProtocol_1_0() {

        // Arrange
        // HTTP/1.0のリクエストに対して200 OKを返すURL
        String url = "http://ambrosoli.jp/test-server/httpProtocol/version1_0";

        // Act
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();

        // Assert
        assertThat(response_1_0.isSuccess(), is(true));
        assertThat(response_1_1.isSuccess(), is(false));

    }

    @Test
    public void testProtocol_1_1() {

        // Arrange
        // HTTP/1.1のリクエストに対して200 OKを返すURL
        String url = "http://ambrosoli.jp/test-server/httpProtocol/version1_1";

        // Act
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();

        // Assert
        assertThat(response_1_1.isSuccess(), is(true));
        assertThat(response_1_0.isSuccess(), is(false));
    }

    @Test
    public void testUserAgent() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/userAgent")
                .userAgent("Ambrosoli/X.X").execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("Ambrosoli/X.X")));
    }

    @Test
    public void testGetWithParameter() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/helloWorld")
                .method(Http.GET).params(add("name", "willard379")).execute();

        // Assert
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testPostWithParameter() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/helloWorld")
                .method(Http.POST).params(add("name", "willard379")).execute();

        // Assert
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testPostWithParameter_Map() {

        // Arrange
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "willard379");

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/helloWorld")
                .method(Http.POST).params(params).execute();

        // Assert
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testCharset() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/helloWorld")
                .method(Http.GET).params(add("name", "まぐ☆なむ")).charset("UTF-8").execute();

        // Assert
        assertThat(response.getAsString(), is(equalTo("Hello, まぐ☆なむ!")));
    }

    @Test
    public void testHeaders() {

        // Act
        HttpResponse res = Http
                .url("http://ambrosoli.jp/test-server/header/sameAsParams")
                .method(Http.POST)
                .headers(add("Accept", "application/xml"), add("Connection", "Keep-Alive"),
                        add("Accept-Language", "ja"), add("Pragma", "no-cache"))
                .params(add("Accept", "application/xml"), add("Connection", "Keep-Alive"),
                        add("Accept-Language", "ja"), add("Pragma", "no-cache")).charset("UTF-8")
                .execute();

        // Assert
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void testHeadersMap() {

        // Arrange
        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept", "application/xml");
        map.put("Connection", "Keep-Alive");
        map.put("Accept-Language", "ja");
        map.put("Pragma", "no-cache");

        // Act
        HttpResponse res = Http.url("http://ambrosoli.jp/test-server/header/sameAsParams")
                .method(Http.POST).headers(map).params(map).charset("UTF-8").execute();

        // Assert
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void testGetContentType() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/setHeaders")
                .params(add("Content-Type", "text/html;charset=Shift_JIS")).execute();

        // Assert
        assertThat(response.getContentType(), is(equalTo("text/html;charset=Shift_JIS")));
    }

    @Test
    public void testGetAllHeaders() {

        // Arrange
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();
        List<HttpHeader> allHeaders = response.getAllHeaders();

        // Assert
        int cnt = 0;
        for (HttpHeader header : allHeaders) {
            String name = header.getName();
            String value = params.get(name);
            if (value != null) {
                assertThat(header.getValue(), is(equalTo(value)));
                cnt++;
            }
        }

        assertThat(cnt, is(params.size()));
    }

    @Test
    public void testGetHeaders() {

        // Arrange
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();

        // Assert
        List<HttpHeader> headers = response.getHeaders(null);
        assertThat(headers.isEmpty(), is(true));

        headers = response.getHeaders("Trailer");
        assertThat(headers.isEmpty(), is(true));

        headers = response.getHeaders("Content-Language");
        assertThat(headers.get(0).getValue(), is(equalTo("ja")));
    }

    @Test
    public void testGetHeader() {

        // Arrange
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();

        // Assert
        HttpHeader header = response.getHeader(null);
        assertThat(header, is(nullValue()));

        header = response.getHeader("Transfer-Encoding");
        assertThat(header, is(nullValue()));

        header = response.getHeader("Allow");
        assertThat(header.getValue(), is(equalTo("GET, POST, PUT, DELETE")));
    }

    @Test
    public void testTimeout_Success() {

        // Act
        HttpResponse res = Http.url("http://ambrosoli.jp/test-server/timeout/100").timeout(1000)
                .execute();

        // Assert
        assertThat(res.isSuccess(), is(true));
    }

    @Test(expected = SocketTimeoutRuntimeException.class)
    public void testTimeout_Failure() {

        // Act
        Http.url("http://ambrosoli.jp/test-server/timeout/500").timeout(500).execute();
        fail("タイムアウトしませんでした");
    }

    @Test
    public void testAccept() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/test-server/header/accept")
                .accept(HTML, XHTML, JSON, XML, TEXT).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));

        // Acceptヘッダーに設定した値がレスポンスボディにCSV形式で設定される
        String responseBody = response.getAsString();
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.contains(","), is(true));

        List<String> accepts = Arrays.asList(responseBody.split(", *"));
        assertThat(accepts, hasItems(HTML, XHTML, JSON, XML, TEXT));
    }

    @Ignore("サーバ側の準備ができていないため保留")
    @Test
    public void testAuth_Basic() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/auth/basic")
                .auth(basic("username", "password")).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

    @Ignore("サーバ側の準備ができていないため保留")
    @Test
    public void testAuth_Digest() {

        // Act
        HttpResponse response = Http.url("http://ambrosoli.jp/auth/digest")
                .auth(digest("username", "password")).execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

}
