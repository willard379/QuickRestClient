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
package jp.ambrosoli.http.client.params;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

public class BasicQueryStringBuilderTest {

    @Test
    public void testCreateConformedParams() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A"));
        params.add(new NameValueObject("b", "B"));
        params.add(new NameValueObject("c", "C"));

        // Act
        String queryString = paramBuilder.createConformedParams(params, "UTF-8");

        // Assert
        assertThat(queryString, is(equalTo("?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateConformedParams_EmptyParams() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();

        // Act
        String queryString = paramBuilder.createConformedParams(params, "UTF-8");

        // Assert
        assertThat(queryString, is(equalTo("")));

    }

    @Test
    public void testCreateConformedParams_NullParam() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        // Act
        String queryString = paramBuilder.createConformedParams(null, "UTF-8");

        // Assert
        assertThat(queryString, is(equalTo("")));

    }

    @Test
    public void testCreateConformedParams_NullEncoding() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A"));
        params.add(new NameValueObject("b", "B"));
        params.add(new NameValueObject("c", "C"));

        // Act
        String queryString = paramBuilder.createConformedParams(params, null);

        // Assert
        assertThat(queryString, is(equalTo("?a=A&b=B&c=C")));

    }

    @Test
    public void testCreateConformedParams_ParamsFilledWithNull() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);

        // Act
        String queryString = paramBuilder.createConformedParams(params, "UTF-8");

        // Assert
        assertThat(queryString, is(equalTo("")));

    }

    @Test
    public void testCreateConformedParams_MultibyteParams() {

        // Arrange
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("あ", "あいうえお"));

        // Act
        String queryString = paramBuilder.createConformedParams(params, "UTF-8");

        // Assert
        assertThat(queryString,
                is(equalTo("?%E3%81%82=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A")));

    }

}
