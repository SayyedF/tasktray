<!doctype html>
<html lang="en" class="no-js" xmlns:asset="http://www.w3.org/1999/xhtml" xmlns:g="http://www.w3.org/1999/XSL/Transform">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">


    <title>
        <g:layoutTitle default="OliveTasks"/>
    </title>


    <asset:link rel="icon" href="favicon.png" type="image/png"/>

    <asset:stylesheet src="all.min.css" />
    <asset:stylesheet src="fontawesome-free/css/all.min.css" />
    <asset:stylesheet src="vendor/datatables/dataTables.bootstrap4.min.css"/>
    <asset:stylesheet href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" />

    <!-- Custom styles for this template-->
    <asset:stylesheet src="sb-admin-2.min.css"/>
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />

    <g:set var="secService" bean="springSecurityService"/>

    <g:layoutHead/>
</head>

<body class="bg-gray-800">


    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul class="nav navbar-nav ml-auto">
            <g:pageProperty name="page.nav"/>
            <sec:ifLoggedIn>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                        <sec:loggedInUserInfo field='username'/>
                    </a>
                    <div class="dropdown-menu navbar-dark">
                        <g:form controller="profile">
                            <g:submitButton class="dropdown-item navbar-dark color-light" name="Submit" value="My Profile" style="color:gray" />
                        </g:form>
                        <g:form controller="logout">
                            <g:submitButton class="dropdown-item navbar-dark color-light" name="Submit" value="Logout" style="color:gray" />
                        </g:form>
                    </div>
                </li>
            </sec:ifLoggedIn>
        </ul>
    </div>


<div class="container">
    <div class="row mt-5">
        <div class="col-md-12 col-sm-12 col-lg-12 text-center">
            <asset:image class="img-fluid" src="${olivetasks.Company.last(sort: 'id')?.logo}" width="200"/>
        </div>
    </div>
    <g:layoutBody/>
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>