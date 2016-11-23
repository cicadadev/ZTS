package intune.gsf.common.supports;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @author victor
 */
@Component("databaseMessageResource")
public class DatabaseMessageResource extends AbstractMessageSource implements ResourceLoaderAware {

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat mf = null;
		String langCd = locale.getLanguage();
		String message = messageSource.getMessageLocale(code, langCd);
		if (message == null) {
			mf = new MessageFormat(code);
		} else {
			mf = new MessageFormat(message, locale);
		}
		return mf;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		
	}
}
