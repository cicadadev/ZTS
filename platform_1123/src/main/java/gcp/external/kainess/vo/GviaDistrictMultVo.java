/**
 * @FileName    : GviaDistrictMultVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 3. 9. 
 * @Author      : 유창기
 * @Description : 행정구역 리스트 검색(다건)
 * @변경이력     :
 */

package gcp.external.kainess.vo;

import java.util.ArrayList;

public class GviaDistrictMultVo
{
    private ArrayList entityList = new ArrayList();
    private int ErrorCode = 0; // 에러코드(ErrorCode Class 참조)
    
    private String resultcd = "0"; // FLAG1 (1:정상, 0:불능)
    private String totrecord = "0"; // 전체 검색결과 수량
    private String strecord = "0"; // 시작레코드 번호
    private String rorecord = "0"; // 출력 제한 수량
    
    public int getErrorCode()
    {
        return ErrorCode;
    }
    
    public void setErrorCode(int errorCode)
    {
        ErrorCode = errorCode;
    }
    
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
    
    public int getCount()
    {
        return entityList.size();
    }
    
    public void addEntity(GviaDistrictVo entity)
    {
        entityList.add(entity);
    }
    
    public GviaDistrictVo getEntity(int nCount)
    {
        return (GviaDistrictVo) entityList.get(nCount);
    }
}
