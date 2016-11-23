package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsDealproduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDealmember extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String dealId; //딜ID		[primary key, primary key, primary key, not null]
	private BigDecimal dealProductNo; //딜상품번호		[primary key, primary key, primary key, not null]
	private String memGradeCd; //회원등급코드		[primary key, primary key, primary key, not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]
	private BigDecimal preOpenDays; //선오픈일수		[not null]

	private SpsDealproduct spsDealproduct;

	public String getMemGradeName(){
			return CodeUtil.getCodeName("MEM_GRADE_CD", getMemGradeCd());
	}
}