(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionRecordDetailController', VideoDirectionRecordDetailController);

    VideoDirectionRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoDirectionRecord', 'VideoDirection'];

    function VideoDirectionRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoDirectionRecord, VideoDirection) {
        var vm = this;

        vm.videoDirectionRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:videoDirectionRecordUpdate', function(event, result) {
            vm.videoDirectionRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
