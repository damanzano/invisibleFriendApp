/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Created on : 07-nov-2013, 15:02:12
 * @author David Andrés Maznzano Herrera <damanzano>
 */
app.controller('juegosController', ['$scope', 'appFactory', function ($scope, appFactory) {
    $scope.juegos = [];
    init();
    function init(){
        $scope.juegos = juegosFactory.getJuegos();
    }
    
    $scope.addJuego = function(){
        num_juegos++;
        fecha= new Date();
        $scope.juegos.push({
            numeroId:num_juegos
            , descripcion:$scope.newJuego.descripcion
            , fechaCreacion:fecha
            , fechaInicial:$scope.newJuego.fechaInicial
            , fechaFinal:$scope.newJuego.fechaFinal});
    };
}]);