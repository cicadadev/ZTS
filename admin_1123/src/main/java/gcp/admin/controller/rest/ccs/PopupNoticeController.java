package gcp.admin.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.search.CcsPopupNoticeSearch;
import gcp.ccs.service.PopupNoticeService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/popupnotice")
public class PopupNoticeController {

	@Autowired
	private PopupNoticeService popupService;

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsPopup> selectList(@RequestBody CcsPopupNoticeSearch ccsPopupNoticeSearch) throws Exception {
		ccsPopupNoticeSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsPopup> popupList = popupService.selectList(ccsPopupNoticeSearch);

		if (popupList != null && popupList.size() > 0) {
			for (CcsPopup ccsPopupInfo : popupList) {
				if (ccsPopupInfo.getPcDisplayYn().equals("Y")) {
					if (ccsPopupInfo.getMobileDisplayYn().equals("Y")) {
						ccsPopupInfo.setChannelName("PC, MOBILE");
					} else {
						ccsPopupInfo.setChannelName("PC");
					}
				} else {
					if (ccsPopupInfo.getMobileDisplayYn().equals("Y")) {
						ccsPopupInfo.setChannelName("MOBILE");
					}
				}
			}
		}

		return popupList;
	}

	@RequestMapping(method = RequestMethod.GET)
	public CcsPopup selectOne(@RequestParam("popupNo") String popupNo) throws Exception {
		CcsPopup popup = new CcsPopup();
		popup.setPopupNo(new BigDecimal(popupNo));
		popup.setStoreId(SessionUtil.getStoreId());
		return popupService.selectOne(popup);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody CcsPopup popup) throws Exception {
		popup.setStoreId(SessionUtil.getStoreId());
		popupService.insert(popup);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody CcsPopup popup) throws Exception {
		popup.setStoreId(SessionUtil.getStoreId());
		popup.setUpdId(SessionUtil.getLoginId());
		popupService.update(popup);
	}

	/**
	 * 팝업 공지 삭제
	 * 
	 * @Method Name : deletePopup
	 * @author : roy
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public void deletePopup(@RequestBody List<CcsPopup> reserves) throws Exception {
		popupService.deleteGrid(reserves);
	}
}
