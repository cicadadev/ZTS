package gcp.admin.controller.rest.dms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsOffshop;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsExhibitcoupon;
import gcp.dms.model.DmsExhibitgroup;
import gcp.dms.model.DmsExhibitmainproduct;
import gcp.dms.model.DmsExhibitproduct;
import gcp.dms.model.base.BaseDmsExhibit;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.ExhibitService;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseSearchCondition;

@RestController
@RequestMapping("api/dms/exhibit")
public class ExhibitController {


	@Autowired
	private ExhibitService	exhibitService;

	/**
	 * 
	 * @Method Name : getExhibitList
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 리스트 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public List<DmsExhibit> getExhibitList(@RequestBody DmsExhibitSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return exhibitService.getExhibitList(search);
	}

	/**
	 * @Method Name : getExhibitId
	 * @author : ian
	 * @date : 2016. 4. 25.
	 * @description : 기획전 아이디 MAX +1
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getExhibitId", method = RequestMethod.GET)
	@ResponseBody
	public BaseDmsExhibit getExhibitId() throws Exception {
		return exhibitService.getExhibitId();
	}

	/**
	 * 
	 * @Method Name : deleteExhibit
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 삭제
	 *
	 * @param request
	 * @param exhibitIds
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteExhibit(HttpServletRequest request, @RequestBody List<DmsExhibit> dmsExhibitList) throws Exception {
		exhibitService.deleteExhibit(dmsExhibitList);
	}

	/**
	 * 
	 * @Method Name : updateExhibit
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 업데이트
	 *
	 * @param request
	 * @param exhibitIds
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void updateExhibit(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit) throws Exception {
		exhibitService.updateExhibit(dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : changeState
	 * @author : allen
	 * @date : 2016. 8. 1.
	 * @description : 기획전 상태 변경
	 *
	 * @param request
	 * @param dmsExhibit
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateExhibitStatus", method = RequestMethod.POST)
	public void changeState(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit) throws Exception {
		exhibitService.updateExhibitStatus(dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : insertExhibit
	 * @author : allen
	 * @date : 2016. 6. 7.
	 * @description : 기획전 저장
	 *
	 * @param request
	 * @param dmsExhibit
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertExhibit(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit) throws Exception {
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		String exhibitId = exhibitService.insertExhibit(dmsExhibit);

		return exhibitId;
	}

	/**
	 * 
	 * @Method Name : saveExhibit
	 * @author : allen
	 * @date : 2016. 6. 7.
	 * @description : 그리드 기획전 저장
	 *
	 * @param request
	 * @param dmsExhibit
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveExhibit(HttpServletRequest request, @RequestBody List<DmsExhibit> dmsExhibitList) throws Exception {
		exhibitService.saveExhibit(dmsExhibitList);
	}

	/**
	 * 
	 * @Method Name :
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 상세
	 *
	 * @param request
	 * @param exhibitId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{exhibitId}", method = RequestMethod.POST)
	public BaseDmsExhibit getExhibitDetail(HttpServletRequest request, @PathVariable("exhibitId") String exhibitId)
			throws Exception {
		BaseDmsExhibit dmsExhibit = new BaseDmsExhibit();
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		dmsExhibit.setExhibitId(exhibitId);
		dmsExhibit = exhibitService.getExhibitDetail(dmsExhibit);

		return dmsExhibit;
	}

	/**
	 * 
	 * @Method Name : getDivTitleList
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 기획전 구분 타이틀 목록 조회
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/divTitle/list", method = RequestMethod.POST)
	public List<DmsExhibitgroup> getDivTitleList(HttpServletRequest request, @RequestBody DmsExhibitSearch search)
			throws Exception {
		return exhibitService.getExhibitGroupList(search);
	}

	/**
	 * 
	 * @Method Name : saveDivTitleList
	 * @author : allen
	 * @date : 2016. 6. 21.
	 * @description : 구분 타이틀 Grid 저장
	 *
	 * @param request
	 * @param dmsExhibitGroupList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/divTitle/save", method = RequestMethod.POST)
	public void saveDivTitleList(HttpServletRequest request,
			@RequestBody List<DmsExhibitgroup> dmsExhibitGroupList)
			throws Exception {
		exhibitService.saveExhibitGroupList(dmsExhibitGroupList);
	}

	/**
	 * 
	 * @Method Name : divTitleInsert
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 구분타이틀 등록
	 *
	 * @param request
	 * @param exhibitIds
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertDivTitle", method = RequestMethod.POST)
	public void insertDivTitle(HttpServletRequest request, @RequestBody DmsExhibitgroup dmsExhibitGroup) throws Exception {
		exhibitService.insertExhibitGroup(dmsExhibitGroup);
	}

	@RequestMapping(value = "/getDivTitleDetail", method = RequestMethod.POST)
	public DmsExhibitgroup getDivTitleDetail(HttpServletRequest request, @RequestBody DmsExhibitgroup dmsExhibitGroup)
			throws Exception {
		return exhibitService.getDivTitleDetail(dmsExhibitGroup);
	}

	/**
	 * 
	 * @Method Name : deleteDivTitle
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 기획전 구분타이틀 삭제
	 *
	 * @param request
	 * @param dmsExhibitGroupList
	 * @throws Exception
	 */
	@RequestMapping(value = "/divTitle/delete", method = RequestMethod.POST)
	public void deleteDivTitle(HttpServletRequest request, @RequestBody List<DmsExhibitgroup> dmsExhibitGroupList)
			throws Exception {
		exhibitService.deleteExhibitGroup(dmsExhibitGroupList);
	}

	/**
	 * 
	 * @Method Name : getExhibitProductList
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 기획전 상품리스트 조회
	 *
	 * @param request
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/divTitlePoduct/list", method = RequestMethod.POST)
	public List<DmsExhibitproduct> getExhibitProductList(HttpServletRequest request,
			@RequestBody DmsExhibitSearch dmsExhibitSearch) throws Exception {
		return exhibitService.getExhibitProductList(dmsExhibitSearch);
	}
	
	/**
	 * 
	 * @Method Name : insertDivTitleProduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 구분타이틀 상품 저장
	 *
	 * @param request
	 * @param dmsExhibitProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/divTitlePoduct/save", method = RequestMethod.POST)
	public void insertDivTitleProduct(HttpServletRequest request,
			@RequestBody List<DmsExhibitproduct> dmsExhibitProductList)
			throws Exception {
		exhibitService.insertExhibitProduct(dmsExhibitProductList);
	}

	/**
	 * 
	 * @Method Name : insertExhibitMainProduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 기획전 대표 상품 저장
	 *
	 * @param request
	 * @param dmsExhibitmainproduct
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertExhibitMainProduct", method = RequestMethod.POST)
	public void insertExhibitMainProduct(HttpServletRequest request, @RequestBody DmsExhibitmainproduct dmsExhibitmainproduct)
			throws Exception {
		exhibitService.insertExhibitMainProduct(dmsExhibitmainproduct);
	}

	/**
	 * 
	 * @Method Name : checkExhibitDivTitleProduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 구분 타이틀 존재 여부 체크
	 *
	 * @param request
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkExhibitDivTitleProduct", method = RequestMethod.POST)
	public List<DmsExhibitproduct> checkExhibitDivTitleProduct(HttpServletRequest request,
			@RequestBody DmsExhibitSearch dmsExhibitSearch)
			throws Exception {
		
		List<DmsExhibitproduct> productIdList = new ArrayList<DmsExhibitproduct>();
		// 구분타이틀 상품에 존재하는 상품 조회
		List<DmsExhibitproduct> existsProductIdList = exhibitService.getExistsExhibitProduct(dmsExhibitSearch);
		boolean flag = true;
		for (int i = 0; i < dmsExhibitSearch.getProductIds().length; i++) {
			for (int j = 0; j < existsProductIdList.size(); j++) {
				if (dmsExhibitSearch.getProductIds()[i].equals(existsProductIdList.get(j).getProductId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				DmsExhibitproduct dmsExhibitproduct = new DmsExhibitproduct();
				dmsExhibitproduct.setProductId(dmsExhibitSearch.getProductIds()[i]);
				productIdList.add(dmsExhibitproduct);
			}
			flag = true;
		}

		return productIdList;
	}
	
	/**
	 * 
	 * @Method Name : checkExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 기획전 등록 쿠폰 확인
	 *
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkExhibitCoupon", method = RequestMethod.POST)
	public List<DmsExhibitcoupon> checkExhibitCoupon(@RequestBody DmsExhibitSearch dmsExhibitSearch)
					throws Exception {
		
		List<DmsExhibitcoupon> couponIdList = new ArrayList<DmsExhibitcoupon>();
		// 구분타이틀 상품에 존재하는 상품 조회
		List<DmsExhibitcoupon> existsCouponIdList = exhibitService.getExistsExhibitCoupon(dmsExhibitSearch);
		boolean flag = true;
		for (int i = 0; i < dmsExhibitSearch.getCouponIds().length; i++) {
			for (int j = 0; j < existsCouponIdList.size(); j++) {
				if (dmsExhibitSearch.getCouponIds()[i].equals(existsCouponIdList.get(j).getCouponId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				DmsExhibitcoupon dmsExhibitcoupon = new DmsExhibitcoupon();
				dmsExhibitcoupon.setCouponId(dmsExhibitSearch.getCouponIds()[i]);
				couponIdList.add(dmsExhibitcoupon);
			}
			flag = true;
		}
		
		return couponIdList;
	}
	
	/**
	 * 
	 * @Method Name : deleteDivTitleProduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 구분타이틀 상품 삭제
	 *
	 * @param request
	 * @param dmsExhibitProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/divTitlePoduct/delete ", method = RequestMethod.POST)
	public void deleteDivTitleProduct(HttpServletRequest request,
			@RequestBody List<DmsExhibitproduct> dmsExhibitProductList)
			throws Exception {
		exhibitService.deleteDivTitleProduct(dmsExhibitProductList);
	}
	
	/**
	 * 
	 * @Method Name : getExhibitCouponList
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 쿠폰 받기 기획전 쿠폰 목록 조회
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/coupon/list", method = RequestMethod.POST)
	public List<DmsExhibitcoupon> getExhibitCouponList(HttpServletRequest request, @RequestBody DmsExhibitSearch search)
			throws Exception {
		return exhibitService.getExhibitCouponList(search);
	}

	/**
	 * 
	 * @Method Name : saveExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 쿠폰 받기 기획전 쿠폰 저장
	 *
	 * @param request
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	@RequestMapping(value = "/coupon/save", method = RequestMethod.POST)
	public void saveExhibitCoupon(HttpServletRequest request,
			@RequestBody List<DmsExhibitcoupon> dmsExhibitcouponList) throws Exception {
		exhibitService.saveExhibitCoupon(dmsExhibitcouponList);
	}

	/**
	 * 
	 * @Method Name : deleteExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 쿠폰 받기 기획전 쿠폰 삭제
	 *
	 * @param request
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	@RequestMapping(value = "/coupon/delete", method = RequestMethod.POST)
	public void deleteExhibitCoupon(HttpServletRequest request, @RequestBody List<DmsExhibitcoupon> dmsExhibitcouponList)
			throws Exception {
		exhibitService.deleteExhibitCoupon(dmsExhibitcouponList);
	}

	/**
	 * 
	 * @Method Name : getMainProductList
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 원데이 기획전 대표 상품 목록 조회
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/list", method = RequestMethod.POST)
	public List<DmsExhibitmainproduct> getMainProductList(HttpServletRequest request, @RequestBody DmsExhibitSearch search)
			throws Exception {
		List<DmsExhibitmainproduct> mainProductList = exhibitService.getExhibitMainProductList(search);
		return mainProductList;
	}

	/**
	 * 
	 * @Method Name : getMainProductDetail
	 * @author : intune
	 * @date : 2016. 7. 4.
	 * @description : 대표상품 상세
	 *
	 * @param request
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/detail", method = RequestMethod.POST)
	public DmsExhibitmainproduct getMainProductDetail(HttpServletRequest request, @RequestBody DmsExhibitmainproduct product)
			throws Exception {
		return exhibitService.getExhibitMainProductDetail(product);
	}

	@RequestMapping(value = "/checkMainProduct", method = RequestMethod.POST)
	public boolean checkMainProduct(HttpServletRequest request, @RequestBody DmsExhibitmainproduct product) throws Exception {
		boolean existsFlag = false;
		existsFlag = exhibitService.checkExhibitMainProduct(product);
		return existsFlag;
	}

	/**
	 * 
	 * @Method Name : saveMainProduct
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 원데이 기획전 대표 상품 저장
	 *
	 * @param request
	 * @param mainProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/save", method = RequestMethod.POST)
	public void saveMainProduct(HttpServletRequest request, @RequestBody List<DmsExhibitmainproduct> mainProductList)
			throws Exception {
		exhibitService.saveExhibitMainProduct(mainProductList);
	}

	/**
	 * 
	 * @Method Name : deleteMainProduct
	 * @author : allen
	 * @date : 2016. 6. 18.
	 * @description : 원데이 기획전 대표 상품 삭제
	 *
	 * @param request
	 * @param mainProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/delete", method = RequestMethod.POST)
	public void deleteMainProduct(HttpServletRequest request, @RequestBody List<DmsExhibitmainproduct> mainProductList)
			throws Exception {
		exhibitService.deleteExhibitMainProduct(mainProductList);
	}

	/**
	 * 
	 * @Method Name : deleteBatchMainProduct
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 원데이 기획전 대표 상품 일괄 삭제
	 *
	 * @param request
	 * @param dmsExhibit
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/deleteBatch", method = RequestMethod.POST)
	public void deleteBatchMainProduct(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit) throws Exception {
		exhibitService.deleteBatchExhibitMainProduct(dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : deleteExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 쿠폰 받기 기획전 쿠폰 일괄 삭제
	 *
	 * @param request
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	@RequestMapping(value = "/coupon/deleteBatch", method = RequestMethod.POST)
	public void deleteBatchExhibitCoupon(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit)
			throws Exception {
		exhibitService.deleteBatchExhibitCoupon(dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : deleteBatchExhibitOffshop
	 * @author : allen
	 * @date : 2016. 8. 8.
	 * @description : 오프라인 기획전 매장 일괄 삭제
	 *
	 * @param request
	 * @param dmsExhibit
	 * @throws Exception
	 */
	@RequestMapping(value = "/offshop/deleteBatch", method = RequestMethod.POST)
	public void deleteBatchExhibitOffshop(HttpServletRequest request, @RequestBody DmsExhibit dmsExhibit) throws Exception {
		exhibitService.deleteBatchExhibitOffshop(dmsExhibit);
	}

	@RequestMapping(value = "/offshop/list", method = RequestMethod.POST)
	public List<CcsOffshop> getExhibitOffshoptList(HttpServletRequest request, @RequestBody DmsExhibitSearch search)
			throws Exception {
		List<CcsOffshop> offshopList = exhibitService.getExhibitOffshopList(search);
		return offshopList;
	}

	/**
	 * 
	 * @Method Name : getExhibitListExcel
	 * @author : intune
	 * @date : 2016. 6. 24.
	 * @description : 기획전 엑셀 다운로드
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getExhibitListExcel(@RequestBody DmsExhibitSearch search) throws Exception {

		List<DmsExhibit> list = new ArrayList<DmsExhibit>();
		search.setStoreId(SessionUtil.getStoreId());
		list = exhibitService.getExhibitList(search);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("exhibitId", list.get(i).getExhibitId());
			map.put("name", list.get(i).getName());
			map.put("exhibitTypeName", list.get(i).getExhibitTypeName());
			map.put("exhibitStateName", list.get(i).getExhibitStateName());
			map.put("displayYn", list.get(i).getDisplayYn());
			map.put("sortNo", list.get(i).getSortNo().toString());
			map.put("startDt", list.get(i).getStartDt());
			map.put("endDt", list.get(i).getEndDt());
			map.put("insDt", list.get(i).getInsDt());
			map.put("insId", list.get(i).getInsId());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) search, dataList);

		return msg;
	}

	/**
	 * 
	 * @Method Name : getMainProductListExcel
	 * @author : intune
	 * @date : 2016. 6. 24.
	 * @description : 기획전 대표상품 엑셀 다운로드
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mainProduct/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMainProductListExcel(@RequestBody DmsExhibitSearch search) throws Exception {

		search.setStoreId(SessionUtil.getStoreId());
		List<DmsExhibitmainproduct> list = exhibitService.getExhibitMainProductList(search);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("productId", list.get(i).getProductId());
			map.put("pmsProduct.name", list.get(i).getPmsProduct().getName());
			map.put("name", list.get(i).getName());
			map.put("img", list.get(i).getImg());
			map.put("sortNo", list.get(i).getSortNo().toString());
			map.put("pmsProduct.productTypeName", list.get(i).getPmsProduct().getProductTypeName());
			map.put("pmsProduct.saleStateName", list.get(i).getPmsProduct().getSaleStateName());
			map.put("pmsProduct.salePrice", list.get(i).getPmsProduct().getSalePrice().toString());
			map.put("pmsProduct.brandName", list.get(i).getPmsProduct().getBrandName());
			map.put("insDt", list.get(i).getInsDt());
			map.put("insId", list.get(i).getInsId());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) search, dataList);

		return msg;
	}
}
