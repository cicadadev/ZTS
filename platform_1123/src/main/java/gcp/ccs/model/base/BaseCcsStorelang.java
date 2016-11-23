package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsStore;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsStorelang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //상점명		[not null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]

	private CcsStore ccsStore;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}