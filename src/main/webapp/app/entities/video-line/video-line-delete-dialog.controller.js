(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoLineDeleteController',VideoLineDeleteController);

    VideoLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'VideoLine'];

    function VideoLineDeleteController($uibModalInstance, entity, VideoLine) {
        var vm = this;

        vm.videoLine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VideoLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
