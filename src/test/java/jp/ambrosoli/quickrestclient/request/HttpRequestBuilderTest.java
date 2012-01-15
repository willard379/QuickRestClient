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

import static jp.ambrosoli.quickrestclient.Operations.*;
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
import jp.ambrosoli.quickrestclient.request.HttpRequestBuilder;
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

        // Arrange
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("URL may not be null or blank.")));

        // Act
        new HttpRequestBuilder(null);

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCharset() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.charset("Shift_JIS");

        // Assert
        String charset = builder.request.getCharset();
        assertThat(charset, is(equalTo("Shift_JIS")));

    }

    @Test
    public void testCharset_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.charset(null);

        // Assert
        String charset = builder.request.getCharset();
        assertThat(charset, is(nullValue()));
    }

    @Test
    public void testHeaders_NameValueObjectArray() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        NameValueObject[] params = new NameValueObject[3];
        params[0] = new NameValueObject("Header1", "Value1");
        params[1] = new NameValueObject("Header2", "Value2");
        params[2] = new NameValueObject("Header3", "Value3");

        // Act
        builder.headers(params);

        // Assert
        List<NameValueObject> headers = builder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(3));

        for (int i = 0; i < params.length; i++) {
            assertThat(headers.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(headers.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void testHeaders_NameValueObjectArray_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");
        NameValueObject[] params = null;

        // Act
        builder.headers(params);

        // Assert
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));

    }

    @Test
    public void testHeaders_Map() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        Map<String, String> params = new HashMap<String, String>();
        params.put("ibaraki", "mito");
        params.put("tochigi", "utsunomiya");
        params.put("gunma", "maebashi");
        params.put("saitama", "saitama");
        params.put("chiba", "chiba");
        params.put("tokyo", "shinjyuku");
        params.put("kanagawa", "yokohama");

        // Act
        builder.headers(params);

        // Assert
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

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        Map<String, String> params = null;

        // Act
        builder.headers(params);

        // Assert
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers.isEmpty(), is(true));
    }

    @Test
    public void testMethod() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
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

        // Assert
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void testMethod_Null() {

        // Arrange
        this.expectedException.expect(is(instanceOf(NullPointerException.class)));
        this.expectedException.expectMessage(is(equalTo("method may not to be null.")));

        // Act
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");
        builder.method(null);

        // Assert
        fail("例外が発生しませんでした");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethod_Invalid() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.method("TRACE");

        // Assert
        fail("例外が発生しませんでした");
    }

    @Test
    public void testMethod_LowerCase() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
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

        // Assert
        assertThat(method_get, is(HttpMethod.GET));
        assertThat(method_post, is(HttpMethod.POST));
        assertThat(method_put, is(HttpMethod.PUT));
        assertThat(method_delete, is(HttpMethod.DELETE));
        assertThat(method_options, is(HttpMethod.OPTIONS));
        assertThat(method_head, is(HttpMethod.HEAD));
    }

    @Test
    public void testParams_NameValueObjectArray() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        NameValueObject[] params = new NameValueObject[1];
        params[0] = new NameValueObject("lettuce", "レタス");
        params[0] = new NameValueObject("celery", "セロリ");
        params[0] = new NameValueObject("bracken", "蕨");

        // Act
        builder.params(params);

        // Assert
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList.size(), is(params.length));
        for (int i = 0; i < params.length; i++) {
            assertThat(paramList.get(i).getName(), is(equalTo(params[i].getName())));
            assertThat(paramList.get(i).getValue(), is(equalTo(params[i].getValue())));
        }

    }

    @Test
    public void testParams_NameValueObjectArray_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");
        NameValueObject[] params = null;

        // Act
        builder.params(params);

        // Assert
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void testParams_Map() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        Map<String, String> params = new HashMap<String, String>();
        params.put("NISSAN", "Japan");
        params.put("Rolls Royce", "Britain");
        params.put("Chrysler", "U.S.A");
        params.put("Porsche", "Germany");

        // Act
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

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.params((Map<String, String>) null);

        // Assert
        List<NameValueObject> paramList = builder.request.getParams().getParams();
        assertThat(paramList, is(nullValue()));
    }

    @Test
    public void testProtocol() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.protocol(Http.HTTP_1_0);

        // Assert
        assertThat(builder.request.getProtocol(), is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void testProtocol_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.protocol(null);

        // Assert
        assertThat(builder.request.getProtocol(), is(nullValue()));
    }

    @Test
    public void testProxy() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.proxy("localhost", 8080);

        // Assert
        ProxyInfo proxyInfo = builder.request.getProxyInfo();
        assertThat(proxyInfo.getHost(), is(equalTo("localhost")));
        assertThat(proxyInfo.getPort(), is(equalTo(8080)));
    }

    @Test
    public void testProxy_Null() {

        // Arrange
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("hostname may not be null")));

        // Act
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");
        builder.proxy(null, 8080);

        // Assert
        fail("例外が発生しませんでした");

    }

    @Test
    public void testTimeout_PositiveValue() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.timeout(15000);

        // Assert
        assertThat(builder.request.getTimeout(), is(15000));

    }

    @Test
    public void testTimeout_ZeroValue() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.timeout(0);

        // Assert
        assertThat(builder.request.getTimeout(), is(0));

    }

    @Test
    public void testTimeout_NegativeValue() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.timeout(-15000);

        // Assert
        assertThat(builder.request.getTimeout(), is(-15000));

    }

    @Test
    public void testUserAgent() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.userAgent("hoge/1.0");

        // Assert
        List<NameValueObject> headers = builder.request.getHeaders().getHeaders();
        assertThat(headers.size(), is(1));
        assertThat(headers.get(0).getName(), is(equalTo("User-Agent")));
        assertThat(headers.get(0).getValue(), is(equalTo("hoge/1.0")));
    }

    @Test
    public void testExecute() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder(
                "http://ambrosoli.jp/test-server/statusCode/ok");

        // Act
        HttpResponse response = builder.execute();

        // Assert
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void testSetUserAgent() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.userAgent("Mozilla/10.0");

        // Assert
        NameValueObject userAgent = builder.request.getHeaders().getHeaders().get(0);
        assertThat(userAgent.getName(), is("User-Agent"));
        assertThat(userAgent.getValue(), is("Mozilla/10.0"));

    }

    @Test
    public void testSetUserAgent_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.userAgent(null);

        // Assert
        HttpHeaders headers = builder.request.getHeaders();
        assertThat(headers, is(nullValue()));

    }

    @Test
    public void testAccept() {

        // Act
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder("http://www.ambrosoli.jp/");
        requestBuilder.accept(HTML, XHTML, XML, JSON, TEXT);

        // Assert
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

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        AuthInfo auth = Operations.digest("aaa", "bbb");
        builder.auth(auth);

        // Assert
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.DIGEST));
        assertThat(authInfo.username, is(equalTo("aaa")));
        assertThat(authInfo.password, is(equalTo("bbb")));
    }

    @Test
    public void testAuth_String() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.auth("BASIC", "pppp", "hhhh");

        // Assert
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.BASIC));
        assertThat(authInfo.username, is(equalTo("pppp")));
        assertThat(authInfo.password, is(equalTo("hhhh")));
    }

    @Test
    public void testAuth_Null() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.auth(null);

        // Assert
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(nullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void testAuth_AuthTypeNull() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        String authType = null;
        builder.auth(authType, "user", "password");

        // Assert
        fail("例外が発生しませんでした。");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuth_InvalidAuthType() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.auth("hoge", "user", "password");

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testAuth3() {

        // Arrange
        HttpRequestBuilder builder = new HttpRequestBuilder("http://ambrosoli.jp/");

        // Act
        builder.auth(AuthType.CLIENT_CERT, "abc", "xyz");

        // Assert
        AuthInfo authInfo = builder.request.getAuthInfo();
        assertThat(authInfo, is(notNullValue()));
        assertThat(authInfo.type, is(AuthType.CLIENT_CERT));
        assertThat(authInfo.username, is(equalTo("abc")));
        assertThat(authInfo.password, is(equalTo("xyz")));
    }

}
