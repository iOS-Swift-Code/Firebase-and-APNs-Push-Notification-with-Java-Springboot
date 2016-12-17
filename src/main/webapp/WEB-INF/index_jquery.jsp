<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="/resources/jquery.bootpag.min.js"></script>
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<title>Insert title here</title>
</head>
<body>

	<div id="page-selection">
	
	</div>
	
	<script type="text/javascript">

		$(function(){
			var restaurants;
	 		var totalPages;
	 		var currentPage = 1;
	 		var limit = 1;
	 		var check = true;
	 		
	 		setUpPagination = function(){
		        $('#page-selection').bootpag({
		            total: totalPages,
		            page : currentPage
		        }).on("page", function(event,  pageNum){
		             currentPage = pageNum;
		             getAllRestaurants();
		        });
				
			};
	 		
			getAllRestaurants = function(){
				$.ajax({
					url:"/rest",
					method: "GET",
					type:"JSON",
					data:{
						"page": currentPage
					},
					success: function(response){
						restaurants = response.DATA;
						totalPages = response.PAGINATION.TOTAL_PAGES;
						for(var i = 0; i < restaurants.length; i++){
							console.log(restaurants[i].NAME);
						}
						if(check){
							setUpPagination();
							check = false;
						}
					}
				});
			}
			
			getAllRestaurants();
	        
		});
    </script>
</body>
</html>