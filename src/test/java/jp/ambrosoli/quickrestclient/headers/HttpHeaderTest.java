package jp.ambrosoli.quickrestclient.headers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class HttpHeaderTest {

    @Test
    public void toStringを呼び出すと文字列表現が返されること() {

        // Setup
        HttpHeader sut = new HttpHeader("name", "value"); //$NON-NLS-1$ //$NON-NLS-2$

        // Exercise
        String actual = sut.toString();

        // Verify
        assertThat(actual, is(equalTo("HttpHeader[" + File.separator + "  name=name" + File.separator + "  value=value" //$NON-NLS-1$
                + File.separator + "]")));
    }
}
