package gcp.frontpc.controller.view.ccs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsFaqSearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.CsService;
import gcp.ccs.service.FaqService;
import gcp.ccs.service.NoticeService;
import gcp.ccs.service.QnaService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.service.OrderService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;

@Controller("csViewController")
@RequestMapping("ccs/cs")
public class CsController {

	@Autowired
	private MemberService	memberService;
	
	@Autowired
	private NoticeService	noticeService;
	
	@Autowired
	private FaqService		faqService;
	
	@Autowired
	private QnaService		qnaService;
	
	@Autowired
	private OrderService	orderService;
	
	@Autowired
	private CsService		csService;
	
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : defaultSetDate
	 * @author : roy
	 * @date : 2016. 8. 24.
	 * @description : 고객센터 기본 조회기간 세팅 (현재 월 -1 ~ 현재 월)
	 *
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> defaultSetDate() throws Exception {
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 날짜 세팅
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String editDate = date.getAddMonth(date.FORMAT_2, currentDate, new BigDecimal(-1));

		map.put("startDate", editDate);
		map.put("endDate", currentDate);
		
		return map;
	}
	
	/**
	 * @Method Name : mainPage
	 * @author : roy
	 * @date : 2016. 7. 7.
	 * @description : cs 메인
	 *
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/main", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mainPage(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		MmsMemberSearch search = new MmsMemberSearch();
		

//		search.setMemberId(SessionUtil.getMemberId());
		search.setMemberId("allen");//TODO 임시

//		MmsMemberZts member = memberService.getMemberWalletInfo(search);

		Map<String, String> map = defaultSetDate();
		
		CcsNoticeSearch searchNotice = new CcsNoticeSearch();
		searchNotice.setStartDate(map.get("startDate"));
		searchNotice.setEndDate(map.get("endDate"));
		
		CcsFaqSearch searchFaq = new CcsFaqSearch();
		searchFaq.setStartDate(map.get("startDate"));
		searchFaq.setEndDate(map.get("endDate"));
		
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		searchNotice.setNewDt(yesterDay);
		searchNotice.setNewDt(yesterDay);

		// 공지사항 조회
		List<CcsNotice> notice = noticeService.selectCsNoticeList(searchNotice);
		
		// 이벤트 조회
		List<CcsNotice> event = noticeService.selectCsEventList(searchNotice);

		// FAQ 5건 조회
		List<CcsFaq> 	faq = faqService.selectCsList(searchFaq);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "고객센터");
		mv.addObject("pageNavi", pageMap);

//		mv.addObject("member", member);
		mv.addObject("notice", notice);
		mv.addObject("event", event);
		mv.addObject("faq",    faq);

		return mv;
		
		/*ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;*/
	}

//	/**
//	 * 비회원 주문조회
//	 * 
//	 * @Method Name : nonmemberorder
//	 * @author : eddie
//	 * @date : 2016. 9. 21.
//	 * @description :
//	 *
//	 * @param omsOrder
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/guest" ,method = RequestMethod.GET)
//	public String guestOrder(Model model, HttpServletRequest request) throws Exception {
//		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo(request);
//		String orderId = loginInfo.getLoginId();
//		
//		OmsOrderSearch orderSearch = new OmsOrderSearch();
//		orderSearch.setOrderId(orderId);
//
//		Object omsOrder = orderService.selectOne("oms.order.select.fodetail", orderSearch);
//		Object omsDelivery = orderService.selectOne("oms.delivery.selectAmt", orderId);
//		List<?> couponList = orderService.selectList("oms.order.select.coupon", orderId);
//
//		OmsClaimSearch claimSearch = new OmsClaimSearch();
//		claimSearch.setOrderId(orderId);
//		List<OmsPayment> paymentList = (List<OmsPayment>) orderService.selectList("oms.payment.selectList", claimSearch);
//		String partCancelYn = "Y";
//		if (paymentList != null) {
//			for (OmsPayment omsPayment : paymentList) {
//				if ("N".equals(omsPayment.getPartialCancelYn())) {
//					partCancelYn = "N";
//				}
//			}
//		}
//
//		model.addAttribute("partCancelYn", partCancelYn);
//
//		model.addAttribute("order", omsOrder);
//		model.addAttribute("delivery", omsDelivery);
//		model.addAttribute("couponList", couponList);
//		model.addAttribute("paymentList", paymentList);
//
//		return CommonUtil.makeJspUrl(request, FoSessionUtil.isMobile(request));
//	}

}
