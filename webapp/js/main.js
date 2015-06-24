(function() {
    var app = angular.module('App', ['ui.utils', 'ngRoute', 'ui.bootstrap', 'ngAnimate', 'leaflet-directive']);

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
