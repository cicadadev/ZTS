package gcp.sa.model.base;

import java.math.BigDecimal;
import java.util.List;

import gcp.sa.model.SaFeedbackImg;
import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSaFeedback extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal fbNo; //피드백번호		[primary key, primary key, primary key, not null]
	private String shopId; //매장ID		[null]
	private String productId; //상품ID		[null]
	private String erpProductId; //ERP_상품ID		[null]
	private String title; //제목		[null]
	private String content; //내용		[null]
	private String useYn; //사용여부		[null]

	private List<SaFeedbackImg> saFeedbackImgs;
}