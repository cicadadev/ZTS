package gcp.external.model.membership;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
@ToString(callSuper = true)
public class MemberPoint {

	@JsonProperty("UNFY_MMB_NO")
	private String	unfyMmbNo;

	@JsonProperty("XTNCT_SCHE_PINT")
	private String	xtnctSchePint;

	@JsonProperty("USE_PINT_CMLT")
	private String	usePintCmlt;

	@JsonProperty("TNSF_POSB_PINT")
	private String	tnsfPosbPint;

	@JsonProperty("RMND_PINT")
	private String	rmndPint;

	@JsonProperty("PREM_MMB_YN")
	private String	premMmbYn;

	@JsonProperty("ACML_PINT_CMLT")
	private String	acmlPintCmlt;

	@JsonProperty("MMB_NM")
	private String	mmbNm;

	@JsonProperty("STFF_DV_CD")
	private String	stffDvCd;
	
	@JsonProperty("SOC_MMB_YN")
	private String	socMmbYn;
	
	@JsonProperty("AGRM_YN")
	private String	agrmYn;
	
	@JsonProperty("STPL_AGRM_YN")
	private String	stplAgrmYn;
	

	@JsonProperty("SOC_KIND_CD")
	private String	socKindCd;
	
	
	public String getStffDvCd() {
		return stffDvCd;
	}

	public void setStffDvCd(String stffDvCd) {
		this.stffDvCd = stffDvCd;
	}

	public String getPremMmbYn() {
		return premMmbYn;
	}

	public void setPremMmbYn(String premMmbYn) {
		this.premMmbYn = premMmbYn;
	}

	public String getUnfyMmbNo() {
		return unfyMmbNo;
	}

	public void setUnfyMmbNo(String unfyMmbNo) {
		this.unfyMmbNo = unfyMmbNo;
	}

	public String getMmbNm() {
		return mmbNm;
	}

	public void setMmbNm(String mmbNm) {
		this.mmbNm = mmbNm;
	}


	public String getRmndPint() {
		return rmndPint;
	}

	public void setRmndPint(String rmndPint) {
		this.rmndPint = rmndPint;
	}

	public String getTnsfPosbPint() {
		return tnsfPosbPint;
	}

	public void setTnsfPosbPint(String tnsfPosbPint) {
		this.tnsfPosbPint = tnsfPosbPint;
	}

	public String getAcmlPintCmlt() {
		return acmlPintCmlt;
	}

	public void setAcmlPintCmlt(String acmlPintCmlt) {
		this.acmlPintCmlt = acmlPintCmlt;
	}


	public String getUsePintCmlt() {
		return usePintCmlt;
	}

	public void setUsePintCmlt(String usePintCmlt) {
		this.usePintCmlt = usePintCmlt;
	}

	public String getXtnctSchePint() {
		return xtnctSchePint;
	}


	public void setXtnctSchePint(String xtnctSchePint) {
		this.xtnctSchePint = xtnctSchePint;
	}

	public String getSocMmbYn() {
		return socMmbYn;
	}

	public void setSocMmbYn(String socMmbYn) {
		this.socMmbYn = socMmbYn;
	}

	public String getAgrmYn() {
		return agrmYn;
	}

	public void setAgrmYn(String agrmYn) {
		this.agrmYn = agrmYn;
	}

	public String getStplAgrmYn() {
		return stplAgrmYn;
	}

	public void setStplAgrmYn(String stplAgrmYn) {
		this.stplAgrmYn = stplAgrmYn;
	}

	public String getSocKindCd() {
		return socKindCd;
	}

	public void setSocKindCd(String socKindCd) {
		this.socKindCd = socKindCd;
	}


}
