package gcp.oms.model;

import gcp.oms.model.base.BaseOmsUploadconf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsUploadconf extends BaseOmsUploadconf {
	//사이트명
	private String siteName;
	//사이트유형코드
	private String siteTypeCd;
}