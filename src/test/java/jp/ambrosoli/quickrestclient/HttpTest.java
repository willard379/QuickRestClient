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
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.ambrosoli.quickrestclient.exception.SocketTimeoutRuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.response.HttpResponse;
import jp.ambrosoli.quickrestclient.unittest.DataSource;

public class HttpTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void URLのみ指定して正しく通信が行われること() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url()).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));

    }

    @Test
    public void HTTPメソッドを指定しなかった場合_GETリクエストが送信されること() {

        // Exercise

        // このURLにGETメソッドでリクエストした場合、ステータスコード200が返却される。
        // GETメソッド以外でリクエストした場合、ステータスコード405が返却される。
        HttpResponse sut = Http.url(DataSource.url("method/get")).execute();

        // Verify
        assertThat(sut.isSuccess(), is(true));
    }

    @Test
    public void GETメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/get")).method(Http.GET).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void POSTメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/post")).method(Http.POST).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void PUTメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/put")).method(Http.PUT).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void DELETEメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/delete")).method(Http.DELETE).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void HEADメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/head")).method(Http.HEAD).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void OPTIONSメソッドを送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/options")).method(Http.OPTIONS).execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void GETメソッドを文字列で指定して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/get")).method("GET").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void POSTメソッドを文字列で指定して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("method/post")).method("post").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void HTTPプロトコルバージョンに1てん0を指定する() {

        // Setup
        // HTTP/1.0のリクエストに対して200 OKを返すURL
        String url = DataSource.url("httpProtocol/version1_0");

        // Exercise
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();

        // Verify
        assertThat(response_1_0.isSuccess(), is(true));
        assertThat(response_1_1.isSuccess(), is(false));

    }

    @Test
    public void HTTPプロトコルバージョンに1てん1を指定する() {

        // Setup
        // HTTP/1.1のリクエストに対して200 OKを返すURL
        String url = DataSource.url("httpProtocol/version1_1");

        // Exercise
        HttpResponse response_1_1 = Http.url(url).protocol(Http.HTTP_1_1).execute();
        HttpResponse response_1_0 = Http.url(url).protocol(Http.HTTP_1_0).execute();

        // Verify
        assertThat(response_1_1.isSuccess(), is(true));
        assertThat(response_1_0.isSuccess(), is(false));
    }

    @Test
    public void UserAgentを設定して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/userAgent")).userAgent("Ambrosoli/X.X").execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("Ambrosoli/X.X")));
    }

    @Test
    public void GETリクエストにリクエストパラメータを付与して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("helloWorld")).method(Http.GET)
                .params(add("name", "willard379")).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void POSTリクエストにリクエストパラメータを付与して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("helloWorld")).method(Http.POST)
                .params(add("name", "willard379")).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void リクエストパラメータをMapで設定する() {

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "willard379");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("helloWorld")).method(Http.POST).params(params).execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, willard379!")));
    }

    @Test
    public void charsetを指定して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("helloWorld")).method(Http.GET).params(add("name", "まぐ☆なむ"))
                .charset("UTF-8").execute();

        // Verify
        assertThat(response.getAsString(), is(equalTo("Hello, まぐ☆なむ!")));
    }

    @Test
    public void HTTPヘッダーを付与する() {

        // Exercise
        HttpResponse res = Http.url(DataSource.url("header/sameAsParams")).method(Http.POST)
                .headers(add("Accept", "application/xml"), add("Connection", "Keep-Alive"),
                        add("Accept-Language", "ja"), add("Pragma", "no-cache"))
                .params(add("Accept", "application/xml"), add("Connection", "Keep-Alive"), add("Accept-Language", "ja"),
                        add("Pragma", "no-cache"))
                .charset("UTF-8").execute();

        // Verify
        // HTTPヘッダーとリクエストパラメータの内容が同じ場合にのみステータスコード200が返される。
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void HTTPヘッダーをMapで設定する() {

        // Setup
        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept", "application/xml");
        map.put("Connection", "Keep-Alive");
        map.put("Accept-Language", "ja");
        map.put("Pragma", "no-cache");

        // Exercise
        HttpResponse res = Http.url(DataSource.url("header/sameAsParams")).method(Http.POST).headers(map).params(map)
                .charset("UTF-8").execute();

        // Verify
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void ContentTypeを設定する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders"))
                .params(add("Content-Type", "text/html;charset=Shift_JIS")).execute();

        // Verify
        assertThat(response.getContentType(), is(equalTo("text/html;charset=Shift_JIS")));
    }

    @Test
    public void HTTPレスポンスからすべてのHTTPヘッダーを取得する() {

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
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
    public void getHeadersの引数にnullを指定してHTTPレスポンスヘッダーを取得した場合_空のリストが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeaders()メソッドは、引数に該当するすべてのHTTPヘッダーをListで返却する。
        // 該当するHTTPヘッダーのうち最初の１件のみ取得したい場合は、getHeader()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        List<HttpHeader> headers = response.getHeaders(null);

        // Verify
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void getHeadersの引数に指定したヘッダー名がHTTPレスポンスヘッダーに含まれない場合_空のリストが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeaders()メソッドは、引数に該当するすべてのHTTPヘッダーをListで返却する。
        // 該当するHTTPヘッダーのうち最初の１件のみ取得したい場合は、getHeader()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        List<HttpHeader> headers = response.getHeaders("Trailer");

        // Verify
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void getHeadersの引数に指定したヘッダー名がHTTPレスポンスヘッダーに含まれる場合_該当するHTTPレスポンスヘッダーが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeaders()メソッドは、引数に該当するすべてのHTTPヘッダーをListで返却する。
        // 該当するHTTPヘッダーのうち最初の１件のみ取得したい場合は、getHeader()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        List<HttpHeader> headers = response.getHeaders("Content-Language");

        // Verify
        assertThat(headers.get(0).getValue(), is(equalTo("ja")));
    }

    @Test
    public void getHeaderの引数にnullを指定してHTTPレスポンスヘッダーを取得した場合_nullが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeader()メソッドは、引数に該当するHTTPヘッダーをのうち最初の１件のみ返却する。
        // 該当するすべてのHTTPヘッダーを取得したい場合、getHeaders()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        HttpHeader header = response.getHeader(null);

        // Verify
        assertThat(header, is(nullValue()));
    }

    @Test
    public void getHeaderの引数に指定したヘッダー名がHTTPレスポンスヘッダーに含まれない場合_空のリストが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeader()メソッドは、引数に該当するHTTPヘッダーをのうち最初の１件のみ返却する。
        // 該当するすべてのHTTPヘッダーを取得したい場合、getHeaders()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        HttpHeader header = response.getHeader("Transfer-Encoding");

        // Verify
        assertThat(header, is(nullValue()));
    }

    @Test
    public void getHeaderの引数に指定したヘッダー名がHTTPレスポンスヘッダーに含まれる場合_該当するHTTPレスポンスヘッダーが返されること() {

        // HTTPヘッダーはヘッダー名の重複を許容する。
        // getHeader()メソッドは、引数に該当するHTTPヘッダーをのうち最初の１件のみ返却する。
        // 該当するすべてのHTTPヘッダーを取得したい場合、getHeaders()メソッドを使用する。

        // Setup
        Map<String, String> params = new HashMap<String, String>();
        params.put("Allow", "GET, POST, PUT, DELETE");
        params.put("Content-Language", "ja");
        params.put("Connection", "close");

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/setHeaders")).params(params).execute();
        HttpHeader header = response.getHeader("Allow");

        // Verify
        assertThat(header.getValue(), is(equalTo("GET, POST, PUT, DELETE")));
    }

    @Test
    public void timeoutメソッドで指定したミリ秒以内にHTTPレスポンスが返却された場合_Exceptionが発生しないこと() {

        // Exercise
        HttpResponse res = Http.url(DataSource.url("timeout/100")).timeout(1000).execute();

        // Verify
        assertThat(res.isSuccess(), is(true));
    }

    @Test
    public void timeoutメソッドで指定したミリ秒以内にHTTPレスポンスが返却されない場合_SocketTimeoutRuntimeExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(SocketTimeoutRuntimeException.class)));

        // Exercise
        Http.url(DataSource.url("timeout/500")).timeout(500).execute();

        // Verify
        fail("タイムアウトしませんでした");
    }

    @Test
    public void Acceptを設定して送信する() {

        // Exercise
        HttpResponse response = Http.url(DataSource.url("header/accept")).accept(HTML, XHTML, JSON, XML, TEXT)
                .execute();

        // Verify
        assertThat(response.isSuccess(), is(true));

        // Acceptヘッダーに設定した値がレスポンスボディにCSV形式で設定される
        String responseBody = response.getAsString();
        List<String> accepts = Arrays.asList(responseBody.split(", *"));
        assertThat(accepts, hasItems(HTML, XHTML, JSON, XML, TEXT));
    }

    @Ignore("サーバ側の準備ができていないため保留")
    @Test
    public void Basic認証を使用する() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/auth/basic").auth(basic("username", "password"))
                .execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

    @Ignore("サーバ側の準備ができていないため保留")
    @Test
    public void Digest認証を使用する() {

        // Exercise
        HttpResponse response = Http.url("http://www.ambrosoli.jp/auth/digest").auth(digest("username", "password"))
                .execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(notNullValue()));
    }

}
