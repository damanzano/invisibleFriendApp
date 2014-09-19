/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Created on : 07-nov-2013, 15:02:43
 * @author David Andrés Manzano Herrera <damanzano>
 */

/**
 *  This factory manage the connection to the rest services
 *  
 *  @param {type} $http The inyected angular http object  
 */
app.factory('appFactory', ['$http', function($http) {
        var gamesUrlBase = 'rest/games';
        var playersUrlBase = 'rest/players';

        var games = [
            {numeroId: '1', descripcion: 'Desarrollo cunpleaños', fechaCreacion: '2013-10-01', fechaInicial: '2013-10-01', fechaFinal: '2013-11-06'}
            , {numeroId: '2', descripcion: 'Desarrollo Navidad', fechaCreacion: '2013-11-07', fechaInicial: '2013-11-15', fechaFinal: '2013-12-15'}
            , {numeroId: '3', descripcion: 'Reyes', fechaCreacion: '2011-10-01', fechaInicial: '2014-01-01', fechaFinal: '2014-01-06'}
        ];
        
        var players = [];

        var factory = {};
        
        // Game services
        factory.getGames = function() {
            return $http.get(gamesUrlBase);
        };
        factory.countGames = function() {
            return $http.get(gamesUrlBase + '/count');
        };
        factory.getGame= function(gameId) {
            return $http.get(gamesUrlBase + '/' + gameId);
        };
        factory.createGame = function(game) {
            /* Call the games/create service*/
            return $http.post(gamesUrlBase, game);
        };
        factory.updateGame = function(game) {
            /* Call the games/edit service*/
            return $http.put(gamesUrlBase, game);
        };
        factory.deleteGame = function(gameId) {
            /* Call the game/delete service*/
            return $http.delete(gamesUrlBase + '/' + gameId);
        };
        factory.getStaticGames = function() {
            return games;
        };
        
        
        // Players services
        factory.getPlayers = function() {
            return $http.get(playersUrlBase);
        };
        factory.countPlayers = function() {
            return $http.get(playersUrlBase + '/count');
        };
        factory.getPlayer = function(id) {
            return $http.get(playersUrlBase + '/' + id);
        };
        factory.createPlayer = function(persona) {
            /* Call the games/create service*/
            return $http.post(playersUrlBase, persona);
        };
        factory.updatePlayer = function(persona) {
            /* Call the games/edit service*/
            return $http.put(playersUrlBase, persona);
        };
        factory.deletePlayer = function(id) {
            /* Call the game/delete service*/
            return $http.delete(playersUrlBase + '/' + id);
        };
        factory.uploadPlayerFoto = function(photo) {
            /* Call the games/create service*/
            return $http({
                method: 'POST'
                , url: playersUrlBase +'/upload'
                , data: { files:photo}
                , headers: {'Content-Type': undefined}
                , transformRequest: function(data){
                    var formData = new FormData();
                    formData.append('files', photo);
                    return formData;
                }
            });
        };
        factory.loginPlayer = function(){
          return $http.get(playersUrlBase + '/login');  
        };
//        factory.login=function (person){
//          var $promise = $http.post(loginUrlBase, person);
//          $promise.then(function(mdg){
//             if(msg.data=="succes"){
//                 console.log("success login");
//             }else{
//                 console.log("error login");
//             }
//          });
//        };
        
        
        return factory;
    }]);