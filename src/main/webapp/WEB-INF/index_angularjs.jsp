<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
	<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="/resources/jquery.bootpag.min.js"></script>
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<title>Insert title here</title>
</head>
<body ng-app="myApp">

	<div ng-controller="myCtrl" ng-cloak>
		<h4 ng-repeat="res in restaurants">{{res.NAME}}</h4>
	</div>
	
	<div id="page-selection">
	
	</div>
	
	
	
	<script type="text/javascript">
 		var app = angular.module('myApp', []);
 		var check = true;
 		app.controller('myCtrl', function($scope, $http) {
 	 		$scope.currentPage = 1;
 	 		$scope.limit = 1;
 	 		
 	 		$scope.getAllRestaurant = function(){
 	 		    $http({
 	 		        method : "GET",
 	 		        url : "/rest",
 	 		        params:{
 	 		        	"page": $scope.currentPage
 	 		        }
 	 		    }).then(function mySuccess(response) {
 	 		        $scope.restaurants = response.data.DATA;
 	 	 	 		$scope.totalPage = response.data.PAGINATION.TOTAL_PAGES;
 	 	 	 		if(check){
	 	 	 	 		$scope.setUpPagination();
		 	 	 	 	check = false;
 	 	 	 		}
 	 	 	 	
 	 		    }, function myError(response) {
 	 		        alert('Error');
 	 		    });
 	 		}
 	 		
 	 		$scope.setUpPagination = function (){
 	 			// init bootpag
 		        $('#page-selection').bootpag({
 		            total: $scope.totalPage,
 		            page : $scope.currentPage
 		        }).on("page", function(event, num){
 		             $scope.currentPage = num;
 		           	 $scope.getAllRestaurant();
 		        });
 	 		}
	        
	        $scope.getAllRestaurant();
 		});
    </script>
</body>
</html>