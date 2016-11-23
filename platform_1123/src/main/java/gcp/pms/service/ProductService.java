/**
 * 
 */
package gcp.pms.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.NoticeService;
import gcp.common.util.FoSessionUtil;
import gcp.common.util.PmsUtil;
import gcp.dms.model.DmsDisplaycategoryproduct;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.base.BaseDmsDisplaycategoryproduct;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.ExhibitService;
import gcp.external.service.ErpService;
import gcp.mms.model.MmsWishlist;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsOffshopstock;
import gcp.pms.model.PmsPricereserve;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductage;
import gcp.pms.model.PmsProductattribute;
import gcp.pms.model.PmsProductimg;
import gcp.pms.model.PmsProductnotice;
import gcp.pms.model.PmsProductnoticefield;
import gcp.pms.model.PmsProductoption;
import gcp.pms.model.PmsProductreserve;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSaleproductoptionvalue;
import gcp.pms.model.PmsSaleproductpricereserve;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.base.BasePmsPricereserve;
import gcp.pms.model.base.BasePmsProduct;
import gcp.pms.model.base.BasePmsProductattribute;
import gcp.pms.model.base.BasePmsProductnotice;
import gcp.pms.model.base.BasePmsProductreserve;
import gcp.pms.model.base.BasePmsSaleproduct;
import gcp.pms.model.base.BasePmsSaleproductoptionvalue;
import gcp.pms.model.custom.PmsOptimalprice;
import gcp.pms.model.custom.PmsOptionvalue;
import gcp.pms.model.search.PmsCategorySearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.search.SpsCardPromotionSearch;
import gcp.sps.model.search.SpsDealSearch;
import gcp.sps.model.search.SpsPresentSearch;
import gcp.sps.service.DealService;
import gcp.sps.service.DiscountService;
import gcp.sps.service.PointService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.LocaleUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @FileName : ProductService.java
 * @author : dennis
 * @date : 2016. 4. 20.
 * @description : Product Service
 */
@Service("productService")
public class ProductService extends BaseService {

	@Autowired
	private PointService	pointService;
	@Autowired
	private ErpService	erpService;

	@Autowired
	private DealService		dealService;

	@Autowired
	private ExhibitService	exhibitService;

	@Autowired
	private CommonService	commonService;

	@Autowired
	private NoticeService	noticeService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private DiscountService	discountService;

	@Autowired
	private PresentService	presentService;

	@Autowired
	private PriceService	priceService;

	private final Log logger = LogFactory.getLog(getClass());

	public List<PmsProduct> getProductList(PmsProductSearch search) {

		// id 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(search.getProductIds()) || CommonUtil.isNotEmpty(search.getSaleproductIds())) {

			String temp = CommonUtil.isNotEmpty(search.getProductIds()) ? search.getProductIds() : "";
			String temp2 = CommonUtil.isNotEmpty(search.getSaleproductIds()) ? search.getSaleproductIds() : "";

			search = new PmsProductSearch();
			if (!"".equals(temp)) {
				search.setProductIds(temp);
			}
			if (!"".equals(temp2)) {
				search.setSaleproductIds(temp2);
			}
		} else if (CommonUtil.isNotEmpty(search.getSearchKeyword())) {
			
			String temp = search.getSearchKeyword();
			String type = search.getInfoType();
			if ("ID".equals(type)) {
				search = new PmsProductSearch();
				search.setInfoType(type);
				search.setSearchKeyword(temp);
			}
		}
		search.setStoreId(SessionUtil.getStoreId());

		return (List<PmsProduct>) dao.selectList("pms.product.getProductList", search);
	}

	public List<PmsProduct> getProductListWithSaleStock(List<BasePmsProduct> productList) {

		Map<String, List<BasePmsProduct>> map = new HashMap<String, List<BasePmsProduct>>();
		map.put("search", productList);
		return (List<PmsProduct>) dao.selectList("pms.product.getProductListWithSaleStock", map);
	}

	private PmsOptimalprice setOptimalPriceParam(PmsProduct product, String saleproductId) {

		String productId = product.getProductId();
		BigDecimal salePrice = product.getSalePrice();
		BigDecimal listPrice = product.getListPrice();
		BigDecimal commissionRate = product.getCommissionRate();

		// 2. 상품 가격 ( 할인가, 판매가, 정기배송가, 혜택가 ) 
		PmsOptimalprice p = new PmsOptimalprice();
		p.setStoreId(SessionUtil.getStoreId());
		p.setProductId(productId);

		List<String> memberTypeCds = new ArrayList<String>();

		// 회원 유형 설정

		memberService.getMemberTypeInfo(memberTypeCds);
		// 회원 등급 설정
		p.setMemGradeCd(FoSessionUtil.getMemGradeCd());
		p.setMemberNo(SessionUtil.getMemberNo());
		p.setMemberTypeCds(memberTypeCds);
		p.setTargetAmt(salePrice);
		p.setSalePrice(salePrice);

		String deviceType = FoSessionUtil.getDeviceTypeCd();
		p.setDeviceTypeCd(deviceType);
//		p.setAddSalePrice(addSalePrice);
//		p.setSaleproductId(saleproductId);
		p.setCommissionRate(commissionRate);
		p.setListPrice(listPrice);
		return p;
	}
	/**
	 * 프론트 상품 상세
	 * 
	 * @Method Name : getFoProductDetail
	 * @author : intune
	 * @date : 2016. 8. 25.
	 * @description :
	 *
	 * @return
	 * @throws Exception
	 */
	public PmsProduct getFoProductDetail(PmsProductSearch productSearch ) throws Exception {

		String channelId = null;
		BigDecimal memberNo = null;
		String deviceTypeCd = productSearch.getDeviceTypeCd();

		if (FoSessionUtil.isMemberLogin()) {
			memberNo = SessionUtil.getMemberNo();
		}


		String storeId = SessionUtil.getStoreId();

		//허용 제어 파라메터 설정
		CcsControlSearch controlSearch = new CcsControlSearch();
		controlSearch.setExceptionFlag(true);
		controlSearch.setStoreId(storeId);
		controlSearch.setChannelId(channelId);
		controlSearch.setMemberNo(memberNo);
		controlSearch.setDeviceTypeCd(deviceTypeCd);
//
//		controlSearch.setControlNo(product.getControlNo());
//		controlSearch.setApplyObjectName("상품," + product.getName());
//
//		// 허용제어 체크
//		boolean isControllSuccess = true;
//
//		if (CommonUtil.isNotEmpty(product.getControlNo())) {
//			isControllSuccess = commonService.checkControl(controlSearch);
//		}
//
//		if (!isControllSuccess) {
//			return null;
//		}

		// 0. 상품기본정보 조회
		PmsProduct product = (PmsProduct) dao.selectOne("pms.product.getProductDetail", productSearch);

		if (product == null) {
			throw new ServiceException("pms.product.detail");
		}

		String productId = product.getProductId();
		productSearch.setProductId(productId);

		// 브랜드 조회
		if (CommonUtil.isNotEmpty(product.getBrandId())) {
			PmsBrand brandSearch = new PmsBrand();
			brandSearch.setStoreId(storeId);
			brandSearch.setBrandId(product.getBrandId());
			PmsBrand brand = (PmsBrand) dao.selectOneTable(brandSearch);
			product.setPmsBrand(brand);
		}

		// 픽업Y일경우 픽업가능여부 체크(재고, 매장등..)
		if ("Y".equals(product.getOffshopPickupYn())) {
			String psaleproductId = (String) dao.selectOne("pms.product.getOffshopPickupYn", productSearch);
			if (CommonUtil.isEmpty(psaleproductId)) {
				product.setOffshopPickupYn("N");
			}
		}
		// 단품(옵션정보 조회)
		// 1. 일반상품

		// 2. 세트상품 
		if (BaseConstants.PRODUCT_TYPE_CD_SET.equals(product.getProductTypeCd())) {
			List<PmsSetproduct> pmsSetproducts = selectSetproductList(productSearch);
			product.setPmsSetproducts(pmsSetproducts);
		}
		// 상품 옵션목록 조회
		List<PmsProductoption> options = getPmsProductOptionList(productSearch);
		product.setPmsProductoptions(options);

		// 첫번째 옵션의 옵션값 목록 조회
		if (options != null && options.size() > 0) {
			String optionName = options.get(0).getOptionName();
			productSearch.setTargetOptionName(optionName);

			// 픽업구매 전용 상품인경우 픽업매장용 옵션목록 조회
			if (!BaseConstants.SALE_STATE_CD_SALE.equals(product.getSaleStateCd()) && "Y".equals(product.getOffshopPickupYn())) {
				productSearch.setPickupProduct("Y");
			}

			options.get(0).setOptionValues(getOptionValueList(productSearch));
		}

		// 옵션없을경우 기본 단품 정보 조회(일반,세트)
		if ("N".equals(product.getOptionYn()) || BaseConstants.PRODUCT_TYPE_CD_SET.equals(product.getProductTypeCd())) {
			List<PmsSaleproduct> defaultSaleproduct = (List<PmsSaleproduct>) dao.selectList("pms.product.selectSaleproductList",
					productSearch);
			product.setSaleproductId(defaultSaleproduct.get(0).getSaleproductId());

			if (!BaseConstants.PRODUCT_TYPE_CD_SET.equals(product.getProductTypeCd())) {
				// 일반, 옵션 없는 상품의 경우 재고
				product.setRealStockQty(defaultSaleproduct.get(0).getRealStockQty());
			}

			// 핍업여부가Y일경우 재고
			if ("Y".equals(product.getOffshopPickupYn())) {
				int stock = (Integer) dao.selectOne("pms.product.getMaxOffshopStockBySaleproduct",
						defaultSaleproduct.get(0).getSaleproductId());
				product.setPickupMaxQty(new BigDecimal(stock));
			}

		}

		// 1. 쇼킹제로 조회
		SpsDealSearch dealSearch = new SpsDealSearch();
		dealSearch.setProductId(productId);
		dealSearch.setStoreId(storeId);
		controlSearch.setControlNo(null);
		SpsDeal deal = dealService.getProductShockingDeal(controlSearch, dealSearch);

		if (deal != null) {
			logger.debug("@@@@ ShockDeal : " + deal.getEndDt());
		}
		product.setSpsDeal(deal);

		// 최적가 조회
		PmsOptimalprice p = setOptimalPriceParam(product, null);

		PmsOptimalprice op = priceService.optimalPrice(p);

		if (CommonUtil.isEmpty(op.getDeliveryFeeFreeYn())) {
			op.setDeliveryFeeFreeYn(product.getDeliveryFeeFreeYn());
		}
		if (CommonUtil.isEmpty(op.getPointSaveRate())) {
			op.setPointSaveRate(product.getPointSaveRate());
		}
		product.setOptimalprice(op);

		logger.debug("## optimal price : " + op);
		logger.debug("## optimal total sale price : " + op.getTotalSalePrice());

		
		
		// 사은품 조회
		SpsPresentSearch spsPresentSearch = new SpsPresentSearch();
		spsPresentSearch.setStoreId(storeId);
		spsPresentSearch.setProductId(productId);
		spsPresentSearch.setDealId(op.getDealId());
		spsPresentSearch.setPresentTypeCd(BaseConstants.PRESENT_TYPE_CD_PRODUCT);

		List<SpsPresent> presents = presentService.getPresentPromotion(spsPresentSearch);
		if (presents != null && presents.size() > 0) {
			product.setPresentYn("Y");
		} else {
			product.setPresentYn("N");
		}
		
		// 3. 관련 기획전
		DmsExhibitSearch exhibitSearch = new DmsExhibitSearch();
		exhibitSearch.setProductId(productId);
		exhibitSearch.setStoreId(storeId);
		controlSearch.setControlNo(null);
		List<DmsExhibit> exhibits = exhibitService.getProductExhibitList(controlSearch, exhibitSearch);
		product.setExhibits(exhibits);

		// 5. MD공지
		CcsNoticeSearch noticeSearch = new CcsNoticeSearch();
		noticeSearch.setProductId(productId);
		noticeSearch.setStoreId(storeId);
		CcsNotice mdNotice = noticeService.getProductMdNotice(noticeSearch);
		product.setCcsNotice(mdNotice);

		// 6. 고시정보
		productSearch.setProductNoticeTypeCd(product.getProductNoticeTypeCd());
		product.setPmsProductnotices(
				(List<PmsProductnotice>) dao.selectList("pms.product.selectProductNoticeList", productSearch));


		// 상품 이미지
		List<PmsProductimg> images = getProductImages(productSearch);
		product.setPmsProductimgs(images);

		// 카드사 할인 프로모션
		SpsCardPromotionSearch cardParam = new SpsCardPromotionSearch();
		cardParam.setStoreId(storeId);
		cardParam.setDeviceTypeCd(productSearch.getDeviceTypeCd());
		List<SpsCardpromotion> card = discountService.getApplyCardPromotion(cardParam);
		if (card != null && card.size() > 0) {
			product.setSpsCardpromotion(card.get(0));
		}

		// 배송정보 조회
		CcsDeliverypolicy policySearch = new CcsDeliverypolicy();
		policySearch.setDeliveryPolicyNo(product.getDeliveryPolicyNo());
		policySearch.setStoreId(SessionUtil.getStoreId());
		CcsDeliverypolicy policy = (CcsDeliverypolicy) dao.selectOneTable(policySearch);
		product.setCcsDeliverypolicy(policy);
//		if ("N".equals(product.getOptimalprice().getDeliveryFeeFreeYn())) {
//		}
		// 찜 여부 조회
		String wishlistNo = null;
		if (CommonUtil.isNotEmpty(memberNo)) {

			MmsWishlist wish = new MmsWishlist();
			wish.setStoreId(storeId);
			wish.setMemberNo(memberNo);
			wish.setProductId(productId);
			wishlistNo = (String) dao.selectOne("mms.member.getExistsWishlist", wish);
			product.setWishlistNo(wishlistNo);
		}

		// 포인트 프로모션

		BigDecimal minPrice = product.getOptimalprice().getTotalSalePrice().subtract(product.getOptimalprice().getCouponDcAmt());
		BigDecimal pointRate = product.getOptimalprice().getPointSaveRate();
		BigDecimal objPrice = PmsUtil.applyPointRate(minPrice, pointRate);
		// 포인트 적립가
		SpsPointsave spsPointsave = pointService.getPointPromotion(productId, objPrice);
		product.setTotalPoint(spsPointsave.getTotalPoint());

		//상품의 전시카테고리 ID parh조회
		String categorys = (String) dao.selectOne("pms.product.getDisplayCategoryIdPathOfProduct", productSearch);
		product.setDisplayCategoryIdPath(categorys);
		if (CommonUtil.isNotEmpty(categorys)) {
			String[] cateArr = categorys.split(",");
			productSearch.setDisplayCategoryId(cateArr[cateArr.length - 1]);
		} else {
			productSearch.setDisplayCategoryId(null);
		}
//		if (CommonUtil.isNotEmpty(productSearch.getDisplayCategoryId())) {
//			//카테고리 스트링 패스 조회(레코벨 전송용)
//			String categorys = (String) dao.selectOne("pms.product.getDisplayCategoryIdPathOfProduct", productSearch);
//
//			List<List<DmsDisplaycategory>> cateList = new ArrayList<List<DmsDisplaycategory>>();
//
//			if (CommonUtil.isNotEmpty(categorys)) {
//
//				String[] cates = categorys.split(",");
//
//
//				for (int i = 0; i < cates.length - 1; i++) {
//					//첫번째 upper
//					PmsProductSearch cateSearch = new PmsProductSearch();
//					cateSearch.setStoreId(storeId);
//					cateSearch.setDisplayCategoryId(cates[i]);
//					List<DmsDisplaycategory> cate = (List<DmsDisplaycategory>) dao
//							.selectList("pms.product.getCategoryListByUpper", cateSearch);
//					cateList.add(cate);
//
//				}
//			}
//			product.setCateList(cateList);
//
//		}
		return product;
	}

	/**
	 * 상품의 옵션 목록 조회
	 * 
	 * @Method Name : getPmsProductOptionList
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProductoption> getPmsProductOptionList(PmsProductSearch search) {
		// 상품 옵션 정보 조회
		return (List<PmsProductoption>) dao.selectList("pms.product.getProductOptionList", search);
	}

	/**
	 * 옵션명으로 옵션값 목록 조회
	 * 
	 * @Method Name : getOptionValueList
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description : 상품의 옵션명에 해당하는 옵션값 목록을 조회한다.<br/>
	 *              상품의 옵션이 2개 이상인 경우 이미 선택된 옵션값을 파라메터로 하여 단품정보를 확인하여 해당 옵션의 품절여부를 함께 조회한다.
	 *
	 * @param search (필수)targetOptionName <br/>
	 *            (옵션)pickupProduct 픽업매장용 옵션조회시 Y <br/>
	 *            (옵션)selectedOptions 기 선택된 다른 옵션값
	 * @return
	 */
	public List<PmsOptionvalue> getOptionValueList(PmsProductSearch search) {
		// 옵션의 옵션값 목록 조회
		return (List<PmsOptionvalue>) dao.selectList("pms.product.getOptionValueList", search);
	}

	/**
	 * 선택한 옵션들로 단품정보 가져오기
	 * 
	 * @Method Name : getSaleproductByOptions
	 * @author : eddie
	 * @date : 2016. 8. 30.
	 * @description : pickupProduct = 'Y' 일경우 픽업가로 리턴
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public PmsSaleproduct getSaleproductByOptions(PmsProductSearch search) throws Exception {

		PmsSaleproduct saleProduct = (PmsSaleproduct) dao.selectOne("pms.product.getSaleproductByOptions", search);

		// 단품의 최저가 조회 ( 픽업상품은 픽업할인율 적용)
		if (!"Y".equals(search.getPickupProduct())) {

			PmsOptimalprice optPrice = new PmsOptimalprice();

			//dealId couponId productId saleproductId memberNo
			optPrice.setDealId(search.getDealId());
			optPrice.setCouponId(search.getCouponId());
			optPrice.setProductId(search.getProductId());
			optPrice.setSaleproductId(saleProduct.getSaleproductId());
			optPrice.setMemberNo(SessionUtil.getMemberNo());
			optPrice.setTargetAmt(saleProduct.getSalePrice().add(saleProduct.getAddSalePrice()));
			optPrice.setTotalSalePrice(saleProduct.getSalePrice().add(saleProduct.getAddSalePrice()));
			optPrice.setSalePrice(saleProduct.getSalePrice());
			optPrice.setAddSalePrice(saleProduct.getAddSalePrice());
			priceService.optimalSaleprice(optPrice);

			saleProduct.setAddSalePrice(optPrice.getAddSalePrice());
			saleProduct.setSalePrice(optPrice.getSalePrice());
			saleProduct.setMinSalePrice(optPrice.getTotalSalePrice().subtract(optPrice.getCouponDcAmt()));
		} else {

			// 픽업 판매가
//			PmsProduct product = new PmsProduct();
//			product.setStoreId(SessionUtil.getStoreId());
//			product.setProductId(search.getProductId());
//			product = (PmsProduct) dao.selectOneTable(product);

//			BigDecimal pickupPrice = PmsUtil.applyDcRateToPrice(product.getSalePrice(), product.getOffshopPickupDcRate());
//			saleProduct.setSalePrice(pickupPrice);
//			saleProduct.setMinSalePrice(pickupPrice);

//			System.out.println("### saleProduct.setSalePrice : " + saleProduct.getSalePrice());
		}

		return saleProduct;
	}
	/**
	 * 
	 * @Method Name : getStockList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 단품 재고 목록 조회
	 *
	 * @param pmsProductSearch
	 * @return List<PmsSaleproduct>
	 */
	public List<PmsSaleproduct> getStockList(PmsProductSearch pmsProductSearch) {

		// id 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(pmsProductSearch.getProductIds())
				|| CommonUtil.isNotEmpty(pmsProductSearch.getSaleproductIds())) {

			String temp = CommonUtil.isNotEmpty(pmsProductSearch.getProductIds()) ? pmsProductSearch.getProductIds() : "";
			String temp2 = CommonUtil.isNotEmpty(pmsProductSearch.getSaleproductIds()) ? pmsProductSearch.getSaleproductIds()
					: "";

			pmsProductSearch = new PmsProductSearch();
			if (!"".equals(temp)) {
				pmsProductSearch.setProductIds(temp);
			}
			if (!"".equals(temp2)) {
				pmsProductSearch.setSaleproductIds(temp2);
			}
		}
		pmsProductSearch.setStoreId(SessionUtil.getStoreId());

		return (List<PmsSaleproduct>) dao.selectList("pms.product.getStockList", pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updateStockList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 단품 재고 수정
	 *
	 * @param pmsStockList
	 * @throws Exception
	 */
	public void updateStockList(List<PmsSaleproduct> pmsStockList) throws Exception {
		for (PmsSaleproduct pmsSaleproduct : pmsStockList) {
			dao.updateOneTable(pmsSaleproduct);
		}
	}

	/**
	 * 
	 * @Method Name : insertPmsProductreserve
	 * @author : allen
	 * @date : 2016. 5. 31.
	 * @description : 상품 예약 변경 저장
	 *
	 * @param pmsProductReserve
	 * @throws Exception
	 */
	public void insertPmsProductreserve(PmsProductreserve pmsProductReserve) throws Exception {
		dao.insertOneTable(pmsProductReserve);
	}

	/**
	 * 
	 * @Method Name : insertPmsProductreserve
	 * @author : allen
	 * @date : 2016. 5. 31.
	 * @description : 상품 가격 예약 변경 저장
	 *
	 * @param pmsProductReserve
	 * @throws Exception
	 */
	public void insertPmsPricereserve(PmsPricereserve r) throws Exception {

		boolean isNew = false;

		if (StringUtils.isEmpty(r.getPriceReserveNo())) {
			isNew = true;
		}

		r.setStoreId(SessionUtil.getStoreId());
		r.setReqDt(BaseConstants.SYSDATE);

		if (StringUtils.isEmpty(r.getPriceReserveStateCd())) {
			r.setPriceReserveStateCd("PRICE_RESERVE_STATE_CD.REQ");
		}

		// 가격 예약 등록
		if (isNew) {
			dao.insertOneTable(r);
		} else {
			dao.updateOneTable(r);
		}
		// 단품 가격 예약 등록
		dao.delete("pms.product.deleteSaleProductPriceReserve", r);

		if ("Y".equals(r.getSalePriceReserveYn()) && r.getPmsSaleproductpricereserves() != null
				&& r.getPmsSaleproductpricereserves().size() > 0) {
			for (PmsSaleproductpricereserve u : r.getPmsSaleproductpricereserves()) {

				u.setPriceReserveNo(r.getPriceReserveNo());
				u.setStoreId(SessionUtil.getStoreId());
				u.setProductId(r.getProductId());
				dao.insertOneTable(u);

			}
		}
	}

	/**
	 * 상품 수정
	 * 
	 * @Method Name : saveProduct
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description :
	 * 
	 * @param product
	 * @throws Exception
	 */
	public void saveProduct(PmsProduct product) throws Exception {

		dao.updateOneTable(product);
	}

	/**
	 * 단품 생성
	 * 
	 * @Method Name : insertSaleproducts
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description PmsProduct > pmsProductsaleproducts 의 단품정보를 등록한다. <br/>
	 *              optionYn = 'N'인 경우 기본 단품을 생성한다.
	 * @param product
	 * @throws Exception
	 */
	private void insertSaleproducts(PmsProduct product) throws Exception {

		// 2. 단품 정보 등록
		if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
			// 기본 단품 등록
			insertDefaultSaleproduct(product.getProductId(), product.getName());

		} else {
			// 옵션이 있는 단품 등록
			for (BasePmsSaleproduct saleproduct : product.getPmsSaleproducts()) {

				if (!PmsUtil.isApproval(product.getSaleStateCd())) {
					saleproduct.setSaleproductId("");
				}
				saleproduct.setProductId(product.getProductId());
				insertSaleproduct(saleproduct, product.getName());
			}
		}
	}

	/**
	 * 기본 단품 생성
	 * 
	 * @Method Name : insertDefaultSaleproduct
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description : 옵션이 없는 상품의 경우 기본 단품을 생성한다.
	 *
	 * @param productId
	 * @param productName
	 */
	private void insertDefaultSaleproduct(String productId, String productName) throws Exception {

		// 입점업체 상품이고, optionYn == N 이면 디폴트 단품을 생성
		BasePmsSaleproduct saleproduct = new BasePmsSaleproduct();
		saleproduct.setProductId(productId);
		saleproduct.setStoreId(SessionUtil.getStoreId());
		saleproduct.setName(productName);
		saleproduct.setRealStockQty(BigDecimal.valueOf(999));
		saleproduct.setSafeStockQty(BigDecimal.valueOf(0));
		saleproduct.setAddSalePrice(BigDecimal.valueOf(0));
		saleproduct.setSaleproductStateCd("SALEPRODUCT_STATE_CD.SALE");
		saleproduct.setInsId(SessionUtil.getLoginId());
//		dao.insert("pms.product.insertSaleproduct", saleproduct);
		dao.insertOneTable(saleproduct);

	}

	/**
	 * 신규 단품 등록
	 * 
	 * @Method Name : insertSaleproduct
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description : pms_saleproduct, pms_saleproductoptionvalue 에 insert 한다
	 *
	 * @param saleproduct
	 * @param productId
	 * @throws Exception
	 */
	private void insertSaleproduct(BasePmsSaleproduct saleproduct, String productName) throws Exception {

		String saleProductId = null;

		// 2-1. 단품 (pms_saleproduct) 등록
		saleproduct.setStoreId(SessionUtil.getStoreId());
		saleproduct.setSafeStockQty(BigDecimal.valueOf(0));// TODO
		if (CommonUtil.isEmpty(saleproduct.getSaleproductStateCd())) {
			saleproduct.setSaleproductStateCd("SALEPRODUCT_STATE_CD.SALE");
		}
		// 단품명 설정
		saleproduct.setName(CommonUtil.replaceNull(PmsUtil.makeSaleproductName(saleproduct), productName));
		saleproduct.setInsId(SessionUtil.getLoginId());
		dao.insertOneTable(saleproduct);

		saleProductId = saleproduct.getSaleproductId();

		// 2-2. 단품옵션값 (pms_saleproductoptionvalue) 등록
		if (saleproduct.getPmsSaleproductoptionvalues() != null) {
			for (BasePmsSaleproductoptionvalue optionvalue : saleproduct.getPmsSaleproductoptionvalues()) {
				optionvalue.setSaleproductId(saleProductId);
				optionvalue.setStoreId(SessionUtil.getStoreId());
				optionvalue.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(optionvalue);
			}
		}
	}

	/**
	 * 단품 정보 수정
	 * 
	 * @Method Name : updateSaleproduct
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description : 단품과 단품 옵션 정보를 수정한다.
	 *
	 * @param saleproduct
	 * @throws Exception
	 */
	private void updateSaleproduct(BasePmsSaleproduct saleproduct) throws Exception {

		// 단품 수정 ( pms_saleproduct )
		dao.updateOneTable(saleproduct);

		// 단품 옵션 재등록 ( pms_saleproductoptionvalue )
		dao.delete("pms.product.deleteSaleproductoptionvalue", saleproduct);

		if (saleproduct.getPmsSaleproductoptionvalues() != null) {
			for (PmsSaleproductoptionvalue option : saleproduct.getPmsSaleproductoptionvalues()) {
				option.setStoreId(SessionUtil.getStoreId());
				option.setSaleproductId(saleproduct.getSaleproductId());
				dao.insertOneTable(option);
			}
		}
	}

	/**
	 * 상품등록 기본값 설정
	 * 
	 * @Method Name : setProductDefault
	 * @author : eddie
	 * @date : 2016. 8. 19.
	 * @description :
	 *
	 * @param product
	 */
	private void setProductDefault(PmsProduct product){
		
		// 신규등록 기본값
		if (CommonUtil.isEmpty(product.getProductId())) {
			product.setOutSendYn("N");// 사방넷 전송여부
			product.setSaleStateCd(BaseConstants.SALE_STATE_CD_REQ);
			product.setDisplayYn("Y");
			product.setUseYn("Y");
			product.setNoticeConfirmYn("N");//품목정보확인여부
		}

		product.setFixedDeliveryYn("N");//TODO 삭제예정
		product.setReserveYn("N");// TODO 추수 변경

		// 세트는 옵션 없음
		if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
			product.setOptionYn("N");
		}

//		logger.debug("## product.getErpProductYn() : " + product.getErpProductYn());

		// 상품유형별 설정불가 항목
		if ("N".equals(product.getErpProductYn())) {

			// 위탁 상품

			product.setRegularDeliveryYn("N");//정기배송
			product.setRegularDeliveryFeeFreeYn("N");//정기배송무료여부
			product.setRegularDeliveryPrice(new BigDecimal("0"));//정기배송가
			product.setOffshopPickupYn("N");//매팡픽업여부
			product.setWrapYn("N");//포장여부
			product.setBoxDeliveryYn("N");//박스배송여부
			
			if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
				product.setGiftYn("N");//기프티콘여부;
				product.setOverseasPurchaseYn("N");//해외구매대행여부
			}
		} else {

			// 직매입상품

			product.setOverseasPurchaseYn("N");//해외구매대행여부

			if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
				product.setOffshopPickupYn("N");//매팡픽업여부
				product.setWrapYn("N");//포장여부
			}

		}

		// optionYn = "N" 이면 단품정보 삭제
		if ("N".equals(product.getOptionYn())) {
			product.setPmsProductoptions(null);

			// 승인 이전이면 단품 삭제 : 판매중인것은 단품삭제 불가(기본단품이 존재하기때문)
			if (!PmsUtil.isApproval(product.getSaleStateCd())) {
//				product.setPmsSaleproducts(null);
			}
		}

	}

	/**
	 * 상품 등록
	 * 
	 * @Method Name : insertProduct
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description :
	 *
	 * @param product
	 * @return productId
	 * @throws Exception
	 */
	public String insertProduct(PmsProduct product) throws Exception {

		String productId = null;// 신규 상품ID

		// 기본값
		setProductDefault(product);

		// 0. 허용 설정
		boolean isControlSet = product.getCcsControl() != null && "N".equals(product.getIsAllPermit());

		if (isControlSet) {
			BigDecimal newControlNo = commonService.updateControl(product.getControlNo(), product.getCcsControl());
			product.setControlNo(newControlNo);
		}

		// clob은 별도 update
//		String claimInfo = product.getClaimInfo();
//		String detail = product.getDetail();
//		product.setClaimInfo("");
//		product.setDetail("");

		// 1. 상품 (pms_product) 등록
		dao.insertOneTable(product);
		productId = product.getProductId();

//		// clob 데이터 update
//		product.setClaimInfo(claimInfo);
//		product.setDetail(detail);
//		updatePmsProductClob(product);

		// 2. 단품 정보 등록
		insertSaleproducts(product);

		// 2-1. 상품 옵션 등록
		if (product.getPmsProductoptions() != null && product.getPmsProductoptions().size() > 0) {
			for (PmsProductoption option : product.getPmsProductoptions()) {
				option.setProductId(productId);
				option.setStoreId(SessionUtil.getStoreId());
				option.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(option);
			}
		}

		// 3.상품속성 (pms_productattribute) 등록
		if (product.getPmsProductattributes() != null) {
			for (BasePmsProductattribute attribute : product.getPmsProductattributes()) {
				attribute.setProductId(productId);
				attribute.setStoreId(SessionUtil.getStoreId());
				attribute.setInsId(SessionUtil.getLoginId());
				if (CommonUtil.isNotEmpty(attribute.getAttributeValue())) {
					dao.insertOneTable(attribute);
				}
			}
		}

		// 4. 상품고시정보 ( pms_productnotice) 등록
		if (product.getPmsProductnotices() != null) {
			for (BasePmsProductnotice notice : product.getPmsProductnotices()) {
				notice.setProductId(productId);
				notice.setStoreId(SessionUtil.getStoreId());
				notice.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(notice);
			}
		}

		// 5. 전시카테고리 등록
		for (BaseDmsDisplaycategoryproduct category : product.getDmsDisplaycategoryproducts()) {
			category.setProductId(productId);
			category.setStoreId(SessionUtil.getStoreId());
			category.setInsId(SessionUtil.getLoginId());
			dao.insertOneTable(category);
		}

		// 6. 세트 구성상품 등록
		if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
			for (PmsSetproduct set : product.getPmsSetproducts()) {
				set.setProductId(productId);
				set.setStoreId(SessionUtil.getStoreId());
				set.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(set);
			}
		}

		// 7. 월령정보 등록
		if (product.getPmsProductages() != null) {
			for (PmsProductage age : product.getPmsProductages()) {
				age.setProductId(product.getProductId());
				age.setStoreId(SessionUtil.getStoreId());
				age.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(age);
			}
		}

		return productId;
	}

	private void updatePmsProductClob(PmsProduct product) {

		// clob update
		PmsProduct clob = new PmsProduct();
		clob.setClaimInfo(product.getClaimInfo());
		clob.setDetail(product.getDetail());
		clob.setStoreId(product.getStoreId());
		clob.setProductId(product.getProductId());
		dao.update("pms.product.updateClob", clob);

	}
	/**
	 * 상품 수정
	 * 
	 * @Method Name : updateProduct
	 * @author : eddie
	 * @date : 2016. 6. 12.
	 * @description :
	 *
	 * @param product
	 * @throws Exception
	 */
	public void updateProduct(PmsProduct product) throws Exception {

		// 기본값
		setProductDefault(product);

		// 1. [신규]제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 2. [신규]전체허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass

		// 3. [수정]전체허용 -> 제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 4. [수정]전체허용 -> 허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass
		// 5. [수정]제한 -> 허용 : controlNo(not null), ccsControl(null), isAllPermit(Y) => ccs_control 삭제
		// 6. [수정]제한 -> 제한(내용변경 없음) : controlNo(not null), ccsControl(null), isAllPermit(N) => by pass
		// 7. [수정]제한 -> 제한(내용변경 있음) : controlNo(not null), ccsControl(not null), isAllPermit(N)

		// 0. 허용설정
		BigDecimal newControlNo = null;
		BigDecimal deleteControlNo = null;
		boolean isControlDelete = product.getControlNo() != null && "Y".equals(product.getIsAllPermit());
		boolean isControlSet = product.getCcsControl() != null && "N".equals(product.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(product.getControlNo(), product.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = product.getControlNo();
			product.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			product.setControlNo(newControlNo);
		}

		// clob은 별도 update
//		String claimInfo = product.getClaimInfo();
//		String detail = product.getDetail();
//		product.setClaimInfo("");
//		product.setDetail("");

		// 1. 상품 (pms_product) 수정
		dao.updateOneTable(product);

		// clob update
//		product.setClaimInfo(claimInfo);
//		product.setDetail(detail);
//		updatePmsProductClob(product);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}


		// 2. 단품 정보 수정 : 승인 이후 단품의 옵션정보 변경 불가
		if (PmsUtil.isApproval(product.getSaleStateCd())) {
			// 자사 : pms_saleproduct의 update만 가능
			// 입점 : pms_saleproduct의 update, pms_saleproductoptionvalue 추가 가능

			for (BasePmsSaleproduct saleproduct : product.getPmsSaleproducts()) {
				saleproduct.setProductId(product.getProductId());
				saleproduct.setStoreId(SessionUtil.getStoreId());

				if (CommonUtil.isEmpty(saleproduct.getSaleproductId())) {
					// 2-1.단품, 단품 옵션 등록
					insertSaleproduct(saleproduct, product.getName());

				} else {
					// 2-2. 단품, 단품 옵션 (pms_saleproduct, pms_saleproductoptionvalue) 수정
					updateSaleproduct(saleproduct);
				}
			}
		} else {

			// 승인 전 : 모든 수정 가능함

			// 단품정보가 변경되었으면
			// if ("Y".equals(product.getChangeSaleproductYn())) {

			// 2-1. 단품, 단품옵션, 상품옵션 삭제
			dao.delete("pms.product.deleteSaleproductoptionvalueByProductId", product);
			dao.delete("pms.product.deleteSaleproduct", product);
			dao.delete("pms.product.deleteProductoption", product);

			// 2-2. 단품 정보 등록
			insertSaleproducts(product);

			// 2-3. 상품 옵션 정보 등록
			if (product.getPmsProductoptions() != null && product.getPmsProductoptions().size() > 0) {
				for (PmsProductoption option : product.getPmsProductoptions()) {
					option.setProductId(product.getProductId());
					option.setStoreId(SessionUtil.getStoreId());
					option.setInsId(SessionUtil.getLoginId());
					dao.insertOneTable(option);
				}
			}
		}

		// 3.상품속성 (pms_productattribute) 등록
		// 3-1. 기존 상품속성 삭제
		dao.delete("pms.product.deleteProductAttribute", product);

		// 3-2. 상품속성 등록
		if (product.getPmsProductattributes() != null) {
			for (BasePmsProductattribute attribute : product.getPmsProductattributes()) {
				attribute.setProductId(product.getProductId());
				attribute.setStoreId(SessionUtil.getStoreId());
				attribute.setInsId(SessionUtil.getLoginId());
				if (CommonUtil.isNotEmpty(attribute.getAttributeValue())) {
					dao.insertOneTable(attribute);
				}
			}
		}

		// 4. 상품고시정보 ( pms_productnotice) 등록
		// 4-1. 기존 고시정보 삭제
		dao.delete("pms.product.deleteProductNotice", product);
		if (product.getPmsProductnotices() != null) {
			// 4-2. 고시정보 등록
			for (BasePmsProductnotice notice : product.getPmsProductnotices()) {
				notice.setProductId(product.getProductId());
				notice.setStoreId(SessionUtil.getStoreId());
				notice.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(notice);
			}
		}

		// 5. 전시카테고리 등록
		// 5-1. 기존 전시카테고리 삭제
		dao.delete("dms.category.deleteDisplayCategoryProduct", product);

		// 5-2. 전시카테고리 등록
		for (BaseDmsDisplaycategoryproduct category : product.getDmsDisplaycategoryproducts()) {
			category.setProductId(product.getProductId());
			category.setStoreId(SessionUtil.getStoreId());
			category.setInsId(SessionUtil.getLoginId());
			dao.insertOneTable(category);
		}

		// 6. 세트 구성상품 등록
		// 6-1. 기존 세트 구성상품 삭제
		if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) {
			dao.delete("pms.product.deleteSetProduct", product);
			for (PmsSetproduct set : product.getPmsSetproducts()) {
				set.setProductId(product.getProductId());
				set.setStoreId(SessionUtil.getStoreId());
				set.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(set);
			}
		}

		// 7. 월령정보 수정
		dao.delete("pms.product.deleteProductages", product);
		if (product.getPmsProductages() != null) {
			for (PmsProductage age : product.getPmsProductages()) {
				age.setProductId(product.getProductId());
				age.setStoreId(SessionUtil.getStoreId());
				age.setInsId(SessionUtil.getLoginId());
				dao.insertOneTable(age);
			}
		}

	}



	/**
	 * 상품 상세 조회
	 * 
	 * @Method Name : getProductDetail
	 * @author : eddie
	 * @date : 2016. 5. 31.
	 * @description :
	 *
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PmsProduct getProductDetail(String productId) throws Exception {

		// 1. 상품 마스터 조회
		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());
		PmsProduct product = (PmsProduct) dao.selectOne("pms.product.getProductDetail", search);
		// 브랜드 조회
		if (CommonUtil.isNotEmpty(product.getBrandId())) {
			PmsBrand brandSearch = new PmsBrand();
			brandSearch.setStoreId(SessionUtil.getStoreId());
			brandSearch.setBrandId(product.getBrandId());
			PmsBrand brand = (PmsBrand) dao.selectOneTable(brandSearch);
			product.setPmsBrand(brand);
		}
		// 2. 표준 카테고리 조회
		PmsCategorySearch cateSearch = new PmsCategorySearch();
		cateSearch.setStoreId(SessionUtil.getStoreId());
		cateSearch.setLangCd("KOR");
		cateSearch.setCategoryId(product.getCategoryId());
		PmsCategory pmsCategory = (PmsCategory) dao.selectOne("pms.category.getPmsCategoryOne", cateSearch);
		product.setPmsCategory(pmsCategory);

		// 3. 전시 카테고리 조회
		PmsProductSearch disCateSearch = new PmsProductSearch();
		disCateSearch.setStoreId(SessionUtil.getStoreId());
		disCateSearch.setLangCd("KOR");
		disCateSearch.setProductId(productId);
		List<DmsDisplaycategoryproduct> dmsDisplaycategorys = (List<DmsDisplaycategoryproduct>) dao.selectList("dms.category.getProductCategorys",
				disCateSearch);

		product.setDmsDisplaycategoryproducts(dmsDisplaycategorys);

		// 4. 단품 정보 조회
		PmsProductSearch prdSearch = new PmsProductSearch();
		prdSearch.setStoreId(SessionUtil.getStoreId());
		prdSearch.setProductId(productId);
		List<PmsSaleproduct> pmsSaleproducts = (List<PmsSaleproduct>) dao.selectList("pms.product.selectSaleproductsWithOptions", prdSearch);
		product.setPmsSaleproducts(pmsSaleproducts);

		// 4-1. 상품 옵션정보 조회(pms_productoption)
		List<PmsProductoption> options = (List<PmsProductoption>) dao.selectList("pms.product.getProductOptionList", prdSearch);
		product.setPmsProductoptions(options);

		// 5. 고시정보 유형 목록 조회
		prdSearch.setProductNoticeTypeCd(product.getProductNoticeTypeCd());
		product.setPmsProductnotices((List<PmsProductnotice>) dao.selectList("pms.product.selectProductNoticeList", prdSearch));

		// 6. 배송 정책 정보 조회
		if (product.getDeliveryPolicyNo() != null) {
			CcsDeliverypolicy policySearch = new CcsDeliverypolicy();
			policySearch.setDeliveryPolicyNo(product.getDeliveryPolicyNo());
			CcsDeliverypolicy deliverypolicy = (CcsDeliverypolicy) dao.selectOneTable(policySearch);
			product.setCcsDeliverypolicy(deliverypolicy);
		}

		// 7. 상품 속성 목록 조회
		product.setPmsProductattributes((List<PmsProductattribute>) dao.selectList("pms.product.selectProductAttributeList", prdSearch));

		// 8. 업체 정보 조회
		CcsBusiness busi = new CcsBusiness();
		busi.setBusinessId(product.getBusinessId());
		busi.setStoreId(SessionUtil.getStoreId());
		product.setCcsBusiness((CcsBusiness) dao.selectOneTable(busi));

		// 9. 월령정보 조회
		List<PmsProductage> pmsProductages = (List<PmsProductage>) dao.selectList("pms.product.getProductages", prdSearch);
		product.setPmsProductages(pmsProductages);

		// 10. 세트이면 구성상품 목록 조회 : 그리드 로드 후 rest로 조회
		return product;
	}


	/**
	 * 상품 고시정보 목록 조회
	 * 
	 * @Method Name : selectProductNoticeList
	 * @author : Administrator
	 * @date : 2016. 6. 9.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProductnotice> selectProductNoticeList(PmsProductSearch search) {
		return (List<PmsProductnotice>) dao.selectList("pms.product.selectProductNoticeList", search);
	}

	/**
	 * 
	 * @Method Name : getApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 상품 승인 목록 조회
	 *
	 * @param pmsProductSearchpms.product.getPriceApprovalList
	 * @return List<PmsProduct>
	 */
	public List<PmsProduct> getApprovalList(PmsProductSearch pmsProductSearch) {
		// 상품 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(pmsProductSearch.getProductIds())) {
			String temp = pmsProductSearch.getProductIds();
			pmsProductSearch = new PmsProductSearch();
			pmsProductSearch.setProductIds(temp);
		}
		pmsProductSearch.setStoreId(SessionUtil.getStoreId());
		return (List<PmsProduct>) dao.selectList("pms.product.getApprovalList", pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updateProductList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 품목고시정보 확인목록 저장
	 *
	 * @param pmsProductList
	 * @throws Exception
	 */
	public void updateNoticeConfirmList(List<PmsProduct> pmsProductList) throws Exception {

		for (PmsProduct pmsProduct : pmsProductList) {
			pmsProduct.setStoreId(SessionUtil.getStoreId());
			pmsProduct.setUpdId(SessionUtil.getLoginId());

			if ("Y".equals(pmsProduct.getNoticeConfirmYn())) {
				pmsProduct.setNoticeConfirmDt(BaseConstants.SYSDATE);
				pmsProduct.setNoticeConfirmId(SessionUtil.getLoginId());
			}
			dao.updateOneTable(pmsProduct);
		}
	}

	/**
	 * 
	 * @Method Name : updateProductState
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 상품 상태 수정
	 *
	 * @param pmsProductList
	 * @throws Exception
	 */
	public void updateProductState(List<PmsProduct> pmsProductList) throws Exception {

		for (PmsProduct pmsProduct : pmsProductList) {
			pmsProduct.setStoreId(SessionUtil.getStoreId());
			pmsProduct.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(pmsProduct);

			// 판매중일 경우 ERP 연동
			if (BaseConstants.SALE_STATE_CD_SALE.equals(pmsProduct.getSaleStateCd())) {
				erpService.insertErpProduct(pmsProduct.getProductId());
			}
		}
//
//			String orgSaleStateCd = pmsProduct.getSaleStateCd();
//			if (CommonUtil.isNotEmpty(rejectReason)) {
//				pmsProduct.setRejectReason(rejectReason);
//			}
//
//			// 1차승인 상품이고 현상태가 요청이고, 1차승인 요청하면 => 판매중
//			if ("N".equals(pmsProduct.getPmsCategory().getSecondApprovalYn()) && BaseConstants.SALE_STATE_CD_REQ.equals(orgSaleStateCd)
//					&& BaseConstants.SALE_STATE_CD_APPROVAL1.equals(saleStateCd)) {
//				pmsProduct.setSaleStateCd(BaseConstants.SALE_STATE_CD_SALE);
//			}
//			// 2차 reject일경우
//			else if (BaseConstants.SALE_STATE_CD_REJECT2.equals(saleStateCd)) {
//				if (BaseConstants.SALE_STATE_CD_REQ.equals(orgSaleStateCd)) {
//					pmsProduct.setSaleStateCd(BaseConstants.SALE_STATE_CD_REJECT1);
//				} else if (BaseConstants.SALE_STATE_CD_APPROVAL1.equals(orgSaleStateCd)) {
//					pmsProduct.setSaleStateCd(BaseConstants.SALE_STATE_CD_REJECT2);
//				} else {
//					pmsProduct.setSaleStateCd(saleStateCd);
//				}
//			} else {// 그외의 경우는 요청 상태로 update
//				pmsProduct.setSaleStateCd(saleStateCd);
//			}
//			dao.update("pms.product.updateProductApproval", pmsProduct);
//
//			logger.debug("@@ saleStateCd :" + saleStateCd + ", orgSaleStateCd : " + orgSaleStateCd);
//
//			// 판매중일 경우 ERP 연동
//			if (BaseConstants.SALE_STATE_CD_SALE.equals(pmsProduct.getSaleStateCd())) {
//				erpService.insertErpProduct(pmsProduct.getProductId());
//			}
//
//		}
	}

	/**
	 * 
	 * @Method Name : getPriceApprovalList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 가격변경 승인 목록 조회
	 *
	 * @param pmsProductSearch
	 * @return List<PmsProduct>
	 */
	public List<PmsProduct> getPriceApprovalList(PmsProductSearch pmsProductSearch) {
		// 상품 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(pmsProductSearch.getProductIds())) {
			String temp = pmsProductSearch.getProductIds();
			pmsProductSearch = new PmsProductSearch();
			pmsProductSearch.setProductIds(temp);
		}
		pmsProductSearch.setStoreId(SessionUtil.getStoreId());

		return (List<PmsProduct>) dao.selectList("pms.product.getPriceApprovalList", pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : updatePricereserveList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 가격변경 승인 수정
	 *
	 * @param pmsPricereseveList
	 * @throws Exception
	 */
	public void updatePricereserveList(List<PmsPricereserve> pmsPricereseveList) throws Exception {
		for (PmsPricereserve pmsPricereserve : pmsPricereseveList) {
			if (BaseConstants.PRICE_RESERVE_STATE_CD_APPROVAL.equals(pmsPricereserve.getPriceReserveStateCd())) { // 승인일때 일자.
				pmsPricereserve.setApprovalDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
			}
			dao.updateOneTable(pmsPricereserve);
		}
	}

	/**
	 * 
	 * @Method Name : updatePricereserveList
	 * @author : dennis
	 * @date : 2016. 5. 31.
	 * @description : 가격변경 승인 수정
	 *
	 * @param pmsPricereseveList
	 * @throws Exception
	 */
	public void updatePricereserveList(List<PmsPricereserve> pmsPricereseveList, String priceReserveStateCd, String rejectReason)
			throws Exception {
		for (PmsPricereserve pmsPricereserve : pmsPricereseveList) {
			if (CommonUtil.isNotEmpty(rejectReason)) {
				pmsPricereserve.setRejectReason(rejectReason);
			}
			if (BaseConstants.PRICE_RESERVE_STATE_CD_APPROVAL.equals(priceReserveStateCd)) { // 승인일때 일자.
				pmsPricereserve.setApprovalDt(BaseConstants.SYSDATE);
			}
			pmsPricereserve.setPriceReserveStateCd(priceReserveStateCd);
			pmsPricereserve.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(pmsPricereserve);

		}
	}

	/**
	 * 상품 가격 예약 목록(상품 상세)
	 * 
	 * @Method Name : selectProductPriceReserveList
	 * @author : eddie
	 * @date : 2016. 6. 3.
	 * @description :
	 *
	 * @param search
	 */
	public List<PmsPricereserve> selectProductPriceReserveList(PmsProductSearch search) {
		return (List<PmsPricereserve>) dao.selectList("pms.product.selectProductPriceReserveList", search);
	}

	/**
	 * 가격 예약 등록
	 * 
	 * @Method Name : saveProductPriceReserve
	 * @author : eddie
	 * @date : 2016. 6. 5.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveProductPriceReserves(List<PmsPricereserve> reserve) throws Exception {
		for (PmsPricereserve r : reserve) {
			this.insertPmsPricereserve(r);
		}
	}

	/**
	 * 
	 * @Method Name : getProductPriceHistoryList
	 * @author : emily
	 * @date : 2016. 6. 7.
	 * @description : 상품가격 이력조회 팝업
	 *
	 * @param search
	 * @return
	 */
	public List<PmsPricereserve> getProductPriceHistoryList(PmsProductSearch search) {
		return (List<PmsPricereserve>) dao.selectList("pms.product.getPriceHistoryList", search);
	}

	/**
	 * 
	 * @Method Name : getSalproductPriceList
	 * @author : emily
	 * @date : 2016. 6. 7.
	 * @description : 단품가격 이력조회 팝업
	 *
	 * @param model
	 * @return
	 */
	public List<PmsSaleproductpricereserve> getSalproductPriceList(PmsProductSearch search) {
		return (List<PmsSaleproductpricereserve>) dao.selectList("pms.product.getSalePoructPriceList", search);
	}

	/**
	 * 
	 * @Method Name : getProductnoticefieldList
	 * @author : dennis
	 * @date : 2016. 6. 8.
	 * @description : 상품고시정보 항목 조회
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	public List<PmsProductnoticefield> getProductnoticefieldList(PmsProductSearch pmsProductSearch) {
		return (List<PmsProductnoticefield>) dao.selectList("pms.product.getProductnoticefieldList", pmsProductSearch);
	}

	/**
	 * 
	 * @Method Name : getProductnoticeList
	 * @author : dennis
	 * @date : 2016. 6. 8.
	 * @description : 상품고시정보 조회
	 *
	 * @param pmsProductSearch
	 * @return
	 */
	public List<PmsProduct> getProductnoticeList(PmsProductSearch pmsProductSearch) {
		// 상품 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(pmsProductSearch.getProductIds())) {
			String temp = pmsProductSearch.getProductIds();
			pmsProductSearch = new PmsProductSearch();
			pmsProductSearch.setProductIds(temp);
		}
		return (List<PmsProduct>) dao.selectList("pms.product.getProductnoticeList", pmsProductSearch);
	}

	public void updatePmsProductnotice(PmsProductnotice notice) throws Exception {
		dao.updateOneTable(notice);
	}

	public void insertExcelNewTx(Object model, String methodName) throws Exception {
		try {
			Method method = this.getClass().getMethod(methodName, model.getClass());
			ReflectionUtils.invokeMethod(method, this, model);
		} catch (NoSuchMethodException e) {
			String[] args = { methodName };
			String errMsg = MessageUtil.getMessage("ccs.common.error.nosuchmethod", args, LocaleUtil.getCurrentLocale());
			throw new NoSuchMethodException(errMsg);
		} catch (SecurityException e) {
			throw new SecurityException(e.getMessage());
		}
	}

	public void updateExcelNewTx(PmsProduct product) throws Exception {
		try {
			product.setStoreId(SessionUtil.getStoreId());
			product.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(product);
		} catch (Exception e) {
			throw new NoSuchMethodException("error");
		}
	}

	private void saveOffshopImage(PmsProduct image) throws Exception {

		String productId = image.getProductId();

		//이미지 삭제
		if (CommonUtil.isEmpty(image.getOffshopImg())) {
			image.setOffshopImg("");
			image.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(image);
			return;
		}
		
		//매장컷 이미지 저장
		String newFileName = productId + "_" + DateUtil.getCurrentDate(DateUtil.FORMAT_10);
		String tempFilePath = image.getOffshopImg();

		// 임시 파일경로를 저장할 경로로 변경
		String newFilePath = PmsUtil.getImagePathDb() + FileUploadUtil.SEPARATOR + productId + FileUploadUtil.SEPARATOR
				+ newFileName + "." + FileUploadUtil.getFileExtension(tempFilePath);

		image.setOffshopImg(newFilePath);
		image.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(image);

		// temp -> real 로 이동
		try {
			FileUploadUtil.moveFile(tempFilePath, newFilePath);
		} catch (ServiceException e) {
			logger.error("@@ moveFileError : " + e.getMessage());
		}

	}

	/**
	 * 상품 이미지 업로드
	 * 
	 * @Method Name : saveProductImages
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description : 이미지 경로를 DB에 저장하고, temp에 있던 이미지를 real 경로로 복사한다.
	 *
	 * @param images
	 * @throws Exception
	 */
	public void saveProductImages(PmsProduct image) throws Exception {

		String productId = image.getProductId();

		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());



		// 매장컷 이미지 저장
		saveOffshopImage(image);

		if (image.getPmsProductimgs() == null || image.getPmsProductimgs() != null && image.getPmsProductimgs().size() == 0) {
			return;
		}

		String maxNoStr = (String) dao.selectOne("pms.product.getProductMaxImageNo", search);
		int maxNo = 0;
		if (CommonUtil.isEmpty(maxNoStr)) {
			maxNo = 1;

		} else {
			maxNo = Integer.parseInt(maxNoStr);
		}
		// 상품 이미지 저장
		for (PmsProductimg img : image.getPmsProductimgs()) {

			img.setProductId(productId);

			if (BaseConstants.CRUD_TYPE_DELETE.equals(img.getCrudType())) {
				// DB 삭제
				dao.deleteOneTable(img);

				// 파일 삭제
				try {
					FileUploadUtil.deleteFile(img.getDeleteImg());
				} catch (ServiceException e) {
					logger.error("@@ deleteFileError : " + e.getMessage());
				}
			}


			if (BaseConstants.CRUD_TYPE_CREATE.equals(img.getCrudType())) {

				int imgNo = 0;
				boolean isNew = false;
				if (CommonUtil.isEmpty(img.getImgNo())) {

					img.setImgNo(new BigDecimal(maxNo));
					isNew = true;
					imgNo = maxNo;
					maxNo++;

				} else {
					imgNo = img.getImgNo().intValue();
				}
				String newFileName = productId + "_" + imgNo;

				String tempFilePath = img.getImg();

				// 임시 파일경로를 저장할 경로로 변경
				String newFilePath = PmsUtil.getImagePathDb() + FileUploadUtil.SEPARATOR + productId + FileUploadUtil.SEPARATOR
						+ newFileName + "." + FileUploadUtil.getFileExtension(tempFilePath);

				img.setImg(newFilePath);
				img.setStoreId(SessionUtil.getStoreId());

				if (isNew || CommonUtil.isEmpty(maxNoStr) && new BigDecimal(0).compareTo(img.getImgNo())==0) {
					dao.insertOneTable(img);
				}
				// temp -> real 로 이동
				try {
					FileUploadUtil.moveFile(tempFilePath, newFilePath);
				} catch (ServiceException e) {
					logger.error("@@ moveFileError : " + e.getMessage());
				}

				// 이미지 퍼징
				if (!isNew) {
					logger.debug("### purge start #################################################################");

					String purgeDomain = Config.getString("image.purge.domain");
					String targetUrl = Config.getString("image.domain") + newFilePath;

					String arr1 = Config.getString("upload.product.resize");
					
					String[] sizeArray = arr1.split(",");

					for (String size : sizeArray) {

						// 이미지 퍼지
						String purgeUrl = purgeDomain + "/erase?/cmd/LB_" + size + "x" + size + "/Q_100/src/" + targetUrl;

						// 문자열로 URL 표현
						logger.debug("@ purgeUrl :" + purgeUrl);

						URL url = new URL(purgeUrl);

						// HTTP Connection 구하기 
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();

						conn.setConnectTimeout(2000); // 2초 
						conn.setReadTimeout(2000); // 2초 
						conn.getInputStream();

					}
				}

			}
		}
	}

	/**
	 * 상품 이미지 정보 조회
	 * 
	 * @Method Name : getProductImages
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProductimg> getProductImages(PmsProductSearch search) {
		return (List<PmsProductimg>) dao.selectList("pms.product.getProductImages", search);
	}

	/**
	 * 
	 * @Method Name : getSaleProductList
	 * @author : allen
	 * @date : 2016. 6. 17.
	 * @description : 단품 목록 조회
	 *
	 * @param productSearch
	 * @return
	 */
	public List<PmsSaleproduct> getSaleProductList(PmsProductSearch productSearch) {
		return (List<PmsSaleproduct>) dao.selectList("pms.product.selectSaleproductList", productSearch);
	}

	/**
	 * 세트상품의 구성상품 목록 조회
	 * 
	 * @Method Name : selectSetproductList
	 * @author : eddie
	 * @date : 2016. 6. 20.
	 * @description :
	 *
	 * @param productSearch
	 * @return
	 */
	public List<PmsSetproduct> selectSetproductList(PmsProductSearch productSearch) {
		return (List<PmsSetproduct>) dao.selectList("pms.product.selectSetproductList", productSearch);
	}

	/**
	 * 상품 가격 변경 배치 처리
	 * 
	 * @Method Name : updatePriceReserve
	 * @author : eddie
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @throws Exception
	 */
	public void updatePriceReserve(String storeId, String updId) throws Exception {

		// 예약 목록 조회
		List<BasePmsPricereserve> list = (List<BasePmsPricereserve>) dao.selectList("pms.product.getPriceReserveTargetList", storeId);

		for (BasePmsPricereserve reserve : list) {

			// 단품에 반영(pms_saleproduct)
			if (reserve.getPmsSaleproductpricereserves() != null) {
				for (PmsSaleproductpricereserve r : reserve.getPmsSaleproductpricereserves()) {
					BasePmsSaleproduct s = new BasePmsSaleproduct();
					s.setUpdId(updId);
					s.setAddSalePrice(r.getAddSalePrice());
					s.setProductId(reserve.getProductId());
					s.setSaleproductId(r.getSaleproductId());
					s.setStoreId(storeId);
					dao.updateOneTable(s);
				}
			}

//			String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

			BasePmsProduct product = new BasePmsProduct();
			product.setUpdId(updId);
			product.setStoreId(storeId);
			product.setProductId(reserve.getProductId());
			product.setListPrice(reserve.getListPrice());
			product.setSupplyPrice(reserve.getSupplyPrice());
			product.setSalePrice(reserve.getSalePrice());
			product.setPointSaveRate(reserve.getPointSaveRate());
			product.setCommissionRate(reserve.getCommissionRate());
			product.setPriceApplyDt(BaseConstants.SYSDATE);
			product.setRegularDeliveryPrice(reserve.getRegularDeliveryPrice());
			// 상품에 반영 ( pms_product )
			dao.updateOneTable(product);

			// 완료 처리 ( pms_pricereserve )
			BasePmsPricereserve update = new BasePmsPricereserve();
			update.setStoreId(storeId);
			update.setUpdId(BaseConstants.DEFAULT_BATCH_USER_ID);
			update.setProductId(reserve.getProductId());
			update.setPriceReserveNo(reserve.getPriceReserveNo());
			update.setPriceReserveStateCd(BaseConstants.PRICE_RESERVE_STATE_CD_COMPLETE);
			update.setCompleteDt(BaseConstants.SYSDATE);
			dao.updateOneTable(update);
		}
	}

	public void updateProductReserve(String storeId, String updId) throws Exception {

		// 예약 목록 조회
		List<BasePmsProductreserve> list = (List<BasePmsProductreserve>) dao.selectList("pms.product.getProductReserveTargetList", storeId);

		for (BasePmsProductreserve reserve : list) {

			String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

			BasePmsProduct product = new BasePmsProduct();
			product.setUpdId(updId);
			product.setStoreId(storeId);
			product.setProductId(reserve.getProductId());
			product.setAdCopy(reserve.getAdCopy());
			product.setSaleStateCd(reserve.getSaleStateCd());

			// 상품에 반영 ( pms_product )
			dao.updateOneTable(product);

			// 완료 처리 ( pms_productreserve )
			BasePmsProductreserve update = new BasePmsProductreserve();
			update.setStoreId(storeId);
			update.setProductId(reserve.getProductId());
			update.setProductReserveNo(reserve.getProductReserveNo());

			update.setUpdId(BaseConstants.DEFAULT_BATCH_USER_ID);
			update.setProductReserveStateCd(BaseConstants.PRODUCT_RESERVE_STATE_CD_COMPLETE);
			update.setCompleteDt(currentDate);
			dao.updateOneTable(update);
		}
	}

	/**
	 * 단품 가격 예약 신청 목록 조회
	 * 
	 * @Method Name : getSaleproductPriceReserveList
	 * @author : eddie
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param productSearch
	 * @return
	 */
	public List<PmsSaleproductpricereserve> getSaleproductPriceReserveList(PmsProductSearch productSearch) {
		return (List<PmsSaleproductpricereserve>) dao.selectList("pms.product.getSaleproductPriceReserveList", productSearch);
	}

	/**
	 * 상품 가격 예약 삭제
	 * 
	 * @Method Name : deleteProductPriceReserves
	 * @author : eddie
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	public void deleteProductPriceReserves(List<PmsPricereserve> reserves) throws Exception {
		for (PmsPricereserve r : reserves) {
			dao.delete("deleteSaleProductPriceReserve", r);

			r.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(r);
		}
	}

	/**
	 * 상품 정보 예약 이력 조회
	 * 
	 * @Method Name : getProductReserveList
	 * @author : eddie
	 * @date : 2016. 7. 21.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProductreserve> getProductReserveList(PmsProductSearch search) {
		return (List<PmsProductreserve>) dao.selectList("pms.product.getProductReserveList", search);
	}

	/**
	 * 
	 * @Method Name : getOffshopstock
	 * @author : dennis
	 * @date : 2016. 8. 2.
	 * @description : 단품 매장재고 조회
	 *
	 * @param pmsOffshopstock
	 * @return
	 */
	public PmsOffshopstock getOffshopstock(PmsOffshopstock pmsOffshopstock) {
		return (PmsOffshopstock) dao.selectOne("pms.product.getOffshopstock", pmsOffshopstock);
	}

	/**
	 * @Method Name : getProductBulk
	 * @author : ian
	 * @date : 2016. 8. 3.
	 * @description : 엑셀 업로드 상품
	 *
	 * @param pmsProduct
	 * @return
	 */
	public PmsProduct getProductBulk(PmsProduct pmsProduct) {
		return (PmsProduct) dao.selectOne("pms.product.getProductBulk", pmsProduct);
	}



	public List<PmsSaleproductoptionvalue> selectSaleproductOptionvalues(PmsProductSearch search) {
		return (List<PmsSaleproductoptionvalue>) dao.selectList("pms.product.selectSaleproductOptionvalues", search);
	}

	/**
	 * 상품 상태 변경
	 * 
	 * @Method Name : updateProductState
	 * @author : eddie
	 * @date : 2016. 8. 31.
	 * @description : 아래와 같은 순서로 실행되어 상품의 상태를 최신화 한다.<br/>
	 *              1. [일반] 모든 단품의 상태가 '판매중지'일 경우 판매중, 품절 -> 일시중지<br/>
	 *              2. [일반] 판매중인 1개 이상의 단품 재고가 0이상 품절 -> 판매중<br/>
	 *              3. [일반] 모든 판매중 상태의 단품의 재고가 0일 경우 판매중 -> 품절<br/>
	 *              4. [세트] 한 개 이상의 구성상품 상태가 판매중,품절이 아닐때 판매중, 품절 -> 일시중지<br/>
	 *              5. [세트] 구성상품이 하나이상 품절일경우 판매중 -> 품절<br/>
	 *              6. [세트] 모든 구성상품이 판매중일 경우 품절 -> 판매중<br/>
	 *              <br/>
	 * @param product storeId(필수), updId(필수), productId (옵션) - productId가 없을 경우 전체 상품을 대상으로 한다.
	 * @throws Exception
	 */
	public void updateProductState(PmsProduct product) throws Exception {

//		List<String> list = (List<String>) dao.selectList("pms.product.getSalestopTargetProductList", product);

//		for (String productId : list) {
//			PmsProduct p = new PmsProduct();
//			p.setProductId(productId);
//			p.setStoreId(product.getStoreId());
//			p.setUpdId(product.getUpdId());
//			p.setSaleStateCd(BaseConstants.SALE_STATE_CD_STOP);
//			dao.updateOneTable(p);
//		}

		dao.update("pms.product.updateProductState", product);
	}

	/**
	 * 상품의 판매 상태 현행화
	 * 
	 * @Method Name : updateProductStateOne
	 * @author : eddie
	 * @date : 2016. 8. 31.
	 * @description : 일반상품의 상태를 현행화 한다.<br/>
	 *              세트의 구성상품인 경우는 세트상품의ID의 상태도 함께 현행화 한다.
	 *
	 * @param search storeId(필수), productId (필수), updId(필수),
	 * @throws ServiceException 일반상품의ID가 아닐경우
	 */
	public void updateProductStateOne(PmsProductSearch search) throws ServiceException, Exception {

		// 상품 상세 조회
		PmsProduct product = new PmsProduct();
		product.setProductId(search.getProductId());
		product.setStoreId(search.getStoreId());
		product = (PmsProduct)dao.selectOneTable(product);

		// 일반상품이 아닌경우 에러발생
		if (product == null || product != null && !BaseConstants.PRODUCT_TYPE_CD_GENERAL.equals(product.getProductTypeCd())) {
			throw new ServiceException("pms.product.type.general.error");
		}

		// 일반 상품의 상태 변경
		product = new PmsProduct();
		product.setProductId(search.getProductId());
		product.setStoreId(search.getStoreId());
		updateProductState(product);

		// 세트의 구성인지 체크하여 세트상품ID조회
		List<String> setProductList = (List<String>) dao.selectList("pms.product.getSetProductIdList", search);

		// 세트상품의 상태 변경
		if (setProductList != null && setProductList.size() > 0) {
			for (String setProductId : setProductList) {
				product = new PmsProduct();
				product.setProductId(setProductId);
				product.setStoreId(search.getStoreId());
				updateProductState(product);
			}
		}
	}

	/**
	 * 상품집계테이블 현행화(PMS_PRODUCTSUMMARY)
	 * 
	 * @Method Name : updateProductsummary
	 * @author : eddie
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @throws ServiceException
	 * @throws Exception
	 */
	public void updateProductsummary(PmsProduct search) throws ServiceException, Exception {
		dao.update("pms.product.updateProductsummary", search);
	}

	/**
	 * 상품 가격 생성 배치
	 * 
	 * @Method Name : updateProductprice
	 * @author : eddie
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @throws ServiceException
	 * @throws Exception
	 */
	public void updateProductprice(PmsProduct search) throws ServiceException, Exception {
		dao.update("pms.product.updateProductprice", search);
	}

//	/**
//	 * 단품별 픽업 가능한 지역1 목록 조회
//	 * 
//	 * @Method Name : getAreaDiv1ListByOption
//	 * @author : eddie
//	 * @date : 2016. 9. 2.
//	 * @description :
//	 *
//	 * @param search
//	 * @return
//	 */
//	public List<PmsOffPickup> getAreaDiv1ListByOption(PmsProductSearch search) {
//		return (List<PmsOffPickup>) dao.selectList("pms.product.getAreaDiv1ListByOption", search);
//	}

	public Map<String, Object> getOptionList(PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		// 단품번호로 옵션값 조회
		List<PmsSaleproductoptionvalue> selectedOptions = selectSaleproductOptionvalues(search);

		Map<String, String> selectedOptionMap = new HashMap<String, String>();
		// 선택된 옵션값을 맵으로 만든다
		for (PmsSaleproductoptionvalue selected : selectedOptions) {
			selectedOptionMap.put(selected.getOptionName(), selected.getOptionValue());
		}

		List<PmsProductoption> optionList = getPmsProductOptionList(search);

		// 첫번째 옵션 목록 조회
		PmsProductSearch s = new PmsProductSearch();
		s.setStoreId(search.getStoreId());
		s.setProductId(search.getProductId());
		if (optionList.size() > 0) {
			setOptionList(optionList.get(0), selectedOptionMap, s);
		}

		// 첫번째 이후 옵션 목록 조회
		List<PmsSaleproductoptionvalue> selectedParam = new ArrayList<PmsSaleproductoptionvalue>();

		for (int i = 1; i < optionList.size(); i++) {

			// 필터세팅 : 상위 콤보의 선택값 세팅
			PmsSaleproductoptionvalue v = new PmsSaleproductoptionvalue();
			v.setOptionName(optionList.get(i - 1).getOptionName());
			v.setOptionValue(selectedOptionMap.get(optionList.get(i - 1).getOptionName()));
			selectedParam.add(v);
			s.setSelectedOptions(selectedParam);

			setOptionList(optionList.get(i), selectedOptionMap, s);

		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("optionList", optionList);
		resultMap.put("selectedOptionMap", selectedOptionMap);

		return resultMap;
	}

	private void setOptionList(PmsProductoption targetOption, Map<String, String> selectedOptionMap, PmsProductSearch search) {

		// 첫번째 옵션 목록 조회
		search.setTargetOptionName(targetOption.getOptionName());
		List<PmsOptionvalue> valueList = getOptionValueList(search);

		// 선택되어지게 처리
		for (PmsOptionvalue value : valueList) {
			if (value.getOptionValue().equals(selectedOptionMap.get(value.getOptionName()))) {
				value.setSelected("Y");
				break;
			}
		}
		targetOption.setOptionValues(valueList);

	}

	/**
	 * 상품 존재 여부
	 * 
	 * @Method Name : existProductCheck
	 * @author : roy
	 * @date : 2016. 10. 8.
	 * @description :
	 *
	 * @param search
	 */
	public int existProductCheck(PmsProduct search) {
		return (int) dao.selectOne("pms.product.existProductCheck", search);
	}

	/**
	 * 브랜드관 브랜드별 Best 상품
	 * 
	 * @Method Name : getBrandBestProduct
	 * @author : stella
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsProduct> getBrandBestProduct(PmsProductSearch search) throws Exception {
		return (List<PmsProduct>) dao.selectList("pms.product.getBrandBestProduct", search);
	}

	public void deleteDisplayCategoryProduct(PmsProduct product) {
		dao.delete("dms.category.deleteDisplayCategoryProduct", product);
	}

	public void deleteProductages(PmsProductage product) {
		dao.delete("pms.product.deleteProductages", product);
	}
}
