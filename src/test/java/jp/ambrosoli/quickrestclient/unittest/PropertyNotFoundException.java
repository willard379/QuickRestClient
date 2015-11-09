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

	public PropertyNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}

}
