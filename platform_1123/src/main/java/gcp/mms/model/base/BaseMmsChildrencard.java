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
public class BaseMmsChildrencard extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String accountNo; //다자녀카드번호		[primary key, primary key, primary key, not null]
	private String childrencardTypeCd; //다자녀카드유형코드		[not null]
	private String startDt; //시작일		[null]
	private String endDt; //종료일		[null]
	private String regYn; //등록여부		[not null]
	private BigDecimal memberNo; //회원번호		[null]
	private String name; //이름		[null]

	private MmsMemberZts mmsMemberZts;

	public String getChildrencardTypeName(){
			return CodeUtil.getCodeName("CHILDRENCARD_TYPE_CD", getChildrencardTypeCd());
	}
}