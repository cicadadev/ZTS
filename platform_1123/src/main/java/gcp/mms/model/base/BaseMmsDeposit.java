package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import gcp.oms.model.OmsOrder;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsDeposit extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal depositNo; //예치금번호		[primary key, primary key, primary key, not null]
	private BigDecimal depositAmt; //예치금		[null]
	private String depositTypeCd; //예치금유형코드		[null]
	private String note; //비고		[null]
	private String orderId; //주문ID		[null]
	private BigDecimal claimNo; //클레임번호		[null]

	private MmsMemberZts mmsMemberZts;
	private OmsOrder omsOrder;

	public String getDepositTypeName(){
			return CodeUtil.getCodeName("DEPOSIT_TYPE_CD", getDepositTypeCd());
	}
}