(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoRecordDetailController', VideoRecordDetailController);

    VideoRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoRecord', 'VideoLine'];

    function VideoRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoRecord, VideoLine) {
        var vm = this;

        vm.videoRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:videoRecordUpdate', function(event, result) {
            vm.videoRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
