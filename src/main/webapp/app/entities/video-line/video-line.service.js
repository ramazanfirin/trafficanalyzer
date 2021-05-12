(function() {
    'use strict';
    angular
        .module('trafficanalyzerApp')
        .factory('VideoLine', VideoLine);

    VideoLine.$inject = ['$resource'];

    function VideoLine ($resource) {
        var resourceUrl =  'api/video-lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
