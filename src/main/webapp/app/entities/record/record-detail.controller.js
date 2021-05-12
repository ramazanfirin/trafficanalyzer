(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('RecordDetailController', RecordDetailController);

    RecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Record', 'Line'];

    function RecordDetailController($scope, $rootScope, $stateParams, previousState, entity, Record, Line) {
        var vm = this;

        vm.record = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:recordUpdate', function(event, result) {
            vm.record = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
