/**
 * @FileName    : GviaAddressToSearchVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 3. 9. 
 * @Author      : 유창기 
 * @Description : 주소 정제/전환 결과 -> 행정구역이나 건물이 여러건이라서 검색결과가 나오는 경우
 * @변경이력     :
 */

package gcp.external.kainess.vo;

public class GviaAddressToSearchVo
{
    
    private String flag = ""; // 'A' : 행정구역 멀티, 'B' : 건물 멀티
    private String donggubun = ""; // 입력동구분
    private String resultcnt = ""; // 멀티 주소 매칭 수
    private String etcAddr = ""; // 나머지주소
    
    private String postno = ""; // 우편번호
    private String sido = ""; // 광역시/도명칭
    private String gungu = ""; // 시/군/구명칭
    private String dong = ""; // 읍/면/동명칭
    private String ri = ""; // 리명칭
    private String roadnm = ""; // 도로명
    private String hdongnm = ""; // 행정동명칭
    private String bdongnm = ""; // 법정동명칭
    private String san_under = ""; // 산구분
    private String mn = ""; // 주번지
    private String sn = ""; // 부번지
    private String buildnm = ""; // 분석된 건물명
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String s_flag)
    {
        flag = s_flag;
    }
    
    public String getDonggubun()
    {
        return donggubun;
    }
    
    public void setDonggubun(String s_donggubun)
    {
        donggubun = s_donggubun;
    }
    
    public String getResultcnt()
    {
        return resultcnt;
    }
    
    public void setResultcnt(String s_resultcnt)
    {
        resultcnt = s_resultcnt;
    }
    
    public String getEtcAddr()
    {
        return etcAddr;
    }
    
    public void setEtcAddr(String s_etcAddr)
    {
        etcAddr = s_etcAddr;
    }
    
    public String getPostno()
    {
        return postno;
    }
    
    public void setPostno(String s_postno)
    {
        postno = s_postno;
    }
    
    public String getSido()
    {
        return sido;
    }
    
    public void setsido(String s_sido)
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
    
    public String geSan_under()
    {
        return san_under;
    }
    
    public void setSan_under(String s_san_under)
    {
        san_under = s_san_under;
    }
    
    public String getMn()
    {
        return mn;
    }
    
    public void setMn(String s_mn)
    {
        mn = s_mn;
    }
    
    public String getSn()
    {
        return sn;
    }
    
    public void setSn(String s_sn)
    {
        sn = s_sn;
    }
    
    public String getBuildnm()
    {
        return buildnm;
    }
    
    public void setBuildnm(String s_buildnm)
    {
        buildnm = s_buildnm;
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
    
    public String getRoadnm()
    {
        return roadnm;
    }
    
    public void setRoadnm(String s_roadnm)
    {
        roadnm = s_roadnm;
    }
}
