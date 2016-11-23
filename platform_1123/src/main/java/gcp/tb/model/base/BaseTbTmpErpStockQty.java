package gcp.tb.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import java.sql.Date;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseTbTmpErpStockQty extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String barcode; //null		[primary key, primary key, primary key, not null]
	private BigDecimal stockQty; //null		[not null]
	private String updateYn; //null		[null]
	private Date updateDate; //null		[null]
	private Date insertDate; //null		[primary key, primary key, primary key, not null]
	private BigDecimal realUpdateStockQty; //null		[null]
	private String itemCode; //null		[not null]
	private BigDecimal orderCnt; //null		[null]
	private BigDecimal apiStockQty; //null		[null]
	private BigDecimal updateQty; //null		[null]
	private BigDecimal virStockQty; //null		[null]
	private String gbnCd; //null		[null]


	public String getGbnName(){
			return CodeUtil.getCodeName("GBN_CD", getGbnCd());
	}
}