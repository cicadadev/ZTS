package gcp.admin.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.search.CcsFaqSearch;
import gcp.ccs.service.FaqService;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/faq")
public class FaqController {

	@Autowired
	private FaqService faqService;

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsFaq> selectList(@RequestBody CcsFaqSearch ccsFaqSearch) throws Exception {
		ccsFaqSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsFaq> faqList = faqService.selectList(ccsFaqSearch);
		return faqList;
	}

	@RequestMapping(method = RequestMethod.GET)
	public CcsFaq selectOne(@RequestParam("faqNo") String faqNo) throws Exception {
		CcsFaq faq = new CcsFaq();
		faq.setFaqNo(new BigDecimal(faqNo));
		faq.setStoreId(SessionUtil.getStoreId());
		return faqService.selectOne(faq);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String insert(@RequestBody CcsFaq faq) throws Exception {
		faq.setStoreId(SessionUtil.getStoreId());
		return String.valueOf(faqService.insert(faq));
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@RequestBody CcsFaq faq) throws Exception {
		faq.setUpdId(SessionUtil.getLoginId());
		faq.setStoreId(SessionUtil.getStoreId());
		return String.valueOf(faqService.update(faq));
	}

	/**
	 * 그리드 데이터 일괄변경(정렬순서, 사용여부)
	 * 
	 * @Method Name : saveGrid
	 * @author : victor
	 * @date : 2016. 6. 21.
	 * @description : 정렬순서와 사용여부에 대한 수정값을 일괄저장한다.
	 *
	 * @param ccsFaqs
	 * @return int (변경된 그리드 row 수)
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public int saveGrid(@RequestBody List<CcsFaq> ccsFaqs) throws Exception {
		int updCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (CcsFaq faq : ccsFaqs) {
			faq.setUpdDt(currentDate);
			faq.setUpdId(userId);
			updCnt += faqService.update(faq);
		}
		return updCnt;
	}

	/**
	 * 그리드 데이터 일괄삭제
	 * 
	 * @Method Name : deleteGrid
	 * @author : victor
	 * @date : 2016. 6. 21.
	 * @description : 그리드에서 선택한 값을 일괄 삭제한다.
	 *
	 * @param ccsFaqs
	 * @return int (삭제된 그리드 row 수)
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public int deleteGrid(@RequestBody List<CcsFaq> ccsFaqs) throws Exception {
		int delCnt = 0;
		for (CcsFaq faq : ccsFaqs) {
			delCnt += faqService.delete(faq);
		}
		return delCnt;
	}

}
