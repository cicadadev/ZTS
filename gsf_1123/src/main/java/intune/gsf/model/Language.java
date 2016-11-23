/**
 * 
 */
package intune.gsf.model;

import java.io.Serializable;

import intune.gsf.model.BaseEntity;

/**
 * @author victor
 * 
 */
public class Language extends BaseEntity {
	private static final long	serialVersionUID	= 1L;
	
	private String	langCd		= null;
	
	private String	name		= null;
	
	private String	englishName	= null;
	
	private String	useYn		= null;
	
	private String	sortNo		= null;
	
	private String	useYnChk		= null;
	
	private String	updateYn		= null;
	
	
	
	public String getLangCd() {
		return langCd;
	}
	
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEnglishName() {
		return englishName;
	}
	
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	
	public String getUseYn() {
		return useYn;
	}
	
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	public String getSortNo() {
		return sortNo;
	}
	
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public String getUseYnChk() {
		return useYnChk;
	}

	public void setUseYnChk(String useYnChk) {
		this.useYnChk = useYnChk;
	}

	public String getUpdateYn() {
		return updateYn;
	}

	public void setUpdateYn(String updateYn) {
		this.updateYn = updateYn;
	}

	@Override
	public String toString() {
		return "Language [langCd=" + langCd + ", name=" + name
				+ ", englishName=" + englishName + ", useYn=" + useYn
				+ ", sortNo=" + sortNo + ", useYnChk=" + useYnChk
				+ ", updateYn=" + updateYn + "]";
	}

	
	
}
