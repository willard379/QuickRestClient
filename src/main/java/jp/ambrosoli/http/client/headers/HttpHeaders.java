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
package jp.ambrosoli.http.client.headers;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.http.client.params.NameValueObject;

/**
 * HTTPヘッダーパラメータを扱うクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
public class HttpHeaders {

    /** ヘッダーパラメータ */
    protected List<NameValueObject> headers;

    /**
     * ヘッダーパラメータがnull空か判定します。
     * 
     * @return nullまたは空の場合true、そうでない場合false
     */
    public boolean isEmpty() {
        return this.headers == null || this.headers.isEmpty();
    }

    /**
     * {@link HttpHeaderBuilder<T>}を通じて、それぞれのクライアントに合ったHTTPヘッダを返します。
     * 
     * @param <T>
     * @param builder
     *            ビルダー
     * @return HTTPヘッダ
     */
    public <T> T getConformedHeaders(final HttpHeaderBuilder<T> builder) {
        return builder.createConformedHeaders(this.headers);
    }

    /**
     * ヘッダー名と値のペアのリストを返します。
     * 
     * @return　ヘッダー名と値のペアのリスト
     */
    public List<NameValueObject> getHeaders() {
        return this.headers;
    }

    /**
     * ヘッダーを追加します。
     * 
     * @param nameValueObject
     *            追加するヘッダー情報
     */
    public void addHeader(final NameValueObject nameValueObject) {
        if (this.headers == null) {
            this.headers = new ArrayList<NameValueObject>();
        }
        if (nameValueObject != null) {
            this.headers.add(nameValueObject);
        }
    }

    /**
     * 複数のヘッダーを追加します。
     * 
     * @param headers
     *            ヘッダー
     */
    public void addHeaders(final List<NameValueObject> headers) {
        if (this.headers == null) {
            this.headers = new ArrayList<NameValueObject>();
        }
        if (headers != null) {
            this.headers.addAll(headers);
        }
    }

    /**
     * ヘッダーを全削除します。
     */
    public void clear() {
        if (this.headers != null) {
            this.headers.clear();
        }
    }

}
