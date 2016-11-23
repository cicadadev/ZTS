package gcp.admin.controller.rest.oms;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrdermemo;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.service.OrderService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 
	 * @Method Name : getOrderListRest
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : 주문목록조회
	 *
	 * @param orderSearchCondition
	 * @return ResponseEntity<List<Order>>
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<?> selectList(@RequestBody OmsOrderSearch orderSearch) throws Exception {
		orderSearch.setStoreId(SessionUtil.getStoreId());
		List<?> orderList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
		return orderList;
	}

	@RequestMapping(method = RequestMethod.GET)
	public OmsOrder selectOne(@RequestParam("orderId") String orderId) throws Exception {
//		OmsOrder order = new OmsOrder();
//		order.setOrderId(orderId);
		return orderService.selectOne(orderId);
	}
	// @RequestMapping(method = RequestMethod.POST)
	// public String insert(@RequestBody OmsOrderproduct omsOrderproduct) throws Exception {
	// return String.valueOf(orderService.insert(omsOrderproduct));
	// }

	/**
	 * 일단 옵션변경
	 * 
	 * @Method Name : udpate
	 * @author : victor
	 * @date : 2016. 8. 1.
	 * @description :
	 *
	 * @param omsOrderproduct
	 * @return CcsMessage
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public CcsMessage udpate(@RequestBody OmsOrderproduct omsOrderproduct) throws Exception {
		String userId = SessionUtil.getLoginId();
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			omsOrderproduct.setStoreId(SessionUtil.getStoreId());
			omsOrderproduct.setInsId(userId);
			omsOrderproduct.setUpdId(userId);

			if (orderService.updateOption(omsOrderproduct) < 1) {
				throw new ServiceException("oms.common.error", "주문 상품의 옵션 변경 중 에러가 발생 하였습니다.");
			}
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		}
		ccsMessage.setReturnObject(omsOrderproduct);
		return ccsMessage;
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public int saveGrid(@RequestBody List<OmsOrderproduct> omsOrderproducts) throws Exception {
		int updCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsOrderproduct product : omsOrderproducts) {
			product.setUpdDt(currentDate);
			product.setUpdId(userId);
			updCnt += orderService.update(product);
		}
		return updCnt;
	}

	@RequestMapping(value = "/memo", method = { RequestMethod.GET })
	public List<OmsOrdermemo> selectMemo(@RequestParam("orderId") String orderId, @RequestParam("orderProductNo") String orderProductNo) throws Exception {
		OmsOrdermemo omsOrdermemo = new OmsOrdermemo();
		omsOrdermemo.setOrderId(orderId);
		omsOrdermemo.setOrderProductNo(new BigDecimal(orderProductNo));
		return orderService.selectMemo(omsOrdermemo);
	}

	@RequestMapping(value = "/memo", method = { RequestMethod.POST })
	public String insertMemo(@RequestBody OmsOrdermemo omsOrdermemo) throws Exception {
		return String.valueOf(orderService.insertMemo(omsOrdermemo));
	}
}
