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

import org.apache.http.annotation.Immutable;

/**
 * HTTPプロキシの認証情報を保持するクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
@Immutable
public class ProxyInfo {

    /** ホスト名 */
    private String host;

    /** ポート */
    private int port;

    /**
     * ProxyInfoインスタンスを生成します。
     * 
     * @param host
     *            ホスト名
     * @param port
     *            ポート
     */
    public ProxyInfo(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }

    /**
     * ホスト名を返します。
     * 
     * @return ホスト名
     */
    public String getHost() {
        return this.host;
    }

    /**
     * ポートを返します。
     * 
     * @return ポート
     */
    public int getPort() {
        return this.port;
    }

}
