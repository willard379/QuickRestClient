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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.quickrestclient.enums.AuthType;
import jp.ambrosoli.quickrestclient.params.AuthInfo;
import jp.ambrosoli.quickrestclient.params.NameValueObject;

import org.junit.Ignore;
import org.junit.Test;

public class OperationsTest {

    @Test
    public void testAdd() {

        // Exercise
        NameValueObject obj1 = add("name", "value");

        // Verify
        assertThat(obj1.getName(), is(equalTo("name")));
        assertThat(obj1.getValue(), is(equalTo("value")));
    }

    @Test
    public void testDigest() {

        // Exercise
        AuthInfo digest = digest("user", "password");

        // Verify
        assertThat(digest, is(notNullValue()));
        assertThat(digest.type, is(AuthType.DIGEST));
        assertThat(digest.username, is(equalTo("user")));
        assertThat(digest.password, is(equalTo("password")));
    }

    @Test
    public void testBasic() {

        // Exercise
        AuthInfo basic = basic("user", "password");

        // Verify
        assertThat(basic, is(notNullValue()));
        assertThat(basic.type, is(AuthType.BASIC));
        assertThat(basic.username, is(equalTo("user")));
        assertThat(basic.password, is(equalTo("password")));
    }

    @Ignore
    @Test
    public void testClientCert() {
        fail();
    }

}
