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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author willard379
 * 
 */
public class CloseableUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testClose() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);

        // Exercise
        CloseableUtil.close(closeable);

        // Verify
        verify(closeable).close();
    }

    @Test
    public void testClose_Null() throws Exception {

        // Setup
        Closeable closeable = null;

        // Exercise
        CloseableUtil.close(closeable);

        // Verify
        // 例外が発生しなければOK

    }

    @Test
    public void testClose_Exception() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);
        doThrow(new IOException()).when(closeable).close();

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Exercise
        CloseableUtil.close(closeable);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }

    @Test
    public void testCloseSilentry() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);

        // Exercise
        CloseableUtil.closeSilently(closeable);

        // Verify
        verify(closeable).close();
    }

    @Test
    public void testCloseSilentry_Null() throws Exception {

        // Setup
        Closeable closeable = null;

        // Exercise
        CloseableUtil.closeSilently(closeable);

        // Verify
        // 例外が発生しなければOK

    }

    @Test
    public void testCloseSilentry_Exception() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);
        doThrow(new IOException()).when(closeable).close();

        // Exercise
        CloseableUtil.closeSilently(closeable);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void testCloseAll() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        Reader reader = mock(Reader.class);
        Writer writer = mock(Writer.class);

        // Exercise
        CloseableUtil.closeAll(closeable, inputStream, outputStream, reader, writer);

        // Verify
        verify(closeable).close();
        verify(inputStream).close();
        verify(outputStream).close();
        verify(reader).close();
        verify(writer).close();

    }

    @Test
    public void testCloseAll_Null() throws Exception {

        // Setup
        Closeable closeable = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Reader reader = null;
        Writer writer = null;

        // Exercise
        CloseableUtil.closeAll(closeable, inputStream, outputStream, reader, writer);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void testCloseAll_Exception() throws Exception {

        // Setup
        Closeable closeable = mock(Closeable.class);
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        Reader reader = mock(Reader.class);
        Writer writer = mock(Writer.class);

        doThrow(new RuntimeException("occured in Closeable")).when(closeable).close();
        doThrow(new RuntimeException("occured in InputStream")).when(inputStream).close();
        doThrow(new RuntimeException("occured in OutputStream")).when(outputStream).close();
        doThrow(new RuntimeException("occured in Reader")).when(reader).close();
        doThrow(new RuntimeException("occured in Writer")).when(writer).close();

        // Expected
        // 最初に発生した例外がスローされる
        this.expectedException.expect(RuntimeException.class);
        this.expectedException.expectMessage("occured in Closeable");

        // Exercise
        CloseableUtil.closeAll(closeable, inputStream, outputStream, reader, writer);

        // Verify
        // 例外が発生しなければOK
    }

}
