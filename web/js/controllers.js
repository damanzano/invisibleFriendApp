/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Created on : 07-nov-2013, 15:02:12
 * Juegos entity controller
 * @author David Andrés Maznzano Herrera <damanzano>
 */
app.controller('juegosController', ['$scope', 'appFactory', function($scope, appFactory) {
        $scope.status;
        $scope.juegos;

        init();
        function init() {
            appFactory.getJuegos()
                    .success(function(juegosCollection) {
                        if(juegosCollection != null){
                            $scope.juegos = juegosCollection;
                        }else{
                            $scope.juegos = appFactory.getStaticJuegos();
                        }
                    }).error(function(error) {
                        $scope.status = 'Unable to load juegos data: ' + error.message;
                        $scope.juegos = appFactory.getStaticJuegos();
                    });
        }

        $scope.updateJuego = function(juegoId) {
            var juego;
            for (var i = 0; i < $scope.juegos.length; i++) {
                var currentJuego = $scope.juegos[i];
                if (currentJuego.numeroId === juegoId) {
                    juego = currentJuego;
                    break;
                }
            }

            appFactory.updateJuego(juego)
                    .succes(function() {
                        $scope.status = 'Updated juego! Refreshing the juegos liost.';
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to update juego: ' + error.message;
                    });
        };

        $scope.createJuego = function() {
            var newJuego = {
                descripcion: $scope.newJuego.descripcion
                , fechaCreacion: new Date()
                , fechaInicial: $scope.newJuego.fechaInicial
                , fechaFinal: $scope.newJuego.fechaFinal
            };

            appFactory.createJuego(newJuego)
                    .success(function() {
                        $scope.status = 'Inserted juego! Refreshing customer list.';
                        $scope.customers.push(cust);
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to insert juego: ' + error.message;
                    });
        };

        $scope.deleteJuego = function(juegoId) {
            appFactory.deleteJuego(juegoId)
                    .success(function() {
                        $scope.status = 'Deleted juego! Refreshing customer list.';
                        for (var i = 0; i < $scope.customers.length; i++) {
                            var currentJuego = $scope.juegos[i];
                            if (currentJuego.numeroId === juegoId) {
                                $scope.juego.splice(i, 1);
                                break;
                            }
                        }
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to delete juego: ' + error.message;
                    });
        };

        $scope.addJuego = function() {
            num_juegos++;
            fecha = new Date();
            $scope.juegos.push({
                numeroId: num_juegos
                , descripcion: $scope.newJuego.descripcion
                , fechaCreacion: fecha
                , fechaInicial: $scope.newJuego.fechaInicial
                , fechaFinal: $scope.newJuego.fechaFinal});
        };
    }]);

/**
 * Created on : 07-nov-2013, 15:02:12
 * Participante entity controller
 * @author David Andrés Maznzano Herrera <damanzano>
 */
app.controller('participantesController', ['$scope', '$routeParams' ,'appFactory', function($scope, $routeParams, appFactory) {
        $scope.juego;
        $scope.status;
        $scope.participantes;

        init();
        function init() {
            var juegoId = ($routeParams.juegoId) ? parseInt($routeParams.juegoId) : 0;
            if(juegoId > 0){
                 appFactory.getJuego(juegoId)
                         .success(function(juegoObject){
                             $scope.juego = juegoObject;
                         })
                         .error(function(error){
                             $scope.status = 'Unable to load juego data: ' + error.message;
                         });
                   
                                            
                            
                
            }
            appFactory.getJuegos()
                    .success(function(juegosCollection) {
                        if(juegosCollection != null){
                            $scope.juegos = juegosCollection;
                        }else{
                            $scope.juegos = appFactory.getStaticJuegos();
                        }
                    }).error(function(error) {
                        $scope.status = 'Unable to load participantes data: ' + error.message;
                        $scope.juegos = appFactory.getStaticJuegos();
                    });
        }

        
    }]);