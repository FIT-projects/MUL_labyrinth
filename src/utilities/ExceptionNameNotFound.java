package utilities;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Name of tile not found
 */
public class ExceptionNameNotFound extends Exception {


	/**
	 * For serialization (warning removed)
	 */
	private static final long serialVersionUID = 2736746523387671412L;

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
