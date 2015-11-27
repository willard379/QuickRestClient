package jp.ambrosoli.quickrestclient.unittest;

/**
 * データソースプロパティが存在しないことを示す例外です。
 * 
 * @author willard379
 * @since 0.3.0
 */
public class PropertyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PropertyNotFoundException() {
        super();
    }

    public PropertyNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PropertyNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PropertyNotFoundException(final String message) {
        super(message);
    }

    public PropertyNotFoundException(final Throwable cause) {
        super(cause);
    }

}
