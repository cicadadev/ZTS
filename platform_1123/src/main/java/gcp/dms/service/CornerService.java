package gcp.dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.base.BaseDmsDisplay;
import gcp.dms.model.base.BaseDmsDisplayitem;
import gcp.dms.model.search.DmsDisplaySearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class CornerService extends BaseService {
	

	/**
	 * [전시코너] 삭제시 체크
	 * 
	 * @Method Name : checkChildCorner
	 * @author : eddie
	 * @date : 2016. 5. 11.
	 * @description : 하위 전시코너가 존재하는지, 매핑 템플릿이 존재하는지, 매핑 컨텐츠가 존재하는지 체크
	 *
	 * @param displayId
	 * @throws ServiceException
	 */
	public void checkChildCorner(String displayId) throws ServiceException {

		// 삭제전 체크
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setDisplayId(displayId);
		search.setStoreId(SessionUtil.getStoreId());

		//하위 전시가 존재하는지
		String exists1 = (String) dao.selectOne("dms.corner.checkChildCornerExist", search);
		if (CommonUtil.isNotEmpty(exists1)) {
			throw new ServiceException("dms.display.delete.child");
		}

		// 코너의 템플릿이 존재하는지
		String exists2 = (String) dao.selectOne("dms.corner.checkDisplayTemplateExist", search);
		if (CommonUtil.isNotEmpty(exists2)) {
			throw new ServiceException("dms.display.delete.template");
		}

		// 매핑 컨텐트가 존재하는지
		String exists3 = (String) dao.selectOne("dms.corner.checkCornerContentExist", search);
		if (CommonUtil.isNotEmpty(exists3)) {
			throw new ServiceException("dms.display.delete.content");
		}
	}

	/**
	 * [전시코너]leafYn 을 Y로 수정하는데 하위가 존재하는 경우 체크
	 * 
	 * @Method Name : checkUdateLeaf
	 * @author : eddie
	 * @date : 2016. 5. 11.
	 * @description :
	 *
	 * @param displayId
	 * @throws ServiceException
	 */
	public void checkUdateLeafY(String displayId) throws ServiceException {
		// 삭제전 체크
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setDisplayId(displayId);
		search.setStoreId(SessionUtil.getStoreId());

		//하위 전시가 존재하는지
		String exists1 = (String) dao.selectOne("dms.corner.checkChildCornerExist", search);
		if (CommonUtil.isNotEmpty(exists1)) {
			throw new ServiceException("dms.display.update.child.leaf");
		}
	}

	/**
	 * [전시코너]leafYn 을 N로 수정하는데 컨텐츠가 존재하는경우 체크
	 * 
	 * @Method Name : checkUdateLeaf
	 * @author : eddie
	 * @date : 2016. 5. 11.
	 * @description :
	 *
	 * @param displayId
	 * @throws ServiceException
	 */
	public void checkUdateLeafN(String displayId) throws ServiceException {
		// 삭제전 체크
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setDisplayId(displayId);
		search.setStoreId(SessionUtil.getStoreId());

		// 매핑 컨텐트가 존재하는지
		String exists = (String) dao.selectOne("dms.corner.checkCornerContentExist", search);
		if (CommonUtil.isNotEmpty(exists)) {
			throw new ServiceException("dms.display.update.leaf");
		}
	}

	/**
	 * [전시 코너] 아이템 저장
	 * 
	 * @Method Name : saveCornerItems
	 * @author : eddie
	 * @date : 2016. 5. 13.
	 * @description :
	 *
	 * @param items
	 * @throws Exception
	 */
	public void saveCornerItems(List<DmsDisplayitem> items) throws Exception {
		//수정
		for (DmsDisplayitem item : items) {
			
			// 이미지 temp to real
			if (CommonUtil.isNotEmpty(item.getImg1())) {
				item.setImg1(FileUploadUtil.moveTempToReal(item.getImg1()));
			}
			if (CommonUtil.isNotEmpty(item.getImg2())) {
				item.setImg2(FileUploadUtil.moveTempToReal(item.getImg2()));
			}
			
			item.setStoreId(SessionUtil.getStoreId());
			
			if ("I".equals(item.getSaveType())) {
				dao.insertOneTable(item);
			} else {
				dao.updateOneTable(item);
			}
		}
	}

	/**
	 * 코너 아이템 단건 수정
	 * 
	 * @Method Name : saveCornerItem
	 * @author : eddie
	 * @date : 2016. 8. 12.
	 * @description :
	 *
	 * @param item
	 * @throws Exception
	 */
	public void saveCornerItem(DmsDisplayitem item) throws Exception {

		// 이미지 temp to real
		if (CommonUtil.isNotEmpty(item.getImg1())) {
			item.setImg1(FileUploadUtil.moveTempToReal(item.getImg1()));
		}
		if (CommonUtil.isNotEmpty(item.getImg2())) {
			item.setImg2(FileUploadUtil.moveTempToReal(item.getImg2()));
		}

		item.setStoreId(SessionUtil.getStoreId());

		if (CommonUtil.isEmpty(item.getDisplayItemNo())) {
			dao.insertOneTable(item);
		} else {
			dao.updateOneTable(item);
		}
	}

	/**
	 * [전시 코너] 아이템 삭제
	 * 
	 * @Method Name : deleteCornerItems
	 * @author : Administrator
	 * @date : 2016. 5. 20.
	 * @description :
	 *
	 * @param items
	 * @throws Exception
	 */
	public void deleteCornerItems(List<BaseDmsDisplayitem> items) throws Exception {
		for (BaseDmsDisplayitem item : items) {
			item.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(item);
		}
	}
	/**
	 * 전시 코너 트리 목록 조회
	 * @Method Name : displayTree
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplay> displayTree(DmsDisplaySearch search) {
		return (List<DmsDisplay>) dao.selectList("dms.corner.getCornerTreeList", search);
	}
	/**
	 * 전시코너 등록
	 * @Method Name : createCorner
	 * @author : Administrator
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param display
	 * @return
	 */
	public String createCorner(BaseDmsDisplay display) throws Exception {
		dao.insert("dms.corner.insertCorner", display);
		return display.getDisplayId();
	}

	/**
	 * 전시코너 상품 타입 아이템 목록 조회
	 * @Method Name : getCornerItemProduct
	 * @author : Administrator
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplayitem> getCornerItemProduct(DmsDisplaySearch search) throws Exception {
		return (List<DmsDisplayitem>) dao.selectList("dms.corner.getCornerItemProduct", search);
	}
	/**
	 * 코너 기획전 타입 아이템 목록 조회
	 * @Method Name : getCornerItemExhibit
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplayitem> getCornerItemExhibit(DmsDisplaySearch search) throws Exception {
		return (List<DmsDisplayitem>) dao.selectList("dms.corner.getCornerItemExhibit", search);
	}
	
	/**
	 * 코너  아이템 목록 조회
	 * @Method Name : getCornerItemExhibit
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<DmsDisplayitem> getCornerItems(DmsDisplaySearch search) throws Exception {
		return (List<DmsDisplayitem>) dao.selectList("dms.corner.getCornerItems", search);
	}

	/**
	 * 코너 아이템 조회
	 * 
	 * @Method Name : getDisplayitem
	 * @author : stella
	 * @date : 2016. 6. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public DmsDisplayitem getDisplayitem(DmsDisplayitem item) throws Exception {
		return (DmsDisplayitem) dao.selectOneTable(item);
	}

	/**
	 * 상위 코너 번호로 하위 코너 목록 조회 ( 아이템 목록도 함께 조회 )
	 * 
	 * @Method Name : getChildCornerList
	 * @author : eddie
	 * @date : 2016. 10. 1.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<DmsDisplay> getChildCornerList(DmsDisplaySearch search) {

		List<DmsDisplay> childs = (List<DmsDisplay>) dao.selectList("dms.corner.getChildCornerList", search);

		if (childs != null && childs.size() > 0) {
			childs = getDisplayCornerItems(childs, search);
		}

		return childs;
	}

	/**
	 * 코너에 해당하는 하위 아이템 조회( 복수 코너 조회)
	 * 
	 * @Method Name : getDisplayCornerItems
	 * @author : eddie
	 * @date : 2016. 10. 1.
	 * @description :
	 *
	 * @param cornerList
	 * @param itemDivId displayItemDivCd 가 common 이 아닐경우(기획전 번호, 전시 카테고리 번호, 브랜드 번호)
	 * @return
	 */

	//public List<DmsDisplay> getDisplayCornerItems(List<DmsDisplay> cornerList, String itemDivId, String sort,String regularDeliveryYn) {
	public List<DmsDisplay> getDisplayCornerItems(List<DmsDisplay> cornerList, DmsDisplaySearch param) {

		DmsDisplaySearch search = new DmsDisplaySearch();

		search.setStoreId(SessionUtil.getStoreId());

		if (cornerList != null && cornerList.size() > 0) {
			for (DmsDisplay disp : cornerList) {
				//setDisplayCornerItems(disp, itemDivId, sort, regularDeliveryYn);
				setDisplayCornerItems(disp, param);
			}
		}
		return cornerList;

	}

	/**
	 * 코너에 해당하는 아템 목록 조회
	 * 
	 * @Method Name : getDisplayCornerItems
	 * @author : emily
	 * @date : 2016. 10. 1.
	 * @description : display 의 dmsDisplayitems에 아이템 조회하여 세팅
	 * @param display
	 * @param itemDivId
	 */
	//public void setDisplayCornerItems(DmsDisplay display, String itemDivId, String sort, String regularDeliveryYn) {
	public void setDisplayCornerItems(DmsDisplay display, DmsDisplaySearch param) {
	
		/*************************************************************************
		 * 파라미터 설정
		 *************************************************************************/
		DmsDisplaySearch search = new DmsDisplaySearch();

		search.setStoreId(SessionUtil.getStoreId());
		search.setDisplayId(display.getDisplayId());
		search.setSort(param.getSort());
		search.setRegularDeliveryYn(param.getRegularDeliveryYn());
		search.setDisplayItemDivId(param.getDisplayItemDivId());// 기획전 번호, 전시 카테고리 번호, 브랜드 번호..
		search.setCornerFirstRow(param.getCornerFirstRow()); //코너의 페이징 있는 경우 
		search.setCornerLastRow(param.getCornerLastRow());
		search.setPagingYn(param.getPagingYn());
//		search.setDisplayItemTypeCd(displayItemTypeCd);

		List<DmsDisplayitem> itemList = new ArrayList<DmsDisplayitem>();

		/*************************************************************************
		 * 아이템 목록 조회
		 *************************************************************************/
		if (CommonUtil.isNotEmpty(display.getDisplayItemTypeCd())) {

			if (CommonUtil.equals(BaseConstants.DISPLAY_ITEM_TYPE_CD_PRODUCT, display.getDisplayItemTypeCd())) {
				//상품
				itemList = (List<DmsDisplayitem>) dao.selectList("dms.corner.getFrnProductItemList", search);
			} else if (CommonUtil.equals(BaseConstants.DISPLAY_ITEM_TYPE_CD_EXHIBIT, display.getDisplayItemTypeCd())) {
				//기획전
				search.setPagingYn(BaseConstants.YN_N);
				search.setDisplayYn("true");
				search.setDateLimit("true");
				search.setExhibitStateCd(BaseConstants.EXHIBIT_STATE_CD_RUN);
				itemList = (List<DmsDisplayitem>) dao.selectList("dms.corner.getCornerItemExhibit", search);
			} else {
				//이미지,HTML
				itemList = (List<DmsDisplayitem>) dao.selectList("dms.corner.getFrnCommonItemList", search);
			}

			if (itemList != null && itemList.size() > 0) {
				display.setDmsDisplayitems(itemList);
				display.setTotalCount(itemList.size());
			}
		}
	}

	/**
	 * 프론트 코너와 아이템목록 조회
	 * @Method Name : getCommonCornerList
	 * @author : emily
	 * @date : 2016. 8. 4.
	 * @description : 
	 * 
	 * @param seach (displayTypeCd, displayId)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Cacheable(cacheName = "getCommonCornerList-cache")
	public List<DmsDisplay> getCommonCornerList(DmsDisplaySearch search) {
		
		/*************************************************************************
		 * 코너정보 조회
		 *************************************************************************/	
		List<DmsDisplay> cornerList = (List<DmsDisplay>) dao.selectList("dms.corner.getFrnCornerList", search);
		
		if(cornerList != null && cornerList.size() > 0){

			//getDisplayCornerItems(cornerList, search.getDisplayItemDivId(), search.getSort(), search.getRegularDeliveryYn());
			getDisplayCornerItems(cornerList, search);
		}
		
		return cornerList;
	}

	@Cacheable(cacheName = "getMainCornerMap-cache")
	public Map<String, DmsDisplay> getMainCornerMap(DmsDisplaySearch search) {

		List<DmsDisplay> returnCornerList = new ArrayList<DmsDisplay>();
		Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();

		//메인화면의 코너의 상위코너목록을 조회해온다.
		search.setDisplayId(Config.getString("corner.main.banner"));
		List<DmsDisplay> childList = getChildCornerList(search);

		//화면에 보여줄 코너 목록을 조회해온다.
		for (DmsDisplay dmsDisplay : childList) {
//			List<String> pcCorner = new ArrayList();
//
//			if (childList.contains(dmsDisplay.getDisplayId())) {
//				continue;
//			}

			DmsDisplaySearch cornerSearch = new DmsDisplaySearch();
			cornerSearch.setDisplayId(dmsDisplay.getDisplayId());
			cornerSearch.setStoreId(SessionUtil.getStoreId());
			List<DmsDisplay> cornerList = getChildCornerList(cornerSearch);

			if (cornerList != null && cornerList.size() > 0) {
				returnCornerList.addAll(cornerList);
			} else {
				returnCornerList.add(dmsDisplay);
			}
		}

		//코너를 MAP으로 return한다.
		for (DmsDisplay maincorner : returnCornerList) {
			int maxPage = maincorner.getTotalCount() / 4;
			if (maincorner.getTotalCount() % 4 != 0) {
				maxPage++;
			}
			maincorner.setPageSize(maxPage);
			returnMap.put(maincorner.getDisplayId(), maincorner);
		}
		return returnMap;
	}
}
