<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Vending Machine Backend Service</h1><br/>		
	<table border="1" style="border-collapse:collapse;margin-left:25px;">
		<tr>
			<td width="200" height="50" align="center">
				<c:url value="BackendServlet.do" var="queryGoodsUrl">
					<c:param name="action" value="queryGoods"/>
					<c:param name="pageNo" value="1"/>		
				</c:url>
				<a href="${queryGoodsUrl}">商品列表</a>
			</td>
			<td width="200" height="50" align="center">
				<c:url value="BackendServlet.do" var="updateGoodsViewUrl">
					<c:param name="action" value="updateGoodsView"/>
				</c:url>
				<a href="${updateGoodsViewUrl}">商品維護作業</a>
			</td>
			<td width="200" height="50" align="center">
				<c:url value="BackendServlet.do" var="addGoodsViewUrl">
					<c:param name="action" value="addGoodsView"/>
				</c:url>
				<a href="${addGoodsViewUrl}">商品新增上架</a>
			</td>
			<td width="200" height="50" align="center">
				<c:url value="BackendServlet.do" var="querySalesReportViewUrl">
					<c:param name="action" value="querySalesReportView"/>
				</c:url>
				<a href="${querySalesReportViewUrl}">銷售報表</a>
			</td>
			<td width="200" height="50" align="center">
				<c:url value="FrontendServlet.do" var="searchGoodsViewUrl">
					<c:param name="action" value="searchGoods"/>
					<c:param name="pageNo" value="1"/>
				</c:url>
				<a href="${searchGoodsViewUrl}">販賣機前台</a>
			</td>
		</tr>
	</table>
	<br/><br/><HR>