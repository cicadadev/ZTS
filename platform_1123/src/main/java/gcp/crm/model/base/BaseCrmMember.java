package gcp.crm.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import java.sql.Date;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCrmMember extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, not null]
	private String customerno; //고객번호		[null]
	private String status; //회원상태		[null]
	private String userid; //회원ID		[null]
	private String password; //비밀번호		[null]
	private String snsid; //SNS회원ID		[null]
	private String snsChannel; //SNS채널		[null]
	private String email; //이메일		[null]
	private String cellno; //휴대전화번호		[null]
	private String idChange; //ID변경여부		[null]
	private String infoChange; //INFO_CHANGE		[null]
	private String onlineChange; //ONLINE_CHANGE		[null]
	private String activityClass; //ACTIVITY_CLASS		[null]
	private BigDecimal activityRank; //ACTIVITY_RANK		[null]
	private String restingStatus; //RESTING_STATUS		[null]
	private String maritalStatus; //MARITAL_STATUS		[null]
	private String emailOpenYn; //EMAIL_OPEN_YN		[null]
	private String childOpenYn; //CHILD_OPEN_YN		[null]
	private String closeComments; //CLOSE_COMMENTS		[null]
	private String subsLoc; //회원가입Site		[null]
	private Date approveDate; //APPROVE_DATE		[null]
	private String recommendid; //추천인ID		[null]
	private Date createDate; //ID생성일		[null]
	private String updateChannel; //수정Site		[null]
	private String updateDate; //정보수정일		[null]
	private BigDecimal uriiCustNo; //URII_CUST_NO		[null]
	private String babySeq; //BABY_SEQ		[null]
	private String lastbabySeq; //LASTBABY_SEQ		[null]
	private String postReceiptYn; //POST_RECEIPT_YN		[null]
	private String buyWhere; //BUY_WHERE		[null]
	private String presentReceiptYn; //PRESENT_RECEIPT_YN		[null]
	private String partnerEmail; //PARTNER_EMAIL		[null]
	private String partnerEmailYn; //PARTNER_EMAIL_YN		[null]
	private String agreementYn; //AGREEMENT_YN		[null]
	private String cardIssueNo; //멤버십카드번호		[null]
	private Date cardIssueDate; //멤버십카드번호등록일		[null]

}