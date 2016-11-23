package gcp.frontpc.controller.rest.sps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.sps.service.PresentPromotionService;
import intune.gsf.controller.BaseController;

@RestController("presentPromotionController")
@RequestMapping("api/sps/present")
public class PresentPromotionController extends BaseController {
	private final Log				logger	= LogFactory.getLog(getClass());

	@Autowired
	private PresentPromotionService	presentService;



}
