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
package jp.ambrosoli.http.client.ahc.headers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.http.client.params.NameValueObject;

import org.apache.http.Header;
import org.junit.Test;

public class AHCHeaderBuilderTest {

    @Test
    public void testCreateConformedHeaders() {

        // Arrange
        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1"));
        values.add(new NameValueObject("Name2", "Value2"));
        values.add(new NameValueObject("Name3", "Value3"));

        // Act
        AHCHeaderBuilder builder = new AHCHeaderBuilder();
        Header[] headers4 = builder.createConformedHeaders(values);

        // Assert
        assertThat(headers4.length, is(values.size()));
        assertThat(headers4[0].getName(), is(equalTo(values.get(0).getName())));
        assertThat(headers4[0].getValue(), is(equalTo(values.get(0).getValue())));
    }

    @Test
    public void testCreateConformedHeaders_Null() {

        // Act
        AHCHeaderBuilder builder = new AHCHeaderBuilder();
        Header[] headers1 = builder.createConformedHeaders(null);

        // Assert
        assertThat(headers1, is(nullValue()));

    }

    @Test
    public void testCreateConformedHeaders_Empty() {

        // Act
        AHCHeaderBuilder builder = new AHCHeaderBuilder();
        Header[] headers2 = builder.createConformedHeaders(new ArrayList<NameValueObject>());

        // Asseert
        assertThat(headers2, is(nullValue()));
    }

}
