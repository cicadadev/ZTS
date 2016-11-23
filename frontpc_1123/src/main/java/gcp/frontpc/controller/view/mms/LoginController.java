package gcp.frontpc.controller.view.mms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("LoginViewController")
@RequestMapping("mms/login")
public class LoginController {


	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * 통합로그인 후 콜백
	 * 
	 * @Method Name : loginCallback
	 * @author : eddie
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public ModelAndView loginCallback(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/mms/sso/sso");

		// 로그인 후 이동할 url
		mv.addObject("url", request.getParameter("url"));

		//주문에서 필요
		mv.addObject("type", request.getParameter("type"));
		mv.addObject("callbackFlag", "Y");// sso.jsp를 콜백을 통해 호출하는것을 알려줌
		return mv;
	}

	@RequestMapping(value = "/callback2", method = RequestMethod.GET)
	public ModelAndView callback2(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/mms/include/membershipcallback");

		mv.addObject("ctype", request.getParameter("ctype"));

		return mv;
	}

	/**
	 * PC 상단 로그인/비로그인 영역 refresh
	 * 
	 * @Method Name : refresh
	 * @author : eddie
	 * @date : 2016. 11. 7.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ModelAndView refresh(HttpServletRequest request) throws Exception {
		return new ModelAndView("/ccs/common/inner/loginBtnbox");
	}

}
