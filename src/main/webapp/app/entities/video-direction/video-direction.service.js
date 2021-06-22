(function() {
    'use strict';
    angular
        .module('trafficanalyzerApp')
        .factory('VideoDirection', VideoDirection);

    VideoDirection.$inject = ['$resource'];

    function VideoDirection ($resource) {
        var resourceUrl =  'api/video-directions/:id';

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
