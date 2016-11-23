package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsStore;
import gcp.mms.model.MmsMember;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsReviewpermit extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal permitNo; //허용번호		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[null]
	private BigDecimal memberNo; //회원번호		[null]

	private CcsStore ccsStore;
	private MmsMember mmsMember;
	private PmsProduct pmsProduct;
}