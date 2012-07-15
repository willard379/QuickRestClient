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
package jp.ambrosoli.quickrestclient.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;

public class URIUtilTest {

    @Test
    public void isSSLの引数にhttpスキームのURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("http://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にhttpsスキームのURIを渡した場合_trueが返されること() {

        // Setup
        URI uri = URI.create("https://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(true));
    }

    @Test
    public void isSSLの引数にftpスキームのURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("ftp://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にデタラメなURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("willard379");

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にURI型のnullを渡した場合_falseが返されること() {

        // Setup
        URI uri = null;

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にhttpスキームのURI文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = "http://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にhttpsスキームのURI文字列を渡した場合_trueが返されること() {

        // Setup
        String uri = "https://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(true));
    }

    @Test
    public void isSSLの引数にftpスキームのURI文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = null;

        // Exercise
        boolean actual = URIUtil.isSSL(uri);
        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にURIでない文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = "ftp://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isSSLの引数にString型のnullを渡した場合_falseが返されること() {

        // Setup
        String uri = "willard379";

        // Exercise
        boolean actual = URIUtil.isSSL(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にhttpスキームのURIを渡した場合_trueが返されること() {

        // Setup
        URI uri = URI.create("http://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(true));
    }

    @Test
    public void isPlainの引数にhttpsスキームのURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("https://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にftpスキームのURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("ftp://www.ambrosoli.jp/");

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にデタラメなURIを渡した場合_falseが返されること() {

        // Setup
        URI uri = URI.create("willard379");

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にURI型のnullを渡した場合_falseが返されること() {

        // Setup
        URI uri = null;

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にhttpスキームのURI文字列を渡した場合_trueが返されること() {

        // Setup
        String uri = "http://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(true));
    }

    @Test
    public void isPlainの引数にhttpsスキームのURI文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = "https://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にftpスキームのURI文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = null;

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数にURIでない文字列を渡した場合_falseが返されること() {

        // Setup
        String uri = "ftp://www.ambrosoli.jp/";

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void isPlainの引数に文字列型のnullを渡した場合_falseが返されること() {

        // Setup
        String uri = "willard379";

        // Exercise
        boolean actual = URIUtil.isPlain(uri);

        // Verify
        assertThat(actual, is(false));
    }

    @Test
    public void toURIの引数にhttpスキームのURI文字列を渡した場合_URIが生成されること() {

        // Setup
        String uri = "http://www.ambrosoli.jp/";

        // Exercise
        URI actual = URIUtil.toURI(uri);

        // Verify
        assertThat(actual.toString(), is(equalTo(uri)));
    }

    @Test
    public void toURIの引数にhttpsスキームのURI文字列を渡した場合_URIが生成されること() {

        // Setup
        String uri = "https://www.ambrosoli.jp/";

        // Exercise
        URI actual = URIUtil.toURI(uri);

        // Verify
        assertThat(actual.toString(), is(equalTo(uri)));
    }

    @Test
    public void toURIの引数にnullを渡した場合_nullが返されること() {

        // Setup
        String uri = null;

        // Exercise
        URI actual = URIUtil.toURI(uri);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void toURIの引数に空文字を渡した場合_nullが返されること() {

        // Setup
        String uri = "";

        // Exercise
        URI actual = URIUtil.toURI(uri);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getPortの引数にポート番号8080を指定したhttpスキームのURIを渡した場合_8080が返されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp:8080/");

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(8080));
    }

    @Test
    public void getPortの引数にポート番号を指定しないhttpスキームのURIを渡した場合_80が返されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(80));
    }

    @Test
    public void getPortの引数にポート番号5432を指定したhttpsスキームのURIを渡した場合_5432が返されること() {

        // Setup
        URI uri = URIUtil.toURI("https://www.ambrosoli.jp:5432/");

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(5432));
    }

    @Test
    public void getPortの引数にポート番号を指定しないhttpsスキームのURIを渡した場合_443が返されること() {

        // Setup
        URI uri = URIUtil.toURI("https://www.ambrosoli.jp/");

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(443));
    }

    @Test
    public void getPortの引数にnullを渡した場合_マイナス1が返されること() {

        // Setup
        URI uri = null;

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(-1));
    }

    @Test
    public void getPortの引数にftpスキームのURIを渡した場合_マイナス1が返されること() {

        // Setup
        URI uri = URIUtil.toURI("ftp://www.ambrosoli.jp/");

        // Exercise
        int actual = URIUtil.getPort(uri);

        // Verify
        assertThat(actual, is(-1));
    }

    @Test
    public void addQueryStringの引数にURIとクエリストリングを渡すと_クエリストリングが追加されたURIが返されること() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        String queryString = "?a=A";

        // Exercise
        URI actual = URIUtil.addQueryString(uri, queryString);

        // Verify
        assertThat(actual.toString(), is(equalTo("http://www.ambrosoli.jp/?a=A")));
    }

    @Test
    public void addQueryStringの引数にURIとnullを渡すと_クエリストリングが追加されないこと() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        String queryString = null;

        // Exercise
        URI actual = URIUtil.addQueryString(uri, queryString);

        // Verify
        assertThat(actual.toString(), is(equalTo("http://www.ambrosoli.jp/")));
    }

    @Test
    public void addQueryStringの引数にURIと空文字を渡すと_クエリストリングが追加されないこと() {

        // Setup
        URI uri = URIUtil.toURI("http://www.ambrosoli.jp/");
        String queryString = "";

        // Exercise
        URI actual = URIUtil.addQueryString(uri, queryString);

        // Verify
        assertThat(actual.toString(), is(equalTo("http://www.ambrosoli.jp/")));
    }

    @Test
    public void addQueryStringの引数にnullとクエリストリングを渡すと_nullが返されること() {

        // Setup
        URI uri = null;
        String queryString = "?a=A";

        // Exercise
        URI actual = URIUtil.addQueryString(uri, queryString);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void addQueryStringの引数がどちらもnullの場合_nullが返されること() {

        // Setup
        URI uri = null;
        String queryString = null;

        // Exercise
        URI actual = URIUtil.addQueryString(uri, queryString);

        // Verify
        assertThat(actual, is(nullValue()));
    }
}
