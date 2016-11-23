/**
 * 필터 세팅
 */      
commonServiceModule.filter('displayYnFilter', function() {// 전시여부
	var comboHash = {
	    Y: '전시',
	    N: '미전시'
	};
	return function(input) {
	    if (!input){
	        return '';
	    } else {
	    	return comboHash[input];
	    }
	};
}).filter('useYnFilter', function() {// 사용/미사용
	var comboHash = {
		    Y: '사용',
		    N: '미사용'
		};
		return function(input) {
		    if (!input){
		        return '';
		    } else {
		    	return comboHash[input];
		    }
		};
}).filter('yesOrNoFilter', function() {// 예/아니오
	var comboHash = {
		    Y: '예',
		    N: '아니오'
		};
		return function(input) {
		    if (!input){
		        return '';
		    } else {
		    	return comboHash[input];
		    }
		};
}).filter('yyyymmdd', function() {// yyyy-mm-dd 
	
	var formmater = function(input){
		var date = new Date(input);
		
		var day = ('0' + date.getDate()).slice(-2);
 		var month = ('0' + (date.getMonth() + 1)).slice(-2);
 		var year = date.getFullYear();

		return year + '-' + month + '-' + day;
	}
	return formmater;
}).filter('price', function() { // 1,234 금액표시
	
	return function (input){
		var len, point, str;  
	       
		input = input + "";  
	    point = input.length % 3 ;
	    len = input.length;  
	   
	    str = input.substring(0, point);  
	    while (point < len) {  
	        if (str != "") str += ",";  
	        str += input.substring(point, point + 3);  
	        point += 3;  
	    }  
	     
	    return str;
	}
}).filter('fax', function () {// 전화번호 형식
    return function (tel) {
        //console.log(tel);
        if (!tel) { return ''; }

        var value = tel.toString().trim().replace(/^\+/, '');

        if (value.match(/[^0-9]/)) {
            return tel;
        }

        var country, city, number;

        switch (value.length) {
            case 1:
            case 2:
            case 3:
                city = value;
                break;

            default:
                city = value.slice(0, 4);
                number = value.slice(4);
        }

        if(city.slice(0,2).match(/^(02)/)){
        	number = city.slice(2,4) + value.slice(4);
        	if(number){
        		if(number.length>3){
        			if(number.length > 7){
        				number = number.slice(0, 4) + '-' + number.slice(4,8);
        				number = number.slice(0, 10);
        			}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);
        		}
        		else{
        			number = number;
        		}        		
        		return ("" + city.slice(0, 2) + "-" + number).trim();
        	}
        	else{
        		return "" + city;
        	}
        }else if(city.slice(0,3).match(/^(0(3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80))/)){
        	number = city.slice(3,4) + value.slice(4);
        	if(number){
                if(number.length>3){
                	if(number.length > 7){
                		number = number.slice(0, 4) + '-' + number.slice(4,8);
                		number = number.slice(0, 10);
                	}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);                	
                }
                else{
                    number = number;
                }
                return ("" + city.slice(0, 3) + "-" + number).trim();
            }
            else{
                return "" + city.slice(0, 3);
            }
        }else if(city.match(/^(0(50[2-7]))/)){
        	if(number){
                if(number.length>3){
                	if(number.length > 7){
                		number = number.slice(0, 4) + '-' + number.slice(4,8);
                		number = number.slice(0, 10);
                	}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);                	
                }
                else{
                    number = number;
                }
                return ("" + city + "-" + number).trim();
            }
            else{
                return "" + city;
            }
        }else{
        	return "" + city;
        }
        

    };
}).filter('tel', function () {// 전화번호 형식
    return function (tel) {
        //console.log(tel);
        if (!tel) { return ''; }

        var value = tel.toString().trim().replace(/^\+/, '');

        if (value.match(/[^0-9]/)) {
            return tel;
        }

        var country, city, number;

        switch (value.length) {
            case 1:
            case 2:
            case 3:
                city = value;
                break;

            default:
            	city = value.slice(0, 4);
            	number = value.slice(4);
        }

        if(city.slice(0,2).match(/^(02)/)){
        	number = city.slice(2,4) + value.slice(4);
        	if(number){
        		if(number.length>3){
        			if(number.length > 7){
        				number = number.slice(0, 4) + '-' + number.slice(4,8);
        				number = number.slice(0, 10);
        			}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);
        		}
        		else{
        			number = number;
        		}        		
        		return ("" + city.slice(0, 2) + "-" + number).trim();
        	}
        	else{
        		return "" + city;
        	}
        }else if(city.match(/^(0(1[0|1|6|7|8|9]|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80))/)){
        	number = city.slice(3,4) + value.slice(4);
        	if(number){
                if(number.length>3){
                	if(number.length > 7){
                		number = number.slice(0, 4) + '-' + number.slice(4,8);
                		number = number.slice(0, 10);
                	}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);                	
                }
                else{
                    number = number;
                }
                return ("" + city.slice(0, 3) + "-" + number).trim();
            }
            else{
                return "" + city.slice(0, 3);
            }
        }else if(city.match(/^(0(130|50[2-7]))/)){
        	if(number){
                if(number.length>3){
                	if(number.length > 7){
                		number = number.slice(0, 4) + '-' + number.slice(4,8);
                		number = number.slice(0, 10);
                	}
                	else
                		number = number.slice(0, 3) + '-' + number.slice(3,7);                	
                }
                else{
                    number = number;
                }
                return ("" + city + "-" + number).trim();
            }
            else{
                return "" + city;
            }
        }else{
        	return "" + city;
        }
    };
}).filter('reg', function () {// 전화번호 형식
    return function (reg) {
        //console.log(tel);
        if (!reg) { return ''; }

        var value = reg.toString().trim().replace(/^\+/, '');

        if (value.match(/[^0-9]/)) {
            return reg;
        }

        var first, middle, last;

        switch (value.length) {
            case 1:
            case 2:
            case 3:
            	first = value;
                break;

            default:
                first = value.slice(0, 3);
                middle = value.slice(3);
                last = middle.slice(2);
        }
        
        if(last){
        	if(last.length > 5){
        		return ("" + first + "-" + middle.slice(0,2) + "-" + last.slice(0, 5)).trim();
        	}else{
        		return ("" + first + "-" + middle.slice(0,2) + "-" + last).trim();
        	}
        }
        else if(middle){
        	return ("" + first + "-" + middle).trim();
        }else{
        	return "" + first;
        }
    };
});
