package gcp.admin.controller.rest.oms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsPickupproduct;
import gcp.oms.model.search.OmsPickupSearch;
import gcp.oms.service.PickupService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.admin.controller.rest.oms
 * @FileName : PickupController.java
 * @author : victor
 * @date : 2016. 7. 5.
 * @description :
 */
@RestController
@RequestMapping("api/oms/pickup")
public class PickupController {

	@Autowired
	private PickupService pickupService;

	@RequestMapping(value = "/list" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<OmsPickupproduct> selectList(@RequestBody OmsPickupSearch pickupSearch) throws Exception {
		pickupSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsPickupproduct> pickupList = pickupService.selectList(pickupSearch.getSearchId(), pickupSearch);
		return pickupList;
	}

	// @RequestMapping(method = RequestMethod.GET)
	// public OmsPickup selectOne(@RequestParam("pickupId") String pickupId) throws Exception {
	// return pickupService.selectOne("oms.pickup.selectOne",pickupId);
	// }

	// @RequestMapping(method = RequestMethod.POST)
	// public String insert(@RequestBody CcsFaq faq) throws Exception {
	// faq.setStoreId(SessionUtil.getStoreId());
	// return String.valueOf(faqService.insert(faq));
	// }
	@RequestMapping(method = RequestMethod.PUT)
	public int udpate(@RequestBody List<OmsPickupproduct> omsPickupproducts) throws Exception {
		int updateCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsPickupproduct product : omsPickupproducts) {
			product.setPickupProductStateCd("");
			product.setPickupCancelDt("");
			product.setUpdDt(currentDate);
			product.setUpdId(userId);

			updateCnt += pickupService.update("oms.pickup.updatePickupProduct", product);
		}
		return updateCnt;
	}

	// @RequestMapping(value = "/save", method = { RequestMethod.POST })
	// public int saveGrid(@RequestBody List<OmsPickupproduct> omsPickupproducts) throws Exception {
	// int updateCnt = 0;
	// String userId = SessionUtil.getLoginId();
	// String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
	//
	// for (OmsPickupproduct product : omsPickupproducts) {
	// OmsPickup pickup = product.getOmsPickup();
	// pickup.setUpdDt(currentDate);
	// pickup.setUpdId(userId);
	// updateCnt += pickupService.update(pickup);
	// }
	// // updateCnt = pickupService.updateReserveDate(omsPickupproducts);
	// return updateCnt;
	// }

	@RequestMapping(value = "/cancel" ,method = { RequestMethod.PUT })
	public CcsMessage cancelGrid(@RequestBody List<OmsPickupproduct> omsPickupproducts) throws Exception {
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			if (pickupService.cancelPickup(omsPickupproducts, SessionUtil.getLoginId()) < 1) {
				throw new ServiceException("oms.common.error", "픽업 상품의 취소 중 에러가 발생 하였습니다.");
			}
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		}
		ccsMessage.setReturnObject(omsPickupproducts);
		return ccsMessage;
	}

}
