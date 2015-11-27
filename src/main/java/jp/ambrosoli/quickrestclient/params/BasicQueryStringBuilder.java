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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.UnsupportedEncodingRuntimeException;
import jp.ambrosoli.quickrestclient.util.StringUtil;

/**
 * クエリストリングを生成します。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class BasicQueryStringBuilder implements RequestParamBuilder<String> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * jp.ambrosoli.http.client.params.RequestParamBuilder#createEnclosingParams
     * (jp.ambrosoli.http.client.content.NameValueObject[], java.lang.String)
     */
    public String createConformedParams(final List<NameValueObject> params, final String encoding) {
        if (params == null || params.isEmpty()) {
            return ""; //$NON-NLS-1$
        }

        String enc = StringUtil.isNotEmpty(encoding) ? encoding : StringUtil.DEFAULT_ENCODING;

        try {
            StringBuilder sb = new StringBuilder(100);
            for (NameValueObject param : params) {
                if (param == null) {
                    continue;
                }

                if (StringUtil.isEmpty(sb)) {
                    sb.append("?"); //$NON-NLS-1$
                } else {
                    sb.append("&"); //$NON-NLS-1$
                }

                sb.append(URLEncoder.encode(param.getName(), enc));
                sb.append("="); //$NON-NLS-1$
                sb.append(URLEncoder.encode(param.getValue(), enc));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingRuntimeException(e);
        }

    }

}
