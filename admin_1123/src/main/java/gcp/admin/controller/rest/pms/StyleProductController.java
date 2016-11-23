package gcp.admin.controller.rest.pms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.model.PmsStyleproduct;
import gcp.pms.model.search.PmsStyleProductSearch;
import gcp.pms.service.StyleProductService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/pms/styleShop")
public class StyleProductController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private StyleProductService	styleProductService;

	/**
	 * 
	 * @Method Name : getStyleProductList
	 * @author : allen
	 * @date : 2016. 8. 19.
	 * @description : 스타일상품 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<PmsStyleproduct> getStyleProductList(@RequestBody PmsStyleProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return styleProductService.getStyleProductList(search);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveStyleProduct(@RequestBody PmsStyleproduct pmsStyleproduct) throws Exception {
		pmsStyleproduct.setStoreId(SessionUtil.getStoreId());
		styleProductService.saveStyleProduct(pmsStyleproduct);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteStyleProduct(@RequestBody List<PmsStyleproduct> pmsStyleproductList) throws Exception {
		styleProductService.deleteStyleProduct(pmsStyleproductList);
	}

	@RequestMapping(value = "/styleProduct/{styleProductNo}", method = RequestMethod.POST)
	public PmsStyleproduct getStyleProductList(@PathVariable("styleProductNo") String styleProductNo) {
		return styleProductService.getStyleProductDetail(styleProductNo);
	}


}
