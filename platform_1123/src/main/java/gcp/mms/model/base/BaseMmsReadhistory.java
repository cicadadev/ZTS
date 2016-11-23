package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsReadhistory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal readhistoryNo; //null		[primary key, primary key, primary key, not null]
	private String userId; //null		[not null]
	private BigDecimal memberNo; //null		[null]
	private String detail; //null		[null]

}