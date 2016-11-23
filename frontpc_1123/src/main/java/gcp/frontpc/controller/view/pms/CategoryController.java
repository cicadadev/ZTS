package gcp.frontpc.controller.view.pms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.pms.service.StandardCategoryService;

@Controller("productCategoryViewController")
@RequestMapping("pms/category")
public class CategoryController {

	@Autowired
	private StandardCategoryService standardCategoryService;



}
