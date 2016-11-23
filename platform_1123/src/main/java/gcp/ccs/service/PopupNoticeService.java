package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.CcsPopupurl;
import gcp.ccs.model.search.CcsPopupNoticeSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class PopupNoticeService extends BaseService {

	/**
	 * 팝업 리스트 조회
	 * 
	 * @Method Name : selectList
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param ccsPopupNoticeSearch
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public List<CcsPopup> selectList(CcsPopupNoticeSearch ccsPopupNoticeSearch) {
		return (List<CcsPopup>) dao.selectList("ccs.popupnotice.list", ccsPopupNoticeSearch);
	}

	/**
	 * 팝업 상세 조회
	 * 
	 * @Method Name : selectOne
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param popup
	 * @throws Exception
	 */

	public CcsPopup selectOne(CcsPopup popup) throws Exception {
		CcsPopup popupInfo = (CcsPopup) dao.selectOne("ccs.popupnotice.getPopupDetail", popup);

		return popupInfo;
	}

	/**
	 * 팝업 등록
	 * 
	 * @Method Name : insert
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param popup
	 * @throws Exception
	 */
	public void insert(CcsPopup popup) throws Exception {
		dao.insertOneTable(popup);
		BigDecimal popupNoBd = popup.getPopupNo();
		popup.setStoreId(SessionUtil.getStoreId());

		if (popup.getPopupTypeCd().equals(BaseConstants.POPUP_TYPE_CD_PARTNER)) {

		} else {
			// PC 형태 URLs 추가
			if (popup.getPcDisplayYn().equals("Y")) {
				for (int i = 0; i < popup.getCcsPcUrls().length; i++) {
					CcsPopupurl poUrl = new CcsPopupurl();
					if (CommonUtil.isNotEmpty(popup.getCcsPcUrls()[i])) {
						poUrl.setStoreId(SessionUtil.getStoreId());
						poUrl.setPopupNo(popupNoBd);
						poUrl.setPopupUrl(popup.getCcsPcUrls()[i]);
						poUrl.setDeviceChannelTypeCd("DEVICE_CHANNEL_TYPE_CD.PC");

						dao.insertOneTable(poUrl);
					}
				}
			}

			// MOBILE 형태 URLs 추가
			if (popup.getMobileDisplayYn().equals("Y")) {
				for (int i = 0; i < popup.getCcsMoUrls().length; i++) {
					CcsPopupurl moUrl = new CcsPopupurl();
					if (CommonUtil.isNotEmpty(popup.getCcsMoUrls()[i])) {
						moUrl.setStoreId(SessionUtil.getStoreId());
						moUrl.setPopupNo(popupNoBd);
						moUrl.setPopupUrl(popup.getCcsMoUrls()[i]);
						moUrl.setDeviceChannelTypeCd("DEVICE_CHANNEL_TYPE_CD.MW");

						dao.insertOneTable(moUrl);
					}
				}
			}
		}
	}

	/**
	 * 팝업 수정
	 * 
	 * @Method Name : update
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param popup
	 * @throws Exception
	 */

	public void update(CcsPopup popup) throws Exception {
		popup.setStoreId(SessionUtil.getStoreId());

		if (popup.getPopupTypeCd().equals(BaseConstants.POPUP_TYPE_CD_PARTNER)) {

		} else {

			// PopupUrl 삭제
			dao.delete("ccs.popupnotice.deletePopupUrl", popup);

			// PC 형태 URLs 추가
			if (popup.getPcDisplayYn().equals("Y")) {
				for (int i = 0; i < popup.getCcsPcUrls().length; i++) {
					CcsPopupurl poUrl = new CcsPopupurl();
					if (CommonUtil.isNotEmpty(popup.getCcsPcUrls()[i])) {
						poUrl.setStoreId(SessionUtil.getStoreId());
					}
					poUrl.setPopupNo(popup.getPopupNo());
					poUrl.setPopupUrl(popup.getCcsPcUrls()[i]);
					poUrl.setDeviceChannelTypeCd("DEVICE_CHANNEL_TYPE_CD.PC");

					dao.insertOneTable(poUrl);
				}
			}

			// MOBILE 형태 URLs 추가
			if (popup.getMobileDisplayYn().equals("Y")) {
				for (int i = 0; i < popup.getCcsMoUrls().length; i++) {
					CcsPopupurl moUrl = new CcsPopupurl();
					if (CommonUtil.isNotEmpty(popup.getCcsMoUrls()[i])) {
						moUrl.setStoreId(SessionUtil.getStoreId());
					}
					moUrl.setPopupNo(popup.getPopupNo());
					moUrl.setPopupUrl(popup.getCcsMoUrls()[i]);
					moUrl.setDeviceChannelTypeCd("DEVICE_CHANNEL_TYPE_CD.MW");

					dao.insertOneTable(moUrl);
				}
			}
		}
		popup.setUpdId(SessionUtil.getLoginId());
		popup.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(popup);
	}

	/**
	 * 그리드 팝업 삭제
	 * 
	 * @Method Name : deleteGrid
	 * @author : roy
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void deleteGrid(List<CcsPopup> reserve) throws Exception {
		for (CcsPopup r : reserve) {
			r.setStoreId(SessionUtil.getStoreId());
			delete(r);
		}
	}

	/**
	 * 팝업 삭제
	 * 
	 * @Method Name : delete
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description :
	 *
	 * @param popup
	 * @throws Exception
	 */
	public void delete(CcsPopup popup) throws Exception {
		popup.setStoreId(SessionUtil.getStoreId());
		// PopupUrl 삭제
		dao.delete("ccs.popupnotice.deletePopupUrl", popup);

		dao.deleteOneTable(popup);
	}

	/**
	 * 팝업 상세 조회 (FO)
	 * 
	 * @Method Name : selectOneFo
	 * @author : roy
	 * @date : 2016. 9. 10.
	 * @description :
	 *
	 * @param popup
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public List<CcsPopup> selectOneFo(CcsPopup popup) {
		List<CcsPopup> popupInfo = (List<CcsPopup>) dao.selectList("ccs.popupnotice.getPopupFoList", popup);

		return popupInfo;
	}
}
