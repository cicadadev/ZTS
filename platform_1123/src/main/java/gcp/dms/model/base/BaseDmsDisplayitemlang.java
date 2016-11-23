package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplayitem;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsDisplayitemlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String displayId; //전시ID		[primary key, primary key, primary key, not null]
	private BigDecimal displayItemNo; //전시대상번호		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String text1; //텍스트1		[null]
	private String text2; //텍스트2		[null]
	private String title; //타이틀		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]

	private DmsDisplayitem dmsDisplayitem;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}