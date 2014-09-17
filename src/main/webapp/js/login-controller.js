/* 
 * Universidad Icesi - SYRI, Desarrollo de sistemas
 */

/**
 * Created on : 30-jul-2014, 16:29:55
 * Login controller
 * @author David Andrés Manzano Herrera <damanzano>
 */
'use strict';
app.controller('loginController', ['$scope', '$routeParams', 'appFactory', function($scope, $routeParams, appFactory) {
        $scope.login =  function(person){
            appFactory.login(person);
        };
}]);