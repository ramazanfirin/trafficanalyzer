(function() {
    'use strict';
    angular
        .module('trafficanalyzerApp')
        .factory('VideoDirectionRecord', VideoDirectionRecord);

    VideoDirectionRecord.$inject = ['$resource', 'DateUtils'];

    function VideoDirectionRecord ($resource, DateUtils) {
        var resourceUrl =  'api/video-direction-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
             'getSummary': { method: 'GET', isArray: true,url:'/api/video-direction-records/getAllDataSummary/9'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insertDate = DateUtils.convertDateTimeFromServer(data.insertDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
