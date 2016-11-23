package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsPriceReserveBulk {

	@NotEmpty
	@Size(min = 1, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsPricereserve")
	private String productId; // 상점ID [primary key, not null]

	@NotEmpty
	@Excel(name = "변경신청상태", target = "gcp.pms.model.PmsPricereserve", codegroup = "PRICE_RESERVE_STATE_CD")
	private String priceReserveStateCd; // 변경신청상태 [not null]

	@NotNull
	@NumberFormat(style = Style.CURRENCY)
	@Excel(name = "정상가", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal listPrice; // 정상가 [not null]

	@NotNull
	@NumberFormat(style = Style.CURRENCY)
	@Excel(name = "판매가", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal salePrice; // 판매가 [not null]

	@NotNull
	@NumberFormat(style = Style.CURRENCY)
	@Excel(name = "공급가", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal supplyPrice; // 공급가 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@Excel(name = "정기배송가", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal regularDeliveryPrice; // 정기배송가 [null]

	@NumberFormat(style = Style.PERCENT)
	@NotNull
	@Excel(name = "수수료율", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal commissionRate; // 수수료율 [not null]

	@NumberFormat(style = Style.PERCENT)
	@NotNull
	@Excel(name = "포인트적립율", target = "gcp.pms.model.PmsPricereserve")
	private BigDecimal pointSaveRate; // 포인트적립율 [not null]

	@NotEmpty
	@Excel(name = "예약일시", target = "gcp.pms.model.PmsPricereserve")
	private String reserveDt;

	/* 3. 단품(PmsSaleproduct) */
	@Excel(name = "단품가격(", target = "gcp.pms.model.PmsSaleproductpricereserve")
	private String PmsSaleproductpricereserveArray; // 단품

	// private String updDt;
	//
	// private String updId;
}
