package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsOrderproduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsLogistics extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal logisticsInoutNo; //주문상품입출고일련번호		[primary key, primary key, primary key, not null]
	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal orderProductNo; //주문상품일련번호		[primary key, primary key, primary key, not null]
	private BigDecimal claimNo; //클레임번호		[null]
	private String warehouseInoutTypeCd; //주문입출고유형코드		[not null]
	private String outReserveQty; //출고예정수량		[null]
	private BigDecimal outQty; //출고수량		[null]
	private String cancelDeliveryQty; //미출고수량		[null]
	private String inReserveQty; //입고예정수량		[null]
	private BigDecimal goodInQty; //정상입고수량		[null]
	private BigDecimal badInQty; //불량입고수량		[null]
	private BigDecimal virtualInQty; //가상입고수량		[null]
	private String deliveryIfTypeCd; //연동유형코드		[null]
	private BigDecimal deliveryOrder; //배송차수		[null]
	private String invoiceDt; //운송장생성일시		[null]
	private String deliveryServiceCd; //택배사코드		[null]
	private String invoiceNo; //송장번호		[null]
	private String logisticsStateCd; //주문입출고상태코드		[not null]
	private String deliveryCancelReasonCd; //배송승인취소사유코드		[null]
	private String cancelDeliveryReasonCd; //미출고사유코드		[null]
	private String completeDt; //처리완료일시||승인취소,출고확정,입고완료,입고취소		[null]
	private String trackingIfYn; //배송추적연동여부		[not null]
	private String trackingIfResult; //배송추적연동결과		[null]
	private BigDecimal wrapQty; //포장수량		[null]
	private BigDecimal ioSeq; //입출고일련번호		[not null]

	private OmsClaimproduct omsClaimproduct;
	private OmsOrderproduct omsOrderproduct;

	public String getWarehouseInoutTypeName(){
			return CodeUtil.getCodeName("WAREHOUSE_INOUT_TYPE_CD", getWarehouseInoutTypeCd());
	}

	public String getDeliveryIfTypeName(){
			return CodeUtil.getCodeName("DELIVERY_IF_TYPE_CD", getDeliveryIfTypeCd());
	}

	public String getDeliveryServiceName(){
			return CodeUtil.getCodeName("DELIVERY_SERVICE_CD", getDeliveryServiceCd());
	}

	public String getLogisticsStateName(){
			return CodeUtil.getCodeName("LOGISTICS_STATE_CD", getLogisticsStateCd());
	}

	public String getDeliveryCancelReasonName(){
			return CodeUtil.getCodeName("DELIVERY_CANCEL_REASON_CD", getDeliveryCancelReasonCd());
	}

	public String getCancelDeliveryReasonName(){
			return CodeUtil.getCodeName("CANCEL_DELIVERY_REASON_CD", getCancelDeliveryReasonCd());
	}
}