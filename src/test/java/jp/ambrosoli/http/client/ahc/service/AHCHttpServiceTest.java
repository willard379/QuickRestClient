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
package jp.ambrosoli.http.client.ahc.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.http.client.Http;
import jp.ambrosoli.http.client.enums.AuthType;
import jp.ambrosoli.http.client.enums.HttpMethod;
import jp.ambrosoli.http.client.headers.HttpHeaders;
import jp.ambrosoli.http.client.params.AuthInfo;
import jp.ambrosoli.http.client.params.NameValueObject;
import jp.ambrosoli.http.client.params.ProxyInfo;
import jp.ambrosoli.http.client.params.RequestParams;
import jp.ambrosoli.http.client.request.HttpRequest;
import jp.ambrosoli.http.client.response.HttpResponse;
import jp.ambrosoli.http.client.util.StringUtil;
import jp.ambrosoli.http.client.util.URIUtil;

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

public class AHCHttpServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testExecute_Minimum() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        HttpRequest request = new HttpRequest(
                URIUtil.toURI("http://ambrosoli.jp/test-server/statusCode/ok"));
        HttpResponse response = service.execute(request);

        // Act
        boolean isOk = response.isSuccess();

        // Assert
        assertThat(isOk, is(true));
    }

    @Test
    public void testExecute_Maximum() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Accept", "application/xml"));
        values.add(new NameValueObject("Connection", "Keep-Alive"));
        values.add(new NameValueObject("Accept-Language", "ja"));
        values.add(new NameValueObject("Pragma", "no-cache"));
        values.add(new NameValueObject("Vary", "Accept-Charset, Accept-Language"));

        HttpRequest request = new HttpRequest(
                URIUtil.toURI("http://ambrosoli.jp/test-server/header/sameAsParams"));
        request.setMethod(HttpMethod.POST);
        request.setCharset(StringUtil.DEFAULT_ENCODING);
        request.addHeaders(values);
        request.setParams(new RequestParams(values));
        request.setProtocol(Http.HTTP_1_0);
        request.setTimeout(2000);

        // Act
        HttpResponse response = service.execute(request);

        // Assert
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("")));
    }

    @Test
    public void testGetSocketFactory_HTTP() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        SocketFactory factory = service.getSocketFactory(URIUtil
                .toURI("http://www.ambrosoli.jp/test-server/"));

        // Assert
        assertThat(factory, is(instanceOf(PlainSocketFactory.class)));

    }

    @Test
    public void testGetSocketFactory_SSL() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        SocketFactory factory = service.getSocketFactory(URIUtil
                .toURI("https://www.ambrosoli.jp/test-server/"));

        // Assert
        assertThat(factory, is(instanceOf(SSLSocketFactory.class)));
    }

    @Test
    public void testGetSocketFactory_Null() {

        // Arrange
        this.exceptionRule.expect(is(instanceOf(IllegalStateException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null.")));

        // Act
        AHCHttpService service = new AHCHttpService();
        service.getSocketFactory(null);

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testGetSocketFactory_InvalidScheme() {

        // Arrange
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme.")));

        // Act
        AHCHttpService service = new AHCHttpService();
        service.getSocketFactory(URIUtil.toURI("ws://www.ambrosoli.jp/test-server/"));

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateSchemeRegistry_HTTP() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        SchemeRegistry schreg = service.createSchemeRegistry(URIUtil
                .toURI("http://www.ambrosoli.jp/test-server/"));

        // Assert
        assertThat(schreg, is(notNullValue()));

    }

    @Test
    public void testCreateSchemeRegistry_SSL() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        SchemeRegistry schreg = service.createSchemeRegistry(URIUtil
                .toURI("https://www.ambrosoli.jp/test-server/"));

        // Assert
        assertThat(schreg, is(notNullValue()));
    }

    @Test
    public void testCreateSchemeRegistry_Null() {

        // Arrange
        this.exceptionRule.expect(is(instanceOf(IllegalStateException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null.")));

        // Act
        AHCHttpService service = new AHCHttpService();
        service.createSchemeRegistry(null);

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateSchemeRegistry_InvalidScheme() {

        // Arrange
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme.")));

        // Act
        AHCHttpService service = new AHCHttpService();
        service.createSchemeRegistry(URIUtil.toURI("wss://www.ambrosoli.jp/test-server/"));

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testCreateHttpParams() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        HttpParams params = service.createHttpParams();

        // Assert
        assertThat(params, is(notNullValue()));
    }

    @Test
    public void testCreateClientConnectionManager() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        HttpParams params = new BasicHttpParams();
        SchemeRegistry schreg = new SchemeRegistry();

        // Act
        ClientConnectionManager conman = service.createClientConnectionManager(params, schreg);

        // Assert
        assertThat(conman, is(notNullValue()));
    }

    @Test
    public void testCreateHttpClient() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        HttpParams params = new BasicHttpParams();
        SingleClientConnManager conman = new SingleClientConnManager(params, new SchemeRegistry());

        // Act
        HttpClient client = service.createHttpClient(conman, params);

        // Assert
        assertThat(client, is(notNullValue()));
    }

    @Test
    public void testCreateHttpUriRequest_Get() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/get");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.GET, params, "UTF-8");

        // Assert
        assertThat(request, is(instanceOf(HttpGet.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_DELETE() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/delete");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.DELETE, params,
                "UTF-8");

        // Assert
        assertThat(request, is(instanceOf(HttpDelete.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Head() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/head");
        HttpUriRequest request = service
                .createHttpUriRequest(uri, HttpMethod.HEAD, params, "UTF-8");

        // Assert
        assertThat(request, is(instanceOf(HttpHead.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Options() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/options");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.OPTIONS, params,
                "UTF-8");

        // Assert
        assertThat(request, is(instanceOf(HttpOptions.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateHttpUriRequest_Get_Null() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/get");
        HttpUriRequest request = service.createHttpUriRequest(uri, HttpMethod.GET, null, "UTF-8");

        // Assert
        assertThat(request, is(instanceOf(HttpGet.class)));
        assertThat(request.getURI().toString(), is(equalTo(uri.toString())));

    }

    @Test
    public void testCreateHttpUriRequest_Post() throws IOException {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/post");
        HttpPost httpPost = (HttpPost) service.createHttpUriRequest(uri, HttpMethod.POST, params,
                "UTF-8");

        // Assert
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

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/put");
        HttpPut httpPut = (HttpPut) service.createHttpUriRequest(uri, HttpMethod.PUT, params,
                "UTF-8");

        // Assert
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

        // Arrange
        AHCHttpService service = new AHCHttpService();

        // Act
        URI uri = URIUtil.toURI("http://ambrosoli.jp/post");
        HttpPost httpPost2 = (HttpPost) service.createHttpUriRequest(uri, HttpMethod.POST, null,
                "UTF-8");

        // Assert
        assertThat(httpPost2, is(instanceOf(HttpPost.class)));
        assertThat(httpPost2.getURI(), is(equalTo(uri)));
        assertThat(httpPost2.getEntity(), is(nullValue()));
    }

    @Test
    public void testAddQueryString() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("http://ambrosoli.jp/");
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        String fullURI = service.addQueryString(uri, params, "UTF-8").toString();

        // Assert
        assertThat(fullURI, is(equalTo("http://ambrosoli.jp/?a=A&b=B&c=C")));

    }

    @Test
    public void testAddQueryString_NullParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        URI uri = URIUtil.toURI("http://ambrosoli.jp/");

        // Act
        String fullURI = service.addQueryString(uri, null, "UTF-8").toString();
        // Assert
        assertThat(fullURI, is(equalTo("http://ambrosoli.jp/")));

    }

    @Test
    public void testAddQueryString_EmptyParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        URI uri = URIUtil.toURI("http://ambrosoli.jp/");

        // Act
        String fullURI = service.addQueryString(uri, new RequestParams(null), "UTF-8").toString();
        // Assert
        assertThat(fullURI, is(equalTo("http://ambrosoli.jp/")));

    }

    @Test
    public void testAddQueryString_NoEncoding() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("http://ambrosoli.jp/");
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A"));
        nvo.add(new NameValueObject("b", "B"));
        nvo.add(new NameValueObject("c", "C"));
        RequestParams params = new RequestParams(nvo);

        // Act
        String fullURI = service.addQueryString(uri, params, null).toString();

        // Assert
        assertThat(fullURI, is(equalTo("http://ambrosoli.jp/?a=A&b=B&c=C")));
    }

    @Test
    public void testSetProxy() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();
        ProxyInfo proxy = new ProxyInfo("localhost", 8080);
        service.setProxy(httpParams, proxy);

        // Act
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);

        // Assert
        assertThat(httpHost, is(notNullValue()));
        assertThat(httpHost.getHostName(), is(equalTo(proxy.getHost())));
        assertThat(httpHost.getPort(), is(equalTo(proxy.getPort())));

    }

    @Test
    public void testSetProxy_NullParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();
        service.setProxy(httpParams, null);

        // Act
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);

        // Assert
        assertThat(httpHost, is(nullValue()));
    }

    @Test
    public void testSetProtocolVersion_HTTP1_0() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        HttpRequestBase httpRequestBase = new HttpGet();

        // Act
        service.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_0);

        // Assert
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void testSetProtocolVersion_HTTP1_1() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        HttpRequestBase httpRequestBase = new HttpPost();

        // Act
        service.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_1);

        // Assert
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_1)));

    }

    @Test
    public void testSetProtocolVersion_Invalid() {

        // Arrange
        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("Http protocol version is illegal")));

        // Act
        AHCHttpService service = new AHCHttpService();
        service.setProtocolVersion(new BasicHttpParams(), "HTTP/2.0");

        // Assert
        fail("例外が発生しませんでした。");
    }

    @Test
    public void testSetTimeout_PositiveValueParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Act
        service.setTimeout(httpParams, 1000);

        // Assert
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(1000));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(1000));

    }

    @Test
    public void testSetTimeout_NegativeValueParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Act
        service.setTimeout(httpParams, -1);

        // Assert
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(-1));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(-1));

    }

    @Test
    public void testSetTimeout_ZeroValueParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Act
        service.setTimeout(httpParams, 0);

        // Assert
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(0));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(0));

    }

    @Test
    public void testSetCharset() {

        // Arrange
        AHCHttpService service = new AHCHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Act
        service.setCharset(httpParams, "UTF-8");

        // Assert
        String httpConnectCharset = (String) httpParams
                .getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
        assertThat(httpConnectCharset, is(equalTo("UTF-8")));

        String httpElementCharset = (String) httpParams
                .getParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET);
        assertThat(httpElementCharset, is(equalTo("UTF-8")));
    }

    @Test
    public void testSetHeaders() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml"));
        headers.add(new NameValueObject("Connection", "Keep-Alive"));
        headers.add(new NameValueObject("Accept-Language", "ja"));
        headers.add(new NameValueObject("Pragma", "no-cache"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Act
        HttpRequestBase httpRequestBase = new HttpGet();
        service.setHeaders(httpRequestBase, httpHeaders);

        // Assert
        Header[] result = httpRequestBase.getAllHeaders();
        assertThat(result.length, is(4));

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i].getName(), is(equalTo(headers.get(i).getName())));
            assertThat(result[i].getValue(), is(equalTo(headers.get(i).getValue())));
        }

    }

    @Test
    public void testSetHeaders_NullParam() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml"));
        headers.add(new NameValueObject("Connection", "Keep-Alive"));
        headers.add(new NameValueObject("Accept-Language", "ja"));
        headers.add(new NameValueObject("Pragma", "no-cache"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Act
        HttpRequestBase httpRequestBase = new HttpPost();
        service.setHeaders(httpRequestBase, null);

        // Assert
        assertThat(httpRequestBase.getAllHeaders().length, is(0));

    }

    @Test
    public void testSetFormEntity() throws IOException {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        HttpPost httpPost = new HttpPost();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("キン肉スグル", "日本"));
        params.add(new NameValueObject("テリーマン", "アメリカ"));
        params.add(new NameValueObject("ロビンマスク", "イギリス"));
        params.add(new NameValueObject("中国", "ラーメンマン"));
        params.add(new NameValueObject("ウォーズマン", "ソ連"));

        RequestParams requestParams = new RequestParams(params);

        // Act
        service.setFormEntity(httpPost, requestParams, "UTF-8");

        // Assert
        List<NameValuePair> list = URLEncodedUtils.parse(httpPost.getEntity());
        assertThat(list.size(), is(5));

        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getName(), is(equalTo(params.get(i).getName())));
            assertThat(list.get(i).getValue(), is(equalTo(params.get(i).getValue())));
        }
    }

    @Test
    public void testSetCredentialsAuthenticate_Basic() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("http://ambrosoli.jp");
        AuthInfo authInfo = new AuthInfo(AuthType.BASIC, "username", "password");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Act
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Assert
        Credentials credentials = provider.getCredentials(new AuthScope("ambrosoli.jp", 80));
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("username")));
        assertThat(credentials.getPassword(), is(equalTo("password")));
    }

    @Test
    public void testSetCredentialsAuthenticate_Digest() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("https://ambrosoli.jp");
        AuthInfo authInfo = new AuthInfo(AuthType.DIGEST, "u1", "p1");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Act
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Assert
        Credentials credentials = provider.getCredentials(new AuthScope("ambrosoli.jp", 443));
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("u1")));
        assertThat(credentials.getPassword(), is(equalTo("p1")));
    }

    @Test
    public void testSetCredentialsAuthenticate_Other() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("http://ambrosoli.jp");
        AuthInfo authInfo = new AuthInfo(AuthType.CLIENT_CERT, "u1", "p1");
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Act
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Assert
        Credentials credentials = provider.getCredentials(new AuthScope("ambrosoli.jp", 80));
        assertThat(credentials, is(nullValue()));
    }

    @Test
    public void testSetCredentialsAuthenticate_AuthInfoNull() {

        // Arrange
        AHCHttpService service = new AHCHttpService();

        URI uri = URIUtil.toURI("http://ambrosoli.jp");
        AuthInfo authInfo = null;
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Act
        service.setCredentialsAuthenticate(uri, authInfo, provider);

        // Assert
        Credentials credentials = provider.getCredentials(new AuthScope("ambrosoli.jp", 80));
        assertThat(credentials, is(nullValue()));
    }

}
