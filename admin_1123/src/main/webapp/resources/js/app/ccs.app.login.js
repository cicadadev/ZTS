var loginApp = angular.module("loginApp", [	'commonServiceModule','ccsServiceModule']);

loginApp.controller("loginController", function($window, $scope, ccsService, $httpParamSerializer, userService) {
	$window.$scope = $scope;
	$("meta[name='_system_type']").attr("content","BO");
	$scope.ccsUser = {};
	this.loginData = {j_username : '',j_password:'',_spring_security_remember_me:''}
	
	this.callback = function(response){
		
		var result=response.response;
		
		if(result.error){
			alert("로그인정보가 올바르지 않습니다.");
		}
		if(!result.error){
			location.href="/main";
		}
	}
			
	this.checkNetwork = function(){		
		
		if ($scope.frm.$invalid) return;
		// 외부망 체크
		ccsService.checkExternalNetwork(function(response) {
			
			// 임시 주석 처리
//			$scope.ctrl.login();
			if (response.content == 'true') {
				$scope.ctrl.login();
			} else {
				// TODO : ID, PASS 확인
				$scope.ccsUser.userId = frm.j_username.value;
				$scope.ccsUser.pwd = frm.j_password.value;
				userService.checkUserInfo($scope.ccsUser, function(response) {
					if (response.content == 'true') {
						$scope.ctrl.authExternalNetwork();
					} else {
						alert("로그인 정보가 일치하지 않습니다.");
					}
				});
			}
		});
	}
	
	this.login = function() {
		if ($scope.frm.$invalid) return;
		var data = $httpParamSerializer({
										j_username : "BO," + frm.j_username.value, 
										j_password: frm.j_password.value									
							});
		//console.log("loginBo", data);
		ccsService.loginProcess(data,this.callback);	
	}
	
	this.linkGuide = function(){
		location.href = "/samplePage/guide";
	}
	
	// 비밀번호 찾기
	this.findPwd = function() {
		$scope.systemType = "BO";
		var winName='비밀번호찾기';
		var winURL = Rest.context.path +"/ccs/user/popup/findPwd";
		popupwindow(winURL,winName,340,230);
	}
	
	this.authExternalNetwork = function() {
		var winName='외부망인증';
		var winURL = Rest.context.path +"/ccs/user/popup/authExternalNetwork";
		popupwindow(winURL,winName,360,250);
	}
	
	//iframe일때 parent 이동.
	if(parent.$scope._main_page ){
		parent.location.href = "/ccs/login";
	}
	
	var opener = $window.opener;
	while(opener) {
		$window.close();
		opener.location.href = "/ccs/login";
		opener = opener.opener;
	}
	
	
	
}).controller("loginPoController", function($window, $scope, ccsService, $httpParamSerializer) {
	$window.$scope = $scope;
	$("meta[name='_system_type']").attr("content","PO");
	
	this.loginData = {j_username : '',j_password:'',_spring_security_remember_me:''}
	
	this.callback = function(response){
		
		var result=response.response;
		alert(result.message);
		
		if(!result.error){
			location.href="/main";
		}
	}
			
	this.login = function(){		
		if ($scope.frm.$invalid) return;
		var data = $httpParamSerializer({
										j_username : "PO,"+frm.j_username.value, 
										j_password: frm.j_password.value									
							});
		console.log("loginPo", data);
		ccsService.loginProcess(data,this.callback);		
	}
	
	// 입점신청
	this.openRegBusiness = function(){
		//location.href = "/ccs/business/insert";
		var winName='입점신청';
		var winURL = Rest.context.path +"/ccs/business/insert";
		popupwindow(winURL,winName,1200,1000);
	}
	
	// 비밀번호 찾기
	this.findPwd = function() {
		$scope.systemType = "PO";
		var winName='비밀번호 찾기';
		var winURL = Rest.context.path +"/ccs/user/popup/findPwd";
		popupwindow(winURL,winName,340,250);
	}
	
	//iframe일때 parent 이동.
	if(parent.$scope._main_page ){
		parent.location.href = "/ccs/loginPo";
	}
	
	var opener = $window.opener;
	while(opener) {
		$window.close();
		opener.location.href = "/ccs/loginPo";
		opener = $window.opener;
	}
	
	
	
}).controller("findPwdController", function($window, $scope, ccsService, $httpParamSerializer, commonService, userService) {
	
	pScope = $window.opener.$scope;
	$scope.search = {};
	$scope.search.systemType = pScope.systemType;
	this.close = function() {
		$window.close();
	}
	
	this.findPwd = function() {
		if (!commonService.checkForm($scope.form)) {
			return;
		}
		$scope.search.myPwd = 'Y';
		userService.findPwd($scope.search, function(response) {
			if(response.content=='success'){
				if($scope.search.systemType=='PO'){
					alert("영업담당자 휴대폰 번호로 비밀번호가 전송되었습니다.");
				}else{
					alert("휴대폰 번호로 비밀번호가 전송되었습니다.");
				}
				$window.close();
			}else{
				alert("입력한 정보가 올바르지 않습니다.");
			}
			
			
		});
	}
	
}).controller("authExternalNetworkController", function($window, $scope, ccsService, $httpParamSerializer, commonService, userService) {
	var pScope = $window.opener.$scope;
	angular.element(document).ready(function () {
		
		// 인증번호 생성, 세션 생성 및 문자 발송
		ccsService.sendAuthSms(pScope.ccsUser, function(resposne) {
			
			// 3분 countdown
			countdown("count", 3, 0);
		});
		
	});
	
	
	function countdown( elementName, minutes, seconds )
	{
	    var element, endTime, hours, mins, msLeft, time;

	    function twoDigits( n )
	    {
	        return (n <= 9 ? "0" + n : n);
	    }

	    function updateTimer()
	    {
	        msLeft = endTime - (+new Date);
	        if ( msLeft < 1000 ) {
	            alert("시간이 만료 되었습니다.");
	        } else {
	            time = new Date( msLeft );
	            hours = time.getUTCHours();
	            mins = time.getUTCMinutes();
	            element.innerHTML = (hours ? hours + ':' + twoDigits( mins ) : mins) + ':' + twoDigits( time.getUTCSeconds() );
	            
	            setTimeout( updateTimer, time.getUTCMilliseconds() + 500 );
	        }
	    }

	    element = document.getElementById( elementName );
	    endTime = (+new Date) + 1000 * (60*minutes + seconds) + 500;
	    updateTimer();
	}
	
	// 인증번호 체크
	this.auth = function() {
		console.log($scope.authNumber);
		if ($scope.authNumber != null) {
			ccsService.checkAuthSms($scope.authNumber, function(response) {
				
				if (response.content == 'true') {
					pScope.ctrl.login();
					$window.close();
				} else {
					alert("인증번호가 잘못되었습니다.");
				}
			});
		}
	}
	
});
