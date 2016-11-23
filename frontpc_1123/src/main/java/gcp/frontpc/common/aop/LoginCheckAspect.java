package gcp.frontpc.common.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import intune.gsf.common.utils.SessionUtil;

@Aspect
@Component
public class LoginCheckAspect {

	/**
	 * 로그인이 필요한 url접근시 체크
	 * 
	 * @Method Name : loginCheck
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(intune.gsf.common.annotation.LoginCheck)")
	public Object loginCheck(ProceedingJoinPoint pjp) throws Throwable {
//		System.out.println(pjp);

		boolean isLogin = SessionUtil.isMemberLogin();

		HttpServletRequest req = null;
		ModelAndView mav = new ModelAndView();
		for (Object obj : pjp.getArgs()) {
			if (obj instanceof HttpServletRequest) {
				req = (HttpServletRequest) obj;
			}
		}


		if (!isLogin) {
			mav.setViewName("redirect:/ccs/common/main");
			return mav;
		}

		Object ret = pjp.proceed();

		return ret;
	}
}