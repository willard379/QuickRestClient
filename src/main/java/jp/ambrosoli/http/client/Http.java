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
package jp.ambrosoli.http.client;

import jp.ambrosoli.http.client.request.HttpRequestBuilder;

/**
 * HTTP通信処理のユーザインタフェースを提供するクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 * 
 */
public final class Http {

    /** HTTPメソッド「GET」を表す定数です。 */
    public static final String GET = "GET";

    /** HTTPメソッド「POST」を表す定数です。 */
    public static final String POST = "POST";

    /** HTTPメソッド「PUT」を表す定数です。 */
    public static final String PUT = "PUT";

    /** HTTPメソッド「DELETE」を表す定数です。 */
    public static final String DELETE = "DELETE";

    /** HTTPメソッド「HEAD」を表す定数です。 */
    public static final String HEAD = "HEAD";

    /** HTTPメソッド「OPTIONS」を表す定数です。 */
    public static final String OPTIONS = "OPTIONS";

    /** プロトコルバージョン 「HTTP/1.0」を表す定数です。 */
    public static final String HTTP_1_0 = "HTTP/1.0";

    /** プロトコルバージョン 「HTTP/1.1」を表す定数です。 */
    public static final String HTTP_1_1 = "HTTP/1.1";

    /**
     * URLを設定します。
     * 
     * @param url
     *            URL
     * @return HttpBuilderオブジェクト
     */
    public static HttpRequestBuilder url(final String url) {
        return new HttpRequestBuilder(url);
    }
}
