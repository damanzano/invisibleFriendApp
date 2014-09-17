/* 
 * Universidad Icesi - SYRI, Desarrollo de sistemas
 */

/**
 * Created on : 14-nov-2013, 16:22:00
 * Personas entity controller
 * @author David Andrés Manzano Herrera <damanzano>
 */
app.controller('personasController', ['$scope', '$routeParams', 'appFactory', function($scope, $routeParams, appFactory) {
        $scope.status;
        $scope.personas = [];
        $scope.juegos = [];
        $scope.currentPersona;
        $scope.imagePreview;
        $scope.genders = [{id: 'M', desc: 'Masculino'}, {id: 'F', desc: 'Femenino'}];
        $scope.selectedGender = $scope.genders[0];

        init();
        function init() {
            var personaId = ($routeParams.personaId) ? parseInt($routeParams.personaId) : 0;
            getPersonas(personaId);

        }

        function getPersonas(personaId) {
            appFactory.getPersonas()
                    .success(function(personasCollection) {
                        if (personasCollection != null) {
                            var json = personasCollection.personas;
                            if (json instanceof Array) {
                                for (var i = 0; i < personasCollection.personas.length; i++) {
                                    $scope.personas.push(personasCollection.personas[i]);
                                }
                            } else {
                                $scope.personas.push(personasCollection.personas);
                            }
                        }

                        if (personaId > 0) {
                            getCurrentPersona(personaId);
                        }
                    })
                    .error(function(error) {
                        $scope.status = 'Unable to load personas data: ' + error.message;
                    });
        }

        function getCurrentPersona(personaId) {
            for (var i = 0; i < $scope.personas.length; i++) {
                if ($scope.personas[i].numeroId == personaId) {
                    $scope.currentPersona = $scope.personas[i];
                    if($scope.currentPersona.sexo=='M'){
                        $scope.selectedGender = $scope.genders[0];
                    }else{
                        $scope.selectedGender = $scope.genders[1];
                    }
                    break;
                }
            }

            //if persona is not in the list try to get it from database
            if ($scope.currentPersona == null) {
                appFactory.getPersona(personaId)
                        .success(function(personaObject) {
                            $scope.currentPersona = personaObject;
                        })
                        .error(function(error) {
                            $scope.status = 'Unable to load juego data: ' + error.message;
                        });
            }
        }

        $scope.validatePersona = function(persona) {
            persona.sexo = $scope.selectedGender.id;
            //first upload the new image if it change
            if ($scope.imagePreview != null) {
                appFactory.uploadPersonaFoto($scope.imagePreview)
                        .success(function(data, status, headers, confi) {
                            //if the image was uploaded create the new Persona using the image's filename
                            var photoDirParts = data.url.split('/');
                            var photoName = photoDirParts[3];

                            console.log(photoDirParts[3]);

                            persona.foto = photoName;
                            $scope.updatePersona(persona);

                        })
                        .error(function(data, status, headers, confi) {
                            console.log("There was a problem uploading your img, plese try again later");
                        });
            } else {
                $scope.updatePersona(persona);
            }

        };

        $scope.updatePersona = function(persona) {
            //call the update person service
            appFactory.updatePersona(persona)
                    .success(function(data, status, headers, config) {
                        $scope.status = 'Updated juego! Refreshing the juegos list.';

                        console.log(persona);
                        //refresh the juegos list
                        for (var i = 0; i < $scope.juegos.length; i++) {
                            if ($scope.personas[i].numeroId == persona.numeroId) {
                                $scope.personas[i] = persona;
                            }
                        }
                    })
                    .error(function(data, status, headers, config) {
                        //TODO Do someting when errors
                    });

        };

        $scope.createPersona = function() {
            //first call the opload image services
            appFactory.uploadPersonaFoto($scope.imagePreview)
                    .success(function(data, status, headers, confi) {
                        //if the image was uploaded create the new Persona using the image's filename
                        var photoDirParts = data.url.split('/');
                        var photoName = photoDirParts[3];

                        console.log(photoDirParts[3]);

                        var newPersona = {
                            nombre: $scope.newPersona.nombre
                            , apellidos: $scope.newPersona.apellidos
                            , correoElectronico: $scope.newPersona.correoElectronico
                            , ubicacion: $scope.newPersona.ubicacion
                            , sexo: $scope.newPersona.sexo.id
                            , foto: photoName
                        };

                        //call the create person service
                        appFactory.createPersona(newPersona)
                                .success(function(data, status, headers, config) {
                                    console.log(data);
                                    $scope.status = 'Inserted persona! Refreshing personas list.';
                                    var location = headers('Location');
                                    var parts = location.split('/');
                                    var newId = parts[6]
                                    newPersona.numeroId = newId;
                                    $scope.personas.push(newPersona);

                                })
                                .error(function(data, status, headers, config) {
                                    $scope.status = 'Unable to insert persona: ' + status;
                                    console.log(status);
                                });

                    })
                    .error(function(data, status, headers, confi) {
                        console.log("There was a problem uploading your img, plese try again later");
                    });

        };

        $scope.deletePersona = function(personaId) {
            appFactory.deleteJuego(personaId)
                    .success(function(data, status, headers, confi) {
                        $scope.status = 'Deleted persona! Refreshing customer list.';
                        for (var i = 0; i < $scope.customers.length; i++) {
                            var currentPersona = $scope.personas[i];
                            if (currentPersona.numeroId === personaId) {
                                $scope.peronas.splice(i, 1);
                                break;
                            }
                        }
                    })
                    .error(function(data, status, headers, confi) {
                        $scope.status = 'Unable to delete juego: ' + error.message;
                    });
        };

        $scope.addParticipante = function(persona) {

        };

        $scope.deleteParticipante = function(participante) {

        };

        $scope.fileNameChaged = function(input) {
            if (input && input.files[0]) {
                $scope.imagePreview = input.files[0];

                var fileReader = new FileReader();
                fileReader.onload = function(e) {
                    jQuery('#img-preview').attr('src', e.target.result);
                }

                fileReader.readAsDataURL(input.files[0]);

            } else {
                // do some kind of visual feedback
            }

        };
    }]);
