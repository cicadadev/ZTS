package intune.gsf.common.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

import intune.gsf.model.Message;

/**
 * 메세지 properties로 부터 메세지 조회 ( 다국어 )
 * 
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : MessageUtils.java
 * @author : eddie
 * @date : 2016. 4. 25.
 * @description :
 */
public class MessageUtil {

	private static MessageSourceAccessor	messageSourceAccessor;

	@Autowired(required = true)
	public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
		MessageUtil.messageSourceAccessor = messageSourceAccessor;
	}

	public static String getMessage(String code) {
		return messageSourceAccessor.getMessage(code, LocaleUtil.getCurrentLocale());
	}

	public static String getMessage(String code, Object[] args) {
		return messageSourceAccessor.getMessage(code, args, LocaleUtil.getCurrentLocale());
	}

	public static String getMessage(String code, Locale locale) {
		return messageSourceAccessor.getMessage(code, locale);
	}

	public static String getMessage(String code, Object[] args, Locale locale) {
		return messageSourceAccessor.getMessage(code, args, locale);
	}

	public static String getMessage(String code, Object[] args, String defaultMessage) {
		return messageSourceAccessor.getMessage(code, args, defaultMessage, LocaleUtil.getCurrentLocale());
	}
	
	public static Map<String, String> getMessages(String[] codes) {

		Map<String, String> messageMap = new HashMap<String, String>();

		for (String code : codes) {
			String message = "";
			try {
				message = messageSourceAccessor.getMessage(code, LocaleUtil.getCurrentLocale());
			} catch (Exception e) {

			}
			messageMap.put(code, message);
		}
		return messageMap;
	}

}
