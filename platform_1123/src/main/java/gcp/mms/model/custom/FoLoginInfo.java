package gcp.mms.model.custom;

import java.util.List;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsQuickmenu;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.model.BaseLoginInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class FoLoginInfo extends BaseLoginInfo{
	
	private String membershipYn;
	private String childrenYn;
	private String b2eYn;
	private String premiumYn;
	private String memGradeCd;
	private String employeeYn;
	private String customerNo;
	private String phone2;
	private String childrenDealId;
	// 레코벨 검색 추천용
	private String babyBirthday;
	private String babyGenderCd;
	private String genderCd;
	private String birthday;
	private String babyAgeInMonths;
	private String memberStateCd;
	
	
	
//	private String memberYn;	//회원 or 비회원
	private List<MmsQuickmenu> memberMenus; 

	// age
	/**
	 * 아기의 개월수 구하기
	 * @Method Name : getAgeMonth
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description : 
	 *
	 * @return
	 */
	public int getAgeMonth() {
		//	0~3개월4~6개월7~12개월13~24개월2~4세4~6세7세이상기타
		String birthStr = "";
		if (babyBirthday != null && babyBirthday.length() >= 6) {
			birthStr = babyBirthday.substring(0, 7).replace("-", "");
			return DateUtil.getMonthPeriod(birthStr, DateUtil.getCurrentDate(DateUtil.FORMAT_11));
		} else {
			return -1;
		}
	}
	
	/**
	 * 엄마의 나이 구하기
	 * @Method Name : getMyAge
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description : 
	 *
	 * @return
	 */
	public int getMyAge(){
		if (birthday != null && birthday.length() >= 8) {
			return DateUtil.getMyAge(birthday.substring(0, 10).replace("-", ""));
		}else{
			return -1;
		}
	}
	
	public String getRecobellBabyGenderCd(){
		return FoSessionUtil.getRecobellBabyGenderCd();
	}
	
	public String getRecobellBabyMonthCd(){
		return FoSessionUtil.getRecobellBabyMonthCd();
	}
	
	public String getRecobellGenderCd(){
		return FoSessionUtil.getRecobellGenderCd();
	}

}
