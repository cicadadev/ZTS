package gcp.admin.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsBusinessinquiry;
import gcp.ccs.model.search.CcsBusinessinquirySearch;
import gcp.ccs.service.BusinessinquiryService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/businessinquiry")
public class BusinessinquiryController {

	@Autowired
	private BusinessinquiryService businessinquiryService;
	
	
	/**
	 * 
	 * @Method Name : getBusinessInquiryListInfo
	 * @author : roy
	 * @date : 2016. 8. 2.
	 * @description : 입점상담관리 화면 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsBusinessinquiry> getBusinessInquiryListInfo(@RequestBody CcsBusinessinquirySearch search) {

		search.setStoreId(SessionUtil.getStoreId());
		List<CcsBusinessinquiry> list= businessinquiryService.getBusinessInquiryListInfo(search); 
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : getBusinessInquiryDetail
	 * @author : roy
	 * @date : 2016. 8. 2.
	 * @description : 입점상담 정보 상세 조회
	 *
	 * @param businessInquiryNo
	 * @return
	 */
	@RequestMapping(value = "/{businessInquiryNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public CcsBusinessinquiry getBusinessInquiry(@PathVariable("businessInquiryNo") String businessInquiryNo) {
		CcsBusinessinquiry businessInquiry = new CcsBusinessinquiry();
		businessInquiry.setStoreId(SessionUtil.getStoreId());
		businessInquiry.setBusinessInquiryNo(new BigDecimal(businessInquiryNo));
		return businessinquiryService.getBusinessInquiryDetail(businessInquiry);
	}
}