package gcp.mms.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsMember;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsOrder;
import intune.gsf.common.utils.MessageUtil;

public class MemberLoginService extends BaseService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(MemberLoginService.class);

	public User loadUserByUsername(String loginId) throws UsernameNotFoundException {

		logger.debug(" Login ID : " + loginId);

		Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		String type = loginId.substring(0, loginId.indexOf("_"));
		String id = loginId.substring(loginId.indexOf("_") + 1);
		String pw = null;
		if ("M".equals(type)) {

			MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
			mmsMemberSearch.setMemberId(id);
			
			MmsMember mmsMember = (MmsMember) dao.selectOne("mms.member.getMemberLogin", mmsMemberSearch);

			if (mmsMember == null) {
				throw new UsernameNotFoundException(MessageUtil.getMessage("AbstractUserDetailsAuthenticationProvider.disabled"));
			}
//			pw = mmsMember.getPassword();
			roles.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		} else {

			OmsOrder omsOrder = new OmsOrder();
			omsOrder.setOrderId(id);
			omsOrder = (OmsOrder) dao.selectOne("oms.order.getOrderLogin", omsOrder);
			if (omsOrder == null) {
				throw new UsernameNotFoundException(MessageUtil.getMessage("AbstractUserDetailsAuthenticationProvider.disabled"));
			}
//			pw = omsOrder.getOrderPwd();
			roles.add(new SimpleGrantedAuthority("ROLE_NON_MEMBER"));
		}

		//logger.debug("password ===================== > " + ccsUser.getPwd());
		//LOGIN INFO RETURN
		User user = new User(loginId, pw, roles);

		return user;
	}
}
