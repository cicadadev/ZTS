package gcp.external.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class ErpCustTable {
	//매장의 키값
	private String	accountNum;
	//구분키
	private String dataareaId;
	//매장유형코드
	private String	tkrShopType;
	//매장명
	private String	ztsCustname;
	//우편번호
	private String zipCode;
	//주소
	private String esgKrKraddress;
	//매장전화번호
	private String phone;
	//영업관리자연락처
	private String	tkrManagerPhone;
	//GPS 위도
	private String	ztsLatitude;
	//GPS 경도
	private String	ztsLongitude;
	//영업시작시간
	private String	openTime;
	//영업종료시간
	private String	closedTime;
	//매장픽업가능여부
	private int	pickup;
	//수정일시
	private Date	modifiedDateTime;
	//매장계열명
	private String	tkrSeriesName;
	//매장지역
	private String tkrRegion;
	//매장지역-시도
	private String state;
	//매장지역-군구
	private String county;
	//매장휴일안내
	private String holidayInfo;

	//매장휴일
	private List<ErpApxCustHoliday> erpApxCustHolidays;
	//매장취급브랜드
	private List<ErpApxSellingBrand> erpApxSellingBrands;
}
