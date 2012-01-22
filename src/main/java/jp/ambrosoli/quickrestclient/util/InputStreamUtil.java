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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;

/**
 * {@link InputStream}用のユーティリティクラスです。
 * 
 * @author willard379
 * @since 0.2.0
 */
public final class InputStreamUtil {

    private static final int BUFFER_SIZE = 4096;

    /**
     * {@link InputStream}をクローズします。
     * 
     * @param input
     *            クローズする入力ストリーム
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static void close(final InputStream input) {
        CloseableUtil.close(input);
    }

    /**
     * {@link InputStream}をクローズします。<br/>
     * このメソッドはIORuntimeExceptionを発生させません。
     * 
     * @param input
     *            クローズする入力ストリーム
     */
    public static void closeSilently(final InputStream input) {
        CloseableUtil.close(input);
    }

    /**
     * {@link InputStream}の内容を{@link OutputStream}へ書き出します。
     * 
     * @param input
     *            入力ストリーム
     * @param output
     *            出力ストリーム
     */
    public static void copy(final InputStream input, final OutputStream output) {

        if (input == null) {
            CloseableUtil.close(output);
            return;
        }
        if (output == null) {
            CloseableUtil.close(input);
            return;
        }

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readSize = 0;
            while ((readSize = input.read(buffer, 0, buffer.length)) != -1) {
                output.write(buffer, 0, readSize);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.closeAll(input, output);
        }
    }

}
