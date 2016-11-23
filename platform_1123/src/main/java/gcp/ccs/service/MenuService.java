package gcp.ccs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsMenugroup;
import gcp.ccs.model.search.CcsMenuSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.SessionUtil;

@Service
public class MenuService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<CcsMenugroup> selectMenugroupList(CcsMenugroup menuGroupSearch) {
		return (List<CcsMenugroup>) dao.selectList("ccs.menu.getMenugroupList", null);
	}

	/**
	 * 
	 * @Method Name : selectCodeList
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description : 코드그 목록 조회
	 * 
	 * @param codeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsMenu> selectMenuList(CcsMenuSearch menuSearch) {
		return (List<CcsMenu>) dao.selectList("ccs.menu.getMenuList", menuSearch);
	}

	/**
	 * 
	 * @Method Name : selectCodeList
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description : 코드그 목록 조회
	 * 
	 * @param codeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CcsMenugroup selectMenugroup(CcsMenugroup menuSearch) {
		return (CcsMenugroup) dao.selectOne("ccs.menu.selectMenuGroup", menuSearch);
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
	 */
	public Integer insertMenuGroup(CcsMenugroup menuGroup) throws Exception {
		// 메뉴그룹명이 존재하는지
		/*String exists1 = (String) dao.selectOne("ccs.menu.selectMenuGroupOne", menuGroup);
		if (CommonUtil.isNotEmpty(exists1)) {
			throw new ServiceException("ccs.menu.save.MenuGroupId");
		}
		dao.insertOneTable(menuGroup);*/
		return dao.insertOneTable(menuGroup);
	}

	/**
	 * 
	 * @Method Name : updateMenuGroup
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴 그룹 기본정보 수정
	 *
	 * @param menuGroup
	 * @return
	 */
	public void updateMenuGroup(CcsMenugroup menuGroup) throws Exception {
		menuGroup.setUpdId(SessionUtil.getLoginId());
		dao.updateOneTable(menuGroup);
	}

	/**
	 * 
	 * @Method Name : deleteMenuGroup
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description : 메뉴그룹 삭제
	 *
	 * @param menuGroup
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuGroup(CcsMenugroup menuGroup) throws Exception {
		dao.delete("ccs.menu.deleteMenuIDAccessFunc", menuGroup);
		dao.delete("ccs.menu.deleteMenuIDAccessMenu", menuGroup);
		dao.delete("ccs.menu.deleteMenuIDFuncLang", menuGroup);
		dao.delete("ccs.menu.deleteMenuIDFunc", menuGroup);
		//dao.delete("ccs.menu.deleteMenuIDMenuLang", menuGroup);
		//dao.delete("ccs.menu.deleteMenuIDMenuGrpLang", menuGroup);
		dao.delete("ccs.menu.deleteMenu", menuGroup);
		return dao.deleteOneTable(menuGroup);
	}

	/**
	 * 메뉴 저장
	 * 
	 * @Method Name : saveMenu
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveMenu(List<CcsMenu> reserve) throws Exception {

		for (CcsMenu r : reserve) {
			if ("C".equals(r.getCrudType())) {
				r.setSystemTypeCd(BaseConstants.SYSTEM_TYPE_CD_BO);
				dao.insertOneTable(r);
			} else {
				r.setUpdId(SessionUtil.getLoginId());
				dao.updateOneTable(r);
			}
		}
	}


	/**
	 * 메뉴 삭제
	 * 
	 * @Method Name : deleteMenu
	 * @author : roy
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void deleteMenu(List<CcsMenu> reserve) throws Exception {
		for (CcsMenu r : reserve) {
			dao.delete("ccs.menu.deleteMenuChildAccessFunc", r);
			dao.delete("ccs.menu.deleteMenuChildAccessMenu", r);
			dao.delete("ccs.menu.deleteMenuChildFuncLang", r);
			dao.delete("ccs.menu.deleteMenuChildFunc", r);
			//dao.delete("ccs.menu.deleteMenuChildMenuLang", r);
			dao.deleteOneTable(r);
		}
	}

}
