/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Created on : 26-sep-2014, 17:19:06
 * @author David Andrés Manzano Herrera <damanzano>
 */
app.controller('profileController', ['$scope', '$routeParams', '$location', 'appFactory', function ($scope, $routeParams, $location, appFactory) {
        $scope.currentPlayer;
        $scope.genders = [{id: 'M', desc: 'Masculino'}, {id: 'F', desc: 'Femenino'}];
        $scope.selectedGender = $scope.genders[0];
        $scope.imagePreview;

//        init();
//        function init() {
//            $scope.currentPlayer = $scope.$parent.loginInfo.currentPlayer;
//            
//        }

        $scope.$watch('loginInfo', function (newValue, oldValue) {
            $scope.currentPlayer = newValue.currentPlayer;
        });

        $scope.validatePlayer = function (player) {
            player.gender = $scope.selectedGender.id;
            //first upload the new image if it change
            if ($scope.imagePreview != null) {
                appFactory.uploadPlayerPhoto($scope.imagePreview)
                        .success(function (data, status, headers, config) {
                            //if the image was uploaded create the new Player using the image's filename
                            var photoDirParts = data.url.split('/');
                            var photoName = photoDirParts[3];

                            console.log(photoDirParts[3]);

                            player.foto = photoName;
                            $scope.updatePlayer(player);

                        })
                        .error(function (data, status, headers, confi) {
                            console.log("There was a problem uploading your img, plese try again later");
                        });
            } else {
                $scope.updatePlayer(player);
            }

        };

        /** This method is used for player registration so it must modify current player information */
        $scope.createPlayer = function () {
            //first call the opload image services
            appFactory.uploadPlayerPhoto($scope.imagePreview)
                    .success(function (data, status, headers, confi) {
                        //if the image was uploaded create the new Player using the image's filename
                        var photoDirParts = data.url.split('/');
                        var photoName = photoDirParts[3];

                        console.log(photoDirParts[3]);

                        var newPlayer = {
                            name: $scope.newPlayer.name
                            , lastname: $scope.newPlayer.lastname
                            , googleUser: $scope.currentPlayer.googleUser
                            , ubicacion: $scope.newPlayer.location
                            , sexo: $scope.newPlayer.gender.id
                            , foto: photoName
                        };

                        //call the create person service
                        appFactory.createPlayer(newPlayer)
                                .success(function (data, status, headers, config) {
                                    console.log(data);
                                    $scope.status = 'Inserted persona! Refreshing personas list.';
                                    var location = headers('Location');
                                    var parts = location.split('/');
                                    var newId = parts[6]
                                    newPlayer.numeroId = newId;
                                    $scope.personas.push(newPlayer);

                                })
                                .error(function (data, status, headers, config) {
                                    $scope.status = 'Unable to insert persona: ' + status;
                                    console.log(status);
                                });

                    })
                    .error(function (data, status, headers, confi) {
                        console.log("There was a problem uploading your img, plese try again later");
                    });

        };

        $scope.updatePlayer = function (player) {
            //call the update person service
            appFactory.updatePlayer(player)
                    .success(function (data, status, headers, config) {
                        $scope.status = 'Updated juego! Refreshing the juegos list.';

                        console.log(player);

                    })
                    .error(function (data, status, headers, config) {
                        //TODO Do someting when errors
                    });

        };

        $scope.fileNameChaged = function (input) {
            if (input && input.files[0]) {
                $scope.imagePreview = input.files[0];

                var fileReader = new FileReader();
                fileReader.onload = function (e) {
                    jQuery('#img-preview').attr('src', e.target.result);
                }

                fileReader.readAsDataURL(input.files[0]);

            } else {
                // do some kind of visual feedback
            }
        };
    }]);