package gcp.ccs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import gcp.ccs.model.CcsAccessfunction;
import gcp.ccs.model.CcsAccessmenu;
import gcp.ccs.model.CcsFunction;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsRole;
import gcp.ccs.model.search.CcsAccessmenuSearch;
import gcp.ccs.model.search.CcsFunctionSearch;
import gcp.ccs.model.search.CcsRoleSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class RoleService extends BaseService {

	/**
	 * 
	 * @Method Name : getRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<CcsRole> getRoleGroup(CcsRoleSearch search) throws Exception {
		return (List<CcsRole>) dao.selectList("ccs.role.getRoleGroup", search);
	}
	
	/**
	 * 
	 * @Method Name : getAccessibleMenuFunction
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹별 사용 가능한 메뉴,기능 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<CcsMenu> getAccessibleMenuFunction(CcsRoleSearch search) throws Exception {
		return (List<CcsMenu>) dao.selectList("ccs.role.getAccessibleMenuList", search);
	}

	/**
	 * 
	 * @Method Name : insertRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹 등록
	 *
	 * @param ccsRole
	 * @return
	 * @throws Exception
	 */
	public Integer insertRoleGroup(CcsRole ccsRole) throws Exception {
		return dao.insertOneTable(ccsRole);
	}
	
	/**
	 * 
	 * @Method Name : updateRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹 정보 수정
	 *
	 * @param ccsRole
	 * @return
	 * @throws Exception
	 */
	public Integer updateRoleGroup(CcsRole ccsRole) throws Exception {
		ccsRole.setUpdId(SessionUtil.getLoginId());
		ccsRole.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(ccsRole);
	}
	
	/**
	 * 
	 * @Method Name : updateRoleMenus
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹별 메뉴 권한여부 수정
	 *
	 * @param accessMenus
	 * @return
	 * @throws Exception
	 */	
	public Integer updateRoleMenus(List<CcsAccessmenu> accessMenus) throws Exception {
		int iResult = 0;
		
		List<String> accessMenuList = (List<String>) dao.selectList("ccs.role.checkAccessMenus", accessMenus.get(0).getRoleId());
		for (CcsAccessmenu menu: accessMenus) {
			if (!accessMenuList.contains(menu.getMenuId())) {
				if ("Y".equals(menu.getMenuRoleYn())) {
					dao.insertOneTable(menu);
				}
			} else {
				if ("N".equals(menu.getMenuRoleYn())) {
					menu.setUpdId(SessionUtil.getLoginId());
					dao.deleteOneTable(menu);
				}
			}
		}
		
		return iResult;
	}
	
	/**
	 * 
	 * @Method Name : updateRoleMenuFunctions
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹별 메뉴별 기능 사용여부 수정
	 *
	 * @param ccsRole
	 * @return
	 * @throws Exception
	 */	
	public Integer updateRoleMenuFunctions(List<CcsAccessfunction> accessFunctions) throws Exception {
		int iResult = 0;
		
		if (accessFunctions.size() > 0) {		
			for (CcsAccessfunction function: accessFunctions) {
				String strIsAccessible = (String) dao.selectOne("ccs.role.checkAccessFunctions", function);
				if (CommonUtil.isEmpty(strIsAccessible)) {
					if ("Y".equals(function.getUseYn())) {
						dao.insertOneTable(function);
					}
				} else {
					if ("N".equals(function.getUseYn())) {
						function.setUpdId(SessionUtil.getLoginId());
						dao.deleteOneTable(function);
					}
				}
				/*// 모든 기능이 미사용으로 체크된 상태
				if (CommonUtil.isEmpty(function.getMenuId()) && CommonUtil.isEmpty(function.getFunctionId())) {
					dao.delete("ccs.role.deleteAllFunctions", function.getRoleId());
				} else {
					String strIsAccessible = (String) dao.selectOne("ccs.role.checkAccessFunctions", function);
					if (CommonUtil.isEmpty(strIsAccessible)) {
						if ("Y".equals(function.getUseYn())) {
							dao.insertOneTable(function);
						}
					} else {
						if ("N".equals(function.getUseYn())) {
							dao.deleteOneTable(function);
						}
					}
				}*/
			}
		}
		
		return iResult;
	}

	/**
	 * 
	 * @Method Name : checkRoleMenu
	 * @author : roy
	 * @date : 2016. 7. 14.
	 * @description : 권한 메뉴 여부 체크
	 *
	 * @param ccsRole
	 * @return
	 */
	public void checkRoleMenu(CcsAccessmenuSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		String exists1 = (String) dao.selectOne("ccs.role.checkAccessMenu", search);
		if (CommonUtil.isEmpty(exists1)) {
			throw new ServiceException("pms.mdnotice.notice");
		}
	}

	/**
	 * 권한 삭제 전 체크
	 * 
	 * @Method Name : checkRoleGroup
	 * @author : roy
	 * @date : 2016. 7. 26.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	public void checkRoleGroup(@RequestBody CcsRole ccsRole) throws Exception {
		ccsRole.setStoreId(SessionUtil.getStoreId());
		int exists1 = (int) dao.selectOne("ccs.role.selectRoleCheck", ccsRole);
		if (exists1 > 0) {
			throw new ServiceException("ccs.role.found");
		}
	}

	/**
	 * 
	 * @Method Name : deleteRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹 삭제
	 *
	 * @param ccsRole
	 * @return
	 */	
	public Integer deleteRoleGroup(CcsRole ccsRole) throws Exception {
		// 하쉬 권한 기능 삭제
		CcsAccessfunction function = new CcsAccessfunction();
		function.setRoleId(ccsRole.getRoleId());
		dao.selectList("ccs.role.deleteAllFunctions", function);

		// 하위 권한 메뉴 삭제
		CcsAccessmenu menu = new CcsAccessmenu();
		menu.setRoleId(ccsRole.getRoleId());

		dao.selectList("ccs.role.deleteAllMenus", menu);
		return dao.deleteOneTable(ccsRole);
	}

	/**
	 * 
	 * @Method Name : getFunctionList
	 * @author : roy
	 * @date : 2016. 7. 14.
	 * @description : 기능 목록 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<CcsFunction> getFunctionList(CcsFunctionSearch search) throws Exception {
		return (List<CcsFunction>) dao.selectList("ccs.role.getFunctionList", search);
	}

	/**
	 * 기능 등록
	 * 
	 * @Method Name : saveFunction
	 * @author : roy
	 * @date : 2016. 7. 14.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveFunction(List<CcsAccessfunction> reserve) throws Exception {
		if (reserve.size() > 0) {
			for (CcsAccessfunction function : reserve) {
				String strIsAccessible = (String) dao.selectOne("ccs.role.checkAccessFunctions", function);
				if (CommonUtil.isEmpty(strIsAccessible)) {
					if ("Y".equals(function.getUseYn())) {
						dao.insertOneTable(function);
					}
				} else {
					if ("N".equals(function.getUseYn())) {
						function.setUpdId(SessionUtil.getLoginId());
						dao.deleteOneTable(function);
					}
				}
				/*// 모든 기능이 미사용으로 체크된 상태
				if (CommonUtil.isEmpty(function.getMenuId()) && CommonUtil.isEmpty(function.getFunctionId())) {
					dao.delete("ccs.role.deleteAllFunctions", function.getRoleId());
				} else {
					String strIsAccessible = (String) dao.selectOne("ccs.role.checkAccessFunctions", function);
					if (CommonUtil.isEmpty(strIsAccessible)) {
						if ("Y".equals(function.getFunctionRoleYn())) {
							dao.insertOneTable(function);
						}
					} else {
						if ("N".equals(function.getFunctionRoleYn())) {
							dao.deleteOneTable(function);
						}
					}
				}*/
			}
		}
	}
}
