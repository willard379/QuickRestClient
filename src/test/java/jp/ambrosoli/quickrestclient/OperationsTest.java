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
package jp.ambrosoli.quickrestclient;

import static jp.ambrosoli.quickrestclient.Operations.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;

import org.junit.Ignore;
import org.junit.Test;

public class OperationsTest {

    @Test
    public void addメソッドを呼び出すとNameValueObjectが生成されること() {

        // Exercise
        NameValueObject actual = add("name", "value");

        // Verify
        assertThat(actual.getName(), is(equalTo("name")));
        assertThat(actual.getValue(), is(equalTo("value")));
    }

    @Test
    public void digestメソッドを呼び出すとAuthInfoが生成されること() {

        // Exercise
        AuthInfo actual = digest("user", "password");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(AuthType.DIGEST));
        assertThat(actual.username, is(equalTo("user")));
        assertThat(actual.password, is(equalTo("password")));
    }

    @Test
    public void basicメソッドを呼び出すとAuthInfoが生成されること() {

        // Exercise
        AuthInfo actual = basic("user", "password");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(AuthType.BASIC));
        assertThat(actual.username, is(equalTo("user")));
        assertThat(actual.password, is(equalTo("password")));
    }

    @Ignore
    @Test
    public void clientCertメソッドを呼び出すとAuthInfoが生成されること() {
        fail();
    }

}
