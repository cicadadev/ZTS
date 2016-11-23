package gcp.external.kainess.addressSearch4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import gcp.external.kainess.vo.GviaInputVo;

public class AddrApi


{

	private final Log	logger	= LogFactory.getLog(getClass());
    private String API_VER = "1.0.0.0";
    
    public AddrApi()
    {
    }
    
    public String getApiVER()
    {
        return API_VER;
    }
    
    public static void main(String[] args) throws Exception
    {
    }
    
    public Document process(GviaInputVo gviaInputVo)
    {
        // 값이 많아 짐으로 인해서 분리
        String prop = ""; // 프로퍼티 경로
        String gviaip = ""; // G-VIA 경로 IP
        String gviaport = ""; // G-VIA 경로 PORT
        String gviaip2 = ""; // G-VIA 경로 IP2
        String gviaport2 = ""; // G-VIA 경로 PORT2
        String outcnt = ""; //
        String addr = ""; // 주소 [정제/전환]
        String postno = ""; // 우편번호 [정제/전환]
        String searchpostno = ""; // 검색 우편번호
        String searchaddr = ""; // 검색 주소
        String searchbuild = ""; // 검색 건물
        String dongnm = ""; // 동명
        String roadnm = ""; // 도로명
        String sidocd = ""; // 시도
        String gungucd = ""; // 군구
        String umdcd = ""; // 읍면동
        String ricd = ""; // 리
        String roadgungucd = ""; // 도로군구코드
        // String roadpnu=""; // 도로명PNU
        // String roadaddr=""; // 도로주소
        String tot_post_str = ""; // 통합검색
        
        String bsizonno = ""; // 국가기초구역번호
        String roadadd2 = ""; // 도로명+건물번호
        String jibunadd2 = ""; // 동/리+지번
        String searchbuild2 = ""; // 건물명
        String tot_post_str2 = ""; // 통합검색
        String districtzonno = ""; // 행정구역 기초구역번호
        
        // String errchk=""; // 에러체크
        // String searchlike=""; // 검색Like여부
        String inputjusogubun = ""; // 주소구분
        
        String recordCall = ""; // 광역시도별 레코드 건수 조회 여부
        String pgyn = ""; // 페이징 여부
        String pgcd = ""; // 페이징 시작번호|출력수량
        
        String stpg = ""; // 페이징 시작 번호
        String outpg = ""; // 페이징 출력 수량
        
        String bsi_mat_jibun = ""; // 기초구역번호 매칭 지번
        String bsi_mat_road = ""; // 기초구역번호 매칭 도로명
        
        String eng_addr_yn = ""; // 영문 주소 사용여부
        String nBsizonnoYn = ""; // 행정구역별 기초구역번호 출력여부
        
        // /////////////////////////////////////////////////////////////////////////////////////////////////
        
        if (!(gviaInputVo.getProp().equals(""))) prop = gviaInputVo.getProp(); // 프로퍼티 경로
        if (!(gviaInputVo.getGviaip().equals(""))) gviaip = gviaInputVo.getGviaip(); // G-VIA 경로 IP
        if (!(gviaInputVo.getGviaport().equals(""))) gviaport = gviaInputVo.getGviaport(); // G-VIA 경로 Port
        if (!(gviaInputVo.getGviaip2().equals(""))) gviaip2 = gviaInputVo.getGviaip2(); // G-VIA 경로 IP2
        if (!(gviaInputVo.getGviaport2().equals(""))) gviaport2 = gviaInputVo.getGviaport2(); // G-VIA 경로 Port2
        if (!(gviaInputVo.getOutcnt().equals(""))) outcnt = gviaInputVo.getOutcnt();
        if (!(gviaInputVo.getAddr().equals(""))) addr = gviaInputVo.getAddr(); // 주소 [정제/전환]
        if (!(gviaInputVo.getPostno().equals(""))) postno = gviaInputVo.getPostno(); // 우편번호 [정제/전환]
        if (!(gviaInputVo.getSearchpostno().equals(""))) searchpostno = gviaInputVo.getSearchpostno(); // 검색 우편번호
        if (!(gviaInputVo.getSearchaddr().equals(""))) searchaddr = gviaInputVo.getSearchaddr(); // 검색 주소
        if (!(gviaInputVo.getSearchbuild().equals(""))) searchbuild = gviaInputVo.getSearchbuild(); // 검색 건물
        if (!(gviaInputVo.getDongnm().equals(""))) dongnm = gviaInputVo.getDongnm(); // 동명
        if (!(gviaInputVo.getRoadnm().equals(""))) roadnm = gviaInputVo.getRoadnm(); // 도로명
        if (!(gviaInputVo.getSidocd().equals(""))) sidocd = gviaInputVo.getSidocd(); // 시도
        if (!(gviaInputVo.getGungucd().equals(""))) gungucd = gviaInputVo.getGungucd(); // 군구
        if (!(gviaInputVo.getUmdcd().equals(""))) umdcd = gviaInputVo.getUmdcd(); // 읍면동
        if (!(gviaInputVo.getRicd().equals(""))) ricd = gviaInputVo.getRicd(); // 리
        if (!(gviaInputVo.getRoadgungucd().equals(""))) roadgungucd = gviaInputVo.getRoadgungucd(); // 도로군구코드
        // if (!(gviaInputVo.getRoadpnu().equals(""))) roadpnu = gviaInputVo.getRoadpnu(); // 도로명PNU
        // if (!(gviaInputVo.getRoadaddr().equals(""))) roadaddr = gviaInputVo.getRoadaddr(); // 도로명주소
        if (!(gviaInputVo.getTot_post_str().equals(""))) tot_post_str = gviaInputVo.getTot_post_str(); // 통합검색
        if (!(gviaInputVo.getBsizonno().equals(""))) bsizonno = gviaInputVo.getBsizonno(); // 국가기초구역번호
        if (!(gviaInputVo.getRoadadd2().equals(""))) roadadd2 = gviaInputVo.getRoadadd2(); // 도로명+건물번호
        if (!(gviaInputVo.getJibunadd2().equals(""))) jibunadd2 = gviaInputVo.getJibunadd2(); // 동/리명+지번
        if (!(gviaInputVo.getSearchbuild2().equals(""))) searchbuild2 = gviaInputVo.getSearchbuild2(); // 건물명
        if (!(gviaInputVo.getTot_post_str2().equals(""))) tot_post_str2 = gviaInputVo.getTot_post_str2(); // 통합검색
        if (!(gviaInputVo.getDistrictzonno().equals(""))) districtzonno = gviaInputVo.getDistrictzonno(); // 행정구역 기초구역번호
        // if (!(gviaInputVo.getErrchk().equals(""))) errchk = gviaInputVo.getErrchk(); // 에러체크
        // if (!(gviaInputVo.getSearchlike().equals(""))) searchlike = gviaInputVo.getSearchlike(); // 검색Like여부
        if (!(gviaInputVo.getInputjusogubun().equals(""))) inputjusogubun = gviaInputVo.getInputjusogubun(); // 주소구분
        if (!(gviaInputVo.getPgyn().equals(""))) pgyn = gviaInputVo.getPgyn(); // 페이징 여부
        if (!(gviaInputVo.getPgcd().equals(""))) pgcd = gviaInputVo.getPgcd(); // 페이징 시작번호|끝번호
        if (!(gviaInputVo.getBsi_mat_jibun().equals(""))) bsi_mat_jibun = gviaInputVo.getBsi_mat_jibun(); // 기초구역번호 매칭 지번
        if (!(gviaInputVo.getBsi_mat_road().equals(""))) bsi_mat_road = gviaInputVo.getBsi_mat_road(); // 기초구역번호 매칭 도로명
        if (!(gviaInputVo.getEng_addr_yn().equals(""))) eng_addr_yn = gviaInputVo.getEng_addr_yn(); // 영문 주소 사용여부
        if (!(gviaInputVo.getRecordCall().equals(""))) recordCall = gviaInputVo.getRecordCall(); // 광역 시도별 레코드 건수 조회 여부
        if (!(gviaInputVo.getNBsizonnoYn().equals(""))) nBsizonnoYn = gviaInputVo.getNBsizonnoYn(); // 행정구역별 기초구역번호 출력 여부
            
        // /////////////////////////////////////////////////////////////////////////////////////////////////
        
        Document doc = new Document();
        
        boolean paramchk = true;
        
        String[][] arrResult;
        int noutcnt = 0;
        String useflag = "";
        String msgStr = "";
        
        String txtPostSearch = "";
        String txtAddrRecord1 = "";
        String txtAddrRecord2 = "";
        
        String ioptMain = ""; // 메인코드
        String ioptPage = ""; // paging코드
        String ioptSub = ""; // 서브코드
        String ioptVer = "2"; // 버전코드(GVIA : 2)
        
        if (pgyn.equals("Y"))
        { // paging 기능 사용
            ioptPage = "1";
            
            // 임시 start
            if (pgcd.indexOf("|") != -1)
            {
                String[] tmpPgCd = pgcd.split("[\u007C]"); // [\u007C]
                stpg = tmpPgCd[0];
                outpg = tmpPgCd[1];
            }
            else
            {
                stpg = "";
                outpg = "";
            }
            // 임시 end
        }
        else
        { // paging 기능 미사용
            ioptPage = "0";
        }
        
        // if ((gviaip.trim().equals("") || gviaport.trim().equals("")) && prop.trim().equals(""))
        // {
        // useflag = "0000";
        // paramchk = false;
        // msgStr = "GVIA 서버 정보 없음";
        // }
        
        if (paramchk)
        {
            if (outcnt.trim().equals("") || outcnt.trim().equals("000"))
            {
                noutcnt = 9999;
            }
            else
            {
                noutcnt = Integer.parseInt(outcnt);
            }
            if (noutcnt == 0)
            {
                noutcnt = 9999;
            }
            if (postno.trim().equals("") && !addr.trim().equals(""))
            {
                if (eng_addr_yn.equals("Y")) useflag = "C002";
                else useflag = "3002"; // 3002
                
                txtPostSearch = "";
                txtAddrRecord1 = addr;
                txtAddrRecord2 = "";
            }
            else if (!postno.trim().equals("") && (postno.trim().length() == 5 || postno.trim().length() == 6))
            {
                if (eng_addr_yn.equals("Y")) useflag = "C012";
                else useflag = "3012"; // 3010
                
                txtPostSearch = postno;
                txtAddrRecord1 = addr;
                txtAddrRecord2 = "";
            }
            else if (!dongnm.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && dongnm.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2)
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "A" + ioptVer;
                else useflag = "5" + ioptPage + "A" + ioptVer;
                
                txtPostSearch = dongnm.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!searchpostno.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && searchpostno.replaceAll("[$]", "").replaceAll("%24", "").trim().length() == 6 && !inputjusogubun.trim().equals("2"))
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "C" + ioptVer;
                else useflag = "5" + ioptPage + "C" + ioptVer;
                
                txtPostSearch = searchpostno.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!searchpostno.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && searchpostno.replaceAll("[$]", "").replaceAll("%24", "").trim().length() == 6 && inputjusogubun.trim().equals("2")) // 도로명 우편번호
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "F" + ioptVer;
                else useflag = "5" + ioptPage + "F" + ioptVer;
                
                txtPostSearch = searchpostno.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!roadnm.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && roadnm.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2)
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "D" + ioptVer;
                else useflag = "5" + ioptPage + "D" + ioptVer;
                
                txtPostSearch = roadnm.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!searchaddr.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && searchaddr.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2)
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "H" + ioptVer;
                else useflag = "5" + ioptPage + "H" + ioptVer;
                
                txtPostSearch = searchaddr.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!searchbuild.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && searchbuild.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2)
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "J" + ioptVer;
                else useflag = "5" + ioptPage + "J" + ioptVer;
                
                txtPostSearch = searchbuild.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!bsizonno.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && bsizonno.replaceAll("[$]", "").replaceAll("%24", "").trim().length() == 5) // 기초구역번호
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "L" + ioptVer;
                else useflag = "5" + ioptPage + "L" + ioptVer;
                
                txtPostSearch = bsizonno.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!bsizonno.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && bsizonno.replaceAll("[$]", "").replaceAll("%24", "").trim().length() == 10) // 시도(2)+시군구(3)+기초구역번호
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "L" + ioptVer;
                else useflag = "5" + ioptPage + "L" + ioptVer;
                
                txtPostSearch = bsizonno.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!roadadd2.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && roadadd2.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 도로명+건물번호
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "O" + ioptVer;
                else useflag = "5" + ioptPage + "O" + ioptVer;
                
                txtPostSearch = roadadd2.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!jibunadd2.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && jibunadd2.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 동/리명+지번
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "M" + ioptVer;
                else useflag = "5" + ioptPage + "M" + ioptVer;
                
                txtPostSearch = jibunadd2.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!districtzonno.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && districtzonno.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 행정구역 기초구역번호
            {
                ioptVer = "3";
                if (nBsizonnoYn.equals("Y")) ioptSub = "Q";
                else ioptSub = "q";
                
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + ioptSub + ioptVer;
                else useflag = "5" + ioptPage + ioptSub + ioptVer;
                
                txtPostSearch = districtzonno.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!tot_post_str2.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && tot_post_str2.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 통합검색-개별
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "W" + ioptVer;
                else useflag = "5" + ioptPage + "W" + ioptVer;
                
                txtPostSearch = tot_post_str2.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!searchbuild2.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && searchbuild2.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2)
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "Y" + ioptVer;
                else useflag = "5" + ioptPage + "Y" + ioptVer;
                
                txtPostSearch = searchbuild2.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            // else if (!bsi_mat_jibun.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && bsi_mat_jibun.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 지번-기초구역번호
            // {
            // ioptVer = "3";
            // // bsi_mat_jibun
            // if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "Q" + ioptVer;
            // else useflag = "5" + ioptPage + "Q" + ioptVer;
            //
            // txtPostSearch = bsi_mat_jibun.trim();
            // txtAddrRecord1 = stpg;
            // txtAddrRecord2 = outpg;
            // }
            else if (!bsi_mat_road.replaceAll("[$]", "").replaceAll("%24", "").trim().equals("") && bsi_mat_road.replaceAll("[$]", "").replaceAll("%24", "").trim().length() >= 2) // 도로명-기초구역번호
            {
                ioptVer = "3";
                // bsi_mat_road
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "S" + ioptVer;
                else useflag = "5" + ioptPage + "S" + ioptVer;
                
                txtPostSearch = bsi_mat_road.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!tot_post_str.trim().equals("") && !recordCall.trim().equals("Y"))
            {
                ioptVer = "3";
                if (eng_addr_yn.equals("Y")) useflag = "D" + ioptPage + "U" + ioptVer;
                else useflag = "5" + ioptPage + "U" + ioptVer;
                
                txtPostSearch = tot_post_str.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!tot_post_str.trim().equals("") && recordCall.trim().equals("Y"))
            {
                ioptVer = "3";
                useflag = "5" + ioptPage + "V" + ioptVer;
                
                txtPostSearch = tot_post_str.trim();
                txtAddrRecord1 = "";
                txtAddrRecord2 = "";
            }
            else if (!roadgungucd.trim().equals("") && roadgungucd.trim().length() == 10)
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "02";
                else useflag = "6" + ioptPage + "02";
                
                txtPostSearch = roadgungucd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!ricd.trim().equals("") && ricd.trim().length() == 10 && !ricd.trim().equals("0000000000"))
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "E2";
                else useflag = "6" + ioptPage + "E2";
                
                txtPostSearch = ricd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!umdcd.trim().equals("") && umdcd.trim().length() == 10 && !umdcd.trim().equals("0000000000") && !inputjusogubun.trim().equals("2"))
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "C2";
                else useflag = "6" + ioptPage + "C2";
                
                txtPostSearch = umdcd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!umdcd.trim().equals("") && umdcd.trim().length() == 10 && !umdcd.trim().equals("0000000000") && inputjusogubun.trim().equals("2")) // 도로명 주소에서 법정동으로 조회
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "D2";
                else useflag = "6" + ioptPage + "D2";
                
                txtPostSearch = umdcd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!gungucd.trim().equals("") && gungucd.trim().length() == 10 && !gungucd.trim().equals("0000000000"))
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "B2";
                else useflag = "6" + ioptPage + "B2";
                
                txtPostSearch = gungucd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else if (!sidocd.trim().equals("") && sidocd.trim().length() == 10)
            {
                if (eng_addr_yn.equals("Y")) useflag = "E" + ioptPage + "A2";
                else useflag = "6" + ioptPage + "A2";
                
                txtPostSearch = sidocd.trim();
                txtAddrRecord1 = stpg;
                txtAddrRecord2 = outpg;
            }
            else
            {
                useflag = "0000";
                paramchk = false;
            }
        }
        
        
        if (paramchk)
        {
            LinkGVIA gvia = null;
            if (!prop.trim().equals(""))
            { // 프로퍼티 사용 시
                gvia = new LinkGVIA(prop);
            }
            else if (!gviaip.equals("") && !gviaport.equals(""))
            { // 프로퍼티 미사용 시
                if (!gviaip2.equals("") && !gviaport2.equals(""))
                {
                    gvia = new LinkGVIA(gviaip, Integer.parseInt(gviaport), gviaip2, Integer.parseInt(gviaport2));
                }
                else
                {
                    gvia = new LinkGVIA(gviaip, Integer.parseInt(gviaport));
                }
            }
            else if (!gviaip2.equals("") && !gviaport2.equals(""))
            { // 프로퍼티 미사용 시(서버1정보가 없는경우)
                gvia = new LinkGVIA(gviaip2, Integer.parseInt(gviaport2));
            }
            else
            { // 내부 정보 사용 시
                gvia = new LinkGVIA();
            }
            
            try
            {
                if (gvia.SocketConnect())
                {
                    gvia.outLog("useflag: >>>>" + useflag);
                    gvia.outLog("txtPostSearch: >>>>" + txtPostSearch);
                    gvia.outLog("txtAddrRecord1: >>>>" + txtAddrRecord1);
                    gvia.outLog("txtAddrRecord2: >>>>" + txtAddrRecord2);
                    
                    if (useflag.substring(0, 1).equals("3") || useflag.substring(0, 1).equals("C")) // 주소 정제 표준 출력
                    {
                        arrResult = gvia.Address2(useflag, txtPostSearch, txtAddrRecord1, txtAddrRecord2);
                        
                        if (arrResult[0][2].equals("A") || arrResult[0][2].equals("B"))
                        {
                            doc = getAddressMultiDoc2(arrResult, noutcnt); // 일반 + 정제 멀티
                        }
                        else
                        {
                            if (useflag.substring(0, 1).equals("3"))
                            { // 주소결과를 Document 형식으로
                                doc = getAddressDoc2(arrResult, noutcnt);
                            }
                            else
                            { // 주소 정제 표준+영문 출력
                                doc = getAddressDoc2EngAddr(arrResult, noutcnt); // dk-work
                            }
                        }
                    }
                    else
                    // 검색 (행정구역,우편번호)
                    {
                        arrResult = gvia.Search2(useflag, txtPostSearch, txtAddrRecord1, txtAddrRecord2);
                        
                        ioptMain = useflag.substring(0, 1);
                        ioptSub = useflag.substring(2, 3);
                        
                        // 주소결과를 Document 형식으로 start
                        if (ioptMain.equals("6"))
                        {
                            doc = getDistrictDoc2(arrResult, noutcnt);
                        }
                        else if (ioptMain.equals("E"))
                        { // 현재 사용 안됨
                            doc = getDistrictDoc2(arrResult, noutcnt);
                        }
                        else if (ioptMain.equals("5"))
                        { // 표준
                            if (ioptSub.equals("D") || ioptSub.equals("F") || ioptSub.equals("H") || ioptSub.equals("J"))
                            {
                                doc = getRoadPostCdDoc2(arrResult, noutcnt);
                            }
                            // else if (ioptSub.equals("L") || ioptSub.equals("M") || ioptSub.equals("N") || ioptSub.equals("O") || ioptSub.equals("P") || ioptSub.equals("W") || ioptSub.equals("Y") || ioptSub.equals("Z"))
                            else if (ioptSub.matches("[LMNOPQRqrWwYyZ]"))
                            { // 국가기초구역번호
                                doc = getRoadPostCdDoc3(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("A") || ioptSub.equals("C"))
                            { //
                                doc = getLtnoPostCdDoc2(arrResult, noutcnt);
                            }
                            // else if (ioptSub.equals("Q") || ioptSub.equals("R"))
                            // { //
                            // doc = getJibunBsizonnoRangeDoc(arrResult, noutcnt);
                            // }
                            else if (ioptSub.equals("S") || ioptSub.equals("T"))
                            {
                                doc = getRoadBsizonnoRangeDoc(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("V") || ioptSub.equals("X"))
                            {
                                doc = getTotalPostCdDocRecordNum(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("U"))
                            {
                                doc = getTotalPostCdDoc(arrResult, noutcnt);
                            }
                            else
                            {
                                doc = getErrDoc("1", "SubCode Error");
                            }
                        }
                        else if (ioptMain.equals("D"))
                        { // 영문추가
                            if (ioptSub.equals("D") || ioptSub.equals("F") || ioptSub.equals("H") || ioptSub.equals("J"))
                            {
                                doc = getRoadPostCdDoc2EngAddr(arrResult, noutcnt);
                            }
                            // else if (ioptSub.equals("L") || ioptSub.equals("M") || ioptSub.equals("N") || ioptSub.equals("O") || ioptSub.equals("P") || ioptSub.equals("W") || ioptSub.equals("Y") || ioptSub.equals("Z"))
                            else if (ioptSub.matches("[LMNOPQRqrWwYyZ]"))
                            { // 국가기초구역번호
                                doc = getRoadPostCdDoc3EngAddr(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("A") || ioptSub.equals("C"))
                            { //
                                doc = getLtnoPostCdDoc2EngAddr(arrResult, noutcnt);
                            }
                            // else if (ioptSub.equals("Q") || ioptSub.equals("R"))
                            // { //
                            // doc = getJibunBsizonnoRangeDocEngAddr(arrResult, noutcnt);
                            // }
                            else if (ioptSub.equals("S") || ioptSub.equals("T"))
                            {
                                doc = getRoadBsizonnoRangeDocEngAddr(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("V") || ioptSub.equals("X"))
                            { // 현재 사용 안함
                                doc = getTotalPostCdDocRecordNum(arrResult, noutcnt);
                            }
                            else if (ioptSub.equals("U"))
                            {
                                doc = getTotalPostCdDocEngAddr(arrResult, noutcnt);
                            }
                            else
                            {
                                doc = getErrDoc("1", "SubCode Error(ENG)");
                            }
                        }
                        else
                        {
                            doc = getErrDoc("1", "MainCode Error");
                        }
                    }
                }
            }
            catch (Exception e)
            {
				logger.error("AddrApi.java > process > Exception:" + e);
                // 오류 결과를 Document 형식으로
                doc = getErrDoc("1", e.toString());
            }
            finally
            {
                gvia.SocketClose();
            }
        } // if(paramchk)
        
        if (paramchk == false)
        {
            if (msgStr.equals(""))
            {
                doc = getErrDoc("1", "Parameter Check.");
            }
            else
            {
                doc = getErrDoc("1", msgStr);
            }
        } // if(paramchk == false)
        
        return doc;
    } // public void process
    
    /******************** gvia 결과 ********************/
    /*
     * function : getAddressDoc2()
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getAddressDoc2(String[][] arrResult, int noutcnt)
    {
        /*
         * for(int p = 0; p < arrResult.length ; p++)
         * {
         * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
         * System.out.println("  ==> * 지번/새주소구분(입력값기준, J:지번주소, N:도로명주소)  : " + arrResult[p][0]);
         * System.out.println("  ==> * FLAG1 (0:정상, 1:불능, 2:매칭실패)  : " + arrResult[p][1]);
         * System.out.println("  ==> * FLAG2 (변환코드) : " + arrResult[p][2]);
         * System.out.println("  ==> * 정제 결과 주소(4:우편번호)  : " + arrResult[p][3]);
         * System.out.println("  ==> * 정제 결과 주소(5:우편번호일련번호)  : " + arrResult[p][4]);
         * System.out.println("  ==> * 정제 결과 주소(6:기본주소): " + arrResult[p][5]);
         * System.out.println("  ==> * 정제 결과 주소(7:상세주소): " + arrResult[p][6]);
         * System.out.println("  ==> * 변환 결과 주소(8:우편번호)  : " + arrResult[p][7]);
         * System.out.println("  ==> * 변환 결과 주소(9:우편번호일련번호)  : " + arrResult[p][8]);
         * System.out.println("  ==> * 변환 결과 주소(10:기본주소): " + arrResult[p][9]);
         * System.out.println("  ==> * 변환 결과 주소(11:상세주소): " + arrResult[p][10]);
         * System.out.println("  ==> * 도로명 주소(12:건물관리번호): " + arrResult[p][11]);
         * System.out.println("  ==> * 기본 주소(13:시도): " + arrResult[p][12]);
         * System.out.println("  ==> * 기본 주소(14:시군구): " + arrResult[p][13]);
         * System.out.println("  ==> * 기본 주소(15:읍면동): " + arrResult[p][14]);
         * System.out.println("  ==> * 기본 주소(16:리): " + arrResult[p][15]);
         * System.out.println("  ==> * 지번 주소(17:산여부): " + arrResult[p][16]);
         * System.out.println("  ==> * 지번 주소(18:주번지): " + arrResult[p][17]);
         * System.out.println("  ==> * 지번 주소(19:부번지): " + arrResult[p][18]);
         * System.out.println("  ==> * 좌표(20:X좌표): " + arrResult[p][19]);
         * System.out.println("  ==> * 좌표(21:Y좌표): " + arrResult[p][20]);
         * System.out.println("  ==> * 좌표(22:좌표부여레벨): " + arrResult[p][21]);
         * System.out.println("  ==> * 기본 주소(23:법정동코드): " + arrResult[p][22]);
         * System.out.println("  ==> * 기본 주소(24:행정동코드): " + arrResult[p][23]);
         * System.out.println("  ==> * 도로명 주소(25:괄호(참고) 주소): " + arrResult[p][24]);
         * System.out.println("  ==> * 도로명 주소(26:도로명 코드): " + arrResult[p][25]);
         * System.out.println("  ==> * 도로명 주소(27:읍/면/동 일련번호): " + arrResult[p][26]);
         * System.out.println("  ==> * 도로명 주소(28:지하 여부): " + arrResult[p][27]);
         * System.out.println("  ==> * 도로명 주소(29:건물 본번): " + arrResult[p][28]);
         * System.out.println("  ==> * 도로명 주소(30:건물 부번): " + arrResult[p][29]);
         * System.out.println("  ==> * 도로명 주소(31:법정동명): " + arrResult[p][30]);
         * System.out.println("  ==> * 도로명 주소(32:행정동명): " + arrResult[p][31]);
         * System.out.println("  ==> * 지번 주소 PNU코드: " + arrResult[p][32]);
         * System.out.println("  ==> * 도로명 주소(34:도로명): " + arrResult[p][33]);
         * System.out.println("  ==> * 지번 주소(35:국가지점번호): " + arrResult[p][34]);
         * System.out.println("  ==> * 지번 주소(36:국가기초구역번호): " + arrResult[p][35]);
         * System.out.println("  ==> * 지번 주소(37:국가기초구역일련번호): " + arrResult[p][36]);
         * System.out.println("  ==> * 도로명 주소(38:국가지점번호): " + arrResult[p][37]);
         * System.out.println("  ==> * 도로명 주소(39:국가기초구역번호): " + arrResult[p][38]);
         * System.out.println("  ==> * 도로명 주소(40:국가기초구역일련번호): " + arrResult[p][39]);
         * System.out.println("  ==> * 기본 주소(41:입력동구분): " + arrResult[p][40]);
         * System.out.println("  ==> * 기본 주소(42:빌딩 매칭 정보): " + arrResult[p][41]);
         * System.out.println("  ==> * 기본 주소(43:빌딩 동 정보): " + arrResult[p][42]);
         * System.out.println("  ==> * 기본 주소(44:빌딩 호 정보): " + arrResult[p][43]);
         * System.out.println("  ==> * 기본 주소(45:빌딩 층 정보): " + arrResult[p][44]);
         * System.out.println("  ==> * 기본 주소(46:나머지 주소): " + arrResult[p][45]);
         * System.out.println("  ==> * 도로명 주소(47:도로명건물명): " + arrResult[p][46]);
         * System.out.println("  ==> * 도로명 주소(48:도로명건물명상세): " + arrResult[p][47]);
         * System.out.println("  ==> * 도로명 주소(49:도로명주소PNU): " + arrResult[p][48]);
         * System.out.println("  ==> * 기본 주소(50:안행부 배포-대표매칭여부): " + arrResult[p][49]);
         * System.out.println("  ==> * 기본 주소(51:공동주택여부): " + arrResult[p][50]);
         * System.out.println("  ==> * 기본 주소(52:부속건물명): " + arrResult[p][51]);
         * System.out.println("  ==> * 기본 주소(53:부속정보명): " + arrResult[p][52]);
         * System.out.println("  ==> * 기본 주소(54:예약1): " + arrResult[p][53]);
         * System.out.println("  ==> * 기본 주소(55:예약2): " + arrResult[p][54]);
         * System.out.println("  ==> * 기본 주소(56:예약3): " + arrResult[p][55]);
         * System.out.println("  ==> * 기본 주소(57:주소파싱레벨): " + arrResult[p][56]);
         * System.out.println("  ==> * 기본 주소(58:우편번호 부여레벨): " + arrResult[p][57]);
         * System.out.println("  ==> * 기본 주소(59:우편번호 사용 동탑입): " + arrResult[p][58]);
         * System.out.println("  ==> * 기본 주소(60:에러코드): " + arrResult[p][59]);
         * System.out.println("  ==> * 기본 주소(61:정보변경코드): " + arrResult[p][60]);
         * System.out.println("  ==> * 도로명 주소(62:도로명 X좌표): " + arrResult[p][61]);
         * System.out.println("  ==> * 도로명 주소(63:도로명 Y좌표): " + arrResult[p][62]);
         * System.out.println("  ==> * 기본 주소(64:멀티주소여부): " + arrResult[p][63]);
         * }
         */
        Document doc = new Document();
        
        Element root = new Element("address");
        
        Element el1 = new Element("ad_resultcd");
        Element el2 = new Element("ad_dongcd");
        
        Element el3 = new Element("ad_sido");
        Element el4 = new Element("ad_gungu");
        Element el5 = new Element("ad_dong");
        Element el6 = new Element("ad_ri");
        Element el7 = new Element("ad_san");
        Element el8 = new Element("ad_mn");
        Element el9 = new Element("ad_sn");
        Element el10 = new Element("ad_buildinfo");
        Element el11 = new Element("ad_builddong");
        Element el12 = new Element("ad_buildho");
        Element el13 = new Element("ad_buildstair");
        Element el14 = new Element("ad_remain");
        Element el15 = new Element("ad_postno");
        Element el16 = new Element("ad_postseq");
        Element el17 = new Element("ad_bdongcd");
        Element el18 = new Element("ad_hdongcd");
        Element el19 = new Element("ad_bdongnm");
        Element el20 = new Element("ad_hdongnm");
        Element el21 = new Element("ad_add1");
        Element el22 = new Element("ad_add2");
        Element el23 = new Element("ad_pnucd");
        
        Element el24 = new Element("ad_ro_roadnm");
        Element el25 = new Element("ad_ro_roadcd");
        Element el26 = new Element("ad_ro_dongseq");
        Element el27 = new Element("ad_ro_undercd");
        Element el28 = new Element("ad_ro_buildmn");
        Element el29 = new Element("ad_ro_buildsn");
        Element el30 = new Element("ad_ro_buildnm");
        Element el31 = new Element("ad_ro_builddtail");
        Element el32 = new Element("ad_ro_buildmgno");
        Element el33 = new Element("ad_ro_postno");
        Element el34 = new Element("ad_ro_postseq");
        Element el35 = new Element("ad_ro_add1");
        Element el36 = new Element("ad_ro_add2");
        Element el37 = new Element("ad_ro_ref");
        Element el38 = new Element("ad_ro_pnu");
        Element el39 = new Element("ad_ro_bsizonno");
        Element el40 = new Element("ad_ro_bsizonnoseq");
        Element el41 = new Element("ad_cx");
        Element el42 = new Element("ad_cy");
        Element el43 = new Element("ad_lev");
        Element el44 = new Element("ad_bsizonno");
        Element el45 = new Element("ad_bsizonnoseq");
        Element el46 = new Element("ad_reprematching");
        Element el47 = new Element("ad_apathouse");
        
        Element el48 = new Element("ad_annexbuild");
        Element el49 = new Element("ad_annexinfo");
        Element el50 = new Element("ad_etc1");
        Element el51 = new Element("ad_etc2");
        Element el52 = new Element("ad_etc3");
        
        Element el53 = new Element("ad_addrlev");
        Element el54 = new Element("ad_postlev");
        Element el55 = new Element("ad_posttype");
        Element el56 = new Element("ad_errcd");
        Element el57 = new Element("ad_datachgcd");
        
        Element el58 = new Element("ad_ro_cx");
        Element el59 = new Element("ad_ro_cy");
        
        Element el60 = new Element("ad_ro_totaladdcnt");
        Element el61 = new Element("ad_ro_repeatcnt");
        
        root.addContent(el1);
        root.addContent(el2);
        root.addContent(el3);
        root.addContent(el4);
        root.addContent(el5);
        root.addContent(el6);
        root.addContent(el7);
        root.addContent(el8);
        root.addContent(el9);
        root.addContent(el10);
        root.addContent(el11);
        root.addContent(el12);
        root.addContent(el13);
        root.addContent(el14);
        root.addContent(el15);
        root.addContent(el16);
        root.addContent(el17);
        root.addContent(el18);
        root.addContent(el19);
        root.addContent(el20);
        root.addContent(el21);
        root.addContent(el22);
        root.addContent(el23);
        root.addContent(el24);
        root.addContent(el25);
        root.addContent(el26);
        root.addContent(el27);
        root.addContent(el28);
        root.addContent(el29);
        root.addContent(el30);
        root.addContent(el31);
        root.addContent(el32);
        root.addContent(el33);
        root.addContent(el34);
        root.addContent(el35);
        root.addContent(el36);
        root.addContent(el37);
        root.addContent(el38);
        root.addContent(el39);
        root.addContent(el40);
        root.addContent(el41);
        root.addContent(el42);
        root.addContent(el43);
        root.addContent(el44);
        root.addContent(el45);
        root.addContent(el46);
        root.addContent(el47);
        root.addContent(el48);
        root.addContent(el49);
        root.addContent(el50);
        root.addContent(el51);
        root.addContent(el52);
        root.addContent(el53);
        root.addContent(el54);
        root.addContent(el55);
        root.addContent(el56);
        root.addContent(el57);
        root.addContent(el58);
        root.addContent(el59);
        root.addContent(el60);
        root.addContent(el61);
        
        if (!arrResult[0][1].equals("1") && !arrResult[0][2].equals("99"))
        {
            el1.setText(arrResult[0][2]);
            el2.setText(arrResult[0][40]); // ad_dongcd
            
            el3.setText(arrResult[0][12]); // ad_sido
            el4.setText(arrResult[0][13]);
            el5.setText(arrResult[0][14]);
            el6.setText(arrResult[0][15]); // ri
            
            el7.setText(arrResult[0][16]); // san
            el8.setText(arrResult[0][17]); // bunji
            el9.setText(arrResult[0][18]); // ho
            
            el10.setText(arrResult[0][41]); // buildinfo
            el11.setText(arrResult[0][42]); // builddong
            el12.setText(arrResult[0][43]); // buildho
            el13.setText(arrResult[0][44]); // buildstair
            el14.setText(arrResult[0][45]); // remain
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소, 사서함
            {
                el15.setText(arrResult[0][7]); // ad_postno
            }
            else
            // B 지번주소
            {
                el15.setText(arrResult[0][3]); // ad_postno
            }
            
            // el16.setText(arrResult[0][8]); //ad_postseq //2013-12-19 수정 김대광
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) //
            {
                el16.setText(arrResult[0][8]); // ad_postseq
            }
            else
            // B 지번 조회시 정제결과 우편번호
            {
                el16.setText(arrResult[0][4]); // ad_postseq
            }
            
            el17.setText(arrResult[0][22]); // ad_bdongcd
            el18.setText(arrResult[0][23]); // ad_hdongcd
            el19.setText(arrResult[0][30]); // ad_bdongnm
            el20.setText(arrResult[0][31]); // ad_hdongnm
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소
            {
                el21.setText(arrResult[0][9]); // ad_add1
                el22.setText(arrResult[0][10]); // ad_add2
            }
            else
            // 지번주소
            {
                el21.setText(arrResult[0][5]); // ad_add1
                el22.setText(arrResult[0][6]); // ad_add2
            }
            el23.setText(arrResult[0][32]); // ad_pnucd
            el24.setText(arrResult[0][33]); // ad_ro_roadnm
            el25.setText(arrResult[0][25]); // ad_ro_roadcd
            el26.setText(arrResult[0][26]); // ad_ro_dongseq
            el27.setText(arrResult[0][27]); // ad_ro_undercd
            el28.setText(arrResult[0][28]); // ad_ro_buildmn
            el29.setText(arrResult[0][29]); // ad_ro_buildsn
            
            el30.setText(arrResult[0][46]); // ad_ro_buildnm
            el31.setText(arrResult[0][47]); // ad_ro_builddtail
            el32.setText(arrResult[0][11]); // ad_ro_buildmgno
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S"))
            {
                el33.setText(arrResult[0][3]); // ad_ro_postno
            }
            else
            {
                el33.setText(arrResult[0][7]); // ad_ro_postno
            }
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S"))
            {
                el34.setText(arrResult[0][4]); // ad_ro_postseq
            }
            else
            {
                el34.setText(arrResult[0][8]); // ad_ro_postseq
            }
            
            // 도로명주소주소
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소
            {
                el35.setText(arrResult[0][5]); // ad_ro_add1 --------------도로명주소
                el36.setText(arrResult[0][6]); // ad_ro_add2 ----------------
            }
            else
            {
                el35.setText(arrResult[0][9]); // ad_ro_add1 --------------지번주소
                el36.setText(arrResult[0][10]); // ad_ro_add2 ----------------
            }
            el37.setText(arrResult[0][24]); // ad_ro_ref
            el38.setText(arrResult[0][48]); // ad_ro_pnu
            
            el39.setText(arrResult[0][38]); // ad_ro_bsizonno
            el40.setText(arrResult[0][39]); // ad_ro_bsizonnoseq
            
            el41.setText(arrResult[0][19]); // ad_cx
            el42.setText(arrResult[0][20]); // ad_cy
            el43.setText(arrResult[0][21]); // ad_lev
            
            el44.setText(arrResult[0][35]); // ad_bsizonno
            el45.setText(arrResult[0][36]); // ad_bsizonnoseq
            el46.setText(arrResult[0][49]); // ad_reprematching
            el47.setText(arrResult[0][50]); // ad_apathouse
            
            el48.setText(arrResult[0][51]); // ad_annexbuild
            el49.setText(arrResult[0][52]); // ad_annexinfo
            el50.setText(arrResult[0][53]); // ad_etc1
            el51.setText(arrResult[0][54]); // ad_etc2
            el52.setText(arrResult[0][55]); // ad_etc3
            
            el53.setText(arrResult[0][56]); // ad_addrlev
            el54.setText(arrResult[0][57]); // ad_postlev
            el55.setText(arrResult[0][58]); // ad_posttype
            el56.setText(arrResult[0][59]); // ad_errcd
            el57.setText(arrResult[0][60]); // ad_datachgcd
            
            el58.setText(arrResult[0][61]); // ad_ro_cx
            el59.setText(arrResult[0][62]); // ad_ro_cy
            
            el60.setText(Integer.toString(1)); // ad_ro_totaladdcnt
            el61.setText(Integer.toString(arrResult.length - 1)); // ad_ro_repeatcnt
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            if (arrResult[0][40].equals("N") || arrResult[0][40].equals("U") || (arrResult[0][40]).equals("S"))
            {
                for (int p = 1; p < noutcnt; p++)
                {
                    Element el62 = new Element("jibunaddr");
                    root.addContent(el62);
                    
                    Element el_sub1 = new Element("ji_dong");
                    Element el_sub2 = new Element("ji_ri");
                    Element el_sub3 = new Element("ji_san");
                    Element el_sub4 = new Element("ji_mn");
                    Element el_sub5 = new Element("ji_sn");
                    Element el_sub6 = new Element("ji_buildnm");
                    Element el_sub7 = new Element("ji_builddtail");
                    Element el_sub8 = new Element("ji_buildmgno");
                    Element el_sub9 = new Element("ji_bdongcd");
                    Element el_sub10 = new Element("ji_hdongcd");
                    Element el_sub11 = new Element("ji_bdongnm");
                    Element el_sub12 = new Element("ji_hdongnm");
                    Element el_sub13 = new Element("ji_postno");
                    Element el_sub14 = new Element("ji_postseq");
                    Element el_sub15 = new Element("ji_add1");
                    Element el_sub16 = new Element("ji_add2");
                    Element el_sub17 = new Element("ji_pnucd");
                    Element el_sub18 = new Element("ji_cx");
                    Element el_sub19 = new Element("ji_cy");
                    Element el_sub20 = new Element("ji_lev");
                    Element el_sub21 = new Element("ji_roadcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub22 = new Element("ji_bsizonno");
                    Element el_sub23 = new Element("ji_bsizonnoseq");
                    Element el_sub24 = new Element("ji_reprematching");
                    Element el_sub25 = new Element("ji_apathouse");
                    el_sub1.setText(arrResult[p][14]);
                    el_sub2.setText(arrResult[p][15]);
                    el_sub3.setText(arrResult[p][16]);
                    el_sub4.setText(arrResult[p][17]);
                    el_sub5.setText(arrResult[p][18]);
                    el_sub6.setText(arrResult[p][46]);
                    el_sub7.setText(arrResult[p][47]);
                    el_sub8.setText(arrResult[p][11]);
                    el_sub9.setText(arrResult[p][22]);
                    el_sub10.setText(arrResult[p][23]);
                    el_sub11.setText(arrResult[p][30]);
                    el_sub12.setText(arrResult[p][31]);
                    el_sub13.setText(arrResult[p][3]);
                    el_sub14.setText(arrResult[p][4]);
                    el_sub15.setText(arrResult[p][9]);
                    el_sub16.setText(arrResult[p][10]);
                    el_sub17.setText(arrResult[p][32]);
                    el_sub18.setText(arrResult[p][19]);
                    el_sub19.setText(arrResult[p][20]);
                    el_sub20.setText(arrResult[p][21]);
                    el_sub21.setText(arrResult[p][25]);
                    el_sub22.setText(arrResult[p][35]);
                    el_sub23.setText(arrResult[p][36]);
                    el_sub24.setText(arrResult[p][49]);
                    el_sub25.setText(arrResult[p][50]);
                    el62.addContent(el_sub1);
                    el62.addContent(el_sub2);
                    el62.addContent(el_sub3);
                    el62.addContent(el_sub4);
                    el62.addContent(el_sub5);
                    el62.addContent(el_sub6);
                    el62.addContent(el_sub7);
                    el62.addContent(el_sub8);
                    el62.addContent(el_sub9);
                    el62.addContent(el_sub10);
                    el62.addContent(el_sub11);
                    el62.addContent(el_sub12);
                    el62.addContent(el_sub13);
                    el62.addContent(el_sub14);
                    el62.addContent(el_sub15);
                    el62.addContent(el_sub16);
                    el62.addContent(el_sub17);
                    el62.addContent(el_sub18);
                    el62.addContent(el_sub19);
                    el62.addContent(el_sub20);
                    el62.addContent(el_sub21);
                    el62.addContent(el_sub22);
                    el62.addContent(el_sub23);
                    el62.addContent(el_sub24);
                    el62.addContent(el_sub25);
                }
            }
            else
            // 입력이 지번
            {
                for (int p = 1; p < noutcnt; p++)
                {
                    Element el62 = new Element("roadaddr");
                    root.addContent(el62);
                    
                    Element el_sub1 = new Element("ro_roadnm");
                    Element el_sub2 = new Element("ro_roadcd");
                    Element el_sub3 = new Element("ro_dongseq");
                    Element el_sub4 = new Element("ro_undercd");
                    Element el_sub5 = new Element("ro_buildmn");
                    Element el_sub6 = new Element("ro_buildsn");
                    Element el_sub7 = new Element("ro_buildnm");
                    Element el_sub8 = new Element("ro_builddtail");
                    Element el_sub9 = new Element("ro_buildmgno");
                    Element el_sub10 = new Element("ro_postno");
                    Element el_sub11 = new Element("ro_postseq");
                    Element el_sub12 = new Element("ro_add1");
                    Element el_sub13 = new Element("ro_add2");
                    Element el_sub14 = new Element("ro_ref");
                    Element el_sub15 = new Element("ro_pnu");
                    Element el_sub16 = new Element("ro_cx");
                    Element el_sub17 = new Element("ro_cy");
                    Element el_sub18 = new Element("ro_lev");
                    Element el_sub19 = new Element("ro_bdongcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub20 = new Element("ro_hdongcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub21 = new Element("ro_bdongnm"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub22 = new Element("ro_hdongnm"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub23 = new Element("ro_bsizonno");
                    Element el_sub24 = new Element("ro_bsizonnoseq");
                    Element el_sub25 = new Element("ro_reprematching");
                    Element el_sub26 = new Element("ro_apathouse");
                    el_sub1.setText(arrResult[p][33]);
                    el_sub2.setText(arrResult[p][25]);
                    el_sub3.setText(arrResult[p][26]);
                    el_sub4.setText(arrResult[p][27]);
                    el_sub5.setText(arrResult[p][28]);
                    el_sub6.setText(arrResult[p][29]);
                    el_sub7.setText(arrResult[p][46]);
                    el_sub8.setText(arrResult[p][47]);
                    el_sub9.setText(arrResult[p][11]);
                    el_sub10.setText(arrResult[p][7]);
                    el_sub11.setText(arrResult[p][8]);
                    el_sub12.setText(arrResult[p][9]);
                    el_sub13.setText(arrResult[p][10]);
                    el_sub14.setText(arrResult[p][24]);
                    el_sub15.setText(arrResult[p][48]);
                    el_sub16.setText(arrResult[p][61]);
                    el_sub17.setText(arrResult[p][62]);
                    el_sub18.setText(arrResult[p][21]);
                    el_sub19.setText(arrResult[p][22]);
                    el_sub20.setText(arrResult[p][23]);
                    el_sub21.setText(arrResult[p][30]);
                    el_sub22.setText(arrResult[p][31]);
                    el_sub23.setText(arrResult[p][38]);
                    el_sub24.setText(arrResult[p][39]);
                    el_sub25.setText(arrResult[p][49]);
                    el_sub26.setText(arrResult[p][50]);
                    el62.addContent(el_sub1);
                    el62.addContent(el_sub2);
                    el62.addContent(el_sub3);
                    el62.addContent(el_sub4);
                    el62.addContent(el_sub5);
                    el62.addContent(el_sub6);
                    el62.addContent(el_sub7);
                    el62.addContent(el_sub8);
                    el62.addContent(el_sub9);
                    el62.addContent(el_sub10);
                    el62.addContent(el_sub11);
                    el62.addContent(el_sub12);
                    el62.addContent(el_sub13);
                    el62.addContent(el_sub14);
                    el62.addContent(el_sub15);
                    el62.addContent(el_sub16);
                    el62.addContent(el_sub17);
                    el62.addContent(el_sub18);
                    el62.addContent(el_sub19);
                    el62.addContent(el_sub20);
                    el62.addContent(el_sub21);
                    el62.addContent(el_sub22);
                    el62.addContent(el_sub23);
                    el62.addContent(el_sub24);
                    el62.addContent(el_sub25);
                    el62.addContent(el_sub26);
                }
            } // else
        }
        else
        {
            el1.setText(arrResult[0][2]);
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getAddressDoc2EngAddr()
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getAddressDoc2EngAddr(String[][] arrResult, int noutcnt)
    {
        /*
         * for(int p = 0; p < arrResult.length ; p++)
         * {
         * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
         * System.out.println("  ==> * 지번/새주소구분(입력값기준, J:지번주소, N:도로명주소)  : " + arrResult[p][0]);
         * System.out.println("  ==> * FLAG1 (0:정상, 1:불능, 2:매칭실패)  : " + arrResult[p][1]);
         * System.out.println("  ==> * FLAG2 (변환코드) : " + arrResult[p][2]);
         * System.out.println("  ==> * 정제 결과 주소(4:우편번호)  : " + arrResult[p][3]);
         * System.out.println("  ==> * 정제 결과 주소(5:우편번호일련번호)  : " + arrResult[p][4]);
         * System.out.println("  ==> * 정제 결과 주소(6:기본주소): " + arrResult[p][5]);
         * System.out.println("  ==> * 정제 결과 주소(7:상세주소): " + arrResult[p][6]);
         * System.out.println("  ==> * 변환 결과 주소(8:우편번호)  : " + arrResult[p][7]);
         * System.out.println("  ==> * 변환 결과 주소(9:우편번호일련번호)  : " + arrResult[p][8]);
         * System.out.println("  ==> * 변환 결과 주소(10:기본주소): " + arrResult[p][9]);
         * System.out.println("  ==> * 변환 결과 주소(11:상세주소): " + arrResult[p][10]);
         * System.out.println("  ==> * 도로명 주소(12:건물관리번호): " + arrResult[p][11]);
         * System.out.println("  ==> * 기본 주소(13:시도): " + arrResult[p][12]);
         * System.out.println("  ==> * 기본 주소(14:시군구): " + arrResult[p][13]);
         * System.out.println("  ==> * 기본 주소(15:읍면동): " + arrResult[p][14]);
         * System.out.println("  ==> * 기본 주소(16:리): " + arrResult[p][15]);
         * System.out.println("  ==> * 지번 주소(17:산여부): " + arrResult[p][16]);
         * System.out.println("  ==> * 지번 주소(18:주번지): " + arrResult[p][17]);
         * System.out.println("  ==> * 지번 주소(19:부번지): " + arrResult[p][18]);
         * System.out.println("  ==> * 좌표(20:X좌표): " + arrResult[p][19]);
         * System.out.println("  ==> * 좌표(21:Y좌표): " + arrResult[p][20]);
         * System.out.println("  ==> * 좌표(22:좌표부여레벨): " + arrResult[p][21]);
         * System.out.println("  ==> * 기본 주소(23:법정동코드): " + arrResult[p][22]);
         * System.out.println("  ==> * 기본 주소(24:행정동코드): " + arrResult[p][23]);
         * System.out.println("  ==> * 도로명 주소(25:괄호(참고) 주소): " + arrResult[p][24]);
         * System.out.println("  ==> * 도로명 주소(26:도로명 코드): " + arrResult[p][25]);
         * System.out.println("  ==> * 도로명 주소(27:읍/면/동 일련번호): " + arrResult[p][26]);
         * System.out.println("  ==> * 도로명 주소(28:지하 여부): " + arrResult[p][27]);
         * System.out.println("  ==> * 도로명 주소(29:건물 본번): " + arrResult[p][28]);
         * System.out.println("  ==> * 도로명 주소(30:건물 부번): " + arrResult[p][29]);
         * System.out.println("  ==> * 도로명 주소(31:법정동명): " + arrResult[p][30]);
         * System.out.println("  ==> * 도로명 주소(32:행정동명): " + arrResult[p][31]);
         * System.out.println("  ==> * 지번 주소 PNU코드: " + arrResult[p][32]);
         * System.out.println("  ==> * 도로명 주소(34:도로명): " + arrResult[p][33]);
         * System.out.println("  ==> * 지번 주소(35:국가지점번호): " + arrResult[p][34]);
         * System.out.println("  ==> * 지번 주소(36:국가기초구역번호): " + arrResult[p][35]);
         * System.out.println("  ==> * 지번 주소(37:국가기초구역일련번호): " + arrResult[p][36]);
         * System.out.println("  ==> * 도로명 주소(38:국가지점번호): " + arrResult[p][37]);
         * System.out.println("  ==> * 도로명 주소(39:국가기초구역번호): " + arrResult[p][38]);
         * System.out.println("  ==> * 도로명 주소(40:국가기초구역일련번호): " + arrResult[p][39]);
         * System.out.println("  ==> * 기본 주소(41:입력동구분): " + arrResult[p][40]);
         * System.out.println("  ==> * 기본 주소(42:빌딩 매칭 정보): " + arrResult[p][41]);
         * System.out.println("  ==> * 기본 주소(43:빌딩 동 정보): " + arrResult[p][42]);
         * System.out.println("  ==> * 기본 주소(44:빌딩 호 정보): " + arrResult[p][43]);
         * System.out.println("  ==> * 기본 주소(45:빌딩 층 정보): " + arrResult[p][44]);
         * System.out.println("  ==> * 기본 주소(46:나머지 주소): " + arrResult[p][45]);
         * System.out.println("  ==> * 도로명 주소(47:도로명건물명): " + arrResult[p][46]);
         * System.out.println("  ==> * 도로명 주소(48:도로명건물명상세): " + arrResult[p][47]);
         * System.out.println("  ==> * 도로명 주소(49:도로명주소PNU): " + arrResult[p][48]);
         * System.out.println("  ==> * 기본 주소(50:안행부 배포-대표매칭여부): " + arrResult[p][49]);
         * System.out.println("  ==> * 기본 주소(51:공동주택여부): " + arrResult[p][50]);
         * System.out.println("  ==> * 기본 주소(52:부속건물명): " + arrResult[p][51]);
         * System.out.println("  ==> * 기본 주소(53:부속정보명): " + arrResult[p][52]);
         * System.out.println("  ==> * 기본 주소(54:예약1): " + arrResult[p][53]);
         * System.out.println("  ==> * 기본 주소(55:예약2): " + arrResult[p][54]);
         * System.out.println("  ==> * 기본 주소(56:예약3): " + arrResult[p][55]);
         * System.out.println("  ==> * 기본 주소(57:주소파싱레벨): " + arrResult[p][56]);
         * System.out.println("  ==> * 기본 주소(58:우편번호 부여레벨): " + arrResult[p][57]);
         * System.out.println("  ==> * 기본 주소(59:우편번호 사용 동탑입): " + arrResult[p][58]);
         * System.out.println("  ==> * 기본 주소(60:에러코드): " + arrResult[p][59]);
         * System.out.println("  ==> * 기본 주소(61:정보변경코드): " + arrResult[p][60]);
         * System.out.println("  ==> * 도로명 주소(62:도로명 X좌표): " + arrResult[p][61]);
         * System.out.println("  ==> * 도로명 주소(63:도로명 Y좌표): " + arrResult[p][62]);
         * System.out.println("  ==> * 기본 주소(64:멀티주소여부): " + arrResult[p][63]);
         * // 영어주소 출력 활성화 시 영어주소 출력항목이 추가된다. [7개]
         * System.out.println("  ==> * 기본 주소(65:영문 시도명): " + arrResult[p][64]);
         * System.out.println("  ==> * 기본 주소(66:영문 시군구명): " + arrResult[p][65]);
         * System.out.println("  ==> * 기본 주소(67:영문 읍면동명): " + arrResult[p][66]);
         * System.out.println("  ==> * 기본 주소(68:영문 법정리): " + arrResult[p][67]);
         * System.out.println("  ==> * 기본 주소(69:영문 다량배달처): " + arrResult[p][68]);
         * System.out.println("  ==> * 기본 주소(70:영문 도로명): " + arrResult[p][69]);
         * System.out.println("  ==> * 기본 주소(71:영문 시군구건물명): " + arrResult[p][70]);
         * }
         */
        Document doc = new Document();
        
        Element root = new Element("address");
        
        Element el1 = new Element("ad_resultcd");
        Element el2 = new Element("ad_dongcd");
        
        Element el3 = new Element("ad_sido");
        Element el4 = new Element("ad_gungu");
        Element el5 = new Element("ad_dong");
        Element el6 = new Element("ad_ri");
        Element el7 = new Element("ad_san");
        Element el8 = new Element("ad_mn");
        Element el9 = new Element("ad_sn");
        Element el10 = new Element("ad_buildinfo");
        Element el11 = new Element("ad_builddong");
        Element el12 = new Element("ad_buildho");
        Element el13 = new Element("ad_buildstair");
        Element el14 = new Element("ad_remain");
        Element el15 = new Element("ad_postno");
        Element el16 = new Element("ad_postseq");
        Element el17 = new Element("ad_bdongcd");
        Element el18 = new Element("ad_hdongcd");
        Element el19 = new Element("ad_bdongnm");
        Element el20 = new Element("ad_hdongnm");
        Element el21 = new Element("ad_add1");
        Element el22 = new Element("ad_add2");
        Element el23 = new Element("ad_pnucd");
        
        Element el24 = new Element("ad_ro_roadnm");
        Element el25 = new Element("ad_ro_roadcd");
        Element el26 = new Element("ad_ro_dongseq");
        Element el27 = new Element("ad_ro_undercd");
        Element el28 = new Element("ad_ro_buildmn");
        Element el29 = new Element("ad_ro_buildsn");
        Element el30 = new Element("ad_ro_buildnm");
        Element el31 = new Element("ad_ro_builddtail");
        Element el32 = new Element("ad_ro_buildmgno");
        Element el33 = new Element("ad_ro_postno");
        Element el34 = new Element("ad_ro_postseq");
        Element el35 = new Element("ad_ro_add1");
        Element el36 = new Element("ad_ro_add2");
        Element el37 = new Element("ad_ro_ref");
        Element el38 = new Element("ad_ro_pnu");
        Element el39 = new Element("ad_ro_bsizonno");
        Element el40 = new Element("ad_ro_bsizonnoseq");
        Element el41 = new Element("ad_cx");
        Element el42 = new Element("ad_cy");
        Element el43 = new Element("ad_lev");
        Element el44 = new Element("ad_bsizonno");
        Element el45 = new Element("ad_bsizonnoseq");
        Element el46 = new Element("ad_reprematching");
        Element el47 = new Element("ad_apathouse");
        
        Element el48 = new Element("ad_annexbuild");
        Element el49 = new Element("ad_annexinfo");
        Element el50 = new Element("ad_etc1");
        Element el51 = new Element("ad_etc2");
        Element el52 = new Element("ad_etc3");
        
        Element el53 = new Element("ad_addrlev");
        Element el54 = new Element("ad_postlev");
        Element el55 = new Element("ad_posttype");
        Element el56 = new Element("ad_errcd");
        Element el57 = new Element("ad_datachgcd");
        
        Element el58 = new Element("ad_ro_cx");
        Element el59 = new Element("ad_ro_cy");
        
        Element el60 = new Element("ad_sido_eng");
        Element el61 = new Element("ad_gungu_eng");
        Element el62 = new Element("ad_dong_eng");
        Element el63 = new Element("ad_ri_eng");
        Element el64 = new Element("ad_buildnm_eng");
        Element el65 = new Element("ad_roadnm_eng");
        Element el66 = new Element("ad_ro_buildnm_eng");
        
        Element el67 = new Element("ad_ro_totaladdcnt");
        Element el68 = new Element("ad_ro_repeatcnt");
        
        root.addContent(el1);
        root.addContent(el2);
        root.addContent(el3);
        root.addContent(el4);
        root.addContent(el5);
        root.addContent(el6);
        root.addContent(el7);
        root.addContent(el8);
        root.addContent(el9);
        root.addContent(el10);
        root.addContent(el11);
        root.addContent(el12);
        root.addContent(el13);
        root.addContent(el14);
        root.addContent(el15);
        root.addContent(el16);
        root.addContent(el17);
        root.addContent(el18);
        root.addContent(el19);
        root.addContent(el20);
        root.addContent(el21);
        root.addContent(el22);
        root.addContent(el23);
        root.addContent(el24);
        root.addContent(el25);
        root.addContent(el26);
        root.addContent(el27);
        root.addContent(el28);
        root.addContent(el29);
        root.addContent(el30);
        root.addContent(el31);
        root.addContent(el32);
        root.addContent(el33);
        root.addContent(el34);
        root.addContent(el35);
        root.addContent(el36);
        root.addContent(el37);
        root.addContent(el38);
        root.addContent(el39);
        root.addContent(el40);
        root.addContent(el41);
        root.addContent(el42);
        root.addContent(el43);
        root.addContent(el44);
        root.addContent(el45);
        root.addContent(el46);
        root.addContent(el47);
        root.addContent(el48);
        root.addContent(el49);
        root.addContent(el50);
        root.addContent(el51);
        root.addContent(el52);
        root.addContent(el53);
        root.addContent(el54);
        root.addContent(el55);
        root.addContent(el56);
        root.addContent(el57);
        root.addContent(el58);
        root.addContent(el59);
        root.addContent(el60);
        root.addContent(el61);
        root.addContent(el62);
        root.addContent(el63);
        root.addContent(el64);
        root.addContent(el65);
        root.addContent(el66);
        root.addContent(el67);
        root.addContent(el68);
        
        if (!arrResult[0][1].equals("1") && !arrResult[0][2].equals("99"))
        {
            el1.setText(arrResult[0][2]);
            el2.setText(arrResult[0][40]); // ad_dongcd
            
            el3.setText(arrResult[0][12]); // ad_sido
            el4.setText(arrResult[0][13]);
            el5.setText(arrResult[0][14]);
            el6.setText(arrResult[0][15]); // ri
            
            el7.setText(arrResult[0][16]); // san
            el8.setText(arrResult[0][17]); // bunji
            el9.setText(arrResult[0][18]); // ho
            
            el10.setText(arrResult[0][41]); // buildinfo
            el11.setText(arrResult[0][42]); // builddong
            el12.setText(arrResult[0][43]); // buildho
            el13.setText(arrResult[0][44]); // buildstair
            el14.setText(arrResult[0][45]); // remain
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소, 사서함
            {
                el15.setText(arrResult[0][7]); // ad_postno
            }
            else
            // B 지번주소
            {
                el15.setText(arrResult[0][3]); // ad_postno
            }
            
            // el16.setText(arrResult[0][8]); //ad_postseq //2013-12-19 수정 김대광
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) //
            {
                el16.setText(arrResult[0][8]); // ad_postseq
            }
            else
            // B 지번 조회시 정제결과 우편번호
            {
                el16.setText(arrResult[0][4]); // ad_postseq
            }
            
            el17.setText(arrResult[0][22]); // ad_bdongcd
            el18.setText(arrResult[0][23]); // ad_hdongcd
            el19.setText(arrResult[0][30]); // ad_bdongnm
            el20.setText(arrResult[0][31]); // ad_hdongnm
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소
            {
                el21.setText(arrResult[0][9]); // ad_add1
                el22.setText(arrResult[0][10]); // ad_add2
            }
            else
            // 지번주소
            {
                el21.setText(arrResult[0][5]); // ad_add1
                el22.setText(arrResult[0][6]); // ad_add2
            }
            el23.setText(arrResult[0][32]); // ad_pnucd
            el24.setText(arrResult[0][33]); // ad_ro_roadnm
            el25.setText(arrResult[0][25]); // ad_ro_roadcd
            el26.setText(arrResult[0][26]); // ad_ro_dongseq
            el27.setText(arrResult[0][27]); // ad_ro_undercd
            el28.setText(arrResult[0][28]); // ad_ro_buildmn
            el29.setText(arrResult[0][29]); // ad_ro_buildsn
            
            el30.setText(arrResult[0][46]); // ad_ro_buildnm
            el31.setText(arrResult[0][47]); // ad_ro_builddtail
            el32.setText(arrResult[0][11]); // ad_ro_buildmgno
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S"))
            {
                el33.setText(arrResult[0][3]); // ad_ro_postno
            }
            else
            {
                el33.setText(arrResult[0][7]); // ad_ro_postno
            }
            
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S"))
            {
                el34.setText(arrResult[0][4]); // ad_ro_postseq
            }
            else
            {
                el34.setText(arrResult[0][8]); // ad_ro_postseq
            }
            
            // 도로명주소주소
            if ((arrResult[0][40]).equals("N") || (arrResult[0][40]).equals("U") || (arrResult[0][40]).equals("S")) // 도로명주소주소, 사서함
            {
                el35.setText(arrResult[0][5]); // ad_ro_add1 --------------도로명주소
                el36.setText(arrResult[0][6]); // ad_ro_add2 ----------------
            }
            else
            {
                el35.setText(arrResult[0][9]); // ad_ro_add1 --------------지번주소
                el36.setText(arrResult[0][10]); // ad_ro_add2 ----------------
            }
            el37.setText(arrResult[0][24]); // ad_ro_ref
            el38.setText(arrResult[0][48]); // ad_ro_pnu
            
            el39.setText(arrResult[0][38]); // ad_ro_bsizonno
            el40.setText(arrResult[0][39]); // ad_ro_bsizonnoseq
            
            el41.setText(arrResult[0][19]); // ad_cx
            el42.setText(arrResult[0][20]); // ad_cy
            el43.setText(arrResult[0][21]); // ad_lev
            
            el44.setText(arrResult[0][35]); // ad_bsizonno
            el45.setText(arrResult[0][36]); // ad_bsizonnoseq
            el46.setText(arrResult[0][49]); // ad_reprematching
            el47.setText(arrResult[0][50]); // ad_apathouse
            
            el48.setText(arrResult[0][51]); // ad_annexbuild
            el49.setText(arrResult[0][52]); // ad_annexinfo
            el50.setText(arrResult[0][53]); // ad_etc1
            el51.setText(arrResult[0][54]); // ad_etc2
            el52.setText(arrResult[0][55]); // ad_etc3
            
            el53.setText(arrResult[0][56]); // ad_addrlev
            el54.setText(arrResult[0][57]); // ad_postlev
            el55.setText(arrResult[0][58]); // ad_posttype
            el56.setText(arrResult[0][59]); // ad_errcd
            el57.setText(arrResult[0][60]); // ad_datachgcd
            
            el58.setText(arrResult[0][61]); // ad_ro_cx
            el59.setText(arrResult[0][62]); // ad_ro_cy
            
            el60.setText(arrResult[0][64]); // ad_sido_eng
            el61.setText(arrResult[0][65]); // ad_gungu_eng
            el62.setText(arrResult[0][66]); // ad_dong_eng
            el63.setText(arrResult[0][67]); // ad_ri_eng
            el64.setText(arrResult[0][68]); // ad_buildnm_eng
            el65.setText(arrResult[0][69]); // ad_roadnm_eng
            el66.setText(arrResult[0][70]); // ad_ro_buildnm_eng
            
            el67.setText(Integer.toString(1)); // ad_ro_totaladdcnt
            el68.setText(Integer.toString(arrResult.length - 1)); // ad_ro_repeatcnt
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            if (arrResult[0][40].equals("N") || arrResult[0][40].equals("U") || (arrResult[0][40]).equals("S"))
            {
                for (int p = 1; p < noutcnt; p++)
                {
                    Element el69 = new Element("jibunaddr");
                    root.addContent(el69);
                    
                    Element el_sub1 = new Element("ji_dong");
                    Element el_sub2 = new Element("ji_ri");
                    Element el_sub3 = new Element("ji_san");
                    Element el_sub4 = new Element("ji_mn");
                    Element el_sub5 = new Element("ji_sn");
                    Element el_sub6 = new Element("ji_buildnm");
                    Element el_sub7 = new Element("ji_builddtail");
                    Element el_sub8 = new Element("ji_buildmgno");
                    Element el_sub9 = new Element("ji_bdongcd");
                    Element el_sub10 = new Element("ji_hdongcd");
                    Element el_sub11 = new Element("ji_bdongnm");
                    Element el_sub12 = new Element("ji_hdongnm");
                    Element el_sub13 = new Element("ji_postno");
                    Element el_sub14 = new Element("ji_postseq");
                    Element el_sub15 = new Element("ji_add1");
                    Element el_sub16 = new Element("ji_add2");
                    Element el_sub17 = new Element("ji_pnucd");
                    Element el_sub18 = new Element("ji_cx");
                    Element el_sub19 = new Element("ji_cy");
                    Element el_sub20 = new Element("ji_lev");
                    Element el_sub21 = new Element("ji_roadcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub22 = new Element("ji_bsizonno");
                    Element el_sub23 = new Element("ji_bsizonnoseq");
                    Element el_sub24 = new Element("ji_reprematching");
                    Element el_sub25 = new Element("ji_apathouse");
                    Element el_sub26 = new Element("ji_dong_eng");
                    Element el_sub27 = new Element("ji_ri_eng");
                    Element el_sub28 = new Element("ji_buildnm_eng");
                    
                    el_sub1.setText(arrResult[p][14]);
                    el_sub2.setText(arrResult[p][15]);
                    el_sub3.setText(arrResult[p][16]);
                    el_sub4.setText(arrResult[p][17]);
                    el_sub5.setText(arrResult[p][18]);
                    el_sub6.setText(arrResult[p][46]);
                    el_sub7.setText(arrResult[p][47]);
                    el_sub8.setText(arrResult[p][11]);
                    el_sub9.setText(arrResult[p][22]);
                    el_sub10.setText(arrResult[p][23]);
                    el_sub11.setText(arrResult[p][30]);
                    el_sub12.setText(arrResult[p][31]);
                    el_sub13.setText(arrResult[p][3]);
                    el_sub14.setText(arrResult[p][4]);
                    el_sub15.setText(arrResult[p][9]);
                    el_sub16.setText(arrResult[p][10]);
                    el_sub17.setText(arrResult[p][32]);
                    el_sub18.setText(arrResult[p][19]);
                    el_sub19.setText(arrResult[p][20]);
                    el_sub20.setText(arrResult[p][21]);
                    el_sub21.setText(arrResult[p][25]);
                    el_sub22.setText(arrResult[p][35]);
                    el_sub23.setText(arrResult[p][36]);
                    el_sub24.setText(arrResult[p][49]);
                    el_sub25.setText(arrResult[p][50]);
                    el_sub26.setText(arrResult[p][66]); // ji_dong_eng
                    el_sub27.setText(arrResult[p][67]); // ji_ri_eng
                    el_sub28.setText(arrResult[p][68]); // ji_buildnm_eng
                    el69.addContent(el_sub1);
                    el69.addContent(el_sub2);
                    el69.addContent(el_sub3);
                    el69.addContent(el_sub4);
                    el69.addContent(el_sub5);
                    el69.addContent(el_sub6);
                    el69.addContent(el_sub7);
                    el69.addContent(el_sub8);
                    el69.addContent(el_sub9);
                    el69.addContent(el_sub10);
                    el69.addContent(el_sub11);
                    el69.addContent(el_sub12);
                    el69.addContent(el_sub13);
                    el69.addContent(el_sub14);
                    el69.addContent(el_sub15);
                    el69.addContent(el_sub16);
                    el69.addContent(el_sub17);
                    el69.addContent(el_sub18);
                    el69.addContent(el_sub19);
                    el69.addContent(el_sub20);
                    el69.addContent(el_sub21);
                    el69.addContent(el_sub22);
                    el69.addContent(el_sub23);
                    el69.addContent(el_sub24);
                    el69.addContent(el_sub25);
                    el69.addContent(el_sub26);
                    el69.addContent(el_sub27);
                    el69.addContent(el_sub28);
                }
            }
            else
            // 입력이 지번
            {
                for (int p = 1; p < noutcnt; p++)
                {
                    Element el69 = new Element("roadaddr");
                    root.addContent(el69);
                    
                    Element el_sub1 = new Element("ro_roadnm");
                    Element el_sub2 = new Element("ro_roadcd");
                    Element el_sub3 = new Element("ro_dongseq");
                    Element el_sub4 = new Element("ro_undercd");
                    Element el_sub5 = new Element("ro_buildmn");
                    Element el_sub6 = new Element("ro_buildsn");
                    Element el_sub7 = new Element("ro_buildnm");
                    Element el_sub8 = new Element("ro_builddtail");
                    Element el_sub9 = new Element("ro_buildmgno");
                    Element el_sub10 = new Element("ro_postno");
                    Element el_sub11 = new Element("ro_postseq");
                    Element el_sub12 = new Element("ro_add1");
                    Element el_sub13 = new Element("ro_add2");
                    Element el_sub14 = new Element("ro_ref");
                    Element el_sub15 = new Element("ro_pnu");
                    Element el_sub16 = new Element("ro_cx");
                    Element el_sub17 = new Element("ro_cy");
                    Element el_sub18 = new Element("ro_lev");
                    Element el_sub19 = new Element("ro_bdongcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub20 = new Element("ro_hdongcd"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub21 = new Element("ro_bdongnm"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub22 = new Element("ro_hdongnm"); // jsy IN/OUT 일치 시키기 위하여 추가
                    Element el_sub23 = new Element("ro_bsizonno");
                    Element el_sub24 = new Element("ro_bsizonnoseq");
                    Element el_sub25 = new Element("ro_reprematching");
                    Element el_sub26 = new Element("ro_apathouse");
                    Element el_sub27 = new Element("ro_dong_eng");
                    Element el_sub28 = new Element("ro_roadnm_eng");
                    Element el_sub29 = new Element("ro_buildnm_eng");
                    
                    el_sub1.setText(arrResult[p][33]);
                    el_sub2.setText(arrResult[p][25]);
                    el_sub3.setText(arrResult[p][26]);
                    el_sub4.setText(arrResult[p][27]);
                    el_sub5.setText(arrResult[p][28]);
                    el_sub6.setText(arrResult[p][29]);
                    el_sub7.setText(arrResult[p][46]);
                    el_sub8.setText(arrResult[p][47]);
                    el_sub9.setText(arrResult[p][11]);
                    el_sub10.setText(arrResult[p][7]);
                    el_sub11.setText(arrResult[p][8]);
                    el_sub12.setText(arrResult[p][9]);
                    el_sub13.setText(arrResult[p][10]);
                    el_sub14.setText(arrResult[p][24]);
                    el_sub15.setText(arrResult[p][48]);
                    el_sub16.setText(arrResult[p][61]);
                    el_sub17.setText(arrResult[p][62]);
                    el_sub18.setText(arrResult[p][21]);
                    el_sub19.setText(arrResult[p][22]);
                    el_sub20.setText(arrResult[p][23]);
                    el_sub21.setText(arrResult[p][30]);
                    el_sub22.setText(arrResult[p][31]);
                    el_sub23.setText(arrResult[p][38]);
                    el_sub24.setText(arrResult[p][39]);
                    el_sub25.setText(arrResult[p][49]);
                    el_sub26.setText(arrResult[p][50]);
                    el_sub27.setText(arrResult[p][66]); // ro_dong_eng
                    el_sub28.setText(arrResult[p][69]); // ro_roadnm_eng
                    el_sub29.setText(arrResult[p][70]); // ro_buildnm_eng
                    el69.addContent(el_sub1);
                    el69.addContent(el_sub2);
                    el69.addContent(el_sub3);
                    el69.addContent(el_sub4);
                    el69.addContent(el_sub5);
                    el69.addContent(el_sub6);
                    el69.addContent(el_sub7);
                    el69.addContent(el_sub8);
                    el69.addContent(el_sub9);
                    el69.addContent(el_sub10);
                    el69.addContent(el_sub11);
                    el69.addContent(el_sub12);
                    el69.addContent(el_sub13);
                    el69.addContent(el_sub14);
                    el69.addContent(el_sub15);
                    el69.addContent(el_sub16);
                    el69.addContent(el_sub17);
                    el69.addContent(el_sub18);
                    el69.addContent(el_sub19);
                    el69.addContent(el_sub20);
                    el69.addContent(el_sub21);
                    el69.addContent(el_sub22);
                    el69.addContent(el_sub23);
                    el69.addContent(el_sub24);
                    el69.addContent(el_sub25);
                    el69.addContent(el_sub26);
                    el69.addContent(el_sub27);
                    el69.addContent(el_sub28);
                    el69.addContent(el_sub29);
                }
            } // else
        }
        else
        {
            el1.setText(arrResult[0][2]);
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getAddressMultiDoc2()
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-Via 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getAddressMultiDoc2(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        
        Element root = new Element("address");
        
        Element el1 = new Element("ad_resultcd");
        Element el2 = new Element("ad_dongcd");
        Element el3 = new Element("ad_totcnt");
        Element el4 = new Element("ad_etcaddr");
        
        root.addContent(el1);
        root.addContent(el2);
        root.addContent(el3);
        root.addContent(el4);
        
        el1.setText(arrResult[0][2]);
        el2.setText(arrResult[0][3]); // ad_dongcd
        el3.setText(arrResult[0][4]);
        el4.setText(arrResult[0][5]);
        
        if (noutcnt > arrResult.length)
        {
            noutcnt = arrResult.length;
        }
        
        for (int p = 1; p < noutcnt; p++)
        {
            /*
             * System.out.println("  ==> *----------- "+ p +"  --------------------------------------------------------------------------------------");
             * System.out.println("  ==> * 우편번호  : " + arrResult[p][0]);
             * System.out.println("  ==> * 광역시/도 : " + arrResult[p][1]);
             * System.out.println("  ==> * 시/군/구  : " + arrResult[p][2]);
             * System.out.println("  ==> * 읍/면/동  : " + arrResult[p][3]);
             * System.out.println("  ==> * 리  : " + arrResult[p][4]);
             * System.out.println("  ==> * 도로명  : " + arrResult[p][5]);
             * System.out.println("  ==> * 행정동  : " + arrResult[p][6]);
             * System.out.println("  ==> * 법정동  : " + arrResult[p][7]);
             * System.out.println("  ==> * 산/지하  : " + arrResult[p][8]);
             * System.out.println("  ==> * 지번/건물 주번호  : " + arrResult[p][9]);
             * System.out.println("  ==> * 지번/건물 부번호  : " + arrResult[p][10]);
             * System.out.println("  ==> * 건물명  : " + arrResult[p][11]);
             * System.out.println("  ==> * 예약1  : " + arrResult[p][12]);
             * System.out.println("  ==> * 예약2  : " + arrResult[p][13]);
             */
            
            Element el5 = new Element("ad_list");
            root.addContent(el5);
            
            Element el_sub1 = new Element("rs_postcd");
            Element el_sub2 = new Element("rs_sido");
            Element el_sub3 = new Element("rs_gungu");
            Element el_sub4 = new Element("rs_umd");
            Element el_sub5 = new Element("rs_ri");
            Element el_sub6 = new Element("rs_roadnm");
            Element el_sub7 = new Element("rs_hdong");
            Element el_sub8 = new Element("rs_bdong");
            Element el_sub9 = new Element("rs_gbcd");
            Element el_sub10 = new Element("rs_mnno");
            Element el_sub11 = new Element("rs_snno");
            Element el_sub12 = new Element("rs_buildnm");
            Element el_sub13 = new Element("rs_etc1");
            Element el_sub14 = new Element("rs_etc2");
            el_sub1.setText(arrResult[p][0]);
            el_sub2.setText(arrResult[p][1]);
            el_sub3.setText(arrResult[p][2]);
            el_sub4.setText(arrResult[p][3]);
            el_sub5.setText(arrResult[p][4]);
            el_sub6.setText(arrResult[p][5]);
            el_sub7.setText(arrResult[p][6]);
            el_sub8.setText(arrResult[p][7]);
            el_sub9.setText(arrResult[p][8]);
            el_sub10.setText(arrResult[p][9]);
            el_sub11.setText(arrResult[p][10]);
            el_sub12.setText(arrResult[p][11]);
            el_sub13.setText(arrResult[p][12]);
            el_sub14.setText(arrResult[p][13]);
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
        }
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getDistrictDoc2() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getDistrictDoc2(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("regioninfo");
        Element el1 = new Element("ri_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
        }
        else
        // 정상 처리
        {
            Element el2 = new Element("ri_totrecord");
            Element el3 = new Element("ri_strecord");
            Element el4 = new Element("ri_rorecord");
            Element el5 = new Element("ri_donggu");
            Element el6 = new Element("ri_hdonglv");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            root.addContent(el5);
            root.addContent(el6);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            el5.setText(arrResult[0][4]);
            el6.setText(arrResult[0][5]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("  ==> * 동코드구분  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 동레벨 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 코드  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 명칭  : " + arrResult[p][7]);
                 */
                Element el7 = new Element("ri_rep");
                root.addContent(el7);
                
                Element el_sub1 = new Element("ri_regioncd");
                el_sub1.setText(arrResult[p][6]);
                Element el_sub2 = new Element("ri_regionnm");
                el_sub2.setText(arrResult[p][7]);
                el7.addContent(el_sub1);
                el7.addContent(el_sub2);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getDistrictDoc2EngAddr() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getDistrictDoc2EngAddr(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("regioninfo");
        Element el1 = new Element("ri_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
        }
        else
        // 정상 처리
        {
            Element el2 = new Element("ri_totrecord");
            Element el3 = new Element("ri_strecord");
            Element el4 = new Element("ri_rorecord");
            Element el5 = new Element("ri_donggu");
            Element el6 = new Element("ri_hdonglv");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            root.addContent(el5);
            root.addContent(el6);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            el5.setText(arrResult[0][4]);
            el6.setText(arrResult[0][5]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("  ==> * 동코드구분  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 동레벨 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 코드  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 명칭  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 영문 명칭  : " + arrResult[p][8]);
                 */
                Element el7 = new Element("ri_rep");
                root.addContent(el7);
                Element el_sub1 = new Element("ri_regioncd");
                Element el_sub2 = new Element("ri_regionnm");
                Element el_sub3 = new Element("ri_regionnm");
                el_sub1.setText(arrResult[p][6]);
                el_sub2.setText(arrResult[p][7]);
                el_sub3.setText(arrResult[p][8]);
                el7.addContent(el_sub1);
                el7.addContent(el_sub2);
                el7.addContent(el_sub3);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getLtnoPostCdDoc2() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getLtnoPostCdDoc2(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("jibunpost");
        Element el1 = new Element("jp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("jp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("jp_postno");
            Element el_sub2 = new Element("jp_postseq");
            Element el_sub3 = new Element("jp_sido");
            Element el_sub4 = new Element("jp_gungu");
            Element el_sub5 = new Element("jp_dong");
            Element el_sub6 = new Element("jp_ri");
            Element el_sub7 = new Element("jp_island");
            Element el_sub8 = new Element("jp_san");
            Element el_sub9 = new Element("jp_start_mn");
            Element el_sub10 = new Element("jp_start_sn");
            Element el_sub11 = new Element("jp_end_mn");
            Element el_sub12 = new Element("jp_end_sn");
            Element el_sub13 = new Element("jp_div_nm");
            Element el_sub14 = new Element("jp_div_start");
            Element el_sub15 = new Element("jp_div_end");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
        }
        else
        // 정상 처리
        {
            
            Element el2 = new Element("jp_totrecord");
            Element el3 = new Element("jp_strecord");
            Element el4 = new Element("jp_rorecord");
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 우편번호  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 도서 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 산  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시작주번지 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 시작부번지 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 종료주번지 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 종료부번지 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 다량시작동 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 다량종료동 : " + arrResult[p][18]);
                 */
                
                Element el5 = new Element("jp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("jp_postno");
                Element el_sub2 = new Element("jp_postseq");
                Element el_sub3 = new Element("jp_sido");
                Element el_sub4 = new Element("jp_gungu");
                Element el_sub5 = new Element("jp_dong");
                Element el_sub6 = new Element("jp_ri");
                Element el_sub7 = new Element("jp_island");
                Element el_sub8 = new Element("jp_san");
                Element el_sub9 = new Element("jp_start_mn");
                Element el_sub10 = new Element("jp_start_sn");
                Element el_sub11 = new Element("jp_end_mn");
                Element el_sub12 = new Element("jp_end_sn");
                Element el_sub13 = new Element("jp_div_nm");
                Element el_sub14 = new Element("jp_div_start");
                Element el_sub15 = new Element("jp_div_end");
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
            }
        }
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getLtnoPostCdDoc2EngAddr() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getLtnoPostCdDoc2EngAddr(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("jibunpost");
        Element el1 = new Element("jp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("jp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("jp_postno");
            Element el_sub2 = new Element("jp_postseq");
            Element el_sub3 = new Element("jp_sido");
            Element el_sub4 = new Element("jp_gungu");
            Element el_sub5 = new Element("jp_dong");
            Element el_sub6 = new Element("jp_ri");
            Element el_sub7 = new Element("jp_island");
            Element el_sub8 = new Element("jp_san");
            Element el_sub9 = new Element("jp_start_mn");
            Element el_sub10 = new Element("jp_start_sn");
            Element el_sub11 = new Element("jp_end_mn");
            Element el_sub12 = new Element("jp_end_sn");
            Element el_sub13 = new Element("jp_div_nm");
            Element el_sub14 = new Element("jp_div_start");
            Element el_sub15 = new Element("jp_div_end");
            Element el_sub16 = new Element("jp_sido_eng");
            Element el_sub17 = new Element("jp_gungu_eng");
            Element el_sub18 = new Element("jp_dong_eng");
            Element el_sub19 = new Element("jp_ri_eng");
            Element el_sub20 = new Element("jp_div_nm_eng");
            
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
        }
        else
        // 정상 처리
        {
            
            Element el2 = new Element("jp_totrecord");
            Element el3 = new Element("jp_strecord");
            Element el4 = new Element("jp_rorecord");
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 우편번호  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 도서 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 산  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시작주번지 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 시작부번지 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 종료주번지 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 종료부번지 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 다량시작동 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 다량종료동 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 영문 시도 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 영문 시군구 : " + arrResult[p][20]);
                 * System.out.println("  ==> * 영문 읍면동 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 영문 리 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 영문 다량 : " + arrResult[p][23]);
                 */
                
                Element el5 = new Element("jp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("jp_postno");
                Element el_sub2 = new Element("jp_postseq");
                Element el_sub3 = new Element("jp_sido");
                Element el_sub4 = new Element("jp_gungu");
                Element el_sub5 = new Element("jp_dong");
                Element el_sub6 = new Element("jp_ri");
                Element el_sub7 = new Element("jp_island");
                Element el_sub8 = new Element("jp_san");
                Element el_sub9 = new Element("jp_start_mn");
                Element el_sub10 = new Element("jp_start_sn");
                Element el_sub11 = new Element("jp_end_mn");
                Element el_sub12 = new Element("jp_end_sn");
                Element el_sub13 = new Element("jp_div_nm");
                Element el_sub14 = new Element("jp_div_start");
                Element el_sub15 = new Element("jp_div_end");
                Element el_sub16 = new Element("jp_sido_eng");
                Element el_sub17 = new Element("jp_gungu_eng");
                Element el_sub18 = new Element("jp_dong_eng");
                Element el_sub19 = new Element("jp_ri_eng");
                Element el_sub20 = new Element("jp_div_nm_eng");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el_sub19.setText(arrResult[p][22]);
                el_sub20.setText(arrResult[p][23]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
            }
        }
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadPostCdDoc2() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadPostCdDoc2(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("roadpost");
        Element el1 = new Element("rp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("rp_postno");
            Element el_sub2 = new Element("rp_postseq");
            Element el_sub3 = new Element("rp_sido");
            Element el_sub4 = new Element("rp_gungu");
            Element el_sub5 = new Element("rp_dong");
            Element el_sub6 = new Element("rp_ri");
            Element el_sub7 = new Element("rp_hdongcd");
            Element el_sub8 = new Element("rp_bdongcd");
            Element el_sub9 = new Element("rp_roadcd");
            Element el_sub10 = new Element("rp_roadnm");
            Element el_sub11 = new Element("rp_undercd");
            Element el_sub12 = new Element("rp_start_buildmn");
            Element el_sub13 = new Element("rp_start_buildsn");
            Element el_sub14 = new Element("rp_end_buildmn");
            Element el_sub15 = new Element("rp_end_buildsn");
            Element el_sub16 = new Element("rp_dlv_nm");
            Element el_sub17 = new Element("rp_hdongnm");
            Element el_sub18 = new Element("rp_bdongnm");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 우편번호  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 행정동코드 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 법정동코드  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 도로코드 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 지하 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 시작주번호 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 시작부번호 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 종료주번호 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 종료부번호 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 행정동 : " + arrResult[p][20]);
                 * System.out.println("  ==> * 법정동 : " + arrResult[p][21]);
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rp_postno");
                Element el_sub2 = new Element("rp_postseq");
                Element el_sub3 = new Element("rp_sido");
                Element el_sub4 = new Element("rp_gungu");
                Element el_sub5 = new Element("rp_dong");
                Element el_sub6 = new Element("rp_ri");
                Element el_sub7 = new Element("rp_hdongcd");
                Element el_sub8 = new Element("rp_bdongcd");
                Element el_sub9 = new Element("rp_roadcd");
                Element el_sub10 = new Element("rp_roadnm");
                Element el_sub11 = new Element("rp_undercd");
                Element el_sub12 = new Element("rp_start_buildmn");
                Element el_sub13 = new Element("rp_start_buildsn");
                Element el_sub14 = new Element("rp_end_buildmn");
                Element el_sub15 = new Element("rp_end_buildsn");
                Element el_sub16 = new Element("rp_dlv_nm");
                Element el_sub17 = new Element("rp_hdongnm");
                Element el_sub18 = new Element("rp_bdongnm");
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadPostCdDoc2EngAddr() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadPostCdDoc2EngAddr(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("roadpost");
        Element el1 = new Element("rp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("rp_postno");
            Element el_sub2 = new Element("rp_postseq");
            Element el_sub3 = new Element("rp_sido");
            Element el_sub4 = new Element("rp_gungu");
            Element el_sub5 = new Element("rp_dong");
            Element el_sub6 = new Element("rp_ri");
            Element el_sub7 = new Element("rp_hdongcd");
            Element el_sub8 = new Element("rp_bdongcd");
            Element el_sub9 = new Element("rp_roadcd");
            Element el_sub10 = new Element("rp_roadnm");
            Element el_sub11 = new Element("rp_undercd");
            Element el_sub12 = new Element("rp_start_buildmn");
            Element el_sub13 = new Element("rp_start_buildsn");
            Element el_sub14 = new Element("rp_end_buildmn");
            Element el_sub15 = new Element("rp_end_buildsn");
            Element el_sub16 = new Element("rp_dlv_nm");
            Element el_sub17 = new Element("rp_hdongnm");
            Element el_sub18 = new Element("rp_bdongnm");
            Element el_sub19 = new Element("rp_roadnm_eng");
            Element el_sub20 = new Element("rp_sido_eng");
            Element el_sub21 = new Element("rp_gungu_eng");
            Element el_sub22 = new Element("rp_dong_eng");
            Element el_sub23 = new Element("rp_dlv_nm_eng");
            
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            el_sub21.setText("");
            el_sub22.setText("");
            el_sub23.setText("");
            
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
            el5.addContent(el_sub23);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 우편번호  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 행정동코드 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 법정동코드  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 도로코드 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 지하 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 시작주번호 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 시작부번호 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 종료주번호 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 종료부번호 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 행정동 : " + arrResult[p][20]);
                 * System.out.println("  ==> * 법정동 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 영문 도로명 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 영문 시도 : " + arrResult[p][23]);
                 * System.out.println("  ==> * 영문 시군구 : " + arrResult[p][24]);
                 * System.out.println("  ==> * 영문 읍면동 : " + arrResult[p][25]);
                 * System.out.println("  ==> * 영문 다량 : " + arrResult[p][26]);
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rp_postno");
                Element el_sub2 = new Element("rp_postseq");
                Element el_sub3 = new Element("rp_sido");
                Element el_sub4 = new Element("rp_gungu");
                Element el_sub5 = new Element("rp_dong");
                Element el_sub6 = new Element("rp_ri");
                Element el_sub7 = new Element("rp_hdongcd");
                Element el_sub8 = new Element("rp_bdongcd");
                Element el_sub9 = new Element("rp_roadcd");
                Element el_sub10 = new Element("rp_roadnm");
                Element el_sub11 = new Element("rp_undercd");
                Element el_sub12 = new Element("rp_start_buildmn");
                Element el_sub13 = new Element("rp_start_buildsn");
                Element el_sub14 = new Element("rp_end_buildmn");
                Element el_sub15 = new Element("rp_end_buildsn");
                Element el_sub16 = new Element("rp_dlv_nm");
                Element el_sub17 = new Element("rp_hdongnm");
                Element el_sub18 = new Element("rp_bdongnm");
                Element el_sub19 = new Element("rp_roadnm_eng");
                Element el_sub20 = new Element("rp_sido_eng");
                Element el_sub21 = new Element("rp_gungu_eng");
                Element el_sub22 = new Element("rp_dong_eng");
                Element el_sub23 = new Element("rp_dlv_nm_eng");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el_sub19.setText(arrResult[p][22]);
                el_sub20.setText(arrResult[p][23]);
                el_sub21.setText(arrResult[p][24]);
                el_sub22.setText(arrResult[p][25]);
                el_sub23.setText(arrResult[p][26]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
                el5.addContent(el_sub23);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadPostCdDoc3() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadPostCdDoc3(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("roadpost");
        Element el1 = new Element("rp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("rp_postno");
            Element el_sub2 = new Element("rp_postseq");
            Element el_sub3 = new Element("rp_sido");
            Element el_sub4 = new Element("rp_gungu");
            Element el_sub5 = new Element("rp_dong");
            Element el_sub6 = new Element("rp_ri");
            Element el_sub7 = new Element("rp_hdongcd");
            Element el_sub8 = new Element("rp_bdongcd");
            Element el_sub9 = new Element("rp_roadcd");
            Element el_sub10 = new Element("rp_roadnm");
            Element el_sub11 = new Element("rp_undercd");
            Element el_sub12 = new Element("rp_start_buildmn");
            Element el_sub13 = new Element("rp_start_buildsn");
            Element el_sub14 = new Element("rp_end_buildmn");
            Element el_sub15 = new Element("rp_end_buildsn");
            Element el_sub16 = new Element("rp_dlv_nm");
            Element el_sub17 = new Element("rp_hdongnm");
            Element el_sub18 = new Element("rp_bdongnm");
            Element el_sub19 = new Element("rp_bsizonno");
            Element el_sub20 = new Element("rp_sancd");
            Element el_sub21 = new Element("rp_start_bunji");
            Element el_sub22 = new Element("rp_start_ho");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            el_sub21.setText("");
            el_sub22.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 기초구역번호  : " + arrResult[p][4]); // 도로명
                 * System.out.println("  ==> * 우편번호 : " + arrResult[p][5]); // 건물본번
                 * System.out.println("  ==> * 우편일련번호  : " + arrResult[p][6]); // 건물부번
                 * System.out.println("  ==> * 광역시/도  : " + arrResult[p][7]); // 건물명
                 * System.out.println("  ==> * 시/군/구 : " + arrResult[p][8]); // 우편번호
                 * System.out.println("  ==> * 읍/면/동 : " + arrResult[p][9]); // 법정동
                 * System.out.println("  ==> * 리 : " + arrResult[p][10]); //
                 * System.out.println("  ==> * 도로명  : " + arrResult[p][11]); // 지번
                 * System.out.println("  ==> * 지하여부 : " + arrResult[p][12]); // 지번-부번
                 * System.out.println("  ==> * 건물주번호 : " + arrResult[p][13]); // 시/도
                 * System.out.println("  ==> * 건물부번호 : " + arrResult[p][14]); // 시군구
                 * System.out.println("  ==> * 건물명 : " + arrResult[p][15]); // 읍면동
                 * System.out.println("  ==> * 도로명코드 : " + arrResult[p][16]); //
                 * System.out.println("  ==> * 읍면일련번호 : " + arrResult[p][17]); // 행정동
                 * System.out.println("  ==> * 산여부 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 주번지 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 부번지 : " + arrResult[p][20]); // 좌표
                 * System.out.println("  ==> * 법정동코드 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 행정동코드 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 법정동명칭 : " + arrResult[p][23]);
                 * System.out.println("  ==> * 행정동명칭 : " + arrResult[p][24]);
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rp_postno");
                Element el_sub2 = new Element("rp_postseq");
                Element el_sub3 = new Element("rp_sido");
                Element el_sub4 = new Element("rp_gungu");
                Element el_sub5 = new Element("rp_dong");
                Element el_sub6 = new Element("rp_ri");
                Element el_sub7 = new Element("rp_hdongcd");
                Element el_sub8 = new Element("rp_bdongcd");
                Element el_sub9 = new Element("rp_roadcd");
                Element el_sub10 = new Element("rp_roadnm");
                Element el_sub11 = new Element("rp_undercd");
                Element el_sub12 = new Element("rp_start_buildmn");
                Element el_sub13 = new Element("rp_start_buildsn");
                Element el_sub14 = new Element("rp_end_buildmn");
                Element el_sub15 = new Element("rp_end_buildsn");
                Element el_sub16 = new Element("rp_dlv_nm");
                Element el_sub17 = new Element("rp_hdongnm");
                Element el_sub18 = new Element("rp_bdongnm");
                Element el_sub19 = new Element("rp_bsizonno");
                Element el_sub20 = new Element("rp_sancd");
                Element el_sub21 = new Element("rp_start_bunji");
                Element el_sub22 = new Element("rp_start_ho");
                
                el_sub1.setText(arrResult[p][5]); // 우편번호
                el_sub2.setText(arrResult[p][6]); // 우편번호 차수
                el_sub3.setText(arrResult[p][7]); // 시/도
                el_sub4.setText(arrResult[p][8]); // 시/군/구
                el_sub5.setText(arrResult[p][9]); // 동
                el_sub6.setText(arrResult[p][10]); // 리
                el_sub7.setText(arrResult[p][21]); // 법정동코드
                el_sub8.setText(arrResult[p][22]); // 행정동코드
                el_sub9.setText(arrResult[p][16]); // 도로명코드
                el_sub10.setText(arrResult[p][11]); // 도로명
                el_sub11.setText(arrResult[p][12]); // 지하여부
                el_sub12.setText(arrResult[p][13]); // 건물시작번호(주)
                el_sub13.setText(arrResult[p][14]); // 건물시작번호(부)
                el_sub14.setText(""); // 건물끝번호(주)
                el_sub15.setText(""); // 건물끝번호(부)
                el_sub16.setText(arrResult[p][15]); // 건물명
                el_sub17.setText(arrResult[p][24]); // 행정동명
                el_sub18.setText(arrResult[p][23]); // 법정동명
                el_sub19.setText(arrResult[p][4]); // 기초구역번호
                el_sub20.setText(arrResult[p][18]); // 산여부
                el_sub21.setText(arrResult[p][19]); // 번지
                el_sub22.setText(arrResult[p][20]); // 호
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadPostCdDoc3EngAddr() -- ver 2.#
     * content : G-VIA 주소 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadPostCdDoc3EngAddr(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("roadpost");
        Element el1 = new Element("rp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("rp_postno");
            Element el_sub2 = new Element("rp_postseq");
            Element el_sub3 = new Element("rp_sido");
            Element el_sub4 = new Element("rp_gungu");
            Element el_sub5 = new Element("rp_dong");
            Element el_sub6 = new Element("rp_ri");
            Element el_sub7 = new Element("rp_hdongcd");
            Element el_sub8 = new Element("rp_bdongcd");
            Element el_sub9 = new Element("rp_roadcd");
            Element el_sub10 = new Element("rp_roadnm");
            Element el_sub11 = new Element("rp_undercd");
            Element el_sub12 = new Element("rp_start_buildmn");
            Element el_sub13 = new Element("rp_start_buildsn");
            Element el_sub14 = new Element("rp_end_buildmn");
            Element el_sub15 = new Element("rp_end_buildsn");
            Element el_sub16 = new Element("rp_dlv_nm");
            Element el_sub17 = new Element("rp_hdongnm");
            Element el_sub18 = new Element("rp_bdongnm");
            Element el_sub19 = new Element("rp_bsizonno");
            Element el_sub20 = new Element("rp_roadnm_eng");
            Element el_sub21 = new Element("rp_sido_eng");
            Element el_sub22 = new Element("rp_gungu_eng");
            Element el_sub23 = new Element("rp_dong_eng");
            Element el_sub24 = new Element("rp_dlv_nm_eng");
            Element el_sub25 = new Element("rp_sancd");
            Element el_sub26 = new Element("rp_start_bunji");
            Element el_sub27 = new Element("rp_start_ho");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            el_sub21.setText("");
            el_sub22.setText("");
            el_sub23.setText("");
            el_sub24.setText("");
            el_sub25.setText("");
            el_sub26.setText("");
            el_sub27.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
            el5.addContent(el_sub23);
            el5.addContent(el_sub24);
            el5.addContent(el_sub25);
            el5.addContent(el_sub26);
            el5.addContent(el_sub27);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 기초구역번호  : " + arrResult[p][4]); // 도로명
                 * System.out.println("  ==> * 우편번호 : " + arrResult[p][5]); // 건물본번
                 * System.out.println("  ==> * 우편일련번호  : " + arrResult[p][6]); // 건물부번
                 * System.out.println("  ==> * 광역시/도  : " + arrResult[p][7]); // 건물명
                 * System.out.println("  ==> * 시/군/구 : " + arrResult[p][8]); // 우편번호
                 * System.out.println("  ==> * 읍/면/동 : " + arrResult[p][9]); // 법정동
                 * System.out.println("  ==> * 리 : " + arrResult[p][10]); //
                 * System.out.println("  ==> * 도로명  : " + arrResult[p][11]); // 지번
                 * System.out.println("  ==> * 지하여부 : " + arrResult[p][12]); // 지번-부번
                 * System.out.println("  ==> * 건물주번호 : " + arrResult[p][13]); // 시/도
                 * System.out.println("  ==> * 건물부번호 : " + arrResult[p][14]); // 시군구
                 * System.out.println("  ==> * 건물명 : " + arrResult[p][15]); // 읍면동
                 * System.out.println("  ==> * 도로명코드 : " + arrResult[p][16]); //
                 * System.out.println("  ==> * 읍면일련번호 : " + arrResult[p][17]); // 행정동
                 * System.out.println("  ==> * 산여부 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 주번지 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 부번지 : " + arrResult[p][20]); // 좌표
                 * System.out.println("  ==> * 법정동코드 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 행정동코드 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 법정동명칭 : " + arrResult[p][23]);
                 * System.out.println("  ==> * 행정동명칭 : " + arrResult[p][24]);
                 * System.out.println("  ==> * 영문 시도 : " + arrResult[p][25]);
                 * System.out.println("  ==> * 영문 시군구 : " + arrResult[p][26]);
                 * System.out.println("  ==> * 영문 읍면동 : " + arrResult[p][27]);
                 * System.out.println("  ==> * 영문 리 : " + arrResult[p][28]);
                 * System.out.println("  ==> * 영문 도로명 : " + arrResult[p][29]);
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rp_postno");
                Element el_sub2 = new Element("rp_postseq");
                Element el_sub3 = new Element("rp_sido");
                Element el_sub4 = new Element("rp_gungu");
                Element el_sub5 = new Element("rp_dong");
                Element el_sub6 = new Element("rp_ri");
                Element el_sub7 = new Element("rp_hdongcd");
                Element el_sub8 = new Element("rp_bdongcd");
                Element el_sub9 = new Element("rp_roadcd");
                Element el_sub10 = new Element("rp_roadnm");
                Element el_sub11 = new Element("rp_undercd");
                Element el_sub12 = new Element("rp_start_buildmn");
                Element el_sub13 = new Element("rp_start_buildsn");
                Element el_sub14 = new Element("rp_end_buildmn");
                Element el_sub15 = new Element("rp_end_buildsn");
                Element el_sub16 = new Element("rp_dlv_nm");
                Element el_sub17 = new Element("rp_hdongnm");
                Element el_sub18 = new Element("rp_bdongnm");
                Element el_sub19 = new Element("rp_bsizonno");
                Element el_sub20 = new Element("rp_roadnm_eng");
                Element el_sub21 = new Element("rp_sido_eng");
                Element el_sub22 = new Element("rp_gungu_eng");
                Element el_sub23 = new Element("rp_dong_eng");
                Element el_sub24 = new Element("rp_dlv_nm_eng");
                Element el_sub25 = new Element("rp_sancd");
                Element el_sub26 = new Element("rp_start_bunji");
                Element el_sub27 = new Element("rp_start_ho");
                
                el_sub1.setText(arrResult[p][5]); // 우편번호
                el_sub2.setText(arrResult[p][6]); // 우편번호 차수
                el_sub3.setText(arrResult[p][7]); // 시/도
                el_sub4.setText(arrResult[p][8]); // 시/군/구
                el_sub5.setText(arrResult[p][9]); // 동
                el_sub6.setText(arrResult[p][10]); // 리
                el_sub7.setText(arrResult[p][21]); // 법정동코드
                el_sub8.setText(arrResult[p][22]); // 행정동코드
                el_sub9.setText(arrResult[p][16]); // 도로명코드
                el_sub10.setText(arrResult[p][11]); // 도로명
                el_sub11.setText(arrResult[p][12]); // 지하여부
                el_sub12.setText(arrResult[p][13]); // 건물시작번호(주)
                el_sub13.setText(arrResult[p][14]); // 건물시작번호(부)
                el_sub14.setText(""); // 건물끝번호(주)
                el_sub15.setText(""); // 건물끝번호(부)
                el_sub16.setText(arrResult[p][15]); // 건물명
                el_sub17.setText(arrResult[p][24]); // 행정동명
                el_sub18.setText(arrResult[p][23]); // 법정동명
                el_sub19.setText(arrResult[p][4]); // 기초구역번호
                el_sub20.setText(arrResult[p][29]);
                el_sub21.setText(arrResult[p][25]);
                el_sub22.setText(arrResult[p][26]);
                el_sub23.setText(arrResult[p][27]);
                el_sub24.setText("");
                el_sub25.setText(arrResult[p][18]); // 산여부
                el_sub26.setText(arrResult[p][19]); // 번지
                el_sub27.setText(arrResult[p][20]); // 호
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
                el5.addContent(el_sub23);
                el5.addContent(el_sub24);
                el5.addContent(el_sub25);
                el5.addContent(el_sub26);
                el5.addContent(el_sub27);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getJibunBsizonnoRangeDoc() -- ver 2.#
     * content : G-VIA 도로명-기초구역번호 매칭 Range 범위 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getJibunBsizonnoRangeDoc(String[][] arrResult, int noutcnt)
    {
        System.out.println("===========================> getJibunBsizonnoRangeDoc()");
        Document doc = new Document();
        Element root = new Element("jibunbsizonno");
        Element el1 = new Element("jb_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("jb_bdongcd");
            Element el_sub2 = new Element("jp_san");
            Element el_sub3 = new Element("jp_start_mn");
            Element el_sub4 = new Element("jp_start_sn");
            Element el_sub5 = new Element("jp_end_mn");
            Element el_sub6 = new Element("jp_end_sn");
            Element el_sub7 = new Element("rb_bsizonno");
            Element el_sub8 = new Element("jp_sido");
            Element el_sub9 = new Element("jp_gungu");
            Element el_sub10 = new Element("jp_dong");
            Element el_sub11 = new Element("jp_ri");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 법정동코드  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 산여부 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 지번시작본번  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 지번시작부번  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 지번끝본번 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 지번끝부번 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 기초구역번호 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 시도 : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시군구 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 읍면 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][14]);
                 * //
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("jb_bdongcd");
                Element el_sub2 = new Element("jp_san");
                Element el_sub3 = new Element("jp_start_mn");
                Element el_sub4 = new Element("jp_start_sn");
                Element el_sub5 = new Element("jp_end_mn");
                Element el_sub6 = new Element("jp_end_sn");
                Element el_sub7 = new Element("rb_bsizonno");
                Element el_sub8 = new Element("jp_sido");
                Element el_sub9 = new Element("jp_gungu");
                Element el_sub10 = new Element("jp_dong");
                Element el_sub11 = new Element("jp_ri");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getJibunBsizonnoRangeDocEngAddr() -- ver 2.#
     * content : G-VIA 도로명-기초구역번호 매칭 Range 범위 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getJibunBsizonnoRangeDocEngAddr(String[][] arrResult, int noutcnt)
    {
        System.out.println("===========================> getJibunBsizonnoRangeDoc()");
        Document doc = new Document();
        Element root = new Element("jibunbsizonno");
        Element el1 = new Element("jb_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("jb_bdongcd");
            Element el_sub2 = new Element("jp_san");
            Element el_sub3 = new Element("jp_start_mn");
            Element el_sub4 = new Element("jp_start_sn");
            Element el_sub5 = new Element("jp_end_mn");
            Element el_sub6 = new Element("jp_end_sn");
            Element el_sub7 = new Element("rb_bsizonno");
            Element el_sub8 = new Element("jp_sido");
            Element el_sub9 = new Element("jp_gungu");
            Element el_sub10 = new Element("jp_dong");
            Element el_sub11 = new Element("jp_ri");
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
        }
        else
        {
            Element el2 = new Element("rp_totrecord");
            Element el3 = new Element("rp_strecord");
            Element el4 = new Element("rp_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 법정동코드  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 산여부 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 지번시작본번  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 지번시작부번  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 지번끝본번 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 지번끝부번 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 기초구역번호 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 시도 : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시군구 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 읍면 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][14]);
                 * //
                 */
                
                Element el5 = new Element("rp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("jb_bdongcd");
                Element el_sub2 = new Element("jp_san");
                Element el_sub3 = new Element("jp_start_mn");
                Element el_sub4 = new Element("jp_start_sn");
                Element el_sub5 = new Element("jp_end_mn");
                Element el_sub6 = new Element("jp_end_sn");
                Element el_sub7 = new Element("rb_bsizonno");
                Element el_sub8 = new Element("jp_sido");
                Element el_sub9 = new Element("jp_gungu");
                Element el_sub10 = new Element("jp_dong");
                Element el_sub11 = new Element("jp_ri");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadBsizonnoRangeDoc() -- ver 2.#
     * content : G-VIA 도로명-기초구역번호 매칭 Range 범위 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadBsizonnoRangeDoc(String[][] arrResult, int noutcnt)
    {
        // System.out.println("===========================> getRoadBsizonnoRangeDoc()");
        Document doc = new Document();
        Element root = new Element("roadbsizonno");
        Element el1 = new Element("rb_resultcd");
        root.addContent(el1);
        
        // System.out.println("arrResult.length:"+arrResult.length);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rb_rep");
            root.addContent(el5);
            Element el_sub1 = new Element("rb_roadcd");
            Element el_sub2 = new Element("rb_undercd");
            Element el_sub3 = new Element("rb_start_buildmn");
            Element el_sub4 = new Element("rb_start_buildsn");
            Element el_sub5 = new Element("rb_end_buildmn");
            Element el_sub6 = new Element("rb_end_buildsn");
            Element el_sub7 = new Element("rb_type");
            Element el_sub8 = new Element("rb_bsizonno");
            Element el_sub9 = new Element("rb_sido");
            Element el_sub10 = new Element("rb_gungu");
            Element el_sub11 = new Element("rb_dong");
            Element el_sub12 = new Element("rb_roadnm");
            
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
        }
        else
        {
            Element el2 = new Element("rb_totrecord");
            Element el3 = new Element("rb_strecord");
            Element el4 = new Element("rb_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 도로명코드  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 지하여부 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시작 건물본번  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시작 건물부번  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 끝 건물본번 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 끝 건물부번 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 타입 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 기초구역번호  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시도 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 시군구 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 읍면 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][15]);
                 * //
                 */
                
                Element el5 = new Element("rb_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rb_roadcd");
                Element el_sub2 = new Element("rb_undercd");
                Element el_sub3 = new Element("rb_start_buildmn");
                Element el_sub4 = new Element("rb_start_buildsn");
                Element el_sub5 = new Element("rb_end_buildmn");
                Element el_sub6 = new Element("rb_end_buildsn");
                Element el_sub7 = new Element("rb_type");
                Element el_sub8 = new Element("rb_bsizonno");
                Element el_sub9 = new Element("rb_sido");
                Element el_sub10 = new Element("rb_gungu");
                Element el_sub11 = new Element("rb_dong");
                Element el_sub12 = new Element("rb_roadnm");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getRoadBsizonnoRangeDocEngAddr() -- ver 2.#
     * content : G-VIA 도로명-기초구역번호 매칭 Range 범위 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getRoadBsizonnoRangeDocEngAddr(String[][] arrResult, int noutcnt)
    {
        // System.out.println("===========================> getRoadBsizonnoRangeDoc()");
        Document doc = new Document();
        Element root = new Element("roadbsizonno");
        Element el1 = new Element("rb_resultcd");
        root.addContent(el1);
        
        // System.out.println("arrResult.length:"+arrResult.length);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("rb_rep");
            root.addContent(el5);
            Element el_sub1 = new Element("rb_roadcd");
            Element el_sub2 = new Element("rb_undercd");
            Element el_sub3 = new Element("rb_start_buildmn");
            Element el_sub4 = new Element("rb_start_buildsn");
            Element el_sub5 = new Element("rb_end_buildmn");
            Element el_sub6 = new Element("rb_end_buildsn");
            Element el_sub7 = new Element("rb_type");
            Element el_sub8 = new Element("rb_bsizonno");
            Element el_sub9 = new Element("rb_sido");
            Element el_sub10 = new Element("rb_gungu");
            Element el_sub11 = new Element("rb_dong");
            Element el_sub12 = new Element("rb_roadnm");
            
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
        }
        else
        {
            Element el2 = new Element("rb_totrecord");
            Element el3 = new Element("rb_strecord");
            Element el4 = new Element("rb_rorecord");
            
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 도로명코드  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 지하여부 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 시작 건물본번  : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시작 건물부번  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 끝 건물본번 : " + arrResult[p][8]);
                 * System.out.println("  ==> * 끝 건물부번 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 타입 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 기초구역번호  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 시도 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 시군구 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 읍면 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][15]);
                 * //
                 */
                
                Element el5 = new Element("rb_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("rb_roadcd");
                Element el_sub2 = new Element("rb_undercd");
                Element el_sub3 = new Element("rb_start_buildmn");
                Element el_sub4 = new Element("rb_start_buildsn");
                Element el_sub5 = new Element("rb_end_buildmn");
                Element el_sub6 = new Element("rb_end_buildsn");
                Element el_sub7 = new Element("rb_type");
                Element el_sub8 = new Element("rb_bsizonno");
                Element el_sub9 = new Element("rb_sido");
                Element el_sub10 = new Element("rb_gungu");
                Element el_sub11 = new Element("rb_dong");
                Element el_sub12 = new Element("rb_roadnm");
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getTotalPostCdDoc() -- ver 2.#
     * content : G-VIA 종합우편번호 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getTotalPostCdDoc(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("totalpost");
        Element el1 = new Element("tp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("tp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("tp_gubun");
            Element el_sub2 = new Element("tp_postno");
            Element el_sub3 = new Element("tp_postseq");
            Element el_sub4 = new Element("tp_sido");
            Element el_sub5 = new Element("tp_gungu");
            Element el_sub6 = new Element("tp_dong");
            Element el_sub7 = new Element("tp_ri");
            Element el_sub8 = new Element("tp_island");
            Element el_sub9 = new Element("tp_hdongcd"); // 행정동 코드
            Element el_sub10 = new Element("tp_bdongcd"); // 법 코드
            Element el_sub11 = new Element("tp_roadcd"); // 도로코드
            Element el_sub12 = new Element("tp_roadnm"); // 도로명
            Element el_sub13 = new Element("tp_san_undercd");
            Element el_sub14 = new Element("tp_start_mn");
            Element el_sub15 = new Element("tp_start_sn");
            Element el_sub16 = new Element("tp_end_mn");
            Element el_sub17 = new Element("tp_end_sn");
            Element el_sub18 = new Element("tp_div_nm");
            Element el_sub19 = new Element("tp_div_start");
            Element el_sub20 = new Element("tp_div_end");
            Element el_sub21 = new Element("tp_hdongnm"); // 행명
            Element el_sub22 = new Element("tp_bdongnm"); // 법명
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            el_sub21.setText("");
            el_sub22.setText("");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
        }
        else
        // 정상 처리
        {
            
            Element el2 = new Element("tp_totrecord");
            Element el3 = new Element("tp_strecord");
            Element el4 = new Element("tp_outrecord");
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 주소구분  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][8]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 도서 : " + arrResult[p][11]);
                 * System.out.println("  ==> * 행정동 코드 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 법정동 코드 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 도로코드 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 산  or 지하 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 시작주번지 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 시작부번지 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 종료주번지 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 종료부번지 : " + arrResult[p][20]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 다량시작동 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 다량종료동 : " + arrResult[p][23]);
                 * System.out.println("  ==> * 행정동명칭 : " + arrResult[p][24]);
                 * System.out.println("  ==> * 법정동명칭 : " + arrResult[p][25]);
                 * //
                 */
                
                Element el5 = new Element("tp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("tp_gubun");
                Element el_sub2 = new Element("tp_postno");
                Element el_sub3 = new Element("tp_postseq");
                Element el_sub4 = new Element("tp_sido");
                Element el_sub5 = new Element("tp_gungu");
                Element el_sub6 = new Element("tp_dong");
                Element el_sub7 = new Element("tp_ri");
                Element el_sub8 = new Element("tp_island");
                Element el_sub9 = new Element("tp_hdongcd"); // 행정동 코드
                Element el_sub10 = new Element("tp_bdongcd"); // 법 코드
                Element el_sub11 = new Element("tp_roadcd"); // 도로코드
                Element el_sub12 = new Element("tp_roadnm"); // 도로명
                Element el_sub13 = new Element("tp_san_undercd");
                Element el_sub14 = new Element("tp_start_mn");
                Element el_sub15 = new Element("tp_start_sn");
                Element el_sub16 = new Element("tp_end_mn");
                Element el_sub17 = new Element("tp_end_sn");
                Element el_sub18 = new Element("tp_div_nm");
                Element el_sub19 = new Element("tp_div_start");
                Element el_sub20 = new Element("tp_div_end");
                Element el_sub21 = new Element("tp_hdongnm"); // 행명
                Element el_sub22 = new Element("tp_bdongnm"); // 법명
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el_sub19.setText(arrResult[p][22]);
                el_sub20.setText(arrResult[p][23]);
                el_sub21.setText(arrResult[p][24]);
                el_sub22.setText(arrResult[p][25]);
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
            }
        }
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getTotalPostCdDocEngAddr() -- ver 2.#
     * content : G-VIA 종합우편번호 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getTotalPostCdDocEngAddr(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("totalpost");
        Element el1 = new Element("tp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            el1.setText("0");
            
            Element el5 = new Element("tp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("tp_gubun");
            Element el_sub2 = new Element("tp_postno");
            Element el_sub3 = new Element("tp_postseq");
            Element el_sub4 = new Element("tp_sido");
            Element el_sub5 = new Element("tp_gungu");
            Element el_sub6 = new Element("tp_dong");
            Element el_sub7 = new Element("tp_ri");
            Element el_sub8 = new Element("tp_island");
            Element el_sub9 = new Element("tp_hdongcd"); // 행정동 코드
            Element el_sub10 = new Element("tp_bdongcd"); // 법 코드
            Element el_sub11 = new Element("tp_roadcd"); // 도로코드
            Element el_sub12 = new Element("tp_roadnm"); // 도로명
            Element el_sub13 = new Element("tp_san_undercd");
            Element el_sub14 = new Element("tp_start_mn");
            Element el_sub15 = new Element("tp_start_sn");
            Element el_sub16 = new Element("tp_end_mn");
            Element el_sub17 = new Element("tp_end_sn");
            Element el_sub18 = new Element("tp_div_nm");
            Element el_sub19 = new Element("tp_div_start");
            Element el_sub20 = new Element("tp_div_end");
            Element el_sub21 = new Element("tp_hdongnm"); // 행명
            Element el_sub22 = new Element("tp_bdongnm"); // 법명
            Element el_sub23 = new Element("tp_sido_eng"); // 영문 시도명
            Element el_sub24 = new Element("tp_gungu_eng"); // 영문 시군구명
            Element el_sub25 = new Element("tp_dong_eng"); // 영문 읍면동명
            Element el_sub26 = new Element("tp_ri_roadnm_eng"); // 영문 법정리(지번) or 도로명(도로명)
            Element el_sub27 = new Element("tp_div_nm_eng"); // 영문 다량배달처 or 시군구건물명
            
            el_sub1.setText("");
            el_sub2.setText("");
            el_sub3.setText("");
            el_sub4.setText("");
            el_sub5.setText("");
            el_sub6.setText("");
            el_sub7.setText("");
            el_sub8.setText("");
            el_sub9.setText("");
            el_sub10.setText("");
            el_sub11.setText("");
            el_sub12.setText("");
            el_sub13.setText("");
            el_sub14.setText("");
            el_sub15.setText("");
            el_sub16.setText("");
            el_sub17.setText("");
            el_sub18.setText("");
            el_sub19.setText("");
            el_sub20.setText("");
            el_sub21.setText("");
            el_sub22.setText("");
            el_sub23.setText("");
            el_sub24.setText("");
            el_sub25.setText("");
            el_sub26.setText("");
            el_sub27.setText("");
            
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
            el5.addContent(el_sub23);
            el5.addContent(el_sub24);
            el5.addContent(el_sub25);
            el5.addContent(el_sub26);
            el5.addContent(el_sub27);
        }
        else
        // 정상 처리
        {
            
            Element el2 = new Element("tp_totrecord");
            Element el3 = new Element("tp_strecord");
            Element el4 = new Element("tp_outrecord");
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 시작 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 반복 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 주소구분  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 우편번호 : " + arrResult[p][5]);
                 * System.out.println("  ==> * 우편번호 일련번호 : " + arrResult[p][6]);
                 * System.out.println("  ==> * 시도  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 시군구  : " + arrResult[p][8]);
                 * System.out.println("  ==> * 읍면동 : " + arrResult[p][9]);
                 * System.out.println("  ==> * 리 : " + arrResult[p][10]);
                 * System.out.println("  ==> * 도서 : " + arrResult[p][11]);
                 * System.out.println("  ==> * 행정동 코드 : " + arrResult[p][12]);
                 * System.out.println("  ==> * 법정동 코드 : " + arrResult[p][13]);
                 * System.out.println("  ==> * 도로코드 : " + arrResult[p][14]);
                 * System.out.println("  ==> * 도로명 : " + arrResult[p][15]);
                 * System.out.println("  ==> * 산  or 지하 : " + arrResult[p][16]);
                 * System.out.println("  ==> * 시작주번지 : " + arrResult[p][17]);
                 * System.out.println("  ==> * 시작부번지 : " + arrResult[p][18]);
                 * System.out.println("  ==> * 종료주번지 : " + arrResult[p][19]);
                 * System.out.println("  ==> * 종료부번지 : " + arrResult[p][20]);
                 * System.out.println("  ==> * 다량 : " + arrResult[p][21]);
                 * System.out.println("  ==> * 다량시작동 : " + arrResult[p][22]);
                 * System.out.println("  ==> * 다량종료동 : " + arrResult[p][23]);
                 * System.out.println("  ==> * 행정동명칭 : " + arrResult[p][24]);
                 * System.out.println("  ==> * 법정동명칭 : " + arrResult[p][25]);
                 * //지번과 도로명 영문 순서가 다름(참고)
                 * System.out.println("  ==> * 영문 시도명 : " + arrResult[p][26]);
                 * System.out.println("  ==> * 영문 시군구명 : " + arrResult[p][27]);
                 * System.out.println("  ==> * 영문 읍면동명 : " + arrResult[p][28]);
                 * System.out.println("  ==> * 영문 법정리(지번) or 도로명(도로명) : " + arrResult[p][29]);
                 * System.out.println("  ==> * 영문 다량배달처 or 시군구건물명 : " + arrResult[p][30]);
                 * //
                 */
                
                Element el5 = new Element("tp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("tp_gubun");
                Element el_sub2 = new Element("tp_postno");
                Element el_sub3 = new Element("tp_postseq");
                Element el_sub4 = new Element("tp_sido");
                Element el_sub5 = new Element("tp_gungu");
                Element el_sub6 = new Element("tp_dong");
                Element el_sub7 = new Element("tp_ri");
                Element el_sub8 = new Element("tp_island");
                Element el_sub9 = new Element("tp_hdongcd"); // 행정동 코드
                Element el_sub10 = new Element("tp_bdongcd"); // 법 코드
                Element el_sub11 = new Element("tp_roadcd"); // 도로코드
                Element el_sub12 = new Element("tp_roadnm"); // 도로명
                Element el_sub13 = new Element("tp_san_undercd");
                Element el_sub14 = new Element("tp_start_mn");
                Element el_sub15 = new Element("tp_start_sn");
                Element el_sub16 = new Element("tp_end_mn");
                Element el_sub17 = new Element("tp_end_sn");
                Element el_sub18 = new Element("tp_div_nm");
                Element el_sub19 = new Element("tp_div_start");
                Element el_sub20 = new Element("tp_div_end");
                Element el_sub21 = new Element("tp_hdongnm"); // 행명
                Element el_sub22 = new Element("tp_bdongnm"); // 법명
                Element el_sub23 = new Element("tp_sido_eng"); // 영문 시도명
                Element el_sub24 = new Element("tp_gungu_eng"); // 영문 시군구명
                Element el_sub25 = new Element("tp_dong_eng"); // 영문 읍면동명
                Element el_sub26 = new Element("tp_ri_roadnm_eng"); // 영문 법정리(지번) or 도로명(도로명)
                Element el_sub27 = new Element("tp_div_nm_eng"); // 영문 다량배달처 or 시군구건물명
                
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el_sub19.setText(arrResult[p][22]);
                el_sub20.setText(arrResult[p][23]);
                el_sub21.setText(arrResult[p][24]);
                el_sub22.setText(arrResult[p][25]);
                el_sub23.setText(arrResult[p][26]);
                el_sub24.setText(arrResult[p][27]);
                el_sub25.setText(arrResult[p][28]);
                el_sub26.setText(arrResult[p][29]);
                el_sub27.setText(arrResult[p][30]);
                
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
                el5.addContent(el_sub23);
                el5.addContent(el_sub24);
                el5.addContent(el_sub25);
                el5.addContent(el_sub26);
                el5.addContent(el_sub27);
            }
        }
        doc.setRootElement(root);
        
        return doc;
    }
    
    /*
     * function : getTotalPostCdDocRecordNum() -- ver 2.#
     * content : G-VIA 종합우편번호 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-VIA 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     */
    public Document getTotalPostCdDocRecordNum(String[][] arrResult, int noutcnt)
    {
        Document doc = new Document();
        Element root = new Element("totalpost");
        Element el1 = new Element("tp_resultcd");
        root.addContent(el1);
        
        if (arrResult.length == 1 && arrResult[0][0].equals("0")) // 에러
        {
            
            el1.setText("0");
            
            Element el5 = new Element("tp_rep");
            root.addContent(el5);
            
            Element el_sub1 = new Element("tp_gubun");
            Element el_sub2 = new Element("tp_rcdno_se");
            Element el_sub3 = new Element("tp_rcdcnt_se");
            Element el_sub4 = new Element("tp_rcdno_bs");
            Element el_sub5 = new Element("tp_rcdcnt_bs");
            Element el_sub6 = new Element("tp_rcdno_dg");
            Element el_sub7 = new Element("tp_rcdcnt_dg");
            Element el_sub8 = new Element("tp_rcdno_ic");
            Element el_sub9 = new Element("tp_rcdcnt_ic");
            Element el_sub10 = new Element("tp_rcdno_gj");
            Element el_sub11 = new Element("tp_rcdcnt_gj");
            Element el_sub12 = new Element("tp_rcdno_dj");
            Element el_sub13 = new Element("tp_rcdcnt_dj");
            Element el_sub14 = new Element("tp_rcdno_us");
            Element el_sub15 = new Element("tp_rcdcnt_us");
            Element el_sub16 = new Element("tp_rcdno_sj");
            Element el_sub17 = new Element("tp_rcdcnt_sj");
            Element el_sub18 = new Element("tp_rcdno_gg");
            Element el_sub19 = new Element("tp_rcdcnt_gg");
            Element el_sub20 = new Element("tp_rcdno_gw");
            Element el_sub21 = new Element("tp_rcdcnt_gw");
            Element el_sub22 = new Element("tp_rcdno_cb");
            Element el_sub23 = new Element("tp_rcdcnt_cb");
            Element el_sub24 = new Element("tp_rcdno_cn");
            Element el_sub25 = new Element("tp_rcdcnt_cn");
            Element el_sub26 = new Element("tp_rcdno_jb");
            Element el_sub27 = new Element("tp_rcdcnt_jb");
            Element el_sub28 = new Element("tp_rcdno_jn");
            Element el_sub29 = new Element("tp_rcdcnt_jn");
            Element el_sub30 = new Element("tp_rcdno_gb");
            Element el_sub31 = new Element("tp_rcdcnt_gb");
            Element el_sub32 = new Element("tp_rcdno_gn");
            Element el_sub33 = new Element("tp_rcdcnt_gn");
            Element el_sub34 = new Element("tp_rcdno_jj");
            Element el_sub35 = new Element("tp_rcdcnt_jj");
            el_sub1.setText("");
            el_sub2.setText("0");
            el_sub3.setText("0");
            el_sub4.setText("0");
            el_sub5.setText("0");
            el_sub6.setText("0");
            el_sub7.setText("0");
            el_sub8.setText("0");
            el_sub9.setText("0");
            el_sub10.setText("0");
            el_sub11.setText("0");
            el_sub12.setText("0");
            el_sub13.setText("0");
            el_sub14.setText("0");
            el_sub15.setText("0");
            el_sub16.setText("0");
            el_sub17.setText("0");
            el_sub18.setText("0");
            el_sub19.setText("0");
            el_sub20.setText("0");
            el_sub21.setText("0");
            el_sub22.setText("0");
            el_sub23.setText("0");
            el_sub24.setText("0");
            el_sub25.setText("0");
            el_sub26.setText("0");
            el_sub27.setText("0");
            el_sub28.setText("0");
            el_sub29.setText("0");
            el_sub30.setText("0");
            el_sub31.setText("0");
            el_sub32.setText("0");
            el_sub33.setText("0");
            el_sub34.setText("0");
            el_sub35.setText("0");
            el5.addContent(el_sub1);
            el5.addContent(el_sub2);
            el5.addContent(el_sub3);
            el5.addContent(el_sub4);
            el5.addContent(el_sub5);
            el5.addContent(el_sub6);
            el5.addContent(el_sub7);
            el5.addContent(el_sub8);
            el5.addContent(el_sub9);
            el5.addContent(el_sub10);
            el5.addContent(el_sub11);
            el5.addContent(el_sub12);
            el5.addContent(el_sub13);
            el5.addContent(el_sub14);
            el5.addContent(el_sub15);
            el5.addContent(el_sub16);
            el5.addContent(el_sub17);
            el5.addContent(el_sub18);
            el5.addContent(el_sub19);
            el5.addContent(el_sub20);
            el5.addContent(el_sub21);
            el5.addContent(el_sub22);
            el5.addContent(el_sub23);
            el5.addContent(el_sub24);
            el5.addContent(el_sub25);
            el5.addContent(el_sub26);
            el5.addContent(el_sub27);
            el5.addContent(el_sub28);
            el5.addContent(el_sub29);
            el5.addContent(el_sub30);
            el5.addContent(el_sub31);
            el5.addContent(el_sub32);
            el5.addContent(el_sub33);
            el5.addContent(el_sub34);
            el5.addContent(el_sub35);
        }
        else
        // 정상 처리
        {
            
            Element el2 = new Element("tp_totrecord");
            Element el3 = new Element("tp_jirecord");
            Element el4 = new Element("tp_rorecord");
            root.addContent(el2);
            root.addContent(el3);
            root.addContent(el4);
            el1.setText(arrResult[0][0]);
            el2.setText(arrResult[0][1]);
            el3.setText(arrResult[0][2]);
            el4.setText(arrResult[0][3]);
            
            if (noutcnt > arrResult.length)
            {
                noutcnt = arrResult.length;
            }
            
            for (int p = 0; p < noutcnt; p++)
            {
                /*
                 * System.out.println("  ==> *-------------------------------------------------------------------------------------------------");
                 * System.out.println("  ==> * 결과  : " + arrResult[p][0]);
                 * System.out.println("  ==> * 전체 레코드  : " + arrResult[p][1]);
                 * System.out.println("  ==> * 지번 레코드  : " + arrResult[p][2]);
                 * System.out.println("  ==> * 도로명 레코드  : " + arrResult[p][3]);
                 * System.out.println("-----------------------------------------------");
                 * System.out.println("  ==> * 주소구분  : " + arrResult[p][4]);
                 * System.out.println("  ==> * 레코드번호(서울)  : " + arrResult[p][5]);
                 * System.out.println("  ==> * 레코드건수(서울) : " + arrResult[p][6]);
                 * System.out.println("  ==> * 레코드번호(부산)  : " + arrResult[p][7]);
                 * System.out.println("  ==> * 레코드건수(부산) : " + arrResult[p][8]);
                 * System.out.println("  ==> * 레코드번호(대구)  : " + arrResult[p][9]);
                 * System.out.println("  ==> * 레코드건수(대구) : " + arrResult[p][10]);
                 * System.out.println("  ==> * 레코드번호(인천)  : " + arrResult[p][11]);
                 * System.out.println("  ==> * 레코드건수(인천) : " + arrResult[p][12]);
                 * System.out.println("  ==> * 레코드번호(광주)  : " + arrResult[p][13]);
                 * System.out.println("  ==> * 레코드건수(광주) : " + arrResult[p][14]);
                 * System.out.println("  ==> * 레코드번호(대전)  : " + arrResult[p][15]);
                 * System.out.println("  ==> * 레코드건수(대전) : " + arrResult[p][16]);
                 * System.out.println("  ==> * 레코드번호(울산)  : " + arrResult[p][17]);
                 * System.out.println("  ==> * 레코드건수(울산) : " + arrResult[p][18]);
                 * System.out.println("  ==> * 레코드번호(세종)  : " + arrResult[p][19]);
                 * System.out.println("  ==> * 레코드건수(세종) : " + arrResult[p][20]);
                 * System.out.println("  ==> * 레코드번호(경기)  : " + arrResult[p][21]);
                 * System.out.println("  ==> * 레코드건수(경기) : " + arrResult[p][22]);
                 * System.out.println("  ==> * 레코드번호(강원)  : " + arrResult[p][23]);
                 * System.out.println("  ==> * 레코드건수(강원) : " + arrResult[p][24]);
                 * System.out.println("  ==> * 레코드번호(충북)  : " + arrResult[p][25]);
                 * System.out.println("  ==> * 레코드건수(충북) : " + arrResult[p][26]);
                 * System.out.println("  ==> * 레코드번호(충남)  : " + arrResult[p][27]);
                 * System.out.println("  ==> * 레코드건수(충남) : " + arrResult[p][28]);
                 * System.out.println("  ==> * 레코드번호(전북)  : " + arrResult[p][29]);
                 * System.out.println("  ==> * 레코드건수(전북) : " + arrResult[p][30]);
                 * System.out.println("  ==> * 레코드번호(전남)  : " + arrResult[p][31]);
                 * System.out.println("  ==> * 레코드건수(전남) : " + arrResult[p][32]);
                 * System.out.println("  ==> * 레코드번호(경북)  : " + arrResult[p][33]);
                 * System.out.println("  ==> * 레코드건수(경북) : " + arrResult[p][34]);
                 * System.out.println("  ==> * 레코드번호(경남)  : " + arrResult[p][35]);
                 * System.out.println("  ==> * 레코드건수(경남) : " + arrResult[p][36]);
                 * System.out.println("  ==> * 레코드번호(제주)  : " + arrResult[p][37]);
                 * System.out.println("  ==> * 레코드건수(제주) : " + arrResult[p][38]);
                 * //
                 */
                
                Element el5 = new Element("tp_rep");
                root.addContent(el5);
                
                Element el_sub1 = new Element("tp_gubun"); // 주소구분
                Element el_sub2 = new Element("tp_rcdno_se"); // 레코드시작번호(서울)
                Element el_sub3 = new Element("tp_rcdcnt_se"); // 레코드건수(서울)
                Element el_sub4 = new Element("tp_rcdno_bs");
                Element el_sub5 = new Element("tp_rcdcnt_bs");
                Element el_sub6 = new Element("tp_rcdno_dg");
                Element el_sub7 = new Element("tp_rcdcnt_dg");
                Element el_sub8 = new Element("tp_rcdno_ic");
                Element el_sub9 = new Element("tp_rcdcnt_ic");
                Element el_sub10 = new Element("tp_rcdno_gj");
                Element el_sub11 = new Element("tp_rcdcnt_gj");
                Element el_sub12 = new Element("tp_rcdno_dj");
                Element el_sub13 = new Element("tp_rcdcnt_dj");
                Element el_sub14 = new Element("tp_rcdno_us");
                Element el_sub15 = new Element("tp_rcdcnt_us");
                Element el_sub16 = new Element("tp_rcdno_sj");
                Element el_sub17 = new Element("tp_rcdcnt_sj");
                Element el_sub18 = new Element("tp_rcdno_gg");
                Element el_sub19 = new Element("tp_rcdcnt_gg");
                Element el_sub20 = new Element("tp_rcdno_gw");
                Element el_sub21 = new Element("tp_rcdcnt_gw");
                Element el_sub22 = new Element("tp_rcdno_cb");
                Element el_sub23 = new Element("tp_rcdcnt_cb");
                Element el_sub24 = new Element("tp_rcdno_cn");
                Element el_sub25 = new Element("tp_rcdcnt_cn");
                Element el_sub26 = new Element("tp_rcdno_jb");
                Element el_sub27 = new Element("tp_rcdcnt_jb");
                Element el_sub28 = new Element("tp_rcdno_jn");
                Element el_sub29 = new Element("tp_rcdcnt_jn");
                Element el_sub30 = new Element("tp_rcdno_gb");
                Element el_sub31 = new Element("tp_rcdcnt_gb");
                Element el_sub32 = new Element("tp_rcdno_gn");
                Element el_sub33 = new Element("tp_rcdcnt_gn");
                Element el_sub34 = new Element("tp_rcdno_jj");
                Element el_sub35 = new Element("tp_rcdcnt_jj");
                el_sub1.setText(arrResult[p][4]);
                el_sub2.setText(arrResult[p][5]);
                el_sub3.setText(arrResult[p][6]);
                el_sub4.setText(arrResult[p][7]);
                el_sub5.setText(arrResult[p][8]);
                el_sub6.setText(arrResult[p][9]);
                el_sub7.setText(arrResult[p][10]);
                el_sub8.setText(arrResult[p][11]);
                el_sub9.setText(arrResult[p][12]);
                el_sub10.setText(arrResult[p][13]);
                el_sub11.setText(arrResult[p][14]);
                el_sub12.setText(arrResult[p][15]);
                el_sub13.setText(arrResult[p][16]);
                el_sub14.setText(arrResult[p][17]);
                el_sub15.setText(arrResult[p][18]);
                el_sub16.setText(arrResult[p][19]);
                el_sub17.setText(arrResult[p][20]);
                el_sub18.setText(arrResult[p][21]);
                el_sub19.setText(arrResult[p][22]);
                el_sub20.setText(arrResult[p][23]);
                el_sub21.setText(arrResult[p][24]);
                el_sub22.setText(arrResult[p][25]);
                el_sub23.setText(arrResult[p][26]);
                el_sub24.setText(arrResult[p][27]);
                el_sub25.setText(arrResult[p][28]);
                el_sub26.setText(arrResult[p][29]);
                el_sub27.setText(arrResult[p][30]);
                el_sub28.setText(arrResult[p][31]);
                el_sub29.setText(arrResult[p][32]);
                el_sub30.setText(arrResult[p][33]);
                el_sub31.setText(arrResult[p][34]);
                el_sub32.setText(arrResult[p][35]);
                el_sub33.setText(arrResult[p][36]);
                el_sub34.setText(arrResult[p][37]);
                el_sub35.setText(arrResult[p][38]);
                el5.addContent(el_sub1);
                el5.addContent(el_sub2);
                el5.addContent(el_sub3);
                el5.addContent(el_sub4);
                el5.addContent(el_sub5);
                el5.addContent(el_sub6);
                el5.addContent(el_sub7);
                el5.addContent(el_sub8);
                el5.addContent(el_sub9);
                el5.addContent(el_sub10);
                el5.addContent(el_sub11);
                el5.addContent(el_sub12);
                el5.addContent(el_sub13);
                el5.addContent(el_sub14);
                el5.addContent(el_sub15);
                el5.addContent(el_sub16);
                el5.addContent(el_sub17);
                el5.addContent(el_sub18);
                el5.addContent(el_sub19);
                el5.addContent(el_sub20);
                el5.addContent(el_sub21);
                el5.addContent(el_sub22);
                el5.addContent(el_sub23);
                el5.addContent(el_sub24);
                el5.addContent(el_sub25);
                el5.addContent(el_sub26);
                el5.addContent(el_sub27);
                el5.addContent(el_sub28);
                el5.addContent(el_sub29);
                el5.addContent(el_sub30);
                el5.addContent(el_sub31);
                el5.addContent(el_sub32);
                el5.addContent(el_sub33);
                el5.addContent(el_sub34);
                el5.addContent(el_sub35);
            }
        }
        
        doc.setRootElement(root);
        
        return doc;
    }
    
    /******************** 공통 유틸 ********************/
    /*
     * function : getErrDoc()
     * content : 오류 결과를 Document 형식으로 생성한다
     * value 1.String [][] arrResult : G-Point 결과
     * 2.int noutcnt : 출력길이
     * return : Document
     * ----------------------------------
     * errGb 00001 : 파라미터 오류, 프로퍼티 오류
     * 00002 : 실행중 예외처리 에러
     */
    public Document getErrDoc(String errGb, String errCd)
    {
        Document doc = new Document();
        
        Element root = new Element("resultfail");
        Element el1 = new Element("err_resultcd");
        Element el2 = new Element("msg");
        root.addContent(el1);
        root.addContent(el2);
        el1.setText(errGb);
        el2.setText("Fail : " + errCd);
        doc.setRootElement(root);
        
        return doc;
    }
    
}
