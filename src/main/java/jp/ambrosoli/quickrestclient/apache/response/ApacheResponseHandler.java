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
package jp.ambrosoli.quickrestclient.apache.response;

import java.io.IOException;
import java.io.InputStream;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;

/**
 * Apache HttpComponents用のHttpResponseを生成するレスポンスハンドラです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class ApacheResponseHandler implements ResponseHandler<HttpResponse> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.http.client.ResponseHandler#handleResponse(org.apache.http
     * .HttpResponse)
     */
    public HttpResponse handleResponse(final org.apache.http.HttpResponse response) {
        try {
            return new ApacheHttpResponse(response);
        } finally {
            if (response != null) {
                this.consumeEntity(response.getEntity());
            }
        }
    }

    /**
     * エンティティをconsumeします。
     * 
     * @param entity
     *            エンティティ
     */
    public void consumeEntity(final HttpEntity entity) {
        try {
            if (entity == null) {
                return;
            }
            if (entity.isStreaming()) {
                InputStream instream = entity.getContent();
                if (instream != null) {
                    instream.close();
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
