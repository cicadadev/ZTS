package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //상품명		[not null]
	private String keyword; //키워드		[null]
	private String icon; //상품아이콘||복수		[null]
	private String adCopy; //상품홍보문구		[null]
	private String detail; //상세설명		[null]
	private String notice; //상품고시정보		[null]
	private String attribute1; //추가속성1		[null]
	private String attribute2; //추가속성2		[null]
	private String attribute3; //추가속성3		[null]
	private String group1; //그룹코드1		[null]
	private String group2; //그룹코드2		[null]
	private String group3; //그룹코드3		[null]
	private String claimInfoMsg; //교환반품안내메시지		[null]

	private PmsProduct pmsProduct;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}