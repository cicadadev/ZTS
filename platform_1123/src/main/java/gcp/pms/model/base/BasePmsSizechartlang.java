package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsSizechart;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsSizechartlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //사이즈조견표명		[not null]
	private String detail; //사이즈조견표내용		[null]

	private PmsSizechart pmsSizechart;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}