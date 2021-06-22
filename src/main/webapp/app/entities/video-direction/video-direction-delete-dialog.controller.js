(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionDeleteController',VideoDirectionDeleteController);

    VideoDirectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'VideoDirection'];

    function VideoDirectionDeleteController($uibModalInstance, entity, VideoDirection) {
        var vm = this;

        vm.videoDirection = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VideoDirection.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
