(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('CameraDetailController', CameraDetailController);

    CameraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Camera', 'Location'];

    function CameraDetailController($scope, $rootScope, $stateParams, previousState, entity, Camera, Location) {
        var vm = this;

        vm.camera = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:cameraUpdate', function(event, result) {
            vm.camera = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
