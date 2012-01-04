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
package jp.ambrosoli.http.client.ahc.response;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import jp.ambrosoli.http.client.response.HttpResponse;

import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Test;

public class AHCResponseHandlerTest {

    @Test
    public void testHandleResponse() {

        // Arrange
        BasicHttpResponse httpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        byte[] data = "Stay here, I'll be back".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(input, data.length);
        httpResponse.setEntity(inputStreamEntity);
        AHCResponseHandler handler = new AHCResponseHandler();

        // Act
        HttpResponse response = handler.handleResponse(httpResponse);

        // Assert
        assertThat(response, is(notNullValue()));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAsString(), is(equalTo("Stay here, I'll be back")));

    }

    @Test(expected = NullPointerException.class)
    public void testHandleResponse_Null() {

        // Arrange
        AHCResponseHandler handler = new AHCResponseHandler();

        // Act
        handler.handleResponse(null);
    }
}
