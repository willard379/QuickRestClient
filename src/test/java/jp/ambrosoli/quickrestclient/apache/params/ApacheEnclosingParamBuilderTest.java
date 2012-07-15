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

import static org.hamcrest.Matchers.*;
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
    public void RequestParamsにParamBuilderを渡してリクエストパラメータを生成する() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Exercise
        UrlEncodedFormEntity actual = reqParams.getConformedParams(sut);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(greaterThan(0L)));

    }

    @Test
    public void RequestParamsにParamBuilderとエンコーディングを渡してリクエストパラメータを生成する() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Exercise
        UrlEncodedFormEntity actual = reqParams.getConformedParams(sut, "UTF-8");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(greaterThan(0L)));
    }

    @Test
    public void createConformedParamsを呼び出すと_リクエストパラメータが生成されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        UrlEncodedFormEntity actual = sut.createConformedParams(params, "UTF-8");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(greaterThan(0L)));
    }

    @Test
    public void createConformedParamsの第一引数にnullを渡すと_空のエンティティが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        // Exercise
        UrlEncodedFormEntity actual = sut.createConformedParams(null, "UTF-8");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(0L));
    }

    @Test
    public void createConformedParamsの第一引数に空のListを渡すと_空のエンティティが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        // Exercise
        UrlEncodedFormEntity actual = sut.createConformedParams(new ArrayList<NameValueObject>(),
                "UTF-8");

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(0L));
    }

    @Test
    public void createConformedParamsの第二引数にnullを渡すと_デフォルトエンコーディングでエンティティが生成されること() throws Exception {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        UrlEncodedFormEntity actual = sut.createConformedParams(params, null);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getContentLength(), is(greaterThan(0L)));
    }

    @Test
    public void createConformedParamsの第二引数に存在しないエンコーディングを渡すと_IllegalArgumentExceptionが発生すること()
            throws Exception {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        this.expectedException.expect(is(instanceOf(IllegalArgumentException.class)));

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        String charset = "UTF-48";

        // Exercise
        sut.createConformedParams(params, charset);

        // Verify
        fail("IllegalArgumentExceptionが発生しませんでした。");
    }

    @Test
    public void createNameValuePairListを呼び出すと_引数で渡したNameValueObjectリストからNameValuePairリストが生成されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Exercise
        List<NameValuePair> actual = sut.createNameValuePairList(params);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(params.size()));

    }

    @Test
    public void createNameValuePairListにnullを渡すと_空のListが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        // Exercise
        List<NameValuePair> actual = sut.createNameValuePairList(null);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.isEmpty(), is(true));

    }

    @Test
    public void createNameValuePairListに空のListを渡すと_空のリストが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();

        // Exercise
        List<NameValuePair> actual = sut.createNameValuePairList(params);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.isEmpty(), is(true));

    }

    @Test
    public void createNameValuePairListにnullの要素を含むListを渡すと_nullがcompactされたNameValuePairのListが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(new NameValueObject("name", "value"));
        params.add(null);

        // Exercise
        List<NameValuePair> actual = sut.createNameValuePairList(params);

        // Verify
        assertThat(actual.isEmpty(), is(false));
        assertThat(actual.size(), is(1));

    }

    @Test
    public void createNameValuePairListにすべての要素がnullのListを渡すと_空のListが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);

        // Exercise
        List<NameValuePair> actual = sut.createNameValuePairList(params);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.isEmpty(), is(true));

    }

    @Test
    public void toNameValuePairを呼び出すと_NameValueObjectからNameValuePairが生成されて返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        NameValueObject nvo = new NameValueObject("name", "value");

        // Exercise
        NameValuePair actual = sut.toNameValuePair(nvo);

        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getName(), is(equalTo(nvo.getName())));
        assertThat(actual.getValue(), is(equalTo(nvo.getValue())));
        assertThat(sut.toNameValuePair(null), is(nullValue()));
    }

    @Test
    public void toNameValuePairにnullを渡すと_nullが返されること() {

        // Setup
        ApacheEnclosingParamBuilder sut = new ApacheEnclosingParamBuilder();

        // Exercise
        NameValuePair actual = sut.toNameValuePair(null);

        // Verify
        assertThat(actual, is(nullValue()));
    }

}
