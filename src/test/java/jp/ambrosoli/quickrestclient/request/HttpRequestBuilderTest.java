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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.ambrosoli.quickrestclient.Http;
import jp.ambrosoli.quickrestclient.Operations;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HttpRequestBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testConstruct_Null() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("URL may not be null or blank.")));

        // Exercise
        new HttpRequestBuilder(null);

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCharset() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.charset("Shift_JIS");

        // Verify
        String charset = builder.request.getCharset();
        assertThat(charset, is(equalTo("Shift_JIS")));

    }

    @Test
    public void testCharset_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.charset(null);

        // Verify
        String charset = builder.request.getCharset();
        assertThat(charset, is(nullValue()));
    }

    @Test
    public void testHeaders_NameValueObjectArray() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        NameValueObject[] params = new NameValueObject[3];
        params[0] = new NameValueObject("Header1", "Value1");
        params[1] = new NameValueObject("Header2", "Value2");
        params[2] = new NameValueObject("Header3", "Value3");

        // Exercise
        builder.headers(params);

        // Verify
        List<NameValueObject> headers = builder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(3));

        for (int i = 0; i < params.length; i++) {
            assertThat(headers.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(headers.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void testHeaders_NameValueObjectArray_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        NameValueObject[] params = null;

        // Exercise
        builder.headers(params);

        // Verify
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));

    }

    @Test
    public void testHeaders_Map() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        Map<String, String> params = new HashMap<String, String>();
        params.put("ibaraki", "mito");
        params.put("tochigi", "utsunomiya");
        params.put("gunma", "maebashi");
        params.put("saitama", "saitama");
        params.put("chiba", "chiba");
        params.put("tokyo", "shinjyuku");
        params.put("kanagawa", "yokohama");

        // Exercise
        builder.headers(params);

        // Verify
        List<NameValueObject> headers = builder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(7));

        Iterator<Entry<String, String>> itr = params.entrySet().iterator();
        for (int i = 0; itr.hasNext(); i++) {
            Entry<String, String> entry = itr.next();
            assertThat(headers.get(i).getName(), is(equalTo(entry.getKey())));
            assertThat(headers.get(i).getValue(), is(equalTo(entry.getValue())));
        }

    }

    @Test
    public void testHeaders_Map_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        Map<String, String> params = null;

        // Exercise
        builder.headers(params);

        // Verify
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void testMethod() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.method(Http.GET);
        HttpMethod method_get = builder.request.getMethod();

        builder.method(Http.POST);
        HttpMethod method_post = builder.request.getMethod();

        builder.method(Http.PUT);
        HttpMethod method_put = builder.request.getMethod();

        builder.method(Http.DELETE);
        HttpMethod method_delete = builder.request.getMethod();

        builder.method(Http.OPTIONS);
        HttpMethod method_options = builder.request.getMethod();

        builder.method(Http.HEAD);
        HttpMethod method_head = builder.request.getMethod();

        // Verify
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void testMethod_Null() {

        // Setup
        this.expectedException.expect(is(instanceOf(NullPointerException.class)));
        this.expectedException.expectMessage(is(equalTo("method may not to be null.")));

        // Exercise
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        builder.method(null);

        // Verify
        fail("例外が発生しませんでした");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethod_Invalid() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.method("TRACE");

        // Verify
        fail("例外が発生しませんでした");
    }

    @Test
    public void testMethod_LowerCase() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.method("get");
        HttpMethod method_get = builder.request.getMethod();

        builder.method("post");
        HttpMethod method_post = builder.request.getMethod();

        builder.method("put");
        HttpMethod method_put = builder.request.getMethod();

        builder.method("delete");
        HttpMethod method_delete = builder.request.getMethod();

        builder.method("options");
        HttpMethod method_options = builder.request.getMethod();

        builder.method("head");
        HttpMethod method_head = builder.request.getMethod();

        // Verify
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void testParams_NameValueObjectArray() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        NameValueObject[] params = new NameValueObject[1];
        params[0] = new NameValueObject("lettuce", "レタス");
        params[0] = new NameValueObject("celery", "セロリ");
        params[0] = new NameValueObject("bracken", "蕨");

        // Exercise
        builder.params(params);

        // Verify
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList.size(), is(params.length));
        for (int i = 0; i < params.length; i++) {
            assertThat(paramList.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(paramList.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void testParams_NameValueObjectArray_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        NameValueObject[] params = null;

        // Exercise
        builder.params(params);

        // Verify
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void testParams_Map() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        Map<String, String> params = new HashMap<String, String>();
        params.put("NISSAN", "Japan");
        params.put("Rolls Royce", "Britain");
        params.put("Chrysler", "U.S.A");
        params.put("Porsche", "Germany");

        // Exercise
        builder.params(params);

        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList.size(), is(params.size()));

        Iterator<Entry<String, String>> itr = params.entrySet().iterator();
        for (int i = 0; itr.hasNext(); i++) {
            Entry<String, String> entry = itr.next();
            assertThat(paramList.get(i).getName(), is(equalTo(entry.getKey())));
            assertThat(paramList.get(i).getValue(), is(equalTo(entry.getValue())));
        }

    }

    @Test
    public void testParams_Map_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.params((Map<String, String>) null);

        // Verify
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void testProtocol() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.protocol(Http.HTTP_1_0);

        // Verify
        assertThat(builder.request.getProtocol(), is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void testProtocol_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.protocol(null);

        // Verify
        assertThat(builder.request.getProtocol(), is(nullValue()));
    }

    @Test
    public void testProxy() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.proxy("localhost", 8080);

        // Verify
        ProxyInfo proxyInfo = builder.request.getProxyInfo();
        assertThat(proxyInfo.getHost(), is(equalTo("localhost")));
        assertThat(proxyInfo.getPort(), is(equalTo(8080)));
    }

    @Test
    public void testProxy_Null() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("hostname may not be null")));

        // Exercise
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        builder.proxy(null, 8080);

        // Verify
        fail("例外が発生しませんでした");

    }

    @Test
    public void testTimeout_PositiveValue() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.timeout(15000);

        // Verify
        assertThat(builder.request.getTimeout(), is(15000));

    }

    @Test
    public void testTimeout_ZeroValue() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.timeout(0);

        // Verify
        assertThat(builder.request.getTimeout(), is(0));

    }

    @Test
    public void testTimeout_NegativeValue() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.timeout(-15000);

        // Verify
        assertThat(builder.request.getTimeout(), is(-15000));

    }

    @Test
    public void testUserAgent() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.userAgent("hoge/1.0");

        // Verify
        List<NameValueObject> headers = builder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(1));
        assertThat(headers.get(0).getName(), is(equalTo("User-Agent")));
        assertThat(headers.get(0).getValue(), is(equalTo("hoge/1.0")));
    }

    @Test
    public void testExecute() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder(
                "http://www.ambrosoli.jp/test-server/statusCode/ok");

        // Exercise
        HttpResponse response = builder.execute();

        // Verify
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testSetUserAgent() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.userAgent("Mozilla/10.0");

        // Verify
        NameValueObject userAgent = builder.request.getHeaders().getHeaders().get(0);
        assertThat(userAgent.getName(), is("User-Agent"));
        assertThat(userAgent.getValue(), is("Mozilla/10.0"));

    }

    @Test
    public void testSetUserAgent_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.userAgent(null);

        // Verify
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers, is(nullValue()));

    }

    @Test
    public void testAccept() {

        // Exercise
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        requestBuilder.accept(HTML, XHTML, XML, JSON, TEXT);

        // Verify
        List<NameValueObject> headers = requestBuilder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(equalTo(5)));

        List<Matcher<? extends String>> values = new ArrayList<Matcher<? extends String>>();
        values.add(equalTo(HTML));
        values.add(equalTo(XHTML));
        values.add(equalTo(XML));
        values.add(equalTo(JSON));
        values.add(equalTo(TEXT));

        for (NameValueObject header : headers) {
            assertThat(header.getName(), is(equalTo("Accept")));
            assertThat(header.getValue(), is(anyOf(values)));
        }

    }

    @Test
    public void testAccept_ContainsNull() throws Exception {

        // Exercise
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        requestBuilder.accept(null, HTML, XHTML, XML, null, JSON, TEXT, null);

        // Verify
        List<NameValueObject> headers = requestBuilder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(equalTo(5)));

        List<Matcher<? extends String>> values = new ArrayList<Matcher<? extends String>>();
        values.add(equalTo(HTML));
        values.add(equalTo(XHTML));
        values.add(equalTo(XML));
        values.add(equalTo(JSON));
        values.add(equalTo(TEXT));

        for (NameValueObject header : headers) {
            assertThat(header.getName(), is(equalTo("Accept")));
            assertThat(header.getValue(), is(anyOf(values)));
        }

    }

    @Test
    public void testAuth_TypeSafe() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        AuthInfo auth = Operations.digest("aaa", "bbb");
        builder.auth(auth);

        // Verify
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.DIGEST));
        assertThat(authInfo.username, is(equalTo("aaa")));
        assertThat(authInfo.password, is(equalTo("bbb")));
    }

    @Test
    public void testAuth_String() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.auth("BASIC", "pppp", "hhhh");

        // Verify
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.BASIC));
        assertThat(authInfo.username, is(equalTo("pppp")));
        assertThat(authInfo.password, is(equalTo("hhhh")));
    }

    @Test
    public void testAuth_Enum() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.auth(AuthType.CLIENT_CERT, "abc", "xyz");

        // Verify
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.CLIENT_CERT));
        assertThat(authInfo.username, is(equalTo("abc")));
        assertThat(authInfo.password, is(equalTo("xyz")));
    }

    @Test
    public void testAuth_Null() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.auth(null);

        // Verify
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuth_InvalidAuthType() {

        // Setup
        HttpRequestBuilder builder = new HttpRequestBuilder("http://www.ambrosoli.jp/");

        // Exercise
        builder.auth("hoge", "user", "password");

        // Verify
        fail("例外が発生しませんでした。");
    }

}
