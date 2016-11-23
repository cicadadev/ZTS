package intune.gsf.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
		
		if(req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) rep;
			
			if(request.getProtocol().compareTo("HTTP/1.0") == 0) {
				response.setHeader("Pragma", "no-cache");
			} else if(request.getProtocol().compareTo("HTTP/1.1") == 0) {
				response.setHeader("Cache-Control", "no-cache");
			}
			
			response.setDateHeader("Expires", 1);
			
			chain.doFilter(request, rep);
		} else {
			chain.doFilter(req, rep);
		}
		
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
