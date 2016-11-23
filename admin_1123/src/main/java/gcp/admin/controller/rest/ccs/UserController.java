package gcp.admin.controller.rest.ccs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsRole;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsRoleSearch;
import gcp.ccs.model.search.CcsUserPopupSearch;
import gcp.ccs.model.search.CcsUserSearch;
import gcp.ccs.service.UserService;
import gcp.common.util.BoSessionUtil;
import gcp.common.util.SHAPasswordEncoder;
import gcp.mms.model.custom.BoLoginInfo;
import gcp.pms.model.PmsReviewpermit;
import gcp.pms.model.search.PmsCategorySearch;
import gcp.pms.model.search.PmsReviewpermitSearch;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/ccs/user")
public class UserController {

	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private UserService userService;

	/**
	 * 
	 * @Method Name : getUserList
	 * @author : emily
	 * @date : 2016. 6. 13.
	 * @description : 사용자 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsUser> getUserList(@RequestBody CcsUserSearch search) {

		String systemType = BoSessionUtil.getSystemType();

		search.setSystemType(systemType);
		search.setStoreId(SessionUtil.getStoreId());

		if ("PO".equals(systemType)) {
			search.setBusinessId(BoSessionUtil.getBusinessId());
		}
		return userService.getUserList(search);
	}

	/**
	 * 유저 그리드 저장
	 * 
	 * @Method Name : saveUser
	 * @author : roy
	 * @date : 2016. 8. 22.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public BaseEntity saveUser(@RequestBody List<CcsUser> reserves) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			userService.saveUser(reserves);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}
	/**
	 * 
	 * @Method Name : getUser
	 * @author : emily
	 * @date : 2016. 6. 13.
	 * @description : 사용자 상세 조회
	 *
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public CcsUser getUser(@PathVariable("userId") String userId) throws Exception {
		CcsUser user = new CcsUser();
		user.setStoreId(SessionUtil.getStoreId());
		user.setUserId(userId);
		return userService.getUserDetail(user);
	}

	/**
	 * 
	 * @Method Name : updateUser
	 * @author : emily
	 * @date : 2016. 6. 13.
	 * @description : 사용자 정보 수정
	 *
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(@RequestBody CcsUser user) throws Exception {
		String resultCode = "";
		user.setStoreId(SessionUtil.getStoreId());
		user.setUpdId(SessionUtil.getLoginId());
		if ("Y".equals(user.getPwdModifyYn())) {
			SHAPasswordEncoder encoder = new SHAPasswordEncoder();
			user.setPwd(encoder.encodePassword(user.getNewPwd(), null));
		}

		try {
			userService.updateUser(user);
			resultCode = "success";

		} catch (Exception e) {
			resultCode = "fail";
			logger.error(e);
		}

		return resultCode;
	}

	/**
	 * 
	 * @Method Name : insertUser
	 * @author : emily
	 * @date : 2016. 6. 13.
	 * @description : 사용자 신규 등록
	 *
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	public String insertUser(@RequestBody CcsUser user) throws Exception {

		SHAPasswordEncoder encoder = new SHAPasswordEncoder();
		user.setPwd(encoder.encodePassword(user.getPwd(), null));
		
		String resultCode = "";
		try{
			if("USER_TYPE_CD.BUSINESS".equals(user.getUserTypeCd())){
				user.setRoleId(Config.getString("partner.user.role.id"));
			}
			resultCode = "success";
			userService.insertUser(user);

		} catch (Exception e) {
			resultCode = "fail";
			logger.error(e);
		}

		return resultCode;
	}

	/**
	 * 
	 * @Method Name : deleteUser
	 * @author : emily
	 * @date : 2016. 6. 14.
	 * @description : 사용자 삭제
	 *
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteUser(@RequestBody List<CcsUser> userList) throws Exception {
		userService.deleteUser(userList);

	}

	/**
	 * 
	 * @Method Name : userListPopup
	 * @author : dennis
	 * @date : 2016. 5. 26.
	 * @description : 사용자 검색 (popup)
	 *
	 * @param ccsUserPopupSearch
	 * @return List<CcsUser>
	 */
	@RequestMapping(value = "/popup/list", method = RequestMethod.POST)
	public List<CcsUser> userListPopup(@RequestBody CcsUserPopupSearch ccsUserPopupSearch) {
		ccsUserPopupSearch.setStoreId(SessionUtil.getStoreId());
		ccsUserPopupSearch.setLangCd(SessionUtil.getLangCd());
		return userService.getUserListPopup(ccsUserPopupSearch);
	}

	/**
	 * 
	 * @Method Name : getRoleList
	 * @author : emily
	 * @date : 2016. 6. 14.
	 * @description : 역할 (권한)POPUP 목록 조회
	 *
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/role/list", method = RequestMethod.POST)
	public List<CcsRole> getRoleList(@RequestBody CcsRoleSearch search) {
		return userService.getRoleList(search);
	}

	/**
	 * 
	 * @Method Name : getUserIdDuplicate
	 * @author : emily
	 * @date : 2016. 6. 15.
	 * @description : 사용자 ID 중복체크
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/duplicateCheck", method = RequestMethod.POST)
	public String getUserIdDuplicate(@RequestParam("id") String userId) {

		int idCount = userService.getUserIdDuplicate(userId);

		return String.valueOf(idCount);
	}

	/**
	 * 
	 * @Method Name : findPwd
	 * @author : allen
	 * @date : 2016. 8. 9.
	 * @description : 사용자 비밀번호 찾기
	 *
	 * @param userSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findPwd", method = RequestMethod.POST)
	public String findPwd(@RequestBody CcsUserSearch userSearch) throws Exception {

//		String systemType = BoSessionUtil.getSystemType();
//		if (CommonUtil.isNotEmpty(systemType)) {
//			userSearch.setSystemType(systemType);
//		}

		boolean success = userService.issueTempPwd(userSearch);

		if (success) {
			return "success";
		} else {
			return "fail";
		}

	}

	@RequestMapping(value = "/checkUserInfo", method = RequestMethod.POST)
	public boolean checkUserInfo(@RequestBody CcsUser user) throws Exception {
		
		boolean checkResult = false;
		SHAPasswordEncoder encoder = new SHAPasswordEncoder();
		user.setPwd(encoder.encodePassword(user.getPwd(), null));
		CcsUser userInfo = userService.getUserByBoLogin(user); 
		if (userInfo != null) {
			checkResult = true;
		} 
		return checkResult;
	}

	/**
	 * 
	 * @Method Name : getReviewpermitList
	 * @author : emily
	 * @date : 2016. 6. 21.
	 * @description : 체험단 관리 검색 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/reviewpermit/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsReviewpermit> getReviewpermitList(@RequestBody PmsReviewpermitSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		return userService.getReviewpermitList(search);
	}

	/**
	 * 
	 * @Method Name : deleteReviewpermit
	 * @author : emily
	 * @date : 2016. 6. 21.
	 * @description : 체험단 관리 회원 삭제
	 *
	 * @param userList
	 * @throws Exception
	 */
	@RequestMapping(value = "/reviewpermit/delete", method = RequestMethod.POST)
	public void deleteReviewpermit(@RequestBody List<PmsReviewpermit> userList) throws Exception {
		userService.deleteReviewpermit(userList);
	}

	/**
	 * 
	 * @Method Name : insertReviewpermit
	 * @author : emily
	 * @date : 2016. 6. 21.
	 * @description : 체험단 관리 회원 등록& 수정
	 *
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reviewpermit/product/save", method = RequestMethod.POST)
	public void saveReviewpermit(@RequestBody List<PmsReviewpermit> prd) throws Exception {

		try {

			for (PmsReviewpermit info : prd) {
				info.setStoreId(SessionUtil.getStoreId());
			}

			userService.saveReviewpermit(prd);
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @Method Name : getReviewpermiProductList
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 체험단 관리 상품 목록
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/reviewpermit/product/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsReviewpermit> getReviewpermiProductList(@RequestBody PmsReviewpermitSearch search) {
		logger.debug("############### getMemNo:" + search.getMemberNo());
		search.setStoreId(SessionUtil.getStoreId());

		return userService.getReviewpermiProductList(search);
	}

	/**
	 * 
	 * @Method Name : getReviewpermitDuplicate
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 체험단 관리 회원과 상품 중복체크
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/reviewpermit/duplicate", method = { RequestMethod.POST })
	public List<PmsReviewpermit> getReviewpermitDuplicate(@RequestBody PmsReviewpermitSearch search) {

		for (String info : search.getProductList()) {
			logger.debug("############ info :" + info);
		}
		search.setStoreId(SessionUtil.getStoreId());
		return userService.getReviewpermitDuplicate(search);
	}


	/*@RequestMapping(value = "/roles", method = { RequestMethod.GET })
	public List<CcsRole> getRoleList(HttpServletRequest request) {
		return userService.getRoleList(((LoginInfo) SessionUtil.getLoginInfo(request)).getStoreId());
	}*/

	@RequestMapping(value = "/roles/{id}", method = { RequestMethod.GET })
	public CcsRole getRole(@PathVariable("id") String id, HttpServletRequest request) {
		return userService.getRole(id);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST, produces = "text/plain")
	public Integer insertRole(@RequestBody CcsRole role, HttpServletRequest request) throws Exception {
		BoLoginInfo user = (BoLoginInfo) SessionUtil.getLoginInfo(request);
		role.setStoreId(user.getStoreId());
		role.setInsId(user.getLoginId());
		role.setUpdId(user.getLoginId());
		return userService.insertRole(role);
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
	public Integer updateRole(@PathVariable("id") String id, @RequestBody CcsRole role, HttpServletRequest request)
			throws Exception {
		BoLoginInfo user = (BoLoginInfo) SessionUtil.getLoginInfo(request);
		role.setStoreId(user.getStoreId());
		role.setUpdId(user.getLoginId());
		return userService.updateRole(role);
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
	public Integer deleteRole(@PathVariable("id") String id) throws Exception {
		return userService.deleteRole(id);
	}

	@RequestMapping(value = "/roles/{id}/amfs", method = { RequestMethod.GET })
	public List getAmfList(@PathVariable("id") String id, HttpServletRequest request) {
		return userService.getAmfList(id, ((BoLoginInfo) SessionUtil.getLoginInfo(request)).getLangCd());
	}

	@RequestMapping(value="/category", method={RequestMethod.GET})
	public CcsUser getUserByCategoryId(PmsCategorySearch search){
		search.setStoreId(SessionUtil.getStoreId());
		return userService.getUserByCategoryId(search);
	}
}
