package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsSizechartlang;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsCategory;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsSizechart extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[primary key, primary key, primary key, not null]
	private String name; //사이즈조견표명		[not null]
	private String detail; //사이즈조견표내용		[null]
	private String useYn; //사용여부		[not null]

	private List<PmsSizechartlang> pmsSizechartlangs;
	private PmsBrand pmsBrand;
	private PmsCategory pmsCategory;
}