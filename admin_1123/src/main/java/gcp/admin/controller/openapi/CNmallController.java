package gcp.admin.controller.openapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.external.service.CNmallService;
import gcp.external.service.ChinaReceiveOrderService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.controller.BaseController;

@RestController
@RequestMapping("openapi")
public class CNmallController extends BaseController {
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private CNmallService		cnmallService;

	@Autowired
	private ChinaReceiveOrderService	chinaOrderService;

	@RequestMapping(value = "/insertCnOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public String insertCNmallOrder(HttpServletRequest request, HttpServletResponse response) {
		String storeId = BaseConstants.STORE_ID;
		String updId = "cnmall";
		String mallType = "CNMALL";
		String result = "";
		int rtnVal = 0;

		logger.info("CNmall Order Receive Start...");

		try {
			rtnVal = cnmallService.getCNmallOrder(request, storeId);

			//주문건수가 있으면 BO 주문생성
			if (rtnVal > 0) {
				chinaOrderService.processChinaReceiveOrder(storeId, updId, mallType);
			}

			result = "Total Count [" + rtnVal + "]";
		} catch (Exception e) {
			logger.error("CNmall Order Receive Exception");
			e.printStackTrace();
			return null;
		}

		logger.info("CNmall Order Receive End.");

		return result;
	}
}
