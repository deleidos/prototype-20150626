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

    /**
     * Search Factory
     * @param $http
     * @returns {{}}
     */
    var searchFactory = function($http, $q) {
        var factory = {};

        factory.tabs = [
            {name : "Search by Drug", url : "partials/search-drugs.html"},
            {name : "Search by State", url : "partials/search-states.html"}
        ];

        factory.getDrugs = function() {
            return $http.get("data/drugs.json", {cache: true})
                .then( function( results ) {
                    return parseDrugs( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getStates = function() {
            return $http.get("data/states.json", {cache: true})
                .then( function( results ) {
                    return results.data
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        function parseDrugs( result ) {
            var drug_list = [];

            var length = result.length;
            for( var i=0; i<length; i++) {
                drug_list.push(result[i].openfda.brand_name[0][0])
            }

            return drug_list;
        }

        return factory;
    };
    angular.module('App').factory('searchFactory', ['$http', '$q', searchFactory]);
}());