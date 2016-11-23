package gcp.oms.model;

import java.math.BigDecimal;

import gcp.oms.model.base.BaseOmsRegulardeliveryproduct;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.sps.model.SpsPointsave;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsRegulardeliveryproduct extends BaseOmsRegulardeliveryproduct {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 7143400859037058453L;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;

	private String treeLevel;
	private String maxDate; // 수정가능한 최대일(다다음차수 예정일)
	private String minDate; // 수정가능한 최소일
	private String deliveryDay;
	private String deliverySchedule;
	private int memoCnt;

	// private String paymentBusinessNm; //정기결제결제사명 [null]

	private BigDecimal regularDeliveryMinCnt;
	private BigDecimal regularDeliveryMaxCnt;
	private BigDecimal regularDeliveryMaxQty;

	private BigDecimal productPoint; // 상품적립포인트
	private BigDecimal addPoint; // 추가적립포인트
	private BigDecimal totalPoint; // 총적립포인트(상품적립포인트+추가적립포인트)
	
	private BigDecimal increaseDay; // 배송일 증가
	
	private SpsPointsave spsPointsave;

	private String regularDeliveryDt;
	private String regularDeliveryYn;
	
	public BigDecimal getMemberNo() {
		return this.getOmsRegulardelivery().getMemberNo();
	}

	public String getMemberId() {
		return this.getOmsRegulardelivery().getMemberId();
	}
}