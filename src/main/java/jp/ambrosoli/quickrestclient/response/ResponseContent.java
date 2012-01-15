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
package jp.ambrosoli.quickrestclient.response;

import java.io.InputStream;

/**
 * HTTP通信のレスポンスボディを保持するクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public interface ResponseContent {

    /**
     * レスポンスデータをバイト配列で返します。
     * 
     * @return レスポンスデータ
     */
    byte[] getAsByteArray();

    /**
     * レスポンスデータを文字列で返します。
     * 
     * @return レスポンスデータの文字列
     */
    String getAsString();

    /**
     * レスポンスデータを指定された文字コードの文字列で返します。
     * 
     * @param encoding
     *            文字コード
     * @return レスポンスデータの文字列
     */
    String getAsString(String encoding);

    /**
     * レスポンスデータを{@link InputStream}で返します。
     * 
     * @return レスポンスデータの入力ストリーム
     */
    InputStream getAsInputStream();

}
