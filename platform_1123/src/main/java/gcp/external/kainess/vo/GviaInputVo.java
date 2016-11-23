package gcp.external.kainess.vo;

public class GviaInputVo
{
    private String prop = ""; // 프로퍼티 경로
    private String gviaip = ""; // 서버ip정보
    private String gviaport = ""; // 서버port정보
    private String gviaip2 = ""; // 서버ip정보2
    private String gviaport2 = ""; // 서버port정보2
    private String outcnt = ""; //
    private String addr = ""; // 주소 [정제/전환]
    private String postno = ""; // 우편번호 [정제/전환]
    private String searchpostno = ""; // 검색 우편번호
    private String searchaddr = ""; // 검색 주소
    private String searchbuild = ""; // 검색 건물
    private String dongnm = ""; // 동명
    private String roadnm = ""; // 도로명
    private String sidocd = ""; // 시도
    private String gungucd = ""; // 군구
    private String umdcd = ""; // 읍면동
    private String ricd = ""; // 리
    private String roadgungucd = ""; // 도로군구코드
    private String roadpnu = ""; // 도로명PNU
    private String roadaddr = ""; // 도로명주소
    private String tot_post_str = ""; // 통합검색
    
    private String bsizonno = ""; // 국가기초구역번호
    private String roadadd2 = ""; // 도로명+건물번호
    private String jibunadd2 = ""; // 동/리+지번
    private String searchbuild2 = ""; // 건물명
    private String tot_post_str2 = ""; // 통합검색
    private String districtzonno = ""; // 행정구역 기초구역번호
    
    private String errchk = ""; // 에러확인
    private String searchlike = ""; // 라이크검색
    private String inputjusogubun = ""; // 주소구분
    private String pgyn = ""; // 페이징 여부
    private String pgcd = ""; // 페이징 시작번호|출력수량
    private String bsi_mat_jibun = ""; // 기초구역번호 매칭 지번
    private String bsi_mat_road = ""; // 기초구역번호 매칭 도로명
    private String eng_addr_yn = ""; // 영문 주소 사용여부
    private String recordCall = ""; // 광역시도별 레코드 건수 조회 여부
    private String nBsizonnoYn = ""; // 행정구역별 기초구역번호 출력여부
    
    public String getProp()
    {
        return prop;
    }
    
    public void setProp(String prop)
    {
        this.prop = prop;
    }
    
    public String getGviaip()
    {
        return gviaip;
    }
    
    public void setGviaip(String vgviaip)
    {
        gviaip = vgviaip;
    }
    
    public String getGviaport()
    {
        return gviaport;
    }
    
    public void setGviaport(String vgviaport)
    {
        gviaport = vgviaport;
    }
    
    public String getGviaip2()
    {
        return gviaip2;
    }
    
    public void setGviaip2(String vgviaip2)
    {
        gviaip2 = vgviaip2;
    }
    
    public String getGviaport2()
    {
        return gviaport2;
    }
    
    public void setGviaport2(String vgviaport2)
    {
        gviaport2 = vgviaport2;
    }
    
    public String getOutcnt()
    {
        return outcnt;
    }
    
    public void setOutcnt(String outcnt)
    {
        this.outcnt = outcnt;
    }
    
    public String getAddr()
    {
        return addr;
    }
    
    public void setAddr(String addr)
    {
        this.addr = addr;
    }
    
    public String getPostno()
    {
        return postno;
    }
    
    public void setPostno(String postno)
    {
        this.postno = postno;
    }
    
    public String getSearchpostno()
    {
        return searchpostno;
    }
    
    public void setSearchpostno(String searchpostno)
    {
        this.searchpostno = searchpostno;
    }
    
    public String getSearchaddr()
    {
        return searchaddr;
    }
    
    public void setSearchaddr(String searchaddr)
    {
        this.searchaddr = searchaddr;
    }
    
    public String getSearchbuild()
    {
        return searchbuild;
    }
    
    public void setSearchbuild(String searchbuild)
    {
        this.searchbuild = searchbuild;
    }
    
    public String getDongnm()
    {
        return dongnm;
    }
    
    public void setDongnm(String dongnm)
    {
        this.dongnm = dongnm;
    }
    
    public String getRoadnm()
    {
        return roadnm;
    }
    
    public void setRoadnm(String roadnm)
    {
        this.roadnm = roadnm;
    }
    
    public String getSidocd()
    {
        return sidocd;
    }
    
    public void setSidocd(String sidocd)
    {
        this.sidocd = sidocd;
    }
    
    public String getGungucd()
    {
        return gungucd;
    }
    
    public void setGungucd(String gungucd)
    {
        this.gungucd = gungucd;
    }
    
    public String getUmdcd()
    {
        return umdcd;
    }
    
    public void setUmdcd(String umdcd)
    {
        this.umdcd = umdcd;
    }
    
    public String getRicd()
    {
        return ricd;
    }
    
    public void setRicd(String ricd)
    {
        this.ricd = ricd;
    }
    
    public String getRoadgungucd()
    {
        return roadgungucd;
    }
    
    public void setRoadgungucd(String roadgungucd)
    {
        this.roadgungucd = roadgungucd;
    }
    
    public String getTot_post_str()
    {
        return tot_post_str;
    }
    
    public void setTot_post_str(String tot_post_str)
    {
        this.tot_post_str = tot_post_str;
    }
    
    public String getBsizonno()
    {
        return bsizonno;
    }
    
    public void setBsizonno(String bsizonno)
    {
        this.bsizonno = bsizonno;
    }
    
    public String getRoadadd2()
    {
        return roadadd2;
    }
    
    public void setRoadadd2(String roadadd2)
    {
        this.roadadd2 = roadadd2;
    }
    
    public String getJibunadd2()
    {
        return jibunadd2;
    }
    
    public void setJibunadd2(String jibunadd2)
    {
        this.jibunadd2 = jibunadd2;
    }
    
    public String getSearchbuild2()
    {
        return searchbuild2;
    }
    
    public void setSearchbuild2(String searchbuild2)
    {
        this.searchbuild2 = searchbuild2;
    }
    
    public String getTot_post_str2()
    {
        return tot_post_str2;
    }
    
    public void setTot_post_str2(String tot_post_str2)
    {
        this.tot_post_str2 = tot_post_str2;
    }
    
    public String getInputjusogubun()
    {
        return inputjusogubun;
    }
    
    public void setInputjusogubun(String inputjusogubun)
    {
        this.inputjusogubun = inputjusogubun;
    }
    
    public String getRecordCall()
    {
        return recordCall;
    }
    
    public void setRecordCall(String recordCall)
    {
        this.recordCall = recordCall;
    }
    
    public String getPgyn()
    {
        return pgyn;
    }
    
    public void setPgyn(String pgyn)
    {
        this.pgyn = pgyn;
    }
    
    public String getPgcd()
    {
        return pgcd;
    }
    
    public void setPgcd(String pgcd)
    {
        this.pgcd = pgcd;
    }
    
    public String getBsi_mat_jibun()
    {
        return bsi_mat_jibun;
    }
    
    public void setBsi_mat_jibun(String bsi_mat_jibun)
    {
        this.bsi_mat_jibun = bsi_mat_jibun;
    }
    
    public String getBsi_mat_road()
    {
        return bsi_mat_road;
    }
    
    public void setBsi_mat_road(String bsi_mat_road)
    {
        this.bsi_mat_road = bsi_mat_road;
    }
    
    public String getEng_addr_yn()
    {
        return eng_addr_yn;
    }
    
    public void setEng_addr_yn(String eng_addr_yn)
    {
        this.eng_addr_yn = eng_addr_yn;
    }
    
    public String getRoadpnu()
    {
        return roadpnu;
    }
    
    public void setRoadpnu(String s_roadpnu)
    {
        roadpnu = s_roadpnu;
    }
    
    public String getRoadaddr()
    {
        return roadaddr;
    }
    
    public void setRoadaddr(String s_roadaddr)
    {
        roadaddr = s_roadaddr;
    }
    
    public String getErrchk()
    {
        return errchk;
    }
    
    public void setErrchk(String s_errchk)
    {
        this.errchk = s_errchk;
    }
    
    public String getSearchlike()
    {
        return searchlike;
    }
    
    public void setSearchlike(String s_searchlike)
    {
        this.searchlike = s_searchlike;
    }
    
    public String getDistrictzonno()
    {
        return districtzonno;
    }
    
    public void setDistrictzonno(String vdistrictzonno)
    {
        districtzonno = vdistrictzonno;
    }
    
    public String getNBsizonnoYn()
    {
        return nBsizonnoYn;
    }
    
    public void setNBsizonnoYn(String vnBsizonnoYn)
    {
        nBsizonnoYn = vnBsizonnoYn;
    }
    
}
