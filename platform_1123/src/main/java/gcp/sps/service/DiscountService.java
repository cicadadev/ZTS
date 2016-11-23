package gcp.sps.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.search.SpsCardPromotionSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class DiscountService extends BaseService {

	/**
	 * 카드사 할인 목록 조회
	 * @Method Name : getCreditCardList
	 * @author : emily
	 * @date : 2016. 7. 27.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpsCardpromotion> getCreditCardList(SpsCardPromotionSearch search){
		return (List<SpsCardpromotion>) dao.selectList("sps.discount.getCardPromotionList", search);
	}

	/**
	 * 카드사 할인정보 삭제
	 * @Method Name : deleteCreditCard
	 * @author : emily
	 * @date : 2016. 7. 27.
	 * @description : 
	 *
	 * @param list
	 * @throws Exception
	 */
	public void deleteCreditCard(List<SpsCardpromotion> list) throws Exception {		
		for (SpsCardpromotion cardPromotion : list) {
			cardPromotion.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(cardPromotion);
		}
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
	public SpsCardpromotion getCreditCardInfo(SpsCardpromotion cardPromotion) throws Exception {
		return (SpsCardpromotion) dao.selectOne("sps.discount.getCardpromotion", cardPromotion);
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
	public String saveCreditCard(SpsCardpromotion cardPromotion) throws Exception {
		if(CommonUtil.isNotEmpty(cardPromotion.getCardPromotionNo())){
			 dao.updateOneTable(cardPromotion);
		}else{
			dao.insertOneTable(cardPromotion);
//			 dao.insert("sps.discount.insertCreditCard", cardPromotion);
		}
		return String.valueOf(cardPromotion.getCardPromotionNo());
	}
	
	/**
	 * 
	 * @Method Name : changeState
	 * @author : roy
	 * @date : 2016. 8. 22.
	 * @description : 카드사 할인 상태 수정
	 *
	 * @param cardPromotion
	 * @return
	 * @throws Exception
	 */
	public String changeState(SpsCardpromotion cardPromotion) throws Exception {
		// 업체 수정
		dao.updateOneTable(cardPromotion);
		return cardPromotion.getCardPromotionNo().toString();
	}

	/**
	 * 
	 * @Method Name : getApplyCardPromotion
	 * @author : dennis
	 * @date : 2016. 9. 8.
	 * @description : 유효한 카드사프로모션
	 *
	 * @return
	 */
	public List<SpsCardpromotion> getApplyCardPromotion(SpsCardPromotionSearch search) {
		return (List<SpsCardpromotion>) dao.selectList("sps.discount.getApplyCardPromotion", search);
	}

}
