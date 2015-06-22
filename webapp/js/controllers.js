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
    var MapController = function($scope) {
        function init() {
            // Init map
        }
        init()
    };
    angular.module('App').controller('MapController', ['$scope', MapController]);


}());