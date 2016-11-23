package gcp.frontpc.controller.openApi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jd.open.api.sdk.internal.JSON.JSON;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsPolicy;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.service.CodeService;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.PolicyService;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.MmsStyleproduct;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.MemberService;
import gcp.mms.service.StyleService;
import gcp.pms.model.PmsStyleproduct;
import gcp.pms.model.search.PmsStyleProductSearch;
import gcp.pms.service.StyleProductService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;
import intune.gsf.model.FileInfo;

@RestController("ApiAppController")
@CrossOrigin(origins = "*")
@RequestMapping("app")
public class AppController extends BaseController {

	@Autowired
	private CodeService codeService;

	@Autowired
	private StyleProductService	styleProductService;

	@Autowired
	private StyleService		styleService;

	@Autowired
	private CommonService		commonService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private PolicyService		policyService;

	/**
	 * 
	 * @Method Name : styleshopInfo
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 스타일샵 앱 기초 데이터 조회
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/styleshopInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf8")
	public String styleshopInfo(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> resultCodeMap = new HashMap<String, String>();
		Map<String, Object> response = new HashMap<String, Object>();
		String code = "0";
		String msg = "success";
		// 김미정대리 요청
		// 알로앤루500359 / 알퐁소500362 / 포래즈500360 / 섀르반500361 
		String[] tempBrand = new String[] { "500359", "500362", "500360", "500361" };
		try {

			// 자사 브랜드 리스트
			CcsCodeSearch codeSearch = new CcsCodeSearch();
			codeSearch.setCdGroupCd("BRAND_CD");
			List<CcsCode> brandCodeList = codeService.selectCodeList(codeSearch);
			
			List<Map> brandList = new ArrayList<Map>();
			for (CcsCode ccsCode : brandCodeList) {
				Map<String, String> brandMap = new HashMap<String, String>();
				if (ccsCode.getUseYn().equals(BaseConstants.YN_Y)) {
					for (int i = 0; i < tempBrand.length; i++) {
						if (ccsCode.getCd().equals(tempBrand[i])) {
							brandMap.put("id", ccsCode.getCd());
							brandMap.put("title", ccsCode.getName());
							brandList.add(brandMap);
						}
					}
				}
			}

			response.put("brands", brandList);

			codeSearch.setCdGroupCd("GENDER_TYPE_CD");
			List<CcsCode> genderCodeList = codeService.selectCodeList(codeSearch);

			List<Map> genderList = new ArrayList<Map>();
			for (CcsCode ccsCode : genderCodeList) {
				Map<String, String> gnderMap = new HashMap<String, String>();
				gnderMap.put("id", ccsCode.getCd());
				gnderMap.put("title", ccsCode.getName());
				genderList.add(gnderMap);
			}
			response.put("sex", genderList);

			// 테마 리스트
			codeSearch.setCdGroupCd("THEME_CD");
			List<CcsCode> themeCodeList = codeService.selectCodeList(codeSearch);

			List<Map> themeList = new ArrayList<Map>();
			for (CcsCode ccsCode : themeCodeList) {
				// 기타 테마 제외
				if (!ccsCode.getCd().equals("THEME_CD.ETC")) {
					Map<String, String> themeMap = new HashMap<String, String>();
					themeMap.put("id", ccsCode.getCd());
					themeMap.put("title", ccsCode.getName());
					themeList.add(themeMap);
				}
				
			}
			response.put("themes", themeList);

			// 스타일샵에 등록된 상품의 카테고리 리스트
			DmsDisplaySearch search = new DmsDisplaySearch();
			search.setStoreId(SessionUtil.getStoreId());
			search.setRootCategoryId(Config.getString("root.display.category.id"));
			List<DmsDisplaycategory> categoryProductList = styleProductService.getStyleCategoryProductList(search);

			List<Map> categoryList = new ArrayList<Map>();
			for (DmsDisplaycategory dmsDisplaycategory : categoryProductList) {
				Map<String, String> categoryMap = new HashMap<String, String>();
				categoryMap.put("id", dmsDisplaycategory.getDisplayCategoryId());
				categoryMap.put("title", dmsDisplaycategory.getCategoryName());
				categoryList.add(categoryMap);

			}
			response.put("categories", categoryList);

			// 스타일샵 상품 리스트
			DmsDisplaySearch dmsDisplaySearch = new DmsDisplaySearch();
			dmsDisplaySearch.setStoreId(SessionUtil.getStoreId());
			dmsDisplaySearch.setRootCategoryId(Config.getString("root.display.category.id"));
			List<PmsStyleproduct> styleProductList = styleProductService.getAppStyleProductList(dmsDisplaySearch);
			List<Map> productList = new ArrayList<Map>();

			for (PmsStyleproduct pmsStyleproduct : styleProductList) {
				Map<String, String> productMap = new HashMap<String, String>();
				productMap.put("id", pmsStyleproduct.getStyleProductNo().toString());
				productMap.put("productId", pmsStyleproduct.getProductId());
				productMap.put("categoryId", pmsStyleproduct.getDisplayCategoryId());
				productMap.put("brandId", pmsStyleproduct.getBrandId());
				productMap.put("color", pmsStyleproduct.getStyleProductColorCd());
				productMap.put("rate", String.valueOf(pmsStyleproduct.getPopularCnt()));
				productMap.put("register_date", pmsStyleproduct.getInsDt());
				productMap.put("image_width_percent", "30");
				productMap.put("image_height_percent", "30");
				productMap.put("image_url", Config.getString("image.domain") + pmsStyleproduct.getImg());
				productList.add(productMap);
			}
			response.put("items", productList);

			// 컬러 10개
			codeSearch.setCdGroupCd("STYLE_PRODUCT_COLOR_CD");
			List<CcsCode> colorCodeList = codeService.selectCodeList(codeSearch);

			List<Map> colorList = new ArrayList<Map>();
			for (CcsCode ccsCode : colorCodeList) {
				Map<String, String> colorMap = new HashMap<String, String>();
				colorMap.put("id", ccsCode.getCd());
				colorMap.put("title", ccsCode.getName());
				colorList.add(colorMap);
			}
			response.put("colors", colorList);

			// 꾸미기 아이템 리스트
			PmsStyleProductSearch styleProductSearch = new PmsStyleProductSearch();
			styleProductSearch.setStoreId(SessionUtil.getStoreId());
			styleProductSearch.setStyleProductItemCd("STYLE_PRODUCT_ITEM_CD.DECO");
			List<PmsStyleproduct> StyleList = styleProductService.getStyleProductList(styleProductSearch);
			
			List<Map> decoList = new ArrayList<Map>();
			for (PmsStyleproduct pmsStyleproduct : StyleList) {
				Map<String, String> decoMap = new HashMap<String, String>();
				decoMap.put("id", pmsStyleproduct.getStyleProductNo().toString());
				decoMap.put("register_date", pmsStyleproduct.getInsDt());
				decoMap.put("color", pmsStyleproduct.getStyleProductColorCd());
				decoMap.put("image_url", Config.getString("image.domain") + pmsStyleproduct.getImg());
				decoMap.put("image_width_percent", "30");
				decoMap.put("image_height_percent", "30");
				decoList.add(decoMap);
			}
			response.put("extraitems", decoList);

			// 모델 정보(남유아, 여유아, 남아동, 여아동)
			codeSearch.setCdGroupCd("STYLE_BACK_IMG_CD");
			List<CcsCode> modelCodeList = codeService.selectCodeList(codeSearch);

			List<Map> modelList = new ArrayList<Map>();
			for (int i = 0; i < modelCodeList.size(); i++) {
				Map<String, String> babyModelMap = new HashMap<String, String>();
				babyModelMap.put("id", modelCodeList.get(i).getCd());
				babyModelMap.put("title", modelCodeList.get(i).getName());
				babyModelMap.put("index", String.valueOf(i));
				babyModelMap.put("image_url", Config.getString("front.domain.url") + modelCodeList.get(i).getNote());

				StringBuffer sb = new StringBuffer(modelCodeList.get(i).getNote());
				int index = modelCodeList.get(i).getNote().indexOf(".");
				sb.insert(index, "thumb");
				babyModelMap.put("image_thumb_url", Config.getString("front.domain.url") + sb.toString());
				modelList.add(babyModelMap);
			}
			response.put("models", modelList);

		} catch (Exception e) {
			code = "1";
			msg = "fail";
		}
		
		resultCodeMap.put("code", code);
		resultCodeMap.put("msg", msg);
		response.put("error", resultCodeMap);
		resultMap.put("response", response);
		
		logger.debug(JSON.toString(resultMap));
		return JSON.toString(resultMap);
	}

	/**
	 * 
	 * @Method Name : styleshopInsert
	 * @author : intune
	 * @date : 2016. 10. 12.
	 * @description : 스타일 저장
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/styleshopInsert", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public String styleshopInsert(MultipartHttpServletRequest req) {

		// RequestParameter
		String styleNo = req.getParameter("styleNo");
		String memberId = req.getParameter("memberId");
		String content = req.getParameter("content");
		String productIds = req.getParameter("productIds");
		String title = req.getParameter("title");
		String gender = req.getParameter("sex");
		String theme = req.getParameter("theme");
		String brand = req.getParameter("brand");
		String json = req.getParameter("json");
		String displayYn = req.getParameter("displayYn");
		String referer = req.getHeader("referer");

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> resultCodeMap = new HashMap<String, String>();
		MmsStyle style = new MmsStyle();

		String code = "0";
		String msg = "success";

		try {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setReferer(referer);
			String uploadedPath = null;
			uploadedPath = commonService.imgFileUpload(req, fileInfo);

			CcsCodeSearch codeSearch = new CcsCodeSearch();
			codeSearch.setCdGroupCd("BRAND_CD");
			List<CcsCode> brandCodeList = codeService.selectCodeList(codeSearch);

			for (CcsCode ccsCode : brandCodeList) {
				if (ccsCode.getName().equals(brand)) {
					brand = ccsCode.getCd();
					break;
				}
			}

			codeSearch.setCdGroupCd("GENDER_TYPE_CD");
			List<CcsCode> genderCodeList = codeService.selectCodeList(codeSearch);

			for (CcsCode ccsCode : genderCodeList) {
				if (ccsCode.getName().equals(gender)) {
					gender = ccsCode.getCd();
				}
			}

			// 테마 리스트
			codeSearch.setCdGroupCd("THEME_CD");
			List<CcsCode> themeCodeList = codeService.selectCodeList(codeSearch);

			for (CcsCode ccsCode : themeCodeList) {
				if (ccsCode.getName().equals(theme)) {
					theme = ccsCode.getCd();
				}
			}

			String[] prdArr = StringUtils.split(productIds, ",");
			
			List<String> productList = new ArrayList<String>();
			prdArr = StringUtils.split(productIds, ",");
			
			for (int i = 0; i < prdArr.length; i++) {
				productList.add(prdArr[i]);
			}

			// HashSet 데이터 형태로 생성되면서 중복 제거됨
			HashSet hs = new HashSet(productList);
			// ArrayList 형태로 다시 생성
			ArrayList<String> newArrList = new ArrayList<String>(hs);

			MmsMember mmsMember = memberService.getMemberInfoByMemberId(memberId);
			
			if (mmsMember != null) {
				style.setMemberNo(mmsMember.getMemberNo());
				style.setTitle(title);
				style.setDetail(content);
				style.setGenderTypeCd(gender);
				style.setBrandId(brand);
				style.setThemeCd(theme);
				style.setStyleInfo(json);
				style.setStyleImg(FileUploadUtil.moveTempToReal(uploadedPath));

				if (BaseConstants.YN_N.equals(displayYn)) {
					style.setStyleStateCd("STYLE_STATE_CD.READY");
				} else {
					style.setStyleStateCd("STYLE_STATE_CD.RUN");
				}

				if (StringUtils.isNotEmpty(styleNo)) {
					// UPDATE
					style.setStyleNo(new BigDecimal(styleNo));
					style.setUpdId(memberId);
					styleService.updateMemberStyle(style);
				} else {
					// INSERT
					style.setMemberNo(mmsMember.getMemberNo());
					style.setLikeCnt(new BigDecimal(0));
					style.setDisplayYn(BaseConstants.YN_Y);
					style.setInsId(memberId);
					style.setUpdId(memberId);
					styleService.insertMemberStyle(style);
				}
				
				// MMS_STYLEPRODUCTINSERT
				if (StringUtils.isNotEmpty(styleNo)) {
					MmsStyleproduct product = new MmsStyleproduct();
//					product.setMemberNo(mmsMember.getMemberNo());
					product.setStyleNo(style.getStyleNo());
					styleService.deleteMemberStyleproduct(product);
				}
					
				List<MmsStyleproduct> mmsStyleproductList = new ArrayList<MmsStyleproduct>();
				for (String productId : newArrList) {
					MmsStyleproduct product = new MmsStyleproduct();
//					product.setMemberNo(mmsMember.getMemberNo());
					product.setStyleNo(style.getStyleNo());
					product.setStoreId(SessionUtil.getStoreId());
					product.setProductId(productId);
					mmsStyleproductList.add(product);
				}
				styleService.insertMemberStyleproduct(mmsStyleproductList);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = "1";
			msg = "fail";
		}

		resultCodeMap.put("code", code);
		resultCodeMap.put("msg", msg);
		response.put("error", resultCodeMap);
		response.put("styleNo", style.getStyleNo());

		logger.debug(JSON.toString(response));
		return JSON.toString(response);
	}

	/**
	 * 
	 * @Method Name : getStyleshop
	 * @author : intune
	 * @date : 2016. 10. 12.
	 * @description : 스타일 조회
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/getStyleshop", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public String getStyleshop(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> resultCodeMap = new HashMap<String, String>();
		Map<String, Object> response = new HashMap<String, Object>();

		String code = "0";
		String msg = "success";
		MmsStyle styleDetail = null;

		try {
			String styleNo = req.getParameter("styleNo");
			String memberId = req.getParameter("memberId");

			MmsMember mmsMember = memberService.getMemberInfoByMemberId(memberId);

			if (mmsMember != null) {
				MmsStyleSearch search = new MmsStyleSearch();
				search.setStoreId(SessionUtil.getStoreId());
				search.setStyleNo(styleNo);
				search.setMemberNo(mmsMember.getMemberNo());
				styleDetail = styleService.getMemberStyleDetail(search);

				if (styleDetail != null) {
					response.put("title", styleDetail.getDetail());
					response.put("content", styleDetail.getTitle());
					response.put("sex", styleDetail.getGenderTypeName());
					response.put("theme", styleDetail.getThemeName());
					response.put("brand", styleDetail.getBrandName());

					if ("STYLE_STATE_CD.READY".equals(styleDetail.getStyleStateCd())) {
						response.put("displayYn", BaseConstants.YN_N);
					} else {
						response.put("displayYn", BaseConstants.YN_Y);
					}
					response.put("json", String.valueOf(styleDetail.getStyleInfo()));
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			code = "1";
			msg = "fail";
		}
		resultCodeMap.put("code", code);
		resultCodeMap.put("msg", msg);

		response.put("error", resultCodeMap);
		resultMap.put("response", response);
		return JSON.toString(resultMap);
	}

	/**
	 * 
	 * @Method Name : appVersion
	 * @author : intune
	 * @date : 2016. 10. 24.
	 * @description :
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/appVersion", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public String appVersion(HttpServletRequest req, HttpServletResponse res) {

		String code = "0";
		String msg = "success";
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> androidMap = new HashMap<String, String>();
		Map<String, String> iosMap = new HashMap<String, String>();
		Map<String, String> resultCodeMap = new HashMap<String, String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// 업데이트 메세지
			String iosUpdMsg = MessageUtil.getMessage("ios.upd.msg", req.getLocale());
			String androidUpdMsg = MessageUtil.getMessage("android.upd.msg", req.getLocale());

			// 스토어 URL
			String androidAppUrl = Config.getString("android.app.url");
			String iosAppUrl = Config.getString("ios.app.url");

			// 안드로이드 앱 버전
			CcsPolicy ccsPolicy = new CcsPolicy();
			ccsPolicy.setStoreId(SessionUtil.getStoreId());
			ccsPolicy.setPolicyTypeCd(BaseConstants.POLICY_TYPE_CD_COMMON);
			ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_ANDROID_APP_VER);
			CcsPolicy anroidAppVer = policyService.getPolicy(ccsPolicy);
			// IOS 앱 버전
			ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_IOS_APP_VER);
			CcsPolicy iosAppVer = policyService.getPolicy(ccsPolicy);
			// 안드로이드 앱 강제업데이트 여부
			ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_ANDROID_FORCE_YN);
			CcsPolicy androidForceYn = policyService.getPolicy(ccsPolicy);
			// IOS 앱 강제업데이트 여부
			ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_IOS_FORCE_YN);
			CcsPolicy iosForceYn = policyService.getPolicy(ccsPolicy);

			if (anroidAppVer != null && androidForceYn != null) {
				androidMap.put("is_force", androidForceYn.getValue());
				androidMap.put("version", anroidAppVer.getValue());
				androidMap.put("update_message", androidUpdMsg);
				androidMap.put("redirect_url", androidAppUrl);
			}
			if (iosAppVer != null && iosForceYn != null) {
				iosMap.put("is_force", iosForceYn.getValue());
				iosMap.put("version", iosAppVer.getValue());
				iosMap.put("update_message", iosUpdMsg);
				iosMap.put("redirect_url", iosAppUrl);
			}
			resultMap.put("iphone", iosMap);
			resultMap.put("android", androidMap);
			resultMap.put("info", null);

		} catch (Exception e) {
			code = "1";
			msg = "fail";
		}

		resultCodeMap.put("code", code);
		resultCodeMap.put("msg", msg);
		resultMap.put("error", resultCodeMap);
		response.put("response", resultMap);
		logger.debug(JSON.toString(response));
		return JSON.toString(response);
	}
}
