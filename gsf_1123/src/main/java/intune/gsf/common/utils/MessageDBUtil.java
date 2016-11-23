package intune.gsf.common.utils;

import java.util.List;

import intune.gsf.model.Message;
import intune.gsf.model.Messagelang;

/**
 * DB에서 관리되는 메세지를 메모리에서 조회
 * 
 * @Pagckage Name : gcp.common.util
 * @FileName : MessageDBUtil.java
 * @author : victor
 * @date : 2016. 4. 20.
 * @description :
 */
public class MessageDBUtil {

	private static List<Message> messageListAll;

	public static String getMessage(String msgCd) {
		return getMessage(msgCd, LocaleUtil.getCurrentLanguage());
	}

	public static String getMessage(String msgCd, String langCd) {
		for (Message msg : messageListAll) {
			List<Messagelang> msgLangList = msg.getMessagelangs();
			if (langCd != null) {
				for (Messagelang msgLang : msgLangList) {
					if (msgLang.getLangCd().equals(langCd) && msgLang.getMsgCd().equals(msgCd)) {
						return msgLang.getMsg();
					}
				}
			}

			if (msg.getMsgCd().equals(msgCd)) {
				return msg.getMsg();
			}
		}

		return null;
	}

	public static List<Message> getMessageListAll() {
		return messageListAll;
	}

	public static void setMessageListAll(List<Message> messageListAll) {
		MessageDBUtil.messageListAll = messageListAll;
	}

}
