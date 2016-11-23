package gcp.ccs.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import gcp.ccs.model.CcsUser;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;


public class UserLoginService extends BaseService implements UserDetailsService {

	private static final Logger	logger	= LoggerFactory.getLogger(UserLoginService.class);

	public User loadUserByUsername(String userParam) throws UsernameNotFoundException {

		String systemType = "";
		String userId = "";
		User user;
		String[] userInfos = null;

		logger.debug(userParam);

		try {

			if (!CommonUtil.isEmpty(userParam)) {
				userInfos = userParam.split(",");
				systemType = userInfos[0];
				userId = userInfos[1];
			}

			CcsUser ccsUser = new CcsUser();
			ccsUser.setUserId(userId);
			if (BaseConstants.SYSTEM_BO.equals(systemType)) {

				logger.debug("##UserId:" + ccsUser.getUserId() + ",Pwd:" + ccsUser.getPwd());

				ccsUser = (CcsUser) dao.selectOne("ccs.user.getUserByBoLogin", ccsUser);

			} else if (BaseConstants.SYSTEM_PO.equals(systemType)) {
				ccsUser = (CcsUser) dao.selectOne("ccs.user.getUserByPoLogin", ccsUser);
			}

			if (ccsUser == null) {
				throw new UsernameNotFoundException("사용자정보가 없습니다.");
			} else if (BaseConstants.USER_STATE_CD_UNUSE.equals(ccsUser.getUserStateCd())) {
				throw new UsernameNotFoundException("중지된 사용자 입니다.");
			}
			//LOGIN CHECK
			Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));

			//logger.debug("password ===================== > " + ccsUser.getPwd());
			//LOGIN INFO RETURN
			user = new User(userParam, ccsUser.getPwd(), roles);
		} catch (Exception e) {
			logger.error("ERROR : " + e.getMessage());
			throw new UsernameNotFoundException("사용자정보 오류.", e);
		}

		return user;
	}

}
