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
package jp.ambrosoli.quickrestclient.ahc.response;

import java.util.List;

import jp.ambrosoli.quickrestclient.ahc.util.AHCUtil;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;
import jp.ambrosoli.quickrestclient.response.AbstractHttpResponse;
import jp.ambrosoli.quickrestclient.response.ByteArrayResponseContent;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

/**
 * Apache HttpComponentsのHTTPレスポンスを扱うクラスです。
 *
 * @author willard379
 * @since 0.1.0
 */
public class AHCHttpResponse extends AbstractHttpResponse {

    /** Apache HttpComponentsのレスポンス */
    private org.apache.http.HttpResponse response;

    /**
     * AHCHttpResponseを生成します。
     * 
     * @param response
     *            AHCHttpResponse
     */
    public AHCHttpResponse(final org.apache.http.HttpResponse response) {
        super();
        if (response == null) {
            throw new NullPointerException();
        }
        this.response = response;
        HttpEntity entity = response.getEntity();
        byte[] data = AHCUtil.toByteArray(entity);
        super.setContent(new ByteArrayResponseContent(data));
    }

    public List<HttpHeader> getAllHeaders() {
        return AHCUtil.convertHttpHeaders(this.response.getAllHeaders());
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
        return AHCUtil.convertHttpHeader(this.response.getFirstHeader(headerName));
    }

    public List<HttpHeader> getHeaders(final String headerName) {
        return AHCUtil.convertHttpHeaders(this.response.getHeaders(headerName));
    }

    public int getStatusCode() {
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine == null) {
            return -1;
        }
        return statusLine.getStatusCode();
    }

}
