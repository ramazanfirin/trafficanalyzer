(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionDetailController', VideoDirectionDetailController);

    VideoDirectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoDirection', 'VideoLine', 'Video'];

    function VideoDirectionDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoDirection, VideoLine, Video) {
        var vm = this;

        vm.videoDirection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:videoDirectionUpdate', function(event, result) {
            vm.videoDirection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
