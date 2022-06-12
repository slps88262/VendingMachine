<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url value="/" var="WEB_PATH"/>
<c:url value="/js" var="JS_PATH"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
<script src="${JS_PATH}/jquery-1.11.1.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#goodsID").bind("change",function(){
				var goodsID = $("#goodsID option:selected").val();
				var radios = $('input:radio[name=status]');
				radios.filter('[value=1]').prop('checked', false);
				radios.filter('[value=0]').prop('checked', false);
				if(goodsID != ""){ 
					$.ajax({
						url: '${WEB_PATH}BackendServlet.do?action=getUpdateGood', // 指定要進行呼叫的位址
						type: "GET", // 請求方式 POST/GET
						data: {id : goodsID}, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
						dataType : 'json', // Server回傳的資料類型
						success: function(goodsInfo) { // 請求成功時執行函式
							$("#goodsPrice").val(goodsInfo.goodsPrice);
							$("#goodsQuantityDiv").html(goodsInfo.goodsQuantity);
							radios.filter("[value='"+ goodsInfo.status +"']").prop('checked', true);
						  }, 
						  error: function(error) { // 請求發生錯誤時執行函式
						    alert("Ajax Error!");
						  }
					});
				}else{
					$("#goodsPrice").val('');
					$("#goodsQuantityDiv").empty();
				}
			});
		});
	</script>
</head>
<body>
	<%@ include file="VM_Backend_FunMenu.jsp" %>
	<h2>商品維護作業</h2><br/>
	<div style="margin-left:25px;">
	<p style="color:blue;">${sessionScope.updateMsg}</p>
	<% session.removeAttribute("updateMsg"); %>
	<form name="updateGoodForm" action="BackendServlet.do" method="post">
		<input type="hidden" name="action" value="updateGoods"/>
		<p>
			飲料名稱：
			 <select size="1" name="goodsID" id="goodsID">
			 	<option value="">----- 請選擇 -----</option>
			 	<c:forEach items="${goods}" var="good">
			 		<option <c:if test="${good.goodsID eq updateGoods.goodsID}">selected</c:if> 
						value="${good.goodsID}">
						${good.goodsName}
					</option>
			 	</c:forEach>
			 </select>
		</p>		
		<p>
			更改價格： 
			<input type="number" name="goodsPrice" id="goodsPrice" size="5" value="${updateGoods.goodsPrice}" min="0"/>
		</p>
		<p>
			<div style="display: inline-block;">商品庫存：</div>
			<div id="goodsQuantityDiv" style="display: inline-block;">
				<fmt:formatNumber value="${updateGoods.goodsQuantity}" type="number" pattern="#,###"/>			
			</div>
		</p>
		<p>
			補貨數量：
			<input type="number" name="goodsQuantity" size="5" value="0" min="0" />
		</p>
		<p>
			商品狀態：
			<input type="radio" name="status" <c:if test="${updateGoods.status eq '1' }">checked</c:if> value="1"/>上架
			<input type="radio" name="status" <c:if test="${updateGoods.status eq '0' }">checked</c:if> value="0"/>下架
		</p>		
		<p>
			<input type="submit" value="送出">
		</p>
	</form>
	</div>
</body>
</html>