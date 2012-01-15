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
package jp.ambrosoli.quickrestclient.params;

import jp.ambrosoli.quickrestclient.enums.AuthType;

/**
 * HTTP認証の情報を保持するクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
public class AuthInfo {

    /** 認証タイプ */
    public AuthType type;

    /** ユーザ名 */
    public String username;

    /** パスワード */
    public String password;

    /**
     * AuthInfoを生成します。
     * 
     * @param type
     *            認証タイプ
     * @param username
     *            ユーザ名
     * @param password
     *            パラメータ
     */
    public AuthInfo(final AuthType type, final String username, final String password) {
        this.type = type;
        this.username = username;
        this.password = password;
    }

    /**
     * AuthInfoを生成します。
     */
    public AuthInfo() {
        super();
    }

}
