package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsAccessmenu;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.CcsStore;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsRole extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String roleId; //역할ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String name; //역할명		[not null]
	private String note; //설명		[null]

	private List<CcsAccessmenu> ccsAccessmenus;
	private List<CcsUser> ccsUsers;
	private CcsStore ccsStore;
}