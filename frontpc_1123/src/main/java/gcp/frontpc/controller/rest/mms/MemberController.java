package gcp.frontpc.controller.rest.mms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsInterest;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.MemberService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.service.CouponService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/mms/member")
public class MemberController {


	@Autowired
	private MemberService	memberService;
	
	@Autowired
	private CouponService	couponService;

	/**
	 * 
	 * @Method Name : saveWishList
	 * @author : dennis
	 * @date : 2016. 7. 5.
	 * @description : 위쉬리스트 저장
	 *
	 * @param mmsMember
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wish/save", method = RequestMethod.POST)
	public Map<String, String> saveWishList(@RequestBody MmsMemberZts mmsMemberZts, HttpServletRequest request) throws Exception {

		Map<String, String> result = new HashMap<String, String>();
		mmsMemberZts.setStoreId(SessionUtil.getStoreId());
		mmsMemberZts.setMemberNo(SessionUtil.getMemberNo());
		mmsMemberZts.setUpdId(SessionUtil.getLoginId());

		result = memberService.saveWishList(mmsMemberZts);

		return result;
	}

	/**
	 * 위시리스트 삭제
	 * 
	 * @Method Name : deleteWishList
	 * @author : intune
	 * @date : 2016. 10. 8.
	 * @description :
	 *
	 * @param list
	 * @throws Exception
	 */
	@RequestMapping(value = "/wish/delete")
	public void deleteWishList(MmsWishlist list) throws Exception {
		if (SessionUtil.isMemberLogin()) {
			list.setMemberNo(SessionUtil.getMemberNo());
			memberService.deleteWishList(list);
		}

	}
	/**
	 * 
	 * @Method Name : saveMmsInterest
	 * @author : roy
	 * @date : 2016. 9. 9.
	 * @description : 브랜드 관심사 변경
	 *
	 * @param MmsInterest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/interest/save", method = RequestMethod.POST)
	public Map<String, String> saveMmsInterest(@ModelAttribute MmsInterest memInterest, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		memInterest.setMemberNo(SessionUtil.getMemberNo());
		if (CommonUtil.isEmpty(memInterest.getInterestNo())) {
			memInterest.setInterestTypeCd("INTEREST_TYPE_CD.BRAND");
			result = memberService.insertMmsInterest(memInterest);
		}else{
			result = memberService.deleteMmsInterest(memInterest);
		}
		/*if (CommonUtil.isEmpty(qna.getInquiryTypeCd())) {
			result.put(BaseConstants.RESULT_MESSAGE, "문의 유형을 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(qna.getTitle())) {
			result.put(BaseConstants.RESULT_MESSAGE, "제목을 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(qna.getDetail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "문의 내용을 입력하지 않으셨습니다.");
		} else if (qna.getSmsYn().equals('Y') && CommonUtil.isEmpty(qna.getPhone())) {
			result.put(BaseConstants.RESULT_MESSAGE, "전화번호를 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(qna.getEmail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "메일을 입력하지 않으셨습니다.");
		} else if ((qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.ORDER")
				|| qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.DELIVERY")
				|| qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.EXCHANGE")) && CommonUtil.isEmpty(qna.getOrderId())) {
			result.put(BaseConstants.RESULT_MESSAGE, "주문 상품을 선택하지 않으셨습니다.");
		} else if (qna.getInquiryTypeCd().equals("INQUIRY_TYPE_CD.BRAND") && CommonUtil.isEmpty(qna.getBrandId())) {
			result.put(BaseConstants.RESULT_MESSAGE, "브랜드를 선택하지 않으셨습니다.");
		}*/
		/*else {
			result = qnaService.saveQna(qna);
		}*/
		return result;
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
	@RequestMapping(value = "/interest/info/save", method = RequestMethod.POST)
	public List<Map<String, String>> saveMmsInterestInfo(@RequestBody List<MmsInterest> mmsInterests, HttpServletRequest request) throws Exception {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		// 멤버십 최초 설정시에만 -> N을 기억하고 있음, y시무시
		boolean isMembership = BaseConstants.YN_N.equals(FoSessionUtil.getMembershipYn()) ? true : false;
		
		memberService.saveMmsInterestInfo(mmsInterests);
		
		// 앱 설치유도 쿠폰 발급
		if(isMembership) {
			String couponId = Config.getString("coupon.benefit.app.install.id");
			List<SpsCouponissue> issues = new ArrayList<SpsCouponissue>();
			SpsCouponissue issue = new SpsCouponissue();
			issue.setStoreId(SessionUtil.getStoreId());
			issue.setMemberNo(SessionUtil.getMemberNo());
			issue.setCouponId(couponId);
			issues.add(issue);
			resultList = couponService.createFoissueCoupon(issues);
		}
		
		return resultList;
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
	@RequestMapping(value = "/interest/check")
	public ModelMap getExistInterest(HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		
		MmsMemberSearch memberSearch = new MmsMemberSearch();
		memberSearch.setMemberNo(SessionUtil.getMemberNo());
		
		String exist = memberService.getExistMemberInterest(memberSearch);
		if (CommonUtil.isNotEmpty(exist)) {
			map.addAttribute("exist", "Y");
		} else {
			map.addAttribute("exist", "N");
		}
		
		return map;
	}
}
