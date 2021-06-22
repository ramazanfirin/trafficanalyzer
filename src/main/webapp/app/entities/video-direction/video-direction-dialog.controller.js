(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionDialogController', VideoDirectionDialogController);

    VideoDirectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoDirection', 'VideoLine', 'Video'];

    function VideoDirectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VideoDirection, VideoLine, Video) {
        var vm = this;

        vm.videoDirection = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videolines = VideoLine.query();
        vm.videos = Video.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.videoDirection.id !== null) {
                VideoDirection.update(vm.videoDirection, onSaveSuccess, onSaveError);
            } else {
                VideoDirection.save(vm.videoDirection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:videoDirectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
