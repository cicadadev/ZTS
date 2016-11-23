package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsUser;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsInquirytransfer extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal inquiryNo; //문의번호		[primary key, primary key, primary key, not null]
	private BigDecimal inquiryTransferNo; //이관일련번호		[primary key, primary key, primary key, not null]
	private String userId; //담당자ID		[null]
	private String transferNote; //이관사유		[null]

	private CcsInquiry ccsInquiry;
	private CcsUser ccsUser;
}