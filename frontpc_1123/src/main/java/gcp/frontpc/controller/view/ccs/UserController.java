package gcp.frontpc.controller.view.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.ccs.service.UserService;

@Controller("userViewController")
@RequestMapping("ccs/user")
public class UserController {

	@Autowired
	private UserService userService;


}
