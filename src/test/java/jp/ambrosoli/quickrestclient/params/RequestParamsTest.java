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
package jp.ambrosoli.quickrestclient.params;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RequestParamsTest {

    @Test
    public void testConstruct() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "a"));

        // Exercise
        RequestParams reqParams = new RequestParams(params);

        // Verify
        assertThat(reqParams.params, is(params));
        assertThat(reqParams.isEmpty(), is(false));
    }

    @Test
    public void testIsEmpty_Null() {
        // Setup
        RequestParams requestParams = new RequestParams(null);

        // Exercise
        boolean empty = requestParams.isEmpty();

        // Verify
        assertThat(empty, is(true));

    }

    @Test
    public void testIsEmpty_Empty() {

        // Setup
        RequestParams requestParams = new RequestParams(new ArrayList<NameValueObject>());

        // Exercise
        boolean empty = requestParams.isEmpty();

        // Verify
        assertThat(empty, is(true));

    }

    @Test
    public void testIsEmpty() {

        // Setup
        List<NameValueObject> params1 = new ArrayList<NameValueObject>();
        params1.add(new NameValueObject("a", "a"));
        RequestParams requestParams = new RequestParams(params1);
        // Exercise
        boolean empty = requestParams.isEmpty();

        // Verify
        assertThat(empty, is(false));
    }

    @Test
    public void testGetConformedParams_NullParams() {

        // Setup
        RequestParamBuilder<String> builder = new BasicQueryStringBuilder();
        RequestParams requestParams = new RequestParams(null);

        // Exercise
        String queryString = requestParams.getConformedParams(builder);

        // Verify
        assertThat(queryString, is(equalTo("")));

    }

    @Test
    public void testGetConformedParams() {

        // Setup
        RequestParamBuilder<String> builder = new BasicQueryStringBuilder();
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A"));
        params.add(new NameValueObject("b", "B"));
        params.add(new NameValueObject("c", "C"));
        RequestParams requestParams = new RequestParams(params);

        // Exercise
        String queryString = requestParams.getConformedParams(builder);

        // Verify
        assertThat(queryString, is(equalTo("?a=A&b=B&c=C")));

    }

    @Test
    public void testGetConformedParams_WithEncoding() {

        // Setup
        RequestParamBuilder<String> builder = new BasicQueryStringBuilder();
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A"));
        params.add(new NameValueObject("b", "B"));
        params.add(new NameValueObject("c", "C"));
        RequestParams requestParams = new RequestParams(params);

        // Exercise
        String queryString = requestParams.getConformedParams(builder, null);

        // Verify
        assertThat(queryString, is(equalTo("?a=A&b=B&c=C")));

    }

    @Test
    public void testGetConformedParams_ParamsFilledWithNull() {

        // Setup
        RequestParamBuilder<String> builder = new BasicQueryStringBuilder();
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);
        RequestParams requestParams = new RequestParams(params);

        // Exercise
        String queryString = requestParams.getConformedParams(builder);

        // Verify
        assertThat(queryString, is(equalTo("")));

    }

    @Test
    public void testGetParams() {

        // Setup
        List<NameValueObject> params1 = new ArrayList<NameValueObject>();
        params1.add(new NameValueObject("a", "A"));
        params1.add(new NameValueObject("b", "B"));
        params1.add(new NameValueObject("c", "C"));
        RequestParams requestParams = new RequestParams(params1);

        // Exercise
        List<NameValueObject> params2 = requestParams.getParams();

        // Verify
        assertThat(params2, is(sameInstance(params1)));

    }
}
