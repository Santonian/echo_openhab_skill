var itemApp = angular.module('itemApp', ['ngResource'])


itemApp.factory("Item", function($resource){
	return $resource('/service/item')
	
});

itemApp.controller("itemController", function($scope, Item){
	$scope.itemTypes = [
	               "SWITCH", 
	               "ROLLERSHUTTER", 
	               "NUMBER",
	               "TV",
	               "DIMMER"
	               ];
	
	
	$scope.storeItem = function() {
        $scope.messages = [];

        Item.save($scope.item)
        
        $scope.messages.push({type: 'success', msg: 'Item saved'})
	}
	
    $scope.closeAlert = function (index) {
        $scope.messages.splice(index, 1);
    };
});