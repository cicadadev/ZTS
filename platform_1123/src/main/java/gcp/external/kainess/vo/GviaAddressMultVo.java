/**
 * @FileName    : GviaAddressMultVo.java
 * @Project     : GVIA_Web
 * @Date        : 2015. 3. 9. 
 * @Author      : 유창기
 * @Description : 주소 정제/전환 결과(다건)
 * @변경이력     :
 */

package gcp.external.kainess.vo;

import java.util.ArrayList;

public class GviaAddressMultVo
{
    private ArrayList entityList1 = new ArrayList();
    private ArrayList entityList2 = new ArrayList();
    private int nResultFlag = 0; // 출력결과구분(0:정상, 1:입력다량)
    private int ErrorCode = 0; // 에러코드(ErrorCode Class 참조)
    private int IsProhibit = 0; // 금칙어 포함 여부
    private String[] ProhibitWord = null; // 금칙어 목록
    
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
    
    public int getIsProhibit()
    {
        return IsProhibit;
    }
    
    public void setIsProhibit(int s_IsProhibit)
    {
        IsProhibit = s_IsProhibit;
    }
    
    public String[] getProhibitWord()
    {
        return ProhibitWord;
    }
    
    public void setProhibitWord(String[] s_ProhibitWord)
    {
        ProhibitWord = s_ProhibitWord;
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
    
    public void addAddrEntity(GviaAddressVo entity)
    {
        entityList1.add(entity);
    }
    
    public GviaAddressVo getAddrEntity(int nCount)
    {
        return (GviaAddressVo) entityList1.get(nCount);
    }
    
    public void addSearchEntity(GviaAddressToSearchVo entity)
    {
        entityList2.add(entity);
    }
    
    public GviaAddressToSearchVo getSearchEntity(int nCount)
    {
        return (GviaAddressToSearchVo) entityList2.get(nCount);
    }
}
