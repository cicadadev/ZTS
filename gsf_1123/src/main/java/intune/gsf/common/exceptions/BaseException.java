package intune.gsf.common.exceptions;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import intune.gsf.common.utils.MessageUtil;

public class BaseException extends RuntimeException {

	private static final long	serialVersionUID	= 1L;
	protected Logger			logger				= Logger.getLogger(this.getClass());
	private String				message;
	private String				msgCd;
	private Object[]			args;

	public BaseException(String msgCd) {
		super(msgCd);
		this.msgCd = msgCd;
		this.setMessage(this.msgCd);
		logger.error(this.message);
	}

	public BaseException(String msgCd, Object[] args) {
		super(msgCd);
		this.msgCd = msgCd;
		if (ArrayUtils.isNotEmpty(args)) {
			this.setMessage(this.msgCd, args);
			this.args = args;
		} else {
			this.setMessage(this.msgCd);
		}
		logger.error(this.message);
	}

	public BaseException(String msgCd, Throwable cause) {
		super(msgCd, cause);
		this.msgCd = msgCd;
		this.setMessage(this.msgCd);
		logger.error(StringUtils.abbreviate(this.message + "\n" + ExceptionUtils.getStackTrace(cause), 2000));
	}

	public BaseException(String businessLayer, String code) {
		super("errors." + businessLayer + "." + code);
		this.msgCd = "errors." + businessLayer + "." + code;
		setMessage(this.msgCd);
		logger.error(this.message);
	}

	public BaseException(String businessLayer, String code, Throwable cause) {
		super("errors." + businessLayer + "." + code, cause);
		this.msgCd = "errors." + businessLayer + "." + code;
		this.setMessage(this.msgCd);
		logger.error(StringUtils.abbreviate(this.message + "\n" + ExceptionUtils.getStackTrace(cause), 2000));
	}

	public BaseException(String businessLayer, String code, Object[] args, Throwable cause) {
		super("errors." + businessLayer + "." + code, cause);
		this.msgCd = "errors." + businessLayer + "." + code;
		this.setMessage(this.msgCd);
		this.args = args;
		logger.error(StringUtils.abbreviate(this.message + "\n" + ExceptionUtils.getStackTrace(cause), 2000));
	}

	public BaseException(String businessLayer, String code, Object[] args) {
		super("errors." + businessLayer + "." + code);
		this.msgCd = "errors." + businessLayer + "." + code;
		if (ArrayUtils.isNotEmpty(args)) {
			this.setMessage(this.msgCd, args);
			this.args = args;
		} else {
			this.setMessage(this.msgCd);
		}
		logger.error(this.message);
	}

	private void setMessage(String code) {
		this.message = "메세지 코드[" + code + "]가 messages.properties에 정의가 되지 않았습니다. ";
		try {
			this.message = MessageUtil.getMessage(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setMessage(String code, Object[] args) {
		this.message = "메세지 코드[" + code + "]가 messages.properties에 정의가 되지 않았습니다. ";
		try {
			this.message = MessageUtil.getMessage(code, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return this.message;
	}

	public String getMessageCd() {
		return this.msgCd;
	}

	public void setMessageCd(String msgCd) {
		this.msgCd = msgCd;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
