<%@ page language="java" contentType="text/html; charset=euc-kr"
    pageEncoding="euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script type="text/javascript">


	var page = 1;

	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScript 이용
	function fncGetPageList(currentPage) {
		//document.getElementById("currentPage").value = currentPage;
	   	//document.detailForm.submit();
	   	$("#currentPage").val(currentPage);
	   	$("form").attr("method" , "POST").attr("action" , "/product/listProduct").submit();
	}
	
	$(function () {
		$( "#tableBody" ).on("click" , ".ct_list_pop td:nth-child(3)", function() {
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
		
		$("c:if:contains('배송하기')").on("click", function(){
			self.location.href = "/purchase/updateTranCode?prodNo=" + prodNo + "&tranCode=2&currentPage=" + ${resultPage.currentPage};
		});
		
	});
	
	$(function() {
		$("td.ct_btn01:contains('검색')").on("click" , function() {
			fncGetPageList(1);
		});
	});
	 
	$(function () {
		$( "#tableBody" ).on( "mouseenter", ".ct_list_pop td:nth-child(3)" ,function() {
				var prodNo = $(this).data('prodno');
				console.log(prodNo);
	    		setTimeout(function() {
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
								var file = JSONData.fileName.split(',')[0];
								var display = "<h3>"
													+ "상품명 : " + JSONData.prodName + "<br/>"
													+ "상품 상세정보 : " + JSONData.prodDetail + "<br/>"
													+ "제조일자 : " + JSONData.manuDate + "<br/>"
													+ "가 격 : " + JSONData.price + " 원 <br/>"
													+ "상품 이미지  <br/>"
													+ "<img width='200' height='150' src = /images/uploadFiles/" + file + "/>"
													+ "</h3>";
								
								$("h3").remove();
								$("#"+JSONData.prodNo+"").html(display);
							}
						}		
					);
				}, 100);
			}
		);
		
		$( "#tableBody" ).on("mouseleave", ".ct_list_pop td:nth-child(3)", function() {
			$("h3").remove();
		});

		$( ".ct_list_pop td:nth-child(3)" ).css("color" , "red");
	});

	
	$(function(){
	    var availableTags = [];

	    $("input[name=searchKeyword]").autocomplete({
	        source: availableTags,
	        autoFocus: true,
	        messages: {
	            noResults: '',
	            results: function() {}
	        }
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
	                availableTags = data; // 받아온 데이터로 availableTags 업데이트
	                $("input[name=searchKeyword]").autocomplete("option", "source", availableTags); // Autocomplete의 source 옵션을 업데이트
	            }
	        });
	        
	    });
	    
	    
	    

	    $("#inputBox").on("focus", function() {
	        if (availableTags.length >= 0) {
	            $(this).autocomplete("search");
	        }
	    });
	});

	$(function(){
		$( ".ct_list_pop td:nth-child(3)" ).css("color" , "red");
		var win = $(window);
		$(window).scroll(function() {
			
		    if($(window).scrollTop() >= $(document).height() - $(window).height()) {
		    	$.ajax({
		    		url: '/product/json/listProduct',
		    		type: 'POST',
		    		headers : {
						"Accept" : "application/json" ,
						"Content-Type" : "application/json"
					},
		    		data: JSON.stringify({ 
		    	        currentPage: ++page,  // 페이지 번호 증가
		    	       	pageSize: 10,
		    	       	pageUnit: 1,
		    	    }),
		    	    contentType: "application/json",
		    	    dataType: 'json',
		    	    success: function(data) {
		    	    	var productList = data.list;
		    	    	var menu = "manage";
		    	    	var countNo = $("tr.ct_list_pop:last td:first").text();
		    	    	
		    	    	$.each(productList, function(index, product){
		    	    		var row = 
		    	    			"<tr class='ct_list_pop' id='list'>" +
		    	            	"<td align='center' height='100'>" + ++countNo + "</td>" +
		    	            	"<td></td>" +
		    	            	"<td align='left' data-prodno='" + product.prodNo + "'>" + product.prodName + "</td>" +
		    	            	"<td></td>" +
		    	            	"<td align='left'>" + product.price+ " 원</td>" +
		    	            	"<td></td>" +
		    	            	"<td align='left'>" + product.regDate+ "</td>" +
		    	            	"<td></td>" +
		    	            	"<td align='left'>" +
		    	            		(menu == 'manage' ? 
		    	                        (product.proTranCode.trim() == null ? '판매중' :
		    	                          (product.proTranCode.trim() == '1' ? '구매완료' :
		    	                            (product.proTranCode.trim() == '2' ? '배송중' :
		    	                              (product.proTranCode.trim() == '3' ? '배송완료' : '')))) : '') +
		    	                (menu == "search" ? (product.proTranCode.trim() == null ? "판매중" : "판매완료") : '') +
		    	                "</td></tr>" +
		    	                "<tr>" +
		    	        		"<td id=" +  product.prodNo + " colspan='11' bgcolor='D6D7D6' height='5'></td>" +
		    	        		"</tr>";	
		    	        		
		    	    			$("#tableBody").append(row);		// 받아온 데이터 추가
		    	    		}
		    	    	);
		    	        
		    	    },
		    	    error: function() {
		    	    	alert("ERROR");
		    	    // 요청 실패 시 처리할 내용 작성
		    	    }
		        });
		    }
		});
	});
	 
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

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
						상품 목록 조회
					</c:if>
					<c:if test="${menu.equals('manage')}" >
						상품 관리
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
				<option value="0" ${search.searchCondition.equals("0") ? "selected" : "" } >상품번호</option>
				<option value="1" ${search.searchCondition.equals("1") ? "selected" : "" } >상품명</option>
				<option value="2" ${search.searchCondition.equals("2") ? "selected" : "" } >상품가격</option>
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
						검색
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table id="table" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
<tbody id="tableBody" >
	<tr>
		<td colspan="11" >
			전체 상품 :  ${resultPage.totalCount}  건수<%-- ,	현재 ${resultPage.currentPage} 페이지 --%>
			
		</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="5"></td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="500">상품명<br>
			<h7>(상품 :: 상세정보)</h7>
		</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="500">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="5"></td>
	</tr>

	<c:set var="i" value="0" />
	<c:forEach var="vo" items="${list}" >
		<c:set var="i" value="${ i + 1 }" />
	<tr id="list" class="ct_list_pop" >
		<td align="center" height="100">${ i }</td>
		<td></td>
		<td align="left" data-prodno="${vo.prodNo}"> ${vo.prodName}</td>
		<td></td>
		<td align="left">${vo.price} 원</td>
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
	</tr>
	
	
	<tr>
		<td id="${vo.prodNo}" colspan="11" bgcolor="D6D7D6" height="5"></td>
	</tr>	
	
	</c:forEach>
	</tbody>
</table>

</form>
</div>
</body>
</html>
