package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMember;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventgift;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventjoin extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private BigDecimal joinNo; //응모일련번호		[primary key, primary key, primary key, not null]
	private String url; //블로그주소		[null]
	private String detail; //댓글		[null]
	private String winYn; //당첨여부		[null]
	private BigDecimal giftNo; //경품번호		[null]
	private String note; //비고		[null]
	private BigDecimal memberNo; //회원번호		[not null]
	private String naverblogUrl; //네이버블로그URL		[null]
	private String instagramUrl; //인스타그램URL		[null]
	private String facebookUrl; //페이스북URL		[null]
	private String kakaostoryUrl; //카카오스토리URL		[null]

	private MmsMember mmsMember;
	private SpsEvent spsEvent;
	private SpsEventgift spsEventgift;
}