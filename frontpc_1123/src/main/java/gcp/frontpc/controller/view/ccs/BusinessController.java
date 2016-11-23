package gcp.frontpc.controller.view.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.ccs.service.BusinessService;

@Controller("businessViewController")
@RequestMapping("ccs/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

}
