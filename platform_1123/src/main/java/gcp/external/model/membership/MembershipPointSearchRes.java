package gcp.external.model.membership;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
@ToString(callSuper = true)
public class MembershipPointSearchRes extends MembershipBase {
	@JsonProperty("RES_CD")
	private String				resCd;
	@JsonProperty("RES_MSG")
	private String				resMsg;
	@JsonProperty("RESULTS")
	private List<MemberPoint>	results;



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

	public List<MemberPoint> getResults() {
		return results;
	}

	public void setResults(List<MemberPoint> results) {
		this.results = results;
	}

}
