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
package jp.ambrosoli.quickrestclient.apache.headers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.params.NameValueObject;

import org.apache.http.Header;
import org.junit.Test;

public class ApacheHeaderBuilderTest {

    @Test
    public void createConformedHeadersを呼び出すと_引数で渡した名前と値でHTTPヘッダーが生成されること() {

        // Setup
        ApacheHeaderBuilder sut = new ApacheHeaderBuilder();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name3", "Value3")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        Header[] actual = sut.createConformedHeaders(values);

        // Verify
        assertThat(actual.length, is(values.size()));
        assertThat(actual[0].getName(), is(equalTo(values.get(0).getName())));
        assertThat(actual[0].getValue(), is(equalTo(values.get(0).getValue())));
    }

    @Test
    public void createConformedHeaderにnullを渡すと_nullが返されること() {

        // Setup
        ApacheHeaderBuilder sut = new ApacheHeaderBuilder();

        // Exercise
        Header[] actual = sut.createConformedHeaders(null);

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void createConformedHeaderに空のListを渡すと_nullが返されること() {

        // Setup
        ApacheHeaderBuilder sut = new ApacheHeaderBuilder();

        // Exercise
        Header[] actual = sut.createConformedHeaders(new ArrayList<NameValueObject>());

        // Asseert
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void createConformedHeaderにnullの要素を含むListを渡すと_nullがconpactされてHTTPヘッダーが返されること() {

        // Setup
        ApacheHeaderBuilder sut = new ApacheHeaderBuilder();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(null);
        values.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(null);
        values.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(null);

        // Exercise
        Header[] actual = sut.createConformedHeaders(values);

        // Verify
        assertThat(actual.length, is(2));
    }

}
