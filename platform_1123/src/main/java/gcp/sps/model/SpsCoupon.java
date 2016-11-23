package gcp.sps.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsControl;
import gcp.sps.model.base.BaseSpsCoupon;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Pagckage Name : gcp.sps.model
 * @FileName : SpsCoupon.java
 * @author : paul
 * @date : 2016. 5. 3.
 * @description : SPS Base MODEL 확장
 */
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsCoupon extends BaseSpsCoupon {

	private String			orgTargetTypeCd;		// 쿠폰사용대상 기존코드
	private String			targetTypeCd;			// 쿠폰사용대상 코드

	private String			couponStateCds;			// 쿠폰상태코드
	private String			couponTypeCds;			// 쿠폰유형코드

	private String			chgDcValue;				// 쿠폰 정율/정액 값
	private String			publicCin;				// 인증번호 사용여부
	private String			privateCin;			// 쿠폰 인증번호
	private String			dealTypeCds;			// 딜 유형 코드
	private String			controlType;			// 제한유형코드

	private boolean		editAll;				// 전체수정여부
	private boolean		couponCopy;				// 쿠폰 복사등록여부
	private String			couponCopyReg;			// 복사등록 text

	private List<SpsDeal>	spsDeals;
	private CcsControl		ccsControl;
	private CcsApply		ccsApply;
	private String			memberNo;
	private String			businessInfo;			// 등록업체(번호)

	private String insName;		// 등록자
	private String updName;		// 수정자

	/* FO */
	private String dealId;	//적용쿠폰딜
	private	 BigDecimal couponIssueNo;	//적용쿠폰일련번호
	private BigDecimal dealListPrice;	//적용쿠폰딜 정상가
	private BigDecimal dealSalePrice;	//적용쿠폰딜 판매가
	private BigDecimal dealAddSalePrice;	//적용쿠폰딜 추가판매가
	private String dealTypeCd;	//적용쿠폰딜유형
	private String deliveryFeeFreeYn;	//적용쿠폰딜 배송비무료여부
	private BigDecimal targetAmt;//적용대상금액(총판매가)
	private BigDecimal pointSaveRate;	//적용대상쿠폰딜포인트적용비율
	private BigDecimal applyDcAmt;//적용할인금액	
	private BigDecimal dealStockQty;	//적용한 딜 수량.
}