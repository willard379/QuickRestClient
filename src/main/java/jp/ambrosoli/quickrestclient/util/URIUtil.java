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
package jp.ambrosoli.quickrestclient.util;

import java.net.URI;

/**
 * URLに関するユーティリティクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public final class URIUtil {

    /** HTTPスキーム */
    public static final String SCHEME_NAME_HTTP = "http";

    /** HTTPSスキーム */
    public static final String SCHEME_NAME_HTTPS = "https";

    /** HTTPのポート番号 */
    public static final int PORT_HTTP = 80;

    /** HTTPSのポート番号 */
    public static final int PORT_SSL = 443;

    /** 不明なポート番号 */
    private static final int PORT_UNKNOWN = -1;

    /**
     * URLがSSL(https://...)かどうかを判定します。
     * 
     * @param url
     *            URL
     * @return SSLの場合true、その他の場合false
     */
    public static boolean isSSL(final String url) {
        if (url != null) {
            return url.startsWith(SCHEME_NAME_HTTPS + ":");
        }
        return false;
    }

    /**
     * URLがSSL(https://...)かどうかを判定します。
     * 
     * @param url
     *            URI
     * @return SSLの場合true、その他の場合false
     */
    public static boolean isSSL(final URI url) {
        if (url != null && url.getScheme() != null) {
            return url.getScheme().equals(SCHEME_NAME_HTTPS);
        }
        return false;
    }

    /**
     * 文字列からURIオブジェクトに変換して返します。
     * 
     * @param url
     *            文字列のURL
     * @return URIオブジェクト
     */
    public static URI toURI(final String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        return URI.create(url);
    }

    /**
     * URLがPlain（http://...）かどうかを判定します。
     * 
     * @param uri
     *            URI
     * @return Plainの場合true、その他の場合false
     */
    public static boolean isPlain(final URI uri) {
        if (uri != null && uri.getScheme() != null) {
            return uri.getScheme().equals(SCHEME_NAME_HTTP);
        }
        return false;
    }

    /**
     * URLがPlain（http://...）かどうかを判定します。
     * 
     * @param url
     *            URL
     * @return Plainの場合true、その他の場合false
     */
    public static boolean isPlain(final String url) {
        if (url != null) {
            return url.startsWith(SCHEME_NAME_HTTP + ":");
        }
        return false;
    }

    /**
     * ポートを返します。ポートが未定義の場合はスキームから判断して80(http)か443(https)を返します。
     * ポートが未定義でスキームがhttp/httpsでない場合は-1を返します。
     * 
     * @param uri
     *            URL
     * @return このURLのポート。取得できない場合-1
     */
    public static int getPort(final URI uri) {
        if (uri == null) {
            return PORT_UNKNOWN;
        }

        int port = uri.getPort();
        if (port != PORT_UNKNOWN) {
            return port;
        }

        if (isPlain(uri)) {
            return PORT_HTTP;
        } else if (isSSL(uri)) {
            return PORT_SSL;
        }

        return PORT_UNKNOWN;
    }

    /**
     * クエリストリングを追加した新しいURIを生成して返します。
     * 
     * @param uri
     *            URI
     * @param queryString
     *            クエリストリング
     * @return　クエリストリングを追加した新しいURI
     */
    public static URI addQueryString(final URI uri, final String queryString) {
        if (queryString == null) {
            return uri;
        }
        if (uri == null) {
            return null;
        }
        return uri.resolve(uri.toString() + queryString);
    }
}
