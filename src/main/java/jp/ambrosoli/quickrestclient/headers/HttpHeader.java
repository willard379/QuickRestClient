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

import jp.ambrosoli.quickrestclient.params.NameValueObject;

/**
 * HTTPヘッダの情報を保持するクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class HttpHeader extends NameValueObject {

    /**
     * HttpHeaderを生成します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     */
    public HttpHeader(final String name, final String value) {
        super(name, value);
    }

}
