package gcp.oms.model;

import java.math.BigDecimal;

import gcp.oms.model.base.BaseOmsPresentproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPresentproduct extends BaseOmsPresentproduct {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1L;

	private String orderProductNos; // 사은품프로모션ID [null]
	private BigDecimal presentTargetAmt; // 사은품프로모션ID [null]
	private BigDecimal presentClaimAmt; // 사은품프로모션ID [null]
	private BigDecimal presentMinOrderAmt; // 사은품프로모션ID [null]
	private BigDecimal presentMaxOrderAmt; // 사은품프로모션ID [null]
}