<!doctype html>
<html lang="en" class="no-js" xmlns:asset="http://www.w3.org/1999/XSL/Transform" xmlns:g="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Login"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
    <p class="navbar-brand" href="/#"></p>
    <h1>OliveTasks</h1>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

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

</nav>

<div class="container">
    <g:layoutBody/>
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>