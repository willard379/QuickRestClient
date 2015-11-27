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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.ambrosoli.quickrestclient.apache.service.ApacheHttpServiceFactory;

public class HttpServiceFactoryTest {

    private static final Class<ApacheHttpServiceFactory> DEFAULT_FACTORY = ApacheHttpServiceFactory.class;

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
    public void getFactoryを呼び出すと_デフォルトのHttpServiceFactoryが返されること() {

        // Exercise
        HttpServiceFactory actual = HttpServiceFactory.getFactory();

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(instanceOf(DEFAULT_FACTORY)));
    }

    @Test
    public void getFactoryの引数にnullを渡すと_デフォルトのHttpServiceFactoryが返されること() throws ClassNotFoundException {

        // Exercise
        HttpServiceFactory actual = HttpServiceFactory.getFactory(null);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(instanceOf(DEFAULT_FACTORY)));
    }

    @Test
    public void getFactoryの引数に登録していないファクトリ名を指定した場合_IllegalStateExceptionが返されること() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalStateException.class)));
        this.expectedException.expectMessage(is(equalTo("HttpServiceFactory {unregistered} is not registerd.")));

        // Exercise
        HttpServiceFactory.getFactory("unregistered");

        // Verify
        fail("例外が発生しませんでした");
    }

    @Test
    public void registerを呼び出すと_HttpServiceFactoryのキャッシュに登録されること() {

        // Setup
        HttpServiceFactory factory = mock(HttpServiceFactory.class);

        // Exercise
        HttpServiceFactory.register("test_factory", factory);

        // Verify
        assertThat(HttpServiceFactory.getFactory("test_factory"), is(sameInstance(factory)));
    }

    @Test
    public void registerの第一引数にnullを渡すと_デフォルトのHttpServiceFactoryが上書きされること() {

        // Setup
        HttpServiceFactory factory = mock(HttpServiceFactory.class);

        // Exercise
        HttpServiceFactory.register(null, factory);

        // Verify
        assertThat(HttpServiceFactory.getFactory(null), is(sameInstance(factory)));
    }

    @Test
    public void registerの第二引数にnullを渡すと_IllegalArgumentExceptionが発生すること() {

        // Setup
        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));
        this.expectedException.expectMessage(is(equalTo("HttpServiceFactory {hoge} could not be registerd.")));

        // Exercise
        HttpServiceFactory.register("hoge", null);

        // Verify
        fail("例外が発生しませんでした");
    }

}
