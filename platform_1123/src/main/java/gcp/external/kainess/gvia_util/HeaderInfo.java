package gcp.external.kainess.gvia_util;

/**
 * @Class : BatchType
 * @Date : 2015. 3. 1.
 * @Author : 박민호
 * @Description :
 */
public class HeaderInfo
{
    public static final String GPOINT_BATCH_FULLADDR = "30x0";
    public static final String GPOINT_BATCH_ZIPCODE_FULLADDR = "30y0";
    public static final String GPOINT_BATCH_ZIPCODE_ADDR_DETAILADDR = "30z0";
    
    public static final int GPOINT_BATCH_MAX_PACKET_SIZE = 40960;
    public static final int GPOINT_BATCH_MIN_PACKET_COUNT = 4;
    public static final int GPOINT_BATCH_MAX_ADDR_COUNT = 100;
    
    public static final String GVIA_BATCH_FULLADDR = "30x2";
    public static final String GVIA_BATCH_ZIPCODE_FULLADDR = "30y2";
    public static final String GVIA_BATCH_ZIPCODE_ADDR_DETAILADDR = "30z2";
    
    public static final int GVIA_BATCH_MAX_PACKET_SIZE = 40960;
    public static final int GVIA_BATCH_MIN_PACKET_COUNT = 4;
    public static final int GVIA_BATCH_MAX_ADDR_COUNT = 100;
    
    /**
     * 
     * 약어 정리
     * CVT : 주소정제
     * SRH : 우편번호 검색
     * JZC : 우편번호(지번)
     * RZC : 우편번호(도로명)
     * FA : 전체주소
     * SD : 시/도
     * SGG : 시/군/구
     * UMD : 읍/면/동
     * RI : 리
     * BJ : 번지
     * HO : 호
     * RONM : 도로명
     * BDNM : 건물명
     * BDMN : 건물본번
     * BDSN : 건물부번
     * 
     * JPNU : PNU(지번)
     * RPNU : PNU(도로명)
     * BNM : 법정동명
     * HNM : 행정동명
     * BCD : 법정동코드
     * HCD : 행정동코드
     * 
     * RBZN : 기초구역번호(도로명)
     * JBZN : 기초구역번호(지번)
     * 
     * SGGCD : 시/군/구 코드
     * 
     * TOT : 통합검색
     * LIKE : 유사검색
     * 
     * MULT : 정제 멀티
     * IDX : 인덱스
     * 
     * ENG : 영어
     * PG : 페이징 사용
     * 
     **/
    
    public static final String GVIA_CVT_FA = "3002";
    public static final String GVIA_CVT_JZC_FA = "3012";
    
    public static final String GVIA_CVT_FA_MULT = "30A2";
    public static final String GVIA_CVT_JZC_FA_MULT = "30B2";
    
    public static final String GVIA_SRH_LIKE_UMD_RI_BDNM = "50A2";
    public static final String GVIA_SRH_LIKE_UMD_RI_BDNM_PG = "51A2";
    
    public static final String GVIA_SRH_JZC = "50C2";
    public static final String GVIA_SRH_JZC_PG = "51C2";
    
    public static final String GVIA_SRH_LIKE_SD_SGG_RONM = "50D2";
    public static final String GVIA_SRH_LIKE_SD_SGG_RONM_PG = "51D2";
    
    public static final String GVIA_SRH_RZC = "50F2";
    public static final String GVIA_SRH_RZC_PG = "51F2";
    
    public static final String GVIA_SRH_LIKE_BNM = "50H2";
    public static final String GVIA_SRH_LIKE_BNM_PG = "51H2";
    
    public static final String GVIA_SRH_LIKE_SD_SGG_BDNM = "50J2";
    public static final String GVIA_SRH_LIKE_SD_SGG_BDNM_PG = "51J2";
    
    public static final String GVIA_SRH_RBZN = "50L2";
    public static final String GVIA_SRH_RBZN_PG = "51L2";
    
    public static final String GVIA_SRH_LIKE_SGGCD_RONM_BDMN_BDSN = "50O2";
    public static final String GVIA_SRH_LIKE_SGGCD_RONM_BDMN_BDSN_PG = "51O2";
    
    public static final String GVIA_SRH_TOT = "50U2";
    public static final String GVIA_SRH_TOT_PG = "51U2";
    
    public static final String GVIA_SRH_TOT_IDX = "50V2";
    
    // 영문사용
    public static final String GVIA_CVT_FA_ENG = "C012";
    public static final String GVIA_CVT_JZC_FA_ENG = "C012";
    
    public static final String GVIA_CVT_FA_MULT_ENG = "C0A2";
    public static final String GVIA_CVT_JZC_FA_MULT_ENG = "C0B2";
    
    public static final String GVIA_SRH_LIKE_UMD_RI_BDNM_ENG = "D0A2";
    public static final String GVIA_SRH_LIKE_UMD_RI_BDNM_ENG_PG = "D1A2";
    
    public static final String GVIA_SRH_JZC_ENG = "D0C2";
    public static final String GVIA_SRH_JZC_ENG_PG = "D1C2";
    
    public static final String GVIA_SRH_LIKE_SD_SGG_RONM_ENG = "D0D2";
    public static final String GVIA_SRH_LIKE_SD_SGG_RONM_ENG_PG = "D1D2";
    
    public static final String GVIA_SRH_RZC_ENG = "D0F2";
    public static final String GVIA_SRH_RZC_ENG_PG = "D1F2";
    
    public static final String GVIA_SRH_LIKE_BNM_ENG = "D0H2";
    public static final String GVIA_SRH_LIKE_BNM_ENG_PG = "D1H2";
    
    public static final String GVIA_SRH_LIKE_SD_SGG_BDNM_ENG = "D0J2";
    public static final String GVIA_SRH_LIKE_SD_SGG_BDNM_ENG_PG = "D1J2";
    
    public static final String GVIA_SRH_RBZN_ENG = "D0L2";
    public static final String GVIA_SRH_RBZN_ENG_PG = "D1L2";
    
    public static final String GVIA_SRH_LIKE_SGGCD_RONM_BDMN_BDSN_ENG = "D0O2";
    public static final String GVIA_SRH_LIKE_SGGCD_RONM_BDMN_BDSN_ENG_PG = "D1O2";
    
    public static final String GVIA_SRH_TOT_ENG = "D0U2";
    public static final String GVIA_SRH_TOT_ENG_PG = "D1U2";
    
    public static final String GVIA_SRH_TOT_IDX_ENG = "D0V2";
}
