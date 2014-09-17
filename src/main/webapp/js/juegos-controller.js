/* 
 * Universidad Icesi - SYRI, Desarrollo de sistemas
 */

/**
 * Created on : 07-nov-2013, 15:02:12
 * Juegos entity controller
 * @author David Andrés Manzano Herrera <damanzano>
 */
app.controller('juegosController', ['$scope', '$routeParams', 'appFactory', function($scope, $routeParams, appFactory) {
        $scope.status;
        $scope.juegos = [];
        $scope.currentJuego;
        $scope.personas = [];

        init();
        function init() {
            var juegoId = ($routeParams.juegoId) ? parseInt($routeParams.juegoId) : 0;
            getJuegos(juegoId);

        }

        function getJuegos(juegoId) {
            appFactory.getJuegos()
                    .success(function(juegosCollection) {
                        if (juegosCollection != null) {
                            var json = juegosCollection.juego;
                            if (json instanceof Array) {
                                for (var i = 0; i < juegosCollection.juego.length; i++) {
                                    $scope.juegos.push(juegosCollection.juego[i]);
                                }
                            } else {
                                $scope.juegos.push(juegosCollection.juego);
                            }
                        } 

                        if (juegoId > 0) {
                            getCurrentJuego(juegoId);
                        }
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to load juegos data: ' + error.message;
                        $scope.juegos = appFactory.getStaticJuegos();
                    });
        }
        
        function getCurrentJuego(juegoId) {
            for (var i = 0; i < $scope.juegos.length; i++) {
                if ($scope.juegos[i].numeroId == juegoId) {
                    $scope.currentJuego = $scope.juegos[i];
                    getPersonas($scope.currentJuego);
                    break;
                }
            }

            //if juego is not in the list try to get it from database
            if ($scope.currentJuego == null) {
                appFactory.getJuego(juegoId)
                        .success(function(juegoObject) {
                            $scope.currentJuego = juegoObject;
                            getPersonas($scope.currentJuego);
                        })
                        .error(function(error) {
                            $scope.status = 'Unable to load juego data: ' + error.message;
                        });
            }
        }
        
        function getPersonas(juego) {
            appFactory.getPersonas()
                    .success(function(personasCollection) {
                        if (personasCollection != null) {
                            var json = personasCollection.personas;
                            if (json instanceof Array) {
                                for (var i = 0; i < personasCollection.personas.length; i++) {
                                    var persona = personasCollection.personas[i];
                                    //verify if this persona is a participante, if not do not include in the array

                                    var found = false;
                                    for (var j = 0; j < juego.participantesCollection.length; j++) {
                                        var participante = juego.participantesCollection[j];
                                        if (participante.personas.numeroId == persona.numeroId) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        $scope.personas.push(persona);
                                    }

                                }
                            } else {
                                //TODO complete this functionality
                                $scope.personas.push(personasCollection.personas);
                            }
                        }
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to load juegos data: ' + error.message;
                        $scope.juegos = appFactory.getStaticJuegos();
                    });
        }

        

        $scope.updateJuego = function(juego) {
            appFactory.updateJuego(juego)
                    .success(function(data, status, headers, config) {
                        $scope.status = 'Updated juego! Refreshing the juegos list.';

                        console.log(juego);
                        //refresh the juegos list
                        for (var i = 0; i < $scope.juegos.length; i++) {
                            if ($scope.juegos[i].numeroId == juego.numeroId) {
                                $scope.juegos[i] = juego;
                            }
                        }
                    })
                    .error(function(data, status, headers, config) {
                        //TODO Do someting when errors
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
                    .success(function(data, status, headers, config) {
                        $scope.status = 'Inserted juego! Refreshing customer list.';
                        var location = headers('Location');
                        var parts = location.split('/');
                        var newId = parts[6]
                        newJuego.numeroId = newId;
                        $scope.juegos.push(newJuego);

                    })
                    .error(function(error) {
                        $scope.status = 'Unable to insert juego: ' + error.message;
                        console.log(error.message);
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

        $scope.addParticipante = function(persona) {
            var newParticipante = {
                fechaInscripcion: new Date()
                , personas: persona
                , juego: $scope.currentJuego
                , participantesPK: {
                    numeroJuego: $scope.currentJuego.numeroId
                    , numeroPersona: persona.numeroId
                }
            };

            // This is not correct but it is the only way i found to solve circular references
            var newParticipanteForList = {
                fechaInscripcion: new Date()
                , personas: persona
                , participantesPK: {
                    numeroJuego: $scope.currentJuego.numeroId
                    , numeroPersona: persona.numeroId
                }
            };

            //persist newParticipante
            appFactory.createParticipante(newParticipante)
                    .success(function(data, status, headers, config) {
                        $scope.status = 'Inserted participante!';

                        // add participante to the juego
                        $scope.currentJuego.participantesCollection.push(newParticipanteForList);

                        //remove from personas beacuse it is a participante
                        for (var i = 0; i < $scope.personas.length; i++) {
                            if (persona.numeroId == $scope.personas[i].numeroId) {
                                $scope.personas.splice(i, 1);
                                break;
                            }
                        }

                        //persist juego
                        $scope.updateJuego($scope.currentJuego);

                    })
                    .error(function(error) {
                        $scope.status = 'Unable to insert participante: ' + error.message;
                        console.log(error.message);
                    });
        };

        $scope.deleteParticipante = function(participante) {
            appFactory.deleteParticipante('participantePK;numeroJuego=' + participante.participantesPK.numeroJuego + ';numeroPersona=' + participante.participantesPK.numeroPersona)
                    .success(function(data, status, headers, config) {
                        $scope.status = 'Delete participante!';

                        //remove from participantes
                        for (var i = 0; i < $scope.currentJuego.participantesCollection.length; i++) {
                            if (participante.personas.numeroId == $scope.currentJuego.participantesCollection[i].personas.numeroId) {
                                $scope.currentJuego.participantesCollection.splice(i, 1);
                                break;
                            }
                        }

                        //add to persons
                        $scope.personas.push(participante.personas);

                        //persist juego
                        $scope.updateJuego($scope.currentJuego);

                    })
                    .error(function(error) {
                        $scope.status = 'Unable to insert participante: ' + error.message;
                        console.log(error.message);
                    });
        };
    }]);
