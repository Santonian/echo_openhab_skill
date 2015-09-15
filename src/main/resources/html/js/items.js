var itemApp = angular.module('itemApp', ['ngResource'])


itemApp.factory("Item", function($resource){
	return $resource('/service/item')
	
});

itemApp.controller("itemController", function($scope, Item){
	$scope.itemTypes = [
	               "SWITCH", 
	               "ROLLERSHUTTER", 
	               "NUMBER",
	               "TV"
	               ];
	
	
	$scope.storeItem = function() {
		Item.save($scope.item)
	}
});