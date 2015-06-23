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
    var SearchController = function($scope, searchFactory) {
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
                default: break;
            }
        };

    };
    angular.module('App').controller('SearchController', ['$scope', 'searchFactory', SearchController]);

    /**
     * Map Controller
     * @param $scope
     * @constructor
     */
    var MapController = function($scope, $http) {
        function init() {
            angular.extend($scope, {
                center: {
                    lat: 39,
                    lng: -100,
                    zoom: 4
                }
            });
            var all_locations = [];
            $http.get("data/us-states.json").success(function(response, status) {
                state_location = response.features
                $http.get("data/state-response.json").success(function (data, status) {
                    recall_location = data.states
                    angular.forEach(recall_location, function (recall_locale) {
                        angular.forEach(state_location, function (state_locale) {
                            if (recall_locale == state_locale.properties.name) {
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
                        fillColor: "red",
                        weight: 1,
                        opacity: 1,
                        color: 'white',
                        dashArray: '3',
                        fillOpacity: 0.7
                    }
                }
            });
        }
        init()
    };
    angular.module('App').controller('MapController', ["$scope", "$http", MapController]);


}());