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
package jp.ambrosoli.quickrestclient.apache.params;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApacheEnclosingParamBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetConformedParams() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = reqParams.getConformedParams(builder);

        // Verify
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testGetConformedParams_UTF8() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = reqParams.getConformedParams(builder, "UTF-8");

        // Verify
        assertThat(entity, is(notNullValue()));
    }

    @Test
    public void testCreateConformedParams() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(params, "UTF-8");

        // Verify
        assertThat(entity, is(notNullValue()));
    }

    @Test
    public void testCreateConformedParams_NullParams() {

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(null, "UTF-8");

        // Verify
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testCreateConformedParams_EmptyParam() {

        // Setup
        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();
        String charset = "UTF-8";

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(params, charset);

        // Verify
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testCreateConformedParams_NullCharset() throws Exception {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(params, null);

        // Verify
        assertThat(entity, is(notNullValue()));
    }

    @Test
    public void testCreateConformedParams_InvalidCharset() throws Exception {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        String charset = "UTF-48";

        // Expected
        this.expectedException.expect(IllegalArgumentException.class);

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        builder.createConformedParams(params, charset);

    }

    @Test
    public void testCreateNameValuePairList() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Verify
        assertThat(dest, is(notNullValue()));
        assertThat(dest.size(), is(params.size()));

    }

    @Test
    public void testCreateNameValuePairList_NullParam() {

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(null);

        // Verify
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testCreateNameValuePairList_EmptyParam() {

        // Setup
        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Verify
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testCreateNameValuePairList_ContainsNull() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Verify
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testToNameValuePair() {

        // Setup
        NameValueObject nvo = new NameValueObject("name", "value");

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        NameValuePair nameValuePair = builder.toNameValuePair(nvo);

        // Verify
        assertThat(nameValuePair, is(notNullValue()));
        assertThat(nameValuePair.getName(), is(equalTo(nvo.getName())));
        assertThat(nameValuePair.getValue(), is(equalTo(nvo.getValue())));
        assertThat(builder.toNameValuePair(null), is(nullValue()));
    }

    @Test
    public void testToNameValuePair_NullParam() {

        // Exercise
        ApacheEnclosingParamBuilder builder = new ApacheEnclosingParamBuilder();
        NameValuePair nameValuePair = builder.toNameValuePair(null);

        // Verify
        assertThat(nameValuePair, is(nullValue()));
    }

}
