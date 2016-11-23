//사용할 메세지 키 정의
//Constants.message_keys = ["SPS.COUPON.REG.INFO", "SPS.COUPON.REG.INFO2"];

App.service('Sample', ['CommonFactory',function(CommonFactory){
	var url = Rest.context.path + "/sample/sample/:id";
	//var param = {id: '@sampleId'};
	var param = {};
	return {
		
		getSampleList : function(){			
			return CommonFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null);	
		},
		createSample : function(data, callback){
			return CommonFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		updateSample : function(data, callback){
			return CommonFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		deleteSample : function(data, callback){
			return CommonFactory.transaction(Rest.method.DELETE, Rest.responseType.VOID, url, param, data, callback);
		},
		getSample : function(data, callback){	
			return CommonFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		getSample2 : function(data, callback){		
			var url = Rest.context.path + "/sample/sample2/:id";
			return CommonFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		getSample3 : function(data, callback){		
			var url = Rest.context.path + "/sample/sample3/:id";
			return CommonFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		}
		
	}
}]);



App.controller('SampleController',['$scope','$window','Sample','uiGridConstants','uiGridValidateService', 'Commons', function($scope,$window,Sample,uiGridConstants,uiGridValidateService, Commons){
	var self = this;
	self.sample = {sampleId: null ,sampleName: null};
	self.sampleList = [];	
	
	//column template
	$scope.cell_template = '<button type="button" data-ng-click="grid.appScope.edit(grid.getCellValue(row,col))" >Edit</button>  <button type="button" data-ng-click="grid.appScope.remove(grid.getCellValue(row,col))">Remove</button>';
	
	$scope.grid_valid = true;
	//grid object
	$scope.grid_sample = {
 			showGridFooter: true,
 		    showColumnFooter: true,
 			//enableFiltering: true,
 			enableGridMenu: true,
 			paginationPageSizes: [5, 10, 25],
 		    paginationPageSize: 5,
 			exporterCsvFilename: 'fileName.csv',
 			exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")), 			
 			columnDefs: [
 			             
 			             { field: 'sampleId'	, //object property
 			               displayName: "ID",	//column name
 			               width: '100', 		//width
 			               enableCellEdit:true, //edit
 			               enableSorting: true,	//sort
 			               validators: {minLength: 0, maxLength: 1}, //validator
 			               cellTemplate: 'ui-grid/cellTitleValidator', //validator template
 			               aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true	//summary 
 			             },
 			             { field: 'sampleName'	, displayName: "NAME", 	width: '100', enableCellEdit:true, enableSorting: true,
 			            	 					  validators: {required:true, maxLength:5}, 
 			            	 					  cellTemplate: 'ui-grid/cellTitleValidator'  }  , 			    
 			             { field: 'sampleId'	, displayName: "", 		width: '100', enableCellEdit:false, enableSorting: false,
 			            	 					  cellTemplate : $scope.cell_template //cellTemplate 
 			             }
 			         ]
 			};	
	
	//grid event callback
	$scope.grid_sample.onRegisterApi = function(gridApi){
        //set gridApi on scope
        $scope.gridApi = gridApi;
        
        //validation fail
        gridApi.validate.on.validationFailed($scope,function(rowEntity, colDef, newValue, oldValue){
        	
        	var msg =  'rowEntity: '+ rowEntity.sampleId + '\n' +
			            'colDef: ' + colDef.displayName + '\n' + 
			            'newValue: ' + newValue + '\n' +
			            'oldValue: ' + oldValue;        	
        	msg = uiGridValidateService.getErrorMessages(rowEntity, colDef);
        	$scope.grid_valid = {status:false,msg:msg};
            
        });
        
//        angular.forEach($scope.grid_sample.columnDefs, function(value,key){
//        	console.log($scope.val_data);
//        	var validators='';
//    		angular.forEach($scope.val_data,function(valValue,key){
//    			
//    			if(value.field.indexOf(valValue.msgCd) > -1){				
//    				console.log("insert");
//    				//length check
//    				if(valValue["length"] > 0){
//    					validators += "maxLength: "+valValue["length"]+",";
//    				}
//    				//null check
//    				if(valValue["nullYn"] == 'N'){
//    					validators += "required, true,";
//    				}								
//    			} 				
//    		});				
//    		console.log(validators);
//    		value.validators = "{"+validators+"}";
//    	});
//    	
//    	console.log($scope.grid_sample.columnDefs);
      };

	
	//callback function
	 self.callback = function(result){
		 if(result!='undefined' && result!='' && result!=null){
			 alert(result);
		 }
		 self.fetchAllSample();
	 }	
	//search List
	 self.fetchAllSample = function(){
         self.sampleList = Sample.getSampleList();	//scope sampleList         
         $scope.grid_sample.data = self.sampleList; 	//grid binding
     };
       
     //create
     self.createSample = function(){
    	 Sample.createSample(self.sample,self.callback);                  
     };
      
     //update
     self.updateSample = function(){    
    	 if(!$scope.grid_valid.status){	//validation fail시 담김.
    		 alert($scope.grid_valid.msg);
 }else{
    		 Sample.updateSample(self.sample,self.callback);	 
    	 }    	 
         
     };

     
     
     //delete
     self.deleteSample = function(identity){
        Sample.deleteSample({id:identity}, self.callback);
     };
     //get
     self.getSample = function(identity){
        Sample.getSample({id:identity}, function(response){
        	
        	if(response.success){
        		alert(response.sampleName);
        	}else{
        		alert(response.resultMessage);
        	}
        	
        });
     };
     self.getSample2 = function(identity){
         Sample.getSample2({id:identity}, function(response){
         	if(response.success){
        		alert(response.sampleName);
        	}else{
        		alert(response.resultMessage);
        	}
         	
         });
      };
      self.getSample3 = function(identity){
          Sample.getSample3({id:identity}, function(response){
          	if(response.success){
         		alert(response.sampleName);
         	}else{
         		alert(response.resultMessage);
         	}
          	
          });
       };      
 	//search List
	 self.getMessages = function(){
        // self.sampleList = Sample.getMessages();	
         Commons.getMessages(function(response){
        	 
             $scope.MESSAGES = response; 	//grid binding
             var messages = $scope.MESSAGES;
             var key = "SPS.COUPON.REG.INFO";
             //alert("massage:" + messages[key]);
             
         }); 	//grid binding
  
     };
     
     //조회
     self.fetchAllSample();
     
     self.getMessages();
     
     //form submit
     self.submit = function() {
         if(self.sample.sampleId==null){
             //console.log('Saving New Sample', self.sample);    
             self.createSample();
         }else{
             //console.log('Upddating sample with id ', self.sample.sampleId);
             self.updateSample();
             //console.log('Sample updated with id ', self.sample.sampleId);
         }
         self.reset();
     };
          
     //edit
     $scope.edit = function(id){    	 
         //console.log('id to be edited', id);
         for(var i = 0; i < self.sampleList.length; i++){
             if(self.sampleList[i].sampleId == id) {
                self.sample = angular.copy(self.sampleList[i]);
                break;
             }
         }
         
         
         //id = "1"; // Exception 발생
         
         self.getSample(id);//  service exception
         //self.getSample2(id);// runtime exception
         
         //self.getSample3(id);
     };
     
     //remove
     $scope.remove = function(id){
         //console.log('id to be deleted', id);
         if(self.sample.sampleId == id) {//If it is the one shown on screen, reset screen
            self.reset();
         }
         self.deleteSample(id);
     };

     		
     //reset
     self.reset = function(){
         self.sample= {sampleId: null,sampleName: null};
         $scope.myForm.$setPristine(); //reset Form
     };
}]);

