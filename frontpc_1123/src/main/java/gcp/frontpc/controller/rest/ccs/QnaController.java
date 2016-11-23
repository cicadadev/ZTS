package gcp.frontpc.controller.rest.ccs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.service.QnaService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/qna")
public class QnaController {

	@Autowired
	private QnaService qnaService;

	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 
	 * @Method Name : saveQna
	 * @author : roy
	 * @date : 2016. 8. 24.
	 * @description : 1:1 문의 저장
	 *
	 * @param CcsInquiry
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, String> saveQna(@ModelAttribute CcsInquiry qna) throws Exception {
		Map<String, String> result = new HashMap<String,String>();
		result.put(BaseConstants.RESULT_FLAG,BaseConstants.RESULT_FLAG_FAIL);

		logger.debug("inquiryTypeCd info:"+qna.getInquiryTypeCd()+ "inquiryTypeCd info:"+qna.getBrandId());
		logger.debug("title info:"+qna.getTitle() +" detail:" + qna.getDetail());
		logger.debug("smsYn info:"+qna.getSmsYn() +" phone:" + qna.getPhone());
		
		
		qna.setStoreId(SessionUtil.getStoreId());
		qna.setMemberNo(SessionUtil.getMemberNo());
		qna.setInquiryChannelCd("INQUIRY_CHANNEL_CD.ONLINE");
		qna.setInquiryTypeCd(qna.getInquiryTypeCd());
		qna.setInquiryStateCd("INQUIRY_STATE_CD.ACCEPT");
		qna.setEmailYn("Y");
		if((CommonUtil.isEmpty(qna.getSmsYn())) ){
			qna.setSmsYn("N");
		}
		if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
			result.put(BaseConstants.RESULT_MESSAGE, "문의 유형을 입력하지 않으셨습니다.");
		}else if (CommonUtil.isEmpty(qna.getTitle())) {
			result.put(BaseConstants.RESULT_MESSAGE, "제목을 입력하지 않으셨습니다.");
		}else if (CommonUtil.isEmpty(qna.getDetail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "문의 내용을 입력하지 않으셨습니다.");
		}else if (qna.getSmsYn().equals('Y') && CommonUtil.isEmpty(qna.getPhone())) {
			result.put(BaseConstants.RESULT_MESSAGE, "전화번호를 입력하지 않으셨습니다.");
		}else if (CommonUtil.isEmpty(qna.getEmail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "메일을 입력하지 않으셨습니다.");
		}else if ((qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.ORDER") || qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.DELIVERY") || qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.EXCHANGE")) 
				&& CommonUtil.isEmpty(qna.getOrderId())) {
			result.put(BaseConstants.RESULT_MESSAGE, "주문 상품을 선택하지 않으셨습니다.");
		}else if (qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.BRAND") && CommonUtil.isEmpty(qna.getBrandId())) {
			result.put(BaseConstants.RESULT_MESSAGE, "브랜드를 선택하지 않으셨습니다.");
		}
		
//		else if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
//			result.put(BaseConstants.RESULT_MESSAGE, "입력하지 않으셨습니다.");
//		}else if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
//			result.put(BaseConstants.RESULT_MESSAGE, "입력하지 않으셨습니다.");
//		}else if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
//			result.put(BaseConstants.RESULT_MESSAGE, "입력하지 않으셨습니다.");
//		}else if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
//			result.put(BaseConstants.RESULT_MESSAGE, "입력하지 않으셨습니다.");
//		}
		else{
			result = qnaService.saveQna(qna);
		}
		return result;
	}

}


