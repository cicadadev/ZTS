package gcp.frontpc.controller.view.sps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import gcp.sps.service.CouponService;
import intune.gsf.controller.BaseController;

@Controller("couponViewController")
@RequestMapping("sps/coupon")
public class CouponController extends BaseController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CouponService	couponService;
	
}
