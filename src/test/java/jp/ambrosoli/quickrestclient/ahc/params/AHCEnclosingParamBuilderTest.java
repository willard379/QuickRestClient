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
package jp.ambrosoli.quickrestclient.ahc.params;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.ahc.params.AHCEnclosingParamBuilder;
import jp.ambrosoli.quickrestclient.params.NameValueObject;
import jp.ambrosoli.quickrestclient.params.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.junit.Test;

public class AHCEnclosingParamBuilderTest {

    @Test
    public void testGetConformedParams() {

        // Arrange
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity entity = reqParams.getConformedParams(builder);

        // Assert
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testGetConformedParams_UTF8() {

        // Arrange
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));
        RequestParams reqParams = new RequestParams(params);

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity entity = reqParams.getConformedParams(builder, "UTF-8");

        // Assert
        assertThat(entity, is(notNullValue()));
    }

    @Test
    public void testCreateConformedParams() {

        // Arrange
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(params, "UTF-8");

        // Assert
        assertThat(entity, is(notNullValue()));
    }

    @Test
    public void testCreateConformedParams_NoParams() {

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(null, null);

        // Assert
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testCreateConformedParams_EmptyParam() {

        // Arrange
        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();
        String charset = "UTF-8";

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        UrlEncodedFormEntity entity = builder.createConformedParams(params, charset);

        // Assert
        assertThat(entity, is(notNullValue()));

    }

    @Test
    public void testCreateNameValuePairList_NullParam() {

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(null);

        // Assert
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testCreateNameValuePairList_EmptyParam() {

        // Arrange
        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Assert
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testCreateNameValuePairList_NameValueObjectListParam() {

        // Arrange
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("Name1", "Value1"));
        params.add(new NameValueObject("Name2", "Value2"));
        params.add(new NameValueObject("Name3", "Value3"));

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Assert
        assertThat(dest, is(notNullValue()));
        assertThat(dest.size(), is(params.size()));

    }

    @Test
    public void testCreateNameValuePairList_EmptyListParam() {

        // Arrange
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        List<NameValuePair> dest = builder.createNameValuePairList(params);

        // Assert
        assertThat(dest, is(notNullValue()));
        assertThat(dest.isEmpty(), is(true));

    }

    @Test
    public void testToNameValuePair() {

        // Arrange
        NameValueObject nvo = new NameValueObject("name", "value");

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        NameValuePair nameValuePair = builder.toNameValuePair(nvo);

        // Assert
        assertThat(nameValuePair, is(notNullValue()));
        assertThat(nameValuePair.getName(), is(equalTo(nvo.getName())));
        assertThat(nameValuePair.getValue(), is(equalTo(nvo.getValue())));
        assertThat(builder.toNameValuePair(null), is(nullValue()));
    }

    @Test
    public void testToNameValuePair_NullParam() {

        // Act
        AHCEnclosingParamBuilder builder = new AHCEnclosingParamBuilder();
        NameValuePair nameValuePair = builder.toNameValuePair(null);

        // Assert
        assertThat(nameValuePair, is(nullValue()));
    }

}
