(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('LineDetailController', LineDetailController);

    LineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Line', 'Camera'];

    function LineDetailController($scope, $rootScope, $stateParams, previousState, entity, Line, Camera) {
        var vm = this;

        vm.line = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:lineUpdate', function(event, result) {
            vm.line = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
