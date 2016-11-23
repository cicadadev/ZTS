package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMember;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductqna extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal productQnaNo; //QNA번호		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[not null]
	private String productQnaTypeCd; //상품QNA유형코드		[not null]
	private String productQnaStateCd; //상품QNA상태코드		[not null]
	private String saleproductId; //단품ID		[null]
	private String img; //이미지		[null]
	private String title; //제목		[not null]
	private String detail; //내용		[not null]
	private String emailYn; //이메일수신여부		[not null]
	private String email; //이메일		[null]
	private String smsYn; //SMS수신여부		[not null]
	private String phone; //전화번호		[null]
	private BigDecimal memberNo; //회원번호		[null]
	private String confirmDt; //확인일시		[null]
	private String confirmId; //확인자		[null]
	private String answer; //답변		[null]
	private String answerDt; //답변일시		[null]
	private String answerId; //답변자		[null]
	private String secretYn; //비밀글여부		[not null]
	private String displayYn; //공개여부		[not null]

	private MmsMember mmsMember;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;

	public String getProductQnaTypeName(){
			return CodeUtil.getCodeName("PRODUCT_QNA_TYPE_CD", getProductQnaTypeCd());
	}

	public String getProductQnaStateName(){
			return CodeUtil.getCodeName("PRODUCT_QNA_STATE_CD", getProductQnaStateCd());
	}
}