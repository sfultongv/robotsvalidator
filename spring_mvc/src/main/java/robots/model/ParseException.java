package robots.model;

/**
 * ParseException -
 *
 * @author Sam Griffin
 */
public class ParseException extends Exception {
	public ParseException() {
	}

	public ParseException(final String message) {
		super(message);
	}

	public ParseException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ParseException(final Throwable cause) {
		super(cause);
	}
}
