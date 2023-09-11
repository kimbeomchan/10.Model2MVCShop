<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<title>��ǰ �����ȸ</title>

<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script type="text/javascript">
	// �˻� / page �ΰ��� ��� ��� Form ������ ���� JavaScript �̿�
	function fncGetPageList(currentPage) {
		//document.getElementById("currentPage").value = currentPage;
	   	//document.detailForm.submit();
	   	$("#currentPage").val(currentPage)
	   	$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	}
	
	$(function () {
		$( ".ct_list_pop td:nth-child(3)" ).on("click" , function() {
			var prodNo = $(this).data('prodno');
			var menu = "${menu}";
			
			if (menu == "search") {
				$("input[name='prodNo']").val(prodNo);
				$("form").attr("method", "POST").attr("action", "/product/getProduct").submit();
			} else if (menu == "manage") {
				$("input[name='prodNo']").val(prodNo);
				$("form").attr("method", "POST").attr("action", "/product/updateProductView").submit();
			}
		});	
	});
	
	$(function () {
		var prodNo = $(this).data('prodno');
		
		$("c:if:contains('����ϱ�')").on("click", function(){
			self.location.href = "/purchase/updateTranCode?prodNo=" + prodNo + "&tranCode=2&currentPage=" + ${resultPage.currentPage};
		});
		
	});
	
	$(function() {
		$("td.ct_btn01:contains('�˻�')").on("click" , function() {
			fncGetPageList(1);
		});
	});
	 
	$(function () {
		$( ".ct_list_pop td:nth-child(3)" ).on( "mouseenter", function() {
				var prodNo = $(this).data('prodno');
				$.ajax(
					{
						url : "/product/json/getProduct/" + prodNo ,
						method : "GET" ,
						dataType : "json" ,
						headers : {
							"Accept" : "application/json" ,
							"Content-Type" : "application/json"
						},
						success : function(JSONData) {
							var display = "<h3>"
												+ "��ǰ�� : " + JSONData.prodName + "<br/>"
												+ "��ǰ ������ : " + JSONData.prodDetail + "<br/>"
												+ "�������� : " + JSONData.manuDate + "<br/>"
												+ "�� �� : " + JSONData.price + "<br/>"
												+ "��ǰ �̹��� : " + JSONData.fileName + "<br/>"
												+ "</h3>";
							
							$("#"+prodNo+"").html(display);
						}
					}		
				);
			}
		);
		
		$( ".ct_list_pop td:nth-child(3)" ).on("mouseleave" , function() {
			$("h3").remove();
		 	$( this ).fadeOut( 30 );
		 	$( this ).fadeIn( 30 );
		}); 

		$( ".ct_list_pop td:nth-child(3)" ).css("color" , "red");
	});

	
	$(function(){
	    var availableTags = [];

	    $("input[name=searchKeyword]").autocomplete({
	        source: availableTags,
	        autoFocus: true
	    });

	    $("input[name=searchKeyword]").keyup(function(){
	        var searchKeyword = $(this).val();
	        var searchCondition = $("select[name=searchCondition]").val();

	        $.ajax({
	            url: "/product/json/listAutoProduct?searchCondition="+ searchCondition +"&searchKeyword=" + searchKeyword,
	            dataType: "json",
	            headers : {
	                "Accept" : "application/json",
	                "Content-Type" : "application/json"
	            },
	            success: function(data) {
	                availableTags = data; // �޾ƿ� �����ͷ� availableTags ������Ʈ
	                $("input[name=searchKeyword]").autocomplete("option", "source", availableTags); // Autocomplete�� source �ɼ��� ������Ʈ
	            }
	        });
	    });

	    $("#inputBox").on("focus", function() {
	        if (availableTags.length >= 0) {
	            $(this).autocomplete("search");
	        }
	    });
	});


	
/* 	$(function(){
	    $("input[name=searchKeyword]").keyup(function(){
	    	var availableTags = [];
	        var searchKeyword = $(this).val();
	        var searchCondition = $( "select[name=searchCondition]" ).val();
	        $.ajax({
	            url: "/product/json/listAutoProduct?searchCondition="+ searchCondition +"&searchKeyword=" + searchKeyword,
	            dataType: "json",
	            headers : {
	                "Accept" : "application/json" ,
	                "Content-Type" : "application/json"
	            },
	            success: function(data) {
	                availableTags = data; // �޾ƿ� �����͸� �״�� ���
	                $("input[name=searchKeyword]").autocomplete({
	                    source : availableTags,
	                    autoFocus : true
	                });
	            }
	        });
	        
	        $.ajax({
	        	url: "/product/json/listAutoProduct?searchCondition="+ searchCondition +"&searchKeyword=" + searchKeyword,
	        })
	    })

	    $("#inputBox").focus(function() {
	        $(this).autocomplete("search", $(this).val());
	    })
	}); */

	/* $(function(){
	    var availableTags = [];

	    $("input[name=searchKeyword]").keyup(function(){
	        var searchKeyword = $(this).val();
	        $.ajax({
	            url: "/product/json/listAutoProduct?searchCondition=1&searchKeyword=" + searchKeyword,
	            dataType: "json",
	            headers : {
	                "Accept" : "application/json" ,
	                "Content-Type" : "application/json"
	            },
	            success: function(data) {
	                availableTags = data; // �޾ƿ� �����͸� �״�� ���
	            }
	        })
	        
	        $("#inputBox").autocomplete({
		        source : searchKeyword,
		        minLength : 1
		    }).focus(function() {
	            $(this).autocomplete("searchKeyword", $(this).val());
	        });
	    });
	}); 
	*/

	
	
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<%-- <form name="detailForm" method="POST" action="/product/listProduct?menu=${menu}"> --%>

<form name="detailForm" >

<input type="hidden" name="prodNo" />
<input type="hidden" name="menu" value="${menu}"/>

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					
					<c:if test="${menu.equals('search')}" >
						��ǰ ��� ��ȸ
					</c:if>
					<c:if test="${menu.equals('manage')}" >
						��ǰ ����
					</c:if>
					
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="0" ${search.searchCondition.equals("0") ? "selected" : "" } >��ǰ��ȣ</option>
				<option value="1" ${search.searchCondition.equals("1") ? "selected" : "" } >��ǰ��</option>
				<option value="2" ${search.searchCondition.equals("2") ? "selected" : "" } >��ǰ����</option>
			</select>
			<input 	type="text" name="searchKeyword" id="inputBox" value="${search.searchKeyword}" 
							class="ct_input_g" style="width:200px; height:19px" >
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<!-- <a href="javascript:fncGetPageList(1);">�˻�</a> -->
						�˻�
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">

	<tr>
		<td colspan="11" >
			��ü  ${resultPage.totalCount}  �Ǽ�,	���� ${resultPage.currentPage} ������
		</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ǰ��<br>
			<h7>(��ǰ :: ������)</h7>
		</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">�����</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">�������</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	<c:set var="i" value="0" />
	<c:forEach var="vo" items="${list}" >
		<c:set var="i" value="${ i + 1 }" />
	<tr class="ct_list_pop">
		<td align="center">${ i }</td>
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
					�Ǹ���
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '1'}" >
					���ſϷ�
					<%-- <a href="/purchase/updateTranCode?prodNo=${vo.prodNo}&tranCode=2&currentPage=${resultPage.currentPage}">����ϱ�</a> --%>
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '2'}" >
					�����
				</c:if>
				<c:if test="${vo.proTranCode.trim() == '3'}" >
					��ۿϷ�
				</c:if>
			</c:if>
			
			<c:if test="${menu.equals('search')}">
				${vo.proTranCode == null ? '�Ǹ���' : '�ǸſϷ�'}
			</c:if>
			
		</td>	
	</tr>
	<tr>
			<!-- //////////////////////////// �߰� , ����� �κ� /////////////////////////////
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
			////////////////////////////////////////////////////////////////////////////////////////////  -->
		<td id="${vo.prodNo}" colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>	
	
	</c:forEach>
</table>

<!-- PageNavigation Start... -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top:10px;">
	<tr>
		<td align="center">
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			
			<jsp:include page="../common/pageNavigator.jsp" />
		
    	</td>
	</tr>
</table>
<!-- PageNavigation End... -->

</form>
</div>
</body>
</html>
