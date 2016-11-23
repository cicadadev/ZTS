package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.OmsReceiveordermapping;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.utils.DateUtil;

@Service
@Scope("prototype")
public class CommonOmsService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : setOmsOrder
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 주문마스터 정보 설정
	 *
	 * @param orm
	 * @return
	 */
	public OmsOrder setOmsOrder(OmsReceiveordermapping orm) {
		OmsOrder order = new OmsOrder();

		//주문번호
		order.setOrderId((String) dao.selectOne("oms.order.getNewOrderId", null));
		//사이트ID
		order.setSiteId(orm.getSiteId());
		//사이트유형코드
		order.setSiteTypeCd(orm.getSiteTypeCd());
		//외부몰주문ID
		order.setSiteOrderId(orm.getOrderId());
		//주문유형코드: 외부몰 주문
		order.setOrderTypeCd("ORDER_TYPE_CD.EXTERNAL");
		//디바이스유형코드: PC
		order.setDeviceTypeCd("DEVICE_TYPE_CD.PC");
		//주문상태코드: 신규주문(001), 주문확인(002), null(TMALL, 0to7_CN, 0to7)만 존재 => 주문완료로 통일
		order.setOrderStateCd("ORDER_STATE_CD.COMPLETE");
		//배송상태코드: 출고지시 대기
		order.setOrderDeliveryStateCd("ORDER_DELIVERY_STATE_CD.READY");
		//외부몰주문자ID
		order.setSiteMemId(orm.getUserId());
		//주문자명
		order.setName1(orm.getUserName());
		//주문자전화번호
		order.setPhone1(orm.getUserTel());
		//주문자핸드폰번호
		order.setPhone2(orm.getUserCel());
		//주문자이메일
		order.setEmail(orm.getUserEmail());
		//주문금액: 처음은 '0'으로 하고 추후에 update
		order.setOrderAmt(BigDecimal.ZERO);
		//할인금액
		order.setDcAmt(BigDecimal.ZERO);
		//결제금액: 처음은 '0'으로 하고 추후에 orderAmt와 동일하게update
		order.setPaymentAmt(BigDecimal.ZERO);
		//주문일시: 무조건 현재 작업일시
		order.setOrderDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));
		//주문자우편번호: 입력 안함
//		order.setZipCd(orm.getReceiveZipcode());
		//주문자주소: 입력 안함
//		order.setAddress1(orm.getReceiveAddr());

		return order;
	}

	/**
	 * @Method Name : setOmsPayment
	 * @author : peter
	 * @date : 2016. 10. 4.
	 * @description : 결제정보 설정
	 *
	 * @param orm
	 * @return
	 */
	public OmsPayment setOmsPayment(OmsReceiveordermapping orm) {
		OmsPayment payment = new OmsPayment();

		//결제번호
		payment.setPaymentNo((BigDecimal) dao.selectOne("oms.payment.getNewPaymentNo", null));
		//결제수단코드: 무조건 외상매출금
		payment.setPaymentMethodCd("PAYMENT_METHOD_CD.CREDITSALE");
		//결제유형코드: 결제
		payment.setPaymentTypeCd("PAYMENT_TYPE_CD.PAYMENT");
		//주결제수단여부
		payment.setMajorPaymentYn("Y");
		//결제상태코드: 결제완료
		payment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
		//무이자여부
		payment.setInterestFreeYn("N");
		//애스크로여부
		payment.setEscrowYn("N");
		//애스크로 연동여부
		payment.setEscrowIfYn("N");
		//부분취소가능여부
		payment.setPartialCancelYn("N");
		//결제금액: 처음은 '0'으로 하고 추후에 update
		payment.setPaymentAmt(BigDecimal.ZERO);
		//결제수수료
		payment.setPaymentFee(BigDecimal.ZERO);
		//결제일시: 무조건 현재 작업일시
		payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));

		return payment;
	}

	/**
	 * @Method Name : setOrderProduct
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	public List<OmsOrderproduct> setOrderProduct(OmsReceiveordermapping orm, PmsProduct product) {
		List<OmsOrderproduct> orderProductList = null;

		//본품 처리
		if ("PRODUCT_TYPE_CD.GENERAL".equals(product.getProductTypeCd())) { //일반상품
			orderProductList = setOrderProductGeneral(orm, product);
		} else if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) { //세트상품
			orderProductList = setOrderProductSet(orm, product);
		}

		return orderProductList;
	}

	/**
	 * @Method Name : setOrderProductGeneral
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 일반 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	public List<OmsOrderproduct> setOrderProductGeneral(OmsReceiveordermapping orm, PmsProduct product) {
		List<OmsOrderproduct> orderGeneralProductList = new ArrayList<OmsOrderproduct>();

		//기본항목 설정
		OmsOrderproduct orderGeneralProduct = setOrderProductBase(orm, product);

		//주문상품유형코드
		orderGeneralProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.GENERAL");

		//리스트에 추가
		orderGeneralProductList.add(orderGeneralProduct);

		//사은품정보가 존재하는 경우 추가
		if (null != orm.getOrderEtc2() && !"".equals(orm.getOrderEtc2())) {
			logger.debug("사은품존재: SEQ [" + orm.getSeq() + ", orderEtc2 [" + orm.getOrderEtc2() + "]");

			List<OmsOrderproduct> orderPresentList = setOrderProductPresent(orm);
			for (OmsOrderproduct present : orderPresentList) {
				//배송정책번호: 재설정(본품의 번호로 통일) => 사은품의 정보로 설정
//				present.setDeliveryPolicyNo(product.getDeliveryPolicyNo());

				//리스트에 추가
				orderGeneralProductList.add(present);
			}
		}

		return orderGeneralProductList;
	}

	/**
	 * @Method Name : setOrderProductSet
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 세트 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> setOrderProductSet(OmsReceiveordermapping orm, PmsProduct product) {
		List<OmsOrderproduct> orderSetProductList = new ArrayList<OmsOrderproduct>();

		//공통사용변수 설정
		String storeId = orm.getStoreId();
//		BigDecimal setDeliveryPolicyNo = product.getDeliveryPolicyNo();

		//### 세트 자체에 대한 주문상품 설정
		//기본항목 설정
		OmsOrderproduct orderSetProduct = setOrderProductBase(orm, product);
		//구성상품의 단가 계산을 위해 판매가 저장(총판매가_개당)
		BigDecimal totalSalePrice = orderSetProduct.getTotalSalePrice();

		//주문상품유형코드
		orderSetProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SET");

		//임시리스트 추가
		orderSetProductList.add(orderSetProduct);

		//### 세트 구성상품별 주문상품 설정
		//세트상품테이블 조회
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("productId", product.getProductId());
		List<PmsSetproduct> subProductList = (List<PmsSetproduct>) dao.selectList("external.receiveorder.getSubProductList",
				srchMap);
		//구성상품의 정상가 총액 산정
		double totalListPrice = 0;
		for (PmsSetproduct setOne : subProductList) {
			totalListPrice = totalListPrice + setOne.getSumListPrice().doubleValue();
		}

		//구성상품별 주문상품 설정
//		int setCnt = setProductList.size();
		BigDecimal paymentAmt = new BigDecimal(orm.getPayCost()); //사방넷 총결제금액
		BigDecimal stackPaymentAmt = new BigDecimal(0); //구성상품의 누적결제금액
		BigDecimal restPaymentAmt = new BigDecimal(0); //자투리 금액

		BigDecimal stackCurrencyPrice = new BigDecimal(0); //구성상품의 외화누적금액
		BigDecimal restCurrencyPrice = new BigDecimal(0); //외화금액의 자투리 금액
		BigDecimal currencyPrice = null;
		boolean chinaYn = false;
		if (null != orm.getCurrencyPrice() && !"".equals(orm.getCurrencyPrice())) {
			chinaYn = true;
			currencyPrice = new BigDecimal(orm.getCurrencyPrice()); //외화단가
		}
		for (int idx = 0, setCnt = subProductList.size(); idx < setCnt; idx++) {
			PmsSetproduct setOne = subProductList.get(idx);

			//세트구성상품의 상품,단품정보 조회
			PmsProduct subProduct = getProductInfo(storeId, setOne.getSaleproductId());

			//기본항목 설정
			OmsOrderproduct orderItemProduct = setOrderProductBase(orm, subProduct);

			//주문상품유형코드: 세트 구성상품
			orderItemProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SUB");
			//사은품ID: 나중에 상위주문상품일련번호를 부모의 주문상품일련번호로 변경하기 위해 부모의 단품ID 저장
			orderItemProduct.setPresentId(orm.getSkuAlias());
			//상품명: 재설정
			orderItemProduct.setProductName(setOne.getName());
			//단품ID: 재설정
			orderItemProduct.setSaleproductId(setOne.getSaleproductId());
			//세트구성수량: 재설정
			BigDecimal subSetQty = setOne.getQty();
			orderItemProduct.setSetQty(subSetQty);
			//주문수량: 재설정(세트구성수량 * 세트상품주문수량)
			BigDecimal totalOrderQty = subSetQty.multiply(new BigDecimal(orm.getSaleCnt()));
			orderItemProduct.setOrderQty(totalOrderQty);
			//판매가: 재설정(세트상품의 개당 판매가를 구성상품의 정상가*세트구성수량의 비중으로 금액분배하고 끝자리 조정)
			//=> 현 구성상품의 비율가
//			double ratePrice = (setOne.getSumListPrice().doubleValue() / totalListPrice) * product.getSalePrice().doubleValue();
			double ratePrice = (setOne.getSumListPrice().doubleValue() / totalListPrice) * totalSalePrice.doubleValue();
			//=> 현 구성상품의 비율가에 따른 단가(원단위 버림)
//			BigDecimal rateUnitPrice = new BigDecimal(
//					Math.floor((ratePrice / setOne.getQty().doubleValue()) / Math.pow(10, 1)) * Math.pow(10, 1));
			BigDecimal rateUnitPrice = new BigDecimal(Math.round(ratePrice / subSetQty.doubleValue()));
			rateUnitPrice = rateUnitPrice.setScale(-1, BigDecimal.ROUND_HALF_UP); //10원 단위 반올림
			orderItemProduct.setSalePrice(rateUnitPrice);
			//총판매가_개당: 재설정(세트상품의 개당 판매가를 구성상품의 정상가*세트구성수량의 비중으로 금액분배하고 끝자리 조정)
			orderItemProduct.setTotalSalePrice(rateUnitPrice);
			//최종결제가_상품당: 재설정(총판매가_개당 * 주문수량)
			orderItemProduct.setPaymentAmt(rateUnitPrice.multiply(totalOrderQty));
			//=> 자투리 계산을 위해 (판매단가 * 구성상품주문수량) 누적
			stackPaymentAmt = stackPaymentAmt.add(rateUnitPrice.multiply(totalOrderQty));
			//=> 마지막 자료에서 자투리 계산
			if (idx == (setCnt - 1)) {
				//자투리금액 = 전체 결제금액 - 누적 결제금액
				restPaymentAmt = paymentAmt.subtract(stackPaymentAmt);
				//보정판매가: 재설정(자투리금액)
				orderItemProduct.setCalibrateSalePrice(restPaymentAmt);
			}
			//세액: 재설정(구성상품은 0)
			orderItemProduct.setTax(BigDecimal.ZERO);
			//배송정책번호: 재설정(세트상품의 번호로 통일) => 구성상품의 정보로 설정
//			orderItemProduct.setDeliveryPolicyNo(setDeliveryPolicyNo);
			//외화금액: 재설정(중국몰의 경우)
			if (chinaYn) {
				//=> 현 구성상품의 비율가
				double chinaRatePrice = (setOne.getSumListPrice().doubleValue() / totalListPrice) * currencyPrice.doubleValue();
				//=> 현 구성상품의 비율가에 따른 단가(원단위 버림)
				BigDecimal chinaRateUnitPrice = new BigDecimal(Math.floor(chinaRatePrice / subSetQty.doubleValue()));
				chinaRateUnitPrice = chinaRateUnitPrice.setScale(0, BigDecimal.ROUND_DOWN);
				//=> 자투리 계산을 위해 (외화단가 * 세트구성수량) 누적
				stackCurrencyPrice = stackCurrencyPrice.add(chinaRateUnitPrice.multiply(subSetQty));
				logger.debug("stackCurrencyPrice: " + stackCurrencyPrice);
				//=> 마지막 자료에서 자투리 계산
				if (idx == (setCnt - 1)) {
					//자투리단가 = 전체 외화단가 - 누적 외화단가
					restCurrencyPrice = currencyPrice.subtract(stackCurrencyPrice);
					BigDecimal finalPrice = chinaRateUnitPrice.multiply(totalOrderQty);
					orderItemProduct.setCurrencyPrice(finalPrice.add(restCurrencyPrice));
				} else {
					orderItemProduct.setCurrencyPrice(chinaRateUnitPrice.multiply(totalOrderQty));
				}
			}

			//임시리스트 추가
			orderSetProductList.add(orderItemProduct);
		}

		//사은품정보가 존재하는 경우 
		if (null != orm.getOrderEtc2() && !"".equals(orm.getOrderEtc2())) {
			logger.debug("사은품존재: SEQ [" + orm.getSeq() + ", orderEtc2 [" + orm.getOrderEtc2() + "]");

			List<OmsOrderproduct> orderPresentList = setOrderProductPresent(orm);
			for (OmsOrderproduct present : orderPresentList) {
				//배송정책번호: 재설정(세트상품의 번호로 통일) => 사은품의 정보로 설정
//				orderPresent.setDeliveryPolicyNo(setDeliveryPolicyNo);

				//임시리스트 추가
				orderSetProductList.add(present);
			}
		}

		return orderSetProductList;
	}

	/**
	 * @Method Name : setOrderProductPresent
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 사은품 주문상품정보 설정
	 *
	 * @param orm
	 * @return
	 */
	public List<OmsOrderproduct> setOrderProductPresent(OmsReceiveordermapping orm) {
		List<OmsOrderproduct> orderPresentList = null;

		//사은품 상품정보 조회
		PmsProduct present = getProductInfo(orm.getStoreId(), orm.getOrderEtc2());

		if ("PRODUCT_TYPE_CD.GENERAL".equals(present.getProductTypeCd())
				|| "PRODUCT_TYPE_CD.PRESENT".equals(present.getProductTypeCd())) { //일반상품이나 사은품
			orderPresentList = setOrderPresentGeneral(orm, present);
		} else if ("PRODUCT_TYPE_CD.SET".equals(present.getProductTypeCd())) { //세트상품
			orderPresentList = setOrderPresentSet(orm, present);
		}

		return orderPresentList;
	}

	/**
	 * @Method Name : setOrderPresentGeneral
	 * @author : peter
	 * @date : 2016. 10. 27.
	 * @description : 일반이나 상품사은품이 사은품인 경우
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	public List<OmsOrderproduct> setOrderPresentGeneral(OmsReceiveordermapping orm, PmsProduct product) {
		List<OmsOrderproduct> orderGeneralPresentList = new ArrayList<OmsOrderproduct>();

		//기본항목 설정
		OmsOrderproduct orderPresent = setOrderProductBase(orm, product);

		if ("PRODUCT_TYPE_CD.PRESENT".equals(product.getProductTypeCd())) {
			//주문상품유형코드
			orderPresent.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT");
			//사은품ID: 나중에 부모의 주문상품일련번호 변경하기 위해 부모의 단품ID로 저장
			orderPresent.setPresentId(orm.getSkuAlias());
		} else {
			//주문상품유형코드
			orderPresent.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.GENERAL");
		}
		//상품명: 재설정
		orderPresent.setProductName(product.getName());
		//단품ID: 재설정(사은품 단품코드)
		orderPresent.setSaleproductId(orm.getOrderEtc2());
		//주문수량: 재설정(사은품수량)
		orderPresent.setOrderQty(new BigDecimal(orm.getOrderEtc3()));
		//판매가: 재설정(0원)
		orderPresent.setSalePrice(BigDecimal.ZERO);
		//총판매가_개당: 재설정(0원)
		orderPresent.setTotalSalePrice(BigDecimal.ZERO);
		//보정판매가: 재설정(0원)
		orderPresent.setCalibrateSalePrice(BigDecimal.ZERO);
		//최종결제가_상품당: 재설정(0원)
		orderPresent.setPaymentAmt(BigDecimal.ZERO);
		//수수료율: 재설정(0)
		orderPresent.setCommissionRate(BigDecimal.ZERO);
		//세액: 재설정(0원)
		orderPresent.setTax(BigDecimal.ZERO);
		//외화금액: 재설정(0원)
		orderPresent.setCurrencyPrice(BigDecimal.ZERO);
		//배송정책번호: 재설정(세트상품의 번호로 통일) => 구성상품의 정보로 설정
//		orderPresent.setDeliveryPolicyNo(product.getDeliveryPolicyNo());

		orderGeneralPresentList.add(orderPresent);

		return orderGeneralPresentList;
	}

	/**
	 * @Method Name : setOrderPresentSet
	 * @author : peter
	 * @date : 2016. 10. 27.
	 * @description : 세트상품이 사은품인 경우
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> setOrderPresentSet(OmsReceiveordermapping orm, PmsProduct product) {
		List<OmsOrderproduct> orderSetPresentList = new ArrayList<OmsOrderproduct>();

		//공통사용변수 설정
		String storeId = orm.getStoreId();	//상점ID

		//### 세트 자체에 대한 주문상품 설정
		//기본항목 설정
		OmsOrderproduct orderSetPresent = setOrderProductBase(orm, product);

		//주문상품유형코드
		orderSetPresent.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SET");
		//상품명: 재설정
		orderSetPresent.setProductName(product.getName());
		//단품ID: 재설정(사방넷 사은품 단품코드)
		orderSetPresent.setSaleproductId(orm.getOrderEtc2());
		//주문수량: 재설정(사방넷 사은품 수량)
		BigDecimal presentCnt = new BigDecimal(orm.getOrderEtc3());
		orderSetPresent.setOrderQty(presentCnt);
		//판매가: 재설정(0원)
		orderSetPresent.setSalePrice(BigDecimal.ZERO);
		//총판매가_개당: 재설정(0원)
		orderSetPresent.setTotalSalePrice(BigDecimal.ZERO);
		//보정판매가: 재설정(0원)
		orderSetPresent.setCalibrateSalePrice(BigDecimal.ZERO);
		//최종결제가_상품당: 재설정(0원)
		orderSetPresent.setPaymentAmt(BigDecimal.ZERO);
		//세액: 재설정(0원)
		orderSetPresent.setTax(BigDecimal.ZERO);
		//수수료율: 재설정(0)
		orderSetPresent.setCommissionRate(BigDecimal.ZERO);
		//세액: 재설정(0원)
		orderSetPresent.setTax(BigDecimal.ZERO);
		//외화금액: 재설정(0원)
		orderSetPresent.setCurrencyPrice(BigDecimal.ZERO);
		//배송정책번호: 재설정(세트상품의 번호로 통일) => 구성상품의 정보로 설정
//		orderSetPresent.setDeliveryPolicyNo(product.getDeliveryPolicyNo());

		//임시리스트 추가
		orderSetPresentList.add(orderSetPresent);

		//### 세트 구성상품별 주문상품 설정
		//세트상품테이블 조회
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("productId", product.getProductId());
		List<PmsSetproduct> subProductList = (List<PmsSetproduct>) dao.selectList("external.receiveorder.getSubProductList",
				srchMap);

		for (PmsSetproduct setOne : subProductList) {
			//세트구성상품의 상품,단품정보 조회
			PmsProduct subProduct = getProductInfo(storeId, setOne.getSaleproductId());

			//기본항목 설정
			OmsOrderproduct orderItemPresent = setOrderProductBase(orm, subProduct);

			//주문상품유형코드
			orderItemPresent.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SUB");
			//사은품ID: 나중에 상위주문상품일련번호를 부모의 주문상품일련번호로 변경하기 위해 부모사은품의 단품ID 저장
			orderItemPresent.setPresentId(orm.getOrderEtc2());
			//상품명: 재설정
			orderItemPresent.setProductName(setOne.getName());
			//단품ID: 재설정
			orderItemPresent.setSaleproductId(setOne.getSaleproductId());
			//세트구성수량: 재설정
			BigDecimal subSetQty = setOne.getQty();
			orderItemPresent.setSetQty(subSetQty);
			//주문수량: 재설정(세트구성수량 * 사은품수량)
			orderItemPresent.setOrderQty(subSetQty.multiply(presentCnt));
			//판매가: 재설정(0원)
			orderItemPresent.setSalePrice(BigDecimal.ZERO);
			//총판매가_개당: 재설정(0원)
			orderItemPresent.setTotalSalePrice(BigDecimal.ZERO);
			//보정판매가: 재설정(0원)
			orderItemPresent.setCalibrateSalePrice(BigDecimal.ZERO);
			//최종결제가_상품당: 재설정(0원)
			orderItemPresent.setPaymentAmt(BigDecimal.ZERO);
			//수수료율: 재설정(0)
			orderItemPresent.setCommissionRate(BigDecimal.ZERO);
			//세액: 재설정(0원)
			orderItemPresent.setTax(BigDecimal.ZERO);
			//배송정책번호: 재설정(세트상품의 번호로 통일) => 구성상품의 정보로 설정
//			orderItemProduct.setDeliveryPolicyNo(setDeliveryPolicyNo);
			//외화금액: 재설정
			orderItemPresent.setCurrencyPrice(BigDecimal.ZERO);

			//임시리스트 추가
			orderSetPresentList.add(orderItemPresent);
		}

		return orderSetPresentList;
	}

	/**
	 * @Method Name : setOrderProductBase
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 주문상품 기본정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	public OmsOrderproduct setOrderProductBase(OmsReceiveordermapping orm, PmsProduct product) {
		OmsOrderproduct orderProduct = new OmsOrderproduct();

		//상품명: 사방넷 정보, 사은품-세트구성상품에서 재설정
		orderProduct.setProductName(orm.getProductName());
		//단품ID: 사방넷 정보, 사은품-세트구성상품에서 재설정
		orderProduct.setSaleproductId(orm.getSkuAlias());
		//세트구성수량: 기본 "1", 사은품-세트구성상품에서 재설정
		orderProduct.setSetQty(BigDecimal.ONE);
		//주문수량: 사방넷 정보, 사은품-세트구성상품에서 재설정
		BigDecimal saleCnt = new BigDecimal(orm.getSaleCnt());
		orderProduct.setOrderQty(saleCnt);
		//판매가: 사방넷 정보(결제금액 / 주문수량), 사은품-세트구성상품에서 재설정
		BigDecimal payCost = new BigDecimal(orm.getPayCost());
		BigDecimal salePrice = payCost.divide(saleCnt, -1, BigDecimal.ROUND_HALF_UP); //10원 단위 반올림
		orderProduct.setSalePrice(salePrice);
		//총판매가_개당: 사방넷 정보(결제금액 / 주문수량), 사은품-세트구성상품에서 재설정
		orderProduct.setTotalSalePrice(salePrice);
		//보정판매가: 사방넷 정보(결제금액 - (계산된 판매가 * 주문수량)), 사은품-세트구성상품에서 재설정
		orderProduct.setCalibrateSalePrice(payCost.subtract(salePrice.multiply(saleCnt)));
		//최종결제가_상품당: 사방넷 정보, 사은품-세트구성상품에서 재설정
		orderProduct.setPaymentAmt(payCost);
		//세액: 사방넷 정보, 사은품-세트구성상품에서 재설정
		if ("TAX_TYPE_CD.FREE".equals(product.getTaxTypeCd())) { //면세
			orderProduct.setTax(BigDecimal.ZERO);
		} else { //과세
			double taxAmt = salePrice.doubleValue() * 0.1;
			orderProduct.setTax(new BigDecimal(taxAmt).setScale(-1, BigDecimal.ROUND_DOWN)); //10원 단위 버림
		}
		//수수료율: 사은품에서 재설정
		orderProduct.setCommissionRate(product.getCommissionRate());
		//보세여부에 따른 처리
		if (null != orm.getStoreType() && !"".equals(orm.getStoreType())) { //보세여부가 존재
			//VZSCNSH : zerotoseven基森保税仓库(보세)
			//VZSKRZS : zerotoseven韩国集货仓库(한국집화ICB)
			//VZSKRKR : zerotoseven品牌仓库(직발송)
			if ("VZSCNSH".equals(orm.getStoreType())) { //보세인 경우
				//현지배송여부(보세여부): 보세주문이면 "Y"
				orderProduct.setLocalDeliveryYn("Y");
				//주문상품상태코드: 보세주문이면 "출고완료" 처리 
				orderProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.SHIP");
			} else {
				//현지배송여부(보세여부)
				orderProduct.setLocalDeliveryYn("N");
				//주문상품상태코드: 출고지시대기 
				orderProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");
			}
		} else { //보세여부 미존재
			//현지배송여부(보세여부): Tmall 이외는 "N"
			orderProduct.setLocalDeliveryYn("N");
			//주문상품상태코드: Tmall 이외는 출고지시대기 
			orderProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");
		}
		//외화금액: 사방넷 정보, 사은품-세트구성상품에서 재설정
		if (null != orm.getCurrencyPrice() && !"".equals(orm.getCurrencyPrice())) {
			orderProduct.setCurrencyPrice(new BigDecimal(orm.getCurrencyPrice()));
		} else {
			orderProduct.setCurrencyPrice(BigDecimal.ZERO);
		}
		//배송정책번호
		orderProduct.setDeliveryPolicyNo(product.getDeliveryPolicyNo());

		//발송유형코드: 주문
		orderProduct.setOrderDeliveryTypeCd("ORDER_DELIVERY_TYPE_CD.ORDER");
		//카테고리ID
		orderProduct.setCategoryId(product.getCategoryId());
		//브랜드ID
		orderProduct.setBrandId(product.getBrandId());
		//상품ID
		orderProduct.setProductId(product.getProductId());
		//ERP상품ID
		orderProduct.setErpProductId(product.getErpProductId());
		//수출용ERP상품ID: 중국몰만 적용
		if ("SITE_TYPE_CD.CHINA".equals(orm.getSiteTypeCd())) {
			orderProduct.setExportErpProductId(product.getExportErpProductId());
		} else {
			orderProduct.setExportErpProductId("");
		}
		//업체ID
		orderProduct.setBusinessId(product.getBusinessId());
		//업체명
		orderProduct.setBusinessName(product.getCcsBusiness().getName());
		//매입유형코드
		orderProduct.setSaleTypeCd(product.getCcsBusiness().getSaleTypeCd());
		//위탁매입여부
		orderProduct.setPurchaseYn(product.getCcsBusiness().getPurchaseYn());
		//상품고시유형코드
		orderProduct.setProductNoticeTypeCd(product.getProductNoticeTypeCd());
		//과세구분코드
		orderProduct.setTaxTypeCd(product.getTaxTypeCd());
		//단품명
		orderProduct.setSaleproductName(product.getPmsSaleproduct().getName());
		//ERP단품ID
		orderProduct.setErpSaleproductId(product.getPmsSaleproduct().getErpSaleproductId());
		//옵션여부
		orderProduct.setOptionYn(product.getOptionYn());
		//text옵션여부
		orderProduct.setTextOptionYn("N");
		//정상가
		orderProduct.setListPrice(product.getListPrice());
		//추가판매가(0원)
		orderProduct.setAddSalePrice(BigDecimal.ZERO);
		//공급가
		orderProduct.setSupplyPrice(product.getSupplyPrice());
		//포인트적립율(0)
		orderProduct.setPointSaveRate(BigDecimal.ZERO);
		//배송비무료여부
//		orderProduct.setDeliveryFeeFreeYn("Y");
		orderProduct.setDeliveryFeeFreeYn(product.getDeliveryFeeFreeYn());
		//상품쿠폰1개적용여부: null 처리
//		orderProduct.setProductSingleApplyYn("N");
		//플러스쿠폰1개적용여부: null 처리
//		orderProduct.setPlusSingleApplyYn("N");
		//상품적립포인트_개당(0)
		orderProduct.setProductPoint(BigDecimal.ZERO);
		//추가적립포인트_개당(0)
		orderProduct.setAddPoint(BigDecimal.ZERO);
		//총적립포인트_개당(0)
		orderProduct.setTotalPoint(BigDecimal.ZERO);
		//보정포인트(0)
		orderProduct.setCalibratePoint(BigDecimal.ZERO);
		//상품쿠폰할인가_개당(0원)
		orderProduct.setProductCouponDcAmt(BigDecimal.ZERO);
		//보정상품쿠폰할인금액(0원)
		orderProduct.setCalibrateProductDcAmt(BigDecimal.ZERO);
		//플러스쿠폰할인가_개당(0원)
		orderProduct.setPlusCouponDcAmt(BigDecimal.ZERO);
		//보정플러스쿠폰할인금액(0원)
		orderProduct.setCalibratePlusDcAmt(BigDecimal.ZERO);
		//주문쿠폰할인가_상품당(0원)
		orderProduct.setOrderCouponDcAmt(BigDecimal.ZERO);
		//보정주문쿠폰할인금액(0원)
		orderProduct.setCalibrateOrderDcAmt(BigDecimal.ZERO);
		//취소수량(0)
		orderProduct.setCancelQty(BigDecimal.ZERO);
		//출고수량(0)
		orderProduct.setOutQty(BigDecimal.ZERO);
		//반품수량(0)
		orderProduct.setReturnQty(BigDecimal.ZERO);
		//교환수량(0)
		orderProduct.setExchangeQty(BigDecimal.ZERO);
		//재배송수량(0)
		orderProduct.setRedeliveryQty(BigDecimal.ZERO);
		//가출고수량
		orderProduct.setVirtualOutQty(BigDecimal.ZERO);
		//가반품수량
		orderProduct.setVirtualReturnQty(BigDecimal.ZERO);
		//예약판매여부
		orderProduct.setReserveYn("N");
		//지정일배송여부
		orderProduct.setFixedDeliveryYn("N");
		//배송예정일시_예약상품인 경우, 지정일배송일시, 주문일시: 무조건 현재 작업일자
		orderProduct.setDeliveryReserveDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));
		//포장여부
		orderProduct.setWrapYn("N");
		//포장부피(0)
		orderProduct.setWrapVolume(BigDecimal.ZERO);
		//해외구매대행여부
		orderProduct.setOverseasPurchaseYn("N");
		//박스배송여부
		orderProduct.setBoxDeliveryYn("N");
		//사방넷주문번호: 사방넷 정보
		orderProduct.setSabangOrderId(orm.getIdx());
		//외부몰상품ID: 사방넷 정보
		orderProduct.setSiteProductId(orm.getMallProductId());
		//사방넷상품ID: 사방넷 정보
		orderProduct.setSabangProductId(orm.getProductId());
		//사방넷단품ID: 사방넷 정보
		orderProduct.setSabangSaleproductId(orm.getSkuId());
		//중국주문LP번호: 사방넷 정보
		orderProduct.setLpNo(orm.getLpNo());
		//통화코드: 사방넷 정보
		orderProduct.setCurrencyCd(orm.getCurrencyCode());
		//중국주문EMS번호: 사방넷 정보
		orderProduct.setEmsNo(orm.getEmsNo());
		//알리페이번호: 사방넷 정보
		orderProduct.setAlipayTransId(orm.getAlipayTransId());
		//파트너전송번호: 사방넷 정보
		orderProduct.setPartnerTransId(orm.getPartnerTransId());
		//Waybill_URL: 사방넷 정보(미사용)
		orderProduct.setWaybillUrl(orm.getWaybillUrl());
		//주문일자: 무조건 현재 작업일자
		orderProduct.setOrderDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));

		return orderProduct;
	}

	/**
	 * @Method Name : setDeliveryAddress
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 배송지정보 설정
	 *
	 * @param orm
	 * @return
	 */
	public OmsDeliveryaddress setDeliveryAddress(OmsReceiveordermapping orm, boolean chinaMallYn) {
		OmsDeliveryaddress address = new OmsDeliveryaddress();

		//배송지번호
		address.setDeliveryAddressNo((BigDecimal) dao.selectOne("oms.order.getNewDeliveryAddressNo", null));
		//전화번호
		String phone1 = orm.getReceiveTel();
		String phone2 = orm.getReceiveCel();
		//=> 전화번호1: 값이 없으면 전화번호2로 설정
		if (null != phone1 && !"".equals(phone1)) {
			address.setPhone1(phone1.trim());
		} else {
			address.setPhone1(phone2.trim());
		}
		//=> 전화번호2: 값이 없으면 전화번호1로 설정
		if (null != orm.getReceiveCel() && !"".equals(orm.getReceiveCel())) {
			address.setPhone2(phone2.trim());
		} else {
			address.setPhone2(phone1.trim());
		}
		//이메일주소
		if (null != orm.getReceiveEmail() && !"".equals(orm.getReceiveEmail())) {
			address.setEmail(orm.getReceiveEmail().trim());
		}
		//배송메세지, 수취인명
		if (chinaMallYn) { //중국몰인 경우
			//Tmall: 배송메시지를 [LP번호]로 저장
			if ("TMALL".equals(orm.getMallId())) {
				address.setNote("[" + orm.getLpNo() + "]");
			}
			//JDmall: 배송메시지를 LP번호로 저장
			else if ("JD_HK_0to7".equals(orm.getMallId()) || "JD_HK_Goong".equals(orm.getMallId())) {
				address.setNote(orm.getLpNo());
			}
			//CNmall: 그냥 배송메시지로 저장
			else {
				if (null != orm.getDelvMsg() && !"".equals(orm.getDelvMsg())) {
					//배송메세지는 엔터값 space로 치환: DAS I/F 때문
					address.setNote(orm.getDelvMsg().replaceAll("\n", " ").trim());
				}
			}
			//수취인명
			address.setName1(orm.getReceiveName().trim());
		} else { //중국몰이 아닌 경우
			//if 해외배송: 배송메세지는 쇼핑몰주문번호로, 수취인명은 끝에 난수 추가
			if ("Y".equals(orm.getOverseaDeliveryYn())) {
				address.setNote(orm.getOrderId());
				Random random = new Random();
				address.setName1(orm.getReceiveName().trim() + Integer.toString(random.nextInt(100)));
			} else {
				if (null != orm.getDelvMsg() && !"".equals(orm.getDelvMsg())) {
					//배송메세지는 엔터값 space로 치환: DAS I/F 때문
					address.setNote(orm.getDelvMsg().replaceAll("\n", " ").trim());
				}
				address.setName1(orm.getReceiveName().trim());
			}
		}
		//우편번호
		if (null != orm.getReceiveZipcode() && !"".equals(orm.getReceiveZipcode())) {
			String zipCd = orm.getReceiveZipcode().replaceAll("-", "").trim();
			address.setZipCd(zipCd);
		}
		//주소
		String fullAddress = orm.getReceiveAddr().replaceAll("_", " ").trim();
		address.setAddress1(fullAddress);

		return address;
	}

	/**
	 * @Method Name : setDelivery
	 * @author : peter
	 * @date : 2016. 10. 19.
	 * @description : 배송정보 설정
	 *
	 * @param storeId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public OmsDelivery setDelivery(BigDecimal deliveryPolicyNo) throws Exception {
		OmsDelivery delivery = (OmsDelivery) dao.selectOne("external.receiveorder.getOmsDeliveryInfo", deliveryPolicyNo);

		return delivery;

	}
//	@SuppressWarnings("unchecked")
//	private List<OmsDelivery> setDelivery(String storeId, String orderId) throws Exception {
//		List<OmsDelivery> deliveryList = new ArrayList<OmsDelivery>();

	//주문번호별 배송정책 조회
	/*		Map<String, String> srchMap = new HashMap<String, String>();
			srchMap.put("storeId", storeId);
			srchMap.put("orderId", orderId);
			List<OmsDelivery> deliveryList = (List<OmsDelivery>) dao.selectList("external.receiveorder.getDeliveryPolicyList",
					srchMap);*/

//		for (OmsDelivery odOne : odList) {
//			OmsDelivery delivery = new OmsDelivery();
//			//배송지번호
//			delivery.setDeliveryAddressNo(odOne.getDeliveryAddressNo());
//			//배송정책번호
//			delivery.setDeliveryPolicyNo(odOne.getDeliveryPolicyNo());
//			//배송정책명
//			delivery.setName(odOne.getName());
//			//택배사코드
//			delivery.setDeliveryServiceCd(odOne.getDeliveryServiceCd());
//			//배송비유형코드
//			delivery.setDeliveryFeeTypeCd(odOne.getDeliveryFeeTypeCd());
//			//배송비
//			delivery.setDeliveryFee(new BigDecimal(0));
//			//배송비쿠폰할인금액
//			delivery.setDeliveryCouponDcAmt(new BigDecimal(0));
//			//적용배송비
//			delivery.setApplyDeliveryFee(new BigDecimal(0));
//			//합포장여부
//			delivery.setWrapTogetherYn("N");
//			//주문포장비
//			delivery.setOrderWrapFee(new BigDecimal(0));
//			//적용포장비
//			delivery.setApplyWrapFee(new BigDecimal(0));
//			//배송비부담자코드
////			delivery.setDeliveryBurdenCd("");
//			deliveryList.add(delivery);
//		}

//		return deliveryList;
//	}

	/**
	 * @Method Name : getProductInfo
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 상품,단품정보 조회
	 *
	 * @param storeId
	 * @param saleproductId
	 * @return
	 */
	public PmsProduct getProductInfo(String storeId, String saleproductId) {
//		logger.debug("storeId: " + storeId + ", saleproductId: " + saleproductId);
		PmsProductSearch search = new PmsProductSearch();
		search.setStoreId(storeId);
		search.setSaleproductId(saleproductId);

		return (PmsProduct) dao.selectOne("external.receiveorder.getProductInfo", search);
	}

	/**
	 * @Method Name : preTreatmentReceiveOrder
	 * @author : peter
	 * @date : 2016. 10. 26.
	 * @description : 터미널 주문데이터 사전 처리
	 *
	 * @param type
	 */
	public void updatePretreatmentRoNewTx(String type) {
		//중복주문 Error 처리: copy_idx 가 '0'이 아닌 경우
		dao.update("external.receiveorder.updateCopyIdxError", null);

		//사이트정보 Error 처리: mall_id 나 mall_user_id 가 존재하지 않는 경우
		dao.update("external.receiveorder.updateSiteInfoError", null);

		//주문번호 오류: 주문번호가 없는 경우
		dao.update("external.receiveorder.updateOrderIdNullError", null);

		//기등록 여부: 주문번호에 "+" 가 없는 경우만 체크
		dao.update("external.receiveorder.updateRegisterAgainError", null);

		//단품코드 오류: 단품코드가 없는 경우
		dao.update("external.receiveorder.updateSkuAliasNullError", null);

		//해외배송여부 설정(국내외부몰만): OVERSEA_DELIVERY_YN = 'Y' => 현재 해외배송은 없음
		if ("SBN".equals(type)) {
			dao.update("external.receiveorder.updateOverseaDeliveryYn", null);
		}
	}

	/**
	 * @Method Name : updateCheckEachRoNewTx
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 전체 주문 건별 오류 처리
	 *
	 * @param ormList
	 */
	@SuppressWarnings("unchecked")
	public void updateCheckEachRoNewTx(Map<String, String> srchMap) {
		//사방넷 주문정보 사전 조회
		List<OmsReceiveordermapping> preOrmList = (List<OmsReceiveordermapping>) dao
				.selectList("external.receiveorder.getReceiveOrderList", srchMap);

		//Error 건수
		int errorCnt = 0;
		for (OmsReceiveordermapping ormOne : preOrmList) {
			String skuAlias = ormOne.getSkuAlias();

			//유효성 체크
			String rtnMsg = checkValidation(ormOne);
			logger.debug("rtnMsg: " + rtnMsg);
			if (null != rtnMsg && !"".equals(rtnMsg)) {
				ormOne.setBoMsg(rtnMsg);
				dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
				errorCnt++;
				continue;
			}

			//사은품정보가 존재하는 경우: 상품,단품정보 존재하는지 체크
			if (null != ormOne.getOrderEtc2() && !"".equals(ormOne.getOrderEtc2())) {
				Map<String, String> presentMap = new HashMap<String, String>();
				presentMap.put("storeId", ormOne.getStoreId());
				presentMap.put("saleproductId", ormOne.getOrderEtc2());
				String productTypeCd = (String) dao.selectOne("external.receiveorder.getSaleproductExistYn", presentMap);
				if (null == productTypeCd) {
					ormOne.setBoMsg("사은품코드에 대한 자료가 없습니다.");
					dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
					errorCnt++;
					continue;
				} else {
					//사은품 수량 존재여부 체크
					if (null == ormOne.getOrderEtc3() || "".equals(ormOne.getOrderEtc3())
							|| Integer.parseInt(ormOne.getOrderEtc3()) == 0) {
						ormOne.setBoMsg("사은품 수량이 없습니다.");
						dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
						errorCnt++;
						continue;
					}
				}
			}

			//상품,단품정보 조회
			//1. 단품코드값이 null인 경우 에러처리(BO_YN = 'V')하고 skip
			if (null == skuAlias || "".equals(skuAlias)) {
				ormOne.setBoMsg("옵션코드가 없습니다.");
				dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
				errorCnt++;
				continue;
			}
			//2. 옵션코드(단품코드)로 찾지 못한 경우 에러처리(BO_YN = 'V')하고 skip
			else {
				Map<String, String> optionMap = new HashMap<String, String>();
				optionMap.put("storeId", ormOne.getStoreId());
				optionMap.put("saleproductId", skuAlias);
				String productTypeCd = (String) dao.selectOne("external.receiveorder.getSaleproductExistYn", optionMap);
				if (null == productTypeCd) {
					ormOne.setBoMsg("단품코드에 대한 자료가 없습니다.");
					dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
					errorCnt++;
					continue;
				} else {
					//상품종류가 일반이나 세트가 아니면 오류
					if (!"PRODUCT_TYPE_CD.GENERAL".equals(productTypeCd) && !"PRODUCT_TYPE_CD.SET".equals(productTypeCd)) {
						ormOne.setBoMsg("상품종류 오류입니다.");
						dao.update("external.receiveorder.updateReceiveOrderError", ormOne);
						errorCnt++;
						continue;
					}
				}
			}
		}

		//에러발생 보고
		if (errorCnt > 0) {
			String mallType = srchMap.get("mallType");
			if (null == mallType) {
				logger.debug("SabangNet ERROR Count: " + errorCnt);
			} else {
				logger.debug(mallType + " ERROR Count: " + errorCnt);
			}
		}
	}

	/**
	 * @Method Name : checkValidation
	 * @author : peter
	 * @date : 2016. 8. 2.
	 * @description : 주문수집 데이터 유효성 체크
	 *
	 * @param orm
	 * @return
	 */
	private String checkValidation(OmsReceiveordermapping orm) {
		//주문수량 오류
		if (null == orm.getSaleCnt() || "".equals(orm.getSaleCnt()) || Integer.parseInt(orm.getSaleCnt()) <= 0) {
			logger.debug("Sale Count: " + orm.getSaleCnt() + ", Order ID: " + orm.getOrderId());
			return "주문수량이 없습니다";
		}
		//판매단가 오류
		if (null == orm.getSaleCost() || "".equals(orm.getSaleCost()) || Integer.parseInt(orm.getSaleCost()) < 0) {
			logger.debug("Sale Cost: " + orm.getSaleCost() + ", Order ID: " + orm.getOrderId());
			return "판매단가 오류입니다";
		}
		//주문총결제금액 오류
		if (null == orm.getPayCost() || "".equals(orm.getPayCost()) || Integer.parseInt(orm.getPayCost()) <= 0) {
			logger.debug("Pay Cost: " + orm.getPayCost() + ", Order ID: " + orm.getOrderId());
			return "주문총결제금액 오류입니다";
		}
		//수취인명 오류
		if (null == orm.getReceiveName() || "".equals(orm.getReceiveName())) {
			logger.debug("Receive Name: " + orm.getReceiveName() + ", Order ID: " + orm.getOrderId());
			return "수취인 이름이 없습니다";
		} else {
			String receiveName = orm.getReceiveName().trim();
			if (receiveName.getBytes().length > 100) {
				logger.debug("Receive Name: " + orm.getReceiveName() + ", Order ID: " + orm.getOrderId());
				return "수취인명 길이가 100 byte를 초과하였습니다";
			}
		}
		//수취인 전화번호,핸드폰번호 오류
		if ((null == orm.getReceiveTel() || "".equals(orm.getReceiveTel()))
				&& (null == orm.getReceiveCel() || "".equals(orm.getReceiveCel()))) {
			logger.debug("Receive Tel: " + orm.getReceiveTel() + ", Receive Cel: " + orm.getReceiveCel() + ", Order ID: "
					+ orm.getOrderId());
			return "수취인 연락처가 없습니다";
		}
		//수취인 우편번호 오류
		if (null == orm.getReceiveZipcode() || "".equals(orm.getReceiveZipcode())) {
			logger.debug("Receive Zipcode: " + orm.getReceiveZipcode() + ", Order ID: " + orm.getOrderId());
			return "우편번호가 없습니다";
		} else {
			String zipCd = orm.getReceiveZipcode().replaceAll("-", "").trim();
			if (zipCd.length() < 5 || zipCd.length() > 6 || zipCd.contains("00000") || !isHanjinZipCode(zipCd)) {
				logger.debug("Receive Zipcode: " + orm.getReceiveZipcode() + ", Order ID: " + orm.getOrderId());
				return "우편번호가 정확하지 않습니다";
			}
		}
		//수취인 주소
		if (null == orm.getReceiveAddr() || "".equals(orm.getReceiveAddr())) {
			logger.debug("Receive Address: " + orm.getReceiveAddr() + ", Order ID: " + orm.getOrderId());
			return "수취인 주소가 없습니다";
		} else {
			String receiveAddr = orm.getReceiveAddr().trim();
			if (receiveAddr.getBytes().length > 200) {
				logger.debug("Receive Address: " + orm.getReceiveAddr() + ", Order ID: " + orm.getOrderId());
				return "수취인 주소가 200 byte를 초과하였습니다";
			}
		}

		return "";
	}

	/**
	 * @Method Name : isHanjinZipCode
	 * @author : peter
	 * @date : 2016. 8. 2.
	 * @description : 6자리 우편번호의 경우 한진택배 우편번호인지 확인
	 *
	 * @param zipCd
	 * @return
	 * @throws Exception
	 */
	private boolean isHanjinZipCode(String zipCd) {
		//6자리 같은번호가 한진우편번호인지 확인
		if (zipCd.length() == 6) {
			String zipCd1 = zipCd.substring(0, 3);
			String zipCd2 = zipCd.substring(3, 6);

			if (zipCd1.equals(zipCd2)) {
				//한진 배송DB 조회 
				int cnt = (int) edi.selectOne("oms.logistics.checkZipCodeHanjin", zipCd);

				if (cnt > 0) {
					return true;
				} else {
					return false;
				}

			} else {
				return true;
			}
		} else {
			return true;
		}
	}
}