/**
 * @FileName    : GviaDistrictVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 3. 9. 
 * @Author      : 유창기 
 * @Description : 행정구역 리스트 검색
 * @변경이력     :
 */

package gcp.external.kainess.vo;

public class GviaDistrictVo
{
    private String resultcd = ""; // FLAG1 (1:정상, 0:불능)
    private String totrecord = ""; // 전체 검색결과 수량
    private String strecord = ""; // 시작레코드 번호
    private String rorecord = ""; // 출력 제한 수량
    
    private String donggb = ""; // 동구분
    private String hdonglv = ""; // 행정구역 레벨
    private String regioncd = ""; // 행정구역 코드값
    private String regionnm = ""; // 행정구역 명칭
    
    private String eng_regionnm = ""; // 행정구역 명칭
    
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
    
    public String getDonggb()
    {
        return donggb;
    }
    
    public void setDonggb(String s_donggb)
    {
        donggb = s_donggb;
    }
    
    public String getHdonglvd()
    {
        return hdonglv;
    }
    
    public void setHdonglvd(String s_hdonglv)
    {
        hdonglv = s_hdonglv;
    }
    
    public String getRegioncd()
    {
        return regioncd;
    }
    
    public void setRegioncd(String s_regioncd)
    {
        regioncd = s_regioncd;
    }
    
    public String getRegionnm()
    {
        return regionnm;
    }
    
    public void setRegionnm(String s_regionnm)
    {
        regionnm = s_regionnm;
    }
    
    public String getEngRegionnm()
    {
        return eng_regionnm;
    }
    
    public void setEngRegionnm(String s_eng_regionnm)
    {
        eng_regionnm = s_eng_regionnm;
    }
}
