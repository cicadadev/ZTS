package intune.gsf.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String insDt = null;
	protected String updDt = null;
	protected String insId = null;
	protected String updId = null;
	protected String id = null;
	protected String selectKey = null;

	protected String langCd;

	private String resultMessage;
	private String resultCode;
	private boolean isSuccess = true;

	private String crudType; // C,R,U,D
	private int totalCount;
	private int totalOrderCount; // 총주문번호 
	private String isAllPermit; // ccsControl 설정을위한 공통 변수
	private String isMakeModel; // 복사등록시 ccsControl 설정을 위한 공통 변수
	private String system; // BO,PO,FO
	private String businessId; // po 일때 업체 ID

}
