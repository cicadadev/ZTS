package gcp.pms.model.excel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsProductReserveBulk {

	@NotEmpty
	@Size(min = 1, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "상품번호")
	private String productId; // 상점ID [primary key, not null]

	@NotEmpty
	@Size(min = 1, max = 100)
	@Excel(name = "판매상태", codegroup = "SALE_STATE_CD")
	private String saleStateCd; // 판매상태코드 [not null]

	@NotEmpty
	@Size(min = 1, max = 100)
	@Excel(name = "변경예약상태", codegroup = "PRODUCT_RESERVE_STATE_CD")
	private String productReserveStateCd; // 변경예약상태 [not null]

	@NotEmpty
	@Size(min = 1, max = 20)
	@Excel(name = "예약일시")
	private String reserveDt;

	@Size(max = 33)
	@Excel(name = "상품홍보문구")
	private String adCopy; // 상품홍보문구 [null]

	private String updDt;
	private String updId;
}
