package gcp.external.model;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
@Data
public class ClothesAfterService extends BaseSearchCondition{

	private String buyerid;			// 회원번호
//	private BigDecimal buyerid;			// 회원번호
	private String repairid;			// 접수번호
	private String requestdate;			// 접수일
	private String ztsCustname;			// 매장명
	private String itemname;			// 상품 이름
	private String inventcolorid;		// 상품 색상
	private String inventsizeid;		// 상품 사이즈
	private int repairstatus;			// 수선상태 : 1 매장접수 / 2 본사접수 / 3 수선완료
	private String repairenddate;		// 수선 완료일
	private int csordertype;			// 수선구번 : 1 수선 / 2 수선불가 / 3,4,5 교환
	private int ascosttype;				// 유상구분 : 0 유상 / 1 무상
	private BigDecimal price;			// 수선비

	// 리스트 카운트
	private int totalCount;

}