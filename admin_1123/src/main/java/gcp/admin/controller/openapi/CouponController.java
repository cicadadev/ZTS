package gcp.admin.controller.openapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gcp.external.model.coupon.CampaignCoupon;
import gcp.sps.service.CouponService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.JsonUtil;
import intune.gsf.controller.BaseController;
import net.sf.json.JSONObject;

@RestController("ApiCouponController")
@CrossOrigin(origins = "*")
@RequestMapping("openapi/coupon")
public class CouponController extends BaseController {

	@Autowired
	private CouponService couponService;

	@RequestMapping(value = "/issue/campaign", method = { RequestMethod.POST, RequestMethod.GET })
	public String issueCampaignCoupon(@RequestParam String paramerter, HttpServletRequest request) {

		logger.debug("########### PARAMERTER JSON  : " + paramerter);

		JSONObject resultObj = new JSONObject();
		try {
			CampaignCoupon coupons = new ObjectMapper().readValue(paramerter, CampaignCoupon.class);

			resultObj = couponService.createCampaignCoupon(coupons);
		} catch (ServiceException e) {
			resultObj.put("resultCode", e.getMessageCd().replace("sps.coupon.issue.", ""));
			resultObj.put("resultMsg", e.getMessage());

		} catch (JsonParseException e) {
			resultObj.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_UNKNOWN);// 알수없는 에러
			resultObj.put("resultMsg", e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			resultObj.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_UNKNOWN);// 알수없는 에러
			resultObj.put("resultMsg", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			resultObj.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_UNKNOWN);// 알수없는 에러
			resultObj.put("resultMsg", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			resultObj.put("resultCode", BaseConstants.COUPON_ISSUE_RESULT_UNKNOWN);// 알수없는 에러
			resultObj.put("resultMsg", e.getMessage());
			e.printStackTrace();
		}

		logger.debug("########### RESPONSE JSON  : " + resultObj.toString());

		return resultObj.toString();
	}

	public static void main(String[] args) throws JsonProcessingException {
		CouponController c = new CouponController();

//		c.issueCampaignCoupon(issues);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("storeId", "1001");
		param.put("couponId", "123");
		param.put("campaignId", "");
		param.put("campaignOrder", "");
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		Map<String, String> memberMap = new HashMap<String, String>();
		memberMap.put("memberNo", "2222222222");
		memberMap.put("segmentId", "1");
		lists.add(memberMap);
		memberMap.put("memberNo", "1111111111");
		memberMap.put("segmentId", "2");
		lists.add(memberMap);
		param.put("targetMembers", lists);

		ObjectMapper mapper = new ObjectMapper();

		String jsonParam = mapper.writeValueAsString(param);
		System.out.println("@@ Request jsonParam::" + jsonParam);
		Map<String, String> result = new HashMap<String, String>();
		result = (Map<String, String>) JsonUtil.Json2ObjectUrl(result.getClass(), jsonParam,
				"http://192.2.72.131:7001/openapi/coupon/issue/campaign");

		System.out.println("@@ Response result::" + result);

	}
}
