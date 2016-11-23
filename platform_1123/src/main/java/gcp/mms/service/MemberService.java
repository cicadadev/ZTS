package gcp.mms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.service.BaseService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsInterest;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsMemberZtsHistory;
import gcp.mms.model.MmsMemberbaby;
import gcp.mms.model.MmsMembermenu;
import gcp.mms.model.MmsMembersns;
import gcp.mms.model.MmsQuickmenu;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.base.BaseMmsMember;
import gcp.mms.model.base.BaseMmsMemberZts;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPosorder;
import gcp.oms.model.OmsPosorderproduct;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.service.CouponService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;


@Service
public class MemberService extends BaseService {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private CouponService	couponService;
	/**
	 * 
	 * @Method Name : getMemberList
	 * @author : dennis
	 * @date : 2016. 5. 23.
	 * @description : 회원 검색
	 *
	 * @param mmsMemberPopupSearch
	 * @return List<MmsMember>
	 */
	@SuppressWarnings("unchecked")
	public List<MmsMemberZts> getMemberList(MmsMemberPopupSearch mmsMemberPopupSearch) {

		// id 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(mmsMemberPopupSearch.getMemberIds())) {

			String temp = CommonUtil.isNotEmpty(mmsMemberPopupSearch.getMemberIds()) ? mmsMemberPopupSearch.getMemberIds() : "";

			mmsMemberPopupSearch = new MmsMemberPopupSearch();
			if (!"".equals(temp)) {
				mmsMemberPopupSearch.setMemberIds(temp);
			}
		} else if (StringUtils.isNotEmpty(mmsMemberPopupSearch.getMemberType())) {
			String memberTypes[] = mmsMemberPopupSearch.getMemberType().split(",");

			for (String memberType : memberTypes) {
				if (BaseConstants.MEMBER_TYPE_CD_GENERAL.equals(memberType)) {
					mmsMemberPopupSearch.setGeneralYn("Y");
				}
				if (BaseConstants.MEMBER_TYPE_CD_MEMBERSHIP.equals(memberType)) {
					mmsMemberPopupSearch.setMembershipYn("Y");
				}
				if (BaseConstants.MEMBER_TYPE_CD_PREMIUM.equals(memberType)) {
					mmsMemberPopupSearch.setPremiumYn("Y");
				}
				if (BaseConstants.MEMBER_TYPE_CD_EMPLOYEE.equals(memberType)) {
					mmsMemberPopupSearch.setEmployeeYn("Y");
				}
				if (BaseConstants.MEMBER_TYPE_CD_CHILDREN.equals(memberType)) {
					mmsMemberPopupSearch.setChildrenYn("Y");
				}
				if (BaseConstants.MEMBER_TYPE_CD_B2E.equals(memberType)) {
					mmsMemberPopupSearch.setB2eYn("Y");
				}
			}
		}

		mmsMemberPopupSearch.setStoreId(SessionUtil.getStoreId());

		return (List<MmsMemberZts>) dao.selectList("mms.member.getMemberList", mmsMemberPopupSearch);
	}
	
	/**
	 * 
	 * @Method Name : getMemBerRlst
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 회원 검색 목록 조회
	 *
	 * @param mmsMemberSearch
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MmsMember> getMemberSearchList(MmsMemberSearch mmsMemberSearch) throws Exception {
		return (List<MmsMember>) dao.selectList("mms.member.getMemberSearchList", mmsMemberSearch);
	};

	public void deleteWishList(MmsWishlist wish) throws Exception {
		dao.delete("mms.member.deleteWishlist", wish);
	}
	/**
	 * 
	 * @Method Name : saveWishList
	 * @author : dennis
	 * @date : 2016. 7. 5.
	 * @description : 위쉬리스트 저장
	 *
	 * @param mmsWishlist
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> saveWishList(MmsMemberZts mmsMemberZts) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String storeId = mmsMemberZts.getStoreId();
		BigDecimal memberNo = mmsMemberZts.getMemberNo();
		String userId = mmsMemberZts.getUpdId();

		if (storeId == null || memberNo == null) {
			result.put(BaseConstants.RESULT_MESSAGE, "로그인 정보가 없습니다.");
			return result;
		}

		List<MmsWishlist> mmsWishlist = mmsMemberZts.getMmsWishlists();
		for (MmsWishlist wish : mmsWishlist) {
			wish.setStoreId(storeId);
			wish.setMemberNo(memberNo);
			String wishlistNo = (String) dao.selectOne("mms.member.getExistsWishlist", wish);
			
			if(CommonUtil.isEmpty(wishlistNo)) {
				PmsProduct product = new PmsProduct();
				product.setStoreId(storeId);
				product.setProductId(wish.getProductId());
				product = (PmsProduct) dao.selectOneTable(product);
				
				String productTypeCd = product.getProductTypeCd();
				
				String wishlistProductTypeCd = "";
				if (BaseConstants.PRODUCT_TYPE_CD_GENERAL.equals(productTypeCd)) {
					wishlistProductTypeCd = "WISHLIST_PRODUCT_TYPE_CD.GENERAL";
				} else if (BaseConstants.PRODUCT_TYPE_CD_SET.equals(productTypeCd)) {
					wishlistProductTypeCd = "WISHLIST_PRODUCT_TYPE_CD.SET";
				}
				wish.setWishlistProductTypeCd(wishlistProductTypeCd);
				
				dao.insertOneTable(wish);
				
//				BigDecimal upperWishlistNo = wish.getWishlistNo();
//					
//				if (BaseConstants.PRODUCT_TYPE_CD_SET.equals(productTypeCd)) {
//					for (MmsWishlist subWish : wish.getMmsWishlists()) {
//						subWish.setStoreId(storeId);
//						subWish.setMemberNo(memberNo);
//						subWish.setUpperWishlistNo(upperWishlistNo);
//						subWish.setWishlistProductTypeCd("WISHLIST_PRODUCT_TYPE_CD.SUB");
//						dao.insertOneTable(subWish);
//					}
//				}
			} else {
				wish.setUpdId(userId);
				dao.update("mms.member.updateWishlist", wish);
			}
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : getMemberDetail
	 * @author : allen
	 * @date : 2016. 7. 13.
	 * @description : 회원상세 정보 조회
	 *
	 * @param memberNo
	 * @return
	 */
	public MmsMemberZts getMemberDetail(BigDecimal memberNo) {
		return (MmsMemberZts) dao.selectOne("mms.member.getMemberDetail", memberNo);
	}

	/**
	 * 
	 * @Method Name : getMemberGradeHistory
	 * @author : allen
	 * @date : 2016. 7. 13.
	 * @description : 회원 등급 히스토리 조회
	 *
	 * @param memberNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsMemberZtsHistory> getMemberGradeHistory(String memberNo) {
		return (List<MmsMemberZtsHistory>) dao.selectList("mms.member.getMemberGradeHistory", memberNo);
	}

	/**
	 * 
	 * @Method Name : updateMarketingReceipt
	 * @author : allen
	 * @date : 2016. 7. 14.
	 * @description : 마케팅 수신 여부 변경
	 *
	 * @param mmsCustomer
	 */
	public void updateMarketingReceipt(MmsMember mmsMember) {
		dao.update("mms.member.updateMarketingReceipt", mmsMember);
	}

	/**
	 * 
	 * @Method Name : getMemberDepositList
	 * @author : allen
	 * @date : 2016. 7. 19.
	 * @description : 회원 예치금 내역 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsDeposit> getMemberDepositList(MmsMemberPopupSearch search) {
		return (List<MmsDeposit>) dao.selectList("mms.member.getMemberDepositList", search);
	}

	public BigDecimal getDepositBalanceAmt(MmsMemberPopupSearch search) {
		return (BigDecimal) dao.selectOne("mms.member.getLatestDepositBalanceAmt", search);
	}

	/**
	 * @Method Name : getDepositList
	 * @author : ian
	 * @date : 2016. 8. 17.
	 * @description : 주문관리 > 예치금 내역 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsDeposit> getDepositList(MmsMemberPopupSearch search) {
		return (List<MmsDeposit>) dao.selectList("mms.member.getDepositList", search);
	}

	/**
	 * 
	 * @Method Name : saveMemberDeposit
	 * @author : allen
	 * @date : 2016. 7. 20.
	 * @description : 예치금 저장
	 *
	 * @param mmsDeposit
	 * @throws Exception
	 */
	public Map<String, String> saveMemberDeposit(MmsDeposit deposit) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		String resultCode = BaseConstants.RESULT_FLAG_FAIL;
		String resultMsg = "";
		boolean result = false;
		
		if (deposit.getDepositAmt() != null && CommonUtil.isNotEmpty(deposit.getMemberNo())) {
			
			// 적립
			if(deposit.getDepositAmt().compareTo(BigDecimal.ZERO) > 0) {
				dao.insertOneTable(deposit);
				result = true;
			} 
			// 차감
			else {
				BigDecimal latestBalanceAmt = (BigDecimal) dao.selectOne("mms.member.getLatestDepositBalanceAmt", deposit);
				BigDecimal sumBalanceAmt = BigDecimal.ZERO;
				
				if(latestBalanceAmt != null) {
					sumBalanceAmt = deposit.getDepositAmt().add(latestBalanceAmt);
					if (sumBalanceAmt.compareTo(BigDecimal.ZERO) != -1) {
						dao.insertOneTable(deposit);
						result = true;
					}
				}
			}
			
			if(result) {
				resultCode = BaseConstants.RESULT_FLAG_SUCCESS;
				resultMsg = "success";
			} else {
				resultCode = BaseConstants.RESULT_FLAG_FAIL;
				resultMsg = MessageUtil.getMessage("mms.deposit.lack.amt");
			}
			
		} else {
			if(deposit.getDepositAmt() == null) {
				resultMsg = "조정 예치금이 누락 되었습니다.";
			}
			if(CommonUtil.isNotEmpty(deposit.getMemberNo())) {
				resultMsg = "회원번호가 누락되었습니다.";
			}
		}
		
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		
		return resultMap;
	}

	/**
	 * 
	 * @Method Name : getMemberCarrotList
	 * @author : allen
	 * @date : 2016. 7. 19.
	 * @description : 회원 당근 내역 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsCarrot> getMemberCarrotList(MmsMemberPopupSearch search) {
		return (List<MmsCarrot>) dao.selectList("mms.member.getMemberCarrotList", search);
	}

	public BigDecimal getLatestCarrotAmt(MmsMemberPopupSearch search) {

		BigDecimal balanceAmt = (BigDecimal) dao.selectOne("mms.member.getLatestCarrotAmt", search);
		return balanceAmt;
	}

	@SuppressWarnings("unchecked")
	public List<MmsCarrot> getCarrotList(MmsMemberPopupSearch search) {
		return (List<MmsCarrot>) dao.selectList("mms.member.getCarrotList", search);
	}

	@SuppressWarnings("unchecked")
	public List<MmsCarrot> getCarrotSummry(MmsMemberPopupSearch search) {
		return (List<MmsCarrot>) dao.selectList("mms.member.getCarrotSummery", search);
	}

	/**
	 * 
	 * @Method Name : getMemberAddressList
	 * @author : dennis
	 * @date : 2016. 7. 19.
	 * @description : 회원 배송주소 목록 (기본배송지 우선 정렬 조회)
	 *
	 * @param mmsMemberSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsAddress> getMemberAddressList(MmsMemberSearch mmsMemberSearch) {
		return (List<MmsAddress>) dao.selectList("mms.member.getMemberAddressList", mmsMemberSearch);
	}

	/**
	 * 
	 * @Method Name : deleteMemberAddress
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 배송지 삭제
	 *
	 * @param address
	 * @throws Exception
	 */
	public void deleteMemberAddress(MmsAddress address) throws Exception {
		dao.deleteOneTable(address);
	}

	public void saveMemberAddress(MmsAddress address) throws Exception {
		address.setMemberNo(SessionUtil.getMemberNo());
		if (BaseConstants.CRUD_TYPE_CREATE.equals(address.getCrudType())) {
			dao.insertOneTable(address);
		} else {
			dao.updateOneTable(address);
		}

		if (StringUtils.isNotEmpty(address.getBasicYn())) {
			MmsMemberZts mmsMemberZts = new MmsMemberZts();
			mmsMemberZts.setMemberNo(SessionUtil.getMemberNo());
			mmsMemberZts.setUpdId(SessionUtil.getLoginId());
			mmsMemberZts.setAddressNo(address.getAddressNo());
			updateBasicAddress(mmsMemberZts);
		}
	}

	/**
	 * 
	 * @Method Name : updateBasicAddress
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 기본 배송지 수정
	 *
	 * @param mmsMemberZts
	 */
	public void updateBasicAddress(MmsMemberZts mmsMemberZts) {
		dao.update("mms.member.updateBasicAddress", mmsMemberZts);
	}

	/**
	 * 
	 * @Method Name : getMemberBasicAddress
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 회원 기본 배송지 조회
	 *
	 * @param mmsMemberSearch
	 * @return
	 */
	public MmsAddress getMemberBasicAddress(MmsMemberSearch mmsMemberSearch) {
		return (MmsAddress) dao.selectOne("mms.member.getMemberBasicAddress", mmsMemberSearch);
	}

	/**
	 * 
	 * @Method Name : getRecentOrderDeliveyAddress
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 회원 최근 주문 배송지 조회
	 *
	 * @param mmsMemberSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsDeliveryaddress> getRecentOrderDeliveyAddress(MmsMemberSearch mmsMemberSearch) {
		return (List<OmsDeliveryaddress>) dao.selectList("mms.member.getRecentOrderDeliveyAddress", mmsMemberSearch);
	}

	/**
	 * 당근 조정 필수값 체크
	 * 
	 * @Method Name : isCarrotParamValid
	 * @author : intune
	 * @date : 2016. 11. 1.
	 * @description :
	 *
	 * @param carrot
	 * @param resultMap
	 * @return
	 */
	private boolean isCarrotParamValid(MmsCarrot carrot, Map<String, String> resultMap) {

		// 조정당근과 회원번호 누락시 false
		if (carrot.getCarrot() == null || carrot.getCarrot() != null && carrot.getCarrot().compareTo(BigDecimal.ZERO) == 0) {
			resultMap.put("resultCode", BaseConstants.RESULT_FLAG_FAIL);
			resultMap.put("resultMsg", "조정 할 당근이 누락되었습니다.");
			return false;
		}
		if (CommonUtil.isEmpty(carrot.getMemberNo())) {
			resultMap.put("resultCode", BaseConstants.RESULT_FLAG_FAIL);
			resultMap.put("resultMsg", "회원번호가 누락되었습니다.");
			return false;
		}

		return true;

	}

	/**
	 * 당금 적립 처리
	 * 
	 * @Method Name : addCarrot
	 * @author : intune
	 * @date : 2016. 11. 1.
	 * @description :
	 *
	 * @param carrot
	 * @throws Exception
	 */
	private void saveCarrot(MmsCarrot carrot) throws Exception {

		String today = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		// 만료일 6개월 후
		carrot.setExpireDt(DateUtil.getAddMonth(DateUtil.FORMAT_1, today, new BigDecimal(6)));
		carrot.setBalanceCarrot(carrot.getCarrot());
		dao.insertOneTable(carrot);

	}

	/**
	 * 당근 사용 처리
	 * 
	 * @Method Name : useCarrot
	 * @author : intune
	 * @date : 2016. 11. 1.
	 * @description :
	 *
	 * @param carrot
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	private boolean useCarrot(MmsCarrot carrot, Map<String, String> resultMap) throws Exception {

		// 잔액 조회
		BigDecimal latestBalanceAmt = (BigDecimal) dao.selectOne("mms.member.getLatestCarrotAmt", carrot);

		// 잔액이 사용당근 보다 적으면 실패
		if (latestBalanceAmt.compareTo(carrot.getCarrot().negate()) == -1) {
			resultMap.put("resultCode", BaseConstants.RESULT_FLAG_FAIL);
			resultMap.put("resultMsg", "조정할 당근이 없습니다.");

			return false;
		} else {

			carrot.setBalanceCarrot(BigDecimal.ZERO);
			carrot.setExpireDt(BaseConstants.SYSDATE);
			dao.insertOneTable(carrot);	// 사용내역

			saveBalanceCarrot(carrot); // 차감조정
		}

		return true;

	}
	/**
	 * @Method Name : saveCarrot
	 * @author : ian
	 * @date : 2016. 7. 19.
	 * @description : 당근조정
	 *
	 * @param carrot
	 * @return
	 */
	public Map<String, String> updateCarrot(MmsCarrot carrot) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap.put("resultCode", BaseConstants.RESULT_FLAG_SUCCESS);
		resultMap.put("resultMsg", "성공적으로 저장하였습니다.");

		// 1.필수값 체크 : carrot, memberNo
		if (!isCarrotParamValid(carrot, resultMap)) {
			return resultMap;
		}

		// 2. 적립/사용별 처리
		if (carrot.getCarrot().compareTo(BigDecimal.ZERO) == -1) {

			// 2-2. 사용일 경우
			if (useCarrot(carrot, resultMap)) {
				return resultMap;
			}

		} else {

			// 2-1. 적립일경우
			saveCarrot(carrot);

		}
		return resultMap;
		
	}


	/**
	 * 
	 * @Method Name : getMemberOffOrderList
	 * @author : allen
	 * @date : 2016. 8. 17.
	 * @description : 회원 오프라인 구매내역 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsPosorder> getMemberOffOrderList(MmsMemberPopupSearch search) {
		return (List<OmsPosorder>) dao.selectList("mms.member.getMemberOffOrderList", search);
	}
	

	/**
	 * 
	 * @Method Name : getMemberOffOrderProductList
	 * @author : allen
	 * @date : 2016. 8. 19.
	 * @description : 회원 오프라인 구매 상품 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsPosorderproduct> getMemberOffOrderProductList(MmsMemberPopupSearch search) {
		return (List<OmsPosorderproduct>) dao.selectList("mms.member.getMemberOffOrderProductList", search);
	}

	@SuppressWarnings("unchecked")
	public List<OmsPosorder> getMemberOffOrderProductListForFront(MmsMemberSearch search) {
		return (List<OmsPosorder>) dao.selectList("mms.member.getMemberOffOrderProductListForFront", search);
	}

	public OmsPosorder getMemberOffOrderDetail(OmsOrderSearch search) {
		return (OmsPosorder) dao.selectOne("mms.member.getMemberOffOrderDetail", search);
	}

	@SuppressWarnings("unchecked")
	public List<CcsOffshop> getMemberOffOrderRegionList(MmsMemberSearch search) {
		return (List<CcsOffshop>) dao.selectList("mms.member.getMemberOffOrderRegionList", search);
	}

//	/**
//	 * 프론트 공통 영역 위시리스트 Count 조회
//	 * 
//	 * @Method Name : getWishListCount
//	 * @author : emily
//	 * @date : 2016. 8. 2.
//	 * @description :
//	 *
//	 * @param mmsMemberSearch
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<PmsProduct> getWishList(MmsMemberSearch mmsMemberSearch) {
//		return (List<PmsProduct>) dao.selectList("mms.member.getWishList", mmsMemberSearch);
//	}

	/**
	 * 
	 * @Method Name : getMemberTypeInfo
	 * @author : dennis
	 * @date : 2016. 8. 22.
	 * @description : 회원유형코드 정보 (회원등급포함)
	 *
	 * @param memberNo
	 * @return
	 */
	public void getMemberTypeInfo(List<String> memberTypeCds) {
//		Map<String, Object> result = new HashMap<String, Object>();

//		List<String> memberTypeCds = new ArrayList<String>();

		// 회원 유형 설정
		if ("Y".equals(FoSessionUtil.getPremiumYn())) {
			memberTypeCds.add("MEMBER_TYPE_CD.PREMIUM");
		}
		if ("Y".equals(FoSessionUtil.getMembershipYn())) {
			memberTypeCds.add("MEMBER_TYPE_CD.MEMBERSHIP");
		}
		if ("Y".equals(FoSessionUtil.getEmployeeYn())) {
			memberTypeCds.add("MEMBER_TYPE_CD.EMPLOYEE");
		}
		if ("Y".equals(FoSessionUtil.getChildrenYn())) {
			memberTypeCds.add("MEMBER_TYPE_CD.CHILDREN");
		}
		if ("Y".equals(FoSessionUtil.getB2eYn())) {
			memberTypeCds.add("MEMBER_TYPE_CD.B2E");
		}

		if (memberTypeCds == null || (memberTypeCds != null && memberTypeCds.size() == 0)) {
			memberTypeCds.add("MEMBER_TYPE_CD.GENERAL");
		}
//		result.put("memberTypeCd", memberTypeCds);
//		result.put("memGradeCd", SessionUtil.getMemGradeCd());
//		return result;
	}
	
	/**
	 * 
	 * @Method Name : getInterestOffshopList
	 * @author : stella
	 * @date : 2016. 9. 7.
	 * @description : 관심매장 조회
	 *
	 * @param MmsMemberSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsInterestoffshop> getInterestOffshopList(MmsMemberSearch search) {
		return (List<MmsInterestoffshop>) dao.selectList("mms.interestoffshop.getInterestOffshopList", search);
	}

	/**
	 * 
	 * @Method Name : getQuickmenuList
	 * @author : allen
	 * @date : 2016. 9. 9.
	 * @description : 퀵메뉴리스트 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsQuickmenu> getQuickmenuList(MmsMemberSearch search) {
		return (List<MmsQuickmenu>) dao.selectList("mms.member.getQuickmenuList", search);
	}

	public void deleteMemberMenu(MmsMembermenu menu) {
		dao.delete("mms.member.deleteMemberMenu", menu);
	}

	public void saveMemberMenu(List<MmsMembermenu> menuList) throws Exception {

		if (menuList != null) {
			for (MmsMembermenu mmsMembermenu : menuList) {
				mmsMembermenu.setMemberNo(SessionUtil.getMemberNo());
				dao.insertOneTable(mmsMembermenu);
			}
		}
	}

	/**
	 * 
	 * @Method Name : insertMmsInterest
	 * @author : roy
	 * @date : 2016. 9. 9.
	 * @description : 관심 브랜드 저장
	 *
	 * @param MmsInterest
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> insertMmsInterest(MmsInterest mmsInterest) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try {
			dao.insertOneTable(mmsInterest);
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} catch (Exception e) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			return result;
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : deleteMmsInterest
	 * @author : roy
	 * @date : 2016. 9. 9.
	 * @description : 관심 브랜드 삭제
	 *
	 * @param MmsInterest
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> deleteMmsInterest(MmsInterest mmsInterest) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try {
			dao.deleteOneTable(mmsInterest);
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} catch (Exception e) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			return result;
		}
		return result;
	}
	
	public MmsMemberSearch getMemberGiftOrderCnt(MmsMemberSearch search) {
		return (MmsMemberSearch) dao.selectOne("mms.member.getMemberGiftOrderCnt", search);
	}

	@SuppressWarnings("unchecked")
	public List<OmsOrder> getMemberGiftOrderList(MmsMemberSearch search) {
		return (List<OmsOrder>) dao.selectList("mms.member.getMemberGiftOrderList", search);
	}

	public OmsOrder getGiftOrderDetail(MmsMemberSearch search) {
		return (OmsOrder) dao.selectOne("mms.member.getGiftOrderDetail", search);
	}
	
	/**
	 * 
	 * @Method Name : getMemberZts
	 * @author : stella
	 * @date : 2016. 9. 26.
	 * @description : ZTS회원 정보 조회
	 *
	 * @param MmsMemberSearch
	 * @return
	 * @throws Exception
	 */
	public MmsMemberZts getMemberZts(MmsMemberSearch search) throws Exception {
		return (MmsMemberZts) dao.selectOne("mms.member.getMemberZts", search);
	}

	/**
	 * 
	 * @Method Name : getMemberZts
	 * @author : stella
	 * @date : 2016. 9. 26.
	 * @description : 관심정보 설정 조회
	 *
	 * @param MmsInterest
	 * @return
	 * @throws Exception
	 */
	public String getExistMemberInterest(MmsMemberSearch search) throws Exception {
		return (String) dao.selectOne("mms.member.getExistMemberInterest", search);
	}
	
	/**
	 * 
	 * @Method Name : saveMmsInterestInfo
	 * @author : stella
	 * @date : 2016. 9. 26.
	 * @description : 관심정보(레이어 팝업) 저장
	 *
	 * @param MmsInterest
	 * @return
	 * @throws Exception
	 */
	public void saveMmsInterestInfo(List<MmsInterest> interestInfo) throws Exception {
		BigDecimal memberNo = SessionUtil.getMemberNo();
			
		for (MmsInterest interest : interestInfo) {
			interest.setMemberNo(memberNo);
			dao.insert("mms.member.insertMmsInterest", interest);
		}
		
		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(memberNo);
		
		if (interestInfo.size() > 0) {
			if (CommonUtil.isNotEmpty(interestInfo.get(0).getMmsMemberZts())) {
				MmsMemberZts memberInfo = interestInfo.get(0).getMmsMemberZts();

				memberInfo.setMembershipYn("Y");
				memberInfo.setMemberNo(memberNo);

				dao.updateOneTable(memberInfo);

				FoSessionUtil.setMembershipYn("Y");
				// TODO 회원가입시 mms_member_zts 들어가서 무조건 update만 이루어짐
//				if (CommonUtil.isEmpty(getMemberZts(search))) {
//					dao.insertOneTable(memberInfo);
//				} else {
//					dao.updateOneTable(memberInfo);
//				}
			}
		}

	}

	/**
	 * 
	 * @Method Name : getMemberQuickmenuList
	 * @author : intune
	 * @date : 2016. 9. 28.
	 * @description : 회원 퀵메뉴 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsQuickmenu> getMemberQuickmenuList(MmsMemberSearch search) {
		return (List<MmsQuickmenu>) dao.selectList("mms.member.getMemberQuickmenuList", search);
	}

	/**
	 * RXAgreeUser 처리
	 * 
	 * @Method Name : updateRXAgreeUser
	 * @author : intune
	 * @date : 2016. 11. 4.
	 * @description :
	 *
	 * @param member
	 * @throws Exception
	 */
	public BigDecimal updateRXAgreeUser(BaseMmsMember member) throws Exception {
		// 회원 존재 여부 체크

		BaseMmsMember user = (BaseMmsMember) dao.selectOneTable(member);
		if (user == null) {

			createRxMember(member);

		} else {

			// MMS_MEMBER UPDATE
			dao.updateOneTable(member);

			// MMS_MEMBER_ZTS UPDATE
			BaseMmsMemberZts zts = new BaseMmsMemberZts();
			zts.setMemberNo(member.getMemberNo());
			zts.setMemGradeCd("MEM_GRADE_CD.WELCOME");
			zts.setMembershipYn("N");
			zts.setB2eYn("N");
			zts.setChildrenYn("N");
			zts.setAppPushYn("N");
			zts.setInsId(BaseConstants.MEMBERSHIP_ID);
			zts.setStoreId(BaseConstants.STORE_ID);
			dao.updateOneTable(zts);

			// MMS_MEMBER_BABY INSERT
			if (member.getMmsMemberbabys() != null) {
				for (MmsMemberbaby baby : member.getMmsMemberbabys()) {
					baby.setInsId(BaseConstants.MEMBERSHIP_ID);
					baby.setMemberNo(member.getMemberNo());
					dao.insertOneTable(baby);
				}
			}

			// MMS_MEMBER_BABY INSERT
			if (member.getMmsMembersnss() != null) {
				for (MmsMembersns sns : member.getMmsMembersnss()) {
					sns.setInsId(BaseConstants.MEMBERSHIP_ID);
					sns.setMemberNo(member.getMemberNo());
					dao.insertOneTable(sns);
				}
			}

		}

		return member.getMemberNo();
	}
	/**
	 * 회원가입
	 * 
	 * @Method Name : createRxMember
	 * @author : intune
	 * @date : 2016. 10. 31.
	 * @description : 통합멤버십 시스템으로 부터 유입된 회원 가입 정보를 create한다.
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createRxMember(BaseMmsMember member) throws Exception {

		//동일 ID를 null처리 : 개발환경에서 통합멤버십과 db sync가 맞지 않아 중복 아이디가 생성되는 케이스 방지.
		dao.update("mms.member.updateMemberIdToNull", member);

		member.setInsId(BaseConstants.MEMBERSHIP_ID);
		dao.insertOneTable(member);

		if (member.getMmsMemberbabys() != null) {
			for (MmsMemberbaby baby : member.getMmsMemberbabys()) {
				baby.setInsId(BaseConstants.MEMBERSHIP_ID);
				baby.setMemberNo(member.getMemberNo());
				dao.insertOneTable(baby);
			}
		}

		if (member.getMmsMembersnss() != null) {
			for (MmsMembersns sns : member.getMmsMembersnss()) {
				sns.setInsId(BaseConstants.MEMBERSHIP_ID);
				sns.setMemberNo(member.getMemberNo());
				dao.insertOneTable(sns);
			}
		}

		// mms_member_zts생성
		MmsMemberZts zts = new MmsMemberZts();
		zts.setMemberNo(member.getMemberNo());
		zts.setMemGradeCd("MEM_GRADE_CD.WELCOME");
		zts.setMembershipYn("N");
		zts.setB2eYn("N");
		zts.setChildrenYn("N");
		zts.setAppPushYn("N");
		zts.setInsId(BaseConstants.MEMBERSHIP_ID);
		zts.setStoreId(BaseConstants.STORE_ID);
		dao.insertOneTable(zts);

		try {
			// 준회원 번호가 존재하면 pos에 알려준다.
			if (CommonUtil.isNotEmpty(member.getOffshopMemberNo())) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("memberNo", member.getMemberNo().toString());
				param.put("custid", member.getOffshopMemberNo());
				param.put("type", "CREATE");
				pos.update("external.pos.updateChangeOnlineMember", param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			// 회원가입 오프라인 쿠폰 발급
			SpsCouponissue coupon = new SpsCouponissue();
			coupon.setOffshopType(Config.getString("offshop.coupontype.newmember"));
			coupon.setOffshopMemberNo(member.getMemberNo().toString());
			coupon.setOffshopSalePrice("0");
			String validDt = DateUtil.getAddDate(DateUtil.FORMAT_1, DateUtil.getCurrentDate(DateUtil.FORMAT_1),
					new BigDecimal(7));
			coupon.setOffshopValidDt(validDt);
			couponService.createOffshopCoupon(coupon);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return member.getMemberNo();

	}

	public BigDecimal updateRxMember(BaseMmsMember member) throws Exception {

		member.setInsId(BaseConstants.MEMBERSHIP_ID);
		dao.updateOneTable(member);

		dao.delete("mms.member.deleteMemberBaby", member);
		if (member.getMmsMemberbabys() != null) {
			for (MmsMemberbaby baby : member.getMmsMemberbabys()) {
				baby.setInsId(BaseConstants.MEMBERSHIP_ID);
				baby.setMemberNo(member.getMemberNo());
				dao.insertOneTable(baby);
			}
		}
		dao.delete("mms.member.deleteMemberSns", member);
		if (member.getMmsMembersnss() != null) {
			for (MmsMembersns sns : member.getMmsMembersnss()) {
				sns.setInsId(BaseConstants.MEMBERSHIP_ID);
				sns.setMemberNo(member.getMemberNo());
				dao.insertOneTable(sns);
			}
		}

		return member.getMemberNo();
	}

	public void deleteRxMember( String memberNo) throws Exception {
		BaseMmsMember member = new BaseMmsMember();

		// 테이블 삭제
//		MMS_MEMBERBABY
//		MMS_MEMBERMENU
//		MMS_MEMBERSNS
//		MMS_ADDRESS
//		MMS_CARROT
//		MMS_INTEREST
//		MMS_INTERESTOFFSHOP
//		MMS_WISHLIST
//		MMS_LOGINHISTORY
//		OMS_CART
		member.setMemberNo(new BigDecimal(memberNo));
		BaseMmsMember memberInfo = (BaseMmsMember) dao.selectOneTable(member);
		if (memberInfo == null) {
			return;
		}
		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(new BigDecimal(memberNo));
		search.setMemberId(memberInfo.getMemberId());
		search.setStoreId(SessionUtil.getStoreId());
		dao.delete("mms.member.memberWidhdraw", search);

		// 회원번호 남기고 데이터 삭제
		// MMS_MEMBER
		// MMS_MEMBER_ZTS

		member.setMemberStateCd("MEMBER_STATE_CD.WITHDRAW");
		member.setAgreeWithdrawDt(BaseConstants.SYSDATE);

		member.setCustomerNo("");
		member.setMemberId("");
		member.setMemberName("");
		member.setRegChannelUrl("");
		member.setCardIssueNo("");
		member.setCardIssueDt("");
		member.setRegDt("");
		member.setForeignerYn("");
		member.setBirthday("");
		member.setLunarYn("");//양력
		member.setGenderCd("");
		member.setGenderCd("");
		member.setDiscrhash("");
		member.setCiversion("");
		member.setCiscrhash("");
		member.setCertDivCd("");
		member.setPremiumYn("");
		member.setPremiumRegDt("");
		member.setEmployeeYn("");
		member.setPhone2("");
		member.setPhone1("");
		member.setEmail("");
		member.setEmailYn("");
		member.setSmsYn("");
		member.setAppPushYn("");
		member.setSnsId("");
		member.setSnsChannelCd("");
		member.setOffshopId("");
		member.setDeviceTypeCd("");
		//member.setSnsMemberYn("N");//필수
		member.setEmployeeEmail("");
		member.setEmployeeRegDt("");
		member.setRegTypeCd("");
		//member.setAgreeYn("N");//필수
		member.setAgreeDt("");
		member.setOffshopCardNo("");
		//member.setOffshopPointSwitchYn("N");//필수
		member.setSiteMemberId("");
		member.setOffshopMemberNo("");

		dao.updateOneTable(member);


		BaseMmsMemberZts zts = new BaseMmsMemberZts();
		zts.setMemberNo(new BigDecimal(memberNo));
//		zts.setStoreId(SessionUtil.getStoreId());

		zts.setAccountAuthDt("");
		zts.setAccountHolderName("");
		zts.setAccountNo("");
		zts.setAddressNo(BaseConstants.BIGDECIMAL_NULL);
		//zts.setAppPushYn("");
		zts.setB2eRegDt("");
		//zts.setB2eYn("");//필수
		zts.setBabyBirthday("");
		//zts.setBabyGenderCd("");//필수
		zts.setBabyYnCd("");
		zts.setBankCd("");
		zts.setBankName("");
		zts.setBillingKey("");
		zts.setChildrenAccountNo("");
		zts.setChildrenDealId("");
		zts.setChildrenRegDt("");
		//zts.setChildrenYn("");//필수
		zts.setDeviceTypeCd("");
		zts.setJobCd("");
		zts.setMarriageYn("");
		zts.setMembershipRegDt("");
		//zts.setMembershipYn("");//필수
		//zts.setMemGradeCd("");//필수
		zts.setPaymentBusinessCd("");
		zts.setPaymentMethodCd("");
		zts.setRegNo("");
		zts.setRegularPaymentBusinessCd("");
		zts.setRegularPaymentBusinessNm("");
		zts.setSpouseBirthday("");
		zts.setWeddingDay("");
		
		dao.updateOneTable(zts);

		// pos 탈퇴
		try {
			if (CommonUtil.isNotEmpty(member.getOffshopMemberNo())) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("custid", member.getOffshopMemberNo());
				param.put("type", "DELETE");
				pos.update("external.pos.updateChangeOnlineMember", param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public MmsMember getMemberInfoByMemberId(String memberId) {
		return (MmsMember) dao.selectOne("mms.member.getMemberInfoByMemberId", memberId);
	}
	
	/**
	 * @Method Name : getMemberPremiumYn
	 * @author : ian
	 * @date : 2016. 10. 16.
	 * @description : 프리미엄 회원 여부
	 *
	 * @param memberNo
	 * @return
	 */
	public String getMemberPremiumYn(BigDecimal memberNo) {
		return (String) dao.selectOne("mms.member.getPremiumYn", memberNo);
	}
	
	/**
	 * @Method Name : createExpireCarrotNewTx
	 * @author : ian
	 * @date : 2016. 10. 20.
	 * @description : 만료 당근 배치
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void createExpireCarrotNewTx() throws Exception {
		String updId = BaseConstants.DEFAULT_BATCH_USER_ID;
		
		// 만료된 당근을 소유한 모든 회원 조회
		List<MmsCarrot> memberList = (List<MmsCarrot>) dao.selectList("getExpireCarrotMemberList", null);
		
		int totalCnt = memberList.size();
		int successCnt = 0;
		int failCnt = 0;
		
		if(totalCnt > 0) {
			for(MmsCarrot member : memberList) {
				logger.debug("\t ### runExpireCarrot() _ member >> " + member.getMemberNo() + " 회원 당근 만료처리 시작 ###");
				
				// 회원별 만료전 당근 적립내역 조회
				List<MmsCarrot> targetList = (List<MmsCarrot>) dao.selectList("mms.member.getExpireCarrotMember", member);
				
//				for(MmsCarrot target : targetList) {
//					
//					target.setExpireYn("Y");
//					target.setUpdId(updId);
//					
//					BigDecimal carrot = target.getCarrot();
//					
//					// 회원의 만료전 차감내역 조회 -> 차감내역없으 만료처리
//					List<MmsCarrot> minusList = (List<MmsCarrot>) dao.selectList("mms.member.getMinusCarrot", member);
//					
//					// 차감 처리후 만료처리 (update or insert)
//					if(minusList.size() > 0) {
//						for(MmsCarrot minus : minusList) {
//							/*****************************
//							 * Case
//							 * 1. 적립 100	 <	 차감 -50		적립 : 만료당근 U -50 / 만료처리 'N' // 차감 : 만료당근 U 50 / 만료처리 'Y' 		// for 진행
//							 * 2. 적립 100	 =	 차감 -100		적립 : 만료당근 U -100 / 만료처리 'Y' // 차감 : 만료당근 U 100 / 만료처리 'Y'		// for break
//							 * 3. 적립 100	 >	 차감 -150		적립 : 만료당근 U -100 / 만료처리 'Y' // 차감 : 만료당근 U 100 / 만료처리 'N'		// for break
//							 * ****************************/
//							
//							BigDecimal temp = BigDecimal.ZERO;	// 적립당근, 차감당근 연산 값 임시 저장
//							
//							// 차감건에 차감잔액이 남은 경우
//							if(minus.getExpireCarrot().compareTo(BigDecimal.ZERO) > 0 ) {
//								temp = carrot.add(minus.getCarrot().add(minus.getExpireCarrot()));
//							} else {
//								temp = carrot.add(minus.getCarrot());
//							}
//							
//							// 1. 적립 당근 > 차감 당근
//							if(temp.compareTo(BigDecimal.ZERO) == 1) {
//								// 차감내역 만료 업데이트
//								minus.setExpireCarrot(minus.getCarrot());
//								minus.setExpireYn("Y");
//								minus.setUpdId(updId);
//								dao.updateOneTable(minus);
//								
//								// 차감 처리후 남은 적립당근 잔액 저장
//								carrot = temp;
//							} 
//							else {
//								// 2. 적립 당근 = 차감 당근
//								if(temp.compareTo(BigDecimal.ZERO) == 0) {
//									minus.setExpireCarrot(minus.getCarrot());
//									minus.setExpireYn("Y");
//								} 
//								// 3. 적립 당근 < 차감 당근
//								else if(temp.compareTo(BigDecimal.ZERO) == -1) {
//									// 남은 차감액 저장
//									minus.setExpireCarrot(carrot);
//									minus.setExpireYn("N");
//								}
//								
//								// 0 or -당근
//								carrot = temp;
//								
//								minus.setUpdId(updId);
//								dao.updateOneTable(minus);
//								
//								break;
//							}
//						}
//						
//						// 적립건의 차감처리한 당근이 0 또는 이하면 update 
//						if(carrot.compareTo(BigDecimal.ZERO) <= 0) {
//							target.setExpireCarrot(BigDecimal.ZERO);
//							dao.updateOneTable(target);
//						}
//						// 적립건의 차감처리한 당근이 0 이상이면 update&insert
//						else {
//							// 만료 당근 원건에 만료당근 설정
//							target.setExpireCarrot(carrot);
//							dao.updateOneTable(target);
//							
//							// 차감 처리후 잔액으로 만료 레코드 생성
//							MmsCarrot expire = new MmsCarrot();
//							expire.setExpireYn("Y");
//							expire.setCarrotTypeCd(BaseConstants.CARROT_TYPE_CD_EXPIRE);
//							expire.setCarrot(carrot.negate());
//							expire.setUpdId(updId);
//							expire.setNote("유효기간만료");
//							dao.insertOneTable(expire);
//						}
//						
//					}
//					// 차감처리 없이 만료처리 (update&insert)
//					else {
//						// 만료 당근 원건에 만료당근 설정
//						target.setExpireCarrot(carrot);
//						dao.updateOneTable(target);
//						
//						// 만료 당근 원건의 당근으로 만료 레코드 생성
//						MmsCarrot expire = new MmsCarrot();
//						expire.setExpireYn("Y");
//						expire.setCarrotTypeCd(BaseConstants.CARROT_TYPE_CD_EXPIRE);
//						expire.setCarrot(carrot.negate());
//						expire.setUpdId(updId);
//						expire.setNote("유효기간만료");
//						dao.insertOneTable(expire);
//					}
//				}
				successCnt++;
			}	// 회원별 당근 만료처리 END
		} else {
			logger.debug("\t ### runExpireCarrot() : 만료일이 지난 당근을 갖은 회원이 없습니다. ### ");
		}
		
		logger.debug("\t ### runExpireCarrot() : result ################################################ \n");
		logger.debug("\t ### totalCount : " + totalCnt +"\n" );
		logger.debug("\t ### successCnt : " + successCnt +"\n" );
		logger.debug("\t ### failCnt : " + failCnt +"\n");
		logger.debug("\t ### ########################## ################################################ ");
		
	}
	
	/**
	 * @Method Name : getExpireCarrotSendMemberList
	 * @author : ian
	 * @date : 2016. 10. 27.
	 * @description : 다음달 만료될 당근 소유한 회원 내역
	 *
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MmsMember> getExpireCarrotSendMemberList() throws Exception {
		logger.debug("\t ### getExpireCarrotSendMemberList() >> 다음달 당근 만료내역 추출 시작 ###");
		
		List<MmsMember> result = new ArrayList<MmsMember>();
		result = (List<MmsMember>) dao.selectList("mms.member.getExpireCarrotSendMemberList", null);
		
		if(result.size() < 1) {
			result = null;
			logger.debug("\t ### getExpireCarrotSendMemberList() >> 다음달 만료될 당근을 소유한 회원이 없습니다. ###");
		}
		
		logger.debug("\t ### getExpireCarrotSendMemberList() >> 다음달 당근 만료내역 추출 종료 ###");
		
		return result;
	}


	/**
	 * 회원 등급 조정 배치
	 * 
	 * @Method Name : updateMemberGradeAll
	 * @author : eddie
	 * @date : 2016. 10. 27.
	 * @description :
	 *
	 */
	public void updateMemberGradeAll() {
		dao.update("mms.member.updateMemberGradeAll", null);
	}

	public void saveBalanceCarrot(MmsCarrot useCarrot) throws Exception {
		// 사용가능한 당근 목록 조회
		List<MmsCarrot> usableCarrots = (List<MmsCarrot>) dao.selectList("mms.member.getBalanceCarrots", useCarrot);

		List<MmsCarrot> updateCarrots = new ArrayList<MmsCarrot>();
		// 소진 목록 대상 생성
		BigDecimal useCarrotAmt = useCarrot.getCarrot();
		
		// 차감금액 양수화해서 비교
		useCarrotAmt = useCarrotAmt.multiply(new BigDecimal(-1));
		
		for (MmsCarrot usableCarrot : usableCarrots) {
			MmsCarrot updateCarrot = new MmsCarrot();
			updateCarrot.setCarrotNo(usableCarrot.getCarrotNo());
			updateCarrot.setMemberNo(useCarrot.getMemberNo());

			if (usableCarrot.getBalanceCarrot().compareTo(useCarrotAmt) == 1) {//  부분 차감
				updateCarrot.setBalanceCarrot(usableCarrot.getBalanceCarrot().subtract(useCarrotAmt));
				updateCarrots.add(updateCarrot);
				break;
			} else if (usableCarrot.getBalanceCarrot().compareTo(useCarrotAmt) == 0) {// 전액 차감 : 차감액 더 없음
				updateCarrot.setBalanceCarrot(new BigDecimal(0));
				updateCarrots.add(updateCarrot);
				break;
			} else if (usableCarrot.getBalanceCarrot().compareTo(useCarrotAmt) == -1) {//전액 차감 : 차감액 더 있음
				updateCarrot.setBalanceCarrot(new BigDecimal(0));
				useCarrotAmt = useCarrotAmt.subtract(usableCarrot.getBalanceCarrot());// 차감 더할 당근
				updateCarrots.add(updateCarrot);
			}
		}

		// 당근 잔액 수정
		for (MmsCarrot usableCarrot : updateCarrots) {
			dao.updateOneTable(usableCarrot);
		}
	}

	/**
	 * 
	 * @Method Name : updateMemberChildrenCard
	 * @author : allen
	 * @date : 2016. 10. 28.
	 * @description : 회원 다자녀카드 정보 업데이트
	 *
	 * @param mmsMemberZts
	 */
	public void updateMemberChildrenCard(MmsMemberZts mmsMemberZts) {
		dao.update("mms.member.updateMemberChildrenCard", mmsMemberZts);

	}

	/**
	 * 탈퇴 가능여부 체크
	 * 
	 * @Method Name : getDisagreeAvailable
	 * @author : eddie
	 * @date : 2016. 11. 4.
	 * @description : 1 . 주문의 배송상태체크 2. 진행중인 클레임 체크
	 * @param member
	 * @return true 탈퇴가능, false 탈퇴불가능
	 */
	public Map<String, String> getDisagreeAvailable(String memberNo) {

		String orderInfo = (String) dao.selectOne("mms.member.getDisagreeAvailable", memberNo);

		int depositInfo = (Integer) dao.selectOne("mms.member.getMyDepositSum", memberNo);

		String csInfo = (String) dao.selectOne("mms.member.getCsExist", memberNo);

		int failCnt = 0;

		boolean isOrderExist = false;
		if (CommonUtil.isNotEmpty(orderInfo)) {
			isOrderExist = true;
			failCnt++;
		}
		boolean isDepositExist = false;
		if (depositInfo > 0) {
			isDepositExist = true;
			failCnt++;
		}
		boolean isCsExist = false;
		if (CommonUtil.isNotEmpty(csInfo)) {
			isCsExist = true;
			failCnt++;
		}
		String resultMsg = "";
		if (failCnt > 0) {
			resultMsg = "다음과 같은 사유로 약관 철회가 불가합니다.";
		} else {
			resultMsg = "약관 동의 철회 가능한 상태 입니다.";
		}
		if (failCnt >= 2) {
			int no = 1;
			if (isOrderExist) {
				resultMsg += "<br/>" + no++ + ". 배송, 반품 진행중인 주문이 있어, 주문처리가 완료된 후 탈퇴가 가능합니다.";
			}
			if (isDepositExist) {
				resultMsg += "<br/>" + no++ + ". 환불 가능한 예치금이 있어, 예치금 환불 완료 후 탈퇴가 가능합니다.";
			}
			if (isCsExist) {
				resultMsg += "<br/>" + no++ + ". 문의해 주신 상담 처리중이며, 1:1문의 및 상담처리가 완료 후 탈퇴가 가능합니다.";
			}

		} else if (failCnt == 1) {
			if (isOrderExist) {
				resultMsg += "<br/>제로투세븐닷컴에 배송, 반품 진행중인 주문이 있어,주문처리가 완료된 후 탈퇴가 가능합니다.<br/>주문내역 확인: 마이쇼핑>주문내역에서 확인";
			}
			if (isDepositExist) {
				resultMsg += "<br/>제로투세븐닷컴에 환불 가능한 예치금이 있어, 예치금 환불 완료 후 탈퇴가 가능합니다. <br/>예치금 환불신청:  마이쇼핑>MY혜택관리>예치금 / 고객센터(1588-8744) 문의.";
			}
			if (isCsExist) {
				resultMsg += "<br/>콜센터 상담 처리중인 경우 제로투세븐닷컴에 문의해 주신 상담 처리중이며, 1:1문의 및 상담처리가 완료 후 탈퇴가 가능합니다.<br/>상담내역 확인: 고객센터(1588-8744)로 문의.";
			}
		}
		Map<String, String> result = new HashMap<String, String>();

		result.put("available", failCnt > 0 ? "N" : "Y");
		result.put("msg", resultMsg);
		return result;
	}

	public BaseMmsMember getMmsMemberForImRx(BaseMmsMember search) throws Exception {
		
		BaseMmsMember member =  (BaseMmsMember) dao.selectOneTable(search);

		if (member == null) {
			throw new ServiceException("mms.imrx.E0017020");
		}

		List<MmsMemberbaby> babys = (List<MmsMemberbaby>)dao.selectList("mms.member.getMemberbabys", search);
		List<MmsMembersns> snss = (List<MmsMembersns>)dao.selectList("mms.member.getMembersnss", search);
		
		member.setMmsMemberbabys(babys);
		member.setMmsMembersnss(snss);
		 
		return member;
	}
}
