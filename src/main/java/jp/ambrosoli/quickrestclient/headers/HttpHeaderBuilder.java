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
package jp.ambrosoli.quickrestclient.headers;

import java.util.List;

import jp.ambrosoli.quickrestclient.params.NameValueObject;

/**
 * それぞれのクライアントに合わせて、HTTPヘッダーを加工するインタフェースです。
 * 
 * @author willard379
 * @since 0.0.1
 * 
 * @param <T>
 *            呼び出し元へ返すデータ型
 */
public interface HttpHeaderBuilder<T> {

    /**
     * それぞれのクライアントに合ったHTTPヘッダーを構築します。
     * 
     * @param <T>
     * @param headers
     *            ヘッダー
     */
    T createConformedHeaders(final List<NameValueObject> headers);

}
