package gcp.admin.controller.rest.dms;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.base.BaseDmsDisplay;
import gcp.dms.model.base.BaseDmsDisplayitem;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CornerService;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/dms/corner")
public class CornerController {
	@Autowired
	private CornerService	cornerService;


	/**
	 * 전시코너 트리 목록 조회
	 * @Method Name : displayTree
	 * @author : eddie
	 * @date : 2016. 5. 3.
	 * @description : 
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<DmsDisplay> displayTree(DmsDisplaySearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return cornerService.displayTree(search);
	}

	/**
	 * 전시 코너 상세 조회
	 * 
	 * @Method Name : getCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param displayId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{displayId}", method = RequestMethod.GET)
	public DmsDisplay getCorner(@PathVariable("displayId") String displayId) throws Exception {

		DmsDisplay display = new DmsDisplay();
		display.setDisplayId(displayId);
		display.setStoreId(SessionUtil.getStoreId());
		return (DmsDisplay) cornerService.selectOneTable(display);
	}


	/**
	 * 전시 코너 삭제전 체크
	 * @Method Name : checkDeleteCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param displayId
	 * @return
	 */
	@RequestMapping(value = "/{displayId}/check", method = { RequestMethod.GET })
	public BaseEntity checkDeleteCorner(@PathVariable("displayId") String displayId) {

		BaseEntity base = new BaseEntity();

		try {

			cornerService.checkChildCorner(displayId);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}
		return base;
	}

	/**
	 * 전시 코너 삭제
	 * @Method Name : deleteCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param displayId
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public void deleteCorner(@RequestBody BaseDmsDisplay items) throws Exception {
		
		
//		@PathVariable("displayId") String displayId
		
		
//		BaseDmsDisplay dis = new BaseDmsDisplay();
//		dis.setDisplayId(items.getDisplayId());
		cornerService.deleteOneTable(items);
	}

	/**
	 * 전시 코너 등록
	 * 
	 * @Method Name : createCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param display
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST }, produces = "text/plain")
	public String createCorner(@RequestBody BaseDmsDisplay display) throws Exception {

		display.setStoreId(SessionUtil.getStoreId());
		display.setUseYn("Y");

		return cornerService.createCorner(display);
	}

	/**
	 * 전시 코너 수정
	 * 
	 * @Method Name : updateCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description :
	 *
	 * @param displayId
	 * @param dis
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{displayId}", method = { RequestMethod.PUT })
	public BaseEntity updateCorner(@PathVariable("displayId") String displayId, @RequestBody BaseDmsDisplay dis)
			throws Exception {
		dis.setStoreId(SessionUtil.getStoreId());
		BaseEntity base = new BaseEntity();
		try {
			//
			if ("N".equals(dis.getLeafYn())) {
				cornerService.checkUdateLeafN(displayId);
			}

			if ("Y".equals(dis.getLeafYn())) {
				cornerService.checkUdateLeafY(displayId);
			}

			//수정
			cornerService.updateOneTable(dis);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}
		return base;
	}

	/**
	 * 전시코너 아이템 저장(그리드저장)
	 * 
	 * @Method Name : saveCornerItems
	 * @author : eddie
	 * @date : 2016. 5. 13.
	 * @description :
	 *
	 * @param items
	 * @throws Exception
	 */
	@RequestMapping(value = "/items/**/save", method = { RequestMethod.POST })
	public void saveCornerItems(@RequestBody List<DmsDisplayitem> items) throws Exception {
		cornerService.saveCornerItems(items);
		
	}

	/**
	 * 전시코너 아이템 저장(단건저장)
	 * 
	 * @Method Name : saveCornerItem
	 * @author : eddie
	 * @date : 2016. 5. 13.
	 * @description :
	 *
	 * @param item
	 * @throws Exception
	 */
	@RequestMapping(value = "/item", method = { RequestMethod.POST })
	public void saveCornerItem(@RequestBody DmsDisplayitem item) throws Exception {
		cornerService.saveCornerItem(item);
	}

	/**
	 * 전시코너 상품 타입 아이템 목록 조회
	 * 
	 * @Method Name : getCornerItems
	 * @author : eddie
	 * @date : 2016. 5. 13.
	 * @description :
	 *
	 * @param displayId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/items/product/list", method = { RequestMethod.POST })
	public List<DmsDisplayitem> getCornerItemProduct(@RequestBody DmsDisplaySearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return cornerService.getCornerItemProduct(search);
	}

	/**
	 * 전시코너 기획전 타입 아이템 조회
	 * 
	 * @Method Name : getCornerItemExhibit
	 * @author : eddie
	 * @date : 2016. 5. 20.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/items/exhibit/list", method = { RequestMethod.POST })
	public List<DmsDisplayitem> getCornerItemExhibit(@RequestBody DmsDisplaySearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return cornerService.getCornerItemExhibit(search);
	}
	
	/**
	 * 전시코너 아이템 조회
	 * 
	 * @Method Name : getCornerItems
	 * @author : eddie
	 * @date : 2016. 5. 20.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/items/**/list", method = { RequestMethod.POST })
	public List<DmsDisplayitem> getCornerItems(@RequestBody DmsDisplaySearch search) throws Exception{
		search.setStoreId(SessionUtil.getStoreId());
		List<DmsDisplayitem> list = cornerService.getCornerItems(search);
		return list;
	}

	/**
	 * 전시코너 아이템 삭제
	 * 
	 * @Method Name : getCornerItemExhibit
	 * @author : eddie
	 * @date : 2016. 5. 20.
	 * @description :
	 *
	 * @param items
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/items/**/delete", method = { RequestMethod.POST })
	public void getCornerItemExhibit(@RequestBody List<BaseDmsDisplayitem> items) throws Exception {
		cornerService.deleteCornerItems(items);
	}
	
	/**
	 * 전시코너 아이템 상세조회
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
	@RequestMapping(value = "/{displayId}/item/{displayItemNo}", method = { RequestMethod.GET })
	public DmsDisplayitem getDisplayitem(@PathVariable("displayId") String displayId,
			@PathVariable("displayItemNo") String displayItemNo) throws Exception {

		DmsDisplayitem search = new DmsDisplayitem();
		search.setStoreId(SessionUtil.getStoreId());
		search.setDisplayId(displayId);
		search.setDisplayItemNo(new BigDecimal(Long.parseLong(displayItemNo)));
		return cornerService.getDisplayitem(search);
	}

}
