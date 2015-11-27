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
package jp.ambrosoli.quickrestclient.headers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ambrosoli.quickrestclient.apache.headers.ApacheHeaderBuilder;
import jp.ambrosoli.quickrestclient.params.NameValueObject;

import org.apache.http.Header;
import org.junit.Test;

public class HttpHeadersTest {

    @Test
    public void HttpHeadersにHTTPヘッダーが登録されている状態でisEmptyを呼び出すと_falseが返却されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();
        sut.addHeader(new NameValueObject("a", "a")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void HttpHeadersにHTTPヘッダーが登録されていない状態でisEmptyを呼び出すと_trueが返却されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HttpHeadersに空のリストを設定した状態でisEmptyを呼び出すと_trueが返却されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();
        sut.addHeaders(new ArrayList<NameValueObject>());

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HttpHeadersにnullを設定した状態でisEmptyを呼び出すと_trueが返却されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();
        sut.addHeaders(null);

        // Exercise
        boolean actual = sut.isEmpty();

        // Verify
        assertThat(actual, is(true));

    }

    @Test
    public void HttpHeadersに何も設定せずにgetConformedHeadersを呼び出した場合_Header配列が生成されないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        Header[] actual = sut.getConformedHeaders(new ApacheHeaderBuilder());

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpHeadersに空のリストを登録した状態でgetConformedHeadersを呼び出すと_Header配列が生成されないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();
        sut.addHeaders(new ArrayList<NameValueObject>());

        // Exercise
        Header[] actual = sut.getConformedHeaders(new ApacheHeaderBuilder());

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpHeadersにnullを設定した状態でgetConformedHeadersを呼び出すと_Header配列が生成されないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();
        sut.addHeaders(null);

        // Exercise
        Header[] actual = sut.getConformedHeaders(new ApacheHeaderBuilder());

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void HttpHeadersにHTTPヘッダーを設定した状態でgetConformedHeadersを呼び出すと_設定したHTTPヘッダーのHeader配列が生成されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name3", "Value3")); //$NON-NLS-1$ //$NON-NLS-2$
        sut.addHeaders(values);

        // Exercise
        Header[] actual = sut.getConformedHeaders(new ApacheHeaderBuilder());

        // Verify
        assertThat(actual.length, is(values.size()));
        assertThat(actual[0].getName(), is(equalTo(values.get(0).getName())));
        assertThat(actual[0].getValue(), is(equalTo(values.get(0).getValue())));
    }

    @Test
    public void addHeaderの引数にNameValueObjectを渡すと_引数に渡したNameValueObjectがHttpHeadersに保持されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        sut.addHeader(new NameValueObject("Name3", "Value3-2")); //$NON-NLS-1$ //$NON-NLS-2$

        // Verify
        List<NameValueObject> headers = sut.getHeaders();
        assertThat(headers.size(), is(1));

        NameValueObject nvo = headers.get(0);
        assertThat(nvo.getName(), is(equalTo("Name3"))); //$NON-NLS-1$
        assertThat(nvo.getValue(), is(equalTo("Value3-2"))); //$NON-NLS-1$

    }

    @Test
    public void addHeaderの引数にnullを渡すと_nullは保持されないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        sut.addHeader(null);

        // Verify
        List<NameValueObject> headers = sut.getHeaders();
        assertThat(headers.size(), is(0));
    }

    @Test
    public void addHeadersの引数にNameValueObjectのListを渡すと_引数で渡したNameValueObjectがすべてHttpHeadersに保持されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name3", "Value3")); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        sut.addHeaders(values);

        // Verify
        List<NameValueObject> headers = sut.getHeaders();
        assertThat(headers.size(), is(3));

        for (int i = 0; i < headers.size(); i++) {
            assertThat(headers.get(i).getName(), is(equalTo(values.get(i).getName())));
            assertThat(headers.get(i).getValue(), is(equalTo(values.get(i).getValue())));
        }

    }

    @Test
    public void addHeadersの引数にnullを渡した場合_nullは保持されないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        sut.addHeaders(null);

        // Verify
        assertThat(sut.getHeaders().size(), is(0));
    }

    @Test
    public void あらかじめ２件分のヘッダーを登録した状態でaddHeadersの引数にNameValueObjectを渡すと_引数で渡したNameValueObjectがすべてHttpHeadersに追加されること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // あらかじめ2件分のヘッダーをセットしておく
        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values1.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        sut.addHeaders(values1);

        // Exercise
        // 3件分のヘッダーを追加する
        List<NameValueObject> values2 = new ArrayList<NameValueObject>();
        values2.add(new NameValueObject("Name3", "Value3")); //$NON-NLS-1$ //$NON-NLS-2$
        values2.add(new NameValueObject("Name4", "Value4")); //$NON-NLS-1$ //$NON-NLS-2$
        values2.add(new NameValueObject("Name5", "Value5")); //$NON-NLS-1$ //$NON-NLS-2$

        sut.addHeaders(values2);

        // Verify
        List<NameValueObject> headers = sut.getHeaders();
        assertThat(headers.size(), is(5));

        sut.addHeaders(null);
        assertThat(sut.getHeaders().size(), is(5));
    }

    @Test
    public void あらかじめ２件分のヘッダーを登録した状態でaddHeadersの引数にnullを渡すと_nullは追加されないこと() {

        // Setup
        // あらかじめ2件分のデータをセットしておく
        HttpHeaders sut = new HttpHeaders();

        List<NameValueObject> values1 = new ArrayList<NameValueObject>();
        values1.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values1.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        sut.addHeaders(values1);

        // Exercise
        // nullを追加する
        sut.addHeaders(null);

        // Verify
        // 件数が2件のままであること
        assertThat(sut.getHeaders().size(), is(2));
    }

    @Test
    public void HttpHeadersがHTTPヘッダーを保持していない状態でclearを呼び出すと_何も起こらないこと() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        // Exercise
        sut.clear();

        // Verify
        assertThat(sut.isEmpty(), is(true));

    }

    @Test
    public void HttpHeadersがHTTPヘッダーを保持している状態でclearを呼び出すと_HttpHeadersに保持しているHTTPヘッダーがクリアされること() {

        // Setup
        HttpHeaders sut = new HttpHeaders();

        List<NameValueObject> values = new ArrayList<NameValueObject>();
        values.add(new NameValueObject("Name1", "Value1")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name2", "Value2")); //$NON-NLS-1$ //$NON-NLS-2$
        values.add(new NameValueObject("Name3", "Value3")); //$NON-NLS-1$ //$NON-NLS-2$
        sut.addHeaders(values);

        // Exercise
        sut.clear();

        // Verify
        assertThat(sut.isEmpty(), is(true));

    }

}
