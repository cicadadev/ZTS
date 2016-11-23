package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusinessinquiry;
import gcp.pms.model.PmsCategory;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsBusinessinquirycategory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal businessInquiryNo; //문의번호		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[primary key, primary key, primary key, not null]

	private CcsBusinessinquiry ccsBusinessinquiry;
	private PmsCategory pmsCategory;
}