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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.response.HttpResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApacheResponseHandlerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testHandleResponse() {

        // Setup
        BasicHttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        byte[] data = "Stay here, I'll be back".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(input, data.length);
        httpResponse.setEntity(inputStreamEntity);
        ApacheResponseHandler handler = new ApacheResponseHandler();

        // Exercise
        HttpResponse response = handler.handleResponse(httpResponse);

        // Verify
        assertThat(response, is(notNullValue()));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("Stay here, I'll be back")));

    }

    @Test(expected = NullPointerException.class)
    public void testHandleResponse_Null() {

        // Setup
        ApacheResponseHandler handler = new ApacheResponseHandler();

        // Exercise
        handler.handleResponse(null);
    }

    @Test
    public void testConsumeEntity() throws Exception {

        // Setup
        InputStream input = mock(InputStream.class);
        HttpEntity entity = new InputStreamEntity(input, 100);
        assertThat(entity.isStreaming(), is(true));

        // Exercise
        ApacheResponseHandler handler = new ApacheResponseHandler();
        handler.consumeEntity(entity);

        // Verify
        verify(input).close();

    }

    @Test
    public void testConsumeEntity_Null() throws Exception {

        // Exercise
        ApacheResponseHandler handler = new ApacheResponseHandler();
        handler.consumeEntity(null);
    }

    @Test
    public void testConsumeEntity_Empty() throws Exception {

        // Exercise
        ApacheResponseHandler handler = new ApacheResponseHandler();
        handler.consumeEntity(new BasicHttpEntity());
    }

    @Test
    public void testConsumeEntity_NoInputStream() throws Exception {

        // Setup
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenReturn(null);

        // Exercise
        ApacheResponseHandler handler = new ApacheResponseHandler();
        handler.consumeEntity(entity);

    }

    @Test
    public void testConsumeEntity_Exception() throws Exception {

        // Setup
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.isStreaming()).thenReturn(true);
        when(entity.getContent()).thenThrow(new IOException());

        // Expected
        this.expectedException.expect(IORuntimeException.class);

        // Exercise
        ApacheResponseHandler handler = new ApacheResponseHandler();
        handler.consumeEntity(entity);

    }

}
