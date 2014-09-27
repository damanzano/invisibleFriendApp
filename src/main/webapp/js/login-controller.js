/* 
 * Universidad Icesi - SYRI, Desarrollo de sistemas
 */

/**
 * Created on : 30-jul-2014, 16:29:55
 * Login controller
 * 
 * This controls the user authentication status
 * @author David Andrés Manzano Herrera <damanzano>
 */
'use strict';
app.controller('loginController', ['$scope', '$location', 'appFactory', function($scope, $location, appFactory) {
        $scope.loginInfo;
        
        init();
        function init() {
            appFactory.loginPlayer()
                    .success(function(response){
                        $scope.loginInfo = response;
//                        if($scope.loginInfo.currentPlayer!=null){
//                            $location.path('/profile');
//                        }
                    })
                    .error(function(){
                        console.log("Unable to retrive login information ");
                    });
        }
}]);