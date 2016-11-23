/**
 * @FileName    : GviaPostSearchNewVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 5. 15. 
 * @Author      : 조성연 
 * @Description : 우편번호(기초구역번호) 검색 결과-개별출력
 * @변경이력     :
 */

package gcp.external.kainess.vo;

public class GviaPostSearchNewVo
{
    private String resultcd = ""; // FLAG1 (1:정상, 0:불능)
    private String totrecord = ""; // 전체 검색결과 수량
    private String strecord = ""; // 시작레코드 번호
    private String rorecord = ""; // 출력 제한 수량
    private String bsizonno = ""; // 기초구역번호
    private String postno = ""; // 우편번호
    private String postseq = ""; // 우편번호 일련번호
    private String sido = ""; // 광역시/도명칭
    private String gungu = ""; // 시/군/구명칭
    private String dong = ""; // 읍/면/동명칭
    private String ri = ""; // 리명칭
    private String roadnm = ""; // 도로명
    private String undercd = ""; // 지하구분(0:지상, 1:지하, 2:공중)
    private String under = ""; // 지하여부(지하, 공중)
    private String buildmn = "";// 건물 본번
    private String buildsn = "";// 건물 부번
    private String buildnm = ""; // 건물명
    private String roadcd = ""; // 도로코드
    private String dongseq = ""; // 읍면일련번호
    private String sancd = ""; // 산구분(0:평지, 1:산)
    private String san = ""; // 산여부(산)
    private String bunji = ""; // 주번지
    private String ho = ""; // 부번지
    private String bdongcd = ""; // 법정동코드
    private String hdongcd = ""; // 행정동코드
    private String bdongnm = ""; // 법정동명칭
    private String hdongnm = ""; // 행정동명칭
    
    private String eng_sido = ""; // 영문 시도명
    private String eng_gungu = ""; // 영문 시군구명
    private String eng_dong = ""; // 영문 읍면동명
    private String eng_ri = ""; // 영문 법정리
    private String eng_roadnm = ""; // 영문 도로명
    
    public String getResultcd()
    {
        return resultcd;
    }
    
    public void setResultcd(String s_resultcd)
    {
        resultcd = s_resultcd;
    }
    
    public String getTotrecord()
    {
        return totrecord;
    }
    
    public void setTotrecord(String s_totrecord)
    {
        totrecord = s_totrecord;
    }
    
    public String getStrecord()
    {
        return strecord;
    }
    
    public void setStrecord(String s_strecord)
    {
        strecord = s_strecord;
    }
    
    public String getRorecord()
    {
        return rorecord;
    }
    
    public void setRorecord(String s_rorecord)
    {
        rorecord = s_rorecord;
    }
    
    public String getBsizonno()
    {
        return bsizonno;
    }
    
    public void setBsizonno(String s_bsizonno)
    {
        bsizonno = s_bsizonno;
    }
    
    public String getPostno()
    {
        return postno;
    }
    
    public void setPostno(String s_postno)
    {
        postno = s_postno;
    }
    
    public String getPostseq()
    {
        return postseq;
    }
    
    public void setPostseq(String s_postseq)
    {
        postseq = s_postseq;
    }
    
    public String getSido()
    {
        return sido;
    }
    
    public void setSido(String s_sido)
    {
        sido = s_sido;
    }
    
    public String getGungu()
    {
        return gungu;
    }
    
    public void setGungu(String s_gungu)
    {
        gungu = s_gungu;
    }
    
    public String getDong()
    {
        return dong;
    }
    
    public void setDong(String s_dong)
    {
        dong = s_dong;
    }
    
    public String getRi()
    {
        return ri;
    }
    
    public void setRi(String s_ri)
    {
        ri = s_ri;
    }
    
    public String getRoadnm()
    {
        return roadnm;
    }
    
    public void setRoadnm(String s_roadnm)
    {
        roadnm = s_roadnm;
    }
    
    public String getUndercd()
    {
        return undercd;
    }
    
    public void setUndercd(String s_undercd)
    {
        undercd = s_undercd;
    }
    
    public String getUnder()
    {
        return under;
    }
    
    public void setUnder(String s_under)
    {
        under = s_under;
    }
    
    public String getBuildmn()
    {
        return buildmn;
    }
    
    public void setBuildmn(String s_buildmn)
    {
        buildmn = s_buildmn;
    }
    
    public String getBuildsn()
    {
        return buildsn;
    }
    
    public void setBuildsn(String s_buildsn)
    {
        buildsn = s_buildsn;
    }
    
    public String getBuildnm()
    {
        return buildnm;
    }
    
    public void setBuildnm(String s_buildnm)
    {
        buildnm = s_buildnm;
    }
    
    public String getRoadcd()
    {
        return roadcd;
    }
    
    public void setRoadcd(String s_roadcd)
    {
        roadcd = s_roadcd;
    }
    
    public String getDongseq()
    {
        return dongseq;
    }
    
    public void setDongseq(String s_dongseq)
    {
        dongseq = s_dongseq;
    }
    
    public String getSancd()
    {
        return sancd;
    }
    
    public void setSancd(String s_sancd)
    {
        sancd = s_sancd;
    }
    
    public String getSan()
    {
        return san;
    }
    
    public void setSan(String s_san)
    {
        san = s_san;
    }
    
    public String getBunji()
    {
        return bunji;
    }
    
    public void setBunji(String s_bunji)
    {
        bunji = s_bunji;
    }
    
    public String getHo()
    {
        return ho;
    }
    
    public void setHo(String s_ho)
    {
        ho = s_ho;
    }
    
    public String getBdongcd()
    {
        return bdongcd;
    }
    
    public void setBdongcd(String s_bdongcd)
    {
        bdongcd = s_bdongcd;
    }
    
    public String getHdongcd()
    {
        return hdongcd;
    }
    
    public void setHdongcd(String s_hdongcd)
    {
        hdongcd = s_hdongcd;
    }
    
    public String getBdongnm()
    {
        return bdongnm;
    }
    
    public void setBdongnm(String s_bdongnm)
    {
        bdongnm = s_bdongnm;
    }
    
    public String getHdongnm()
    {
        return hdongnm;
    }
    
    public void setHdongnm(String s_hdongnm)
    {
        hdongnm = s_hdongnm;
    }
    
    public String getEng_sido()
    {
        return eng_sido;
    }
    
    public void setEng_sido(String s_eng_sido)
    {
        eng_sido = s_eng_sido;
    }
    
    public String getEng_gungu()
    {
        return eng_gungu;
    }
    
    public void setEng_gungu(String s_eng_gungu)
    {
        eng_gungu = s_eng_gungu;
    }
    
    public String getEng_dong()
    {
        return eng_dong;
    }
    
    public void setEng_dong(String s_eng_dong)
    {
        eng_dong = s_eng_dong;
    }
    
    public String getEng_ri()
    {
        return eng_ri;
    }
    
    public void setEng_ri(String s_eng_ri)
    {
        eng_ri = s_eng_ri;
    }
    
    public String getEng_roadnm()
    {
        return eng_roadnm;
    }
    
    public void setEng_roadnm(String s_eng_roadnm)
    {
        eng_roadnm = s_eng_roadnm;
    }
    
}
