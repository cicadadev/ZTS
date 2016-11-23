package gcp.frontpc.controller.rest.mms;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsMessage;
import gcp.ccs.model.CcsOffshop;
import gcp.common.util.FoSessionUtil;
import gcp.external.service.MembershipService;
import gcp.external.service.OffshopCouponService;
import gcp.external.service.TmsService;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsMembermenu;
import gcp.mms.model.MmsQuickmenu;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.MemberService;
import gcp.mms.service.MypageService;
import gcp.mms.service.StyleService;
import gcp.oms.model.OmsPayment;
import gcp.oms.service.DeliveryService;
import gcp.oms.service.OrderService;
import gcp.oms.service.PaymentService;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsReviewrating;
import gcp.pms.service.ProductReviewService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/mms/mypage")
public class MypageController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MypageService mypageService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ProductReviewService productReviewService;

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private StyleService styleService;

	@Autowired
	private OffshopCouponService offshopCouponService;

	@Autowired
	private TmsService tmsService;

	private final Log logger = LogFactory.getLog(getClass());
	// /**
	// *
	// * @Method Name : saveWishList
	// * @author : dennis
	// * @date : 2016. 7. 5.
	// * @description : 위쉬리스트 저장
	// *
	// * @param mmsMember
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value = "/wish/save" ,method = RequestMethod.POST)
	// public Map<String, String> saveWishList(@RequestBody MmsMemberZts mmsMemberZts, HttpServletRequest request) throws Exception {
	//
	// Map<String, String> result = new HashMap<String, String>();
	// mmsMemberZts.setStoreId(SessionUtil.getStoreId());
	// BoLoginInfo loginInfo = (BoLoginInfo) SessionUtil.getLoginInfo(request);
	// BigDecimal memberNo = loginInfo.getMemberNo();
	// mmsMemberZts.setMemberNo(memberNo);
	//
	// result = memberService.saveWishList(mmsMemberZts);
	//
	// return result;
	// }

	/**
	 * @Method Name : deleteInquiryQna
	 * @author : ian
	 * @date : 2016. 9. 1.
	 * @description : 내가 문의한 글 삭제 ( 1:1 문의 / 상품 Q&A)
	 *
	 * @param search
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiry/delete" ,method = { RequestMethod.POST })
	public void deleteInquiryQna(@RequestBody MmsMemberSearch search, HttpServletRequest request) throws Exception {
		String storeId = SessionUtil.getStoreId();
		if (BaseConstants.PAGE_TYPE_PRODUCT.equals(search.getPageType())) {
			PmsProductqna productQna = new PmsProductqna();
			productQna.setStoreId(storeId);
			productQna.setProductQnaNo(search.getInquiryNo());

			mypageService.deleteProductQna(productQna);
		} else if (BaseConstants.PAGE_TYPE_MYQA.equals(search.getPageType())) {
			CcsInquiry myQa = new CcsInquiry();
			myQa.setStoreId(storeId);
			myQa.setInquiryNo(search.getInquiryNo());

			mypageService.deleteMyQa(myQa);
		}
	}

	/**
	 * 
	 * @Method Name : deliveryAddressListAjax
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 회원 배송지 리스트 조회
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deliveryAddress/list/ajax" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public ModelMap deliveryAddressListAjax(@RequestBody MmsMemberSearch search, HttpServletRequest request) throws Exception {
		List<MmsAddress> addressList = memberService.getMemberAddressList(search);
		ModelMap map = new ModelMap();
		map.addAttribute("addressList", addressList);
		return map;
	}

	/**
	 * 
	 * @Method Name : delDeliveryAddressAjax
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 회원 배송지 삭제
	 *
	 * @param address
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/deliveryAddress/delete" ,method = RequestMethod.POST)
	public void delDeliveryAddressAjax(@RequestBody MmsMemberSearch search, HttpServletRequest request) throws Exception {
		MmsAddress address = new MmsAddress();
		address.setAddressNo(new BigDecimal(search.getAddressNo()));
		address.setMemberNo(SessionUtil.getMemberNo());
		memberService.deleteMemberAddress(address);
	}

	/**
	 * 
	 * @Method Name : saveDelDeliveryAddress
	 * @author : allen
	 * @date : 2016. 9. 1.
	 * @description : 배송지 저장
	 *
	 * @param address
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/deliveryAddress/save" ,method = RequestMethod.POST)
	public void saveDelDeliveryAddress(@ModelAttribute MmsAddress address, HttpServletRequest request) throws Exception {
		memberService.saveMemberAddress(address);
	}

	/**
	 * 
	 * @Method Name : updateBasicAddress
	 * @author : allen
	 * @date : 2016. 8. 31.
	 * @description : 기본 배송지 설정
	 *
	 * @param address
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/deliveryAddress/updateBasicAddress" ,method = RequestMethod.POST)
	public void updateBasicAddress(@RequestBody MmsMemberSearch search, HttpServletRequest request) throws Exception {
		MmsMemberZts mmsMemberZts = new MmsMemberZts();
		mmsMemberZts.setMemberNo(SessionUtil.getMemberNo());
		mmsMemberZts.setUpdId(SessionUtil.getLoginId());
		mmsMemberZts.setAddressNo(new BigDecimal(search.getAddressNo()));
		memberService.updateBasicAddress(mmsMemberZts);
	}

	/**
	 * 
	 * @Method Name : getInterestOffshop
	 * @author : stella
	 * @date : 2016. 9. 8.
	 * @description : 관심매장 조회
	 *
	 * @param address
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/offshop/list" ,method = RequestMethod.POST)
	public ModelMap getInterestOffshop(@RequestBody MmsMemberSearch search, HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());

		if ("Y".equals(search.getAllOffshopYn())) {
			map.addAttribute("offshopList", memberService.getInterestOffshopList(search));
		} else {
			search.setTopOffshopYn("Y");
			List<MmsInterestoffshop> topOffshop = memberService.getInterestOffshopList(search);
			map.addAttribute("topOffshop", topOffshop);

			search.setTopOffshopYn("N");
			List<MmsInterestoffshop> offshopList = memberService.getInterestOffshopList(search);
			map.addAttribute("offshopList", offshopList);

			map.addAttribute("totalCnt", offshopList.size() + topOffshop.size());
		}

		return map;
	}

	/**
	 * @Method Name : refundAccountReg
	 * @author : ian
	 * @date : 2016. 9. 10.
	 * @description : 환불 계좌 등록
	 *
	 * @param omsPayment
	 * @throws Exception
	 */
	@RequestMapping(value = "/refundAccount/reg" ,method = { RequestMethod.POST })
	public String refundAccountReg(@RequestBody OmsPayment omsPayment) throws Exception {
		omsPayment.setMemberNo(SessionUtil.getMemberNo());
		return mypageService.updateRegRefundAccount(omsPayment);
	}

	/**
	 * @Method Name : insert
	 * @author : ian
	 * @date : 2016. 9. 9.
	 * @description : 예치금 환불 신청
	 *
	 * @param omsPayment
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/payment" ,method = RequestMethod.POST)
	public String insert(@RequestBody OmsPayment omsPayment) throws Exception {

		omsPayment.setMemberNo(SessionUtil.getMemberNo());
		// Map<String, String> map = (Map<String, String>) mypageService.insertDepositRefundData(omsPayment);

		// if(BaseConstants.DEPOSIT_REFUND_REQ_RESULT_SUCCESS.equals(map.get("code")) ) {

		omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.REFUND_READY");
		omsPayment.setPaymentMethodCd("PAYMENT_METHOD_CD.CASH");
		omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.REFUND");
		omsPayment.setRefundReasonCd("REFUND_REASON_CD.REFUNDDEPOSIT");
		// omsPayment.setDepositNo(new BigDecimal(map.get("depositNo")));
		// omsPayment.setPaymentAmt(new BigDecimal(map.get("balanceAmt")));

		int result = paymentService.insertRefundByDeposit(omsPayment);
		if (result == 1) {
			return BaseConstants.RESULT_FLAG_SUCCESS;
		} else {
			return BaseConstants.RESULT_FLAG_FAIL;
		}
		// } else {
		// return (String) map.get("msg");
		// }
	}

	@RequestMapping(value = "/myMenu/save" ,method = { RequestMethod.POST })
	public void updateInterestOffshop(@RequestBody List<MmsMembermenu> memberMenuList, HttpServletRequest request) throws Exception {

		// 삭제후 저장
		MmsMembermenu menu = new MmsMembermenu();
		menu.setMemberNo(SessionUtil.getMemberNo());
		memberService.deleteMemberMenu(menu);
		memberService.saveMemberMenu(memberMenuList);

		MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		List<MmsQuickmenu> memberMenus = new ArrayList<MmsQuickmenu>();
		memberMenus = memberService.getMemberQuickmenuList(mmsMemberSearch);

		FoLoginInfo loginInfo = new FoLoginInfo();
		loginInfo = (FoLoginInfo) SessionUtil.getLoginInfo();
		loginInfo.setMemberMenus(memberMenus);
		SessionUtil.setLoginInfo(request, loginInfo);
	}

	/**
	 * @Method Name : deleteWishList
	 * @author : stella
	 * @date : 2016. 9. 9.
	 * @description : 쇼핑찜 목록 삭제
	 *
	 * @param MmsMemberSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wishlist/delete" ,method = RequestMethod.POST)
	public void deleteWishList(@RequestBody MmsWishlist wishlist, HttpServletRequest request) throws Exception {
		wishlist.setStoreId(SessionUtil.getStoreId());
		wishlist.setMemberNo(SessionUtil.getMemberNo());

		mypageService.deleteWishList(wishlist);
	}

	/**
	 * 
	 * @Method Name : reviewDelete
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 삭제
	 *
	 * @param review
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/review/delete" ,method = RequestMethod.POST)
	public Map<String, String> reviewDelete(@RequestBody PmsReview review, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		try {
			productReviewService.updatePmsReview(review);
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} catch (Exception e) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : reviewSave
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 등록 & 수정
	 *
	 * @param review
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/review/save" ,method = RequestMethod.POST)
	public Map<String, String> reviewSave(@RequestBody PmsReview review, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		if (CommonUtil.isEmpty(review.getDetail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "상품평을 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(review.getProductId())) {
			result.put(BaseConstants.RESULT_MESSAGE, "상품을 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(review.getPmsReviewratings())) {
			result.put(BaseConstants.RESULT_MESSAGE, "별점이 없습니다.");
		} else if (review.getDetail().length() < 5 || review.getDetail().length() > 2000) {
			result.put(BaseConstants.RESULT_MESSAGE, "상품평을 최소 5자  / 최대 2000자까지 입력가능합니다.");
		} else {
			if (CommonUtil.isEmpty(review.getImg1())) {
				if (CommonUtil.isNotEmpty(review.getImg2())) {
					review.setImg1(review.getImg2());
					if (CommonUtil.isNotEmpty(review.getImg3())) {
						review.setImg2(review.getImg3());
					}
				} else {
					if (CommonUtil.isNotEmpty(review.getImg3())) {
						review.setImg1(review.getImg3());
					}
				}
			} else if (CommonUtil.isEmpty(review.getImg2())) {
				if (CommonUtil.isNotEmpty(review.getImg3())) {
					review.setImg2(review.getImg3());
				}
			}
			BigDecimal ratingTotal = new BigDecimal("0");
			BigDecimal cnt = new BigDecimal("0");
			for (PmsReviewrating list : review.getPmsReviewratings()) {
				ratingTotal = ratingTotal.add(list.getRating());
				cnt = cnt.add(new BigDecimal("1"));
			}

			try {
				review.setRating(ratingTotal.divide(cnt, 1, BigDecimal.ROUND_UP));
			} catch (Exception e) {
				result.put(BaseConstants.RESULT_MESSAGE, "별점을 입력하지 않으셨습니다.");
				return result;
			}

			if (CommonUtil.isNotEmpty(review.getDetail().split("\n")[0])) {
				review.setTitle(review.getDetail().split("\n")[0]);
			} else {
				review.setTitle(review.getDetail());
			}
			review.setStoreId(SessionUtil.getStoreId());
			review.setDisplayYn("Y");
			review.setMemberNo(SessionUtil.getMemberNo());

			try {
				if (CommonUtil.isEmpty(review.getReviewNo())) {
					// 상품평 중복 여부
					review.setSearchType("EXIST");
					if (productReviewService.getFirstReview(review)) {
						result.put(BaseConstants.RESULT_MESSAGE, "이미 해당 주문 상품의 상품평을 작성하셨습니다.");
						result.put("EXIST", "TRUE");
						throw new Exception();
					} else {
						// 리뷰 등록
						BigDecimal reviewNo = productReviewService.insertPmsReview(review);

						// 리뷰 별점 수정
						PmsReviewrating reviewRating = new PmsReviewrating();
						reviewRating.setStoreId(SessionUtil.getStoreId());
						reviewRating.setReviewNo(reviewNo);
						reviewRating.setProductId(review.getProductId());
						productReviewService.deletePmsReviewrating(reviewRating);
						for (PmsReviewrating list : review.getPmsReviewratings()) {
							reviewRating.setRating(list.getRating());
							reviewRating.setRatingId(list.getRatingId());
							productReviewService.insertPmsReviewrating(reviewRating);
						}
					}
				} else {
					productReviewService.updatePmsReview(review);
					PmsReviewrating reviewRating = new PmsReviewrating();
					reviewRating.setStoreId(SessionUtil.getStoreId());
					reviewRating.setReviewNo(review.getReviewNo());
					reviewRating.setProductId(review.getProductId());
					productReviewService.deletePmsReviewrating(reviewRating);
					for (PmsReviewrating list : review.getPmsReviewratings()) {
						reviewRating.setRating(list.getRating());
						reviewRating.setRatingId(list.getRatingId());
						productReviewService.insertPmsReviewrating(reviewRating);
					}
				}
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			} catch (Exception e) {
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			}

		}
		return result;
	}

	/**
	 * 
	 * @Method Name : getOffShopBranch
	 * @author : allen
	 * @date : 2016. 9. 20.
	 * @description : 오프라인 지점 리스트 조회
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/offShopBranch/list/ajax" ,method = RequestMethod.POST)
	public ModelMap getOffShopBranch(HttpServletRequest request, @RequestBody MmsMemberSearch search) throws Exception {
		ModelMap map = new ModelMap();
		search.setMemberNo(SessionUtil.getMemberNo());
		List<CcsOffshop> list = memberService.getMemberOffOrderRegionList(search);
		map.addAttribute("branchList", list);
		return map;
	}

	/**
	 * @Method Name : deleteLatestProduct
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : 최근 본 상품 삭제
	 *
	 * @param value
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/latestProduct/delete" ,method = RequestMethod.POST)
	public void deleteLatestProduct(HttpServletRequest req, HttpServletResponse res, @RequestBody String value) throws Exception {
		CookieUtil cookie = new CookieUtil();
		if ("ALL".equals(value)) {
			cookie.removeCookieAll(req, res, "latestProduct");
		} else {
			cookie.removeCookieData(req, res, "latestProduct", value);
		}
	}

	/**
	 * @Method Name : deleteMobileHistory
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : MO history 삭제
	 *
	 * @param value
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/history/delete" ,method = RequestMethod.POST)
	public void deleteMobileHistory(HttpServletRequest req, HttpServletResponse res, @RequestBody String value) throws Exception {
		CookieUtil cookie = new CookieUtil();
		if ("ALL".equals(value)) {
			cookie.removeCookieAll(req, res, "moHistory");
		} else {
			value = URLEncoder.encode(value, "UTF-8");
			value = value.replaceAll("\\+", "%20"); // 공백 인코딩 처리
			
			cookie.removeCookieData(req, res, "moHistory", value);
		}
	}

	/**
	 * 
	 * @Method Name : deleteStyle
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 회원 스타일 삭제
	 *
	 * @param req
	 * @param res
	 * @param search
	 * @throws Exception
	 */
	@RequestMapping(value = "/style/delete" ,method = RequestMethod.POST)
	public void deleteStyle(HttpServletRequest req, HttpServletResponse res, @RequestBody MmsStyleSearch search) throws Exception {
		styleService.deleteStyle(search);
	}

	/**
	 * @Method Name : getShoppingAlarmCount
	 * @author : ian
	 * @date : 2016. 11. 3.
	 * @description : 쇼핑 알림 보관함 카운트
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shoppingAlarm/count" ,method = RequestMethod.POST)
	public BigDecimal getShoppingAlarmCount(HttpServletRequest request) throws Exception {
		BigDecimal count = BigDecimal.ZERO;
		if (FoSessionUtil.isMobile(request)) {
			MmsMemberSearch search = new MmsMemberSearch();
			search.setStoreId(SessionUtil.getStoreId());
			search.setMemberNo(SessionUtil.getMemberNo());
			search.setSiteKey(Config.getString("tms.site.key"));

			count = tmsService.getMemberShoppingAlarmCount(search);
		}
		return count == null ? BigDecimal.ZERO : count;
	}

	/**
	 * @Method Name : getOffshopCount
	 * @author : ian
	 * @date : 2016. 11. 7.
	 * @description : 오프라인 매장 쿠폰 사용 카운트
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/offshopCoupon/count" ,method = RequestMethod.POST)
	public int getOffshopCount(HttpServletRequest request) throws Exception {
		int result = 0;
		
		MmsMemberSearch search = new MmsMemberSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		Map<String, String> offshopCoupon = offshopCouponService.getOffshopCouponInfo(search);
		if(CommonUtil.isNotEmpty(offshopCoupon.get("USE"))) {
			 result = Integer.parseInt(offshopCoupon.get("USE"));
		}
		
		return result;
	}

}
