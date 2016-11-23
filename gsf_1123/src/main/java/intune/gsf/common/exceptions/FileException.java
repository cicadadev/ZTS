package intune.gsf.common.exceptions;

public class FileException extends BaseException {

	private static final long	serialVersionUID	= 1L;

	public FileException(String messageKey) {
		super(messageKey);
	}

	public FileException(String messageKey, Object[] args) {
		super(messageKey, args);
	}

	public FileException(String messageKey, Throwable causeThrowable) {
		super(messageKey, causeThrowable);
	}

	public FileException(String businessLayer, String code) {
		super(businessLayer, code);
	}

	public FileException(String businessLayer, String code, Throwable cause) {
		super(businessLayer, code, cause);
	}

	public FileException(String businessLayer, String code, Object[] args) {
		super(businessLayer, code, args);
	}

	public FileException(String businessLayer, String code, Object[] args, Throwable cause) {
		super(businessLayer, code, args, cause);
	}
}
