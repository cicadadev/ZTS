<%--
	화면명 : 딜 관리 > 딜 구분 타이틀 관리 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<style>
	body {margin: 10px}
.where {
  display: block;
  margin: 25px 15px;
  font-size: 11px;
  color: #000;
  text-decoration: none;
  font-family: verdana;
  font-style: italic;
} 
.filebox {display:inline-block; margin-right: 10px;}


.filebox label {
  display: inline-block;
  padding: .5em .75em;
  color: #999;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #fdfdfd;
  cursor: pointer;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: .25em;
}

.filebox input[type="file"] {  /* 파일 필드 숨기기 */
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip:rect(0,0,0,0);
  border: 0;
}
</style>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup"  ng-app="dealApp" ng-controller="sps_dealDivTitleManagerPopApp_controller as ctrl">
		<ul class="tab_type2">
			<li>
				<button type="button" ng-click="ctrl.moveTab('dealDetail')"><spring:message code="c.spsDeal.detail"/> <!-- 딜 상세 --></button>
			</li>
			<li class="on">
				<button type="button"><spring:message code="c.spsDeal.divTitle.manage"/><!-- 구분타이틀 관리 --></button>
			</li>
			<li>
				<button type="button" ng-click="ctrl.moveTab('dealProduct')"><spring:message code="c.spsDeal.product.manage"/><!-- 상품 관리 --></button>
			</li>
		</ul>
		
		<div class="division_set2">
			<!-- ### 1depth 목록 ### -->
			<section>
				<div class="btn_alignR marginT3">
					<button type="button" class="btn_type1" ng-click="ctrl.addOneDepth()">
						<b><spring:message code="c.grid.add.row"/><!-- 행추가 --></b>
					</button>
					<button type="button" class="btn_type1" ng-click="ctrl.deleteOneDepth()">
						<b><spring:message code="c.common.delete"/><!-- 삭제 --></b>
					</button>
					<button type="button" class="btn_type1" ng-click="myGrid1.saveGridData(null, oneDepthReload)">
						<b><spring:message code="c.common.save"/><!-- 저장 --></b>
					</button>
				</div>

				<div class="box_type1 marginT1">
					<h3 class="sub_title2">
						<spring:message code="c.spsDeal.deal.1depthList"/> <!-- 1depth 목록 -->
						<span><spring:message code="c.search.totalCount" arguments="{{ grid_depth1.data.length }}" /></span>
						<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid1.initGrid()">되돌리기</button>
					</h3>
					
					
					<div class="gridbox gridbox200">
						<div class="grid" ui-grid="grid_depth1" 
							data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-auto-resize 
							data-ui-grid-selection 
							data-ui-grid-row-edit
							data-ui-grid-cell-nav
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-validate>
						</div>
					</div>
				</div>
			</section>
			<!-- ### //1depth 목록 ### -->

			<!-- ### 2depth 목록 ### -->
			<section>
				<div class="btn_alignR marginT3">
					<button type="button" class="btn_type1" ng-click="ctrl.addTwoDepth()" ng-disabled = "depth1Rows<=0">
						<b><spring:message code="c.grid.add.row"/><!-- 행추가 --></b>
					</button>
					<button type="button" class="btn_type1" ng-click="ctrl.deleteTwoDepth()" ng-disabled = "depth1Rows<=0">
						<b><spring:message code="c.common.delete"/><!-- 삭제 --></b>
					</button>
					<button type="button" class="btn_type1" ng-click="myGrid2.saveGridData(null, twoDepthReload)" ng-disabled = "depth1Rows<=0">
						<b><spring:message code="c.common.save"/><!-- 저장 --></b>
					</button>
				</div>
	
				<div class="box_type1 marginT1">
					<h3 class="sub_title2">
						<spring:message code="c.spsDeal.deal.2depthList"/> <!-- 2depth 목록 -->
						<span><spring:message code="c.search.totalCount" arguments="{{ grid_depth2.data.length }}" /></span>
						<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid2.initGrid()">되돌리기</button>
					</h3>
					<div class="gridbox gridbox200">
						<div class="grid" ui-grid="grid_depth2" 
							data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-auto-resize 
							data-ui-grid-selection 
							data-ui-grid-row-edit
							data-ui-grid-cell-nav
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-validate>
						</div>
					</div>
				</div>
			</section>
			<!-- ### //2depth 목록 ### -->
		</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>