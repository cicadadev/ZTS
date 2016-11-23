package gcp.frontpc.controller.view.oms;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.external.service.PgService;
import gcp.oms.model.OmsPaymentif;

@Controller("pgViewController")
@RequestMapping("oms/pg")
public class PgController {

	private static final Logger	logger	= LoggerFactory.getLogger(PgController.class);

	@Autowired
	private PgService pgService;

	@RequestMapping(value = "return", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView returnPage(@ModelAttribute OmsPaymentif omsPaymentif, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();

//		String message = new String(omsPaymentif.getLGD_RESPMSG().getBytes("ISO-8859-1"), "UTF-8");
//		logger.debug("RETURN MESSGAGE : " + message);
//		omsPaymentif.setLGD_RESPMSG(message);
		
		mv.addObject("omsPaymentif", omsPaymentif);
		mv.setViewName("/oms/pg/returnurl");
		return mv;
	}

	@RequestMapping(value = "casnote", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView casnotePage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();

//		String message = new String(omsPaymentif.getLGD_RESPMSG().getBytes("ISO-8859-1"), "UTF-8");
//		logger.debug("RETURN MESSGAGE : " + message);
//		omsPaymentif.setLGD_RESPMSG(message);				 		
		OmsPaymentif omsPaymentif = new OmsPaymentif();
		omsPaymentif.setOrderId(request.getParameter("orderId"));
		omsPaymentif = pgService.getCasnoteTest(omsPaymentif);
		mv.addObject("omsPaymentif", omsPaymentif);
		mv.setViewName("/oms/pg/casnote");
		return mv;
	}
}
