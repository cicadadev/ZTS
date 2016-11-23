<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="/resources/js/sample/sample.js"></script>
<article class="con_box" data-ng-controller="SampleController as ctrl">
<h2 class="sub_title1">GCP2.0</h2>
	<form data-ng-submit="ctrl.submit()" name="myForm">
	<div class="btn_alignR">
	<input type="submit"  value="{{!ctrl.sample.sampleId ? 'Add' : 'Update'}}" data-ng-disabled="myForm.$invalid">
	<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.reset()" data-ng-disabled="myForm.$pristine">
	<br>Reset Form<br>
	</button>
	</div>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="9%" />
				<col width="10%" />
				<col width="9%" />
				<col width="*" />				
			</colgroup>
			<tbody>
				<tr>
					<th>ID</th>
					<td><input type="text" data-ng-model="ctrl.sample.sampleId" id="sampleId" /></td>
					<th rowspan="4" class="alignC">내용</th>
					<td rowspan="4">
						<textarea data-ng-model="ctrl.sample.sampleName" id="sampleName" cols="30" rows="5" placeholder="" > </textarea>						
					</td>									
				</tr>				
			</tbody>
		</table>
	</div>
	</form>	
	<div class="box_type1" style="margin-top:10px;">
		<h3 class="sub_title2">
			정리 목록			
		</h3>
	
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_sample" data-ui-grid-move-columns data-ui-grid-resize-columns data-ui-grid-pagination
													 data-ui-grid-auto-resize data-ui-grid-selection data-ui-grid-exporter
													 data-ui-grid-edit data-ui-grid-validate></div>
		</div>
		<div class="tb_bar">
			<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')">더보기</button>
		</div>
	</div>
	
</article>








<%--
	
	<div>
		<form data-ng-submit="ctrl.submit()" name="myForm">
		ID : <input type="text" data-ng-model="ctrl.sample.sampleId" id="sampleId"/></br>
		NAME : <input type="text" data-ng-model="ctrl.sample.sampleName" id="sampleName"/>
	    <input type="submit"  value="{{!ctrl.sample.sampleId ? 'Add' : 'Update'}}" data-ng-disabled="myForm.$invalid">
        <button type="button" data-ng-click="ctrl.reset()" data-ng-disabled="myForm.$pristine">Reset Form</button>
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
				<tr data-ng-repeat="s in ctrl.sampleList">
					<td>{{s.sampleId}}</td>
					<td>{{s.sampleName}}</td>
					<td>
	                <button type="button" data-ng-click="ctrl.edit(s.sampleId)" >Edit</button>  <button type="button" data-ng-click="ctrl.remove(s.sampleId)">Remove</button>
	                </td>
				</tr>
			</tbody>
		</table>
	</div>
	</div>
 --%>