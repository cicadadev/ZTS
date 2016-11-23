package gcp.mms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsQuickmenu;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsOrder;

@Service
public class LoginService extends BaseService {

	/**
	 * 프론트 로그인(일반회원)
	 * 
	 * @Method Name : doFrontLogin
	 * @author : eddie
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param loginId
	 * @return
	 */
	public FoLoginInfo doFrontLogin(String memberNo) {


		FoLoginInfo loginInfo = new FoLoginInfo();

		MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
		mmsMemberSearch.setMemberNo(new BigDecimal(memberNo));

		//member info
		MmsMember mmsMember = (MmsMember) dao.selectOne("mms.member.getMemberLogin", mmsMemberSearch);

		if (mmsMember == null) {
			return null;
		}
		mmsMemberSearch.setMemberNo(mmsMember.getMemberNo());
		// member Quick Memu
		List<MmsQuickmenu> memberMenus = new ArrayList<MmsQuickmenu>();
		memberMenus = (List<MmsQuickmenu>) dao.selectList("mms.member.getMemberQuickmenuList", mmsMemberSearch);

		loginInfo.setStoreId(mmsMember.getMmsMemberZts().getStoreId());
		loginInfo.setLoginId(mmsMember.getMemberId());	//member id
		loginInfo.setLoginName(mmsMember.getMemberName());	//member name
		loginInfo.setMemberNo(mmsMember.getMemberNo());	//member number
		loginInfo.setCustomerNo(mmsMember.getCustomerNo());
		loginInfo.setMemberStateCd(mmsMember.getMemberStateCd());
//		loginInfo.setMemberYn("Y");
		if (memberMenus != null) {
			loginInfo.setMemberMenus(memberMenus);
		}
		loginInfo.setMembershipYn(mmsMember.getMmsMemberZts().getMembershipYn());
		loginInfo.setB2eYn(mmsMember.getMmsMemberZts().getB2eYn());
		loginInfo.setChildrenYn(mmsMember.getMmsMemberZts().getChildrenYn());
		loginInfo.setChildrenDealId(mmsMember.getMmsMemberZts().getChildrenDealId());
		loginInfo.setPremiumYn(mmsMember.getPremiumYn());
		loginInfo.setMemGradeCd(mmsMember.getMmsMemberZts().getMemGradeCd());
		//임직원여부
		loginInfo.setEmployeeYn(mmsMember.getEmployeeYn());

		loginInfo.setBirthday(mmsMember.getBirthday());
		loginInfo.setGenderCd(mmsMember.getGenderCd());
		loginInfo.setBabyBirthday(mmsMember.getMmsMemberZts().getBabyBirthday());
		loginInfo.setBabyGenderCd(mmsMember.getMmsMemberZts().getBabyGenderCd());
		loginInfo.setPhone2(mmsMember.getPhone2());
		return loginInfo;
	}

	/**
	 * 프론트 비회원주문조회 로그인
	 * 
	 * @Method Name : doNonMemberLogin
	 * @author : eddie
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param loginId
	 * @return
	 */
	public FoLoginInfo doNonMemberLogin(OmsOrder omsOrder) {

		FoLoginInfo loginInfo = new FoLoginInfo();

		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		// System.out.println(encoder.encodePassword("1111", null));

		// 암호화
		omsOrder.setOrderPwd(encoder.encodePassword(omsOrder.getOrderPwd(), null));

		// 비회원 로그인
		omsOrder = (OmsOrder) dao.selectOne("oms.order.getOrderLogin", omsOrder);
		if (omsOrder != null) {
			loginInfo.setLoginId(omsOrder.getOrderId());
			loginInfo.setLoginName(omsOrder.getName1());
			loginInfo.setStoreId(omsOrder.getStoreId());
			return loginInfo;
		} else {
			return null;
		}
	}

}
