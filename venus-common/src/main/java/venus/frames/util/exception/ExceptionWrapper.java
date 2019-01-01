/*
 * Created on 2004-12-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package venus.frames.util.exception;

/**
 * @author sundaiyong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExceptionWrapper {
	
	public static String Code = "Code";
	public static String Type = "Type";
	public static String Severity = "Severity";
	public static String Description = "Description";
	public static String Message = "Message";
	
	private ExceptionInfo exceptionInfo;
	
	private Throwable cause;
	
	public ExceptionWrapper(ExceptionInfo exceptionInfo, Throwable cause) {
		this.exceptionInfo = exceptionInfo;
		this.cause = cause;
	}
	
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return exceptionInfo == null ? null : this.exceptionInfo.getCode();
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return exceptionInfo == null ? null : this.exceptionInfo.getDescription();
	}

	/**
	 * @return Returns the severity.
	 */
	public int getSeverity() {
		return exceptionInfo == null ? 0 : this.exceptionInfo.getSeverity();
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return exceptionInfo == null ? null : this.exceptionInfo.getType();
	}

	/**
	 * @return Returns the cause.
	 */
	public Throwable getCause() {
		return cause;
	}
	
	/**
	 * 
	 * @return Return the messgae
	 */
	public String getMessage() {
		return cause == null ? null : cause.getMessage();
	}
}
