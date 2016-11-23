package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsTemplate;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsTemplateDisplay extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String templateId; //템플릿ID		[primary key, primary key, primary key, not null]
	private String displayId; //전시ID		[primary key, primary key, primary key, not null]

	private DmsDisplay dmsDisplay;
	private DmsTemplate dmsTemplate;
}