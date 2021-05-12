(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoLineDetailController', VideoLineDetailController);

    VideoLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoLine', 'Video'];

    function VideoLineDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoLine, Video) {
        var vm = this;

        vm.videoLine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:videoLineUpdate', function(event, result) {
            vm.videoLine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
