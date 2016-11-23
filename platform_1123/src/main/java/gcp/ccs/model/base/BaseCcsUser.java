package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquirytransfer;
import gcp.pms.model.PmsCategory;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.CcsRole;
import gcp.ccs.model.CcsStore;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsUser extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String userId; //사용자ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String userTypeCd; //사용자유형코드		[not null]
	private String businessId; //업체ID		[null]
	private String depCd; //부서코드		[null]
	private String offshopId; //매장ID		[null]
	private String empId; //사번		[null]
	private String name; //이름		[not null]
	private String email; //이메일		[null]
	private String pwd; //비밀번호		[not null]
	private String phone1; //전화번호1		[null]
	private String phone2; //전화번호2		[null]
	private String fax; //팩스번호		[null]
	private String userStateCd; //사용자상태		[not null]
	private String roleId; //역할ID		[not null]
	private String mdYn; //MD여부		[not null]

	private List<CcsInquirytransfer> ccsInquirytransfers;
	private List<PmsCategory> pmsCategorys;
	private CcsBusiness ccsBusiness;
	private CcsOffshop ccsOffshop;
	private CcsRole ccsRole;
	private CcsStore ccsStore;

	public String getUserTypeName(){
			return CodeUtil.getCodeName("USER_TYPE_CD", getUserTypeCd());
	}

	public String getDepName(){
			return CodeUtil.getCodeName("DEP_CD", getDepCd());
	}

	public String getUserStateName(){
			return CodeUtil.getCodeName("USER_STATE_CD", getUserStateCd());
	}
}