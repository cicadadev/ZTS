package gcp.oms.model;

import java.util.List;

import gcp.mms.model.MmsMemberZts;
import gcp.oms.model.base.BaseOmsRegulardelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsRegulardelivery extends BaseOmsRegulardelivery {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = -3095578651601849626L;

	private String cartProductNos;

	private String regularPaymentBusinessNm;// 정기결제결제사명

	private OmsPaymentif omsPaymentif;

	private MmsMemberZts mmsMemberZts;

	private List<String> memberTypeCds;
	private String memGradeCd;
	private String deviceTypeCd;
	private String channelId;
	private String storeId;
	private String orderEmail;

}