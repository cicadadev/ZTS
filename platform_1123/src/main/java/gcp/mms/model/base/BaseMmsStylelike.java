package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsStyle;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsStylelike extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal styleNo; //스타일번호		[primary key, primary key, primary key, not null]
	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]

	private MmsMemberZts mmsMemberZts;
	private MmsStyle mmsStyle;
}