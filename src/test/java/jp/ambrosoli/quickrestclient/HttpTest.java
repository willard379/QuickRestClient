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

import static jp.ambrosoli.quickrestclient.HttpConstants.*;
import static jp.ambrosoli.quickrestclient.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ambrosoli.quickrestclient.exception.SocketTimeoutRuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.junit.Ignore;
import org.junit.Test;

public class HttpTest {

    @Test
    public void testUrl() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testGetRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/get")
                .method(Http.GET).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPostRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/post")
                .method(Http.POST).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPutRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/put")
                .method(Http.PUT).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testDeleteRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/delete")
                .method(Http.DELETE).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testHeadRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/head")
                .method(Http.HEAD).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testOptionsRequest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/options")
                .method(Http.OPTIONS).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testGetRequestWithString() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/get")
                .method("GET").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testPostRequestWithStringLowerCase() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/method/post")
                .method("post").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testProtocol_1_0() {

        // Setup
        // HTTP/1.0のリクエストに対して200 OKを返すURL
        String url = "http://www.ambrosoli.jp/test-server/httpProtocol/version1_0";

        // Exercise
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();

        // Verify
        assertThat(response_1_0.isSuccess(), is(true));
        assertThat(response_1_1.isSuccess(), is(false));

    }

    @Test
    public void testProtocol_1_1() {

        // Setup
        // HTTP/1.1のリクエストに対して200 OKを返すURL
        String url = "http://www.ambrosoli.jp/test-server/httpProtocol/version1_1";

        // Exercise
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();

        // Verify
        assertThat(response_1_1.isSuccess(), is(true));
        assertThat(response_1_0.isSuccess(), is(false));
    }

    @Test
    public void testUserAgent() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/userAgent")
                .userAgent("Ambrosoli/X.X").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("Ambrosoli/X.X")));
    }

    @Test
    public void testGetWithParameter() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/helloWorld")
                .method(Http.GET).params(add("name", "willard379")).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testPostWithParameter() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/helloWorld")
                .method(Http.POST).params(add("name", "willard379")).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testPostWithParameter_Map() {

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "willard379");

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/helloWorld")
                .method(Http.POST).params(params).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void testCharset() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/helloWorld")
                .method(Http.GET).params(add("name", "まぐ☆なむ")).charset("UTF-8").execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, まぐ☆なむ!")));
    }

    @Test
    public void testHeaders() {

        // Exercise
        HttpResponse res = Http
                .url("http://www.ambrosoli.jp/test-server/header/sameAsParams")
                .method(Http.POST)
                .headers(add("Accept", "application/xml"), add("Connection", "Keep-Alive"),
                        add("Accept-Language", "ja"), add("Pragma", "no-cache"))
                .params(add("Accept", "application/xml"), add("Connection", "Keep-Alive"),
                        add("Accept-Language", "ja"), add("Pragma", "no-cache")).charset("UTF-8")
                .execute();

        // Verify
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void testHeadersMap() {

        // Setup
        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept", "application/xml");
        map.put("Connection", "Keep-Alive");
        map.put("Accept-Language", "ja");
        map.put("Pragma", "no-cache");

        // Exercise
        HttpResponse res = Http.url("http://www.ambrosoli.jp/test-server/header/sameAsParams")
                .method(Http.POST).headers(map).params(map).charset("UTF-8").execute();

        // Verify
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void testGetContentType() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/setHeaders")
                .params(add("Content-Type", "text/html;charset=Shift_JIS")).execute();

        // Verify
        assertThat(response.getContentType(), is(equalTo("text/html;charset=Shift_JIS")));
    }

    @Test
    public void testGetAllHeaders() {

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();
        List<HttpHeader> allHeaders = response.getAllHeaders();

        // Verify
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

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();

        // Verify
        List<HttpHeader> headers = response.getHeaders(null);
        assertThat(headers.isEmpty(), is(true));

        headers = response.getHeaders("Trailer");
        assertThat(headers.isEmpty(), is(true));

        headers = response.getHeaders("Content-Language");
        assertThat(headers.get(0).getValue(), is(equalTo("ja")));
    }

    @Test
    public void testGetHeader() {

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/setHeaders")
                .params(params).execute();

        // Verify
        HttpHeader header = response.getHeader(null);
        assertThat(header, is(nullValue()));

        header = response.getHeader("Transfer-Encoding");
        assertThat(header, is(nullValue()));

        header = response.getHeader("Allow");
        assertThat(header.getValue(), is(equalTo("GET, POST, PUT, DELETE")));
    }

    @Test
    public void testTimeout_Success() {

        // Exercise
        HttpResponse res = Http.url("http://www.ambrosoli.jp/test-server/timeout/100")
                .timeout(1000).execute();

        // Verify
        assertThat(res.isSuccess(), is(true));
    }

    @Test(expected = SocketTimeoutRuntimeException.class)
    public void testTimeout_Failure() {

        // Exercise
        Http.url("http://www.ambrosoli.jp/test-server/timeout/500").timeout(500).execute();
        fail("タイムアウトしませんでした");
    }

    @Test
    public void testAccept() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/test-server/header/accept")
                .accept(HTML, XHTML, JSON, XML, TEXT).execute();

        // Verify
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

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/auth/basic")
                .auth(basic("username", "password")).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

    @Ignore("サーバ側の準備ができていないため保留")
    @Test
    public void testAuth_Digest() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/auth/digest")
                .auth(digest("username", "password")).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

}
