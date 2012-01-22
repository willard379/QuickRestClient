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
package jp.ambrosoli.quickrestclient.apache.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.response.AbstractHttpResponse;
import jp.ambrosoli.quickrestclient.response.ByteArrayResponseContent;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

/**
 * Apache HttpComponentsのHTTPレスポンスを扱うクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 */
public class ApacheHttpResponse extends AbstractHttpResponse {

    /** Apache HttpComponentsのレスポンス */
    private org.apache.http.HttpResponse response;

    /**
     * ApacheHttpResponseを生成します。
     * 
     * @param response
     *            ApacheHttpResponse
     */
    public ApacheHttpResponse(final org.apache.http.HttpResponse response) {
        super();
        if (response == null) {
            throw new NullPointerException();
        }
        this.response = response;
        HttpEntity entity = response.getEntity();
        byte[] data = this.toByteArray(entity);
        super.setContent(new ByteArrayResponseContent(data));
    }

    public List<HttpHeader> getAllHeaders() {
        return this.convertHttpHeaders(this.response.getAllHeaders());
    }

    public String getContentType() {
        HttpEntity entity = this.response.getEntity();
        if (entity == null) {
            return null;
        }
        Header contentType = entity.getContentType();
        if (contentType == null) {
            return null;
        }
        return contentType.getValue();
    }

    public long getContentLength() {
        HttpEntity entity = this.response.getEntity();
        if (entity == null) {
            return 0;
        }
        return entity.getContentLength();
    }

    public HttpHeader getHeader(final String headerName) {
        return this.convertHttpHeader(this.response.getFirstHeader(headerName));
    }

    public List<HttpHeader> getHeaders(final String headerName) {
        return this.convertHttpHeaders(this.response.getHeaders(headerName));
    }

    public int getStatusCode() {
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine == null) {
            return -1;
        }
        return statusLine.getStatusCode();
    }

    /**
     * {@link HttpEntity}からbyte配列を取得して返します。
     * 
     * @param entity
     *            HTTPエンティティ
     * @return 取得したbyte配列
     */
    public byte[] toByteArray(final HttpEntity entity) {
        if (entity == null) {
            return null;
        }

        try {
            return EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            throw new IORuntimeException();
        }
    }

    /**
     * {@link Header}の配列を{@link HttpHeader}のリストに変換して返します。
     * 
     * @param headers
     *            {@link Header}の配列
     * @return {@link HttpHeader}のリスト
     */
    public List<HttpHeader> convertHttpHeaders(final Header[] headers) {
        if (headers == null) {
            return null;
        }
        List<HttpHeader> headerList = new ArrayList<HttpHeader>();
        for (Header header : headers) {
            headerList.add(this.convertHttpHeader(header));
        }
        return headerList;
    }

    /**
     * {@link Header}を{@link HttpHeader}に変換して返します。
     * 
     * @param header
     *            {@link Header}オブジェクト
     * @return {@link HttpHeader}オブジェクト
     */
    public HttpHeader convertHttpHeader(final Header header) {
        if (header == null) {
            return null;
        }
        return new HttpHeader(header.getName(), header.getValue());
    }
}
