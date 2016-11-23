package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsFunction;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsFunctionurl extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]
	private String functionId; //기능ID		[primary key, primary key, primary key, not null]
	private String url; //필수URL		[primary key, primary key, primary key, not null]

	private CcsFunction ccsFunction;
}