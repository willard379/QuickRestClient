package jp.ambrosoli.quickrestclient.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
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
        assertThat(actual, is(equalTo("<null>"))); //$NON-NLS-1$
    }

    @Test
    public void 引数にフィールドを持たないオブジェクトを渡した場合_クラス名に続いて空のブラケットが設定されること() throws Exception {

        // Setup
        Object obj = new Object();

        // Exercise
        String actual = ReflectionToStringBuilder.toString(obj);

        // Verify
        assertThat(actual, is(equalTo("Object[" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void 引数にフィールドを持つオブジェクトを渡した場合_ブラケットの中にフィールド名と値が設定されること() {

        // Setup
        Hoge object = new Hoge("value"); //$NON-NLS-1$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[" + File.separator + "  name=value" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void 引数に複数のフィールドを持つオブジェクトを渡した場合_各フィールド名と値が改行で区切られて設定されること() {

        // Setup
        MultiFieldHoge object = new MultiFieldHoge("value1", "value2", "value3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("MultiFieldHoge[" + File.separator + "  name1=value1" + File.separator //$NON-NLS-1$
                + "  name2=value2" + File.separator + "  name3=value3" + File.separator + "]")));
    }

    @Test
    public void フィールドの値がnullの場合_nullという文字列が不等号に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(null);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[" + File.separator + "  name=<null>" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void スーパークラスから継承したフィールドの情報も設定されること() {

        // Setup
        Foo object = new Foo("fooValue", "hogeValue"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Foo[" + File.separator + "  name=fooValue" + File.separator + "  name=hogeValue" //$NON-NLS-1$
                + File.separator + "]")));
    }

    @Test
    public void int型のフィールドを持つオブジェクトを指定した場合_フィールド情報が正しく設定されること() {

        // Setup
        Hoge object = new Hoge(1);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[" + File.separator + "  name=1" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void 配列型のフィールドを持つオブジェクトを指定した場合_配列の内容が波括弧に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(new String[] { "value1", "value2", "value3", }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入しない
        assertThat(actual,
                is(equalTo("Hoge[" + File.separator + "  name={value1,value2,value3}" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void nullを含む配列型のフィールドを持つオブジェクトを指定した場合_nullという文字列が不等号に囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(new String[] { "value1", "value2", "value3", null, }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入しない
        assertThat(actual,
                is(equalTo("Hoge[" + File.separator + "  name={value1,value2,value3,<null>}" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void List型のフィールドを持つオブジェクトを指定した場合_Listの内容がブラケットに囲まれて設定されること() {

        // Setup
        Hoge object = new Hoge(Arrays.asList("value1", "value2", "value3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入する。
        assertThat(actual,
                is(equalTo("Hoge[" + File.separator + "  name=[value1, value2, value3]" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void nullを含むList型のフィールドを持つオブジェクトを指定した場合_nullという文字列がそのまま設定されること() {

        // Setup
        Hoge object = new Hoge(Arrays.asList("value1", "value2", "value3", null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        // commons-langのToStringBuilderの仕様に基づき、値同士の間にスペースを挿入する。
        assertThat(actual,
                is(equalTo("Hoge[" + File.separator + "  name=[value1, value2, value3, null]" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void Map型のフィールドを持つオブジェクトを指定した場合_Mapの内容が波括弧に囲まれてkeyイコールvalue形式で出力されること() {

        // Setup
        @SuppressWarnings("serial")
        Hoge object = new Hoge(new LinkedHashMap<String, String>() {
            {
                this.put("key1", "value1"); //$NON-NLS-1$ //$NON-NLS-2$
                this.put("key2", "value2"); //$NON-NLS-1$ //$NON-NLS-2$
                this.put("key3", "value3"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo(
                "Hoge[" + File.separator + "  name={key1=value1, key2=value2, key3=value3}" + File.separator + "]"))); //$NON-NLS-1$
    }

    @Test
    public void nullを含むMap型のフィールドを持つオブジェクトを指定した場合_nullという文字列がそのまま設定されること() {

        // Setup
        @SuppressWarnings("serial")
        Hoge object = new Hoge(new LinkedHashMap<String, String>() {
            {
                this.put("key1", "value1"); //$NON-NLS-1$ //$NON-NLS-2$
                this.put("key2", "value2"); //$NON-NLS-1$ //$NON-NLS-2$
                this.put("key3", "value3"); //$NON-NLS-1$ //$NON-NLS-2$
                this.put(null, null);
            }
        });

        // Exercise
        String actual = ReflectionToStringBuilder.toString(object);

        // Verify
        assertThat(actual, is(equalTo("Hoge[" + File.separator //$NON-NLS-1$
                + "  name={key1=value1, key2=value2, key3=value3, null=null}" + File.separator + "]")));
    }

    @Test
    public void オブジェクト型のフィールドを持つオブジェクトを指定した場合_完全修飾クラス名とハッシュコードが設定されること() {

        // Setup
        Object obj = new Object();
        Hoge hoge = new Hoge(obj);

        // Exercise
        String actual = ReflectionToStringBuilder.toString(hoge);

        // Verify
        assertThat(actual,
                is(equalTo("Hoge[" + File.separator + "  name=" + obj.toString() + "" + File.separator + "]"))); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void staticなフィールドを持つクラスを渡した場合_該当のフィールドは出力されないこと() {

        // Setup
        Bar bar = new Bar();

        // Exercise
        String actual = ReflectionToStringBuilder.toString(bar);

        // Verify
        assertThat(actual, is(equalTo("Bar[" + File.separator + "  memberName=memberValue" + File.separator + "]"))); //$NON-NLS-1$
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
    public static String staticName = "staticValue"; //$NON-NLS-1$
    public String memberName = "memberValue"; //$NON-NLS-1$

}