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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author willard379
 * 
 */
public class OutputStreamUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testClose1() throws Exception {

        // Setup
        OutputStream output = mock(OutputStream.class);

        // Exercise
        OutputStreamUtil.close(output);

        // Verify
        verify(output).close();
    }

    @Test
    public void testClose2() throws Exception {

        // Setup
        OutputStream output = null;

        // Exercise
        OutputStreamUtil.close(output);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void testClose3() throws Exception {

        // Setup
        OutputStream output = mock(OutputStream.class);
        doThrow(new IOException()).when(output).close();

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        OutputStreamUtil.close(output);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }

    @Test
    public void testCloseSilentry1() throws Exception {

        // Setup
        OutputStream output = mock(OutputStream.class);

        // Exercise
        OutputStreamUtil.closeSilently(output);

        // Verify
        verify(output).close();
    }

    @Test
    public void testCloseSilentry2() throws Exception {

        // Setup
        OutputStream output = null;

        // Exercise
        OutputStreamUtil.closeSilently(output);

        // Verify
        // 例外が発生しなければOK
    }

    @Test
    public void testCloseSilentry3() throws Exception {

        // Setup
        OutputStream output = mock(OutputStream.class);
        doThrow(new IOException()).when(output).close();

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        OutputStreamUtil.closeSilently(output);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }

}
