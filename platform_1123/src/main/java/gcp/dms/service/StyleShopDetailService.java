package gcp.dms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.dms.model.search.DmsStyleShopDetailSearch;
import gcp.mms.model.MmsStyle;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class StyleShopDetailService extends BaseService {
	
	private static final Logger logger = LoggerFactory.getLogger(StyleShopDetailService.class);

	/**
	 * 
	 * @Method Name : getStyleShopDetailLst
	 * @author : allen
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<MmsStyle> getStyleShopDetailLst(DmsStyleShopDetailSearch search) throws Exception {

		// 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(search.getStyleNo())) {

			String temp = CommonUtil.isNotEmpty(search.getStyleNo()) ? search.getStyleNo() : "";

			search = new DmsStyleShopDetailSearch();
			if (!"".equals(temp)) {
				search.setStyleNo(temp);
			}
		}

		return (List<MmsStyle>) dao.selectList("dms.styleShopDetail.getStyleShopDetailList", search);
	}

	public void updateStyleDisplayYn(List<MmsStyle> styleList) {
		for (MmsStyle mmsStyle : styleList) {
			mmsStyle.setUpdId(SessionUtil.getLoginId());
			dao.update("dms.styleShopDetail.updateStyleDisplayYn", mmsStyle);
		}
	}
}
