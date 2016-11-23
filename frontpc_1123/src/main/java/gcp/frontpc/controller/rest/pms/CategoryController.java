package gcp.frontpc.controller.rest.pms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.service.StandardCategoryService;

@RestController("productCategoryController")
@RequestMapping("api/pms/category")
public class CategoryController {

	@Autowired
	private StandardCategoryService standardCategoryService;



}
