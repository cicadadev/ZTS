package gcp.admin.controller.rest.sps;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.search.SpsCardPromotionSearch;
import gcp.sps.service.DiscountService;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/sps/discount")
public class DiscountController {
	
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private DiscountService discountService;
	
	/**
	 * 카드사 할인관리 검색 목록
	 * @Method Name : getCreditCardList
	 * @author : emily
	 * @date : 2016. 7. 27.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/card/list", method = {RequestMethod.POST })
	public List<SpsCardpromotion> getCreditCardList(@RequestBody SpsCardPromotionSearch search) {

		search.setStoreId(SessionUtil.getStoreId());
		List<SpsCardpromotion> list= discountService.getCreditCardList(search);
		
		return list;
	}
	
	/**
	 * 카드사 할인 정보 삭제 
	 * @Method Name : deleteCreditCard
	 * @author : emily
	 * @date : 2016. 7. 27.
	 * @description : 
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/delete", method = RequestMethod.POST)
	public BaseEntity deleteCreditCard(@RequestBody List<SpsCardpromotion> list) throws Exception {	
		BaseEntity baseMasage = new BaseEntity();

		try {
			discountService.deleteCreditCard(list);
		} catch (Exception e) {
			baseMasage.setSuccess(false);
			baseMasage.setResultMessage(e.getMessage());
		}
		return baseMasage;
	}
	
	/**
	 * 카드사 할인정보 상세 조회 
	 * @Method Name : getCreditCardInfo
	 * @author : emily
	 * @date : 2016. 7. 27.
	 * @description : 
	 *
	 * @param card
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/detail", method = { RequestMethod.POST })
	public SpsCardpromotion getCreditCardInfo(@RequestBody SpsCardpromotion search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return discountService.getCreditCardInfo(search);
	}
	
	/**
	 * 카드사 할인정보 저장
	 * @Method Name : saveCreditCard
	 * @author : emily
	 * @date : 2016. 7. 28.
	 * @description : 
	 *
	 * @param card
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/save",method = RequestMethod.PUT )
	public String saveCreditCard(@RequestBody SpsCardpromotion cardPromotion) throws Exception {	
		String resultCode = "";
		
		try {
			cardPromotion.setStoreId(SessionUtil.getStoreId());			
			cardPromotion.setInsId(SessionUtil.getLoginId());
			cardPromotion.setUpdId(SessionUtil.getLoginId());
			discountService.saveCreditCard(cardPromotion);
			
			resultCode = String.valueOf(cardPromotion.getCardPromotionNo());
		} catch (Exception e) {
			resultCode = "fail";
			logger.debug(e);
		}
		
		return resultCode;
	}
	
	/**
	 * 
	 * @Method Name : changeState
	 * @author : roy
	 * @date : 2016. 8. 22.
	 * @description : 카드사 할인 상태 변경
	 *
	 * @param bu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeState", method = RequestMethod.PUT)
	public String changeState(@RequestBody SpsCardpromotion cardPromotion) throws Exception {

		String resultCode = "";

		try {
			cardPromotion.setStoreId(SessionUtil.getStoreId());
			cardPromotion.setUpdId(SessionUtil.getLoginId());
			discountService.changeState(cardPromotion);
			resultCode = "success";
		} catch (Exception e) {
			resultCode = "fail";
		}

		return resultCode;
	}
}
