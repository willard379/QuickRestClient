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
package jp.ambrosoli.quickrestclient.ahc.params;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.UnsupportedEncodingRuntimeException;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.RequestParamBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 * Apache HttpComponents用のPOSTリクエストパラメータを生成するクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
public class AHCEnclosingParamBuilder implements RequestParamBuilder<UrlEncodedFormEntity> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * jp.ambrosoli.http.client.params.PostParamBuilder#buildPostParams(jp.ambrosoli
     * .http.client.bean.NameValueObject[], java.lang.String)
     */
    public UrlEncodedFormEntity createConformedParams(final List<NameValueObject> params,
            final String charset) {
        try {
            List<NameValuePair> paramList = this.createNameValuePairList(params);
            return new UrlEncodedFormEntity(paramList, charset);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingRuntimeException(e);
        }
    }

    /**
     * {@link NameValueObject}のリストから{@link NameValuePair}のリストへ変換します。
     * 
     * @param params
     *            変換元のリスト
     * @return 変換後のリスト
     */
    protected List<NameValuePair> createNameValuePairList(final List<NameValueObject> params) {
        List<NameValuePair> dest = new ArrayList<NameValuePair>();
        if (params == null) {
            return dest;
        }
        for (NameValueObject nvo : params) {
            if (nvo == null) {
                continue;
            }
            dest.add(this.toNameValuePair(nvo));
        }
        return dest;
    }

    /**
     * {@link NameValueObject}から{@link BasicNameValuePair}に変換します。
     * 
     * @param nvo
     *            {@link NameValueObject}オブジェクト
     * @return {@link BasicNameValuePair}オブジェクト
     */
    protected NameValuePair toNameValuePair(final NameValueObject nvo) {
        if (nvo == null) {
            return null;
        }
        return new BasicNameValuePair(nvo.getName(), nvo.getValue());
    }
}
