/**
 * @FileName    : GviaPostNewMultVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 5. 15. 
 * @Author      : 조성연
 * @Description : 우편번호(기초구역번호) 검색-개별출력
 * @변경이력     :
 */

package gcp.external.kainess.vo;

import java.util.ArrayList;

public class GviaPostSearchNewMultVo
{
    private ArrayList entityList1 = new ArrayList();
    private ArrayList entityList2 = new ArrayList();
    private int nResultFlag = 0; // 출력결과구분(0:일반, 1:통합검색 건수)
    private int ErrorCode = 0; // 에러코드(ErrorCode Class 참조)
    
    private String resultcd = "0"; // FLAG1 (1:정상, 0:불능)
    private String totrecord = "0"; // 전체 검색결과 수량
    private String strecord = "0"; // 시작레코드 번호
    private String rorecord = "0"; // 출력 제한 수량
    
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
    
    public int getResult()
    {
        return nResultFlag;
    }
    
    public void setResult(int s_nResultFlag)
    {
        nResultFlag = s_nResultFlag;
    }
    
    public int getErrorCode()
    {
        return ErrorCode;
    }
    
    public void setErrorCode(int errorCode)
    {
        ErrorCode = errorCode;
    }
    
    public int getCount()
    {
        if (nResultFlag == 0)
        {
            return entityList1.size();
        }
        else
        {
            return entityList2.size();
        }
    }
    
    public void addEntity(GviaPostSearchNewVo entity)
    {
        entityList1.add(entity);
    }
    
    public GviaPostSearchNewVo getEntity(int nCount)
    {
        return (GviaPostSearchNewVo) entityList1.get(nCount);
    }
    
    public void addCountEntity(GviaPostSearchTotCountVo entity)
    {
        entityList2.add(entity);
    }
    
    public GviaPostSearchTotCountVo getCountEntity(int nCount)
    {
        return (GviaPostSearchTotCountVo) entityList2.get(nCount);
    }
}
