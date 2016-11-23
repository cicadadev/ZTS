package intune.gsf.common.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class DefaultException implements HandlerExceptionResolver {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String view = null;
	
	public void setView(String view) {
		this.view = view;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		request.setAttribute("exception", ex.getMessage());
		return new ModelAndView(view);
	}

}
