(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoLineCreateController', VideoLineCreateController);

    VideoLineCreateController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoLine', 'Video','$window'];

    function VideoLineCreateController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VideoLine, Video,$window) {
        var vm = this;

        vm.videoLine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
	    vm.polygons = []; 
	    vm.addToPolygonList = addToPolygonList;
		vm.polygon = [];
	    vm.points = $window.points;
	    vm.changeMessage = changeMessage;
	    vm.deleteAll = deleteAll;
	    
	    vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    
	    vm.addMessage = vm.addMessageBefore;
	    
	    loadAll ();
	    
	    function loadAll () {
	    	vm.polygon = new Object();
			vm.polygon.id = 1;
			vm.polygon.data = [];
			
			vm.polygon.data.push(316.3999938964844);
			vm.polygon.data.push(307.40000915527344);
			vm.polygon.data.push(410.3999938964844);
			vm.polygon.data.push(361.40000915527344);
			vm.polygon.data.push(371.3999938964844);
			vm.polygon.data.push(397.40000915527344);
			vm.polygon.data.push(274.3999938964844);
			vm.polygon.data.push(325.40000915527344);
			
			vm.polygons.push(vm.polygon);
			
			
			
			
		   		 
	    }
	    
	    function changeMessage () {
	    	vm.addMessage = vm.addMessageAfter;
	    }
	    
	    function deleteAll () {
	    	$window.points = [];
            $window.poly = null;
            vm.polygons = [];
	    }
	    
		function addToPolygonList () {
	        var i;
	        vm.polygon = new Object();
			vm.polygon.id = $window.tempId;
			vm.polygon.name = "1";
			vm.polygon.data = [];
			
			for (i = 0; i < $window.points.length; i++) {
  				vm.polygon.data.push($window.points[i]);
			} 	
            vm.polygons.push(vm.polygon);
            $window.points = [];
            $window.poly = null;
            vm.addMessage = vm.addMessageBefore;
        } 

 		 

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
//            loadAll();
//            loadAll();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.videoLine.id !== null) {
                VideoLine.update(vm.videoLine, onSaveSuccess, onSaveError);
            } else {
                VideoLine.save(vm.videoLine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:videoLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
