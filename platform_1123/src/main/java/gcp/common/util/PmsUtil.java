package gcp.common.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import gcp.pms.model.base.BasePmsSaleproduct;
import gcp.pms.model.base.BasePmsSaleproductoptionvalue;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.FileUploadUtil;

public class PmsUtil {

	// 상품의 승인 이후 상태들
	private static List<String>	approvalAfterState	= Arrays.asList(BaseConstants.SALE_STATE_CD_SALE,
			BaseConstants.SALE_STATE_CD_SOLDOUT, BaseConstants.SALE_STATE_CD_STOP, BaseConstants.SALE_STATE_CD_MDSTOP,
			BaseConstants.SALE_STATE_CD_END);

	// 상품의 승인 이전 상태들
	private static List<String>	approvalBeforeState	= Arrays.asList(BaseConstants.SALE_STATE_CD_REQ,
			BaseConstants.SALE_STATE_CD_APPROVAL1, BaseConstants.SALE_STATE_CD_APPROVAL2, BaseConstants.SALE_STATE_CD_REJECT1);

	/**
	 * 상품의 현재 상태가 승인 이후인지 조회
	 * 
	 * @Method Name : isApproval
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description :
	 *
	 * @param currentState
	 * @return
	 */
	public static boolean isApproval(String currentState) {

		if (approvalAfterState.contains(currentState)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 단품명 생성
	 * 
	 * @Method Name : makeSaleproductName
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description : 단품의 옵션 정보로 단품명을 생성한다. <br/>
	 *              옵션값1/ 옵션값2...
	 *
	 * @param saleproduct
	 * @return
	 */
	public static String makeSaleproductName(BasePmsSaleproduct saleproduct) {
		String saleProductName = "";

//		if (saleproduct.getPmsSaleproductoptionvalues() != null && saleproduct.getPmsSaleproductoptionvalues().size() > 0) {
//
//			for (BasePmsSaleproductoptionvalue optionvalue : saleproduct.getPmsSaleproductoptionvalues()) {
//				if (CommonUtil.isEmpty(saleProductName)) {
//					saleProductName = optionvalue.getOptionName() + ":" + optionvalue.getOptionValue();
//				} else {
//					saleProductName += ", " + optionvalue.getOptionName() + ":" + optionvalue.getOptionValue();
//				}
//			}
//
//		}
		if (saleproduct.getPmsSaleproductoptionvalues() != null && saleproduct.getPmsSaleproductoptionvalues().size() > 0) {

			for (BasePmsSaleproductoptionvalue optionvalue : saleproduct.getPmsSaleproductoptionvalues()) {
				if (CommonUtil.isEmpty(saleProductName)) {
					saleProductName = optionvalue.getOptionValue();
				} else {
					saleProductName += "/" + optionvalue.getOptionValue();
				}
			}

		}
		return saleProductName;
	}

	public static String getImagePath() {
		return Config.getString("upload.image.path") + FileUploadUtil.SEPARATOR + "pms/product";
	}

	public static String getImagePathDb() {
		return CommonUtil.replace(Config.getString("upload.image.path"), Config.getString("upload.base.path"), "")
				+ FileUploadUtil.SEPARATOR + "pms/product";
	}

	/**
	 * 
	 * 가격에 할인율 적용( 원단위 반올림)
	 * 
	 * @Method Name : applyPriceSale
	 * @author : eddie
	 * @date : 2016. 10. 3.
	 * @description :
	 *
	 * @return
	 */
	public static BigDecimal applyDcRateToPrice(BigDecimal price, BigDecimal saleRate) {

		long pricdD = price.longValue();
		long rateD = saleRate.longValue();

		double result1 = pricdD * rateD / 1000;
		long ratePrice = Math.round(result1);
		return new BigDecimal(pricdD - ratePrice * 10);
	}


	/**
	 * 상품의 포인트 적립액 계산
	 * 
	 * @Method Name : applyPointRate
	 * @author : intune
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @param salePrice
	 * @param pointRate
	 * @return
	 */
	public static BigDecimal applyPointRate(BigDecimal salePrice, BigDecimal pointRate) {

		return salePrice.multiply(pointRate).divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP);

	}
	
	/**
	 * 가격 할인율 계산
	 * 
	 * @Method Name : getDcRate
	 * @author : ian
	 * @date : 2016. 10. 12.
	 * @description : 
	 *
	 * @param stdSalePrice 원가격
	 * @param dcSalePrice 할인가격
	 * @param loc 소수점 자리수
	 * @param mode 1 내림, 2 반올림, 3 올림
	 * @return
	 */
	public static BigDecimal getDcRate(BigDecimal stdSalePrice, BigDecimal dcSalePrice, int loc, int mode) {
		
		BigDecimal rate = new BigDecimal(0);
		if (dcSalePrice.compareTo(BigDecimal.ZERO) <= 0) {
			rate = new BigDecimal(100);
		} else {
			rate = dcSalePrice.divide(stdSalePrice, 10, BigDecimal.ROUND_CEILING);
			rate = (BigDecimal.ONE).add(rate.negate());
			rate = rate.multiply(new BigDecimal(100));	// 백분위 변환

			if (mode == 1) {
				rate = rate.setScale(loc, BigDecimal.ROUND_DOWN); // 내림
			} else if (mode == 2) {
				rate = rate.setScale(loc, BigDecimal.ROUND_HALF_UP); // 반올림
			} else if (mode == 3) {
				rate = rate.setScale(loc, BigDecimal.ROUND_UP); // 올림
			}

			if (rate.compareTo(BigDecimal.ZERO) <= 0) {
				rate = BigDecimal.ZERO;
			}
		}
		
		return rate;
	}

	/**
	 * referer로 부터 displayCategoryId조회
	 * 
	 * @Method Name : getDisplayCategoryIdFromRefer
	 * @author : intune
	 * @date : 2016. 10. 23.
	 * @description :
	 *
	 * @param referer
	 * @return
	 */
	public static String getDisplayCategoryIdFromRefer(String referer) {

		//http://local.0to7.com:7002/dms/common/templateDisplay?dispCategoryId=30020


		// 통합 카테고리만 해당
		if (referer.indexOf("templateDisplay") < 0) {
			return null;
		}

		String param = "dispCategoryId";

		int start = referer.indexOf(param);
		if (start > 0) {

			String displayCategoryId = null;
			int paramLength = param.length();

			referer = referer.substring(start);
			int last = referer.indexOf("&");

			displayCategoryId = referer.substring(paramLength + 1, last == -1 ? referer.length() : last);

			return displayCategoryId;
		}
		return null;

	}
}
