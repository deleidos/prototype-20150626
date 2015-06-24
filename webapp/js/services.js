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
            {name : "Search by State", url : "partials/search-states.html"},
            {name : "Search by Manufacturer", url : "partials/search-manufacturer.html"}
        ];

        factory.getDrugs = function() {
            //return $http.get("http://ec2-54-147-248-210.compute-1.amazonaws.com:8080/mongorest/mongo/query?host=10.153.211.57&database=dbname&collection=fda_enforcement&fields=openfda.brand_name", {cache: true})
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

        factory.getManufacturers = function() {
            return $http.get("data/manufacturers.json", {cache: true})
                .then( function( results ) {
                    return parseManufacturers( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getDrugLabelInfo = function( drug_name ) {
            var drug_name_urlencode = drug_name.replace(" ", "+");
            //return $http.get("https://api.fda.gov/drug/label.json?api_key=iNSQYfxgqZX5zRRtCLhDiLjRKOlacIexWT78gxHR&search=openfda.brand_name:\"" + drug_name_urlencode + "\"")
            return $http.get("data/sample-label-response.json")
                .then( function( results ) {
                    return results.data
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getDrugRecallInfo = function( drug_name ) {
            //return $http.get("http://ec2-54-147-248-210.compute-1.amazonaws.com:8080/mongorest/mongo/query?host=10.153.211.57&database=dbname&collection=fda_enforcement&filter={\"openfda.substance_name.0.0\":\"" + drug_name + "\"}")
            return $http.get("data/sample-drug-recall-response.json")
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

        function parseManufacturers( result ) {
            var drug_list = [];

            var length = result.length;
            for( var i=0; i<length; i++) {
                drug_list.push(result[i].openfda.manufacturer_name[0][0])
            }

            return drug_list;
        }

        return factory;
    };
    angular.module('App').factory('searchFactory', ['$http', '$q', searchFactory]);

    var mapFactory = function($http, $q) {
        var factory = {};

        factory.getRecallsByDrug = function() {
            return $http.get("data/drugs.json", {cache: true})
                .then( function( results ) {
                    return parseDrugNames( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getRecallsByState = function(selected_state) {
            //return $http.get("http://ec2-54-147-248-210.compute-1.amazonaws.com:8080/mongorest/mongo/query?host=10.153.211.57&database=dbname&collection=fda_enforcement&filter={%22recall_area%22:%22" + selected_state + "%22}", {cache:true})
            return $http.get("data/state-search.json", {cache: true})
                .then( function( results ) {
                    return parseDrugNames( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        function parseDrugNames( result ) {
            var drug_list = [];
            var drug_list_obj

            var length = result.length;
            for( var i=0; i<length; i++) {
                drug_list_obj = {}
                drug_list_obj.name = result[i].openfda.brand_name[0][0]
                drug_list_obj.reason = result[i].reason_for_recall
                dateString = result[i].recall_initiation_date
                year = dateString.substring(0,4);
                month = dateString.substring(4,6);
                day = dateString.substring(6,8);
                drug_list_obj.date = year + "-" + month + "-" + day
                drug_list.push(drug_list_obj)
            }

            return drug_list;
        }

        return factory;
    };
    angular.module('App').factory('mapFactory', ['$http', '$q', mapFactory]);


}());