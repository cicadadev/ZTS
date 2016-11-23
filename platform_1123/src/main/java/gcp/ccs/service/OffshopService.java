package gcp.ccs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.mms.model.MmsInterestoffshop;
import gcp.pms.model.custom.PmsOffpickupProduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class OffshopService extends BaseService {

	/**
	 * 
	 * @Method Name : getOffshopList
	 * @author : emily
	 * @date : 2016. 6. 23.
	 * @description : 매장 검색 팝업
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsOffshop> getOffshopList(CcsOffshopSearch search) {
		return (List<CcsOffshop>) dao.selectList("ccs.offshop.getOffshopPopupList", search);
	}
	
	/**
	 * 
	 * @Method Name : getCcsOffshop
	 * @author : allen
	 * @date : 2016. 8. 4.
	 * @description : 매장 상세 정보 조회
	 *
	 * @param search
	 * @return
	 */
	public CcsOffshop getOffshopInfo(CcsOffshopSearch search) {
		return (CcsOffshop) dao.selectOne("ccs.offshop.getOffshopInfo", search);
	}

	/**
	 * 
	 * @Method Name : getOffshopType
	 * @author : stella
	 * @date : 2016. 8. 22.
	 * @description : 매장 유형 조회
	 *
	 * @param search
	 * @return
	 */
	public List<String> getOffshopType(CcsOffshopSearch search) {
		return (List<String>) dao.selectList("ccs.offshop.getOffshopType", search);
	}
	
	/**
	 * 
	 * @Method Name : getOffshopSearchList
	 * @author : stella
	 * @date : 2016. 8. 22.
	 * @description : 매장 조회
	 *
	 * @param search
	 * @return
	 */
	public List<CcsOffshop> getOffshopSearchList(CcsOffshopSearch search) {
		return (List<CcsOffshop>) dao.selectList("ccs.offshop.getOffshopList", search);
	}
	
	/**
	 * 
	 * @Method Name : updateInterestOffshop
	 * @author : stella
	 * @date : 2016. 8. 22.
	 * @description : 관심매장 등록/해제
	 *
	 * @param search
	 * @return 
	 * @return
	 */
	public void updateInterestOffshop(List<MmsInterestoffshop> interestOffshop) throws Exception {
		for (MmsInterestoffshop offshop : interestOffshop) {
			offshop.setStoreId(SessionUtil.getStoreId());
			offshop.setMemberNo(SessionUtil.getMemberNo());
			
			if ("Y".equals(offshop.getInterestYn())) {
				if (CommonUtil.isEmpty(offshop.getTopYn())) {
					offshop.setTopYn("N");
					dao.insert("mms.interestoffshop.insertInterestOffshop", offshop);
				} else {
					dao.update("mms.interestoffshop.updateInterestOffshop", offshop);
				}
				
			} else if ("N".equals(offshop.getInterestYn())){
				dao.deleteOneTable(offshop);
			}
		}
		
	}

	/**
	 * 오프매장 시/도정보 조회
	 * 
	 * @Method Name : getOffshopAreaDiv2List
	 * @author : eddie
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @param search saleproduct_id, brandId, pickupYn
	 * @return
	 */
	public List<CcsOffshop> getOffshopAreaDiv1List(CcsOffshopSearch search) {
		return (List<CcsOffshop>) dao.selectList("ccs.offshop.getOffshopAreaDiv1List", search);
	}
	
	/**
	 * 오프매장 시/도 정보로 시/군/구정보 조회
	 * 
	 * @Method Name : getOffshopAreaDiv2List
	 * @author : eddie
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @param search saleproduct_id, brandId, pickupYn, offshopArea1(필수)
	 * @return
	 */
	public List<CcsOffshop> getOffshopAreaDiv2List(CcsOffshopSearch search) {
		return (List<CcsOffshop>) dao.selectList("ccs.offshop.getOffshopAreaDiv2List", search);
	}

	/**
	 * 오프매장 시/도,시/군/구 정보로 지점 목록 조회
	 * 
	 * @Method Name : getOffshopAreaDiv2List
	 * @author : eddie
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<CcsOffshop> getOffshopList2(CcsOffshopSearch search) {
		return (List<CcsOffshop>) dao.selectList("ccs.offshop.getOffshopList2", search);
	}
	
	
	public List<PmsOffpickupProduct> getSaleproductPickupshopInfo(List<CcsOffshopSearch> searchList) {

		String storeId = SessionUtil.getStoreId();

		List<PmsOffpickupProduct> results = new ArrayList<PmsOffpickupProduct>();
		for (CcsOffshopSearch search : searchList) {
			search.setStoreId(storeId);
			PmsOffpickupProduct info = (PmsOffpickupProduct) dao.selectOne("ccs.offshop.getSaleproductPickupshopInfo", search);

			CcsOffshopSearch s1 = new CcsOffshopSearch();
			s1.setSaleproductId(info.getSaleproductId());
			s1.setStoreId(storeId);
			s1.setPickupYn("Y");
			List<CcsOffshop> areaDiv1s = (List<CcsOffshop>) getOffshopAreaDiv1List(s1);

			info.setAreaDiv1s(areaDiv1s);

			// offshop정보가 있으면 div2목록, 매장목록 조회
			if (info.getCcsOffshop() != null && CommonUtil.isNotEmpty(info.getCcsOffshop().getOffshopId())) {
				// 시군구 목록 조회
				s1.setOffshopArea1(info.getCcsOffshop().getAreaDiv1());
				List<CcsOffshop> areaDiv2s = (List<CcsOffshop>) getOffshopAreaDiv2List(s1);

				// 매장목록 조회
				s1.setOffshopArea1(info.getCcsOffshop().getAreaDiv2());
				List<CcsOffshop> shopList = (List<CcsOffshop>) getOffshopList2(s1);

				info.setAreaDiv2s(areaDiv2s);
				info.setShopList(shopList);

			}

			results.add(info);
		}

		return results;
	}

	/**
	 * 
	 * @Method Name : getShopOnOffShopInfoList
	 * @author : allen
	 * @date : 2016. 10. 17.
	 * @description : 회원 관심 매장 조회
	 *
	 * @param search
	 * @return
	 */
	public List<MmsInterestoffshop> getShopOnOffShopInfoList(CcsOffshopSearch search) {
		return (List<MmsInterestoffshop>) dao.selectList("ccs.offshop.getShopOnOffShopInfoList", search);
	}
	 
}
