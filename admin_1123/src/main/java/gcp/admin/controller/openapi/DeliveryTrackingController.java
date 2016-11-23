package gcp.admin.controller.openapi;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import gcp.external.model.DeliveryTracking;
import gcp.external.service.SweetTrackerService;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("openapi")
public class DeliveryTrackingController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryTrackingController.class);

	@Autowired
	private SweetTrackerService sweetTrackerService;

	@RequestMapping(value = "/delivery/tracking", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String insertDeliveryTracking(Model model, @ModelAttribute DeliveryTracking deliveryTracking) {

		JSONObject resultObj = null;
		
		try {
			resultObj = sweetTrackerService.insertDeliveryTracking(deliveryTracking);
			
		} catch (Exception e) {
			resultObj = new JSONObject();
			resultObj.put("code", false);
			resultObj.put("message", e.getMessage());
			
			logger.error("\n=======================================");
			logger.error("\n스윗트래커 트래킹 정보 수집 에러 : " + e.getMessage());
			logger.error("\n식별값 : " + deliveryTracking.getFid());
			logger.error("\n운송장번호 : " + deliveryTracking.getInvoice_no());
			logger.error("\n=======================================");
		}
		logger.debug("\n스윗트래커 트래킹 API RESULT : " + resultObj.toString());
		return resultObj.toString();
	}
}
