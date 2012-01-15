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

import java.util.Map;

import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.enums.HttpMethod;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.ProxyInfo;
import jp.ambrosoli.quickrestclient.params.RequestParams;
import jp.ambrosoli.quickrestclient.response.HttpResponse;
import jp.ambrosoli.quickrestclient.service.HttpService;
import jp.ambrosoli.quickrestclient.service.HttpServiceFactory;
import jp.ambrosoli.quickrestclient.util.StringUtil;
import jp.ambrosoli.quickrestclient.util.URIUtil;

import org.apache.http.protocol.HTTP;

/**
 * HTTPリクエストの内容を構築するビルダークラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class HttpRequestBuilder {

    /** HTTPリクエストの内容 */
    protected HttpRequest request;

    /**
     * HttpBuilderを生成します。
     * 
     * @param url
     *            URL
     */
    public HttpRequestBuilder(final String url) {
        if (StringUtil.isEmpty(url)) {
            throw new IllegalArgumentException("URL may not be null or blank.");
        }
        this.request = new HttpRequest(URIUtil.toURI(url));
    }

    /**
     * HTTPリクエストを送信し、通信結果を返します。
     * 
     * @return 通信結果
     */
    public HttpResponse execute() {
        HttpService httpService = HttpServiceFactory.getFactory().create();
        return httpService.execute(this.request);
    }

    /**
     * HTTPメソッドを設定します。
     * 
     * @param method
     *            HTTPメソッド
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder method(final String method) {
        if (method == null) {
            throw new NullPointerException("method may not to be null.");
        }
        this.request.setMethod(HttpMethod.valueOf(method.toUpperCase()));
        return this;
    }

    /**
     * プロキシを設定します。
     * 
     * @param hostname
     *            プロキシサーバのホスト名
     * @param port
     *            プロキシサーバのポート
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder proxy(final String hostname, final int port) {
        if (hostname == null) {
            throw new IllegalArgumentException("hostname may not be null");
        }
        ProxyInfo proxyInfo = new ProxyInfo(hostname, port);
        this.request.setProxyInfo(proxyInfo);
        return this;
    }

    /**
     * HTTPプロトコルバージョンを設定します。
     * 
     * @param protocolVersion
     *            HTTPプロトコルバージョン(例： "HTTP/1.1")
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder protocol(final String protocolVersion) {
        this.request.setProtocol(protocolVersion);
        return this;
    }

    /**
     * User-Agentを設定します。
     * 
     * @param userAgent
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder userAgent(final String userAgent) {
        if (StringUtil.isNotEmpty(userAgent)) {
            this.request.addHeader(new HttpHeader(HTTP.USER_AGENT, userAgent));
        }
        return this;
    }

    /**
     * 送信するパラメータを設定します。
     * 
     * @param params
     *            Operations.add()で生成された名前と値のセット
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder params(final NameValueObject... params) {
        RequestParams requestParams = new RequestParams(NameValueObject.asList(params));
        this.request.setParams(requestParams);
        return this;
    }

    /**
     * 送信するパラメータを設定します。
     * 
     * @param paramMap
     *            Mapオブジェクト
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder params(final Map<String, String> params) {
        RequestParams requestParams = new RequestParams(NameValueObject.asList(params));
        this.request.setParams(requestParams);
        return this;
    }

    /**
     * 文字コードを設定します。
     * 
     * @param charset
     *            文字コード
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder charset(final String charset) {
        this.request.setCharset(charset);
        return this;
    }

    /**
     * HTTPヘッダーを設定します。
     * 
     * @param headers
     *            設定するHTTPヘッダー
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder headers(final NameValueObject... headers) {
        this.request.addHeaders(NameValueObject.asList(headers));
        return this;
    }

    /**
     * HTTPヘッダーを設定します。
     * 
     * @param headerMap
     *            設定するHTTPヘッダー
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder headers(final Map<String, String> headers) {
        this.request.addHeaders(NameValueObject.asList(headers));
        return this;
    }

    /**
     * タイムアウト（SO_TIMEOUT）をミリ秒単位で設定します。
     * 
     * @param timeout
     *            タイムアウト（ミリ秒）
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder timeout(final int timeout) {
        this.request.setTimeout(timeout);
        return this;
    }

    /**
     * ACCEPTヘッダを設定します。
     * 
     * @param mediaType
     *            メディアタイプ
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder accept(final String... mediaTypes) {
        for (String type : mediaTypes) {
            if (StringUtil.isNotEmpty(type)) {
                HttpHeader header = new HttpHeader("Accept", type);
                this.request.addHeader(header);
            }
        }
        return this;
    }

    /**
     * Digest,BasicなどのHTTP認証の情報を設定します.
     * 
     * @param authInfo
     *            Opertaionsクラスのメソッドを使って生成された認証情報
     * @return HttpBuilderオブジェクト
     */
    public HttpRequestBuilder auth(final AuthInfo authInfo) {
        if (authInfo == null) {

        }
        this.request.authInfo = authInfo;
        return this;
    }

    /**
     * Digest,BasicなどのHTTP認証の情報を設定します.
     * 
     * @param authType
     *            認証の種類(設定可能な値は{@link AuthType}を参照)
     * @param user
     *            ユーザ
     * @param password
     *            パスワード
     * @return
     */
    public HttpRequestBuilder auth(final String authType, final String user, final String password) {
        this.request.authInfo = new AuthInfo(AuthType.valueOf(authType), user, password);
        return this;
    }

    /**
     * Digest,BasicなどのHTTP認証の情報を設定します.
     * 
     * @param type
     *            認証の種類(設定可能な値は{@link AuthType}を参照)
     * @param user
     *            ユーザ
     * @param password
     *            パスワード
     * @return
     */
    public HttpRequestBuilder auth(final AuthType type, final String user, final String password) {
        this.request.authInfo = new AuthInfo(type, user, password);
        return this;
    }

}
