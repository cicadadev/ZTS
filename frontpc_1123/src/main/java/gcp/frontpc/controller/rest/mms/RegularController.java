package gcp.frontpc.controller.rest.mms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.model.OmsRegulardeliveryproduct;
import gcp.oms.model.OmsRegulardeliveryschedule;
import gcp.oms.service.DeliveryService;
import gcp.oms.service.OrderService;
import gcp.oms.service.PaymentService;
import gcp.oms.service.RegularService;
import intune.gsf.common.exceptions.ServiceException;

@RestController("mmsRegularRestController")
@RequestMapping("api/mms/mypage/regular")
public class RegularController {

	@Autowired
	private OrderService	orderService;

	@Autowired
	private DeliveryService	deliveryService;

	@Autowired
	private PaymentService	paymentService;

	@Autowired
	private RegularService	regularService;

	private final Log		logger	= LogFactory.getLog(getClass());

	/**
	 * 
	 * @Method Name : updateRegular
	 * @author : roy
	 * @date : 2016. 10. 17.
	 * @description :
	 *
	 * @param address
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/delivery", method = RequestMethod.POST)
	public Object updateRegular(@ModelAttribute OmsRegulardelivery address) throws Exception {
		if (regularService.update(address) > 0) {
			return address;
		} else {
			return null;
		}
	}


	/**
	 * 정기배송 설정 변경
	 * 
	 * @Method Name : update
	 * @author : roy
	 * @date : 2016. 10. 19.
	 * @description :
	 *
	 * @param omsRegulardeliveryproduct
	 * @return CcsMessage
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public CcsMessage update(@RequestBody List<OmsRegulardeliveryproduct> omsRegulardeliveryproducts) throws Exception {
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			if (regularService.updateRegularProduct(omsRegulardeliveryproducts) < 1) {
				throw new ServiceException("oms.common.error", "정기배송 설정 변경 중 에러가 발생 하였습니다.");
			}
		} catch (ServiceException e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage("정기배송 설정 변 중 알수 없는 에러가 발생하였습니다.");
		}
//		ccsMessage.setReturnObject(omsRegulardeliveryproducts);
		return ccsMessage;
	}

	/**
	 * 정기배송 해지
	 * 
	 * @Method Name : cancel
	 * @author : roy
	 * @date : 2016. 10. 19.
	 * @description :
	 *
	 * @param omsRegulardeliveryproduct
	 * @return CcsMessage
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = "application/json")
	public CcsMessage cancel(@RequestBody List<OmsRegulardeliveryproduct> omsRegulardeliveryproducts) throws Exception {
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			if (regularService.updateRegularCancel(omsRegulardeliveryproducts) < 1) {
				throw new ServiceException("oms.common.error", "정기배송 상품  변경 중 에러가 발생 하였습니다.");
			}
		} catch (ServiceException e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage("정기배송 상품 취소 중 알수 없는 에러가 발생하였습니다.");
		}
//		ccsMessage.setReturnObject(omsRegulardeliveryproducts);
		return ccsMessage;
	}

	/**
	 * 스케쥴 업데이트 (이번 배송 건너뛰기)
	 * 
	 * @Method Name : schedule
	 * @author : roy
	 * @date : 2016. 10. 19.
	 * @description :
	 *
	 * @param omsRegulardeliveryschedules
	 * @return CcsMessage
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/schedule", method = RequestMethod.POST, produces = "application/json")
	public CcsMessage schedule(@RequestBody List<OmsRegulardeliveryschedule> omsRegulardeliveryschedules) throws Exception {
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			if (regularService.updateSchedule(omsRegulardeliveryschedules) < 1) {

				throw new ServiceException("oms.common.error", "이번 배송 건너뛰기 중 에러가 발생 하였습니다.");
			}
		} catch (ServiceException e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage("이번 배송 건너뛰기 중 알수 없는 에러가 발생하였습니다.");
		}
//		ccsMessage.setReturnObject(omsRegulardeliveryschedules);
		return ccsMessage;
	}

}
