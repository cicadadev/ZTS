package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsInquirySearch extends BaseSearchCondition {
	
	private String periodType;
	private String keyword;
	private String memberNo;
	private String businessId;
	private String memGradeCds;
	private String memberLoginId;
	private String inquiryChannelCds;
	private String csUserId;
	private String csUserName;
	private String inquiryStateCds;
	private String answerId;
	private String answererName;
	private String inquiryTypeCd;
	private String customerName;
	private String memberYn;//회원,비회원
	private String brandId;
}
