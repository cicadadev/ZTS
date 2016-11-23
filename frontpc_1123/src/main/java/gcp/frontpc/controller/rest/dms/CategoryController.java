package gcp.frontpc.controller.rest.dms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.service.CategoryService;

@RestController("displayCategoryController")
@RequestMapping("api/dms/category")
public class CategoryController {
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private CategoryService	displayCategoryService;
}
