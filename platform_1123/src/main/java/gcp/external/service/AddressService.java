package gcp.external.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.kainess.addressSearch4.LinkGVIA;
import gcp.external.kainess.vo.GviaAddressMultVo;
import gcp.external.kainess.vo.GviaAddressToSearchVo;
import gcp.external.kainess.vo.GviaAddressVo;
import gcp.external.kainess.vo.GviaDistrictMultVo;
import gcp.external.kainess.vo.GviaDistrictVo;
import gcp.external.kainess.vo.GviaPostSearchNewMultVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.kainess.vo.GviaPostSearchTotCountVo;
import gcp.external.model.search.AddressSearch;
import intune.gsf.common.utils.Config;

/**
 * 주소 서비스
 * 
 * @Pagckage Name : gcp.external.service
 * @FileName : MembershipService.java
 * @author : eddie
 * @date : 2016. 8. 20.
 * @description :
 */
@Service("addressService")
public class AddressService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(AddressService.class);


	/**
	 * 
	 * @Method Name : getDistrictInfo
	 * @author : allen
	 * @date : 2016. 8. 23.
	 * @description : 행정구역 조회 
	 *
	 *	행정구역 검색 구분자
	 * A(0x41)	행정동 검색 : 시/도
	 * B(0x42)	행정동 검색 : 시/군/구
	 * C(0x43)	행정동 검색 : 읍/면/동(행정동)
	 * D(0x44)	행정동 검색 : 읍/면/동(법정동)
	 * E(0x45)	행정동 검색 : 리(법정동)
	 * 0(0x30)	도로명 검색 : 시/군/구 코드
	 * 1(0x31)	도로명 검색 : 읍/면 코드
	 *
	 * @return
	 */
	public List<GviaDistrictVo> getDistrictInfo(AddressSearch search) {
		
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");

		String searchFlag = "";
		String searchStr = "";

		if (StringUtils.isEmpty(search.getSearchFlag())) {
			searchFlag = "A"; // 검색모드설정(부록2 참고)
		} else {
			searchFlag = search.getSearchFlag();
		}
		if (StringUtils.isEmpty(search.getSearchStr())) {
			searchStr = "0000000000"; // 검색어
		} else {
			searchStr = search.getSearchStr();
		}

		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시 서버 직접설정
		List<GviaDistrictVo> gviaDistrictVoList = new ArrayList<GviaDistrictVo>();
		
		GviaDistrictMultVo gviaDistrictMultVo = gvia.DistrictSearchVo(searchFlag, searchStr); // 행정구역검색vo 호출
		String resultcd = gviaDistrictMultVo.getResultcd();// 검색성공여부 확인
		if (resultcd.equals("1")) // 성공(결과 있음)
		{
			GviaDistrictVo gviaDistrictVo = new GviaDistrictVo();// 일반개별정보 class
			for (int i = 0; i < gviaDistrictMultVo.getCount(); i++)  // 검색수량에 따른 표시
			{
				gviaDistrictVo = gviaDistrictMultVo.getEntity(i); // 개별정보 가져오기
				logger.debug("  ==> * 코드  : " + gviaDistrictVo.getRegioncd());
				logger.debug("  ==> * 명칭  : " + gviaDistrictVo.getRegionnm());
				gviaDistrictVoList.add(gviaDistrictVo);
			}
		}
		return gviaDistrictVoList;
	}
	
	/**
	 *  주소 검색
	 * @Method Name : getSearchAddress
	 * @author : allen
	 * @date : 2016. 8. 23.
	 * @description : 
	 *
	 * L :	기초구역번호(5자리) [새주소-개별출력]
	 * M :	[[시/도]+[시도+시/군/구]+]+동+주번지+"-"+부번지 LIKE 검색 [새주소-개별출력]
	 * N : 	[[시/도]+[시도+시/군/구]+]+동+주번지+"-"+부번지 정명칭 검색 [새주소-개별출력]
	 * O :	[[시/도]+[시도+시/군/구]+]+도로명+건물본번+"-"+건물부번 LIKE 검색 [새주소-개별출력]
	 * P : 	[[시/도]+[시도+시/군/구]+]+도로명+건물본번+"-"+건물부번 정명칭 검색 [새주소-개별출력]
	 * Q :	행정구역별 기초구역번호 LIKE검색 [[시/도]+[시도+시/군/구]+]+읍/면/동/리 [새주소-개별출력]
	 * R :	행정구역별 기초구역번호 정명칭 검색 [[시/도]+[시도+시/군/구]+]+읍/면/동/리 [새주소-개별출력]
	 * q :	행정구역(단건) 기초구역번호 LIKE 검색 [[시/도]+[시도+시/군/구]+]+읍/면/동/리 [새주소-개별출력]
	 * r :	행정구역(단건) 기초구역번호 정명칭 검색 [[시/도]+[시도+시/군/구]+]+읍/면/동/리 [새주소-개별출력]
	 * W :	지번,도로명 통합 우편번호 검색(뒤 유사검색) [새주소-개별출력]
	 * w :	지번,도로명 통합 우편번호 검색(앞뒤 유사검색) [새주소-개별출력]
	 * X :	지번,도로명 통합 우편번호 레코드 건수 검색 [새주소-개별출력]
	 * Y :	[[시/도]+[시도+시/군/구]+]+건물명 뒤LIKE검색 [새주소-개별출력]
	 * y :	[[시/도]+[시도+시/군/구]+]+건물명 앞뒤LIKE검색 [새주소-개별출력]
	 * Z :	[[시/도]+[시도+시/군/구]+]+건물명 정명칭검색 [새주소-개별출력]
	 *
	 */
	public List<GviaPostSearchNewVo> getSearchAddress(AddressSearch search) {

		GviaPostSearchNewMultVo arrlistSearch = new GviaPostSearchNewMultVo();
		List<GviaPostSearchNewVo> gviaPostSearchNewVoList = new ArrayList<GviaPostSearchNewVo>();
		String searchflag = "";
		String sido = "";
		String gungu = "";
		String searchStr = "";
		int pageNo = 1;
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");

		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시

//	    sido = "서울특별시";
		sido = "";
		gungu = "";

		// searchflag = "L"; searchStr = "03175"; // 기초구역번호
		// searchflag = "M"; searchStr = "상동"; // 동/리명+지번 유사
		// searchflag = "N"; searchStr = "가산동 60-11"; // 동/리명+지번 정명칭
		// searchflag = "O"; searchStr = "디지털로9 99"; // 도로명+건물번호 유사
		// searchflag = "P"; searchStr = "디지털로9길 99"; // 도로명+건물번호 정명칭
		// searchflag = "Y"; searchStr = "스타"; // 건물명 유사
		// searchflag = "Z"; searchStr = "스타밸리"; // 건물명 정명칭
		
		searchflag = "W";

		if (StringUtils.isNotEmpty(search.getSidoName())) {
			sido = search.getSidoName(); // 통합검색
		}
		if (StringUtils.isNotEmpty(search.getSigunguName())) {
			gungu = search.getSigunguName(); // 통합검색
		}
		if (StringUtils.isNotEmpty(search.getSearchKeyword())) {
			searchStr = search.getSearchKeyword(); // 통합검색
		}

		if (StringUtils.isNotEmpty(search.getPageNo())) {
			pageNo = Integer.parseInt(search.getPageNo()); // 통합검색
		} else {
			pageNo = search.getCurrentPage();
		}

		searchStr = gvia.MakeGviaSearchData(sido, gungu, searchStr);
		arrlistSearch = gvia.PostSearchNewVo(searchflag, searchStr, pageNo, 10); // 검색 결과

		logger.debug("** Input searchflag  : " + searchflag);
		logger.debug("** Input searchStr   : " + searchStr);
		logger.debug("** Output ErrCode   : " + arrlistSearch.getErrorCode());

		logger.debug("  ==> * 결과코드  : " + arrlistSearch.getResultcd());
		logger.debug("  ==> * 전체 레코드 수  : " + arrlistSearch.getTotrecord());
		logger.debug("  ==> * 시작레코드 번호 : " + arrlistSearch.getStrecord());
		logger.debug("  ==> * 반복 레코드 수  : " + arrlistSearch.getRorecord());

		int nResult = arrlistSearch.getResult();

		if (arrlistSearch.getResultcd().equals("1")) {
			if (nResult == 0) // 우편번호 결과
			{
				GviaPostSearchNewVo entitySearchList = null;
				for (int j = 0; j < arrlistSearch.getCount(); j++) {
					entitySearchList = arrlistSearch.getEntity(j);
					gviaPostSearchNewVoList.add(entitySearchList);
					logger.debug(
							"  ==> *-------------------------------------------------------------------------------------------------");
					logger.debug("  ==> * No.  : " + (j + 1));
					logger.debug("  ==> * 기초구역번호 : " + entitySearchList.getBsizonno());
					logger.debug("  ==> * 우편번호 : " + entitySearchList.getPostno());
					logger.debug("  ==> * 우편일련번호 : " + entitySearchList.getPostseq());
					logger.debug("  ==> * 광역시/도 : " + entitySearchList.getSido());
					logger.debug("  ==> * 시/군/구 : " + entitySearchList.getGungu());
					logger.debug("  ==> * 읍/면/동 : " + entitySearchList.getDong());
					logger.debug("  ==> * 리 : " + entitySearchList.getRi());
					logger.debug("  ==> * 도로명 : " + entitySearchList.getRoadnm());
					logger.debug("  ==> * 지하구분 : " + entitySearchList.getUndercd());
					logger.debug("  ==> * 지하여부 : " + entitySearchList.getUnder());
					logger.debug("  ==> * 건물본번 : " + entitySearchList.getBuildmn());
					logger.debug("  ==> * 건물부번 : " + entitySearchList.getBuildsn());
					logger.debug("  ==> * 건물명 : " + entitySearchList.getBuildnm());
					logger.debug("  ==> * 도로명코드 : " + entitySearchList.getRoadcd());
					logger.debug("  ==> * 읍면일련번호 : " + entitySearchList.getDongseq());
					logger.debug("  ==> * 산구분 : " + entitySearchList.getSancd());
					logger.debug("  ==> * 산여부 : " + entitySearchList.getSan());
					logger.debug("  ==> * 주번지 : " + entitySearchList.getBunji());
					logger.debug("  ==> * 부번지 : " + entitySearchList.getHo());
					logger.debug("  ==> * 법정동코드 : " + entitySearchList.getBdongcd());
					logger.debug("  ==> * 행정동코드 : " + entitySearchList.getHdongcd());
					logger.debug("  ==> * 법정동명 : " + entitySearchList.getBdongnm());
					logger.debug("  ==> * 행정동명 : " + entitySearchList.getHdongnm());

					logger.debug("  ==> * 광역시/도(영문) : " + entitySearchList.getEng_sido());
					logger.debug("  ==> * 시/군/구(영문) : " + entitySearchList.getEng_gungu());
					logger.debug("  ==> * 읍/면/동(영문) : " + entitySearchList.getEng_dong());
					logger.debug("  ==> * 리(영문) : " + entitySearchList.getEng_ri());
					logger.debug("  ==> * 도로명(영문) : " + entitySearchList.getEng_roadnm());
				}
			} else {
				GviaPostSearchTotCountVo entitySearchList2 = null;
				for (int j = 0; j < arrlistSearch.getCount(); j++) {
					entitySearchList2 = arrlistSearch.getCountEntity(j);

					logger.debug(
							"  ==> *-------------------------------------------------------------------------------------------------");
					logger.debug("  ==> * No.  : " + (j + 1));
					logger.debug("  ==> * 주소구분  : " + entitySearchList2.getAd_jr());
					logger.debug("  ==> * 서울-인덱스/건수  : " + entitySearchList2.getStartrcd_se() + " / "
							+ entitySearchList2.getCount_se());
					logger.debug("  ==> * 부산-인덱스/건수  : " + entitySearchList2.getStartrcd_bs() + " / "
							+ entitySearchList2.getCount_bs());
					logger.debug("  ==> * 대구-인덱스/건수  : " + entitySearchList2.getStartrcd_dg() + " / "
							+ entitySearchList2.getCount_dg());
					logger.debug("  ==> * 인천-인덱스/건수  : " + entitySearchList2.getStartrcd_ic() + " / "
							+ entitySearchList2.getCount_ic());
					logger.debug("  ==> * 광주-인덱스/건수  : " + entitySearchList2.getStartrcd_gj() + " / "
							+ entitySearchList2.getCount_gj());
					logger.debug("  ==> * 대전-인덱스/건수  : " + entitySearchList2.getStartrcd_dj() + " / "
							+ entitySearchList2.getCount_dj());
					logger.debug("  ==> * 울산-인덱스/건수  : " + entitySearchList2.getStartrcd_us() + " / "
							+ entitySearchList2.getCount_us());
					logger.debug("  ==> * 세종-인덱스/건수  : " + entitySearchList2.getStartrcd_sj() + " / "
							+ entitySearchList2.getCount_sj());
					logger.debug("  ==> * 경기-인덱스/건수  : " + entitySearchList2.getStartrcd_gg() + " / "
							+ entitySearchList2.getCount_gg());
					logger.debug("  ==> * 강원-인덱스/건수  : " + entitySearchList2.getStartrcd_gw() + " / "
							+ entitySearchList2.getCount_gw());
					logger.debug("  ==> * 충북-인덱스/건수  : " + entitySearchList2.getStartrcd_cb() + " / "
							+ entitySearchList2.getCount_cb());
					logger.debug("  ==> * 충남-인덱스/건수  : " + entitySearchList2.getStartrcd_cn() + " / "
							+ entitySearchList2.getCount_cn());
					logger.debug("  ==> * 전북-인덱스/건수  : " + entitySearchList2.getStartrcd_jb() + " / "
							+ entitySearchList2.getCount_jb());
					logger.debug("  ==> * 전남-인덱스/건수  : " + entitySearchList2.getStartrcd_jn() + " / "
							+ entitySearchList2.getCount_jn());
					logger.debug("  ==> * 경북-인덱스/건수  : " + entitySearchList2.getStartrcd_gb() + " / "
							+ entitySearchList2.getCount_gb());
					logger.debug("  ==> * 경남-인덱스/건수  : " + entitySearchList2.getStartrcd_gn() + " / "
							+ entitySearchList2.getCount_gn());
					logger.debug("  ==> * 제주-인덱스/건수  : " + entitySearchList2.getStartrcd_jj() + " / "
							+ entitySearchList2.getCount_jj());
				}
			}
		} else {
			logger.debug("** 우편번호 검색 결과 없음");
		}
		return gviaPostSearchNewVoList;
	}


	/**
	 * 
	 * 주소 정제
	 * 
	 * @Method Name : getRefineAddress
	 * @author : allen
	 * @date : 2016. 8. 23.
	 * @description :
	 * 
	 * @return
	 */
	public void getRefineAddress(String addr) {
		
		String addressIp = Config.getString("address.ip");
		int addressPort = Config.getInt("address.port");
		
		LinkGVIA gvia = new LinkGVIA(addressIp, addressPort); // 프로퍼티 미사용 시
        GviaAddressMultVo arrlistAddr = null;
        
        String post = "";

		addr = "가산동";
        
        arrlistAddr = gvia.AddressVo(post, addr.trim(), 0);
        
        int nResult = arrlistAddr.getResult();
        
        if (nResult == 1) // 검색
        {
            GviaAddressToSearchVo entitySearchList = null;
            for (int j = 0; j < arrlistAddr.getCount(); j++)
            {
                entitySearchList = arrlistAddr.getSearchEntity(j);
                
				logger.debug(
						"  ==> *-------------------------------------------------------------------------------------------------");
				logger.debug("  ==> * 'A' : 행정구역 멀티, 'B' : 건물 멀티 : " + entitySearchList.getFlag());
				logger.debug("  ==> * 입력동구분 : " + entitySearchList.getDonggubun());
				logger.debug("  ==> * 멀티 주소 매칭 수 : " + entitySearchList.getResultcnt());
				logger.debug("  ==> * 나머지주소 : " + entitySearchList.getEtcAddr());
				logger.debug("  ==> * 우편번호 : " + entitySearchList.getPostno());
				logger.debug("  ==> * 광역시도명칭 : " + entitySearchList.getSido());
				logger.debug("  ==> * 시군구명칭 : " + entitySearchList.getGungu());
				logger.debug("  ==> * 읍면동명칭 : " + entitySearchList.getDong());
				logger.debug("  ==> * 리명칭 : " + entitySearchList.getRi());
				logger.debug("  ==> * 산 또는 지하구분 : " + entitySearchList.geSan_under());
				logger.debug("  ==> * 주번지 또는 주번호 : " + entitySearchList.getMn());
				logger.debug("  ==> * 부번지 또는 부번호 : " + entitySearchList.getSn());
				logger.debug("  ==> * 건물명 : " + entitySearchList.getBuildnm());
				logger.debug("  ==> * 법정동명 : " + entitySearchList.getBdongnm());
				logger.debug("  ==> * 행정동명 : " + entitySearchList.getHdongnm());
				logger.debug("  ==> * 도로명 : " + entitySearchList.getRoadnm());
            }
        }
        else
        {
            GviaAddressVo entityAddrList = null;
            for (int j = 0; j < arrlistAddr.getCount(); j++)
            {
                entityAddrList = arrlistAddr.getAddrEntity(j);
				logger.debug(
						"  ==> *-------------------------------------------------------------------------------------------------");
				logger.debug("  ==> * J:지번주소, R:도로명주소 : " + entityAddrList.getAd_jr());
				logger.debug("  ==> * 0:정상, 1:불능, 2:매칭실패 : " + entityAddrList.getAd_resultflag());
				logger.debug("  ==> * 변환코드 : " + entityAddrList.getAd_resultcd());
				logger.debug("  ==> * 지번주소 우편번호 : " + entityAddrList.getAd_postno());
				logger.debug("  ==> * 지번주소 우편번호일련번호 : " + entityAddrList.getAd_postseq());
				logger.debug("  ==> * 지번주소 국가기초구역번호 : " + entityAddrList.getAd_bsizonno());
				logger.debug("  ==> * 지번주소 기본주소(행정구역) : " + entityAddrList.getAd_add1());
				logger.debug("  ==> * 지번주소 상세주소 : " + entityAddrList.getAd_add2());
				logger.debug("  ==> *---------------------------------");
				logger.debug("  ==> * 도로명주소 우편번호 : " + entityAddrList.getAd_ro_postno());
				logger.debug("  ==> * 도로명주소 우편번호일련번호 : " + entityAddrList.getAd_ro_postseq());
				logger.debug("  ==> * 도로명주소 국가기초구역번호 : " + entityAddrList.getAd_ro_bsizonno());
				logger.debug("  ==> * 도로명주소 기본주소(건물번호까지) : " + entityAddrList.getAd_ro_add1());
				logger.debug("  ==> * 도로명주소 상세주소 : " + entityAddrList.getAd_ro_add2());
				logger.debug("  ==> * 도로명주소 참조주소,괄호주소 : " + entityAddrList.getAd_ro_ref());
				logger.debug("  ==> *---------------------------------");
				logger.debug("  ==> * 광역시/도명칭 : " + entityAddrList.getAd_sido());
				logger.debug("  ==> * 시/군/구명칭 : " + entityAddrList.getAd_gungu());
				logger.debug("  ==> * 읍/면/동명칭 : " + entityAddrList.getAd_dong());
				logger.debug("  ==> * 리명칭 : " + entityAddrList.getAd_ri());
				logger.debug("  ==> * 산구분 : " + entityAddrList.getAd_sancd());
				logger.debug("  ==> * 산여부 : " + entityAddrList.getAd_san());
				logger.debug("  ==> * 주번지 : " + entityAddrList.getAd_mn());
				logger.debug("  ==> * 부번지 : " + entityAddrList.getAd_sn());
				logger.debug("  ==> * 분석된 건물명 : " + entityAddrList.getAd_buildinfo());
				logger.debug("  ==> * 건물동 : " + entityAddrList.getAd_builddong());
				logger.debug("  ==> * 건물호 : " + entityAddrList.getAd_buildho());
				logger.debug("  ==> * 건물층 : " + entityAddrList.getAd_buildstair());
				logger.debug("  ==> * 건물추정정보 : " + entityAddrList.getAd_remain());
				logger.debug("  ==> * 입력동구분 : " + entityAddrList.getAd_dongcd());
				logger.debug("  ==> * 법정동코드 : " + entityAddrList.getAd_bdongcd());
				logger.debug("  ==> * 행정동코드 : " + entityAddrList.getAd_hdongcd());
				logger.debug("  ==> * 법정동명칭 : " + entityAddrList.getAd_bdongnm());
				logger.debug("  ==> * 행정동명칭 : " + entityAddrList.getAd_hdongnm());
				logger.debug("  ==> * 지번PNU : " + entityAddrList.getAd_pnucd());
				logger.debug("  ==> * X좌표 : " + entityAddrList.getAd_cx());
				logger.debug("  ==> * Y좌표 : " + entityAddrList.getAd_cy());
				logger.debug("  ==> * 좌표 부여 레벨 : " + entityAddrList.getAd_lev());
				logger.debug("  ==> * 도로명 : " + entityAddrList.getAd_ro_roadnm());
				logger.debug("  ==> * 도로코드 : " + entityAddrList.getAd_ro_roadcd());
				logger.debug("  ==> * 읍면동 일련번호 : " + entityAddrList.getAd_ro_dongseq());
				logger.debug("  ==> * 지하구분 : " + entityAddrList.getAd_ro_undercd());
				logger.debug("  ==> * 지하여부 : " + entityAddrList.getAd_ro_under());
				logger.debug("  ==> * 건물주번호 : " + entityAddrList.getAd_ro_buildmn());
				logger.debug("  ==> * 건물부번호 : " + entityAddrList.getAd_ro_buildsn());
				logger.debug("  ==> * 도로명 빌딩명칭 : " + entityAddrList.getAd_ro_buildnm());
				logger.debug("  ==> * 도로명 빌딩 상세 : " + entityAddrList.getAd_ro_builddtail());
				logger.debug("  ==> * 건물관리번호 : " + entityAddrList.getAd_ro_buildmgno());
				logger.debug("  ==> * 도로명주소 PNU : " + entityAddrList.getAd_ro_pnu());
				logger.debug("  ==> * 도로명주소 X좌표 : " + entityAddrList.getAd_ro_cx());
				logger.debug("  ==> * 도로명주소 Y좌표 : " + entityAddrList.getAd_ro_cy());
				logger.debug("  ==> * 안행부 배포 - 대표매칭여부 Y N : " + entityAddrList.getAd_reprematching());
				logger.debug("  ==> * 공동주택여부 Y N : " + entityAddrList.getAd_apathouse());
				logger.debug("  ==> * 주소파싱레벨 : " + entityAddrList.getAd_addrlev());
				logger.debug("  ==> * 우편번호 부여레벨 : " + entityAddrList.getAd_postlev());
				logger.debug("  ==> * 우편번호 사용 동타입 : " + entityAddrList.getAd_posttype());
				logger.debug("  ==> * 좌표부여 에러코드 : " + entityAddrList.getAd_errcd());
				logger.debug("  ==> * 정보변경코드 : " + entityAddrList.getAd_datachgcd());
				logger.debug("  ==> * 기초구역번호부여레벨 : " + entityAddrList.getAd_newpostlev());
				logger.debug("  ==> * 참조주소2(법정동/리, 건물명) : " + entityAddrList.getAd_ro_ref2());
				logger.debug("  ==> * 참조주소3(법정동/리 산 번지-호) : " + entityAddrList.getAd_ro_ref3());
				logger.debug("  ==> * 참조주소4(법정동/리 산 번지-호 건물명) : " + entityAddrList.getAd_ro_ref4());
				logger.debug("  ==> * 영문출력(시도) : " + entityAddrList.getAd_eng_sido());
				logger.debug("  ==> * 영문출력(시/군/구) : " + entityAddrList.getAd_eng_gungu());
				logger.debug("  ==> * 영문출력(읍/면/동) : " + entityAddrList.getAd_eng_dong());
				logger.debug("  ==> * 영문출력(리) : " + entityAddrList.getAd_eng_ri());
				logger.debug("  ==> * 영문출력(도로명) : " + entityAddrList.getAd_eng_roadnm());
            }
        }
    }
}
