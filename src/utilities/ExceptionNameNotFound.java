package utilities;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Name of tile not found
 */
public class ExceptionNameNotFound extends Exception {


	public ExceptionNameNotFound() {
	}

	/**
	 * Name of tile not found
	 * @param message Message of the exception
	 */
	public ExceptionNameNotFound(String message) {
		super(message);
	}

}
