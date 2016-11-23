<%--
	화면명 : 회원상세 > 오프라인 주문내역 > 오프라인 주문 상세 
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_offOrderDetailPopApp_controller as ctrl">
		<h2 class="sub_title1">오프라인 주문상세 <!-- 오프라인 주문상세 --></h2>	
		<div class="box_type1">
			<table class="tb_type2">
				<colgroup>
					<col width="20%" />
					<col width="20%" />
					<col width="20%" />
					<col width="20%" />
				</colgroup>
				<tbody>
					<tr>
						<th>상품ID</th>
						<th>상품명</th>
						<th>수량</th>
						<th>단가</th>
					</tr>
					
					<tr ng-repeat="product in omsPosorderproduct">
						<td>
							{{product.erpSaleproductId}}
						</td>
						<td>
							{{product.productName}}
						</td>
						<td>
							{{product.orderQty}}
						</td>
						<td>
							{{product.orderAmt}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		

	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
		</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>