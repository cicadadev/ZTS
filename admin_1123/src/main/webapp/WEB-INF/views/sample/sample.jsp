<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sample</title>

</head>
<body ng-app="myApp">
<script src="https://code.angularjs.org/1.5.4/angular.js"></script>
<script src="https://code.angularjs.org/1.5.4/angular-resource.js"></script>
<script type="text/javascript">
var App = angular.module('myApp',['ngResource']);

App.factory('Sample', ['$resource',function($resource){
	return $resource('http://localhost:8080/gcp2.0/test/sample/:id',
					 {id: '@sampleId'},
					 {
						 'update': {method:'PUT'}
					 },
					 {});
}]);

App.controller('SampleController',['$scope','Sample',function($scope,Sample){
	var self = this;
	self.sample = new Sample();
	self.sampleList = [];
	
	 self.fetchAllSample = function(){
         self.sampleList = Sample.query();
     };
       
     self.createSample = function(){
         self.sample.$save(function(){
             self.fetchAllSample();
         });
     };
      
     self.updateSample = function(){
         self.sample.$update(function(){
             self.fetchAllSample();
         });
     };

    self.deleteSample = function(identity){
        var sample = Sample.get({id:identity}, function() {
             sample.$delete(function(){
                 console.log('Deleting sample with id ', identity);
                 self.fetchAllSample();
             });
        });
     };

     self.fetchAllSample();

     self.submit = function() {
         if(self.sample.sampleId==null){
             console.log('Saving New Sample', self.sample);    
             self.createSample();
         }else{
             console.log('Upddating sample with id ', self.sample.sampleId);
             self.updateSample();
             console.log('Sample updated with id ', self.sample.sampleId);
         }
         self.reset();
     };
          
     self.edit = function(id){
         console.log('id to be edited', id);
         for(var i = 0; i < self.sampleList.length; i++){
             if(self.sampleList[i].sampleId == id) {
                self.sample = angular.copy(self.sampleList[i]);
                break;
             }
         }
     };
          
     self.remove = function(id){
         console.log('id to be deleted', id);
         if(self.sample.sampleId == id) {//If it is the one shown on screen, reset screen
            self.reset();
         }
         self.deleteSample(id);
     };

      
     self.reset = function(){
         self.sample= new Sample();
         $scope.myForm.$setPristine(); //reset Form
     };
}]);

</script>
	<div ng-controller="SampleController as ctrl">
	<div>
		<form ng-submit="ctrl.submit()" name="myForm">
		ID : <input type="text" ng-model="ctrl.sample.sampleId" id="sampleId"/></br>
		NAME : <input type="text" ng-model="ctrl.sample.sampleName" id="sampleName"/>
	    <input type="submit"  value="{{!ctrl.sample.sampleId ? 'Add' : 'Update'}}" ng-disabled="myForm.$invalid">
        <button type="button" ng-click="ctrl.reset()" ng-disabled="myForm.$pristine">Reset Form</button>
         </form>
	</div>
	<div>
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>NAME</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="s in ctrl.sampleList">
					<td>{{s.sampleId}}</td>
					<td>{{s.sampleName}}</td>
					<td>
	                <button type="button" ng-click="ctrl.edit(s.sampleId)" >Edit</button>  <button type="button" ng-click="ctrl.remove(s.sampleId)">Remove</button>
	                </td>
				</tr>
			</tbody>
		</table>
	</div>
	</div>
</body>

</html>