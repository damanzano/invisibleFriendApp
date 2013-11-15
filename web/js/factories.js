/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Created on : 07-nov-2013, 15:02:43
 * @author David Andrés Maznzano Herrera <damanzano>
 */

/**
 *  This factory manage the connection to the rest services
 *  
 *  @param {type} $http The inyected angular http object  
 */
app.factory('appFactory', ['$http', function($http) {
        var juegosUrlBase = 'webresources/juegos';
        var particUrlBase = 'webresources/participantes';
        var personasUrlBase = 'webresources/personas';
        var amigosdeUrlBase = 'webresources/amigosde';

        var juegos = [
            {numeroId: '1', descripcion: 'Desarrollo cunpleaños', fechaCreacion: '2013-10-01', fechaInicial: '2013-10-01', fechaFinal: '2013-11-06'}
            , {numeroId: '2', descripcion: 'Desarrollo Navidad', fechaCreacion: '2013-11-07', fechaInicial: '2013-11-15', fechaFinal: '2013-12-15'}
            , {numeroId: '3', descripcion: 'Reyes', fechaCreacion: '2011-10-01', fechaInicial: '2014-01-01', fechaFinal: '2014-01-06'}
        ];
        
        var personas = [];
        var participantes = [];
        var amigosDe = [];

        var factory = {};
        
        // Juegos services
        factory.getJuegos = function() {
            return $http.get(juegosUrlBase);
        };
        factory.countJuegos = function() {
            return $http.get(juegosUrlBase + '/count');
        };
        factory.getJuego = function(juegoId) {
            return $http.get(juegosUrlBase + '/' + juegoId);
        };
        factory.createJuego = function(juego) {
            /* Call the juegos/create service*/
            return $http.post(juegosUrlBase, juego);
        };
        factory.updateJuego = function(juego) {
            /* Call the juegos/edit service*/
            return $http.put(juegosUrlBase, juego);
        };
        factory.deleteJuego = function(juegoId) {
            /* Call the juego/delete service*/
            return $http.delete(juegosUrlBase + '/' + juegoId);
        };
        factory.getStaticJuegos = function() {
            return juegos;
        };
        
        // Participantes services
        factory.getParticipantes = function() {
            return $http.get(particUrlBase);
        };
        factory.countParticipantes = function() {
            return $http.get(particUrlBase + '/count');
        };
        factory.getParticipante = function(id) {
            return $http.get(particUrlBase + '/' + id);
        };
        factory.createParticipante = function(participante) {
            /* Call the juegos/create service*/
            return $http.post(particUrlBase, participante);
        };
        factory.updateParticipante = function(participante) {
            /* Call the juegos/edit service*/
            return $http.put(particUrlBase, participante);
        };
        factory.deleteParticipante = function(id) {
            /* Call the juego/delete service*/
            return $http.delete(particUrlBase + '/' + id);
        };
        
        // Personas services
        factory.getPersonas = function() {
            return $http.get(personasUrlBase);
        };
        factory.countPersonas = function() {
            return $http.get(personasUrlBase + '/count');
        };
        factory.getPersona = function(id) {
            return $http.get(personasUrlBase + '/' + id);
        };
        factory.createPersona = function(persona) {
            /* Call the juegos/create service*/
            return $http.post(personasUrlBase, persona);
        };
        factory.updatePersona = function(persona) {
            /* Call the juegos/edit service*/
            return $http.put(personasUrlBase, persona);
        };
        factory.deletePersona = function(id) {
            /* Call the juego/delete service*/
            return $http.delete(personasUrlBase + '/' + id);
        };
        factory.uploadPersonaFoto = function(foto) {
            /* Call the juegos/create service*/
            return $http.post(personasUrlBase +'/upload', foto);
        };
        
        
        return factory;
    }]);