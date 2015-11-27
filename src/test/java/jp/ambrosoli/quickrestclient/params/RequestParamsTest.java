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

public class RequestParamsTest {

    @Test
    public void RequestParamsのコンストラクタを呼び出した場合_RequestParamsのインスタンスにコンストラクタの引数で渡したリクエストパラメータが設定されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "b")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        RequestParams actual = new RequestParams(params);

        // Verify
        assertThat(actual.params, is(params));
        assertThat(actual.isEmpty(), is(false));
        assertThat(actual.params.get(0).getName(), is(equalTo("a"))); //$NON-NLS-1$
        assertThat(actual.params.get(0).getValue(), is(equalTo("b"))); //$NON-NLS-1$
    }

    @Test
    public void RequestParamsのコンストラクタにnullを渡してisEmptyを呼び出した場合_trueが返されること() {

        // Setup
        RequestParams sut = new RequestParams(null);

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void RequestParamsのコンストラクタに空のListを渡してisEmptyを呼び出した場合_trueが返されること() {

        // Setup
        RequestParams sut = new RequestParams(new ArrayList<NameValueObject>());

        // Exercise
        boolean acutal = sut.isEmpty();

        // Verify
        assertThat(acutal, is(true));

    }

    @Test
    public void RequestParamsのコンストラクタにNameValueObjectのListを渡してisEmptyを呼び出した場合_falseが返されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "a")); //$NON-NLS-1$ //$NON-NLS-2$

        RequestParams sut = new RequestParams(params);

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void RequestParamsにリクエストパラメータがnullの状態でgetConformedParamsを呼び出した場合_空文字が返されること() {

        // Setup
        RequestParams sut = new RequestParams(null);

        // Exercise
        String actual = sut.getConformedParams(new BasicQueryStringBuilder());

        // Verify
        assertThat(actual, is(equalTo(""))); //$NON-NLS-1$

    }

    @Test
    public void ReauestParamsにリクエストパラメータが設定されている状態でgetConformedParamsを呼び出した場合_クエリストリングが生成されて返されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$

        RequestParams sut = new RequestParams(params);

        // Exercise
        String actual = sut.getConformedParams(new BasicQueryStringBuilder());

        // Verify
        assertThat(actual, is(equalTo("?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void RequestParamsにリクエストパラメータが設定されている状態でgetConformedParamsにエンコーディングをnullで渡した場合でも_デフォルトエンコーディングでクエリストリングが生成されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams sut = new RequestParams(params);

        // Exercise
        String actual = sut.getConformedParams(new BasicQueryStringBuilder(), null);

        // Verify
        assertThat(actual, is(equalTo("?a=A&b=B&c=C"))); //$NON-NLS-1$

    }

    @Test
    public void RequestParamsにすべての要素がnullのリクエストパラメータを設定した状態でgetConformedParamsを呼び出した場合_空文字が返されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);
        RequestParams sut = new RequestParams(params);

        // Exercise
        String actual = sut.getConformedParams(new BasicQueryStringBuilder());

        // Verify
        assertThat(actual, is(equalTo(""))); //$NON-NLS-1$

    }

    @Test
    public void getParamsを呼び出すと_RequestParamsに設定されたリクエストパラメータのListが返されること() {

        // Setup
        List<NameValueObject> params = new ArrayList<NameValueObject>();
        params.add(new NameValueObject("a", "A")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("b", "B")); //$NON-NLS-1$ //$NON-NLS-2$
        params.add(new NameValueObject("c", "C")); //$NON-NLS-1$ //$NON-NLS-2$
        RequestParams sut = new RequestParams(params);

        // Exercise
        List<NameValueObject> actual = sut.getParams();

        // Verify
        assertThat(actual, is(sameInstance(params)));

    }
}
