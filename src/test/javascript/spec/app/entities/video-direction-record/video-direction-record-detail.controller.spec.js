'use strict';

describe('Controller Tests', function() {

    describe('VideoDirectionRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVideoDirectionRecord, MockVideoDirection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVideoDirectionRecord = jasmine.createSpy('MockVideoDirectionRecord');
            MockVideoDirection = jasmine.createSpy('MockVideoDirection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VideoDirectionRecord': MockVideoDirectionRecord,
                'VideoDirection': MockVideoDirection
            };
            createController = function() {
                $injector.get('$controller')("VideoDirectionRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalyzerApp:videoDirectionRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
