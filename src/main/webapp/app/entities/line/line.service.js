(function() {
    'use strict';
    angular
        .module('trafficanalyzerApp')
        .factory('Line', Line);

    Line.$inject = ['$resource'];

    function Line ($resource) {
        var resourceUrl =  'api/lines/:id';

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
