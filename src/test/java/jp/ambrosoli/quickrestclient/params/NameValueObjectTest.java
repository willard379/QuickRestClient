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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NameValueObjectTest {

    @Test
    public void asListの引数にMapを渡した場合_Mapの内容からNameValueObjectのListが生成されて返されること() {

        // Setup
        Map<String, String> src = new HashMap<String, String>();
        src.put("param1", "value1");
        src.put("param2", "value2");
        src.put("param3", "value3");
        src.put("param4", "value4");

        // Exercise
        List<NameValueObject> actual = NameValueObject.asList(src);

        // Verify
        assertThat(actual.size(), is(src.size()));

    }

    @Test
    public void asListの引数にMap型のnullを渡した場合_nullが返されること() {

        // Setup
        Map<String, String> src = null;

        // Exercise
        List<NameValueObject> actual = NameValueObject.asList(src);

        // Verify
        assertThat(actual, is(nullValue()));

    }

    @Test
    public void asListの引数に空のMapを渡した場合_nullが返されること() {

        // Setup
        Map<String, String> src = new HashMap<String, String>(0);

        // Exercise
        List<NameValueObject> actual = NameValueObject.asList(src);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void asListの引数にNameValueObjectの配列を渡した場合_NameValueObjectのListが生成されて返されること() {

        // Setup
        NameValueObject[] ary = new NameValueObject[5];
        ary[0] = new NameValueObject("Name1", "Value1");
        ary[1] = new NameValueObject("Name2", "Value2");
        ary[2] = new NameValueObject("Name3", "Value3");
        ary[3] = new NameValueObject("Name4", "Value4");
        ary[4] = new NameValueObject("Name5", "Value5");

        // Exercise
        List<NameValueObject> actual = NameValueObject.asList(ary);

        // Verify
        assertThat(actual.size(), is(ary.length));
        for (int i = 0; i < ary.length; i++) {
            assertThat(actual.get(i).getName(), is(equalTo(ary[i].getName())));
            assertThat(actual.get(i).getValue(), is(equalTo(ary[i].getValue())));
        }

        assertThat(NameValueObject.asList((NameValueObject[]) null), is(nullValue()));
        assertThat(NameValueObject.asList(new NameValueObject[0]).isEmpty(), is(true));
    }

    @Test
    public void asListの引数にNameValueObject配列型のnullを渡した場合_nullが返されること() {

        // Setup
        NameValueObject[] ary = null;

        // Exercise
        List<NameValueObject> actual = NameValueObject.asList(ary);

        // Verify
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void toStringを呼び出すと文字列表現が返されること() {

        // Setup
        NameValueObject sut = new NameValueObject("name", "value");

        // Exercise
        String actual = sut.toString();

        // Verify
        assertThat(actual, is(equalTo("NameValueObject[\n  name=name\n  value=value\n]")));
    }
}
