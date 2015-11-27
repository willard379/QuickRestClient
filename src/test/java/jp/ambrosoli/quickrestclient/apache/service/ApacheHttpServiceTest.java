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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import jp.ambrosoli.quickrestclient.unittest.DataSource;
import jp.ambrosoli.quickrestclient.util.StringUtil;
import jp.ambrosoli.quickrestclient.util.URIUtil;

public class ApacheHttpServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void HttpRequestにURLのみ指定してexecuteを実行すると_正常に通信できること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        HttpRequest request = new HttpRequest(URIUtil.toURI(DataSource.url("statusCode/ok"))); //$NON-NLS-1$

        // Exercise
        HttpResponse actual = sut.execute(request);

        // Verify
        assertThat(actual.isSuccess(), is(true));
    }

    @Test
    public void HttpRequestに各種パラメータを指定してexecuteを実行すると_正常に通信できること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Accept", "application/xml")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Connection", "Keep-Alive")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Accept-Language", "ja")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Pragma", "no-cache")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Vary", "Accept-Charset, Accept-Language")); //$NON-NLS-1$ //$NON-NLS-2$

        HttpRequest request = new HttpRequest(URIUtil.toURI(DataSource.url("header/sameAsParams"))); //$NON-NLS-1$
        request.setMethod(HttpMethod.POST);
        request.setCharset(StringUtil.DEFAULT_ENCODING);
        request.addHeaders(values);
        request.setParams(new RequestParams(values));
        request.setProtocol(Http.HTTP_1_0);
        request.setTimeout(2000);

        // Exercise
        HttpResponse actual = sut.execute(request);

        // Verify
        assertThat(actual.isSuccess(), is(true));
    }

    @Test
    public void getSocketFactoryの引数にhttpスキームのURIを指定した場合_PlainSocketFactoryが生成されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        SocketFactory actual = sut.getSocketFactory(URIUtil.toURI("http://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(PlainSocketFactory.class)));

    }

    @Test
    public void getSocketFactoryの引数にhttpsスキームのURIを指定した場合_SSLSocketFactoryが生成されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        SocketFactory actual = sut.getSocketFactory(URIUtil.toURI("https://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(SSLSocketFactory.class)));
    }

    @Test
    public void getSocketFactoryの引数にnullを指定した場合_IllegalStateExceptionが発生すること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        this.exceptionRule.expect(is(instanceOf(NullPointerException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null."))); //$NON-NLS-1$

        // Exercise
        sut.getSocketFactory(null);

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void getSocketFactoryの引数にwsスキームのURIを指定した場合_IllegalArgumentExceptionが発生すること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme."))); //$NON-NLS-1$

        // Exercise
        sut.getSocketFactory(URIUtil.toURI("ws://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void createSchemeRegistryにhttpスキームのURIを渡した場合_戻り値のSchemeRegistryにhttpスキームが設定されていること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        SchemeRegistry actual = sut.createSchemeRegistry(URIUtil.toURI("http://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(notNullValue()));
        List<String> schemeNames = actual.getSchemeNames();
        assertThat(schemeNames.size(), is(1));
        assertThat(schemeNames.get(0), is(equalTo("http"))); //$NON-NLS-1$
    }

    @Test
    public void createSchemeRegistryにhttpsスキームのURIを渡した場合_戻り値のSchemeRegistryにhttpsスキームが設定されていること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        SchemeRegistry actual = sut.createSchemeRegistry(URIUtil.toURI("https://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(notNullValue()));
        List<String> schemeNames = actual.getSchemeNames();
        assertThat(schemeNames.size(), is(1));
        assertThat(schemeNames.get(0), is(equalTo("https"))); //$NON-NLS-1$
    }

    @Test
    public void createSchemeREgistryにnullを渡した場合_NullPointerExceptionが発生すること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        this.exceptionRule.expect(is(instanceOf(NullPointerException.class)));
        this.exceptionRule.expectMessage(is(equalTo("URL is null."))); //$NON-NLS-1$

        // Exercise
        sut.createSchemeRegistry(null);

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void createSchemeRegistryの引数にwssスキームのURIを指定した場合_IllegalArgumentExceptionが発生すること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("invalid scheme."))); //$NON-NLS-1$

        // Exercise
        sut.createSchemeRegistry(URIUtil.toURI("wss://www.ambrosoli.jp/test-server/")); //$NON-NLS-1$

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void createHttpParamsを呼び出すと_HttpParamsが生成されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        HttpParams actual = sut.createHttpParams();

        // Verify
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void createClientConnectionManagerを呼び出すと_ClientConnectionManagerが生成されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        HttpParams params = new BasicHttpParams();
        SchemeRegistry schreg = new SchemeRegistry();

        // Exercise
        ClientConnectionManager actual = sut.createClientConnectionManager(params, schreg);

        // Verify
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void createHttpClientを呼び出すと_HttpClientが生成されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        HttpParams params = new BasicHttpParams();
        SingleClientConnManager conman = new SingleClientConnManager(params, new SchemeRegistry());

        // Exercise
        HttpClient actual = sut.createHttpClient(conman, params);

        // Verify
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void createHttpUriRequestの第二引数にGETを指定すると_HttpGetが生成され_クエリストリングが設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/get"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.GET, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpGet.class)));
        assertThat(actual.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createHttpUriRequestの第二引数にDELETEを指定すると_HttpDeleteが生成され_クエリストリングが設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/delete"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.DELETE, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpDelete.class)));
        assertThat(actual.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createHttpUriRequestの第二引数にHEADを指定すると_HttpHeadが生成され_クエリストリングが設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/head"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.HEAD, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpHead.class)));
        assertThat(actual.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createHttpUriRequestの第二引数にOptionsを指定すると_HttpOptionsが生成され_クエリストリングが設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/options"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.OPTIONS, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpOptions.class)));
        assertThat(actual.getURI().toString(), is(equalTo(uri.toString() + "?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createHttpUriRequestの第二引数にGetを指定して_第三引数のリクエストパラメータがnullの場合_クエリストリングが設定されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/get"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.GET, null, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpGet.class)));
        assertThat(actual.getURI().toString(), is(equalTo(uri.toString())));

    }

    @Test
    public void createHttpUriRequestの第二引数にPostを指定すると_HttpPostが生成され_リクエストパラメータが設定されること() throws IOException {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/post"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.POST, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpPost.class)));
        assertThat(actual.getURI(), is(equalTo(uri)));

        HttpPost post = (HttpPost) actual;
        assertThat(post.getEntity(), is(notNullValue()));
        List<NameValuePair> postParams = URLEncodedUtils.parse(post.getEntity());
        for (int i = 0; i < nvo.size(); i++) {
            assertThat(postParams.get(i).getName(), is(nvo.get(i).getName()));
            assertThat(postParams.get(i).getValue(), is(nvo.get(i).getValue()));
        }

    }

    @Test
    public void createHttpUriRequestの第二引数にPutを指定すると_HttpPutが生成され_リクエストパラメータが設定されること() throws IOException {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/put"); //$NON-NLS-1$
        HttpUriRequest actual = sut.createHttpUriRequest(uri, HttpMethod.PUT, params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpPut.class)));
        assertThat(actual.getURI(), is(equalTo(uri)));

        HttpPut put = (HttpPut) actual;
        assertThat(put.getEntity(), is(notNullValue()));
        List<NameValuePair> putParams = URLEncodedUtils.parse(put.getEntity());
        for (int i = 0; i < nvo.size(); i++) {
            assertThat(putParams.get(i).getName(), is(nvo.get(i).getName()));
            assertThat(putParams.get(i).getValue(), is(nvo.get(i).getValue()));
        }

    }

    @Test
    public void createHttpUriRequestの第二引数にPOSTを指定して_第三引数のリクエストパラメータがnullの場合_リクエストパラメータが設定されないこと() throws IOException {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        // Exercise
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/post"); //$NON-NLS-1$
        HttpPost actual = (HttpPost) sut.createHttpUriRequest(uri, HttpMethod.POST, null, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(instanceOf(HttpPost.class)));
        assertThat(actual.getURI(), is(equalTo(uri)));
        assertThat(actual.getEntity(), is(nullValue()));
    }

    @Test
    public void addQueryStringを呼び出すと_URIにクエリストリングが付与されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        String actual = sut.addQueryString(uri, params, "UTF-8").toString(); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(equalTo("http://www.ambrosoli.jp/?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void addQueryStringに渡すRequestParamsがnullの場合_URIにクエリストリングが付与されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        String actual = sut.addQueryString(uri, null, "UTF-8").toString(); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(equalTo("http://www.ambrosoli.jp/"))); //$NON-NLS-1$

    }

    @Test
    public void addQueryStringに渡すRequestParamsにリクエストパラメータがセットされていない場合_URIにクエリストリングが付与されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/"); //$NON-NLS-1$

        // Exercise
        String actual = sut.addQueryString(uri, new RequestParams(null), "UTF-8").toString(); //$NON-NLS-1$
        // Verify
        assertThat(actual, is(equalTo("http://www.ambrosoli.jp/"))); //$NON-NLS-1$

    }

    @Test
    public void addQueryStringに渡すエンコーディングを指定しない場合でも_URIにクエリストリングが付与されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        List<NameValueObject> nvo = new ArrayList<NameValueObject>();
        nvo.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        nvo.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams params = new RequestParams(nvo);

        // Exercise
        String actual = sut.addQueryString(uri, params, null).toString();

        // Verify
        assertThat(actual, is(equalTo("http://www.ambrosoli.jp/?a=A&b=B&c=C"))); //$NON-NLS-1$
    }

    @Test
    public void setProxyを呼び出すと_HttpHostが生成されプロキシの情報が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();
        ProxyInfo proxy = new ProxyInfo("localhost", 8080); //$NON-NLS-1$

        // Exercise
        sut.setProxy(httpParams, proxy);

        // Verify
        HttpHost actual = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getHostName(), is(equalTo(proxy.getHost())));
        assertThat(actual.getPort(), is(equalTo(proxy.getPort())));

    }

    @Test
    public void setProxyに渡すプロキシがnullの場合_HttpHostが生成されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        sut.setProxy(httpParams, null);

        // Verify
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);
        assertThat(httpHost, is(nullValue()));
    }

    @Test
    public void setProtocolVersionにHTTP_1_0を指定した場合_HTTPスラッシュ1てん0が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        HttpRequestBase httpRequestBase = new HttpGet();

        // Exercise
        sut.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_0);

        // Verify
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_0)));

    }

    @Test
    public void setProtocolVersionにHTTP_1_1を指定した場合_HTTPスラッシュ1てん1が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        HttpRequestBase httpRequestBase = new HttpPost();

        // Exercise
        sut.setProtocolVersion(httpRequestBase.getParams(), Http.HTTP_1_1);

        // Verify
        String protocolVersion = httpRequestBase.getProtocolVersion().toString();
        assertThat(protocolVersion, is(equalTo(Http.HTTP_1_1)));

    }

    @Test
    public void setProtocolVersionにHTTP_2_0を指定した場合_IllegalArgumentExceptionが発生すること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        this.exceptionRule.expect(is(instanceOf(IllegalArgumentException.class)));
        this.exceptionRule.expectMessage(is(equalTo("Http protocol version is illegal."))); //$NON-NLS-1$

        // Exercise
        sut.setProtocolVersion(new BasicHttpParams(), "HTTP/2.0"); //$NON-NLS-1$

        // Verify
        fail("例外が発生しませんでした。"); //$NON-NLS-1$
    }

    @Test
    public void setTimeoutの引数に1000を渡した場合_SO_TIMEOUTとCONNECTION_TIMEOUTに1000が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        sut.setTimeout(httpParams, 1000);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(1000));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(1000));

    }

    @Test
    public void setTimeoutの引数にマイナス1を渡した場合_SO_TIMEOUTとCONNECTION_TIMEOUTにマイナス1が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        sut.setTimeout(httpParams, -1);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(-1));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(-1));

    }

    @Test
    public void setTimeoutの引数に0を指定した場合_SO_TIMEOUTとCONNECTION_TIMEOUTに0が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        sut.setTimeout(httpParams, 0);

        // Verify
        int soTimeout = httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
        assertThat(soTimeout, is(0));

        int connTimeout = httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0);
        assertThat(connTimeout, is(0));

    }

    @Test
    public void setCharsetの引数にUTF8を指定した場合_HTTP_CONTENT_CHARSETとHTTP_ELEMENT_CHARSETにUTF8が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();
        BasicHttpParams httpParams = new BasicHttpParams();

        // Exercise
        sut.setCharset(httpParams, "UTF-8"); //$NON-NLS-1$

        // Verify
        String httpConnectCharset = (String) httpParams.getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
        assertThat(httpConnectCharset, is(equalTo("UTF-8"))); //$NON-NLS-1$

        String httpElementCharset = (String) httpParams.getParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET);
        assertThat(httpElementCharset, is(equalTo("UTF-8"))); //$NON-NLS-1$
    }

    @Test
    public void setHeadersを呼び出すと_引数で渡したヘッダーがHTTPヘッダーに設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        HttpRequestBase httpRequestBase = new HttpGet();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Connection", "Keep-Alive")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Accept-Language", "ja")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Pragma", "no-cache")); //$NON-NLS-1$ //$NON-NLS-2$
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Exercise
        sut.setHeaders(httpRequestBase, httpHeaders);

        // Verify
        Header[] result = httpRequestBase.getAllHeaders();
        assertThat(result.length, is(4));

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i].getName(), is(equalTo(headers.get(i).getName())));
            assertThat(result[i].getValue(), is(equalTo(headers.get(i).getValue())));
        }

    }

    @Test
    public void setHeadersに渡すヘッダーのリストがnullの場合_HTTPヘッダーが設定されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        List<NameValueObject> headers = new ArrayList<NameValueObject>();
        headers.add(new NameValueObject("Accept", "application/xml")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Connection", "Keep-Alive")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Accept-Language", "ja")); //$NON-NLS-1$ //$NON-NLS-2$
        headers.add(new NameValueObject("Pragma", "no-cache")); //$NON-NLS-1$ //$NON-NLS-2$
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addHeaders(headers);

        // Exercise
        HttpRequestBase httpRequestBase = new HttpPost();
        sut.setHeaders(httpRequestBase, null);

        // Verify
        assertThat(httpRequestBase.getAllHeaders().length, is(0));

    }

    @Test
    public void setFormEntityを呼び出すと_引数で渡したリクエストパラメータが設定されること() throws IOException {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        HttpPost httpPost = new HttpPost();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("キン肉スグル", "日本")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("テリーマン", "アメリカ")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("ロビンマスク", "イギリス")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("中国", "ラーメンマン")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("ウォーズマン", "ソ連")); //$NON-NLS-1$ //$NON-NLS-2$

        RequestParams requestParams = new RequestParams(params);

        // Exercise
        sut.setFormEntity(httpPost, requestParams, "UTF-8"); //$NON-NLS-1$

        // Verify
        List<NameValuePair> list = URLEncodedUtils.parse(httpPost.getEntity());
        assertThat(list.size(), is(5));

        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getName(), is(equalTo(params.get(i).getName())));
            assertThat(list.get(i).getValue(), is(equalTo(params.get(i).getValue())));
        }
    }

    @Test
    public void setCredentialsAuthenticateを呼び出すと_引数で渡したBasic認証情報が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/"); //$NON-NLS-1$
        AuthInfo authInfo = new AuthInfo(AuthType.BASIC, "username", "password"); //$NON-NLS-1$ //$NON-NLS-2$
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        sut.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80)); //$NON-NLS-1$
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("username"))); //$NON-NLS-1$
        assertThat(credentials.getPassword(), is(equalTo("password"))); //$NON-NLS-1$
    }

    @Test
    public void setCredentialsAuthenticateを呼び出すと_引数で渡したDigest認証情報が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("https://www.ambrosoli.jp/"); //$NON-NLS-1$
        AuthInfo authInfo = new AuthInfo(AuthType.DIGEST, "u1", "p1"); //$NON-NLS-1$ //$NON-NLS-2$
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        sut.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 443)); //$NON-NLS-1$
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUserPrincipal().getName(), is(equalTo("u1"))); //$NON-NLS-1$
        assertThat(credentials.getPassword(), is(equalTo("p1"))); //$NON-NLS-1$
    }

    @Test
    public void setCredentialsAuthenticateを呼び出すと_引数で渡したClientCert認証情報が設定されること() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp"); //$NON-NLS-1$
        AuthInfo authInfo = new AuthInfo(AuthType.CLIENT_CERT, "u1", "p1"); //$NON-NLS-1$ //$NON-NLS-2$
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        sut.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80)); //$NON-NLS-1$
        assertThat(credentials, is(nullValue()));
    }

    @Test
    public void setCredentialsAuthenticateに渡す認証情報がnullの場合_認証情報が設定されないこと() {

        // Setup
        ApacheHttpService sut = new ApacheHttpService();

        URI uri = URIUtil.toURI("http://www.ambrosoli.jp"); //$NON-NLS-1$
        AuthInfo authInfo = null;
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Exercise
        sut.setCredentialsAuthenticate(uri, authInfo, provider);

        // Verify
        Credentials credentials = provider.getCredentials(new AuthScope("www.ambrosoli.jp", 80)); //$NON-NLS-1$
        assertThat(credentials, is(nullValue()));
    }

}
