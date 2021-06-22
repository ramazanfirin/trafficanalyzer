(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionRecordDeleteController',VideoDirectionRecordDeleteController);

    VideoDirectionRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'VideoDirectionRecord'];

    function VideoDirectionRecordDeleteController($uibModalInstance, entity, VideoDirectionRecord) {
        var vm = this;

        vm.videoDirectionRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VideoDirectionRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
