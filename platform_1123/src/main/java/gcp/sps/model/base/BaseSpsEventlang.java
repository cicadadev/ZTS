package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsEvent;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //이벤트명		[not null]
	private String detail; //설명		[null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String text1; //텍스트1		[null]
	private String text2; //텍스트2		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]

	private SpsEvent spsEvent;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}