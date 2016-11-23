/**
 * @FileName    : GviaAddressVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 3. 9. 
 * @Author      : 유창기 
 * @Description : 주소 정제/전환 결과
 * @변경이력     :
 */

package gcp.external.kainess.vo;

public class GviaAddressVo
{
    
    private String ad_jr = ""; // 지번/새주소구분(입력값기준, J:지번주소, R:도로명주소)
    private String ad_resultflag = ""; // FLAG1 (0:정상, 1:불능, 2:매칭실패)
    private String ad_resultcd = ""; // FLAG2 (변환코드), G-VIA_InOut Protocol.doc 참조
    
    private String ad_postno = ""; // 지번주소 우편번호
    private String ad_postseq = ""; // 지번주소 우편번호일련번호
    private String ad_bsizonno = ""; // 지번주소 국가기초구역번호
    private String ad_add1 = ""; // 지번주소 - 기본주소(행정구역)
    private String ad_add2 = ""; // 지번주소 - 상세주소
    private String ad_ro_postno = ""; // 도로명주소 우편번호
    private String ad_ro_postseq = ""; // 도로명주소 우편번호일련번호
    private String ad_ro_bsizonno = ""; // 도로명주소 국가기초구역번호
    private String ad_ro_add1 = ""; // 도로명주소 - 기본주소(건물번호까지)
    private String ad_ro_add2 = ""; // 도로명주소 - 상세주소
    private String ad_ro_ref = ""; // 도로명주소 - 참조주소(법정동, 건물명)
    
    private String ad_ro_ref2 = ""; // 참조주소2(법정동/리, 건물명)
    private String ad_ro_ref3 = ""; // 참조주소3(법정동/리 산 번지-호)
    private String ad_ro_ref4 = ""; // 참조주소4(법정동/리 산 번지-호 건물명)
    
    private String ad_sido = ""; // 광역시/도명칭
    private String ad_gungu = ""; // 시/군/구명칭
    private String ad_dong = ""; // 읍/면/동명칭
    private String ad_ri = ""; // 리명칭
    private String ad_sancd = ""; // 산구분(0:평지, 1:산)
    private String ad_san = ""; // 산여부
    private String ad_mn = ""; // 주번지
    private String ad_sn = ""; // 부번지
    private String ad_buildinfo = ""; // 분석된 건물명
    private String ad_builddong = ""; // 건물동
    private String ad_buildho = ""; // 건물호
    private String ad_buildstair = ""; // 건물 층
    private String ad_remain = ""; // 건물추정정보
    private String ad_dongcd = ""; // 입력동구분
    private String ad_bdongcd = ""; // 법정동코드
    private String ad_hdongcd = ""; // 행정동코드
    private String ad_bdongnm = ""; // 법정동명칭
    private String ad_hdongnm = ""; // 행정동명칭
    private String ad_pnucd = ""; // 지번PNU
    private String ad_cx = ""; // X좌표
    private String ad_cy = ""; // Y좌표
    private String ad_lev = ""; // 좌표 부여 레벨
    private String ad_ro_roadnm = ""; // 도로명
    private String ad_ro_roadcd = ""; // 도로코드
    private String ad_ro_dongseq = ""; // 읍면동 일련번호
    private String ad_ro_undercd = ""; // 지하구분(0:지상, 1:지하, 2:공중)
    private String ad_ro_under = ""; // 지하여부
    private String ad_ro_buildmn = ""; // 건물주번호
    private String ad_ro_buildsn = ""; // 건물부번호
    private String ad_ro_buildnm = ""; // 도로명 빌딩명칭
    private String ad_ro_builddtail = ""; // 도로명 빌딩 상세
    private String ad_ro_buildmgno = ""; // 건물관리번호
    private String ad_ro_pnu = ""; // 도로명주소 PNU
    private String ad_ro_cx = ""; // 도로명주소 X좌표
    private String ad_ro_cy = ""; // 도로명주소 Y좌표
    private String ad_reprematching = ""; // 안행부 배포 - 대표매칭여부 Y N
    private String ad_apathouse = ""; // 공동주택여부 Y N
    private String ad_annexbuild = ""; // 부속건물명
    private String ad_annexinfo = ""; // 부속건물 정보
    private String ad_addrlev = ""; // 주소파싱레벨
    private String ad_postlev = ""; // 우편번호 부여레벨
    private String ad_posttype = ""; // 우편번호 사용 동타입
    private String ad_errcd = ""; // 좌표부여 에러코드
    private String ad_datachgcd = ""; // 정보변경코드
    private String ad_newpostlev = ""; // 기초구역번호 부여 레벨
    private String ad_ro_totaladdcnt = ""; // 주소 설정값(총 결과 수량)
    private String ad_ro_repeatcnt = ""; // 주소 설정값(멀티매칭수)
    
    private String ad_eng_sido = ""; // 영문 시도명
    private String ad_eng_gungu = ""; // 영문 시군구명
    private String ad_eng_dong = ""; // 영문 읍면동명
    private String ad_eng_ri = ""; // 영문 법정리
    // private String ad_eng_bd = ""; // 영문 다량배달처
    private String ad_eng_roadnm = ""; // 영문 도로명
    
    // private String ad_eng_sggbd = ""; // 영문 시군구건물명
    
    private String custom_add1 = ""; // 고객사공간 - 지번 기번주소
    private String custom_add2 = ""; // 고객사공간 - 지번 상세주소
    private String custom_ro_add1 = ""; // 고객사공간 - 도로명 기번주소
    private String custom_ro_add2 = ""; // 고객사공간 - 도로명 상세주소
    private String custom_ro_ref = ""; // 고객사공간 - 도로명 참조주소
    
    public String getAd_jr()
    {
        return ad_jr;
    }
    
    public void setAd_jr(String s_ad_jr)
    {
        ad_jr = s_ad_jr;
    }
    
    public String getAd_resultflag()
    {
        return ad_resultflag;
    }
    
    public void setAd_resultflag(String s_ad_resultflag)
    {
        ad_resultflag = s_ad_resultflag;
    }
    
    public String getAd_resultcd()
    {
        return ad_resultcd;
    }
    
    public void setAd_resultcd(String s_ad_resultcd)
    {
        ad_resultcd = s_ad_resultcd;
    }
    
    public String getAd_postno()
    {
        return ad_postno;
    }
    
    public void setAd_postno(String s_ad_postno)
    {
        ad_postno = s_ad_postno;
    }
    
    public String getAd_postseq()
    {
        return ad_postseq;
    }
    
    public void setAd_postseq(String s_ad_postseq)
    {
        ad_postseq = s_ad_postseq;
    }
    
    public String getAd_bsizonno()
    {
        return ad_bsizonno;
    }
    
    public void setAd_bsizonno(String s_ad_bsizonno)
    {
        ad_bsizonno = s_ad_bsizonno;
    }
    
    public String getAd_add1()
    {
        return ad_add1;
    }
    
    public void setAd_add1(String s_ad_add1)
    {
        ad_add1 = s_ad_add1;
    }
    
    public String getAd_add2()
    {
        return ad_add2;
    }
    
    public void setAd_add2(String s_ad_add2)
    {
        ad_add2 = s_ad_add2;
    }
    
    public String getAd_ro_postno()
    {
        return ad_ro_postno;
    }
    
    public void setAd_ro_postno(String s_ad_ro_postno)
    {
        ad_ro_postno = s_ad_ro_postno;
    }
    
    public String getAd_ro_postseq()
    {
        return ad_ro_postseq;
    }
    
    public void setAd_ro_postseq(String s_ad_ro_postseq)
    {
        ad_ro_postseq = s_ad_ro_postseq;
    }
    
    public String getAd_ro_bsizonno()
    {
        return ad_ro_bsizonno;
    }
    
    public void setAd_ro_bsizonno(String s_ad_ro_bsizonno)
    {
        ad_ro_bsizonno = s_ad_ro_bsizonno;
    }
    
    public String getAd_ro_add1()
    {
        return ad_ro_add1;
    }
    
    public void setAd_ro_add1(String s_ad_ro_add1)
    {
        ad_ro_add1 = s_ad_ro_add1;
    }
    
    public String getAd_ro_add2()
    {
        return ad_ro_add2;
    }
    
    public void setAd_ro_add2(String s_ad_ro_add2)
    {
        ad_ro_add2 = s_ad_ro_add2;
    }
    
    public String getAd_ro_ref()
    {
        return ad_ro_ref;
    }
    
    public void setAd_ro_ref(String s_ad_ro_ref)
    {
        ad_ro_ref = s_ad_ro_ref;
    }
    
    public String getAd_ro_ref2()
    {
        return ad_ro_ref2;
    }
    
    public void setAd_ro_ref2(String s_ad_ro_ref2)
    {
        ad_ro_ref2 = s_ad_ro_ref2;
    }
    
    public String getAd_ro_ref3()
    {
        return ad_ro_ref3;
    }
    
    public void setAd_ro_ref3(String s_ad_ro_ref3)
    {
        ad_ro_ref3 = s_ad_ro_ref3;
    }
    
    public String getAd_ro_ref4()
    {
        return ad_ro_ref4;
    }
    
    public void setAd_ro_ref4(String s_ad_ro_ref4)
    {
        ad_ro_ref4 = s_ad_ro_ref4;
    }
    
    public String getAd_sido()
    {
        return ad_sido;
    }
    
    public void setAd_sido(String s_ad_sido)
    {
        ad_sido = s_ad_sido;
    }
    
    public String getAd_gungu()
    {
        return ad_gungu;
    }
    
    public void setAd_gungu(String s_ad_gungu)
    {
        ad_gungu = s_ad_gungu;
    }
    
    public String getAd_dong()
    {
        return ad_dong;
    }
    
    public void setAd_dong(String s_ad_dong)
    {
        ad_dong = s_ad_dong;
    }
    
    public String getAd_ri()
    {
        return ad_ri;
    }
    
    public void setAd_ri(String s_ad_ri)
    {
        ad_ri = s_ad_ri;
    }
    
    public String getAd_san()
    {
        return ad_san;
    }
    
    public void setAd_san(String s_ad_san)
    {
        ad_san = s_ad_san;
    }
    
    public String getAd_sancd()
    {
        return ad_sancd;
    }
    
    public void setAd_sancd(String s_ad_sancd)
    {
        ad_sancd = s_ad_sancd;
    }
    
    public String getAd_mn()
    {
        return ad_mn;
    }
    
    public void setAd_mn(String s_ad_mn)
    {
        ad_mn = s_ad_mn;
    }
    
    public String getAd_sn()
    {
        return ad_sn;
    }
    
    public void setAd_sn(String s_ad_sn)
    {
        ad_sn = s_ad_sn;
    }
    
    public String getAd_buildinfo()
    {
        return ad_buildinfo;
    }
    
    public void setAd_buildinfo(String s_ad_buildinfo)
    {
        ad_buildinfo = s_ad_buildinfo;
    }
    
    public String getAd_builddong()
    {
        return ad_builddong;
    }
    
    public void setAd_builddong(String s_ad_builddong)
    {
        ad_builddong = s_ad_builddong;
    }
    
    public String getAd_buildho()
    {
        return ad_buildho;
    }
    
    public void setAd_buildho(String s_ad_buildho)
    {
        ad_buildho = s_ad_buildho;
    }
    
    public String getAd_buildstair()
    {
        return ad_buildstair;
    }
    
    public void setAd_buildstair(String s_ad_buildstair)
    {
        ad_buildstair = s_ad_buildstair;
    }
    
    public String getAd_remain()
    {
        return ad_remain;
    }
    
    public void setAd_remain(String s_ad_remain)
    {
        ad_remain = s_ad_remain;
    }
    
    public String getAd_dongcd()
    {
        return ad_dongcd;
    }
    
    public void setAd_dongcd(String s_ad_dongcd)
    {
        ad_dongcd = s_ad_dongcd;
    }
    
    public String getAd_bdongcd()
    {
        return ad_bdongcd;
    }
    
    public void setAd_bdongcd(String s_ad_bdongcd)
    {
        ad_bdongcd = s_ad_bdongcd;
    }
    
    public String getAd_hdongcd()
    {
        return ad_hdongcd;
    }
    
    public void setAd_hdongcd(String s_ad_hdongcd)
    {
        ad_hdongcd = s_ad_hdongcd;
    }
    
    public String getAd_bdongnm()
    {
        return ad_bdongnm;
    }
    
    public void setAd_bdongnm(String s_ad_bdongnm)
    {
        ad_bdongnm = s_ad_bdongnm;
    }
    
    public String getAd_hdongnm()
    {
        return ad_hdongnm;
    }
    
    public void setAd_hdongnm(String s_ad_hdongnm)
    {
        ad_hdongnm = s_ad_hdongnm;
    }
    
    public String getAd_pnucd()
    {
        return ad_pnucd;
    }
    
    public void setAd_pnucd(String s_ad_pnucd)
    {
        ad_pnucd = s_ad_pnucd;
    }
    
    public String getAd_cx()
    {
        return ad_cx;
    }
    
    public void setAd_cx(String s_ad_cx)
    {
        ad_cx = s_ad_cx;
    }
    
    public String getAd_cy()
    {
        return ad_cy;
    }
    
    public void setAd_cy(String s_ad_cy)
    {
        ad_cy = s_ad_cy;
    }
    
    public String getAd_lev()
    {
        return ad_lev;
    }
    
    public void setAd_lev(String s_ad_lev)
    {
        ad_lev = s_ad_lev;
    }
    
    public String getAd_ro_roadnm()
    {
        return ad_ro_roadnm;
    }
    
    public void setAd_ro_roadnm(String s_ad_ro_roadnm)
    {
        ad_ro_roadnm = s_ad_ro_roadnm;
    }
    
    public String getAd_ro_roadcd()
    {
        return ad_ro_roadcd;
    }
    
    public void setAd_ro_roadcd(String s_ad_ro_roadcd)
    {
        ad_ro_roadcd = s_ad_ro_roadcd;
    }
    
    public String getAd_ro_dongseq()
    {
        return ad_ro_dongseq;
    }
    
    public void setAd_ro_dongseq(String s_ad_ro_dongseq)
    {
        ad_ro_dongseq = s_ad_ro_dongseq;
    }
    
    public String getAd_ro_undercd()
    {
        return ad_ro_undercd;
    }
    
    public void setAd_ro_undercd(String s_ad_ro_undercd)
    {
        ad_ro_undercd = s_ad_ro_undercd;
    }
    
    public String getAd_ro_under()
    {
        return ad_ro_under;
    }
    
    public void setAd_ro_under(String s_ad_ro_under)
    {
        ad_ro_under = s_ad_ro_under;
    }
    
    public String getAd_ro_buildmn()
    {
        return ad_ro_buildmn;
    }
    
    public void setAd_ro_buildmn(String s_ad_ro_buildmn)
    {
        ad_ro_buildmn = s_ad_ro_buildmn;
    }
    
    public String getAd_ro_buildsn()
    {
        return ad_ro_buildsn;
    }
    
    public void setAd_ro_buildsn(String s_ad_ro_buildsn)
    {
        ad_ro_buildsn = s_ad_ro_buildsn;
    }
    
    public String getAd_ro_buildnm()
    {
        return ad_ro_buildnm;
    }
    
    public void setAd_ro_buildnm(String s_ad_ro_buildnm)
    {
        ad_ro_buildnm = s_ad_ro_buildnm;
    }
    
    public String getAd_ro_builddtail()
    {
        return ad_ro_builddtail;
    }
    
    public void setAd_ro_builddtail(String s_ad_ro_builddtail)
    {
        ad_ro_builddtail = s_ad_ro_builddtail;
    }
    
    public String getAd_ro_buildmgno()
    {
        return ad_ro_buildmgno;
    }
    
    public void setAd_ro_buildmgno(String s_ad_ro_buildmgno)
    {
        ad_ro_buildmgno = s_ad_ro_buildmgno;
    }
    
    public String getAd_ro_pnu()
    {
        return ad_ro_pnu;
    }
    
    public void setAd_ro_pnu(String s_ad_ro_pnu)
    {
        ad_ro_pnu = s_ad_ro_pnu;
    }
    
    public String getAd_ro_cx()
    {
        return ad_ro_cx;
    }
    
    public void setAd_ro_cx(String s_ad_ro_cx)
    {
        ad_ro_cx = s_ad_ro_cx;
    }
    
    public String getAd_ro_cy()
    {
        return ad_ro_cy;
    }
    
    public void setAd_ro_cy(String s_ad_ro_cy)
    {
        ad_ro_cy = s_ad_ro_cy;
    }
    
    public String getAd_reprematching()
    {
        return ad_reprematching;
    }
    
    public void setAd_reprematching(String s_ad_reprematching)
    {
        ad_reprematching = s_ad_reprematching;
    }
    
    public String getAd_apathouse()
    {
        return ad_apathouse;
    }
    
    public void setAd_apathouse(String s_ad_apathouse)
    {
        ad_apathouse = s_ad_apathouse;
    }
    
    public String getAd_annexbuild()
    {
        return ad_annexbuild;
    }
    
    public void setAd_annexbuild(String s_ad_annexbuild)
    {
        ad_annexbuild = s_ad_annexbuild;
    }
    
    public String getAd_annexinfo()
    {
        return ad_annexinfo;
    }
    
    public void setAd_annexinfo(String s_ad_annexinfo)
    {
        ad_annexinfo = s_ad_annexinfo;
    }
    
    public String getAd_addrlev()
    {
        return ad_addrlev;
    }
    
    public void setAd_addrlev(String s_ad_addrlev)
    {
        ad_addrlev = s_ad_addrlev;
    }
    
    public String getAd_postlev()
    {
        return ad_postlev;
    }
    
    public void setAd_postlev(String s_ad_postlev)
    {
        ad_postlev = s_ad_postlev;
    }
    
    public String getAd_posttype()
    {
        return ad_posttype;
    }
    
    public void setAd_posttype(String s_ad_posttype)
    {
        ad_posttype = s_ad_posttype;
    }
    
    public String getAd_errcd()
    {
        return ad_errcd;
    }
    
    public void setAd_errcd(String s_ad_errcd)
    {
        ad_errcd = s_ad_errcd;
    }
    
    public String getAd_datachgcd()
    {
        return ad_datachgcd;
    }
    
    public void setAd_datachgcd(String s_ad_datachgcd)
    {
        ad_datachgcd = s_ad_datachgcd;
    }
    
    public String getAd_newpostlev()
    {
        return ad_newpostlev;
    }
    
    public void setAd_newpostlev(String s_ad_newpostlev)
    {
        ad_newpostlev = s_ad_newpostlev;
    }
    
    public String getAd_ro_totaladdcnt()
    {
        return ad_ro_totaladdcnt;
    }
    
    public void setAd_ro_totaladdcnt(String s_ad_ro_totaladdcnt)
    {
        ad_ro_totaladdcnt = s_ad_ro_totaladdcnt;
    }
    
    public String getAd_ro_repeatcnt()
    {
        return ad_ro_repeatcnt;
    }
    
    public void setAd_ro_repeatcnt(String s_ad_ro_repeatcnt)
    {
        ad_ro_repeatcnt = s_ad_ro_repeatcnt;
    }
    
    public String getAd_eng_sido()
    {
        return ad_eng_sido;
    }
    
    public void setAd_eng_sido(String s_ad_eng_sido)
    {
        ad_eng_sido = s_ad_eng_sido;
    }
    
    public String getAd_eng_gungu()
    {
        return ad_eng_gungu;
    }
    
    public void setAd_eng_gungu(String s_ad_eng_gungu)
    {
        ad_eng_gungu = s_ad_eng_gungu;
    }
    
    public String getAd_eng_dong()
    {
        return ad_eng_dong;
    }
    
    public void setAd_eng_dong(String s_ad_eng_dong)
    {
        ad_eng_dong = s_ad_eng_dong;
    }
    
    public String getAd_eng_ri()
    {
        return ad_eng_ri;
    }
    
    public void setAd_eng_ri(String s_ad_eng_ri)
    {
        ad_eng_ri = s_ad_eng_ri;
    }
    
    // public String getAd_eng_bd()
    // {
    // return ad_eng_bd;
    // }
    //
    // public void setAd_eng_bd(String s_ad_eng_bd)
    // {
    // ad_eng_bd = s_ad_eng_bd;
    // }
    
    public String getAd_eng_roadnm()
    {
        return ad_eng_roadnm;
    }
    
    public void setAd_eng_roadnm(String s_ad_eng_roadnm)
    {
        ad_eng_roadnm = s_ad_eng_roadnm;
    }
    
    // public String getAd_eng_sggbd()
    // {
    // return ad_eng_sggbd;
    // }
    //
    // public void setAd_eng_sggbd(String s_ad_eng_sggbd)
    // {
    // ad_eng_sggbd = s_ad_eng_sggbd;
    // }
    
    public String getCustom_add1()
    {
        return custom_add1;
    }
    
    public void setCustom_add1(String s_custom_add1)
    {
        custom_add1 = s_custom_add1;
    }
    
    public String getCustom_add2()
    {
        return custom_add2;
    }
    
    public void setCustom_add2(String s_custom_add2)
    {
        custom_add2 = s_custom_add2;
    }
    
    public String getCustom_ro_add1()
    {
        return custom_ro_add1;
    }
    
    public void setCustom_ro_add1(String s_custom_ro_add1)
    {
        custom_ro_add1 = s_custom_ro_add1;
    }
    
    public String getCustom_ro_add2()
    {
        return custom_ro_add2;
    }
    
    public void setCustom_ro_add2(String s_custom_ro_add2)
    {
        custom_ro_add2 = s_custom_ro_add2;
    }
    
    public String getCustom_ro_ref()
    {
        return custom_ro_ref;
    }
    
    public void setCustom_ro_ref(String s_custom_ro_ref)
    {
        custom_ro_ref = s_custom_ro_ref;
    }
    
}
