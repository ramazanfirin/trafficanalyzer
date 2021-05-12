(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoLineDialogController', VideoLineDialogController);

    VideoLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoLine', 'Video'];

    function VideoLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VideoLine, Video) {
        var vm = this;

        vm.videoLine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.videoLine.id !== null) {
                VideoLine.update(vm.videoLine, onSaveSuccess, onSaveError);
            } else {
                VideoLine.save(vm.videoLine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:videoLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
