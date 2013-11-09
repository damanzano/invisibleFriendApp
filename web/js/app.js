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
            .when('/participantes/:juegoId',{
                controller: 'participantesController',
                templateUrl: 'views/juegos/participantes.html'
            }).when('/personas',{
                controller: 'personasController',
                templateUrl: 'views/juegos/juegos.html'
            })
            .otherwise({redirectTo: '/'});
}]);