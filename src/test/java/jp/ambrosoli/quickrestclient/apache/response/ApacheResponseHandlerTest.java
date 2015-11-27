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
package jp.ambrosoli.quickrestclient.apache.response;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

public class ApacheResponseHandlerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void handleResponseを呼び出すと_HttpResponseが返されること() {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        BasicHttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        byte[] data = "Stay here, I'll be back".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(input, data.length);
        httpResponse.setEntity(inputStreamEntity);

        // Exercise
        HttpResponse actual = sut.handleResponse(httpResponse);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.isSuccess(), is(true));
        assertThat(actual.getAsString(), is(equalTo("Stay here, I'll be back")));

    }

    @Test
    public void handleResponseの引数にnullを渡すと_NullPointerExceptionが発生すること() {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        this.expectedException.expect(is(instanceOf(NullPointerException.class)));

        // Exercise
        sut.handleResponse(null);

        // Verify
        fail("NullPoiinterExceptionが発生しませんでした。");
    }

    @Test
    public void consumeEntityを呼び出すと_InputStreamがcloseされること() throws Exception {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 100);
        assertThat(entity.isStreaming(), is(true));

        // Exercise
        sut.consumeEntity(entity);

        // Verify
        verify(input).close();

    }

    @Test
    public void consumeEntityの引数にnullを渡してもExceptionが発生しないこと() throws Exception {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        // Exercise
        sut.consumeEntity(null);

        // Verify
        // noop
    }

    @Test
    public void consumeEntityに空のEntityを渡してもExceptionが発生しないこと() throws Exception {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        // Exercise
        sut.consumeEntity(new BasicHttpEntity());

        // Exercise
        // noop
    }

    @Test
    public void EntityからInputStreamが返されない状態でconsumeEntityを呼び出してもExceptionが発生しないこと() throws Exception {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenReturn(null);

        // Exercise
        sut.consumeEntity(entity);

        // Verify
    }

    @Test
    public void consumeEntityを呼び出した際にIOExceptionが発生した場合_IORuntimeExcepitonにラップされてスローされること() throws Exception {

        // Setup
        ApacheResponseHandler sut = new ApacheResponseHandler();

        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenThrow(new IOException());

        this.expectedException.expect(is(instanceOf(IORuntimeException.class)));

        // Exercise
        sut.consumeEntity(entity);

        // Verify
        fail("IORuntimeExceptionが発生しませんでした。");
    }
}
