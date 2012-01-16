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
/**
 * 
 */
package jp.ambrosoli.quickrestclient.ahc.response;

import jp.ambrosoli.quickrestclient.ahc.util.AHCUtil;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.apache.http.client.ResponseHandler;

/**
 * Apache HttpComponents用のHttpResponseを生成するレスポンスハンドラです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class AHCResponseHandler implements ResponseHandler<HttpResponse> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.http.client.ResponseHandler#handleResponse(org.apache.http
     * .HttpResponse)
     */
    public HttpResponse handleResponse(final org.apache.http.HttpResponse response) {
        try {
            return new AHCHttpResponse(response);
        } finally {
            if (response != null) {
                AHCUtil.consumeEntity(response.getEntity());
            }
        }
    }
}