package gcp.frontpc.controller.view.pms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.pms.service.AttributeService;

@Controller("attributeViewController")
@RequestMapping("pms/attribute")
public class AttributeController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private AttributeService attrService;


}
