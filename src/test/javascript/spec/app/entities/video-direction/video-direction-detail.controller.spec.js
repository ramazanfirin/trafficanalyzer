'use strict';

describe('Controller Tests', function() {

    describe('VideoDirection Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVideoDirection, MockVideoLine, MockVideo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVideoDirection = jasmine.createSpy('MockVideoDirection');
            MockVideoLine = jasmine.createSpy('MockVideoLine');
            MockVideo = jasmine.createSpy('MockVideo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VideoDirection': MockVideoDirection,
                'VideoLine': MockVideoLine,
                'Video': MockVideo
            };
            createController = function() {
                $injector.get('$controller')("VideoDirectionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalyzerApp:videoDirectionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
