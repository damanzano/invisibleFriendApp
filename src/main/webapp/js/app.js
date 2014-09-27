/**
 * This is the main app javascript file
 * Created on : 07-nov-2013, 11:54:32
 * @author David Andrés Manzano Herrera <damanzano>
 */
var app = angular.module('invisibleFriendApp', ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
                .when('/', {
                    controller: 'mainController',
                    templateUrl: 'views/main.html'
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
                .when('/personas/:personaId', {
                    controller: 'personasController',
                    templateUrl: 'views/personas/persona-details.html'
                })
                .when('/personas/edit/:personaId', {
                    controller: 'personasController',
                    templateUrl: 'views/personas/persona-edit.html'
                })
                .when('/profile', {
                    controller: 'profileController',
                    templateUrl: 'views/players/player-profile.html'
                })
                .when('/profile/edit/:playerId', {
                    controller: 'profileController',
                    templateUrl: 'views/players/player-edit.html'
                })
//                .when('/login', {
//                    controller: 'loginController',
//                    templateUrl: 'views/authentication/google_login.html'
//                })
                .otherwise({redirectTo: '/'});
    }]);

/**
 * This custom directive fix a bug with Google Chrome input type="date" tags
 **/
app.directive('input', function () {
    return {
        require: '?ngModel',
        restrict: 'E',
        link: function (scope, element, attrs, ngModel) {
            if (attrs.type = "date" && ngModel) {
                element.bind('change', function () {
                    scope.$apply(function () {
                        ngModel.$setViewValue(element.val());
                    });
                });
            }
        }
    };
});