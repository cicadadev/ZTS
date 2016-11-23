package gcp.external.model.membership;

import com.fasterxml.jackson.annotation.JsonProperty;

import intune.gsf.common.utils.DateUtil;

public class MembershipBase {

	public MembershipBase(){
		
		String yyyymmdd = DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		String HHmmss = DateUtil.getCurrentDate(DateUtil.FORMAT_12);
		this.setTrcNo(yyyymmdd + HHmmss);

		this.setCoopcoCd("7020");//제휴사코드
		this.setChnlTypCd("1");//채널유형코드 (1:온라인,2:오프라인)

		this.setTrscDt(yyyymmdd);//관계사기준 매출/결제일자(YYYYMMDD)
		this.setTrscHr(HHmmss);//관계사기준 매출/결제시간(HH24MISS)

	}

	@JsonProperty("TRSC_TYP_CD")
	private String	trscTypCd;											//TRSC_TYP_CD (거래유형코드)
	@JsonProperty("TRSC_BIZ_DV_CD")
	private String	trscBizDvCd;							//TRSC_BIZ_DV_CD(업무구분코드)
	@JsonProperty("COOPCO_CD")
	private String	coopcoCd;														//COOPCO_CD(제휴사코드)
	@JsonProperty("TRSC_DT")
	private String	trscDt;																	//TRSC_DT(거래일자)
	@JsonProperty("TRSC_HR")
	private String	trscHr;																	//TRSC_HR(거래시간)
	@JsonProperty("TRC_NO")
	private String	trcNo;																					//TRC_NO(추적번호)
	@JsonProperty("CHNL_TYP_CD")
	private String	chnlTypCd;									//CHNL_TYP_CD(채널유형코드)

	public String getTrscTypCd() {
		return trscTypCd;
	}

	public void setTrscTypCd(String trscTypCd) {
		this.trscTypCd = trscTypCd;
	}

	public String getTrscBizDvCd() {
		return trscBizDvCd;
	}

	public void setTrscBizDvCd(String trscBizDvCd) {
		this.trscBizDvCd = trscBizDvCd;
	}

	public String getCoopcoCd() {
		return coopcoCd;
	}

	public void setCoopcoCd(String coopcoCd) {
		this.coopcoCd = coopcoCd;
	}

	public String getTrscDt() {
		return trscDt;
	}

	public void setTrscDt(String trscDt) {
		this.trscDt = trscDt;
	}

	public String getTrscHr() {
		return trscHr;
	}

	public void setTrscHr(String trscHr) {
		this.trscHr = trscHr;
	}

	public String getTrcNo() {
		return trcNo;
	}

	public void setTrcNo(String trcNo) {
		this.trcNo = trcNo;
	}

	public String getChnlTypCd() {
		return chnlTypCd;
	}

	public void setChnlTypCd(String chnlTypCd) {
		this.chnlTypCd = chnlTypCd;
	}

}
