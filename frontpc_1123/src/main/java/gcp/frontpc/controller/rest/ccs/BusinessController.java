package gcp.frontpc.controller.rest.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.service.BusinessService;

@RestController
@RequestMapping("api/ccs/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

}
