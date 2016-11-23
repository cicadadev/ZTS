package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.common.util.CodeUtil;
import gcp.mms.model.base.BaseMmsMemberZtsHistory;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsMemberZtsHistory extends BaseMmsMemberZtsHistory {
	
	private String preMemGradeCd;
	
	public String getPreMemGradeName(){
		return CodeUtil.getCodeName("MEM_GRADE_CD", getPreMemGradeCd());
}
	private String insName;		// 등록자
	private String updName;		// 수정자
}