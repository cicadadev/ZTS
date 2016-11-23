package intune.gsf.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import intune.gsf.common.utils.LocaleUtil;

@Controller("baseController")
public class BaseController extends MultiActionController {

	protected final Log	logger	= LogFactory.getLog(getClass());

	/**
	 * handling http request
	 */
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, Exception {

		ModelAndView mv = null;
		// start method that named "init"
		logger.debug("=====basecon==================================");
		mv = this.invokeNamedMethod("init", req, res);
		return mv;
	}


	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		
		return request;
	}

	public String getCurrentLanguage() {
		return LocaleUtil.getCurrentLanguage();
	}

	public Locale getCurrentLocale() {
		return LocaleUtil.getCurrentLocale();
	}


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return null;
	}
	
//	public String validation(HashMap<String, Object> item) {
//		String errorMsg = "";
//		
//		if(item != null) {
//			Set<String> keys = item.keySet();
//			for(String key : keys) {
//				errorMsg = ValidationUtil.isValid(key, item.get(key));
//				if(!CommonUtil.isEmpty(errorMsg)) {
//					return errorMsg;
//				}
//			}
//		}
//		
//		return errorMsg;
//	}
	
}
