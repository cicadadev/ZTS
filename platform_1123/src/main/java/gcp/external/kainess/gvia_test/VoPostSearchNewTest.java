package gcp.external.kainess.gvia_test;

import gcp.external.kainess.addressSearch4.LinkGVIA;
import gcp.external.kainess.vo.GviaPostSearchNewMultVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.kainess.vo.GviaPostSearchTotCountVo;
import intune.gsf.common.utils.Config;

public class VoPostSearchNewTest
{
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        GviaPostSearchNewMultVo arrlistSearch = new GviaPostSearchNewMultVo();
        String searchflag = "";
        String sido = "";
        String gungu = "";
        String searchStr = "";
        
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");

//		LinkGVIA gvia = new LinkGVIA("C:\\Users\\allen\\Desktop\\GVIA_MAEIL_ZTS_20160811\\WebContent\\WEB-INF"); // 프로퍼티 사용 시
		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시
        
//        sido = "서울특별시";
		sido = "";
        gungu = "";
        
        // searchflag = "L"; searchStr = "03175"; // 기초구역번호
        
        // searchflag = "M"; searchStr = "상동"; // 동/리명+지번 유사
        // searchflag = "N"; searchStr = "가산동 60-11"; // 동/리명+지번 정명칭
        
        // searchflag = "O"; searchStr = "디지털로9 99"; // 도로명+건물번호 유사
        // searchflag = "P"; searchStr = "디지털로9길 99"; // 도로명+건물번호 정명칭
        
        // searchflag = "Y"; searchStr = "스타"; // 건물명 유사
        // searchflag = "Z"; searchStr = "스타밸리"; // 건물명 정명칭
        
        searchflag = "M"; searchStr = "가산동"; // 통합검색
        
        // searchStr = gvia.MakeGviaSearchData(searchStr);
        searchStr = gvia.MakeGviaSearchData(sido, gungu, searchStr);
		arrlistSearch = gvia.PostSearchNewVo(searchflag, searchStr, 1, 100); // 검색 결과
        
        System.out.println("** Input searchflag  : " + searchflag);
        System.out.println("** Input searchStr   : " + searchStr);
        System.out.println("** Output ErrCode   : " + arrlistSearch.getErrorCode());
        
        System.out.println("  ==> * 결과코드  : " + arrlistSearch.getResultcd());
        System.out.println("  ==> * 전체 레코드 수  : " + arrlistSearch.getTotrecord());
        System.out.println("  ==> * 시작레코드 번호 : " + arrlistSearch.getStrecord());
        System.out.println("  ==> * 반복 레코드 수  : " + arrlistSearch.getRorecord());
        
        int nResult = arrlistSearch.getResult();
        
        if (arrlistSearch.getResultcd().equals("1"))
        {
            if (nResult == 0) // 우편번호 결과
            {
                GviaPostSearchNewVo entitySearchList = null;
                for (int j = 0; j < arrlistSearch.getCount(); j++)
                {
                    entitySearchList = arrlistSearch.getEntity(j);
                    
                    System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                    System.out.println("  ==> * No.  : " + (j + 1));
                    System.out.println("  ==> * 기초구역번호 : " + entitySearchList.getBsizonno());
                    System.out.println("  ==> * 우편번호 : " + entitySearchList.getPostno());
                    System.out.println("  ==> * 우편일련번호 : " + entitySearchList.getPostseq());
                    System.out.println("  ==> * 광역시/도 : " + entitySearchList.getSido());
                    System.out.println("  ==> * 시/군/구 : " + entitySearchList.getGungu());
                    System.out.println("  ==> * 읍/면/동 : " + entitySearchList.getDong());
                    System.out.println("  ==> * 리 : " + entitySearchList.getRi());
                    System.out.println("  ==> * 도로명 : " + entitySearchList.getRoadnm());
                    System.out.println("  ==> * 지하구분 : " + entitySearchList.getUndercd());
                    System.out.println("  ==> * 지하여부 : " + entitySearchList.getUnder());
                    System.out.println("  ==> * 건물본번 : " + entitySearchList.getBuildmn());
                    System.out.println("  ==> * 건물부번 : " + entitySearchList.getBuildsn());
                    System.out.println("  ==> * 건물명 : " + entitySearchList.getBuildnm());
                    System.out.println("  ==> * 도로명코드 : " + entitySearchList.getRoadcd());
                    System.out.println("  ==> * 읍면일련번호 : " + entitySearchList.getDongseq());
                    System.out.println("  ==> * 산구분 : " + entitySearchList.getSancd());
                    System.out.println("  ==> * 산여부 : " + entitySearchList.getSan());
                    System.out.println("  ==> * 주번지 : " + entitySearchList.getBunji());
                    System.out.println("  ==> * 부번지 : " + entitySearchList.getHo());
                    System.out.println("  ==> * 법정동코드 : " + entitySearchList.getBdongcd());
                    System.out.println("  ==> * 행정동코드 : " + entitySearchList.getHdongcd());
                    System.out.println("  ==> * 법정동명 : " + entitySearchList.getBdongnm());
                    System.out.println("  ==> * 행정동명 : " + entitySearchList.getHdongnm());
                    
                    System.out.println("  ==> * 광역시/도(영문) : " + entitySearchList.getEng_sido());
                    System.out.println("  ==> * 시/군/구(영문) : " + entitySearchList.getEng_gungu());
                    System.out.println("  ==> * 읍/면/동(영문) : " + entitySearchList.getEng_dong());
                    System.out.println("  ==> * 리(영문) : " + entitySearchList.getEng_ri());
                    System.out.println("  ==> * 도로명(영문) : " + entitySearchList.getEng_roadnm());
                }
            }
            else
            {
                GviaPostSearchTotCountVo entitySearchList2 = null;
                for (int j = 0; j < arrlistSearch.getCount(); j++)
                {
                    entitySearchList2 = arrlistSearch.getCountEntity(j);
                    
                    System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                    System.out.println("  ==> * No.  : " + (j + 1));
                    System.out.println("  ==> * 주소구분  : " + entitySearchList2.getAd_jr());
                    System.out.println("  ==> * 서울-인덱스/건수  : " + entitySearchList2.getStartrcd_se() + " / " + entitySearchList2.getCount_se());
                    System.out.println("  ==> * 부산-인덱스/건수  : " + entitySearchList2.getStartrcd_bs() + " / " + entitySearchList2.getCount_bs());
                    System.out.println("  ==> * 대구-인덱스/건수  : " + entitySearchList2.getStartrcd_dg() + " / " + entitySearchList2.getCount_dg());
                    System.out.println("  ==> * 인천-인덱스/건수  : " + entitySearchList2.getStartrcd_ic() + " / " + entitySearchList2.getCount_ic());
                    System.out.println("  ==> * 광주-인덱스/건수  : " + entitySearchList2.getStartrcd_gj() + " / " + entitySearchList2.getCount_gj());
                    System.out.println("  ==> * 대전-인덱스/건수  : " + entitySearchList2.getStartrcd_dj() + " / " + entitySearchList2.getCount_dj());
                    System.out.println("  ==> * 울산-인덱스/건수  : " + entitySearchList2.getStartrcd_us() + " / " + entitySearchList2.getCount_us());
                    System.out.println("  ==> * 세종-인덱스/건수  : " + entitySearchList2.getStartrcd_sj() + " / " + entitySearchList2.getCount_sj());
                    System.out.println("  ==> * 경기-인덱스/건수  : " + entitySearchList2.getStartrcd_gg() + " / " + entitySearchList2.getCount_gg());
                    System.out.println("  ==> * 강원-인덱스/건수  : " + entitySearchList2.getStartrcd_gw() + " / " + entitySearchList2.getCount_gw());
                    System.out.println("  ==> * 충북-인덱스/건수  : " + entitySearchList2.getStartrcd_cb() + " / " + entitySearchList2.getCount_cb());
                    System.out.println("  ==> * 충남-인덱스/건수  : " + entitySearchList2.getStartrcd_cn() + " / " + entitySearchList2.getCount_cn());
                    System.out.println("  ==> * 전북-인덱스/건수  : " + entitySearchList2.getStartrcd_jb() + " / " + entitySearchList2.getCount_jb());
                    System.out.println("  ==> * 전남-인덱스/건수  : " + entitySearchList2.getStartrcd_jn() + " / " + entitySearchList2.getCount_jn());
                    System.out.println("  ==> * 경북-인덱스/건수  : " + entitySearchList2.getStartrcd_gb() + " / " + entitySearchList2.getCount_gb());
                    System.out.println("  ==> * 경남-인덱스/건수  : " + entitySearchList2.getStartrcd_gn() + " / " + entitySearchList2.getCount_gn());
                    System.out.println("  ==> * 제주-인덱스/건수  : " + entitySearchList2.getStartrcd_jj() + " / " + entitySearchList2.getCount_jj());
                }
            }
        }
        else
        {
            System.out.println("** 우편번호 검색 결과 없음");
        }
    }
}
