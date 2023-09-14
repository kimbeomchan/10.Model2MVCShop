<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=euc-kr" %>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>

		<td></td>
		<td align="left" data-prodno="${vo.prodNo}"> ${vo.prodName}</td>
		<td></td>
		<td align="left">${vo.price}</td>
		<td></td>
		<td align="left">${vo.regDate}</td>
		<td></td>
		<td align="left">
		
			<c:if test="${menu.equals('manage')}">
				<c:if test="${vo.proTranCode == null}" >
					판매중
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '1'}" >
					구매완료
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '2'}" >
					배송중
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '3'}" >
					배송완료
				</c:if>
			</c:if>
			
			<c:if test="${menu.equals('search')}">
				${vo.proTranCode == null ? '판매중' : '판매완료'}
			</c:if>
		</td>
			