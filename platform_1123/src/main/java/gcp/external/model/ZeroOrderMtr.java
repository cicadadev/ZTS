package gcp.external.model;


import java.util.List;

import gcp.oms.model.OmsOrderproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class ZeroOrderMtr {

	private String	stx;
	private String	pickPostno;
	private String	sellPostno;
	private String	sellAddress1;
	private String	sellAddress2;
	private String	bxCode;
	private String	sellNm;
	private String	sellPhoneno1;
	private String	sellPhoneno2;
	private String	custPostnpo;
	private String	custAddress1;
	private String	custAddress2;
	private String	custNm;
	private String	custPhoneno1;
	private String	custPhoneno2;
	private String	message;
	private String	orderNo;
	private String	orderSeq;
	private String	wbNo;
	private String	itemcd;
	private String	itemNm;
	private String	boxQty;
	private String	payCond;
	private String	payAmount;
	private String	itemseq;
	private String	unitId;
	private String	unitName;
	private String	barcode;
	private String	sectionNm;
	private String	oneDeliYn;
	private String	dasYn;
	private String	staffYn;
	private String	composeNum;
	private String	venId;
	private String	venName;
	private String	itemGb;
	private String	siteId;
	private String	ordLogisticsNos;
	
	private String orderId;
	private String orderProductNo;
	
	private String seqNo;
	private String storeId;
	
	private List<OmsOrderproduct> list; //승인된 주문상품 리스트
	
}
