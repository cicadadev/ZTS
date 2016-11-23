package gcp.external.model;

import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsReceiveordermapping extends BaseEntity {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1L;

	private String	seq;
	private String	idx;
	private String	orderId;
	private String	mallId;
	private String mallUserId;
	private String	orderStatus;
	private String	userId;
	private String	userName;
	private String	userTel;
	private String	userCel;
	private String	userEmail;
	private String	receiveTel;
	private String	receiveCel;
	private String	receiveEmail;
	private String	delvMsg;
	private String	receiveName;
	private String	receiveZipcode;
	private String	receiveAddr;
	private String	payCost;
	private String wonCost;
	private String	orderDate;
	private String	mallProductId;
	private String	productId;
	private String	skuId;
	private String	productName;
	private String	saleCost;
	private String	skuValue;
	private String	saleCnt;
	private String compaynyGoodsCd;
	private String	skuAlias;
	private String	orderGubun;
	private String	orderEtc2;
	private String orderEtc3;
	private String	ordField2;
	private String	copyIdx;
	private String boYn;
	private String	boMsg;
	private String	boOrderId;
	private String	boDeliNo;
	private String	goodsNmPr;
	private String	overseaDeliveryYn;
	private String	lpNo;
	private String	currencyCode;
	private String	currencyPrice;
	private String	emsNo;
	private String	alipayTransId;
	private String	partnerTransId;
	private String waybillUrl;
	private String storeType;
	
	private String	storeId;
	private String	insId;
	private String siteId;
	private String siteName;
	private String siteTypeCd;

}
