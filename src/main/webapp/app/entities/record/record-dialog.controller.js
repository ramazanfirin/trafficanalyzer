(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .controller('RecordDialogController', RecordDialogController);

    RecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Record', 'Line'];

    function RecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Record, Line) {
        var vm = this;

        vm.record = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lines = Line.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.record.id !== null) {
                Record.update(vm.record, onSaveSuccess, onSaveError);
            } else {
                Record.save(vm.record, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalyzerApp:recordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
