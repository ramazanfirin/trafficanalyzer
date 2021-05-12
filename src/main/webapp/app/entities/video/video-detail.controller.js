(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDetailController', VideoDetailController);

    VideoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Video'];

    function VideoDetailController($scope, $rootScope, $stateParams, previousState, entity, Video) {
        var vm = this;

        vm.video = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalyzerApp:videoUpdate', function(event, result) {
            vm.video = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
