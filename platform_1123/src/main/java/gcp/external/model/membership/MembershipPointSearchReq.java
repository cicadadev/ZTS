package gcp.external.model.membership;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MembershipPointSearchReq extends MembershipBase {

	@JsonProperty("BRND_CD")
	private String	brndCd;		//BRND_CD 브랜드코드(0to7 쇼핑몰:3010)
	@JsonProperty("STOR_NO")
	private String	storNo;		//STOR_NO 가맹점코드
	@JsonProperty("SRCH_DV")
	private String	srchDv;
	@JsonProperty("SRCH_DV_VLU")
	private String	srchDvVlu;


	public String getBrndCd() {
		return brndCd;
	}

	public void setBrndCd(String brndCd) {
		this.brndCd = brndCd;
	}


	public String getStorNo() {
		return storNo;
	}

	public void setStorNo(String storNo) {
		this.storNo = storNo;
	}


	public String getSrchDv() {
		return srchDv;
	}

	public void setSrchDv(String srchDv) {
		this.srchDv = srchDv;
	}


	public String getSrchDvVlu() {
		return srchDvVlu;
	}

	public void setSrchDvVlu(String srchDvVlu) {
		this.srchDvVlu = srchDvVlu;
	}




}
