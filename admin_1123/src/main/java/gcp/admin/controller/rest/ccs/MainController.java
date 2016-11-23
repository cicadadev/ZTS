package gcp.admin.controller.rest.ccs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsInquirySearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.model.search.CcsPopupNoticeSearch;
import gcp.ccs.model.search.CcsUserSearch;
import gcp.ccs.service.MainService;
import gcp.common.util.BoSessionUtil;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.search.PmsProductQnaSearch;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/main")
public class MainController {

	@Autowired
	private MainService mainService;
	
	/**
	 * 
	 * @Method Name : getNoticeList
	 * @author : roy
	 * @date : 2016. 8. 4.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/noticeList", method = RequestMethod.POST)
	public List<CcsNotice> getSimpleNoticeList(HttpServletRequest request) throws Exception {
		CcsNoticeSearch search = new CcsNoticeSearch();
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return mainService.getSimpleNoticeList(search);
	}
	
	/**
	 * 
	 * @Method Name : getProductQnaList2
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQnaList", method = RequestMethod.POST)
	public List<PmsProductqna> getProductQnaList(HttpServletRequest request) throws Exception {
		PmsProductQnaSearch search = new PmsProductQnaSearch();
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return mainService.getProductQnaList(search);
	}
	
	/**
	 * 
	 * @Method Name : getProductQnaState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQnaState", method = { RequestMethod.GET, RequestMethod.POST })
	public PmsProductqna getProductQnaState(@RequestBody PmsProductQnaSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		search.setBusinessId(BoSessionUtil.getBusinessId());
		return mainService.getProductQnaState(search);
	}

	
	/**
	 * 
	 * @Method Name : getInquiryList
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiryList", method = RequestMethod.POST)
	public List<CcsInquiry> getInquiryList(HttpServletRequest request) throws Exception {
		CcsInquirySearch search = new CcsInquirySearch();
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return mainService.getInquiryList(search);
	}
	
	/**
	 * 
	 * @Method Name : getInquiryState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiryState", method = { RequestMethod.GET, RequestMethod.POST })
	public CcsInquiry getInquiryState(@RequestBody CcsInquirySearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return mainService.getInquiryState(search);
	}
	
	/**
	 * 
	 * @Method Name : getOrderState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderState", method = { RequestMethod.GET, RequestMethod.POST })
	public OmsOrder getOrderState(@RequestBody OmsOrderSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return mainService.getOrderState(search);
	}
	
	/**
	 * 
	 * @Method Name : getProductList
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 품절 임박
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getProductList(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		List<PmsProduct> list = mainService.getProductList(search);
		return list;
	}

	/**
	 * 
	 * @Method Name : getMdList
	 * @author : roy
	 * @date : 2016. 10. 4.
	 * @description : 업무 담당자 리스트
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mdList", method = RequestMethod.POST)
	public List<CcsUser> getMdList(HttpServletRequest request) throws Exception {
		CcsUserSearch search = new CcsUserSearch();
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		List<CcsUser> list = mainService.getMdList(search);
		return list;
	}

	/**
	 * 
	 * @Method Name : getPopupList
	 * @author : roy
	 * @date : 2016. 10. 8.
	 * @description : PO 팝업 리스트
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popupList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsPopup> getPopupList(@RequestBody CcsPopupNoticeSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
//		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
//			search.setBusinessId(BoSessionUtil.getBusinessId());
//		}
		List<CcsPopup> list = mainService.getPopupList(search);
		return list;
	}
}
