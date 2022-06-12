<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>會員登入</title>
	<script type="text/javascript">
		
	</script>
</head>
<body>
	<c:if test="${not empty requestScope.loginMsg}">
		<p style="color:blue;">${requestScope.loginMsg}</p>
	</c:if>
<form action="LoginAction.do?action=login" method="post">
	<table width="177" cellSpacing="0" cellPadding="0" border="0" style="margin-top:30px;margin-left:30px;">
		<tr>
			<td><img height="58" width="177" src="HomeImage/l_m_01.gif" ></td>
		<tr height="50">
			<td background="HomeImage/l_m_bg01.gif">
				<p align="center">
				<font size="2">帳號：</font>
				<input type="text" name="identificationNo" size="10" style="height: 30px;"/>
			</td>
		</tr>
		<tr height="50">
			<td background="HomeImage/l_m_bg01.gif">
				<p align="center">
				<font size="2">密碼：</font>
				<input type="password" name="password" size="10" style="height: 30px;"/>
			</td>
		</tr>
		<tr height="50">
			<td  background="HomeImage/l_m_bg01.gif">
				<p align="center">
				<input type="submit" value="登入" style="width:130px;height:30px"/>
			</td>
		</tr>			
		<tr>
			<td><img height="13" width="177" src="HomeImage/l_m_bg02.gif" ></td>
		</tr>				
	</table>
</form>
</body>
</html>