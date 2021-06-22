(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('VideoDirectionRecordDialogController', VideoDirectionRecordDialogController);

    VideoDirectionRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoDirectionRecord', 'VideoDirection'];

    function VideoDirectionRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VideoDirectionRecord, VideoDirection) {
        var vm = this;

        vm.videoDirectionRecord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.videodirections = VideoDirection.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.videoDirectionRecord.id !== null) {
                VideoDirectionRecord.update(vm.videoDirectionRecord, onSaveSuccess, onSaveError);
            } else {
                VideoDirectionRecord.save(vm.videoDirectionRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:videoDirectionRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
