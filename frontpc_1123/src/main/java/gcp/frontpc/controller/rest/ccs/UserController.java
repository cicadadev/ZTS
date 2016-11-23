package gcp.frontpc.controller.rest.ccs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.service.UserService;
import gcp.oms.model.OmsOrder;
import intune.gsf.common.utils.CommonUtil;

@RestController
@RequestMapping("api/ccs/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 비회원 주문 번호 찾기
	 * 
	 * @Method Name : findOrderId
	 * @author : roy
	 * @date : 2016. 10. 31.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nonmember/findOrderId/ajax", method = RequestMethod.POST)
	public String findOrderId(HttpServletRequest request, @RequestBody OmsOrder order) throws Exception {

		String orderId = userService.findOrderId(order);

		// 세션생성
		if (CommonUtil.isNotEmpty(orderId)) {
			return orderId;
		} else {
			return "fail";
		}

	}

	/**
	 * 비회원 주문 번호 찾기
	 * 
	 * @Method Name : findOrderPwd
	 * @author : roy
	 * @date : 2016. 10. 31.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nonmember/findOrderPwd/ajax", method = RequestMethod.POST)
	public String findOrderPwd(HttpServletRequest request, @RequestBody OmsOrder order) throws Exception {

		String orderPwd = userService.findOrderPwd(order);

		// 세션생성
		if (orderPwd != null) {
			return "success";
		} else {
			return "fail";
		}

	}
}
