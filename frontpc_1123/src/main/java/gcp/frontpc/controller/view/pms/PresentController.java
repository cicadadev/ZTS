package gcp.frontpc.controller.view.pms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.pms.service.PresentService;

@Controller("presentViewController")
@RequestMapping("pms/present")
public class PresentController {

	@Autowired
	private PresentService presentService;
	
	

}
