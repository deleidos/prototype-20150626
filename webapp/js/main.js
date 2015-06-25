(function() {
    var app = angular.module('App', ['ui.utils', 'ngRoute', 'ui.bootstrap', 'ngAnimate', 'leaflet-directive']);

    app.directive('state',function(){
        return {
            templateUrl:'partials/state-results.html',
            restrict: 'E'
        }
    });

    app.directive('manufacturer',function(){
        return {
            templateUrl:'partials/manufacturer-results.html',
            restrict: 'E'
        }
    });

    app.directive('drugs',function(){
        return {
            templateUrl:'partials/drug-results.html',
            restrict: 'E'
        }
    });

    app.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

        $routeProvider.
            when('/', {
                templateUrl: 'partials/home.html',
                controller: 'MainController'
            }).
            when('/about', {
                templateUrl: 'partials/about.html'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);
}());
