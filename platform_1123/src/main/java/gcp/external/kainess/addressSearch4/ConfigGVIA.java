package gcp.external.kainess.addressSearch4;

public class ConfigGVIA
{
    public static int AdFrontNumber = 0; // 지번 기본주소 - 지번정보 표시여부(기본주소에 표시 시 상세주소에 미표시)
    public static int AdRemainRemove = 0; // 지번 상세주소 - 나머지정보 제거 여부
    public static int RoBackNumber = 0; // 도로명 기본주소 - 건물번호정보 표시여부(기본주소에 표시 시 상세주소에 미표시)
    public static int RoBuildUse = 0; // 도로명 상세주소 - 건물정보 표시 여부
    public static int RoRemainRemove = 0; // 도로명 상세주소 - 나머지정보 제거 여부
    public static int ZeroUse = 0; // 건물부번호,지번호정보 0 표시 여부
    public static int RefMode = 0; // 참조주소 선택
    // 참조주소 유형 
    // 0:기본 (법정동, 건물명)
    // 1:    (법정동/리, 건물명)
    // 2:    (법정동/리 산 번지-호)
    // 3:조건참조 조건1:법정리가 존재하고 건물명이 존재하는 경우 (법정리, 건물명)
    //           조건2:법정리가 존재하고 건물명이 없는 경우     (법정리 산 번지-호)
    //           조건3:법정리가 없으며 건물명이 존재하는경우    (법정동, 건물명)
    //           조건4:법정리가 없으며 건물명이 없는 경우       (법정동 산 번지-호)
    public static int AdCommaUse = 0; // 지번 상세주소 - 지번정보 상세에 존재 시 쉼표 표시여부(지번정보 뒤에 콤마 표시)
    public static int RoCommaUse = 0; // 도로명 상세주소 - 건물번호 상세에 존재 시 쉼표 표시여부(건물번호정보 뒤에 콤마 표시)
    public static int FrontLikeUse = 1; // 앞 유사검색 활성화 여부(유사검색은 기본 뒤 유사검색을 지원하며 해당 옵션으로 앞 유사검색 활성화 결정)
    public static String API_USER = "KAINESS";
    public static int ShortSido = 0; // 광역시/도 축약 표시 여부
    public static int UserBuildFirst = 1; // 사용자입력 건물명우선 여부
    public static int UserDongFirst = 0; // 사용자입력 읍면동 우선 여부(행정동으로 입력 했을 경우 행정동 출력)
}
