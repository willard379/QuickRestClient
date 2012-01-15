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
package jp.ambrosoli.quickrestclient.ahc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.headers.HttpHeader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Apache HttpComponetsに関するエンティティクラスです。
 * 
 * @author willard379
 * @since 0.1.0
 * 
 */
public class AHCUtil {

    /**
     * エンティティをconsumeします。
     * 
     * @param entity
     *            エンティティ
     */
    public static void consumeEntity(final HttpEntity entity) {
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

    /**
     * {@link HttpEntity}からbyte配列を取得して返します。
     * 
     * @param entity
     *            HTTPエンティティ
     * @return 取得したbyte配列
     */
    public static byte[] toByteArray(final HttpEntity entity) {
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
    public static List<HttpHeader> convertHttpHeaders(final Header[] headers) {
        if (headers == null) {
            return null;
        }
        List<HttpHeader> headerList = new ArrayList<HttpHeader>();
        for (Header header : headers) {
            headerList.add(convertHttpHeader(header));
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
    public static HttpHeader convertHttpHeader(final Header header) {
        if (header == null) {
            return null;
        }
        return new HttpHeader(header.getName(), header.getValue());
    }
}
