<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://cdnjs.cloudflare.com/ajax/libs/systemjs/0.19.26/system.js"></script>
<script src="https://code.angularjs.org/2.0.0-beta.15/Rx.umd.js"></script>
<script src="https://code.angularjs.org/2.0.0-beta.15/angular2.js"></script>
<script src="https://code.angularjs.org/2.0.0-beta.15/angular2-polyfills.js"></script>
<script src="https://code.angularjs.org/2.0.0-beta.15/angular2-all.umd.js"></script>
<script src="https://code.angularjs.org/2.0.0-beta.15/http.js"></script>

<script>


  var helloApp = ng.core.Component({
	  selector: 'hello-app',
	  template: '<h1>hello angular 2!{{TEST}}</h1>'	  
  })
  .Class({
	  constructor: function(){
		  this.TEST = "bindingData";
	  }
  })
  
  var helloApp2 = ng.core.Component({
	  selector: 'hello-app2',
	  template: '<h1>hello angular 2!!!!!!!!!!!</h1>'+
	  '<button (click)="callHttp()" >go http</button>'
  })
  .Class({
	  constructor: function(){},
	  
  	  callHttp: function(){  		  
  		  return ngHttp.get('http://localhost:8080/gcp2.0/oms/order/list').map().Catch(this.handleError);
  	  },
	  
	  handleError: function(response){
		  console.log(response);		  
	  }
  })
  
  
  var appComponent = ng.core.Component({
	  selector: 'my-app',
	  template: '<hello-app></hello-app>'+
	  			'<hello-app2></hello-app2>',
	  directives: [helloApp,helloApp2]
  })
  .Class({
	  constructor: function(){}
  })
  
  document.addEventListener('DOMContentLoaded', function() {
	  ng.platform.browser.bootstrap(appComponent);
	});
  
</script>
<title>Order List</title>
</head>
<body>	
	<my-app></my-app>
</body>
</html>