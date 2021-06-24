(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('video-direction', {
            parent: 'entity',
            url: '/video-direction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoDirection.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-direction/video-directions.html',
                    controller: 'VideoDirectionController',
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
                    $translatePartialLoader.addPart('videoDirection');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-direction-detail', {
            parent: 'video-direction',
            url: '/video-direction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoDirection.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-direction/video-direction-detail.html',
                    controller: 'VideoDirectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('videoDirection');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VideoDirection', function($stateParams, VideoDirection) {
                    return VideoDirection.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'video-direction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('video-direction-detail.edit', {
            parent: 'video-direction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction/video-direction-dialog.html',
                    controller: 'VideoDirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoDirection', function(VideoDirection) {
                            return VideoDirection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-direction.new', {
            parent: 'video-direction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction/video-direction-dialog.html',
                    controller: 'VideoDirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                indexValue: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('video-direction', null, { reload: 'video-direction' });
                }, function() {
                    $state.go('video-direction');
                });
            }]
        })
        .state('video-direction.edit', {
            parent: 'video-direction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction/video-direction-dialog.html',
                    controller: 'VideoDirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoDirection', function(VideoDirection) {
                            return VideoDirection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-direction', null, { reload: 'video-direction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-direction.delete', {
            parent: 'video-direction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction/video-direction-delete-dialog.html',
                    controller: 'VideoDirectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VideoDirection', function(VideoDirection) {
                            return VideoDirection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-direction', null, { reload: 'video-direction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
