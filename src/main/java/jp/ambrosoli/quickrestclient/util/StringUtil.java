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
package jp.ambrosoli.quickrestclient.util;

import java.io.UnsupportedEncodingException;

import jp.ambrosoli.quickrestclient.exception.UnsupportedEncodingRuntimeException;

/**
 * 文字列に対するユーティリティクラスです。
 *
 * @author willard379
 * @since 0.1.0
 */
public final class StringUtil {

    /** デフォルト文字コード */
    public static final String DEFAULT_ENCODING = "UTF-8"; //$NON-NLS-1$

    /**
     * 文字列がnullもしくは空か判定します。
     *
     * @param str
     *            文字列
     * @return nullもしくは空文字の場合true、その他の場合false
     */
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 文字列がnullでも空でもないか判定します。
     *
     * @param str
     *            文字列
     * @return nullでも空文字でもない場合true、その他の場合false
     */
    public static boolean isNotEmpty(final CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * バイト配列から文字列へ変換して返します。
     *
     * @param data
     *            バイト配列
     * @param encoding
     *            文字エンコード
     * @throws UnsupportedEncodingRuntimeException
     *             {@link UnsupportedEncodingException}が発生した場合
     * @return バイト配列から変換した文字列
     */
    public static String toString(final byte[] data, final String encoding) {
        if (data == null) {
            return null;
        }
        String enc = StringUtil.isNotEmpty(encoding) ? encoding : DEFAULT_ENCODING;
        try {
            return new String(data, enc);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingRuntimeException(e);
        }
    }

    /**
     * 配列の要素をseparator文字で連結した１つの文字列にして返します。
     *
     * @param array
     *            結合する要素
     * @param separator
     *            各要素を区切るセパレータ
     * @return separatorで区切られたarrayからなる新しいString
     */
    public static String join(final Object[] array, final CharSequence separator) {
        if (array == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder();
        for (Object element : array) {
            if (buf.length() > 0) {
                buf.append(separator);
            }
            if (element != null) {
                buf.append(element);
            }
        }
        return buf.toString();
    }
}
