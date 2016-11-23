/**
 * 
 */
package intune.gsf.common.supports;

import org.springframework.stereotype.Component;

/**
 * @author victor
 * 
 */
@Component("messageSource")
public interface MessageSource {

	public String getMessageLocale(String code, String langCd);
	
	public String isValid(String fieldCd, Object value);
}
