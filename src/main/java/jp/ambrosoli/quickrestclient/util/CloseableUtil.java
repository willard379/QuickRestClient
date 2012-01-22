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

import java.io.Closeable;
import java.io.IOException;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;

/**
 * {@link Closeable}用のユーティリティクラスです。
 * 
 * @author willard379
 * @since 0.2.0
 */
public final class CloseableUtil {

    /**
     * {@link Closeable}をクローズします。
     * 
     * @param closeable
     *            {@link Closeable}オブジェクト
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static void close(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link Closeable}をクローズします。<br/>
     * このメソッドはIORuntimeExceptionを発生させません。
     * 
     * @param closeable
     *            {@link Closeable}オブジェクト
     */
    public static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            // Do nothing.
        }
    }

    /**
     * すべての{@link Closeable}をクローズします。<br/>
     * 引数のいずれかで例外が発生した場合、最初に発生したIORuntimeExceptionをthrowします。
     * 
     * @param closeables
     *            {@link Closeable}オブジェクト
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static void closeAll(final Closeable... closeables) {

        RuntimeException thrown = null;
        for (Closeable closeable : closeables) {
            if (closeable == null) {
                continue;
            }
            try {
                CloseableUtil.close(closeable);
            } catch (RuntimeException t) {
                if (thrown == null) {
                    thrown = t;
                }
            }
        }
        if (thrown != null) {
            throw thrown;
        }
    }
}
