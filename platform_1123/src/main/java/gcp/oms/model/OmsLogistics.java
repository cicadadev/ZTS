package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.oms.model.base.BaseOmsLogistics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsLogistics extends BaseOmsLogistics {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6980405308930886578L;
	
	private String storeId;              // STORE_ID
	private String quotient;             // 피킹리스트 box수량
	private String remainder;            // 피킹리스트 나머지 갯수
	
	private String dasYn;                // DAS연동 가능여부
	private String oneDeliYn;            // 배송번호별 단일 주문상품여부
	private String sendYn;               // 데이터 전송여부
	
	private String shipYn;               // 출고여부
	private String deliveryQty;          // 출고수량(출고/미출고 리스트화면)
	private String deliveryReserveQty;   // 배송수량(출고/미출고 리스트화면)
	
	private String gubun;                // 상품,선물포장 구분
	private String infoGubun;            // 포장정보 구분
	private BigDecimal wrapVolume;       // 포장부피
	
	private String custOrdSeq;           // 배송번호별 그룹시퀀스
	private String saleTypeCd;           // 매입유형코드

	
	private List<OmsDeliverytracking> omsDeliverytrackings;
}