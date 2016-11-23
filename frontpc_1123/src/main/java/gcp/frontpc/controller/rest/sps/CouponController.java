package gcp.frontpc.controller.rest.sps;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.external.service.OffshopCouponService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.service.CouponService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@RestController("CouponController")
@RequestMapping("api/sps/coupon")
public class CouponController extends BaseController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CouponService	couponService;
	
	@Autowired
	private OffshopCouponService	offshopCouponService;

	/**
	 * @Method Name : issueCoupon
	 * @author : ian
	 * @date : 2016. 9. 7.
	 * @description : 쿠폰 발행 및 다운로드
	 *
	 * @param issue
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/issueCoupon", method = { RequestMethod.POST })
	public List<Map<String, String>> issueCoupon(@RequestBody List<SpsCouponissue> issues, HttpServletRequest request) throws Exception {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		String storeId = SessionUtil.getStoreId();
		BigDecimal memberNo = SessionUtil.getMemberNo();

		for(SpsCouponissue issue : issues) {
			issue.setStoreId(storeId);
			issue.setMemberNo(memberNo);
		}

		resultList = couponService.createFoissueCoupon(issues);

		return resultList;
	}
	
	/**
	 * 회원등급 혜택 : 오프라인 쿠폰 발급
	 * 
	 * @Method Name : issueOffcoupon
	 * @author : intune
	 * @date : 2016. 10. 31.
	 * @description :
	 *
	 * @param couponIssue offshopType
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/issueOffcoupon", method = { RequestMethod.GET })
	public String issueOffcoupon(HttpServletRequest request)
			throws Exception {

		BigDecimal memberNo = SessionUtil.getMemberNo();

		if (CommonUtil.isEmpty(memberNo)) {
			return "notlogin";
		}

		boolean isIssued = offshopCouponService.isCouponIssued(String.valueOf(memberNo),
				Config.getString("offshop.coupontype.gradeup1"));

		if (isIssued) {
			return "issued";
		}


		//매장 쿠폰 발급
		SpsCouponissue coupon = new SpsCouponissue();
		//TODO 등급별 쿠폰
		coupon.setOffshopType(Config.getString("offshop.coupontype.gradeup1"));
		coupon.setOffshopMemberNo(memberNo.toString());
		coupon.setOffshopSalePrice("0");
		String validDt = DateUtil.getMonthLastDay(DateUtil.FORMAT_3, null);
		coupon.setOffshopValidDt(validDt);//발행일 당월 말일의 일자
		couponService.createOffshopCoupon(coupon);

		return "success";
	}

	/**
	 * 등급별 쿠폰 다운로드 가능 시간 체크
	 * 
	 * @Method Name : checkissueable
	 * @author : intune
	 * @date : 2016. 11. 3.
	 * @description : 1일 0시부터 3시까지 발급 불가능
	 * @param issues
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkissueable")
	public String checkissueable(HttpServletRequest request) throws Exception {

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_10);

		//현재시간이 1일 0~3시 사이이면 쿠폰 다운로드 불가
		SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.FORMAT_10);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		String first = formatter.format(cal.getTime());

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 2);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		String last = formatter.format(cal.getTime());

		logger.info("@@currentDate::" + currentDate);
		logger.info("@@first::" + first);
		logger.info("@@last::" + last);

		if (Double.parseDouble(currentDate) > Double.parseDouble(first)
				&& Double.parseDouble(currentDate) < Double.parseDouble(last)) {
			return "fail";
		} else {
			return "success";
		}
	}

}
