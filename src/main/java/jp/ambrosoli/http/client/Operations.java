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

import jp.ambrosoli.http.client.enums.AuthType;
import jp.ambrosoli.http.client.params.AuthInfo;
import jp.ambrosoli.http.client.params.NameValueObject;
import jp.ambrosoli.http.client.request.HttpRequestBuilder;

/**
 * {@link HttpRequestBuilder}で使用するパラメータ情報を構築するための操作を定義したクラスです。
 * <p>
 * このクラスは呼び出し元からstatic importされて使われることを想定しています。
 * </p>
 * 
 * @author willard379
 * @since 0.0.1
 */
public final class Operations implements HttpConstants {

    /**
     * 名前と値のペアを生成して返します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     * @return 名前と値のペアを表すオブジェクト
     */
    public static NameValueObject add(final String name, final String value) {
        return new NameValueObject(name, value);
    }

    /**
     * Digest認証のユーザ/パスワードを設定します。
     * 
     * @param username
     *            ユーザ名
     * @param password
     *            パスワード
     * @return 認証情報
     */
    public static AuthInfo digest(final String username, final String password) {
        return new AuthInfo(AuthType.DIGEST, username, password);
    }

    /**
     * Basic認証のユーザ/パスワードを設定します。
     * 
     * @param username
     *            ユーザ名
     * @param password
     *            パスワード
     * @return 認証情報
     */
    public static AuthInfo basic(final String username, final String password) {
        return new AuthInfo(AuthType.BASIC, username, password);
    }

    /**
     * Basic認証のユーザ/パスワードを設定します。
     * 
     * @param username
     *            ユーザ名
     * @param password
     *            パスワード
     * @return 認証情報
     */
    public static AuthInfo clientCert(final String username, final String password) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
