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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BasicQueryStringBuilderTest {

    @Test
    public void createConformedParamsの引数にリクエストパラメータを渡して呼び出すと_クエリストリングが生成されること() {

        // Setup
        RequestParamBuilder<String> sut = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        String actual = sut.createConformedParams(params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(equalTo("?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createConformedParamsの引数に空のListを渡した場合_空文字が返されること() {

        // Setup
        RequestParamBuilder<String> sut = new BasicQueryStringBuilder();

        ArrayList<NameValueObject> params = new ArrayList<NameValueObject>();

        // Exercise
        String actual = sut.createConformedParams(params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(equalTo(""))); //$NON-NLS-1$

    }

    @Test
    public void createConformedParamsの引数にnullを渡した場合_空文字が返されること() {

        // Setup
        RequestParamBuilder<String> sut = new BasicQueryStringBuilder();

        // Exercise
        String actual = sut.createConformedParams(null, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(actual, is(equalTo(""))); //$NON-NLS-1$

    }

    @Test
    public void createConformedParamsの引数に渡すエンコーディングがnullの場合でも_デフォルトのエンコーディングでクエリストリングが生成されること() {

        // Setup
        RequestParamBuilder<String> sut = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        String actual = sut.createConformedParams(params, null);

        // Verify
        assertThat(actual, is(equalTo("?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void createConformedParamsの引数にすべての要素がnullのListを渡した場合_空文字が返却されること() {

        // Setup
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);

        // Exercise
        String queryString = paramBuilder.createConformedParams(params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(queryString, is(equalTo(""))); //$NON-NLS-1$

    }

    @Test
    public void createConformedParamsの引数にマルチバイト文字を含むリストを渡した場合_URLエンコードされたクエリストリングが返されること() {

        // Setup
        RequestParamBuilder<String> paramBuilder = new BasicQueryStringBuilder();

        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("あ", "あいうえお")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        String queryString = paramBuilder.createConformedParams(params, "UTF-8"); //$NON-NLS-1$

        // Verify
        assertThat(queryString, is(equalTo("?%E3%81%82=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"))); //$NON-NLS-1$

    }

}
