package gcp.ccs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.search.CcsCodeSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.ccs.service
 * @FileName : CodeService.java
 * @author : roy
 * @date : 2016. 6. 15.
 * @description : Code Service
 */
@Service
public class CodeService extends BaseService {

	/**
	 * 
	 * @Method Name : selectCodegroupList
	 * @author : roy
	 * @date : 2016. 6. 13.
	 * @description : 코드그룹관리 목록 조회
	 * 
	 * @param codegroupSerch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsCodegroup> selectCodegroupList(CcsCodeSearch codegroupSearch) {
		return (List<CcsCodegroup>) dao.selectList("ccs.code.getCodegroupList", codegroupSearch);
	}

	/**
	 * 코드 그룹 등록
	 * 
	 * @Method Name : saveCodeGroup
	 * @author : roy
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveCodeGroup(List<CcsCodegroup> reserve) throws Exception {
			
		for (CcsCodegroup grid : reserve) {
			grid.setCdGroupTypeCd("SYSTEM");
			grid.setUpdId(SessionUtil.getLoginId());
			if (StringUtils.isEmpty(grid.getInsDt())) {

				// 코드그룹명이 존재 여부 확인
				CcsCodeSearch cdSearch = new CcsCodeSearch();
				cdSearch.setCdGroupCd(grid.getCdGroupCd());
				cdSearch.setSearchType("GROUP");
				int exists1 = (int) dao.selectOne("ccs.code.selectCodeCheck", cdSearch);
				if (exists1 > 0) {
					throw new ServiceException("ccs.codegrp.save.cdgroupcd");
				}

				// 코드그룹명이 존재하는지
				CcsCodeSearch nameSearch = new CcsCodeSearch();
				nameSearch.setName(grid.getName());
				nameSearch.setSearchType("GROUP");
				int exists2 = (int) dao.selectOne("ccs.code.selectCodeCheck", nameSearch);
				if (exists2 > 0) {
					throw new ServiceException("ccs.codegrp.save.name");
				}
				dao.insertOneTable(grid);
			} else {
				grid.setUpdId(SessionUtil.getLoginId());
				dao.updateOneTable(grid);
			}
		}
	}

	public int deleteCode(CcsCodegroup code) throws Exception {
		return dao.delete("ccs.code.deleteCode", code);
	}

	/**
	 * 코드 그룹 삭제
	 * 
	 * @Method Name : deleteCodeGroup
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void deleteCodeGroup(List<CcsCodegroup> reserve) throws Exception {

		for (CcsCodegroup r : reserve) {
			deleteCode(r);
			dao.deleteOneTable(r);
		}
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
	public List<CcsCode> selectCodeList(CcsCodeSearch codeSearch) {
		return (List<CcsCode>) dao.selectList("ccs.code.getCodeList", codeSearch);
	}

	/**
	 * 코드 등록
	 * 
	 * @Method Name : saveCode
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveCode(List<CcsCode> reserve) throws Exception {

		for (CcsCode grid : reserve) {
			grid.setUpdId(SessionUtil.getLoginId());
			if (StringUtils.isEmpty(grid.getInsDt())) {
				// 코드번호가 존재하는지
				CcsCodeSearch cdSearch = new CcsCodeSearch();
				cdSearch.setCd(grid.getCd());
				cdSearch.setSearchType("CODE");
				int exists1 = (int) dao.selectOne("ccs.code.selectCodeCheck", cdSearch);
				if (exists1 > 0) {
					throw new ServiceException("ccs.code.save.cd");
				}

				/*// 코드명이 존재하는지
				CcsCodeSearch nameSearch = new CcsCodeSearch();
				nameSearch.setName(grid.getName());
				nameSearch.setSearchType("CODE");
				int exists2 = (int) dao.selectOne("ccs.code.selectCodeCheck", nameSearch);
				if (exists2 > 0) {
					throw new ServiceException("ccs.code.save.name");
				}*/
				dao.insertOneTable(grid);
			} else {
				grid.setUpdId(SessionUtil.getLoginId());
				dao.updateOneTable(grid);
			}
		}
	}

	/**
	 * 코드 삭제
	 * 
	 * @Method Name : deleteCode
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void deleteCode(List<CcsCode> reserve) throws Exception {

		for (CcsCode r : reserve) {
			dao.deleteOneTable(r);
		}
	}
	
	/**
	 * 프론트 header Code 목록 조회
	 * @Method Name : getCodeList
	 * @author : emily
	 * @date : 2016. 7. 26.
	 * @description : 
	 *	전시 header 월령코드 목록 조회
	 * @param search
	 * @return
	 */
	@Cacheable(cacheName = "getFrontCodeList-cache")
	public CcsCodegroup getFrontCodeList(CcsCodeSearch search){
		return (CcsCodegroup) dao.selectOne("ccs.code.getFrontCodeList", search);
	}

	/**
	 * 
	 * @Method Name : getCode
	 * @author : dennis
	 * @date : 2016. 10. 17.
	 * @description : 코드 조회
	 *
	 * @param ccsCode
	 * @return
	 */
	public CcsCode getCode(CcsCode ccsCode) {
		return (CcsCode) dao.selectOne("ccs.code.getCode", ccsCode);
	}
}
