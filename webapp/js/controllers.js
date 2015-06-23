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
     * Map Controller
     * @param $scope
     * @constructor
     */
    var MapController = function($scope, $http, $q) {
        var type = false;

        function search_by_name() {
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
                //$http.get("http://ec2-54-147-248-210.compute-1.amazonaws.com:8080/mongorest/mongo/query?host=10.153.211.57&database=dbname&collection=fda_enforcement&filter={%22openfda.substance_name.0.0%22:%22CANDESARTAN%20CILEXETIL%22}").success(function (data, status) {
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

        function search_by_state() {

        }

        if(type) {
            search_by_name();
        } else {
            search_by_state();
        }
    };
    angular.module('App').controller('MapController', ["$scope", "$http", MapController]);


}());