package intune.gsf.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import intune.gsf.common.utils.XSSRequestWrapper;

/**
 * XSS Filter
 * @author ditto
 *
 */
public class XSSFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest)request), response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

}
