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
package jp.ambrosoli.quickrestclient.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import jp.ambrosoli.quickrestclient.apache.service.ApacheHttpServiceFactory;
import jp.ambrosoli.quickrestclient.service.HttpServiceFactory;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HttpServiceFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void tearDown() throws Exception {
        Field cacheField = HttpServiceFactory.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(null);
        cache.clear();
    }

    @Test
    public void testGetFactory_Default() {

        // Act
        HttpServiceFactory factory = HttpServiceFactory.getFactory();

        // Assert
        assertThat(factory, is(notNullValue()));
        assertThat(ApacheHttpServiceFactory.class.isInstance(factory), is(true));
    }

    @Test
    public void testGetFactory_Null() throws ClassNotFoundException {

        // Act
        HttpServiceFactory factory = HttpServiceFactory.getFactory(null);

        // Assert
        assertThat(factory, is(notNullValue()));
        assertThat(ApacheHttpServiceFactory.class.isInstance(factory), is(true));
    }

    @Test
    public void testGetFactory_Unregistered() {

        // Arrange
        this.expectedException.expect(is(instanceOf(IllegalStateException.class)));
        this.expectedException
                .expectMessage(is(equalTo("HttpServiceFactory {unregistered} is not registerd.")));

        // Act
        HttpServiceFactory.getFactory("unregistered");

        // Assert
        fail("例外が発生しませんでした");
    }

    @Test
    public void testRegister() {

        // Arrange
        HttpServiceFactory factory = mock(HttpServiceFactory.class);

        // Act
        HttpServiceFactory.register("test_factory", factory);

        // Assert
        assertThat(HttpServiceFactory.getFactory("test_factory"), is(sameInstance(factory)));
    }

    @Test
    public void testRegister_OverrideDefault() {

        // Arrange
        HttpServiceFactory factory = mock(HttpServiceFactory.class);

        // Act
        HttpServiceFactory.register(null, factory);

        // Assert
        assertThat(HttpServiceFactory.getFactory(null), is(sameInstance(factory)));
    }

    @Test
    public void testRegister_Null() {

        // Arrange
        this.expectedException.expect(is(instanceOf(IllegalStateException.class)));
        this.expectedException
                .expectMessage(is(equalTo("HttpServiceFactory {hoge} could not be registerd.")));

        // Act
        HttpServiceFactory.register("hoge", null);

        // Assert
        fail("例外が発生しませんでした");
    }

}
