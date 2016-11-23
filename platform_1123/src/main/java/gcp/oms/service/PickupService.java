package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.TmsQueue;
import gcp.external.service.TmsService;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsOrderTms;
import gcp.oms.model.OmsPickup;
import gcp.oms.model.OmsPickupproduct;
import gcp.oms.model.search.OmsPickupSearch;
import gcp.pms.model.PmsOffshopstock;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.service.CouponService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.ByteUtil;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : PickupService.java
 * @author : victor
 * @date : 2016. 7. 5.
 * @description :
 */
@Service("pickupService")
public class PickupService extends BaseService {
	private static final Logger	logger	= LoggerFactory.getLogger(PickupService.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderTmsService orderTmsService;

	@Autowired
	private TmsService tmsService;

	@Autowired
	private CouponService		couponService;

	@SuppressWarnings("unchecked")
	public List<OmsPickupproduct> selectList(String queryId, OmsPickupSearch pickupSearch) throws Exception {
		return (List<OmsPickupproduct>) dao.selectList(queryId, pickupSearch);
	}

	public Object selectOne(String queryId, OmsPickupSearch pickupSearch) throws Exception {
		return dao.selectOne(queryId, pickupSearch);
	}

	public int insert(Object pickup) throws Exception {
		return dao.insertOneTable(pickup);
	}

	public int update(Object pickup) throws Exception {
		return dao.updateOneTable(pickup);
	}

	public int update(String queryId, Object pickup) throws Exception {
		return dao.update(queryId, pickup);
	}

	public int delete(Object pickup) throws Exception {
		return dao.deleteOneTable(pickup);
	}

	public int cancelPickup(List<OmsPickupproduct> omsPickupproducts, String userId) throws Exception {

		int cancelCnt = 0;
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
		String phone2 = "";
		String productName = "";
		String pickupId = "";
		String offshopName = "";
		String offshopPhone2 = "";

		for (OmsPickupproduct product : omsPickupproducts) { // TODO - 체크, 단건 전체 갱신
			
			product.setPickupProductStateCd("PICKUP_PRODUCT_STATE_CD.CANCEL");
			product.setPickupCancelDt(currentDate);
			product.setUpdDt(currentDate);
			product.setUpdId(userId);
			product.setPickupDeliveryDt("");

			// 1. 상태값 변경
			// cancelCnt += dao.updateOneTable(product);
			cancelCnt += dao.update("oms.pickup.updatePickupProduct", product);

			// 2. 재고원복
			PmsOffshopstock pmsOffshopstock = new PmsOffshopstock();
			pmsOffshopstock.setStoreId(product.getStoreId());
			pmsOffshopstock.setSaleproductId(product.getSaleproductId());
			pmsOffshopstock.setOffshopId(product.getOffshopId());
			pmsOffshopstock.setRealStockQty(product.getOrderQty());
			pmsOffshopstock.setUpdDt(currentDate);
			pmsOffshopstock.setUpdId(userId);

			dao.update("oms.pickup.updateOffshopStock", pmsOffshopstock);
			
			// sms parameter
			phone2 = product.getOmsPickup().getPhone2();
			productName = product.getProductName();
			pickupId = product.getPickupId();
			offshopName = product.getOffshopName();
			offshopPhone2 = product.getCcsOffshop().getManagerPhone();
			
		}
		// 3. 픽업담당자, 신청자 sms
		productName = ByteUtil.getMaxByteString(productName, 32);

		{
			// to customer
			TmsQueue tmsQueue = new TmsQueue();
			tmsQueue.setToPhone(phone2);
			tmsQueue.setMsgCode("107");
			tmsQueue.setMap1(productName);
			tmsQueue.setMap2(omsPickupproducts.size() > 1 ? " 외 " + (omsPickupproducts.size() - 1) + " 건" : "");
			tmsQueue.setMap3(pickupId);
			tmsQueue.setMap4(offshopName);

			tmsService.sendTmsSmsQueue(tmsQueue);

			// to offshop
			tmsQueue.setToPhone(offshopPhone2);
			tmsQueue.setMsgCode("107");
			tmsQueue.setMap1(productName);
			tmsQueue.setMap2(omsPickupproducts.size() > 1 ? " 외 " + (omsPickupproducts.size() - 1) + " 건" : "");
			tmsQueue.setMap3(pickupId);
			tmsQueue.setMap4(offshopName);
			tmsService.sendTmsSmsQueue(tmsQueue);
		}

		return cancelCnt;
	}

	public Map<String, Object> savePickup(OmsPickup paramOmsPickup) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		String storeId = paramOmsPickup.getStoreId();

		String paramCartProductNos = paramOmsPickup.getCartProductNos();
		BigDecimal memberNo = paramOmsPickup.getMemberNo();
		String[] cartParams = paramCartProductNos.split(",");

		String pickupId = (String) dao.selectOne("oms.order.getNewOrderId", null);

		String cartProductNos = "";
		Map map = new HashMap();
		for (String cartParam : cartParams) {
			String[] param = cartParam.split("_");
			String cartProductNo = param[0];
			String pickupReserveDt = param[1];

			cartProductNos += "," + cartProductNo;
			map.put(cartProductNo, pickupReserveDt);
		}

		cartProductNos = cartProductNos.substring(cartProductNos.indexOf(",") + 1);
		OmsPickup omsPickup = new OmsPickup();
		omsPickup.setCartProductNos(CommonUtil.convertInParam(cartProductNos));
		omsPickup.setMemberNo(memberNo);
		omsPickup = (OmsPickup) dao.selectOne("oms.order.getOmsPickupByCart", omsPickup);				
				
		if (CommonUtil.isNotEmpty(omsPickup)) {
			//회원정보
			MmsMemberZts memberInfo = memberService.getMemberDetail(memberNo);

			omsPickup.setMemberId(memberInfo.getMmsMember().getMemberId());
			omsPickup.setName1(memberInfo.getMmsMember().getMemberName());
			omsPickup.setCountryNo("82");
			omsPickup.setPhone1(memberInfo.getMmsMember().getPhone2());
			omsPickup.setPhone2(memberInfo.getMmsMember().getPhone2());

			omsPickup.setPickupId(pickupId);
			//		String pickupReqDt = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
			//		omsPickup.setPickupReqDt(pickupReqDt);	//요청일자
			dao.insertOneTable(omsPickup);

			//			String pickupId = omsPickup.getPickupId();


			int productCnt = 0;
			String offshopPickupDt = null;

			String name = omsPickup.getName1();
			BigDecimal offshopSalePrice = new BigDecimal(0);

			List<OmsOrderTms> orderTmsList = new ArrayList<OmsOrderTms>();

			for (OmsPickupproduct omsPickupproduct : omsPickup.getOmsPickupproducts()) {

				String saleStateCd = omsPickupproduct.getSaleStateCd();
				
				if ("SALE_STATE_CD.MDSTOP".equals(saleStateCd) || "SALE_STATE_CD.END".equals(saleStateCd)
						|| !"Y".equals(omsPickupproduct.getOffshopPickupYn())) {
					throw new ServiceException("oms.order.nonSale", new String[] { CommonUtil
							.makeProductErrorName(omsPickupproduct.getProductName(), omsPickupproduct.getSaleproductName()) });
				}
				
				omsPickupproduct.setPickupId(pickupId);
				//			omsPickupproduct.setPickupReqDt(pickupReqDt);	//요청일자
				String pickupReserveDt = (String) map.get(omsPickupproduct.getCartProductNo().toString());

				//픽업일자 check
				checkOpenDy(omsPickupproduct.getOffshopId(), pickupReserveDt);

				omsPickupproduct.setPickupReserveDt(pickupReserveDt);
				dao.insertOneTable(omsPickupproduct);

				offshopSalePrice = offshopSalePrice
						.add(omsPickupproduct.getTotalSalePrice().multiply(omsPickupproduct.getOrderQty()));

				productCnt++;

				//재고차감
				updateStockQty(omsPickupproduct, true, "");

				String offshopName = omsPickupproduct.getCcsOffshop().getName();
				String pickupDate = omsPickupproduct.getPickupReserveDt();
				String productName = omsPickupproduct.getProductName();
				String managerPhone = omsPickupproduct.getCcsOffshop().getManagerPhone();

				//sms
				OmsOrderTms orderTms = new OmsOrderTms();
				orderTms.setType("PICKUP_COMPLETE");
				orderTms.setToPhone(omsPickup.getPhone2());
				orderTms.setProductName(productName);
				orderTms.setOrderId(pickupId);
				orderTms.setOffshopName(offshopName);
				orderTms.setPickupDate(pickupDate);
				orderTms.setAddPhone(managerPhone);
				orderTms.setOrderName(name);
				orderTmsList.add(orderTms);

				if (CommonUtil.isEmpty(offshopPickupDt)) {
					offshopPickupDt = pickupReserveDt;
				} else {
					if (DateUtil.compareToDate(pickupReserveDt, offshopPickupDt, DateUtil.FORMAT_3) == 1) {
						offshopPickupDt = pickupReserveDt;
					}
				}

			}

			//장바구니 삭제		
			cartService.deleteCartOrder(cartProductNos, storeId);

			//매장 쿠폰 발급
			SpsCouponissue coupon = new SpsCouponissue();
//			coupon.setOffshopTypeNo("1");
			coupon.setOffshopType(Config.getString("offshop.coupontype.pickup"));
			coupon.setOffshopMemberNo(memberNo.toString());
			coupon.setOffshopSalePrice(offshopSalePrice.toString());
			String pickupDt = DateUtil.getAddDate(DateUtil.FORMAT_3, offshopPickupDt, new BigDecimal(1));	//쿠폰일자 - max 픽업일 + 1
			coupon.setOffshopValidDt(pickupDt);
			couponService.createOffshopCoupon(coupon);

			for (OmsOrderTms orderTms : orderTmsList) {
				orderTmsService.saveOrderTms(orderTms);
			}

		} else {
			result.put(BaseConstants.RESULT_MESSAGE, "상품이 없습니다.");
			return result;
		}
		result.put("pickupId", pickupId);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : checkOpenDy
	 * @author : dennis
	 * @date : 2016. 11. 8.
	 * @description : 픽업일자 check
	 *
	 * @param offshopId
	 * @param pickupReserveDt
	 */
	private void checkOpenDy(String offshopId, String pickupReserveDt) {
		Map searchMap = new HashMap<String, String>();
		searchMap.put("offshopId", offshopId);
		List<Map> dayMapList = (List<Map>) dao.selectList("ccs.offshop.getOffshopOpendayList", searchMap);
		boolean dayCheck = false;
		for (Map dayMap : dayMapList) {
			String openDate = (String) dayMap.get("OPEN_DATE_VAL");
			if (openDate.equals(pickupReserveDt)) {
				dayCheck = true;
			}
		}

		if (!dayCheck) {
			throw new ServiceException("oms.bind.error", new String[] { "선택불가능한 픽업예정일입니다." });
		}
	}

	public void updateStockQty(OmsPickupproduct omsPickupproduct, boolean minus, String stockMinus) {

		PmsOffshopstock pmsOffshopstock = new PmsOffshopstock();
		pmsOffshopstock.setStoreId(omsPickupproduct.getStoreId());
		pmsOffshopstock.setSaleproductId(omsPickupproduct.getSaleproductId());
		pmsOffshopstock.setOffshopId(omsPickupproduct.getOffshopId());
		BigDecimal qty = omsPickupproduct.getOrderQty();
		if (minus) {
			pmsOffshopstock.setRealStockQty(qty.negate());
		} else {
			pmsOffshopstock.setRealStockQty(qty);
		}
		pmsOffshopstock.setStockMinus(stockMinus);
		dao.update("pms.product.updatePickupStock", pmsOffshopstock);

		if (!"SUCCESS".equals(pmsOffshopstock.getResult())) {
			String msg = pmsOffshopstock.getMsg();
			logger.debug(msg);
			String productname = omsPickupproduct.getProductName() + "-" + omsPickupproduct.getSaleproductName();
			throw new ServiceException("oms.order.nonStockQty", new String[] { productname });
		}
	}

	/**
	 * 
	 * @Method Name : getOrderPickupComplete
	 * @author : dennis
	 * @date : 2016. 9. 9.
	 * @description : 픽업신청완료조회
	 *
	 * @param omsPickup
	 * @return
	 */
	public OmsPickup getOrderPickupComplete(OmsPickup omsPickup) {
		return (OmsPickup) dao.selectOne("oms.order.getOrderPickupComplete", omsPickup);
	}

	public Map<String, Object> updatePickupAutoNewTx(OmsPickup pickup) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		for (OmsPickupproduct product : pickup.getOmsPickupproducts()) {

			dao.update("oms.pickup.cancelPickupAuto", product);

			String name = pickup.getName1();
			String pickupId = pickup.getPickupId();
			String pickupDate = product.getPickupReserveDt();
			String productName = product.getProductName();
			String offshopName = product.getCcsOffshop().getName();
			String managerPhone = product.getCcsOffshop().getManagerPhone();

			//sms
			OmsOrderTms orderTms = new OmsOrderTms();
			orderTms.setType("PICKUP_AUTO_CANCEL");
			orderTms.setToPhone(pickup.getPhone2());
			orderTms.setProductName(productName);
			orderTms.setOrderId(pickupId);
			orderTms.setOffshopName(offshopName);
			orderTms.setPickupDate(pickupDate);
			orderTms.setAddPhone(managerPhone);
			orderTms.setOrderName(name);
			orderTmsService.saveOrderTms(orderTms);

		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	public Map<String, Object> cancelPickupAuto() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		List<OmsPickup> pickupList = (List<OmsPickup>) dao.selectList("oms.pickup.getPicupAutoCancelList", null);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		int totalCnt = pickupList.size();
		int successCnt = 0;
		int failCnt = 0;

		for (OmsPickup pickup : pickupList) {

			Map subResult = new HashMap();
			try {
				subResult = ((PickupService) getThis()).updatePickupAutoNewTx(pickup);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			//실패시.
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n=============================");
				errorMsg.append("\n픽업 ID : " + pickup.getPickupId());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n=============================\n");
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;
			} else {
				successCnt++;
			}
		}
		resultMsg.append("\n총건수 	: " + totalCnt);
		resultMsg.append("\n성공 	: " + successCnt);
		resultMsg.append("\n실패 	: " + failCnt);

		if (BaseConstants.RESULT_FLAG_FAIL.equals(result.get(BaseConstants.RESULT_FLAG))) {
			resultMsg.append(errorMsg);
		}

		result.put(BaseConstants.RESULT_MESSAGE, resultMsg.toString());
		return result;
	}


}
