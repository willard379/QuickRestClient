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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.ambrosoli.quickrestclient.exception.UnsupportedEncodingRuntimeException;

public class StringUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void isEmptyの引数にnullを渡した場合_trueが返されること() {
        assertThat(StringUtil.isEmpty(null), is(true));
    }

    @Test
    public void isEmptyの引数に空文字を渡した場合_trueが返されること() {
        assertThat(StringUtil.isEmpty(""), is(true));
    }

    @Test
    public void isEmptyの引数にホワイトスペースを渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty(" "), is(false));
    }

    @Test
    public void isEmptyの引数にブランクでない文字列を渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty("bob"), is(false));
    }

    @Test
    public void isEmptyの引数に前後にホワイトスペースを含むブランクでない文字列を渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty("  bob  "), is(false));
    }

    @Test
    public void isEmptyにStringBuilder型のnullを渡した場合_trueが返されること() {
        assertThat(StringUtil.isEmpty((StringBuilder) null), is(true));
    }

    @Test
    public void isEmptyに空のStringBuilderを渡した場合_trueが返されること() {
        assertThat(StringUtil.isEmpty(new StringBuilder()), is(true));
    }

    @Test
    public void isEmptyの引数にホワイトスペースの文字列を持つStringBuilderを渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty(new StringBuilder(" ")), is(false));
    }

    @Test
    public void isEmptyの引数にブランクでない文字列を持つStringBuilderを渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty(new StringBuilder("bob")), is(false));
    }

    @Test
    public void isEmptyの引数に前後にホワイトスペースを含むブランクでない文字列を持つStringBuilderを渡した場合_falseが返されること() {
        assertThat(StringUtil.isEmpty(new StringBuilder("  bob  ")), is(false));
    }

    @Test
    public void isNotEmptyの引数にnullを渡した場合_falseが返されること() {
        assertThat(StringUtil.isNotEmpty(null), is(false));
    }

    @Test
    public void isNotEmptyの引数に空文字を渡した場合_falseが返されること() {
        assertThat(StringUtil.isNotEmpty(""), is(false));
    }

    @Test
    public void isNotEmptyの引数にホワイトスペースを渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty(" "), is(true));
    }

    @Test
    public void isNotEmptyの引数にブランクでない文字列を渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty("bob"), is(true));
    }

    @Test
    public void isNotEmptyの引数に前後にホワイトスペースを含むブランクでない文字列を渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty("  bob  "), is(true));
    }

    @Test
    public void isNotEmptyにStringBuilder型のnullを渡した場合_falseが返されること() {
        assertThat(StringUtil.isNotEmpty((StringBuilder) null), is(false));
    }

    @Test
    public void isNotEmptyに空のStringBuilderを渡した場合_falseが返されること() {
        assertThat(StringUtil.isNotEmpty(new StringBuilder("")), is(false));
    }

    @Test
    public void isNotEmptyの引数にホワイトスペースの文字列を持つStringBuilderを渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty(new StringBuilder(" ")), is(true));
    }

    @Test
    public void isNotEmptyの引数にブランクでない文字列を持つStringBuilderを渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty(new StringBuilder("bob")), is(true));
    }

    @Test
    public void isNotEmptyの引数に前後にホワイトスペースを含むブランクでない文字列を持つStringBuilderを渡した場合_trueが返されること() {
        assertThat(StringUtil.isNotEmpty(new StringBuilder("  bob  ")), is(true));
    }

    @Test
    public void toStringを呼び出した場合_引数で渡されたバイト配列が指定したエンコーディングの文字列に変換して返されること() throws Exception {

        // Setup
        // ↓全部サロゲートペア
        byte[] data = "𠀋𡈽𡌛𡑮𡢽𠮟𡚴𡸴𣇄𣗄".getBytes("UTF-16");

        // Exercise
        String result = StringUtil.toString(data, "UTF-16");

        // Verify
        assertThat(result, is(equalTo("𠀋𡈽𡌛𡑮𡢽𠮟𡚴𡸴𣇄𣗄")));
    }

    @Test
    public void toStringの引数に空のバイト配列を渡した場合_空文字が返されること() throws Exception {

        // Setup
        byte[] data = new byte[0];

        // Exercise
        String result = StringUtil.toString(data, "UTF-8");

        // Verify
        assertThat(result, is(equalTo("")));
    }

    @Test
    public void toStringの引数にnullを渡した場合_nullが返されること() throws Exception {

        // Setup
        byte[] data = null;

        // Exercise
        String result = StringUtil.toString(data, "UTF-8");

        // Verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void toStringの第二引数にnullを渡した場合_第一引数で渡されたbyte配列がUTF8の文字列に変換されて返されること() throws Exception {

        // Setup
        byte[] data = "ロマンス".getBytes("UTF-8");

        // Exercise
        String result = StringUtil.toString(data, null);

        // Verify
        assertThat(result, is(equalTo("ロマンス")));
    }

    @Test
    public void toStringの第二引数に存在しないエンコーディングを指定した場合_UnsupportedEncodingRuntimeExcetionが発生すること() throws Exception {

        // Setup
        byte[] data = "ハネムーン".getBytes("UTF-8");

        this.expectedException.expect(is(instanceOf(UnsupportedEncodingRuntimeException.class)));

        // Exercise
        StringUtil.toString(data, "UTF-48");

        // Verify
        fail("UnsupportedEncodingRuntimeExceptionが発生しませんでした。");
    }
}
