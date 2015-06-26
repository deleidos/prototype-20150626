(function() {
    /**
     * Main Factory
     * @returns {{}}
     */
    var mainFactory = function() {
        var factory = {};
        factory.defaultHost = "openfda.deleidos.com";
        return factory;
    };
    angular.module('App').factory('mainFactory', [ mainFactory ]);

    /**
     * Search Factory
     * @param $http
     * @returns {{}}
     */
    var searchFactory = function($http, $q, mainFactory) {
        var factory = {};

        factory.tabs = [
            {name : "Search by Drug", url : "partials/search-drugs.html"},
            {name : "Search by State", url : "partials/search-states.html"},
            {name : "Search by Manufacturer", url : "partials/search-manufacturer.html"}
        ];

        factory.getDrugs = function() {
            var url = mainFactory.useTestData ? "data/drugs.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/query?host=mongo&database=dbname&collection=fda_enforcement&fields=openfda.brand_name";

            return $http.get(url, {cache: true})
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
            var url = mainFactory.useTestData ? "data/manufacturers.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/query?host=mongo&database=dbname&collection=fda_enforcement&fields=openfda.manufacturer_name";
            return $http.get(url, {cache: true})
                .then( function( results ) {
                    return parseManufacturers( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getDrugLabelInfo = function( drug_name ) {
            var drug_name_urlencode = drug_name.replace(" ", "+");
            var url = mainFactory.useTestData ? "data/sample-label-response.json" :
                "https://api.fda.gov/drug/label.json?api_key=iNSQYfxgqZX5zRRtCLhDiLjRKOlacIexWT78gxHR&search=openfda.brand_name:\"" + drug_name_urlencode + "\"";

            return $http.get(url)
                .then( function( results ) {
                    return results.data
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getDrugRecallInfo = function( drug_name ) {
            var url = mainFactory.useTestData ? "data/sample-drug-recall-response.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/query?host=mongo&database=dbname&collection=fda_enforcement&filter={\"openfda.brand_name.0.0\":\"" + drug_name + "\"}";

            return $http.get(url)
                .then( function( results ) {
                    return results.data
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getManufacturerInfo = function( maker_name ) {
            var maker_name_urlencode = maker_name.replace(" ", "%20");
            var url = mainFactory.useTestData ? "data/sample-manufacturer-response.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/statecount?host=mongo&database=dbname&collection=fda_enforcement&manufacturer=" + maker_name_urlencode;
            return $http.get(url)
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
    angular.module('App').factory('searchFactory', ['$http', '$q', 'mainFactory', searchFactory]);

    var mapFactory = function($http, $q, mainFactory) {
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
            var url = mainFactory.useTestData ? "data/state-search.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/query?host=mongo&database=dbname&collection=fda_enforcement&filter={%22recall_area%22:%22" + selected_state + "%22}";
            return $http.get(url, {cache: true})
                .then( function( results ) {
                    return parseDrugNames( results.data['results'] )
                }, function( error ) {
                    return $q.reject(error.data)
                });
        };

        factory.getRecallsCountByState = function(selected_state) {
            var url = mainFactory.useTestData ? "data/state-search.json" :
                "http://" + mainFactory.defaultHost + "/mongorest/mongo/query?host=mongo&database=dbname&collection=fda_enforcement&filter={%22recall_area%22:%22" + selected_state + "%22}";
            return $http.get(url, {cache: true})
                .then( function( results ) {
                    return (results.data.count)
                }, function( error ) {
                    return 0
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
                var date = result[i].recall_initiation_date
                result[i].recall_initiation_date = new Date(date.substring(0,4), date.substring(4,6), date.substring(6,8)).toDateString();
                drug_list_obj.date = result[i].recall_initiation_date
                drug_list.push(drug_list_obj)
            }
            return drug_list;
        }
        return factory;
    };
    angular.module('App').factory('mapFactory', ['$http', '$q', 'mainFactory', mapFactory]);


}());