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
/**
 * 
 */
package jp.ambrosoli.quickrestclient.ahc.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;

import jp.ambrosoli.quickrestclient.Http;
import jp.ambrosoli.quickrestclient.ahc.headers.AHCHeaderBuilder;
import jp.ambrosoli.quickrestclient.ahc.params.AHCEnclosingParamBuilder;
import jp.ambrosoli.quickrestclient.ahc.response.AHCResponseHandler;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.exception.SocketTimeoutRuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.BasicQueryStringBuilder;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.params.RequestParams;
import jp.ambrosoli.quickrestclient.request.HttpRequest;
import jp.ambrosoli.quickrestclient.response.HttpResponse;
import jp.ambrosoli.quickrestclient.service.HttpService;
import jp.ambrosoli.quickrestclient.util.URIUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Apache HttpComponentsを使用したRESTエンドポイントのサービス呼び出しを行うクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
public class AHCHttpService implements HttpService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * jp.ambrosoli.http.client.service.HttpService#execute(jp.ambrosoli.http
     * .client.request.HttpRequest)
     */
    public HttpResponse execute(final HttpRequest request) {
        HttpParams httpParams = this.createHttpParams();
        this.setProtocolVersion(httpParams, request.getProtocol());
        this.setTimeout(httpParams, request.getTimeout());
        this.setProxy(httpParams, request.getProxyInfo());
        this.setCharset(httpParams, request.getCharset());

        URI uri = request.getUri();
        HttpUriRequest httpUriRequest = this.createHttpUriRequest(uri, request.getMethod(),
                request.getParams(), request.getCharset());
        this.setHeaders(httpUriRequest, request.getHeaders());

        SchemeRegistry schreg = this.createSchemeRegistry(uri);
        ClientConnectionManager conman = this.createClientConnectionManager(httpParams, schreg);
        AbstractHttpClient client = this.createHttpClient(conman, httpParams);
        this.setCredentialsAuthenticate(uri, request.getAuthInfo(), client.getCredentialsProvider());

        try {
            return client.execute(httpUriRequest, new AHCResponseHandler());
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutRuntimeException(e);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            conman.shutdown();
        }

    }

    /**
     * {@link SchemeRegistry}を生成します。
     * 
     * @param uri
     *            リクエストURI
     * @return 生成した{@link SchemeRegistry}のインスタンス
     */
    protected SchemeRegistry createSchemeRegistry(final URI uri) {
        SchemeRegistry schreg = new SchemeRegistry();
        SocketFactory factory = this.getSocketFactory(uri);
        int port = URIUtil.getPort(uri);
        Scheme scheme = new Scheme(uri.getScheme(), factory, port);
        schreg.register(scheme);
        return schreg;
    }

    /**
     * スキームに応じてソケットファクトリを生成して返します。
     * 
     * @param uri
     *            URI
     * @return ソケットファクトリ
     */
    protected SocketFactory getSocketFactory(final URI uri) {
        if (uri == null) {
            throw new IllegalStateException("URL is null.");
        }

        SocketFactory factory;
        if (URIUtil.isPlain(uri)) {
            factory = PlainSocketFactory.getSocketFactory();
        } else if (URIUtil.isSSL(uri)) {
            factory = SSLSocketFactory.getSocketFactory();
        } else {
            throw new IllegalArgumentException("invalid scheme.");
        }

        return factory;
    }

    /**
     * {@link HttpParams}を生成して返します。
     * 
     * @return HttpParams実装クラスのインスタンス
     */
    protected HttpParams createHttpParams() {
        return new BasicHttpParams();
    }

    /**
     * {@link ClientConnectionManager}を生成して返します。
     * 
     * @param schreg
     *            {@link SchemeRegistry}のインスタンス
     * @param params
     *            {@link HttpParams}のインスタンス
     * @return 生成した{@link ClientConnectionManager}のインスタンス
     */
    protected ClientConnectionManager createClientConnectionManager(final HttpParams params,
            final SchemeRegistry schreg) {
        return new ThreadSafeClientConnManager(params, schreg);
    }

    /**
     * {@link HttpClient}を生成して返します。
     * 
     * @param conman
     *            コネクションマネージャ
     * @param params
     *            HttpParams
     * @return {@link HttpClient}のインスタンス
     */
    protected AbstractHttpClient createHttpClient(final ClientConnectionManager conman,
            final HttpParams params) {
        DefaultHttpClient client = new DefaultHttpClient(conman, params);
        return client;
    }

    /**
     * {@link HttpUriRequest}を生成して返します。 {@link HttpUriRequest}
     * にはリクエストパラメータがセットされます。
     * 
     * @param uri
     *            リクエストURI
     * @param method
     *            HTTPメソッド
     * @param params
     *            リクエストパラメータ
     * @param encoding
     *            文字コード
     * @return {@link HttpRequestBase}インスタンス
     */
    protected HttpUriRequest createHttpUriRequest(final URI uri, final HttpMethod method,
            final RequestParams params, final String encoding) {
        switch (method) {
        case GET:
            return new HttpGet(this.addQueryString(uri, params, encoding));
        case POST:
            HttpPost httpPost = new HttpPost(uri);
            this.setFormEntity(httpPost, params, encoding);
            return httpPost;
        case PUT:
            HttpPut httpPut = new HttpPut(uri);
            this.setFormEntity(httpPut, params, encoding);
            return httpPut;
        case DELETE:
            return new HttpDelete(this.addQueryString(uri, params, encoding));
        case HEAD:
            return new HttpHead(this.addQueryString(uri, params, encoding));
        case OPTIONS:
            return new HttpOptions(this.addQueryString(uri, params, encoding));
        default:
            throw new AssertionError();
        }

    }

    /**
     * クエリストリングを追加した新しいURIを生成して返します。リクエストパラメータがない場合はURIをそのまま返します。
     * 
     * @param uri
     *            URI
     * @param params
     *            リクエストパラメータ
     * @param encoding
     *            文字コード
     * @return クエリストリングを追加した新しいURI。リクエストパラメータがない場合は引数で受け取ったURI
     */
    protected URI addQueryString(final URI uri, final RequestParams params, final String encoding) {
        if (params == null || params.isEmpty()) {
            return uri;
        }
        return URIUtil.addQueryString(uri,
                params.getConformedParams(new BasicQueryStringBuilder(), encoding));
    }

    /**
     * プロキシを設定します。
     * 
     * @param httpParams
     *            HttpParams
     * @param proxy
     *            　プロキシの情報
     */
    protected void setProxy(final HttpParams httpParams, final ProxyInfo proxy) {
        if (proxy == null) {
            return;
        }
        HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
        httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
    }

    /**
     * プロトコルバージョンを設定します。
     * 
     * @param httpParams
     *            HttpParams
     * @param protocol
     *            設定するプロトコルバージョン
     */
    protected void setProtocolVersion(final HttpParams httpParams, final String protocol) {
        if (protocol == null) {
            return;
        }
        ProtocolVersion version;
        if (protocol.equals(Http.HTTP_1_0)) {
            version = HttpVersion.HTTP_1_0;
        } else if (protocol.equals(Http.HTTP_1_1)) {
            version = HttpVersion.HTTP_1_1;
        } else {
            throw new IllegalArgumentException("Http protocol version is illegal");
        }
        HttpProtocolParams.setVersion(httpParams, version);
    }

    /**
     * タイムアウト時間を設定します。
     * 
     * @param httpParams
     *            HttpParams
     * @param timeout
     *            タイムアウト時間
     */
    protected void setTimeout(final HttpParams httpParams, final int timeout) {
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
    }

    /**
     * charsetフィールド（nullの場合は環境の標準エンコーディング）に基づいて、{@link HttpProtocolParams}
     * に送文字コードとしてを設定します。
     * 
     * @param httpUriRequest
     *            HttpUriRequestインスタンス
     */
    protected void setCharset(final HttpParams httpParams, final String charset) {
        HttpProtocolParams.setContentCharset(httpParams, charset);
        HttpProtocolParams.setHttpElementCharset(httpParams, charset);
    }

    /**
     * HTTPヘッダを設定します。
     * 
     * @param httpUriRequest
     *            HttpUriRequest
     * @param httpHeaders
     *            設定するヘッダ
     */
    protected void setHeaders(final HttpUriRequest httpUriRequest, final HttpHeaders httpHeaders) {
        if (httpHeaders == null || httpHeaders.isEmpty()) {
            return;
        }
        Header[] headers = httpHeaders.getConformedHeaders(new AHCHeaderBuilder());
        if (headers == null || headers.length == 0) {
            return;
        }
        httpUriRequest.setHeaders(headers);
    }

    /**
     * POST,PUTメソッドにリクエストパラメータを設定します。
     * 
     * @param enclosingRequest
     *            HttpEntityEnclosingRequest
     * @param requestParams
     *            リクエストパラメータ
     * @param encoding
     *            文字コード
     */
    protected void setFormEntity(final HttpEntityEnclosingRequest enclosingRequest,
            final RequestParams requestParams, final String encoding) {
        if (requestParams == null) {
            return;
        }
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity params = requestParams.getConformedParams(builder, encoding);
        enclosingRequest.setEntity(params);
    }

    /**
     * Basic/Digest認証の情報を設定します。
     * 
     * @param uri
     *            URI
     * @param authInfo
     *            認証情報
     * @param provider
     */
    protected void setCredentialsAuthenticate(final URI uri, final AuthInfo authInfo,
            final CredentialsProvider provider) {
        if (authInfo == null || provider == null) {
            return;
        }

        AuthType type = authInfo.type;
        if (type != AuthType.BASIC && type != AuthType.DIGEST) {
            return;
        }

        AuthScope authscope = new AuthScope(uri.getHost(), URIUtil.getPort(uri));
        Credentials credentials = new UsernamePasswordCredentials(authInfo.username,
                authInfo.password);
        provider.setCredentials(authscope, credentials);
    }

}
