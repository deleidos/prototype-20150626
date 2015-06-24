(function() {
    /**
     * Navigation Controller
     * @param $scope
     * @param $location
     * @constructor
     */
    var NavController = function($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
    };
    angular.module('App').controller('NavController', ['$scope', '$location', NavController]);

    /**
     * Home Controller
     * @param $scope
     * @constructor
     */
    var HomeController = function($scope, homeFactory) {
        function init() {
            homeFactory.init()
                .then(function (response) {
                    $scope.message = response.data.message
                }, function (e) {
                    $scope.message = "oops! Something went wrong."
                });
        }
        init()
    };
    angular.module('App').controller('HomeController', ['$scope', 'homeFactory', HomeController]);

    /**
     * Search Controller
     * @param $scope
     * @constructor
     */
    var SearchController = function($scope, $rootScope, searchFactory) {
        $scope.tabs = searchFactory.tabs;

        $scope.populate = function( tab_name ) {
            switch( tab_name ) {
                case $scope.tabs[0].name:
                    searchFactory.getDrugs()
                        .then( function( results ) {
                            $scope.drug_list = results
                        }, function( error ) {
                            // TODO show alert
                            console.log("got an error, ", error)
                        });
                    break;
                case $scope.tabs[1].name:
                    searchFactory.getStates()
                        .then( function( results ) {
                            $scope.state_list = results
                            $scope.selected_state = results[0].name
                        }, function( error ) {
                            // TODO show alert
                            console.log("got an error, ", error)
                        });
                    break;
                case $scope.tabs[2].name:
                    searchFactory.getManufacturers()
                        .then( function( results ) {
                            $scope.maker_list = results
                            $scope.selected_maker = results[0].name
                        }, function( error ) {
                            // TODO show alert
                            console.log("got an error, ", error)
                        });
                    break;
                default: break;
            }
        };

        $scope.dropdown = {
            isopen : false
        };

        $scope.toggleDropdown = function() {
            $scope.dropdown.isopen = this.search.drug_name.length
        };

        $scope.getDrugData = function(drug) {
            searchFactory.getDrugLabelInfo(drug)
                .then( function( data ) {
                    $scope.selected_drug = drug;
                    setLabelInfo( data.results[0] )
                }, function( error ) {
                    console.log("got an error: ", error);
                    $scope.selected_drug = null
                });
            searchFactory.getDrugRecallInfo(drug)
                .then( function( data ) {
                    setRecallInfo( data.results )
                }, function( error ) {
                    console.log("got an error: ", error);
                    $scope.selected_drug = null
                });
        };

        $scope.update = function(state){
            $rootScope.$broadcast('state-update', state)
        };

        function setLabelInfo( info ){
            if( info.purpose ){
                $scope.selected_drug_purpose = info.purpose[0].replace("PURPOSES ", "")
            }
            $scope.selected_drug_usage = info.indications_and_usage[0].replace("USES ", "")
            $scope.manufacturer_name = info.openfda.manufacturer_name[0]

            var length = info.openfda.substance_name.length;
            var active_ingredients= [];
            for(var i= 0; i<length; i++){
                active_ingredients.push({name: info.openfda.substance_name[i]})
            }
            $scope.active_ingredients = active_ingredients
        }

        function setRecallInfo( results ){
            var length = results.length
            // fix the dates
            for(var i= 0; i<length; i++){
                var date = results[i].recall_initiation_date;
                results[i].recall_initiation_date = new Date(date.substring(0,4), date.substring(4,6), date.substring(6,8)).toDateString()
            }
            $scope.recalls = results
        }

    };
    angular.module('App').controller('SearchController', ['$scope', '$rootScope', 'searchFactory', SearchController]);

    /**
     * Map Controller
     * @param $scope
     * @constructor
     */

    var MapController = function($scope, $http, $rootScope, mapFactory) {
        var type = false;

        angular.extend($scope, {
            center: {
                lat: 39,
                lng: -100,
                zoom: 4
            }
        });

        function search_by_name() {
            var all_locations = [];
            $http.get("data/us-states.json").success(function(response, status) {
                state_location = response.features
                //$http.get("http://ec2-54-147-248-210.compute-1.amazonaws.com:8080/mongorest/mongo/query?host=10.153.211.57&database=dbname&collection=fda_enforcement&filter={%22openfda.brand_name.0.0%22:%22ADVIL%20PM%22}").success(function (data, status) {
                $http.get("data/state-response.json").success(function (data, status) {
                recall_location = data.results[0].recall_area
                    angular.forEach(recall_location, function (recall_locale) {
                        angular.forEach(state_location, function (state_locale) {
                            if (recall_locale == state_locale.properties.name || recall_locale == "Nationwide") {
                                all_locations.push(state_locale);
                            }
                        });
                    });
                });
            });
            angular.extend($scope, {
                geojson: {
                    data: all_locations,
                    style: {
                        fillColor: "orange",
                        weight: 1,
                        opacity: 1,
                        color: 'white',
                        dashArray: '3',
                        fillOpacity: 0.7
                    }
                }
            });
        }

        function search_by_state() {

            $scope.$on('state-update', function(event, args) {
                selected_state = args;

                $rootScope.all_drugs = []

                mapFactory.getRecallsByState(selected_state)
                    .then(function (results) {
                        angular.forEach(results, function(drug_name){
                            $rootScope.all_drugs.push(drug_name);
                        });
                    }, function (error) {
                        // TODO show alert
                        console.log("got an error, ", error)
                    });

                mapFactory.getRecallsByState("Nationwide")
                    .then(function (results) {
                        angular.forEach(results, function(drug_name){
                            $rootScope.all_drugs.push(drug_name);
                        });
                        $rootScope.$broadcast('length-update', ($rootScope.all_drugs).length)
                    }, function (error) {
                        // TODO show alert
                        console.log("got an error, ", error)
                    });

                var one_location = []
                $http.get("data/us-states.json").success(function (response, status) {
                    state_location = response.features
                    angular.forEach(state_location, function (state_locale) {
                        if (state_locale.properties.name == selected_state) {
                            one_location.push(state_locale)
                        }
                    });
                });

                angular.extend($scope, {
                    geojson: {
                        data: one_location,
                        style: {
                            fillColor: "red",
                            weight: 1,
                            opacity: 1,
                            color: 'white',
                            dashArray: '3',
                            fillOpacity: 0.7
                        }
                    }
                });
            });
        }

        if(type) {
            search_by_name();
        } else {
            search_by_state();
        }
    };
    angular.module('App').controller('MapController', ["$scope", "$http", "$rootScope", "mapFactory", MapController]);

    angular.module('App').controller('PaginationCtrl', function ($scope, $rootScope, $log) {

        $scope.$on('length-update', function(event, args){
            $scope.totalItems = args
        });

        $scope.currentPage = 1
        $scope.itemsPerPage = 3
        $scope.maxSize = 5

        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo
        };

        $scope.pageChanged = function() {
            $log.log('Page changed to: ' + $scope.currentPage)
        };

    });


}());