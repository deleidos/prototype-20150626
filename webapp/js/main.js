(function() {
    var app = angular.module('App', ['ngRoute', 'ui.bootstrap', 'ngAnimate', 'leaflet-directive']);

    app.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'partials/home.html',
                controller: 'HomeController'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);
}());
