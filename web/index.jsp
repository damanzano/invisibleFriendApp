<%-- 
    Document   : index
    Created on : 1/11/2013, 10:40:49 AM
    Author     : 14620701
    Version    : 1.0.0 - RC1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" ng-app="invisibleFriendApp"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>invisibleFriendApp</title>
        <meta name="description" content="Application to manage invisible friends games">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Bootstrap -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!--<link href="css/bootstrap-datepicker.css" rel="stylesheet">-->
        <link href="css/main.css" rel="stylesheet">
        <!-- jQuery upload -->
        <link rel="stylesheet" href="css/jquery.fileupload.css">
        <link rel="stylesheet" href="css/jquery.fileupload-ui.css">
        <!-- CSS adjustments for browsers with JavaScript disabled -->
        <noscript><link rel="stylesheet" href="css/jquery.fileupload-noscript.css"></noscript>
        <noscript><link rel="stylesheet" href="css/jquery.fileupload-ui-noscript.css"></noscript>
    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Amigo Secreto</a>
                </div>
                <!-- navbar collapsible content -->
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="#/juegos">Juegos</a></li>
                        <li><a href="#/personas">Personas</a></li>
                    </ul>
                </div><!--/.navbar-collapse -->
            </div>
        </div>

        <!-- content -->
        <div ng-view=""></div>

        <div class="container">
            <hr>
            <footer>
                <p>© SYRI Desarrollo de Sistemas 2013</p>
            </footer>
        </div>




        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/lib/jquery/jquery-1.10.2.min.js"><\/script>')</script>

        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/lib/bootstrap/bootstrap.min.js"></script>
        <!--<script src="js/lib/bootstrap/bootstrap-datepicker.js"></script>-->

        <!-- Angular libraries -->
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.1/angular.min.js"></script>
        <script src="js/lib/angular/angular-animate.min.js"></script>
        <script src="js/lib/angular/angular-resource.min.js"></script>
        <script src="js/lib/angular/angular-route.min.js"></script>


        <!-- App libs -->
        <script src="js/app.js"></script>
        <script src="js/factories.js"></script>
        <script src="js/juegos-controller.js"></script>
        <script src="js/personas-controller.js"></script>
    </body>
</html>
