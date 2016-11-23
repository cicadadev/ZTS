package gcp.oms.model.pg.lgu;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import intune.gsf.common.utils.Config;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
// @Data
public class LguBase implements Serializable {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 3833766578791134554L;

	// private String configPath; // LG유플러스에서 제공한 환경파일(/conf/lgdacom.conf, /conf/mall.conf)이 위치한 디렉토리 지정
	// private String cstPlatform; // LG유플러스 결제서비스 선택(test:테스트, service:서비스)
	private String cstMid; // LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요.

	// private String lgdTxname; // 메서드 : 각각의 연동마다 고정값이 다름.
	private String lgdMid; // t + CSTMID
	private String lgdTid;// LG유플러스으로 부터 내려받은 거래번호(LGD_TID) : 주문할때 받은 거래번호

	public String getConfigPath() {
		return Config.getString("pg.config.path");
	}
	public String getCstPlatform() {
		return Config.getString("pg.platform");
	}
	public String getLgdMid() {
		if (StringUtils.isEmpty(lgdMid)) {
			return ("test".equals(this.getCstPlatform().trim()) ? "t" : "") + this.getCstMid();
		} else {
			return lgdMid;
		}
	}
	public void setLgdMid(String lgdMid) {
		this.lgdMid = lgdMid;
	}
	private String getCstMid() {
		return cstMid;
	}
	public void setCstMid(String cstMid) {
		this.cstMid = cstMid;
	}
	public String getLgdTid() {
		return lgdTid;
	}
	public void setLgdTid(String lgdTid) {
		this.lgdTid = lgdTid;
	}

	// public String getLgdTxname() {
	// return lgdTxname;
	// }
	// public void setLgdTxname(String lgdTxname) {
	// this.lgdTxname = lgdTxname;
	// }

}