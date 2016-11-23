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
import gcp.common.util.FoSessionUtil;
import gcp.common.util.PmsUtil;
import gcp.external.model.TmsQueue;
import gcp.external.service.PgService;
import gcp.external.service.TmsService;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderTms;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentif;
import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.model.OmsRegulardeliveryproduct;
import gcp.oms.model.OmsRegulardeliveryschedule;
import gcp.oms.model.search.OmsRegularSearch;
import gcp.pms.model.PmsSetproduct;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.search.SpsPointSearch;
import gcp.sps.service.PointService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.ImageUrlUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : RegularService.java
 * @author : victor
 * @date : 2016. 7. 11.
 * @description :
 */
@Service("regularService")
public class RegularService extends BaseService {

	private static final Logger	logger	= LoggerFactory.getLogger(RegularService.class);

	@Autowired
	private PointService		pointService;

	@Autowired
	private CartService cartService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private OrderService		orderService;

	@Autowired
	private PgService			pgService;

	@Autowired
	private OrderTmsService		orderTmsService;

	@Autowired
	private TmsService			tmsService;

	@Autowired
	private PaymentService		paymentService;

	public List<?> selectList(String queryId, OmsRegularSearch regularSearch) throws Exception {
		return dao.selectList(queryId, regularSearch);
	}

	public OmsRegulardelivery selectOne(String regularId) throws Exception {
		// return (OmsRegulardelivery) dao.selectOne("oms.regular.select.one", regularId);
		return (OmsRegulardelivery) dao.selectOneTable(regularId);
	}

	public int insert(Object regular) throws Exception {
		return dao.insertOneTable(regular);
	}

	public int update(Object regular) throws Exception {
		return dao.updateOneTable(regular);
	}

	public int update(String queryId, Object regular) throws Exception {
		return dao.update(queryId, regular);
	}

	public int delete(Object regular) throws Exception {
		return dao.deleteOneTable(regular);
	}


	public int updateRegular(List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {

		int cancelCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsRegulardeliveryproduct product : omsRegularproducts) {
			product.setDeliveryProductStateCd("DELIVERY_PRODUCT_STATE_CD.CANCEL");
			product.setUpdDt(currentDate);
			product.setUpdId(userId);

			// 1. 배송상품 상태값 변경
			cancelCnt += dao.update("oms.regular.update.product", product);

			// 2. 스케쥴 상태값 변경
			OmsRegulardeliveryschedule schedule = product.getOmsRegulardeliveryschedules().get(0);
			// schedule.setDeliveryProductNo(product.getDeliveryProductNo());
			schedule.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.CANCEL");
			schedule.setUpdDt(currentDate);
			schedule.setUpdId(userId);
			schedule.setRegularDeliveryDt("");

			dao.update("oms.regular.update.schedule", schedule);

			OmsRegulardelivery regularDelivery = new OmsRegulardelivery();
			regularDelivery.setRegularDeliveryId(product.getRegularDeliveryId());
			regularDelivery = (OmsRegulardelivery) dao.selectOneTable(regularDelivery);
			if (CommonUtil.isNotEmpty(regularDelivery.getPhone2())) {
				TmsQueue tmsQueue = new TmsQueue();
				String phone = regularDelivery.getPhone2().replaceAll("-", ""); // 하이픈 제거

				tmsQueue.setMsgCode("115");
				tmsQueue.setToPhone(phone);
				tmsQueue.setSubject("0to7.com 정기배송 해지완료");
				tmsQueue.setMap1(omsRegularproducts.get(0).getPmsProduct().getName());
				tmsService.sendTmsSmsQueue(tmsQueue);
			}
//
			MmsMember mmsMember = new MmsMember();
			mmsMember.setMemberNo(regularDelivery.getMemberNo());
			mmsMember = (MmsMember) dao.selectOneTable(mmsMember);

			if (CommonUtil.isNotEmpty(mmsMember.getEmail())) {
				TmsQueue tmsQueue = new TmsQueue();
				String imgUrl = ImageUrlUtil.productMakeURL(product.getProductId(), "90", null, null);
				String now = DateUtil.getCurrentDate(DateUtil.FORMAT_14);

				BigDecimal price = product.getRegularDeliveryPrice().multiply(product.getOrderQty());
				String orderPrice = CommonUtil.formatMoney(price.toString());

				tmsQueue.setMsgCode("014");
				tmsQueue.setToEmail(mmsMember.getEmail());
				tmsQueue.setSubject("0to7.com 정기배송 해지완료");
				tmsQueue.setMap1(mmsMember.getMemberName());
				tmsQueue.setMap2(now);
				tmsQueue.setMap3(product.getRegularDeliveryId());
				tmsQueue.setMap4(imgUrl);
				tmsQueue.setMap5(product.getPmsProduct().getName());
				tmsQueue.setMap6(product.getPmsSaleproduct().getName());
				tmsQueue.setMap7(product.getOrderQty().toString());
				tmsQueue.setMap8(orderPrice);
				tmsQueue.setMap9(product.getProductId());
				tmsService.sendTmsEmailQueue(tmsQueue);
			}
		}
		// TODO - 3. 신청자 sms

		return cancelCnt;
	}
	
	/**
	 * 
	 * @Method Name : updateRegularProduct
	 * @author : intune
	 * @date : 2016. 10. 21.
	 * @description :
	 *
	 * @param omsRegularproducts
	 * @return
	 * @throws Exception
	 */
	public int updateRegularProduct(List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {

		int cancelCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsRegulardeliveryproduct product : omsRegularproducts) {
			product.setUpdDt(currentDate);
			product.setUpdId(userId);

			// 1. 배송상품 상태값 변경
			try {
				dao.updateOne(product);
				cancelCnt++;
			} catch (Exception e) {
			}

			// 2. 스케쥴 상태값 변경
			if (!product.getIncreaseDay().equals(new BigDecimal("0"))) {
				OmsRegulardeliveryschedule schedule = new OmsRegulardeliveryschedule();
				schedule.setDeliveryProductNo(product.getDeliveryProductNo());
				schedule.setRegularDeliveryId(product.getRegularDeliveryId());
				schedule.setIncreaseDay(product.getIncreaseDay());

				schedule.setUpdDt(currentDate);
				schedule.setUpdId(userId);

				dao.update("oms.regular.update.delaySchedule", schedule);
			}
		}
		// TODO - 3. 신청자 sms

		return cancelCnt;
	}

//	정기배송 해지
	public int updateRegularCancel(List<OmsRegulardeliveryproduct> omsRegularproducts) throws Exception {

		int cancelCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsRegulardeliveryproduct product : omsRegularproducts) {
			product.setDeliveryProductStateCd("DELIVERY_PRODUCT_STATE_CD.CANCEL");
			product.setUpdDt(currentDate);
			product.setUpdId(userId);

			// 1. 배송상품 상태값 변경
			cancelCnt += dao.update("oms.regular.update.product", product);

			// 2. 스케쥴 상태값 변경
			OmsRegulardeliveryschedule schedule = new OmsRegulardeliveryschedule();
			schedule.setDeliveryProductNo(product.getDeliveryProductNo());
			schedule.setRegularDeliveryId(product.getRegularDeliveryId());
			schedule.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.CANCEL");

//			접수, 결재 안내인 스케쥴 상태만 변경
			schedule.setCancelAll("Y");
			schedule.setUpdDt(currentDate);
			schedule.setUpdId(userId);
			schedule.setRegularDeliveryDt("");

			dao.update("oms.regular.update.schedule", schedule);
		}

		OmsRegulardelivery regularDelivery = new OmsRegulardelivery();
		regularDelivery.setRegularDeliveryId(omsRegularproducts.get(0).getRegularDeliveryId());
		regularDelivery = (OmsRegulardelivery) dao.selectOneTable(regularDelivery);
		if (CommonUtil.isNotEmpty(regularDelivery.getPhone2())) {
			TmsQueue tmsQueue = new TmsQueue();
			String phone = regularDelivery.getPhone2().replaceAll("-", ""); // 하이픈 제거

			tmsQueue.setMsgCode("115");
			tmsQueue.setToPhone(phone);
			tmsQueue.setSubject("0to7.com 정기배송 해지완료");
			tmsQueue.setMap1(omsRegularproducts.get(0).getPmsProduct().getName());
			tmsService.sendTmsSmsQueue(tmsQueue);
		}

		MmsMember mmsMember = new MmsMember();
		mmsMember.setMemberNo(SessionUtil.getMemberNo());
		mmsMember = (MmsMember) dao.selectOneTable(mmsMember);

		if (CommonUtil.isNotEmpty(mmsMember.getEmail())) {
			TmsQueue tmsQueue = new TmsQueue();
			String imgUrl = ImageUrlUtil.productMakeURL(omsRegularproducts.get(0).getProductId(), "90", null, null);
			String now = DateUtil.getCurrentDate(DateUtil.FORMAT_14);

			
			BigDecimal price = omsRegularproducts.get(0).getRegularDeliveryPrice()
					.multiply(omsRegularproducts.get(0).getOrderQty());
			String orderPrice = CommonUtil.formatMoney(price.toString());

			tmsQueue.setMsgCode("014");
			tmsQueue.setToEmail(mmsMember.getEmail());
			tmsQueue.setSubject("0to7.com 정기배송 해지완료");
			tmsQueue.setMap1(mmsMember.getMemberName());
			tmsQueue.setMap2(now);
			tmsQueue.setMap3(omsRegularproducts.get(0).getRegularDeliveryId());
			tmsQueue.setMap4(imgUrl);
			tmsQueue.setMap5(omsRegularproducts.get(0).getPmsProduct().getName());
			tmsQueue.setMap6(omsRegularproducts.get(0).getPmsSaleproduct().getName());
			tmsQueue.setMap7(omsRegularproducts.get(0).getOrderQty().toString());
			tmsQueue.setMap8(orderPrice);
			tmsQueue.setMap9(omsRegularproducts.get(0).getProductId());
			tmsService.sendTmsEmailQueue(tmsQueue);
		}

		return cancelCnt;

	}

	/**
	 * 
	 * @Method Name : updateSchedule
	 * @author : intune
	 * @date : 2016. 10. 19.
	 * @description :
	 *
	 * @param omsRegularproducts
	 * @return
	 * @throws Exception
	 */
	public int updateSchedule(List<OmsRegulardeliveryschedule> omsRegularchedules) throws Exception {

		int updateCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsRegulardeliveryschedule schedule : omsRegularchedules) {
			schedule.setUpdDt(currentDate);
			schedule.setUpdId(userId);

			OmsRegulardeliveryschedule scheduleState = (OmsRegulardeliveryschedule) dao.selectOneTable(schedule);

//			정기배송 스케쥴 상태가 접수거나 결제안내에서만 수정가능
			if (scheduleState.getDeliveryScheduleStateCd().equals("DELIVERY_SCHEDULE_STATE_CD.REQ")
					|| scheduleState.getDeliveryScheduleStateCd().equals("DELIVERY_SCHEDULE_STATE_CD.INFO")) {

				updateCnt += dao.update("oms.regular.update.schedule", schedule);
			}
		}
		// TODO - 3. 신청자 sms

		return updateCnt;
	}

	/**
	 * 
	 * @Method Name : calcRegularDate
	 * @author : dennis
	 * @date : 2016. 8. 3.
	 * @description : 정기배송 일자 계산.
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> calcRegularDate(Map map) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String deliveryPeriodCd = (String) map.get("deliveryPeriodCd");
		BigDecimal deliveryCnt = null;
		Object deliveryCntObj = map.get("deliveryCnt");

		if (deliveryCntObj instanceof BigDecimal) {
			deliveryCnt = (BigDecimal) deliveryCntObj;
		} else if (deliveryCntObj instanceof Integer) {
			deliveryCnt = new BigDecimal((Integer) deliveryCntObj);
		} else if (deliveryCntObj instanceof String) {
			deliveryCnt = new BigDecimal((String) deliveryCntObj);
		}

		BigDecimal deliveryPeriodValue = null;
		Object deliveryPeriodValueObj = map.get("deliveryPeriodValue");

		if (deliveryPeriodValueObj instanceof BigDecimal) {
			deliveryPeriodValue = (BigDecimal) deliveryPeriodValueObj;
		} else if (deliveryPeriodValueObj instanceof Integer) {
			deliveryPeriodValue = new BigDecimal((Integer) deliveryPeriodValueObj);
		} else if (deliveryPeriodValueObj instanceof String) {
			deliveryPeriodValue = new BigDecimal((String) deliveryPeriodValueObj);
		}

		String sqlId = "";

		if (deliveryPeriodCd.indexOf("WEEK") > -1) {
			sqlId = "calcRegularWeek";
			deliveryPeriodCd = deliveryPeriodCd.substring(deliveryPeriodCd.indexOf(".") + 1, deliveryPeriodCd.indexOf("WEEK"));
		} else if (deliveryPeriodCd.indexOf("MONTH") > -1) {
			sqlId = "calcRegularMonth";
			deliveryPeriodCd = deliveryPeriodCd.substring(deliveryPeriodCd.indexOf(".") + 1, deliveryPeriodCd.indexOf("MONTH"));
		}

		OmsRegulardeliveryproduct rgs = new OmsRegulardeliveryproduct();
		rgs.setDeliveryCnt(deliveryCnt);
		rgs.setDeliveryPeriodCd(deliveryPeriodCd);
		rgs.setDeliveryPeriodValue(deliveryPeriodValue);

		List<Map> resultMap = (List<Map>) dao.selectList("oms.order." + sqlId, rgs);

		result.put("dateList", resultMap);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : getOmsRegulardeliveryByCart
	 * @author : dennis
	 * @date : 2016. 8. 3.
	 * @description : 정기배송 상품조회(cart)
	 *
	 * @param omsCart
	 * @return
	 */
	public OmsRegulardelivery getOmsRegulardeliveryByCart(OmsCart omsCart) {
		return (OmsRegulardelivery) dao.selectOne("oms.order.getOmsRegulardeliveryByCart", omsCart);
	}

	/**
	 * 
	 * @Method Name : getRegulardeliveryOrderList
	 * @author : dennis
	 * @date : 2016. 8. 3.
	 * @description : 정기배송 주문목록
	 *
	 * @param omsRegulardelivery
	 * @return
	 * @throws Exception
	 */
	public OmsRegulardelivery getRegulardeliveryOrderList(OmsRegulardelivery omsRegulardelivery) throws Exception {

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);
		String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

		omsRegulardelivery.setMemberTypeCds(memberTypeCds);
		omsRegulardelivery.setMemGradeCd(memGradeCd);

		BigDecimal deliveryProductNo = new BigDecimal(0);

		//정기배송상품
		List<OmsRegulardeliveryproduct> omsRegulardeliveryproducts = new ArrayList<OmsRegulardeliveryproduct>();

		for (OmsRegulardeliveryproduct omsRgs : omsRegulardelivery.getOmsRegulardeliveryproducts()) {
			OmsRegulardeliveryproduct rgs = new OmsRegulardeliveryproduct();
			rgs = (OmsRegulardeliveryproduct) dao.selectOne("oms.order.getOmsRegulardeliveryproductByPms", omsRgs);
			rgs.setDeliveryProductNo(deliveryProductNo);

			String deliveryProductTypeCd = rgs.getDeliveryProductTypeCd();
			BigDecimal upperDeliveryProductNo = deliveryProductNo;

			deliveryProductNo = deliveryProductNo.add(new BigDecimal(1));

			String saleStateCd = rgs.getPmsProduct().getSaleStateCd();
			String saleproductStateCd = rgs.getPmsSaleproduct().getSaleproductStateCd();

//			if ((!"SALE_STATE_CD.SALE".equals(saleStateCd))
//					|| (!"DELIVERY_PRODUCT_TYPE_CD.SET".equals(deliveryProductTypeCd)
//							&& !"SALEPRODUCT_STATE_CD.SALE".equals(saleproductStateCd))
//					|| (!"Y".equals(rgs.getRegularDeliveryYn()))) {
//				throw new ServiceException("oms.order.nonSale", new String[] {
//						CommonUtil.makeProductErrorName(rgs.getPmsProduct().getName(), rgs.getPmsSaleproduct().getName()) });
//			}

			//SET SUB
			if ("DELIVERY_PRODUCT_TYPE_CD.SET".equals(deliveryProductTypeCd)) {
				List<OmsRegulardeliveryproduct> subRgs = new ArrayList<OmsRegulardeliveryproduct>();

				for (OmsRegulardeliveryproduct subProduct : omsRgs.getOmsRegulardeliveryproducts()) {
					subProduct = (OmsRegulardeliveryproduct) dao.selectOne("oms.order.getOmsRegulardeliveryproductByPms",
							subProduct);
					subProduct.setDeliveryProductNo(deliveryProductNo);
					subProduct.setUpperDeliveryProductNo(upperDeliveryProductNo);
					subProduct.setDeliveryProductTypeCd("DELIVERY_PRODUCT_TYPE_CD.SUB");

					PmsSetproduct pmsSetproduct = new PmsSetproduct();
					pmsSetproduct.setStoreId(BaseConstants.STORE_ID);
					pmsSetproduct.setProductId(omsRgs.getProductId());
					pmsSetproduct.setSubProductId(subProduct.getProductId());

					pmsSetproduct = (PmsSetproduct) dao.selectOneTable(pmsSetproduct);		//set 정보
					subProduct.getPmsProduct().setName(pmsSetproduct.getName());

					BigDecimal setQty = pmsSetproduct.getQty();	//set 구성 수량
					subProduct.setSetQty(setQty);

					subRgs.add(subProduct);
					logger.debug("=============> deliveryProductNo : " + deliveryProductNo);
					deliveryProductNo = deliveryProductNo.add(new BigDecimal(1));
				}

				rgs.setOmsRegulardeliveryproducts(subRgs);
			}
			setRegularProductPoint(rgs, omsRegulardelivery);
			omsRegulardeliveryproducts.add(rgs);
		}

		omsRegulardelivery.setOmsRegulardeliveryproducts(omsRegulardeliveryproducts);

		return omsRegulardelivery;
	}

	/**
	 * 
	 * @Method Name : saveBilling
	 * @author : dennis
	 * @date : 2016. 8. 3.
	 * @description : billing 정보 저장
	 *
	 * @param omsRegulardelivery
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveBilling(BigDecimal memberNo, OmsPaymentif omsPaymentif) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		//회원정보
//		MmsMemberZts memberInfo = memberService.getMemberDetail(omsRegulardelivery.getMemberNo());

//		omsRegulardelivery.setMemberId(memberInfo.getMmsMember().getMemberId());
//		omsRegulardelivery.setName1(memberInfo.getMmsMember().getMemberName());
//		omsRegulardelivery.setCountryNo("82");
//		omsRegulardelivery.setPhone1(memberInfo.getMmsMember().getPhone1());
//		omsRegulardelivery.setPhone2(memberInfo.getMmsMember().getPhone2());

		//기존 정기배송data 조회
//		OmsRegulardelivery exData = (OmsRegulardelivery) dao.selectOneTable(omsRegulardelivery);
//
//		if (exData != null) {
//			dao.updateOneTable(omsRegulardelivery);
//		} else {
//			dao.insertOneTable(omsRegulardelivery);
//		}

//		dao.insertOneTable(omsRegulardelivery);

		MmsMemberZts memberInfo = new MmsMemberZts();
		memberInfo.setMemberNo(memberNo);
		memberInfo.setDeviceTypeCd(omsPaymentif.getDeviceTypeCd());
		memberInfo.setBillingKey(omsPaymentif.getLGD_BILLKEY());
		memberInfo.setRegularPaymentBusinessCd(omsPaymentif.getLGD_FINANCECODE());
		memberInfo.setRegularPaymentBusinessNm(omsPaymentif.getLGD_FINANCENAME());

		dao.updateOneTable(memberInfo);

		result.put("cardData", memberInfo);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : saveRegulardelivery
	 * @author : dennis
	 * @date : 2016. 8. 3.
	 * @description : 정기배송저장
	 *
	 * @param omsRegulardelivery
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveRegulardelivery(OmsRegulardelivery omsRegulardelivery) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String regularDeliveryId = omsRegulardelivery.getRegularDeliveryId();

		dao.insertOneTable(omsRegulardelivery);

		String deliveryProductStateCd = "DELIVERY_PRODUCT_STATE_CD.REQ";	//신청		

		List<OmsRegulardeliveryproduct> productList = new ArrayList<OmsRegulardeliveryproduct>();

		for (OmsRegulardeliveryproduct rgs : omsRegulardelivery.getOmsRegulardeliveryproducts()) {

			String deliveryPeriodCd = rgs.getDeliveryPeriodCd();
			BigDecimal deliveryCnt = rgs.getDeliveryCnt();
			BigDecimal deliveryPeriodValue = rgs.getDeliveryPeriodValue();
			BigDecimal orderQty = rgs.getOrderQty();

			//정기배송상품 등록
			rgs.setRegularDeliveryId(regularDeliveryId);

			OmsRegulardeliveryproduct omsRgs = (OmsRegulardeliveryproduct) dao
					.selectOne("oms.order.getOmsRegulardeliveryproductByPms", rgs);

			String deliveryProductTypeCd = omsRgs.getDeliveryProductTypeCd();
			String saleStateCd = omsRgs.getPmsProduct().getSaleStateCd();
			String saleproductStateCd = omsRgs.getPmsSaleproduct().getSaleproductStateCd();

			if ((!"SALE_STATE_CD.SALE".equals(saleStateCd))
					|| (!"DELIVERY_PRODUCT_TYPE_CD.SET".equals(deliveryProductTypeCd)
							&& !"SALEPRODUCT_STATE_CD.SALE".equals(saleproductStateCd))
					|| (!"Y".equals(omsRgs.getRegularDeliveryYn()))) {
				throw new ServiceException("oms.order.nonSale", new String[] {
						CommonUtil.makeProductErrorName(omsRgs.getPmsProduct().getName(),
								omsRgs.getPmsSaleproduct().getName()) });
			}

			omsRgs.setSetQty(orderQty);
			omsRgs.setDeliveryProductStateCd(deliveryProductStateCd);
			omsRgs.setDeliveryPeriodCd(deliveryPeriodCd);
			omsRgs.setDeliveryCnt(deliveryCnt);
			omsRgs.setDeliveryPeriodValue(deliveryPeriodValue);

			dao.insertOneTable(omsRgs);

			BigDecimal upperDeliveryProductNo = omsRgs.getDeliveryProductNo();

			OmsRegulardeliveryproduct product = omsRgs;

			List<OmsRegulardeliveryproduct> subProductList = new ArrayList<OmsRegulardeliveryproduct>();
			//SET 일때
			if ("DELIVERY_PRODUCT_TYPE_CD.SET".equals(deliveryProductTypeCd)) {
				for (OmsRegulardeliveryproduct subRgs : rgs.getOmsRegulardeliveryproducts()) {

					subRgs.setRegularDeliveryId(regularDeliveryId);

					OmsRegulardeliveryproduct subOmsRgs = (OmsRegulardeliveryproduct) dao
							.selectOne("oms.order.getOmsRegulardeliveryproductByPms", subRgs);

					String subsaleStateCd = subOmsRgs.getPmsProduct().getSaleStateCd();
					String subsaleproductStateCd = subOmsRgs.getPmsSaleproduct().getSaleproductStateCd();

					if (!"SALE_STATE_CD.SALE".equals(subsaleStateCd)) {
						throw new ServiceException("oms.order.nonSale",
								new String[] { CommonUtil.makeProductErrorName(subOmsRgs.getPmsProduct().getName(),
										subOmsRgs.getPmsSaleproduct().getName()) });
					}

					if (!"SALEPRODUCT_STATE_CD.SALE".equals(subsaleproductStateCd)) {
						throw new ServiceException("oms.order.nonSale",
								new String[] { CommonUtil.makeProductErrorName(subOmsRgs.getPmsProduct().getName(),
										subOmsRgs.getPmsSaleproduct().getName()) });
					}

					PmsSetproduct pmsSetproduct = new PmsSetproduct();
					pmsSetproduct.setStoreId(BaseConstants.STORE_ID);
					pmsSetproduct.setProductId(omsRgs.getProductId());
					pmsSetproduct.setSubProductId(subRgs.getProductId());

					pmsSetproduct = (PmsSetproduct) dao.selectOneTable(pmsSetproduct);		//set 정보

					BigDecimal setQty = pmsSetproduct.getQty();

					subOmsRgs.setOrderQty(setQty.multiply(orderQty));
					subOmsRgs.setSetQty(setQty);
					subOmsRgs.setDeliveryProductStateCd(deliveryProductStateCd);
					subOmsRgs.setDeliveryPeriodCd(deliveryPeriodCd);
					subOmsRgs.setDeliveryCnt(deliveryCnt);
					subOmsRgs.setDeliveryPeriodValue(deliveryPeriodValue);
					subOmsRgs.setDeliveryProductStateCd(deliveryProductStateCd);
					subOmsRgs.setUpperDeliveryProductNo(upperDeliveryProductNo);	//상위정기배송상품일련번호
					subOmsRgs.setDeliveryProductTypeCd("DELIVERY_PRODUCT_TYPE_CD.SUB");

					dao.insertOneTable(subOmsRgs);

					subProductList.add(subOmsRgs);
				}
			}



			//스케쥴 등록
			Map map = new HashMap();
			map.put("deliveryPeriodCd", deliveryPeriodCd);
			map.put("deliveryCnt", deliveryCnt);
			map.put("deliveryPeriodValue", deliveryPeriodValue);
			Map<String, Object> resultMap = calcRegularDate(map);

			String firstDate = "";

			List<Map> scheduleList = (List<Map>) resultMap.get("dateList");

			int cnt = 0;
			for (Map saveMap : scheduleList) {

				String regularDeliveryDt = (String) saveMap.get("DELIVERY_DATE");
				BigDecimal regularDeliveryOrder = (BigDecimal) saveMap.get("DELIVERY_ORDER");

				if (CommonUtil.isEmpty(firstDate)) {
					firstDate = regularDeliveryDt;
				} else {
					if (DateUtil.compareToDate(firstDate, regularDeliveryDt, DateUtil.FORMAT_2) == 1) {
						firstDate = regularDeliveryDt;
					}
				}

				OmsRegulardeliveryschedule ors = new OmsRegulardeliveryschedule();
				ors.setRegularDeliveryId(regularDeliveryId);
				ors.setDeliveryProductNo(upperDeliveryProductNo);
				ors.setRegularDeliveryOrder(regularDeliveryOrder);
				ors.setRegularDeliveryDt(regularDeliveryDt);
				ors.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.REQ");

				dao.insertOneTable(ors);

			}
			product.setRegularDeliveryDt(firstDate);

			product.setOmsRegulardeliveryproducts(subProductList);
			productList.add(product);

			String productName = omsRgs.getPmsProduct().getName();
			String strPeriodCd = deliveryPeriodCd.substring(deliveryPeriodCd.indexOf(".") + 1, deliveryPeriodCd.indexOf("WEEK"));

			//sms
			OmsOrderTms orderTms = new OmsOrderTms();
			orderTms.setType("REGULAR_COMPLETE");
			orderTms.setOrderId(regularDeliveryId);
			orderTms.setToPhone(omsRegulardelivery.getPhone2());
			orderTms.setProductName(productName);
			orderTms.setFirstDate(firstDate);
			orderTms.setDeliveryCnt(deliveryCnt.toString());
			orderTms.setPeriodCd(strPeriodCd);
			orderTms.setPeriodValue(deliveryPeriodValue.toString());
			orderTmsService.saveOrderTms(orderTms);

		}

		String email = omsRegulardelivery.getOrderEmail();
		if (CommonUtil.isNotEmpty(email)) {
			OmsOrderTms orderTms = new OmsOrderTms();
			orderTms.setType("REGULAR_COMPLETE");
			orderTms.setToEmail(email);
			orderTms.setOrderName(omsRegulardelivery.getName1());
			orderTms.setOrderId(regularDeliveryId);
			orderTms.setDeliveryName(omsRegulardelivery.getDeliveryName1());
			orderTms.setPhone1(omsRegulardelivery.getDeliveryPhone1());
			orderTms.setPhone2(omsRegulardelivery.getDeliveryPhone2());
			orderTms.setNote(omsRegulardelivery.getNote());
			StringBuffer address = new StringBuffer();
			address.append("(").append(omsRegulardelivery.getDeliveryZipCd()).append(")");
			address.append(" ").append(omsRegulardelivery.getDeliveryAddress1());
			address.append(" ").append(omsRegulardelivery.getDeliveryAddress2());
			orderTms.setAddress(address.toString());
			orderTms.setMapContent(orderTmsService.getRegularProductTempate(productList));
			orderTmsService.saveOrderEmail(orderTms);
		}

		//장바구니 삭제
		String cartProductNos = omsRegulardelivery.getCartProductNos();
		String storeId = omsRegulardelivery.getStoreId();
		cartService.deleteCartOrder(cartProductNos, storeId);

		result.put("regularDeliveryId", regularDeliveryId);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : getOrderRegularComplete
	 * @author : dennis
	 * @date : 2016. 9. 8.
	 * @description : 정기배송신청완료조회
	 *
	 * @param omsRegulardelivery
	 * @return
	 */
	public OmsRegulardelivery getOrderRegularComplete(OmsRegulardelivery omsRegulardelivery) {
		return (OmsRegulardelivery) dao.selectOne("oms.order.getOrderRegularComplete", omsRegulardelivery);
	}

	public void setRegularProductPoint(OmsRegulardeliveryproduct product, OmsRegulardelivery order) {

		String storeId = order.getStoreId();
		String channelId = order.getChannelId();
		BigDecimal memberNo = order.getMemberNo();
		String deviceTypeCd = order.getDeviceTypeCd();
		List<String> memberTypeCds = order.getMemberTypeCds();
		String memGradeCd = order.getMemGradeCd();

		BigDecimal pointSaveRate = product.getPointSaveRate();
		BigDecimal totalSalePrice = product.getRegularDeliveryPrice();

		//포인트적용가 정기배송가

		BigDecimal objPrice = PmsUtil.applyPointRate(totalSalePrice, pointSaveRate);

		SpsPointSearch spsPointSearch = new SpsPointSearch();
		spsPointSearch.setProductId(product.getProductId());

		SpsPointsave spsPointsave = pointService.getPointPromotion(product.getProductId(), objPrice);

		product.setProductPoint(spsPointsave.getBasicPoint());	//상품적립포인트(개당)
		product.setAddPoint(spsPointsave.getAddPoint());	//추가적립포인트(개당)
		product.setTotalPoint(objPrice.add(spsPointsave.getAddPoint()));	//총적립포인트(개당)

		product.setSpsPointsave(spsPointsave);

	}

	/**
	 * 
	 * @Method Name : createOrderRegularDelivery
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 정기배송 주문생성.
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createOrderRegularDelivery() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		OmsRegulardeliveryschedule or = new OmsRegulardeliveryschedule();
		or.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.INFO");
		List<OmsOrder> orderList = (List<OmsOrder>) dao.selectList("oms.order.getRegularDeliveryListByDate", or);
		int totalCnt = orderList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderList) {
			Map subResult = new HashMap();
			try {
				subResult = ((RegularService) getThis()).createOrderNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			//실패시.
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n=============================");
				errorMsg.append("\n정기배송 ID : " + order.getRegularDeliveryId());
				errorMsg.append("\n정기배송일자 : " + order.getRegularDeliveryDt());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n=============================\n");
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;

				String productName2 = "";
				int productCnt = 0;
				for (OmsOrderproduct product : order.getOmsOrderproducts()) {
					if (productCnt == 0) {
						productName2 = product.getProductName();
					}
					productCnt++;
				}

				if (productCnt > 1) {
					productName2 = productName2 + " 외 " + (productCnt - 1) + "건";
				}

				OmsOrderTms orderTms = new OmsOrderTms();
				orderTms.setType("REGULAR_PAYMENT_FAIL");
				orderTms.setToPhone(order.getPhone2());
				orderTms.setProductName(productName2);
				orderTms.setRegularDeliveryDt(order.getRegularDeliveryDt());
				orderTms.setPaymentAmt(order.getPaymentAmt().toString());
				orderTmsService.saveOrderTms(orderTms);

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

	/**
	 * 
	 * @Method Name : saveOrderRegularPaymentInfoNewTx
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 정기배송 결제안내(주문건당)
	 *
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrderRegularPaymentInfoNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String phone2 = "";
		String productName = "";
		int productCnt = 0;

		for (OmsDeliveryaddress address : order.getOmsDeliveryaddresss()) {
			phone2 = address.getPhone2();
		}

		for (OmsOrderproduct product : order.getOmsOrderproducts()) {

			if (productCnt == 0) {
				productName = product.getProductName();
			}

			//상태 update
			OmsRegulardeliveryschedule or = new OmsRegulardeliveryschedule();
			or.setRegularDeliveryId(product.getRegularDeliveryId());	//정기배송신청ID
			or.setDeliveryProductNo(product.getDeliveryProductNo());	//정기배송상품일련번호
			or.setRegularDeliveryOrder(product.getRegularDeliveryOrder()); //배송회차
			or.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.INFO");	//결제안내
			or.setShipDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));	//결제안내일자.
			dao.updateOneTable(or);

			productCnt++;

		}

		//sms
		String productName2 = productName;
		if (productCnt > 1) {
			productName2 = productName2 + " 외 " + (productCnt - 1) + "건";
		}

		String regularDeliveryDt = order.getRegularDeliveryDt();
		BigDecimal paymentAmt = order.getPaymentAmt();


		OmsOrderTms orderTms = new OmsOrderTms();
		orderTms.setType("REGULAR_PAYMENT");
		orderTms.setToPhone(phone2);
		orderTms.setProductName(productName2);
		orderTms.setRegularDeliveryDt(regularDeliveryDt);
		orderTms.setPaymentAmt(paymentAmt.toString());
		orderTmsService.saveOrderTms(orderTms);

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : saveOrderRegularPaymentInfo
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 정기배송 결제안내
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveOrderRegularPaymentInfo() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();

		OmsRegulardeliveryschedule or = new OmsRegulardeliveryschedule();
		or.setDeliveryScheduleStateCd("DELIVERY_SCHEDULE_STATE_CD.REQ");
		List<OmsOrder> orderList = (List<OmsOrder>) dao.selectList("oms.order.getRegularDeliveryListByDate", or);

		int totalCnt = orderList.size();
		int successCnt = 0;
		int failCnt = 0;
		for (OmsOrder order : orderList) {
			Map subResult = new HashMap();
			try {
				subResult = ((RegularService) getThis()).saveOrderRegularPaymentInfoNewTx(order);
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}

			//실패시.
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n=============================");
				errorMsg.append("\n정기배송 ID : " + order.getRegularDeliveryId());
				errorMsg.append("\n정기배송일자 : " + order.getRegularDeliveryDt());
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

	/**
	 * 
	 * @Method Name : createOrderNewTx
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 주문생성
	 *
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createOrderNewTx(OmsOrder order) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		//주문 생성

//			if ("20160921125956724201610051".equals(order.getOrderId())) {
//				throw new ServiceException("TEST");
//			}

		OmsPayment payment = order.getOmsPayments().get(0);

		BigDecimal paymentNo = paymentService.getNewPaymentNo();

		OmsPaymentif omsPaymentif = new OmsPaymentif();

		omsPaymentif.setOrderTypeCd("ORDER_TYPE_CD.REGULARDELIVERY");	//정기배송.
		omsPaymentif.setDeviceTypeCd(order.getDeviceTypeCd());

		omsPaymentif.setLGD_AMOUNT(payment.getPaymentAmt().toString());	//결제금액
		omsPaymentif.setLGD_PAN(payment.getBillingKey());	//정기배송 billingkey
		omsPaymentif.setLGD_INSTALL("00");	//할부개월(일시불)
		omsPaymentif.setVBV_ECI("010");//결제방식 정기배송일때 key-in
		omsPaymentif.setLGD_BUYER(order.getName1());	//구매자명
		omsPaymentif.setLGD_BUYERID(order.getMemberId());	//구매자ID
		omsPaymentif.setLGD_BUYERPHONE(order.getPhone2());	//구매자휴대폰번호
		omsPaymentif.setLGD_BUYEREMAIL(order.getEmail());	//구매자email

		omsPaymentif.setLGD_CUSTOM_USABLEPAY("SC0010");	//카드

		omsPaymentif.setLGD_OID(paymentNo.toString());

		pgService.getRequestParam(omsPaymentif);

//			omsPaymentif.setLGD_PRODUCTINFO();	//구매내역

		//전문 setting.
		order.getOmsPayments().get(0).setPaymentNo(paymentNo);
		order.getOmsPayments().get(0).setOmsPaymentif(omsPaymentif);

		result = orderService.createOrder(order);

		String resultFlag = (String) result.get(BaseConstants.RESULT_FLAG);
		if (BaseConstants.RESULT_FLAG_SUCCESS.equals(resultFlag)) {
			//ERPIF											
//			orderService.insertOmsERPIFNewTx(order);
		}

		return result;
	}

	/**
	 * 
	 * @Method Name : getRegulardeliveryproduct
	 * @author : roy
	 * @date : 2016. 10. 18.
	 * @description : 정기배송신청 상품상세
	 *
	 * @param getRegulardeliveryproduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsRegulardeliveryproduct> getRegulardeliveryproduct(OmsRegularSearch search) {
		return (List<OmsRegulardeliveryproduct>) dao.selectList("oms.regular.select.productDetail", search);
	}

	/**
	 * 
	 * @Method Name : getRegulardeliveryschedule
	 * @author : roy
	 * @date : 2016. 10. 18.
	 * @description : 정기배송 일정 상세
	 *
	 * @param getRegulardeliveryschedule
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsRegulardeliveryproduct> getRegulardeliveryschedule(OmsRegularSearch search) {
		return (List<OmsRegulardeliveryproduct>) dao.selectList("oms.regular.select.scheduleDetail", search);
	}
}
