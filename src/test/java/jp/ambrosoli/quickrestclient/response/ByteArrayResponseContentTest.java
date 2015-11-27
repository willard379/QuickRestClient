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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ByteArrayResponseContentTest {

    @Test
    public void setDataの引数にbyte配列を渡すと_ResponseContentに保持されること() {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();

        String content = "abcdefg";

        // Exercise
        sut.setData(content.getBytes());

        // Verify
        assertThat(sut.data, is(equalTo(content.getBytes())));
    }

    @Test
    public void getAsByteArrayを呼び出すと_ResponseContentのデータ内容がbyte配列で返されること() {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData("あいうえお".getBytes());

        // Exercise
        byte[] actual = sut.getAsByteArray();

        // Verify
        assertThat(actual, is(equalTo(sut.data)));
    }

    @Test
    public void ResponseContentのデータがnullの状態でgetAsByteArrayを呼び出すと_nullが返されること() {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData(null);

        // Exercise
        byte[] actual = sut.getAsByteArray();

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getAsInputStreamを呼び出すと_ResponseContentのデータ内容がInputStreamで返されること() throws IOException {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData("OPQRSTUVWXYZ".getBytes());

        // Exercise
        InputStream actual = sut.getAsInputStream();

        // Verify
        assertThat(actual, is(notNullValue()));

        byte[] res = new byte[actual.available()];
        actual.read(res);
        assertThat(res, is(equalTo(sut.data)));
    }

    @Test
    public void ResponseContentのデータがnullの状態でgetAsInputStreamを呼び出すと_nullが返されること() throws IOException {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData(null);

        // Exercise
        InputStream actual = sut.getAsInputStream();

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void getAsStringを呼び出すと_ResponseContentのデータが文字列として返されること() {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData("willard379".getBytes());

        // Exercise
        String actual = sut.getAsString();

        // Verify
        assertThat(actual, is(equalTo("willard379")));
    }

    @Test
    public void ResponseContentのデータがnullの状態でgetAsStringを呼び出すと_nullが返されること() {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData(null);

        // Exercise
        String actual = sut.getAsString();

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getAsStringの引数にエンコーディングを指定して呼び出すと_ResponseContentのデータが指定したエンコーディングの文字列として返されること() throws IOException {

        // Setup
        ByteArrayResponseContent sut = new ByteArrayResponseContent();
        sut.setData("𩸽".getBytes("UTF-16"));

        // Exercise
        String actual = sut.getAsString("UTF-16");

        // Verify
        assertThat(actual, is(equalTo("𩸽")));
    }

    @Test
    public void writeToの引数にOutputStreamを渡すと_ResponseContentのデータがOutputStreamに書き出されること() throws Exception {

        // Setup
        byte[] data = "ABCDEFG".getBytes("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ResponseContent sut = new ByteArrayResponseContent(data);

        // Exercise
        sut.writeTo(outputStream);

        // Verify
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("ABCDEFG")));
    }

}
