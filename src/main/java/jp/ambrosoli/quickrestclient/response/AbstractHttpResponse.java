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
import java.net.HttpURLConnection;
import java.util.List;

import jp.ambrosoli.quickrestclient.headers.HttpHeader;

/**
 * HTTPレスポンスを扱う抽象クラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public abstract class AbstractHttpResponse implements HttpResponse {

    /** HTTPレスポンスの内容 */
    protected ResponseContent content;

    /**
     * AbstractHttpResponseを生成します。
     */
    protected AbstractHttpResponse() {
        super();
    }

    /**
     * AbstractHttpResponseを生成します。
     * 
     * @param content
     *            レスポンスの内容
     */
    protected AbstractHttpResponse(final ResponseContent content) {
        this.content = content;
    }

    /**
     * レスポンスの内容を設定します。
     * 
     * @param content
     *            レスポンスの内容
     */
    public void setContent(final ResponseContent content) {
        this.content = content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.ambrosoli.http.client.response.HttpResponse#is200_OK()
     */
    public boolean isSuccess() {
        int sc = this.getStatusCode();
        return sc == HttpURLConnection.HTTP_OK || sc == HttpURLConnection.HTTP_CREATED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.ambrosoli.http.client.response.HttpResponse#getAsByteArray()
     */
    public byte[] getAsByteArray() {
        if (this.content == null) {
            return null;
        }
        return this.content.getAsByteArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.ambrosoli.http.client.response.HttpResponse#getAsInputStream()
     */
    public InputStream getAsInputStream() {
        if (this.content == null) {
            return null;
        }
        return this.content.getAsInputStream();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.ambrosoli.http.client.response.HttpResponse#getAsString()
     */
    public String getAsString() {
        if (this.content == null) {
            return null;
        }
        return this.content.getAsString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jp.ambrosoli.http.client.response.HttpResponse#getAsString(java.lang.
     * String)
     */
    public String getAsString(final String charset) {
        if (this.content == null) {
            return null;
        }
        return this.content.getAsString(charset);
    }

    public abstract int getStatusCode();

    public abstract List<HttpHeader> getAllHeaders();

    public abstract List<HttpHeader> getHeaders(String headerName);

    public abstract HttpHeader getHeader(String headerName);

    public abstract String getContentType();

    public abstract long getContentLength();

}
