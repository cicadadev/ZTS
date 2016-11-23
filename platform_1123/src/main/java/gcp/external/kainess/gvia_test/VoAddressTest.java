package gcp.external.kainess.gvia_test;

import gcp.external.kainess.addressSearch4.LinkGVIA;
import gcp.external.kainess.vo.GviaAddressMultVo;
import gcp.external.kainess.vo.GviaAddressToSearchVo;
import gcp.external.kainess.vo.GviaAddressVo;
import intune.gsf.common.utils.Config;

public class VoAddressTest
{
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");

//		LinkGVIA gvia = new LinkGVIA("C:\\Users\\allen\\Desktop\\GVIA_MAEIL_ZTS_20160811\\WebContent\\WEB-INF"); // 프로퍼티 사용 시
		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시
        GviaAddressMultVo arrlistAddr = null;
        
        String post = "";
        String addr = "";
        
        // 정상주소 s
        // addr = "서울시 금천구 디지털로9길 99, 스타밸리 1008호 10층 개발부";
        // addr = "전남 담양군 담양읍 지침리 22";
        // 정상주소 e
        
        // 우편번호 + 주소 s
		// post = "153801"; addr = "스타밸리";	
        // post = "153777"; addr = "스타밸리";
        // post = "08510"; addr = "디지털로9길 스타밸리";
        // 우편번호 + 주소 e
        
        // 도로1:지번N s
        // addr = "전라남도 여수시 미평로 56";
        // addr = "경기도 의정부시 누원로 52";
        // addr = "경기도 남양주시 화도읍 경춘로 1605";
        // addr = "부산광역시 수영구 광안해변로 100 ";
        // addr = "경기도 의정부시 누원로 52";
        // 도로1:지번N e
        
        // 지번1:도로N s
        // addr = "경기도 고양시 덕양구 행신동 692";
        // addr = "경상남도 거창군 거창읍 대동리 845-7";
        // addr = "전라북도 무주군 설천면 심곡리 산43-15번지";
        // addr = "서울 동작구 사당2동 105";
        // 지번1:도로N e
        
        // 도로1:지번0 s
        // 없음
        // 도로1:지번0 e
        
        // 지번1:도로0 s
        // addr = "서울특별시 종로구 내자동 168-2";
        // addr = "전라남도 순천시 월등면 갈평리 288-6";
        // 지번1:도로0 e
        
        // 실패주소 s
        // addr = "서울시 노원구 중계동";
        // 실패주소 e
        
        // 입력다량(건물) s
        // addr = "서울특별시 강남구 압구정동 현대아파트 경비실";
        // 입력다량(건물) e
        
        // 입력다량(행정구역) s
        // addr = "서울시 신사동 60-11";
        // 입력다량(행정구역) e
        
//        addr = "서울시 금천구 디지털로9길 99, 스타밸리 1007호 10층 개발부";
		addr = "가산동";
        
        arrlistAddr = gvia.AddressVo(post, addr.trim(), 0);
        // System.out.println("** Input Address : " + addr);
        
        int nResult = arrlistAddr.getResult();
        
        if (nResult == 1) // 검색
        {
            GviaAddressToSearchVo entitySearchList = null;
            for (int j = 0; j < arrlistAddr.getCount(); j++)
            {
                entitySearchList = arrlistAddr.getSearchEntity(j);
                
                System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                System.out.println("  ==> * 'A' : 행정구역 멀티, 'B' : 건물 멀티 : " + entitySearchList.getFlag());
                System.out.println("  ==> * 입력동구분 : " + entitySearchList.getDonggubun());
                System.out.println("  ==> * 멀티 주소 매칭 수 : " + entitySearchList.getResultcnt());
                System.out.println("  ==> * 나머지주소 : " + entitySearchList.getEtcAddr());
                System.out.println("  ==> * 우편번호 : " + entitySearchList.getPostno());
                System.out.println("  ==> * 광역시도명칭 : " + entitySearchList.getSido());
                System.out.println("  ==> * 시군구명칭 : " + entitySearchList.getGungu());
                System.out.println("  ==> * 읍면동명칭 : " + entitySearchList.getDong());
                System.out.println("  ==> * 리명칭 : " + entitySearchList.getRi());
                System.out.println("  ==> * 산 또는 지하구분 : " + entitySearchList.geSan_under());
                System.out.println("  ==> * 주번지 또는 주번호 : " + entitySearchList.getMn());
                System.out.println("  ==> * 부번지 또는 부번호 : " + entitySearchList.getSn());
                System.out.println("  ==> * 건물명 : " + entitySearchList.getBuildnm());
                System.out.println("  ==> * 법정동명 : " + entitySearchList.getBdongnm());
                System.out.println("  ==> * 행정동명 : " + entitySearchList.getHdongnm());
                System.out.println("  ==> * 도로명 : " + entitySearchList.getRoadnm());
            }
        }
        else
        {
            GviaAddressVo entityAddrList = null;
            for (int j = 0; j < arrlistAddr.getCount(); j++)
            {
                entityAddrList = arrlistAddr.getAddrEntity(j);
                System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                System.out.println("  ==> * J:지번주소, R:도로명주소 : " + entityAddrList.getAd_jr());
                System.out.println("  ==> * 0:정상, 1:불능, 2:매칭실패 : " + entityAddrList.getAd_resultflag());
                System.out.println("  ==> * 변환코드 : " + entityAddrList.getAd_resultcd());
                System.out.println("  ==> * 지번주소 우편번호 : " + entityAddrList.getAd_postno());
                System.out.println("  ==> * 지번주소 우편번호일련번호 : " + entityAddrList.getAd_postseq());
                System.out.println("  ==> * 지번주소 국가기초구역번호 : " + entityAddrList.getAd_bsizonno());
                System.out.println("  ==> * 지번주소 기본주소(행정구역) : " + entityAddrList.getAd_add1());
                System.out.println("  ==> * 지번주소 상세주소 : " + entityAddrList.getAd_add2());
                System.out.println("  ==> *---------------------------------");
                System.out.println("  ==> * 도로명주소 우편번호 : " + entityAddrList.getAd_ro_postno());
                System.out.println("  ==> * 도로명주소 우편번호일련번호 : " + entityAddrList.getAd_ro_postseq());
                System.out.println("  ==> * 도로명주소 국가기초구역번호 : " + entityAddrList.getAd_ro_bsizonno());
                System.out.println("  ==> * 도로명주소 기본주소(건물번호까지) : " + entityAddrList.getAd_ro_add1());
                System.out.println("  ==> * 도로명주소 상세주소 : " + entityAddrList.getAd_ro_add2());
                System.out.println("  ==> * 도로명주소 참조주소,괄호주소 : " + entityAddrList.getAd_ro_ref());
                System.out.println("  ==> *---------------------------------");
                System.out.println("  ==> * 광역시/도명칭 : " + entityAddrList.getAd_sido());
                System.out.println("  ==> * 시/군/구명칭 : " + entityAddrList.getAd_gungu());
                System.out.println("  ==> * 읍/면/동명칭 : " + entityAddrList.getAd_dong());
                System.out.println("  ==> * 리명칭 : " + entityAddrList.getAd_ri());
                System.out.println("  ==> * 산구분 : " + entityAddrList.getAd_sancd());
                System.out.println("  ==> * 산여부 : " + entityAddrList.getAd_san());
                System.out.println("  ==> * 주번지 : " + entityAddrList.getAd_mn());
                System.out.println("  ==> * 부번지 : " + entityAddrList.getAd_sn());
                System.out.println("  ==> * 분석된 건물명 : " + entityAddrList.getAd_buildinfo());
                System.out.println("  ==> * 건물동 : " + entityAddrList.getAd_builddong());
                System.out.println("  ==> * 건물호 : " + entityAddrList.getAd_buildho());
                System.out.println("  ==> * 건물층 : " + entityAddrList.getAd_buildstair());
                System.out.println("  ==> * 건물추정정보 : " + entityAddrList.getAd_remain());
                System.out.println("  ==> * 입력동구분 : " + entityAddrList.getAd_dongcd());
                System.out.println("  ==> * 법정동코드 : " + entityAddrList.getAd_bdongcd());
                System.out.println("  ==> * 행정동코드 : " + entityAddrList.getAd_hdongcd());
                System.out.println("  ==> * 법정동명칭 : " + entityAddrList.getAd_bdongnm());
                System.out.println("  ==> * 행정동명칭 : " + entityAddrList.getAd_hdongnm());
                System.out.println("  ==> * 지번PNU : " + entityAddrList.getAd_pnucd());
                System.out.println("  ==> * X좌표 : " + entityAddrList.getAd_cx());
                System.out.println("  ==> * Y좌표 : " + entityAddrList.getAd_cy());
                System.out.println("  ==> * 좌표 부여 레벨 : " + entityAddrList.getAd_lev());
                System.out.println("  ==> * 도로명 : " + entityAddrList.getAd_ro_roadnm());
                System.out.println("  ==> * 도로코드 : " + entityAddrList.getAd_ro_roadcd());
                System.out.println("  ==> * 읍면동 일련번호 : " + entityAddrList.getAd_ro_dongseq());
                System.out.println("  ==> * 지하구분 : " + entityAddrList.getAd_ro_undercd());
                System.out.println("  ==> * 지하여부 : " + entityAddrList.getAd_ro_under());
                System.out.println("  ==> * 건물주번호 : " + entityAddrList.getAd_ro_buildmn());
                System.out.println("  ==> * 건물부번호 : " + entityAddrList.getAd_ro_buildsn());
                System.out.println("  ==> * 도로명 빌딩명칭 : " + entityAddrList.getAd_ro_buildnm());
                System.out.println("  ==> * 도로명 빌딩 상세 : " + entityAddrList.getAd_ro_builddtail());
                System.out.println("  ==> * 건물관리번호 : " + entityAddrList.getAd_ro_buildmgno());
                System.out.println("  ==> * 도로명주소 PNU : " + entityAddrList.getAd_ro_pnu());
                System.out.println("  ==> * 도로명주소 X좌표 : " + entityAddrList.getAd_ro_cx());
                System.out.println("  ==> * 도로명주소 Y좌표 : " + entityAddrList.getAd_ro_cy());
                System.out.println("  ==> * 안행부 배포 - 대표매칭여부 Y N : " + entityAddrList.getAd_reprematching());
                System.out.println("  ==> * 공동주택여부 Y N : " + entityAddrList.getAd_apathouse());
                System.out.println("  ==> * 주소파싱레벨 : " + entityAddrList.getAd_addrlev());
                System.out.println("  ==> * 우편번호 부여레벨 : " + entityAddrList.getAd_postlev());
                System.out.println("  ==> * 우편번호 사용 동타입 : " + entityAddrList.getAd_posttype());
                System.out.println("  ==> * 좌표부여 에러코드 : " + entityAddrList.getAd_errcd());
                System.out.println("  ==> * 정보변경코드 : " + entityAddrList.getAd_datachgcd());
                System.out.println("  ==> * 기초구역번호부여레벨 : " + entityAddrList.getAd_newpostlev());
                System.out.println("  ==> * 참조주소2(법정동/리, 건물명) : " + entityAddrList.getAd_ro_ref2());
                System.out.println("  ==> * 참조주소3(법정동/리 산 번지-호) : " + entityAddrList.getAd_ro_ref3());
                System.out.println("  ==> * 참조주소4(법정동/리 산 번지-호 건물명) : " + entityAddrList.getAd_ro_ref4());
                System.out.println("  ==> * 영문출력(시도) : " + entityAddrList.getAd_eng_sido());
                System.out.println("  ==> * 영문출력(시/군/구) : " + entityAddrList.getAd_eng_gungu());
                System.out.println("  ==> * 영문출력(읍/면/동) : " + entityAddrList.getAd_eng_dong());
                System.out.println("  ==> * 영문출력(리) : " + entityAddrList.getAd_eng_ri());
                System.out.println("  ==> * 영문출력(도로명) : " + entityAddrList.getAd_eng_roadnm());
            }
        }
    }
}
