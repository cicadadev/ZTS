package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsBatchlog extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String batchId; //배치ID		[primary key, primary key, primary key, not null]
	private BigDecimal batchLogNo; //배치로그번호		[primary key, primary key, primary key, not null]
	private String startDt; //배치시작일시		[null]
	private String endDt; //배치종료일시		[null]
	private String result; //배치실행결과		[null]

}