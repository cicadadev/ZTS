package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsOffshop;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsOffshopholiday extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String offshopId; //매장ID		[primary key, primary key, primary key, not null]
	private String holiday; //휴일		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]

	private CcsOffshop ccsOffshop;
}