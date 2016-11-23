package gcp.admin.controller.rest.mms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.OffshopService;
import gcp.external.model.membership.MemberPoint;
import gcp.external.model.membership.MembershipPointSearchReq;
import gcp.external.service.MembershipService;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsMemberZtsHistory;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPosorder;
import gcp.oms.model.OmsPosorderproduct;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.service.OrderService;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.service.CouponService;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseSearchCondition;

@RestController
@RequestMapping("api/mms/member")
public class MemberController {

	@Autowired
	private MemberService	memberService;

	@Autowired
	private CouponService	couponService;

	@Autowired
	private OrderService	orderService;

	@Autowired
	private OffshopService	offshopService;

	@Autowired
	private MembershipService	membershipService;

	/**
	 * 
	 * @Method Name : memberListPopup
	 * @author : dennis
	 * @date : 2016. 5. 23.
	 * @description : 회원 검색 (popup)
	 *
	 * @param mmsMemberPopupSearch
	 * @return List<MmsMember>
	 */
	@RequestMapping(value = "/popup/list", method = RequestMethod.POST)
	public List<MmsMemberZts> memberListPopup(@RequestBody MmsMemberPopupSearch mmsMemberPopupSearch) {
		mmsMemberPopupSearch.setStoreId(SessionUtil.getStoreId());
		mmsMemberPopupSearch.setLangCd(SessionUtil.getLangCd());
		return memberService.getMemberList(mmsMemberPopupSearch);
	}

	/**
	 * 
	 * @Method Name : getMemberList
	 * @author : allen
	 * @date : 2016. 7. 13.
	 * @description : 회원리스트 조회
	 *
	 * @param mmsMemberPopupSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<MmsMemberZts> getMemberList(@RequestBody MmsMemberPopupSearch mmsMemberPopupSearch) {
		mmsMemberPopupSearch.setStoreId(SessionUtil.getStoreId());
		mmsMemberPopupSearch.setLangCd(SessionUtil.getLangCd());
		return memberService.getMemberList(mmsMemberPopupSearch);
	}

	/**
	 * 
	 * @Method Name : getMemberList
	 * @author : allen
	 * @date : 2016. 7. 13.
	 * @description : 회원 상세 정보 조회
	 *
	 * @param memberNo
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/detail/{memberNo}", method = RequestMethod.GET)
	public MmsMemberZts getMemberList(@PathVariable("memberNo") String memberNo) throws Exception {

		MmsMemberZts mmsMemberZts = memberService.getMemberDetail(new BigDecimal(memberNo));

		//멤버쉽 포인트 호출
		MembershipPointSearchReq req = new MembershipPointSearchReq();
		req.setSrchDv("I");//조회구분‘I’- 통합회원번호, ‘C’- 멤버십카드번호 ‘T’- 휴대전화번호
		req.setSrchDvVlu(memberNo);//회원번호
		MemberPoint point = membershipService.getMemberPoint(req);
		mmsMemberZts.setPoint(String.format("%,d", Integer.parseInt(point.getRmndPint())));
		return mmsMemberZts;
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
	@RequestMapping(value = "/gradeHistory", method = RequestMethod.GET)
	public List<MmsMemberZtsHistory> getMemberGradeHistory(@RequestParam("memberNo") String memberNo) {
		List<MmsMemberZtsHistory> mmsMemberZtsHistoryList = memberService.getMemberGradeHistory(memberNo);
		return mmsMemberZtsHistoryList;
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
	@RequestMapping(value = "/updateMarketingReceipt", method = RequestMethod.POST)
	public void updateMarketingReceipt(@RequestBody MmsMember mmsMember) {
		memberService.updateMarketingReceipt(mmsMember);
	}

	/**
	 * 
	 * @Method Name : getMemberCouponList
	 * @author : allen
	 * @date : 2016. 7. 15.
	 * @description : 회원의 쿠폰 리스트 조회
	 *
	 * @param memberNo
	 * @return
	 */
	@RequestMapping(value = "/getMemberCouponList", method = RequestMethod.GET)
	public List<SpsCouponissue> getMemberCouponList(@RequestParam("memberNo") String memberNo) {
		SpsCouponSearch search = new SpsCouponSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(new BigDecimal(memberNo));
		List<SpsCouponissue> issueCouponList = couponService.getMemberCouponList(search);

		return issueCouponList;
	}

	/**
	 * 
	 * @Method Name : getMemberCouponList
	 * @author : allen
	 * @date : 2016. 7. 15.
	 * @description : 회원의 쿠폰 리스트 조회
	 *
	 * @param memberNo
	 * @return
	 */
	@RequestMapping(value = "/coupon/list", method = RequestMethod.POST)
	public List<SpsCouponissue> getMemberCouponList2(@RequestBody SpsCouponSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		List<SpsCouponissue> issueCouponList = couponService.getMemberCouponList(search);

		return issueCouponList;
	}

	/**
	 * 
	 * @Method Name : getMemberCouponCnt
	 * @author : allen
	 * @date : 2016. 7. 22.
	 * @description : 회원 사용가능한 쿠폰 수 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/coupon/count", method = RequestMethod.GET)
	public SpsCouponissue getMemberCouponCnt(@RequestParam("memberNo") String memberNo) {
		SpsCouponSearch search = new SpsCouponSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(new BigDecimal(memberNo));
		SpsCouponissue couponIssue = couponService.getMemberCouponCnt(search);

		return couponIssue;
	}

	/**
	 * 
	 * @Method Name : checkIssueCoupon
	 * @author : allen
	 * @date : 2016. 7. 18.
	 * @description : 회원 쿠폰 수동 발급
	 *
	 * @param SpsCouponList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkIssueCoupon", method = RequestMethod.POST)
	public String checkIssueCoupon(@RequestBody SpsCoupon spsCoupon) throws Exception {

		SpsCouponissue issue = new SpsCouponissue();
		issue.setCouponId(spsCoupon.getCouponId());
		issue.setMemberNo(new BigDecimal(spsCoupon.getMemberNo()));
		issue.setControllCheckType("BO");
		Map<String, String> map = couponService.issueCoupon(issue, false);
		return map.get("resultMsg");
	}

	/**
	 * 
	 * @Method Name : getMemberOnlineOrderList
	 * @author : allen
	 * @date : 2016. 7. 18.
	 * @description : 회원 온라인 주문 내역 조회
	 *
	 * @param orderSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/onlineOrder/list", method = RequestMethod.POST)
	public List<OmsOrder> getMemberOnlineOrderList(@RequestBody OmsOrderSearch orderSearch) throws Exception {
		orderSearch.setStoreId(SessionUtil.getStoreId());
		List<OmsOrder> orderList = (List<OmsOrder>) orderService.selectList("oms.order.select.boList", orderSearch);

		return orderList;
	}

	/**
	 * 
	 * @Method Name : getMemberOfflineOrderList
	 * @author : allen
	 * @date : 2016. 8. 17.
	 * @description : 회원 오프라인 구매 내역 조회
	 *
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/offlineOrder/list", method = RequestMethod.POST)
	public List<OmsPosorder> getMemberOfflineOrderList(@RequestBody MmsMemberPopupSearch search) throws Exception {
		return memberService.getMemberOffOrderList(search);
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
	 * @throws Exception
	 */
	@RequestMapping(value = "/offlineOrder/productList", method = RequestMethod.POST)
	public List<OmsPosorderproduct> getMemberOffOrderProductList(@RequestBody MmsMemberPopupSearch search) throws Exception {
		return memberService.getMemberOffOrderProductList(search);
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
	 * @throws Exception
	 */
	@RequestMapping(value = "/deposit/list", method = RequestMethod.POST)
	public List<MmsDeposit> getMemberDepositList(@RequestBody MmsMemberPopupSearch search) throws Exception {
		List<MmsDeposit> depositList = memberService.getMemberDepositList(search);
		return depositList;
	}

	/**
	 * 
	 * @Method Name : saveMemberDeposit
	 * @author : allen
	 * @date : 2016. 7. 20.
	 * @description : 회원 예치금 저장
	 *
	 * @param mmsDeposit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deposit/save", method = RequestMethod.POST)
	public String saveMemberDeposit(@RequestBody MmsDeposit mmsDeposit) throws Exception {
		String resultMsg = "";
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = memberService.saveMemberDeposit(mmsDeposit);
		
		if("success".equals(resultMap.get("resultCode"))) {
			resultMsg = resultMap.get("resultMsg"); 
		} else {
			resultMsg = "fail";
		}
		
		return resultMsg; 
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
	 * @throws Exception
	 */
	@RequestMapping(value = "/carrot/list", method = RequestMethod.POST)
	public List<MmsCarrot> getMemberCarrotList(@RequestBody MmsMemberPopupSearch search) throws Exception {
		List<MmsCarrot> carrotList = memberService.getMemberCarrotList(search);
		return carrotList;
	}

	@RequestMapping(value = "/carrot/carrotBalanceAmt", method = RequestMethod.POST)
	public BigDecimal getLatestCarrotBalanceAmt(@RequestBody MmsMemberPopupSearch search) throws Exception {
		BigDecimal balanceAmt = memberService.getLatestCarrotAmt(search);
		return balanceAmt;
	}

	@RequestMapping(value = "/deposit/depositBalanceAmt", method = RequestMethod.POST)
	public BigDecimal getDepositBalanceAmt(@RequestBody MmsMemberPopupSearch search) throws Exception {
		BigDecimal balanceAmt = memberService.getDepositBalanceAmt(search);
		return balanceAmt;
	}

	/**
	 * 
	 * @Method Name : getMemberList
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 회원검색 목록
	 *
	 * @param member
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reviewpermit/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsMember> getMemberList(@RequestBody MmsMemberSearch member) throws Exception {
		member.setStoreId(SessionUtil.getStoreId());
		return memberService.getMemberSearchList(member);
	}
	
	/**
	 * 회원 주소록 리스트
	 * 
	 * @Method Name : addressList
	 * @author : victor
	 * @date : 2016. 7. 28.
	 * @description :
	 *
	 * @param member
	 * @return List<MmsAddress>
	 * @throws Exception
	 */
	@RequestMapping(value = "/address/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsAddress> selectAddressList(@RequestBody MmsMemberSearch member) throws Exception {
		return memberService.getMemberAddressList(member);
	}

	/**
	 * 
	 * 회원 관심 매장 리스트
	 * 
	 * @Method Name : selectAddressList
	 * @author : allen
	 * @date : 2016. 10. 24.
	 * @description : 
	 *
	 * @param member
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/interestOffshop/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsInterestoffshop> getInterestOffshop(@RequestParam("memberNo") String memberNo) throws Exception {
		CcsOffshopSearch search = new CcsOffshopSearch();
		search.setMemberNo(memberNo);
		List<MmsInterestoffshop> list = offshopService.getShopOnOffShopInfoList(search);
		return list;
	}


	/**
	 * 
	 * @Method Name : getMemberListExcel
	 * @author : allen
	 * @date : 2016. 8. 2.
	 * @description : 회원 엑셀 다운로드
	 *
	 * @param mmsMemberPopupSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMemberListExcel(@RequestBody MmsMemberPopupSearch mmsMemberPopupSearch) throws Exception {

		List<MmsMemberZts> list = new ArrayList<MmsMemberZts>();
		mmsMemberPopupSearch.setStoreId(SessionUtil.getStoreId());
		list = memberService.getMemberList(mmsMemberPopupSearch);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("memberNo", list.get(i).getMemberNo().toString());
			map.put("mmsMember", list.get(i).getMmsMember().getMemberName());
			map.put("mmsMember.smsYn", list.get(i).getMmsMember().getSmsYn());
			map.put("mmsMember.emailYn", list.get(i).getMmsMember().getEmailYn());
			map.put("mmsMember.appPushYn", list.get(i).getMmsMember().getAppPushYn());
			map.put("blacklistYn", list.get(i).getBlacklistYn());
			map.put("insDt", list.get(i).getInsDt());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) mmsMemberPopupSearch, dataList);

		return msg;
	}

}
