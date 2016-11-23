package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsUploadconf;
import gcp.ccs.model.CcsStore;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsSite extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String siteId; //사이트ID		[primary key, primary key, primary key, not null]
	private String siteTypeCd; //사이트유형코드		[not null]
	private String name; //사이트명		[not null]
	private String erpBusinessId; //ERP업체ID		[null]
	private String mallId; //외부몰ID		[null]
	private String mallUserId; //외부몰사용자ID		[null]
	private String useYn; //사용여부		[not null]

	private List<OmsUploadconf> omsUploadconfs;
	private CcsStore ccsStore;

	public String getSiteTypeName(){
			return CodeUtil.getCodeName("SITE_TYPE_CD", getSiteTypeCd());
	}
}