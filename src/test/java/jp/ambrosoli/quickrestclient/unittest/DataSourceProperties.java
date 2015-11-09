package jp.ambrosoli.quickrestclient.unittest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import jp.ambrosoli.quickrestclient.exception.IORuntimeException;
import jp.ambrosoli.quickrestclient.util.StringUtil;

/**
 * システムプロパティ または datasource.propertiesにアクセスするためのクラスです。
 * システムプロパティとdatasource.propertiesを総称してデータソースプロパティと呼びます。
 * 
 * @author Willard379
 * @since 0.3.0
 *
 */
public class DataSourceProperties {

	private static final Properties testProperty = new Properties();

	static {
		try {
			testProperty.load(DataSourceProperties.class.getClassLoader()
					.getResourceAsStream("datasource.properties"));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 指定されたキーを持つプロパティが存在するかを返します。
	 * 
	 * @param key
	 *            プロパティキー
	 * @return 指定されたキーを持つ値が存在する場合{@code true}、存在しない場合{@code false}
	 */
	public static boolean containsKey(String key) {
		return containsSystemProperty(key) || containsTestProperty(key);
	}

	/**
	 * 指定されたキーを持つシステムプロパティが存在するかを返します。
	 * 
	 * @param key
	 *            システムプロパティキー
	 * @return 指定されたキーを持つ値がシステムプロパティに存在する場合{@code true}、存在しない場合{@code false}
	 */
	public static boolean containsSystemProperty(String key) {
		return StringUtil.isNotEmpty(System.getProperty(key));
	}

	/**
	 * 指定されたキーを持つテストプロパティが存在するかを返します。
	 * 
	 * @param key
	 *            テストプロパティキー
	 * @return 指定されたキーを持つ値がテストプロパティに存在する場合{@code true}、存在しない場合{@code false}
	 */
	public static boolean containsTestProperty(String key) {
		return StringUtil.isNotEmpty(testProperty.getProperty(key));
	}

	/**
	 * 指定されたキーを持つプロパティーを返します。
	 * 
	 * @param key
	 *            プロパティキー
	 * @return 指定されたキーを持つ値
	 */
	public static String getProperty(String key) {
		return containsSystemProperty(key) ? getSystemProperty(key)
				: getTestProperty(key);
	}

	/**
	 * 指定されたキーを持つシステムプロパティーを返します。
	 * 
	 * @param key
	 *            プロパティキー
	 * @return 指定されたキーを持つ値
	 */
	public static String getSystemProperty(String key) {
		String property = System.getProperty(key);
		if (StringUtil.isEmpty(property)) {
			throw new PropertyNotFoundException(MessageFormat.format(
					"Property not found on system property. key:[{0}]", key));
		}
		return property;
	}

	/**
	 * 指定されたキーを持つテストプロパティーを返します。
	 * 
	 * @param key
	 *            　プロパティキー
	 * @return　指定されたキーを持つ値
	 */
	public static String getTestProperty(String key) {
		String property = testProperty.getProperty(key);
		if (StringUtil.isEmpty(property)) {
			throw new PropertyNotFoundException(MessageFormat.format(
					"Property not found  datasource.property. key:[{0}]", key));
		}
		return property;
	}

}
