package gcp.external.kainess.gvia_test;

import gcp.external.kainess.addressSearch4.LinkGVIA;
import gcp.external.kainess.vo.GviaDistrictMultVo;
import gcp.external.kainess.vo.GviaDistrictVo;
import intune.gsf.common.utils.Config;

public class VoDistrictSearchTest
{
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        GviaDistrictMultVo arrlistSearch = null;
        String searchflag = "";
        String searchStr = "";
        
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");

//		LinkGVIA gvia = new LinkGVIA("C:\\Users\\allen\\Desktop\\GVIA_MAEIL_ZTS_20160811\\WebContent\\WEB-INF"); // 프로퍼티 사용 시
		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시
        
        /* 행정구역 검색 s */
//        searchStr = "0000000000"; searchflag = "A"; // 시도
         searchStr = "1100000000"; searchflag = "B"; //시군구
        // searchStr = "1168000000"; searchflag = "C"; //읍면동(행정동)
        // searchStr = "1168000000"; searchflag = "D"; //읍면동(법정동)
        // searchStr = "4481000000"; searchflag = "D"; //읍면(예산군)
        // searchStr = "4481025000"; searchflag = "E"; //리(예산읍)
        // searchStr = "4380000000"; searchflag = "0"; //도로명- 시군구 코드
        /* 행정구역 검색 e */
        
        arrlistSearch = gvia.DistrictSearchVo(searchflag, searchStr); // 검색 결과
//        arrlistSearch = gvia.DistrictSearchVo(searchflag, searchStr, 1, 3); // 검색 결과
        
        System.out.println("** Input searchflag  : " + searchflag);
        System.out.println("** Input searchStr   : " + searchStr);
        
        System.out.println("  ==> * 결과코드  : " + arrlistSearch.getResultcd());
        System.out.println("  ==> * 전체 레코드 수  : " + arrlistSearch.getTotrecord());
        System.out.println("  ==> * 시작레코드 번호 : " + arrlistSearch.getStrecord());
        System.out.println("  ==> * 반복 레코드 수  : " + arrlistSearch.getRorecord());
        
        if (arrlistSearch.getResultcd().equals("1"))
        {
            GviaDistrictVo entitySearchList = null;
            for (int j = 0; j < arrlistSearch.getCount(); j++)
            {
                entitySearchList = arrlistSearch.getEntity(j);
                
                System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                System.out.println("  ==> * No.  : " + (j + 1));
                System.out.println("  ==> * 동코드구분  : " + entitySearchList.getDonggb());
                System.out.println("  ==> * 동레벨  : " + entitySearchList.getHdonglvd());
                System.out.println("  ==> * 코드  : " + entitySearchList.getRegioncd());
                System.out.println("  ==> * 명칭  : " + entitySearchList.getRegionnm());
                System.out.println("  ==> * 명칭(영문)  : " + entitySearchList.getEngRegionnm());
            }
        }
        else
        {
            System.out.println("** 행정구역 검색 결과 없음");
        }
    }
}
