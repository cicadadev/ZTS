package gcp.admin.controller.rest.pms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.PresentService;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseSearchCondition;

@RestController("presentController")
@RequestMapping("api/pms/present")
public class PresentController {

	@Autowired
	private PresentService presentService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> getProductList(@RequestBody PmsProductSearch search) {
		List<PmsProduct> list = presentService.getPresentList(search);
		
		return list;
	}
	
	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getPresentListExcel(@RequestBody PmsProductSearch search) {

		List<PmsProduct> list = new ArrayList<PmsProduct>();
		search.setStoreId(SessionUtil.getStoreId());
		list = presentService.getPresentList(search);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("productId", list.get(i).getProductId());
			map.put("name", list.get(i).getName());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) search, dataList);

		return msg;
	}

	@RequestMapping(value = "/popup/save", method = { RequestMethod.GET, RequestMethod.POST })
	public String savePresent(@RequestBody PmsProduct present) throws Exception {
		present.setStoreId(SessionUtil.getStoreId());
		present.setSearchExcYn("N");

		if ("I".equals(present.getSaveType())) {
			present.setProductId(presentService.insertPresent(present));
		} else if ("U".equals(present.getSaveType())) {
			presentService.updatePresent(present);
		}

		return present.getProductId();
	}

	@RequestMapping(value = "/popup/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public PmsProduct getPresentDetail(@RequestBody PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return presentService.getPresentDetail(search);
	}

}
