package common.util;

public class ResourceException extends Exception {
	@SuppressWarnings("compatibility:6016697102146882818")
	private static final long serialVersionUID = 1L;

	public ResourceException(Throwable throwable) {
		super(throwable);
	}

	public ResourceException(String string, Throwable throwable) {
		super(string, throwable);
	}

	String param;

	public ResourceException(String rs_msg) {
		super(rs_msg);
		this.message = rs_msg;
	}

	public ResourceException(String rs_msg, String param) {
		super(rs_msg);
		this.message = rs_msg;
		this.param = param;
	}

	public ResourceException() {
		super();
	}

	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	private Object[] params;

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Object[] getParams() {
		return params;
	}

	public ResourceException(String message, Object[] params) {
		super(message);
		this.message = message;
		this.params = params;
	}
}