package gcp.oms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class CancelApprovalBulk {
	
	@NotNull(groups = { ValidationUtil.Update.class })
	@NumberFormat(style = Style.NUMBER)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "배송번호", target = "gcp.oms.model.OmsLogistics")
	private BigDecimal logisticsInoutNo;               // 주문상품 입출고 일련번호 [primary key]
	
	@NotEmpty(groups = { ValidationUtil.Update.class })
	@Size(min = 0, max = 20)
	@Excel(name = "주문번호", target = "gcp.oms.model.OmsLogistics")
	private String orderId;                            // 주문ID [primary key]
	
	@NotNull(groups = { ValidationUtil.Update.class })
	@NumberFormat(style = Style.NUMBER)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "주문상품일련번호", target = "gcp.oms.model.OmsLogistics")
	private BigDecimal orderProductNo;                 // 주문상품 일련번호 [primary key]
	
	@Size(min = 0, max = 100)
	@Excel(name = "배송승인취소사유", target = "gcp.oms.model.OmsLogistics", codegroup = "DELIVERY_CANCEL_REASON_CD")
	private String deliveryCancelReasonCd;             // 배송승인취소사유
	
	
	private String StoreId;                            // 상점번호

}
