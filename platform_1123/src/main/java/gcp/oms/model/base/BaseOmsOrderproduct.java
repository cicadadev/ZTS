package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsErpif;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrderproduct;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrdercoupon;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.sps.model.SpsPointsave;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsOrderproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal orderProductNo; //주문상품일련번호		[primary key, primary key, primary key, not null]
	private BigDecimal claimNo; //클레임번호		[null]
	private BigDecimal upperOrderProductNo; //상위주문상품일련번호		[null]
	private BigDecimal originOrderProductNo; //원주문상품일련번호||교환재배송용		[null]
	private BigDecimal setQty; //세트구성수량		[not null]
	private String storeId; //상점ID		[not null]
	private String offshopId; //추천매장ID		[null]
	private String orderProductTypeCd; //주문상품유형코드		[not null]
	private String presentId; //사은품ID		[null]
	private String presentName; //사은품프로모션명		[null]
	private BigDecimal presentMinOrderAmt; //사은품최소주문금액		[null]
	private BigDecimal presentMaxOrderAmt; //사은품최대주문금액		[null]
	private String orderDeliveryTypeCd; //발송유형코드		[not null]
	private String categoryId; //표준카테고리ID		[null]
	private String brandId; //브랜드ID		[null]
	private String productId; //상품ID		[not null]
	private String productName; //상품명		[not null]
	private String businessId; //공급업체ID		[null]
	private String businessName; //업체명		[null]
	private String erpBusinessId; //ERP업체ID		[null]
	private String saleTypeCd; //매입유형코드		[not null]
	private String purchaseYn; //위탁매입여부		[not null]
	private String businessPhone; //업체연락처		[null]
	private String erpProductId; //ERP상품ID		[null]
	private String exportErpProductId; //수출용ERP상품ID		[null]
	private String businessProductId; //업체상품ID		[null]
	private String productNoticeTypeCd; //상품고시유형코드		[null]
	private String taxTypeCd; //과세구분코드		[not null]
	private String saleproductId; //단품ID		[not null]
	private String erpSaleproductId; //ERP단품ID		[null]
	private String erpColorId; //ERP색상옵션		[null]
	private String erpSizeId; //ERP사이즈옵션		[null]
	private String businessSaleproductId; //업체단품ID		[null]
	private String saleproductName; //단품명		[not null]
	private String warehouseId; //창고ID		[null]
	private String locationId; //로케이션ID		[null]
	private String sabangOrderId; //사방넷주문번호		[null]
	private String siteProductId; //외부몰상품ID		[null]
	private String sabangProductId; //사방넷상품ID		[null]
	private String sabangSaleproductId; //사방넷단품ID		[null]
	private String lpNo; //중국주문LP번호		[null]
	private String currencyCd; //통화코드		[null]
	private BigDecimal currencyPrice; //외화금액		[null]
	private String emsNo; //중국주문EMS번호		[null]
	private String alipayTransId; //알리페이번호		[null]
	private String partnerTransId; //파트너전송번호		[null]
	private String localDeliveryYn; //현지배송여부		[null]
	private String optionYn; //옵션여부		[not null]
	private String textOptionYn; //텍스트옵션여부		[not null]
	private String textOptionName; //텍스트옵션명		[null]
	private String textOptionValue; //텍스트옵션값		[null]
	private String dealId; //딜ID		[null]
	private String dealName; //딜명		[null]
	private String dealTypeCd; //딜유형코드		[null]
	private BigDecimal dealProductNo; //딜상품번호		[null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]
	private BigDecimal totalSalePrice; //총판매가||개당		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[not null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private String deliveryFeeFreeYn; //배송비무료여부		[not null]
	private BigDecimal productPoint; //상품적립포인트||개당		[not null]
	private String pointSaveId; //포인트적립ID		[null]
	private String pointName; //포인트적립명		[null]
	private BigDecimal pointValue; //포인트적립적용값		[null]
	private String pointTypeCd; //포인트적립유형코드		[null]
	private BigDecimal addPoint; //추가적립포인트||개당		[not null]
	private BigDecimal totalPoint; //총적립포인트||개당		[not null]
	private String saveDt; //적립일시		[null]
	private String productCouponId; //상품쿠폰ID		[null]
	private BigDecimal productCouponIssueNo; //상품쿠폰발행번호		[null]
	private BigDecimal productCouponDcAmt; //상품쿠폰할인가||개당		[null]
	private String plusCouponId; //플러스쿠폰ID		[null]
	private BigDecimal plusCouponIssueNo; //플러스쿠폰발행번호		[null]
	private BigDecimal plusCouponDcAmt; //플러스쿠폰할인가||개당		[null]
	private String orderCouponId; //주문쿠폰ID		[null]
	private BigDecimal orderCouponIssueNo; //주문쿠폰발행번호		[null]
	private BigDecimal orderCouponDcAmt; //주문쿠폰할인가||상품당		[null]
	private BigDecimal paymentAmt; //최종결제가||상품당		[not null]
	private BigDecimal tax; //세액		[not null]
	private String orderProductStateCd; //주문상품상태코드		[not null]
	private BigDecimal orderQty; //주문수량		[not null]
	private BigDecimal cancelQty; //취소수량		[not null]
	private BigDecimal outQty; //출고수량		[not null]
	private BigDecimal returnQty; //반품수량		[not null]
	private BigDecimal exchangeQty; //교환수량		[not null]
	private BigDecimal redeliveryQty; //재배송수량		[not null]
	private BigDecimal deliveryAddressNo; //배송지번호		[null]
	private BigDecimal deliveryPolicyNo; //배송정책번호		[not null]
	private String reserveYn; //예약판매여부		[not null]
	private String fixedDeliveryYn; //지정일배송여부		[not null]
	private String deliveryReserveDt; //배송예정일시||예약상품,지정일배송or주문일시		[not null]
	private String wrapYn; //포장여부		[not null]
	private BigDecimal wrapVolume; //포장부피		[not null]
	private String overseasPurchaseYn; //해외구매대행여부		[not null]
	private String boxDeliveryYn; //박스배송여부		[not null]
	private String boxUnitCd; //박스구성단위		[null]
	private BigDecimal boxUnitQty; //박스구성수량		[null]
	private BigDecimal deliveryTogetherQty; //합배송수량		[null]
	private String orderDt; //주문접수일시		[null]
	private String deliveryOrderDt; //출고지시일시		[null]
	private String shipDt; //출고완료일시		[null]
	private String deliveryDt; //배송완료일시		[null]
	private String confirmDt; //구매확정일시		[null]
	private String cancelDt; //주문취소일시		[null]
	private String deliveryCancelReasonCd; //배송승인취소사유코드		[null]
	private String sendErrorYn; //전송오류여부		[null]
	private String sendErrorReasonCd; //전송오류사유코드		[null]
	private String personalCustomsCode; //개인통관고유부호		[null]
	private String waybillUrl; //중국WAYBILLURL		[null]
	private BigDecimal calibrateSalePrice; //보정판매가		[null]
	private BigDecimal calibratePoint; //보정포인트		[null]
	private BigDecimal calibrateProductDcAmt; //보정상품쿠폰할인금액		[null]
	private BigDecimal calibratePlusDcAmt; //보정플러스쿠폰할인금액		[null]
	private BigDecimal calibrateOrderDcAmt; //보정주문쿠폰할인금액		[null]
	private BigDecimal styleNo; //스타일번호		[null]
	private String productSingleApplyYn; //상품쿠폰1개적용여부		[null]
	private String plusSingleApplyYn; //플러스쿠폰1개적용여부		[null]
	private BigDecimal virtualOutQty; //가출고수량		[null]
	private BigDecimal virtualReturnQty; //가반품수량		[null]

	private List<OmsClaimproduct> omsClaimproducts;
	private List<OmsErpif> omsErpifs;
	private List<OmsLogistics> omsLogisticss;
	private List<OmsOrderproduct> omsOrderproducts;
	private CcsDeliverypolicy ccsDeliverypolicy;
	private OmsClaim omsClaim;
	private OmsDelivery omsDelivery;
	private OmsDeliveryaddress omsDeliveryaddress;
	private OmsOrder omsOrder;
	private OmsOrdercoupon omsOrdercoupon;
	private OmsOrderproduct omsOrderproduct;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
	private SpsPointsave spsPointsave;

	public String getOrderProductTypeName(){
			return CodeUtil.getCodeName("ORDER_PRODUCT_TYPE_CD", getOrderProductTypeCd());
	}

	public String getOrderDeliveryTypeName(){
			return CodeUtil.getCodeName("ORDER_DELIVERY_TYPE_CD", getOrderDeliveryTypeCd());
	}

	public String getSaleTypeName(){
			return CodeUtil.getCodeName("SALE_TYPE_CD", getSaleTypeCd());
	}

	public String getProductNoticeTypeName(){
			return CodeUtil.getCodeName("PRODUCT_NOTICE_TYPE_CD", getProductNoticeTypeCd());
	}

	public String getTaxTypeName(){
			return CodeUtil.getCodeName("TAX_TYPE_CD", getTaxTypeCd());
	}

	public String getCurrencyName(){
			return CodeUtil.getCodeName("CURRENCY_CD", getCurrencyCd());
	}

	public String getDealTypeName(){
			return CodeUtil.getCodeName("DEAL_TYPE_CD", getDealTypeCd());
	}

	public String getPointTypeName(){
			return CodeUtil.getCodeName("POINT_TYPE_CD", getPointTypeCd());
	}

	public String getOrderProductStateName(){
			return CodeUtil.getCodeName("ORDER_PRODUCT_STATE_CD", getOrderProductStateCd());
	}

	public String getBoxUnitName(){
			return CodeUtil.getCodeName("BOX_UNIT_CD", getBoxUnitCd());
	}

	public String getDeliveryCancelReasonName(){
			return CodeUtil.getCodeName("DELIVERY_CANCEL_REASON_CD", getDeliveryCancelReasonCd());
	}

	public String getSendErrorReasonName(){
			return CodeUtil.getCodeName("SEND_ERROR_REASON_CD", getSendErrorReasonCd());
	}
}