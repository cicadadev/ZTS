package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsEvent;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventprize extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private BigDecimal eventPrizeNo; //이벤트혜택번호		[primary key, primary key, primary key, not null]
	private String joinTypeCd; //출석조건유형코드		[not null]
	private BigDecimal dayValue; //기간적용값		[not null]
	private String joinStartDt; //지정기간 시작일자		[null]
	private String joinEndDt; //지정기간 종료일자		[null]
	private String prizeTypeCd; //혜택유형코드		[not null]
	private String couponId; //쿠폰ID		[null]
	private BigDecimal savePoint; //적립포인트		[null]

	private SpsCoupon spsCoupon;
	private SpsEvent spsEvent;

	public String getJoinTypeName(){
			return CodeUtil.getCodeName("JOIN_TYPE_CD", getJoinTypeCd());
	}

	public String getPrizeTypeName(){
			return CodeUtil.getCodeName("PRIZE_TYPE_CD", getPrizeTypeCd());
	}
}