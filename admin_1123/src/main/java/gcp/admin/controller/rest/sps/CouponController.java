package gcp.admin.controller.rest.sps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.search.SpsCouponIssueSearch;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.service.CouponService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@RestController("CouponController")
@RequestMapping("api/sps/coupon")
public class CouponController extends BaseController {
	private final Log		log	= LogFactory.getLog(getClass());

	@Autowired
	private CouponService	couponService;

	/**
	 * @Method Name : getCouponList
	 * @author : paul
	 * @date : 2016. 4. 20.
	 * @Description : 쿠폰 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public List<SpsCoupon> getCouponList(@RequestBody SpsCouponSearch couponSearch) {
		List<SpsCoupon> couponlist = couponService.getCouponList(couponSearch);

		return couponlist;
	}

	/**
	 * @Method Name : deleteCoupon
	 * @author : ian
	 * @date : 2016. 7. 27.
	 * @description : 쿠폰 삭제
	 *
	 * @param coupon
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void deleteCoupon(@RequestBody List<SpsCoupon> coupon) throws Exception {
		log.info("\n\t enter");
		log.info(coupon);
		couponService.deleteCoupon(coupon);
	}

	/**
	 * @Method Name : duplicateCheck
	 * @author : paul
	 * @date : 2016. 5. 20.
	 * @Description : 쿠폰 인증번호 중복 체크
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/duplicateCheck", method = RequestMethod.POST)
//	public String duplicateCheck(@RequestBody SpsCouponIssueSearch spsCouponIssueSearch) throws Exception {
//
//		spsCouponIssueSearch.setStoreId(SessionUtil.getStoreId());
//
//		return couponService.duplicateCheck(spsCouponIssueSearch);
//	}
	
	/**
	 * @Method Name : createPrivateCin
	 * @author : ian
	 * @date : 2016. 10. 18.
	 * @description : 쿠폰인증번호 생성
	 *
	 * @param spsCouponIssueSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createPrivateCin", method = RequestMethod.POST)
	public String createPrivateCin() throws Exception {
		return couponService.makePublicCin();
	}
	
	

	/**
	 * @Method Name : updateCoupon
	 * @author : ian
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰 상세 등록/수정/복사
	 *
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCoupon(@RequestBody SpsCoupon coupon) throws Exception {
		
		// 선물포장비 쿠폰 가격 1000원
		if(BaseConstants.COUPON_TYPE_CD_WRAP.equals(coupon.getCouponTypeCd())) {
			coupon.setDcValue(new BigDecimal(Config.getString("coupon.type.wrap.value")));
		}
		
		String id = null;
		if (CommonUtil.isEmpty(coupon.getCouponId())) {
			id = couponService.createCoupon(coupon);
		} else {
			id = couponService.updateCoupon(coupon);
		}
		return id;
	}

	/**
	 * @Method Name : updateCouponState
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 쿠폰 상태 변경 (진행/ 중지)
	 *
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateCouponState", method = RequestMethod.POST)
	public String updateCouponState(@RequestBody SpsCoupon coupon) throws Exception {
		return couponService.updateCouponState(coupon);
	}

	/**
	 * @Method Name : getCouponIssueState
	 * @author : ian
	 * @date : 2016. 6. 9.
	 * @description : 총 발급매수, 총 사용량 카운팅
	 *
	 * @param spsCouponSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/couponIssueState", method = RequestMethod.POST)
	public String getCouponIssueState(@RequestBody SpsCouponSearch spsCouponSearch) throws Exception {
		return couponService.getCouponIssueState(spsCouponSearch.getCouponId(), null); 
	}

	/**
	 * @Method Name : getCouponDetail
	 * @author : ian
	 * @date : 2016. 6. 9.
	 * @description :
	 *
	 * @param spsCoupon
	 * @return
	 */
	@RequestMapping(value = "/popup/detail", method = RequestMethod.POST)
	public SpsCoupon getCouponDetail(@RequestBody SpsCoupon spsCoupon) {
		spsCoupon.setStoreId(SessionUtil.getStoreId());
		return couponService.getCouponDetail(spsCoupon);
	}

	/**
	 * @Method Name : getCouponIssued
	 * @author : ian
	 * @date : 2016. 6. 14.
	 * @description : 쿠폰 발급 내역
	 *
	 * @param spsCouponIssueSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/issued/list", method = { RequestMethod.POST })
	public List<SpsCouponissue> getCouponIssued(@RequestBody SpsCouponIssueSearch spsCouponIssueSearch) throws Exception {

		spsCouponIssueSearch.setStoreId(SessionUtil.getStoreId());

		return couponService.getCouponIssuedList(spsCouponIssueSearch);
	}

	/**
	 * @Method Name : stopCouponIssue
	 * @author : ian
	 * @date : 2016. 6. 17.
	 * @description : 쿠폰 사용 중지
	 *
	 * @param spsCouponIssues
	 * @throws Exception
	 */
	@RequestMapping(value = "/issued/stop", method = { RequestMethod.POST })
	public void stopCouponIssue(@RequestBody List<SpsCouponissue> spsCouponIssues) throws Exception {
		couponService.stopCouponIssue(spsCouponIssues);
	}

	/**
	 * @Method Name : issueCoupon
	 * @author : ian
	 * @date : 2016. 6. 21.
	 * @description : 쿠폰 발급(수동, excel일괄발급)
	 *
	 * @param mmsMemberSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/issue", method = { RequestMethod.POST })
	public Map<String, String> issueCoupon(@RequestBody SpsCouponissue issues) throws Exception {
		issues.setControllCheckType("BO");//허용설정 체크타입
		return couponService.issueCoupon(issues, false);
	}

}
