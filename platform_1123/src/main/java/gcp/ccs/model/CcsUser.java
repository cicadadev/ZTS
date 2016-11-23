package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsUser extends BaseCcsUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pwdModifyYn;
	private String newPwd;
	
	private String insName;	  	// 등록자
	private String updName;		// 수정자
	
	private String useYn;		// 사용여부
	
	private String categoryNames; // 담당부서
}