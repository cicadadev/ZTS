
//consider dual screen
function popupwindow(url, title, w, h) {
	var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : screen.left;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : screen.top;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;

    var agent = navigator.userAgent.toLowerCase();
    
    // navigator.userAgent.search('Trident') != -1) : ie 11, (agent.indexOf("msie") != -1) : ie 10 이하
    if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)) {
    	// ie popup open시 target에 공백, - 있으면 error
    	title = title.replace(/\s|-/gi, '');
	}
    
    top-= 100;
    
    if(h > height){
    	h = 700;
    	top = 0;
    }
  
    if(top < 0 )top = 0;
    
    var newWindow = window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
};

/*
 * 정해진 일자의 받은 일수만큼 이전 이후일자를 계산해서 리턴해준다 
 * AUTH : Paul , 2016.05.04
 */

function dateCalculator(termDay,calDate,fmt){
	
	var myDate = null;
	if( calDate.substring(6,7) == "0" ){
		
		myDate = new Date(calDate.substring(0,4), 
						  parseInt(calDate.substring(4,6),10)-1,
						  parseInt(calDate.substring(7,8),10)+parseInt(termDay,10)
						  ).format(fmt);
	}
	else{
		myDate = new Date(calDate.substring(0,4), 
				          parseInt(calDate.substring(4,6),10)-1,
				          parseInt(calDate.substring(6,8),10)+parseInt(termDay,10)
				          ).format(fmt);
	}
	
    return 	myDate;
};

Date.prototype.format = function(f){
	
	if (!this.valueOf()) return " ";
	 
    var weekName = ["SUN", "MON", "TUE", "WED", "THE", "FRI", "SAT"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "AM" : "PM";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

/*
 * input 태그의 validation 체크
 * null , 공백 , 특수문자 포함여부
 */
function inputValuecheck(strTmp){
	 
	 var tmp = strTmp;
	 var tmp1 = tmp.charAt(0).charCodeAt();
	 var tmp2 = tmp.search(/[`~!@#$%^&*|\\\'\";:\/?]/gi);
	 
	 if( tmp == null || tmp == "" ){
		 
		 alert("인증번호를 입력 바랍니다.");
		 return false;
	 }
	 else if(tmp1 == "32"){
		 alert("인증번호에 공백을 입력할 수 없습니다.");
		 return false;
	 }
	 else if( tmp2 > -1 ){
		 alert("특수문자는 입력할 수 없습니다.");
		 return false;
	 }
	 else{
		 return true;
	 }
	
};

/*
 * 난수 발생 
 */

function setId(){
	
	var a = Math.floor(Math.random()*10) + 1 ;
	var b = Math.floor(Math.random()*10) + 1 ;
	var c = Math.floor(Math.random()*10) + 1 ;
	var d = Math.floor(Math.random()*10) + 1 ;
	 
	 
	return a+""+b+""+c+""+d;
} 

