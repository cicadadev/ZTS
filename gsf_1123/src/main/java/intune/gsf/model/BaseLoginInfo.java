package intune.gsf.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class BaseLoginInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String	loginId;		//login id
	private String	loginName;	//login name
	
	private String	storeId;	//store id
	private String	storeName;	//store name
	private String langCd;		//lang cd	
	
	private BigDecimal memberNo;	//회원번호

}
