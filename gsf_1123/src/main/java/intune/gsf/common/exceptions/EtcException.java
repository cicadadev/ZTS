package intune.gsf.common.exceptions;

public class EtcException extends BaseException {

	private static final long	serialVersionUID	= 1L;

	public EtcException(String messageKey) {
		super(messageKey);
	}

	public EtcException(String messageKey, Object[] args) {
		super(messageKey, args);
	}

	public EtcException(String messageKey, Throwable causeThrowable) {
		super(messageKey, causeThrowable);
	}

	public EtcException(String businessLayer, String code) {
		super(businessLayer, code);
	}

	public EtcException(String businessLayer, String code, Throwable cause) {
		super(businessLayer, code, cause);
	}

	public EtcException(String businessLayer, String code, Object[] args) {
		super(businessLayer, code, args);
	}

	public EtcException(String businessLayer, String code, Object[] args, Throwable cause) {
		super(businessLayer, code, args, cause);
	}
}
