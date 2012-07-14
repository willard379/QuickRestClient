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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ByteArrayResponseContentTest {

    @Test
    public void testSetData() {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();

        // Exercise
        content.setData("abcdefg".getBytes());

        // Verify
        assertThat(content.data, is(notNullValue()));
    }

    @Test
    public void testGetAsByteArray() {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("あいうえお".getBytes());

        // Exercise
        byte[] data = content.getAsByteArray();

        // Verify
        assertThat(data, is(equalTo(content.data)));
    }

    @Test
    public void testGetAsByteArray_Null() {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData(null);

        // Exercise
        byte[] data = content.getAsByteArray();

        // Verify
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testGetAsInputStream() throws IOException {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("OPQRSTUVWXYZ".getBytes());

        // Exercise
        InputStream input = content.getAsInputStream();

        // Verify
        assertThat(input, is(notNullValue()));

        byte[] res = new byte[input.available()];
        input.read(res);
        assertThat(res, is(equalTo(content.data)));
    }

    @Test
    public void testGetAsInputStream_Null() throws IOException {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData(null);

        // Exercise
        InputStream inputStream = content.getAsInputStream();

        // Verify
        assertThat(inputStream, is(nullValue()));

    }

    @Test
    public void testGetAsString() {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("willard379".getBytes());

        // Exercise
        String data = content.getAsString();

        // Verify
        assertThat(data, is(equalTo("willard379")));
    }

    @Test
    public void testGetAsString_Null() {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData(null);

        // Exercise
        String data = content.getAsString();

        // Verify
        assertThat(data, is(nullValue()));
    }

    @Test
    public void testGetAsStringString() throws IOException {

        // Setup
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("山田太郎".getBytes("MS932"));

        // Exercise
        String data = content.getAsString("MS932");

        // Verify
        assertThat(data, is(equalTo("山田太郎")));
    }

    @Test
    public void testWriteTo() throws Exception {

        // Setup
        byte[] data = "ABCDEFG".getBytes("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Exercise
        ResponseContent responseContent = new ByteArrayResponseContent(data);
        responseContent.writeTo(outputStream);

        // Verify
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("ABCDEFG")));
    }

}
