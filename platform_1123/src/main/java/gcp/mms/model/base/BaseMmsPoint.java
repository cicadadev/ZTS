package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMember;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsPoint extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal pointNo; //포인트번호		[primary key, primary key, primary key, not null]
	private BigDecimal point; //포인트		[null]
	private String pointTypeCd; //포인트유형코드		[null]
	private String note; //비고		[null]

	private MmsMember mmsMember;

	public String getPointTypeName(){
			return CodeUtil.getCodeName("POINT_TYPE_CD", getPointTypeCd());
	}
}