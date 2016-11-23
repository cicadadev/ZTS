package gcp.ccs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsRole;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsRoleSearch;
import gcp.ccs.model.search.CcsUserPopupSearch;
import gcp.ccs.model.search.CcsUserSearch;
import gcp.common.util.BoSessionUtil;
import gcp.common.util.SHAPasswordEncoder;
import gcp.external.model.TmsQueue;
import gcp.external.service.TmsService;
import gcp.oms.model.OmsOrder;
import gcp.pms.model.PmsReviewpermit;
import gcp.pms.model.search.PmsCategorySearch;
import gcp.pms.model.search.PmsReviewpermitSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class UserService extends BaseService {
	
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private TmsService tmsService;
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
	public List<CcsUser> getUserList(CcsUserSearch search) {
		return (List<CcsUser>) dao.selectList("ccs.user.getUserList", search);
	}

	/**
	 * 유저 그리드 저장
	 * 
	 * @Method Name : saveUser
	 * @author : roy
	 * @date : 2016. 8. 22.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveUser(List<CcsUser> reserve) throws Exception {
			
		for (CcsUser grid : reserve) {
			grid.setUpdId(SessionUtil.getLoginId());
			if(grid.getUseYn().equals("Y")){
				grid.setUserStateCd("USER_STATE_CD.USE");
			}else{
				grid.setUserStateCd("USER_STATE_CD.UNUSE");
			}
			
			grid.setUpdId(SessionUtil.getLoginId());
			grid.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(grid);
		}
	}
	
	/**
	 * 
	 * @Method Name : getUser
	 * @author : emily
	 * @date : 2016. 6. 13.
	 * @description : 사용자 상세 조회
	 *
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public CcsUser getUserDetail(CcsUser user) throws Exception {
		return (CcsUser) dao.selectOne("ccs.user.getUserDetail", user);
	}

	public CcsUser getUserByBoLogin(CcsUser user) throws Exception {
		logger.debug(BoSessionUtil.getSystemType());
		if (BoSessionUtil.getSystemType() != null) {
			if (BaseConstants.SYSTEM_BO.equals(BoSessionUtil.getSystemType())) {
				user = (CcsUser) dao.selectOne("ccs.user.getUserByBoLogin", user);
			} else {
				user = (CcsUser) dao.selectOne("ccs.user.getUserByPoLogin", user);
			}
		} else {
			user = (CcsUser) dao.selectOne("ccs.user.getUserByBoLogin", user);
		}
		return user;
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
	public int updateUser(CcsUser user) throws Exception {
		user.setUpdId(SessionUtil.getLoginId());
		user.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(user);
	}

	/**
	 * 
	 * @Method Name : insertUser
	 * @author : emily
	 * @date : 2016. 6. 14.
	 * @description : 사용자 등록
	 *
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public void insertUser(CcsUser user) throws Exception {
		user.setUpdId(SessionUtil.getLoginId());
		user.setStoreId(SessionUtil.getStoreId());
		dao.insertOneTable(user);
	}

	/**
	 * 
	 * @Method Name : getRoleList
	 * @author : emily
	 * @date : 2016. 6. 14.
	 * @description : 역할 목록 조회
	 *
	 * @param search
	 * @return
	 */
	public List<CcsRole> getRoleList(CcsRoleSearch search) {
		return (List<CcsRole>) dao.selectList("ccs.user.getRoleList", search);
	}

	public void deleteUser(List<CcsUser> userList) throws Exception {

		for (CcsUser ccsUser : userList) {
			dao.deleteOneTable(ccsUser);
		}
	}

	public List<CcsUser> getUserListPopup(CcsUserPopupSearch ccsUserPopupSearch) {
		return (List<CcsUser>) dao.selectList("ccs.user.getUserListPopup", ccsUserPopupSearch);
	}

	public List<CcsRole> getRoleList(String storeId) {
		return (List<CcsRole>) dao.selectList("ccs.menu.getRoleList", storeId);
	}

	public CcsRole getRole(String roleId) {
		return (CcsRole) dao.selectOne("ccs.menu.getRole", roleId);
	}

	public int insertRole(CcsRole role) throws Exception {
		role.setUpdId(SessionUtil.getLoginId());
		role.setStoreId(SessionUtil.getStoreId());
		return dao.insert("ccs.menu.insertRole", role);
	}

	public int updateRole(CcsRole role) throws Exception {
		role.setUpdId(SessionUtil.getLoginId());
		role.setStoreId(SessionUtil.getStoreId());
		return dao.update("ccs.menu.updateRole", role);
	}

	public int deleteRole(String roleId) throws Exception {
		return dao.delete("ccs.menu.deleteRole", roleId);
	}

	public List getAmfList(String roleId, String langCd) {
		Map map = new HashMap<String, String>();
		map.put("roleId", roleId);
		map.put("langCd", langCd);
		return (List) dao.selectList("ccs.menu.getAmfListByRole", map);
	}

	/**
	 * 표준 카테고리번호로 사용자 조회
	 * 
	 * @Method Name : getUserByCategoryId
	 * @author : eddie
	 * @date : 2016. 6. 6.
	 * @description : 표준카테고리의 MD담당자를 조회한다.
	 *
	 * @param search
	 * @return
	 */
	public CcsUser getUserByCategoryId(PmsCategorySearch search) {
		return (CcsUser) dao.selectOne("ccs.user.getUserByCategoryId", search);
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
	public int getUserIdDuplicate(String userId) {

		return (int) dao.selectCount("ccs.user.getUserIdCnt", userId);
	}

	/**
	 * 
	 * @Method Name : issueTempPwd
	 * @author : allen
	 * @date : 2016. 8. 9.
	 * @description : 사용자 임시 비밀번호 발급
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public boolean issueTempPwd(CcsUserSearch search) throws Exception {
		String smsMsg = "";
//		int cnt = (int) dao.selectCount("ccs.user.checkUserInfo", search);
		String phone = null;

		if (CommonUtil.isNotEmpty(search.getMyPwd()) && CommonUtil.isEmpty(search.getPhone())) {
			return false;
		}
		if ("BO".equals(search.getSystemType())) {
			phone = (String) dao.selectOne("ccs.user.getBoUserPhone", search);
			
		} else {
			phone = (String) dao.selectOne("ccs.user.getPoUserPhone", search);
		}
		if (CommonUtil.isEmpty(phone)) {
			return false;
		}
		search.setPhone(phone);
		String tempPwd = CommonUtil.makeCode(8);

		smsMsg = "임시비밀번호는 " + tempPwd + " 입니다.";
		CcsUser user = new CcsUser();
		SHAPasswordEncoder encoder = new SHAPasswordEncoder();
		user.setPwd(encoder.encodePassword(tempPwd, null));
		user.setUpdId(SessionUtil.getLoginId());
		user.setStoreId(SessionUtil.getStoreId());
		user.setUserId(search.getUserId());
		dao.updateOneTable(user);

		//문자 발송
		TmsQueue tmsQueue = new TmsQueue();
		tmsQueue.setMsgCode("132");
		tmsQueue.setMap1(search.getUserId());
		tmsQueue.setMap2(tempPwd);
		tmsQueue.setToPhone(search.getPhone());
		tmsService.sendTmsSmsQueue(tmsQueue);

		return true;
		
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
	public List<PmsReviewpermit> getReviewpermitList(PmsReviewpermitSearch search) {
		return (List<PmsReviewpermit>) dao.selectList("ccs.user.getReviewpermitUserList", search);
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
	public void deleteReviewpermit(List<PmsReviewpermit> userList) throws Exception {
		for (PmsReviewpermit info : userList) {
			dao.deleteOneTable(info);
		}

	}


	/**
	 * 
	 * @Method Name : Reviewpermit
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 체험단 관리 등록 & 수정
	 *
	 * @param list
	 * @throws Exception
	 */
	public void saveReviewpermit(List<PmsReviewpermit> list) throws Exception {

		for (PmsReviewpermit info : list) {

			if (CommonUtil.isNotEmpty(info.getPermitNo())) {
				info.setUpdId(SessionUtil.getLoginId());
				info.setStoreId(SessionUtil.getStoreId());
				dao.updateOneTable(info);
			} else {

				dao.insertOneTable(info);
			}
		}
	}
	
	/**
	 * 
	 * @Method Name : getReviewpermiProductList
	 * @author : emily
	 * @date : 2016. 6. 22.
	 * @description : 체험단 관리 상품 목록조회
	 *
	 * @param search
	 * @return
	 */
	public List<PmsReviewpermit> getReviewpermiProductList(PmsReviewpermitSearch search) {
		return (List<PmsReviewpermit>) dao.selectList("ccs.user.getReviewpermiProductList", search);
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
	public List<PmsReviewpermit> getReviewpermitDuplicate(PmsReviewpermitSearch search) {
		return (List<PmsReviewpermit>) dao.selectList("ccs.user.getReviewpermitDuplicate", search);
	}

	/**
	 * 프론트 주문번호 찾기
	 * 
	 * @Method Name : findOrderId
	 * @author : roy
	 * @date : 2016. 10. 31.
	 * @description :
	 *
	 * @param omsOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String findOrderId(OmsOrder omsOrder) {

		//암호화
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		omsOrder.setOrderPwd(encoder.encodePassword(omsOrder.getOrderPwd(), null));

		// 주문 리스트 조회
		List<OmsOrder> list = (List<OmsOrder>) dao.selectList("oms.order.nonememberGetOrderId", omsOrder);

		// 조회된 주문ID
		String orderIds = "";
		for (OmsOrder info : list) {
			if (CommonUtil.isNotEmpty(info.getOrderId())) {
				orderIds += orderIds + info.getOrderId();
			}
		}

//		System.out.println(encoder.encodePassword("1111", null));

		return orderIds;

	}

	/**
	 * 프론트 비밀번호 찾기
	 * 
	 * @Method Name : findOrderPwd
	 * @author : roy
	 * @date : 2016. 10. 31.
	 * @description :
	 *
	 * @param omsOrder
	 * @return
	 * @throws Exception
	 */
	public String findOrderPwd(OmsOrder omsOrder) throws Exception {

		// 주문 조회
		OmsOrder order = (OmsOrder) dao.selectOne("oms.order.nonememberGetOrderPwd", omsOrder);

		// 주문PWD
		if (order != null) {
			String tempPwd = CommonUtil.makeCode(6);

//			암호화
			SHAPasswordEncoder encoder = new SHAPasswordEncoder();
			order.setOrderPwd(encoder.encodePassword(tempPwd, null));
			order.setUpdId(SessionUtil.getLoginId());
			order.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(order);
			
			//문자 발송
//			TmsQueue tmsQueue = new TmsQueue();
//			tmsQueue.setMsgCode("130");
//			tmsQueue.setMap1(omsOrder.getName1());
//			tmsQueue.setMap2(tempPwd);
//			tmsQueue.setToPhone(omsOrder.getPhone2());
//			tmsService.sendTmsSmsQueue(tmsQueue);

			return tempPwd;
		} else {
			return null;
		}

	}
}
