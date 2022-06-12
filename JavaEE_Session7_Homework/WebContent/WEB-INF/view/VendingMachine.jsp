<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機</title>
<style type="text/css">
		.page {
			display:inline-block;
			padding-left: 10px;
		}
	</style>
	<script type="text/javascript">

	</script>
</head>
<body align="center">
<table width="1000" height="400" align="center">
	<tr>
		<td colspan="2" align="right">
			<form action="FrontendServlet.do" method="get">
				<input type="hidden" name="action" value="searchGoods"/>
				<input type="hidden" name="pageNo" value="1"/>
				<input type="text" name="searchKeyword"/>
				<input type="submit" value="商品查詢"/>
			</form>
		</td>
	</tr>
	<tr>
		<form action="FrontendServlet.do" method="post">
		<input type="hidden" name="customerID" value="A124243295"/>
		<td width="400" height="200" align="center">		
			<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >			
			<h1>歡迎光臨，${account.customerName}！</h1>		
			<a href="BackendServlet.do?action=queryGoods&pageNo=1" align="left" >後臺頁面</a>&nbsp; &nbsp;
			<a href="LoginAction.do?action=logout" align="left">登出</a>
			<br/><br/>
			<font face="微軟正黑體" size="4" >
				<b>投入:</b>
				<input type="number" name="inputMoney" max="100000" min="0"  size="5" value="0">
				<b>元</b>		
				<b><input type="submit" value="送出">					
				<br/><br/>
			</font>
			<c:if test="${not empty buyGoodsRtn }">
				<div style="border-width:3px;border-style:dashed;border-color:#FFAC55;
					padding:5px;width:300px;">
					<p style="color: blue;">~~~~~~~ 消費明細 ~~~~~~~</p>
					<p style="margin: 10px;">
						投入金額：<fmt:formatNumber value="${buyGoodsRtn.customerMoney}" type="number" pattern="$#,###" />
					</p>
					<p style="margin: 10px;">
						購買金額：<fmt:formatNumber value="${buyGoodsRtn.orderPrice}" type="number" pattern="$#,###" />
					</p>
					<p style="margin: 10px;">
						找零金額：<fmt:formatNumber value="${buyGoodsRtn.cash}" type="number" pattern="$#,###" />
					</p>
					<c:forEach items="${buyGoodsRtn.goodsOrders}" var="order">
						<p style="margin: 10px;">
						${order.key.goodsName} <fmt:formatNumber value="${order.key.goodsPrice}" type="number" pattern="$#,###" /> * ${order.value}
						</p>
					</c:forEach>
					<% session.removeAttribute("buyGoodsRtn"); %>
				</div>
			</c:if>	
		</td>
		<td width="600" height="400">
			<input type="hidden" name="action" value="buyGoods"/>
			<table border="1" style="border-collapse: collapse">
				<tr>
					<c:forEach items="${searchGoods}" var="searchGood" varStatus="Status">
						<c:if test="${Status.index eq '3' }">
							</tr>
							<tr>
						</c:if>
						<td width="300">							
						<font face="微軟正黑體" size="5" >
							${searchGood.goodsName} 
						</font>
						<br/>
						<font face="微軟正黑體" size="4" style="color: gray;" >
							${searchGood.goodsPrice} 元/罐  
						</font>
						<br/>
						<!-- 各商品圖片 -->
						<img border="0" src="DrinksImage/${searchGood.goodsImageName}" width="150" height="150" >						
						<br/>
						<font face="微軟正黑體" size="3">
							<input type="hidden" name="goodsID" value="${searchGood.goodsID}">
							<!-- 設定最多不能買大於庫存數量 -->
							購買<input type="number" name="buyQuantity" min="0" max="${searchGood.goodsQuantity}" size="5" value="0">罐	
							<!-- 顯示庫存數量 -->
							<br><p style="color: red;">(庫存 ${searchGood.goodsQuantity} 罐)</p>
						</font>			
					</td>
					</c:forEach>
				</tr>									
			</table>
		</td>
		</form>			
	</tr>
	<tr>
		<td colspan="2" align="right">	
			<c:url value="FrontendServlet.do" var="pageUrl">
				<c:param name="action" value="searchGoods"/>
				<c:param name="searchKeyword" value="${param.searchKeyword}"/>
				<c:param name="jessionid" value="${jessionid}"/>
			</c:url>
			<c:if test="${1 ne param.pageNo }">
				<c:url value="${pageUrl}" var="previousPageUrl">
					<c:param name="pageNo" value="${param.pageNo-1}"/>
				</c:url>
				<h3 class="page"> <a href="${previousPageUrl}"> 上一頁 </a> </h3>
			</c:if>
			<c:forEach items="${pageGoods.pagination}" var="pageNo">
				<c:url value="${pageUrl}" var="assignPageUrl">
					<c:param name="pageNo" value="${pageNo}" />
				</c:url>
				<h3 class="page"> 
					<a href="${assignPageUrl}" <c:if test="${pageNo eq param.pageNo}">style="color:red;"</c:if> >${pageNo}</a> 
				</h3>
			</c:forEach>
			<c:if test="${param.pageNo ne pageGoods.endPageNo }">
				<c:url value="${pageUrl}" var="nextPageUrl">
					<c:param name="pageNo" value="${param.pageNo+1}"/>
				</c:url>
				<h3 class="page"> <a href="${nextPageUrl}"> 上一頁 </a> </h3>
			</c:if>
		</td>
	</tr>
</table>


</body>

</html>