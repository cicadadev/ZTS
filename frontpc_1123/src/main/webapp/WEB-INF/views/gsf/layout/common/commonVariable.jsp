<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.mms.model.custom.FoLoginInfo" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ page import="gcp.frontpc.common.util.FrontUtil"%>
<%@ page import="intune.gsf.common.utils.CookieUtil"%>
<%
	if(FoSessionUtil.isMemberLogin()){
		FoLoginInfo loginInfo = (FoLoginInfo)FoSessionUtil.getLoginInfo(); 
		request.setAttribute("loginId", loginInfo.getLoginId());
		request.setAttribute("loginName", loginInfo.getLoginName());
		request.setAttribute("membershipYn", loginInfo.getMembershipYn() );
		request.setAttribute("premiumYn", loginInfo.getPremiumYn() );
		request.setAttribute("customerNo", loginInfo.getCustomerNo());
		request.setAttribute("employeeYn", loginInfo.getEmployeeYn());
		request.setAttribute("memberNo", loginInfo.getMemberNo());
		request.setAttribute("memGradeCd", loginInfo.getMemGradeCd());
		request.setAttribute("childrenDealId", loginInfo.getChildrenDealId());
	}	


	String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
	request.setAttribute("deviceTypeCd", deviceTypeCd );
	
	boolean isMobile = FoSessionUtil.isMobile(request);
	
	if(isMobile){
		request.setAttribute("_deviceType", "mo");
	}else{
		request.setAttribute("_deviceType", "pc");
	}
	request.setAttribute("isMobile", isMobile);
	
	boolean isApp = FoSessionUtil.isApp(request);
	request.setAttribute("isApp", isApp);
	
	boolean isSsecure = request.isSecure();
	String _IMAGE_DOMAIN_ = Config.getString("image.domain");
	request.setAttribute("_httpUrl", "http://");
	if(isSsecure){
		_IMAGE_DOMAIN_ = Config.getString("image.ssl.domain");
		request.setAttribute("_httpUrl", "https://");
	}
	request.setAttribute("_IMAGE_DOMAIN_", _IMAGE_DOMAIN_);
	
	String _FRONT_DOMAIN_URL_ = Config.getString("front.domain.url");
	request.setAttribute("_FRONT_DOMAIN_URL_", _FRONT_DOMAIN_URL_);
	//제휴 채널
	request.setAttribute("_CHANNEL_ID_", FoSessionUtil.getChannelId());
%>
<!DOCTYPE html>
<script type="text/javascript">
	global = {
			corner : {
				headerImg : '<%=Config.getString("corner.common.banner.img.1")%>', 
				skyScraperLeft :  	'<%=Config.getString("corner.common.banner.img.2")%>',  
				searchWord	 :  '<%=Config.getString("corner.etc.search")%>' ,
				skyScraperRgt	 :  '<%=Config.getString("corner.common.banner.img.3")%>' ,
				ageImg	 :  '<%=Config.getString("corner.common.ctg.img.5")%>' ,
				ctgImg1	 :  '<%=Config.getString("corner.common.ctg.img.1")%>' ,
				ctgImg2	 :  '<%=Config.getString("corner.common.ctg.img.2")%>' ,
				ctgImg3	 :  '<%=Config.getString("corner.common.ctg.img.3")%>' ,
				ctgImg4	 :  '<%=Config.getString("corner.common.ctg.img.4")%>',
				milk1	 :  '<%=Config.getString("corner.special.milk1.img.2")%>'
					},
			config : {
				imageDomain :  	'<%=_IMAGE_DOMAIN_%>', //이미지도메인( global.config.imageDomain )
				domainUrl :  '<%=Config.getString("front.domain.url")%>' ,//도메인URL ( global.config.domainUrl )
				domainSslUrl :  '<%=Config.getString("front.domain.ssl.url")%>',//도메인URL ( global.config.domainUrl )
				membershipUrl : '<%=Config.getString("membership.login.url")%>',// 멤버십 로그인 화면
				joinUrl : '<%=Config.getString("membership.join.url")%>',// 멤버십 가입 화면
				memberUpdateUrl : '<%=Config.getString("membership.modify.url")%>',// 멤버십 수정 화면
				memberDisagreeUrl : '<%=Config.getString("membership.disagree.url")%>',// 멤버십 탈퇴 화면 
				memberPwChangeUrl : '<%=Config.getString("membership.pwchange.url")%>',// 비밀번호 변경 화면
				memberMktAgreeUrl : '<%=Config.getString("membership.mktagree.url")%>',// 마케팅동의여부 변경 화면
				logoutUrl : '<%=Config.getString("membership.logout.url")%>',// 멤버십 로그아웃
				recobelCuid : '<%=Config.getString("recobell.cuid")%>'// 레코벨 CUID
				},
			channel : {
				isMobile : '<%=isMobile%>',
				isApp : '<%=isApp%>'
			},
			member : {
				coopcoCd : '7020',// 멤버십API 제휴사 코드-제로투세븐
				babyGenderCd :'<%=FoSessionUtil.getRecobellBabyGenderCd()%>',
				babyMonthCd :'<%=FoSessionUtil.getRecobellBabyMonthCd()%>'
			},
			RB_PCID : '<%=CookieUtil.getCookieValue(request, "RB_PCID")%>',
			main : { current_Index : '<%=request.getParameter("crtIndex")%>'},
			
		};
</script>
