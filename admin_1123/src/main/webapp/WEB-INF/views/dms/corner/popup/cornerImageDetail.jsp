<%--
	화면명 : 전시 관리 > 전시 코너 관리 > 배너 타입 전시 코너 등록 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>

<script type="text/javascript" src="/resources/js/app/dms.app.corner.manager.js"></script>
	<div class="wrap_popup"  data-ng-app="cornerManagerApp" data-ng-controller="imgDetailController as ctrl" data-ng-init="ctrl.init()">
	<form name="form">
		<h1 class="sub_title1">이미지 배너 {{dmsDisplayitem.displayItemNo=='' || dmsDisplayitem.displayItemNo==null ? '등록' : '상세'}}</h1>

		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="13%" />
					<col width="37%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							이미지 배너(PC) <i>필수입력</i>
						</th>
						<td colspan="3">
						<div class="preview" ng-show="dmsDisplayitem.img1">
							<img img-domain ng-src="dmsDisplayitem.img1" onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="" />
							<!-- <button type="button" class="btn_file_del">파일 삭제</button> -->
						</div>
						<div class="input_file">
							<input type="file" image-key="dmsDisplayitem.img1" file-upload/>
							<button type="button" class="btn_type2 btn_addFile">
								<b>찾아보기</b>
							</button>
							<span>(이미지 사이즈 : 000*000)</span>
						</div>

							<p class="txt_type1">
								* jpg, png 00MB 이하의 파일만 업로드가 가능합니다.
							</p>
							<dl class="img_alt">
								<dt>이미지 대체텍스트</dt>
								<dd>
									<input type="text" ng-model="dmsDisplayitem.text1"  style="width:60%;" v-key="dmsDisplayitem.text1"/>
								</dd>
							</dl>

							<p class="txt_type1">
								* 이미지에 포함된 텍스트 정보를 기입해주세요.<br />
								(텍스트가 없는 이미지의 경우에는 이미지에 대한 설명을 기입해주세요.)
							</p>
							<p class="information" ng-show="!dmsDisplayitem.img1 && !dmsDisplayitem.img2">필수 입력 항목 입니다.</p>
						</td>
					</tr>	
					<tr>
						<th>
							이미지 배너(Mobile) <i>필수입력</i>
						</th>
						<td colspan="3">
						<div class="preview" ng-show="dmsDisplayitem.img2">
							<img img-domain ng-src="dmsDisplayitem.img2" onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="" />
							<!-- <button type="button" class="btn_file_del">파일 삭제</button> -->
						</div>
						<div class="input_file">
							<input type="file" image-key="dmsDisplayitem.img2" file-upload/>
							<button type="button" class="btn_type2 btn_addFile">
								<b>찾아보기</b>
							</button>
							<span>(이미지 사이즈 : 000*000)</span>
						</div>

							<p class="txt_type1">
								* jpg, png 00MB 이하의 파일만 업로드가 가능합니다.
							</p>
							<dl class="img_alt">
								<dt>이미지 대체텍스트</dt>
								<dd>
									<input type="text" ng-model="dmsDisplayitem.text2"  style="width:40%;" v-key="dmsDisplayitem.text2"/>
								</dd>
							</dl>

							<p class="txt_type1">
								* 이미지에 포함된 텍스트 정보를 기입해주세요.<br />
								(텍스트가 없는 이미지의 경우에는 이미지에 대한 설명을 기입해주세요.)
							</p>
						</td>
					</tr>						
					<tr>
						<th>PC연결 URL </th>
						<td colspan="3"><input type="text" ng-model="dmsDisplayitem.url1"  style="width:60%;" v-key="dmsDisplayitem.url1"/>
							<!-- <p class="information" ng-show="!dmsDisplayitem.url1 && !dmsDisplayitem.url2">필수 입력 항목 입니다.</p> -->
						</td>
					</tr>		
					<tr>
						<th>Mobile연결 URL</th>
						<td colspan="3"><input type="text" ng-model="dmsDisplayitem.url2"  style="width:60%;" v-key="dmsDisplayitem.url2"/>
						</td>
					</tr>									
					<tr>
						<th>전시기간</th>
						<td colspan="3"><input type="text" ng-model="dmsDisplayitem.startDt"  style="width:180px;" datetime-picker period-start/> ~ <input type="text" datetime-picker period-end ng-model="dmsDisplayitem.endDt"  style="width:180px;"/>
							<p class="information" ng-show="!dmsDisplayitem.startDt || !dmsDisplayitem.endDt">필수 입력 항목 입니다.</p>
						</td>
					</tr>
					<tr>
						<th>우선순위</th>
						<td><input type="text" ng-model="dmsDisplayitem.sortNo" style="width:130px;" v-key="dmsDisplayitem.sortNo"/></td>
						<th>전시여부 <i>필수입력</i></th>
						<td>
							<radio-yn ng-model="dmsDisplayitem.displayYn" labels="전시,미전시" init-val="Y" required></radio-yn>
						</td>
					</tr>
					<tr>
						<th>부가정보</th>
						<td colspan="3"><input type="text" ng-model="dmsDisplayitem.addValue" style="width:30%;" v-key="dmsDisplayitem.addValue"/></td>
					</tr>					
				</tbody>
			</table>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b>저장</b>
			</button>
		</div>
		
	</form>	
	</div>


<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>