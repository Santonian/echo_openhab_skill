var itemApp = angular.module('itemApp', ['ngResource', 'ui.bootstrap'])

itemApp.factory("Item", function($resource){
	return $resource('/service/item/:id')
	
});

itemApp.controller("itemController", function($scope, Item){
	$scope.itemTypes = [
	               "SWITCH", 
	               "ROLLERSHUTTER", 
	               "NUMBER",
	               "TV",
	               "DIMMER",
	               "COLOR"
	               ];
	
	
	var loadAllItems = function(){
		Item.query(function(data){
			$scope.itemList = data
		})
	}

	var fillInfo = function(type){
		$scope.info=new Object()
		switch(type){
		case 'ROLLERSHUTTER':
			$scope.info.itemName = 'Item Name - For Rollershutter you can use something like north, small or all'
			$scope.info.location = 'Item Location (spoken location to Alexa like "living room")'
			break;
		case 'NUMBER':
			$scope.info.itemName = 'Item Name - The only supported Number is temperature right now, so put "temperature" in this field!'
			$scope.info.location = 'Item Location (spoken location to Alexa like "living room")'
			break;
		case 'TV':
			$scope.info.itemName = 'Item Name - TV Intent is WIP! - Put CHANNEL in this field for defining the channel command. Otherwise put the activity or action in this field'
			$scope.info.location = 'Item Location - For TV Intent the only supported location is living room. So put "living room" in this field'
			break;
		case 'COLOR':
			$scope.info.itemName = 'Item Name - Put COLOR in this field'
			$scope.info.location = 'Item Location - The location of the RGB (if there are multiple RGB in a location, the skill must be modified)'
			break;
		default:
			$scope.info.itemName = 'Item Name (spoken item Name to Alexa. Like "light". You should (must) use singular. Use light, not lights)'
			$scope.info.location = 'Item Location (spoken location to Alexa like "living room")'
			break;
		}
		
	}
	
	$scope.storeItem = function() {
        $scope.messages = [];

        Item.save($scope.item, function(value, responseHeaders){
        	//success
        	$scope.messages.push({type: 'success', msg: 'Item "' + value.openHabItem + '" saved'})
        	$scope.item=null
    		loadAllItems()
        }, function(httpResponse){
        	//failure
        	$scope.messages.push({type: 'warning', msg: 'Item of this type, name and location already exists!'})
        })
	}
	
    $scope.closeAlert = function (index) {
        $scope.messages.splice(index, 1);
    };
    
    $scope.deleteItem = function (id) {
    	Item.delete({id: id}, function(){
    		loadAllItems()
    	})
    }

    $scope.updateInfo = function(type){
    	fillInfo(type)
    }
    
    loadAllItems()
    fillInfo("SWITCH")
    
	
	 
});
