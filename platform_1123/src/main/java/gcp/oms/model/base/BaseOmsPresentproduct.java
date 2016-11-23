package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPresentproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String presentId; //사은품ID		[primary key, primary key, primary key, not null]
	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal orderProductNo; //주문상품일련번호		[primary key, primary key, primary key, not null]

}