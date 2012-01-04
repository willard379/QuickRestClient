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
package jp.ambrosoli.http.client.util;

/**
 * 文字列に対するユーティリティクラスです。
 * 
 * @author willard379
 * @since 0.0.1
 */
public final class StringUtil {

    /** デフォルト文字コード */
    public static final String DEFAULT_ENCODING = "UTF-8";

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

}
