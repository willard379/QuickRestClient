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

import java.io.IOException;
import java.io.InputStream;

import jp.ambrosoli.quickrestclient.response.ByteArrayResponseContent;

import org.junit.Test;

public class ByteArrayResponseContentTest {

    @Test
    public void testSetData() {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();

        // Act
        content.setData("abcdefg".getBytes());

        // Assert
        assertThat(content.data, is(notNullValue()));
    }

    @Test
    public void testGetAsByteArray() {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("あいうえお".getBytes());

        // Act
        byte[] data = content.getAsByteArray();

        // Assert
        assertThat(data, is(equalTo(content.data)));
    }

    @Test
    public void testGetAsInputStream() throws IOException {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("OPQRSTUVWXYZ".getBytes());

        // Act
        InputStream input = content.getAsInputStream();

        // Assert
        assertThat(input, is(notNullValue()));

        byte[] res = new byte[input.available()];
        input.read(res);
        assertThat(res, is(equalTo(content.data)));
    }

    @Test
    public void testGetAsString() {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("willard379".getBytes());

        // Act
        String data = content.getAsString();

        // Assert
        assertThat(data, is(equalTo("willard379")));
    }

    @Test
    public void testGetAsStringString_Null() throws IOException {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();

        // Act
        InputStream inputStream = content.getAsInputStream();

        // Assert
        assertThat(inputStream, is(nullValue()));

    }

    @Test
    public void testGetAsStringString() throws IOException {

        // Arrange
        ByteArrayResponseContent content = new ByteArrayResponseContent();
        content.setData("山田太郎".getBytes("MS932"));

        // Act
        String data = content.getAsString("MS932");

        // Assert
        assertThat(data, is(equalTo("山田太郎")));
    }

}
