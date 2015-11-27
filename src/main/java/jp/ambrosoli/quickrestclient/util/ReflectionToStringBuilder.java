package jp.ambrosoli.quickrestclient.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * オブジェクトの文字列表現を生成するユーティリティクラスです。
 *
 * @author willard379
 * @since 0.2.0
 */
public class ReflectionToStringBuilder {

    /** nullのラベル */
    public static String NULL_LABEL = "<null>"; //$NON-NLS-1$

    /**
     * コンストラクタ
     */
    protected ReflectionToStringBuilder() {
    }

    /**
     * objectの文字列表現を生成して返します。
     *
     * @param object
     *            対象オブジェクト
     * @return objectの文字列表現
     */
    public static <T> String toString(final T object) {
        return new ReflectionToStringBuilder().buildToString(object);
    }

    /**
     * objectの文字列表現を生成して返します。
     *
     * @param object
     *            対象のオブジェクト
     * @return objectの文字列表現
     */
    protected <T> String buildToString(final T object) {
        if (object == null) {
            return NULL_LABEL;
        }

        Class<?> cls = object.getClass();
        StringBuilder sb = new StringBuilder().append(cls.getSimpleName()).append("[").append(File.separator); //$NON-NLS-1$
        appendFields(object, sb, cls);

        while (cls.getSuperclass() != null) {
            cls = cls.getSuperclass();
            appendFields(object, sb, cls);
        }

        sb.append("]"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * フィールドの情報をsbに追加します。
     *
     * @param object
     *            対象のオブジェクト
     * @param sb
     *            StringBuilder
     * @param cls
     *            クラス
     */
    protected static <T> void appendFields(final T object, final StringBuilder sb, final Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            Object value = null;
            try {
                value = field.get(object);
            } catch (Exception e) {
                // noop
            }

            sb.append("  ").append(field.getName()).append("="); //$NON-NLS-1$ //$NON-NLS-2$
            if (value == null) {
                sb.append(NULL_LABEL).append(File.separator);
                return;
            }

            if (value.getClass().isArray()) {
                sb.append("{"); //$NON-NLS-1$
                Object[] arrayValues = (Object[]) value;
                for (Object arrayValue : arrayValues) {
                    sb.append(replaceNull(arrayValue)).append(","); //$NON-NLS-1$
                }
                sb.setLength(sb.length() - 1);
                sb.append("}").append(File.separator); //$NON-NLS-1$
            } else {
                sb.append(replaceNull(value)).append(File.separator);
            }

        }
    }

    /**
     * <p>
     * valueがnullの場合、"<null>"を返却します。 valueがnullでない場合はそのままvalueを返します。
     * </p>
     *
     * @param value
     *            値
     * @return valueがnullの場合はnullラベル、それ以外の場合はvalue
     */
    protected static Object replaceNull(final Object value) {
        return (value != null) ? value : NULL_LABEL;
    }

}
