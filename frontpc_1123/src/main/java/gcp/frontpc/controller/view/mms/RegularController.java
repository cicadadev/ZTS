package gcp.frontpc.controller.view.mms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.model.search.OmsRegularSearch;
import gcp.oms.service.RegularService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("mmsRegularViewController")
@RequestMapping("mms/mypage/regular")
public class RegularController {

	// private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private RegularService regularService;

	@RequestMapping(value = { "/{module}" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public String makeView(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("tabIdx", CommonUtil.replaceNull(request.getParameter("tabIdx"), "0"));
		return CommonUtil.makeJspUrl(request);
	}

	// @RequestMapping(value = { "/{module}" } ,method = { RequestMethod.GET })
	// public String selectRegular(Model model, HttpServletRequest request, @PathVariable String module) throws Exception {
	// if ("delivery".equals(module)) {
	//
	// } else if ("change".equals(module)) {
	// // TODO -
	// }
	// return "/mms/mypage/regular/regular" + StringUtils.capitalize(module);
	// }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inner/{tabType}" ,method = { RequestMethod.POST })
	public String selectList(Model model, HttpServletRequest request, @ModelAttribute OmsRegularSearch regularSearch, @PathVariable String tabType)
			throws Exception {
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		regularSearch.setStoreId(SessionUtil.getStoreId());
		regularSearch.setOrdererType("number");
		regularSearch.setOrderer(String.valueOf(SessionUtil.getMemberNo()));
		// regularSearch.setPageSize(1000);

		if ("request".equals(tabType)) {
			regularSearch.setSearchId("oms.regular.select.deliveryList");
			regularSearch.setStartDate(currentDate);
			regularSearch.setDeliveryScheduleStateCd("'DELIVERY_SCHEDULE_STATE_CD.REQ','DELIVERY_SCHEDULE_STATE_CD.INFO'");
			regularSearch.setDeliveryProductStateCd("'DELIVERY_PRODUCT_STATE_CD.REQ','DELIVERY_PRODUCT_STATE_CD.COMPLETE'");
		} else {
			regularSearch.setSearchId("oms.regular.select.deliveryList");
//			해지내역 기간없이 노출
//			regularSearch.setEndDate(currentDate);
			regularSearch.setDeliveryScheduleStateCd("'DELIVERY_SCHEDULE_STATE_CD.ORDER','DELIVERY_SCHEDULE_STATE_CD.CANCEL'");
			regularSearch.setDeliveryProductStateCd("'DELIVERY_PRODUCT_STATE_CD.CANCEL'");
		}
		List<?> regularList = regularService.selectList(regularSearch.getSearchId(), regularSearch);

		model.addAttribute("regularList", regularList);
		if ("request".equals(tabType) && regularList != null && regularList.size() > 0) {
			List<OmsRegulardelivery> list = (List<OmsRegulardelivery>) regularList;
			model.addAttribute("regularPaymentBusinessNm", list.get(0).getRegularPaymentBusinessNm());
		}
		return CommonUtil.makeJspUrl(request);
	}

	// 정기배송 정보변경
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/change/{regularId}", method = { RequestMethod.GET })
	public String change(Model model, HttpServletRequest request, @PathVariable String regularId)
			throws Exception {

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		// 변경가능한 정기배송 설정
		OmsRegularSearch regularSearch = new OmsRegularSearch();
		regularSearch.setRegularDeliveryId(regularId);
		regularSearch.setStoreId(SessionUtil.getStoreId());
		regularSearch.setOrdererType("number");
		regularSearch.setOrderer(String.valueOf(SessionUtil.getMemberNo()));
		regularSearch.setSearchId("oms.regular.select.deliveryList");
		regularSearch.setStartDate(currentDate);
		regularSearch.setDeliveryScheduleStateCd("'DELIVERY_SCHEDULE_STATE_CD.REQ','DELIVERY_SCHEDULE_STATE_CD.INFO'");
		regularSearch.setDeliveryProductStateCd("'DELIVERY_PRODUCT_STATE_CD.REQ','DELIVERY_PRODUCT_STATE_CD.COMPLETE'");

		List<?> regularList = regularService.selectList(regularSearch.getSearchId(), regularSearch);

		model.addAttribute("regularList", regularList);
		if (regularList != null && regularList.size() > 0) {
			List<OmsRegulardelivery> list = (List<OmsRegulardelivery>) regularList;
			model.addAttribute("regularPaymentBusinessNm", list.get(0).getRegularPaymentBusinessNm());
		}


		return "/mms/mypage/regular/regularChange";
	}
}
