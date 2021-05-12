(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('video-line', {
            parent: 'entity',
            url: '/video-line?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-line/video-lines.html',
                    controller: 'VideoLineController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('videoLine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-line-detail', {
            parent: 'video-line',
            url: '/video-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-line/video-line-detail.html',
                    controller: 'VideoLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('videoLine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VideoLine', function($stateParams, VideoLine) {
                    return VideoLine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'video-line',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('video-line-detail.edit', {
            parent: 'video-line-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-line/video-line-dialog.html',
                    controller: 'VideoLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoLine', function(VideoLine) {
                            return VideoLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-line.new', {
            parent: 'video-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-line/video-line-dialog.html',
                    controller: 'VideoLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                startPointX: null,
                                startPointY: null,
                                endPointX: null,
                                endPointY: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('video-line', null, { reload: 'video-line' });
                }, function() {
                    $state.go('video-line');
                });
            }]
        })
        .state('video-line.edit', {
            parent: 'video-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-line/video-line-dialog.html',
                    controller: 'VideoLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoLine', function(VideoLine) {
                            return VideoLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-line', null, { reload: 'video-line' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-line.delete', {
            parent: 'video-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-line/video-line-delete-dialog.html',
                    controller: 'VideoLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VideoLine', function(VideoLine) {
                            return VideoLine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-line', null, { reload: 'video-line' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
