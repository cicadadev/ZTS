package gcp.pms.model.excel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsProductNoticeBulk {

	@NotEmpty
	@Excel(name = "0", codegroup = "PRODUCT_NOTICE_TYPE_CD")
	private String productNoticeTypeCd; // 상품고시유형코드 [primary key, not null]

	@NotEmpty
	@Size(min = 1, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "1")
	private String productId; // 상품번호 [primary key, not null]

	// @NotEmpty
	private String productNoticeFieldId; // 상품고시항목내용 [primary key, not null]

	// @NotEmpty
	private String detail; // 내용 [not null]

	@Excel(name = "2", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail1; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "3", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail2; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "4", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail3; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "5", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail4; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "6", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail5; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "7", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail6; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "8", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail7; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "9", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail8; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "10", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail9; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "11", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail10; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "12", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail11; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "13", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail12; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "14", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail13; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "15", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail14; // 상품고시항목내용 [primary key, notnull]
	@Excel(name = "16", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail15; // 상품고시항목내용 [primary key, notnull]

	private String updDt;

	private String updId;
}
