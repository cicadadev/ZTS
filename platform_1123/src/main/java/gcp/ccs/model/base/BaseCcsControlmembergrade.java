package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsControlmembergrade extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal controlNo; //제어번호		[primary key, primary key, primary key, not null]
	private String memGradeCd; //회원등급코드		[primary key, primary key, primary key, not null]

	private CcsControl ccsControl;

	public String getMemGradeName(){
			return CodeUtil.getCodeName("MEM_GRADE_CD", getMemGradeCd());
	}
}