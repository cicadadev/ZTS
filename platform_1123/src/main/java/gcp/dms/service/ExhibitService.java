package gcp.dms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsExhibitbrand;
import gcp.dms.model.DmsExhibitcoupon;
import gcp.dms.model.DmsExhibitdisplaycategory;
import gcp.dms.model.DmsExhibitgroup;
import gcp.dms.model.DmsExhibitmainproduct;
import gcp.dms.model.DmsExhibitoffshop;
import gcp.dms.model.DmsExhibitproduct;
import gcp.dms.model.base.BaseDmsExhibit;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsBrand;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class ExhibitService extends BaseService {
	
	@Autowired
	CommonService				commonService;

	@Autowired
	MemberService				memberService;

	private static final Logger logger = LoggerFactory.getLogger(ExhibitService.class);

	
	public BaseDmsExhibit getExhibitId() throws Exception {
		return (BaseDmsExhibit) dao.selectOne("dms.exhibit.getExhibitId", "");
	}
	
	

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
	public List<DmsExhibit> getExhibitList(DmsExhibitSearch search) throws Exception {
		return (List<DmsExhibit>) dao.selectList("dms.exhibit.getExhibitList", search);
	}
	
	/**
	 * 
	 * @Method Name : deleteExhibit
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 삭제
	 *
	 * @param exhibitIds
	 * @throws Exception
	 */
	public void deleteExhibit(List<DmsExhibit> dmsExhibitList) throws Exception {
		for (DmsExhibit dmsExhibit : dmsExhibitList) {

			// DMS_EXHIBITPRODUCT 삭제
			DmsExhibitproduct dmsExhibitProduct = new DmsExhibitproduct();
			dmsExhibitProduct.setExhibitId(dmsExhibit.getExhibitId());
			dmsExhibitProduct.setStoreId(SessionUtil.getStoreId());
			dao.delete("dms.exhibit.deleteExhibitproduct", dmsExhibitProduct);

			// DMS_EXHIBITGROUP 삭제
			DmsExhibitgroup dmsExhibitGroup = new DmsExhibitgroup();
			dmsExhibitGroup.setExhibitId(dmsExhibit.getExhibitId());
			dmsExhibitGroup.setStoreId(SessionUtil.getStoreId());
			dao.delete("dms.exhibit.deleteExhibitgroup", dmsExhibitGroup);

			if (BaseConstants.EXHIBIT_TYPE_CD_COUPON.equals(dmsExhibit.getExhibitTypeCd())) {
				dao.delete("dms.exhibit.deleteBatchExhibitCoupon", dmsExhibit);
			} else if (BaseConstants.EXHIBIT_TYPE_CD_ONEDAY.equals(dmsExhibit.getExhibitTypeCd())) {
				dao.delete("dms.exhibit.deleteBatchExhibitMainProduct", dmsExhibit);
			} else if (BaseConstants.EXHIBIT_TYPE_CD_OFFSHOP.equals(dmsExhibit.getExhibitTypeCd())) {
				dao.delete("dms.exhibit.deleteBatchExhibitOffshop", dmsExhibit);
			}

			// DMS_EXHIBITBRAND 삭제
			dao.delete("dms.exhibit.deleteExhibitBrand", dmsExhibit);
			// DMS_EXHIBITDISPLAYCATEGORY 삭제
			dao.delete("dms.exhibit.deleteExhibitDisplayCategory", dmsExhibit);
			// DMS_EXHIBIT 삭제
			dao.deleteOneTable(dmsExhibit);
		}
	}

	/**
	 * 
	 * @Method Name : updateExhibit
	 * @author : allen
	 * @date : 2016. 5. 23.
	 * @description : 기획전 업데이트
	 *
	 * @param dmsExhibitList
	 * @throws Exception
	 */
	public void updateExhibit(DmsExhibit dmsExhibit) throws Exception {

		dmsExhibit.setStoreId(SessionUtil.getStoreId());
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
		boolean isControlDelete = dmsExhibit.getControlNo() != null && "Y".equals(dmsExhibit.getIsAllPermit());
		boolean isControlSet = dmsExhibit.getCcsControl() != null && "N".equals(dmsExhibit.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(dmsExhibit.getControlNo(), dmsExhibit.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			deleteControlNo = dmsExhibit.getControlNo();
			dmsExhibit.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			dmsExhibit.setControlNo(newControlNo);
		}

		String exhibitId = dmsExhibit.getExhibitId();
		logger.debug("#### update ExhibitId = " + exhibitId);

		if (StringUtils.isNotEmpty(dmsExhibit.getImg1())) {
			dmsExhibit.setImg1(moveExhibitImage(dmsExhibit.getImg1()));
		}
		if (StringUtils.isNotEmpty(dmsExhibit.getImg2())) {
			dmsExhibit.setImg2(moveExhibitImage(dmsExhibit.getImg2()));
		}
		if (StringUtils.isEmpty(dmsExhibit.getHtml1())) {
			dmsExhibit.setHtml1("");
		}
		if (StringUtils.isEmpty(dmsExhibit.getHtml2())) {
			dmsExhibit.setHtml2("");
		}
		if (StringUtils.isEmpty(dmsExhibit.getSubHtml1())) {
			dmsExhibit.setSubHtml1("");
		}
		if (StringUtils.isEmpty(dmsExhibit.getSubHtml2())) {
			dmsExhibit.setSubHtml2("");
		}

		// 기획전 업데이트
		dao.updateOneTable(dmsExhibit);

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(deleteControlNo, null);
		}

		// 기획전 카테고리 업데이트
		DmsExhibitdisplaycategory category = new DmsExhibitdisplaycategory();
		category.setStoreId(SessionUtil.getStoreId());
		category.setExhibitId(exhibitId);
		if (StringUtils.isEmpty(dmsExhibit.getDisplayCategoryId())) {
			dao.delete("deleteExhibitDisplayCategory", category);
		} else {
			category.setDisplayCategoryId(dmsExhibit.getDisplayCategoryId());
			dao.delete("deleteExhibitDisplayCategory", category);
			dao.insertOneTable(category);
		}

		// 기획전 브랜드 삭제후 insert
		for (DmsExhibitbrand dmsExhibitBrand : dmsExhibit.getDmsExhibitbrands()) {
			dmsExhibitBrand.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(dmsExhibitBrand);
		}

		if (dmsExhibit.getBrandIds() != null) {
			for (int i = 0; i < dmsExhibit.getBrandIds().length; i++) {
				DmsExhibitbrand dmsExhibitBrand = new DmsExhibitbrand();
				dmsExhibitBrand.setStoreId(SessionUtil.getStoreId());
				dmsExhibitBrand.setExhibitId(exhibitId);
				dmsExhibitBrand.setBrandId(dmsExhibit.getBrandIds()[i]);
				dao.insertOneTable(dmsExhibitBrand);
			}
		} else {
			for (int i = 0; i < dmsExhibit.getDmsExhibitbrands().size(); i++) {
				if (dmsExhibit.getDmsExhibitbrands().get(i).getBrandId() != null) {
					DmsExhibitbrand dmsExhibitBrand = new DmsExhibitbrand();
					dmsExhibitBrand.setStoreId(SessionUtil.getStoreId());
					dmsExhibitBrand.setExhibitId(exhibitId);
					dmsExhibitBrand.setBrandId(dmsExhibit.getDmsExhibitbrands().get(i).getBrandId());
					dao.insertOneTable(dmsExhibitBrand);
				}
			}
		}

		// 대표 상품, 쿠폰정보 delete & insert
		// 기획전
		if (BaseConstants.EXHIBIT_TYPE_CD_ONEDAY.equals(dmsExhibit.getExhibitTypeCd())) {
			deleteBatchExhibitMainProduct(dmsExhibit);
			for (DmsExhibitmainproduct dmsExhibitmainproduct : dmsExhibit.getDmsExhibitmainproducts()) {
				dmsExhibitmainproduct.setStoreId(SessionUtil.getStoreId());
				dmsExhibitmainproduct.setExhibitId(exhibitId);
				dao.insertOneTable(dmsExhibitmainproduct);
			}
		}
		if (BaseConstants.EXHIBIT_TYPE_CD_COUPON.equals(dmsExhibit.getExhibitTypeCd())) {
			deleteBatchExhibitCoupon(dmsExhibit);
			for (DmsExhibitcoupon dmsExhibitcoupon : dmsExhibit.getDmsExhibitcoupons()) {
				dmsExhibitcoupon.setStoreId(SessionUtil.getStoreId());
				dmsExhibitcoupon.setExhibitId(exhibitId);
				dao.insertOneTable(dmsExhibitcoupon);
			}
		}
		if (BaseConstants.EXHIBIT_TYPE_CD_OFFSHOP.equals(dmsExhibit.getExhibitTypeCd())) {
			deleteBatchExhibitOffshop(dmsExhibit);
			for (DmsExhibitoffshop dmsExhibitoffshop : dmsExhibit.getDmsExhibitoffshops()) {
				dmsExhibitoffshop.setStoreId(SessionUtil.getStoreId());
				dmsExhibitoffshop.setExhibitId(exhibitId);
				dao.insertOneTable(dmsExhibitoffshop);
			}
		}
	}

	/**
	 * 
	 * @Method Name : changeState
	 * @author : allen
	 * @date : 2016. 8. 1.
	 * @description : 기획전 상태 변경
	 *
	 * @param dmsExhibit
	 */
	public void updateExhibitStatus(DmsExhibit dmsExhibit) {
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		dmsExhibit.setUpdId(SessionUtil.getLoginId());
		dao.update("dms.exhibit.updateExhibitStatus", dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : insertExhibit
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 저장
	 *
	 * @param dmsExhibit
	 * @return
	 * @throws Exception
	 */
	public String insertExhibit(DmsExhibit dmsExhibit) throws Exception {
		// 1. [신규]제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 2. [신규]전체허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass

		// 3. [수정]전체허용 -> 제한 : controlNo(null), ccsControl(not null), isAllPermit(N)
		// 4. [수정]전체허용 -> 허용 : controlNo(null), ccsControl(null), isAllPermit(Y)  => by pass
		// 5. [수정]제한 -> 허용 : controlNo(not null), ccsControl(null), isAllPermit(Y) => ccs_control 삭제
		// 6. [수정]제한 -> 제한(내용변경 없음) : controlNo(not null), ccsControl(null), isAllPermit(N) => by pass
		// 7. [수정]제한 -> 제한(내용변경 있음) : controlNo(not null), ccsControl(not null), isAllPermit(N)

		// 0. 허용설정
		BigDecimal newControlNo = null;
		boolean isControlDelete = dmsExhibit.getControlNo() != null && "Y".equals(dmsExhibit.getIsAllPermit());
		boolean isControlSet = dmsExhibit.getCcsControl() != null && "N".equals(dmsExhibit.getIsAllPermit());

		// ccs_control 설정
		if (isControlSet) {
			newControlNo = commonService.updateControl(dmsExhibit.getControlNo(), dmsExhibit.getCcsControl());
		}

		// controlNo 삭제
		if (isControlDelete) {
			dmsExhibit.setControlNo(BaseConstants.BIGDECIMAL_NULL);
		} else if (newControlNo != null) {
			dmsExhibit.setControlNo(newControlNo);
		}


		// DMS_EXHIBIT 저장

		if (StringUtils.isNotEmpty(dmsExhibit.getImg1())) {
			dmsExhibit.setImg1(moveExhibitImage(dmsExhibit.getImg1()));
		}
		if (StringUtils.isNotEmpty(dmsExhibit.getImg2())) {
			dmsExhibit.setImg2(moveExhibitImage(dmsExhibit.getImg2()));
		}

		dao.insertOneTable(dmsExhibit);
		logger.debug("###### exhibitId = " + dmsExhibit.getExhibitId());

		// ccs_control 관련 테이블 삭제
		if (isControlDelete) {
			commonService.updateControl(dmsExhibit.getControlNo(), null);
		}

		// DMS_EXHIBITDISPLAYCATEGORY 저장
		if (StringUtils.isNotEmpty(dmsExhibit.getDisplayCategoryId())) {
			DmsExhibitdisplaycategory category = new DmsExhibitdisplaycategory();
			category.setExhibitId(dmsExhibit.getExhibitId());
			category.setStoreId(dmsExhibit.getStoreId());
			category.setDisplayCategoryId(dmsExhibit.getDisplayCategoryId());
			dao.insertOneTable(category);
		}

		// DMS_EXHIBITBRAND 저장
		if (dmsExhibit.getBrandIds() != null) {
			for (int i = 0; i < dmsExhibit.getBrandIds().length; i++) {
				DmsExhibitbrand exhibitBrand = new DmsExhibitbrand();
				exhibitBrand.setStoreId(dmsExhibit.getStoreId());
				exhibitBrand.setBrandId(dmsExhibit.getBrandIds()[i]);
				exhibitBrand.setExhibitId(dmsExhibit.getExhibitId());
				dao.insertOneTable(exhibitBrand);
			}
		}

		// 대표 상품, 쿠폰정보 delete & insert
		// 기획전
		if (BaseConstants.EXHIBIT_TYPE_CD_ONEDAY.equals(dmsExhibit.getExhibitTypeCd())) {
			if (dmsExhibit.getDmsExhibitmainproducts() != null) {
				for (DmsExhibitmainproduct dmsExhibitmainproduct : dmsExhibit.getDmsExhibitmainproducts()) {
					dmsExhibitmainproduct.setStoreId(SessionUtil.getStoreId());
					dmsExhibitmainproduct.setExhibitId(dmsExhibit.getExhibitId());
					dao.insertOneTable(dmsExhibitmainproduct);
				}
			}
		}
		if (BaseConstants.EXHIBIT_TYPE_CD_COUPON.equals(dmsExhibit.getExhibitTypeCd())) {
			if (dmsExhibit.getDmsExhibitcoupons() != null) {
				for (DmsExhibitcoupon dmsExhibitcoupon : dmsExhibit.getDmsExhibitcoupons()) {
					dmsExhibitcoupon.setStoreId(SessionUtil.getStoreId());
					dmsExhibitcoupon.setExhibitId(dmsExhibit.getExhibitId());
					dao.insertOneTable(dmsExhibitcoupon);
				}
			}
		}
		if (BaseConstants.EXHIBIT_TYPE_CD_OFFSHOP.equals(dmsExhibit.getExhibitTypeCd())) {
			if (dmsExhibit.getDmsExhibitoffshops() != null) {
				for (DmsExhibitoffshop dmsExhibitoffshop : dmsExhibit.getDmsExhibitoffshops()) {
					dmsExhibitoffshop.setStoreId(SessionUtil.getStoreId());
					dmsExhibitoffshop.setExhibitId(dmsExhibit.getExhibitId());
					dao.insertOneTable(dmsExhibitoffshop);
				}
			}
		}

		return dmsExhibit.getExhibitId();
	}

	public void saveExhibit(List<DmsExhibit> dmsExhibitList) throws Exception {

		for (DmsExhibit dmsExhibit : dmsExhibitList) {
			dmsExhibit.setStoreId(SessionUtil.getStoreId());
			if ("전시".equals(dmsExhibit.getDisplayYn())) {
				dmsExhibit.setDisplayYn("Y");
			} else {
				dmsExhibit.setDisplayYn("N");
			}
			if ("I".equals(dmsExhibit.getSaveType())) {
				dao.insertOneTable(dmsExhibit);
			} else {
				dao.updateOneTable(dmsExhibit);
			}
		}
	}

	/**
	 * 
	 * @Method Name : getExhibitDetail
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 상세 조회
	 *
	 * @param dmsExhibit
	 * @return
	 * @throws Exception
	 */
	public BaseDmsExhibit getExhibitDetail(BaseDmsExhibit dmsExhibit) throws Exception {
		return (BaseDmsExhibit) dao.selectOne("dms.exhibit.getExhibitDetail", dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : getExhibitGroupList
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 구분 타이틀 리스트 조회
	 *
	 * @param exhibitId
	 * @return
	 * @throws Exception
	 */
	public List<DmsExhibitgroup> getExhibitGroupList(DmsExhibitSearch search) throws Exception {
		return (List<DmsExhibitgroup>) dao.selectList("dms.exhibit.getExhibitGroupList", search);
	}

	public void saveExhibitGroupList(List<DmsExhibitgroup> dmsExhibitGroupList) throws Exception {
		for (DmsExhibitgroup dmsExhibitgroup : dmsExhibitGroupList) {
			dmsExhibitgroup.setStoreId(SessionUtil.getStoreId());
			if (StringUtils.isNotEmpty(dmsExhibitgroup.getImg())) {
				dmsExhibitgroup.setImg(moveExhibitImage(dmsExhibitgroup.getImg()));
			}
			dao.updateOneTable(dmsExhibitgroup);
		}
	}

	/**
	 * 
	 * @Method Name : insertExhibitGroup
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 구분 타이틀 저장
	 *
	 * @param dmsExhibitgroup
	 * @throws Exception
	 */
	public void insertExhibitGroup(DmsExhibitgroup dmsExhibitgroup) throws Exception {
		dmsExhibitgroup.setStoreId(SessionUtil.getStoreId());
		if (StringUtils.isNotEmpty(dmsExhibitgroup.getImg())) {
			dmsExhibitgroup.setImg(moveExhibitImage(dmsExhibitgroup.getImg()));
		}

		if (CommonUtil.isEmpty(dmsExhibitgroup.getInsDt())) {
			dao.insertOneTable(dmsExhibitgroup);
		} else {
			dao.updateOneTable(dmsExhibitgroup);
		}
	}

	public DmsExhibitgroup getDivTitleDetail(DmsExhibitgroup dmsExhibitgroup) throws Exception {
		dmsExhibitgroup.setStoreId(SessionUtil.getStoreId());
		return (DmsExhibitgroup) dao.selectOneTable(dmsExhibitgroup);
	}

	/**
	 * 
	 * @Method Name : deleteExhibitGroup
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 구분 타이틀 삭제
	 *
	 * @param dmsExhibitgroupList
	 * @throws Exception
	 */
	public void deleteExhibitGroup(List<DmsExhibitgroup> dmsExhibitgroupList) throws Exception {

		for (DmsExhibitgroup dmsExhibitgroup : dmsExhibitgroupList) {
			// 구분타이틀 하위 상품들 삭제
			DmsExhibitproduct product = new DmsExhibitproduct();
			product.setStoreId(dmsExhibitgroup.getStoreId());
			product.setExhibitId(dmsExhibitgroup.getExhibitId());
			product.setGroupNo(dmsExhibitgroup.getGroupNo());
			dao.delete("dms.exhibit.deleteExhibitproduct", product);
			//구분 타이틀 삭제
			dao.deleteOneTable(dmsExhibitgroup);
		}
	}

	/**
	 * 
	 * @Method Name : getExhibitProductList
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 구분타이틀 상품 조회
	 *
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	public List<DmsExhibitproduct> getExhibitProductList(DmsExhibitSearch dmsExhibitSearch) throws Exception {
		return (List<DmsExhibitproduct>) dao.selectList("dms.exhibit.getExhibitProductList", dmsExhibitSearch);
	}

	/**
	 * 
	 * @Method Name : insertExhibitProduct
	 * @author : allen
	 * @date : 2016. 6. 1.
	 * @description : 구분 타이틀 상품 저장
	 *
	 * @param dmsExhibitProductList
	 * @throws Exception
	 */
	public void insertExhibitProduct(List<DmsExhibitproduct> dmsExhibitProductList) throws Exception {
		for (DmsExhibitproduct dmsExhibitproduct : dmsExhibitProductList) {
			dmsExhibitproduct.setStoreId(SessionUtil.getStoreId());
			dmsExhibitproduct.setInsId(SessionUtil.getLoginId());
			dmsExhibitproduct.setUpdId(SessionUtil.getLoginId());
			if (StringUtils.isNotEmpty(dmsExhibitproduct.getCrudType()) && "I".equals(dmsExhibitproduct.getCrudType())) {
				dao.insert("dms.exhibit.insertDmsExhibitproduct", dmsExhibitproduct);
			} else {
				dao.updateOneTable(dmsExhibitproduct);
			}
		}
	}

	/**
	 * 
	 * @Method Name : insertExhibitMainProduct
	 * @author : allen
	 * @date : 2016. 6. 7.
	 * @description : 기획전 대표 상품 저장
	 *
	 * @param dmsExhibitmainproduct
	 * @throws Exception
	 */
	public void insertExhibitMainProduct(DmsExhibitmainproduct dmsExhibitmainproduct) throws Exception {
		dmsExhibitmainproduct.setStoreId(SessionUtil.getStoreId());
		if (StringUtils.isNotEmpty(dmsExhibitmainproduct.getImg())) {
			dmsExhibitmainproduct.setImg(moveExhibitImage(dmsExhibitmainproduct.getImg()));
		}
		if (StringUtils.isNotEmpty(dmsExhibitmainproduct.getCrudType()) && "I".equals(dmsExhibitmainproduct.getCrudType())) {
			dao.insertOneTable(dmsExhibitmainproduct);
		} else {
			dao.updateOneTable(dmsExhibitmainproduct);
		}
	}

	/**
	 * 
	 * @Method Name : getExistsExhibitProduct
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 상품 존재 확인
	 *
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	public List<DmsExhibitproduct> getExistsExhibitProduct(DmsExhibitSearch dmsExhibitSearch) throws Exception {
		return (List<DmsExhibitproduct>) dao.selectList("dms.exhibit.getExistsExhibitProduct", dmsExhibitSearch);
	}

	/**
	 * 
	 * @Method Name : getExistsExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 기획전 쿠폰 존재 확인
	 *
	 * @param dmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	public List<DmsExhibitcoupon> getExistsExhibitCoupon(DmsExhibitSearch dmsExhibitSearch) throws Exception {
		dmsExhibitSearch.setStoreId(SessionUtil.getStoreId());
		return (List<DmsExhibitcoupon>) dao.selectList("dms.exhibit.getExistsExhibitCoupon", dmsExhibitSearch);
	}

	/**
	 * 
	 * @Method Name : deleteDivTitleProduct
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 기획전 구분 타이틀 상품 삭제
	 *
	 * @param dmsExhibitProductList
	 * @throws Exception
	 */
	public void deleteDivTitleProduct(List<DmsExhibitproduct> dmsExhibitProductList) throws Exception {
		for (DmsExhibitproduct dmsExhibitproduct : dmsExhibitProductList) {
			dao.delete("dms.exhibit.deleteExhibitproduct", dmsExhibitproduct);
		}
	}

	/**
	 * 
	 * @Method Name : getExhibitMainProductList
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : ONE DAY 기획전 대표상품 리스트 조회
	 *
	 * @param exhibitId
	 * @return
	 * @throws Exception
	 */
	public List<DmsExhibitmainproduct> getExhibitMainProductList(DmsExhibitSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<DmsExhibitmainproduct>) dao.selectList("dms.exhibit.getExhibitMainProductList", search);
	}

	/**
	 * 
	 * @Method Name : getExhibitMainProductDetail
	 * @author : intune
	 * @date : 2016. 6. 28.
	 * @description : 기획전 대표상품 상세 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public DmsExhibitmainproduct getExhibitMainProductDetail(DmsExhibitmainproduct product) throws Exception {
		product.setStoreId(SessionUtil.getStoreId());
		return (DmsExhibitmainproduct) dao.selectOneTable(product);
	}

	/**
	 * 
	 * @Method Name : checkExhibitMainProduct
	 * @author : intune
	 * @date : 2016. 7. 4.
	 * @description : 기획전 상품 체크
	 *
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public boolean checkExhibitMainProduct(DmsExhibitmainproduct product) throws Exception {
		boolean existsFlag = false;
		product.setStoreId(SessionUtil.getStoreId());
		DmsExhibitmainproduct dmsExhibitmainproduct = (DmsExhibitmainproduct) dao.selectOneTable(product);

		if (dmsExhibitmainproduct != null) {
			existsFlag = true;
		}

		return existsFlag;
	}

	/**
	 * 
	 * @Method Name : getExhibitCouponList
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰 받기 기획전의 쿠폰 리스트 조회
	 *
	 * @param dmsExhibit
	 * @return
	 */
	public List<DmsExhibitcoupon> getExhibitCouponList(DmsExhibitSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<DmsExhibitcoupon>) dao.selectList("dms.exhibit.getExhibitCouponList", search);
	}

	/**
	 * 
	 * @Method Name : getExhibitOffshopList
	 * @author : allen
	 * @date : 2016. 8. 1.
	 * @description : 오프라인 기획전 매장 리스트 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public List<CcsOffshop> getExhibitOffshopList(DmsExhibitSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());

		return (List<CcsOffshop>) dao.selectList("dms.exhibit.getExhibitOffshopList", search);
	}

	/**
	 * 
	 * @Method Name : saveExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰 받기 기획전 쿠폰 저장
	 *
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	public void saveExhibitCoupon(List<DmsExhibitcoupon> dmsExhibitcouponList) throws Exception {
		for (DmsExhibitcoupon dmsExhibitcoupon : dmsExhibitcouponList) {
			dmsExhibitcoupon.setStoreId(SessionUtil.getStoreId());
			dao.insertOneTable(dmsExhibitcoupon);
		}
	}

	/**
	 * 
	 * @Method Name : deleteExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰받기 기획전 쿠폰 삭제
	 *
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	public void deleteExhibitCoupon(List<DmsExhibitcoupon> dmsExhibitcouponList) throws Exception {
		for (DmsExhibitcoupon dmsExhibitcoupon : dmsExhibitcouponList) {
			dmsExhibitcoupon.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(dmsExhibitcoupon);
		}
	}

	/**
	 * 
	 * @Method Name : saveExhibitMainProduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 기획전 대표상품 저장
	 *
	 * @param dmsExhibitproductList
	 * @throws Exception
	 */
	public void saveExhibitMainProduct(List<DmsExhibitmainproduct> dmsExhibitmainproductList) throws Exception {
		for (DmsExhibitmainproduct dmsExhibitmainproduct : dmsExhibitmainproductList) {
			dmsExhibitmainproduct.setStoreId(SessionUtil.getStoreId());
			if (StringUtils.isNotEmpty(dmsExhibitmainproduct.getImg())) {
				dmsExhibitmainproduct.setImg(moveExhibitImage(dmsExhibitmainproduct.getImg()));
			}
			if (StringUtils.isNotEmpty(dmsExhibitmainproduct.getCrudType()) && "I".equals(dmsExhibitmainproduct.getCrudType())) {
				dao.insertOneTable(dmsExhibitmainproduct);
			} else {
				dao.updateOneTable(dmsExhibitmainproduct);
			}
		}
	}

	/**
	 * 
	 * @Method Name : deleteExhibitproduct
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 기획전 대표 상품 삭제
	 *
	 * @param dmsExhibitproductList
	 * @throws Exception
	 */
	public void deleteExhibitMainProduct(List<DmsExhibitmainproduct> dmsExhibitmainproductList) throws Exception {
		for (DmsExhibitmainproduct dmsExhibitmainproduct : dmsExhibitmainproductList) {
			dmsExhibitmainproduct.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(dmsExhibitmainproduct);
		}
	}

	/**
	 * 
	 * @Method Name : deleteBatchExhibitMainProduct
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 기획전 대표 상품 일괄 삭제
	 *
	 * @param dmsExhibit
	 * @throws Exception
	 */
	public void deleteBatchExhibitMainProduct(DmsExhibit dmsExhibit) throws Exception {
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		dao.delete("dms.exhibit.deleteBatchExhibitMainProduct", dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : deleteExhibitCoupon
	 * @author : allen
	 * @date : 2016. 6. 20.
	 * @description : 쿠폰받기 기획전 쿠폰 일괄 삭제
	 *
	 * @param dmsExhibitcouponList
	 * @throws Exception
	 */
	public void deleteBatchExhibitCoupon(DmsExhibit dmsExhibit) throws Exception {
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		dao.delete("dms.exhibit.deleteBatchExhibitCoupon", dmsExhibit);
	}

	/**
	 * 
	 * @Method Name : deleteBatchExhibitOffshop
	 * @author : allen
	 * @date : 2016. 8. 8.
	 * @description : 오프라인 기획전 매장 일괄 삭제
	 *
	 * @param dmsExhibit
	 * @throws Exception
	 */
	public void deleteBatchExhibitOffshop(DmsExhibit dmsExhibit) throws Exception {
		dmsExhibit.setStoreId(SessionUtil.getStoreId());
		dao.delete("dms.exhibit.deleteBatchExhibitOffshop", dmsExhibit);
	}

	private String moveExhibitImage(String tempImgUrl) {
		//이미지 복사( temp to real )
		String realImgUrl = FileUploadUtil.moveTempToReal(tempImgUrl);

		return realImgUrl;
	}

	/**
	 * 상품에 걸려있는 기획전 목록 조회
	 * 
	 * @Method Name : getProductExhibitList
	 * @author : eddie
	 * @date : 2016. 8. 25.
	 * @description :
	 *
	 * @param control
	 * @param exhibitSearch
	 * @return
	 */
	public List<DmsExhibit> getProductExhibitList(CcsControlSearch control, DmsExhibitSearch exhibitSearch) {

		// 상품에 걸려있는 기획전 목록 조회 
		List<DmsExhibit> exhibits = (List<DmsExhibit>) dao.selectList("dms.exhibit.getProductExhibitList", exhibitSearch);

		if (exhibits == null) {
			return null;
		}

		List<DmsExhibit> newExhibits = new ArrayList<DmsExhibit>();

		String channelId = SessionUtil.getChannelId();
		String storeId = SessionUtil.getStoreId();
		HttpServletRequest request = SessionUtil.getHttpServletRequest();
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
		String memGradeCd = FoSessionUtil.getMemGradeCd();
		BigDecimal memberNo = FoSessionUtil.getMemberNo();

		List<String> memberTypeCds = new ArrayList<String>();
		memberService.getMemberTypeInfo(memberTypeCds);

		// 허용 제어 체크
		for (DmsExhibit ex : exhibits) {
			control.setControlNo(ex.getControlNo());
			control.setExceptionFlag(false);

			control.setStoreId(storeId);
			control.setChannelId(channelId);
			control.setMemberNo(memberNo);
			control.setDeviceTypeCd(deviceTypeCd);
			control.setMemberTypeCds(memberTypeCds);
			control.setMemGradeCd(memGradeCd);

			if (CommonUtil.isNotEmpty(control.getControlNo()) && !commonService.checkControl(control)) {
				return null;
			}
			newExhibits.add(ex);
		}
		return newExhibits;
	}

	/**
	 * 
	 * @Method Name : getExhibitBrandList
	 * @author : allen
	 * @date : 2016. 9. 23.
	 * @description : 기획전 브랜드 리스트 조회
	 *
	 * @return
	 */
	public List<PmsBrand> getExhibitBrandList() {
		return (List<PmsBrand>) dao.selectList("dms.exhibit.getExhibitBrandList", "");
	}

	/**
	 * 
	 * @Method Name : getMainExhibitList
	 * @author : allen
	 * @date : 2016. 9. 24.
	 * @description : 기획전 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsExhibit> getMainExhibitList(DmsExhibitSearch search) {
		List<String> exhibitIdList = new ArrayList<String>();
		List<DmsExhibit> resultExhibitList = new ArrayList<DmsExhibit>();
		List<DmsExhibit> exhibitAllList = (List<DmsExhibit>) dao.selectList("dms.exhibit.getAllExhibitList", "");

		if (exhibitAllList != null) {
			// 기획전 노출 제어 체크
			for (DmsExhibit dmsExhibit : exhibitAllList) {
				if (CommonUtil.isNotEmpty(dmsExhibit.getControlNo())) {
					HttpServletRequest request = SessionUtil.getHttpServletRequest();
					String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
					String memGradeCd = FoSessionUtil.getMemGradeCd();

					CcsControlSearch control = new CcsControlSearch();
					List<String> memberTypeCds = new ArrayList();
					memberService.getMemberTypeInfo(memberTypeCds);

					control.setExceptionFlag(false);
					control.setMemberTypeCds(memberTypeCds);
					control.setStoreId(SessionUtil.getStoreId());
					control.setChannelId(SessionUtil.getChannelId());
					control.setControlNo(dmsExhibit.getControlNo());
					control.setDeviceTypeCd(deviceTypeCd);
					if (SessionUtil.getMemberNo() != null) {
						control.setMemGradeCd(memGradeCd);
					} else {
						control.setMemGradeCd(BaseConstants.MEM_GRADE_CD_WELCOME);
					}

					control.setMemberNo(SessionUtil.getMemberNo());
					control.setControlType("EXHIBIT");

					if (!commonService.checkControl(control)) {
						exhibitIdList.add(dmsExhibit.getExhibitId());
					}
				}
			}
		}
		search.setExhibitIdList(exhibitIdList);
		List<DmsExhibit> exhibitList = (List<DmsExhibit>) dao.selectList("dms.exhibit.getMainExhibitList", search);
		return exhibitList;
	}

	public DmsExhibit getFrontExhibitDetail(DmsExhibitSearch search) {
		return (DmsExhibit) dao.selectOne("dms.exhibit.getFrontExhibitDetail", search);
	}
	
	/**
	 * 프론트 대,중카 매장 카테고리의 기획전 조회
	 * @Method Name : getCategoryExhibit
	 * @author : emily
	 * @date : 2016. 9. 30.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DmsExhibit> getCategoryExhibit(DmsExhibitSearch search){
		return (List<DmsExhibit>) dao.selectList("dms.exhibit.getCategoryExhibit", search);
	}

	/**
	 * 
	 * @Method Name : getBrandExhibitInfo
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 브랜드 기획전 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsExhibit> getBrandExhibitInfo(DmsExhibitSearch search) throws Exception {
		return (List<DmsExhibit>) dao.selectList("dms.exhibit.getBrandExhibitInfo", search);
	}

	public List<DmsExhibit> getOffshopExhibitList(DmsExhibitSearch search) {
		return (List<DmsExhibit>) dao.selectList("dms.exhibit.getOffshopExhibitList", search);
	}
	
	/**
	 * 프론트 추천기획전 조회
	 * @Method Name : getRecommendExhibit
	 * @author : emily
	 * @date : 2016. 11. 1.
	 * @description : 
	 *	검색 키워드로 기획전을 조회한다.
	 * @param searchKeyword
	 * @return
	 */
	public List<DmsExhibit> getRecommendExhibit(String searchKeyword){
		return (List<DmsExhibit>) dao.selectList("dms.exhibit.getRecommendExhibit", searchKeyword);
	}

	public DmsExhibit getFrontExhibitInfo(DmsExhibitSearch search) {
		return (DmsExhibit) dao.selectOne("dms.exhibit.getFrontExhibitInfo", search);
	}
	
	/**
	 * @Method Name : getExhibit
	 * @author : ian
	 * @date : 2016. 11. 14.
	 * @description : 모바일 히스토리 기획전
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public DmsExhibit getExhibit(DmsExhibit search) throws Exception{
		return (DmsExhibit) dao.selectOneTable(search);
	}
}
