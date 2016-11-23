package gcp.frontpc.controller.rest.pms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.service.BrandService;

@RestController
@RequestMapping("api/pms/brand")
public class BrandController {

	@Autowired
	private BrandService	brandService;

}
