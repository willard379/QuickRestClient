package jp.ambrosoli.quickrestclient.unittest;

import static jp.ambrosoli.quickrestclient.unittest.DataSourceKeys.*;
import static jp.ambrosoli.quickrestclient.unittest.DataSourceProperties.*;

import java.net.URI;
import java.text.MessageFormat;

import jp.ambrosoli.quickrestclient.util.StringUtil;
import jp.ambrosoli.quickrestclient.util.URIUtil;

/**
 * ユニットテスト用のデータソースを管理するクラスです。
 * 
 * @author willard379
 * @since 0.3.0
 */
public class DataSource {

    public static String url() {
        String scheme = getProperty(SERVER_SCHEME);
        String host = getProperty(SERVER_HOST);
        String port = getProperty(SERVER_PORT);
        String contextPath = getProperty(SERVER_CONTEXTPATH);

        return MessageFormat.format("{0}://{1}:{2}/{3}/", scheme, host, port, contextPath); //$NON-NLS-1$
    }

    public static String url(final String pathInfo) {
        URI uri = URIUtil.toURI(url());
        if (StringUtil.isNotEmpty(pathInfo)) {
            uri = uri.resolve(pathInfo);
        }
        return uri.toString();
    }
}
