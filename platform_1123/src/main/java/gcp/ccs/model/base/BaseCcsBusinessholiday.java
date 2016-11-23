package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusiness;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsBusinessholiday extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String businessId; //업체ID		[primary key, primary key, primary key, not null]
	private String holiday; //휴일		[primary key, primary key, primary key, not null]

	private CcsBusiness ccsBusiness;
}