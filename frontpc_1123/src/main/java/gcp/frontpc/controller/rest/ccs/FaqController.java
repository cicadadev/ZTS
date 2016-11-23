package gcp.frontpc.controller.rest.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.service.FaqService;

@RestController
@RequestMapping("api/ccs/faq")
public class FaqController {

	@Autowired
	private FaqService faqService;


}
