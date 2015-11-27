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
package jp.ambrosoli.quickrestclient.request;

import static jp.ambrosoli.quickrestclient.HttpConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.ambrosoli.quickrestclient.Http;
import jp.ambrosoli.quickrestclient.Operations;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.response.HttpResponse;
import jp.ambrosoli.quickrestclient.unittest.DataSource;

public class HttpRequestBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void コンストラクタにnullを渡すと_IllegalArgumentExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("URL may not be null or blank."))); //$NON-NLS-1$

        // Exercise
        new HttpRequestBuilder(null);

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void charsetを呼び出すと_引数に渡したcharsetがHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.charset("Shift_JIS"); //$NON-NLS-1$

        // Verify
        String charset = sut.request.getCharset();
        assertThat(charset, is(equalTo("Shift_JIS"))); //$NON-NLS-1$

    }

    @Test
    public void charsetの引数にnullを渡すと_nullがHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.charset(null);

        // Verify
        String charset = sut.request.getCharset();
        assertThat(charset, is(nullValue()));
    }

    @Test
    public void headersの引数にNameValueObject配列を渡すと_HttpRequestにListとして保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        NameValueObject[] params = new NameValueObject[3];
        params[0] = new NameValueObject("Header1", "Value1"); //$NON-NLS-1$ //$NON-NLS-2$
        params[1] = new NameValueObject("Header2", "Value2"); //$NON-NLS-1$ //$NON-NLS-2$
        params[2] = new NameValueObject("Header3", "Value3"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        sut.headers(params);

        // Verify
        List<NameValueObject> headers = sut.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(3));

        for (int i = 0; i < params.length; i++) {
            assertThat(headers.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(headers.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void headersの引数にNameValueObject配列型でnullを渡すと_HttpRequestに空のListが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        NameValueObject[] params = null;

        // Exercise
        sut.headers(params);

        // Verify
        HttpHeaders headers = sut.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));

    }

    @Test
    public void headersの引数にMapを渡すと_HttpRequestにNameValueObjectのListとして保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        Map<String, String> params = new HashMap<String, String>();
        params.put("ibaraki", "mito"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("tochigi", "utsunomiya"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("gunma", "maebashi"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("saitama", "saitama"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("chiba", "chiba"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("tokyo", "shinjyuku"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("kanagawa", "yokohama"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        sut.headers(params);

        // Verify
        List<NameValueObject> headers = sut.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(7));

        Iterator<Entry<String, String>> itr = params.entrySet().iterator();
        for (int i = 0; itr.hasNext(); i++) {
            Entry<String, String> entry = itr.next();
            assertThat(headers.get(i).getName(), is(equalTo(entry.getKey())));
            assertThat(headers.get(i).getValue(), is(equalTo(entry.getValue())));
        }

    }

    @Test
    public void headersの引数にMap型でnullを渡すと_HttpRequestに空のListが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        Map<String, String> params = null;

        // Exercise
        sut.headers(params);

        // Verify
        HttpHeaders headers = sut.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void methodの引数にHTTPメソッドを文字列で渡すと_対応するHttpMethodが返されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.method(Http.GET);
        HttpMethod method_get = sut.request.getMethod();

        sut.method(Http.POST);
        HttpMethod method_post = sut.request.getMethod();

        sut.method(Http.PUT);
        HttpMethod method_put = sut.request.getMethod();

        sut.method(Http.DELETE);
        HttpMethod method_delete = sut.request.getMethod();

        sut.method(Http.OPTIONS);
        HttpMethod method_options = sut.request.getMethod();

        sut.method(Http.HEAD);
        HttpMethod method_head = sut.request.getMethod();

        // Verify
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void methodの引数にnullを渡すと_NullPointerExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(NullPointerException.class)));
        this.expectedException.expectMessage(is(equalTo("method may not to be null."))); //$NON-NLS-1$

        // Exercise
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        sut.method(null);

        // Verify
        fail("例外が発生しませんでした"); //$NON-NLS-1$
    }

    @Test
    public void methodの引数にTRACEを渡すと_IllegalArgumentExceptionが発生すること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));

        // Exercise
        sut.method("TRACE"); //$NON-NLS-1$

        // Verify
        fail("例外が発生しませんでした"); //$NON-NLS-1$
    }

    @Test
    public void methodの引数にHTTPメソッドを小文字で渡すと_対応するHttpMethodが返されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.method("get"); //$NON-NLS-1$
        HttpMethod method_get = sut.request.getMethod();

        sut.method("post"); //$NON-NLS-1$
        HttpMethod method_post = sut.request.getMethod();

        sut.method("put"); //$NON-NLS-1$
        HttpMethod method_put = sut.request.getMethod();

        sut.method("delete"); //$NON-NLS-1$
        HttpMethod method_delete = sut.request.getMethod();

        sut.method("options"); //$NON-NLS-1$
        HttpMethod method_options = sut.request.getMethod();

        sut.method("head"); //$NON-NLS-1$
        HttpMethod method_head = sut.request.getMethod();

        // Verify
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void paramsの引数にNameValueObjectの配列を渡すと_HttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        NameValueObject[] params = new NameValueObject[3];
        params[0] = new NameValueObject("lettuce", "レタス"); //$NON-NLS-1$ //$NON-NLS-2$
        params[1] = new NameValueObject("celery", "セロリ"); //$NON-NLS-1$ //$NON-NLS-2$
        params[2] = new NameValueObject("bracken", "蕨"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        sut.params(params);

        // Verify
        List<NameValueObject> paramList = sut.request.getParams().getParams();
        assertThat(paramList.size(), is(params.length));
        for (int i = 0; i < params.length; i++) {
            assertThat(paramList.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(paramList.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void paramsの引数にNameValueObject配列型のnullを渡すと_HttpRequestに空のListが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        NameValueObject[] params = null;

        // Exercise
        sut.params(params);

        // Verify
        List<NameValueObject> paramList = sut.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void paramsの引数にMapを渡すと_HttpRequestにNameValueObjectのListとして保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        Map<String, String> params = new HashMap<String, String>();
        params.put("NISSAN", "Japan"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("Rolls Royce", "Britain"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("Chrysler", "U.S.A"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("Porsche", "Germany"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        sut.params(params);

        List<NameValueObject> paramList = sut.request.getParams().getParams();
        assertThat(paramList.size(), is(params.size()));

        Iterator<Entry<String, String>> itr = params.entrySet().iterator();
        for (int i = 0; itr.hasNext(); i++) {
            Entry<String, String> entry = itr.next();
            assertThat(paramList.get(i).getName(), is(equalTo(entry.getKey())));
            assertThat(paramList.get(i).getValue(), is(equalTo(entry.getValue())));
        }

    }

    @Test
    public void paramsの引数にMap型のnullを渡すと_HttpRequestに空のListが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        Map<String, String> param = null;

        // Exercise
        sut.params(param);

        // Verify
        List<NameValueObject> paramList = sut.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void protocolを呼び出すと_引数で渡した値がHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.protocol(Http.HTTP_1_0);

        // Verify
        assertThat(sut.request.getProtocol(), is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void protocolの引数にnullを渡すと_nullがHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.protocol(null);

        // Verify
        assertThat(sut.request.getProtocol(), is(nullValue()));
    }

    @Test
    public void proxyを呼び出すと_HttpRequestにProxyInfoとして保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.proxy("localhost", 8080); //$NON-NLS-1$

        // Verify
        ProxyInfo proxyInfo = sut.request.getProxyInfo();
        assertThat(proxyInfo.getHost(), is(equalTo("localhost"))); //$NON-NLS-1$
        assertThat(proxyInfo.getPort(), is(equalTo(8080)));
    }

    @Test
    public void proxyの第一引数にnullを渡すと_IllegalArgumentExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("hostname may not be null."))); //$NON-NLS-1$

        // Exercise
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        sut.proxy(null, 8080);

        // Verify
        fail("例外が発生しませんでした"); //$NON-NLS-1$

    }

    @Test
    public void timeoutメソッドを呼び出すと_引数で指定した値がHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.timeout(15000);

        // Verify
        assertThat(sut.request.getTimeout(), is(15000));

    }

    @Test
    public void timeoutの引数に0を渡すと_HttpReqeustに0が保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.timeout(0);

        // Verify
        assertThat(sut.request.getTimeout(), is(0));

    }

    @Test
    public void timeoutの引数に負数を渡すと_引数で渡した値がHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.timeout(-15000);

        // Verify
        assertThat(sut.request.getTimeout(), is(-15000));

    }

    @Test
    public void userAgentを呼び出すと_引数に指定した値がHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.userAgent("hoge/1.0"); //$NON-NLS-1$

        // Verify
        List<NameValueObject> headers = sut.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(1));
        assertThat(headers.get(0).getName(), is(equalTo("User-Agent"))); //$NON-NLS-1$
        assertThat(headers.get(0).getValue(), is(equalTo("hoge/1.0"))); //$NON-NLS-1$
    }

    @Test
    public void executeを呼び出すと_コンストラクタで指定したURLの通信結果がHttpResponseとして返されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder(DataSource.url("statusCode/ok")); //$NON-NLS-1$

        // Exercise
        HttpResponse actual = sut.execute();

        // Verify
        assertThat(actual.isSuccess(), is(true));
    }

    @Test
    public void userAgentを呼び出すと_引数で指定した値がHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.userAgent("Mozilla/10.0"); //$NON-NLS-1$

        // Verify
        NameValueObject userAgent = sut.request.getHeaders().getHeaders().get(0);
        assertThat(userAgent.getName(), is("User-Agent")); //$NON-NLS-1$
        assertThat(userAgent.getValue(), is("Mozilla/10.0")); //$NON-NLS-1$

    }

    @Test
    public void userAgentの引数にnullを渡すと_HttpRequestにnullが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.userAgent(null);

        // Verify
        HttpHeaders headers = sut.request.getHeaders();
        assertThat(headers, is(nullValue()));

    }

    @Test
    public void acceptを呼び出すと_HttpRequesで保持しているHTTPヘッダーに引数で渡した値のAcceptヘッダーが追加されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.accept(HTML, XHTML, XML, JSON, TEXT);

        // Verify
        List<NameValueObject> headers = sut.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(equalTo(5)));

        for (NameValueObject header : headers) {
            assertThat(header.getName(), is(equalTo("Accept"))); //$NON-NLS-1$
            assertThat(header.getValue(),
                    anyOf(equalTo(HTML), equalTo(XHTML), equalTo(XML), equalTo(JSON), equalTo(TEXT)));
        }

    }

    @Test
    public void acceptの引数にnullを渡した場合_HttpRequestのHTTPヘッダーにnullのAcceptヘッダーが追加されないこと() throws Exception {

        // Exercise
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        sut.accept(null, HTML, XHTML, XML, null, JSON, TEXT, null);

        // Verify
        List<NameValueObject> headers = sut.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(equalTo(5)));

        for (NameValueObject header : headers) {
            assertThat(header.getName(), is(equalTo("Accept"))); //$NON-NLS-1$
            assertThat(header.getValue(),
                    anyOf(equalTo(HTML), equalTo(XHTML), equalTo(XML), equalTo(JSON), equalTo(TEXT)));
        }

    }

    @Test
    public void authの引数にAuthInfoを渡した場合_HttpRequestにAuthInfoが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        AuthInfo auth = Operations.digest("aaa", "bbb"); //$NON-NLS-1$ //$NON-NLS-2$
        sut.auth(auth);

        // Verify
        AuthInfo authInfo = sut.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.DIGEST));
        assertThat(authInfo.username, is(equalTo("aaa"))); //$NON-NLS-1$
        assertThat(authInfo.password, is(equalTo("bbb"))); //$NON-NLS-1$
    }

    @Test
    public void authの引数に文字列で認証情報を指定した場合_AuthInfoが生成されてHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.auth("BASIC", "pppp", "hhhh"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Verify
        AuthInfo authInfo = sut.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.BASIC));
        assertThat(authInfo.username, is(equalTo("pppp"))); //$NON-NLS-1$
        assertThat(authInfo.password, is(equalTo("hhhh"))); //$NON-NLS-1$
    }

    @Test
    public void authの引数にEnumで認証メソッドを指定した場合_AuthInfoが生成されてHttpRequestに保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.auth(AuthType.CLIENT_CERT, "abc", "xyz"); //$NON-NLS-1$ //$NON-NLS-2$

        // Verify
        AuthInfo authInfo = sut.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.CLIENT_CERT));
        assertThat(authInfo.username, is(equalTo("abc"))); //$NON-NLS-1$
        assertThat(authInfo.password, is(equalTo("xyz"))); //$NON-NLS-1$
    }

    @Test
    public void authの引数にnullを渡した場合_HttpRequestにnullが保持されること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        sut.auth(null);

        // Verify
        AuthInfo authInfo = sut.request.getAuthInfo();
        assertThat(authInfo, is(nullValue()));
    }

    @Test
    public void authの引数に存在しない認証メソッドを指定した場合_IllegalArgumentExceptionが発生すること() {

        // Setup
        HttpRequestBuilder sut = new HttpRequestBuilder("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));

        // Exercise
        sut.auth("hoge", "user", "password"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

}
