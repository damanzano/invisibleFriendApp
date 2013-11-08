/**
 * This is the nmain app javascript file
 * Created on : 07-nov-2013, 11:54:32
 * @author David Andrés Maznzano Herrera <damanzano>
 */
var app = angular.module('invisibleFriendApp',['']);
app.config(function($routeProvider){
   $routeProvider
           .when('/',{
               controller:'juegosController',
               templateUrl:'views/juegos/juegos.html'
           })
           .when('/createJuego',{
               controller:'juegosController',
               templateUrl:'views/juegos/create.html'
           })
           .otherwise({redirectTo:'/'});
});
