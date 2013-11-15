/**
 * This is the main app javascript file
 * Created on : 07-nov-2013, 11:54:32
 * @author David Andrés Maznzano Herrera <damanzano>
 */
var app = angular.module('invisibleFriendApp', ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
        $routeProvider
                .when('/', {
                    controller: 'juegosController',
                    templateUrl: 'views/juegos/juegos-list.html'
                })
                .when('/juegos', {
                    controller: 'juegosController',
                    templateUrl: 'views/juegos/juegos-list.html'
                })
                .when('/juegos/:juegoId', {
                    controller: 'juegosController',
                    templateUrl: 'views/juegos/juego-details.html'
                })
                .when('/juegos/edit/:juegoId', {
                    controller: 'juegosController',
                    templateUrl: 'views/juegos/juego-edit.html'
                })
                .when('/personas', {
                    controller: 'personasController',
                    templateUrl: 'views/personas/personas-list.html'
                })
                .when('/personas/:juegoId', {
                    controller: 'personasController',
                    templateUrl: 'views/personas/personas-details.html'
                })
                .when('/personas/edit/:juegoId', {
                    controller: 'personasController',
                    templateUrl: 'views/personas/personas-edit.html'
                })
                .otherwise({redirectTo: '/'});
    }]);

/**
 * This custom directive fix a bug with Googlr Chrome input type="date" tags
 **/
app.directive('input', function() {
    return {
        require: '?ngModel',
        restrict: 'E',
        link: function(scope, element, attrs, ngModel) {
            if (attrs.type = "date" && ngModel) {
                element.bind('change', function() {
                    scope.$apply(function() {
                        ngModel.$setViewValue(element.val());
                    });
                });
            }
        }
    };
});