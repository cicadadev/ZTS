<%--
	화면명 : 쇼킹제로 > 메인(mo)
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<div class="main_zero">
	<h2 class="title_zero">
		매일 오전 10시! 쇼킹한 가격으로 만나요
	</h2>
	
	
	<ul class="sortBox1 sortBox1_3ea" id="sortBox">
		<li id="popular_li" class="active">
			<a href="javascript:void(0)" onclick="sps.deal.shockingzero.resorting('popular');">
				<span>인기상품순</span>
			</a>
		</li>
		<li id="new_li">
			<a href="javascript:void(0)" onclick="sps.deal.shockingzero.resorting('new');">
				<span>신규오픈순</span>
			</a>
		</li>
		<li id="end_li">
			<a href="javascript:void(0)" onclick="sps.deal.shockingzero.resorting('end');">
				<span>종료임박순</span>
			</a>
		</li>
	</ul>
	
	<div class="product_type1 prodType_3ea">
		<ul id="shockingProductArea">
				
		</ul>
	</div>
							
</div>

<style>
.pc .zero_visual .view .imgBox {height:360px;}
.pc .zero_visual .thumb li a .thumb_img {margin-right:7px; vertical-align:middle; display:inline-block; width:72px; height:72px;}
.mobile .sortBox1_2ea {height:50px;}
.mobile .sortBox1_2ea h3 {text-align:left; margin:15px 0 0 5px;}
.mobile .sortBox1_2ea .select_box1 {width:145px; margin:5px 5px 5px 0; float:right;}
</style>