(function() {
    'use strict';

    angular
        .module('trafficanalyzerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('video-direction-record', {
            parent: 'entity',
            url: '/video-direction-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoDirectionRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-direction-record/video-direction-records.html',
                    controller: 'VideoDirectionRecordController',
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
                    $translatePartialLoader.addPart('videoDirectionRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-direction-record-summary', {
            parent: 'entity',
            url: '/video-direction-record-summary?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoDirectionRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-direction-record/video-direction-records-summary.html',
                    controller: 'VideoDirectionRecordController',
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
                    $translatePartialLoader.addPart('videoDirectionRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-direction-record-detail', {
            parent: 'video-direction-record',
            url: '/video-direction-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalyzerApp.videoDirectionRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-direction-record/video-direction-record-detail.html',
                    controller: 'VideoDirectionRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('videoDirectionRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VideoDirectionRecord', function($stateParams, VideoDirectionRecord) {
                    return VideoDirectionRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'video-direction-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('video-direction-record-detail.edit', {
            parent: 'video-direction-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction-record/video-direction-record-dialog.html',
                    controller: 'VideoDirectionRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoDirectionRecord', function(VideoDirectionRecord) {
                            return VideoDirectionRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-direction-record.new', {
            parent: 'video-direction-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction-record/video-direction-record-dialog.html',
                    controller: 'VideoDirectionRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                insertDate: null,
                                vehicleType: null,
                                duration: null,
                                speed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('video-direction-record', null, { reload: 'video-direction-record' });
                }, function() {
                    $state.go('video-direction-record');
                });
            }]
        })
        .state('video-direction-record.edit', {
            parent: 'video-direction-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction-record/video-direction-record-dialog.html',
                    controller: 'VideoDirectionRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoDirectionRecord', function(VideoDirectionRecord) {
                            return VideoDirectionRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-direction-record', null, { reload: 'video-direction-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-direction-record.delete', {
            parent: 'video-direction-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-direction-record/video-direction-record-delete-dialog.html',
                    controller: 'VideoDirectionRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VideoDirectionRecord', function(VideoDirectionRecord) {
                            return VideoDirectionRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-direction-record', null, { reload: 'video-direction-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
