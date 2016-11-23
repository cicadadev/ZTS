package gcp.admin.controller.rest.oms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsPaymentSearch;
import gcp.oms.service.DeliveryService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/delivery")
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsDeliveryaddress> selectList(@RequestBody OmsOrderSearch orderSearch) throws Exception {
		orderSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsDeliveryaddress> orderList = (List<OmsDeliveryaddress>) deliveryService.selectList(orderSearch.getSearchId(), orderSearch);
		return orderList;
	}

	@RequestMapping(method = { RequestMethod.POST })
	public OmsDeliveryaddress selectOne(@RequestBody OmsDeliveryaddress omsDeliveryaddress) throws Exception {
		return deliveryService.selectOne(omsDeliveryaddress);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String udpate(@RequestBody OmsDeliveryaddress address) throws Exception {
		return String.valueOf(deliveryService.update(address));
	}
	
	@RequestMapping(value = { "/tracking/list" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<?> layer(@RequestBody OmsPaymentSearch omsPaymemtSearch) throws Exception {
		// List<OmsLogistics> logisticss = (List<OmsLogistics>) deliveryService.selectList("oms.delivery.tracking", omsPaymemtSearch);
		return deliveryService.selectList("oms.delivery.tracking", omsPaymemtSearch);
	}
}
