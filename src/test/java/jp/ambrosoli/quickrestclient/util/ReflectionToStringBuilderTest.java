package jp.ambrosoli.quickrestclient.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.Test;

public class ReflectionToStringBuilderTest {

    @Test
    public void 引数にnullを渡した場合_nullという文字列が不等号に囲まれて設定されること() throws Exception {

        // Setup
        Object obj = null;

        // Exercise
        String actual = ReflectionToStringBuilder.toString(obj);

        // Verify
        assertThat(actual, is(equalTo("<null>")));
    }

    @Test
    public void 引数にフィールドを持たないオブジェクトを渡した場合_クラス名に続いて空のブラケットが設定されること() throws Exception {

        // Setup
        Object obj = new Object();

        // Exercise
        String actual = ReflectionToStringBuilder.toString(obj);

        // Verify
        assertThat(actual, is(equalTo("Object[\n]")));
    }

    @Test
    public void 引数にフィールドを持つオブジェクトを渡した場合_ブラケットの中にフィールド名と値が設定されること() {

        // Setup
        Hoge object = new Hoge("value");

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[\n  name=value\n]")));
    }

    @Test
    public void 引数に複数のフィールドを持つオブジェクトを渡した場合_各フィールド名と値が改行で区切られて設定されること() {

        // Setup
        MultiFieldHoge object = new MultiFieldHoge("value1", "value2", "value3");

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual,
                is(equalTo("MultiFieldHoge[\n  name1=value1\n  name2=value2\n  name3=value3\n]")));
    }

    @Test
    public void フィールドの値がnullの場合_nullという文字列が不等号に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(null);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[\n  name=<null>\n]")));
    }

    @Test
    public void スーパークラスから継承したフィールドの情報も設定されること() {

        // Setup
        Foo object = new Foo("fooValue", "hogeValue");

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Foo[\n  name=fooValue\n  name=hogeValue\n]")));
    }

    @Test
    public void int型のフィールドを持つオブジェクトを指定した場合_フィールド情報が正しく設定されること() {

        // Setup
        Hoge object = new Hoge(1);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[\n  name=1\n]")));
    }

    @Test
    public void 配列型のフィールドを持つオブジェクトを指定した場合_配列の内容が波括弧に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(new String[] { "value1", "value2", "value3", });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入しない
        assertThat(actual, is(equalTo("Hoge[\n  name={value1,value2,value3}\n]")));
    }

    @Test
    public void nullを含む配列型のフィールドを持つオブジェクトを指定した場合_nullという文字列が不等号に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(new String[] { "value1", "value2", "value3", null, });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入しない
        assertThat(actual, is(equalTo("Hoge[\n  name={value1,value2,value3,<null>}\n]")));
    }

    @Test
    public void List型のフィールドを持つオブジェクトを指定した場合_Listの内容がブラケットに囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(Arrays.asList("value1", "value2", "value3"));

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入する。
        assertThat(actual, is(equalTo("Hoge[\n  name=[value1, value2, value3]\n]")));
    }

    @Test
    public void nullを含むList型のフィールドを持つオブジェクトを指定した場合_nullという文字列がそのまま設定されること() {

        // Setup
        Hoge object = new Hoge(Arrays.asList("value1", "value2", "value3", null));

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入する。
        assertThat(actual, is(equalTo("Hoge[\n  name=[value1, value2, value3, null]\n]")));
    }

    @Test
    public void Map型のフィールドを持つオブジェクトを指定した場合_Mapの内容が波括弧に囲まれてkeyイコールvalue形式で出力されること() {

        // Setup
        @SuppressWarnings("serial")
		Hoge object = new Hoge(new LinkedHashMap<String, String>() {
            {
                this.put("key1", "value1");
                this.put("key2", "value2");
                this.put("key3", "value3");
            }
        });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[\n  name={key1=value1, key2=value2, key3=value3}\n]")));
    }

    @Test
    public void nullを含むMap型のフィールドを持つオブジェクトを指定した場合_nullという文字列がそのまま設定されること() {

        // Setup
        @SuppressWarnings("serial")
		Hoge object = new Hoge(new LinkedHashMap<String, String>() {
            {
                this.put("key1", "value1");
                this.put("key2", "value2");
                this.put("key3", "value3");
                this.put(null, null);
            }
        });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual,
                is(equalTo("Hoge[\n  name={key1=value1, key2=value2, key3=value3, null=null}\n]")));
    }

    @Test
    public void オブジェクト型のフィールドを持つオブジェクトを指定した場合_完全修飾クラス名とハッシュコードが設定されること() {

        // Setup
        Object obj = new Object();
        Hoge hoge = new Hoge(obj);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(hoge);

        // Verify
        assertThat(actual, is(equalTo("Hoge[\n  name=" + obj.toString() + "\n]")));
    }

    @Test
    public void staticなフィールドを持つクラスを渡した場合_該当のフィールドは出力されないこと() {

        // Setup
        Bar bar = new Bar();

        // Exercise
        String actual = ReflectionToStringBuilder.toString(bar);

        // Verify
        assertThat(actual, is(equalTo("Bar[\n  memberName=memberValue\n]")));
    }

}

class Hoge {
    public Hoge(final Object value) {
        this.name = value;
    }

    public Object name;
}

class MultiFieldHoge {
    public MultiFieldHoge(final Object value1, final Object value2, final Object value3) {
        this.name1 = value1;
        this.name2 = value2;
        this.name3 = value3;
    }

    public Object name1;
    public Object name2;
    public Object name3;
}

class Foo extends Hoge {
    public Foo(final Object fooValue, final Object hogeValue) {
        super(hogeValue);
        this.name = fooValue;
    }

    public Object name;
}

class Bar {
    public static String staticName = "staticValue";
    public String memberName = "memberValue";

}