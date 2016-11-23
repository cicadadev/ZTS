package gcp.admin.controller.rest.ccs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsFunction;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsMenugroup;
import gcp.ccs.model.search.CcsMenuSearch;
import gcp.ccs.service.MenuService;
import gcp.mms.model.custom.BoLoginInfo;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;
import intune.gsf.model.BaseSearchCondition;

@RestController
@RequestMapping("api/ccs/menu")
public class MenuController {
	private final Log	logger	= LogFactory.getLog(getClass());
	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<CcsMenugroup> getMenuList(HttpServletRequest request) {

		return ((BoLoginInfo) SessionUtil.getLoginInfo(request)).getMenuList();
	}

//	@RequestMapping(value = "/function/auth/list", method = RequestMethod.GET)
//	public Map<String, String> checkAuthButton(HttpServletRequest request, @PathParam("pageId") String pageId) {
//		BoLoginInfo loginInfo = (BoLoginInfo) SessionUtil.getLoginInfo(request);
//
//		Map<String, String> map = new HashMap();
//		String menuId = pageId.split("_")[0];
//		List<CcsMenugroup> ccsMenuGroup = loginInfo.getMenuList();
//		for (CcsMenugroup menuGroup : ccsMenuGroup) {
//			for (CcsMenu menu : menuGroup.getCcsMenus()) {
//				if (menuId.equals(menu.getMenuId())) {
//					for (CcsFunction function : menu.getCcsFunctions()) {
//						map.put(function.getFunctionId(), function.getName());
//					}
//				}
//			}
//		}
//
//		return map;
//	}

//	TODO 권한 pageId -> fnId
	@RequestMapping(value = "/function/auth/list", method = RequestMethod.GET)
	public Map<String, String> checkAuthButton(HttpServletRequest request, @PathParam("fnId") String fnId)
			throws ServletException {
		BoLoginInfo loginInfo = (BoLoginInfo) SessionUtil.getLoginInfo(request);
		Map<String, String> map = new HashMap();
		try {
			String menuId = fnId.split("_")[0];
			List<CcsMenugroup> ccsMenuGroup = loginInfo.getMenuList();
			for (CcsMenugroup menuGroup : ccsMenuGroup) {
				for (CcsMenu menu : menuGroup.getCcsMenus()) {
					if (menuId.equals(menu.getMenuId())) {
						for (CcsFunction function : menu.getCcsFunctions()) {
							map.put(function.getFunctionId(), function.getName());
						}
					}
				}
			}

			return map;
		} catch (Exception e) {
			logger.warn("loginInfo is null" + loginInfo.toString());
			logger.warn("loginInfo.getMenuList is null" + loginInfo.getMenuList().toString());
			throw new ServletException("get LoginInfo Error");
		}
	}

	/**
	 * 
	 * @Method Name : getMenuTree
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴그룹 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/group/tree", method = RequestMethod.POST)
	public List<CcsMenugroup> getMenuTree(@RequestBody CcsMenugroup menuGroup) {
		List<CcsMenugroup> menuList = new ArrayList<>();

		try {
			menuList = menuService.selectMenugroupList(menuGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return menuList;
	}

	/**
	 * 
	 * @Method Name : insertMenuGroup
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴그룹 등록
	 *
	 * @param menuGroup
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/group/insert", method = RequestMethod.POST)
	public String insertMenuGroup(@RequestBody CcsMenugroup menuGroup) throws Exception {
		try {
			menuService.insertMenuGroup(menuGroup);
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}

		return menuGroup.getMenuGroupId();
		/*
		BaseEntity base = new BaseEntity();
		try {
		
			menuService.insertMenuGroup(menuGroup);
		
		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}
		
		return base;*/
	}

	/**
	 * 
	 * @Method Name : updateMenu
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴그룹 기본정보 수정
	 *
	 * @param menuGroup
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/group/update", method = RequestMethod.POST)
	public BaseEntity updateMenu(@RequestBody CcsMenugroup menuGroup) throws Exception {
		BaseEntity base = new BaseEntity();
		try {
			menuGroup.setUpdId(SessionUtil.getLoginId());
			menuService.updateMenuGroup(menuGroup);

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
	 * @Method Name : deleteMenuGroup
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴그룹 삭제
	 *
	 * @param menuGroupId
	 * @throws Exception
	 */
	@RequestMapping(value = "group/{menuGroupId}/delete", method = RequestMethod.POST)
	public String deleteMenuGroup(@PathVariable("menuGroupId") String menuGroupId) {
		try {
			CcsMenugroup menu = new CcsMenugroup();
			menu.setMenuGroupId(menuGroupId);
	
			menuService.deleteMenuGroup(menu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return menuGroupId;
	}

	/**
	 * 메뉴 등록
	 * 
	 * @Method Name : saveMenu
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/menu/save", method = { RequestMethod.POST })
	public BaseEntity saveMenu(@RequestBody List<CcsMenu> reserves) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			menuService.saveMenu(reserves);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * 메뉴 삭제
	 * 
	 * @Method Name : deleteMenu
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/menu/delete", method = { RequestMethod.POST })
	public void deleteMenu(@RequestBody List<CcsMenu> reserves) throws Exception {
		menuService.deleteMenu(reserves);
	}
	

	/**
	 * 
	 * @Method Name : selectMenuList
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description : 메뉴 목록 조회
	 *
	 * @param codeSearch
	 * @return
	 */
	@RequestMapping(value = "/menu/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsMenu> selectMenuList(@RequestBody CcsMenuSearch menuSearch) {

		/*if (StringUtils.isNotEmpty(qnaSerch.getQnaIds())) {
			String[] ids = qnaSerch.getQnaIds().split(",");
			if (ids != null && ids.length > 0) {
				Long[] idArray = new Long[ids.length];
		
				for (int i = 0; i < ids.length; i++) {
					idArray[i] = Long.parseLong(ids[i].replaceAll("'", ""));
				}
				qnaSerch.setQnaIdList(idArray);
			}
		}*/
		menuSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsMenu> list = menuService.selectMenuList(menuSearch);

		/*if (list != null && list.size() > 0) {
			for (CcsCode ccsCodeInfo : list) {
				ccsCodeInfo.setMemberInfo(ccsCodeInfo.getMmsMember().getName1() + "(" + ccsCodeInfo.getMmsMember().getMemId() + ")");
			}
		}*/
		return list;
	}
	
	/**
	 * 메뉴 엑셀 다운로드
	 * 
	 * @Method Name : getMenuListExcel
	 * @author : roy
	 * @date : 2016. 6. 30.
	 * @description :
	 *
	 * @param menuSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/menu/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMenuListExcel(@RequestBody CcsMenuSearch menuSearch) {

		List<CcsMenu> list = new ArrayList<CcsMenu>();
		menuSearch.setStoreId(SessionUtil.getStoreId());
		list = menuService.selectMenuList(menuSearch);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("menuId", list.get(i).getMenuId());
			map.put("sortNo", list.get(i).getSortNo().toString());
			map.put("url", list.get(i).getUrl());
			map.put("name", list.get(i).getName());
			map.put("insDt", list.get(i).getInsDt());
			map.put("insId", list.get(i).getInsId());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) menuSearch, dataList);

		return msg;
	}
}

