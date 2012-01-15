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

import java.net.URI;
import java.util.List;

import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.headers.HttpHeaders;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.params.RequestParams;
import jp.ambrosoli.quickrestclient.util.StringUtil;

/**
 * HTTPリクエストの内容を保持するクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 * 
 */
public class HttpRequest {

    /** デフォルトのタイムアウト時間 */
    protected static final int DEFAULT_TIMEOUT = 5000;

    /** リクエストURI */
    protected URI uri;

    /** HTTPメソッド */
    protected HttpMethod method = HttpMethod.GET;

    /** リクエストパラメータ */
    protected RequestParams params;

    /** HTTPヘッダー */
    protected HttpHeaders headers;

    /** プロキシーの情報 */
    protected ProxyInfo proxyInfo;

    /** HTTP認証の情報 */
    public AuthInfo authInfo;

    /** プロトコルバージョン */
    protected String protocol;

    /** タイムアウト（ミリ秒） */
    protected int timeout = DEFAULT_TIMEOUT;

    /** 文字コード */
    protected String charset = StringUtil.DEFAULT_ENCODING;

    /**
     * HttpRequestを生成します。
     * 
     * @param uri
     *            リクエストURI
     */
    public HttpRequest(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI mey not to be null");
        }
        this.uri = uri;
    }

    /**
     * HTTPメソッドをかえします。
     * 
     * @return HTTPメソッド
     */
    public HttpMethod getMethod() {
        return this.method;
    }

    /**
     * HTTPメソッドを設定します。
     * 
     * @param method
     *            HTTPメソッド
     */
    public void setMethod(final HttpMethod method) {
        this.method = method;
    }

    /**
     * リクエストパラメータを返します。
     * 
     * @return リクエストパラメータ
     */
    public RequestParams getParams() {
        return this.params;
    }

    /**
     * リクエストパラメータを設定します。
     * 
     * @param params
     *            リクエストパラメータ
     */
    public void setParams(final RequestParams params) {
        this.params = params;
    }

    /**
     * HTTPヘッダーを返します。
     * 
     * @return HTTPヘッダー
     */
    public HttpHeaders getHeaders() {
        return this.headers;
    }

    /**
     * HTTPヘッダーを追加します。
     * 
     * @param headers
     *            HTTPヘッダー
     */
    public void addHeaders(final List<NameValueObject> headers) {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
        }
        this.headers.addHeaders(headers);
    }

    /**
     * HTTPヘッダーを追加します。
     * 
     * @param headers
     *            HTTPヘッダー
     */
    public void addHeader(final NameValueObject header) {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
        }
        this.headers.addHeader(header);
    }

    /**
     * プロキシーの情報を返します。
     * 
     * @return プロキシーの情報
     */
    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }

    /**
     * プロキシーの情報を設定します。
     * 
     * @param proxyInfo
     *            プロキシーの情報
     */
    public void setProxyInfo(final ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    /**
     * HTTP認証の情報を返します。
     * 
     * @return 認証情報
     */
    public AuthInfo getAuthInfo() {
        return this.authInfo;
    }

    /**
     * HTTP認証の情報を設定します。
     * 
     * @param authInfo
     *            認証情報
     */
    public void setAuthInfo(final AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    /**
     * リクエストURIを返します。
     * 
     * @return リクエストURI
     */
    public URI getUri() {
        return this.uri;
    }

    /**
     * 文字コードを返します。
     * 
     * @return 文字コード
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * 文字コードを設定します。
     * 
     * @param charset
     *            文字コード
     */
    public void setCharset(final String charset) {
        this.charset = charset;
    }

    /**
     * プロトコルバージョンを返します。
     * 
     * @return プロトコルバージョン
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * プロトコルバージョンを設定します。
     * 
     * @param protocol
     *            プロトコルバージョン
     */
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    /**
     * タイムアウト時間を返します。
     * 
     * @return タイムアウト時間
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * タイムアウト時間を設定します。
     * 
     * @param timeout
     *            タイムアウト時間
     */
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

}
