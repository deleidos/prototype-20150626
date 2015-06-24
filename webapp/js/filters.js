(function() {
    var capitalize = function() {
        return function(input, all) {
            return (!!input) ?
                !all ?  input.charAt(0).toUpperCase() + input.slice(1).toLowerCase() :
                    input.replace(/([^\W_]+[^\s-]*) */g,
                        function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}) : '';
        }
    }
    angular.module('App').filter('capitalize', capitalize);
}());