package gcp.admin.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.search.CcsInquirySearch;
import gcp.ccs.service.QnaService;
import gcp.common.util.BoSessionUtil;
import gcp.common.util.CcsUtil;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.search.PmsProductQnaSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs")
public class QnaController {

	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private QnaService	qnaService;

	/**
	 * 
	 * @Method Name : getQnaList
	 * @author : emily
	 * @date : 2016. 6. 1.
	 * @description : 문의관리 목록 조회
	 *
	 * @param qnaSerch
	 * @return
	 */
	@RequestMapping(value = "/inquiry/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsInquiry> selectInquiryList(@RequestBody CcsInquirySearch qnaSerch) {

		qnaSerch.setStoreId(SessionUtil.getStoreId());

		// PO 로그인일경우 business id 값 세팅
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			qnaSerch.setBusinessId(BoSessionUtil.getBusinessId());
		}
		
		List<CcsInquiry> list = qnaService.selectQnaList(qnaSerch);

		if (list != null && list.size() > 0) {
			for (CcsInquiry ccsQnaInfo : list) {
				// 경과시간
				ccsQnaInfo.setPassTime(CcsUtil.getPassTime(ccsQnaInfo.getInsDt(), ccsQnaInfo.getAnswerDt()));
			}
		}

		return list;
	}


	/**
	 * 
	 * @Method Name : selectQna
	 * @author : emily
	 * @date : 2016. 6. 3.
	 * @description : 문의 상세 조회
	 *
	 * @param qnaNo
	 * @return
	 */
	@RequestMapping(value = "/inquiry/{inquiryNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public CcsInquiry selectInquiry(@PathVariable("inquiryNo") String inquiryNo) throws Exception {

		CcsInquiry qna = new CcsInquiry();
		qna.setInquiryNo(new BigDecimal(inquiryNo));
		qna.setStoreId(SessionUtil.getStoreId());

		CcsInquiry result = qnaService.selectInquiry(qna);

		result.setPassTime(CcsUtil.getPassTime(result.getInsDt(), result.getAnswerDt()));
		return result;
	}

	/**
	 * 
	 * @Method Name : updateQna
	 * @author : emily
	 * @date : 2016. 6. 3.
	 * @description : 문의 등록& 수정
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiry", method = RequestMethod.PUT)
	public String saveInquiry(@RequestBody CcsInquiry qna) throws Exception {

		qna.setStoreId(SessionUtil.getStoreId());
		qna.setUpdId(SessionUtil.getLoginId());
		qnaService.saveInquiry(qna);

		return String.valueOf(qna.getInquiryNo());
	}

	/**
	 * 
	 * @Method Name : saveAnswer
	 * @author : emily
	 * @date : 2016. 7. 14.
	 * @description : 문의 확인
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiry/saveAnswer", method = RequestMethod.PUT)
	public void saveAnswer(@RequestBody CcsInquiry qna) throws Exception {
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		qnaService.saveAnswer(qna);
	}

	/**
	 * 
	 * @Method Name : saveConfirm
	 * @author : emily
	 * @date : 2016. 7. 14.
	 * @description : 문의 확인
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiry/saveConfirm", method = RequestMethod.PUT)
	public void saveConfirm(@RequestBody CcsInquiry qna) throws Exception {
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		qnaService.updateConfirm(qna);
	}

	/**
	 * 
	 * @Method Name : getProductQnaList
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품QNA 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/productQna/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProductqna> getProductQnaList(@RequestBody PmsProductQnaSearch search) {

		search.setStoreId(SessionUtil.getStoreId());

		// PO 로그인일경우 business id 값 세팅
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}

		List<PmsProductqna> list = qnaService.getProductQnaList(search);

		if (list != null && list.size() > 0) {
			for (PmsProductqna qna : list) {
				// 경과시간
				qna.setPassTime(CcsUtil.getPassTime(qna.getInsDt(), qna.getAnswerDt()));
			}
		}
		return list;
	}

	/**
	 * 
	 * @Method Name : getProductQnaDetail
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품QNA 상세 조회
	 *
	 * @param productQnaNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQna/{productQnaNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public PmsProductqna getProductQnaDetail(@PathVariable("productQnaNo") String productQnaNo) throws Exception {

		PmsProductqna qna = new PmsProductqna();
		qna.setProductQnaNo(new BigDecimal(productQnaNo));
		qna.setStoreId(SessionUtil.getStoreId());
		PmsProductqna result = qnaService.getProductQnaDetail(qna);
		result.setPassTime(CcsUtil.getPassTime(result.getInsDt(), result.getAnswerDt()));
		return result;

	}

	/**
	 * 
	 * @Method Name : updateProductQna
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품QNA 문의 수정
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQna/update", method = { RequestMethod.PUT })
	public void updatePrdQna(@RequestBody PmsProductqna qna) throws Exception {
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		qnaService.updatePrdQna(qna);
	}

	/**
	 * 
	 * @Method Name : saveQnaConfirm
	 * @author : emily
	 * @date : 2016. 7. 15.
	 * @description : 상품QnA 문의 확인
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQna/confirm", method = RequestMethod.PUT)
	public void updatePrdQnaConfirm(@RequestBody PmsProductqna qna) throws Exception {
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		qnaService.updatePrdQnaConfirm(qna);

	}

	/**
	 * 
	 * @Method Name : savePrdQnaState
	 * @author : emily
	 * @date : 2016. 7. 15.
	 * @description : 상품QnA 답변완료
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/productQna/answer", method = RequestMethod.PUT)
	public void updatePrdQnaAnswer(@RequestBody PmsProductqna qna) throws Exception {
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		qnaService.updatePrdQnaAnswer(qna);

	}

}
