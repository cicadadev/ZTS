package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProductnotice;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductnoticefield extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String productNoticeTypeCd; //상품고시유형코드		[primary key, primary key, primary key, not null]
	private String productNoticeFieldId; //상품고시항목ID		[primary key, primary key, primary key, not null]
	private String title; //제목		[not null]
	private String note; //설명		[null]
	private String erpFieldName; //ERP항목명		[null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<PmsProductnotice> pmsProductnotices;

	public String getProductNoticeTypeName(){
			return CodeUtil.getCodeName("PRODUCT_NOTICE_TYPE_CD", getProductNoticeTypeCd());
	}
}