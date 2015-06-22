(function() {
    /**
     * Home Factory
     * @param $http
     * @returns {{}}
     */
    var homeFactory = function($http) {
        var factory = {};

        factory.init = function() {
            return $http.get("data/sample.json")
                .then( function( results ) {
                    return results
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        return factory;
    };
    angular.module('App').factory('homeFactory', ['$http', homeFactory]);
}());