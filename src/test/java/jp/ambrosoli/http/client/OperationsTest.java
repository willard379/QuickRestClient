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
package jp.ambrosoli.http.client;

import static jp.ambrosoli.http.client.Operations.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.ambrosoli.http.client.enums.AuthType;
import jp.ambrosoli.http.client.params.AuthInfo;
import jp.ambrosoli.http.client.params.NameValueObject;

import org.junit.Ignore;
import org.junit.Test;

public class OperationsTest {

    @Test
    public void testAdd() {

        // Act
        NameValueObject obj1 = add("name", "value");

        // Assert
        assertThat(obj1.getName(), is(equalTo("name")));
        assertThat(obj1.getValue(), is(equalTo("value")));
    }

    @Test
    public void testDigest() {

        // Act
        AuthInfo digest = digest("user", "password");

        // Assert
        assertThat(digest, is(notNullValue()));
        assertThat(digest.type, is(AuthType.DIGEST));
        assertThat(digest.username, is(equalTo("user")));
        assertThat(digest.password, is(equalTo("password")));
    }

    @Test
    public void testBasic() {

        // Act
        AuthInfo basic = basic("user", "password");

        // Assert
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
