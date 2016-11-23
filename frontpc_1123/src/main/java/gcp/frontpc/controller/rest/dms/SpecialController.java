package gcp.frontpc.controller.rest.dms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsChildrenSearch;
import gcp.mms.service.ChildrenService;
import gcp.mms.service.MemberService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("specialRestController")
@RequestMapping("api/dms/special")
public class SpecialController {
	
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MemberService memberService;

	@Autowired
	private ChildrenService	childrenService;

	@ResponseBody
	@RequestMapping(value = "/check/premiumMember", method = RequestMethod.POST)
	public String checkPremiumMember(HttpServletRequest request, HttpServletResponse response) {
		String resultMsg = "N";
		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
		if (loginInfo != null) {
			 resultMsg = memberService.getMemberPremiumYn(loginInfo.getMemberNo());
		}
		
		return resultMsg;
	}

	/**
	 * 
	 * @Method Name : cardAuth
	 * @author : roy
	 * @date : 2016. 10. 14.
	 * @description : 다자녀 카드 인증
	 *
	 * @param request
	 * @param search
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cardAuth", method = RequestMethod.POST)
	public Map<String, String> cardAuth(@RequestBody MmsChildrenSearch search, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String resultMsg = "";
		try {

			MmsChildrencard childrencard = childrenService.childrenCardAuth(search);
			
			if (childrencard != null) {
				// 카드 존재시 MMS_CHILDRENCARD 회원번호 업데이트 
				MmsChildrencard mmsChildrencard = new MmsChildrencard();
				mmsChildrencard.setRegYn(BaseConstants.YN_Y);
				mmsChildrencard.setAccountNo(search.getAccountNo());
				mmsChildrencard.setName(search.getMemberName());
				mmsChildrencard.setChildrencardTypeCd(search.getChildrencardTypeCd());
				mmsChildrencard.setMemberNo(SessionUtil.getMemberNo());
				mmsChildrencard.setUpdId(SessionUtil.getLoginId());
				childrenService.updateChildrencardInfo(mmsChildrencard);

				// MMS_MEMBER_ZTS 다자녀카드 정보 및 딜 정보 업데이트
				MmsMemberZts mmsMemberZts = new MmsMemberZts();
				mmsMemberZts.setStoreId(SessionUtil.getStoreId());
				mmsMemberZts.setMemberNo(SessionUtil.getMemberNo());
				mmsMemberZts.setChildrenAccountNo(search.getAccountNo());
				mmsMemberZts.setUpdId(SessionUtil.getLoginId());
				mmsMemberZts.setChildrenDealId(Config.getString("children.deal.id." + search.getChildrencardTypeCd()));
				memberService.updateMemberChildrenCard(mmsMemberZts);

				FoLoginInfo login = (FoLoginInfo) FoSessionUtil.getLoginInfo();

				login.setChildrenDealId(Config.getString("children.deal.id." + search.getChildrencardTypeCd()));
				login.setChildrenYn(BaseConstants.YN_Y);

			} else {
				resultMsg = "미등록 카드입니다.";
				throw new Exception();
			}
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			result.put(BaseConstants.RESULT_MESSAGE, resultMsg);
		}

		return result;
	}

}

