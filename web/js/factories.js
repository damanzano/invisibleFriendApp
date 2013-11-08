/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Created on : 07-nov-2013, 15:02:43
 * @author David Andrés Maznzano Herrera <damanzano>
 */
app.factory('appFactory', ['$http', function($http) {
        var juegosUrlBase = '/webresources/juegos'; 

        var juegos = [
            {numeroId: '1', descripcion: 'Desarrollo cunpleaños', fechaCreacion: '2013-10-01', fechaInicial: '2013-10-01', fechaFinal: '2013-11-06'}
            , {numeroId: '2', descripcion: 'Desarrollo Navidad', fechaCreacion: '2013-11-07', fechaInicial: '2013-11-15', fechaFinal: '2013-12-15'}
            , {numeroId: '3', descripcion: 'Reyes', fechaCreacion: '2011-10-01', fechaInicial: '2014-01-01', fechaFinal: '2014-01-06'}
        ];
        
        var personas = [];
        var participantes = [];
        var amigosDe = [];

        var factory = {};
        factory.getJuegos = function() {
            return $http.get(juegosUrlBase);
        };
        factory.getJuego = function(juegoId) {
            return $http.get(juegosUrlBase + '/' + juegoId);
        };
        factory.createJuego = function(juego) {
            /* Call the juegos/create service*/
            return $http.post(juegosUrlBase, juego)
        };
        factory.updateJuego = function(juego) {
            /* Call the juegos/edit service*/
            return $http.put(juegosUrlBase, juego)
        };
        factory.deleteJuego = function(juegoId) {
            /* Call the juego/delete service*/
            return $http.delete(juegosUrlBase + '/' + juegoId);
        };
        return factory;
    }]);