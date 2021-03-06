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
            'getFileList': { method: 'GET', isArray: true,url:'/api/videos/getFileList'},
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
