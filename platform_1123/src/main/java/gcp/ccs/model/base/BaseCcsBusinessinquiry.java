package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusinessinquirycategory;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsBusinessinquiry extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal businessInquiryNo; //문의번호		[primary key, primary key, primary key, not null]
	private String name; //업체명		[not null]
	private String phone; //전화번호		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String homepageUrl; //홈페이지주소		[null]
	private String depName; //담당부서		[null]
	private String managerName; //운영자명		[null]
	private String managerPhone1; //운영자전화번호1		[null]
	private String managerEmail; //운영자이메일		[null]
	private String detail; //내용		[not null]
	private String img; //이미지		[null]

	private List<CcsBusinessinquirycategory> ccsBusinessinquirycategorys;
	private CcsStore ccsStore;

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}
}