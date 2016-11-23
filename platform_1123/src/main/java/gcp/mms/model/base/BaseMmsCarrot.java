package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsCarrot extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal carrotNo; //당근번호		[primary key, primary key, primary key, not null]
	private BigDecimal carrot; //당근		[null]
	private String carrotTypeCd; //당근유형코드		[null]
	private String note; //비고		[null]
	private String expireDt; //만료일시		[not null]
	private BigDecimal balanceCarrot; //만료당근		[not null]

	private MmsMemberZts mmsMemberZts;

	public String getCarrotTypeName(){
			return CodeUtil.getCodeName("CARROT_TYPE_CD", getCarrotTypeCd());
	}
}