package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsNotice;
import gcp.pms.model.PmsBrand;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsNoticeBrand extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal noticeNo; //공지사항번호		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[primary key, primary key, primary key, not null]

	private CcsNotice ccsNotice;
	private PmsBrand pmsBrand;
}