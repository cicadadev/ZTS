package gcp.frontpc.controller.rest.dms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.service.ExhibitService;

@RestController
@RequestMapping("api/dms/exhibit")
public class ExhibitController {


	@Autowired
	private ExhibitService	exhibitService;
	
	
}
