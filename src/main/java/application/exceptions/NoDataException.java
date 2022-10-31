package application.exceptions;

public class NoDataException extends Exception {
	private static final long serialVersionUID = 2827021250161110024L;

	public NoDataException(String message) {
		super(message);
	}
}
