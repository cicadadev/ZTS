package gcp.frontpc.controller.rest.pms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.service.PresentService;

@RestController("presentController")
@RequestMapping("api/pms/present")
public class PresentController {

	@Autowired
	private PresentService presentService;
	
	

}
