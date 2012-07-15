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

import static org.hamcrest.Matchers.*;
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
    public void closeを呼び出した場合_引数で渡したInputStreamがcloseされること() throws Exception {

        // Setup
        InputStream inputStream = mock(InputStream.class);

        // Exercise
        InputStreamUtil.close(inputStream);

        // Verify
        verify(inputStream).close();
    }

    @Test
    public void closeの引数にnullを渡した場合_例外が発生しないこと() throws Exception {

        // Setup
        InputStream inputStream = null;

        // Exercise
        InputStreamUtil.close(inputStream);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void closeの引数で渡したInputStreamで例外が発生した場合_IORuntimeExceptionにラップされてスローされること()
            throws Exception {

        // Setup
        InputStream inputStream = mock(InputStream.class);
        doThrow(new IOException()).when(inputStream).close();

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        InputStreamUtil.close(inputStream);

        // Verify
        fail("IORuntimeExceptionがスローされませんでした。");
    }

    @Test
    public void closeSilentryを呼び出した場合_引数で渡したInputStreamがcloseされること() throws Exception {

        // Setup
        InputStream inputStream = mock(InputStream.class);

        // Exercise
        InputStreamUtil.closeSilently(inputStream);

        // Verify
        verify(inputStream).close();
    }

    @Test
    public void closeSilentryの引数にnullを渡した場合_例外が発生しないこと() throws Exception {

        // Setup
        InputStream inputStream = null;

        // Exercise
        InputStreamUtil.closeSilently(inputStream);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void closeSilentryの引数で渡したInputStreamで例外が発生した場合_IORuntimeExceptionにラップされてスローされること()
            throws Exception {

        // Setup
        InputStream inputStream = mock(InputStream.class);
        doThrow(new IOException()).when(inputStream).close();

        this.expectedException.expect(IORuntimeException.class);

        // Exercise
        InputStreamUtil.closeSilently(inputStream);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }

    @Test
    public void copyが呼び出された場合_OutputStreamにデータが書きだされてcloseされること() throws Exception {

        // Setup
        InputStream inputStream = spy(new ByteArrayInputStream("willard379".getBytes("UTF-8")));
        ByteArrayOutputStream outputStream = spy(new ByteArrayOutputStream());

        // Exercise
        InputStreamUtil.copy(inputStream, outputStream);

        // Verify
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertThat(result, is(equalTo("willard379")));

        verify(inputStream).close();
        verify(outputStream).close();
    }

    @Test
    public void copyの第一引数にnullを渡した場合_OutputStreamには書き込まれずにcloseされること() throws Exception {

        // Setup
        InputStream input = null;
        OutputStream output = mock(OutputStream.class);

        // Exercise
        InputStreamUtil.copy(input, output);

        // Verify
        verify(output, never()).write((byte[]) any(), eq(0), anyInt());
        verify(output).close();
    }

    @Test
    public void copyの第二引数にnullを渡した場合_InputStreamの読み込みはされずにcloseされること() throws Exception {

        // Setup
        InputStream input = mock(InputStream.class);
        OutputStream output = null;

        // Exercise
        InputStreamUtil.copy(input, output);

        // Verify
        verify(input, never()).read((byte[]) any(), eq(0), anyInt());
        verify(input).close();
    }

    @Test
    public void copyの第一引数で渡したInputStreamで例外が発生した場合_InputStreamとOutputStreamはcloseされていること()
            throws Exception {

        // Setup
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        doThrow(new IOException("Read error")).when(input).read((byte[]) any(), eq(0), anyInt());

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        InputStreamUtil.copy(input, output);

        // Verify
        verify(input).close();
        verify(output).close();
    }

    @Test
    public void copyの第二引数で渡したOutputStreamで例外が発生した場合_InputStreamとOutputStreamはcloseされていること()
            throws Exception {

        // Setup
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        doThrow(new IOException("Write error")).when(output).write((byte[]) any(), eq(0), anyInt());

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        InputStreamUtil.copy(input, output);

        // Verify
        verify(input).close();
        verify(output).close();
    }
}
