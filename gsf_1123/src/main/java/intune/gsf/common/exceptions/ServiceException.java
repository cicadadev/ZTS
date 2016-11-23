package intune.gsf.common.exceptions;

public class ServiceException extends BaseException {

	private static final long	serialVersionUID	= 1L;

	public ServiceException(String messageKey) {
		super(messageKey);
	}

	public ServiceException(String messageKey, Object[] args) {
		super(messageKey, args);
	}

	public ServiceException(String messageKey, Throwable causeThrowable) {
		super(messageKey, causeThrowable);
	}

	public ServiceException(String businessLayer, String code) {
		super(businessLayer, code);
	}

	public ServiceException(String businessLayer, String code, Throwable cause) {
		super(businessLayer, code, cause);
	}

	public ServiceException(String businessLayer, String code, Object[] args) {
		super(businessLayer, code, args);
	}

	public ServiceException(String businessLayer, String code, Object[] args, Throwable cause) {
		super(businessLayer, code, args, cause);
	}
}
