<%-- 
    Document   : index
    Created on : 1/11/2013, 10:40:49 AM
    Author     : David Andrés Manzano Herrerra <damanzano>
    Version    : 1.0.0 - RC1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" ng-app="invisibleFriendApp"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>InvisibleFriendApp</title>
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
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user != null) {
                pageContext.setAttribute("user", user);
            }
        %>
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
                    <!-- Login button link -->
                    <%
                        if (user != null) {
                    %>
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a href="#" class="btn btn-default navbar-btn dropdown-toggle" type="button" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-user"></span> Hola ${fn:escapeXml(user.nickname)}!
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="<%= userService.createLogoutURL(request.getRequestURI())%>">Log out</a></li>
                                <li><a href="#">Option 2</a></li>
                                <li><a href="#">Option 3</a></li>
                            </ul>
                        </li>
                    </ul>
                    <%
                    } else {
                    %>
                    <div class="nav navbar navbar-right">
                        <a class="btn btn-primary navbar-btn" href="<%=userService.createLoginURL(request.getRequestURI())%>">
                            <span class="glyphicon glyphicon-log-in"></span> Login
                        </a>
                        <a class="btn btn-info navbar-btn" href="<%=userService.createLoginURL(request.getRequestURI())%>">
                            <span class="glyphicon glyphicon-log-in"></span> Regístrate
                        </a>
                    </div>
                    <%
                        }
                    %>
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
        <script src="js/login-controller.js"></script>
    </body>
</html>
