package jp.ambrosoli.quickrestclient.headers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class HttpHeaderTest {

    @Test
    public void toStringを呼び出すと文字列表現が返されること() {

        // Setup
        HttpHeader sut = new HttpHeader("name", "value");

        // Exercise
        String actual = sut.toString();

        // Verify
        assertThat(actual, is(equalTo("HttpHeader[\n  name=name\n  value=value\n]")));
    }
}
