(function() {
    'use strict';
    angular
        .module('trafficanalyzerApp')
        .factory('VideoRecord', VideoRecord);

    VideoRecord.$inject = ['$resource', 'DateUtils'];

    function VideoRecord ($resource, DateUtils) {
        var resourceUrl =  'api/video-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getSummary': { method: 'GET', isArray: true,url:'/api/video-records/getAllDataSummary/1'},
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
