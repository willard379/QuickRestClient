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

import java.util.List;

import jp.ambrosoli.quickrestclient.util.StringUtil;

/**
 * リクエストパラメータを扱うクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class RequestParams {

    /** リクエストパラメータ */
    protected List<NameValueObject> params;

    /**
     * RequestParamsインスタンスを生成します。
     * 
     * @param params
     *            リクエストパラメータ
     */
    public RequestParams(final List<NameValueObject> params) {
        this.params = params;
    }

    /**
     * リクエストパラメータがnull空か判定します。
     * 
     * @return nullまたは空の場合true、そうでない場合false
     */
    public boolean isEmpty() {
        return this.params == null || this.params.isEmpty();
    }

    /**
     * {@link EnclosingParamBuilder<T>}を通じて、それぞれのクライアントに合ったリクエストパラメータを返します。
     * 
     * @param <T>
     * @param builder
     *            ビルダー
     * @return リクエストパラメータ
     */
    public <T> T getConformedParams(final RequestParamBuilder<T> builder) {
        return this.getConformedParams(builder, StringUtil.DEFAULT_ENCODING);
    }

    /**
     * {@link EnclosingParamBuilder<T>}を通じて、それぞれのクライアントに合ったリクエストパラメータを返します。
     * 
     * @param <T>
     * @param builder
     *            ビルダー
     * @param encoding
     *            文字コード
     * @return リクエストパラメータ
     */
    public <T> T getConformedParams(final RequestParamBuilder<T> builder, final String encoding) {
        return builder.createConformedParams(this.params, encoding);
    }

    /**
     * パラメータ名と値のペアのリストを返します。
     * 
     * @return パラメータ名と値のペア
     */
    public List<NameValueObject> getParams() {
        return this.params;
    }

}
