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
package jp.ambrosoli.quickrestclient.apache.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.Http;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.params.RequestParams;
import jp.ambrosoli.quickrestclient.request.HttpRequest;
import jp.ambrosoli.quickrestclient.response.HttpResponse;
import jp.ambrosoli.quickrestclient.util.StringUtil;
import jp.ambrosoli.quickrestclient.util.URIUtil;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApacheHttpServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testExecute_Minimum() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        HttpRequest request = new HttpRequest(
                URIUtil.toURI("http://www.ambrosoli.jp/test-server/statusCode/ok"));
        HttpResponse response = service.execute(request);

        // Exercise
        boolean isOk = response.isSuccess();

        // Verify
        assertThat(isOk, is(true));
    }

    @Test
    public void testExecute_Maximum() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Accept", "application/xml"));
        values.add(new NameValueObject("Connection", "Keep-Alive"));
        values.add(new NameValueObject("Accept-Language", "ja"));
        values.add(new NameValueObject("Pragma", "no-cache"));
        values.add(new NameValueObject("Vary", "Accept-Charset, Accept-Language"));

        HttpRequest request = new HttpRequest(
                URIUtil.toURI("http://www.ambrosoli.jp/test-server/header/sameAsParams"));
        request.setMethod(HttpMethod.POST);
        request.setCharset(StringUtil.DEFAULT_ENCODING);
        request.addHeaders(values);
        request.setParams(new RequestParams(values));
        request.setProtocol(Http.HTTP_1_0);
        request.setTimeout(2000);

        // Exercise
        HttpResponse response = service.execute(request);

        // Verify
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("")));
    }

    @Test
    public void testGetSocketFactory_HTTP() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        SocketFactory factory = service.getSocketFactory(URIUtil
                .toURI("http://www.ambrosoli.jp/test-server/"));

        // Verify
        assertThat(factory, is(instanceOf(PlainSocketFactory.class)));

    }

    @Test
    public void testGetSocketFactory_SSL() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        SocketFactory factory = service.getSocketFactory(URIUtil
                .toURI("https://www.ambrosoli.jp/test-server/"));

        // Verify
        assertThat(factory, is(instanceOf(SSLSocketFactory.class)));
    }

    @Test
    public void testGetSocketFactory_Null() {

        // Setup
        this.exceptionRule.expect(is(instanceOf(IllegalStateException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null.")));

        // Exercise
        ApacheHttpService service = new ApacheHttpService();
        service.getSocketFactory(null);

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testGetSocketFactory_InvalidScheme() {

        // Setup
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme.")));

        // Exercise
        ApacheHttpService service = new ApacheHttpService();
        service.getSocketFactory(URIUtil.toURI("ws://www.ambrosoli.jp/test-server/"));

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateSchemeRegistry_HTTP() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        SchemeRegistry schreg = service.createSchemeRegistry(URIUtil
                .toURI("http://www.ambrosoli.jp/test-server/"));

        // Verify
        assertThat(schreg, is(notNullValue()));

    }

    @Test
    public void testCreateSchemeRegistry_SSL() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        SchemeRegistry schreg = service.createSchemeRegistry(URIUtil
                .toURI("https://www.ambrosoli.jp/test-server/"));

        // Verify
        assertThat(schreg, is(notNullValue()));
    }

    @Test
    public void testCreateSchemeRegistry_Null() {

        // Setup
        this.exceptionRule.expect(is(instanceOf(IllegalStateException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null.")));

        // Exercise
        ApacheHttpService service = new ApacheHttpService();
        service.createSchemeRegistry(null);

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateSchemeRegistry_InvalidScheme() {

        // Setup
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme.")));

        // Exercise
        ApacheHttpService service = new ApacheHttpService();
        service.createSchemeRegistry(URIUtil.toURI("wss://www.ambrosoli.jp/test-server/"));

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateHttpParams() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        HttpParams params = service.createHttpParams();

        // Verify
        assertThat(params, is(notNullValue()));
    }

    @Test
    public void testCreateClientConnectionManager() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        HttpParams params = new BasicHttpParams();
        SchemeRegistry schreg = new SchemeRegistry();

        // Exercise
        ClientConnectionManager conman = service.createClientConnectionManager(params, schreg);

        // Verify
        assertThat(conman, is(notNullValue()));
    }

    @Test
    public void testCreateHttpClient() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        HttpParams params = new BasicHttpParams();
        SingleClientConnManager conman = new SingleClientConnManager(params, new SchemeRegistry());

        // Exercise
        HttpClient client = service.createHttpClient(conman, params);

        // Verify
        assertThat(client, is(notNullValue()));
    }

    @Test
    public void testCreateHttpUriRequest_Get() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/get");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.GET, params, "UTF-8");

        // Verify
        assertThat(request, is(instanceOf(HttpGet.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_DELETE() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/delete");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.DELETE, params,
                "UTF-8");

        // Verify
        assertThat(request, is(instanceOf(HttpDelete.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Head() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/head");
        HttpUriRequest request = service
                .createHttpUriRequest(uri, HttpMethod.HEAD, params, "UTF-8");

        // Verify
        assertThat(request, is(instanceOf(HttpHead.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Options() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/options");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.OPTIONS, params,
                "UTF-8");

        // Verify
        assertThat(request, is(instanceOf(HttpOptions.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Get_Null() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/get");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.GET, null, "UTF-8");

        // Verify
        assertThat(request, is(instanceOf(HttpGet.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString())));

    }

    @Test
    public void testCreateHttpUriRequest_Post() throws IOException {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/post");
        HttpPost httpPost = (HttpPost) service.createHttpUriRequest(uri, HttpMethod.POST, params,
                "UTF-8");

        // Verify
        assertThat(httpPost, is(instanceOf(HttpPost.class)));
        assertThat(httpPost.getURI(), is(equalTo(uri)));
        assertThat(httpPost.getEntity(), is(notNullValue()));
        List<NameValuePair> postParams = URLEncodedUtils.parse(httpPost.getEntity());
        for (int i = 0; i < nvo.size(); i++) {
            assertThat(postParams.get(i).getName(), is(nvo.get(i).getName()));
            assertThat(postParams.get(i).getValue(), is(nvo.get(i).getValue()));
        }

    }

    @Test
    public void testCreateHttpUriRequest_Put() throws IOException {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/put");
        HttpPut httpPut = (HttpPut) service.createHttpUriRequest(uri, HttpMethod.PUT, params,
                "UTF-8");

        // Verify
        assertThat(httpPut, is(instanceOf(HttpPut.class)));
        assertThat(httpPut.getURI(), is(equalTo(uri)));
        assertThat(httpPut.getEntity(), is(notNullValue()));
        List<NameValuePair> putParams = URLEncodedUtils.parse(httpPut.getEntity());
        for (int i = 0; i < nvo.size(); i++) {
            assertThat(putParams.get(i).getName(), is(nvo.get(i).getName()));
            assertThat(putParams.get(i).getValue(), is(nvo.get(i).getValue()));
        }

    }

    @Test
    public void testCreateHttpUriRequest_Post_Null() throws IOException {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/post");
        HttpPost httpPost2 = (HttpPost) service.createHttpUriRequest(uri, HttpMethod.POST, null,
                "UTF-8");

        // Verify
        assertThat(httpPost2, is(instanceOf(HttpPost.class)));
        assertThat(httpPost2.getURI(), is(equalTo(uri)));
        assertThat(httpPost2.getEntity(), is(nullValue()));
    }

    @Test
    public void testAddQueryString() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        String fullURI = service.addQueryString(uri, params, "UTF-8").toString();

        // Verify
        assertThat(fullURI, is(equalTo("http://www.ambrosoli.jp/?a=A&b=B&c=C")));

    }

    @Test
    public void testAddQueryString_NullParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");

        // Exercise
        String fullURI = service.addQueryString(uri, null, "UTF-8").toString();
        // Verify
        assertThat(fullURI, is(equalTo("http://www.ambrosoli.jp/")));

    }

    @Test
    public void testAddQueryString_EmptyParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");

        // Exercise
        String fullURI = service.addQueryString(uri, new RequestParams(null), "UTF-8").toString();
        // Verify
        assertThat(fullURI, is(equalTo("http://www.ambrosoli.jp/")));

    }

    @Test
    public void testAddQueryString_NoEncoding() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Exercise
        String fullURI = service.addQueryString(uri, params, null).toString();

        // Verify
        assertThat(fullURI, is(equalTo("http://www.ambrosoli.jp/?a=A&b=B&c=C")));
    }

    @Test
    public void testSetProxy() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();
        ProxyInfo proxy = new ProxyInfo("localhost", 8080);
        service.setProxy(httpParams, proxy);

        // Exercise
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);

        // Verify
        assertThat(httpHost, is(notNullValue()));
        assertThat(httpHost.getHostName(), is(equalTo(proxy.getHost())));
        assertThat(httpHost.getPort(), is(equalTo(proxy.getPort())));

    }

    @Test
    public void testSetProxy_NullParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();
        service.setProxy(httpParams, null);

        // Exercise
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);

        // Verify
        assertThat(httpHost, is(nullValue()));
    }

    @Test
    public void testSetProtocolVersion_HTTP1_0() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        HttpRequestBase httpRequestBase = new HttpGet();

        // Exercise
        service.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_0);

        // Verify
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void testSetProtocolVersion_HTTP1_1() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        HttpRequestBase httpRequestBase = new HttpPost();

        // Exercise
        service.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_1);

        // Verify
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_1)));

    }

    @Test
    public void testSetProtocolVersion_Invalid() {

        // Setup
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("Http protocol version is illegal")));

        // Exercise
        ApacheHttpService service = new ApacheHttpService();
        service.setProtocolVersion(new BasicHttpParams(), "HTTP/2.0");

        // Verify
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testSetTimeout_PositiveValueParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        service.setTimeout(httpParams, 1000);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(1000));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(1000));

    }

    @Test
    public void testSetTimeout_NegativeValueParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        service.setTimeout(httpParams, -1);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(-1));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(-1));

    }

    @Test
    public void testSetTimeout_ZeroValueParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        service.setTimeout(httpParams, 0);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(0));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(0));

    }

    @Test
    public void testSetCharset() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        service.setCharset(httpParams, "UTF-8");

        // Verify
        String httpConnectCharset = (String) httpParams
                .getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
        assertThat(httpConnectCharset, is(equalTo("UTF-8")));

        String httpElementCharset = (String) httpParams
                .getParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET);
        assertThat(httpElementCharset, is(equalTo("UTF-8")));
    }

    @Test
    public void testSetHeaders() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml"));
        headers.add(new NameValueObject("Connection", "Keep-Alive"));
        headers.add(new NameValueObject("Accept-Language", "ja"));
        headers.add(new NameValueObject("Pragma", "no-cache"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Exercise
        HttpRequestBase httpRequestBase = new HttpGet();
        service.setHeaders(httpRequestBase, httpHeaders);

        // Verify
        Header[] result = httpRequestBase.getAllHeaders();
        assertThat(result.length, is(4));

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i].getName(), is(equalTo(headers.get(i).getName())));
            assertThat(result[i].getValue(), is(equalTo(headers.get(i).getValue())));
        }

    }

    @Test
    public void testSetHeaders_NullParam() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml"));
        headers.add(new NameValueObject("Connection", "Keep-Alive"));
        headers.add(new NameValueObject("Accept-Language", "ja"));
        headers.add(new NameValueObject("Pragma", "no-cache"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Exercise
        HttpRequestBase httpRequestBase = new HttpPost();
        service.setHeaders(httpRequestBase, null);

        // Verify
        assertThat(httpRequestBase.getAllHeaders().length, is(0));

    }

    @Test
    public void testSetFormEntity() throws IOException {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        HttpPost httpPost = new HttpPost();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("キン肉スグル", "日本"));
        params.add(new NameValueObject("テリーマン", "アメリカ"));
        params.add(new NameValueObject("ロビンマスク", "イギリス"));
        params.add(new NameValueObject("中国", "ラーメンマン"));
        params.add(new NameValueObject("ウォーズマン", "ソ連"));

        RequestParams requestParams = new RequestParams(params);

        // Exercise
        service.setFormEntity(httpPost, requestParams, "UTF-8");

        // Verify
        List<NameValuePair> list = URLEncodedUtils.parse(httpPost.getEntity());
        assertThat(list.size(), is(5));

        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getName(), is(equalTo(params.get(i).getName())));
            assertThat(list.get(i).getValue(), is(equalTo(params.get(i).getValue())));
        }
    }

    @Test
    public void testSetCredentialsAuthenticate_Basic() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        AuthInfo authInfo = new AuthInfo(AuthType.BASIC, "username", "password");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80));
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("username")));
        assertThat(credentials.getPassword(), is(equalTo("password")));
    }

    @Test
    public void testSetCredentialsAuthenticate_Digest() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("https://www.ambrosoli.jp/");
        AuthInfo authInfo = new AuthInfo(AuthType.DIGEST, "u1", "p1");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 443));
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("u1")));
        assertThat(credentials.getPassword(), is(equalTo("p1")));
    }

    @Test
    public void testSetCredentialsAuthenticate_Other() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp");
        AuthInfo authInfo = new AuthInfo(AuthType.CLIENT_CERT, "u1", "p1");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80));
        assertThat(credentials, is(nullValue()));
    }

    @Test
    public void testSetCredentialsAuthenticate_AuthInfoNull() {

        // Setup
        ApacheHttpService service = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp");
        AuthInfo authInfo = null;
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80));
        assertThat(credentials, is(nullValue()));
    }

}
