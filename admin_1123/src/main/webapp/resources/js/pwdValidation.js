/**
 * @type   : function
 * @access : public
 * @desc   : 문자, 특수문자, 숫자 중 2개 좁합일때 10자리이상 3개조합일때 8자리이상
 * <pre>사용예 :
 *      isChkUserPwd(frm.password1);
 * </pre>
 * @return : String 메세지
 */
function isChkUserPwd(value1, value2){
		if( value1 != value2) {
			alert("패스워드가 일치하지 않습니다.");
			return false;
		}

		if(/(\w)\1\1/.test(value1)){
			alert("비밀번호에 같은 문자를 3번 이상 반복할 수 없습니다.");
			return false;
		}
		
		
		if(value1.length <= 7){
			alert("비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 2종류 조합 10~20자리 또는 3종류 조합 8~20자리로 사용 가능합니다.");
			return false;
		} else if(value1.length <= 8){
			var chk_num = value1.search(/[0-9]/g);
			var chk_eng = value1.search(/[a-z]/ig);
			var chk_spe = value1.search(/[#\&\+\-%@=\/\\\:;,\.'\"\^`~\_|\!\?\*$#<>()\[\]\{\}]/g);
			if(chk_num < 0 || chk_eng < 0 || chk_spe < 0){
				alert("비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 2종류 조합 10~20자리 또는 3종류 조합 8~20자리로 사용 가능합니다.");
				return false;
			}
		} else if(value1.length >= 10){
			if(!/^[a-zA-Z0-9!@#$%^&*]{10,20}$/.test(value1)){
				alert("10자리 이상의 비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 2종류 조합 10~20자리로 사용 가능합니다.");
				return false;
			}
			var chk_num = value1.search(/[0-9]/g);
			var chk_eng = value1.search(/[a-z]/g);
			var chk_spe = value1.search(/[#\&\+\-%@=\/\\\:;,\.'\"\^`~\_|\!\?\*$#<>()\[\]\{\}]/g);
			var check = 0;

			if(chk_num != -1){ check++; }
			if(chk_eng != -1){ check++; }
			if(chk_spe != -1){ check++; }

			if(check < 2){
				alert("10자리 이상의 비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 2종류 조합 10~20자리로 사용 가능합니다.");
				return false;
			}
		} else if(value1.length <= 9){
			if(!/^[a-zA-Z0-9#\&\+\-%@=\/\\\:;,\.'\"\^`~\_|\!\?\*$#<>()\[\]\{\}]{8,20}$/.test(value1)){
				alert("비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 2종류 조합 10~20자리 또는 3종류 조합 8~20자리로 사용 가능합니다.");
				return false;
			}
			var chk_num = value1.search(/[0-9]/g);
			var chk_eng = value1.search(/[a-z]/ig);
			var chk_spe = value1.search(/[#\&\+\-%@=\/\\\:;,\.'\"\^`~\_|\!\?\*$#<>()\[\]\{\}]/g);
			if(chk_num < 0 || chk_eng < 0 || chk_spe < 0){
				alert("10자리 미만의 비밀번호는 영문자(대소문자 구분), 숫자, 특수문자 중 3종류 조합 8~20자리로 사용 가능합니다.");
				return false;
			}
		}
		
		
	return true;
}

/**
 * 비밀번호에 아이디가 포함되어있는지 체크한다
 * 사용예 : if(chkSeqUserId(frm.password, frm.login_id))
 */
function chkSeqUserId(value1, value2) {
	var pw = value1;
	var id = value2;
	for (var i=0; i<id.length; i++) { 
		var id2 = id.substr(i) + id.substr(0, i);   
	    if (id2.indexOf(pw)>-1 || pw.indexOf(id2)>-1) {
	    	alert("비밀번호에 아이디를 포함할수 없습니다.");
			return false;
	   	}
  	}
	return true;
}


/**
 * 비밀번호에 'qwe', 'asd'와같이 키보드상 연속된 문자를 사용했는지 체크
 * 사용예 : if(chkSeqQwer(frm.password))
 */
function chkSeqQwer(value) {
	var pw = value;
	var qwerty = "qwertyuiopasdfghjklzxcvbnm";
	var start = 3 - 1;
	var seq = "_" + pw.slice(0, start);
	for ( var i = start; i < pw.length; i++) {
		seq = seq.slice(1) + pw.charAt(i);
		if (qwerty.indexOf(seq) > -1) {
			alert("비밀번호에 키보드상 연속된 문자를 사용할수 없습니다.");
			return false;
		}
	}
	return true;
}

/**
 * 비밀번호에 'abc', '123'와같이 연속된 문자를 사용했는지 체크
 * 사용예 : if(chkSeqQwer(frm.password))
 */
function chkSeqChar(str, limit) {
	var o, d, p, n = 0, l = limit == null ? 3 : limit;
	for ( var i = 0; i < str.length; i++) {
		var c = str.charCodeAt(i);
		if (i > 0 && (p = o - c) > -2 && p < 2
				&& (n = p == d ? n + 1 : 0) > l - 3) {
			return false;
		}
		d = p, o = c;
	}
	return true;
}

/**
 * 비밀번호에 전화번호가 포함되어있는지 체크
 * 사용예 : if(chkNumber(frm.emp_tel1.value, frm.emp_tel2.value, frm.emp_tel3.value, frm.password.value))
 */
function chkNumber(phone, pass) {
	phone = phone.replace("-", "");
	var patternPhone1 = new RegExp(phone);
	if(phone != ""){
		if(patternPhone1.test(pass,"(\d)")) {
			alert("비밀번호에 전화번호를 포함할 수 없습니다.");
			return false;
		}
	}
	return true;
}