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
public class BaseMmsMemberZtsHistory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String applyStartDt; //적용시작일시		[primary key, primary key, primary key, not null]
	private String memGradeCd; //회원등급코드		[not null]

	private MmsMemberZts mmsMemberZts;

	public String getMemGradeName(){
			return CodeUtil.getCodeName("MEM_GRADE_CD", getMemGradeCd());
	}
}