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
package jp.ambrosoli.http.client.ahc.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.http.client.service.HttpService;

import org.junit.Test;

public class AHCHttpServiceFactoryTest {

    @Test
    public void testCreate() {

        // Arrange
        AHCHttpServiceFactory factory = new AHCHttpServiceFactory();

        // Act
        HttpService service = factory.create();

        // Assert
        assertThat(service, is(notNullValue()));
    }
}
