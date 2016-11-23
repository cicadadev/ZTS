package gcp.admin.controller.rest.ccs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsAccessfunction;
import gcp.ccs.model.CcsAccessmenu;
import gcp.ccs.model.CcsFunction;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsRole;
import gcp.ccs.model.search.CcsAccessmenuSearch;
import gcp.ccs.model.search.CcsFunctionSearch;
import gcp.ccs.model.search.CcsRoleSearch;
import gcp.ccs.service.RoleService;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/ccs/role")
public class RoleController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private RoleService	roleService;


	/**
	 * 
	 * @Method Name : getRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/group/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsRole> getRoleGroup() {
		List<CcsRole> roleGroup = new ArrayList<>();

		try {
			CcsRoleSearch search = new CcsRoleSearch();
			search.setStoreId(SessionUtil.getStoreId());

			roleGroup = roleService.getRoleGroup(search);
		} catch (Exception e) {
			logger.error(e);
		}

		return roleGroup;
	}

	/**
	 * 
	 * @Method Name : getRolePageList
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹별 메뉴&기능 선택
	 *
	 * @param search
	 */
	@RequestMapping(value = "/menu/list", method = RequestMethod.POST)
	public List<CcsMenu> getRoleMenus(@RequestBody CcsRoleSearch search) {
		List<CcsMenu> accessibleMenuFunction = new ArrayList<>();
		try {
			search.setLangCd(SessionUtil.getLangCd());			
			accessibleMenuFunction = roleService.getAccessibleMenuFunction(search);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return accessibleMenuFunction;
	}

	/**
	 * 
	 * @Method Name : insertRoleGroupo
	 * @author : stella
	 * @date : 2016. 6. 16.
	 * @description : 권한 그룹 등록
	 *
	 * @param ccsRole
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertRoleGroup(@RequestBody CcsRole ccsRole) {
		try {
			ccsRole.setStoreId(SessionUtil.getStoreId());
			roleService.insertRoleGroup(ccsRole);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		return ccsRole.getRoleId();
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
	 */
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public String updateRoleGroup(@RequestBody CcsRole ccsRole) {
		try {
			ccsRole.setStoreId(SessionUtil.getStoreId());
			ccsRole.setUpdId(SessionUtil.getLoginId());
			roleService.updateRoleGroup(ccsRole);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		return ccsRole.getRoleId();
	}
	
	/**
	 * 
	 * @Method Name : updateRoleMenus
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹별 메뉴 사용권한  수정
	 *
	 * @param accessMenu
	 * @return
	 */
	@RequestMapping(value = "/menu/update", method = RequestMethod.PUT)
	public String updateRoleMenus(@RequestBody List<CcsAccessmenu> accessMenu) {
		try {
			roleService.updateRoleMenus(accessMenu);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		return Integer.toString(accessMenu.size());
	}
	
	/**
	 * 
	 * @Method Name : checkRoleMenu
	 * @author : roy
	 * @date : 2016. 7. 14.
	 * @description : 권한 메뉴 여부 체크
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/menu/check", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity checkRoleMenu(@RequestBody CcsAccessmenuSearch search) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			roleService.checkRoleMenu(search);

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
	 * @Method Name : updateRoleMenuFunctions
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹별 메뉴별 기능 사용여부 수정
	 *
	 * @param accessFunctions
	 * @return
	 */
	@RequestMapping(value = "/menu/function/update", method = RequestMethod.PUT)
	public String updateRoleMenuFunctions(@RequestBody List<CcsAccessfunction> accessFunctions) {
		try {
			roleService.updateRoleMenuFunctions(accessFunctions);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		return Integer.toString(accessFunctions.size());
	}

	/**
	 * 권한 그룹 삭제 전 체크
	 * 
	 * @Method Name : checkRoleGroup
	 * @author : roy
	 * @date : 2016. 7. 26.
	 * @description :
	 *
	 * @param ccsRole
	 * @throws Exception
	 */
	@RequestMapping(value = "/check", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity checkRoleGroup(@RequestBody CcsRole ccsRole) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			roleService.checkRoleGroup(ccsRole);

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
	 * @Method Name : deleteRoleGroup
	 * @author : stella
	 * @date : 2016. 6. 21.
	 * @description : 권한 그룹 삭제
	 *
	 * @param ccsRole
	 * @return
	 */	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteRoleGroup(@RequestBody CcsRole ccsRole) {
		try {
			ccsRole.setStoreId(SessionUtil.getStoreId());
			roleService.deleteRoleGroup(ccsRole);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		return ccsRole.getRoleId();
	}

	/**
	 * 
	 * @Method Name : getFuncList
	 * @author : roy
	 * @date : 2016. 7. 14.
	 * @description : 기능 리스트 검색
	 *
	 * @param search
	 */
	@RequestMapping(value = "/func/list", method = RequestMethod.POST)
	public List<CcsFunction> getFuncList(@RequestBody CcsFunctionSearch search) {
		List<CcsFunction> functionSearch = new ArrayList<>();
		try {
			search.setLangCd(SessionUtil.getLangCd());
			functionSearch = roleService.getFunctionList(search);
		} catch (Exception e) {
			logger.error(e);
		}

		return functionSearch;
	}

	/**
	 * 기능 등록
	 * 
	 * @Method Name : saveFunction
	 * @author : roy
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/func/save", method = { RequestMethod.POST })
	public BaseEntity saveFunction(@RequestBody List<CcsAccessfunction> reserves) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			roleService.saveFunction(reserves);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}
}
