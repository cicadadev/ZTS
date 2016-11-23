package gcp.admin.controller.rest.oms;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.model.OmsRegulardeliveryproduct;
import gcp.oms.model.OmsRegulardeliveryschedule;
import gcp.oms.model.search.OmsRegularSearch;
import gcp.oms.service.RegularService;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.admin.controller.rest.oms
 * @FileName : RegularController.java
 * @author : victor
 * @date : 2016. 7. 11.
 * @description :
 */
@RestController
@RequestMapping("api/oms/regular")
public class RegularController {

	// private static final Logger logger = LoggerFactory.getLogger(RegularController.class);

	@Autowired
	private RegularService regularService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<OmsRegulardeliveryproduct> selectList(@RequestBody OmsRegularSearch regularSearch) throws Exception {
		regularSearch.setStoreId(SessionUtil.getStoreId());
		regularSearch.setDeliveryScheduleStateCd("'DELIVERY_SCHEDULE_STATE_CD.REQ','DELIVERY_SCHEDULE_STATE_CD.INFO'");
		regularSearch.setDeliveryProductStateCd("'DELIVERY_PRODUCT_STATE_CD.REQ','DELIVERY_PRODUCT_STATE_CD.COMPLETE'");
		List<OmsRegulardeliveryproduct> regularList = (List<OmsRegulardeliveryproduct>) regularService.selectList(regularSearch.getSearchId(), regularSearch);
		return regularList;
	}

	@RequestMapping(method = RequestMethod.GET)
	public OmsRegulardelivery selectOne(@RequestParam("regularId") String regularId) throws Exception {
		return regularService.selectOne(regularId);
	}

	// @RequestMapping(method = RequestMethod.POST)
	// public String insert(@RequestBody CcsFaq faq) throws Exception {
	// faq.setStoreId(SessionUtil.getStoreId());
	// return String.valueOf(faqService.insert(faq));
	// }
	@RequestMapping(method = RequestMethod.PUT)
	public int udpate(@RequestBody List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {
		int updateCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsRegulardeliveryproduct product : omsRegularproducts) {
			OmsRegulardeliveryschedule schedule = product.getOmsRegulardeliveryschedules().get(0);
			BigDecimal periodValue = product.getDeliveryPeriodValue();

			schedule.setIncreaseDay(new BigDecimal(
					DateUtil.getDayPeriod(product.getRegularDeliveryDt(), schedule.getRegularDeliveryDt(), DateUtil.FORMAT_2)));

			product.setDeliveryPeriodValue(DateUtil.getDateDay(schedule.getRegularDeliveryDt(), DateUtil.FORMAT_2));
			schedule.setDeliveryScheduleStateCd("");
			schedule.setUpdDt(currentDate);
			schedule.setUpdId(userId);

			// 1. 배송상품 배송지 변경
			regularService.update("oms.regular.update.delivery", product.getOmsRegulardelivery());

			// 2. 배송 상품 예정요일  변경
			if (!periodValue.equals(product.getDeliveryPeriodValue())) {
				regularService.update(product);
			}
			
			// 3. 스케쥴 배송예정일 변경
			updateCnt += regularService.update("oms.regular.update.delaySchedule", schedule);
//			regularService.updateRegularProduct(omsRegularproducts);

		}
		return updateCnt;
	}

	// @RequestMapping(value = "/save", method = { RequestMethod.POST })
	// public int saveGrid(@RequestBody List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {
	// int updateCnt = 0;
	// String userId = SessionUtil.getLoginId();
	// String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
	//
	// for (OmsRegulardeliveryproduct product : omsRegularproducts) {
	// OmsRegulardelivery regular = product.getOmsRegulardelivery();
	// regular.setUpdDt(currentDate);
	// regular.setUpdId(userId);
	// updateCnt += regularService.update(regular);
	// }
	// return updateCnt;
	// }

	@RequestMapping(value = "/cancel" ,method = { RequestMethod.PUT })
	public int cancelGrid(@RequestBody List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {
		return regularService.updateRegular(omsRegularproducts);
	}

}
