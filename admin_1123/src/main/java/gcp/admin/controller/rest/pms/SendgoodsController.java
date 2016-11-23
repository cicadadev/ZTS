package gcp.admin.controller.rest.pms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.external.model.search.PmsSendgoodsSearch;
import gcp.external.service.SendgoodsService;
import gcp.external.util.TerminalUtil;
import gcp.pms.model.PmsProduct;

@RestController
@RequestMapping("api/pms/sendgoods")
public class SendgoodsController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private SendgoodsService	sendgoodsService;

	/**
	 * @Method Name : getEpexcproductList
	 * @author : peter
	 * @date : 2016. 6. 22.
	 * @description : 상품 목록 조회
	 *
	 * @param pss
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<PmsProduct> getSendgoodsList(@RequestBody PmsSendgoodsSearch pss) {
		return sendgoodsService.getSendgoodsList(pss);
	}

	/**
	 * @Method Name : insertSendgoods
	 * @author : peter
	 * @date : 2016. 6. 22.
	 * @description : 선택 상품 사방넷 전송
	 *
	 * @param ppList
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertSendgoods(@RequestBody List<PmsProduct> ppList) {
		String result = "";

		try {
			result = sendgoodsService.insertSendgoods(ppList);
		} catch (Exception e) {
			logger.error("상품전송 Error: " + e.toString());
			result = "상품전송 Error";
			e.printStackTrace();
		}

		try {
			//사방넷 호출
			String rtnValue = TerminalUtil.sendPostSbn("good");
			result = result + "작업이 완료되었습니다. 호출결과: " + rtnValue;
		} catch (Exception e) {
			logger.error("사방넷 통신 Error: " + e.toString());
			result += "사방넷 통신 에러!!";
			e.printStackTrace();
		}

		return result;
	}
}
