/**
 * @FileName : GviaPostSearchNewVo.java
 * @Project : GVIA_Web
 * @Date : 2015. 5. 15.
 * @Author : 조성연
 * @Description : 우편번호(기초구역번호) 검색 결과-개별출력
 * @변경이력 :
 */

package gcp.external.kainess.vo;

public class GviaPostSearchTotCountVo
{
    private String resultcd = ""; // FLAG1 (1:정상, 0:불능)
    private String totrecord = ""; // 전체 검색결과 수량
    private String strecord = ""; // 시작레코드 번호
    private String rorecord = ""; // 출력 제한 수량
    private String ad_jr = ""; // 지번/새주소구분(J:지번주소, R:도로명주소)
    private String startrcd_se = ""; // 시작레코드(서울)
    private String count_se = ""; // 검색결과 수량(서울)
    private String startrcd_bs = "";
    private String count_bs = "";
    private String startrcd_dg = "";
    private String count_dg = "";
    private String startrcd_ic = "";
    private String count_ic = "";
    private String startrcd_gj = "";
    private String count_gj = "";
    private String startrcd_dj = "";
    private String count_dj = "";
    private String startrcd_us = "";
    private String count_us = "";
    private String startrcd_sj = "";
    private String count_sj = "";
    private String startrcd_gg = "";
    private String count_gg = "";
    private String startrcd_gw = "";
    private String count_gw = "";
    private String startrcd_cb = "";
    private String count_cb = "";
    private String startrcd_cn = "";
    private String count_cn = "";
    private String startrcd_jb = "";
    private String count_jb = "";
    private String startrcd_jn = "";
    private String count_jn = "";
    private String startrcd_gb = "";
    private String count_gb = "";
    private String startrcd_gn = "";
    private String count_gn = "";
    private String startrcd_jj = "";
    private String count_jj = "";
    
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
    
    public String getAd_jr()
    {
        return ad_jr;
    }
    
    public void setAd_jr(String s_ad_jr)
    {
        ad_jr = s_ad_jr;
    }
    
    public String getStartrcd_se()
    {
        return startrcd_se;
    }
    
    public void setStartrcd_se(String s_startrcd_se)
    {
        startrcd_se = s_startrcd_se;
    }
    
    public String getCount_se()
    {
        return count_se;
    }
    
    public void setCount_se(String s_count_se)
    {
        count_se = s_count_se;
    }
    
    public String getStartrcd_bs()
    {
        return startrcd_bs;
    }
    
    public void setStartrcd_bs(String s_startrcd_bs)
    {
        startrcd_bs = s_startrcd_bs;
    }
    
    public String getCount_bs()
    {
        return count_bs;
    }
    
    public void setCount_bs(String s_count_bs)
    {
        count_bs = s_count_bs;
    }
    
    public String getStartrcd_dg()
    {
        return startrcd_dg;
    }
    
    public void setStartrcd_dg(String s_startrcd_dg)
    {
        startrcd_dg = s_startrcd_dg;
    }
    
    public String getCount_dg()
    {
        return count_dg;
    }
    
    public void setCount_dg(String s_count_dg)
    {
        count_dg = s_count_dg;
    }
    
    public String getStartrcd_ic()
    {
        return startrcd_ic;
    }
    
    public void setStartrcd_ic(String s_startrcd_ic)
    {
        startrcd_ic = s_startrcd_ic;
    }
    
    public String getCount_ic()
    {
        return count_ic;
    }
    
    public void setCount_ic(String s_count_ic)
    {
        count_ic = s_count_ic;
    }
    
    public String getStartrcd_gj()
    {
        return startrcd_gj;
    }
    
    public void setStartrcd_gj(String s_startrcd_gj)
    {
        startrcd_gj = s_startrcd_gj;
    }
    
    public String getCount_gj()
    {
        return count_gj;
    }
    
    public void setCount_gj(String s_count_gj)
    {
        count_gj = s_count_gj;
    }
    
    public String getStartrcd_dj()
    {
        return startrcd_dj;
    }
    
    public void setStartrcd_dj(String s_startrcd_dj)
    {
        startrcd_dj = s_startrcd_dj;
    }
    
    public String getCount_dj()
    {
        return count_dj;
    }
    
    public void setCount_dj(String s_count_dj)
    {
        count_dj = s_count_dj;
    }
    
    public String getStartrcd_us()
    {
        return startrcd_us;
    }
    
    public void setStartrcd_us(String s_startrcd_us)
    {
        startrcd_us = s_startrcd_us;
    }
    
    public String getCount_us()
    {
        return count_us;
    }
    
    public void setCount_us(String s_count_us)
    {
        count_us = s_count_us;
    }
    
    public String getStartrcd_sj()
    {
        return startrcd_sj;
    }
    
    public void setStartrcd_sj(String s_startrcd_sj)
    {
        startrcd_sj = s_startrcd_sj;
    }
    
    public String getCount_sj()
    {
        return count_sj;
    }
    
    public void setCount_sj(String s_count_sj)
    {
        count_sj = s_count_sj;
    }
    
    public String getStartrcd_gg()
    {
        return startrcd_gg;
    }
    
    public void setStartrcd_gg(String s_startrcd_gg)
    {
        startrcd_gg = s_startrcd_gg;
    }
    
    public String getCount_gg()
    {
        return count_gg;
    }
    
    public void setCount_gg(String s_count_gg)
    {
        count_gg = s_count_gg;
    }
    
    public String getStartrcd_gw()
    {
        return startrcd_gw;
    }
    
    public void setStartrcd_gw(String s_startrcd_gw)
    {
        startrcd_gw = s_startrcd_gw;
    }
    
    public String getCount_gw()
    {
        return count_gw;
    }
    
    public void setCount_gw(String s_count_gw)
    {
        count_gw = s_count_gw;
    }
    
    public String getStartrcd_cb()
    {
        return startrcd_cb;
    }
    
    public void setStartrcd_cb(String s_startrcd_cb)
    {
        startrcd_cb = s_startrcd_cb;
    }
    
    public String getCount_cb()
    {
        return count_cb;
    }
    
    public void setCount_cb(String s_count_cb)
    {
        count_cb = s_count_cb;
    }
    
    public String getStartrcd_cn()
    {
        return startrcd_cn;
    }
    
    public void setStartrcd_cn(String s_startrcd_cn)
    {
        startrcd_cn = s_startrcd_cn;
    }
    
    public String getCount_cn()
    {
        return count_cn;
    }
    
    public void setCount_cn(String s_count_cn)
    {
        count_cn = s_count_cn;
    }
    
    public String getStartrcd_jb()
    {
        return startrcd_jb;
    }
    
    public void setStartrcd_jb(String s_startrcd_jb)
    {
        startrcd_jb = s_startrcd_jb;
    }
    
    public String getCount_jb()
    {
        return count_jb;
    }
    
    public void setCount_jb(String s_count_jb)
    {
        count_jb = s_count_jb;
    }
    
    public String getStartrcd_jn()
    {
        return startrcd_jn;
    }
    
    public void setStartrcd_jn(String s_startrcd_jn)
    {
        startrcd_jn = s_startrcd_jn;
    }
    
    public String getCount_jn()
    {
        return count_jn;
    }
    
    public void setCount_jn(String s_count_jn)
    {
        count_jn = s_count_jn;
    }
    
    public String getStartrcd_gb()
    {
        return startrcd_gb;
    }
    
    public void setStartrcd_gb(String s_startrcd_gb)
    {
        startrcd_gb = s_startrcd_gb;
    }
    
    public String getCount_gb()
    {
        return count_gb;
    }
    
    public void setCount_gb(String s_count_gb)
    {
        count_gb = s_count_gb;
    }
    
    public String getStartrcd_gn()
    {
        return startrcd_gn;
    }
    
    public void setStartrcd_gn(String s_startrcd_gn)
    {
        startrcd_gn = s_startrcd_gn;
    }
    
    public String getCount_gn()
    {
        return count_gn;
    }
    
    public void setCount_gn(String s_count_gn)
    {
        count_gn = s_count_gn;
    }
    
    public String getStartrcd_jj()
    {
        return startrcd_jj;
    }
    
    public void setStartrcd_jj(String s_startrcd_jj)
    {
        startrcd_jj = s_startrcd_jj;
    }
    
    public String getCount_jj()
    {
        return count_jj;
    }
    
    public void setCount_jj(String s_count_jj)
    {
        count_jj = s_count_jj;
    }
    
}
