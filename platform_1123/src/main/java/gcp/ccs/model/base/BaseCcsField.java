package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsFieldlang;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsField extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String fieldCd; //항목코드		[primary key, primary key, primary key, not null]
	private String fieldTypeCd; //항목유형코드		[not null]
	private String tableName; //테이블명		[not null]
	private String columnName; //항목명		[not null]
	private String dataType; //데이타타입		[null]
	private String requiredYn; //필수여부		[not null]
	private BigDecimal length; //길이		[null]
	private String format; //입력형식		[null]
	private String pkYn; //키여부		[null]

	private List<CcsFieldlang> ccsFieldlangs;

	public String getFieldName(){
			return CodeUtil.getCodeName("FIELD_CD", getFieldCd());
	}

	public String getFieldTypeName(){
			return CodeUtil.getCodeName("FIELD_TYPE_CD", getFieldTypeCd());
	}
}