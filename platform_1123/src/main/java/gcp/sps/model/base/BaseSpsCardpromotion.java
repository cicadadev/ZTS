package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsCardpromotionlang;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsCardpromotion extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal cardPromotionNo; //카드사할인번호		[primary key, primary key, primary key, not null]
	private String name; //카드사할인명		[null]
	private String cardPromotionStateCd; //카드사할인상태코드		[not null]
	private String startDt; //카드사할인시작일시		[null]
	private String endDt; //카드사할인종료일시		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]

	private List<SpsCardpromotionlang> spsCardpromotionlangs;
	private CcsStore ccsStore;

	public String getCardPromotionStateName(){
			return CodeUtil.getCodeName("CARD_PROMOTION_STATE_CD", getCardPromotionStateCd());
	}
}