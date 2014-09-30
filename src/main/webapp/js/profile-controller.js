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
            if(newValue!=null){
                $scope.currentPlayer = newValue.currentPlayer;
            }
        });

        $scope.validatePlayer = function (player) {
            player.gender = $scope.selectedGender.id;
            //first upload the new image if it change
            if ($scope.imagePreview != null) {
                appFactory.uploadPlayerPhoto($scope.imagePreview)
                        .success(function (data, status, headers, config) {
                            console.log(data.url);
                            player.photo = data.url;
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
                        //if the image was uploaded create the new Player using the image's url
                        var newPlayer = {
                            name: $scope.newPlayer.name
                            , lastname: $scope.newPlayer.lastname
                            , googleUser: $scope.currentPlayer.googleUser
                            , ubicacion: $scope.newPlayer.location
                            , gender: $scope.newPlayer.gender.id
                            , photo: data.url
                        };

                        //call the create person service
                        appFactory.createPlayer(newPlayer)
                                .success(function (data, status, headers, config) {
                                    console.log(data);
                                })
                                .error(function (data, status, headers, config) {
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