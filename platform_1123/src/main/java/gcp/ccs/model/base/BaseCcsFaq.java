package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsFaqlang;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsFaq extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal faqNo; //FAQ번호		[primary key, primary key, primary key, not null]
	private String faqTypeCd; //FAQ유형코드		[not null]
	private String title; //제목		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]
	private String detail; //내용		[not null]

	private List<CcsFaqlang> ccsFaqlangs;
	private CcsStore ccsStore;

	public String getFaqTypeName(){
			return CodeUtil.getCodeName("FAQ_TYPE_CD", getFaqTypeCd());
	}
}