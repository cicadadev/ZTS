package gcp.admin.controller.rest.dms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.search.DmsStyleShopDetailSearch;
import gcp.dms.service.StyleShopDetailService;
import gcp.mms.model.MmsStyle;

@RestController
@RequestMapping("api/dms/styleShopDetail")
public class StyleShopDetailController {

	@Autowired
	StyleShopDetailService styleShopDetailService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public List<MmsStyle> getExhibitList(@RequestBody DmsStyleShopDetailSearch search) throws Exception {
		return styleShopDetailService.getStyleShopDetailLst(search);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public void updateStyleDisplayYn(@RequestBody List<MmsStyle> styleList) throws Exception {
		styleShopDetailService.updateStyleDisplayYn(styleList);
	}
}
