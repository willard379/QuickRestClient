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
import java.io.OutputStream;
import java.util.List;

import jp.ambrosoli.quickrestclient.headers.HttpHeader;

/**
 * HTTPレスポンスの情報にアクセスするためのインタフェースです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public interface HttpResponse {

    /**
     * HTTPステータスコードを返します。
     * 
     * @return HTTPステータスコード
     */
    int getStatusCode();

    /**
     * HTTP通信に成功した場合にtrueを返します。<br />
     * HTTPステータスコードが200 もしくは 201の場合に成功とみなします。
     * 
     * @return HTTP通信に成功した場合true,その他の場合false
     */
    boolean isSuccess();

    /**
     * レスポンスの内容をテキストで返します。
     * 
     * @return レスポンスデータ
     */
    String getAsString();

    /**
     * レスポンスの内容をテキストで返します。
     * 
     * @param charset
     *            文字コード
     * @return レスポンスデータ
     */
    String getAsString(String charset);

    /**
     * レスポンスの内容を入力ストリームで返します。
     * 
     * @return レスポンスデータ
     */
    InputStream getAsInputStream();

    /**
     * レスポンスの内容をバイト配列で返します。
     * 
     * @return レスポンスデータ
     */
    byte[] getAsByteArray();

    /**
     * すべてのHTTPヘッダのリストを返します。何もない場合は空のリストを返します。
     * 
     * @return すべてのHTTPヘッダ
     */
    List<HttpHeader> getAllHeaders();

    /**
     * headerNameで指定されたHTTPヘッダのリストを返します。何もない場合は空のリストを返します。
     * 
     * @param headerName
     *            ヘッダ名
     * @return HTTPヘッダのリスト
     */
    List<HttpHeader> getHeaders(final String headerName);

    /**
     * headerNameで指定されたHTTPヘッダ（最初の1件）を返します。何もない場合はnullを返します。
     * 
     * @param headerName
     *            ヘッダ名
     * @return HTTPヘッダ
     */
    HttpHeader getHeader(final String headerName);

    /**
     * Content-Typeの返します。
     * 
     * @return Content-Typeの値
     */
    String getContentType();

    /**
     * Content-Lengthを返します。
     * 
     * @return Content-Lengthの値
     */
    long getContentLength();

    /**
     * レスポンスの内容を{@link OutputStream}に書き出します。
     * 
     * @param output
     *            出力ストリーム
     */
    void writeTo(OutputStream output);
}
