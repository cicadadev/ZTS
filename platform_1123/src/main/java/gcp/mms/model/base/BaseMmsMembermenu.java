package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsQuickmenu;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsMembermenu extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private MmsMemberZts mmsMemberZts;
	private MmsQuickmenu mmsQuickmenu;
}