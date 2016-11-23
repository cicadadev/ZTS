package gcp.external.model.membership;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString(callSuper = true)
public class MembershipPointRes extends MembershipBase {
	@JsonProperty("APV_DT")
	private String	apvDt;						//APV_DT
	@JsonProperty("APV_NO")
	private String	apvNo;						//APV_NO
	@JsonProperty("ACML_PINT")
	private String	acmlPint;			//ACML_PINT
	@JsonProperty("USE_PINT")
	private String	usePint;			//USE_PINT
	@JsonProperty("RMND_PINT")
	private String	rmndPint;			//RMND_PINT
	@JsonProperty("RES_CD")
	private String	resCd;						//RES_CD
	@JsonProperty("RES_MSG")
	private String	resMsg;				//RES_MSG

	public String getApvDt() {
		return apvDt;
	}

	public void setApvDt(String apvDt) {
		this.apvDt = apvDt;
	}

	public String getApvNo() {
		return apvNo;
	}

	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	public String getAcmlPint() {
		return acmlPint;
	}

	public void setAcmlPint(String acmlPint) {
		this.acmlPint = acmlPint;
	}

	public String getUsePint() {
		return usePint;
	}

	public void setUsePint(String usePint) {
		this.usePint = usePint;
	}

	public String getRmndPint() {
		return rmndPint;
	}

	public void setRmndPint(String rmndPint) {
		this.rmndPint = rmndPint;
	}

	public String getResCd() {
		return resCd;
	}

	public void setResCd(String resCd) {
		this.resCd = resCd;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

}
