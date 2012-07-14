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
package jp.ambrosoli.quickrestclient.apache.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.quickrestclient.service.HttpService;

import org.junit.Test;

public class ApacheHttpServiceFactoryTest {

    @Test
    public void testCreate() {

        // Setup
        ApacheHttpServiceFactory factory = new ApacheHttpServiceFactory();

        // Exercise
        HttpService service = factory.create();

        // Verify
        assertThat(service, is(notNullValue()));
    }
}
