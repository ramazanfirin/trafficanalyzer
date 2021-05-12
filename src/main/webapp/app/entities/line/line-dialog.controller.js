(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('LineDialogController', LineDialogController);

    LineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Line', 'Camera'];

    function LineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Line, Camera) {
        var vm = this;

        vm.line = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cameras = Camera.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.line.id !== null) {
                Line.update(vm.line, onSaveSuccess, onSaveError);
            } else {
                Line.save(vm.line, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:lineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
