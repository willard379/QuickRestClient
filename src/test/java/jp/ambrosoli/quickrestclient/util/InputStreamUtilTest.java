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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author willard379
 * 
 */
public class InputStreamUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testClose() throws Exception {

        // Arrange
        InputStream inputStream = mock(InputStream.class);

        // Act
        InputStreamUtil.close(inputStream);

        // Assert
        verify(inputStream).close();
    }

    @Test
    public void testCloseSilentry() throws Exception {

        // Arrange
        InputStream inputStream = mock(InputStream.class);

        // Act
        InputStreamUtil.closeSilently(inputStream);

        // Assert
        verify(inputStream).close();
    }

    @Test
    public void testCopy() throws Exception {

        // Arrange
        InputStream inputStream = new ByteArrayInputStream("willard379".getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        InputStreamUtil.copy(inputStream, outputStream);

        // Assert
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("willard379")));
    }

    @Test
    public void testCopy_NullInputStream() throws Exception {

        // Arrange
        InputStream input = null;
        OutputStream output = mock(OutputStream.class);

        // Act
        InputStreamUtil.copy(input, output);

        // Assert
        verify(output, never()).write((byte[]) any(), eq(0), anyInt());
        verify(output).close();
    }

    @Test
    public void testCopy_NullOutputStream() throws Exception {

        // Arrange
        InputStream input = mock(InputStream.class);
        OutputStream output = null;

        // Act
        InputStreamUtil.copy(input, output);

        // Assert
        verify(input, never()).read((byte[]) any(), eq(0), anyInt());
        verify(input).close();
    }

    @Test
    public void testCopy_ExceptionInInputStream() throws Exception {

        // Arrange
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        doThrow(new IOException("Read error")).when(input).read((byte[]) any(), eq(0), anyInt());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Act
        InputStreamUtil.copy(input, output);

        // Assert
        verify(output).close();
    }

    @Test
    public void testCopy_ExceptionInOutputStream() throws Exception {

        // Arrange
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        doThrow(new IOException("Write error")).when(output).write((byte[]) any(), eq(0), anyInt());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Act
        InputStreamUtil.copy(input, output);

        // Assert
        verify(input).close();
    }

}
